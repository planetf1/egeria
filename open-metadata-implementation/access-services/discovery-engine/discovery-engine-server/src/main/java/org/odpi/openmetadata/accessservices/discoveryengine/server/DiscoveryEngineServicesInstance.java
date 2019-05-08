/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.discoveryengine.server;


import org.odpi.openmetadata.accessservices.discoveryengine.ffdc.DiscoveryEngineErrorCode;
import org.odpi.openmetadata.accessservices.discoveryengine.handlers.DiscoveryConfigurationHandler;
import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceDescription;
import org.odpi.openmetadata.commonservices.multitenant.OCFOMASServiceInstance;
import org.odpi.openmetadata.commonservices.multitenant.ffdc.exceptions.NewInstanceException;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLog;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;

import java.util.List;

/**
 * DiscoveryEngineServicesInstance caches references to OMRS objects for a specific server.
 * It is also responsible for registering itself in the instance map.
 * It is created by the admin class during server start up and
 */
public class DiscoveryEngineServicesInstance extends OCFOMASServiceInstance
{
    private static AccessServiceDescription myDescription = AccessServiceDescription.DISCOVERY_ENGINE_OMAS;

    private DiscoveryConfigurationHandler discoveryConfigurationHandler;


    /**
     * Set up the local repository connector that will service the REST Calls.
     *
     * @param repositoryConnector link to the repository responsible for servicing the REST calls.
     * @param supportedZones list of zones that DiscoveryEngine is allowed to serve Assets from.
     * @param defaultZones list of zones that DiscoveryEngine should set in all new Assets.
     * @param auditLog logging destination
     *
     * @throws NewInstanceException a problem occurred during initialization
     */
    public DiscoveryEngineServicesInstance(OMRSRepositoryConnector repositoryConnector,
                                           List<String>            supportedZones,
                                           List<String>            defaultZones,
                                           OMRSAuditLog            auditLog) throws NewInstanceException
    {
        super(myDescription.getAccessServiceName(),
              repositoryConnector,
              auditLog);

        final String methodName = "new ServiceInstance";

        super.supportedZones = supportedZones;
        super.defaultZones = defaultZones;

        if (repositoryHandler != null)
        {
            this.discoveryConfigurationHandler = new DiscoveryConfigurationHandler(serviceName,
                                                                                   serverName,
                                                                                   invalidParameterHandler,
                                                                                   repositoryHelper,
                                                                                   repositoryHandler,
                                                                                   errorHandler,
                                                                                   connectionHandler);
        }
        else
        {
            DiscoveryEngineErrorCode errorCode    = DiscoveryEngineErrorCode.OMRS_NOT_INITIALIZED;
            String                errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(methodName);

            throw new NewInstanceException(errorCode.getHTTPErrorCode(),
                                           this.getClass().getName(),
                                           methodName,
                                           errorMessage,
                                           errorCode.getSystemAction(),
                                           errorCode.getUserAction());

        }
    }


    /**
     * Return the handler for configuration requests.
     *
     * @return handler object
     */
    DiscoveryConfigurationHandler getDiscoveryConfigurationHandler()
    {
        return discoveryConfigurationHandler;
    }
}
