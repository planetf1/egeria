/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.governanceengine.api.events;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * GovernanceEngineEventTypes describes the different types of events produced by the GovernanceEngine OMAS.
 */
@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public enum GovernanceEngineEventType implements Serializable {

    NEW_CLASSIFIED_ASSET(0, "ClassifiedAsset", "A governed classification has been assigned to an asset."),
    RE_CLASSIFIED_ASSET(1, "ReClassifiedAsset", "A classification has been changed for governed asset."),
    DE_CLASSIFIED_ASSET(2, "DeClassifiedAsset", "A classification has been deleted for governed asset."),
    DELETED_ASSET(3, "DeletedAsset", "A governed asset has been deleted."),
    UNKNOWN_GOVERNANCE_ENGINE_EVENT(4, "UnknownGovernanceEngineEvent", "A Governance Engine event that is not recognized by the local handlers.");

    private static final long serialVersionUID = 1L;

    private int eventTypeCode;
    private String eventTypeName;
    private String eventTypeDescription;


    /**
     * Default Constructor - sets up the specific values for this instance of the enum.
     *
     * @param eventTypeCode        - int identifier used for indexing based on the enum.
     * @param eventTypeName        - string name used for messages that include the enum.
     * @param eventTypeDescription - default description for the enum value - used when natural resource
     *                             bundle is not available.
     */
    GovernanceEngineEventType(int eventTypeCode, String eventTypeName, String eventTypeDescription) {
        this.eventTypeCode = eventTypeCode;
        this.eventTypeName = eventTypeName;
        this.eventTypeDescription = eventTypeDescription;
    }


    /**
     * Return the int identifier used for indexing based on the enum.
     *
     * @return int identifier code
     */
    public int getEventTypeCode() {
        return eventTypeCode;
    }


    /**
     * Return the string name used for messages that include the enum.
     *
     * @return String name
     */
    public String getEventTypeName() {
        return eventTypeName;
    }


    /**
     * Return the default description for the enum value - used when natural resource
     * bundle is not available.
     *
     * @return String default description
     */
    public String getEventTypeDescription() {
        return eventTypeDescription;
    }
}
