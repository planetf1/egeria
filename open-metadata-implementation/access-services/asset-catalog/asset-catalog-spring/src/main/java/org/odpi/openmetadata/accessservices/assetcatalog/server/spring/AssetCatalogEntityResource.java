/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetcatalog.server.spring;

import org.odpi.openmetadata.accessservices.assetcatalog.model.rest.body.SearchParameters;
import org.odpi.openmetadata.accessservices.assetcatalog.model.rest.responses.AssetDescriptionResponse;
import org.odpi.openmetadata.accessservices.assetcatalog.model.rest.responses.AssetResponse;
import org.odpi.openmetadata.accessservices.assetcatalog.model.rest.responses.ClassificationsResponse;
import org.odpi.openmetadata.accessservices.assetcatalog.model.rest.responses.RelationshipsResponse;
import org.odpi.openmetadata.accessservices.assetcatalog.service.AssetCatalogRESTService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

/**
 * The AssetCatalogEntityResource provides the server-side implementation of the
 * Asset Catalog Open Metadata Assess Service (OMAS).
 * This interface facilitates the searching for assets, provides details about specific assets.
 */
@RestController
@RequestMapping("/servers/{serverName}/open-metadata/access-services/asset-catalog/users/{userId}")
public class AssetCatalogEntityResource {

    private AssetCatalogRESTService assetService = new AssetCatalogRESTService();

    /**
     * Fetch asset's header, classification and properties
     *
     * @param serverName unique identifier for requested server.
     * @param userId     the unique identifier for the user
     * @param assetGUID  the unique identifier for the asset
     * @param assetType  the type of the asset
     * @return the asset with its header and the list of associated classifications and specific properties
     */
    @GetMapping(path = "/asset-details/{assetGUID}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public AssetDescriptionResponse getAssetDetail(@PathVariable("serverName") String serverName,
                                                   @PathVariable("userId") String userId,
                                                   @PathVariable("assetGUID") @NotBlank String assetGUID,
                                                   @RequestParam(name = "assetType") @NotNull String assetType) {
        return assetService.getAssetDetailsByGUID(serverName, userId, assetGUID, assetType);
    }

    /**
     * Fetch asset's header, classification, properties and relationships
     *
     * @param serverName unique identifier for requested server.
     * @param userId     the unique identifier for the user
     * @param assetGUID  the unique identifier for the asset
     * @param assetType  the asset type
     * @return the asset with its header and the list of associated classifications and relationship
     */
    @GetMapping(path = "/asset-universe/{assetGUID}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public AssetDescriptionResponse getAssetUniverse(@PathVariable("serverName") String serverName,
                                                     @PathVariable("userId") String userId,
                                                     @PathVariable("assetGUID") @NotBlank String assetGUID,
                                                     @RequestParam(name = "assetType") @NotNull String assetType) {
        return assetService.getAssetUniverseByGUID(serverName, userId, assetGUID, assetType);
    }

    /**
     * Fetch the relationships for a specific asset
     *
     * @param serverName       unique identifier for requested server
     * @param userId           the unique identifier for the user
     * @param assetGUID        the unique identifier for the asset
     * @param assetType        the type of the asset
     * @param relationshipType the type of the relationship
     * @param from             offset
     * @param pageSize         limit the number of the assets returned
     * @return list of relationships for the given asset
     */
    @GetMapping(path = "/asset-relationships/{assetGUID}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public RelationshipsResponse getAssetRelationships(@PathVariable("serverName") String serverName,
                                                       @PathVariable("userId") String userId,
                                                       @PathVariable("assetGUID") @NotBlank String assetGUID,
                                                       @RequestParam(name = "assetType") @NotNull String assetType,
                                                       @RequestParam(name = "relationshipType") String relationshipType,
                                                       @RequestParam(name = "from") @PositiveOrZero Integer from,
                                                       @RequestParam(name = "pageSize") @PositiveOrZero Integer pageSize) {
        return assetService.getAssetRelationships(serverName, userId, assetGUID, assetType, relationshipType, from, pageSize);
    }

    /**
     * Fetch the classification for a specific asset
     *
     * @param serverName         unique identifier for requested server.
     * @param userId             the unique identifier for the user
     * @param assetGUID          the unique identifier for the asset
     * @param assetType          the type of the asset
     * @param classificationName the name of the classification
     * @return ClassificationsResponse the classification for the asset
     */
    @GetMapping(path = "/asset-classifications/{assetGUID}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ClassificationsResponse getClassificationsForAsset(@PathVariable("serverName") String serverName,
                                                              @PathVariable("userId") String userId,
                                                              @PathVariable("assetGUID") @NotBlank String assetGUID,
                                                              @RequestParam(name = "assetType") @NotNull String assetType,
                                                              @RequestParam(name = "classificationName") String classificationName) {
        return assetService.getClassificationByAssetGUID(serverName, userId, assetGUID, assetType, classificationName);
    }

    /**
     * Returns a sub-graph of intermediate assets that connected two assets
     *
     * @param serverName     unique identifier for requested server.
     * @param userId         the unique identifier for the user
     * @param startAssetGUID the starting asset identifier of the query
     * @param endAssetGUID   the ending asset identifier of the query
     * @return a list of assets between the given assets
     */
    @GetMapping(path = "/linking-assets/from/{assetGUID}/to/{endAssetGUID}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public AssetDescriptionResponse getLinkingAssets(@PathVariable("serverName") String serverName,
                                                     @PathVariable("userId") String userId,
                                                     @PathVariable("assetGUID") @NotBlank String startAssetGUID,
                                                     @PathVariable("endAssetGUID") @NotBlank String endAssetGUID) {
        return assetService.getLinkingAssets(serverName, userId, startAssetGUID, endAssetGUID);
    }

    /**
     * Return a sub-graph of relationships that connect two assets
     *
     * @param serverName     unique identifier for requested server.
     * @param userId         the unique identifier for the user
     * @param startAssetGUID the starting asset identifier of the query
     * @param endAssetGUID   the ending asset identifier of the query
     * @return a list of relationships that connects the assets
     */
    @GetMapping(path = "/linking-assets-relationships/from/{assetGUID}/to/{endAssetGUID}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public RelationshipsResponse getLinkingRelationships(@PathVariable("serverName") String serverName,
                                                         @PathVariable("userId") String userId,
                                                         @PathVariable("assetGUID") String startAssetGUID,
                                                         @PathVariable("endAssetGUID") String endAssetGUID) {
        return assetService.getLinkingRelationships(serverName, userId, startAssetGUID, endAssetGUID);
    }

    /**
     * Returns the sub-graph that represents the returned linked relationships.
     *
     * @param serverName       unique identifier for requested server.
     * @param userId           the unique identifier for the user
     * @param assetGUID        the starting asset identifier of the query
     * @param searchParameters constrains to make the assets's search results more precise
     * @return a list of assets that in neighborhood of the given asset
     */
    @PostMapping(path = "/assets-from-neighborhood/{assetGUID}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public AssetDescriptionResponse getAssetsFromNeighborhood(@PathVariable("serverName") String serverName,
                                                              @PathVariable("userId") String userId,
                                                              @PathVariable("assetGUID") @NotBlank String assetGUID,
                                                              @RequestBody SearchParameters searchParameters) {
        return assetService.getAssetsFromNeighborhood(serverName, userId, assetGUID, searchParameters);
    }

    /**
     * Return a list of assets matching the search criteria without the full context
     *
     * @param serverName       unique identifier for requested server.
     * @param userId           the unique identifier for the user
     * @param searchCriteria   a string expression of the characteristics of the required assets
     * @param searchParameters constrains to make the assets's search results more precise
     * @return list of properties used to narrow the search
     */
    @PostMapping(path = "/search/{searchCriteria}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public AssetResponse searchByType(@PathVariable("serverName") String serverName,
                                      @PathVariable("userId") String userId,
                                      @PathVariable("searchCriteria") @NotBlank String searchCriteria,
                                      @RequestBody SearchParameters searchParameters) {
        return assetService.searchByType(serverName, userId, searchCriteria, searchParameters);
    }


    /**
     * Return the full context of an asset/glossary term based on its identifier.
     * The response contains the list of the connections assigned to the asset.
     *
     * @param serverName unique identifier for requested server.
     * @param userId     the unique identifier for the user
     * @param assetGUID  the global unique identifier of the asset
     * @param assetType  the type of the asset
     * @return list of properties used to narrow the search
     */
    @GetMapping(path = "/asset-context/{assetGUID}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public AssetResponse getAssetContext(@PathVariable("serverName") String serverName,
                                         @PathVariable("userId") String userId,
                                         @PathVariable("assetGUID") @NotBlank String assetGUID,
                                         @RequestParam(name = "assetType") @NotNull String assetType) {
        return assetService.buildContext(serverName, userId, assetGUID, assetType);
    }
}