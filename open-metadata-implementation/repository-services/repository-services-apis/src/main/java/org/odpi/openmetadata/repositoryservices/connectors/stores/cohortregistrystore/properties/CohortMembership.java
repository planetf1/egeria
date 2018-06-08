/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.repositoryservices.connectors.stores.cohortregistrystore.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;

/**
 * CohortMembership describes the structure of the cohort registry store.  It contains details
 * of the local registration and a list of remote member registrations.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class CohortMembership implements Serializable
{
    private static final long serialVersionUID = 1L;

    private MemberRegistration            localRegistration   = null;
    private List<MemberRegistration>      remoteRegistrations = null;

    /**
     * Default constructor
     */
    public CohortMembership()
    {
    }


    /**
     * Return the description of the local server's registration with the cohort.
     *
     * @return local registration object
     */
    public MemberRegistration getLocalRegistration()
    {
        return localRegistration;
    }


    /**
     * Set up the description of the local server's registration with the cohort.
     *
     * @param localRegistration - local registration object
     */
    public void setLocalRegistration(MemberRegistration localRegistration)
    {
        this.localRegistration = localRegistration;
    }


    /**
     * Return details of each of the remote repositories registered with this cohort.
     *
     * @return details about the remote members of the cohort
     */
    public List<MemberRegistration> getRemoteRegistrations()
    {
        if (remoteRegistrations == null)
        {
            return null;
        }
        else
        {
            return new ArrayList<>(remoteRegistrations);
        }
    }


    /**
     * Set up details of each of the remote repositories registered with this cohort.
     *
     * @param remoteRegistrations - details about the remote members of the cohort
     */
    public void setRemoteRegistrations(List<MemberRegistration> remoteRegistrations)
    {
        if (remoteRegistrations == null)
        {
            this.remoteRegistrations = null;
        }
        else
        {
            this.remoteRegistrations = new ArrayList<>(remoteRegistrations);
        }
    }
}
