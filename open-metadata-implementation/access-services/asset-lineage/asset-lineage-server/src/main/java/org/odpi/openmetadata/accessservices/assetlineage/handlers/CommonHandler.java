/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetlineage.handlers;

import org.odpi.openmetadata.accessservices.assetlineage.model.AssetContext;
import org.odpi.openmetadata.accessservices.assetlineage.model.GraphContext;
import org.odpi.openmetadata.accessservices.assetlineage.model.LineageEntity;
import org.odpi.openmetadata.accessservices.assetlineage.util.Converter;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryHandler;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * The common handler provide common methods that is generic and reusable for other handlers.
 */
public class CommonHandler {

    private static final Logger log = LoggerFactory.getLogger(CommonHandler.class);

    private static final String GUID_PARAMETER = "guid";
    private String serviceName;
    private String serverName;
    private RepositoryHandler repositoryHandler;
    private OMRSRepositoryHelper repositoryHelper;
    private InvalidParameterHandler invalidParameterHandler;

    /**
     * Construct the discovery engine configuration handler caching the objects
     * needed to operate within a single server instance.
     *
     * @param serviceName             name of the consuming service
     * @param serverName              name of this server instance
     * @param invalidParameterHandler handler for invalid parameters
     * @param repositoryHelper        helper used by the converters
     * @param repositoryHandler       handler for calling the repository services
     */
    public CommonHandler(String serviceName,
                         String serverName,
                         InvalidParameterHandler invalidParameterHandler,
                         OMRSRepositoryHelper repositoryHelper,
                         RepositoryHandler repositoryHandler) {
        this.serviceName = serviceName;
        this.serverName = serverName;
        this.invalidParameterHandler = invalidParameterHandler;
        this.repositoryHelper = repositoryHelper;
        this.repositoryHandler = repositoryHandler;
    }


    /**
     * Query about the entity in the repositories based on the Guid
     *
     * @param userId   String - userId of user making request.
     * @param guid     guid of the asset we need to retrieve from a repository
     * @param typeName the name of the Open Metadata type for getting details
     * @return optional with entity details if found, empty optional if not found
     * @throws InvalidParameterException  the invalid parameter exception
     * @throws PropertyServerException    the property server exception
     * @throws UserNotAuthorizedException the user not authorized exception
     */
    public Optional<EntityDetail> getEntityDetails(String userId, String guid, String typeName) throws InvalidParameterException,
            PropertyServerException,
            UserNotAuthorizedException {

        String methodName = "getEntityDetails";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(guid, GUID_PARAMETER, methodName);

        return Optional.ofNullable(repositoryHandler.getEntityByGUID(userId, guid, GUID_PARAMETER, typeName, methodName));
    }

    /**
     * Query about the relationships of an entity based on the type of the relationship
     *
     * @param userId               String - userId of user making request.
     * @param assetGuid            guid of the asset we need to retrieve the relationships
     * @param relationshipTypeName the type of the relationship
     * @param entityTypeName       the type of the entity
     * @return List of the relationships if found, empty list if not found
     * @throws UserNotAuthorizedException the user not authorized exception
     * @throws PropertyServerException    the property server exception
     * @throws InvalidParameterException  the invalid parameter exception
     */
    public List<Relationship> getRelationshipsByType(String userId, String assetGuid,
                                                     String relationshipTypeName, String entityTypeName) throws
            UserNotAuthorizedException, PropertyServerException, InvalidParameterException {

        final String methodName = "getRelationshipsByType";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(assetGuid, GUID_PARAMETER, methodName);

        String typeGuid = getTypeName(userId, relationshipTypeName);

        List<Relationship> relationships = repositoryHandler.getRelationshipsByType(userId,
                assetGuid,
                entityTypeName,
                typeGuid,
                relationshipTypeName,
                methodName);

        if (relationships != null) {
            return relationships;
        }

        return new ArrayList<>();
    }

    /**
     * Retrieves guid for a specific type
     *
     * @param userId      String - userId of user making request.
     * @param typeDefName type of the Entity
     * @return Guid of the type if found, null String if not found
     */
    public String getTypeName(String userId, String typeDefName) {
        final TypeDef typeDefByName = repositoryHelper.getTypeDefByName(userId, typeDefName);

        if (typeDefByName != null) {
            return typeDefByName.getGUID();
        }
        return null;
    }


    /**
     * Gets entity at the end.
     *
     * @param userId           the user id
     * @param entityDetailGUID the entity detail guid
     * @param relationship     the relationship
     * @return the entity at the end
     * @throws InvalidParameterException  the invalid parameter exception
     * @throws PropertyServerException    the property server exception
     * @throws UserNotAuthorizedException the user not authorized exception
     */
    public EntityDetail getEntityAtTheEnd(String userId, String entityDetailGUID, Relationship relationship) throws InvalidParameterException,
            PropertyServerException,
            UserNotAuthorizedException {

        String methodName = "getEntityAtTheEnd";

        if (relationship.getEntityOneProxy().getGUID().equals(entityDetailGUID)) {
            return repositoryHandler.getEntityByGUID(userId,
                    relationship.getEntityTwoProxy().getGUID(),
                    GUID_PARAMETER,
                    relationship.getEntityTwoProxy().getType().getTypeDefName(), methodName);
        } else {
            return repositoryHandler.getEntityByGUID(userId,
                    relationship.getEntityOneProxy().getGUID(),
                    GUID_PARAMETER,
                    relationship.getEntityOneProxy().getType().getTypeDefName(), methodName);
        }
    }

    /**
     * Adds entities and relationships for the process Context structure
     *
     * @param userId       String - userId of user making request.
     * @param startEntity  parent entity of the relationship
     * @param relationship the relationship of the parent node
     * @param graph        the graph
     * @return Entity which is the child of the relationship, null if there is no Entity
     * @throws InvalidParameterException  the invalid parameter exception
     * @throws PropertyServerException    the property server exception
     * @throws UserNotAuthorizedException the user not authorized exception
     */
    protected EntityDetail buildGraphEdgeByRelationship(String userId, EntityDetail startEntity,
                                                        Relationship relationship, AssetContext graph) throws InvalidParameterException,
            PropertyServerException,
            UserNotAuthorizedException {

        Converter converter = new Converter();
        EntityDetail endEntity = getEntityAtTheEnd(userId, startEntity.getGUID(), relationship);

        if (endEntity == null) return null;

        LineageEntity startVertex = converter.createLineageEntity(startEntity);
        LineageEntity endVertex = converter.createLineageEntity(endEntity);

        graph.addVertex(startVertex);
        graph.addVertex(endVertex);

        GraphContext edge = new GraphContext(relationship.getType().getTypeDefName(), relationship.getGUID(), startVertex, endVertex);
        graph.addEdge(edge);

        return endEntity;
    }
}
