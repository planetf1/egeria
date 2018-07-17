/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * MapPropertyValue stores the values of a map within an entity, struct or relationship properties.
 * The elements of the map are stored in an InstanceProperties map.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class MapPropertyValue extends InstancePropertyValue
{
    private  InstanceProperties    mapValues = null;


    /**
     * Default constructor sets the map to empty.
     */
    public MapPropertyValue()
    {
        super(InstancePropertyCategory.MAP);
    }


    /**
     * Copy/clone constructor set up the map using the supplied template.
     *
     * @param template ArrayPropertyValue
     */
    public MapPropertyValue(MapPropertyValue template)
    {
        super(template);

        if (template !=null)
        {
            mapValues = template.getMapValues();
        }
    }


    /**
     * Delegate the process of cloning to the subclass.
     *
     * @return subclass of InstancePropertyValue
     */
    public  InstancePropertyValue cloneFromSubclass()
    {
        return new MapPropertyValue(this);
    }


    /**
     * Return the number of elements in the map.
     *
     * @return int map size
     */
    public int getMapElementCount()
    {
        if (mapValues == null)
        {
            return 0;
        }
        else
        {
            return mapValues.getPropertyCount();
        }
    }


    /**
     * Return a copy of the map elements.
     *
     * @return InstanceProperties containing the map elements
     */
    public InstanceProperties getMapValues()
    {
        if (mapValues == null)
        {
            return mapValues;
        }
        else
        {
            return new InstanceProperties(mapValues);
        }
    }


    /**
     * Add or update an element in the map.
     * If a null is supplied for the property name, an OMRS runtime exception is thrown.
     * If a null is supplied for the property value, the property is removed.
     *
     * @param propertyName String name
     * @param propertyValue InstancePropertyValue value to store
     */
    public void setMapValue(String  propertyName, InstancePropertyValue  propertyValue)
    {
        if (mapValues == null)
        {
            mapValues = new InstanceProperties();
        }
        mapValues.setProperty(propertyName, propertyValue);
    }


    /**
     * Set up the map elements in one call.
     *
     * @param mapValues InstanceProperties containing the array elements
     */
    public void setMapValues(InstanceProperties mapValues) { this.mapValues = mapValues; }


    /**
     * Standard toString method.
     *
     * @return JSON style description of variables.
     */
    @Override
    public String toString()
    {
        return "MapPropertyValue{" +
                "mapValues=" + mapValues +
                ", mapElementCount=" + getMapElementCount() +
                ", instancePropertyCategory=" + getInstancePropertyCategory() +
                ", typeGUID='" + getTypeGUID() + '\'' +
                ", typeName='" + getTypeName() + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o)
        {
            return true;
        }
        if (o == null || getClass() != o.getClass())
        {
            return false;
        }
        MapPropertyValue that = (MapPropertyValue) o;
        return Objects.equals(mapValues, that.mapValues);
    }

    @Override
    public int hashCode()
    {

        return Objects.hash(mapValues);
    }
}
