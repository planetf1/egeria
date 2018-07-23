<!-- SPDX-License-Identifier: Apache-2.0 -->

# DevOps Open Metadata Access Service (OMAS)

The DevOps OMAS provides APIs and events for tools that play a role in a
DevOps pipeline.  It enables these tools to query information about the assets it
is deploying, the infrastructure options and any governance actions that need
to be performed.

The module structure for the DevOps OMAS is as follows:

* [dev-ops-client](dev-ops-client) supports the client library.
* [dev-ops-api](dev-ops-api) supports the common Java classes that are used both by the client and the server.
* [dev-ops-server](dev-ops-server) supports in implementation of the access service and its related event management.
* [dev-ops-spring](dev-ops-spring) supports the REST API using the [Spring](../../../developer-resources/Spring.md) libraries.
