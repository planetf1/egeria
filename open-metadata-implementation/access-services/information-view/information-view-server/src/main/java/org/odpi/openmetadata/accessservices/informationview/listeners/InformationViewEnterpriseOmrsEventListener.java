/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.accessservices.informationview.listeners;



import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditCode;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLog;
import org.odpi.openmetadata.repositoryservices.connectors.omrstopic.OMRSTopicListener;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;
import org.odpi.openmetadata.repositoryservices.events.OMRSEventOriginator;
import org.odpi.openmetadata.repositoryservices.events.OMRSInstanceEvent;
import org.odpi.openmetadata.repositoryservices.events.OMRSInstanceEventProcessor;
import org.odpi.openmetadata.repositoryservices.events.OMRSInstanceEventType;
import org.odpi.openmetadata.repositoryservices.events.OMRSRegistryEvent;
import org.odpi.openmetadata.repositoryservices.events.OMRSTypeDefEvent;
import org.odpi.openmetadata.repositoryservices.events.beans.v1.OMRSEventV1;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InformationViewEnterpriseOmrsEventListener implements OMRSTopicListener {


    private static final Logger log = LoggerFactory.getLogger(InformationViewEnterpriseOmrsEventListener.class);
    private OMRSInstanceEventProcessor instanceEventProcessor;
    private OMRSAuditLog auditLog;

    private OMRSRepositoryConnector igcConnector;

    /**
     *
     * @param instanceEventProcessor
     * @param auditLog
     */
    public InformationViewEnterpriseOmrsEventListener(OMRSInstanceEventProcessor instanceEventProcessor, OMRSAuditLog auditLog) {

        this.instanceEventProcessor = instanceEventProcessor;
        this.auditLog = auditLog;
    }


    /**
     * @param event - inbound event
     */
    public void processEvent(OMRSEventV1 event) {
        String actionDescription = "Process Incoming Event";

        /*
         * The event should not be null but worth checking.
         */
        if (event != null) {
            /*
             * Determine the category of event to process.
             */
            switch (event.getEventCategory()) {

                case INSTANCE:
                    this.processInstanceEvent(new OMRSInstanceEvent(event));
                    break;

                default:
                    log.debug("This event should not be handled by iv omas: " + event.getEventCategory());
            }
        } else {
            /*
             * A null event was passed - probably should not happen so log audit record.
             */
            OMRSAuditCode auditCode = OMRSAuditCode.NULL_OMRS_EVENT_RECEIVED;

            auditLog.logRecord(actionDescription,
                    auditCode.getLogMessageId(),
                    auditCode.getSeverity(),
                    auditCode.getFormattedLogMessage(),
                    null,
                    auditCode.getSystemAction(),
                    auditCode.getUserAction());


            log.debug("Null OMRS Event received ");
        }

    }


    @Override
    public void processRegistryEvent(OMRSRegistryEvent event) {

    }

    @Override
    public void processTypeDefEvent(OMRSTypeDefEvent event) {

    }

    /**
     * @param instanceEvent - the event coming from enterprise topic
     */
    public void processInstanceEvent(OMRSInstanceEvent instanceEvent) {

        log.debug("Processing instance event", instanceEvent);

        if (instanceEvent == null) {
            log.debug("Null instance event - ignoring event");
        } else {
            OMRSInstanceEventType instanceEventType = instanceEvent.getInstanceEventType();
            OMRSEventOriginator instanceEventOriginator = instanceEvent.getEventOriginator();

            if ((instanceEventType != null) && (instanceEventOriginator != null)) {
                switch (instanceEventType) {

                    //More events will be added
                    case NEW_RELATIONSHIP_EVENT:
                        instanceEventProcessor.processNewRelationshipEvent("EnterpriseOMRSTopic",
                                instanceEventOriginator.getMetadataCollectionId(),
                                instanceEventOriginator.getServerName(),
                                instanceEventOriginator.getServerType(),
                                instanceEventOriginator.getOrganizationName(),
                                instanceEvent.getRelationship());
                        break;

                }
            } else {
                log.debug("Ignored instance event - null type");
            }
        }


    }

}
