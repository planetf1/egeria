<!-- SPDX-License-Identifier: Apache-2.0 -->

# Information View Open Metadata Access Service (OMAS)

The Information View OMAS configures and manages metadata for data tools that 
create virtual views over data - such as business intelligence tools and
data virtualization platforms.

The module structure for the Information View OMAS is as follows:

* [information-view-client](information-view-client) supports the client library.
* [information-view-api](information-view-api) supports the common Java classes that are used both by the client and the server.
* [information-view-server](information-view-server) supports in implementation of the access service and its related event management.
* [information-view-spring](information-view-spring) supports the REST API using the [Spring](../../../developer-resources/Spring.md) libraries.
