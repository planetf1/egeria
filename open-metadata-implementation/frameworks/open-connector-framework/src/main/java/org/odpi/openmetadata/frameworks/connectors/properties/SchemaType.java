/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.frameworks.connectors.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * SchemaType describes the type of schema element.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public enum SchemaType implements Serializable
{
    UNKNOWN (0, "<Unknown>", "The schema type is unknown."),
    STRUCT  (1, "Struct"   , "The schema type is a structure containing a list of properties of potentially different types."),
    ARRAY   (2, "Array"    , "The schema type is a structure containing an ordered list of properties all of the same type."),
    SET     (3, "Set"      , "The schema type is a structure containing an unordered collection of properties, all of the same type.");

    private int      schemaTypeCode;
    private String   schemaTypeName;
    private String   schemaTypeDescription;

    private static final long     serialVersionUID = 1L;

    /**
     * Constructor to set up the instance of this enum.
     *
     * @param schemaTypeCode - code number
     * @param schemaTypeName - default name
     * @param schemaTypeDescription - default description
     */
    SchemaType(int schemaTypeCode, String schemaTypeName, String schemaTypeDescription)
    {
        this.schemaTypeCode = schemaTypeCode;
        this.schemaTypeName = schemaTypeName;
        this.schemaTypeDescription = schemaTypeDescription;
    }


    /**
     * Return the code for this enum used for indexing based on the enum value.
     *
     * @return int code number
     */
    public int getSchemaTypeCode()
    {
        return schemaTypeCode;
    }


    /**
     * Return the default name for this enum type.
     *
     * @return String name
     */
    public String getSchemaTypeName()
    {
        return schemaTypeName;
    }


    /**
     * Return the default description for this enum.
     *
     * @return String description
     */
    public String getSchemaTypeDescription()
    {
        return schemaTypeDescription;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "SchemaType{" +
                "schemaTypeCode=" + schemaTypeCode +
                ", schemaTypeName='" + schemaTypeName + '\'' +
                ", schemaTypeDescription='" + schemaTypeDescription + '\'' +
                '}';
    }
}
