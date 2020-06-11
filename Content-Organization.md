<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->


## ODPi Egeria Site organization
  
The Egeria content is organized into the following modules:

* **[developer-resources](developer-resources)** - contains useful files and documentation for an Egeria developer.
* **[open-metadata-conformance-suite](open-metadata-conformance-suite)** - implementation of the tests that determine if a vendor or open source technology is compliant with the open metadata and governance standards.
* **[open-metadata-distribution](open-metadata-distribution)** - contains scripts to extract the completed artifacts from the other modules and stores them together to make it easy to find them.
* **[open-metadata-implementation](open-metadata-implementation)** - implementation of standards, frameworks and connectors.
  * **[access-services](open-metadata-implementation/access-services)** - domain specific APIs known as the Open Metadata Access Services (OMAS).
    * **[asset-catalog](open-metadata-implementation/access-services/asset-catalog)** - search for assets.
    * **[asset-consumer](open-metadata-implementation/access-services/asset-consumer)** - create connectors to access assets.
    * **[asset-lineage](open-metadata-implementation/access-services/asset-lineage)** - provide lineage reporting.
    * **[asset-owner](open-metadata-implementation/access-services/asset-owner)** - manage metadata and feedback for owned assets.
    * **[community-profile](open-metadata-implementation/access-services/community-profile)** - manage personal profiles and communities.
    * **[data-engine](open-metadata-implementation/access-services/data-engine)** - exchange metadata with a data processing engine.
    * **[data-manager](open-metadata-implementation/access-services/data-manager)** - exchange metadata with a technology that manages collections of data.
    * **[data-privacy](open-metadata-implementation/access-services/data-privacy)** - support a data privacy officer.
    * **[data-science](open-metadata-implementation/access-services/data-science)** - manage metadata for analytics.
    * **[design-model](open-metadata-implementation/access-services/design-model)** - manage content from design models.
    * **[dev-ops](open-metadata-implementation/access-services/dev-ops)** - manage metadata for a devOps pipeline.
    * **[digital-architecture](open-metadata-implementation/access-services/digital-architecture)** - support the definition of data standards and models.
    * **[digital-service](open-metadata-implementation/access-services/digital-service)** - manage metadata for a Digital Service.
    * **[discovery-engine](open-metadata-implementation/access-services/discovery-engine)** - manage metadata for metadata discovery services.
    * **[glossary-view](open-metadata-implementation/access-services/glossary-view)** - search for glossary content.
    * **[governance-engine](open-metadata-implementation/access-services/governance-engine)** - manage metadata for an operational governance engine.
    * **[governance-program](open-metadata-implementation/access-services/governance-program)** - set up and manage a governance program.
    * **[information-view](open-metadata-implementation/access-services/information-view)** - configure and manage metadata for data tools that create virtual views over data - such as business intelligence tools and data virtualization platforms.
    * **[it-infrastructure](open-metadata-implementation/access-services/it-infrastructure)** - manage metadata about deployed infrastructure.
    * **[project-management](open-metadata-implementation/access-services/project-management)** - manage definitions of projects for metadata management and governance.
    * **[security-officer](open-metadata-implementation/access-services/security-officer)** - set up rules to protect data.
    * **[software-developer](open-metadata-implementation/access-services/software-developer)** - deliver useful metadata to software developers.
    * **[stewardship-action](open-metadata-implementation/access-services/stewardship-action)** - manage metadata as part of a data steward's work to improve the data landscape.
    * **[subject-area](open-metadata-implementation/access-services/subject-area)** - develop a definition of a subject area including glossary terms, reference data and rules.
  * **[adapters](open-metadata-implementation/adapters)** - pluggable component implementations.
    * **[authentication-plugins](open-metadata-implementation/adapters/authentication-plugins)** support extensions to technology such as LDAP that are used to verify the identity of an individual or service requesting access to data/metadata.
    * **[governance-engines-plugins](open-metadata-implementation/adapters/governance-engines-plugins)** support plugins to governance engines to enable them to use open metadata settings in their validation and enforcement decisions, and the resulting actions they take.
    * **[open-connectors](open-metadata-implementation/adapters/open-connectors)** are connectors that support the Open Connector Framework (OCF).
      * **[configuration-store-connectors](open-metadata-implementation/adapters/open-connectors/configuration-store-connectors)** contains the connectors that manage the open metadata configuration.
      * **[connector-configuration-factory](open-metadata-implementation/adapters/open-connectors/connector-configuration-factory)** creates **Connection** objects to configure the open connectors.
      * **[data-store-connectors](open-metadata-implementation/adapters/open-connectors/data-store-connectors)** contain OCF connectors to data stores on different data platforms.
      * **[discovery-service-connectors](open-metadata-implementation/adapters/open-connectors/discovery-service-connectors)** contain ODF discovery service implementations.
      * **[event-bus-connectors](open-metadata-implementation/adapters/open-connectors/event-bus-connectors)** supports different event/messaging infrastructures.  They can be plugged into the topic connectors from the access-service-connectors and repository-service-connectors.
      * **[governance-action-connectors](open-metadata-implementation/adapters/open-connectors/governance-action-connectors)** contains GAF governance action implementations.
      * **[governance-daemon-connectors](open-metadata-implementation/adapters/open-connectors/governance-daemon-connectors)** contains connectors for the governance daemon servers that monitor activity or synchronize metadata and configuration asynchronously between different tools.
      * **[repository-services-connectors](open-metadata-implementation/adapters/open-connectors/repository-services-connectors)** contains connector implementations for each type of connector supported by the Open Metadata Repository Services (OMRS).
        * **[audit-log-connectors](open-metadata-implementation/adapters/open-connectors/repository-services-connectors/audit-log-connectors)** supports different destinations for audit log messages.
        * **[cohort-registry-store-connectors](open-metadata-implementation/adapters/open-connectors/repository-services-connectors/cohort-registry-store-connectors)** contains connectors that store the cohort membership details used and maintained by the cohort registry.
        * **[open-metadata-archive-connectors](open-metadata-implementation/adapters/open-connectors/repository-services-connectors/open-metadata-archive-connectors)** contains connectors that can read and write open metadata archives.
        * **[open-metadata-collection-store-connectors](open-metadata-implementation/adapters/open-connectors/repository-services-connectors/open-metadata-collection-store-connectors)** contains connectors that support mappings to different vendors' metadata repositories.
          * **[graph-repository-connector](open-metadata-implementation/adapters/open-connectors/repository-services-connectors/open-metadata-collection-store-connectors/graph-repository-connector)** - provides a local repository that uses a graph store as its persistence store.
          * **[inmemory-repository-connector](open-metadata-implementation/adapters/open-connectors/repository-services-connectors/open-metadata-collection-store-connectors/inmemory-repository-connector)** - provides a local repository that is entirely in memory.  It is useful for testing/developing OMASs and demos.
          * **[omrs-rest-repository-connector](open-metadata-implementation/adapters/open-connectors/repository-services-connectors/open-metadata-collection-store-connectors/omrs-rest-repository-connector)** - enables IBM Information Governance Catalog to support open metadata.
      * **[rest-client-connectors](open-metadata-implementation/adapters/open-connectors/rest-client-connectors)** contains connector implementations for issuing REST calls.
  * **[admin-services](open-metadata-implementation/admin-services)** - supports the configuration of the OMAG Server Platform.  This configuration determines which of the open metadata and governance services are active.
  * **[common-services](open-metadata-implementation/common-services)** - support modules that are reused by other services.
    * **[ffdc-services](open-metadata-implementation/common-services/ffdc-services)** - provides base classes and validation for First Failure Data Capture (FFDC).
    * **[gaf-metadata-management](open-metadata-implementation/common-services/gaf-metadata-management)** - provides metadata management for the [Governance Action Framework (GAF)](open-metadata-implementation/frameworks/governance-action-framework) properties and APIs.
    * **[metadata-security](open-metadata-implementation/common-services/metadata-security)** - provides integration points for fine-grained security for metadata.
    * **[multi-tenant](open-metadata-implementation/common-services/multi-tenant)** - provides management of server instances within the OMAG Server Platform.
    * **[ocf-metadata-management](open-metadata-implementation/common-services/ocf-metadata-management)** - provides metadata management for the [Open Connector Framework (OCF)](open-metadata-implementation/frameworks/open-connector-framework) properties and APIs.
    * **[odf-metadata-management](open-metadata-implementation/common-services/odf-metadata-management)** - provides metadata management for the [Open Discovery Framework (ODF)](open-metadata-implementation/frameworks/open-discovery-framework) properties and APIs.
    * **[repository-handler](open-metadata-implementation/common-services/repository-handler)** - provides an enhanced set of services for accessing metadata from the [repository services](open-metadata-implementation/repository-services).
  * **[frameworks](open-metadata-implementation/frameworks)** - frameworks that support pluggable components.
    * **[audit-log-framework](open-metadata-implementation/frameworks/audit-log-framework)** provides the interfaces and base implementations for components (called connectors) that access data-related assets. OCF connectors also provide detailed metadata about the assets they access.
    * **[open-connector-framework](open-metadata-implementation/frameworks/open-connector-framework)** provides the interfaces for diagnostics and exceptions.
    * **[open-discovery-framework](open-metadata-implementation/frameworks/open-discovery-framework)** provides the interfaces and base implementations for components (called discovery services) that access data-related assets and extract characteristics about the data that can be stored in an open metadata repository.
    * **[governance-action-framework](open-metadata-implementation/frameworks/governance-action-framework)** provides the interfaces and base implementations for components (called governance actions) that take action to correct a situation that is harmful the data, or the organization in some way.
  * **[governance-servers](open-metadata-implementation/governance-servers)** - servers and daemons to run open metadata and governance function.
    * **[data-engine-proxy-services](open-metadata-implementation/governance-servers/data-engine-proxy-services)** - supports automated metadata cataloguing from data engines.
    * **[data-platform-services](open-metadata-implementation/governance-servers/data-platform-services)** - supports automated metadata cataloguing by data platforms.
    * **[discovery-engine-services](open-metadata-implementation/governance-servers/discovery-engine-services)** - supports automated metadata discovery.
    * **[open-lineage-services](open-metadata-implementation/governance-servers/open-lineage-services)** - provides historic warehouse for lineage.
    * **[security-sync-services](open-metadata-implementation/governance-servers/security-sync-services)** - supports automated configuration of security engines.
    * **[stewardship-engine-services](open-metadata-implementation/governance-servers/stewardship-engine-services)** - supports automated stewardship actions.
    * **[virtualization-services](open-metadata-implementation/governance-servers/virtualization-services)** - supports automated stewardship actions.
  * **[platform-services](open-metadata-implementation/platform-services)** - the platform services support REST APIs for the OMAG Server Platform.
  * **[repository-services](open-metadata-implementation/repository-services)** - metadata exchange and federation - aka the Open Metadata Repository Services (OMRS).
  * **[server-chassis](open-metadata-implementation/server-chassis)** - the server chassis provides the server framework for the OMAG Server Platform.
  * **[user-interfaces](open-metadata-implementation/user-interfaces)** - browser based user interfaces.
* **[open-metadata-publication](open-metadata-publication)** - contains scripts that send artifacts collected together by the open-metadata-distribution module to external parties.
* **[open-metadata-resources](open-metadata-resources)** - contains the open metadata demos and samples.
  * **[open-metadata-archives](open-metadata-resources/open-metadata-archives)** - contains pre-canned collections of metadata for loading into an open metadata server.
  * **[open-metadata-demos](open-metadata-resources/open-metadata-demos)** - contains resources for running demos.
  * **[open-metadata-deployment](open-metadata-resources/open-metadata-deployment)** - contains resources for deploying ODPi Egeria and related technology.
  * **[open-metadata-labs](open-metadata-resources/open-metadata-labs)** - contains Jupyter Notebooks to guide you through using ODPi Egeria.
  * **[open-metadata-samples](open-metadata-resources/open-metadata-samples)** - contains coding samples for Egeria APIs.
  * **[open-metadata-tutorials](open-metadata-resources/open-metadata-tutorials)** - contains tutorials for managing your own ODPi Egeria environment.
* **[open-metadata-test](open-metadata-test)** - supports additional test cases beyond unit test.


----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.