/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.repositoryservices.rest.properties;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Classification;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceStatus;

import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * EntityCreateRequest carries the properties needed to create a new entity.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class EntityCreateRequest extends OMRSAPIRequest
{
    private String               entityTypeGUID         = null;
    private InstanceProperties   initialProperties      = null;
    private List<Classification> initialClassifications = null;
    private InstanceStatus       initialStatus          = null;


    /**
     * Default constructor
     */
    public EntityCreateRequest()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public EntityCreateRequest(OMRSAPIRequest template)
    {
        super(template);
    }


    /**
     * Return the type of the new entity.
     *
     * @return String guid
     */
    public String getEntityTypeGUID()
    {
        return entityTypeGUID;
    }


    /**
     * Set up the type of the new entity.
     *
     * @param entityTypeGUID String guid
     */
    public void setEntityTypeGUID(String entityTypeGUID)
    {
        this.entityTypeGUID = entityTypeGUID;
    }


    /**
     * Return the list of properties for the new entity.
     *
     * @return instance properties object
     */
    public InstanceProperties getInitialProperties()
    {
        if (initialProperties == null)
        {
            return null;
        }
        else
        {
            return new InstanceProperties(initialProperties);
        }
    }


    /**
     * Set up the initial properties for the entity.
     *
     * @param initialProperties InstanceProperties object
     */
    public void setInitialProperties(InstanceProperties initialProperties)
    {
        this.initialProperties = initialProperties;
    }


    /**
     * Return the list of classification for the new entity.
     *
     * @return list of classification objects
     */
    public List<Classification> getInitialClassifications()
    {
        if (initialClassifications == null)
        {
            return null;
        }
        else if (initialClassifications.isEmpty())
        {
            return null;
        }
        else
        {
            return initialClassifications;
        }
    }


    /**
     * Set up the list of classification for the new entity.
     *
     * @param initialClassifications list of classification objects
     */
    public void setInitialClassifications(List<Classification> initialClassifications)
    {
        this.initialClassifications = initialClassifications;
    }


    /**
     * Return the initial status for the new entity.
     *
     * @return instance status enum
     */
    public InstanceStatus getInitialStatus()
    {
        return initialStatus;
    }


    /**
     * Set up the initial status for the new entity.
     *
     * @param initialStatus instance status enum
     */
    public void setInitialStatus(InstanceStatus initialStatus)
    {
        this.initialStatus = initialStatus;
    }


    @Override
    public String toString()
    {
        return "EntityCreateRequest{" +
                "entityTypeGUID='" + entityTypeGUID + '\'' +
                ", initialProperties=" + initialProperties +
                ", initialClassifications=" + initialClassifications +
                ", initialStatus=" + initialStatus +
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
        if (!(objectToCompare instanceof EntityCreateRequest))
        {
            return false;
        }
        EntityCreateRequest that = (EntityCreateRequest) objectToCompare;
        return Objects.equals(getEntityTypeGUID(), that.getEntityTypeGUID()) &&
                Objects.equals(getInitialProperties(), that.getInitialProperties()) &&
                Objects.equals(getInitialClassifications(), that.getInitialClassifications()) &&
                getInitialStatus() == that.getInitialStatus();
    }


    /**
     * Create a hash code for this element type.
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {

        return Objects.hash(getEntityTypeGUID(),
                            getInitialProperties(),
                            getInitialClassifications(),
                            getInitialStatus());
    }
}
