/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetowner.api;


import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;

import java.util.List;

/**
 * AssetOnboardingAvroFileInterface provides the client-side interface for an asset owner to set up the metadata about
 * an Avro file asset.  This includes defining its name, source and license.
 */
public interface AssetOnboardingAvroFileInterface
{
    /**
     * Add a simple asset description linked to a connection object for an Avro file.
     *
     * @param userId calling user (assumed to be the owner)
     * @param displayName display name for the file in the catalog
     * @param description description of the file in the catalog
     * @param fullPath full path of the file - used to access the file through the connector
     *
     * @return unique identifier (guid) of the asset description that represents the avro file
     *
     * @throws InvalidParameterException full path or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    String  addAvroFileToCatalog(String userId,
                                 String displayName,
                                 String description,
                                 String fullPath) throws InvalidParameterException,
                                                         UserNotAuthorizedException,
                                                         PropertyServerException;


    /**
     * Ensure the schema associated with an Avro file is correct.
     *
     * @param userId calling user
     * @param assetGUID unique identifier for the Avro file's asset in the catalog
     *
     * @throws InvalidParameterException full path or assetId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    void refreshAvroSchemaInCatalog(String    userId,
                                    String    assetGUID) throws InvalidParameterException,
                                                                UserNotAuthorizedException,
                                                                PropertyServerException;
}
