package org.odpi.openmetadata.accessservices.informationview.ffdc.exceptions.runtime;

public class DeleteEntityException extends InformationViewUncheckedExceptionBase{
    public DeleteEntityException(String className, String actionDescription, String errorMessage, String systemAction, String userAction, Throwable caughtError) {
        super(className, actionDescription, errorMessage, systemAction, userAction, caughtError);
    }
}
