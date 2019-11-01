/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.accessservices.assetcatalog;

import org.odpi.openmetadata.accessservices.assetcatalog.model.rest.body.SearchParameters;
import org.odpi.openmetadata.accessservices.assetcatalog.model.rest.responses.AssetDescriptionResponse;
import org.odpi.openmetadata.accessservices.assetcatalog.model.rest.responses.AssetResponse;
import org.odpi.openmetadata.accessservices.assetcatalog.model.rest.responses.ClassificationsResponse;
import org.odpi.openmetadata.accessservices.assetcatalog.model.rest.responses.RelationshipResponse;
import org.odpi.openmetadata.accessservices.assetcatalog.model.rest.responses.RelationshipsResponse;
import org.odpi.openmetadata.commonservices.ffdc.exceptions.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;

/**
 * The Asset Catalog Open Metadata Access Service (OMAS) provides services to search for data assets including
 * data stores, event feeds, APIs and data sets. The search will locate assets based on the content of the Asset
 * metadata itself and the metadata that links to it.  This includes:
 * <ul>
 * <li>Glossary terms</li>
 * <li>Schema elements</li>
 * <li>Classifications</li>
 * </ul>
 */
public interface AssetCatalogInterface {

    /**
     * Fetch asset's header, classification and properties
     *
     * @param userId    the unique identifier for the user
     * @param assetGUID the unique identifier for the asset
     * @param assetType the type of the asset
     * @return the asset with its header and the list of associated classifications and specific properties
     * @throws PropertyServerException   if a problem occurs while serving the request
     * @throws InvalidParameterException if parameter validation fails
     */
    AssetDescriptionResponse getAssetDetails(String userId, String assetGUID, String assetType) throws org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException, PropertyServerException;

    /**
     * Fetch asset's header, classification, properties and relationships
     *
     * @param userId    the unique identifier for the user
     * @param assetGUID the unique identifier for the asset
     * @param assetType the asset type
     * @return the asset with its header and the list of associated classifications and relationship
     * @throws PropertyServerException   if a problem occurs while serving the request
     * @throws InvalidParameterException if parameter validation fails
     */
    AssetDescriptionResponse getAssetUniverse(String userId, String assetGUID, String assetType) throws org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException, PropertyServerException;

    /**
     * Fetch the relationships for a specific asset
     *
     * @param userId           the unique identifier for the user
     * @param assetGUID        the unique identifier for the asset
     * @param assetType        the type of the asset
     * @param relationshipType the type of the relationship
     * @param from             offset
     * @param pageSize         limit the number of the assets returned
     * @return list of relationships for the given asset
     * @throws PropertyServerException   if a problem occurs while serving the request
     * @throws InvalidParameterException if parameter validation fails
     */
    RelationshipsResponse getAssetRelationships(String userId, String assetGUID, String assetType, String relationshipType, Integer from, Integer pageSize) throws org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException, PropertyServerException;

    /**
     * Fetch the classification for a specific asset
     *
     * @param userId             the unique identifier for the user
     * @param assetGUID          the unique identifier for the asset
     * @param assetType          the type of the asset
     * @param classificationName the name of the classification
     * @return ClassificationsResponse the classification for the asset
     * @throws PropertyServerException   if a problem occurs while serving the request
     * @throws InvalidParameterException if parameter validation fails
     */
    ClassificationsResponse getClassificationsForAsset(String userId, String assetGUID, String assetType, String classificationName) throws org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException, PropertyServerException;

    /**
     * Returns a sub-graph of intermediate assets that connected two assets
     *
     * @param userId         the unique identifier for the user
     * @param startAssetGUID the starting asset identifier of the query
     * @param endAssetGUID   the ending asset identifier of the query
     * @return a list of assets between the given assets
     * @throws PropertyServerException   if a problem occurs while serving the request
     * @throws InvalidParameterException if parameter validation fails
     */
    AssetDescriptionResponse getLinkingAssets(String userId, String startAssetGUID, String endAssetGUID) throws org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException, PropertyServerException;

    /**
     * Return a sub-graph of relationships that connect two assets
     *
     * @param userId         the unique identifier for the user
     * @param startAssetGUID the starting asset identifier of the query
     * @param endAssetGUID   the ending asset identifier of the query
     * @return a list of relationships that connects the assets
     * @throws PropertyServerException   if a problem occurs while serving the request
     * @throws InvalidParameterException if parameter validation fails
     */
    RelationshipsResponse getLinkingRelationships(String userId, String startAssetGUID, String endAssetGUID) throws org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException, PropertyServerException;

    /**
     * Returns the sub-graph that represents the list of assets that in neighborhood of the given asset
     *
     * @param userId           the unique identifier for the user
     * @param assetGUID        the starting asset identifier of the query
     * @param searchParameters constrains to make the assets's search results more precise
     * @return a list of assets that in neighborhood of the given asset
     * @throws PropertyServerException   if a problem occurs while serving the request
     * @throws InvalidParameterException if parameter validation fails
     */
    AssetDescriptionResponse getAssetsFromNeighborhood(String userId, String assetGUID, SearchParameters searchParameters) throws org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException, PropertyServerException;

    /**
     * Return a list of assets matching the search criteria without the full context
     *
     * @param userId           the unique identifier for the user
     * @param searchCriteria   a string expression of the characteristics of the required assets
     * @param searchParameters constrains to make the assets's search results more precise
     * @return list of properties used to narrow the search
     * @throws PropertyServerException   if a problem occurs while serving the request
     * @throws InvalidParameterException if parameter validation fails
     */
    AssetResponse searchByType(String userId, String searchCriteria, SearchParameters searchParameters) throws org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException, PropertyServerException;

    /**
     * Return the full context of an asset/glossary term based on its identifier.
     * The response contains the list of the connections assigned to the asset.
     *
     * @param userId    the unique identifier for the user
     * @param assetGUID the global unique identifier of the asset
     * @param assetType the type of the asset
     * @return list of properties used to narrow the search
     * @throws PropertyServerException   if a problem occurs while serving the request
     * @throws InvalidParameterException if parameter validation fails
     */
    AssetResponse getAssetContext(String userId, String assetGUID, String assetType) throws org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException, PropertyServerException;


    /**
     * Fetch relationship between entities details based on its unique identifier of the ends
     * Filtering based on the relationship type is supported
     *
     * @param userId           the unique identifier for the user
     * @param entity1GUID      Entity guid of the first end of the relationship
     * @param entity2GUID      Entity guid of the second end of the relationship
     * @param relationshipType Type of the relationship
     * @return relationships between entities
     * @throws PropertyServerException   if a problem occurs while serving the request
     * @throws InvalidParameterException if parameter validation fails
     */
    RelationshipResponse getRelationshipBetweenEntities(String userId, String entity1GUID, String entity2GUID, String relationshipType) throws org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException, PropertyServerException;
}