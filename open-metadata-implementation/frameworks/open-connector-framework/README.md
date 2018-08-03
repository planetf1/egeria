<!-- SPDX-License-Identifier: Apache-2.0 -->

# Open Connector Framework (OCF)

The OCF, as the name suggests, is an open framework for supporting connectors.
Connectors support access to remote assets such as data sets, APIs and software components.
OCF Connectors also provide access to metadata about the asset and they may call
the [Governance Action Framework (GAF)](../governance-action-framework) to execute
appropriate governance actions related to the use of these assets
in real-time.

## Terminology

There are a number of key components within the OCF:

* **Connector** -  this is a Java object for accessing an asset and its
related metadata and governance functions.

* **Connection** - this is a Java object containing the properties needed to
create a connector instance.
Connection properties are typically managed as metadata entities in the metadata
repository but they can also be manually populated.
Connections have 2 sub-objects:
  * **ConnectorType** - this is a Java object that describes the type of
  the connector, including the Java implementation class of its connector provider (see below).
  * **Endpoint** - this is the Java object that describes the server endpoint where the asset is accessed from.

* **Connector Broker** - this is a generic factory for all OCF connectors.
See [docs](docs/connector-broker.md) for more information.

* **Connector Provider** - this is a factory for a specific type of connector.
It is used by the Connector Broker.

* **Connected Asset** - this is the asset that the connector is accessing.  It is hosted on a server
and the connector makes the remote calls necessary to retrieve, update, delete the asset itself.
The connector also includes an API to retrieve the metadata properties about the connected asset.

## Open Metadata Type Models

* [Model 0040](../../../open-metadata-publication/website/open-metadata-types/0040-Servers.png) defines the structure of an Endpoint.
* [Model 0201](../../../open-metadata-publication/website/open-metadata-types/0201-Connectors-and-Connections.png) defines the structures for Connections and Connector Types.
* [Model 0205](../../../open-metadata-publication/website/open-metadata-types/0205-Connection-Linkage.png) defines the linkage between the connection and the connected asset.

## Java Implementation

The OCF provides the interface schema and base class implementation for these components.
The Java implementation is located in packages `org.odpi.openmetadata.ocf.*`:

* **org.odpi.openmetadata.ocf** - Java interface and base classes for Connector and Connector Provider
plus the implementation of the Connector Broker.

* **org.odpi.openmetadata.ocf.ffdc** - Implementation of the OCF's error codes and exceptions.

* **org.odpi.openmetadata.ocf.properties** - Implementation of the properties for connections and connected assets.
These are simple POJO objects.

## Related Modules

The [ConnectedAsset OMAS](../../access-services/connected-asset) supports the retrieval
of connection and connected asset properties from the open metadata
repository/repositories.

The [AssetConsumer OMAS](../../access-services/asset-consumer) embeds the OCF to provide
client-side support for connectors.

The [Open Metadata Repository Services (OMRS)](../../repository-services)
makes extensive use of OCF connectors for accessing open metadata repository servers and other resources.
These connectors are collectively called the [OMRS Connectors](../../repository-services/docs/component-descriptions/connectors).
