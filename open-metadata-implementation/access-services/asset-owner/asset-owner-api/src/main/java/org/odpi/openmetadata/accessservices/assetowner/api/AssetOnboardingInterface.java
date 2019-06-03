/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetowner.api;


import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.SchemaAttribute;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.SchemaType;

import java.util.List;
import java.util.Map;

/**
 * AssetOnboardingInterface provides the client-side interface for an asset owner to set up the metadata about their
 * asset.  This includes defining its name, source and license.
 */
public interface AssetOnboardingInterface
{
    /**
     * Add a simple asset description to the catalog.
     *
     * @param userId calling user (assumed to be the owner)
     * @param typeName specific type of the asset - this must match a defined subtype
     * @param qualifiedName unique name for the asset in the catalog
     * @param displayName display name for the asset in the catalog
     * @param description description of the asset in the catalog
     *
     * @return unique identifier (guid) of the asset
     *
     * @throws InvalidParameterException full path or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    String  addAssetToCatalog(String               userId,
                              String               typeName,
                              String               qualifiedName,
                              String               displayName,
                              String               description,
                              Map<String, String>  additionalProperties,
                              Map<String, Object>  extendedProperties) throws InvalidParameterException,
                                                                              UserNotAuthorizedException,
                                                                              PropertyServerException;

    /**
     * Links the supplied schema to the asset.  If the schema is not defined in the metadata repository, it
     * is created.
     *
     * @param userId calling user
     * @param assetGUID unique identifier of the asset that the schema is to be attached to
     * @param schemaType schema to attach - a new schema is always created because schema can not be shared
     *                   between assets.
     *
     * @throws InvalidParameterException full path or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    String   addSchemaToAsset(String      userId,
                              String      assetGUID,
                              SchemaType  schemaType) throws InvalidParameterException,
                                                             UserNotAuthorizedException,
                                                             PropertyServerException;

    /**
     * Adds attributes to a complex schema type like a relational table or a structured document.
     * This method can be called repeatedly to add many attributes to a schema.
     *
     * @param userId calling user
     * @param schemaTypeGUID unique identifier if the schema to anchor these attributes to.
     * @param schemaAttributes list of schema attribute objects.
     */
    void   addSchemaAttributesToSchema(String                 userId,
                                       String                 schemaTypeGUID,
                                       List<SchemaAttribute>  schemaAttributes) throws InvalidParameterException,
                                                                                       UserNotAuthorizedException,
                                                                                       PropertyServerException;


    /**
     * Adds a connection to an asset.  Assets can have multiple connections attached.
     *
     * @param userId calling user
     * @param assetGUID unique identifier of the attest to attach the connection to
     * @param connection connection object.  If the connection is already stored (matching guid)
     *                   then the existing connection is used.
     * @throws InvalidParameterException full path or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    void addConnectionToAsset(String        userId,
                              String        assetGUID,
                              Connection    connection) throws InvalidParameterException,
                                                               UserNotAuthorizedException,
                                                               PropertyServerException;
}
