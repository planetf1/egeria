/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.ocf.metadatamanagement.client;

import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.rest.RatingsResponse;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.properties.AssetDescriptor;
import org.odpi.openmetadata.frameworks.connectors.properties.AssetRating;
import org.odpi.openmetadata.frameworks.connectors.properties.AssetRatings;
import org.odpi.openmetadata.frameworks.connectors.properties.AssetPropertyBase;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Rating;

import java.util.ArrayList;
import java.util.List;


/**
 * ConnectedAssetRatings provides the open metadata concrete implementation of the
 * Open Connector Framework (OCF) AssetRatings abstract class.
 * Its role is to query the property servers (metadata repository cohort) to extract ratings
 * related to the connected asset.
 */
public class ConnectedAssetRatings extends AssetRatings
{
    private String                 serverName;
    private String                 userId;
    private String                 omasServerURL;
    private String                 assetGUID;
    private ConnectedAssetUniverse connectedAsset;
    private OCFRESTClient          restClient;



    /**
     * Typical constructor creates an iterator with the supplied list of elements.
     *
     * @param serverName  name of the server.
     * @param userId user id to use on server calls.
     * @param omasServerURL url root of the server to use.
     * @param assetGUID unique identifier of the asset.
     * @param parentAsset descriptor of parent asset.
     * @param totalElementCount the total number of elements to process.  A negative value is converted to 0.
     * @param maxCacheSize maximum number of elements that should be retrieved from the property server and
     *                     cached in the element list at any one time.  If a number less than one is supplied, 1 is used.
     * @param restClient client to call REST API
     */
    ConnectedAssetRatings(String                 serverName,
                          String                 userId,
                          String                 omasServerURL,
                          String                 assetGUID,
                          ConnectedAssetUniverse parentAsset,
                          int                    totalElementCount,
                          int                    maxCacheSize,
                          OCFRESTClient restClient)
    {
        super(parentAsset, totalElementCount, maxCacheSize);

        this.serverName      = serverName;
        this.userId          = userId;
        this.omasServerURL   = omasServerURL;
        this.assetGUID       = assetGUID;
        this.connectedAsset  = parentAsset;
        this.restClient      = restClient;
    }


    /**
     * Copy/clone constructor.  Used to reset iterator element pointer to 0;
     *
     * @param parentAsset descriptor of parent asset
     * @param template type-specific iterator to copy; null to create an empty iterator
     */
    private ConnectedAssetRatings(ConnectedAssetUniverse parentAsset, ConnectedAssetRatings template)
    {
        super(parentAsset, template);

        if (template != null)
        {
            this.serverName     = template.serverName;
            this.userId         = template.userId;
            this.omasServerURL  = template.omasServerURL;
            this.assetGUID      = template.assetGUID;
            this.connectedAsset = parentAsset;
            this.restClient     = template.restClient;
        }
    }


    /**
     * Clones this iterator.
     *
     * @param parentAsset descriptor of parent asset
     * @return new cloned object.
     */
    protected  AssetRatings cloneIterator(AssetDescriptor parentAsset)
    {
        return new ConnectedAssetRatings(connectedAsset, this);
    }



    /**
     * Method implemented by a subclass that ensures the cloning process is a deep clone.
     *
     * @param parentAsset descriptor of parent asset
     * @param template object to clone
     * @return new cloned object.
     */
    protected  AssetPropertyBase cloneElement(AssetDescriptor  parentAsset, AssetPropertyBase template)
    {
        return new AssetRating(parentAsset, (AssetRating)template);
    }


    /**
     * Method implemented by subclass to retrieve the next cached list of elements.
     *
     * @param cacheStartPointer where to start the cache.
     * @param maximumSize maximum number of elements in the cache.
     * @return list of elements corresponding to the supplied cache pointers.
     * @throws PropertyServerException there is a problem retrieving elements from the property (metadata) server.
     */
    protected  List<AssetPropertyBase> getCachedList(int  cacheStartPointer,
                                                     int  maximumSize) throws PropertyServerException
    {
        final String   methodName = "AssetRatings.getCachedList";
        final String   urlTemplate = "/servers/{0}/open-metadata/common-services/ocf/connected-asset/users/{1}/assets/{2}/ratings?elementStart={3}&maxElements={4}";

        RESTExceptionHandler    restExceptionHandler    = new RESTExceptionHandler();

        try
        {
            RatingsResponse restResult = restClient.callRatingsGetRESTCall(methodName,
                                                                           omasServerURL + urlTemplate,
                                                                           userId,
                                                                           assetGUID,
                                                                           cacheStartPointer,
                                                                           maximumSize);

            restExceptionHandler.detectAndThrowInvalidParameterException(methodName, restResult);
            restExceptionHandler.detectAndThrowUserNotAuthorizedException(methodName, restResult);
            restExceptionHandler.detectAndThrowPropertyServerException(methodName, restResult);

            List<Rating>  beans = restResult.getList();
            if ((beans == null) || (beans.isEmpty()))
            {
                return null;
            }
            else
            {
                List<AssetPropertyBase>   resultList = new ArrayList<>();

                for (Rating  bean : beans)
                {
                    if (bean != null)
                    {
                        resultList.add(new AssetRating(connectedAsset, bean));
                    }
                }

                return resultList;
            }
        }
        catch (Throwable  error)
        {
            restExceptionHandler.handleUnexpectedException(error, methodName, serverName, omasServerURL);
        }

        return null;
    }
}
