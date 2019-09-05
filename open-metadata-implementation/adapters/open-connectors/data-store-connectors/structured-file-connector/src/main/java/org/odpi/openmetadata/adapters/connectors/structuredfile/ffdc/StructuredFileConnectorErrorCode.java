/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.structuredfile.ffdc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.MessageFormat;
import java.util.Arrays;

/**
 * The StructuredFileConnectorErrorCode is used to define first failure data capture (FFDC) for errors that occur when working with
 * the Structured File Connector.  It is used in conjunction with both Checked and Runtime (unchecked) exceptions.
 *
 * The 5 fields in the enum are:
 * <ul>
 *     <li>HTTP Error Code - for translating between REST and JAVA - Typically the numbers used are:</li>
 *     <li><ul>
 *         <li>500 - internal error</li>
 *         <li>400 - invalid parameters</li>
 *         <li>404 - not found</li>
 *         <li>409 - data conflict errors - eg item already defined</li>
 *     </ul></li>
 *     <li>Error Message Id - to uniquely identify the message</li>
 *     <li>Error Message Text - includes placeholder to allow additional values to be captured</li>
 *     <li>SystemAction - describes the result of the error</li>
 *     <li>UserAction - describes how a consumer should correct the error</li>
 * </ul>
 */
public enum StructuredFileConnectorErrorCode
{
    FILE_NOT_SPECIFIED(400, "STRUCTURED-FILE-CONNECTOR-400-001 ",
            "The file name is null in the Connection object {0}",
            "The connector is unable to open the structure file because the name of the file is not passed in the Connection object.",
            "The name of the file should be set up in the address property of the connection's Endpoint object."),
    DIRECTORY_SPECIFIED(400, "STRUCTURED-FILE-CONNECTOR-400-002 ",
            "The file {0} given in Connection object {1} is a directory",
            "The connector is unable to work with a directory.",
            "Ensure a valid file name is passed in the address property in the Endpoint object of the Connection object."),
    FILE_NOT_READABLE(400, "STRUCTURED-FILE-CONNECTOR-400-003 ",
            "The file {0} given in Connection object {1} is not readable",
            "The connector is unable to open the file.",
            "Ensure a valid file name is passed in the address property in the Endpoint object of the Connection object."),
    FILE_TOO_SHORT(400, "STRUCTURED-FILE-CONNECTOR-400-004 ",
            "File {0} does not have {1} rows",
            "The connector is unable to retrieve the requested record because the file is too short.",
            "Ensure the record number requested is within the size of the file.  Method getRecordCount will provide information on the number of data records in the file"),
    FILE_NOT_FOUND(404, "STRUCTURED-FILE-CONNECTOR-404-001 ",
             "The file named {0} in the Connection object {1} does not exist",
             "The connector is unable to open the structure file.",
             "Add an existing file to the address property of the connection's Endpoint object."),
    UNEXPECTED_IO_EXCEPTION(500, "STRUCTURED-FILE-CONNECTOR-500-001 ",
             "The connector received an unexpected IO exception when reading the file named {0}; the error message was: {1}",
             "The connector is unable to process the structure file.",
             "Use details from the error message to determine the cause of the error and retry the request once it is resolved.");


    private int    httpErrorCode;
    private String errorMessageId;
    private String errorMessage;
    private String systemAction;
    private String userAction;

    private static final Logger log = LoggerFactory.getLogger(StructuredFileConnectorErrorCode.class);


    /**
     * The constructor for StructuredFileConnectorErrorCode expects to be passed one of the enumeration rows defined in
     * AssetConsumerErrorCode above.   For example:
     *
     *     StructuredFileConnectorErrorCode   errorCode = StructuredFileConnectorErrorCode.FILE_NOT_FOUND;
     *
     * This will expand out to the 5 parameters shown below.
     *
     * @param newHTTPErrorCode  error code to use over REST calls
     * @param newErrorMessageId  unique Id for the message
     * @param newErrorMessage text for the message
     * @param newSystemAction  description of the action taken by the system when the error condition happened
     * @param newUserAction  instructions for resolving the error
     */
    StructuredFileConnectorErrorCode(int  newHTTPErrorCode, String newErrorMessageId, String newErrorMessage, String newSystemAction, String newUserAction)
    {
        this.httpErrorCode = newHTTPErrorCode;
        this.errorMessageId = newErrorMessageId;
        this.errorMessage = newErrorMessage;
        this.systemAction = newSystemAction;
        this.userAction = newUserAction;
    }


    public int getHTTPErrorCode()
    {
        return httpErrorCode;
    }


    /**
     * Returns the unique identifier for the error message.
     *
     * @return errorMessageId
     */
    public String getErrorMessageId()
    {
        return errorMessageId;
    }


    /**
     * Returns the error message with placeholders for specific details.
     *
     * @return errorMessage (unformatted)
     */
    public String getUnformattedErrorMessage()
    {
        return errorMessage;
    }


    /**
     * Returns the error message with the placeholders filled out with the supplied parameters.
     *
     * @param params  strings that plug into the placeholders in the errorMessage
     * @return errorMessage (formatted with supplied parameters)
     */
    public String getFormattedErrorMessage(String... params)
    {
        log.debug(String.format("<== StructuredFileConnectorErrorCode.getMessage(%s)", Arrays.toString(params)));

        MessageFormat mf = new MessageFormat(errorMessage);
        String result = mf.format(params);

        log.debug(String.format("==> StructuredFileConnectorErrorCode.getMessage(%s): %s", Arrays.toString(params), result));

        return result;
    }


    /**
     * Returns a description of the action taken by the system when the condition that caused this exception was
     * detected.
     *
     * @return systemAction
     */
    public String getSystemAction()
    {
        return systemAction;
    }


    /**
     * Returns instructions of how to resolve the issue reported in this exception.
     *
     * @return userAction
     */
    public String getUserAction()
    {
        return userAction;
    }


    /**
     * JSON-style toString
     *
     * @return string of property names and values for this enum
     */
    @Override
    public String toString()
    {
        return "StructuredFileConnectorErrorCode{" +
                "httpErrorCode=" + httpErrorCode +
                ", errorMessageId='" + errorMessageId + '\'' +
                ", errorMessage='" + errorMessage + '\'' +
                ", systemAction='" + systemAction + '\'' +
                ", userAction='" + userAction + '\'' +
                '}';
    }
}
