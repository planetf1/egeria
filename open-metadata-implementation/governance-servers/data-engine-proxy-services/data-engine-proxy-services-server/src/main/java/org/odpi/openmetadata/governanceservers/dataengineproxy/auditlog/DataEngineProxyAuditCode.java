/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.governanceservers.dataengineproxy.auditlog;

import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLogRecordSeverity;

import java.text.MessageFormat;

/**
 * The DataEngineProxyAuditCode is used to define the message content for the OMRS Audit Log.
 *
 * The 5 fields in the enum are:
 * <ul>
 *     <li>Log Message Id - to uniquely identify the message</li>
 *     <li>Severity - is this an event, decision, action, error or exception</li>
 *     <li>Log Message Text - includes placeholder to allow additional values to be captured</li>
 *     <li>Additional Information - further parameters and data relating to the audit message (optional)</li>
 *     <li>SystemAction - describes the result of the situation</li>
 *     <li>UserAction - describes how a user should correct the situation</li>
 * </ul>
 */
public enum DataEngineProxyAuditCode {

    SERVICE_INITIALIZING("DATA-ENGINE-PROXY-0001",
            OMRSAuditLogRecordSeverity.INFO,
            "The Data Engine Proxy is initializing a new server instance",
            "The local server has started up a new instance of the Data Engine Proxy.",
            "No action is required.  This is part of the normal operation of the service."),
    NO_CONFIG_DOC("DATA-ENGINE-PROXY-0002",
            OMRSAuditLogRecordSeverity.ERROR,
            "Data Engine proxy {0} is not configured with a configuration document",
            "The server is not able to retrieve its configuration.  It fails to start.",
            "Add the configuration document for this data engine proxy."),
    NO_OMAS_SERVER_URL("DATA-ENGINE-PROXY-0003",
            OMRSAuditLogRecordSeverity.ERROR,
            "Data Engine proxy {0} is not configured with the platform URL root for the Data Engine OMAS",
            "The server is not able to retrieve its configuration.  It fails to start.",
            "Add the configuration for the platform URL root to this data engine proxy's configuration document."),
    NO_OMAS_SERVER_NAME("DATA-ENGINE-PROXY-0004",
            OMRSAuditLogRecordSeverity.ERROR,
            "Data Engine proxy {0} is not configured with the name for the server running the Data Engine OMAS",
            "The server is not able to retrieve its configuration.  It fails to start.",
            "Add the configuration for the server name to this data engine proxy's configuration document."),
    SERVICE_INITIALIZED("DATA-ENGINE-PROXY-0005",
            OMRSAuditLogRecordSeverity.INFO,
            "The Data Engine Proxy has initialized a new instance for server {0}",
            "The local server has completed initialization of a new instance.",
            "No action is required.  This is part of the normal operation of the service."),
    ERROR_INITIALIZING_CONNECTION("DATA-ENGINE-PROXY-0006",
            OMRSAuditLogRecordSeverity.EXCEPTION,
            "Unable to initialize the Data Engine connection",
            "The connection could not be initialized.",
            "Review the exception and resolve the configuration. "),
    SERVICE_SHUTDOWN("DATA-ENGINE-PROXY-0007",
            OMRSAuditLogRecordSeverity.INFO,
            "The Data Engine Proxy is shutting down its instance for server {0}",
            "The local server has requested shut down of a Data Engine Proxy instance.",
            "No action is required.  This is part of the normal operation of the service."),
    ERROR_SHUTDOWN("DATA-ENGINE-PROXY-0008",
            OMRSAuditLogRecordSeverity.EXCEPTION,
            "The service is not shutdown properly.",
            "The connection could not be shutdown.",
            "Try again. ")
    ;

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
     * @param severity     - the severity of the message
     * @param message      - text for the message
     * @param systemAction - description of the action taken by the system when the condition happened
     * @param userAction   - instructions for resolving the situation, if any
     */
    DataEngineProxyAuditCode(String messageId, OMRSAuditLogRecordSeverity severity, String message,
                             String systemAction, String userAction) {
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
     * Return the severity object for the log
     * @return severity
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

}
