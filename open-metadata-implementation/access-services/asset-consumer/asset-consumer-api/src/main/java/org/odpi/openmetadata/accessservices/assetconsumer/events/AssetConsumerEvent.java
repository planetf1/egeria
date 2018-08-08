/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.accessservices.assetconsumer.events;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.accessservices.assetconsumer.properties.Asset;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * AssetConsumerEvent describes the structure of the events emitted by the Asset Consumer OMAS.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class AssetConsumerEvent extends AssetConsumerEventHeader
{
    private AssetConsumerEventType eventType     = null;
    private Asset                  originalAsset = null;
    private Asset                  asset         = null;


    /**
     * Default constructor
     */
    public AssetConsumerEvent()
    {
        super();
    }


    /**
     * Copy/clone constructor
     */
    public AssetConsumerEvent(AssetConsumerEvent  template)
    {
        super(template);

        if (template != null)
        {
            this.eventType = template.getEventType();
            this.asset = template.getAsset();
            this.originalAsset = template.getOriginalAsset();
        }
    }

    /**
     * Return the type of event.
     *
     * @return event type enum
     */
    public AssetConsumerEventType getEventType()
    {
        return eventType;
    }


    /**
     * Set up the type of event.
     *
     * @param eventType - event type enum
     */
    public void setEventType(AssetConsumerEventType eventType)
    {
        this.eventType = eventType;
    }


    /**
     * Return the original asset description.
     *
     * @return properties about the asset
     */
    public Asset getOriginalAsset()
    {
        return originalAsset;
    }


    /**
     * Set up the original asset description.
     *
     * @param originalAsset - properties about the asset.
     */
    public void setOriginalAsset(Asset originalAsset)
    {
        this.originalAsset = originalAsset;
    }


    /**
     * Return the asset description.
     *
     * @return properties about the asset
     */
    public Asset getAsset()
    {
        return asset;
    }


    /**
     * Set up the asset description.
     *
     * @param asset - properties about the asset.
     */
    public void setAsset(Asset asset)
    {
        this.asset = asset;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "AssetConsumerEvent{" +
                "eventType=" + eventType +
                ", originalAsset=" + originalAsset +
                ", asset=" + asset +
                ", eventVersionId=" + getEventVersionId() +
                '}';
    }


    /**
     * Return comparison result based on the content of the properties.
     *
     * @param objectToCompare test object
     * @return result of comparison
     */
    @Override
    public boolean equals(Object objectToCompare)
    {
        if (this == objectToCompare)
        {
            return true;
        }
        if (!(objectToCompare instanceof AssetConsumerEvent))
        {
            return false;
        }
        if (!super.equals(objectToCompare))
        {
            return false;
        }
        AssetConsumerEvent that = (AssetConsumerEvent) objectToCompare;
        return getEventType() == that.getEventType() &&
                Objects.equals(getOriginalAsset(), that.getOriginalAsset()) &&
                Objects.equals(getAsset(), that.getAsset());
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), getEventType(), getOriginalAsset(), getAsset());
    }
}
