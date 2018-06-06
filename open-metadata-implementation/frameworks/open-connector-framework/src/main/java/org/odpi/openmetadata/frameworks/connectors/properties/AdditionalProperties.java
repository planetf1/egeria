/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.frameworks.connectors.properties;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


/**
 * The AdditionalProperties class provides support for arbitrary properties to be added to a properties object.
 * It wraps a java.util.Map map object built around HashMap.
 */
public class AdditionalProperties extends AssetPropertyBase
{
    protected Map<String,Object>  additionalProperties = new HashMap<>();


    /**
     * Constructor for a new set of additional properties that are not connected either directly or indirectly to an asset.
     *
     * @param additionalProperties   map of properties for the metadata element.
     */
    public AdditionalProperties(Map<String,Object>  additionalProperties)
    {
        this(null, additionalProperties);
    }


    /**
     * Constructor for a new set of additional properties that are connected either directly or indirectly to an asset.
     *
     * @param parentAsset   description of the asset that these additional properties are attached to.
     * @param additionalProperties   map of properties for the metadata element.
     */
    public AdditionalProperties(AssetDescriptor     parentAsset,
                                Map<String,Object>  additionalProperties)
    {
        super(parentAsset);

        if (additionalProperties != null)
        {
            this.additionalProperties = new HashMap<>(additionalProperties);
        }
    }


    /**
     * Copy/clone Constructor for additional properties that are connected to an asset.
     *
     * @param parentAsset   description of the asset that these additional properties are attached to.
     * @param templateProperties   template object to copy.
     */
    public AdditionalProperties(AssetDescriptor   parentAsset, AdditionalProperties templateProperties)
    {
        super(parentAsset, templateProperties);

        /*
         * An empty properties object is created in the private variable declaration so nothing to do.
         */
        if (templateProperties != null)
        {
            /*
             * Process templateProperties if they are not null
             */
            Iterator<String> propertyNames = templateProperties.getPropertyNames();

            if (propertyNames != null)
            {
                while (propertyNames.hasNext())
                {
                    String newPropertyName = propertyNames.next();
                    Object newPropertyValue = templateProperties.getProperty(newPropertyName);

                    additionalProperties.put(newPropertyName, newPropertyValue);
                }
            }
        }
    }


    /**
     * Returns a list of the additional stored properties for the element.
     * If no stored properties are present then null is returned.
     *
     * @return list of additional properties
     */
    public Iterator<String> getPropertyNames()
    {
        return additionalProperties.keySet().iterator();
    }


    /**
     * Returns the requested additional stored property for the element.
     * If no stored property with that name is present then null is returned.
     *
     * @param name   String name of the property to return.
     * @return requested property value.
     */
    public Object getProperty(String name)
    {
        return additionalProperties.get(name);
    }


    /**
     * Test whether the supplied object is equal to this object.
     *
     * @param testObject   object to test
     * @return boolean indicating if the supplied object represents the same content as this object.
     */
    @Override
    public boolean equals(Object testObject)
    {
        if (this == testObject)
        {
            return true;
        }
        if (testObject == null || getClass() != testObject.getClass())
        {
            return false;
        }

        AdditionalProperties that = (AdditionalProperties) testObject;

        return additionalProperties != null ? additionalProperties.equals(that.additionalProperties) : that.additionalProperties == null;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "AdditionalProperties{" +
                "additionalProperties=" + additionalProperties +
                '}';
    }
}