/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.connectors.properties;

import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Asset;

import java.util.Objects;

/**
 * RelatedAsset describes assets that are related to this asset.  For example, if the asset is a data store, the
 * related assets could be its supported data sets.
 */
public class RelatedAsset extends AssetReferenceable
{
    protected Asset                  assetBean;
    protected String                 relationshipTypeName;
    protected RelatedAssetProperties relatedAssetProperties;


    /**
     * Bean constructor
     *
     * @param assetBean bean containing basic properties
     * @param relationshipTypeName name of the relationship between the anchor asset and th related asset
     * @param relatedAssetProperties client for retrieving properties from the server
     */
    public RelatedAsset(Asset                  assetBean,
                        String                 relationshipTypeName,
                        RelatedAssetProperties relatedAssetProperties)
    {
        super(assetBean);

        if (assetBean == null)
        {
            this.assetBean = new Asset();
        }
        else
        {
            this.assetBean = new Asset(assetBean);
        }

        this.relationshipTypeName = relationshipTypeName;
        this.relatedAssetProperties = relatedAssetProperties;
    }


    /**
     * Bean constructor with parent asset
     *
     * @param parentAsset descriptor for parent asset
     * @param assetBean bean containing basic properties
     * @param relationshipTypeName name of the relationship between the anchor asset and th related asset
     * @param relatedAssetProperties client for retrieving properties from the server
     */
    public RelatedAsset(AssetDescriptor        parentAsset,
                        Asset                  assetBean,
                        String                 relationshipTypeName,
                        RelatedAssetProperties relatedAssetProperties)
    {
        super(assetBean);

        if (assetBean == null)
        {
            this.assetBean = new Asset();
        }
        else
        {
            this.assetBean = new Asset(assetBean);
        }

        this.relationshipTypeName = relationshipTypeName;
        this.relatedAssetProperties = relatedAssetProperties;
    }


    /**
     * Copy/clone constructor
     *
     * @param parentAsset description of the asset that this related asset is attached to.
     * @param templateRelatedAsset template object to copy.
     */
    public RelatedAsset(AssetDescriptor parentAsset, RelatedAsset templateRelatedAsset)
    {
        super(parentAsset, templateRelatedAsset);
        if (templateRelatedAsset == null)
        {
            this.assetBean = new Asset();
            this.relatedAssetProperties = null;
            this.relationshipTypeName = null;
        }
        else
        {
            this.assetBean = templateRelatedAsset.getAssetBean();
            this.relatedAssetProperties = templateRelatedAsset.relatedAssetProperties;
            this.relationshipTypeName = templateRelatedAsset.getRelationshipTypeName();
        }
    }


    /**
     * Return the bean with basic information about the asset.
     *
     * @return assetBean
     */
    protected Asset  getAssetBean()
    {
        return assetBean;
    }


    /**
     * Returns the stored display name property for the related asset.
     * If no display name is available then null is returned.
     *
     * @return displayName
     */
    public String getDisplayName() { return assetBean.getDisplayName(); }


    /**
     * Returns the summary description property for the related asset.
     * If no description is provided then null is returned.
     *
     * @return summary description
     */
    public String getShortDescription() { return assetBean.getShortDescription(); }


    /**
     * Returns the stored description property for the related asset.
     * If no description is provided then null is returned.
     *
     * @return description
     */
    public String getDescription() { return assetBean.getDescription(); }


    /**
     * Returns the details of the owner for this related asset.
     *
     * @return String owner
     */
    public String getOwner() { return assetBean.getOwner(); }


    /**
     * Return the name of the relationship between the anchor asset and th related asset
     *
     * @return relationship type name
     */
    public String getRelationshipTypeName()
    {
        return relationshipTypeName;
    }


    /**
     * Return the detailed properties for a related asset.
     *
     * @return a refreshed version of the RelatedAssetProperties
     * @throws UserNotAuthorizedException the calling user does not have access to the asset.
     * @throws PropertyServerException problems communicating with the property (metadata) server
     */
    public RelatedAssetProperties getRelatedAssetProperties() throws PropertyServerException, UserNotAuthorizedException
    {
        if (relatedAssetProperties != null)
        {
            relatedAssetProperties.refresh();
        }

        return relatedAssetProperties;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return assetBean.toString();
    }


    /**
     * Compare the values of the supplied object with those stored in the current object.
     *
     * @param objectToCompare supplied object
     * @return boolean result of comparison
     */
    @Override
    public boolean equals(Object objectToCompare)
    {
        if (this == objectToCompare)
        {
            return true;
        }
        if (!(objectToCompare instanceof RelatedAsset))
        {
            return false;
        }
        if (!super.equals(objectToCompare))
        {
            return false;
        }
        RelatedAsset that = (RelatedAsset) objectToCompare;
        return Objects.equals(getAssetBean(), that.getAssetBean());
    }
}