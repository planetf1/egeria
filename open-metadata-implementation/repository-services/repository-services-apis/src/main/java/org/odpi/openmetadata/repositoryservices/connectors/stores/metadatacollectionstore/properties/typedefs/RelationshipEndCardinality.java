/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * RelationshipCardinality is used in a RelationshipEndDef to indicate how many relationships of this type
 * can be connected to an entity instance.:
 * <ul>
 *     <li>
 *         UNKNOWN - uninitialized cardinality
 *     </li>
 *     <li>
 *         AT_MOST_ONE - means there can be zero or one instances of this relationship connected
 *                       to an instance of the EntityDef.  This relationship is written as 0..1 in UML.
 *     </li>
 *     <li>
 *         ANY_NUMBER - means there can be none, one or many instances of this relationship connected
 *                      to an instance of the EntityDef.
 *                      This relationship is often written as 0..* or * in UML.
 *     </li>
 * </ul>
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public enum RelationshipEndCardinality implements Serializable
{
    UNKNOWN      (0, "<Unknown>",    "Unknown or uninitialized cardinality"),
    AT_MOST_ONE  (1, "At Most One",  "0..1 - Zero or one instances. 0..1."),
    ANY_NUMBER   (2, "Any Number",   "0..* or * - Any number of instances");

    private static final long serialVersionUID = 1L;

    private int     ordinal;
    private String  name;
    private String  description;


    /**
     * Constructor to set up a single instances of the enum.
     *
     * @param ordinal - numerical representation of the cardinality
     * @param name - default string name of the cardinality
     * @param description - default string description of the cardinality
     */
    RelationshipEndCardinality(int  ordinal, String name, String description)
    {
        this.ordinal = ordinal;
        this.name = name;
        this.description = description;
    }


    /**
     * Return the numeric representation of the cardinality.
     *
     * @return int ordinal
     */
    public int getOrdinal() { return ordinal; }


    /**
     * Return the default name of the cardinality.
     *
     * @return String name
     */
    public String getName() { return name; }


    /**
     * Return the default description of the cardinality.
     *
     * @return String description
     */
    public String getDescription() { return description; }
}
