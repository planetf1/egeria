/* SPDX-License-Identifier: Apache-2.0 */

package org.odpi.openmetadata.accessservices.governanceengine.common.ffdc.errorcode;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.MessageFormat;
import java.util.Arrays;

/**
 * The GovernanceEngineErrorCode is used to define first failure data capture (FFDC) for errors that occur when working with
 * the Governance Engine Access Service.  It is used in conjunction with both Checked and Runtime (unchecked) exceptions.
 * <p>
 * The 5 fields in the enum are:
 * <ul>
 * <li>HTTP Error Code - for translating between REST and JAVA - Typically the numbers used are:</li>
 * <li><ul>
 * <li>500 - internal error</li>
 * <li>400 - invalid parameters</li>
 * <li>404 - not found</li>
 * <li>409 - data conflict errors - eg item already defined</li>
 * </ul></li>
 * <li>Error Message Id - to uniquely identify the message</li>
 * <li>Error Message Text - includes placeholder to allow additional values to be captured</li>
 * <li>SystemAction - describes the result of the error</li>
 * <li>UserAction - describes how a GovernanceEngineInterface should correct the error</li>
 * </ul>
 */

//TODO Update descriptions for error codes with more information
public enum GovernanceEngineErrorCode {
    SERVER_URL_NOT_SPECIFIED(400, "OMAS-GOVERNANCEENGINE-400-001 ",
            "The OMAS Server URL is empty or null",
            "The system is unable to connect to the Access Services Server to retrieve metadata objects.",
            "Ensure a valid OMAS Server URL is passed to the GovernanceEngineClient when it is created."),
    EMPTY_USER_ID(400, "OMAS-GOVERNANCEENGINE-400-003 ",
            "The user identifier (user id) passed on the {0} operation is empty or null",
            "The system is unable to process the request without a user id.",
            "Correct the code in the caller to provide the user id."),
    NULL_GUID(400, "OMAS-GOVERNANCEENGINE-400-004 ",
            "The unique identifier (guid) passed on the {0} parameter of the {1} operation is null",
            "The system is unable to process the request without a guid.",
            "Correct the code in the caller to provide the guid."),
    INVALID_EVENT_FORMAT(400, "OMAS-GOVERNANCEENGINE-400-006 ",
            "Invalid event format ",
            "The access service has received an event that it can not interpret",
            "Look for errors in the local server's console to understand and correct the source of the error."),
    USER_NOT_AUTHORIZED(400, "OMAS-GOVERNANCEENGINE-400-008 ",
            "User {0} is not authorized to issue the {1} request for Access Service {3} on server {4}",
            "The system is unable to process the request.",
            "Verify the access rights of the user."),
    PROPERTY_SERVER_ERROR(400, "OMAS-GOVERNANCEENGINE-400-009 ",
            "An unexpected error with message \'{0}\' was returned by the property server during {1} request for open metadata access service {2} on server {3}",
            "The system is unable to process the request.",
            "Verify the access rights of the user."),
    NULL_TEXT(400, "OMAS-GOVERNANCEENGINE-400-011 ",
            "The text field value passed on the {0} parameter of the {1} operation is null",
            "The system is unable to process the request without this text field value.",
            "Correct the code in the caller to provide the name."),
    OMRS_NOT_INITIALIZED(404, "OMAS-GOVERNANCEENGINE-404-001 ",
            "The open metadata repository services are not initialized for the {0} operation",
            "The system is unable to connect to the open metadata property server.",
            "Check that the server where the Asset Consumer OMAS is running initialized correctly.  " +
                    "Correct any errors discovered and retry the request when the open metadata services are available."),
    OMRS_NOT_AVAILABLE(404, "OMAS-GOVERNANCEENGINE-404-002 ",
            "The open metadata repository services are not available for the {0} operation",
            "The system is unable to connect to the open metadata property server.",
            "Check that the server where the Asset Consumer OMAS is running initialized correctly.  " +
                    "Correct any errors discovered and retry the request when the open metadata services are available."),
    NO_METADATA_COLLECTION(404, "OMAS-GOVERNANCEENGINE-404-004 ",
            "The requested connection {0} is not found in OMAS Server {1}",
            "The system is unable to populate the requested connection object.",
            "Check that the connection name and the open metadata server URL is correct.  Retry the request when the connection is available in the OMAS Service"),
    CLIENT_SIDE_REST_API_ERROR(503, "OMAS-GOVERNANCEENGINE-503-002 ",
            "A client-side exception was received from API call {0} to repository {1}.  The error message was {2}",
            "The server has issued a call to the open metadata access service REST API in a remote server and has received an exception from the local client libraries.",
            "Look for errors in the local server's console to understand and correct the source of the error."),
    SERVICE_NOT_INITIALIZED(503, "OMAS-GOVERNANCEENGINE-503-003 ",
            "The access service has not been initialized and can not support REST API call {0}",
            "The server has received a call to one of its Open Metadata Access Services but is unable to process it because the access service is not active.",
            "If the server is supposed to have this access service activated, correct the server configuration and restart the server."),
    ;


    private int httpErrorCode;
    private String errorMessageId;
    private String errorMessage;
    private String systemAction;
    private String userAction;

    private static final Logger log = LoggerFactory.getLogger(GovernanceEngineErrorCode.class);


    /**
     * The constructor for GovernanceEngineErrorCode expects to be passed one of the enumeration rows defined in
     * GovernanceEngineErrorCode above.   For example:
     * <p>
     * GovernanceEngineErrorCode   errorCode = GovernanceEngineErrorCode.ASSET_NOT_FOUND;
     * <p>
     * This will expand out to the 5 parameters shown below.
     *
     * @param newHTTPErrorCode  - error code to use over REST calls
     * @param newErrorMessageId - unique Id for the message
     * @param newErrorMessage   - text for the message
     * @param newSystemAction   - description of the action taken by the system when the error condition happened
     * @param newUserAction     - instructions for resolving the error
     */
    GovernanceEngineErrorCode(int newHTTPErrorCode, String newErrorMessageId, String newErrorMessage, String newSystemAction, String newUserAction) {
        this.httpErrorCode = newHTTPErrorCode;
        this.errorMessageId = newErrorMessageId;
        this.errorMessage = newErrorMessage;
        this.systemAction = newSystemAction;
        this.userAction = newUserAction;
    }


    public int getHTTPErrorCode() {
        return httpErrorCode;
    }


    /**
     * Returns the unique identifier for the error message.
     *
     * @return errorMessageId
     */
    public String getErrorMessageId() {
        return errorMessageId;
    }


    /**
     * Returns the error message with placeholders for specific details.
     *
     * @return errorMessage (unformatted)
     */
    public String getUnformattedErrorMessage() {
        return errorMessage;
    }


    /**
     * Returns the error message with the placeholders filled out with the supplied parameters.
     *
     * @param params - strings that plug into the placeholders in the errorMessage
     * @return errorMessage (formatted with supplied parameters)
     */
    public String getFormattedErrorMessage(String... params) {
        if (log.isDebugEnabled()) {
            log.debug(String.format("<== OCFErrorCode.getMessage(%s)", Arrays.toString(params)));
        }

        MessageFormat mf = new MessageFormat(errorMessage);
        String result = mf.format(params);

        if (log.isDebugEnabled()) {
            log.debug(String.format("==> OCFErrorCode.getMessage(%s): %s", Arrays.toString(params), result));
        }

        return result;
    }


    /**
     * Returns a description of the action taken by the system when the condition that caused this exception was
     * detected.
     *
     * @return systemAction
     */
    public String getSystemAction() {
        return systemAction;
    }


    /**
     * Returns instructions of how to resolve the issue reported in this exception.
     *
     * @return userAction
     */
    public String getUserAction() {
        return userAction;
    }
}
