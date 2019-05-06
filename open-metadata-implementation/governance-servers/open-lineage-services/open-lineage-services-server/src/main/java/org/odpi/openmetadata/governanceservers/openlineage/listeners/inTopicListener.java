/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.governanceservers.openlineage.listeners;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.odpi.openmetadata.accessservices.assetlineage.model.AssetLineageEvent;
import org.odpi.openmetadata.governanceservers.openlineage.eventprocessors.GraphBuilder;
import org.odpi.openmetadata.governanceservers.openlineage.responses.ffdc.OpenLineageErrorCode;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLog;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLogRecordSeverity;
import org.odpi.openmetadata.repositoryservices.connectors.openmetadatatopic.OpenMetadataTopicListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class inTopicListener implements OpenMetadataTopicListener {

    private static final Logger log = LoggerFactory.getLogger(inTopicListener.class);
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private final OMRSAuditLog auditLog;
    private GraphBuilder graphBuilder;

    public inTopicListener(GraphBuilder gremlinBuilder, OMRSAuditLog auditLog) {

        this.graphBuilder = gremlinBuilder;
        this.auditLog = auditLog;

    }


    /**
     * @param eventAsString contains all the information needed to build asset lineage like connection details, database
     *                      name, schema name, table name, derived columns details
     */
    @Override
    public void processEvent(String eventAsString) {
        AssetLineageEvent event = null;
        try {
            event = OBJECT_MAPPER.readValue(eventAsString, AssetLineageEvent.class);
            log.info("Started processing OpenLineageEvent");
        } catch (Exception e) {
            log.error("Exception processing event from in topic", e);
            OpenLineageErrorCode auditCode = OpenLineageErrorCode.PROCESS_EVENT_EXCEPTION;

            auditLog.logException("processEvent",
                    auditCode.getErrorMessageId(),
                    OMRSAuditLogRecordSeverity.EXCEPTION,
                    auditCode.getFormattedErrorMessage(eventAsString, e.getMessage()),
                    e.getMessage(),
                    auditCode.getSystemAction(),
                    auditCode.getUserAction(),
                    e);
        }
        switch (event.getOmrsInstanceEventType()) {
            case NEW_ENTITY_EVENT:
                log.error("A NEW_ENTITY_EVENT was received but this should never be published by Asset Lineage OMAS. Ignoring.");
                break;
            case UPDATED_ENTITY_EVENT:
   //             updatedEntityEvent assetContextEvent = (updatedEntityEvent) event;
    //            graphBuilder.processEvent(updatedEntityEvent);
                break;
            case DELETED_ENTITY_EVENT: //TODO Check the difference between delete and purged events
                //             updatedEntityEvent assetContextEvent = (updatedEntityEvent) event;
                //            graphBuilder.processEvent(updatedEntityEvent);
                break;
            case NEW_RELATIONSHIP_EVENT:
                //             updatedEntityEvent assetContextEvent = (updatedEntityEvent) event;
                //            graphBuilder.processEvent(updatedEntityEvent);
                break;
            case UPDATED_RELATIONSHIP_EVENT:
                //             updatedEntityEvent assetContextEvent = (updatedEntityEvent) event;
                //            graphBuilder.processEvent(updatedEntityEvent);;
                break;
            case DELETED_RELATIONSHIP_EVENT: //TODO Check the difference between delete and purged events
                //             updatedEntityEvent assetContextEvent = (updatedEntityEvent) event;
                //            graphBuilder.processEvent(updatedEntityEvent);
                break;
        }
    }
}

