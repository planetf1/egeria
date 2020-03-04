/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.communityprofile.auditlog;

import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLogRecordSeverity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.MessageFormat;
import java.util.Arrays;

/**
 * The CommunityProfileAuditCode is used to define the message content for the OMRS Audit Log.
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
public enum CommunityProfileAuditCode
{
    SERVICE_INITIALIZING("OMAS-COMMUNITY-PROFILE-0001",
             OMRSAuditLogRecordSeverity.STARTUP,
             "The Community Profile Open Metadata Access Service (OMAS) is initializing a new server instance",
             "The local server has started up a new instance of the Community Profile OMAS.  This service " +
                                 "supports the personal profiles used in the user interface.  It also manages organization," +
                                 "departmental and community " +
                                 "information and will reward individual contribution to open metadata as karma points.  " +
                                 "It can receive a feed of the latest user, organizational and profile information through " +
                                 "a feed from another system and will publish significant events such as whenever an individual " +
                                 "reaches a karma point plateau.",
             "No action is required if this service is required."),

    SERVICE_REGISTERED_WITH_ENTERPRISE_TOPIC("OMAS-COMMUNITY-PROFILE-0002",
             OMRSAuditLogRecordSeverity.STARTUP,
             "The Community Profile Open Metadata Access Service (OMAS) is registering a listener with the OMRS Topic for server {0}",
             "The Community Profile OMAS is registering to receive events from the connected open metadata repositories.",
             "No action is required.  This is part of the normal operation of the server."),

    SERVICE_INITIALIZED("OMAS-COMMUNITY-PROFILE-0003",
             OMRSAuditLogRecordSeverity.STARTUP,
             "The Community Profile Open Metadata Access Service (OMAS) has initialized a new instance for server {0}",
             "The access service has completed initialization of a new instance.",
             "No action is required.  This is part of the normal operation of the service."),

    SERVICE_SHUTDOWN("OMAS-COMMUNITY-PROFILE-0004",
             OMRSAuditLogRecordSeverity.SHUTDOWN,
             "The Community Profile Open Metadata Access Service (OMAS) is shutting down its instance for server {0}",
             "The local server has requested shut down of an Community Profile OMAS instance.",
             "No action is required.  This is part of the normal operation of the service."),

    SERVICE_INSTANCE_FAILURE("OMAS-COMMUNITY-PROFILE-0005",
             OMRSAuditLogRecordSeverity.EXCEPTION,
             "The Community Profile Open Metadata Access Service (OMAS) is unable to initialize a new instance; error message is {0}",
             "The Community Profile OMAS detected an error during the start up of a specific server instance.  Its services are not available for the server.",
             "Review the error message and any other reported failures to determine the cause of the problem.  Once this is resolved, restart the server."),

    OUTBOUND_EVENT("OMAS-COMMUNITY-PROFILE-0006",
             OMRSAuditLogRecordSeverity.EVENT,
             "The Community Profile Open Metadata Access Service (OMAS) has sent an event of type {0} on its out topic.  Event subject is {1}",
             "The Community Profile OMAS has detected a situation that results in an outbound event.",
             "No action is required.  This is part of the normal operation of the service."),

    INBOUND_EVENT("OMAS-COMMUNITY-PROFILE-0007",
             OMRSAuditLogRecordSeverity.EVENT,
             "The Community Profile Open Metadata Access Service (OMAS) has received an event of type {0} on its in topic.  Event subject is {1}",
             "The Community Profile OMAS has detected an incoming event.",
             "No action is required.  This is part of the normal operation of the service."),

    KARMA_PLATEAU_AWARD("OMAS-COMMUNITY-PROFILE-0008",
             OMRSAuditLogRecordSeverity.INFO,
            "{0} has reached a new karma point plateau of {1} with {2} karma points",
            "The Community Profile OMAS has detected an incoming event.",
            "No action is required.  This is part of the normal operation of the service."),

    KARMA_POINT_EXCEPTION("OMAS-COMMUNITY-PROFILE-0009",
             OMRSAuditLogRecordSeverity.EXCEPTION,
            "Unable to award karma points to {0} due to exception {1}.  The error message from the exception was {2}",
            "The system detected an exception whilst attempting to award karma points.  No karma points were awarded.",
            "Investigate and correct the source of the error.  Once fixed, karma points will be awarded."),

    OUTBOUND_EVENT_EXCEPTION("OMAS-COMMUNITY-PROFILE-0010",
            OMRSAuditLogRecordSeverity.EXCEPTION,
            "Unable to send an outbound event for instance with unique identifier of {0} and type name {1} due to exception {2}.  The error message from the exception was {3}",
            "The system detected an exception whilst attempting to send an event to the out topic.  No event is sent.",
            "Investigate and correct the source of the error.  Once fixed, events will be sent.")
    ;

    private String                     logMessageId;
    private OMRSAuditLogRecordSeverity severity;
    private String                     logMessage;
    private String                     systemAction;
    private String                     userAction;

    private static final Logger log = LoggerFactory.getLogger(CommunityProfileAuditCode.class);


    /**
     * The constructor for CommunityProfileAuditCode expects to be passed one of the enumeration rows defined in
     * CommunityProfileAuditCode above.   For example:
     *
     *     CommunityProfileAuditCode   auditCode = CommunityProfileAuditCode.SERVER_NOT_AVAILABLE;
     *
     * This will expand out to the 4 parameters shown below.
     *
     * @param messageId - unique Id for the message
     * @param severity - severity of the message
     * @param message - text for the message
     * @param systemAction - description of the action taken by the system when the condition happened
     * @param userAction - instructions for resolving the situation, if any
     */
    CommunityProfileAuditCode(String                     messageId,
                              OMRSAuditLogRecordSeverity severity,
                              String                     message,
                              String                     systemAction,
                              String                     userAction)
    {
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
    public String getLogMessageId()
    {
        return logMessageId;
    }


    /**
     * Return the severity of the audit log record.
     *
     * @return OMRSAuditLogRecordSeverity enum
     */
    public OMRSAuditLogRecordSeverity getSeverity()
    {
        return severity;
    }

    /**
     * Returns the log message with the placeholders filled out with the supplied parameters.
     *
     * @param params - strings that plug into the placeholders in the logMessage
     * @return logMessage (formatted with supplied parameters)
     */
    public String getFormattedLogMessage(String... params)
    {
        if (log.isDebugEnabled())
        {
            log.debug(String.format("<== CommunityProfile Audit Code.getMessage(%s)", Arrays.toString(params)));
        }

        MessageFormat mf = new MessageFormat(logMessage);
        String result = mf.format(params);

        if (log.isDebugEnabled())
        {
            log.debug(String.format("==> CommunityProfile Audit Code.getMessage(%s): %s", Arrays.toString(params), result));
        }

        return result;
    }



    /**
     * Returns a description of the action taken by the system when the condition that caused this exception was
     * detected.
     *
     * @return systemAction String
     */
    public String getSystemAction()
    {
        return systemAction;
    }


    /**
     * Returns instructions of how to resolve the issue reported in this exception.
     *
     * @return userAction String
     */
    public String getUserAction()
    {
        return userAction;
    }
}
