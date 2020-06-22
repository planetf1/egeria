<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->
  
# Open Metadata View Services (OMVS)

![In Development](../../../open-metadata-publication/website/images/egeria-content-status-in-development.png)

Develop an explorer interface to support the Repository Explorer UI, which enables a technical user (such as an Enterprise Architect) to 
retrieve metadata instances from repositories and explore the connectivity of those instances to other metadata objects. The interface
enables retrieval and search and the construction of a visualization of a graph of connected objects. 

This OMVS calls a remote server using the [repository services client](../../repository-services/repository-services-client/README.md).


The module structure for the Repository Explorer OMVS is as follows:

* [rex-view-api](rex-view-api) defines the interface to the view service.
* [rex-view-server](rex-view-server) supports an implementation of the view service.
* [rex-view-spring](rex-view-spring) supports the REST API using the [Spring](../../../developer-resources/Spring.md) libraries.


Return to [open-metadata-implementation](../..).

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.