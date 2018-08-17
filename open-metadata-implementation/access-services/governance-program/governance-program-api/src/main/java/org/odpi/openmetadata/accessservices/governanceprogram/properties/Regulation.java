/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.accessservices.governanceprogram.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * Regulation defines a legal obligation that the organization must satisfy.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class Regulation extends GovernanceDriver
{
    private String                   jurisdiction = null;
    private List<CertificationType>  certificationTypes = null;


    /**
     * Default constructor
     */
    public Regulation()
    {
        super();
        setType("Regulation");
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public Regulation(Regulation template)
    {
        super(template);

        if (template != null)
        {
            this.jurisdiction = template.getJurisdiction();
            this.certificationTypes = template.getCertificationTypes();
        }
    }


    /**
     * Return the jurisdiction that this regulation applies to.
     *
     * @return name of jurisdiction
     */
    public String getJurisdiction()
    {
        return jurisdiction;
    }


    /**
     * Set up the jurisdiction that this regulation applies to.
     *
     * @param jurisdiction name of jurisdiction
     */
    public void setJurisdiction(String jurisdiction)
    {
        this.jurisdiction = jurisdiction;
    }


    /**
     * Return the list of certification types that are recognized by this regulation.
     *
     * @return list of certification types
     */
    public List<CertificationType> getCertificationTypes()
    {
        return certificationTypes;
    }


    /**
     * Set up the list of certification types that are recognized by this regulation.
     *
     * @param certificationTypes list of certification types
     */
    public void setCertificationTypes(List<CertificationType> certificationTypes)
    {
        this.certificationTypes = certificationTypes;
    }


    /**
     * JSON-style toString
     *
     * @return string containing the properties and their values
     */
    @Override
    public String toString()
    {
        return "Regulation{" +
                "jurisdiction='" + jurisdiction + '\'' +
                ", certificationTypes=" + certificationTypes +
                ", relatedGovernanceDrivers=" + getRelatedGovernanceDrivers() +
                ", governancePolicies=" + getGovernancePolicies() +
                ", description='" + getDescription() + '\'' +
                ", scope='" + getScope() + '\'' +
                ", status=" + getStatus() +
                ", priority='" + getPriority() + '\'' +
                ", implications=" + getImplications() +
                ", outcomes=" + getOutcomes() +
                ", externalReferences=" + getExternalReferences() +
                ", additionalProperties=" + getAdditionalProperties() +
                ", governanceMetrics=" + getGovernanceMetrics() +
                ", governanceZones=" + getGovernanceZones() +
                ", GUID='" + getGUID() + '\'' +
                ", type='" + getType() + '\'' +
                ", documentId='" + getDocumentId() + '\'' +
                ", title='" + getTitle() + '\'' +
                ", summary='" + getSummary() + '\'' +
                '}';
    }


    /**
     * Test the properties of the Regulation to determine if the supplied object is equal to this one.
     *
     * @param objectToCompare object
     * @return boolean evaluation
     */
    @Override
    public boolean equals(Object objectToCompare)
    {
        if (this == objectToCompare)
        {
            return true;
        }
        if (!(objectToCompare instanceof Regulation))
        {
            return false;
        }
        if (!super.equals(objectToCompare))
        {
            return false;
        }
        Regulation that = (Regulation) objectToCompare;
        return Objects.equals(getJurisdiction(), that.getJurisdiction()) &&
                Objects.equals(getCertificationTypes(), that.getCertificationTypes());
    }
}
