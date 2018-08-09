/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances;

import com.fasterxml.jackson.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;


/**
 * EntitySummary is a POJO that provides the basic header attributes for an open metadata entity.
 * This includes a summary of its type, its unique
 * identifier (guid) last update data and a list of the classifications for the entity.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "class")
@JsonSubTypes({
        @JsonSubTypes.Type(value = EntityDetail.class, name = "EntityDetail"),
        @JsonSubTypes.Type(value = EntityProxy.class, name = "EntityProxy")
})
public class EntitySummary extends InstanceHeader
{
    /*
     * Details of classifications.
     */
    private List<Classification> classifications = null;


    /**
     * Default constructor creates an empty entity
     */
    public EntitySummary()
    {
        /*
         * Nothing to do as everything already initialized
         */
        super();
    }

    /**
     * Copy/clone constructor.
     *
     * @param templateElement template to copy.
     */
    public EntitySummary(EntitySummary   templateElement)
    {
        super(templateElement);

        if (templateElement != null)
        {
            this.setClassifications(templateElement.getClassifications());
        }
    }


    /**
     * Return a copy of the classifications for the entity.  This is a list stored in a newly initialized
     * iterator.
     *
     * @return Classifications list
     */
    public List<Classification> getClassifications()
    {
        if (classifications == null)
        {
            return null;
        }
        else if (classifications.isEmpty())
        {
            return null;
        }
        else
        {
            return new ArrayList<>(classifications);
        }
    }


    /**
     * Set up the classifications for an entity.  This is stored as an iterator.
     *
     * @param classifications Classification list
     */
    public void setClassifications(List<Classification> classifications)
    {
        this.classifications = classifications;
    }


    /**
     * Standard toString method.
     *
     * @return JSON style description of variables.
     */
    @Override
    public String toString()
    {
        return "EntitySummary{" +
                "classifications=" + classifications +
                ", type=" + getType() +
                ", instanceProvenanceType=" + getInstanceProvenanceType() +
                ", metadataCollectionId='" + getMetadataCollectionId() + '\'' +
                ", instanceURL='" + getInstanceURL() + '\'' +
                ", GUID='" + getGUID() + '\'' +
                ", status=" + getStatus() +
                ", createdBy='" + getCreatedBy() + '\'' +
                ", updatedBy='" + getUpdatedBy() + '\'' +
                ", createTime=" + getCreateTime() +
                ", updateTime=" + getUpdateTime() +
                ", version=" + getVersion() +
                ", statusOnDelete=" + getStatusOnDelete() +
                '}';
    }


    /**
     * Validate that an object is equal depending on their stored values.
     *
     * @param objectToCompare object
     * @return boolean result
     */
    @Override
    public boolean equals(Object objectToCompare)
    {
        if (this == objectToCompare)
        {
            return true;
        }
        if (!(objectToCompare instanceof EntitySummary))
        {
            return false;
        }
        if (!super.equals(objectToCompare))
        {
            return false;
        }
        EntitySummary that = (EntitySummary) objectToCompare;
        return Objects.equals(getClassifications(), that.getClassifications());
    }


    /**
     * Return a hash code based on the values of this object.
     *
     * @return in hash code
     */
    @Override
    public int hashCode()
    {

        return Objects.hash(super.hashCode(), getClassifications());
    }
}
