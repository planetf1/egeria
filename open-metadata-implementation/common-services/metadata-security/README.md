<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Open Metadata Security

Open Metadata Security defines the base classes and interfaces for the
open metadata security connectors that
validate access to specific Open Metadata services and instances
for individual users.

There are two types of connector:

* **Open metadata platform security connector** - secures access to the
platform services that are not specific to an OMAG Server.

* **Open metadata server security connector** - secures access to the specific services of an OMAG server.

The connectors are optional.  If they are not defined then there are no additional authorization checks
performed inside the OMAG Server Platform.

This module provides the interfaces, connector implementation and plug points that sit in the
server.

## Open metadata platform security connector interface

The connector that plugs in to the platform implements the following interface.

*  **OpenMetadataPlatformSecurity** - provides the interface for a plugin connector that validates whether a calling
   user can access any service on an OMAG Server Platform.  It is called within the context of a specific
   OMAG Server Platform request.
   Each OMAG Server Platform can define its own plugin connector implementation and will have its own instance
   of the connector. 
   
   * **validateUserForPlatform** - Check that the calling user is authorized to issue a (any) request to the OMAG Server Platform.
   * **validateUserAsAdminForPlatform** - Check that the calling user is authorized to issue administration requests to the OMAG Server Platform.
   * **validateUserAsOperatorForPlatform** - Check that the calling user is authorized to issue operator requests to the OMAG Server Platform.
   * **validateUserAsInvestigatorForPlatform** - Check that the calling user is authorized to issue operator requests to the OMAG Server Platform.
  
## Open metadata server security connector interface

The connector that can be defined for an OMAG Server implements the
following interfaces:

* **OpenMetadataServerSecurity** - provides the root interface for a connector that validates access to Open
 Metadata services and instances for a specific user.  There are other optional interfaces that
 define which actions should be validated.
 
  * **validateUserForServer** - Checks that the calling user is authorized to issue a (any) request to the OMAG Server.
  * **validateUserAsServerAdmin** - Checks that the calling user is authorized to update the configuration for a server.
  * **validateUserAsServerOperator** - Checks that the calling user is authorized to issue operator requests to the OMAG Server.
  * **validateUserAsServerInvestigator** - Checks that the calling user is authorized to issue operator requests to the OMAG Server.

* **OpenMetadataServiceSecurity**  - provides the interface for a plugin connector that validates whether a calling
 user can access a specific metadata service.  It is called within the context of a specific OMAG Server.
 Each OMAG Server can define its own plugin connector implementation and will have its own instance
 of the connector.  However the server name is supplied so a single connector can use it for logging
 error messages and locating the valid user list for the server.
 
  * **validateUserForService** - Checks that the calling user is authorized to issue this request.
  * **validateUserForServiceOperation** - Checks that the calling user is authorized to issue this specific request.
 
* **OpenMetadataRepositorySecurity** - defines security checks for accessing and maintaining open metadata types
 and instances in the local repository.
 
  An instance is an entity or a relationship.  There is also a special method for changing classifications
  added to an entity.
  
  * **validateUserForTypeCreate** - Tests for whether a specific user should have the right to create a typeDef within a repository.
  * **validateUserForTypeRead** - Tests for whether a specific user should have read access to a specific typeDef within a repository.
  * **validateUserForTypeUpdate** - Tests for whether a specific user should have the right to update a typeDef within a repository.
  * **validateUserForTypeDelete** - Tests for whether a specific user should have the right to delete a typeDef within a repository.
  * **validateUserForEntityCreate** - Tests for whether a specific user should have the right to create a instance within a repository.
  * **validateUserForEntityRead** - Tests for whether a specific user should have read access to a specific instance within a repository.
  * **validateUserForEntitySummaryRead** - Tests for whether a specific user should have read access to a specific instance within a repository.
  * **validateUserForEntityProxyRead** - Tests for whether a specific user should have read access to a specific instance within a repository.
  * **validateUserForEntityUpdate** - Tests for whether a specific user should have the right to update a instance within a repository.
  * **validateUserForEntityClassificationUpdate** - Tests for whether a specific user should have the right to update the classification for an entity instance
   within a repository.
  * **validateUserForEntityDelete** - Tests for whether a specific user should have the right to delete a instance within a repository.
  * **validateUserForRelationshipCreate** - Tests for whether a specific user should have the right to create a instance within a repository.
  * **validateUserForRelationshipRead** - Tests for whether a specific user should have read access to a specific instance within a repository.
  * **validateUserForRelationshipUpdate** - Tests for whether a specific user should have the right to update a instance within a repository.
  * **validateUserForRelationshipDelete** - Tests for whether a specific user should have the right to delete a instance within a repository.
 
* **OpenMetadataAssetSecurity** - validates what a user is allowed to do with to Assets.
  The methods are given access to the whole asset to allow a variety of values to be tested.
  
  * **setSupportedZonesForUser** - Provides an opportunity to override the deployed module setting of [**supportedZones**](../../access-services/docs/concepts/governance-zones) for a user specific list.
  * **validateUserForAssetCreate** - Tests for whether a specific user should have the right to create an asset.
  * **validateUserForAssetRead** - Tests for whether a specific user should have read access to a specific asset.
  * **validateUserForAssetDetailUpdate** - Tests for whether a specific user should have the right to update an asset.
   This is used for a general asset update, which may include changes to the
   zones and the ownership.
  * **validateUserForAssetAttachmentUpdate** - Tests for whether a specific user should have the right to update elements attached directly
   to an asset such as schema and connections.
  * **validateUserForAssetFeedback** - Tests for whether a specific user should have the right to attach feedback - such as comments,
   ratings, tags and likes, to the asset.
  * **validateUserForAssetDelete** - Tests for whether a specific user should have the right to delete an asset.
    
* **OpenMetadataConnectionSecurity** - defines the interface of a connector that is validating whether a specific
  user should be given access to a specific Connection object.  This connection information has been retrieved
  from an open metadata repository.  It is used to create a Connector to an Asset.  It may include user
  credentials that could enhance the access to data and function within the Asset that is far above
  the specific user's approval.  This is why this optional check is performed by any open metadata service
  that is returning a Connection object (or a Connector created with the Connection object) to an external party.

  * **validateUserForConnection** - Tests for whether a specific user should have access to a connection.

Return to [**Common Services**](..)

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.