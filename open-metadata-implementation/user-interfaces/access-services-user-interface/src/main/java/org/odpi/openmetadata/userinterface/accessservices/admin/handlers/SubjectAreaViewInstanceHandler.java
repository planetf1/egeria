/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.userinterface.accessservices.admin.handlers;

import org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.MetadataServerUncontactableException;
import org.odpi.openmetadata.commonservices.ffdc.exceptions.InvalidParameterException;
import org.odpi.openmetadata.commonservices.ffdc.exceptions.PropertyServerException;
import org.odpi.openmetadata.commonservices.ffdc.exceptions.UserNotAuthorizedException;
import org.odpi.openmetadata.commonservices.multitenant.OMVSServiceInstanceHandler;
import org.odpi.openmetadata.userinterface.accessservices.admin.registration.SubjectAreaViewRegistration;
import org.odpi.openmetadata.userinterface.accessservices.admin.serviceinstances.SubjectAreaViewServicesInstance;
import org.odpi.openmetadata.userinterface.accessservices.ffdc.ViewServiceErrorCode;
import org.odpi.openmetadata.userinterface.adminservices.configuration.registration.ViewServiceDescription;

/**
 * SubjectAreaViewInstanceHandler retrieves information from the instance map for the
 * access service instances.  The instance map is thread-safe.  Instances are added
 * and removed by the SubjectAreaViewAdmin class.
 */
public class SubjectAreaViewInstanceHandler extends OMVSServiceInstanceHandler
{
    /**
     * Default constructor registers the view service
     */
    public SubjectAreaViewInstanceHandler(String serviceName) {
        super(serviceName);
        SubjectAreaViewRegistration.registerViewService();
    }
    /**
     * Return the Subject Area view's official view Service Name
     *
     * @param serverName  name of the server that the request is for
     * @param userId local server userid
     * @param serviceOperationName service operation - usually the top level rest call
     * @return String the service name
     * @throws MetadataServerUncontactableException no available instance for the requested server
     */
    public String  getViewServiceName(String serverName, String userId, String serviceOperationName) throws MetadataServerUncontactableException, InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        SubjectAreaViewServicesInstance instance = (SubjectAreaViewServicesInstance)
                super.getServerServiceInstance(userId,
                serverName,
                serviceOperationName);

        if (instance != null) {
            return instance.getViewServiceName();
        } else {
            final String methodName = "getViewServiceName";

            ViewServiceErrorCode errorCode    = ViewServiceErrorCode.SERVICE_NOT_INITIALIZED;
            String                    errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(serverName, ViewServiceDescription.SUBJECT_AREA.getViewServiceName(),methodName);

            throw new MetadataServerUncontactableException(errorCode.getHTTPErrorCode(),
                                              this.getClass().getName(),
                                              methodName,
                                              errorMessage,
                                              errorCode.getSystemAction(),
                                              errorCode.getUserAction());
        }
    }

    /**
     * This serverName has an associated metadata server. This call returns that metadata servers's name.
     * @param serverName  name of the server that the request is for
     * @param userId local server userid
     * @param serviceOperationName service operation - usually the top level rest call
     * @return String Metadata server name
     * @throws MetadataServerUncontactableException Metadata server uncontactable
     */
    public String getMetadataServerName(String serverName, String userId, String serviceOperationName) throws MetadataServerUncontactableException, InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        SubjectAreaViewServicesInstance instance = (SubjectAreaViewServicesInstance)
                super.getServerServiceInstance(userId,
                        serverName,
                        serviceOperationName);

        if (instance != null) {
            return instance.getMetadataServerName();
        } else {
            final String methodName = "getMetadataServerURL";

            ViewServiceErrorCode errorCode    = ViewServiceErrorCode.SERVICE_NOT_INITIALIZED;
            String                    errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(serverName, ViewServiceDescription.SUBJECT_AREA.getViewServiceName(),methodName);

            throw new MetadataServerUncontactableException(errorCode.getHTTPErrorCode(),
                    this.getClass().getName(),
                    methodName,
                    errorMessage,
                    errorCode.getSystemAction(),
                    errorCode.getUserAction());
        }
    }
    /**
     * This serverName has an associated metadata server. This call returns that metadata servers's URL.
     * @param serverName  name of the server that the request is for
     * @param userId local server userid
     * @param serviceOperationName service operation - usually the top level rest call
     * @return String Metadata server URL
     * @throws MetadataServerUncontactableException Metadata server uncontactable
     */
    public String getMetadataServerURL(String serverName, String userId, String serviceOperationName ) throws MetadataServerUncontactableException, InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        SubjectAreaViewServicesInstance instance = (SubjectAreaViewServicesInstance)
                super.getServerServiceInstance(userId,
                        serverName,
                        serviceOperationName);

        if (instance != null) {
            return instance.getMetadataServerURL();
        } else {
            final String methodName = "getMetadataServerURL";

            ViewServiceErrorCode errorCode    = ViewServiceErrorCode.SERVICE_NOT_INITIALIZED;
            String                    errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(serverName, ViewServiceDescription.SUBJECT_AREA.getViewServiceName(),methodName);

            throw new MetadataServerUncontactableException(errorCode.getHTTPErrorCode(),
                    this.getClass().getName(),
                    methodName,
                    errorMessage,
                    errorCode.getSystemAction(),
                    errorCode.getUserAction());
        }
    }

}
