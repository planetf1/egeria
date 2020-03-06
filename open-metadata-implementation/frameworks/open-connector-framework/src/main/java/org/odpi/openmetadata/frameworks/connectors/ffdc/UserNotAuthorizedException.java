/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.connectors.ffdc;

import org.odpi.openmetadata.frameworks.auditlog.messagesets.ExceptionMessageDefinition;

import java.util.Map;
import java.util.Objects;


/**
 * The UserNotAuthorizedException is thrown by the OCF when a userId passed on a request is not
 * authorized to perform the requested action.
 */
public class UserNotAuthorizedException extends OCFCheckedExceptionBase
{
    private static final long    serialVersionUID = 1L;

    private String  userId;


    /**
     * This is the typical constructor used for creating an exception.
     *
     * @param messageDefinition content of message
     * @param className   name of class reporting error
     * @param actionDescription   description of function it was performing when error detected
     * @param userId failing userId
     */
    public UserNotAuthorizedException(ExceptionMessageDefinition messageDefinition,
                                      String                     className,
                                      String                     actionDescription,
                                      String                     userId)
    {
        super(messageDefinition, className, actionDescription);

        this.userId = userId;
    }


    /**
     * This is the typical constructor used for creating an exception.
     *
     * @param messageDefinition content of message
     * @param className   name of class reporting error
     * @param actionDescription   description of function it was performing when error detected
     * @param userId failing userId
     * @param relatedProperties  arbitrary properties that may help with diagnosing the problem.
     */
    public UserNotAuthorizedException(ExceptionMessageDefinition messageDefinition,
                                      String                     className,
                                      String                     actionDescription,
                                      String                     userId,
                                      Map<String, Object>        relatedProperties)
    {
        super(messageDefinition, className, actionDescription, relatedProperties);

        this.userId = userId;
    }


    /**
     * This is the constructor used for creating an exception that resulted from a previous error.
     *
     * @param messageDefinition content of message
     * @param className   name of class reporting error
     * @param actionDescription   description of function it was performing when error detected
     * @param caughtError   the error that resulted in this exception.
     * @param userId failing userId
     */
    public UserNotAuthorizedException(ExceptionMessageDefinition messageDefinition,
                                      String                     className,
                                      String                     actionDescription,
                                      Throwable                  caughtError,
                                      String                     userId)
    {
        super(messageDefinition, className, actionDescription, caughtError);

        this.userId = userId;
    }


    /**
     * This is the constructor used for creating an exception that resulted from a previous error.
     *
     * @param messageDefinition content of message
     * @param className   name of class reporting error
     * @param actionDescription   description of function it was performing when error detected
     * @param caughtError   the error that resulted in this exception.
     * @param userId failing userId
     * @param relatedProperties  arbitrary properties that may help with diagnosing the problem.
     */
    public UserNotAuthorizedException(ExceptionMessageDefinition messageDefinition,
                                      String                     className,
                                      String                     actionDescription,
                                      Throwable                  caughtError,
                                      String                     userId,
                                      Map<String, Object>        relatedProperties)
    {
        super(messageDefinition, className, actionDescription, caughtError, relatedProperties);

        this.userId = userId;
    }


    /**
     * This is the typical constructor used for creating an exception.
     *
     * @param httpCode   http response code to use if this exception flows over a rest call
     * @param className   name of class reporting error
     * @param actionDescription   description of function it was performing when error detected
     * @param errorMessage   description of error
     * @param systemAction   actions of the system as a result of the error
     * @param userAction   instructions for correcting the error
     * @param userId failing userId
     */
    @Deprecated
    public UserNotAuthorizedException(int    httpCode,
                                      String className,
                                      String actionDescription,
                                      String errorMessage,
                                      String systemAction,
                                      String userAction,
                                      String userId)
    {
        super(httpCode, className, actionDescription, errorMessage, systemAction, userAction);

        this.userId = userId;
    }


    /**
     * This is the typical constructor used for creating an exception.
     *
     * @param httpCode   http response code to use if this exception flows over a rest call
     * @param className   name of class reporting error
     * @param actionDescription   description of function it was performing when error detected
     * @param errorMessage   description of error
     * @param systemAction   actions of the system as a result of the error
     * @param userAction   instructions for correcting the error
     * @param userId failing userId
     * @param relatedProperties  arbitrary properties that may help with diagnosing the problem.
     */
    @Deprecated
    public UserNotAuthorizedException(int                 httpCode,
                                      String              className,
                                      String              actionDescription,
                                      String              errorMessage,
                                      String              systemAction,
                                      String              userAction,
                                      String              userId,
                                      Map<String, Object> relatedProperties)
    {
        super(httpCode, className, actionDescription, errorMessage, systemAction, userAction, relatedProperties);

        this.userId = userId;
    }


    /**
     * This is the constructor used for creating an exception that resulted from a previous error.
     *
     * @param httpCode   http response code to use if this exception flows over a rest call
     * @param className   name of class reporting error
     * @param actionDescription   description of function it was performing when error detected
     * @param errorMessage   description of error
     * @param systemAction   actions of the system as a result of the error
     * @param userAction   instructions for correcting the error
     * @param caughtError   the error that resulted in this exception.
     * @param userId failing userId
     */
    @Deprecated
    public UserNotAuthorizedException(int       httpCode,
                                      String    className,
                                      String    actionDescription,
                                      String    errorMessage,
                                      String    systemAction,
                                      String    userAction,
                                      Throwable caughtError,
                                      String    userId)
    {
        super(httpCode, className, actionDescription, errorMessage, systemAction, userAction, caughtError);

        this.userId = userId;
    }


    /**
     * This is the constructor used for creating an exception that resulted from a previous error.
     *
     * @param httpCode   http response code to use if this exception flows over a rest call
     * @param className   name of class reporting error
     * @param actionDescription   description of function it was performing when error detected
     * @param errorMessage   description of error
     * @param systemAction   actions of the system as a result of the error
     * @param userAction   instructions for correcting the error
     * @param caughtError   the error that resulted in this exception.
     * @param userId failing userId
     * @param relatedProperties  arbitrary properties that may help with diagnosing the problem.
     */
    @Deprecated
    public UserNotAuthorizedException(int                 httpCode,
                                      String              className,
                                      String              actionDescription,
                                      String              errorMessage,
                                      String              systemAction,
                                      String              userAction,
                                      Throwable           caughtError,
                                      String              userId,
                                      Map<String, Object> relatedProperties)
    {
        super(httpCode, className, actionDescription, errorMessage, systemAction, userAction, caughtError, relatedProperties);

        this.userId = userId;
    }


    /**
     * This is the copy/clone constructor used for creating an exception.
     *
     * @param template   object to copy
     */
    public UserNotAuthorizedException(UserNotAuthorizedException template)
    {
        super(template);

        if (template != null)
        {
            this.userId = template.getUserId();
        }
    }


    /**
     * Return the userId passed on the request.
     *
     * @return string user id
     */
    public String getUserId()
    {
        return userId;
    }


    /**
     * JSON-style toString
     *
     * @return string of property names and values for this enum
     */
    @Override
    public String toString()
    {
        return "UserNotAuthorizedException{" +
                "userId='" + userId + '\'' +
                ", reportedHTTPCode=" + getReportedHTTPCode() +
                ", reportingClassName='" + getReportingClassName() + '\'' +
                ", reportingActionDescription='" + getReportingActionDescription() + '\'' +
                ", errorMessage='" + getErrorMessage() + '\'' +
                ", reportedSystemAction='" + getReportedSystemAction() + '\'' +
                ", reportedUserAction='" + getReportedUserAction() + '\'' +
                ", reportedCaughtException=" + getReportedCaughtException() +
                ", relatedProperties=" + getRelatedProperties() +
                '}';
    }


    /**
     * Return comparison result based on the content of the properties.
     *
     * @param objectToCompare test object
     * @return result of comparison
     */
    @Override
    public boolean equals(Object objectToCompare)
    {
        if (this == objectToCompare)
        {
            return true;
        }
        if (objectToCompare == null || getClass() != objectToCompare.getClass())
        {
            return false;
        }
        if (!super.equals(objectToCompare))
        {
            return false;
        }
        UserNotAuthorizedException that = (UserNotAuthorizedException) objectToCompare;
        return Objects.equals(getUserId(), that.getUserId());
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), getUserId());
    }
}
