/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataengine.ffdc;


import java.text.MessageFormat;

/**
 * The DataEngineErrorCode is used to define first failure data capture (FFDC) for errors that occur when working with
 * the Data Engine OMAS Services.  It is used in conjunction with both Checked and Runtime (unchecked) exceptions.
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
 * <li>UserAction - describes how a AssetConsumerInterface should correct the error</li>
 * </ul>
 */

public enum DataEngineErrorCode {
    OMRS_NOT_INITIALIZED(404, "OMAS-DATA-ENGINE-404-001 ",
            "The open metadata repository services are not initialized for server {0}",
            "The system is unable to connect to the open metadata property server.",
            "Check that the server initialized correctly.  " +
                    "Correct any errors discovered and retry the request when the open metadata services are available."),
    INVALID_PORT_TYPE(400, "OMAS-DATA-ENGINE-400-005 ",
            "The port type passed for the {0} is invalid, or different from {1}",
            "The system is unable to create a new PortDelegation relation the request without equal types between the ports.",
            "Correct the code in the caller to provide the correct port type."),
    NO_SCHEMA_ATTRIBUTE(404, "OMAS-DATA-ENGINE-404-002 ",
            "No schema attribute found for qualifiedName {0}",
            "The system is unable to retrieve a schema attribute for the specified qualifiedName.",
            "Correct the code in the caller to provide the qualified name.");

    private int HTTPErrorCode;
    private String errorMessageId;
    private String errorMessage;
    private String systemAction;
    private String userAction;

    /**
     * The constructor for DataEngineErrorCode expects to be passed one of the enumeration rows defined in
     * DataEngineErrorCode above.   For example:
     * <p>
     * DataEngineErrorCode   errorCode = DataEngineErrorCode.NULL_USER_ID;
     * <p>
     * This will expand out to the 5 parameters shown below.
     *
     * @param newHTTPErrorCode  - error code to use over REST calls
     * @param newErrorMessageId - unique Id for the message
     * @param newErrorMessage   - text for the message
     * @param newSystemAction   - description of the action taken by the system when the error condition happened
     * @param newUserAction     - instructions for resolving the error
     */
    DataEngineErrorCode(int newHTTPErrorCode, String newErrorMessageId, String newErrorMessage, String newSystemAction,
                        String newUserAction) {
        this.HTTPErrorCode = newHTTPErrorCode;
        this.errorMessageId = newErrorMessageId;
        this.errorMessage = newErrorMessage;
        this.systemAction = newSystemAction;
        this.userAction = newUserAction;
    }

    /**
     * Returns the error message with the placeholders filled out with the supplied parameters.
     *
     * @param params - strings that plug into the placeholders in the errorMessage
     * @return errorMessage (formatted with supplied parameters)
     */
    public String getFormattedErrorMessage(String... params) {
        MessageFormat mf = new MessageFormat(errorMessage);
        return mf.format(params);
    }

    /**
     * Returns the numeric code that can be used in a REST response.
     *
     * @return int
     */
    public int getHTTPErrorCode() {
        return HTTPErrorCode;
    }

    /**
     * Returns the unique error message identifier of the error.
     *
     * @return String
     */
    public String getErrorMessageId() {
        return errorMessageId;
    }

    /**
     * Returns the un-formatted error message.
     *
     * @return String
     */
    public String getErrorMessage() {
        return errorMessage;
    }

    /**
     * Returns the action taken by the system when the error occurred.
     *
     * @return String
     */
    public String getSystemAction() {
        return systemAction;
    }

    /**
     * Returns the proposed action for a user to take when encountering the error.
     *
     * @return String
     */
    public String getUserAction() {
        return userAction;
    }
}

