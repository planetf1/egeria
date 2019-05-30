/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.ocf.metadatamanagement.handlers;

import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.builders.EndpointBuilder;
import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.converters.EndpointConverter;
import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.mappers.EndpointMapper;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryHandler;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Endpoint;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;


/**
 * EndpointHandler manages the Endpoint entity found within a connection object.  The Endpoint entity
 * describes the network location of a specific asset.  It may belong to multiple connections as well as being
 * part of a SoftWareServer definition.  This means it must be handled assuming it may be part of many constructs.
 * This is particularly important on delete.
 */
public class EndpointHandler
{
    private String                  serviceName;
    private String                  serverName;
    private OMRSRepositoryHelper    repositoryHelper;
    private RepositoryHandler       repositoryHandler;
    private InvalidParameterHandler invalidParameterHandler;


    /**
     * Construct the handler information needed to interact with the repository services
     *
     * @param serviceName      name of this service
     * @param serverName       name of the local server
     * @param invalidParameterHandler handler for managing parameter errors
     * @param repositoryHandler     manages calls to the repository services
     * @param repositoryHelper provides utilities for manipulating the repository services objects
     */
    public EndpointHandler(String                  serviceName,
                           String                  serverName,
                           InvalidParameterHandler invalidParameterHandler,
                           RepositoryHandler       repositoryHandler,
                           OMRSRepositoryHelper    repositoryHelper)
    {
        this.serviceName = serviceName;
        this.serverName = serverName;
        this.invalidParameterHandler = invalidParameterHandler;
        this.repositoryHandler = repositoryHandler;
        this.repositoryHelper = repositoryHelper;
    }


    /**
     * Find out if the endpoint is already stored in the repository.
     *
     * @param userId     calling user
     * @param endpoint   endpoint to find
     * @param methodName calling method
     * @return unique identifier of the endpoint or null
     * @throws InvalidParameterException  the endpoint bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    private String findEndpoint(String   userId,
                                Endpoint endpoint,
                                String   methodName) throws InvalidParameterException,
                                                            PropertyServerException,
                                                            UserNotAuthorizedException
    {
        final String guidParameterName      = "endpoint.getGUID";
        final String qualifiedNameParameter = "endpoint.getQualifiedName";

        if (endpoint != null)
        {
            if (endpoint.getGUID() != null)
            {
                if (repositoryHandler.isEntityKnown(userId,
                                                    endpoint.getGUID(),
                                                    EndpointMapper.ENDPOINT_TYPE_NAME,
                                                    methodName,
                                                    guidParameterName) != null)
                {
                    return endpoint.getGUID();
                }
            }

            invalidParameterHandler.validateName(endpoint.getQualifiedName(), qualifiedNameParameter, methodName);

            EndpointBuilder endpointBuilder = new EndpointBuilder(endpoint.getQualifiedName(),
                                                                  endpoint.getDisplayName(),
                                                                  endpoint.getDescription(),
                                                                  repositoryHelper,
                                                                  serviceName,
                                                                  serverName);

            EntityDetail existingEndpoint = repositoryHandler.getUniqueEntityByName(userId,
                                                                                    endpoint.getQualifiedName(),
                                                                                    qualifiedNameParameter,
                                                                                    endpointBuilder.getQualifiedNameInstanceProperties(methodName),
                                                                                    EndpointMapper.ENDPOINT_TYPE_GUID,
                                                                                    EndpointMapper.ENDPOINT_TYPE_NAME,
                                                                                    methodName);
            if (existingEndpoint != null)
            {
                return existingEndpoint.getGUID();
            }
        }

        return null;
    }


    /**
     * Verify that the Endpoint object is stored in the repository and create it if it is not.
     * If the endpoint is located, there is no check that the endpoint values are equal to those in
     * the supplied endpoint object.
     *
     * @param userId   calling userId
     * @param endpoint object to add
     * @return unique identifier of the endpoint in the repository.
     * @throws InvalidParameterException  the endpoint bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    public String saveEndpoint(String userId,
                               Endpoint endpoint) throws InvalidParameterException,
                                                         PropertyServerException,
                                                         UserNotAuthorizedException
    {
        final String methodName = "saveEndpoint";

        String existingEndpoint = this.findEndpoint(userId, endpoint, methodName);
        if (existingEndpoint == null)
        {
            return addEndpoint(userId, endpoint);
        }
        else
        {
            return updateEndpoint(userId, existingEndpoint, endpoint);
        }
    }


    /**
     * Verify that the Endpoint object is stored in the repository and create it if it is not.
     * If the endpoint is located, there is no check that the endpoint values are equal to those in
     * the supplied endpoint object.
     *
     * @param userId   calling userId
     * @param endpoint object to add
     * @return unique identifier of the endpoint in the repository.
     * @throws InvalidParameterException  the endpoint bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    private String addEndpoint(String userId,
                               Endpoint endpoint) throws InvalidParameterException,
                                                         PropertyServerException,
                                                         UserNotAuthorizedException
    {
        final String methodName = "addEndpoint";

        EndpointBuilder endpointBuilder = new EndpointBuilder(endpoint.getQualifiedName(),
                                                              endpoint.getDisplayName(),
                                                              endpoint.getDescription(),
                                                              endpoint.getAddress(),
                                                              endpoint.getProtocol(),
                                                              endpoint.getEncryptionMethod(),
                                                              endpoint.getAdditionalProperties(),
                                                              endpoint.getExtendedProperties(),
                                                              repositoryHelper,
                                                              serviceName,
                                                              serverName);
        return repositoryHandler.createEntity(userId,
                                              EndpointMapper.ENDPOINT_TYPE_GUID,
                                              EndpointMapper.ENDPOINT_TYPE_NAME,
                                              endpointBuilder.getInstanceProperties(methodName),
                                              methodName);
    }


    /**
     * Update a stored endpoint.
     *
     * @param userId               userId
     * @param existingEndpointGUID unique identifier of the existing endpoint entity
     * @param endpoint             new endpoint values
     * @return unique identifier of the endpoint in the repository.
     * @throws InvalidParameterException  the endpoint bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    private String updateEndpoint(String userId,
                                  String existingEndpointGUID,
                                  Endpoint endpoint) throws InvalidParameterException,
                                                            PropertyServerException,
                                                            UserNotAuthorizedException
    {
        final String methodName = "updateEndpoint";

        EndpointBuilder endpointBuilder = new EndpointBuilder(endpoint.getQualifiedName(),
                                                              endpoint.getDisplayName(),
                                                              endpoint.getDescription(),
                                                              endpoint.getAddress(),
                                                              endpoint.getProtocol(),
                                                              endpoint.getEncryptionMethod(),
                                                              endpoint.getAdditionalProperties(),
                                                              endpoint.getExtendedProperties(),
                                                              repositoryHelper,
                                                              serviceName,
                                                              serverName);
        repositoryHandler.updateEntity(userId,
                                       existingEndpointGUID,
                                       EndpointMapper.ENDPOINT_TYPE_GUID,
                                       EndpointMapper.ENDPOINT_TYPE_NAME,
                                       endpointBuilder.getInstanceProperties(methodName),
                                       methodName);

        return existingEndpointGUID;
    }


    /**
     * Remove the requested Endpoint if it is no longer connected to any other connection or server
     * definition.
     *
     * @param userId       calling user
     * @param endpointGUID object to delete
     * @throws InvalidParameterException  the entity guid is not known
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    public void removeEndpoint(String userId,
                               String endpointGUID) throws InvalidParameterException,
                                                           PropertyServerException,
                                                           UserNotAuthorizedException
    {
        final String methodName        = "removeEndpoint";
        final String guidParameterName = "endpointGUID";

        repositoryHandler.deleteEntityOnLastUse(userId,
                                                endpointGUID,
                                                guidParameterName,
                                                EndpointMapper.ENDPOINT_TYPE_GUID,
                                                EndpointMapper.ENDPOINT_TYPE_NAME,
                                                methodName);
    }


    /**
     * Retrieve the requested endpoint object.
     *
     * @param userId       calling user
     * @param endpointGUID unique identifier of the endpoint object.
     * @return Endpoint bean
     * @throws InvalidParameterException  the parameters are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    public Endpoint getEndpoint(String userId,
                                String endpointGUID) throws InvalidParameterException,
                                                            PropertyServerException,
                                                            UserNotAuthorizedException
    {
        final String methodName        = "getEndpoint";
        final String guidParameterName = "endpointGUID";

        EntityDetail endpointEntity = repositoryHandler.getEntityByGUID(userId,
                                                                        endpointGUID,
                                                                        guidParameterName,
                                                                        EndpointMapper.ENDPOINT_TYPE_NAME,
                                                                        methodName);

        EndpointConverter converter = new EndpointConverter(endpointEntity,
                                                            repositoryHelper,
                                                            serviceName);

        return converter.getBean();
    }
}
