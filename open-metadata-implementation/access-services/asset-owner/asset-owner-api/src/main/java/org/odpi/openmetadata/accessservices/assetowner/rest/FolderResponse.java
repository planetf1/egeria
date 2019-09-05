/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.assetowner.rest;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.accessservices.assetowner.properties.GovernanceZone;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;


/**
 * ZoneResponse is the response structure used on the OMAS REST API calls that return the properties
 * for a governance zone.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class FolderResponse extends AssetOwnerOMASAPIResponse
{
    private GovernanceZone governanceZone = null;


    /**
     * Default constructor
     */
    public FolderResponse()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public FolderResponse(FolderResponse template)
    {
        super(template);

        if (template != null)
        {
            this.governanceZone = template.getGovernanceZone();
        }
    }


    /**
     * Return the governanceZone result.
     *
     * @return bean
     */
    public GovernanceZone getGovernanceZone()
    {
        return governanceZone;
    }


    /**
     * Set up the governanceZone result.
     *
     * @param governanceZone - bean
     */
    public void setGovernanceZone(GovernanceZone governanceZone)
    {
        this.governanceZone = governanceZone;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "ZoneResponse{" +
                "zone='" + getGovernanceZone() + '\'' +
                ", relatedHTTPCode=" + getRelatedHTTPCode() +
                ", exceptionClassName='" + getExceptionClassName() + '\'' +
                ", exceptionErrorMessage='" + getExceptionErrorMessage() + '\'' +
                ", exceptionSystemAction='" + getExceptionSystemAction() + '\'' +
                ", exceptionUserAction='" + getExceptionUserAction() + '\'' +
                ", exceptionProperties=" + getExceptionProperties() +
                '}';
    }


    /**
     * Return comparison result based on the content of the properties.
     *
     * @param objectToCompare test object
     * @return result of comparison
     */
    @Override
    public boolean equals(Object objectToCompare)
    {
        if (this == objectToCompare)
        {
            return true;
        }
        if (!(objectToCompare instanceof FolderResponse))
        {
            return false;
        }
        if (!super.equals(objectToCompare))
        {
            return false;
        }
        FolderResponse that = (FolderResponse) objectToCompare;
        return Objects.equals(governanceZone, that.governanceZone);
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(governanceZone);
    }
}
