/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.accessservices.connectedasset.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * A StarRating defines the rating that a user has placed against an asset. This ranges from not recommended
 * through to five stars (excellent).
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public enum StarRating implements Serializable
{
    UNRATED(0, "X", "Not recommended"),
    ONE_STAR(1, "*", "Poor"),
    TWO_STARS(2, "**", "Usable"),
    THREE_STARS(3, "***", "Good"),
    FOUR_STARS(4, "****", "Very Good"),
    FIVE_STARS(5, "*****", "Excellent");

    private static final long     serialVersionUID = 1L;

    private int            starRatingCode = 99;
    private String         starRatingSymbol = "";
    private String         starRatingDescription = "";


    /**
     * Typical Constructor
     */
    StarRating(int     starRatingCode, String   starRatingSymbol, String   starRatingDescription)
    {
        /*
         * Save the values supplied
         */
        this.starRatingCode = starRatingCode;
        this.starRatingSymbol = starRatingSymbol;
        this.starRatingDescription = starRatingDescription;
    }


    /**
     * Return the code for this enum instance
     *
     * @return int - star rating code
     */
    public int getStarRatingCode()
    {
        return starRatingCode;
    }


    /**
     * Return the default symbol for this enum instance.
     *
     * @return String - default symbol
     */
    public String getStarRatingSymbol()
    {
        return starRatingSymbol;
    }


    /**
     * Return the default description for the star rating for this enum instance.
     *
     * @return String - default description
     */
    public String getStarRatingDescription()
    {
        return starRatingDescription;
    }
}