/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetconsumer.server;

import org.odpi.openmetadata.accessservices.assetconsumer.handlers.GlossaryHandler;
import org.odpi.openmetadata.accessservices.assetconsumer.handlers.LoggingHandler;
import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceDescription;
import org.odpi.openmetadata.commonservices.multitenant.OCFOMASServiceInstanceHandler;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;


/**
 * AssetConsumerInstanceHandler retrieves information from the instance map for the
 * access service instances.  The instance map is thread-safe.  Instances are added
 * and removed by the AssetConsumerAdmin class.
 */
class AssetConsumerInstanceHandler extends OCFOMASServiceInstanceHandler
{
    /**
     * Default constructor registers the access service
     */
    AssetConsumerInstanceHandler()
    {
        super(AccessServiceDescription.ASSET_CONSUMER_OMAS);

        AssetConsumerRegistration.registerAccessService();
    }


    /**
     * Retrieve the specific handler for the access service.
     *
     * @param userId calling user
     * @param serverName name of the server tied to the request
     * @return handler for use by the requested instance
     * @throws InvalidParameterException no available instance for the requested server
     * @throws UserNotAuthorizedException user does not have access to the requested server
     * @throws PropertyServerException error in the requested server
     */
    GlossaryHandler getGlossaryHandler(String userId,
                                       String serverName) throws InvalidParameterException,
                                                                 UserNotAuthorizedException,
                                                                 PropertyServerException
    {
        AssetConsumerServicesInstance instance = (AssetConsumerServicesInstance)super.getServerServiceInstance(userId, serverName);

        if (instance != null)
        {
            return instance.getGlossaryHandler();
        }

        return null;
    }


    /**
     * Retrieve the specific handler for the access service.
     *
     * @param userId calling user
     * @param serverName name of the server tied to the request
     * @return handler for use by the requested instance
     * @throws InvalidParameterException no available instance for the requested server
     * @throws UserNotAuthorizedException user does not have access to the requested server
     * @throws PropertyServerException error in the requested server
     */
    LoggingHandler getLoggingHandler(String userId,
                                     String serverName) throws InvalidParameterException,
                                                                UserNotAuthorizedException,
                                                                PropertyServerException
    {
        AssetConsumerServicesInstance instance = (AssetConsumerServicesInstance)super.getServerServiceInstance(userId, serverName);

        if (instance != null)
        {
            return instance.getLoggingHandler();
        }

        return null;
    }
}
