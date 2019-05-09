/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.communityprofile.ffdc.exceptions;

import org.odpi.openmetadata.frameworks.connectors.ffdc.OCFCheckedExceptionBase;

/**
 * CommunityProfileCheckedExceptionBase provides a checked exception for reporting errors found when using
 * the Community Profile OMAS services.
 *
 * Typically these errors are either configuration or operational errors that can be fixed by an administrator
 * or the developer that wrote the consuming service.   However, there may be the odd bug that surfaces here.
 * The CommunityProfileErrorCode can be used with this exception to populate it with standard messages.
 * The aim is to be able to uniquely identify the cause and remedy for the error.
 */
public abstract class CommunityProfileCheckedExceptionBase extends OCFCheckedExceptionBase
{
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
    public CommunityProfileCheckedExceptionBase(int    httpCode,
                                                String className,
                                                String actionDescription,
                                                String errorMessage,
                                                String systemAction,
                                                String userAction)
    {
        super(httpCode, className, actionDescription, errorMessage, systemAction, userAction);
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
    public CommunityProfileCheckedExceptionBase(int       httpCode,
                                                String    className,
                                                String    actionDescription,
                                                String    errorMessage,
                                                String    systemAction,
                                                String    userAction,
                                                Throwable caughtError)
    {
        super(httpCode, className, actionDescription, errorMessage, systemAction, userAction, caughtError);
    }
}
