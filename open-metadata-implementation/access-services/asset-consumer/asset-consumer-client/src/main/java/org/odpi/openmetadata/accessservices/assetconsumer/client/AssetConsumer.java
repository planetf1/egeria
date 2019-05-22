/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetconsumer.client;

import org.odpi.openmetadata.accessservices.assetconsumer.api.*;
import org.odpi.openmetadata.accessservices.assetconsumer.ffdc.AssetConsumerErrorCode;
import org.odpi.openmetadata.accessservices.assetconsumer.properties.GlossaryTerm;
import org.odpi.openmetadata.accessservices.assetconsumer.rest.*;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
import org.odpi.openmetadata.commonservices.ffdc.rest.GUIDResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.NullRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.VoidResponse;
import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.client.ConnectedAssetProperties;
import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.client.ConnectedAssetUniverse;
import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.rest.AssetResponse;
import org.odpi.openmetadata.frameworks.connectors.Connector;
import org.odpi.openmetadata.frameworks.connectors.ConnectorBroker;
import org.odpi.openmetadata.frameworks.connectors.ffdc.*;
import org.odpi.openmetadata.frameworks.connectors.properties.AssetUniverse;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.*;

import java.util.ArrayList;
import java.util.List;

/**
 * The Asset Consumer Open Metadata Access Service (OMAS) is used by applications and tools as a factory for Open
 * Connector Framework (OCF) connectors.  The configuration for the connectors is managed as open metadata in
 * a Connection definition.  The caller to the Asset Consumer OMAS passes either the name, GUID or URL for the
 * connection to the appropriate method to retrieve a connector.  The Asset Consumer OMAS retrieves the connection
 * from the metadata repository and creates an appropriate connector as described the connection and
 * returns it to the caller.
 *
 * The Asset Consumer OMAS supports access to the asset properties either through the connector, or by a direct
 * call to Asset Consumer API.  It is also possible to look up the definitions of terms associated with the assets.
 *
 * It supports the ability to add and remove feedback for an asset.
 * This feedback may be in the form of reviews, likes and comments.
 * Asset Consumer OMAS also supports the maintenance of informal tags and their attachments to assets.
 *
 * Finally, Asset Consumer OMAS supports the ability to add audit log records to the local server's audit log
 * about an asset.
 */
public class AssetConsumer implements AssetConsumerAssetInterface,
                                      AssetConsumerConnectorFactoryInterface,
                                      AssetConsumerFeedbackInterface,
                                      AssetConsumerGlossaryInterface,
                                      AssetConsumerLoggingInterface,
                                      AssetConsumerTaggingInterface
{
    private String     serverName;               /* Initialized in constructor */
    private String     omasServerURL;            /* Initialized in constructor */
    private RESTClient restClient;               /* Initialized in constructor */

    private InvalidParameterHandler invalidParameterHandler = new InvalidParameterHandler();
    private RESTExceptionHandler    exceptionHandler        = new RESTExceptionHandler();
    private NullRequestBody         nullRequestBody         = new NullRequestBody();


    /**
     * Create a new client with no authentication embedded in the HTTP request.
     *
     * @param serverName name of the server to connect to
     * @param omasServerURL the network address of the server running the OMAS REST servers
     * @throws InvalidParameterException null URL or server name
     */
    public AssetConsumer(String     serverName,
                         String     omasServerURL) throws InvalidParameterException
    {
        final String   methodName = "AssetConsumer client constructor";

        invalidParameterHandler.validateOMAGServerPlatformURL(omasServerURL, serverName, methodName);

        this.serverName = serverName;
        this.omasServerURL = omasServerURL;
        this.restClient = new RESTClient(serverName, omasServerURL);
    }


    /**
     * Create a new client that passes userId and password in each HTTP request.  This is the
     * userId/password of the calling server.  The end user's userId is sent on each request.
     *
     * @param serverName name of the server to connect to
     * @param omasServerURL the network address of the server running the OMAS REST servers
     * @param userId caller's userId embedded in all HTTP requests
     * @param password caller's userId embedded in all HTTP requests
     * @throws InvalidParameterException null URL or server name
     */
    public AssetConsumer(String     serverName,
                         String     omasServerURL,
                         String     userId,
                         String     password) throws InvalidParameterException
    {
        final String   methodName = "AssetConsumer client constructor with security";

        invalidParameterHandler.validateOMAGServerPlatformURL(omasServerURL, serverName, methodName);
        this.serverName = serverName;
        this.omasServerURL = omasServerURL;
        this.restClient = new RESTClient(serverName, omasServerURL, userId, password);
    }


    /*
     * ===============================================
     * AssetConsumerAssetInterface
     * ===============================================
     */


    /**
     * Returns the unique identifier for the asset connected to the requested connection.
     *
     * @param userId the userId of the requesting user.
     * @param connectionGUID  unique identifier for the connection.
     *
     * @return unique identifier of asset.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem retrieving information from the property server.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public String  getAssetForConnection(String   userId,
                                         String   connectionGUID) throws InvalidParameterException,
                                                                         PropertyServerException,
                                                                         UserNotAuthorizedException
    {
        final String   methodName = "getAssetForConnection";
        final String   guidParameter = "connectionGUID";
        final String   urlTemplate = "/servers/{0}/open-metadata/access-services/asset-consumer/users/{1}/assets/by-connection/{2}";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(connectionGUID, guidParameter, methodName);

        GUIDResponse restResult = restClient.callGUIDGetRESTCall(methodName,
                                                                 omasServerURL + urlTemplate,
                                                                 serverName,
                                                                 userId,
                                                                 connectionGUID);

        exceptionHandler.detectAndThrowInvalidParameterException(methodName, restResult);
        exceptionHandler.detectAndThrowUserNotAuthorizedException(methodName, restResult);
        exceptionHandler.detectAndThrowPropertyServerException(methodName, restResult);

        return restResult.getGUID();
    }


    /**
     * Returns the asset corresponding to the supplied connection name.
     *
     * @param userId           userId of user making request.
     * @param connectionName   this may be the qualifiedName or displayName of the connection.
     *
     * @return unique identifier of asset.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public String  getAssetForConnectionName(String userId,
                                             String connectionName) throws InvalidParameterException,
                                                                           PropertyServerException,
                                                                           UserNotAuthorizedException
    {
        final String   methodName = "getAssetForConnectionName";
        final String   nameParameter = "connectionName";
        final String   urlTemplate = "/servers/{0}/open-metadata/access-services/asset-consumer/users/{1}/assets/by-connection-name/{2}";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(connectionName, nameParameter, methodName);

        GUIDResponse restResult = restClient.callGUIDGetRESTCall(methodName,
                                                                 omasServerURL + urlTemplate,
                                                                 serverName,
                                                                 userId,
                                                                 connectionName);

        exceptionHandler.detectAndThrowInvalidParameterException(methodName, restResult);
        exceptionHandler.detectAndThrowUserNotAuthorizedException(methodName, restResult);
        exceptionHandler.detectAndThrowPropertyServerException(methodName, restResult);

        return restResult.getGUID();
    }


    /**
     * Return a list of assets with the requested name.
     *
     * @param userId calling user
     * @param guid unique identifier of asset
     * @param methodName calling method
     *
     * @return Asset bean
     *
     * @throws InvalidParameterException the name is invalid
     * @throws PropertyServerException there is a problem access in the property server
     * @throws UserNotAuthorizedException the user does not have access to the properties
     */
    private Asset getAssetByGUID(String   userId,
                                 String   guid,
                                 String   methodName) throws InvalidParameterException,
                                                             PropertyServerException,
                                                             UserNotAuthorizedException
    {
        final String   urlTemplate = "/servers/{0}/open-metadata/common-services/ocf/connected-asset/users/{1}/assets/{2}";

        AssetResponse restResult = restClient.callAssetGetRESTCall(methodName,
                                                                  omasServerURL + urlTemplate,
                                                                  serverName,
                                                                  userId,
                                                                  guid);

        exceptionHandler.detectAndThrowInvalidParameterException(methodName, restResult);
        exceptionHandler.detectAndThrowUserNotAuthorizedException(methodName, restResult);
        exceptionHandler.detectAndThrowPropertyServerException(methodName, restResult);

        return restResult.getAsset();
    }


    /**
     * Return a list of assets with the requested name.
     *
     * @param userId calling user
     * @param name name to search for
     * @param startFrom starting element (used in paging through large result sets)
     * @param pageSize maximum number of results to return
     * @param methodName calling method
     *
     * @return list of unique identifiers of assets with matching name.
     *
     * @throws InvalidParameterException the name is invalid
     * @throws PropertyServerException there is a problem access in the property server
     * @throws UserNotAuthorizedException the user does not have access to the properties
     */
    private List<Asset> getAssetsByName(String   userId,
                                        String   name,
                                        int      startFrom,
                                        int      pageSize,
                                        String   methodName) throws InvalidParameterException,
                                                                    PropertyServerException,
                                                                    UserNotAuthorizedException
    {
        final String urlTemplate = "/servers/{0}/open-metadata/access-services/asset-consumer/users/{1}/assets/by-name/{2}?startFrom={3}&pageSize={4}";

        AssetListResponse restResult = restClient.callAssetListGetRESTCall(methodName,
                                                                           omasServerURL + urlTemplate,
                                                                           serverName,
                                                                           userId,
                                                                           name,
                                                                           startFrom,
                                                                           pageSize);

        exceptionHandler.detectAndThrowInvalidParameterException(methodName, restResult);
        exceptionHandler.detectAndThrowUserNotAuthorizedException(methodName, restResult);
        exceptionHandler.detectAndThrowPropertyServerException(methodName, restResult);

        return restResult.getAssets();
    }


    /**
     * Return a list of GUIDs based on a list of Assets.
     *
     * @param assets list of assets
     * @return list of GUIDs
     */
    private List<String>   getGUIDs(List<Asset>  assets)
    {
        if ((assets != null) && (! assets.isEmpty()))
        {
            List<String>  results = new ArrayList<>();

            for (Asset asset : assets)
            {
                if (asset != null)
                {
                    String guid = asset.getGUID();

                    if (guid != null)
                    {
                        results.add(guid);
                    }
                }
            }

            if (! results.isEmpty())
            {
                return  results;
            }

        }

        return null;
    }


    /**
     * Return a list of assets with the requested name.
     *
     * @param userId calling user
     * @param name name to search for
     * @param startFrom starting element (used in paging through large result sets)
     * @param pageSize maximum number of results to return
     *
     * @return list of unique identifiers of assets with matching name.
     *
     * @throws InvalidParameterException the name is invalid
     * @throws PropertyServerException there is a problem access in the property server
     * @throws UserNotAuthorizedException the user does not have access to the properties
     */
    public List<String> getAssetsByName(String   userId,
                                        String   name,
                                        int      startFrom,
                                        int      pageSize) throws InvalidParameterException,
                                                                  PropertyServerException,
                                                                  UserNotAuthorizedException
    {
        final String   methodName = "getAssetsByName";
        final String   nameParameter = "name";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(name, nameParameter, methodName);

        List<Asset>  assets = this.getAssetsByName(userId, name, startFrom, pageSize, methodName);

        return this.getGUIDs(assets);
    }


    /**
     * Returns a list of assets that match the token. The following calls are issued in
     * in order for find the asset.
     * - getAssetProperties passing the token as the GUID
     * - getAssetByName passing the token as the name
     *
     * @param userId         userId of user making request.
     * @param assetToken     token used to find the Asset - may be a name or GUID
     * @param startFrom starting element (used in paging through large result sets)
     * @param pageSize maximum number of results to return
     *                 *
     * @return a list of unique identifiers for the matching assets
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem retrieving the asset properties from the property servers).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public List<String> getAssetsByToken(String userId,
                                         String assetToken,
                                         int    startFrom,
                                         int    pageSize) throws InvalidParameterException,
                                                                 PropertyServerException,
                                                                 UserNotAuthorizedException
    {
        final String   methodName    = "getAssetsByToken";
        final String   tokenParameter = "assetToken";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(assetToken, tokenParameter, methodName);

        List<Asset>  retrievedAssets = new ArrayList<>();

        Asset asset;

        try
        {
            asset = this.getAssetByGUID(userId, assetToken, methodName);
        }
        catch (Throwable error)
        {
            asset = null;
        }

        if (asset != null)
        {
            retrievedAssets.add(asset);
        }
        else
        {
            retrievedAssets = this.getAssetsByName(userId, assetToken, startFrom, pageSize, methodName);
        }

        return this.getGUIDs(retrievedAssets);
    }


    /**
     * Returns a comprehensive collection of properties about the requested asset.
     *
     * @param userId         userId of user making request.
     * @param assetGUID      unique identifier for asset.
     *
     * @return a comprehensive collection of properties about the asset.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem retrieving the asset properties from the property servers).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public AssetUniverse getAssetProperties(String userId,
                                            String assetGUID) throws InvalidParameterException,
                                                                     PropertyServerException,
                                                                     UserNotAuthorizedException
    {
        final String   methodName = "getAssetProperties";
        final String   guidParameter = "assetGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(assetGUID, guidParameter, methodName);

        try
        {
            /*
             * Make use of the ConnectedAsset OMAS Service which provides the metadata services for the
             * Open Connector Framework (OCF).
             */
            return new ConnectedAssetUniverse(serverName, omasServerURL, userId, assetGUID);
        }
        catch (UserNotAuthorizedException | InvalidParameterException | PropertyServerException error)
        {
            throw error;
        }
        catch (Throwable error)
        {
            AssetConsumerErrorCode errorCode = AssetConsumerErrorCode.NO_ASSET_PROPERTIES;
            String                 errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(methodName);

            throw new PropertyServerException(errorCode.getHTTPErrorCode(),
                                              this.getClass().getName(),
                                              methodName,
                                              errorMessage,
                                              errorCode.getSystemAction(),
                                              errorCode.getUserAction());
        }
    }


    /*
     * ===============================================
     * AssetConsumerConnectorFactoryInterface
     * ===============================================
     */

    /**
     * Use the Open Connector Framework (OCF) to create a connector using the supplied connection.
     *
     * @param requestedConnection  connection describing the required connector.
     * @param methodName  name of the calling method.
     *
     * @return a new connector.
     *
     * @throws ConnectionCheckedException  there are issues with the values in the connection
     * @throws ConnectorCheckedException the connector had an operational issue accessing the asset.
     */
    private Connector  getConnectorForConnection(String          userId,
                                                 Connection      requestedConnection,
                                                 String          methodName) throws ConnectionCheckedException,
                                                                                    ConnectorCheckedException
    {
        ConnectorBroker connectorBroker = new ConnectorBroker();

        /*
         * Pass the connection to the ConnectorBroker to create the connector instance.
         * Again, exceptions from this process are returned directly to the caller.
         */
        Connector newConnector = connectorBroker.getConnector(requestedConnection);

        /*
         * If no exception is thrown by getConnector, we should have a connector instance.
         */
        if (newConnector == null)
        {
            /*
             * This is probably some sort of logic error since the connector should have been returned.
             * Whatever the cause, the process can not proceed without a connector.
             */
            AssetConsumerErrorCode errorCode    = AssetConsumerErrorCode.NULL_CONNECTOR_RETURNED;
            String                 errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage();

            throw new ConnectorCheckedException(errorCode.getHTTPErrorCode(),
                                                this.getClass().getName(),
                                                methodName,
                                                errorMessage,
                                                errorCode.getSystemAction(),
                                                errorCode.getUserAction(),
                                                null);
        }

        try
        {
            String  assetGUID = this.getAssetForConnection(userId, requestedConnection.getGUID());

            /*
             * If the connector is successfully created, set up the Connected Asset Properties for the connector.
             * The properties should be retrieved from the open metadata repositories, so use an OMAS implementation
             * of the ConnectedAssetProperties object.
             */
            ConnectedAssetProperties connectedAssetProperties = new ConnectedAssetProperties(serverName,
                                                                                             userId,
                                                                                             omasServerURL,
                                                                                             newConnector.getConnectorInstanceId(),
                                                                                             newConnector.getConnection(),
                                                                                             assetGUID);

            /*
             * Pass the new connected asset properties to the connector
             */
            newConnector.initializeConnectedAssetProperties(connectedAssetProperties);
        }
        catch (Throwable  error)
        {
            /*
             * Ignore error - connectedAssetProperties is left at null.
             */
        }

        /*
         * At this stage, the asset properties are not retrieved from the server.  This does not happen until the caller
         * issues a connector.getConnectedAssetProperties.  This causes the connectedAssetProperties.refresh() call
         * to be made, which contacts the OMAS server and retrieves the asset properties.
         *
         * Delaying the population of the connected asset properties ensures the latest values are returned to the
         * caller (consider a long running connection).  Alternatively, these properties may not ever be used by the
         * caller so retrieving the properties at this point would be unnecessary.
         */

        return newConnector;
    }


    /**
     * Returns the connection object corresponding to the supplied connection name.
     *
     * @param userId  String - userId of user making request.
     * @param name  this may be the qualifiedName or displayName of the connection.
     *
     * @return Connection retrieved from property server.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem retrieving information from the property (metadata) server.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    private Connection getConnectionByName(String   userId,
                                           String   name) throws InvalidParameterException,
                                                                 PropertyServerException,
                                                                 UserNotAuthorizedException
    {
        final String   methodName = "getConnectionByName";
        final String   urlTemplate = "/servers/{0}/open-metadata/access-services/asset-consumer/users/{1}/connections/by-name/{2}";

        ConnectionResponse restResult = restClient.callConnectionGetRESTCall(methodName,
                                                                             omasServerURL + urlTemplate,
                                                                             serverName,
                                                                             userId,
                                                                             name);

        exceptionHandler.detectAndThrowInvalidParameterException(methodName, restResult);
        exceptionHandler.detectAndThrowUserNotAuthorizedException(methodName, restResult);
        exceptionHandler.detectAndThrowPropertyServerException(methodName, restResult);

        return restResult.getConnection();
    }


    /**
     * Returns the connector corresponding to the supplied connection name.
     *
     * @param userId           userId of user making request.
     * @param connectionName   this may be the qualifiedName or displayName of the connection.
     *
     * @return Connector   connector instance.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws ConnectionCheckedException there are errors in the configuration of the connection which is preventing
     *                                      the creation of a connector.
     * @throws ConnectorCheckedException there are errors in the initialization of the connector.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public Connector getConnectorByName(String userId,
                                        String connectionName) throws InvalidParameterException,
                                                                      ConnectionCheckedException,
                                                                      ConnectorCheckedException,
                                                                      PropertyServerException,
                                                                      UserNotAuthorizedException
    {
        final String   methodName = "getConnectorByName";
        final  String  nameParameter = "connectionName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(connectionName, nameParameter, methodName);

        return this.getConnectorForConnection(userId,
                                              this.getConnectionByName(userId, connectionName),
                                              methodName);
    }



    /**
     * Returns the connection corresponding to the supplied asset GUID.
     *
     * @param userId       userId of user making request.
     * @param assetGUID   the unique id for the asset within the metadata repository.
     *
     * @return Connector   connector instance.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    private Connection getConnectionForAsset(String userId,
                                             String assetGUID) throws InvalidParameterException,
                                                                      PropertyServerException,
                                                                      UserNotAuthorizedException
    {
        final String   methodName = "getConnectionForAsset";
        final String   urlTemplate = "/servers/{0}/open-metadata/access-services/asset-consumer/users/{1}/assets/{2}/connection";

        ConnectionResponse restResult = restClient.callConnectionGetRESTCall(methodName,
                                                                             omasServerURL + urlTemplate,
                                                                             serverName,
                                                                             userId,
                                                                             assetGUID);

        exceptionHandler.detectAndThrowInvalidParameterException(methodName, restResult);
        exceptionHandler.detectAndThrowUserNotAuthorizedException(methodName, restResult);
        exceptionHandler.detectAndThrowPropertyServerException(methodName, restResult);

        return restResult.getConnection();
    }


    /**
     * Returns the connector corresponding to the supplied asset GUID.
     *
     * @param userId       userId of user making request.
     * @param assetGUID   the unique id for the asset within the metadata repository.
     *
     * @return Connector   connector instance.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws ConnectionCheckedException there are errors in the configuration of the connection which is preventing
     *                                      the creation of a connector.
     * @throws ConnectorCheckedException there are errors in the initialization of the connector.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public Connector getConnectorForAsset(String userId,
                                          String assetGUID) throws InvalidParameterException,
                                                                   ConnectionCheckedException,
                                                                   ConnectorCheckedException,
                                                                   PropertyServerException,
                                                                   UserNotAuthorizedException
    {
        final  String  methodName = "getConnectorForAsset";
        final  String  guidParameter = "assetGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(assetGUID, guidParameter, methodName);

        return this.getConnectorForConnection(userId,
                                              this.getConnectionForAsset(userId, assetGUID),
                                              methodName);
    }


    /**
     * Returns the connection corresponding to the supplied connection GUID.
     *
     * @param userId userId of user making request.
     * @param guid   the unique id for the connection within the metadata repository.
     *
     * @return connection instance.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    private Connection getConnectionByGUID(String     userId,
                                           String     guid) throws InvalidParameterException,
                                                                   PropertyServerException,
                                                                   UserNotAuthorizedException
    {
        final String   methodName  = "getConnectionByGUID";
        final String   urlTemplate = "/servers/{0}/open-metadata/access-services/asset-consumer/users/{1}/connections/{2}";

        ConnectionResponse   restResult = restClient.callConnectionGetRESTCall(methodName,
                                                                               omasServerURL + urlTemplate,
                                                                               serverName,
                                                                               userId,
                                                                               guid);

        exceptionHandler.detectAndThrowInvalidParameterException(methodName, restResult);
        exceptionHandler.detectAndThrowUserNotAuthorizedException(methodName, restResult);
        exceptionHandler.detectAndThrowPropertyServerException(methodName, restResult);

        return restResult.getConnection();
    }


    /**
     * Returns the connector corresponding to the supplied connection GUID.
     *
     * @param userId           userId of user making request.
     * @param connectionGUID   the unique id for the connection within the metadata repository.
     *
     * @return Connector   connector instance.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws ConnectionCheckedException there are errors in the configuration of the connection which is preventing
     *                                      the creation of a connector.
     * @throws ConnectorCheckedException there are errors in the initialization of the connector.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public Connector getConnectorByGUID(String userId,
                                        String connectionGUID) throws InvalidParameterException,
                                                                      ConnectionCheckedException,
                                                                      ConnectorCheckedException,
                                                                      PropertyServerException,
                                                                      UserNotAuthorizedException
    {
        final  String  methodName = "getConnectorByGUID";
        final  String  guidParameter = "connectionGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(connectionGUID, guidParameter, methodName);

        return this.getConnectorForConnection(userId,
                                              this.getConnectionByGUID(userId, connectionGUID),
                                              methodName);
    }


    /**
     * Returns the connector corresponding to the supplied connection.
     *
     * @param userId       userId of user making request.
     * @param connection   the connection object that contains the properties needed to create the connection.
     *
     * @return Connector   connector instance
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws ConnectionCheckedException there are errors in the configuration of the connection which is preventing
     *                                      the creation of a connector.
     * @throws ConnectorCheckedException there are errors in the initialization of the connector.
     */
    public Connector  getConnectorByConnection(String userId,
                                               Connection connection) throws InvalidParameterException,
                                                                             ConnectionCheckedException,
                                                                             ConnectorCheckedException
    {
        final  String  methodName = "getConnectorByConnection";

        invalidParameterHandler.validateUserId(userId, methodName);

        return this.getConnectorForConnection(userId, connection, methodName);
    }


    /*
     * ===============================================
     * AssetConsumerFeedbackInterface
     * ===============================================
     */

    /**
     * Adds a star rating and optional review text to the asset.  If the user has already attached
     * a rating then the original one is over-ridden.
     *
     * @param userId      userId of user making request.
     * @param assetGUID   unique identifier for the asset.
     * @param starRating  StarRating enumeration for not recommended, one to five stars.
     * @param review      user review of asset.  This can be null.
     * @param isPublic   indicates whether the feedback should be shared or only be visible to the originating user
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem adding the asset properties to the property server.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void   addRatingToAsset(String     userId,
                                   String     assetGUID,
                                   StarRating starRating,
                                   String     review,
                                   boolean    isPublic) throws InvalidParameterException,
                                                                PropertyServerException,
                                                                UserNotAuthorizedException
    {
        final String   methodName  = "addRatingToAsset";
        final String   guidParameter = "assetGUID";

        final String   urlTemplate = "/servers/{0}/open-metadata/access-services/asset-consumer/users/{1}/assets/{2}/ratings";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(assetGUID, guidParameter, methodName);

        RatingRequestBody requestBody = new RatingRequestBody();
        requestBody.setStarRating(starRating);
        requestBody.setReview(review);
        requestBody.setPublic(isPublic);

        VoidResponse restResult = restClient.callVoidPostRESTCall(methodName,
                                                                  omasServerURL + urlTemplate,
                                                                  requestBody,
                                                                  serverName,
                                                                  userId,
                                                                  assetGUID);

        exceptionHandler.detectAndThrowInvalidParameterException(methodName, restResult);
        exceptionHandler.detectAndThrowUserNotAuthorizedException(methodName, restResult);
        exceptionHandler.detectAndThrowPropertyServerException(methodName, restResult);
    }




    /**
     * Removes of a review that was added to the asset by this user.
     *
     * @param userId      userId of user making request.
     * @param assetGUID  unique identifier for the attached asset.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem updating the asset properties in the property server.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void removeRatingFromAsset(String userId,
                                      String assetGUID) throws InvalidParameterException,
                                                               PropertyServerException,
                                                               UserNotAuthorizedException
    {
        final String   methodName = "removeRatingFromAsset";
        final String   guidParameter = "assetGUID";

        final String   urlTemplate = "/servers/{0}/open-metadata/access-services/asset-consumer/users/{1}/assets/{2}/ratings/delete";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(assetGUID, guidParameter, methodName);

        VoidResponse restResult = restClient.callVoidPostRESTCall(methodName,
                                                                  omasServerURL + urlTemplate,
                                                                  nullRequestBody,
                                                                  serverName,
                                                                  userId,
                                                                  assetGUID);

        exceptionHandler.detectAndThrowInvalidParameterException(methodName, restResult);
        exceptionHandler.detectAndThrowUserNotAuthorizedException(methodName, restResult);
        exceptionHandler.detectAndThrowPropertyServerException(methodName, restResult);
    }



    /**
     * Adds a "Like" to the asset.
     *
     * @param userId      userId of user making request
     * @param assetGUID   unique identifier for the asset
     * @param isPublic   indicates whether the feedback should be shared or only be visible to the originating user
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem adding the asset properties to the property server.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void addLikeToAsset(String         userId,
                                 String       assetGUID,
                                 boolean      isPublic) throws InvalidParameterException,
                                                                PropertyServerException,
                                                                UserNotAuthorizedException
    {
        final String   methodName  = "addLikeToAsset";
        final String   guidParameter = "assetGUID";

        final String   urlTemplate = "/servers/{0}/open-metadata/access-services/asset-consumer/users/{1}/assets/{2}/likes";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(assetGUID, guidParameter, methodName);

        FeedbackRequestBody requestBody = new FeedbackRequestBody();
        requestBody.setPublic(isPublic);
        VoidResponse restResult = restClient.callVoidPostRESTCall(methodName,
                                                                  omasServerURL + urlTemplate,
                                                                  requestBody,
                                                                  serverName,
                                                                  userId,
                                                                  assetGUID);

        exceptionHandler.detectAndThrowInvalidParameterException(methodName, restResult);
        exceptionHandler.detectAndThrowUserNotAuthorizedException(methodName, restResult);
        exceptionHandler.detectAndThrowPropertyServerException(methodName, restResult);
    }


    /**
     * Removes a "Like" added to the asset by this user.
     *
     * @param userId   userId of user making request.
     * @param assetGUID unique identifier for the like object.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem updating the asset properties in the property server.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void   removeLikeFromAsset(String userId,
                                      String assetGUID) throws InvalidParameterException,
                                                               PropertyServerException,
                                                               UserNotAuthorizedException
    {
        final String   methodName = "removeLikeFromAsset";
        final String   guidParameter = "assetGUID";

        final String   urlTemplate = "/servers/{0}/open-metadata/access-services/asset-consumer/users/{1}/assets/{2}/likes/delete";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(assetGUID, guidParameter, methodName);

        VoidResponse restResult = restClient.callVoidPostRESTCall(methodName,
                                                                  omasServerURL + urlTemplate,
                                                                  nullRequestBody,
                                                                  serverName,
                                                                  userId,
                                                                  assetGUID);

        exceptionHandler.detectAndThrowInvalidParameterException(methodName, restResult);
        exceptionHandler.detectAndThrowUserNotAuthorizedException(methodName, restResult);
        exceptionHandler.detectAndThrowPropertyServerException(methodName, restResult);
    }


    /**
     * Adds a comment to the asset.
     *
     * @param userId        userId of user making request.
     * @param assetGUID     unique identifier for the asset.
     * @param commentType   type of comment enum.
     * @param commentText   the text of the comment.
     * @param isPublic   indicates whether the feedback should be shared or only be visible to the originating user
     *
     * @return guid of new comment.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem adding the asset properties to the property server.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public String addCommentToAsset(String      userId,
                                    String      assetGUID,
                                    CommentType commentType,
                                    String      commentText,
                                    boolean     isPublic) throws InvalidParameterException,
                                                                 PropertyServerException,
                                                                 UserNotAuthorizedException
    {
        final String   methodName  = "addCommentToAsset";
        final String   guidParameter = "assetGUID";

        final String   urlTemplate = "/servers/{0}/open-metadata/access-services/asset-consumer/users/{1}/assets/{2}/comments";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(assetGUID, guidParameter, methodName);

        CommentRequestBody  requestBody = new CommentRequestBody();
        requestBody.setCommentType(commentType);
        requestBody.setCommentText(commentText);
        requestBody.setPublic(isPublic);

        GUIDResponse restResult = restClient.callGUIDPostRESTCall(methodName,
                                                                  omasServerURL + urlTemplate,
                                                                  requestBody,
                                                                  serverName,
                                                                  userId,
                                                                  assetGUID);

        exceptionHandler.detectAndThrowInvalidParameterException(methodName, restResult);
        exceptionHandler.detectAndThrowUserNotAuthorizedException(methodName, restResult);
        exceptionHandler.detectAndThrowPropertyServerException(methodName, restResult);

        return restResult.getGUID();
    }


    /**
     * Adds a comment to another comment.
     *
     * @param userId        userId of user making request.
     * @param commentGUID   unique identifier for an existing comment.  Used to add a reply to a comment.
     * @param commentType   type of comment enum.
     * @param commentText   the text of the comment.
     * @param isPublic   indicates whether the feedback should be shared or only be visible to the originating user
     *
     * @return guid of new comment.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem adding the asset properties to the property server.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public String addCommentReply(String      userId,
                                  String      commentGUID,
                                  CommentType commentType,
                                  String      commentText,
                                  boolean     isPublic) throws InvalidParameterException,
                                                                PropertyServerException,
                                                                UserNotAuthorizedException
    {
        final String   methodName  = "addCommentReply";
        final String   commentGUIDParameter = "commentGUID";
        final String   urlTemplate = "/servers/{0}/open-metadata/access-services/asset-consumer/users/{1}/comments/{2}/replies";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(commentGUID, commentGUIDParameter, methodName);

        CommentRequestBody  requestBody = new CommentRequestBody();
        requestBody.setCommentType(commentType);
        requestBody.setCommentText(commentText);
        requestBody.setPublic(isPublic);

        GUIDResponse restResult = restClient.callGUIDPostRESTCall(methodName,
                                                                  omasServerURL + urlTemplate,
                                                                  requestBody,
                                                                  serverName,
                                                                  userId,
                                                                  commentGUID);

        exceptionHandler.detectAndThrowInvalidParameterException(methodName, restResult);
        exceptionHandler.detectAndThrowUserNotAuthorizedException(methodName, restResult);
        exceptionHandler.detectAndThrowPropertyServerException(methodName, restResult);

        return restResult.getGUID();
    }


    /**
     * Update an existing comment.
     *
     * @param userId        userId of user making request.
     * @param commentGUID   unique identifier for the comment to change.
     * @param commentType   type of comment enum.
     * @param commentText   the text of the comment.
     * @param isPublic   indicates whether the feedback should be shared or only be visible to the originating user
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem adding the asset properties to the property server.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void   updateComment(String      userId,
                                String      commentGUID,
                                CommentType commentType,
                                String      commentText,
                                boolean     isPublic) throws InvalidParameterException,
                                                             PropertyServerException,
                                                             UserNotAuthorizedException
    {
        final String   methodName  = "updateComment";
        final String   commentGUIDParameter = "commentGUID";
        final String   urlTemplate = "/servers/{0}/open-metadata/access-services/asset-consumer/users/{1}/comments/{2}/update";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(commentGUID, commentGUIDParameter, methodName);

        CommentRequestBody  requestBody = new CommentRequestBody();
        requestBody.setCommentType(commentType);
        requestBody.setCommentText(commentText);
        requestBody.setPublic(isPublic);

        VoidResponse restResult = restClient.callVoidPostRESTCall(methodName,
                                                                  omasServerURL + urlTemplate,
                                                                  requestBody,
                                                                  serverName,
                                                                  userId,
                                                                  commentGUID);

        exceptionHandler.detectAndThrowInvalidParameterException(methodName, restResult);
        exceptionHandler.detectAndThrowUserNotAuthorizedException(methodName, restResult);
        exceptionHandler.detectAndThrowPropertyServerException(methodName, restResult);
    }


    /**
     * Removes a comment added to the asset by this user.
     *
     * @param userId       userId of user making request.
     * @param commentGUID  unique identifier for the comment object.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem updating the asset properties in the property server.
     * @throws UserNotAuthorizedException the user does not have permission to perform this request.
     */
    public void removeComment(String     userId,
                              String     commentGUID) throws InvalidParameterException,
                                                             PropertyServerException,
                                                             UserNotAuthorizedException
    {
        final  String  methodName = "removeComment";
        final  String  guidParameter = "commentGUID";

        final String   urlTemplate = "/servers/{0}/open-metadata/access-services/asset-consumer/users/{1}/comments/{2}/delete";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(commentGUID, guidParameter, methodName);

        VoidResponse restResult = restClient.callVoidPostRESTCall(methodName,
                                                                  omasServerURL + urlTemplate,
                                                                  nullRequestBody,
                                                                  serverName,
                                                                  userId,
                                                                  commentGUID);

        exceptionHandler.detectAndThrowInvalidParameterException(methodName, restResult);
        exceptionHandler.detectAndThrowUserNotAuthorizedException(methodName, restResult);
        exceptionHandler.detectAndThrowPropertyServerException(methodName, restResult);
    }


    /*
     * ===============================================
     * AssetConsumerGlossaryInterface
     * ===============================================
     */


    /**
     * Return the full definition (meaning) of a term using the unique identifier of the glossary term
     * that contains the definition.
     *
     * @param userId userId of the user making the request.
     * @param guid unique identifier of the glossary term.
     *
     * @return meaning response object
     * @throws InvalidParameterException the userId is null or invalid.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public GlossaryTerm getMeaning(String userId,
                                   String guid) throws InvalidParameterException,
                                                       PropertyServerException,
                                                       UserNotAuthorizedException
    {
        final String   methodName = "getMeaning";
        final String   urlTemplate = "/servers/{0}/open-metadata/access-services/asset-consumer/users/{1}/meanings/{2}";
        final String   guidParameter = "guid";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(guid, guidParameter, methodName);

        MeaningResponse restResult = restClient.callMeaningGetRESTCall(methodName,
                                                                       omasServerURL + urlTemplate,
                                                                       serverName,
                                                                       userId,
                                                                       guid);

        exceptionHandler.detectAndThrowInvalidParameterException(methodName, restResult);
        exceptionHandler.detectAndThrowUserNotAuthorizedException(methodName, restResult);
        exceptionHandler.detectAndThrowPropertyServerException(methodName, restResult);

        return restResult.getGlossaryTerm();
    }


    /**
     * Return the full definition (meaning) of the terms matching the supplied name.
     *
     * @param userId the name of the calling user.
     * @param term name of term.  This may include wild card characters.
     * @param startFrom  index of the list ot start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     *
     * @return meaning list response or
     * @throws InvalidParameterException the userId is null or invalid.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public List<GlossaryTerm> getMeaningByName(String userId,
                                               String term,
                                               int    startFrom,
                                               int    pageSize) throws InvalidParameterException,
                                                                       PropertyServerException,
                                                                       UserNotAuthorizedException
    {
        final String   methodName = "getMeaningByName";
        final String   urlTemplate = "/servers/{0}/open-metadata/access-services/asset-consumer/users/{1}/meanings/by-name/{2}?startFrom={3}&pageSize={4}";
        final String   nameParameter = "term";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(term, nameParameter, methodName);

        MeaningListResponse restResult = restClient.callMeaningListGetRESTCall(methodName,
                                                                               omasServerURL + urlTemplate,
                                                                               serverName,
                                                                               userId,
                                                                               term,
                                                                               startFrom,
                                                                               pageSize);

        exceptionHandler.detectAndThrowInvalidParameterException(methodName, restResult);
        exceptionHandler.detectAndThrowUserNotAuthorizedException(methodName, restResult);
        exceptionHandler.detectAndThrowPropertyServerException(methodName, restResult);

        return restResult.getMeanings();
    }



    /*
     * ===============================================
     * AssetConsumerLoggingInterface
     * ===============================================
     */


    /**
     * Creates an Audit log record about the asset.  This log record is stored in the local server's Audit Log.
     *
     * @param userId               userId of user making request.
     * @param assetGUID            unique id for the asset.
     * @param connectorInstanceId  (optional) id of connector in use (if any).
     * @param connectionName       (optional) name of the connection (extracted from the connector).
     * @param connectorType        (optional) type of connector in use (if any).
     * @param contextId            (optional) function name, or processId of the activity that the caller is performing.
     * @param message              log record content.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException There is a problem adding the asset properties to the property server.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void  addLogMessageToAsset(String userId,
                                      String assetGUID,
                                      String connectorInstanceId,
                                      String connectionName,
                                      String connectorType,
                                      String contextId,
                                      String message) throws InvalidParameterException,
                                                             PropertyServerException,
                                                             UserNotAuthorizedException
    {
        final String   methodName = "addLogMessageToAsset";
        final String   guidParameter = "assetGUID";

        final String   urlTemplate = "/servers/{0}/open-metadata/access-services/asset-consumer/users/{1}/assets/{2}/log-records";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(assetGUID, guidParameter, methodName);

        LogRecordRequestBody  requestBody = new LogRecordRequestBody();
        requestBody.setConnectorInstanceId(connectorInstanceId);
        requestBody.setConnectionName(connectionName);
        requestBody.setConnectorType(connectorType);
        requestBody.setContextId(contextId);
        requestBody.setMessage(message);

        VoidResponse restResult = restClient.callVoidPostRESTCall(methodName,
                                                                  omasServerURL + urlTemplate,
                                                                  requestBody,
                                                                  serverName,
                                                                  userId,
                                                                  assetGUID);

        exceptionHandler.detectAndThrowInvalidParameterException(methodName, restResult);
        exceptionHandler.detectAndThrowUserNotAuthorizedException(methodName, restResult);
        exceptionHandler.detectAndThrowPropertyServerException(methodName, restResult);
    }


    /*
     * ===============================================
     * AssetConsumerTaggingInterface
     * ===============================================
     */

    /**
     * Creates a new informal tag and returns the unique identifier for it.
     *
     * @param urlTemplate      string template for the URL.
     * @param methodName       name of calling method.
     * @param userId           userId of user making request.
     * @param tagName          name of the tag.
     * @param tagDescription  (optional) description of the tag.  Setting a description, particularly in a public tag
     *                        makes the tag more valuable to other users and can act as an embryonic glossary term.
     *
     * @return GUID for new tag.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem adding the asset properties to the property server.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    private String createTag(String urlTemplate,
                             String methodName,
                             String userId,
                             String tagName,
                             String tagDescription) throws InvalidParameterException,
                                                                PropertyServerException,
                                                                UserNotAuthorizedException
    {
        invalidParameterHandler.validateUserId(userId, methodName);

        TagRequestBody  tagRequestBody = new TagRequestBody();
        tagRequestBody.setTagName(tagName);
        tagRequestBody.setTagDescription(tagDescription);

        GUIDResponse restResult = restClient.callGUIDPostRESTCall(methodName,
                                                                  omasServerURL + urlTemplate,
                                                                  tagRequestBody,
                                                                  serverName,
                                                                  userId);

        exceptionHandler.detectAndThrowInvalidParameterException(methodName, restResult);
        exceptionHandler.detectAndThrowUserNotAuthorizedException(methodName, restResult);
        exceptionHandler.detectAndThrowPropertyServerException(methodName, restResult);

        return restResult.getGUID();
    }


    /**
     * Creates a new public informal tag and returns the unique identifier for it.
     *
     * @param userId           userId of user making request.
     * @param tagName          name of the tag.
     * @param tagDescription  (optional) description of the tag.  Setting a description, particularly in a public tag
     *                        makes the tag more valuable to other users and can act as an embryonic glossary term.
     *
     * @return GUID for new tag.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem adding the asset properties to the property server.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public String createPublicTag(String userId,
                                  String tagName,
                                  String tagDescription) throws InvalidParameterException,
                                                                PropertyServerException,
                                                                UserNotAuthorizedException
    {
        final String   methodName = "createPublicTag";
        final String   urlTemplate = "/servers/{0}/open-metadata/access-services/asset-consumer/users/{1}/tags/public";

        return this.createTag(urlTemplate, methodName, userId, tagName, tagDescription);
    }


    /**
     * Creates a new private informal tag and returns the unique identifier for it.
     *
     * @param userId           userId of user making request.
     * @param tagName          name of the tag.
     * @param tagDescription  (optional) description of the tag.  Setting a description, particularly in a public tag
     *                        makes the tag more valuable to other users and can act as an embryonic glossary term.
     *
     * @return GUID for new tag.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem adding the asset properties to the property server.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public String createPrivateTag(String userId,
                                   String tagName,
                                   String tagDescription) throws InvalidParameterException,
                                                                 PropertyServerException,
                                                                 UserNotAuthorizedException
    {
        final String   methodName = "createPrivateTag";
        final String   urlTemplate = "/servers/{0}/open-metadata/access-services/asset-consumer/users/{1}/tags/private";

        return this.createTag(urlTemplate, methodName, userId, tagName, tagDescription);
    }


    /**
     * Updates the description of an existing tag (either private of public).
     *
     * @param userId          userId of user making request.
     * @param tagGUID         unique identifier for the tag.
     * @param tagDescription  description of the tag.  Setting a description, particularly in a public tag
     *                        makes the tag more valuable to other users and can act as an embryonic glossary term.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem adding the asset properties to the property server.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void   updateTagDescription(String userId,
                                       String tagGUID,
                                       String tagDescription) throws InvalidParameterException,
                                                                     PropertyServerException,
                                                                     UserNotAuthorizedException
    {
        final String   methodName = "updateTagDescription";
        final String   guidParameter = "tagGUID";

        final String   urlTemplate = "/servers/{0}/open-metadata/access-services/asset-consumer/users/{1}/tags/{2}/update";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(tagGUID, guidParameter, methodName);

        TagRequestBody  tagRequestBody = new TagRequestBody();
        tagRequestBody.setTagDescription(tagDescription);

        VoidResponse restResult = restClient.callVoidPostRESTCall(methodName,
                                                                  omasServerURL + urlTemplate,
                                                                  tagRequestBody,
                                                                  serverName,
                                                                  userId,
                                                                  tagGUID);

        exceptionHandler.detectAndThrowInvalidParameterException(methodName, restResult);
        exceptionHandler.detectAndThrowUserNotAuthorizedException(methodName, restResult);
        exceptionHandler.detectAndThrowPropertyServerException(methodName, restResult);
    }


    /**
     * Removes a tag from the repository.  All of the relationships to assets are lost.
     *
     * @param userId    userId of user making request.
     * @param tagGUID   unique id for the tag.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem updating the asset properties in the property server.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void   deleteTag(String userId,
                            String tagGUID) throws InvalidParameterException,
                                                   PropertyServerException,
                                                   UserNotAuthorizedException
    {
        final String   methodName = "deleteTag";
        final String   guidParameter = "tagGUID";

        final String   urlTemplate = "/servers/{0}/open-metadata/access-services/asset-consumer/users/{1}/tags/{2}/delete";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(tagGUID, guidParameter, methodName);

        VoidResponse restResult = restClient.callVoidPostRESTCall(methodName,
                                                                  omasServerURL + urlTemplate,
                                                                  nullRequestBody,
                                                                  serverName,
                                                                  userId,
                                                                  tagGUID);

        exceptionHandler.detectAndThrowInvalidParameterException(methodName, restResult);
        exceptionHandler.detectAndThrowUserNotAuthorizedException(methodName, restResult);
        exceptionHandler.detectAndThrowPropertyServerException(methodName, restResult);
    }


    /**
     * Return the tag for the supplied unique identifier (guid).
     *
     * @param userId userId of the user making the request.
     * @param guid unique identifier of the tag.
     *
     * @return tag
     * @throws InvalidParameterException the userId is null or invalid.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public InformalTag getTag(String userId,
                              String guid) throws InvalidParameterException,
                                                  PropertyServerException,
                                                  UserNotAuthorizedException
    {
        final String   methodName = "getTag";
        final String   urlTemplate = "/servers/{0}/open-metadata/access-services/asset-consumer/users/{1}/tags/{2}";
        final String   guidParameter = "guid";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(guid, guidParameter, methodName);

        TagResponse restResult = restClient.callTagGetRESTCall(methodName,
                                                               omasServerURL + urlTemplate,
                                                               serverName,
                                                               userId,
                                                               guid);

        exceptionHandler.detectAndThrowInvalidParameterException(methodName, restResult);
        exceptionHandler.detectAndThrowUserNotAuthorizedException(methodName, restResult);
        exceptionHandler.detectAndThrowPropertyServerException(methodName, restResult);

        return restResult.getTag();
    }


    /**
     * Return the list of tags matching the supplied name.
     *
     * @param userId the name of the calling user.
     * @param tag name of tag.  This may include wild card characters.
     * @param startFrom  index of the list ot start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     *
     * @return tag list
     * @throws InvalidParameterException the userId is null or invalid.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public List<InformalTag> getTagsByName(String userId,
                                           String tag,
                                           int    startFrom,
                                           int    pageSize) throws InvalidParameterException,
                                                                   PropertyServerException,
                                                                   UserNotAuthorizedException
    {
        final String   methodName = "getTagsByName";
        final String   urlTemplate = "/servers/{0}/open-metadata/access-services/asset-consumer/users/{1}/tags/by-name/{2}?startFrom={3}&pageSize={4}";
        final String   nameParameter = "tag";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(tag, nameParameter, methodName);

        TagListResponse restResult = restClient.callTagListGetRESTCall(methodName,
                                                                       omasServerURL + urlTemplate,
                                                                       serverName,
                                                                       userId,
                                                                       tag,
                                                                       startFrom,
                                                                       pageSize);

        exceptionHandler.detectAndThrowInvalidParameterException(methodName, restResult);
        exceptionHandler.detectAndThrowUserNotAuthorizedException(methodName, restResult);
        exceptionHandler.detectAndThrowPropertyServerException(methodName, restResult);

        return restResult.getTags();
    }


    /**
     * Adds a tag (either private of public) to an asset.
     *
     * @param userId           userId of user making request.
     * @param assetGUID        unique id for the asset.
     * @param tagGUID          unique id of the tag.
     * @param isPublic         flag indicating whether the attachment of the tag is public or not
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem adding the asset properties to the property server.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void   addTagToAsset(String  userId,
                                String  assetGUID,
                                String  tagGUID,
                                boolean isPublic) throws InvalidParameterException,
                                                         PropertyServerException,
                                                         UserNotAuthorizedException
    {
        final String   methodName  = "addTagToAsset";
        final String   assetGUIDParameterName = "assetGUID";
        final String   tagGUIDParameterName = "tagGUID";

        final String   urlTemplate = "/servers/{0}/open-metadata/access-services/asset-consumer/users/{1}/assets/{2}/tags/{3}";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(assetGUID, assetGUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(tagGUID, tagGUIDParameterName, methodName);

        FeedbackRequestBody requestBody = new FeedbackRequestBody();
        requestBody.setPublic(isPublic);
        VoidResponse restResult = restClient.callVoidPostRESTCall(methodName,
                                                                  omasServerURL + urlTemplate,
                                                                  requestBody,
                                                                  serverName,
                                                                  userId,
                                                                  assetGUID,
                                                                  tagGUID);

        exceptionHandler.detectAndThrowInvalidParameterException(methodName, restResult);
        exceptionHandler.detectAndThrowUserNotAuthorizedException(methodName, restResult);
        exceptionHandler.detectAndThrowPropertyServerException(methodName, restResult);
    }


    /**
     * Removes a tag from the asset that was added by this user.
     *
     * @param userId    userId of user making request.
     * @param assetGUID unique id for the asset.
     * @param tagGUID   unique id for the tag.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem updating the asset properties in the property server.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void   removeTagFromAsset(String userId,
                                     String assetGUID,
                                     String tagGUID) throws InvalidParameterException,
                                                            PropertyServerException,
                                                            UserNotAuthorizedException
    {
        final String   methodName  = "removeTagFromAsset";
        final String   assetGUIDParameterName = "assetGUID";
        final String   tagGUIDParameterName = "tagGUID";

        final String   urlTemplate = "/servers/{0}/open-metadata/access-services/asset-consumer/users/{1}/assets/{2}/tags/{3}/delete";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(assetGUID, assetGUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(tagGUID, tagGUIDParameterName, methodName);

        NullRequestBody  requestBody = new NullRequestBody();

        VoidResponse restResult = restClient.callVoidPostRESTCall(methodName,
                                                                  omasServerURL + urlTemplate,
                                                                  requestBody,
                                                                  serverName,
                                                                  userId,
                                                                  assetGUID,
                                                                  tagGUID);

        exceptionHandler.detectAndThrowInvalidParameterException(methodName, restResult);
        exceptionHandler.detectAndThrowUserNotAuthorizedException(methodName, restResult);
        exceptionHandler.detectAndThrowPropertyServerException(methodName, restResult);
    }
}
