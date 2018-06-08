/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.adminservices.ffdc.exception;

/**
 * OMAGNotAuthorizedException is used when invalid parameters are passed on an OMAG call.
 */
public class OMAGNotAuthorizedException extends OMAGCheckedExceptionBase
{
    /**
     * This is the typical constructor used for creating a OMAGNotAuthorizedException.
     *
     * @param httpCode - http response code to use if this exception flows over a REST call
     * @param className - name of class reporting error
     * @param actionDescription - description of function it was performing when error detected
     * @param errorMessage - description of error
     * @param systemAction - actions of the system as a result of the error
     * @param userAction - instructions for correcting the error
     */
    public OMAGNotAuthorizedException(int  httpCode, String className, String  actionDescription, String errorMessage, String systemAction, String userAction)
    {
        super(httpCode, className, actionDescription, errorMessage, systemAction, userAction);
    }


    /**
     * This is the constructor used for creating a OMAGNotAuthorizedException that resulted from a previous error.
     *
     * @param httpCode - http response code to use if this exception flows over a REST call
     * @param className - name of class reporting error
     * @param actionDescription - description of function it was performing when error detected
     * @param errorMessage - description of error
     * @param systemAction - actions of the system as a result of the error
     * @param userAction - instructions for correcting the error
     * @param caughtError - the error that resulted in this exception.
     * */
    public OMAGNotAuthorizedException(int  httpCode, String className, String  actionDescription, String errorMessage, String systemAction, String userAction, Throwable caughtError)
    {
        super(httpCode, className, actionDescription, errorMessage, systemAction, userAction, caughtError);
    }
}
