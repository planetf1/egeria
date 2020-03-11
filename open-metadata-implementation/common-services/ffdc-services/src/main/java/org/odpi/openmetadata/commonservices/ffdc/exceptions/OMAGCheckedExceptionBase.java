/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.ffdc.exceptions;

import org.odpi.openmetadata.frameworks.auditlog.messagesets.ExceptionMessageDefinition;

import java.util.Map;

/**
 * OMAGCheckedExceptionBase provides a checked exception for reporting errors found when using
 * the Open Metadata and Governance (OMAG) services.
 */
public abstract class OMAGCheckedExceptionBase extends org.odpi.openmetadata.frameworks.connectors.ffdc.OCFCheckedExceptionBase
{
    private static final long    serialVersionUID = 1L;

    /**
     * This is the typical constructor used for creating an OMAGCheckedExceptionBase.
     *
     * @param messageDefinition  content of the message
     * @param className   name of class reporting error
     * @param actionDescription   description of function it was performing when error detected
     */
    public OMAGCheckedExceptionBase(ExceptionMessageDefinition messageDefinition,
                                    String                     className,
                                    String                     actionDescription)
    {
        super(messageDefinition, className, actionDescription);
    }


    /**
     * This is the typical constructor used for creating an OMAGCheckedExceptionBase.
     * The properties allow additional information to be associated with the exception.
     *
     * @param messageDefinition  content of the message
     * @param className   name of class reporting error
     * @param actionDescription   description of function it was performing when error detected
     * @param relatedProperties  arbitrary properties that may help with diagnosing the problem.
     */
    public OMAGCheckedExceptionBase(ExceptionMessageDefinition messageDefinition,
                                    String                     className,
                                    String                     actionDescription,
                                    Map<String, Object>        relatedProperties)
    {
        super(messageDefinition, className, actionDescription, relatedProperties);
    }


    /**
     * This is the constructor used for creating an OMAGCheckedExceptionBase when an unexpected error has been caught.
     * The properties allow additional information to be associated with the exception.
     *
     * @param messageDefinition  content of the message
     * @param className   name of class reporting error
     * @param actionDescription   description of function it was performing when error detected
     * @param caughtError   previous error causing this exception
     */
    public OMAGCheckedExceptionBase(ExceptionMessageDefinition messageDefinition,
                                    String                     className,
                                    String                     actionDescription,
                                    Throwable                  caughtError)
    {
        super(messageDefinition, className, actionDescription, caughtError);
    }


    /**
     * This is the constructor used for creating an OMAGCheckedExceptionBase when an unexpected error has been caught.
     * The properties allow additional information to be associated with the exception.
     *
     * @param messageDefinition  content of the message
     * @param className   name of class reporting error
     * @param actionDescription   description of function it was performing when error detected
     * @param caughtError   previous error causing this exception
     * @param relatedProperties  arbitrary properties that may help with diagnosing the problem.
     */
    public OMAGCheckedExceptionBase(ExceptionMessageDefinition messageDefinition,
                                    String                     className,
                                    String                     actionDescription,
                                    Throwable                  caughtError,
                                    Map<String, Object>        relatedProperties)
    {
        super(messageDefinition, className, actionDescription, caughtError, relatedProperties);
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
     */
    @Deprecated
    public OMAGCheckedExceptionBase(int    httpCode,
                                    String className,
                                    String actionDescription,
                                    String errorMessage,
                                    String systemAction,
                                    String userAction)
    {
        super(httpCode, className, actionDescription, errorMessage, systemAction, userAction);
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
     * @param relatedProperties  arbitrary properties that may help with diagnosing the problem.
     */
    @Deprecated
    public OMAGCheckedExceptionBase(int                 httpCode,
                                    String              className,
                                    String              actionDescription,
                                    String              errorMessage,
                                    String              systemAction,
                                    String              userAction,
                                    Map<String, Object> relatedProperties)
    {
        super(httpCode, className, actionDescription, errorMessage, systemAction, userAction, relatedProperties);
    }


    /**
     * This is the  constructor used for creating an exception
     * that resulted from a previous error.
     *
     * @param httpCode   http response code to use if this exception flows over a rest call
     * @param className   name of class reporting error
     * @param actionDescription   description of function it was performing when error detected
     * @param errorMessage   description of error
     * @param systemAction   actions of the system as a result of the error
     * @param userAction   instructions for correcting the error
     * @param caughtError   the error that resulted in this exception.
     */
    @Deprecated
    public OMAGCheckedExceptionBase(int       httpCode,
                                    String    className,
                                    String    actionDescription,
                                    String    errorMessage,
                                    String    systemAction,
                                    String    userAction,
                                    Throwable caughtError)
    {
        super(httpCode, className, actionDescription, errorMessage, systemAction, userAction, caughtError);
    }


    /**
     * This is the  constructor used for creating an exception
     * that resulted from a previous error.
     *
     * @param httpCode   http response code to use if this exception flows over a rest call
     * @param className   name of class reporting error
     * @param actionDescription   description of function it was performing when error detected
     * @param errorMessage   description of error
     * @param systemAction   actions of the system as a result of the error
     * @param userAction   instructions for correcting the error
     * @param caughtError   the error that resulted in this exception.
     * @param relatedProperties  arbitrary properties that may help with diagnosing the problem.
     */
    @Deprecated
    public OMAGCheckedExceptionBase(int                  httpCode,
                                    String               className,
                                    String               actionDescription,
                                    String               errorMessage,
                                    String               systemAction,
                                    String               userAction,
                                    Throwable            caughtError,
                                    Map<String, Object>  relatedProperties)
    {
        super(httpCode, className, actionDescription, errorMessage, systemAction, userAction, caughtError, relatedProperties);
    }


    /**
     * This is the copy/clone constructor used for creating an exception.
     *
     * @param errorMessage associated message
     * @param template   object to copy
     */
    public OMAGCheckedExceptionBase(String                   errorMessage,
                                    OMAGCheckedExceptionBase template)
    {
        super(errorMessage, template);
    }


    /**
     * JSON-style toString
     *
     * @return string of property names and values for this enum
     */
    @Override
    public String toString()
    {
        return "OMAGCheckedExceptionBase{" +
                "reportedHTTPCode=" + getReportedHTTPCode() +
                ", reportingClassName='" + getReportingClassName() + '\'' +
                ", reportingActionDescription='" + getReportingActionDescription() + '\'' +
                ", errorMessage='" + getReportedErrorMessage() + '\'' +
                ", reportedSystemAction='" + getReportedSystemAction() + '\'' +
                ", reportedUserAction='" + getReportedUserAction() + '\'' +
                ", reportedCaughtException=" + getReportedCaughtException() +
                ", relatedProperties=" + getRelatedProperties() +
                '}';
    }
}
