/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.accessservices.subjectarea.validators;

import org.odpi.openmetadata.accessservices.subjectarea.ffdc.SubjectAreaErrorCode;
import org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.InvalidParameterException;
import org.odpi.openmetadata.accessservices.subjectarea.properties.enums.Status;

/**
 * Methods used for rest API input validation
 */
public class InputValidator {
    /**
     * Throw an exception if a org.odpi.openmetadata.accessservices.subjectarea.server URL has not been supplied on the constructor.
     *
     * @param className - name of the class making the call.
     * @param methodName - name of the method making the call.
     * @param omasServerURL - omas server url.
     * @throws InvalidParameterException - the org.odpi.openmetadata.accessservices.subjectarea.server URL is not set
     */
    static public void validateOMASServerURLNotNull( String className,String methodName, String omasServerURL) throws InvalidParameterException
    {
        if (omasServerURL == null)
        {
            /*
             * It is not possible to retrieve a connection without knowledge of where the OMAS Server is located.
             */
            SubjectAreaErrorCode errorCode    = SubjectAreaErrorCode.SERVER_URL_NOT_SPECIFIED;
            String                 errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage();

            throw new InvalidParameterException(errorCode.getHTTPErrorCode(),
                    className,
                    methodName,
                    errorMessage,
                    errorCode.getSystemAction(),
                    errorCode.getUserAction());
        }
    }


    /**
     * Throw an exception if the supplied userId is null
     * @param className name of the class
     * @param methodName name of the method making the call.
     * @param userId user name to validate
     * @throws InvalidParameterException the userId is null
     */
    static public void validateUserIdNotNull(
                                      String className,
                                      String methodName,
                                      String userId) throws InvalidParameterException
    {
        if (userId == null)
        {
            SubjectAreaErrorCode errorCode    = SubjectAreaErrorCode.NULL_USER_ID;
            String                 errorMessage = errorCode.getErrorMessageId()
                    + errorCode.getFormattedErrorMessage(methodName);

            throw new InvalidParameterException(errorCode.getHTTPErrorCode(),
                    className,
                    methodName,
                    errorMessage,
                    errorCode.getSystemAction(),
                    errorCode.getUserAction());
        }
    }

    /**
     * Validate the supplied string can be converted to a Status and return that status. If it cannot be converted then null is returned.
     * @param className - name of the class making the call.
     * @param methodName - name of the method making the call.
     * @param statusName - the String name to convert to a Status
     * @return Status or null.
     * @throws InvalidParameterException invalid status
     */
    static public Status validateStatusAndCheckNotDeleted(
            String className,
            String methodName,
            String statusName) throws InvalidParameterException {
        Status newStatus = null;
        try {
            newStatus = Status.valueOf(statusName);
        } catch (Exception e) {
            SubjectAreaErrorCode errorCode    = SubjectAreaErrorCode.INVALID_STATUS_VALUE_SUPPLIED;
            String                 errorMessage = errorCode.getErrorMessageId()
                    + errorCode.getFormattedErrorMessage(statusName);

            throw new InvalidParameterException(errorCode.getHTTPErrorCode(),
                    className,
                    methodName,
                    errorMessage,
                    errorCode.getSystemAction(),
                    errorCode.getUserAction());

        }
        if (Status.DELETED == newStatus) {
            SubjectAreaErrorCode errorCode    = SubjectAreaErrorCode.STATUS_UPDATE_TO_DELETED_NOT_ALLOWED;
            String                 errorMessage = errorCode.getErrorMessageId()
                    + errorCode.getUnformattedErrorMessage();

            throw new InvalidParameterException(errorCode.getHTTPErrorCode(),
                    className,
                    methodName,
                    errorMessage,
                    errorCode.getSystemAction(),
                    errorCode.getUserAction());
        }

        return newStatus;
    }


    /**
     * Throw an exception if the supplied guid is null
     *
     * @param className - name of the class making the call.
     * @param methodName - name of the method making the call.
     * @param guid - unique identifier to validate
     * @param guidParameter - name of the parameter that passed the guid
     * @throws InvalidParameterException - the guid is null
     */
    static public void validateGUIDNotNull(
                              String className,
                              String methodName,
                              String guid,
                              String guidParameter) throws InvalidParameterException
    {
        if (guid == null)
        {
            SubjectAreaErrorCode errorCode    = SubjectAreaErrorCode.NULL_GUID;
            String                 errorMessage = errorCode.getErrorMessageId()
                    + errorCode.getFormattedErrorMessage(guidParameter,
                    methodName);

            throw new InvalidParameterException(errorCode.getHTTPErrorCode(),
                    className,
                    methodName,
                    errorMessage,
                    errorCode.getSystemAction(),
                    errorCode.getUserAction());
        }
    }


    /**
     * Throw an exception if the supplied userId is null
     * @param className - name of the class making the call.
     * @param methodName - name of the method making the call.
     * @param name - unique name to validate
     * @param nameParameter - name of the parameter that passed the name.
     *
     * @throws InvalidParameterException - the guid is null
     */
    public static void validateNameNotNull(String className,
                                           String methodName,
                                           String name,
                                           String nameParameter
                              ) throws InvalidParameterException
    {
        if (name == null)
        {
            SubjectAreaErrorCode errorCode    = SubjectAreaErrorCode.NULL_NAME;
            String                 errorMessage = errorCode.getErrorMessageId()
                    + errorCode.getFormattedErrorMessage(nameParameter,
                    methodName);

            throw new InvalidParameterException(errorCode.getHTTPErrorCode(),
                    className,
                    methodName,
                    errorMessage,
                    errorCode.getSystemAction(),
                    errorCode.getUserAction());
        }
    }
}
