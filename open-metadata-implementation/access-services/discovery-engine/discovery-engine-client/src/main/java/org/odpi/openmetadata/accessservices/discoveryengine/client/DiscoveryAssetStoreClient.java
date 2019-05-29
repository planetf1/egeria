/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.discoveryengine.client;

import org.odpi.openmetadata.frameworks.connectors.ffdc.*;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.frameworks.discovery.DiscoveryAssetStore;


/**
 * DiscoveryAssetStoreClient provides the client-side library for the Open Discovery Framework (ODF)'s
 * Discovery Asset Store that provides a Discovery service with access to the connector for the
 * asset to be discovered.  From the connector, the Discovery service is able to extract the known properties
 * about the asset and access its data.
 *
 * An instance of this client is created for each discovery service instance that runs.  This is
 * why the REST client is passed in on the constructor (since creating a new RestTemplate object is
 * very expensive).
 */
public class DiscoveryAssetStoreClient extends DiscoveryAssetStore
{
    private DiscoveryEngineClient discoveryEngineClient;    /* Initialized in constructor */


    /**
     * Constructor sets up the key parameters for accessing the asset store.
     *
     * @param userId calling user
     * @param assetGUID unique identifier of the asset that the annotations should be attached to
     * @param discoveryEngineClient client for calling REST APIs
     */
    public DiscoveryAssetStoreClient(String                assetGUID,
                                     String                userId,
                                     DiscoveryEngineClient discoveryEngineClient)
    {
        super(assetGUID, userId);

        this.discoveryEngineClient = discoveryEngineClient;
    }


    /**
     * Return the connection information for the asset.  This is used to create the connector.  The connector
     * is an Open Connector Framework (OCF) connector that provides access to the asset's data and metadata properties.
     *
     * @return Connection bean
     * @throws InvalidParameterException the asset guid is not recognized
     * @throws UserNotAuthorizedException the user is not authorized to access the asset and/or connection
     * @throws PropertyServerException there was a problem in the store whether the asset/connection properties are kept.
     */
    protected  Connection  getConnectionForAsset() throws InvalidParameterException,
                                                          UserNotAuthorizedException,
                                                          PropertyServerException
    {
        return discoveryEngineClient.getConnectionForAsset(userId, assetGUID);
    }
}
