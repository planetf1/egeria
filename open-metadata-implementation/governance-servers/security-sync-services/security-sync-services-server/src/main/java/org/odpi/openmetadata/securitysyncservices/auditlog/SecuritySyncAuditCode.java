/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.securitysyncservices.auditlog;

import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLogRecordSeverity;

import java.text.MessageFormat;

public enum SecuritySyncAuditCode {

    SERVICE_INITIALIZING("SECURITY-SYNC-0001",
            OMRSAuditLogRecordSeverity.INFO,
            "The Security Sync is initializing a new server instance",
            "The local server has started up a new instance of the Security Sync.",
            "No action is required.  This is part of the normal operation of the service."),
    SERVICE_INITIALIZED("SECURITY-SYNC-0002",
            OMRSAuditLogRecordSeverity.INFO,
            "The Security Sync has initialized a new instance for server {0}",
            "The Security Sync has completed initialization of a new instance.",
            "No action is required.  This is part of the normal operation of the service."),
    SERVICE_SHUTDOWN("SECURITY-SYNC-0003",
            OMRSAuditLogRecordSeverity.INFO,
            "The Security Sync is shutting down its instance for server {0}",
            "The local server has requested shut down of a Security Sync instance.",
            "No action is required.  This is part of the normal operation of the service."),
    ERROR_INITIALIZING_SECURITY_SYNC_TOPIC_CONNECTION("SECURITY-SYNC-0004",
            OMRSAuditLogRecordSeverity.EXCEPTION,
            "The Security Sync is unable to initialize the connection to topic {0} in the instance for server {1} ",
            "The connection to topic could not be initialized.",
            "Review the exception and resolve the configuration."),
    CLASSIFIED_GOVERNED_ASSET_INITIAL_LOAD("SECURITY-SYNC--0005",
            OMRSAuditLogRecordSeverity.INFO,
            "The Security Sync exchange the classified governed asset from Open Metadata Repository with configured Security Sync Server",
            "The Security Sync instance synchronize the Ranger Server with the repository for initial loading of existing governed assets",
            "No action is required.  This is part of the normal flow of the initial load."),
    CLASSIFIED_GOVERNED_ASSET_EVENT_RECEIVED("SECURITY-SYNC--0006",
            OMRSAuditLogRecordSeverity.INFO,
            "The Security Sync received a new event for classified governed asset",
            "The Security Sync instance should synchronize the Ranger Server instance with the repository.",
            "No action is required.  This is part of the normal flow."),
    RE_CLASSIFIED_GOVERNED_ASSET_EVENT_RECEIVED("SECURITY-SYNC--0007",
            OMRSAuditLogRecordSeverity.INFO,
            "The Security Sync received a new event for re-classified governed asset",
            "The Security Sync instance should synchronize the Ranger Server instance with the repository.",
            "No action is required.  This is part of the normal flow."),
    DE_CLASSIFIED_GOVERNED_ASSET_EVENT_RECEIVED("SECURITY-SYNC--0008",
            OMRSAuditLogRecordSeverity.INFO,
            "The Security Sync received an event for de-classified governed asset",
            "The Security Sync instance should synchronize the Ranger Server instance with the repository.",
            "No action is required.  This is part of the normal flow."),
    DELETED_GOVERNED_ASSET_EVENT_RECEIVED("SECURITY-SYNC--0009",
            OMRSAuditLogRecordSeverity.INFO,
            "The Security Sync received an event for deleted governed asset",
            "The Security Sync instance should synchronize the Ranger Server instance with the repository.",
            "No action is required.  This is part of the normal flow.");
    private String logMessageId;
    private OMRSAuditLogRecordSeverity severity;
    private String logMessage;
    private String systemAction;
    private String userAction;


    /**
     * The constructor for OMRSAuditCode expects to be passed one of the enumeration rows defined in
     * OMRSAuditCode above.   For example:
     * <p>
     * OMRSAuditCode   auditCode = OMRSAuditCode.SERVER_NOT_AVAILABLE;
     * <p>
     * This will expand out to the 4 parameters shown below.
     *
     * @param messageId    - unique Id for the message
     * @param message      - text for the message
     * @param severity     - the severity of the message
     * @param systemAction - description of the action taken by the system when the condition happened
     * @param userAction   - instructions for resolving the situation, if any
     */
    SecuritySyncAuditCode(String messageId, OMRSAuditLogRecordSeverity severity,
                          String message, String systemAction, String userAction) {
        this.logMessageId = messageId;
        this.severity = severity;
        this.logMessage = message;
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
     * Returns the log message with the placeholders filled out with the supplied parameters.
     *
     * @param params - strings that plug into the placeholders in the logMessage
     * @return logMessage (formatted with supplied parameters)
     */
    public String getFormattedLogMessage(String... params) {
        MessageFormat mf = new MessageFormat(logMessage);
        return mf.format(params);
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

    /**
     * Return the severity of the audit log record.
     *
     * @return OMRSAuditLogRecordSeverity enum
     */
    public OMRSAuditLogRecordSeverity getSeverity() {
        return severity;
    }
}
