/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.discovery.properties;

import org.odpi.openmetadata.frameworks.connectors.properties.beans.PropertyBase;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * DataField describes a single data field (column, attribute or property) discovered during the analysis of an asset.  It provides an anchor for
 * annotations that are specific to the field.
 */
public class DataField extends PropertyBase
{
    private static final long   serialVersionUID = 1L;

    private int                 dataFieldPosition    = 0;
    private String              dataFieldName        = null;
    private String              dataFieldType        = null;
    private int                 dataFieldAnnotations = 0;
    private Map<String, String> additionalProperties = null;
    private int                 nestedDataFields     = 0;
    private Map<String, Object> extendedProperties   = null;


    /**
     * Default constructor
     */
    public DataField()
    {
    }


    /**
     * Copy clone constructor
     *
     * @param template object to copy
     */
    public DataField(DataField template)
    {
        super(template);

        if (template != null)
        {
            dataFieldPosition = template.getDataFieldPosition();
            dataFieldName = template.getDataFieldName();
            dataFieldType = template.getDataFieldType();
            dataFieldAnnotations = template.getDataFieldAnnotations();
            additionalProperties = template.getAdditionalProperties();
            nestedDataFields = template.getNestedDataFields();
            extendedProperties = template.getExtendedProperties();
        }
    }


    /**
     * Return the position (index) of the data field in the schema.
     *
     * @return integer
     */
    public int getDataFieldPosition()
    {
        return dataFieldPosition;
    }


    /**
     * Set up the position (index) of the data field in the schema.
     *
     * @param dataFieldPosition integer
     */
    public void setDataFieldPosition(int dataFieldPosition)
    {
        this.dataFieldPosition = dataFieldPosition;
    }


    /**
     * Return the name of this data field.
     *
     * @return string name
     */
    public String getDataFieldName()
    {
        return dataFieldName;
    }


    /**
     * Set up the name of this data field.
     *
     * @param dataFieldName string name
     */
    public void setDataFieldName(String dataFieldName)
    {
        this.dataFieldName = dataFieldName;
    }


    /**
     * Return the name of type of this data field.
     *
     * @return string type name
     */
    public String getDataFieldType()
    {
        return dataFieldType;
    }


    /**
     * Set up the name of type of this data field.
     *
     * @param dataFieldType string type name
     */
    public void setDataFieldType(String dataFieldType)
    {
        this.dataFieldType = dataFieldType;
    }


    /**
     * Return the number of the annotations currently attached to this data field.
     *
     * @return integer
     */
    public int getDataFieldAnnotations()
    {
        return dataFieldAnnotations;
    }


    /**
     * Set up the number of the annotations currently attached to this data field.
     *
     * @param dataFieldAnnotations integer
     */
    public void setDataFieldAnnotations(int dataFieldAnnotations)
    {
        this.dataFieldAnnotations = dataFieldAnnotations;
    }


    /**
     * Return any additional properties.
     *
     * @return map of property name to property value
     */
    public Map<String, String> getAdditionalProperties()
    {
        if (additionalProperties == null)
        {
            return null;
        }
        else if (additionalProperties.isEmpty())
        {
            return null;
        }

        return new HashMap<>(additionalProperties);
    }


    /**
     * Set up any additional properties.
     *
     * @param additionalProperties map of property name to property value
     */
    public void setAdditionalProperties(Map<String, String> additionalProperties)
    {
        this.additionalProperties = additionalProperties;
    }


    /**
     * Return the number of nested data fields.
     *
     * @return integer
     */
    public int getNestedDataFields()
    {
        return nestedDataFields;
    }


    /**
     * Set up the number of nested data fields.
     *
     * @param nestedDataFields integer
     */
    public void setNestedDataFields(int nestedDataFields)
    {
        this.nestedDataFields = nestedDataFields;
    }


    /**
     * Return any properties from subtypes.
     *
     * @return map of property name to property value
     */
    public Map<String, Object> getExtendedProperties()
    {
        if (extendedProperties == null)
        {
            return null;
        }
        else if (extendedProperties.isEmpty())
        {
            return null;
        }

        return new HashMap<>(extendedProperties);
    }


    /**
     * Set up any properties from subtypes.
     *
     * @param extendedProperties map of property name to property value
     */
    public void setExtendedProperties(Map<String, Object> extendedProperties)
    {
        this.extendedProperties = extendedProperties;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "DataField{" +
                       "dataFieldPosition=" + dataFieldPosition +
                       ", dataFieldName='" + dataFieldName + '\'' +
                       ", dataFieldType='" + dataFieldType + '\'' +
                       ", dataFieldAnnotations=" + dataFieldAnnotations +
                       ", additionalProperties=" + additionalProperties +
                       ", nestedDataFields=" + nestedDataFields +
                       ", extendedProperties=" + extendedProperties +
                '}';
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
        if (objectToCompare == null || getClass() != objectToCompare.getClass())
        {
            return false;
        }
        DataField dataField = (DataField) objectToCompare;
        return getDataFieldPosition() == dataField.getDataFieldPosition() &&
                       getNestedDataFields() == dataField.getNestedDataFields() &&
                       Objects.equals(getDataFieldName(), dataField.getDataFieldName()) &&
                       Objects.equals(getDataFieldType(), dataField.getDataFieldType()) &&
                       Objects.equals(getDataFieldAnnotations(), dataField.getDataFieldAnnotations()) &&
                       Objects.equals(getAdditionalProperties(), dataField.getAdditionalProperties());
    }


    /**
     * Create a hash code for this element type.
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(getDataFieldPosition(), getDataFieldName(), getDataFieldType(), getDataFieldAnnotations(),
                            getAdditionalProperties(), getNestedDataFields());
    }
}
