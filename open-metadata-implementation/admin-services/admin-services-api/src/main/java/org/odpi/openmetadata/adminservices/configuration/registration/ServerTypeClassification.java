/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adminservices.configuration.registration;

/**
 * ServerTypeClassification manages a list of different server types.
 */
public enum ServerTypeClassification
{
    OMAG_SERVER("Open Metadata and Governance (OMAG) Server",
                "Generic name for a server that runs on the OMAG Platform",
                null,
                "https://egeria.odpi.org/open-metadata-implementation/admin-services/docs/concepts/omag-server.html"),
    COHORT_MEMBER("Cohort Member",
                "OMAG Server that is capable of joining one or more open metadata repository cohorts.",
                 ServerTypeClassification.OMAG_SERVER,
                "https://egeria.odpi.org/open-metadata-implementation/admin-services/docs/concepts/cohort-member.html"),
    METADATA_ACCESS_POINT("Metadata Access Point",
                "Server that provides specialist APIs for accessing and storing metadata in" +
                            "open metadata repositories connected through one or more open metadata repository cohorts.",
                 ServerTypeClassification.COHORT_MEMBER,
                "https://egeria.odpi.org/open-metadata-implementation/admin-services/docs/concepts/metadata-access-point.html"),
    METADATA_SERVER("Metadata Server",
                "Server that is a metadata access point with its own native open metadata repository.",
                 ServerTypeClassification.METADATA_ACCESS_POINT,
                "https://egeria.odpi.org/open-metadata-implementation/admin-services/docs/concepts/metadata-server.html"),
    REPOSITORY_PROXY("Repository Proxy",
                "Hosting environment for a repository connector acting as an adapter to a third party metadata server",
                 ServerTypeClassification.COHORT_MEMBER,
                "https://egeria.odpi.org/open-metadata-implementation/admin-services/docs/concepts/repository-proxy.html"),
    CONFORMANCE_SERVER("Conformance Test Server",
                "Server that hosts the conformance test suite",
                 ServerTypeClassification.COHORT_MEMBER,
                "https://egeria.odpi.org/open-metadata-implementation/admin-services/docs/concepts/conformance-test-server.html"),
    GOVERNANCE_SERVER("Governance Server",
                "Server that hosts integration, management or governance function",
                 ServerTypeClassification.OMAG_SERVER,
                "https://egeria.odpi.org/open-metadata-implementation/admin-services/docs/concepts/governance-server-types.html"),
    INTEGRATION_DAEMON("Integration Daemon",
                "Governance server that hosts connectors that are exchanging metadata with third party technology.  " +
                               "These servers typically do not have their own API which is why they are called daemons",
                 ServerTypeClassification.GOVERNANCE_SERVER,
                "https://egeria.odpi.org/open-metadata-implementation/admin-services/docs/concepts/integration-daemon.html"),
    ENGINE_HOST("Governance Engine Host",
                "Governance server that hosts a specific engine that is managing workloads for an aspect of governance",
                 ServerTypeClassification.GOVERNANCE_SERVER,
                "https://egeria.odpi.org/open-metadata-implementation/admin-services/docs/concepts/engine-host.html"),
    DISCOVERY_SERVER("Discovery Server",
                "Server that hosts one of more discovery engines used to analyze asset contents" +
                             "and the associated metadata",
                 ServerTypeClassification.ENGINE_HOST,
                "https://egeria.odpi.org/open-metadata-implementation/admin-services/docs/concepts/discovery-server.html"),
    STEWARDSHIP_SERVER("Stewardship Server",
                "Server that manages the selection and execution of stewardship actions",
                 ServerTypeClassification.ENGINE_HOST,
                "https://egeria.odpi.org/open-metadata-implementation/admin-services/docs/concepts/stewardship-server.html"),
    OPEN_LINEAGE_SERVER("Open Lineage Server",
                "Server that manages a warehouse of lineage information",
                 ServerTypeClassification.GOVERNANCE_SERVER,
                "https://egeria.odpi.org/open-metadata-implementation/admin-services/docs/concepts/open-lineage-server.html"),
    DATA_PLATFORM_SERVER("Data Platform Server",
                "Server that manages the extraction of technical metadata from data platforms " +
                                "and then stores it into a metadata server",
                 ServerTypeClassification.INTEGRATION_DAEMON,
                "https://egeria.odpi.org/open-metadata-implementation/admin-services/docs/concepts/data-platform-server.html"),
    DATA_ENGINE_PROXY("Data Engine Proxy",
                "Server that manages the extraction of metadata from a single data engine",
                 ServerTypeClassification.INTEGRATION_DAEMON,
                "https://egeria.odpi.org/open-metadata-implementation/admin-services/docs/concepts/data-engine-proxy.html"),
    SECURITY_SYNC_SERVER("Security Sync Server",
                "Server that manages the configuration for a security service using metadata settings",
                 ServerTypeClassification.INTEGRATION_DAEMON,
                "https://egeria.odpi.org/open-metadata-implementation/admin-services/docs/concepts/security-sync-server.html"),
    SECURITY_OFFICER_SERVER("Security Officer Server",
                "Server that manages complex security settings for assets",
                 ServerTypeClassification.GOVERNANCE_SERVER,
                "https://egeria.odpi.org/open-metadata-implementation/admin-services/docs/concepts/security-officer-server.html"),
    VIRTUALIZER_SERVER("Virtualizer Server",
                "Server that manages the configuration for a data virtualization platform",
                 ServerTypeClassification.INTEGRATION_DAEMON,
                "https://egeria.odpi.org/open-metadata-implementation/admin-services/docs/concepts/virtualizer.html"),
    ;

    private String                   serverTypeName;
    private String                   serverTypeDescription;
    private ServerTypeClassification superType;
    private String                   serverTypeWiki;



    ServerTypeClassification(String                     serverTypeName,
                             String                     serverTypeDescription,
                             ServerTypeClassification   superType,
                             String                     serverTypeWiki)
    {
        this.serverTypeName        = serverTypeName;
        this.serverTypeDescription = serverTypeDescription;
        this.superType             = superType;
        this.serverTypeWiki        = serverTypeWiki;
    }


    /**
     * Returns the unique identifier for the error message.
     *
     * @return logMessageId
     */
    public String getServerTypeName()
    {
        return serverTypeName;
    }


    /**
     * Returns description of server type
     *
     * @return userAction String
     */
    public String getServerTypeDescription()
    {
        return serverTypeDescription;
    }


    /**
     * Returns super type of server - null for top level.
     *
     * @return systemAction String
     */
    public ServerTypeClassification getSuperType()
    {
        return superType;
    }


    /**
     * Return the link to the page on the Egeria website that describes this server.
     *
     * @return url
     */
    public String getServerTypeWiki()
    {
        return serverTypeWiki;
    }
}
