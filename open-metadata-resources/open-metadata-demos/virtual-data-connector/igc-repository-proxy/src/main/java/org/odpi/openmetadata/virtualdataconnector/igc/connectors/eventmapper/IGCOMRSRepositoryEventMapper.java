/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.virtualdataconnector.igc.connectors.eventmapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;

import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.PrimitiveDefCategory;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryeventmapper.OMRSRepositoryEventMapperBase;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.TypeErrorException;
import org.odpi.openmetadata.virtualdataconnector.igc.connectors.eventmapper.jackson.IGCKafkaEvent;
import org.odpi.openmetadata.virtualdataconnector.igc.connectors.repositoryconnector.jackson.IGCObject;
import org.odpi.openmetadata.virtualdataconnector.igc.connectors.repositoryconnector.jackson.Item;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.odpi.openmetadata.virtualdataconnector.igc.connectors.repositoryconnector.IGCOMRSRepositoryConnector;

import java.util.*;


/**
 * IGCOMRSRepositoryEventMapper provides an implementation of a repository event mapper for the
 * IBM Governance Catalog (IGC).
 */

public class IGCOMRSRepositoryEventMapper extends OMRSRepositoryEventMapperBase {


    private static final Logger log = LoggerFactory.getLogger(IGCOMRSRepositoryEventMapper.class);
    private IGCObject igcObject;
    private IGCOMRSRepositoryConnector igcomrsRepositoryConnector;
    private String originatorServerName;
    private String metadataCollectionId;
    private String originatorServerType;
    private String sourceName;
    private String originatorOrganizationName;

    /**
     * Default constructor
     */
    public IGCOMRSRepositoryEventMapper() {

    }

    /**
     * Indicates that the connector is completely configured and can begin processing.
     *
     * @throws ConnectorCheckedException - there is a problem within the connector.
     */
    @Override
    public void start() throws ConnectorCheckedException {
        super.start();
        this.igcomrsRepositoryConnector = (IGCOMRSRepositoryConnector) this.repositoryConnector;
        this.originatorServerName = this.localServerName;
        this.metadataCollectionId = igcomrsRepositoryConnector.getMetadataCollectionId();
        this.originatorServerType = "IGC";
        this.sourceName = "IGCOMRSRepositoryEventMapper";
        this.originatorOrganizationName = "ING";
        runConsumer();
    }


    /**
     * Set properties for the kafka consumer.
     *
     * @return kafka consumer object
     */
    private Consumer<Long, String> createConsumer() {
        final Properties props = new Properties();
        String address = this.connectionBean.getEndpoint().getAddress().split("/")[0];
        String topicName = this.connectionBean.getEndpoint().getAddress().split("/")[1];
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, address);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "kafka_IGC_Consumer");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());//LongDeserializer.class.getName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());

        final Consumer<Long, String> consumer = new KafkaConsumer<>(props);
        consumer.subscribe(Collections.singletonList(topicName));
        return consumer;
    }

    /**
     * Start a new thread to read IGC kafka events.
     */
    public void runConsumer() {
        new Thread(new ConsumerThread()).start();
    }


    private class ConsumerThread implements Runnable {

        /**
         * Read the Kafka events originating from IGC.
         */
        @Override
        public void run() {
            log.info("Started IGC Eventmapper");
            IGCKafkaEvent igcKafkaEvent;
            final Consumer<Long, String> consumer = createConsumer();
            ConsumerRecords<Long, String> records;
            while (true) {
                try {
                    records = consumer.poll(100);
                    for (ConsumerRecord<Long, String> record : records) {
                        ObjectMapper mapper = new ObjectMapper();
                        igcKafkaEvent = mapper.readValue(record.value(), IGCKafkaEvent.class);
                        log.info("Consumer Record");
                        log.info(record.value());
                        if (igcKafkaEvent.getEventType().equals("IMAM_SHARE_EVENT"))
                            processIMAM(igcKafkaEvent);
                        else
                            processAsset(igcKafkaEvent);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Propagate the importation of databases into IGC through the OMRS.
     * @param igcKafkaEvent     A Kafka event originating from IGC
     * @throws TypeErrorException
     */
    //TODO Propagation of database, schema.
    private void processIMAM(IGCKafkaEvent igcKafkaEvent) throws TypeErrorException {
        igcObject = igcomrsRepositoryConnector.genericIGCQuery(igcKafkaEvent.getDatacollectionRID());
        List<Item> database_columns = new ArrayList<>();
        for(Item item : igcObject.getDatabaseColumns().getItems())
            database_columns.add(item);
        for(Item item : database_columns){
            igcObject = igcomrsRepositoryConnector.genericIGCQuery(item.getId());
            createEntity("TabularColumn");
        }

    }

    /**
     * Process individual IGC Kafka events and publish them through OMRS.
     *
     * @param igcKafkaEvent a Kafka event originating from IGC.
     */
    private void processAsset(IGCKafkaEvent igcKafkaEvent) {
        try {
            igcObject = igcomrsRepositoryConnector.genericIGCQuery(igcKafkaEvent.getASSETRID());
            String technicalTerm = igcKafkaEvent.getASSETRID();
            switch (igcKafkaEvent.getASSETTYPE()) {
                case "Database Column":
                    if (igcKafkaEvent.getACTION().equals("ASSIGNED_RELATIONSHIP") || igcKafkaEvent.getACTION().equals("MODIFY")) { //Relationship between a technical term and a glossary term.
                        if (igcObject.getAssignedToTerms() != null) {

                            String glossaryTermID = igcObject.getAssignedToTerms().getItems().get(0).getName();
                            String glossaryTermName = igcObject.getAssignedToTerms().getItems().get(0).getName();

                            Relationship relationship = newRelationship(
                                    "SemanticAssignment",
                                    igcKafkaEvent.getASSETRID(),
                                    "RelationalColumn",
                                    technicalTerm,
                                    glossaryTermID,
                                    "GlossaryTerm",
                                    glossaryTermName);

                            this.repositoryEventProcessor.processNewRelationshipEvent(
                                    sourceName,
                                    metadataCollectionId,
                                    originatorServerName,
                                    originatorServerType,
                                    originatorOrganizationName,
                                    relationship
                            );
                        }
                    }
                    break;
                case "Term":
                    if (igcKafkaEvent.getACTION().equals("CREATE")) { //Creation of a glossary term.
                        createEntity("GlossaryTerm");
                    }
                    break;
                case "Category":
                    if (igcKafkaEvent.getACTION().equals("CREATE")) { //Creation of a  glossary category.
                        createEntity("GlossaryCategory");
                    }
                    if (igcKafkaEvent.getACTION().equals("ASSIGNED_RELATIONSHIP")) { //Relationship between a glossary category and a glossary term.
                        String glossaryTermID = igcObject.getTerms().getItems().get(0).getId();
                        String glossaryTermName = igcObject.getTerms().getItems().get(0).getName();

                        Relationship relationship = newRelationship(
                                "TermCategorization",
                                igcKafkaEvent.getASSETRID(),
                                "GlossaryCategory",
                                technicalTerm,
                                glossaryTermID,
                                "GlossaryTerm",
                                glossaryTermName
                        );
                        this.repositoryEventProcessor.processNewRelationshipEvent(
                                sourceName,
                                metadataCollectionId,
                                originatorServerName,
                                originatorServerType,
                                originatorOrganizationName,
                                relationship
                        );
                    }
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createEntity(String typeName) throws TypeErrorException {

        String username = igcObject.getCreatedBy();
        Map<String, InstancePropertyValue> properties = new HashMap<>();

        PrimitivePropertyValue qualifiedName = new PrimitivePropertyValue();
        qualifiedName.setPrimitiveValue(igcObject.getId());
        qualifiedName.setPrimitiveDefCategory(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_STRING);
        properties.put("qualifiedName", qualifiedName);

        PrimitivePropertyValue displayname = new PrimitivePropertyValue();
        displayname.setPrimitiveValue(igcObject.getName());
        displayname.setPrimitiveDefCategory(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_STRING);
        properties.put("displayName", displayname);

        PrimitivePropertyValue summary = new PrimitivePropertyValue();
        summary.setPrimitiveValue(igcObject.getShortDescription());
        summary.setPrimitiveDefCategory(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_STRING);
        properties.put("summary", summary);

        PrimitivePropertyValue description = new PrimitivePropertyValue();
        description.setPrimitiveValue(igcObject.getLongDescription());
        description.setPrimitiveDefCategory(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_STRING);
        properties.put("description", description);

        InstanceProperties instanceProperties = new InstanceProperties();
        instanceProperties.setInstanceProperties(properties);

        EntityDetail entityDetail = this.repositoryHelper.getNewEntity(
                sourceName,
                metadataCollectionId,
                InstanceProvenanceType.LOCAL_COHORT,
                username,
                typeName,
                instanceProperties,
                null
        );
        this.repositoryEventProcessor.processNewEntityEvent(
                sourceName,
                metadataCollectionId,
                originatorServerName,
                originatorServerType,
                originatorOrganizationName,
                entityDetail

        );
    }

    /**
     * Set relationship between two entities.
     *
     * @param relationshipType name of the type
     * @param entityId1        String unique identifier
     * @param entityType1      name of the type
     * @param entityId2        String unique identifier
     * @param entityType2      name of the type
     * @return Relationship is a POJO that manages the properties of an open metadata relationship.  This includes information
     * about the relationship type, the two entities it connects and the properties it holds.
     * @throws TypeErrorException The type name is not recognized as a relationship type.
     */
    private Relationship newRelationship(String relationshipType, String entityId1, String entityType1, String entityName1, String entityId2, String entityType2, String entityName2) throws TypeErrorException {

        Relationship relationship = this.repositoryHelper.getNewRelationship(
                sourceName,
                metadataCollectionId,
                InstanceProvenanceType.LOCAL_COHORT,
                null,
                relationshipType,
                null
        );

        EntityProxy entityProxyOne = newEntityProxy(entityType1);
        EntityProxy entityProxyTwo = newEntityProxy(entityType2);

        entityProxyOne.setGUID(entityId1);
        entityProxyTwo.setGUID(entityId2);

        relationship.setEntityOnePropertyName(entityName1);
        relationship.setEntityOneProxy(entityProxyOne);

        relationship.setEntityTwoPropertyName(entityName2);
        relationship.setEntityTwoProxy(entityProxyTwo);
        return relationship;
    }


    /**
     * Return a filled out entity.
     *
     * @param typeName name of the type
     * @return an entity that is filled out
     * @throws TypeErrorException the type name is not recognized as an entity type
     */
    private EntityProxy newEntityProxy(String typeName) throws TypeErrorException {
        EntityProxy entityProxy = this.repositoryHelper.getNewEntityProxy(
                sourceName,
                metadataCollectionId,
                InstanceProvenanceType.LOCAL_COHORT,
                null,
                typeName,
                null,
                null
        );
        entityProxy.setStatus(InstanceStatus.ACTIVE);
        return entityProxy;
    }

}

