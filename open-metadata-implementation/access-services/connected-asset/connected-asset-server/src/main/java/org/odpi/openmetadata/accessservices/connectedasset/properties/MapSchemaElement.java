/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.accessservices.connectedasset.properties;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * MapSchemaElement describes a schema element of type map.  It stores the type of schema element for the domain
 * (eg property name) for the map and the schema element for the range (eg property value) for the map.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class MapSchemaElement extends SchemaElement
{
    private   SchemaElement  mapFromElement = null;
    private   SchemaElement  mapToElement = null;


    /**
     * Default constructor
     */
    public MapSchemaElement()
    {
        super();
    }


    /**
     * Copy/clone Constructor - the parentAsset is passed separately to the template because it is also
     * likely to be being cloned in the same operation and we want the definitions clone to point to the
     * asset clone and not the original asset.
     *
     * @param templateSchema - template object to copy.
     */
    public MapSchemaElement(MapSchemaElement templateSchema)
    {
        super(templateSchema);

        if (templateSchema != null)
        {
            SchemaElement  templateMapFromElement = templateSchema.getMapFromElement();
            SchemaElement  templateMapToElement = templateSchema.getMapToElement();

            if (templateMapFromElement != null)
            {
                mapFromElement = templateMapFromElement.cloneSchemaElement();
            }

            if (templateMapToElement != null)
            {
                mapToElement = templateMapToElement.cloneSchemaElement();
            }
        }
    }


    /**
     * Return the type of schema element that represents the key or property name for the map.
     * This is also called the domain of the map.
     *
     * @return SchemaElement
     */
    public SchemaElement getMapFromElement()
    {
        return mapFromElement;
    }


    /**
     * Set up the type of schema element that represents the key or property name for the map.
     * This is also called the domain of the map.
     *
     * @param mapFromElement - SchemaElement
     */
    public void setMapFromElement(SchemaElement mapFromElement)
    {
        this.mapFromElement = mapFromElement;
    }


    /**
     * Return the type of schema element that represents the property value for the map.
     * This is also called the range of the map.
     *
     * @return SchemaElement
     */
    public SchemaElement getMapToElement()
    {
        return mapToElement;
    }


    /**
     * Set up the type of schema element that represents the property value for the map.
     * This is also called the range of the map.
     *
     * @param mapToElement - SchemaElement
     */
    public void setMapToElement(SchemaElement mapToElement)
    {
        this.mapToElement = mapToElement;
    }


    /**
     * Returns a clone of this object as the abstract SchemaElement class.
     *
     * @return SchemaElement
     */
    @Override
    public SchemaElement cloneSchemaElement()
    {
        return new MapSchemaElement(this);
    }
}