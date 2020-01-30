/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.discovery.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * SuspectDuplicateAnnotation is the annotation used to record details of an asset that seems to be a duplicate of the asset being
 * analysed by a discovery service.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class SuspectDuplicateAnnotation extends Annotation
{
    private static final long serialVersionUID = 1L;

    private List<String>  duplicateAnchorGUIDs = null;
    private List<String>  matchingPropertyNames = null;
    private List<String>  matchingClassificationNames = null;
    private List<String>  matchingAttachmentGUIDs = null;
    private List<String>  matchingRelationshipGUIDs = null;


    /**
     * Default constructor
     */
    public SuspectDuplicateAnnotation()
    {
        super();
    }


    /**
     * Copy/clone constructor.
     *
     * @param template object to copy
     */
    public SuspectDuplicateAnnotation(SuspectDuplicateAnnotation template)
    {
        super(template);

        if (template != null)
        {
            duplicateAnchorGUIDs = template.getDuplicateAnchorGUIDs();
            matchingPropertyNames = template.getMatchingPropertyNames();
            matchingClassificationNames = template.getMatchingClassificationNames();
            matchingAttachmentGUIDs = template.getMatchingAttachmentGUIDs();
            matchingRelationshipGUIDs = template.getMatchingRelationshipGUIDs();
        }
    }


    /**
     * Return the list of unique identifiers for the Assets that are identifier as duplicate suspects.
     *
     * @return list of string guids
     */
    public List<String> getDuplicateAnchorGUIDs()
    {
        return duplicateAnchorGUIDs;
    }


    /**
     * Set up the list of unique identifiers for the Assets that are identifier as duplicate suspects.
     *
     * @param duplicateAnchorGUIDs list of string guids
     */
    public void setDuplicateAnchorGUIDs(List<String> duplicateAnchorGUIDs)
    {
        this.duplicateAnchorGUIDs = duplicateAnchorGUIDs;
    }


    /**
     * Return the list of property names whose values match in all of the duplicate suspects.
     *
     * @return list of property names
     */
    public List<String> getMatchingPropertyNames()
    {
        return matchingPropertyNames;
    }


    /**
     * Set up the list of property names whose values match in all of the duplicate suspects.
     *
     * @param matchingPropertyNames list of property names
     */
    public void setMatchingPropertyNames(List<String> matchingPropertyNames)
    {
        this.matchingPropertyNames = matchingPropertyNames;
    }


    /**
     * Return the list of classifications that match in all of the duplicate suspects.
     *
     * @return list of classification names
     */
    public List<String> getMatchingClassificationNames()
    {
        return matchingClassificationNames;
    }


    /**
     * Set up the list of classifications that match in all of the duplicate suspects.
     *
     * @param matchingClassificationNames list of classification names
     */
    public void setMatchingClassificationNames(List<String> matchingClassificationNames)
    {
        this.matchingClassificationNames = matchingClassificationNames;
    }


    /**
     * Return the list of unique identifiers for attachments that match in all of the duplicate suspects.
     *
     * @return list of string guids
     */
    public List<String> getMatchingAttachmentGUIDs()
    {
        return matchingAttachmentGUIDs;
    }


    /**
     * Set up the list of unique identifiers for attachments that match in all of the duplicate suspects.
     *
     * @param matchingAttachmentGUIDs list of string guids
     */
    public void setMatchingAttachmentGUIDs(List<String> matchingAttachmentGUIDs)
    {
        this.matchingAttachmentGUIDs = matchingAttachmentGUIDs;
    }


    /**
     * Return the list of relationships that are common across all of the duplicate suspects.
     *
     * @return list of string guids
     */
    public List<String> getMatchingRelationshipGUIDs()
    {
        return matchingRelationshipGUIDs;
    }


    /**
     * Set up the list of relationships that are common across all of the duplicate suspects.
     *
     * @param matchingRelationshipGUIDs  list of string guids
     */
    public void setMatchingRelationshipGUIDs(List<String> matchingRelationshipGUIDs)
    {
        this.matchingRelationshipGUIDs = matchingRelationshipGUIDs;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "SuspectDuplicateAnnotation{" +
                "matchingClassificationNames=" + matchingClassificationNames +
                ", matchingAttachmentGUIDs=" + matchingAttachmentGUIDs +
                ", matchingRelationshipGUIDs=" + matchingRelationshipGUIDs +
                ", annotationType='" + annotationType + '\'' +
                ", summary='" + summary + '\'' +
                ", confidenceLevel=" + confidenceLevel +
                ", expression='" + expression + '\'' +
                ", explanation='" + explanation + '\'' +
                ", analysisStep='" + analysisStep + '\'' +
                ", jsonProperties='" + jsonProperties + '\'' +
                ", numAttachedAnnotations=" + numAttachedAnnotations +
                ", annotationStatus=" + annotationStatus +
                ", reviewDate=" + reviewDate +
                ", steward='" + steward + '\'' +
                ", reviewComment='" + reviewComment + '\'' +
                ", additionalProperties=" + additionalProperties +
                ", type=" + type +
                ", guid='" + guid + '\'' +
                ", url='" + url + '\'' +
                ", classifications=" + classifications +
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
        if (!super.equals(objectToCompare))
        {
            return false;
        }
        SuspectDuplicateAnnotation that = (SuspectDuplicateAnnotation) objectToCompare;
        return Objects.equals(duplicateAnchorGUIDs, that.duplicateAnchorGUIDs) &&
                Objects.equals(matchingPropertyNames, that.matchingPropertyNames) &&
                Objects.equals(matchingClassificationNames, that.matchingClassificationNames) &&
                Objects.equals(matchingAttachmentGUIDs, that.matchingAttachmentGUIDs) &&
                Objects.equals(matchingRelationshipGUIDs, that.matchingRelationshipGUIDs);
    }


    /**
     * Create a hash code for this element type.
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), duplicateAnchorGUIDs, matchingPropertyNames, matchingClassificationNames, matchingAttachmentGUIDs, matchingRelationshipGUIDs);
    }
}
