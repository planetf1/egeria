/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetlineage.handlers;

import org.odpi.openmetadata.accessservices.assetlineage.ffdc.exception.AssetLineageException;
import org.odpi.openmetadata.accessservices.assetlineage.model.AssetContext;
import org.odpi.openmetadata.accessservices.assetlineage.model.GraphContext;
import org.odpi.openmetadata.accessservices.assetlineage.util.SuperTypesRetriever;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryHandler;
import org.odpi.openmetadata.frameworks.connectors.ffdc.OCFCheckedExceptionBase;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

import static org.odpi.openmetadata.accessservices.assetlineage.ffdc.AssetLineageErrorCode.RELATIONSHIP_NOT_FOUND;
import static org.odpi.openmetadata.accessservices.assetlineage.util.AssetLineageConstants.*;

/**
 * The process context handler provides methods to build lineage context from processes.
 */
public class ProcessContextHandler {

    private static final Logger log = LoggerFactory.getLogger(ProcessContextHandler.class);

    private final AssetContextHandler assetContextHandler;
    private final InvalidParameterHandler invalidParameterHandler;
    private final List<String> supportedZones;
    private final HandlerHelper handlerHelper;
    private SuperTypesRetriever superTypesRetriever;

    private AssetContext graph;

    /**
     * Construct the discovery engine configuration handler caching the objects
     * needed to operate within a single server instance.
     *
     * @param invalidParameterHandler handler for invalid parameters
     * @param repositoryHelper        helper used by the converters
     * @param repositoryHandler       handler for calling the repository services
     * @param supportedZones          configurable list of zones that Asset Lineage is allowed to retrieve Assets from
     */
    public ProcessContextHandler(InvalidParameterHandler invalidParameterHandler,
                                 OMRSRepositoryHelper repositoryHelper,
                                 RepositoryHandler repositoryHandler,
                                 AssetContextHandler assetContextHandler,
                                 List<String> supportedZones,
                                 List<String> lineageClassificationTypes) {
        this.invalidParameterHandler = invalidParameterHandler;
        this.handlerHelper = new HandlerHelper(invalidParameterHandler, repositoryHelper, repositoryHandler, lineageClassificationTypes);
        this.assetContextHandler = assetContextHandler;
        this.supportedZones = supportedZones;
        this.superTypesRetriever = new SuperTypesRetriever(repositoryHelper);
    }

    /**
     * Retrieves the full context for a Process
     *
     * @param userId  String - userId of user making request.
     * @param process the asset that has been created
     * @return Map of the relationships between the Entities that are relevant to a Process
     */
    public Map<String, Set<GraphContext>> getProcessContext(String userId, EntityDetail process) throws OCFCheckedExceptionBase {

        final String methodName = "getProcessContext";

        graph = new AssetContext();

        invalidParameterHandler.validateAssetInSupportedZone(process.getGUID(),
                GUID_PARAMETER,
                handlerHelper.getAssetZoneMembership(process.getClassifications()),
                supportedZones,
                ASSET_LINEAGE_OMAS,
                methodName);

        return checkIfAllRelationshipsExist(userId, process);
    }

    private Map<String, Set<GraphContext>> checkIfAllRelationshipsExist(String userId, EntityDetail entityDetail) throws OCFCheckedExceptionBase {

        boolean entitiesTillLastRelationshipExist = hasEntitiesLinkedWithProcessPort(userId, entityDetail);
        if (entitiesTillLastRelationshipExist) {
            return graph.getNeighbors();
        }

        log.error("Some relationships are missing for the entity with guid {}", entityDetail.getGUID());

        throw new AssetLineageException(RELATIONSHIP_NOT_FOUND.getHTTPErrorCode(),
                this.getClass().getName(),
                "Retrieving Relationships",
                RELATIONSHIP_NOT_FOUND.getErrorMessage(),
                RELATIONSHIP_NOT_FOUND.getSystemAction(),
                RELATIONSHIP_NOT_FOUND.getUserAction());
    }

    private boolean hasEntitiesLinkedWithProcessPort(String userId, EntityDetail entityDetail) throws OCFCheckedExceptionBase {

        List<EntityDetail> entityDetails = getRelationshipsBetweenEntities(userId, entityDetail, PROCESS_PORT);

        if (entityDetails.isEmpty()) {
            log.error("No relationships Process Port has been found for the entity with guid {}", entityDetail.getGUID());

            throw new AssetLineageException(RELATIONSHIP_NOT_FOUND.getHTTPErrorCode(),
                    this.getClass().getName(),
                    "Retrieving Relationship",
                    RELATIONSHIP_NOT_FOUND.getErrorMessage(),
                    RELATIONSHIP_NOT_FOUND.getSystemAction(),
                    RELATIONSHIP_NOT_FOUND.getUserAction());
        }

        return hasRelationshipBasedOnType(entityDetails, userId);
    }


    /**
     * Retrieves the relationships of an Entity
     *
     * @param userId           String - userId of user making request.
     * @param startEntity      parent entity
     * @param relationshipType type of the relationship
     * @return List of entities that are on the other end of the relationship, empty list if none
     */
    private List<EntityDetail> getRelationshipsBetweenEntities(String userId, EntityDetail startEntity, String relationshipType) throws OCFCheckedExceptionBase {
        if (startEntity == null) return Collections.emptyList();

        handlerHelper.addLineageClassificationToContext(startEntity, graph);
        String startEntityType = startEntity.getType().getTypeDefName();

        List<Relationship> relationships = handlerHelper.getRelationshipsByType(userId, startEntity.getGUID(), relationshipType, startEntityType);
        List<EntityDetail> entityDetails = new ArrayList<>();
        for (Relationship relationship : relationships) {
            EntityDetail endEntity = handlerHelper.buildGraphEdgeByRelationship(userId, startEntity, relationship, graph);
            if (endEntity == null) {
                return Collections.emptyList();
            }

            addContextForTabularColumns(userId, endEntity);
            entityDetails.add(endEntity);
        }

        return entityDetails;
    }

    /**
     * Enhance the process context with Tabular Column context
     * Add the asset neighbors for Tabular Column
     *
     * @param userId String - userId of user making request.
     * @param entity details of the entity
     * @throws OCFCheckedExceptionBase checked exception for reporting errors found when using OCF connectors
     */
    private void addContextForTabularColumns(String userId, EntityDetail entity) throws OCFCheckedExceptionBase {
        Set<String> superTypes = superTypesRetriever.getSuperTypes(entity.getType().getTypeDefName());

        if (superTypes.contains(TABULAR_COLUMN)) {
            AssetContext assetContext = assetContextHandler.getAssetContext(userId, entity);
            graph.getGraphContexts().addAll(assetContext.getGraphContexts());
            assetContext.getNeighbors().forEach(this::mergeGraphNeighbors);
        }
    }

    private void mergeGraphNeighbors(String k, Set<GraphContext> v) {
        if (graph.getNeighbors().containsKey(k)) {
            graph.getNeighbors().get(k).addAll(v);
        } else {
            graph.getNeighbors().put(k, v);
        }
    }

    /**
     * Creates the full context for a Process. There are two cases, a process can have a relationship to either
     * a Port Alias or to a Port Implementation. In case of Port Alias it should take the context until Port Implementation
     * entities otherwise it should take the context down to TabularColumn entities.
     *
     * @param entityDetails list of entities
     * @param userId        String - userId of user making request.
     * @return boolean true if relationships exist otherwise false.
     */
    private boolean hasRelationshipBasedOnType(List<EntityDetail> entityDetails, String userId) throws OCFCheckedExceptionBase {
        boolean relationshipsExist = false;
        if (checkIfEntityExistWithSpecificType(entityDetails, PORT_ALIAS))
            relationshipsExist = hasEndRelationship(entityDetails, userId);

        if (checkIfEntityExistWithSpecificType(entityDetails, PORT_IMPLEMENTATION))
            relationshipsExist = hasTabularSchemaTypes(entityDetails, userId);

        return relationshipsExist;
    }

    /**
     * Returns if the entities that are passed as an argument in the method have any relationships.
     *
     * @param entityDetails list of entities
     * @param userId        String - userId of user making request.
     * @return boolean true if relationships exist otherwise false.
     */
    private boolean hasEndRelationship(List<EntityDetail> entityDetails, String userId) throws OCFCheckedExceptionBase {
        List<EntityDetail> result = new ArrayList<>();
        for (EntityDetail entityDetail : entityDetails) {
            result.addAll(getRelationshipsBetweenEntities(userId, entityDetail,
                    immutableProcessRelationshipsTypes.get(entityDetail.getType().getTypeDefName())));
        }
        return !result.isEmpty();
    }

    /**
     * Returns if there are any TabularSchemaTypes that are related to a Port Implementation Entity.
     *
     * @param entityDetails list of entities
     * @param userId        String - userId of user making request.
     * @return boolean true if relationships exist otherwise false.
     */
    private boolean hasTabularSchemaTypes(List<EntityDetail> entityDetails, String userId) throws OCFCheckedExceptionBase {
        List<EntityDetail> result = new ArrayList<>();
        for (EntityDetail entityDetail : entityDetails) {

            List<EntityDetail> tabularSchemaType = getRelationshipsBetweenEntities(userId,
                    entityDetail,
                    immutableProcessRelationshipsTypes.get(entityDetail.getType().getTypeDefName()));
            Optional<EntityDetail> first = tabularSchemaType.stream().findFirst();
            result.add(first.orElse(null));
        }
        return hasSchemaAttributes(result, userId);
    }

    /**
     * Returns if the TabularColumns are part of a TabularSchemaType.
     *
     * @param entityDetails list of entities
     * @param userId        String - userId of user making request.
     * @return boolean true if relationships exist otherwise false.
     */
    private boolean hasSchemaAttributes(List<EntityDetail> entityDetails, String userId) throws OCFCheckedExceptionBase {
        List<EntityDetail> result = new ArrayList<>();
        for (EntityDetail entityDetail : entityDetails) {

            List<EntityDetail> newListOfEntityDetails = getRelationshipsBetweenEntities(userId,
                    entityDetail,
                    immutableProcessRelationshipsTypes.get(entityDetail.getType().getTypeDefName()));
            result.addAll(newListOfEntityDetails);
        }
        return hasEndRelationship(result, userId);
    }

    /**
     * Check if the Type of the entity exists on Open Metadata Types.
     *
     * @param entityDetails list of entities
     * @param typeDefName   String - the type to be checked
     * @return Boolean if type exists or not
     */
    private boolean checkIfEntityExistWithSpecificType(List<EntityDetail> entityDetails, String typeDefName) {
        return entityDetails.stream().anyMatch(entity -> entity.getType().getTypeDefName().equals(typeDefName));
    }
}

