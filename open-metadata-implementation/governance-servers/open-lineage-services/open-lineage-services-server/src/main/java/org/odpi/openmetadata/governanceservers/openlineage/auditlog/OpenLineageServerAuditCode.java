/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.governanceservers.openlineage.auditlog;


import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLogRecordSeverity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.MessageFormat;
import java.util.Arrays;


public enum OpenLineageServerAuditCode {

    SERVER_INITIALIZING("OPEN-LINEAGE-0001",
            OMRSAuditLogRecordSeverity.INFO,
            "The Open Lineage Services  is initializing a new server instance.",
            "The local server has started up a new instance of the Open Lineage Services.",
            "No action is required.  This is part of the normal operation of the server."),

    SERVER_INITIALIZED("OPEN-LINEAGE-0002",
            OMRSAuditLogRecordSeverity.INFO,
            "The Open Lineage Services has initialized a new instance for server {0}.",
            "The Open Lineage Services has completed initialization.",
            "No action is required. This is part of the normal operation of the server."),


    SERVER_REGISTERED_WITH_IN_TOPIC("OPEN-LINEAGE-0003",
            OMRSAuditLogRecordSeverity.INFO,
            "The Open Lineage Services server {0} is registering a listener for its in topic ",
            "The Open Lineage Services is registering to receive incoming events to store lineage data",
            "No action is required.  This is part of the normal operation of the server."),

    SERVER_SHUTTING_DOWN("OPEN-LINEAGE-0004",
            OMRSAuditLogRecordSeverity.INFO,
            "The Open Lineage Services server {0} is shutting down",
            "The local administrator has requested shut down of this Open Lineage server.",
            "No action is required.  This is part of the normal operation of the service."),

    SERVER_SHUTDOWN("OPEN-LINEAGE-0005",
            OMRSAuditLogRecordSeverity.INFO,
            "The Open Lineage Services server {0} has completed shutdown",
            "The local administrator has requested shut down of this Open Lineage server and the operation has completed.",
            "No action is required.  This is part of the normal operation of the service."),

    NO_CONFIG_DOC("OPEN-LINEAGE-0006",
            OMRSAuditLogRecordSeverity.ERROR,
            "The Open Lineage Services server {0} is not configured with a configuration document.",
            "The server is not able to retrieve its configuration.  It fails to start.",
            "Add the configuration document for this open lineage service."),

    ERROR_OBTAINING_IN_TOPIC_CONNECTOR("OPEN-LINEAGE-0007",
            OMRSAuditLogRecordSeverity.EXCEPTION,
            "The Open Lineage Services server {0} was unable to obtain an in topic connector with the provided configuration {1}.",
            "The in topic connector could not be obtained.",
            "Review the topic name set by the Open Lineage Services configuration."),

    ERROR_STARTING_IN_TOPIC_CONNECTOR("OPEN-LINEAGE-0008",
            OMRSAuditLogRecordSeverity.EXCEPTION,
            "The Open Lineage Services server {0} is unable to start an in topic listener with the provided configuration {1}.",
            "The topic connector could not be started.",
            "Review the status of the eventbus server and review the topic name set by the Open Lineage Services configuration."),

    ERROR_OBTAINING_BUFFER_GRAPH_CONNNECTOR("OPEN-LINEAGE-SERVICES-0009",
            OMRSAuditLogRecordSeverity.ERROR,
            "The Open Lineage Services server {0} is not able to obtain a Buffergraph database connector with the values provided in configuration {1}.",
            "The Buffergraph database connector could not be obtained.",
            "Please verify the Buffergraph connection object within the Open Lineage Services configuration."),

    ERROR_OBTAINING_MAIN_GRAPH_CONNNECTOR("OPEN-LINEAGE-SERVICES-0010",
            OMRSAuditLogRecordSeverity.ERROR,
            "The Open Lineage Services server {0} is not able to obtain a Maingraph database connector with the values provided in configuration {1}.",
            "The Maingraph database connector could not be obtained.",
            "Please verify the Maingraph connection object within the Open Lineage Services configuration."),

    ERROR_INITIALIZING_BUFFER_GRAPH_CONNNECTOR_DB("OPEN-LINEAGE-SERVICES-0011",
            OMRSAuditLogRecordSeverity.ERROR,
            "The Open Lineage Services server {0} is not able to initialize the Buffergraph database connector with the values provided in configuration {1}.",
            "The Buffergraph database connector could not be initialized.",
            "Please check that the Buffergraph database exists and is not in use by another process, and verify the Open Lineage Services configuration."),

    ERROR_INITIALIZING_MAIN_GRAPH_CONNECTOR_DB("OPEN-LINEAGE-SERVICES-0012",
            OMRSAuditLogRecordSeverity.ERROR,
            "The Open Lineage Services server {0} is not able to initialize the Maingraph database connector with the values provided in configuration {1}.",
            "The Maingraph database connector could not be initialized.",
            "Please check that the Maingraph database exists and is not in use by another process, and verify the Open Lineage Services configuration."),

    ERROR_STARTING_BUFFER_GRAPH_CONNECTOR("OPEN-LINEAGE-SERVICES-0013",
            OMRSAuditLogRecordSeverity.ERROR,
            "The Open Lineage Services server {0} is not able to register the Buffergraph database connector as \"active\" with the values provided in configuration {1}.",
            "The Buffergraph database connector could not be started.",
            "Please check that the Buffergraph database exists and is not in use by another process, and verify the Open Lineage Services configuration."),

    ERROR_STARTING_MAIN_GRAPH_CONNECTOR("OPEN-LINEAGE-SERVICES-0014",
            OMRSAuditLogRecordSeverity.ERROR,
            "The Open Lineage Services server {0} is not able to register the Maingraph database connector as \"active\" with the values provided in configuration {1}.",
            "The Maingraph database connector could not be started.",
            "Please check that the Maingraph database exists and is not in use by another process, and verify the Open Lineage Services configuration."),


    ERROR_INITIALIZING_OLS("OPEN-LINEAGE-SERVICES-0015",
            OMRSAuditLogRecordSeverity.ERROR,
            "The Open Lineage Services server {0} encountered an unexpected error and could not start. The server configuration was {1}.",
            "An unexpected error occurred while initializing the Open Lineage Services.",
            "Please contact an Egeria maintainer about your issue."),


    PROCESS_EVENT_EXCEPTION("OPEN-LINEAGE-SERVICES-0016",
            OMRSAuditLogRecordSeverity.EXCEPTION,
            "Event {0} could not be consumed. Error: {1}",
            "The system is unable to process the request.",
            "Verify the topic configuration.");

    private static final Logger log = LoggerFactory.getLogger(OpenLineageServerAuditCode.class);
    private String logMessageId;
    private OMRSAuditLogRecordSeverity severity;
    private String logMessage;
    private String systemAction;
    private String userAction;

    OpenLineageServerAuditCode(String logMessageId, OMRSAuditLogRecordSeverity severity, String logMessage, String systemAction, String userAction) {
        this.logMessageId = logMessageId;
        this.severity = severity;
        this.logMessage = logMessage;
        this.systemAction = systemAction;
        this.userAction = userAction;
    }

    /**
     * Returns the unique identifier for the error message.
     *
     * @return logMessageId
     */
    public String getLogMessageId() {
        return logMessageId;
    }


    /**
     * Return the severity of the audit log record.
     *
     * @return OMRSAuditLogRecordSeverity enum
     */
    public OMRSAuditLogRecordSeverity getSeverity() {
        return severity;
    }

    /**
     * Returns the log message with the placeholders filled out with the supplied parameters.
     *
     * @param params - strings that plug into the placeholders in the logMessage
     * @return logMessage (formatted with supplied parameters)
     */
    public String getFormattedLogMessage(String... params) {
        if (log.isDebugEnabled()) {
            log.debug(String.format("<== OpenLineageServerAuditCode.getMessage(%s)", Arrays.toString(params)));
        }

        MessageFormat mf = new MessageFormat(logMessage);
        String result = mf.format(params);

        if (log.isDebugEnabled()) {
            log.debug(String.format("==> OpenLineageServerAuditCode.getMessage(%s): %s", Arrays.toString(params), result));
        }

        return result;
    }


    /**
     * Returns a description of the action taken by the system when the condition that caused this exception was
     * detected.
     *
     * @return systemAction String
     */
    public String getSystemAction() {
        return systemAction;
    }


    /**
     * Returns instructions of how to resolve the issue reported in this exception.
     *
     * @return userAction String
     */
    public String getUserAction() {
        return userAction;
    }

}