/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.multitenant;


import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceDescription;
import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
import org.odpi.openmetadata.commonservices.ffdc.exceptions.InvalidParameterException;
import org.odpi.openmetadata.commonservices.ffdc.exceptions.PropertyServerException;
import org.odpi.openmetadata.commonservices.ffdc.exceptions.UserNotAuthorizedException;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryErrorHandler;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryHandler;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.OMRSMetadataCollection;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;

import java.util.List;

/**
 * OMASServiceInstanceHandler retrieves information from the instance map for the
 * access service instances.  The instance map is thread-safe.  Instances are added
 * and removed by the GovernanceProgramAdmin class.
 */
public class OMASServiceInstanceHandler extends AuditableServerServiceInstanceHandler
{
    RESTExceptionHandler  exceptionHandler = new RESTExceptionHandler();

    /**
     * Constructor
     *
     * @param accessServiceDescription a collection of descriptive strings for the OMAS
     */
    public OMASServiceInstanceHandler(AccessServiceDescription accessServiceDescription)
    {
        super(accessServiceDescription.getAccessServiceName() + " OMAS");
    }


    /**
     * Constructor
     *
     * @param serviceName a descriptive name for the OMAS
     */
    public OMASServiceInstanceHandler(String   serviceName)
    {
        super(serviceName);
    }


    /**
     * Retrieve the repository connector for the access service.
     *
     * @param userId calling userId
     * @param serverName name of the server tied to the request
     * @return repository connector for exclusive use by the requested instance
     * @throws InvalidParameterException the server name is not known
     * @throws UserNotAuthorizedException the user is not authorized to issue the request.
     * @throws PropertyServerException the service name is not known or the repository connector is
     *                                 not available - indicating a logic error
     */
    public OMRSRepositoryConnector getRepositoryConnector(String userId,
                                                          String serverName) throws InvalidParameterException,
                                                                                    UserNotAuthorizedException,
                                                                                    PropertyServerException
    {
        OMASServiceInstance instance = (OMASServiceInstance) super.getServerServiceInstance(userId, serverName);

        return instance.getRepositoryConnector();
    }


    /**
     * Retrieve the metadata collection for the access service.
     *
     * @param userId calling userId
     * @param serverName name of the server tied to the request
     * @return metadata collection for exclusive use by the requested instance
     * @throws InvalidParameterException the server name is not known
     * @throws UserNotAuthorizedException the user is not authorized to issue the request.
     * @throws PropertyServerException the service name is not known or the metadata collection is
     *                                 not available - indicating a logic error
     */
    public OMRSMetadataCollection getMetadataCollection(String userId,
                                                        String serverName) throws InvalidParameterException,
                                                                                  UserNotAuthorizedException,
                                                                                  PropertyServerException
    {
        OMASServiceInstance instance = (OMASServiceInstance) super.getServerServiceInstance(userId, serverName);

        return instance.getMetadataCollection();
    }


    /**
     * Retrieve the repository handler for the access service. Provides an advanced API for the
     * repository services.
     *
     * @param userId calling userId
     * @param serverName name of the server tied to the request
     * @return repository handler
     * @throws InvalidParameterException the server name is not known
     * @throws UserNotAuthorizedException the user is not authorized to issue the request.
     * @throws PropertyServerException the service name is not known or the metadata collection is
     *                                 not available - indicating a logic error
     */
    public RepositoryHandler getRepositoryHandler(String userId,
                                                  String serverName) throws InvalidParameterException,
                                                                            UserNotAuthorizedException,
                                                                            PropertyServerException
    {
        OMASServiceInstance instance = (OMASServiceInstance) super.getServerServiceInstance(userId, serverName);

        return instance.getRepositoryHandler();
    }


    /**
     * Retrieve the handler for managing errors from the repository services.
     *
     * @param userId calling userId
     * @param serverName name of the server tied to the request
     * @return repository error handler
     * @throws InvalidParameterException the server name is not known
     * @throws UserNotAuthorizedException the user is not authorized to issue the request.
     * @throws PropertyServerException the service name is not known or the metadata collection is
     *                                 not available - indicating a logic error
     */
    public RepositoryErrorHandler getErrorHandler(String userId,
                                                  String serverName) throws InvalidParameterException,
                                                                            UserNotAuthorizedException,
                                                                            PropertyServerException
    {
        OMASServiceInstance instance = (OMASServiceInstance) super.getServerServiceInstance(userId, serverName);

        return instance.getErrorHandler();
    }


    /**
     * Retrieve the handler for managing errors from the repository services.
     *
     * @param userId calling userId
     * @param serverName name of the server tied to the request
     * @return repository error handler
     * @throws InvalidParameterException the server name is not known
     * @throws UserNotAuthorizedException the user is not authorized to issue the request.
     * @throws PropertyServerException the service name is not known or the metadata collection is
     *                                 not available - indicating a logic error
     */
    public List<String> getSupportedZones(String userId,
                                          String serverName) throws InvalidParameterException,
                                                                    UserNotAuthorizedException,
                                                                    PropertyServerException
    {
        OMASServiceInstance instance = (OMASServiceInstance) super.getServerServiceInstance(userId, serverName);

        return instance.getSupportedZones();
    }


    /**
     * Retrieve the handler for managing errors from the repository services.
     *
     * @param userId calling userId
     * @param serverName name of the server tied to the request
     * @return repository error handler
     * @throws InvalidParameterException the server name is not known
     * @throws UserNotAuthorizedException the user is not authorized to issue the request.
     * @throws PropertyServerException the service name is not known or the metadata collection is
     *                                 not available - indicating a logic error
     */
    public List<String> getDefaultZones(String userId,
                                        String serverName) throws InvalidParameterException,
                                                                  UserNotAuthorizedException,
                                                                  PropertyServerException
    {
        OMASServiceInstance instance = (OMASServiceInstance) super.getServerServiceInstance(userId, serverName);

        return instance.getDefaultZones();
    }


    /**
     * Retrieve the exception handler that can package up common exceptions and pack them into
     * a REST Response.
     *
     * @return exception handler object
     */
    public RESTExceptionHandler getExceptionHandler() { return exceptionHandler; }
}
