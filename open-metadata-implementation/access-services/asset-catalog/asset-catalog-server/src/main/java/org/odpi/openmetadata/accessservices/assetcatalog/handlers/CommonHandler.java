/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetcatalog.handlers;

import org.apache.commons.collections4.CollectionUtils;
import org.odpi.openmetadata.accessservices.assetcatalog.model.Type;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryErrorHandler;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryHandler;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.OMRSMetadataCollection;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefLink;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.EntityNotKnownException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.EntityProxyOnlyException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.RepositoryErrorException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.odpi.openmetadata.accessservices.assetcatalog.util.Constants.ASSET_CATALOG_OMAS;
import static org.odpi.openmetadata.accessservices.assetcatalog.util.Constants.GUID_PARAMETER;
import static org.odpi.openmetadata.accessservices.assetcatalog.util.Constants.REFERENCEABLE;

/**
 * Common  Handler supports the lookup types and metadata collection.
 * These methods are used in the multiple handlers.
 * It runs on the server-side of the Asset Catalog OMAS, fetches the types, metadata collection using the RepositoryHandler.
 */
public class CommonHandler {

    private final RepositoryHandler repositoryHandler;
    private final OMRSRepositoryHelper repositoryHelper;
    private final RepositoryErrorHandler errorHandler;

    CommonHandler(RepositoryHandler repositoryHandler, OMRSRepositoryHelper repositoryHelper, RepositoryErrorHandler errorHandler) {
        this.repositoryHandler = repositoryHandler;
        this.repositoryHelper = repositoryHelper;
        this.errorHandler = errorHandler;
    }

    OMRSMetadataCollection getOMRSMetadataCollection() {
        return repositoryHandler.getMetadataCollection();
    }

    List<Type> getTypeContext(String userId, String typeDefName) {
        List<Type> response = new ArrayList<>();
        TypeDef typeDefByName = repositoryHelper.getTypeDefByName(userId, typeDefName);

        if (typeDefByName != null) {
            List<TypeDef> activeTypeDefs = repositoryHelper.getActiveTypeDefs();

            Type type = convertType(typeDefByName);
            List<Type> subTypes = getSubTypes(activeTypeDefs, type);
            response.add(type);
            response.addAll(subTypes);

            collectSubTypes(subTypes, activeTypeDefs, response);
        }

        return response;
    }

    /**
     * Returns the global identifier of the type using the type def name
     *
     * @param userId      user identifier that issues the call
     * @param typeDefName the type definition name
     * @return the global identifier of the type
     */
    public String getTypeDefGUID(String userId, String typeDefName) {
        if (typeDefName == null) {
            return null;
        }

        TypeDef typeDefByName = repositoryHelper.getTypeDefByName(userId, typeDefName);
        return Optional.ofNullable(typeDefByName).map(TypeDefLink::getGUID).orElse(null);
    }

    /**
     * Fetch the zone membership property
     *
     * @param properties asset properties
     * @return the list that contains the zone membership
     */
    public List<String> getAssetZoneMembership(InstanceProperties properties) {
        String methodName = "getAssetZoneMembership";
        List<String> zoneMembership = repositoryHelper.getStringArrayProperty(ASSET_CATALOG_OMAS, "zoneMembership", properties, methodName);
        if (CollectionUtils.isNotEmpty(zoneMembership)) {
            return zoneMembership;
        }
        return Collections.emptyList();
    }

    /**
     * Return the requested entity, converting any errors from the repository services into the local
     * OMAS exceptions.
     *
     * @param userId calling user
     * @param guid   unique identifier for the entity
     * @return entity detail object
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException    problem retrieving the entity.
     */
    public EntityDetail getEntityByGUID(String userId,
                                        String guid,
                                        String type) throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException {

        String entityTypeName = type == null ? "Unknown" : type;
        String methodName = "getEntityByGUID";
        try {
            return repositoryHandler.getMetadataCollection().getEntityDetail(userId, guid);
        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException e) {
            e.printStackTrace();
        } catch (RepositoryErrorException e) {
            errorHandler.handleRepositoryError(e, methodName);
        } catch (EntityNotKnownException e) {
            errorHandler.handleUnknownEntity(e, guid, entityTypeName, methodName, GUID_PARAMETER);
        } catch (EntityProxyOnlyException e) {
            errorHandler.handleEntityProxy(e, guid, entityTypeName, methodName, GUID_PARAMETER);
        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException e) {
            errorHandler.handleUnauthorizedUser(userId, methodName);
        }

        return null;
    }

    List<String> getTypesGUID(String userId, List<String> types) {
        if (CollectionUtils.isEmpty(types)) {
            return Collections.emptyList();
        }
        return types.stream().map(type -> repositoryHelper.getTypeDefByName(userId, type).getGUID()).collect(Collectors.toList());
    }

    private void collectSubTypes(List<Type> types, List<TypeDef> activeTypeDefs, List<Type> collector) {
        for (Type currentSubType : types) {
            List<Type> subTypes = getSubTypes(activeTypeDefs, currentSubType);
            collector.addAll(subTypes);
            collectSubTypes(subTypes, activeTypeDefs, collector);
        }
    }

    private List<Type> getSubTypes(List<TypeDef> activeTypeDefs, Type type) {
        String typeName = type.getName();

        List<Type> subTypes = new ArrayList<>();
        for (TypeDef typeDef : activeTypeDefs) {
            if (typeDef.getSuperType() != null && typeDef.getSuperType().getName().equals(typeName)) {
                subTypes.add(convertType(typeDef));
            }
        }
        return subTypes;
    }

    private Type convertType(TypeDef openType) {
        Type type = new Type();
        buildType(openType, type);
        return type;
    }

    private void buildType(TypeDef openType, Type type) {
        type.setName(openType.getName());
        type.setDescription(openType.getDescription());
        type.setVersion(openType.getVersion());
        type.setSuperType(openType.getSuperType().getName());
    }

    Set<String> collectSuperTypes(String userId, String typeDefName) {
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
}
