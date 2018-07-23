/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.repositoryservices.auditlog;

/**
 * OMRSAuditingComponent provides identifying and background information about the components writing log records
 * to the OMRS Audit log.  This is to help someone reading the OMRS Audit Log understand the records.
 */
public enum OMRSAuditingComponent
{
    UNKNOWN (0,
             "<Unknown>", "Uninitialized component name", null),

    AUDIT_LOG (1,
             "Audit Log",
             "Reads and writes records to the Open Metadata Repository Services (OMRS) audit log.",
             "https://cwiki.apache.org/confluence/display/ATLAS/OMRS+Audit+Log"),

    OPERATIONAL_SERVICES (3,
             "Operational Services",
             "Supports the administration services for the Open Metadata Repository Services (OMRS).",
             "https://cwiki.apache.org/confluence/display/ATLAS/OMRS+Operational+Services"),

    ARCHIVE_MANAGER (4,
             "Archive Manager",
             "Manages the loading of Open Metadata Archives into an open metadata repository.",
             "https://cwiki.apache.org/confluence/display/ATLAS/OMRS+Archive+Manager"),

    ENTERPRISE_CONNECTOR_MANAGER (5,
             "Enterprise Connector Manager",
             "Manages the list of open metadata repositories that the Enterprise OMRS Repository Connector " +
                                          "should call to retrieve an enterprise view of the metadata collections " +
                                          "supported by these repositories",
             "https://cwiki.apache.org/confluence/display/ATLAS/OMRS+Enterprise+Connector+Manager"),

    ENTERPRISE_REPOSITORY_CONNECTOR (6,
             "Enterprise Repository Connector",
             "Supports enterprise access to the list of open metadata repositories registered " +
                                             "with the OMRS Enterprise Connector Manager.",
             "https://cwiki.apache.org/confluence/display/ATLAS/Enterprise+OMRS+Repository+Connector"),

    LOCAL_REPOSITORY_CONNECTOR (7,
             "Local Repository Connector",
             "Supports access to metadata stored in the local server's repository and ensures " +
                                        "repository events are generated when metadata changes in the local repository",
             "https://cwiki.apache.org/confluence/display/ATLAS/Local+OMRS+Repository+Connector"),

    TYPEDEF_MANAGER (8,
             "Local TypeDef Manager",
             "Supports an in-memory cache for open metadata type definitions (TypeDefs) used for " +
                             "verifying TypeDefs in use in other open metadata repositories and for " +
                             "constructing new metadata instances.",
             "https://cwiki.apache.org/confluence/display/ATLAS/Local+OMRS+TypeDef+Manager"),

    INSTANCE_EVENT_PROCESSOR (9,
             "Local Inbound Instance Event Processor",
             "Supports the loading of reference metadata into the local repository that has come from other members of the local server's cohorts and open metadata archives.",
             "https://cwiki.apache.org/confluence/display/ATLAS/Local+OMRS+Instance+Event+Processor"),

    REPOSITORY_EVENT_MANAGER (10,
             "Repository Event Manager",
             "Distribute repository events (TypeDefs, Entity and Instance events) between internal OMRS components within a server.",
             "https://cwiki.apache.org/confluence/display/ATLAS/OMRS+Repository+Event+Manager"),

    REST_SERVICES (11,
             "Repository REST Services",
             "Provides the server-side support the the OMRS Repository Services REST API.",
             "https://cwiki.apache.org/confluence/display/ATLAS/OMRS+Repository+REST+Services"),

    REST_REPOSITORY_CONNECTOR (12,
             "REST Repository Connector",
             "Supports an OMRS Repository Connector for calling the OMRS Repository REST API in a remote " +
                                       "open metadata repository.",
             "https://cwiki.apache.org/confluence/display/ATLAS/OMRS+REST+Repository+Connector"),

    METADATA_HIGHWAY_MANAGER (13,
             "Metadata Highway Manager",
             "Manages the initialization and shutdown of the components that connector to each of the cohorts that the local server is a member of.",
             "https://cwiki.apache.org/confluence/display/ATLAS/OMRS+Metadata+Highway+Manager"),

    COHORT_MANAGER  (14,
             "Cohort Manager",
             "Manages the initialization and shutdown of the server's connectivity to a cohort.",
             "https://cwiki.apache.org/confluence/display/ATLAS/OMRS+Cohort+Manager"),

    COHORT_REGISTRY(15,
             "Cohort Registry",
             "Manages the registration requests send and received from this local repository.",
             "https://cwiki.apache.org/confluence/display/ATLAS/OMRS+Cohort+Registry"),

    REGISTRY_STORE  (16,
             "Registry Store",
             "Stores information about the repositories registered in the open metadata repository cohort.",
             "https://cwiki.apache.org/confluence/display/ATLAS/OMRS+Cohort+Registry+Store"),

    EVENT_PUBLISHER (17,
             "Event Publisher",
             "Manages the publishing of events that this repository sends to the OMRS topic.",
             "https://cwiki.apache.org/confluence/display/ATLAS/OMRS+Event+Publisher"),

    EVENT_LISTENER  (18,
             "Event Listener",
             "Manages the receipt of incoming OMRS events.",
              "https://cwiki.apache.org/confluence/display/ATLAS/OMRS+Event+Listener"),

    OMRS_TOPIC_CONNECTOR(19,
             "OMRS Topic Connector",
             "Provides access to the OMRS Topic that is used to exchange events between members of a cohort, " +
                                 "or to notify Open Metadata Access Services (OMASs) of changes to " +
                                 "metadata in the enterprise.",
             "https://cwiki.apache.org/confluence/display/ATLAS/OMRS+Topic+Connector"),

    OPEN_METADATA_TOPIC_CONNECTOR(20,
             "Open Metadata Topic Connector",
             "Provides access to an event bus to exchange events with participants in the open metadata ecosystem.",
             "https://cwiki.apache.org/confluence/display/ATLAS/OMRS+Topic+Connector"),

    LOCAL_REPOSITORY_EVENT_MAPPER(21,
             "Local Repository Event Mapper Connector",
             "Provides access to an event bus to process events from a specific local repository.",
             "https://cwiki.apache.org/confluence/display/ATLAS/OMRS+Repository+Event+Mapper")
    ;


    private  int      componentId;
    private  String   componentName;
    private  String   componentDescription;
    private  String   componentWikiURL;


    /**
     * Set up the values of the enum.
     *
     * @param componentId code number for the component.
     * @param componentName name of the component used in the audit log record.
     * @param componentDescription short description of the component.
     * @param componentWikiURL URL link to the description of the component.
     */
    OMRSAuditingComponent(int    componentId,
                          String componentName,
                          String componentDescription,
                          String componentWikiURL)
    {
        this.componentId = componentId;
        this.componentName = componentName;
        this.componentDescription = componentDescription;
        this.componentWikiURL = componentWikiURL;
    }


    /**
     * Return the numerical code for this enum.
     *
     * @return int componentId
     */
    public int getComponentId()
    {
        return componentId;
    }


    /**
     * Return the name of the component.  This is the name used in the audit log records.
     *
     * @return String component name
     */
    public String getComponentName()
    {
        return componentName;
    }


    /**
     * Return the short description of the component. This is an English description.  Natural language support for
     * these values can be added to UIs using a resource bundle indexed with the component Id.  This value is
     * provided as a default if the resource bundle is not available.
     *
     * @return String description
     */
    public String getComponentDescription()
    {
        return componentDescription;
    }

    /**
     * URL link to the wiki page that describes this component.  This provides more information to the log reader
     * on the operation of the component.
     *
     * @return String URL
     */
    public String getComponentWikiURL()
    {
        return componentWikiURL;
    }


    /**
     * toString, JSON-style
     *
     * @return string description
     */
    @Override
    public String toString()
    {
        return "OMRSAuditingComponent{" +
                "componentId=" + componentId +
                ", componentName='" + componentName + '\'' +
                ", componentDescription='" + componentDescription + '\'' +
                ", componentWikiURL='" + componentWikiURL + '\'' +
                '}';
    }
}
