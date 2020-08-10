/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.model;

import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageDefinition;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageSet;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLogRecordSeverity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.MessageFormat;
import java.util.Arrays;

public enum JanusConnectorErrorCode implements AuditLogMessageSet {


    PROCESS_EVENT_EXCEPTION("OPEN-LINEAGE-SERVICES-001 ",
            OMRSAuditLogRecordSeverity.EXCEPTION,
            "Event {0} could not be consumed. Error: {1}",
            "The system is unable to process the request.",
            "Verify the topic configuration."),
    PARSE_EVENT("OPEN-LINEAGE-SERVICES-002 ",
            OMRSAuditLogRecordSeverity.EXCEPTION,
            "Event could not be parsed",
            "The system is unable to process the request.",
            "Verify the topic event."),
    SERVICE_NOT_INITIALIZED("OPEN-LINEAGE-SERVICES-003 ",
            OMRSAuditLogRecordSeverity.STARTUP,
            "The Open Lineage Services have not been initialized for server {0} and can not support REST API calls",
            "The server has received a call to one of its open metadata access services but is unable to process it because the access service is not active for the requested server.",
            "If the server is supposed to have this access service activated, correct the server configuration and restart the server."),
    CANNOT_OPEN_GRAPH_DB("OPEN-LINEAGE-SERVICES-004 ",
            OMRSAuditLogRecordSeverity.STARTUP,
            "Graph cannot be opened with that configuration",
            "It is not possible to open the graph database at path {0} in the {1} method of {2} class for repository {3}",
            "The system was unable to open the graph repository graph database " +
                    "Please check that the graph database exists and is not in use by another process."),
    ENTITY_NOT_CREATED( "OPEN-LINEAGE-SERVICES-005 ",
            OMRSAuditLogRecordSeverity.EXCEPTION,
            "The attempt to create an entity with GUID {0} failed in {1} method of class {2} to open metadata repository {3}",
            "The system was unable to perform the entity create request.",
            "Correct the caller's code and retry the request."),
    ENTITY_ALREADY_EXISTS( "OPEN-LINEAGE-SERVICES-006 ",
            OMRSAuditLogRecordSeverity.EXCEPTION,
            "There is an already an entity with GUID {0} so cannot honour request to create entity in {1} method of class {2} to open metadata repository {3}",
            "The system is unable to perform the request because there is already an entity with the same GUID.",
            "Correct the caller's code and retry the request."),
    GRAPH_INITIALIZATION_ERROR( "OPEN-LINEAGE-SERVICES-007",
            OMRSAuditLogRecordSeverity.STARTUP,
            "The graph database could not be initialized for open metadata repository",
            "The system was unable to initialize.",
            "Please raise a github issue."),
    ENTITY_PROPERTIES_ERROR("OPEN-LINEAGE-SERVICES-008",
            OMRSAuditLogRecordSeverity.EXCEPTION,
            "The attempt to map a vertex and an entity failed because the properties could not be mapped for entity with GUID {0} in {1} method of class {2} to open metadata repository {3}",
            "The system was unable to perform the entity mapping request.",
            "Correct the caller's code and retry the request."),
    RELATIONSHIP_NOT_CREATED( "OPEN-LINEAGE-SERVICES-009 ",
            OMRSAuditLogRecordSeverity.EXCEPTION,
            "The attempt to create a relationship with GUID {0} failed in {1} method of class {2} to open metadata repository {3}",
            "The system was unable to perform the relationship create request.",
            "Correct the caller's code and retry the request."),
    RELATIONSHIP_NOT_FOUND( "OPEN-LINEAGE-SERVICES-010 ",
            OMRSAuditLogRecordSeverity.EXCEPTION,
            "The attempt to retrieve a relationship with GUID {0} failed in {1} method of class {2} to open metadata repository {3}",
            "The system was unable to perform the relationship retrieval.",
            "Correct the caller's code and retry the request."),
    RELATIONSHIP_ALREADY_EXISTS( "OPEN-LINEAGE-SERVICES-011 ",
            OMRSAuditLogRecordSeverity.EXCEPTION,
            "There is an already a relationship with GUID {0} so cannot honour request to create relationship in {1} method of class {2} to open metadata repository {3}",
            "The system is unable to perform the request because there is already a relationship with the same GUID.",
            "Correct the caller's code and retry the request."),
    RELATIONSHIP_TYPE_NAME_NOT_KNOWN( "OPEN-LINEAGE-SERVICES-012 ",
            OMRSAuditLogRecordSeverity.EXCEPTION,
            "The attempt to find a relationship type failed because no type was found with typeGUID {0} failed in {1} method of class {2} to open metadata repository {3}",
            "The system was unable to retrieve the relationship type.",
            "Correct the caller's code and retry the request."),
    RELATIONSHIP_PROPERTIES_ERROR("OPEN-LINEAGE-SERVICES-013 ",
            OMRSAuditLogRecordSeverity.ERROR,
            "The attempt to map an edge and a relationship failed because the properties could not be mapped for relationship with GUID {0} in {1} method of class {2} to open metadata repository {3}",
            "The system was unable to perform the relationship mapping request.",
            "Correct the caller's code and retry the request."),
    GRAPH_TRAVERSAL_EMPTY( "OPEN-LINEAGE-SERVICES-014 ",
            OMRSAuditLogRecordSeverity.INFO,
            "The attempt to start querying the graph failed.",
            "The system was unable to retrieve opening of the transactions needed to perform actions to the graph.",
            "Check your configuration properties for the graph"),
    GRAPH_CLUSTER_INIT_FAILED( "OPEN-LINEAGE-SERVICES-015 ",
            OMRSAuditLogRecordSeverity.STARTUP,
            "The attempt to start a cluster for connecting to a remote gremlin server failed",
            "The system was unable to open the connection to server.",
            "Check your configuration properties for initializing the graph connection or check your gremlin server connectivity."),
    GRAPH_DISCONNECT_ERROR( "OPEN-LINEAGE-SERVICES-016",
            OMRSAuditLogRecordSeverity.SHUTDOWN,
            "The graph database could not be closed for open metadata repository",
            "The system was unable to open the graph repository graph database ",
            "Please check that the graph database in a proper state to be closed."),
    PROCESS_MAPPING_ERROR( "OPEN-LINEAGE-SERVICES-017",
                            OMRSAuditLogRecordSeverity.EXCEPTION,
            "Something went wrong when trying to map a process",
            "The system was unable to create the context for a process ",
            "Please check that the process data is correct"),
    INDEX_NOT_CREATED( "OPEN-LINEAGE-SERVICES-018 ",
            OMRSAuditLogRecordSeverity.EXCEPTION,
            "Could not create index",
            "The system is unable to create an index for the property",
            "Correct the information and retry."),
    INDEX_NOT_ENABLED( "OPEN-LINEAGE-SERVICES-019 ",
            OMRSAuditLogRecordSeverity.EXCEPTION,
            "Could not enable index",
            "The system is unable to enable the index",
            "Correct the information and retry."),
    INDEX_ALREADY_EXISTS( "OPEN-LINEAGE-SERVICES-020",
            OMRSAuditLogRecordSeverity.EXCEPTION,
            "There is an already index with this name in the open metadata repository",
            "The system is unable to create an index with the name because it already exists.",
            "Correct the index name.");


    private static final Logger log = LoggerFactory.getLogger(JanusConnectorErrorCode.class);
    private String errorMessageId;
    private String errorMessage;
    private String systemAction;
    private String userAction;
    private OMRSAuditLogRecordSeverity severity;
    AuditLogMessageDefinition auditLogMessageDefinition;

    JanusConnectorErrorCode(String errorMessageId, OMRSAuditLogRecordSeverity severity, String errorMessage,
                            String systemAction, String userAction) {
        this.severity = severity;
        this.errorMessageId = errorMessageId;
        this.errorMessage = errorMessage;
        this.systemAction = systemAction;
        this.userAction = userAction;
        this.auditLogMessageDefinition = new AuditLogMessageDefinition(errorMessageId,
                severity,
                errorMessage,
                systemAction,
                userAction);
    }

    public String getErrorMessageId() {
        return errorMessageId;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public String getSystemAction() {
        return systemAction;
    }

    public String getUserAction() {
        return userAction;
    }


    public String getFormattedErrorMessage(String... params) {//TODO this should be moved to common code base

        log.debug(String.format("<== JanusConnectorErrorCode.getMessage(%s)", Arrays.toString(params)));


        MessageFormat mf = new MessageFormat(errorMessage);
        String result = mf.format(params);

        log.debug(String.format("==> JanusConnectorErrorCode.getMessage(%s): %s", Arrays.toString(params), result));

        return result;
    }

    @Override
    public AuditLogMessageDefinition getMessageDefinition() {
        return this.auditLogMessageDefinition;
    }

    @Override
    public AuditLogMessageDefinition getMessageDefinition(String... params) {
        this.auditLogMessageDefinition.setMessageParameters(params);
        return auditLogMessageDefinition;
    }
}
