/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.communityprofile.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * CollectionStatus specifies which subset of a collection should be returned.  The relationship
 * that links a member into a collection has an start and end date (called the effective dates).  The
 * Community Profile OMAS uses the CollectionStatus and the effective dates to determine which members to return.
 * <ul>
 *     <li>CONTRIBUTOR - Individual is able to be a contributing member of the community.  This is the default.</li>
 *     <li>ADMINISTER - Individual is able to administer the community.</li>
 *     <li>LEADER - Individual sets the direction of the community.</li>
 *     <li>OBSERVER - Individual is receiving notifications about the community.</li>
 *     <li>OTHER - Another meaning.</li>
 * </ul>
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public enum CommunityMembershipType implements Serializable
{
    CONTRIBUTOR   (0,  "Contributor",   "Individual is able to be a contributing member of the community."),
    ADMINISTRATOR (1,  "Administrator", "Individual is able to administer the community."),
    LEADER        (2,  "Leader",        "Individual sets the direction of the community."),
    OBSERVER      (3,  "Observer",      "Individual is receiving notifications about the community."),
    OTHER         (99, "Other",         "Another meaning.");

    private static final long serialVersionUID = 1L;

    private int            ordinal;
    private String         name;
    private String         description;


    /**
     * Default constructor for the enumeration.
     *
     * @param ordinal numerical representation of the enumeration
     * @param name default string name of the enumeration
     * @param description default string description of the enumeration
     */
    CommunityMembershipType(int  ordinal, String name, String description)
    {
        this.ordinal = ordinal;
        this.name = name;
        this.description = description;
    }


    /**
     * Return the numeric representation of the enumeration.
     *
     * @return int ordinal
     */
    public int getOrdinal() { return ordinal; }


    /**
     * Return the default name of the enumeration.
     *
     * @return String name
     */
    public String getName() { return name; }


    /**
     * Return the default description of the enumeration.
     *
     * @return String description
     */
    public String getDescription() { return description; }


    /**
     * toString() JSON-style
     *
     * @return string description
     */
    @Override
    public String toString()
    {
        return "CommunityMembershipType : " + name;
    }
}
