/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataengine.server.handlers;


import org.odpi.openmetadata.accessservices.dataengine.ffdc.DataEngineErrorCode;
import org.odpi.openmetadata.accessservices.dataengine.model.PortType;
import org.odpi.openmetadata.accessservices.dataengine.server.builders.PortPropertiesBuilder;
import org.odpi.openmetadata.accessservices.dataengine.server.mappers.PortPropertiesMapper;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryHandler;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EnumPropertyValue;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;


/**
 * PortHandler manages Port objects from the property server. It runs server-side in the DataEngine OMAS
 * and creates port entities with wire relationships through the OMRSRepositoryConnector.
 */
public class PortHandler {
    private final String serviceName;
    private final String serverName;
    private final RepositoryHandler repositoryHandler;
    private final OMRSRepositoryHelper repositoryHelper;
    private final InvalidParameterHandler invalidParameterHandler;

    /**
     * Construct the handler information needed to interact with the repository services
     *
     * @param serviceName             name of this service
     * @param serverName              name of the local server
     * @param invalidParameterHandler handler for managing parameter errors
     * @param repositoryHandler       manages calls to the repository services
     * @param repositoryHelper        provides utilities for manipulating the repository services objects
     */
    public PortHandler(String serviceName, String serverName, InvalidParameterHandler invalidParameterHandler,
                       RepositoryHandler repositoryHandler, OMRSRepositoryHelper repositoryHelper) {
        this.serviceName = serviceName;
        this.serverName = serverName;
        this.invalidParameterHandler = invalidParameterHandler;
        this.repositoryHelper = repositoryHelper;
        this.repositoryHandler = repositoryHandler;
    }

    /**
     * Create the port implementation
     *
     * @param userId        the name of the calling user
     * @param qualifiedName the qualifiedName name of the port
     * @param displayName   the display name of the port
     * @param portType      the type of the port
     *
     * @return unique identifier of the port implementation in the repository
     *
     * @throws InvalidParameterException the bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException problem accessing the property server
     */
    public String createPortImplementation(String userId, String qualifiedName, String displayName,
                                           PortType portType) throws InvalidParameterException,
                                                                     UserNotAuthorizedException,
                                                                     PropertyServerException {
        return createPort(userId, qualifiedName, displayName, portType,
                PortPropertiesMapper.PORT_IMPLEMENTATION_TYPE_NAME);
    }

    /**
     * Create the port alias
     *
     * @param userId        the name of the calling user
     * @param qualifiedName the qualifiedName name of the port
     * @param displayName   the display name of the port
     * @param portType      the type of the port
     *
     * @return unique identifier of the port alias in the repository
     *
     * @throws InvalidParameterException the bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException problem accessing the property server
     */
    public String createPortAlias(String userId, String qualifiedName, String displayName, PortType portType) throws
                                                                                                              InvalidParameterException,
                                                                                                              UserNotAuthorizedException,
                                                                                                              PropertyServerException {
        return createPort(userId, qualifiedName, displayName, portType, PortPropertiesMapper.PORT_ALIAS_TYPE_NAME);
    }

    /**
     * Create the port implementation
     *
     * @param userId        the name of the calling user
     * @param qualifiedName the qualifiedName name of the port
     * @param displayName   the display name of the port
     * @param portType      the type of the port
     *
     * @throws InvalidParameterException the bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException problem accessing the property server
     */
    public void updatePortImplementation(String userId, String portGUID, String qualifiedName, String displayName,
                                         PortType portType) throws InvalidParameterException,
                                                                   UserNotAuthorizedException,
                                                                   PropertyServerException {
        updatePort(userId, portGUID, qualifiedName, displayName, portType,
                PortPropertiesMapper.PORT_IMPLEMENTATION_TYPE_NAME);
    }

    /**
     * Update the port alias
     *
     * @param userId        the name of the calling user
     * @param qualifiedName the qualifiedName name of the port
     * @param displayName   the display name of the port
     * @param portType      the type of the port
     *
     * @throws InvalidParameterException the bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException problem accessing the property server
     */
    public void updatePortAlias(String userId, String portGUID, String qualifiedName, String displayName,
                                PortType portType) throws InvalidParameterException, UserNotAuthorizedException,
                                                          PropertyServerException {
        updatePort(userId, portGUID, qualifiedName, displayName, portType, PortPropertiesMapper.PORT_ALIAS_TYPE_NAME);
    }


    /**
     * Create the port
     *
     * @param userId        the name of the calling user
     * @param qualifiedName the qualifiedName name of the port
     * @param displayName   the display name of the port
     * @param portType      the type of the port
     * @param entityTpeName the type name
     *
     * @return unique identifier of the port in the repository
     *
     * @throws InvalidParameterException the bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException problem accessing the property server
     */
    private String createPort(String userId, String qualifiedName, String displayName, PortType portType,
                              String entityTpeName) throws InvalidParameterException, UserNotAuthorizedException,
                                                           PropertyServerException {
        final String methodName = "createPort";

        validatePortParameters(userId, qualifiedName, displayName, methodName);

        InstanceProperties properties = buildPortInstanceProperties(qualifiedName, displayName, portType,
                methodName);

        TypeDef entityTypeDef = repositoryHelper.getTypeDefByName(userId, entityTpeName);
        return repositoryHandler.createEntity(userId, entityTypeDef.getGUID(), entityTypeDef.getName(), properties,
                methodName);
    }

    /**
     * Update the port
     *
     * @param userId         the name of the calling user
     * @param portGUID       the guid of the port to be updated
     * @param qualifiedName  the qualifiedName name of the port
     * @param displayName    the display name of the port
     * @param portType       the type of the port
     * @param entityTypeName the type name
     *
     * @throws InvalidParameterException the bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException problem accessing the property server
     */
    private void updatePort(String userId, String portGUID, String qualifiedName, String displayName, PortType portType,
                            String entityTypeName) throws InvalidParameterException, UserNotAuthorizedException,
                                                          PropertyServerException {
        final String methodName = "updatePort";

        validatePortParameters(userId, qualifiedName, displayName, methodName);

        InstanceProperties properties = buildPortInstanceProperties(qualifiedName, displayName, portType,
                methodName);

        TypeDef entityTypeDef = repositoryHelper.getTypeDefByName(userId, entityTypeName);
        repositoryHandler.updateEntity(userId, portGUID, entityTypeDef.getGUID(), entityTypeDef.getName(), properties,
                methodName);
    }

    /**
     * Create a PortSchema relationship between a Port and the corresponding SchemaType
     *
     * @param userId         the name of the calling user
     * @param portGUID       the unique identifier of the port
     * @param schemaTypeGUID the unique identifier of the schema type
     *
     * @throws InvalidParameterException the bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException problem accessing the property server
     */
    public void addPortSchemaRelationship(String userId, String portGUID, String schemaTypeGUID) throws
                                                                                                 InvalidParameterException,
                                                                                                 UserNotAuthorizedException,
                                                                                                 PropertyServerException {
        final String methodName = "addPortSchemaRelationship";

        validateRelationshipParameters(userId, portGUID, schemaTypeGUID, methodName);

        Relationship relationship = repositoryHandler.getRelationshipBetweenEntities(userId, portGUID,
                PortPropertiesMapper.PORT_TYPE_NAME, schemaTypeGUID, PortPropertiesMapper.PORT_SCHEMA_TYPE_GUID,
                PortPropertiesMapper.PORT_SCHEMA_TYPE_NAME, methodName);

        if (relationship == null) {
            repositoryHandler.createRelationship(userId, PortPropertiesMapper.PORT_SCHEMA_TYPE_GUID, portGUID,
                    schemaTypeGUID, null, methodName);
        }
    }


    /**
     * Create a PortDelegation relationship between two ports. Verifies that the
     * relationship is not present before creating it
     *
     * @param userId      the name of the calling user
     * @param portGUID    the unique identifier of the source port
     * @param portType    the type of the source port
     * @param delegatesTo the unique identifier of the target port
     *
     * @throws InvalidParameterException the bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException problem accessing the property server
     */
    public void addPortDelegationRelationship(String userId, String portGUID, PortType portType,
                                              String delegatesTo) throws InvalidParameterException,
                                                                         UserNotAuthorizedException,
                                                                         PropertyServerException {
        final String methodName = "addPortDelegationRelationship";

        validateRelationshipParameters(userId, portGUID, delegatesTo, methodName);

        EntityDetail delegatedPort = getPortEntityDetailByQualifiedName(userId, delegatesTo);
        String delegatedPortType = getPortType(delegatedPort);

        if (delegatedPortType.equalsIgnoreCase(portType.getName())) {
            Relationship relationship = repositoryHandler.getRelationshipBetweenEntities(userId, portGUID,
                    PortPropertiesMapper.PORT_TYPE_NAME, delegatesTo, PortPropertiesMapper.PORT_DELEGATION_TYPE_GUID,
                    PortPropertiesMapper.PORT_DELEGATION_TYPE_NAME, methodName);

            if (relationship == null) {
                repositoryHandler.createRelationship(userId, PortPropertiesMapper.PORT_DELEGATION_TYPE_GUID, portGUID,
                        delegatesTo, null, methodName);
            }
        } else {
            throwInvalidParameterException(portGUID, methodName, delegatedPort, delegatedPortType);
        }
    }

    /**
     * Remove the port
     *
     * @param userId         the name of the calling user
     * @param portGUID       the unique identifier of the port to be removed
     * @param entityTypeName the type name
     *
     * @throws InvalidParameterException the bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException problem accessing the property server
     */
    public void removePortEntity(String userId, String portGUID, String entityTypeName) throws
                                                                                        InvalidParameterException,
                                                                                        PropertyServerException,
                                                                                        UserNotAuthorizedException {
        final String methodName = "removePortEntity";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(portGUID, PortPropertiesMapper.GUID_PROPERTY_NAME, methodName);

        TypeDef entityTypeDef = repositoryHelper.getTypeDefByName(userId, entityTypeName);
        repositoryHandler.removeEntity(userId, portGUID, entityTypeDef.getGUID(), entityTypeDef.getName(),
                null, null, methodName);
    }

    /**
     * Find out if the Port object is already stored in the repository. It uses the fully qualified name
     * to retrieve the entity
     *
     * @param userId         the name of the calling user
     * @param qualifiedName  the qualifiedName name of the process to be searched
     * @param entityTypeName the type name
     *
     * @return unique identifier of the process or null
     *
     * @throws InvalidParameterException the bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException problem accessing the property server
     */
    public String findPort(String userId, String qualifiedName, String entityTypeName) throws InvalidParameterException,
                                                                                              UserNotAuthorizedException,
                                                                                              PropertyServerException {
        final String methodName = "findPort";

        invalidParameterHandler.validateUserId(userId, methodName);

        qualifiedName = repositoryHelper.getExactMatchRegex(qualifiedName);

        InstanceProperties properties = repositoryHelper.addStringPropertyToInstance(serviceName, null,
                PortPropertiesMapper.QUALIFIED_NAME_PROPERTY_NAME, qualifiedName, methodName);

        TypeDef entityTypeDef = repositoryHelper.getTypeDefByName(userId, entityTypeName);
        EntityDetail retrievedEntity = repositoryHandler.getUniqueEntityByName(userId, qualifiedName,
                PortPropertiesMapper.QUALIFIED_NAME_PROPERTY_NAME, properties, entityTypeDef.getGUID(),
                entityTypeDef.getName(), methodName);

        if (retrievedEntity == null) {
            return null;
        }

        return retrievedEntity.getGUID();
    }

    private void validateRelationshipParameters(String userId, String firstGUID, String secondGUID,
                                                String methodName) throws InvalidParameterException {

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(firstGUID, PortPropertiesMapper.GUID_PROPERTY_NAME, methodName);
        invalidParameterHandler.validateGUID(secondGUID, PortPropertiesMapper.GUID_PROPERTY_NAME, methodName);
    }

    private EntityDetail getPortEntityDetailByQualifiedName(String userId, String qualifiedName) throws
                                                                                                 InvalidParameterException,
                                                                                                 UserNotAuthorizedException,
                                                                                                 PropertyServerException {
        final String methodName = "getPortEntityDetailByQualifiedName";

        qualifiedName = repositoryHelper.getExactMatchRegex(qualifiedName);

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(qualifiedName, PortPropertiesMapper.QUALIFIED_NAME_PROPERTY_NAME,
                methodName);


        InstanceProperties properties = repositoryHelper.addStringPropertyToInstance(serviceName, null,
                PortPropertiesMapper.QUALIFIED_NAME_PROPERTY_NAME, qualifiedName, methodName);

        return repositoryHandler.getUniqueEntityByName(userId, qualifiedName,
                PortPropertiesMapper.QUALIFIED_NAME_PROPERTY_NAME, properties, PortPropertiesMapper.PORT_TYPE_GUID,
                PortPropertiesMapper.PORT_TYPE_NAME, methodName);
    }

    private String getPortType(EntityDetail delegatedPort) {
        if (delegatedPort == null) {
            return null;
        }

        InstanceProperties instanceProperties = delegatedPort.getProperties();
        if (instanceProperties == null) {
            return null;
        }

        EnumPropertyValue portTypeValue =
                (EnumPropertyValue) delegatedPort.getProperties().getPropertyValue(PortPropertiesMapper.PORT_TYPE_PROPERTY_NAME);
        if (portTypeValue == null) {
            return null;
        }

        return portTypeValue.getSymbolicName();
    }

    private InstanceProperties buildPortInstanceProperties(String qualifiedName, String displayName,
                                                           PortType portType, String methodName) throws
                                                                                                 InvalidParameterException {
        PortPropertiesBuilder builder = new PortPropertiesBuilder(qualifiedName, displayName, portType,
                repositoryHelper,
                serviceName, serverName);
        return builder.getInstanceProperties(methodName);
    }

    private void validatePortParameters(String userId, String qualifiedName, String displayName, String methodName) throws
                                                                                                                    InvalidParameterException {
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(displayName, PortPropertiesMapper.DISPLAY_NAME_PROPERTY_NAME,
                methodName);
        invalidParameterHandler.validateName(qualifiedName, PortPropertiesMapper.QUALIFIED_NAME_PROPERTY_NAME,
                methodName);
    }

    private void throwInvalidParameterException(String portGUID, String methodName, EntityDetail delegatedPort,
                                                String delegatedPortType) throws InvalidParameterException {
        DataEngineErrorCode errorCode = DataEngineErrorCode.INVALID_PORT_TYPE;
        String errorMessage = errorCode.getErrorMessageId()
                + errorCode.getFormattedErrorMessage(delegatedPort.getGUID(), portGUID);

        throw new InvalidParameterException(errorCode.getHTTPErrorCode(), this.getClass().getName(), methodName,
                errorMessage, errorCode.getSystemAction(), errorCode.getUserAction(), delegatedPortType);
    }
}