/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetlineage.handlers;

import org.odpi.openmetadata.accessservices.assetlineage.model.AssetContext;
import org.odpi.openmetadata.accessservices.assetlineage.ffdc.exception.AssetLineageException;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryHandler;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefGallery;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.RepositoryErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

import static org.odpi.openmetadata.accessservices.assetlineage.ffdc.AssetLineageErrorCode.ENTITY_NOT_FOUND;
import static org.odpi.openmetadata.accessservices.assetlineage.util.Constants.*;

/**
 * The Asset Context handler provides methods to build graph context for assets that has been created.
 */
public class AssetContextHandler {

    private static final Logger log = LoggerFactory.getLogger(AssetContextHandler.class);


    private String serviceName;
    private String serverName;
    private RepositoryHandler repositoryHandler;
    private OMRSRepositoryHelper repositoryHelper;
    private InvalidParameterHandler invalidParameterHandler;
    private CommonHandler commonHandler;
    private AssetContext graph;

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
    public AssetContextHandler(String serviceName,
                               String serverName,
                               InvalidParameterHandler invalidParameterHandler,
                               OMRSRepositoryHelper repositoryHelper,
                               RepositoryHandler repositoryHandler) {
        this.serviceName = serviceName;
        this.serverName = serverName;
        this.invalidParameterHandler = invalidParameterHandler;
        this.repositoryHelper = repositoryHelper;
        this.repositoryHandler = repositoryHandler;
        this.commonHandler = new CommonHandler(serviceName,serverName,invalidParameterHandler,repositoryHelper,repositoryHandler);
    }


    /**
     * Gets asset context.
     *
     * @param serverName the server name
     * @param userId     the user id
     * @param guid       the guid
     * @param type       the type
     * @return the asset context
     */
    public AssetContext getAssetContext(String serverName, String userId, String guid, String type,String superType) {

        String methodName = "getAssetContext";

        graph = new AssetContext();

        try {

            invalidParameterHandler.validateGUID(guid, GUID_PARAMETER, methodName);

            Optional<EntityDetail> entityDetail = getEntityDetails(userId, guid, type);
            if (!entityDetail.isPresent()) {
                log.error("Something is wrong in the OMRS Connector when a specific operation is performed in the metadata collection." +
                        " Entity not found with guid {}", guid);

                throw new AssetLineageException(ENTITY_NOT_FOUND.getHTTPErrorCode(),
                                                this.getClass().getName(),
                                                "Retrieving Entity",
                                                ENTITY_NOT_FOUND.getErrorMessage(),
                                                ENTITY_NOT_FOUND.getSystemAction(),
                                                ENTITY_NOT_FOUND.getUserAction());
            }
            buildAssetContext(userId, entityDetail.get(),superType);
            return graph;

        }
        catch (UserNotAuthorizedException | InvalidParameterException | PropertyServerException |
                org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException|
                org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException |
                RepositoryErrorException e) {
            throw new AssetLineageException(e.getReportedHTTPCode(),
                                            e.getReportingClassName(),
                                            e.getReportingActionDescription(),
                                            e.getErrorMessage(),
                                            e.getReportedSystemAction(),
                                            e.getReportedUserAction());
        }
    }

    private Optional<EntityDetail> getEntityDetails(String userId, String guid,String type) throws InvalidParameterException,
                                                                                       PropertyServerException,
                                                                                       UserNotAuthorizedException {
        final String methodName = "getEntityDetails";
        return Optional.ofNullable(repositoryHandler.getEntityByGUID(userId, guid, GUID_PARAMETER,type, methodName));
    }


    private void buildAssetContext(String userId, EntityDetail entityDetail,String superType) throws UserNotAuthorizedException,
                                                                                    PropertyServerException,
                                                                                    InvalidParameterException,
                                                                                    RepositoryErrorException,
                                                                                    org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException,
                                                                                    org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException {


        final String typeDefName = entityDetail.getType().getTypeDefName();

        if (typeDefName.equals(RELATIONAL_TABLE) || typeDefName.equals(DATA_FILE)) {
            getSchemaAttributeType(userId,entityDetail,typeDefName);
        }

        if(!isComplexSchemaType(userId,typeDefName).isPresent()) {
            List<EntityDetail> tableTypeEntities = buildGraphByRelationshipType(userId, entityDetail, ATTRIBUTE_FOR_SCHEMA, typeDefName, false);

            for (EntityDetail schemaTypeEntity : tableTypeEntities) {
                if (isComplexSchemaType(userId, schemaTypeEntity.getType().getTypeDefName()).isPresent()) {
                    setAssetDetails(userId, schemaTypeEntity);
                } else {
                    List<EntityDetail> tableEntities = buildGraphByRelationshipType(userId, schemaTypeEntity, SCHEMA_ATTRIBUTE_TYPE, typeDefName, false);
                    if (!CollectionUtils.isEmpty(tableEntities)) {
                        buildAssetContext(userId, tableEntities.stream().findFirst().get(), superType);
                    }
                }
            }
        }
    }

    private List<EntityDetail> buildGraphByRelationshipType(String userId, EntityDetail startEntity,
                                                            String relationshipType, String typeDefName,boolean changeDirection) throws UserNotAuthorizedException,
                                                                                                                   PropertyServerException,
                                                                                                                   InvalidParameterException {
        List<Relationship> relationships = commonHandler.getRelationshipsByType(userId, startEntity.getGUID(), relationshipType, typeDefName);

        if(startEntity.getType().getTypeDefName().equals(FILE_FOLDER)) {
            relationships = relationships.stream().filter(relationship ->
                    relationship.getEntityTwoProxy().getGUID().equals(startEntity.getGUID())).collect(Collectors.toList());
        }

        List<EntityDetail> entityDetails = new ArrayList<>();
        for (Relationship relationship : relationships) {

            EntityDetail endEntity = commonHandler.buildGraphEdgeByRelationship(userId, startEntity, relationship, graph,changeDirection);
            if(endEntity == null) return Collections.emptyList();

            entityDetails.add(endEntity);
        }
        return entityDetails;
    }

    private void setAssetDetails(String userId, EntityDetail startEntity) throws InvalidParameterException,
                                                                                 PropertyServerException,
                                                                                 UserNotAuthorizedException {
        List<EntityDetail> assetEntity = buildGraphByRelationshipType(userId,startEntity, ASSET_SCHEMA_TYPE,startEntity.getType().getTypeDefName(),false);
        Optional<EntityDetail> first = assetEntity.stream().findFirst();
        if(first.isPresent()){
            getAsset(userId, first.get());

        }
    }

    private void getAsset(String userId, EntityDetail dataSet) throws InvalidParameterException,
                                                                      PropertyServerException,
                                                                      UserNotAuthorizedException {
        final String typeDefName = dataSet.getType().getTypeDefName();
        List<EntityDetail> entityDetails;
        if (typeDefName.equals(DATA_FILE)) {
            entityDetails = buildGraphByRelationshipType(userId, dataSet, NESTED_FILE,typeDefName,false);
        } else {
            entityDetails = buildGraphByRelationshipType(userId, dataSet, DATA_CONTENT_FOR_DATA_SET,typeDefName,false);
        }

        if (CollectionUtils.isEmpty(entityDetails)) {
            return;
        }
        getEndpoints(userId,entityDetails.toArray(new EntityDetail[0]));
    }


    private void getEndpoints(String userId,EntityDetail... entityDetails) throws InvalidParameterException,
            PropertyServerException,
            UserNotAuthorizedException {
        for(EntityDetail entityDetail: entityDetails) {
            if (entityDetail != null) {
                if (entityDetail.getType().getTypeDefName().equals(DATABASE)) {
                    getConnections(userId, entityDetail);
                } else {
                    getFolderHierarchy(userId, entityDetail);
                }
            }
        }
    }

    private void getConnections(String userId, EntityDetail entityDetail) throws UserNotAuthorizedException,
            PropertyServerException,
            InvalidParameterException {

        List<EntityDetail> connections = buildGraphByRelationshipType(userId, entityDetail, CONNECTION_TO_ASSET, DATABASE,false);

        if (!connections.isEmpty()) {
            for (EntityDetail entity : connections) {
                buildGraphByRelationshipType(userId, entity, CONNECTION_ENDPOINT, CONNECTION,false);
            }
        }
    }

    private void getFolderHierarchy(String userId,EntityDetail entityDetail) throws InvalidParameterException,
            PropertyServerException,
            UserNotAuthorizedException {

        List<EntityDetail> connections = buildGraphByRelationshipType(userId, entityDetail,
                CONNECTION_TO_ASSET, entityDetail.getType().getTypeDefName(),false);

        Optional<EntityDetail> connection = connections.stream().findFirst();
        if (connection.isPresent()) {
            buildGraphByRelationshipType(userId, entityDetail, CONNECTION_ENDPOINT, CONNECTION,false);
        }

//        List<EntityDetail> nestedFolders = buildGraphByRelationshipType(userId, entityDetail, FOLDER_HIERARCHY, FILE_FOLDER,false);
//        if (CollectionUtils.isEmpty(nestedFolders)) {
//            return;
//        }
//
//        nestedFolders = nestedFolders.stream()
//                .filter(s -> s.getEntityTwoProxy().getGUID().equals(entityDetail.getGUID()))
//                .collect(Collectors.toList());
//        if (nestedFolders.size() != 1) {
//            return;
//        }

        Optional<EntityDetail> nestedFolder =   buildGraphByRelationshipType(userId, entityDetail, FOLDER_HIERARCHY, FILE_FOLDER,false)
                .stream()
                .findFirst();

        if(nestedFolder.isPresent()) {
            getFolderHierarchy(userId, nestedFolder.get());
        }
    }

    private void getSchemaAttributeType(String userId,EntityDetail entityDetail,String typeDefName) throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        List<EntityDetail> schemaAttributeTypes = buildGraphByRelationshipType(userId, entityDetail, SCHEMA_ATTRIBUTE_TYPE, typeDefName,true);

        for(EntityDetail schemaAttributeType: schemaAttributeTypes){
            buildGraphByRelationshipType(userId, schemaAttributeType, ATTRIBUTE_FOR_SCHEMA, typeDefName,true);
        }
    }

    private Optional<TypeDef> isComplexSchemaType(String userId,String typeDefName) throws RepositoryErrorException,
                                                                                           org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException,
                                                                                           org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException {
        TypeDefGallery allTypes =  repositoryHandler.getMetadataCollection().getAllTypes(userId);
        return allTypes.getTypeDefs().stream().filter(t -> t.getName().equals(typeDefName) && t.getSuperType().getName().equals(COMPLEX_SCHEMA_TYPE)).findAny();
    }
}
