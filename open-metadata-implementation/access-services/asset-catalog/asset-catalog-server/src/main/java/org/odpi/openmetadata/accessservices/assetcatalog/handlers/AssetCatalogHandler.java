/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetcatalog.handlers;

import org.apache.commons.collections4.CollectionUtils;
import org.odpi.openmetadata.accessservices.assetcatalog.builders.AssetConverter;
import org.odpi.openmetadata.accessservices.assetcatalog.exception.AssetCatalogErrorCode;
import org.odpi.openmetadata.accessservices.assetcatalog.exception.AssetNotFoundException;
import org.odpi.openmetadata.accessservices.assetcatalog.model.AssetDescription;
import org.odpi.openmetadata.accessservices.assetcatalog.model.AssetElement;
import org.odpi.openmetadata.accessservices.assetcatalog.model.AssetElements;
import org.odpi.openmetadata.accessservices.assetcatalog.model.Connection;
import org.odpi.openmetadata.accessservices.assetcatalog.model.Element;
import org.odpi.openmetadata.accessservices.assetcatalog.model.rest.body.SearchParameters;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryErrorHandler;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryHandler;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.metadatasecurity.server.OpenMetadataServerSecurityVerifier;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.OMRSMetadataCollection;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.SequencingOrder;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Classification;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityProxy;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceGraph;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceStatus;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefLink;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.EntityNotKnownException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.FunctionNotSupportedException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.PagingErrorException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.PropertyErrorException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.RepositoryErrorException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.TypeErrorException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.odpi.openmetadata.accessservices.assetcatalog.util.Constants.*;

/**
 * Asset Catalog Handler supports the lookup of the assets from the repositories.
 * It runs on the server-side of the Asset Catalog OMAS, fetches the entities using the RepositoryHandler.
 */

public class AssetCatalogHandler {

    private static final String ASSET_GUID_PARAMETER = "assetGUID";
    private static final String SEARCH_PARAMETER = "searchParameter";

    private final String serverName;
    private final RepositoryHandler repositoryHandler;
    private final OMRSRepositoryHelper repositoryHelper;
    private final InvalidParameterHandler invalidParameterHandler;
    private final RepositoryErrorHandler errorHandler;
    private List<String> defaultSearchTypes = new ArrayList<>(Arrays.asList(GLOSSARY_TERM_GUID, ASSET_GUID, SCHEMA_ELEMENT_GUID));

    private List<String> supportedZones;
    private List<String> defaultZones;
    private OpenMetadataServerSecurityVerifier securityVerifier;

    /**
     * Construct the handler information needed to interact with the repository services
     *
     * @param serverName              name of the local server
     * @param invalidParameterHandler handler for managing parameter errors
     * @param repositoryHandler       manages calls to the repository services
     * @param repositoryHelper        provides utilities for manipulating the repository services objects
     * @param errorHandler            provides common validation routines for the other handler classes
     * @param defaultZones            default list of zones that Asset Catalog is allowed to serve Assets from
     * @param supportedZones          configurable list of zones that Asset Catalog is allowed to serve Assets from
     */
    public AssetCatalogHandler(String serverName, InvalidParameterHandler invalidParameterHandler,
                               RepositoryHandler repositoryHandler, OMRSRepositoryHelper repositoryHelper,
                               RepositoryErrorHandler errorHandler, List<String> defaultZones, List<String> supportedZones) {
        this.serverName = serverName;
        this.invalidParameterHandler = invalidParameterHandler;
        this.repositoryHelper = repositoryHelper;
        this.repositoryHandler = repositoryHandler;
        this.errorHandler = errorHandler;
        this.supportedZones = supportedZones;
        this.defaultZones = defaultZones;
    }

    /**
     * Set up a new security verifier (the handler runs with a default verifier until this
     * method is called).
     * <p>
     * The security verifier provides authorization checks for access and maintenance
     * changes to open metadata.  Authorization checks are enabled through the
     * OpenMetadataServerSecurityConnector.
     *
     * @param securityVerifier new security verifier
     */
    public void setSecurityVerifier(OpenMetadataServerSecurityVerifier securityVerifier) {
        this.securityVerifier = securityVerifier;
    }

    /**
     * Return the requested entity and converting to Asset Catalog OMAS model
     *
     * @param userId        user identifier that issues the call
     * @param assetGUID     the asset identifier
     * @param assetTypeName the asset type name
     * @return AssetDescription that contains the core properties of the entity and additional properties
     * @throws InvalidParameterException  is thrown by the OMAS when a parameter is null or an invalid value.
     * @throws PropertyServerException    reporting errors when connecting to a metadata repository to retrieve properties about the connection and/or connector
     * @throws UserNotAuthorizedException is thrown by the OCF when a userId passed on a request is not authorized to perform the requested action.
     */
    public AssetDescription getEntityDetails(String userId, String assetGUID, String assetTypeName)
            throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException, RepositoryErrorException {
        EntityDetail entityByGUID = getEntity(userId, assetGUID, assetTypeName);
        AssetConverter converter = new AssetConverter(repositoryHelper);
        return converter.getAssetDescription(entityByGUID);
    }

    /**
     * Returns a list of the relationships for the given entity identifier.
     * Relationship type name can be used for filtering.
     *
     * @param userId           user identifier that issues the call
     * @param assetGUID        the asset identifier
     * @param assetTypeName    the asset type name
     * @param relationshipType the relationship type name
     * @return a list of Relationships
     * @throws UserNotAuthorizedException                                                     is thrown by the OCF when a userId passed on a request is not authorized to perform the requested action.
     * @throws PropertyServerException                                                        reporting errors when connecting to a metadata repository to retrieve properties about the connection and/or connector
     * @throws org.odpi.openmetadata.commonservices.ffdc.exceptions.InvalidParameterException is thrown by the OMAG Service when a parameter is null or an invalid value.
     */
    public List<org.odpi.openmetadata.accessservices.assetcatalog.model.Relationship> getRelationshipsByEntityGUID(String userId,
                                                                                                                   String assetGUID,
                                                                                                                   String assetTypeName,
                                                                                                                   String relationshipType)
            throws UserNotAuthorizedException, PropertyServerException, org.odpi.openmetadata.commonservices.ffdc.exceptions.InvalidParameterException, RepositoryErrorException {
        String methodName = "getRelationshipsByEntityGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(assetGUID, ASSET_GUID_PARAMETER, methodName);

        List<Relationship> relationshipsByType = repositoryHandler.getRelationshipsByType(userId,
                assetGUID,
                assetTypeName,
                null,
                relationshipType,
                methodName);

        if (CollectionUtils.isNotEmpty(relationshipsByType)) {
            return getRelationshipsAfterSecurityValidation(userId, relationshipsByType);
        }

        return Collections.emptyList();
    }

    private List<org.odpi.openmetadata.accessservices.assetcatalog.model.Relationship>
    getRelationshipsAfterSecurityValidation(String userId,
                                            List<Relationship> relationshipsByType) throws UserNotAuthorizedException, RepositoryErrorException {
        List<org.odpi.openmetadata.accessservices.assetcatalog.model.Relationship> relationships = new ArrayList<>();
        AssetConverter converter = new AssetConverter(repositoryHelper);
        String metadataCollectionName = getOMRSMetadataCollectionName(userId);

        for (Relationship relationship : relationshipsByType) {
            securityVerifier.validateUserForRelationshipRead(userId, metadataCollectionName, relationship);
            org.odpi.openmetadata.accessservices.assetcatalog.model.Relationship convertRelationship = converter.convertRelationship(relationship);
            relationships.add(convertRelationship);
        }
        return relationships;
    }

    /**
     * Returns a list of the classification assigned to the given asset.
     * The filtering based on the classification name is possible.
     *
     * @param userId             user identifier that issues the call
     * @param assetGUID          the asset identifier
     * @param assetTypeName      the asset type name
     * @param classificationName the classification type name
     * @return a list of Classifications assigned to the given asset
     * @throws InvalidParameterException  is thrown by the OMAG Service when a parameter is null or an invalid value.
     * @throws PropertyServerException    reporting errors when connecting to a metadata repository to retrieve properties about the connection and/or connector
     * @throws UserNotAuthorizedException is thrown by the OCF when a userId passed on a request is not authorized to perform the requested action.
     */
    public List<org.odpi.openmetadata.accessservices.assetcatalog.model.Classification> getEntityClassificationByName(String userId,
                                                                                                                      String assetGUID,
                                                                                                                      String assetTypeName,
                                                                                                                      String classificationName)
            throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException, RepositoryErrorException {
        String methodName = "getEntityClassificationByName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(assetGUID, ASSET_GUID_PARAMETER, methodName);

        List<Classification> entityClassifications = getEntityClassifications(userId, assetGUID, assetTypeName);
        AssetConverter converter = new AssetConverter(repositoryHelper);

        if (CollectionUtils.isEmpty(entityClassifications)) {
            return Collections.emptyList();
        }

        if (classificationName != null) {
            entityClassifications = filterClassificationByName(entityClassifications, classificationName);
        }

        return converter.convertClassifications(entityClassifications);
    }

    /**
     * @param serverName     name of the local server
     * @param userId         user identifier that issues the call
     * @param startAssetGUID the  starting asset identifier
     * @param endAssetGUID   the ending  asset identifier
     * @return the linking relationship between the given assets
     * @throws AssetNotFoundException     is thrown by the Asset Catalog OMAS when the asset passed on a request is not found in the repository
     * @throws InvalidParameterException  is thrown by the OMAG Service when a parameter is null or an invalid value.
     * @throws PropertyServerException    reporting errors when connecting to a metadata repository to retrieve properties about the connection and/or connector
     * @throws UserNotAuthorizedException is thrown by the OCF when a userId passed on a request is not authorized to perform the requested action.
     */
    public List<org.odpi.openmetadata.accessservices.assetcatalog.model.Relationship> getLinkingRelationshipsBetweenAssets(
            String serverName, String userId, String startAssetGUID, String endAssetGUID)
            throws AssetNotFoundException, InvalidParameterException, PropertyServerException, UserNotAuthorizedException, RepositoryErrorException {
        String methodName = "getLinkingRelationshipsBetweenAssets";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(startAssetGUID, "startAssetGUID", methodName);
        invalidParameterHandler.validateGUID(endAssetGUID, "endAssetGUID", methodName);

        OMRSMetadataCollection metadataCollection = getOMRSMetadataCollection();
        InstanceGraph linkingEntities = null;
        try {
            linkingEntities = metadataCollection.getLinkingEntities(userId,
                    startAssetGUID,
                    endAssetGUID,
                    Collections.singletonList(InstanceStatus.ACTIVE),
                    null);
        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException
                | FunctionNotSupportedException | RepositoryErrorException
                | PropertyErrorException e) {
            errorHandler.handleRepositoryError(e, methodName);
        } catch (EntityNotKnownException e) {
            errorHandler.handleUnknownEntity(e, startAssetGUID, "", methodName, ASSET_GUID_PARAMETER);
        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException e) {
            errorHandler.handleUnauthorizedUser(userId, methodName);
        }

        if (linkingEntities == null || CollectionUtils.isEmpty(linkingEntities.getRelationships())) {
            AssetCatalogErrorCode errorCode = AssetCatalogErrorCode.LINKING_RELATIONSHIPS_NOT_FOUND;
            String errorMessage = errorCode.getErrorMessageId() +
                    errorCode.getFormattedErrorMessage(startAssetGUID, endAssetGUID, serverName);

            throw new AssetNotFoundException(errorCode.getHttpErrorCode(),
                    this.getClass().getName(),
                    "getLinkingRelationshipsBetweenAssets",
                    errorMessage,
                    errorCode.getSystemAction(),
                    errorCode.getUserAction());
        }

        return getRelationshipsAfterSecurityValidation(userId, linkingEntities.getRelationships());
    }

    /**
     * @param userId               user identifier that issues the call
     * @param assetGUID            the asset identifier
     * @param assetTypeName        the asset type name
     * @param relationshipTypeGUID the relationship type global identifier
     * @param relationshipTypeName the relationship type name
     * @param from                 offset
     * @param pageSize             limit the number of the assets returned
     * @return the list of relationships for the given asset
     * @throws UserNotAuthorizedException                                                     is thrown by the OCF when a userId passed on a request is not authorized to perform the requested action.
     * @throws PropertyServerException                                                        reporting errors when connecting to a metadata repository to retrieve properties about the connection and/or connector
     * @throws org.odpi.openmetadata.commonservices.ffdc.exceptions.InvalidParameterException is thrown by the OMAG Service when a parameter is null or an invalid value.
     */
    public List<org.odpi.openmetadata.accessservices.assetcatalog.model.Relationship> getRelationships(String userId,
                                                                                                       String assetGUID,
                                                                                                       String assetTypeName,
                                                                                                       String relationshipTypeGUID,
                                                                                                       String relationshipTypeName,
                                                                                                       Integer from, Integer pageSize)
            throws UserNotAuthorizedException, PropertyServerException, org.odpi.openmetadata.commonservices.ffdc.exceptions.InvalidParameterException, RepositoryErrorException {

        String methodName = "getRelationships";
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(assetGUID, ASSET_GUID_PARAMETER, methodName);
        invalidParameterHandler.validatePaging(from, pageSize, methodName);

        List<Relationship> pagedRelationshipsByType = repositoryHandler.getPagedRelationshipsByType(userId,
                assetGUID,
                assetTypeName,
                relationshipTypeGUID,
                relationshipTypeName,
                from,
                pageSize,
                methodName);

        if (CollectionUtils.isNotEmpty(pagedRelationshipsByType)) {
            return getRelationshipsAfterSecurityValidation(userId, pagedRelationshipsByType);
        }

        return Collections.emptyList();
    }

    /**
     * Returns the global identifier of the type using the type def name
     *
     * @param userId      user identifier that issues the call
     * @param typeDefName the type definition name
     * @return the global identifier of the type
     * @throws org.odpi.openmetadata.commonservices.ffdc.exceptions.InvalidParameterException - is thrown by the OMAG Service when a parameter is null or an invalid value
     */
    public String getTypeDefGUID(String userId, String typeDefName) throws
            org.odpi.openmetadata.commonservices.ffdc.exceptions.InvalidParameterException {
        String methodName = "getTypeDefGUID";
        invalidParameterHandler.validateUserId(userId, methodName);

        TypeDef typeDefByName = repositoryHelper.getTypeDefByName(userId, typeDefName);
        return Optional.ofNullable(typeDefByName).map(TypeDefLink::getGUID).orElse(null);
    }

    /**
     * @param userId         user identifier that issues the call
     * @param startAssetGUID the  starting asset identifier
     * @param endAssetGUID   the ending  asset identifier
     * @return a list of the entities that connects the given assets from the request
     * @throws AssetNotFoundException     is thrown by the Asset Catalog OMAS when the asset passed on a request is not found in the repository
     * @throws InvalidParameterException  is thrown by the OMAG Service when a parameter is null or an invalid value.
     * @throws PropertyServerException    reporting errors when connecting to a metadata repository to retrieve properties about the connection and/or connector
     * @throws UserNotAuthorizedException is thrown by the OCF when a userId passed on a request is not authorized to perform the requested action.
     */
    public List<AssetDescription> getIntermediateAssets(String userId, String startAssetGUID, String endAssetGUID)
            throws AssetNotFoundException, InvalidParameterException, PropertyServerException, UserNotAuthorizedException, RepositoryErrorException {

        String methodName = "getIntermediateAssets";
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(startAssetGUID, "startAssetGUID", methodName);
        invalidParameterHandler.validateGUID(endAssetGUID, "endAssetGUID", methodName);

        OMRSMetadataCollection metadataCollection = getOMRSMetadataCollection();

        InstanceGraph linkingEntities = null;
        try {
            linkingEntities = metadataCollection.getLinkingEntities(userId,
                    startAssetGUID,
                    endAssetGUID,
                    Collections.singletonList(InstanceStatus.ACTIVE),
                    null);
        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException
                | FunctionNotSupportedException
                | PropertyErrorException | RepositoryErrorException e) {
            errorHandler.handleRepositoryError(e, methodName);
        } catch (EntityNotKnownException e) {
            errorHandler.handleUnknownEntity(e, startAssetGUID, "", methodName, ASSET_GUID_PARAMETER);
        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException e) {
            errorHandler.handleUnauthorizedUser(userId, methodName);
        }

        if (linkingEntities == null || CollectionUtils.isEmpty(linkingEntities.getEntities())) {
            AssetCatalogErrorCode errorCode = AssetCatalogErrorCode.LINKING_ASSETS_NOT_FOUND;
            String errorMessage = errorCode.getErrorMessageId() +
                    errorCode.getFormattedErrorMessage(startAssetGUID, endAssetGUID, serverName);

            throw new AssetNotFoundException(errorCode.getHttpErrorCode(),
                    this.getClass().getName(),
                    "getIntermediateAssets",
                    errorMessage,
                    errorCode.getSystemAction(),
                    errorCode.getUserAction());
        }

        return getAssetsDescriptionAfterSecurityValidation(userId, linkingEntities.getEntities());
    }

    private List<AssetDescription> getAssetsDescriptionAfterSecurityValidation(String userId,
                                                                               List<EntityDetail> entityDetails)
            throws RepositoryErrorException, UserNotAuthorizedException {

        AssetConverter converter = new AssetConverter(repositoryHelper);
        String metadataCollectionName = getOMRSMetadataCollectionName(userId);

        List<AssetDescription> result = new ArrayList<>();
        for (EntityDetail entityDetail : entityDetails) {
            securityVerifier.validateUserForEntityRead(userId, metadataCollectionName, entityDetail);
            result.add(converter.getAssetDescription(entityDetail));
        }
        return result;
    }

    /**
     * @param serverName       name of the local server
     * @param userId           user identifier that issues the call
     * @param assetGUID        the asset identifier
     * @param searchParameters additional parameters for searching and filtering
     * @return a list of entities from the neighborhood of the given entity
     * @throws AssetNotFoundException     is thrown by the Asset Catalog OMAS when the asset passed on a request is not found in the repository
     * @throws InvalidParameterException  is thrown by the OMAG Service when a parameter is null or an invalid value.
     * @throws PropertyServerException    reporting errors when connecting to a metadata repository to retrieve properties about the connection and/or connector
     * @throws UserNotAuthorizedException is thrown by the OCF when a userId passed on a request is not authorized to perform the requested action.
     */
    public List<AssetDescription> getEntitiesFromNeighborhood(String serverName, String userId, String assetGUID, SearchParameters searchParameters)
            throws AssetNotFoundException, InvalidParameterException, PropertyServerException, UserNotAuthorizedException, RepositoryErrorException {

        String methodName = "getEntitiesFromNeighborhood";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(assetGUID, ASSET_GUID_PARAMETER, methodName);
        invalidParameterHandler.validateObject(searchParameters, SEARCH_PARAMETER, methodName);
        invalidParameterHandler.validatePaging(searchParameters.getFrom(), searchParameters.getPageSize(), methodName);

        InstanceGraph entityNeighborhood = getAssetNeighborhood(serverName, userId, assetGUID, searchParameters);

        List<EntityDetail> entities = entityNeighborhood.getEntities();
        if (CollectionUtils.isEmpty(entities)) {
            AssetCatalogErrorCode errorCode = AssetCatalogErrorCode.NO_ASSET_FROM_NEIGHBORHOOD_NOT_FOUND;
            String errorMessage = errorCode.getErrorMessageId() +
                    errorCode.getFormattedErrorMessage(assetGUID, serverName);

            throw new AssetNotFoundException(errorCode.getHttpErrorCode(),
                    this.getClass().getName(),
                    "getEntitiesFromNeighborhood",
                    errorMessage,
                    errorCode.getSystemAction(),
                    errorCode.getUserAction());
        }

        return getAssetsDescriptionAfterSecurityValidation(userId, entities);
    }

    /**
     * @param userId           user identifier that issues the call
     * @param searchCriteria   search criteria string used for finding the entities
     * @param searchParameters additional parameters for searching and filtering
     * @return a list of matching criteria entities
     * @throws org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException - is thrown by an OMRS Connector when the supplied UserId
     *                                                                                            is not permitted to perform a specific operation on the metadata collection.
     * @throws FunctionNotSupportedException                                                      - provides a checked exception for reporting that an
     *                                                                                            OMRS repository connector does not support the method called
     * @throws org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException  - is thrown by an OMRS Connector when the parameters passed to a repository connector are not valid
     * @throws RepositoryErrorException                                                           - provides a checked exception for reporting situations where the metadata
     *                                                                                            repository hosting a metadata collection is unable to perform a request
     * @throws PropertyErrorException                                                             - is thrown by an OMRS Connector when the properties defined for a specific entity
     *                                                                                            or relationship instance do not match the TypeDefs for the metadata collection.
     * @throws TypeErrorException                                                                 -  is thrown by an OMRS Connector when the requested type for an instance is not represented by a known TypeDef.
     * @throws PagingErrorException                                                               -  is thrown by an OMRS Connector when the caller has passed invalid paging attributes on a search call.
     * @throws org.odpi.openmetadata.commonservices.ffdc.exceptions.InvalidParameterException     - is thrown by the OMAG Service when a parameter is null or an invalid value.
     */
    public List<AssetElements> searchByType(String userId, String searchCriteria, SearchParameters searchParameters)
            throws org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException,
            FunctionNotSupportedException, org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException,
            RepositoryErrorException, PropertyErrorException, TypeErrorException,
            PagingErrorException, org.odpi.openmetadata.commonservices.ffdc.exceptions.InvalidParameterException, UserNotAuthorizedException {

        String methodName = "searchByType";
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateSearchString(userId, searchCriteria, methodName);
        invalidParameterHandler.validateObject(searchParameters, SEARCH_PARAMETER, methodName);
        invalidParameterHandler.validatePaging(searchParameters.getFrom(), searchParameters.getPageSize(), methodName);

        List<EntityDetail> result;
        if (CollectionUtils.isNotEmpty(searchParameters.getEntityTypeGUIDs())) {
            result = collectSearchedEntitiesByType(userId, searchCriteria, searchParameters, searchParameters.getEntityTypeGUIDs());
        } else {
            result = collectSearchedEntitiesByType(userId, searchCriteria, searchParameters, defaultSearchTypes);
        }

        List<AssetElements> list = new ArrayList<>();
        String metadataCollectionName = getOMRSMetadataCollectionName(userId);

        for (EntityDetail entityDetail : result) {
            securityVerifier.validateUserForEntityRead(userId, metadataCollectionName, entityDetail);
            AssetElements assetElements = buildAssetElements(entityDetail);
            list.add(assetElements);
        }
        return list;
    }

    /**
     * @param userId            user identifier that issues the call
     * @param entityGUID        the identifier of the entity
     * @param entityTypeDefName the type name of the entity
     * @return the context of the given entity
     * @throws org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException - is thrown by the OCF when a userId passed on a request is not
     *                                                                                     authorized to perform the requested action.
     * @throws org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException    - provides a checked exception for reporting errors when connecting to a
     *                                                                                     metadata repository to retrieve properties about the connection and/or connector.
     * @throws org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException  -  is thrown by the OMAS when a parameter is null or an invalid value.
     */
    public AssetElements buildContextByType(String userId,
                                            String entityGUID,
                                            String entityTypeDefName)
            throws org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException,
            org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException,
            org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException, RepositoryErrorException {

        String methodName = "buildContextByType";
        invalidParameterHandler.validateUserId(userId, methodName);

        EntityDetail entityDetail = getEntity(userId, entityGUID, entityTypeDefName);

        if (entityDetail.getType() == null) {
            return null;
        }

        securityVerifier.validateUserForEntityRead(userId, getOMRSMetadataCollectionName(userId), entityDetail);
        String typeDefName = entityDetail.getType().getTypeDefName();
        Set<String> superTypes = collectSuperTypes(userId, entityDetail.getType().getTypeDefName());

        AssetElement assetElement = new AssetElement();

        if (typeDefName.equals(GLOSSARY_TERM)) {
            return getContextForGlossaryTerm(userId, entityDetail);
        } else {
            AssetElements assetElements = buildAssetElements(entityDetail);
            if (superTypes.contains(SCHEMA_ELEMENT)) {
                getContextForSchemaElement(userId, entityDetail, assetElement);
            } else if (superTypes.contains(DEPLOYED_API)) {
                getContextForDeployedAPI(userId, entityDetail, assetElement);
            } else if (superTypes.contains(IT_INFRASTRUCTURE)) {
                getContextForInfrastructure(userId, entityDetail, assetElement);
            } else if (superTypes.contains(PROCESS)) {
                getContextForProcess(userId, entityDetail, assetElement);
            } else if (superTypes.contains(DATA_STORE)) {
                getContextForDataStore(userId, entityDetail, assetElement);
            } else if (superTypes.contains(DATA_SET)) {
                getContextForDataSet(userId, entityDetail, assetElement);
            }

            assetElements.setElements(Collections.singletonList(assetElement));
            return assetElements;
        }
    }

    private EntityDetail getEntity(String userId,
                                   String assetGUID,
                                   String assetTypeName) throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException, RepositoryErrorException {
        String methodName = "getEntity";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(assetGUID, ASSET_GUID_PARAMETER, methodName);

        EntityDetail entityByGUID = repositoryHandler.getEntityByGUID(userId, assetGUID, ASSET_GUID_PARAMETER, assetTypeName, methodName);
        securityVerifier.validateUserForEntityRead(userId, getOMRSMetadataCollectionName(userId), entityByGUID);

        return entityByGUID;
    }

    private List<EntityDetail> collectSearchedEntitiesByType(String userId,
                                                             String searchCriteria,
                                                             SearchParameters searchParameters,
                                                             List<String> types)
            throws org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException, FunctionNotSupportedException,
            org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException, RepositoryErrorException, PropertyErrorException, TypeErrorException, PagingErrorException {
        List<EntityDetail> result = new ArrayList<>();

        OMRSMetadataCollection metadataCollection = getOMRSMetadataCollection();
        for (String type : types) {
            result.addAll(searchEntityByCriteria(userId, searchCriteria, type, searchParameters, metadataCollection));
        }
        return result;
    }

    private void getContextForSchemaElement(String userId,
                                            EntityDetail entityDetail,
                                            AssetElement assetElement)
            throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException, RepositoryErrorException {
        findAsset(userId, Collections.singletonList(entityDetail), assetElement);
    }

    private AssetElements getContextForGlossaryTerm(String userId,
                                                    EntityDetail glossaryTerm)
            throws UserNotAuthorizedException, PropertyServerException, InvalidParameterException, RepositoryErrorException {
        String method = "getContextForGlossaryTerm";

        if (glossaryTerm == null) {
            return null;
        }
        AssetElements assetElements = buildAssetElements(glossaryTerm);

        List<EntityDetail> schemas = repositoryHandler.getEntitiesForRelationshipType(userId,
                glossaryTerm.getGUID(),
                GLOSSARY_TERM,
                SEMANTIC_ASSIGNMENT_GUID,
                SEMANTIC_ASSIGNMENT,
                0,
                0,
                method);

        if (CollectionUtils.isEmpty(schemas)) {
            return assetElements;
        }

        List<AssetElement> assets = new ArrayList<>(schemas.size());
        String metadataCollectionName = getOMRSMetadataCollectionName(userId);

        for (EntityDetail schema : schemas) {
            securityVerifier.validateUserForEntityRead(userId, metadataCollectionName, schema);
            AssetElement assetElement = new AssetElement();
            List<Element> elements = new ArrayList<>();
            elements.add(buildAssetElements(schema));
            assetElement.setContext(elements);

            findAsset(userId, Collections.singletonList(schema), assetElement);
            assets.add(assetElement);
        }

        assetElements.setElements(assets);

        return assetElements;
    }

    private void getContextForDeployedAPI(String userId,
                                          EntityDetail entityDetail,
                                          AssetElement assetElement)
            throws UserNotAuthorizedException, PropertyServerException, RepositoryErrorException {
        String method = "getContextForDeployedAPI";

        List<EntityDetail> endpoints = repositoryHandler.getEntitiesForRelationshipType(
                userId,
                entityDetail.getGUID(),
                DEPLOYED_API,
                API_ENDPOINT_GUID,
                API_ENDPOINT,
                0,
                0,
                method);
        if (CollectionUtils.isEmpty(endpoints)) {
            return;
        }

        String metadataCollectionName = getOMRSMetadataCollectionName(userId);
        for (EntityDetail endpoint : endpoints) {
            securityVerifier.validateUserForEntityRead(userId, metadataCollectionName, endpoint);

            addContextElement(assetElement, endpoint);
            getConnectionContext(userId, endpoint, assetElement);
        }

    }

    private void getContextForInfrastructure(String userId,
                                             EntityDetail entityDetail,
                                             AssetElement assetElement)
            throws UserNotAuthorizedException, PropertyServerException, InvalidParameterException, RepositoryErrorException {
        switch (entityDetail.getType().getTypeDefName()) {
            case HOST:
                getContextForHost(userId, entityDetail, assetElement);
                break;
            case NETWORK:
                getContextForNetwork(userId, entityDetail, assetElement);
                break;
            case SOFTWARE_SERVER_PLATFORM:
                getContextForSoftwareServerPlatform(userId, entityDetail, assetElement);
                break;
            case SOFTWARE_SERVER:
                getContextForSoftwareServer(userId, entityDetail, assetElement);
                break;
            default:
                break;
        }

    }

    private void getContextForProcess(String userId,
                                      EntityDetail entityDetail,
                                      AssetElement assetElement)
            throws UserNotAuthorizedException, PropertyServerException, InvalidParameterException, RepositoryErrorException {
        String method = "getContextForProcess";

        List<EntityDetail> ports = repositoryHandler.getEntitiesForRelationshipType(
                userId,
                entityDetail.getGUID(),
                PROCESS,
                PROCESS_PORT_GUID,
                PROCESS_PORT,
                0,
                0,
                method);

        if (CollectionUtils.isNotEmpty(ports)) {
            String metadataCollectionName = getOMRSMetadataCollectionName(userId);

            for (EntityDetail port : ports) {
                securityVerifier.validateUserForEntityRead(userId, metadataCollectionName, port);
                addContextElement(assetElement, port);
                if (port.getType().getTypeDefName().equals(PORT_IMPLEMENTATION)) {
                    EntityDetail schemaType = repositoryHandler.getEntityForRelationshipType(userId,
                            port.getGUID(),
                            DATABASE,
                            PORT_SCHEMA_GUID,
                            PORT_SCHEMA,
                            method);

                    if (schemaType != null) {
                        securityVerifier.validateUserForEntityRead(userId, metadataCollectionName, schemaType);
                        addElement(assetElement, schemaType);
                        getContextForSchemaType(userId, assetElement, schemaType);
                    }
                }
            }
        }

    }

    private void getContextForDataStore(String userId,
                                        EntityDetail entityDetail,
                                        AssetElement assetElement)
            throws UserNotAuthorizedException, PropertyServerException, InvalidParameterException, RepositoryErrorException {

        if (entityDetail.getType().getTypeDefName().equals(DATABASE)) {
            getContextForDatabase(userId, entityDetail, assetElement);
        } else {
            if (entityDetail.getType().getTypeDefName().equals(DATA_FILE)) {
                getContextForDataFile(userId, entityDetail, assetElement);
            } else if (entityDetail.getType().getTypeDefName().equals(FILE_FOLDER)) {
                getContextForFileFolder(userId, entityDetail, assetElement);
            }
        }
    }

    private void getContextForDatabase(String userId,
                                       EntityDetail entityDetail,
                                       AssetElement assetElement)
            throws UserNotAuthorizedException, PropertyServerException, InvalidParameterException, RepositoryErrorException {
        String method = "getContextForDatabase";

        List<EntityDetail> dataSets = repositoryHandler.getEntitiesForRelationshipType(
                userId,
                entityDetail.getGUID(),
                DATABASE,
                DATA_CONTENT_FOR_DATA_SET_GUID,
                DATA_CONTENT_FOR_DATA_SET,
                0,
                0,
                method);

        if (dataSets != null && !dataSets.isEmpty()) {

            String metadataCollectionName = getOMRSMetadataCollectionName(userId);

            for (EntityDetail dataSet : dataSets) {
                securityVerifier.validateUserForEntityRead(userId, metadataCollectionName, dataSet);
                getContextForDataSet(userId, dataSet, assetElement);
            }
        }
    }

    private void getContextForDataSet(String userId,
                                      EntityDetail dataSet,
                                      AssetElement assetElement) throws
            UserNotAuthorizedException, PropertyServerException, InvalidParameterException, RepositoryErrorException {
        String method = "getContextForDataSet";

        EntityDetail schemaType = repositoryHandler.getEntityForRelationshipType(
                userId,
                dataSet.getGUID(),
                DATA_SET,
                ASSET_SCHEMA_TYPE_GUID,
                ASSET_SCHEMA_TYPE,
                method);


        if (schemaType == null) {
            return;
        }

        String metadataCollectionName = getOMRSMetadataCollectionName(userId);
        securityVerifier.validateUserForEntityRead(userId, metadataCollectionName, schemaType);

        addElement(assetElement, schemaType);
        if (isComplexSchemaType(schemaType.getType().getTypeDefName()).isPresent()) {
            getContextForSchemaType(userId, assetElement, schemaType);
        } else {
            getAsset(userId, assetElement, schemaType);
        }
    }

    private void getContextForFileFolder(String userId,
                                         EntityDetail entityDetail,
                                         AssetElement assetElement)
            throws UserNotAuthorizedException, PropertyServerException, InvalidParameterException, RepositoryErrorException {
        String method = "getContextForFileFolder";

        List<EntityDetail> connections = repositoryHandler.getEntitiesForRelationshipType(
                userId,
                entityDetail.getGUID(),
                FILE_FOLDER,
                CONNECTION_TO_ASSET_GUID,
                CONNECTION_TO_ASSET,
                0,
                0,
                method);


        if (CollectionUtils.isNotEmpty(connections)) {
            setConnections(userId, assetElement, entityDetail);
            return;
        }


        List<Relationship> parentFolderRelationships = repositoryHandler.getRelationshipsByType(userId,
                entityDetail.getGUID(), entityDetail.getType().getTypeDefName(),
                FOLDER_HIERARCHY_GUID, FOLDER_HIERARCHY, method);

        if (CollectionUtils.isEmpty(parentFolderRelationships)) {
            return;
        }

        parentFolderRelationships = parentFolderRelationships.stream()
                .filter(s -> s.getEntityTwoProxy().getGUID().equals(entityDetail.getGUID()))
                .collect(Collectors.toList());
        if (parentFolderRelationships.size() != 1) {
            return;
        }

        String metadataCollectionName = getOMRSMetadataCollectionName(userId);

        EntityProxy parentFolderProxy = repositoryHandler.getOtherEnd(entityDetail.getGUID(), parentFolderRelationships.get(0));
        securityVerifier.validateUserForEntityProxyRead(userId, metadataCollectionName, parentFolderProxy);

        EntityDetail parentFolder = repositoryHandler.getEntityByGUID(userId,
                parentFolderProxy.getGUID(),
                ASSET_GUID_PARAMETER,
                parentFolderProxy.getType().getTypeDefName(),
                method);

        securityVerifier.validateUserForEntityProxyRead(userId, metadataCollectionName, parentFolderProxy);
        addElement(assetElement, parentFolder);
        getContextForFileFolder(userId, parentFolder, assetElement);
    }

    private void getContextForEachParentFolder(String userId,
                                               AssetElement assetElement,
                                               List<EntityDetail> parentFolders)
            throws UserNotAuthorizedException, PropertyServerException, InvalidParameterException, RepositoryErrorException {
        String metadataCollectionName = getOMRSMetadataCollectionName(userId);

        for (EntityDetail folder : parentFolders) {
            securityVerifier.validateUserForEntityRead(userId, metadataCollectionName, folder);
            addElement(assetElement, folder);
            getContextForFileFolder(userId, folder, assetElement);
        }
    }

    private void getContextForDataFile(String userId,
                                       EntityDetail entityDetail,
                                       AssetElement assetElement)
            throws UserNotAuthorizedException, PropertyServerException, InvalidParameterException, RepositoryErrorException {
        String method = "getContextForDataFile";

        List<EntityDetail> fileFolders = repositoryHandler.getEntitiesForRelationshipType(
                userId,
                entityDetail.getGUID(),
                DATA_FILE,
                NESTED_FILE_GUID,
                NESTED_FILE,
                0,
                0,
                method);

        if (CollectionUtils.isEmpty(fileFolders)) {
            return;
        }

        getContextForEachParentFolder(userId, assetElement, fileFolders);
    }

    private void getContextForSoftwareServerPlatform(String userId,
                                                     EntityDetail entityDetail,
                                                     AssetElement assetElement)
            throws UserNotAuthorizedException, PropertyServerException, InvalidParameterException, RepositoryErrorException {
        String method = "getContextForSoftwareServerPlatform";

        EntityDetail host = repositoryHandler.getEntityForRelationshipType(
                userId,
                entityDetail.getGUID(),
                entityDetail.getType().getTypeDefName(),
                SOFTWARE_SERVER_PLATFORM_DEPLOYMENT_GUID,
                SOFTWARE_SERVER_PLATFORM_DEPLOYMENT,
                method);

        if (host != null) {
            securityVerifier.validateUserForEntityRead(userId, getOMRSMetadataCollectionName(userId), host);
            addElement(assetElement, host);
            getContextForHost(userId, host, assetElement);
        }
    }

    private void getContextForNetwork(String userId,
                                      EntityDetail entityDetail,
                                      AssetElement assetElement)
            throws UserNotAuthorizedException, PropertyServerException, InvalidParameterException, RepositoryErrorException {
        String method = "getContextForNetwork";

        List<EntityDetail> networkGateways = repositoryHandler.getEntitiesForRelationshipType(
                userId,
                entityDetail.getGUID(),
                NETWORK,
                NETWORK_GATEWAY_LINK_GUID,
                NETWORK_GATEWAY_LINK,
                0,
                0,
                method);

        if (CollectionUtils.isNotEmpty(networkGateways)) {
            String metadataCollectionName = getOMRSMetadataCollectionName(userId);

            for (EntityDetail element : networkGateways) {
                securityVerifier.validateUserForEntityRead(userId, metadataCollectionName, element);
                addElement(assetElement, element);
            }
        }

        List<EntityDetail> hosts = repositoryHandler.getEntitiesForRelationshipType(
                userId,
                entityDetail.getGUID(),
                NETWORK,
                HOST_NETWORK_GUID,
                HOST_NETWORK,
                0,
                0,
                method);


        String metadataCollectionName = getOMRSMetadataCollectionName(userId);

        if (CollectionUtils.isNotEmpty(hosts)) {
            for (EntityDetail host : hosts) {
                securityVerifier.validateUserForEntityRead(userId, metadataCollectionName, host);
                addElement(assetElement, host);
                getContextForHost(userId, host, assetElement);
            }
        }
    }

    private void getContextForHost(String userId,
                                   EntityDetail entityDetail,
                                   AssetElement assetElement)
            throws UserNotAuthorizedException, PropertyServerException, InvalidParameterException, RepositoryErrorException {
        String method = "getContextForHost";
        List<EntityDetail> hosts = null;
        if (entityDetail.getType().getTypeDefName().equals(VIRTUAL_CONTAINER)) {
            hosts = repositoryHandler.getEntitiesForRelationshipType(userId,
                    entityDetail.getGUID(),
                    HOST,
                    DEPLOYED_VIRTUAL_CONTAINER_GUID,
                    DEPLOYED_VIRTUAL_CONTAINER,
                    0,
                    0,
                    method);

        } else if (entityDetail.getType().getTypeDefName().equals(HOST_CLUSTER)) {
            hosts = repositoryHandler.getEntitiesForRelationshipType(
                    userId,
                    entityDetail.getGUID(),
                    HOST,
                    HOST_CLUSTER_MEMBER_GUID,
                    HOST_CLUSTER_MEMBER,
                    0,
                    0,
                    method);
        }

        if (CollectionUtils.isNotEmpty(hosts)) {
            String metadataCollectionName = getOMRSMetadataCollectionName(userId);
            for (EntityDetail host : hosts) {
                securityVerifier.validateUserForEntityRead(userId, metadataCollectionName, host);
                addElement(assetElement, host);
            }
        }

        EntityDetail operatingPlatform = repositoryHandler.getEntityForRelationshipType(userId,
                entityDetail.getGUID(),
                entityDetail.getType().getTypeDefName(),
                HOST_OPERATING_PLATFORM_GUID,
                HOST_OPERATING_PLATFORM,
                method);

        String metadataCollectionName = getOMRSMetadataCollectionName(userId);
        securityVerifier.validateUserForEntityRead(userId, metadataCollectionName, operatingPlatform);
        addElement(assetElement, operatingPlatform);

        List<EntityDetail> locations = repositoryHandler.getEntitiesForRelationshipType(
                userId,
                entityDetail.getGUID(),
                HOST,
                HOST_LOCATION_GUID,
                HOST_LOCATION,
                0,
                0,
                method);
        if (CollectionUtils.isNotEmpty(locations)) {
            for (EntityDetail location : locations) {
                securityVerifier.validateUserForEntityRead(userId, metadataCollectionName, location);
                addElement(assetElement, location);
                getContextForLocation(userId, assetElement, location);
            }
        }
    }

    private void getContextForLocation(String userId,
                                       AssetElement assetElement,
                                       EntityDetail location)
            throws UserNotAuthorizedException, PropertyServerException, InvalidParameterException, RepositoryErrorException {
        String method = "getContextForLocation";

        List<EntityDetail> assetLocations = repositoryHandler.getEntitiesForRelationshipType(
                userId,
                location.getGUID(),
                LOCATION,
                ASSET_LOCATION_GUID,
                ASSET_LOCATION,
                0,
                0,
                method);

        String metadataCollectionName = getOMRSMetadataCollectionName(userId);
        if (CollectionUtils.isNotEmpty(assetLocations)) {
            for (EntityDetail assetLocation : assetLocations) {
                securityVerifier.validateUserForEntityRead(userId, metadataCollectionName, assetLocation);
                addElement(assetElement, assetLocation);
                getAsset(userId, assetElement, assetLocation);
            }
        }

        List<EntityDetail> nestedLocations = repositoryHandler.getEntitiesForRelationshipType(
                userId,
                location.getGUID(),
                LOCATION,
                NESTED_LOCATION_GUID,
                NESTED_LOCATION,
                0,
                0,
                method);


        if (CollectionUtils.isNotEmpty(nestedLocations)) {
            for (EntityDetail nestedLocation : nestedLocations) {
                securityVerifier.validateUserForEntityRead(userId, metadataCollectionName, nestedLocation);
                addElement(assetElement, nestedLocation);
                getContextForLocation(userId, assetElement, nestedLocation);
            }
        }
    }

    private void getContextForSoftwareServer(String userId,
                                             EntityDetail entityDetail,
                                             AssetElement assetElement)
            throws UserNotAuthorizedException, PropertyServerException, InvalidParameterException, RepositoryErrorException {
        String method = "getContextForSoftwareServer";
        Element parentElement = null;

        EntityDetail softwareServerPlatform = repositoryHandler.getEntityForRelationshipType(
                userId,
                entityDetail.getGUID(),
                SOFTWARE_SERVER,
                SOFTWARE_SERVER_DEPLOYMENT_GUID,
                SOFTWARE_SERVER_DEPLOYMENT,
                method);

        String metadataCollectionName = getOMRSMetadataCollectionName(userId);

        if (softwareServerPlatform != null) {
            securityVerifier.validateUserForEntityRead(userId, metadataCollectionName, softwareServerPlatform);
            parentElement = getLastNode(assetElement);
            addElement(assetElement, softwareServerPlatform);
            getContextForSoftwareServerPlatform(userId, softwareServerPlatform, assetElement);
        }

        EntityDetail endpoint = repositoryHandler.getEntityForRelationshipType(
                userId,
                entityDetail.getGUID(),
                SOFTWARE_SERVER,
                SERVER_ENDPOINT_GUID,
                SERVER_ENDPOINT,
                method);

        if (endpoint != null) {
            securityVerifier.validateUserForEntityRead(userId, metadataCollectionName, softwareServerPlatform);

            if (parentElement != null) {
                addChildElement(parentElement, Collections.singletonList(buildAssetElements(endpoint)));
            } else {
                addContextElement(assetElement, endpoint);
            }
            getConnectionContext(userId, endpoint, assetElement);
        }

    }

    private void getConnectionContext(String userId,
                                      EntityDetail endpoint,
                                      AssetElement assetElement)
            throws UserNotAuthorizedException, PropertyServerException, RepositoryErrorException {
        String method = "getConnectionContext";

        List<EntityDetail> connections = repositoryHandler.getEntitiesForRelationshipType(
                userId,
                endpoint.getGUID(),
                ENDPOINT,
                CONNECTION_ENDPOINT_GUID,
                CONNECTION_ENDPOINT,
                0,
                0,
                method);

        if (CollectionUtils.isEmpty(connections)) {
            return;
        }

        String metadataCollectionName = getOMRSMetadataCollectionName(userId);

        for (EntityDetail connection : connections) {
            securityVerifier.validateUserForEntityRead(userId, metadataCollectionName, connection);
            addElement(assetElement, connection);

            List<EntityDetail> elements = new ArrayList<>();
            EntityDetail connectorType = repositoryHandler.getEntityForRelationshipType(
                    userId,
                    connection.getGUID(),
                    CONNECTION,
                    CONNECTION_CONNECTOR_TYPE_GUID,
                    CONNECTION_CONNECTOR_TYPE,
                    method);

            if (connectorType != null) {
                elements.add(connectorType);
            }

            EntityDetail asset = repositoryHandler.getEntityForRelationshipType(
                    userId,
                    connection.getGUID(),
                    CONNECTION,
                    CONNECTION_TO_ASSET_GUID,
                    CONNECTION_TO_ASSET,
                    method);

            securityVerifier.validateUserForEntityRead(userId, metadataCollectionName, asset);
            elements.add(asset);
            elements.forEach(element -> addElement(assetElement, element));
        }
    }

    private void findAsset(String userId,
                           List<EntityDetail> entitiesByType,
                           AssetElement assetElement)
            throws UserNotAuthorizedException, PropertyServerException, InvalidParameterException, RepositoryErrorException {

        String method = "findAsset";
        for (EntityDetail entityDetail : entitiesByType) {
            List<EntityDetail> schemaAttributes = repositoryHandler.getEntitiesForRelationshipType(
                    userId,
                    entityDetail.getGUID(),
                    SCHEMA_ATTRIBUTE,
                    ATTRIBUTE_FOR_SCHEMA_GUID,
                    ATTRIBUTE_FOR_SCHEMA,
                    0,
                    0,
                    method);
            if (CollectionUtils.isEmpty(schemaAttributes)) {
                continue;
            }

            String metadataCollectionName = getOMRSMetadataCollectionName(userId);
            for (EntityDetail schemaAttribute : schemaAttributes) {
                securityVerifier.validateUserForEntityRead(userId, metadataCollectionName, schemaAttribute);
                addElement(assetElement, schemaAttribute);

                Optional<TypeDef> isComplexSchemaType = isComplexSchemaType(schemaAttribute.getType().getTypeDefName());
                if (isComplexSchemaType.isPresent()) {
                    setAssetDetails(userId, assetElement, schemaAttribute);
                    return;
                } else {
                    List<EntityDetail> schemaAttributeTypeEntities = repositoryHandler.getEntitiesForRelationshipType(
                            userId,
                            schemaAttribute.getGUID(),
                            schemaAttribute.getType().getTypeDefName(),
                            SCHEMA_ATTRIBUTE_TYPE_GUID,
                            SCHEMA_ATTRIBUTE_TYPE,
                            0,
                            0,
                            method);

                    if (CollectionUtils.isNotEmpty(schemaAttributeTypeEntities)) {
                        for (EntityDetail schemaAttributeTypeEntity : schemaAttributeTypeEntities) {
                            securityVerifier.validateUserForEntityRead(userId, metadataCollectionName, schemaAttributeTypeEntity);
                            addElement(assetElement, schemaAttributeTypeEntity);
                        }
                        findAsset(userId, schemaAttributeTypeEntities, assetElement);
                    }
                }
            }
        }
    }

    private void getContextForSchemaType(String userId,
                                         AssetElement assetElement,
                                         EntityDetail entityDetail)
            throws UserNotAuthorizedException, PropertyServerException, InvalidParameterException, RepositoryErrorException {
        Optional<TypeDef> isComplexSchemaType = isComplexSchemaType(entityDetail.getType().getTypeDefName());
        String method = "getContextForSchemaType";

        if (isComplexSchemaType.isPresent()) {
            setAssetDetails(userId, assetElement, entityDetail);
        } else {
            List<EntityDetail> attributeForSchemas = repositoryHandler.getEntitiesForRelationshipType(
                    userId,
                    entityDetail.getGUID(),
                    entityDetail.getType().getTypeDefName(),
                    ATTRIBUTE_FOR_SCHEMA_GUID,
                    ATTRIBUTE_FOR_SCHEMA,
                    0,
                    0,
                    method);
            if (CollectionUtils.isEmpty(attributeForSchemas)) {
                return;
            }

            String metadataCollectionName = getOMRSMetadataCollectionName(userId);
            for (EntityDetail attributeForSchema : attributeForSchemas) {
                securityVerifier.validateUserForEntityRead(userId, metadataCollectionName, attributeForSchema);
                addElement(assetElement, attributeForSchema);

                if (isComplexSchemaType(attributeForSchema.getType().getTypeDefName()).isPresent()) {
                    setAssetDetails(userId, assetElement, attributeForSchema);
                    return;
                } else {
                    List<EntityDetail> schemaAttributeTypeEntities = repositoryHandler.getEntitiesForRelationshipType(
                            userId,
                            attributeForSchema.getGUID(),
                            attributeForSchema.getType().getTypeDefName(),
                            SCHEMA_ATTRIBUTE_TYPE_GUID,
                            SCHEMA_ATTRIBUTE_TYPE,
                            0,
                            0,
                            method);

                    for (EntityDetail schema : schemaAttributeTypeEntities) {
                        securityVerifier.validateUserForEntityRead(userId, metadataCollectionName, schema);
                        addElement(assetElement, schema);
                        getContextForSchemaType(userId, assetElement, schema);
                    }
                }
            }
        }
    }

    private void setAssetDetails(String userId,
                                 AssetElement assetElement,
                                 EntityDetail entity)
            throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException, RepositoryErrorException {
        String method = "setAssetDetails";

        EntityDetail dataSet = repositoryHandler.getEntityForRelationshipType(userId,
                entity.getGUID(),
                entity.getType().getTypeDefName(),
                ASSET_SCHEMA_TYPE_GUID,
                ASSET_SCHEMA_TYPE,
                method);

        String metadataCollectionName = getOMRSMetadataCollectionName(userId);
        securityVerifier.validateUserForEntityRead(userId, metadataCollectionName, dataSet);

        if (assetElement.getContext() != null) {
            addElement(assetElement, dataSet);
        } else {
            assetElement.setContext(Collections.singletonList(buildAssetElements(dataSet)));
        }

        getAsset(userId, assetElement, dataSet);
    }

    private void getAsset(String userId,
                          AssetElement assetElement,
                          EntityDetail dataSet)
            throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException, RepositoryErrorException {
        if (dataSet == null) return;

        String method = "getAsset";
        List<Relationship> assetToDataSetRelationships = repositoryHandler.getRelationshipsByType(userId,
                dataSet.getGUID(),
                dataSet.getType().getTypeDefName(),
                DATA_CONTENT_FOR_DATA_SET_GUID,
                DATA_CONTENT_FOR_DATA_SET,
                method);

        if (CollectionUtils.isEmpty(assetToDataSetRelationships)) {
            return;
        }

        String metadataCollectionName = getOMRSMetadataCollectionName(userId);
        for (Relationship assetToDataSetRelationship : assetToDataSetRelationships) {
            EntityProxy entityOneProxy = assetToDataSetRelationship.getEntityOneProxy();
            securityVerifier.validateUserForEntityProxyRead(userId, metadataCollectionName, entityOneProxy);

            if (entityOneProxy.getGUID().equals(dataSet.getGUID())) {
                setConnections(userId, assetElement, dataSet);
            } else {
                EntityDetail asset = repositoryHandler.getEntityByGUID(userId,
                        entityOneProxy.getGUID(),
                        ASSET_GUID_PARAMETER,
                        entityOneProxy.getType().getTypeDefName(),
                        method);

                if (asset != null) {
                    setAssetElementAttributes(assetElement, asset);
                    setConnections(userId, assetElement, asset);
                }
            }
        }
    }

    private void setConnections(String userId,
                                AssetElement assetElement,
                                EntityDetail asset)
            throws UserNotAuthorizedException, PropertyServerException, RepositoryErrorException {
        List<Connection> connections = getConnections(userId, asset.getGUID());
        assetElement.setConnections(connections);
    }

    private void setAssetElementAttributes(AssetElement assetElement, EntityDetail asset) {
        setElement(assetElement, asset);
    }

    private AssetElements buildAssetElements(EntityDetail entity) {
        if (entity == null) {
            return null;
        }

        AssetElements assetElements = new AssetElements();
        setElement(assetElements, entity);

        return assetElements;
    }

    private void setElement(Element element, EntityDetail entityDetail) {
        element.setGuid(entityDetail.getGUID());
        element.setTypeDefGUID(entityDetail.getType().getTypeDefGUID());
        element.setTypeDefName(entityDetail.getType().getTypeDefName());
        element.setProperties(repositoryHelper.getInstancePropertiesAsMap(entityDetail.getProperties()));
    }

    private Optional<TypeDef> isComplexSchemaType(String typeDefName) {
        List<TypeDef> allTypes = repositoryHelper.getActiveTypeDefs();
        return allTypes.stream().filter(t -> t.getName().equals(typeDefName) && t.getSuperType().getName().equals(COMPLEX_SCHEMA_TYPE)).findAny();
    }

    private List<Connection> getConnections(String userId, String dataSetGuid)
            throws UserNotAuthorizedException, PropertyServerException, RepositoryErrorException {
        String method = "getConnections";
        List<EntityDetail> connections = repositoryHandler.getEntitiesForRelationshipType(
                userId,
                dataSetGuid,
                CONNECTION,
                CONNECTION_TO_ASSET_GUID,
                CONNECTION_TO_ASSET,
                0,
                0,
                method);


        if (CollectionUtils.isNotEmpty(connections)) {
            String metadataCollectionName = getOMRSMetadataCollectionName(userId);
            List<Connection> connectionList = new ArrayList<>();

            for (EntityDetail entityDetail : connections) {
                securityVerifier.validateUserForEntityRead(userId, metadataCollectionName, entityDetail);

                Connection connection = new Connection(
                        entityDetail.getGUID(),
                        repositoryHelper.getStringProperty(ASSET_CATALOG_OMAS,
                                QUALIFIED_NAME, entityDetail.getProperties(), method));
                connectionList.add(connection);
            }
        }

        return Collections.emptyList();
    }

    private Element lastElementAdded(Element tree) {
        List<Element> innerElement = tree.getParentElement();
        if (innerElement == null) {
            return tree;
        }
        return lastElementAdded(innerElement.get(innerElement.size() - 1));
    }

    private void addElement(AssetElement assetElement, EntityDetail entityDetail) {
        List<Element> context = assetElement.getContext();
        List<Element> elements = new ArrayList<>();
        elements.add(buildAssetElements(entityDetail));

        if (context != null) {
            Element leaf = lastElementAdded(context.get(context.size() - 1));
            leaf.setParentElement(elements);
        } else {
            assetElement.setContext(elements);
        }
    }

    private Element getLastNode(AssetElement assetElement) {
        List<Element> context = assetElement.getContext();

        return CollectionUtils.isNotEmpty(context) ? lastElementAdded(context.get(context.size() - 1)) : null;
    }

    private void addChildElement(Element parentElement, List<Element> elements) {
        if (parentElement != null) {
            if (parentElement.getParentElement() != null) {
                parentElement.getParentElement().addAll(elements);
            } else {
                parentElement.setParentElement(elements);
            }
        }
    }

    private void addContextElement(AssetElement assetElement, EntityDetail entityDetail) {
        List<Element> context = assetElement.getContext();
        if (context == null) {
            context = new ArrayList<>();
        }
        context.add(buildAssetElements(entityDetail));
        assetElement.setContext(context);
    }

    private List<Classification> getEntityClassifications(String userId, String assetId, String assetTypeName) throws
            InvalidParameterException, PropertyServerException, UserNotAuthorizedException, RepositoryErrorException {
        String methodName = "getEntityClassifications";

        EntityDetail entityDetails = repositoryHandler.getEntityByGUID(userId, assetId, ASSET_GUID_PARAMETER, assetTypeName, methodName);

        String metadataCollectionName = getOMRSMetadataCollectionName(userId);
        securityVerifier.validateUserForEntityRead(userId, metadataCollectionName, entityDetails);

        return entityDetails.getClassifications();
    }

    private List<EntityDetail> searchEntityByCriteria(String userId,
                                                      String searchCriteria,
                                                      String entityTypeGUID,
                                                      SearchParameters searchParameters, OMRSMetadataCollection metadataCollection)
            throws org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException,
            FunctionNotSupportedException, org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException,
            RepositoryErrorException, PropertyErrorException, TypeErrorException, PagingErrorException {

        List<EntityDetail> entitiesByPropertyValue = metadataCollection.findEntitiesByPropertyValue(userId,
                entityTypeGUID,
                searchCriteria,
                searchParameters.getFrom(),
                Collections.singletonList(InstanceStatus.ACTIVE),
                searchParameters.getLimitResultsByClassification(),
                null,
                searchParameters.getSequencingProperty(),
                searchParameters.getSequencingOrder() == null ? SequencingOrder.ANY : searchParameters.getSequencingOrder(),
                searchParameters.getPageSize());
        if (CollectionUtils.isNotEmpty(entitiesByPropertyValue)) {
            //TODO:
            return entitiesByPropertyValue;
        }
        return new ArrayList<>();
    }

    private List<Classification> filterClassificationByName(List<Classification> classifications, String classificationName) {
        return classifications.stream().filter(classification -> classification.getName().equals(classificationName)).collect(Collectors.toList());
    }

    private InstanceGraph getAssetNeighborhood(String serverName, String userId, String entityGUID, SearchParameters searchParameters)
            throws AssetNotFoundException, PropertyServerException, InvalidParameterException, UserNotAuthorizedException {
        OMRSMetadataCollection metadataCollection = getOMRSMetadataCollection();

        InstanceGraph entityNeighborhood = null;
        String methodName = "getAssetNeighborhood";
        try {
            entityNeighborhood = metadataCollection.getEntityNeighborhood(
                    userId,
                    entityGUID,
                    searchParameters.getEntityTypeGUIDs(),
                    searchParameters.getRelationshipTypeGUIDs(),
                    Collections.singletonList(InstanceStatus.ACTIVE),
                    searchParameters.getLimitResultsByClassification(),
                    null,
                    searchParameters.getLevel());
        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException
                | TypeErrorException
                | FunctionNotSupportedException
                | PropertyErrorException | RepositoryErrorException e) {
            errorHandler.handleRepositoryError(e, methodName);
        } catch (EntityNotKnownException e) {
            errorHandler.handleUnknownEntity(e, entityGUID, searchParameters.getEntityTypeGUIDs().get(0), methodName, ASSET_GUID_PARAMETER);
        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException e) {
            errorHandler.handleUnauthorizedUser(userId, methodName);
        }

        if (entityNeighborhood == null) {
            AssetCatalogErrorCode errorCode = AssetCatalogErrorCode.ASSET_NEIGHBORHOOD_NOT_FOUND;
            String errorMessage = errorCode.getErrorMessageId() +
                    errorCode.getFormattedErrorMessage(entityGUID, serverName);

            throw new AssetNotFoundException(errorCode.getHttpErrorCode(),
                    this.getClass().getName(),
                    "getAssetNeighborhood",
                    errorMessage,
                    errorCode.getSystemAction(),
                    errorCode.getUserAction());
        }

        return entityNeighborhood;
    }

    private Set<String> collectSuperTypes(String userId, String typeDefName) {
        Set<String> superTypes = new HashSet<>();

        TypeDef typeDefByName = repositoryHelper.getTypeDefByName(userId, typeDefName);
        if (typeDefByName != null) {
            collectSuperTypes(userId, typeDefByName, superTypes);
        }

        return superTypes;
    }

    private void collectSuperTypes(String userId, TypeDef type, Set<String> superTypes) {
        if (type.getName().equals(REFERENCEABLE)) {
            return;
        }
        superTypes.add(type.getName());
        TypeDef typeDefByName = repositoryHelper.getTypeDefByName(userId, type.getSuperType().getName());
        if (typeDefByName != null) {
            collectSuperTypes(userId, typeDefByName, superTypes);
        }
    }

    private OMRSMetadataCollection getOMRSMetadataCollection() {
        return repositoryHandler.getMetadataCollection();
    }

    private String getOMRSMetadataCollectionName(String userId) throws RepositoryErrorException {
        OMRSMetadataCollection metadataCollection = getOMRSMetadataCollection();
        String metadataCollectionId = metadataCollection.getMetadataCollectionId(userId);
        if (metadataCollectionId == null) {
            return null;
        }
        return repositoryHelper.getMetadataCollectionName(metadataCollectionId);
    }

}
