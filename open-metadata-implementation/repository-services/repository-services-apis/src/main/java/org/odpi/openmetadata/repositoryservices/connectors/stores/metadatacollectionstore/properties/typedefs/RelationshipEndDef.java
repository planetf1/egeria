/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * RelationshipEndDef describes the type of the entity and the attribute information for one end of a RelationshipDef.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class RelationshipEndDef extends TypeDefElementHeader
{
    private TypeDefLink                entityType               = null;
    private String                     attributeName            = null;
    private String                     attributeDescription     = null;
    private String                     attributeDescriptionGUID = null;
    private RelationshipEndCardinality attributeCardinality     = RelationshipEndCardinality.UNKNOWN;


    /**
     * Default constructor - create an empty end
     */
    public RelationshipEndDef()
    {
        super();
    }


    /**
     * Copy/clone constructor - copy the supplied template into the new end.
     *
     * @param template - RelationshipEndDef
     */
    public RelationshipEndDef(RelationshipEndDef template)
    {
        super(template);

        if (template != null)
        {
            entityType = template.getEntityType();
            attributeName = template.getAttributeName();
            attributeCardinality = template.getAttributeCardinality();
            attributeDescription = template.getAttributeDescription();
        }
    }


    /**
     * Return the identifiers of the EntityDef describing the type of entity on this end of the relationship.
     *
     * @return TypeDefLink unique identifiers
     */
    public TypeDefLink getEntityType()
    {
        if (entityType == null)
        {
            return entityType;
        }
        else
        {
            return new TypeDefLink(entityType);
        }
    }


    /**
     * Set up the guid of the EntityDef describing the type of entity on this end of the relationship.
     *
     * @param entityType - TypeDefLink unique identifiers for the entity's type
     */
    public void setEntityType(TypeDefLink entityType)
    {
        this.entityType = entityType;
    }


    /**
     * Return the attribute name used to describe this end of the relationship
     *
     * @return String name for the attribute
     */
    public String getAttributeName()
    {
        return attributeName;
    }


    /**
     * Set up the attribute name used to describe this end of the relationship.
     *
     * @param attributeName - String name for the attribute
     */
    public void setAttributeName(String attributeName)
    {
        this.attributeName = attributeName;
    }


    /**
     * Return the cardinality for this end of the relationship.
     *
     * @return RelationshipEndCardinality Enum
     */
    public RelationshipEndCardinality getAttributeCardinality()
    {
        return attributeCardinality;
    }


    /**
     * Set up the cardinality for this end of the relationship.
     *
     * @param attributeCardinality - RelationshipEndCardinality Enum
     */
    public void setAttributeCardinality(RelationshipEndCardinality attributeCardinality)
    {
        this.attributeCardinality = attributeCardinality;
    }


    /**
     * Return the attributeDescription of this end of the relationship.
     *
     * @return String attributeDescription
     */
    public String getAttributeDescription()
    {
        return attributeDescription;
    }


    /**
     * Set up the attributeDescription for this end of the relationship.
     *
     * @param attributeDescription - String
     */
    public void setAttributeDescription(String attributeDescription)
    {
        this.attributeDescription = attributeDescription;
    }


    /**
     * Return the unique identifier (guid) of the glossary term that describes this RelationshipEndDef.
     *
     * @return String guid
     */
    public String getAttributeDescriptionGUID()
    {
        return attributeDescriptionGUID;
    }


    /**
     * Set up the unique identifier (guid) of the glossary term that describes this RelationshipEndDef.
     *
     * @param attributeDescriptionGUID - String guid
     */
    public void setAttributeDescriptionGUID(String attributeDescriptionGUID)
    {
        this.attributeDescriptionGUID = attributeDescriptionGUID;
    }


    /**
     * Standard toString method.
     *
     * @return JSON style attributeDescription of variables.
     */
    @Override
    public String toString()
    {
        return "RelationshipEndDef{" +
                "entityType='" + entityType + '\'' +
                ", attributeName='" + attributeName + '\'' +
                ", attributeDescription='" + attributeDescription + '\'' +
                ", attributeCardinality=" + attributeCardinality +
                '}';
    }
}
