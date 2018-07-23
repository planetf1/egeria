/* SPDX-License-Identifier: Apache-2.0 */

package org.odpi.openmetadata.accessservices.informationview.ffdc.exceptions;


public class InvalidEventException extends InformationViewCheckedExceptionBase {

    public InvalidEventException(String className, String actionDescription, String errorMessage, String systemAction, String userAction) {
        super(className, actionDescription, errorMessage, systemAction, userAction);
    }


    /**
     * This is the constructor used for creating a InvalidEventException that resulted from a previous error.
     *
     * @param className         - name of class reporting error
     * @param actionDescription - description of function it was performing when error detected
     * @param errorMessage      - description of error
     * @param systemAction      - actions of the system as a result of the error
     * @param userAction        - instructions for correcting the error
     * @param caughtError       - the error that resulted in this exception.
     */
    public InvalidEventException(String className, String actionDescription, String errorMessage, String systemAction, String userAction, Throwable caughtError) {
        super(className, actionDescription, errorMessage, systemAction, userAction, caughtError);
    }

}
