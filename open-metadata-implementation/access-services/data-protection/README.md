<!-- SPDX-License-Identifier: Apache-2.0 -->

# Data Protection Open Metadata Access Service (OMAS)

The Data Protection OMAS provides APIs and events for tools and applications
that are defining the data protection requirements an implementations for
a governance program.

The module structure for the Data Protection OMAS is as follows:

* [data-protection-client](data-protection-client) supports the client library.
* [data-protection-api](data-protection-api) supports the common Java classes that are used both by the client and the server.
* [data-protection-server](data-protection-server) supports in implementation of the access service and its related event management.
* [data-protection-spring](data-protection-spring) supports the REST API using the [Spring](../../../developer-resources/Spring.md) libraries.
