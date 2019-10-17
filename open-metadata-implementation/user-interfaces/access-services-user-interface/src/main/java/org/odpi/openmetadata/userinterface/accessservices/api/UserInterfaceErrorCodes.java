/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.userinterface.accessservices.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

public enum UserInterfaceErrorCodes {

    MALFORMED_INPUT_EXCEPTION(HttpStatus.INTERNAL_SERVER_ERROR, "USER-INTERFACE-500-001",
                                     "The response received from server does not have the expected format.",
                                     "The access service has not been passed valid configuration for its out topic connection.",
                                     "Correct the configuration and restart the service."),
    INVALID_REQUEST_FOR_ASSET_CATALOG(HttpStatus.INTERNAL_SERVER_ERROR, "USER-INTERFACE-500-002",
                                 "The request for asset catalog is invalid",
                                 "The system is unable to handle request.",
                                 "Check that the configuration for Asset Catalog is correct." ),
    RESOURCE_NOT_FOUND(HttpStatus.SERVICE_UNAVAILABLE, "USER-INTERFACE-503-003",
                                 "The resource cannot be accessed",
                                 "The system is unable to access resource.",
                                 "Check the configuration and service is up"),
    USER_NOT_AUTHORIZED(HttpStatus.UNAUTHORIZED, "USER-INTERFACE-401-004",
                                   "User is not authorized",
                                   "The system is unable to authorize the user.",
                                   "Check your credentials.");

    private HttpStatus httpErrorCode;
    private String errorMessageId;
    private String errorMessage;
    private String systemAction;
    private String userAction;

    private static final Logger log = LoggerFactory.getLogger(UserInterfaceErrorCodes.class);


    UserInterfaceErrorCodes(HttpStatus httpErrorCode, String errorMessageId, String errorMessage, String systemAction, String userAction) {
        this.httpErrorCode = httpErrorCode;
        this.errorMessageId = errorMessageId;
        this.errorMessage = errorMessage;
        this.systemAction = systemAction;
        this.userAction = userAction;
    }


    public HttpStatus getHttpErrorCode() {
        return httpErrorCode;
    }

    public String getErrorMessageId() {
        return errorMessageId;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public String getSystemAction() {
        return systemAction;
    }

    public String getUserAction() {
        return userAction;
    }
}
