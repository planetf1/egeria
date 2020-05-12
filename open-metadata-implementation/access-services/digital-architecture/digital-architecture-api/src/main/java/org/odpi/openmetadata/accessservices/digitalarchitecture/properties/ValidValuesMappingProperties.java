/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.digitalarchitecture.properties;


import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * ValidValuesMappingProperties is a java bean used to create a mapping between two valid values from different valid value sets.
 */
public class ValidValuesMappingProperties implements Serializable
{
    private static final long     serialVersionUID = 1L;

    private String associationDescription = null;
    private int    confidence             = 0;
    private String steward                = null;
    private String notes                  = null;


    /**
     * Default constructor
     */
    public ValidValuesMappingProperties()
    {
    }


    /**
     * Copy/clone constructor.  Note, this is a deep copy
     *
     * @param template object to copy
     */
    public ValidValuesMappingProperties(ValidValuesMappingProperties template)
    {
        if (template != null)
        {
            associationDescription = template.getAssociationDescription();
            confidence             = template.getConfidence();
            steward                = template.getSteward();
            notes                  = template.getNotes();
        }
    }


    /**
     * Returns the short description of the type of association.
     *
     * @return String text
     */
    public String getAssociationDescription()
    {
        return associationDescription;
    }


    /**
     * Set up the short description of the type of association.
     *
     * @param associationDescription String text
     */
    public void setAssociationDescription(String associationDescription)
    {
        this.associationDescription = associationDescription;
    }


    /**
     * Return the confidence level (0-100) that the mapping is correct.
     *
     * @return int
     */
    public int getConfidence()
    {
        return confidence;
    }


    /**
     * Set up the confidence level (0-100) that the mapping is correct.
     *
     * @param confidence int
     */
    public void setConfidence(int confidence)
    {
        this.confidence = confidence;
    }


    /**
     * Returns the id of the steward responsible for the mapping.
     *
     * @return String id
     */
    public String getSteward()
    {
        return steward;
    }


    /**
     * Set up the the id of the steward responsible for the mapping.
     *
     * @param steward String id
     */
    public void setSteward(String steward)
    {
        this.steward = steward;
    }



    /**
     * Return the additional values associated with the symbolic name.
     *
     * @return string text
     */
    public String getNotes()
    {
        return notes;
    }


    /**
     * Set up the additional values associated with the symbolic name.
     *
     * @param notes string text
     */
    public void setNotes(String notes)
    {
        this.notes = notes;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "ValidValuesMappingProperties{" +
                "associationDescription='" + associationDescription + '\'' +
                ", confidence=" + confidence +
                ", steward='" + steward + '\'' +
                ", notes='" + notes + '\'' +
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
        ValidValuesMappingProperties that = (ValidValuesMappingProperties) objectToCompare;
        return confidence == that.confidence &&
                Objects.equals(associationDescription, that.associationDescription) &&
                Objects.equals(steward, that.steward) &&
                Objects.equals(notes, that.notes);
    }


    /**
     * Return has code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(associationDescription, confidence, steward, notes);
    }
}
