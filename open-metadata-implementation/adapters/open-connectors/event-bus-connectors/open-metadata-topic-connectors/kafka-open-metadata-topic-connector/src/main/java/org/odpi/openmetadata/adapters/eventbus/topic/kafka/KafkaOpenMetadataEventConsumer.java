/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.eventbus.topic.kafka;

import java.time.Duration;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerRebalanceListener;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.errors.WakeupException;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



/**
 * KafkaOpenMetadataEventConsumer is used to process events from kafka topic and is part of native
 * Apache Kafka event/messaging infrastructure.
 */
public class KafkaOpenMetadataEventConsumer implements Runnable
{
    private static final Logger       log      = LoggerFactory.getLogger(KafkaOpenMetadataEventConsumer.class);

    private OMRSAuditLog auditLog;

    private final long recoverySleepTimeSec; 
    private final long pollTimeout; ;
    private final long maxQueueSize;

    private				 KafkaOpenMetadataEventConsumerConfiguration config;
    private              KafkaConsumer<String, String>   consumer;
    private              String                          topicToSubscribe;
    private              String                          localServerId;

    private              KafkaOpenMetadataTopicConnector connector;

    private Map<TopicPartition, OffsetAndMetadata> currentOffsets = new HashMap<>();
    private long maxNextPollTimestampToAvoidConsumerTimeout = 0;
    private final long maxMsBetweenPolls;
    
    
    //If we get close enough to the consumer timeout timestamp, force a poll so that
    //we do not exceed the timeout.  This parameter controls how close we can get
    //before forcing a poll.
    private final long consumerTimeoutPreventionSafetyWindowMs;
    
    private Boolean running = true;

    /**
     * Constructor for the event consumer.
     *
     * @param topicName name of the topic to listen on.
     * @param localServerId identifier to enable receiver to identify that an event came from this server.
     * @param config additional properties
     * @param kafkaConsumerProperties properties for the consumer.
     * @param connector connector holding the inbound listeners.
     * @param auditLog  audit log for this component.
     */
    KafkaOpenMetadataEventConsumer(String                                      topicName,
                                   String                                      localServerId,
                                   KafkaOpenMetadataEventConsumerConfiguration config,
                                   Properties                                  kafkaConsumerProperties,
                                   KafkaOpenMetadataTopicConnector             connector,
                                   OMRSAuditLog                                auditLog)
    {
        this.auditLog = auditLog;
        this.consumer = new KafkaConsumer<>(kafkaConsumerProperties);
        this.topicToSubscribe = topicName;
        this.consumer.subscribe(Collections.singletonList(topicToSubscribe), new HandleRebalance());
        this.connector = connector;
        this.localServerId = localServerId;

        final String           actionDescription = "initialize";
        KafkaOpenMetadataTopicConnectorAuditCode auditCode;

        auditCode = KafkaOpenMetadataTopicConnectorAuditCode.SERVICE_CONSUMER_PROPERTIES;
        auditLog.logRecord(actionDescription,
                           auditCode.getLogMessageId(),
                           auditCode.getSeverity(),
                           auditCode.getFormattedLogMessage(Integer.toString(kafkaConsumerProperties.size()), topicName),
                           kafkaConsumerProperties.toString(),
                           auditCode.getSystemAction(),
                           auditCode.getUserAction());
        
        maxMsBetweenPolls = new KafkaConfigurationWrapper(kafkaConsumerProperties).getMaxPollIntervalMs();
        this.recoverySleepTimeSec = config.getLongProperty(KafkaOpenMetadataEventConsumerProperty.RECOVERY_SLEEP_TIME);
        this.maxQueueSize = config.getIntProperty(KafkaOpenMetadataEventConsumerProperty.MAX_QUEUE_SIZE);
        this.consumerTimeoutPreventionSafetyWindowMs = config.getLongProperty(KafkaOpenMetadataEventConsumerProperty.CONSUMER_TIMEOUT_PREVENTION_SAFETY_WINDOW_MS);
        this.pollTimeout = config.getLongProperty(KafkaOpenMetadataEventConsumerProperty.POLL_TIMEOUT);
    }

  

    /**
     * The server is shutting down.
     */
    public void stop()
    {
        running = false;
        if (consumer != null)
        {
            consumer.wakeup();
        }
    }


    private void updateNextMaxPollTimestamp()
    {
    	maxNextPollTimestampToAvoidConsumerTimeout = System.currentTimeMillis() + maxMsBetweenPolls - consumerTimeoutPreventionSafetyWindowMs;
    }


    /**
     * This is the method that provides the behaviour of the thread.
     */
    @Override
    public void run()
    {
        final String           actionDescription = "run";
        KafkaOpenMetadataTopicConnectorAuditCode auditCode;

        while (isRunning())
        {
            try
            {
            	//if we are close to the timeout, force a poll to avoid having the consumer
            	//be marked as dead because we have not polled often enough
            	boolean pollRequired = System.currentTimeMillis() > maxNextPollTimestampToAvoidConsumerTimeout;
            
            	
                	
            	int nUnprocessedEvents = connector.getNumberOfUnprocessedEvents();
            	if (! pollRequired && nUnprocessedEvents > maxQueueSize)
            	{
            		//The connector queue is too big.  Wait until the size goes down until
            		//polling again.  If we let the events just accumulate, we will
            		//eventually run out of memory if the consumer cannot keep up.
            		log.warn("Skipping Kafka polling since unprocessed message queue size {} is greater than {}", nUnprocessedEvents, maxQueueSize);
            		awaitNextPollingTime();
            		continue;
            	
            	}

            	updateNextMaxPollTimestamp();

                Duration pollDuration = Duration.ofMillis(pollTimeout);
                ConsumerRecords<String, String> records = consumer.poll(pollDuration);
                
                log.debug("Found records: " + records.count());
                for (ConsumerRecord<String, String> record : records)
                {
                    String json = record.value();
                    log.debug("Received message: " + json);

                    if (! localServerId.equals(record.key()))
                    {
                        try
                        {
                            connector.distributeToListeners(json);
                        }
                        catch (Exception error)
                        {
                            log.error(String.format("Error distributing inbound event: %s", error.getMessage()), error);

                            if (auditLog != null)
                            {
                                auditCode = KafkaOpenMetadataTopicConnectorAuditCode.EXCEPTION_DISTRIBUTING_EVENT;
                                auditLog.logRecord(actionDescription,
                                                   auditCode.getLogMessageId(),
                                                   auditCode.getSeverity(),
                                                   auditCode.getFormattedLogMessage(topicToSubscribe,
                                                                                    error.getClass().getName(), json,
                                                                                    error.getMessage()),
                                                   null,
                                                   auditCode.getSystemAction(),
                                                   auditCode.getUserAction());
                            }
                        }
                    }
                    else
                    {
                        log.debug("Ignoring message with key: " + record.key() + " and value " + record.value());
                    }

                    /*
                     * Acknowledge receipt of message.
                     */
                    TopicPartition partition = new TopicPartition(record.topic(), record.partition());
                    currentOffsets.put(partition, new OffsetAndMetadata(record.offset() + 1));
                }
            }
            catch (WakeupException e)
            {
                log.debug("Received wakeup call, proceeding with graceful shutdown", e);
            }
            catch (Exception error)
            {
                log.error(String.format("Unexpected error: %s", error.getMessage()), error);

                if (auditLog != null)
                {
                    auditCode = KafkaOpenMetadataTopicConnectorAuditCode.EXCEPTION_RECEIVING_EVENT;
                    auditLog.logRecord(actionDescription,
                                       auditCode.getLogMessageId(),
                                       auditCode.getSeverity(),
                                       auditCode.getFormattedLogMessage(topicToSubscribe, error.getClass().getName(),
                                                                        error.getMessage()),
                                       null,
                                       auditCode.getSystemAction(),
                                       auditCode.getUserAction());
                }
                recoverAfterError();
            }
            finally
            {
                awaitNextPollingTime();
            }
        }

        if (consumer != null)
        {
            try
            {
                consumer.commitSync(currentOffsets);
            }
            finally
            {
                consumer.close();
            }
            consumer = null;
        }
    }


	private void awaitNextPollingTime() {
		try
		{
		    Thread.sleep(1000);
		}
		catch (InterruptedException e)
		{
		    log.error(String.format("Interruption error: %s", e.getMessage()), e);
		}
	}


    private void recoverAfterError()
    {
        log.info(String.format("Waiting %s seconds to recover", recoverySleepTimeSec));

        try
        {
            Thread.sleep(recoverySleepTimeSec * 1000L);
        }
        catch (InterruptedException e1)
        {
            log.debug("Interrupted while recovering", e1);
        }
    }


    /**
     * Normal shutdown
     */
    void safeCloseConsumer()
    {
        stopRunning();

        /*
         * Wake the thread up so it shuts down quicker.
         */
        if (consumer != null)
        {
            consumer.wakeup();
        }
    }


    /**
     * Should the thread keep looping.
     *
     * @return boolean
     */
    private synchronized  boolean isRunning()
    {
        return running;
    }


    /**
     * Flip the switch to stop the thread.
     */
    private synchronized void stopRunning()
    {
        running = false;
    }


    private class HandleRebalance implements ConsumerRebalanceListener
    {
        public void onPartitionsAssigned(Collection<TopicPartition> partitions)
        {
        }

        public void onPartitionsRevoked(Collection<TopicPartition> partitions)
        {
            log.info("Lost partitions in rebalance. Committing current offsets:" + currentOffsets);
            consumer.commitSync(currentOffsets);
        }
    }
}
