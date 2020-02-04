/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.commonservices.odf.metadatamanagement.rest;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.*;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * DiscoveryServiceRegistrationRequestBody provides a structure for passing details of a discovery service
 * that is to be registered with a discovery engine.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class DiscoveryServiceRegistrationRequestBody extends ODFOMASAPIRequestBody
{
    private static final long    serialVersionUID = 1L;

    private String              discoveryServiceGUID      = null;
    private List<String>        discoveryRequestTypes     = null;
    private Map<String, String> defaultAnalysisParameters = null;

    /**
     * Default constructor
     */
    public DiscoveryServiceRegistrationRequestBody()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public DiscoveryServiceRegistrationRequestBody(DiscoveryServiceRegistrationRequestBody template)
    {
        super(template);

        if (template != null)
        {
            discoveryServiceGUID  = template.getDiscoveryServiceGUID();
            discoveryRequestTypes = template.getDiscoveryRequestTypes();
            defaultAnalysisParameters = template.getDefaultAnalysisParameters();
        }
    }


    /**
     * Return the unique identifier of the discovery service.
     *
     * @return guid
     */
    public String getDiscoveryServiceGUID()
    {
        return discoveryServiceGUID;
    }


    /**
     * Set up the unique identifier of the discovery service.
     *
     * @param discoveryServiceGUID guid
     */
    public void setDiscoveryServiceGUID(String discoveryServiceGUID)
    {
        this.discoveryServiceGUID = discoveryServiceGUID;
    }


    /**
     * Return the list of asset types that this discovery service supports.
     *
     * @return list of asset type names
     */
    public List<String> getDiscoveryRequestTypes()
    {
        if (discoveryRequestTypes == null)
        {
            return null;
        }
        else if (discoveryRequestTypes.isEmpty())
        {
            return null;
        }
        else
        {
            return new ArrayList<>(discoveryRequestTypes);
        }
    }


    /**
     * Return the list of analysis parameters that are passed the the discovery service (via
     * the discovery context).  These values can be overridden on the actual discovery request.
     *
     * @return map of parameter name to parameter value
     */
    public Map<String, String> getDefaultAnalysisParameters()
    {
        if (defaultAnalysisParameters == null)
        {
            return null;
        }
        else if (defaultAnalysisParameters.isEmpty())
        {
            return null;
        }
        else
        {
            return new HashMap<>(defaultAnalysisParameters);
        }
    }


    /**
     * Set up the  list of analysis parameters that are passed the the discovery service (via
     * the discovery context).  These values can be overridden on the actual discovery request.
     *
     * @param defaultAnalysisParameters map of parameter name to parameter value
     */
    public void setDefaultAnalysisParameters(Map<String, String> defaultAnalysisParameters)
    {
        this.defaultAnalysisParameters = defaultAnalysisParameters;
    }


    /**
     * Set up the list of asset types that this discovery service supports.
     *
     * @param discoveryRequestTypes list of asset type names
     */
    public void setDiscoveryRequestTypes(List<String> discoveryRequestTypes)
    {
        this.discoveryRequestTypes = discoveryRequestTypes;
    }


    /**
     * JSON-style toString.
     *
     * @return list of properties and their values.
     */
    @Override
    public String toString()
    {
        return "DiscoveryServiceRegistrationRequestBody{" +
                "discoveryServiceGUID='" + discoveryServiceGUID + '\'' +
                ", discoveryRequestTypes=" + discoveryRequestTypes +
                ", defaultAnalysisParameters=" + defaultAnalysisParameters +
                '}';
    }


    /**
     * Equals method that returns true if containing properties are the same.
     *
     * @param objectToCompare object to compare
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
        DiscoveryServiceRegistrationRequestBody that = (DiscoveryServiceRegistrationRequestBody) objectToCompare;
        return Objects.equals(discoveryServiceGUID, that.discoveryServiceGUID) &&
                Objects.equals(discoveryRequestTypes, that.discoveryRequestTypes) &&
                Objects.equals(defaultAnalysisParameters, that.defaultAnalysisParameters);
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(discoveryServiceGUID, discoveryRequestTypes, defaultAnalysisParameters);
    }
}
