/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetlineage.listeners;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.odpi.openmetadata.accessservices.assetlineage.auditlog.AssetLineageAuditCode;
import org.odpi.openmetadata.accessservices.assetlineage.event.AssetLineageEventType;
import org.odpi.openmetadata.accessservices.assetlineage.event.LineageEvent;
import org.odpi.openmetadata.accessservices.assetlineage.outtopic.AssetLineagePublisher;
import org.odpi.openmetadata.accessservices.assetlineage.util.Converter;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.OCFCheckedExceptionBase;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLog;
import org.odpi.openmetadata.repositoryservices.connectors.omrstopic.OMRSTopicListener;
import org.odpi.openmetadata.repositoryservices.connectors.openmetadatatopic.OpenMetadataTopicConnector;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.odpi.openmetadata.repositoryservices.events.OMRSEventOriginator;
import org.odpi.openmetadata.repositoryservices.events.OMRSInstanceEvent;
import org.odpi.openmetadata.repositoryservices.events.OMRSInstanceEventType;
import org.odpi.openmetadata.repositoryservices.events.OMRSRegistryEvent;
import org.odpi.openmetadata.repositoryservices.events.OMRSTypeDefEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.odpi.openmetadata.accessservices.assetlineage.util.Constants.PROCESS;
import static org.odpi.openmetadata.accessservices.assetlineage.util.Constants.VALUE_FOR_ACTIVE;
import static org.odpi.openmetadata.accessservices.assetlineage.util.Constants.immutableValidLineageEntityEvents;
import static org.odpi.openmetadata.accessservices.assetlineage.util.Constants.immutableValidLineageRelationshipTypes;

/**
 * AssetLineageOMRSTopicListener received details of each OMRS event from the cohorts that the local server
 * is connected to.  It passes Lineage Entity events to the publisher.
 */
public class AssetLineageOMRSTopicListener implements OMRSTopicListener {

    private static final Logger log = LoggerFactory.getLogger(AssetLineageOMRSTopicListener.class);
    private static final String PROCESSING_RELATIONSHIP_DEBUG_MESSAGE = "Asset Lineage OMAS is processing an {} event which contains the following relationship {}: ";

    private AssetLineagePublisher publisher;
    private OMRSAuditLog auditLog;
    private Converter converter = new Converter();

    /**
     * The constructor is given the connection to the out topic for Asset Lineage OMAS
     * along with classes for testing and manipulating instances.
     *
     * @param serverName        name of this server instance
     * @param serverUserName    name of the user of the server instance
     * @param repositoryHelper  helper object for building and querying TypeDefs and metadata instances
     * @param outTopicConnector The connector used for the Asset Lineage OMAS Out Topic
     */
    public AssetLineageOMRSTopicListener(String serverName, String serverUserName, OMRSRepositoryHelper repositoryHelper, OpenMetadataTopicConnector outTopicConnector, OMRSAuditLog auditLog)
            throws OCFCheckedExceptionBase {
        this.publisher = new AssetLineagePublisher(serverName, serverUserName, repositoryHelper, outTopicConnector);
        this.auditLog = auditLog;
    }

    /**
     * Method to pass a Registry event received on topic.
     *
     * @param event inbound event
     */
    public void processRegistryEvent(OMRSRegistryEvent event) {
        log.debug("Ignoring registry event: " + event.toString());
    }

    /**
     * Method to pass a Registry event received on topic.
     *
     * @param event inbound event
     */
    public void processTypeDefEvent(OMRSTypeDefEvent event) {
        log.debug("Ignoring type event: " + event.toString());
    }

    /**
     * Unpack and deliver an instance event to the InstanceEventProcessor
     *
     * @param instanceEvent event to unpack
     */
    public void processInstanceEvent(OMRSInstanceEvent instanceEvent) {
        log.debug("Processing instance event" + instanceEvent);

        if (instanceEvent == null) {
            log.debug("Null instance event - Asset Lineage OMAS is ignoring the event");
            return;
        }

        OMRSInstanceEventType instanceEventType = instanceEvent.getInstanceEventType();
        OMRSEventOriginator instanceEventOriginator = instanceEvent.getEventOriginator();

        if (instanceEventOriginator == null)
            return;

        EntityDetail entityDetail = instanceEvent.getEntity();

        try {
            switch (instanceEventType) {
                case NEW_ENTITY_EVENT:
                    processNewEntity(entityDetail);
                    break;
                case UPDATED_ENTITY_EVENT:
                    if (entityDetail.getType().getTypeDefName().equals(PROCESS) && entityDetail.getStatus().getName().equals(VALUE_FOR_ACTIVE))
                        processNewEntity(entityDetail);
                    else
                        processUpdatedEntity(entityDetail);
                    break;
                case DELETED_ENTITY_EVENT:
                    processDeletedEntity(entityDetail);
                    break;
//                case CLASSIFIED_ENTITY_EVENT:
//                    processClassifiedEntityEvent(entityDetail);
//                    break;
//                case RECLASSIFIED_ENTITY_EVENT:
//                    processReclassifiedEntityEvent(entityDetail);
//                    break;
//                case DECLASSIFIED_ENTITY_EVENT:
//                    processDeclassifiedEntityEvent(entityDetail);
//                    break;
//            case NEW_RELATIONSHIP_EVENT:
//                processNewRelationship(entityDetail);
//                break;
                case UPDATED_RELATIONSHIP_EVENT:
                    processUpdatedRelationshipEvent(instanceEvent.getRelationship());
                    break;
                case DELETED_RELATIONSHIP_EVENT:
                    processDeletedRelationshipEvent(instanceEvent.getRelationship());
                    break;
            }
        } catch (OCFCheckedExceptionBase e) {
            log.error("An exception occurred while processing an OMRSTopic event \n \n" + e.toString(), e);
            logExceptionToAudit(instanceEvent, e);
        } catch (Throwable e) {
            log.error("An exception occurred while processing an OMRSTopic event", e);
            logExceptionToAudit(instanceEvent, e);
        }
    }

    private void processNewEntity(EntityDetail entityDetail) throws OCFCheckedExceptionBase, JsonProcessingException {
        if (!immutableValidLineageEntityEvents.contains(entityDetail.getType().getTypeDefName()))
            return;
        log.debug("Asset Lineage OMAS is processing a NewEntity event which contains the following entity {}: ", entityDetail.getGUID());
        if (entityDetail.getType().getTypeDefName().equals(PROCESS))
            publisher.publishProcessContext(entityDetail);
        else
            publisher.publishAssetContext(entityDetail);
    }

    private void processUpdatedEntity(EntityDetail entityDetail) throws ConnectorCheckedException, JsonProcessingException {
        log.debug("Asset Lineage OMAS is processing an UpdatedEntity event which contains the following entity {}: ", entityDetail.getGUID());
        LineageEvent event = new LineageEvent();
        event.setLineageEntity(converter.createLineageEntity(entityDetail));
        event.setAssetLineageEventType(AssetLineageEventType.UPDATE_ENTITY_EVENT);
        publisher.publishEvent(event);
    }

    private void processDeletedEntity(EntityDetail entityDetail) throws ConnectorCheckedException, JsonProcessingException {
        log.debug("Asset Lineage OMAS is processing a DeleteEntity event which contains the following entity {}: ", entityDetail.getGUID());
        LineageEvent event = new LineageEvent();
        event.setLineageEntity(converter.createLineageEntity(entityDetail));
        event.setAssetLineageEventType(AssetLineageEventType.DELETE_ENTITY_EVENT);
        publisher.publishEvent(event);
    }

    private void processClassifiedEntityEvent(EntityDetail entityDetail) throws OCFCheckedExceptionBase, JsonProcessingException {
        if (!immutableValidLineageEntityEvents.contains(entityDetail.getType().getTypeDefName()))
            return;
        log.debug("Asset Lineage OMAS is processing a Classified Entity event which contains the following entity {}: ", entityDetail.getGUID());
        publisher.publishClassificationContext(entityDetail);
    }

    private void processReclassifiedEntityEvent(EntityDetail entityDetail) {
        log.debug("Asset Lineage OMAS is processing a ReClassified Entity event which contains the following entity {}: ", entityDetail.getGUID());
    }

    private void processDeclassifiedEntityEvent(EntityDetail entityDetail) {
        log.debug("Asset Lineage OMAS is processing a DeClassified Entity event which contains the following entity {}: ", entityDetail.getGUID());
    }

    private void processNewRelationship(Relationship relationship) {
        log.debug(PROCESSING_RELATIONSHIP_DEBUG_MESSAGE, "NewRelationship", relationship.getGUID());
    }

    private void processUpdatedRelationshipEvent(Relationship relationship) throws OCFCheckedExceptionBase, JsonProcessingException {
        log.debug(PROCESSING_RELATIONSHIP_DEBUG_MESSAGE, AssetLineageEventType.UPDATE_RELATIONSHIP_EVENT, relationship.getGUID());
        if (!immutableValidLineageRelationshipTypes.contains(relationship.getType().getTypeDefName()))
            return;

        publisher.publishLineageRelationshipEvent(converter.createLineageRelationship(relationship), AssetLineageEventType.UPDATE_RELATIONSHIP_EVENT);
    }

    private void processDeletedRelationshipEvent(Relationship relationship) throws OCFCheckedExceptionBase, JsonProcessingException {
        log.debug(PROCESSING_RELATIONSHIP_DEBUG_MESSAGE, AssetLineageEventType.DELETE_RELATIONSHIP_EVENT, relationship.getGUID());
        if (!immutableValidLineageRelationshipTypes.contains(relationship.getType().getTypeDefName()))
            return;

        publisher.publishLineageRelationshipEvent(converter.createLineageRelationship(relationship), AssetLineageEventType.DELETE_RELATIONSHIP_EVENT);
    }

    private void logExceptionToAudit(OMRSInstanceEvent instanceEvent, Throwable e) {
        AssetLineageAuditCode auditCode = AssetLineageAuditCode.EVENT_PROCESSING_ERROR;
        auditLog.logException("Asset Lineage OMAS is processing an OMRSTopic event.",
                auditCode.getLogMessageId(),
                auditCode.getSeverity(),
                auditCode.getFormattedLogMessage(instanceEvent.toString()),
                null,
                auditCode.getSystemAction(),
                auditCode.getUserAction(),
                e);
    }

}
