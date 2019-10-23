/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adminservices;

import org.odpi.openmetadata.adapters.repositoryservices.ConnectorConfigurationFactory;
import org.odpi.openmetadata.adminservices.configuration.properties.DataPlatformConfig;
import org.odpi.openmetadata.adminservices.configuration.properties.EventBusConfig;
import org.odpi.openmetadata.adminservices.configuration.properties.OMAGServerConfig;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGInvalidParameterException;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGNotAuthorizedException;
import org.odpi.openmetadata.commonservices.ffdc.rest.VoidResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class OMAGServerDataPlatformService {

    private OMAGServerAdminStoreServices configStore = new OMAGServerAdminStoreServices();
    private OMAGServerErrorHandler errorHandler = new OMAGServerErrorHandler();
    private OMAGServerExceptionHandler exceptionHandler = new OMAGServerExceptionHandler();

    private static final String defaultOutTopicName = "OMRSTopic.server.omas.open-metadata.access-services.DataPlatform.inTopic";


    private Logger log = LoggerFactory.getLogger(this.getClass());

    public VoidResponse setDataPlatformServiceConfig(String userId, String serverName, DataPlatformConfig dataPlatformConfig)
    {
        String methodName = "setDataPlatformServiceConfig";
        VoidResponse response = new VoidResponse();

        try
        {
            errorHandler.validateServerName(serverName, methodName);
            errorHandler.validateUserId(userId, serverName, methodName);

            OMAGServerConfig serverConfig = configStore.getServerConfig(userId, serverName, methodName);
            ConnectorConfigurationFactory connectorConfigurationFactory = new ConnectorConfigurationFactory();
            EventBusConfig eventBusConfig = serverConfig.getEventBusConfig();

            dataPlatformConfig.setDataPlatformServiceOutTopic(
                    connectorConfigurationFactory.getDefaultEventBusConnection(
                            defaultOutTopicName,
                            eventBusConfig.getConnectorProvider(),
                            eventBusConfig.getTopicURLRoot() + ".server." + serverName,
                            dataPlatformConfig.getDataPlatformServiceOutTopicName(),
                            UUID.randomUUID().toString(),
                            eventBusConfig.getConfigurationProperties()
                    )
            );

            Map<String, Object> additionalProperties = new HashMap<>();
            additionalProperties.put("serverAddress",dataPlatformConfig.getDataPlatformServerURL());

            dataPlatformConfig.setDataPlatformConnection(
                    connectorConfigurationFactory.getDataPlatformConnection(
                            serverName,
                            dataPlatformConfig.getDataPlatformServerName(),
                            additionalProperties
                    )
            );

            serverConfig.setDataPlatformConfig(dataPlatformConfig);

            configStore.saveServerConfig(serverName, methodName, serverConfig);

        }
        catch (OMAGInvalidParameterException error)
        {
            exceptionHandler.captureInvalidParameterException(response, error);
        }
        catch (OMAGNotAuthorizedException error)
        {
            exceptionHandler.captureNotAuthorizedException(response, error);
        }
        catch (Throwable  error)
        {
            exceptionHandler.captureRuntimeException(serverName, methodName, response, error);
        }

        return response;
    }

    public VoidResponse enableDataPlatformService(String userId, String serverName) {

        final String methodName = "enableDataPlatformService";
        VoidResponse response = new VoidResponse();

        try
        {
            errorHandler.validateServerName(serverName, methodName);
            errorHandler.validateUserId(userId, serverName, methodName);

            OMAGServerConfig serverConfig = configStore.getServerConfig(userId, serverName, methodName);
            DataPlatformConfig dataPlatformConfig = serverConfig.getDataPlatformConfig();
            this.setDataPlatformServiceConfig(userId, serverName, dataPlatformConfig);
        }
        catch (OMAGInvalidParameterException error)
        {
            exceptionHandler.captureInvalidParameterException(response, error);
        }
        catch (OMAGNotAuthorizedException error)
        {
            exceptionHandler.captureNotAuthorizedException(response, error);
        }
        catch (Throwable  error)
        {
            exceptionHandler.captureRuntimeException(serverName, methodName, response, error);
        }

        return response;
    }
}
