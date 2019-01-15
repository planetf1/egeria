/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances;


import com.fasterxml.jackson.annotation.*;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * InstanceHeader manages the attributes that are common to entities and relationship instances.  This includes
 * its unique identifier and URL along with information about its type, provenance and change history.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "class")
@JsonSubTypes({
        @JsonSubTypes.Type(value = EntitySummary.class, name = "EntitySummary"),
        @JsonSubTypes.Type(value = Relationship.class, name = "Relationship")
})
public abstract class InstanceHeader extends InstanceAuditHeader
{
    /*
     * Entities and relationships have unique identifiers.
     */
    private String                    guid                   = null;

    /*
     * Some metadata repositories offer a direct URL to access the instance.
     */
    private String                    instanceURL            = null;

    /**
     * Default Constructor sets the instance to nulls.
     */
    public InstanceHeader()
    {
        super();
    }


    /**
     * Copy/clone constructor set the value to those supplied in the template.
     *
     * @param template Instance header
     */
    public InstanceHeader(InstanceHeader    template)
    {
        super(template);

        if (template != null)
        {
            this.guid = template.getGUID();
            this.instanceURL = template.getInstanceURL();
        }
    }

    /**
     * Return the URL for this instance (or null if the metadata repository does not support instance URLs).
     *
     * @return String URL
     */
    public String getInstanceURL()
    {
        return instanceURL;
    }


    /**
     * Set up the URL for this instance (or null if the metadata repository does not support instance URLs).
     *
     * @param instanceURL String URL
     */
    public void setInstanceURL(String instanceURL)
    {
        this.instanceURL = instanceURL;
    }


    /**
     * Return the unique identifier for this instance.
     *
     * @return guid String unique identifier
     */
    public String getGUID() { return guid; }


    /**
     * Set up the unique identifier for this instance.
     *
     * @param guid String unique identifier
     */
    public void setGUID(String guid) { this.guid = guid; }


    /**
     * Standard toString method.
     *
     * @return JSON style description of variables.
     */
    @Override
    public String toString()
    {
        return "InstanceHeader{" +
                "guid='" + guid + '\'' +
                ", instanceURL='" + instanceURL + '\'' +
                ", type=" + getType() +
                ", instanceProvenanceType=" + getInstanceProvenanceType() +
                ", metadataCollectionId='" + getMetadataCollectionId() + '\'' +
                ", metadataCollectionName='" + getMetadataCollectionName() + '\'' +
                ", instanceLicense='" + getInstanceLicense() + '\'' +
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
        if (objectToCompare == null || getClass() != objectToCompare.getClass())
        {
            return false;
        }
        if (!super.equals(objectToCompare))
        {
            return false;
        }
        InstanceHeader that = (InstanceHeader) objectToCompare;
        return Objects.equals(guid, that.guid) &&
                Objects.equals(getInstanceURL(), that.getInstanceURL());
    }



    /**
     * Return a hash code based on the values of this object.
     *
     * @return in hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), guid, getInstanceURL());
    }
}
