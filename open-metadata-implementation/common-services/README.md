<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# OMAG Common Services

This module provides common services to clients and the specialized services that
run in the OMAG Server.

* **[First-failure Data Capture (FFDC) Services](ffdc-services)** - supports common exceptions
and error handling.  It can but used by clients and server-side services.

* **[Multi-Tenant Services](multi-tenant)** - supports the management of
[OMAG Server](../../open-metadata-publication/website/omag-server) instances
running in an [OMAG Server Platform](../../open-metadata-publication/website/omag-server).

* **[Repository Handler](repository-handler)** - supports access to multiple related metadata instances from the
[Open Metadata Repository Services (OMRS)](../repository-services).

In addition, there are shared metadata management functions for
server-side services that make use of the beans defined in the various [frameworks](../frameworks) that underpin ODPi Egeria.
These include:

* [OCF Metadata Management](ocf-metadata-management) - managing metadata about assets, connections and all of the
different types of metadata defined in the asset properties.
* [ODF Metadata Management](odf-metadata-management) - managing metadata about discovery services.
* [GAF Metadata Management](gaf-metadata-management) - managing metadata about governance requirements and actions.

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.