/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.repositoryhandler;

import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * RepositoryErrorHandler provides common validation routines for the other handler classes
 */
public class RepositoryErrorHandler
{
    private String               serviceName;
    private String               serverName;
    private OMRSRepositoryHelper repositoryHelper;

    /**
     * Typical constructor providing access to the repository connector for this access service.
     *
     * @param repositoryHelper  access to the repository helper.
     * @param serviceName  name of this access service
     * @param serverName  name of this server
     */
    public RepositoryErrorHandler(OMRSRepositoryHelper repositoryHelper,
                                  String               serviceName,
                                  String               serverName)
    {
        this.repositoryHelper = repositoryHelper;
        this.serviceName = serviceName;
        this.serverName = serverName;
    }

    /**
     * Check that there is a repository connector.
     *
     * @param methodName  name of the method being called
     * @param repositoryConnector  connector object
     *
     * @throws PropertyServerException exception thrown if the repository connector
     */
    public void validateRepositoryConnector(OMRSRepositoryConnector   repositoryConnector,
                                            String                    methodName) throws PropertyServerException
    {
        if (repositoryConnector == null)
        {
            RepositoryHandlerErrorCode errorCode    = RepositoryHandlerErrorCode.OMRS_NOT_INITIALIZED;
            String                     errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(methodName);

            throw new PropertyServerException(errorCode.getHTTPErrorCode(),
                                              this.getClass().getName(),
                                              methodName,
                                              errorMessage,
                                              errorCode.getSystemAction(),
                                              errorCode.getUserAction());

        }

        if (! repositoryConnector.isActive())
        {
            RepositoryHandlerErrorCode errorCode    = RepositoryHandlerErrorCode.OMRS_NOT_AVAILABLE;
            String                     errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(methodName);

            throw new PropertyServerException(errorCode.getHTTPErrorCode(),
                                              this.getClass().getName(),
                                              methodName,
                                              errorMessage,
                                              errorCode.getSystemAction(),
                                              errorCode.getUserAction());
        }

        try
        {
            repositoryConnector.getMetadataCollection();
        }
        catch (Throwable error)
        {
            RepositoryHandlerErrorCode errorCode    = RepositoryHandlerErrorCode.NO_METADATA_COLLECTION;
            String                     errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(methodName);

            throw new PropertyServerException(errorCode.getHTTPErrorCode(),
                                              this.getClass().getName(),
                                              methodName,
                                              errorMessage,
                                              errorCode.getSystemAction(),
                                              errorCode.getUserAction());
        }
    }


    /**
     * Compare the retrieved properties with the validating properties supplied by the caller.
     *
     * @param instanceGUID unique identifier of the instance where the properties came from
     * @param validatingPropertyName name of property that should be in the entity if we have the correct one.
     * @param validatingProperty value of property that should be in the entity if we have the correct one.
     * @param retrievedProperties properties retrieved from the repository
     * @param methodName calling method
     *
     * @throws InvalidParameterException mismatch on properties
     */
    void    validateProperties(String               instanceGUID,
                               String               validatingPropertyName,
                               String               validatingProperty,
                               InstanceProperties   retrievedProperties,
                               String               methodName) throws InvalidParameterException
    {
        if ((validatingProperty != null) && (retrievedProperties != null))
        {
            Map<String, InstancePropertyValue> instancePropertyValueMap = retrievedProperties.getInstanceProperties();
            InstancePropertyValue retrievedPropertyValue = instancePropertyValueMap.get(validatingPropertyName);

            if (! validatingProperty.equals(retrievedPropertyValue.valueAsString()))
            {
                RepositoryHandlerErrorCode errorCode = RepositoryHandlerErrorCode.INVALID_PROPERTY;
                String errorMessage = errorCode.getErrorMessageId()
                                    + errorCode.getFormattedErrorMessage(validatingPropertyName,
                                                                         validatingProperty,
                                                                         methodName,
                                                                         retrievedPropertyValue.valueAsString(),
                                                                         instanceGUID);

                throw new InvalidParameterException(errorCode.getHTTPErrorCode(),
                                                    this.getClass().getName(),
                                                    methodName,
                                                    errorMessage,
                                                    errorCode.getSystemAction(),
                                                    errorCode.getUserAction(),
                                                    validatingPropertyName);
            }
        }
    }


    /**
     * Verify whether an instance is of a particular type or not.
     *
     * @param userId calling user
     * @param instanceHeader the entity or relationship header.
     * @param guidParameterName name of the parameter containing the guid.
     * @param expectedTypeName name of the type to test for
     * @param methodName calling method
     *
     * @return boolean flag
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem retrieving the entity.
     */
    void validateInstanceType(String         userId,
                              InstanceHeader instanceHeader,
                              String         guidParameterName,
                              String         expectedTypeName,
                              String         methodName) throws InvalidParameterException
    {
        if (instanceHeader != null)
        {
            InstanceType type = instanceHeader.getType();
            if (type != null)
            {
                if (! repositoryHelper.isTypeOf(methodName, type.getTypeDefName(), expectedTypeName))
                {
                    this.handleWrongTypeForGUIDException(instanceHeader.getGUID(),
                                                         methodName,
                                                         type.getTypeDefName(),
                                                         expectedTypeName);
                }
            }
        }
    }


    /**
     * Test whether an instance is of a particular type or not.
     *
     * @param instanceHeader the entity or relationship header.
     * @param entityTypeName name of the type to test for
     * @param methodName calling method
     *
     * @return boolean flag
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem retrieving the entity.
     */
    boolean isInstanceATypeOf(InstanceHeader         instanceHeader,
                              String                 entityTypeName,
                              String                 methodName) throws InvalidParameterException,
                                                                        UserNotAuthorizedException,
                                                                        PropertyServerException
    {
        if (instanceHeader != null)
        {
            InstanceType type = instanceHeader.getType();
            if (type != null)
            {
                return repositoryHelper.isTypeOf(methodName, type.getTypeDefName(), entityTypeName);
            }
        }

        return false;
    }

    /**
     * Throw an exception if the supplied guid returned an entity of the wrong type
     *
     * @param guid  unique identifier of entity
     * @param methodName  name of the method making the call.
     * @param actualType  type of retrieved entity
     * @param expectedType  type the entity should be
     * @throws InvalidParameterException the guid is for the wrong type of object
     */
    void handleWrongTypeForGUIDException(String guid,
                                         String methodName,
                                         String actualType,
                                         String expectedType) throws InvalidParameterException
    {
        final String  defaultGUIDParameterName = "guid";

        RepositoryHandlerErrorCode errorCode = RepositoryHandlerErrorCode.INSTANCE_WRONG_TYPE_FOR_GUID;
        String                    errorMessage = errorCode.getErrorMessageId()
                + errorCode.getFormattedErrorMessage(methodName,
                                                     guid,
                                                     actualType,
                                                     expectedType);

        throw new InvalidParameterException(errorCode.getHTTPErrorCode(),
                                            this.getClass().getName(),
                                            methodName,
                                            errorMessage,
                                            errorCode.getSystemAction(),
                                            errorCode.getUserAction(),
                                            defaultGUIDParameterName);

    }

    /**
     * Throw an exception if the supplied userId is not authorized to perform a request
     *
     * @param userId  user name to validate
     * @param methodName  name of the method making the call.
     *
     * @throws UserNotAuthorizedException the userId is unauthorised for the request
     */
    public void handleUnauthorizedUser(String userId,
                                String methodName) throws UserNotAuthorizedException
    {
        RepositoryHandlerErrorCode errorCode = RepositoryHandlerErrorCode.USER_NOT_AUTHORIZED;
        String                     errorMessage = errorCode.getErrorMessageId()
                                                + errorCode.getFormattedErrorMessage(userId,
                                                                                     methodName,
                                                                                     serviceName,
                                                                                     serverName);

        throw new UserNotAuthorizedException(errorCode.getHTTPErrorCode(),
                                             this.getClass().getName(),
                                             methodName,
                                             errorMessage,
                                             errorCode.getSystemAction(),
                                             errorCode.getUserAction(),
                                             userId);

    }


    /**
     * Throw an exception if the supplied userId is not authorized to perform a request
     *
     * @param error  caught exception
     * @param methodName  name of the method making the call
     * @param propertyName  name of the property in error
     *
     * @throws InvalidParameterException invalid property
     */
    public void handleUnsupportedProperty(Throwable  error,
                                          String     methodName,
                                          String     propertyName) throws InvalidParameterException
    {
        RepositoryHandlerErrorCode errorCode = RepositoryHandlerErrorCode.INVALID_PROPERTY;
        String                     errorMessage = errorCode.getErrorMessageId()
                                                + errorCode.getFormattedErrorMessage(propertyName,
                                                                                     methodName,
                                                                                     serviceName,
                                                                                     serverName,
                                                                                     error.getMessage());

        throw new InvalidParameterException(errorCode.getHTTPErrorCode(),
                                            this.getClass().getName(),
                                            methodName,
                                            errorMessage,
                                            errorCode.getSystemAction(),
                                            errorCode.getUserAction(),
                                            error,
                                            propertyName);

    }



    /**
     * Throw an exception if an unexpected repository error is received
     *
     * @param error  caught exception
     * @param methodName  name of the method making the call.
     *
     * @throws PropertyServerException unexpected exception from property server
     */
    public void handleRepositoryError(Throwable  error,
                                      String     methodName) throws PropertyServerException
    {
        RepositoryHandlerErrorCode errorCode = RepositoryHandlerErrorCode.PROPERTY_SERVER_ERROR;
        String                     errorMessage = errorCode.getErrorMessageId()
                                                + errorCode.getFormattedErrorMessage(error.getMessage(),
                                                                                     methodName,
                                                                                     serviceName,
                                                                                     serverName);

        throw new PropertyServerException(errorCode.getHTTPErrorCode(),
                                          this.getClass().getName(),
                                          methodName,
                                          errorMessage,
                                          errorCode.getSystemAction(),
                                          errorCode.getUserAction());

    }


    /**
     * Throw an exception if there is a problem with the asset guid
     *
     * @param error  caught exception
     * @param entityGUID  unique identifier for the requested entity
     * @param entityTypeName expected type of asset
     * @param methodName  name of the method making the call
     * @param guidParameterName name of the parameter that passed the GUID.
     *
     * @throws InvalidParameterException unexpected exception from property server
     */
    public void handleUnknownEntity(Throwable  error,
                                    String     entityGUID,
                                    String     entityTypeName,
                                    String     methodName,
                                    String     guidParameterName) throws InvalidParameterException
    {
        RepositoryHandlerErrorCode errorCode = RepositoryHandlerErrorCode.UNKNOWN_ENTITY;
        String                     errorMessage = errorCode.getErrorMessageId()
                                                + errorCode.getFormattedErrorMessage(entityTypeName,
                                                                                     entityGUID,
                                                                                     methodName,
                                                                                     serviceName,
                                                                                     serverName,
                                                                                     error.getMessage());

        throw new InvalidParameterException(errorCode.getHTTPErrorCode(),
                                            this.getClass().getName(),
                                            methodName,
                                            errorMessage,
                                            errorCode.getSystemAction(),
                                            errorCode.getUserAction(),
                                            error,
                                            guidParameterName);

    }



    /**
     * Throw an exception if there is a problem with the asset guid
     *
     * @param error  caught exception
     * @param entityGUID  unique identifier for the requested entity
     * @param entityTypeName expected type of asset
     * @param methodName  name of the method making the call
     * @param guidParameterName name of the parameter that passed the GUID.
     *
     * @throws InvalidParameterException unexpected exception from property server
     */
    public void handleEntityProxy(Throwable  error,
                                  String     entityGUID,
                                  String     entityTypeName,
                                  String     methodName,
                                  String     guidParameterName) throws InvalidParameterException
    {
        RepositoryHandlerErrorCode errorCode = RepositoryHandlerErrorCode.PROXY_ENTITY_FOUND;
        String                     errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(entityTypeName,
                                                                                                                     entityGUID,
                                                                                                                     serverName,
                                                                                                                     error.getMessage());

        throw new InvalidParameterException(errorCode.getHTTPErrorCode(),
                                            this.getClass().getName(),
                                            methodName,
                                            errorMessage,
                                            errorCode.getSystemAction(),
                                            errorCode.getUserAction(),
                                            error,
                                            guidParameterName);

    }



    /**
     * Throw an exception if multiple relationships are returned when not expected.
     *
     * @param entityGUID  unique identifier for the anchor entity
     * @param entityTypeName  name of the entity's type
     * @param relationshipTypeName expected type of relationship
     * @param returnedRelationships list of relationships returned
     * @param methodName  name of the method making the call
     *
     * @throws PropertyServerException unexpected response from property server
     */
    public void handleAmbiguousRelationships(String             entityGUID,
                                             String             entityTypeName,
                                             String             relationshipTypeName,
                                             List<Relationship> returnedRelationships,
                                             String             methodName) throws PropertyServerException
    {
        List<String>  relationshipGUIDs = new ArrayList<>();

        if (returnedRelationships != null)
        {
            for (Relationship  relationship: returnedRelationships)
            {
                if (relationship != null)
                {
                    relationshipGUIDs.add(relationship.getGUID());
                }
            }
        }

        RepositoryHandlerErrorCode errorCode = RepositoryHandlerErrorCode.MULTIPLE_RELATIONSHIPS_FOUND;
        String                     errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(relationshipTypeName,
                                                                                                                     entityTypeName,
                                                                                                                     entityGUID,
                                                                                                                     relationshipGUIDs.toString(),
                                                                                                                     methodName,
                                                                                                                     serverName);

        throw new PropertyServerException(errorCode.getHTTPErrorCode(),
                                          this.getClass().getName(),
                                          methodName,
                                          errorMessage,
                                          errorCode.getSystemAction(),
                                          errorCode.getUserAction());

    }


    /**
     * Throw an exception if multiple relationships are returned when not expected.
     *
     * @param name  requested name for the entity
     * @param nameParameterName  name of hte parameter
     * @param entityTypeName  name of the entity's type
     * @param returnedEntities list of entities returned
     * @param methodName  name of the method making the call
     *
     * @throws PropertyServerException unexpected response from property server
     */
    public void handleAmbiguousEntityName(String             name,
                                          String             nameParameterName,
                                          String             entityTypeName,
                                          List<EntityDetail> returnedEntities,
                                          String             methodName) throws PropertyServerException
    {
        List<String>  entityGUIDs = new ArrayList<>();

        if (returnedEntities != null)
        {
            for (EntityDetail  entity: returnedEntities)
            {
                if (entity != null)
                {
                    entityGUIDs.add(entity.getGUID());
                }
            }
        }

        RepositoryHandlerErrorCode errorCode = RepositoryHandlerErrorCode.MULTIPLE_ENTITIES_FOUND;
        String                   errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(entityTypeName,
                                                                                                                   name,
                                                                                                                   entityGUIDs.toString(),
                                                                                                                   methodName,
                                                                                                                   nameParameterName,
                                                                                                                   serverName);

        throw new PropertyServerException(errorCode.getHTTPErrorCode(),
                                          this.getClass().getName(),
                                          methodName,
                                          errorMessage,
                                          errorCode.getSystemAction(),
                                          errorCode.getUserAction());

    }


    /**
     * Throw an exception if no relationships are returned when not expected.
     *
     * @param entityGUID  unique identifier for the anchor entity
     * @param entityTypeName  name of the entity's type
     * @param relationshipTypeName expected type of relationship
     * @param methodName  name of the method making the call
     *
     * @throws PropertyServerException unexpected response from property server
     */
    public void handleNoRelationship(String             entityGUID,
                                     String             entityTypeName,
                                     String             relationshipTypeName,
                                     String             methodName) throws PropertyServerException
    {
        RepositoryHandlerErrorCode errorCode = RepositoryHandlerErrorCode.NO_RELATIONSHIPS_FOUND;
        String                   errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(relationshipTypeName,
                                                                                                                   entityTypeName,
                                                                                                                   entityGUID,
                                                                                                                   methodName,
                                                                                                                   serverName);

        throw new PropertyServerException(errorCode.getHTTPErrorCode(),
                                          this.getClass().getName(),
                                          methodName,
                                          errorMessage,
                                          errorCode.getSystemAction(),
                                          errorCode.getUserAction());

    }



    /**
     * Throw an exception if it is not possible to update an entity.
     *
     * @param entityTypeGUID  unique identifier for the entity's type
     * @param entityTypeName  name of the entity's type
     * @param properties properties
     * @param methodName  name of the method making the call
     *
     * @throws PropertyServerException unexpected response from property server
     */
    public void handleNoEntity(String             entityTypeGUID,
                               String             entityTypeName,
                               InstanceProperties properties,
                               String             methodName) throws PropertyServerException
    {
        RepositoryHandlerErrorCode errorCode = RepositoryHandlerErrorCode.NULL_ENTITY_RETURNED;
        String                   errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(methodName,
                                                                                                                   serverName,
                                                                                                                   entityTypeName,
                                                                                                                   entityTypeGUID,
                                                                                                                   properties.toString());

        throw new PropertyServerException(errorCode.getHTTPErrorCode(),
                                          this.getClass().getName(),
                                          methodName,
                                          errorMessage,
                                          errorCode.getSystemAction(),
                                          errorCode.getUserAction());

    }



    /**
     * Throw an exception if it is not possible to update an entity.
     *
     * @param entityGUID unique identifier of entity
     * @param classificationTypeGUID  unique identifier for the classification's type
     * @param classificationTypeName  name of the classification's type
     * @param properties properties
     * @param methodName  name of the method making the call
     *
     * @throws PropertyServerException unexpected response from property server
     */
    public void handleNoEntityForClassification(String             entityGUID,
                                                String             classificationTypeGUID,
                                                String             classificationTypeName,
                                                InstanceProperties properties,
                                                String             methodName) throws PropertyServerException
    {
        RepositoryHandlerErrorCode errorCode = RepositoryHandlerErrorCode.NULL_ENTITY_RETURNED_FOR_CLASSIFICATION;
        String                   errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(methodName,
                                                                                                                   serverName,
                                                                                                                   entityGUID,
                                                                                                                   classificationTypeName,
                                                                                                                   classificationTypeGUID,
                                                                                                                   properties.toString());

        throw new PropertyServerException(errorCode.getHTTPErrorCode(),
                                          this.getClass().getName(),
                                          methodName,
                                          errorMessage,
                                          errorCode.getSystemAction(),
                                          errorCode.getUserAction());

    }
}
