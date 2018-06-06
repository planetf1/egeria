/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * EntityDetail stores all of the type-specific properties for the entity.  These properties can be
 * requested in an InstanceProperties object on request.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class EntityDetail extends EntitySummary
{
    private   InstanceProperties    entityProperties = null;

    /**
     * Default Constructor - no properties established
     */
    public EntityDetail()
    {
        super();
    }


    /**
     * Copy/clone constructor - properties copied from template.
     *
     * @param templateElement - element to copy.
     */
    public EntityDetail(EntityDetail   templateElement)
    {
        super(templateElement);

        if (templateElement != null)
        {
            entityProperties = templateElement.getProperties();
        }
    }


    /**
     * Return a copy of all of the properties for this entity.  Null means no properties exist.
     *
     * @return InstanceProperties
     */
    public InstanceProperties  getProperties()
    {
        if (entityProperties == null)
        {
            return entityProperties;
        }
        else
        {
            return new InstanceProperties(entityProperties);
        }
    }


    /**
     * Set up the properties for this entity.
     *
     * @param newProperties - InstanceProperties object
     */
    public void setProperties(InstanceProperties  newProperties)
    {
        entityProperties = newProperties;
    }


    /**
     * Standard toString method.
     *
     * @return JSON style description of variables.
     */
    @Override
    public String toString()
    {
        return "EntityDetail{" +
                "entityProperties=" + entityProperties +
                ", classifications=" + getClassifications() +
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
}
