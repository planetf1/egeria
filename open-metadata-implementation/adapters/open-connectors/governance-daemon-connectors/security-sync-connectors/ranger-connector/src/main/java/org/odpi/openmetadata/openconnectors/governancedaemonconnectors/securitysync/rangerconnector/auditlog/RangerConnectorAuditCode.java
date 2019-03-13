/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.openconnectors.governancedaemonconnectors.securitysync.rangerconnector.auditlog;

import java.text.MessageFormat;

public enum RangerConnectorAuditCode {

    SERVICE_INITIALIZING("RANGER-CONNECTOR-0001",
            "The Ranger Connector is initializing a new server instance",
            "The local server has started up a new instance of the Ranger Connector.",
            "No action is required.  This is part of the normal operation of the service."),

    SERVICE_INITIALIZED("RANGER-CONNECTOR-0002",
            "The Ranger Connector has initialized a new instance for server {0}",
            "The Ranger Connector has completed initialization of a new instance.",
            "No action is required.  This is part of the normal operation of the service."),
    SERVICE_SHUTDOWN("RANGER-CONNECTOR-0003",
            "The Ranger Connector is shutting down its instance for server {0}",
            "The local server has requested shut down of a Ranger Connector instance.",
            "No action is required.  This is part of the normal operation of the service.");

    private String logMessageId;
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
     * @param systemAction - description of the action taken by the system when the condition happened
     * @param userAction   - instructions for resolving the situation, if any
     */
    RangerConnectorAuditCode(String messageId, String message,
                             String systemAction, String userAction) {
        this.logMessageId = messageId;
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
}
