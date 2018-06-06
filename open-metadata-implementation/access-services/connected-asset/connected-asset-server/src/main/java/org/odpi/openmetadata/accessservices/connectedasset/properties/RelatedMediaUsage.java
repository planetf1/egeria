/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.accessservices.connectedasset.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * The RelatedMediaUsage defines how a related media reference can be used in conjunction with the asset properties.
 * These usage options are not mutually exclusive.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public enum RelatedMediaUsage implements Serializable
{
    ICON(0, "Icon", "Provides a small image to represent the asset in tree views and graphs."),
    THUMBNAIL(1, "Thumbnail", "Provides a small image about the asset that can be used in lists."),
    ILLUSTRATION(2, "Illustration", "Illustrates how the asset works or what it contains. It is complementary to the asset's description."),
    USAGE_GUIDANCE(3, "Usage Guidance", "Provides guidance to a person on how to use the asset."),
    OTHER(99, "Other", "Another usage.");

    private static final long     serialVersionUID = 1L;

    private int            mediaUsageCode;
    private String         mediaUsageName;
    private String         mediaUsageDescription;


    /**
     * Typical Constructor
     */
    RelatedMediaUsage(int     mediaUsageCode, String   mediaUsageName, String   mediaUsageDescription)
    {
        /*
         * Save the values supplied
         */
        this.mediaUsageCode = mediaUsageCode;
        this.mediaUsageName = mediaUsageName;
        this.mediaUsageDescription = mediaUsageDescription;
    }


    /**
     * Return the code for this enum instance
     *
     * @return int - media usage code
     */
    public int getMediaUsageCode()
    {
        return mediaUsageCode;
    }


    /**
     * Return the default name for this enum instance.
     *
     * @return String - default name
     */
    public String getMediaUsageName()
    {
        return mediaUsageName;
    }


    /**
     * Return the default description for the media usage pattern for this enum instance.
     *
     * @return String - default description
     */
    public String getMediaUsageDescription()
    {
        return mediaUsageDescription;
    }
}