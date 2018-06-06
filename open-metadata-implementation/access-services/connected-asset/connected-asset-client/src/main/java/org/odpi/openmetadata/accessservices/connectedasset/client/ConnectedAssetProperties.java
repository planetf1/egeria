/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.accessservices.connectedasset.client;

import org.apache.log4j.Logger;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.properties.AssetUniverse;
import org.odpi.openmetadata.frameworks.connectors.properties.Classifications;
import org.odpi.openmetadata.frameworks.connectors.properties.ElementType;
import org.odpi.openmetadata.frameworks.connectors.properties.Connection;


/**
 * ConnectedAssetProperties is associated with a Connector.  Connectors provide access to
 * assets.   ConnectedAssetProperties returns properties (metadata) about the connector's asset.
 *
 * It is a generic interface for all types of open metadata assets.  However, it assumes the asset's metadata model
 * inherits from <b>Asset</b> (see model 0010 in Area 0).
 *
 * The ConnectedAssetProperties returns metadata about the asset at three levels of detail:
 * <ul>
 *     <li><b>assetSummary</b> - used for displaying details of the asset in summary lists or hover text</li>
 *     <li><b>assetDetail</b> - used to display all of the information known about the asset with summaries
 *     of the relationships to other metadata entities</li>
 *     <li><b>assetUniverse</b> - used to define the broader context for the asset</li>
 * </ul>
 *
 * ConnectedAssetProperties is a base class for the connector's metadata API that returns null,
 * for the asset's properties.  Metadata repository implementations extend this class to add their
 * implementation of the refresh() method that calls to the metadata repository to populate the metadata properties.
 */
public class ConnectedAssetProperties extends org.odpi.openmetadata.frameworks.connectors.properties.ConnectedAssetProperties
{
    private String        omasServerURL;
    private String        connectorInstanceId;
    private Connection    connection;
    private String        userId;

    private ConnectedAsset connectedAsset;

    private static final Logger log = Logger.getLogger(ConnectedAssetProperties.class);

    /**
     * Typical constructor.
     *
     * @param userId - identifier of calling user
     * @param omasServerURL - url of server
     * @param connectorInstanceId - unique identifier of connector.
     * @param connection - connection information for connector.
     */
    public ConnectedAssetProperties(String      userId,
                                    String      omasServerURL,
                                    String      connectorInstanceId,
                                    Connection  connection)
    {
        super();

        this.userId = userId;
        this.omasServerURL = omasServerURL;
        this.connectorInstanceId = connectorInstanceId;
        this.connection = connection;

        this.connectedAsset = new ConnectedAsset(omasServerURL);
    }


    /**
     * Copy/clone constructor.
     *
     * @param templateProperties - template to copy.
     */
    public ConnectedAssetProperties(ConnectedAssetProperties   templateProperties)
    {
        super(templateProperties);

        this.connection = templateProperties.connection;
        this.connectorInstanceId = templateProperties.connectorInstanceId;
        this.omasServerURL = templateProperties.omasServerURL;
        this.userId = templateProperties.userId;

        this.connectedAsset = new ConnectedAsset(omasServerURL);
    }


    /**
     * Request the values in the ConnectedAssetProperties are refreshed with the current values from the
     * metadata repository.
     *
     * @throws PropertyServerException there is a problem connecting to the server to retrieve metadata.
     */

    public void refresh() throws PropertyServerException
    {
        AssetUniverse assetUniverse = null;

        try
        {
            assetUniverse = connectedAsset.getAssetPropertiesByConnection(connection.getGUID());
        }
        catch (Throwable  error)
        {
            /*
             * Construct PropertyErrorException
             */
        }

        if (assetUniverse == null)
        {
            super.assetProperties = null;
            return;
        }

        ElementType       elementType = null;
        Classifications   classifications = null;


        if (assetUniverse.getType() != null)
        {
            elementType = new ElementType(assetUniverse.getType().getElementTypeId(),
                                          assetUniverse.getType().getElementTypeName(),
                                          assetUniverse.getType().getElementTypeVersion(),
                                          assetUniverse.getType().getElementTypeDescription(),
                                          assetUniverse.getType().getElementSourceServer(),
                                          assetUniverse.getType().getElementOrigin(),
                                          assetUniverse.getType().getElementHomeMetadataCollectionId());
        }


        if (assetUniverse.getClassifications() != null)
        {

        }
        super.assetProperties = new AssetUniverse(elementType,
                                                  assetUniverse.getGUID(),
                                                  assetUniverse.getURL(),
                                                  assetUniverse.getQualifiedName(),
                                                  assetUniverse.getDisplayName(),
                                                  assetUniverse.getShortDescription(),
                                                  assetUniverse.getDescription(),
                                                  assetUniverse.getOwner(),
                                                  classifications,
                                                  null,
                                                  null,
                                                  null,
                                                  null,
                                                  null,
                                                  null,
                                                  null,
                                                  null,
                                                  null,
                                                  null,
                                                  null,
                                                  null,
                                                  null,
                                                  null,
                                                  null);
    }
}