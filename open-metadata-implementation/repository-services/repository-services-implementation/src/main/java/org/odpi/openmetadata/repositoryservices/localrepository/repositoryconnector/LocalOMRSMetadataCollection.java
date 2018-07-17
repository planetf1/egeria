/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.repositoryservices.localrepository.repositoryconnector;

import org.odpi.openmetadata.repositoryservices.ffdc.OMRSErrorCode;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.OMRSMetadataCollection;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.MatchCriteria;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.SequencingOrder;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryValidator;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryeventmapper.OMRSRepositoryEventProcessor;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.*;
import org.odpi.openmetadata.repositoryservices.localrepository.repositorycontentmanager.OMRSTypeDefManager;

import java.util.List;
import java.util.Date;

/**
 * LocalOMRSMetadataCollection provides a wrapper around the metadata collection for the real local repository.
 * Its role is to manage outbound repository events and audit logging/debug for the real local repository.
 */
public class LocalOMRSMetadataCollection extends OMRSMetadataCollection
{
    private OMRSMetadataCollection       realMetadataCollection;
    private String                       localServerName;
    private String                       localServerType;
    private String                       localOrganizationName;
    private OMRSRepositoryEventProcessor outboundRepositoryEventProcessor;
    private OMRSTypeDefManager           localTypeDefManager;



    /**
     * Constructor used by LocalOMRSRepositoryConnector
     *
     * @param parentConnector connector that this metadata collection supports.  The connector has the information
     *                        to call the metadata repository.
     * @param repositoryName name of the repository used for logging.
     * @param repositoryHelper class used to build type definitions and instances.
     * @param repositoryValidator class used to validate type definitions and instances.
     * @param metadataCollectionId unique Identifier of the metadata collection Id.
     * @param localServerName name of the local server.
     * @param localServerType type of the local server.
     * @param localOrganizationName name of the organization that owns the local server.
     * @param realMetadataCollection metadata collection of the real local connector.
     * @param outboundRepositoryEventProcessor outbound event processor
     *                                         (may be null if a repository event mapper is deployed).
     * @param typeDefManager manager of in-memory cache of type definitions (TypeDefs).
     */
     LocalOMRSMetadataCollection(LocalOMRSRepositoryConnector parentConnector,
                                 String                       repositoryName,
                                 OMRSRepositoryHelper         repositoryHelper,
                                 OMRSRepositoryValidator      repositoryValidator,
                                 String                       metadataCollectionId,
                                 String                       localServerName,
                                 String                       localServerType,
                                 String                       localOrganizationName,
                                 OMRSMetadataCollection       realMetadataCollection,
                                 OMRSRepositoryEventProcessor outboundRepositoryEventProcessor,
                                 OMRSTypeDefManager           typeDefManager)
    {
        /*
         * The super class manages the local metadata collection id.  This is a locally managed value.
         */
        super(parentConnector, repositoryName, metadataCollectionId, repositoryHelper, repositoryValidator);

        /*
         * Save the metadata collection object for the real repository.  This is the metadata collection that
         * does all of the work.  LocalOMRSMetadataCollection is just a wrapper for managing repository events
         * and debug and audit logging.
         */
        if (realMetadataCollection == null)
        {
            final String      actionDescription = "Local OMRS Metadata Collection Constructor";

            OMRSErrorCode errorCode = OMRSErrorCode.NULL_LOCAL_METADATA_COLLECTION;
            String        errorMessage = errorCode.getErrorMessageId()
                                       + errorCode.getFormattedErrorMessage();

            throw new OMRSLogicErrorException(errorCode.getHTTPErrorCode(),
                                              this.getClass().getName(),
                                              actionDescription,
                                              errorMessage,
                                              errorCode.getSystemAction(),
                                              errorCode.getUserAction());
        }
        this.realMetadataCollection = realMetadataCollection;

        /*
         * Save the information needed to send repository events.
         */
        this.localServerName = localServerName;
        this.localServerType = localServerType;
        this.localOrganizationName = localOrganizationName;
        this.outboundRepositoryEventProcessor = outboundRepositoryEventProcessor;
        this.localTypeDefManager = typeDefManager;
    }


    /* ======================================================================
     * Group 1: Confirm the identity of the metadata repository being called.
     */

    /**
     * Returns the identifier of the metadata repository.  This is the identifier used to register the
     * metadata repository with the metadata repository cohort.  It is also the identifier used to
     * identify the home repository of a metadata instance.
     *
     * @return String metadata collection id.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository.
     */
    public String      getMetadataCollectionId() throws RepositoryErrorException
    {
        final String methodName = "getMetadataCollectionId";

        /*
         * Validate parameters
         */
        this.validateRepositoryConnector(methodName);
        parentConnector.validateRepositoryIsActive(methodName);

        /*
         * Perform operation
         */
        return super.metadataCollectionId;
    }


    /* ==============================
     * Group 2: Working with typedefs
     */


    /**
     * Returns the list of different types of metadata organized into two groups.  The first are the
     * attribute type definitions (AttributeTypeDefs).  These provide types for properties in full
     * type definitions.  Full type definitions (TypeDefs) describe types for entities, relationships
     * and classifications.
     *
     * @param userId unique identifier for requesting user.
     * @return TypeDefs Lists of different categories of TypeDefs.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    public TypeDefGallery getAllTypes(String userId) throws RepositoryErrorException,
                                                            UserNotAuthorizedException
    {
        final String                       methodName = "getAllTypes";

        /*
         * Validate parameters
         */
        this.validateRepositoryConnector(methodName);
        parentConnector.validateRepositoryIsActive(methodName);

        repositoryValidator.validateUserId(repositoryName, userId, methodName);

        /*
         * Perform operation
         */
        return realMetadataCollection.getAllTypes(userId);
    }


    /**
     * Returns a list of TypeDefs that have the specified name.  TypeDef names should be unique.  This
     * method allows wildcard character to be included in the name.  These are * (asterisk) for an arbitrary string of
     * characters and ampersand for an arbitrary character.
     *
     * @param userId unique identifier for requesting user.
     * @param name name of the TypeDefs to return (including wildcard characters).
     * @return TypeDefs list.
     * @throws InvalidParameterException the name of the TypeDef is null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    public TypeDefGallery findTypesByName(String      userId,
                                          String      name) throws InvalidParameterException,
                                                                   RepositoryErrorException,
                                                                   UserNotAuthorizedException
    {
        final String   methodName        = "findTypesByName";
        final String   nameParameterName = "name";

        /*
         * Validate parameters
         */
        this.validateRepositoryConnector(methodName);
        parentConnector.validateRepositoryIsActive(methodName);

        repositoryValidator.validateUserId(repositoryName, userId, methodName);
        repositoryValidator.validateTypeName(repositoryName, nameParameterName, name, methodName);

        /*
         * Perform operation
         */
        return realMetadataCollection.findTypesByName(userId, name);
    }


    /**
     * Returns all of the TypeDefs for a specific category.
     *
     * @param userId unique identifier for requesting user.
     * @param category enum value for the category of TypeDef to return.
     * @return TypeDefs list.
     * @throws InvalidParameterException the TypeDefCategory is null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    public List<TypeDef> findTypeDefsByCategory(String          userId,
                                                TypeDefCategory category) throws InvalidParameterException,
                                                                                 RepositoryErrorException,
                                                                                 UserNotAuthorizedException
    {
        final String methodName            = "findTypeDefsByCategory";
        final String categoryParameterName = "category";

        /*
         * Validate parameters
         */
        this.validateRepositoryConnector(methodName);
        parentConnector.validateRepositoryIsActive(methodName);

        repositoryValidator.validateUserId(repositoryName, userId, methodName);
        repositoryValidator.validateTypeDefCategory(repositoryName, categoryParameterName, category, methodName);

        /*
         * Perform operation
         */

        return realMetadataCollection.findTypeDefsByCategory(userId, category);
    }


    /**
     * Returns all of the AttributeTypeDefs for a specific category.
     *
     * @param userId unique identifier for requesting user.
     * @param category enum value for the category of an AttributeTypeDef to return.
     * @return TypeDefs list.
     * @throws InvalidParameterException the TypeDefCategory is null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    public List<AttributeTypeDef> findAttributeTypeDefsByCategory(String                   userId,
                                                                  AttributeTypeDefCategory category) throws InvalidParameterException,
                                                                                                            RepositoryErrorException,
                                                                                                            UserNotAuthorizedException
    {
        final String methodName            = "findAttributeTypeDefsByCategory";
        final String categoryParameterName = "category";

        /*
         * Validate parameters
         */
        this.validateRepositoryConnector(methodName);
        parentConnector.validateRepositoryIsActive(methodName);

        repositoryValidator.validateUserId(repositoryName, userId, methodName);
        repositoryValidator.validateAttributeTypeDefCategory(repositoryName, categoryParameterName, category, methodName);

        /*
         * Perform operation
         */
        return realMetadataCollection.findAttributeTypeDefsByCategory(userId, category);
    }



    /**
     * Return the TypeDefs that have the properties matching the supplied match criteria.
     *
     * @param userId unique identifier for requesting user.
     * @param matchCriteria TypeDefProperties a list of property names.
     * @return TypeDefs list.
     * @throws InvalidParameterException the matchCriteria is null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    public List<TypeDef> findTypeDefsByProperty(String            userId,
                                                TypeDefProperties matchCriteria) throws InvalidParameterException,
                                                                                        RepositoryErrorException,
                                                                                        UserNotAuthorizedException
    {
        final String  methodName                 = "findTypeDefsByProperty";
        final String  matchCriteriaParameterName = "matchCriteria";

        /*
         * Validate parameters
         */
        this.validateRepositoryConnector(methodName);
        parentConnector.validateRepositoryIsActive(methodName);

        repositoryValidator.validateUserId(repositoryName, userId, methodName);
        repositoryValidator.validateMatchCriteria(repositoryName, matchCriteriaParameterName, matchCriteria, methodName);

        /*
         * Perform operation
         */

        return realMetadataCollection.findTypeDefsByProperty(userId, matchCriteria);
    }


    /**
     * Return the types that are linked to the elements from the specified standard.
     *
     * @param userId unique identifier for requesting user.
     * @param standard name of the standard null means any.
     * @param organization name of the organization null means any.
     * @param identifier identifier of the element in the standard null means any.
     * @return TypeDefs list each entry in the list contains a typedef.  This is is a structure
     * describing the TypeDef's category and properties.
     * @throws InvalidParameterException all attributes of the external id are null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    public List<TypeDef> findTypesByExternalID(String    userId,
                                               String    standard,
                                               String    organization,
                                               String    identifier) throws InvalidParameterException,
                                                                            RepositoryErrorException,
                                                                            UserNotAuthorizedException
    {
        final String                       methodName = "findTypesByExternalID";

        /*
         * Validate parameters
         */
        this.validateRepositoryConnector(methodName);
        parentConnector.validateRepositoryIsActive(methodName);

        repositoryValidator.validateUserId(repositoryName, userId, methodName);
        repositoryValidator.validateExternalId(repositoryName, standard, organization, identifier, methodName);

        /*
         * Perform operation
         */

        return realMetadataCollection.findTypesByExternalID(userId, standard, organization, identifier);
    }

    /**
     * Return the TypeDefs that match the search criteria.
     *
     * @param userId unique identifier for requesting user.
     * @param searchCriteria String search criteria.
     * @return TypeDefs list each entry in the list contains a typedef.  This is is a structure
     * describing the TypeDef's category and properties.
     * @throws InvalidParameterException the searchCriteria is null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    public List<TypeDef> searchForTypeDefs(String    userId,
                                           String    searchCriteria) throws InvalidParameterException,
                                                                            RepositoryErrorException,
                                                                            UserNotAuthorizedException
    {
        final String methodName                  = "searchForTypeDefs";
        final String searchCriteriaParameterName = "searchCriteria";

        /*
         * Validate parameters
         */
        this.validateRepositoryConnector(methodName);
        parentConnector.validateRepositoryIsActive(methodName);

        repositoryValidator.validateUserId(repositoryName, userId, methodName);
        repositoryValidator.validateSearchCriteria(repositoryName, searchCriteriaParameterName, searchCriteria, methodName);

        /*
         * Perform operation
         */

        return realMetadataCollection.searchForTypeDefs(userId, searchCriteria);
    }


    /**
     * Return the TypeDef identified by the GUID.
     *
     * @param userId unique identifier for requesting user.
     * @param guid String unique id of the TypeDef
     * @return TypeDef structure describing its category and properties.
     * @throws InvalidParameterException the guid is null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                  the metadata collection is stored.
     * @throws TypeDefNotKnownException The requested TypeDef is not known in the metadata collection.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    public TypeDef getTypeDefByGUID(String    userId,
                                    String    guid) throws InvalidParameterException,
                                                           RepositoryErrorException,
                                                           TypeDefNotKnownException,
                                                           UserNotAuthorizedException
    {
        final String methodName        = "getTypeDefByGUID";
        final String guidParameterName = "guid";

        /*
         * Validate parameters
         */
        this.validateRepositoryConnector(methodName);
        parentConnector.validateRepositoryIsActive(methodName);

        repositoryValidator.validateUserId(repositoryName, userId, methodName);
        repositoryValidator.validateGUID(repositoryName, guidParameterName, guid, methodName);

        /*
         * Perform operation
         */

        return realMetadataCollection.getTypeDefByGUID(userId, guid);
    }


    /**
     * Return the AttributeTypeDef identified by the GUID.
     *
     * @param userId unique identifier for requesting user.
     * @param guid String unique id of the TypeDef
     * @return TypeDef structure describing its category and properties.
     * @throws InvalidParameterException the guid is null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                  the metadata collection is stored.
     * @throws TypeDefNotKnownException The requested TypeDef is not known in the metadata collection.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    public AttributeTypeDef getAttributeTypeDefByGUID(String    userId,
                                                      String    guid) throws InvalidParameterException,
                                                                              RepositoryErrorException,
                                                                              TypeDefNotKnownException,
                                                                              UserNotAuthorizedException
    {
        final String methodName        = "getAttributeTypeDefByGUID";
        final String guidParameterName = "guid";

        /*
         * Validate parameters
         */
        this.validateRepositoryConnector(methodName);
        parentConnector.validateRepositoryIsActive(methodName);

        repositoryValidator.validateUserId(repositoryName, userId, methodName);
        repositoryValidator.validateGUID(repositoryName, guidParameterName, guid, methodName);

        /*
         * Perform operation
         */

        return realMetadataCollection.getAttributeTypeDefByGUID(userId, guid);
    }


    /**
     * Return the TypeDef identified by the unique name.
     *
     * @param userId unique identifier for requesting user.
     * @param name String name of the TypeDef.
     * @return TypeDef structure describing its category and properties.
     * @throws InvalidParameterException the name is null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                  the metadata collection is stored.
     * @throws TypeDefNotKnownException the requested TypeDef is not found in the metadata collection.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    public TypeDef getTypeDefByName(String    userId,
                                    String    name) throws InvalidParameterException,
                                                           RepositoryErrorException,
                                                           TypeDefNotKnownException,
                                                           UserNotAuthorizedException
    {
        final String  methodName = "getTypeDefByName";
        final String  nameParameterName = "name";

        /*
         * Validate parameters
         */
        this.validateRepositoryConnector(methodName);
        parentConnector.validateRepositoryIsActive(methodName);

        repositoryValidator.validateUserId(repositoryName, userId, methodName);
        repositoryValidator.validateTypeName(repositoryName, nameParameterName, name, methodName);

        /*
         * Perform operation
         */

        return realMetadataCollection.getTypeDefByName(userId, name);
    }


    /**
     * Return the AttributeTypeDef identified by the unique name.
     *
     * @param userId unique identifier for requesting user.
     * @param name String name of the TypeDef.
     * @return TypeDef structure describing its category and properties.
     * @throws InvalidParameterException the name is null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                  the metadata collection is stored.
     * @throws TypeDefNotKnownException the requested TypeDef is not found in the metadata collection.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    public  AttributeTypeDef getAttributeTypeDefByName(String    userId,
                                                       String    name) throws InvalidParameterException,
                                                                              RepositoryErrorException,
                                                                              TypeDefNotKnownException,
                                                                              UserNotAuthorizedException
    {
        final String  methodName = "getAttributeTypeDefByName";
        final String  nameParameterName = "name";

        /*
         * Validate parameters
         */
        this.validateRepositoryConnector(methodName);
        parentConnector.validateRepositoryIsActive(methodName);

        repositoryValidator.validateUserId(repositoryName, userId, methodName);
        repositoryValidator.validateTypeName(repositoryName, nameParameterName, name, methodName);

        /*
         * Perform operation
         */

        return realMetadataCollection.getAttributeTypeDefByName(userId, name);
    }


    /**
     * Create a collection of related types.
     *
     * @param userId unique identifier for requesting user.
     * @param newTypes TypeDefGalleryResponse structure describing the new AttributeTypeDefs and TypeDefs.
     * @throws InvalidParameterException the new TypeDef is null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                  the metadata collection is stored.
     * @throws TypeDefNotSupportedException the repository is not able to support this TypeDef.
     * @throws TypeDefKnownException the TypeDef is already stored in the repository.
     * @throws TypeDefConflictException the new TypeDef conflicts with an existing TypeDef.
     * @throws InvalidTypeDefException the new TypeDef has invalid contents.
     * @throws FunctionNotSupportedException the repository does not support this call.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    public  void addTypeDefGallery(String          userId,
                                   TypeDefGallery  newTypes) throws InvalidParameterException,
                                                                    RepositoryErrorException,
                                                                    TypeDefNotSupportedException,
                                                                    TypeDefKnownException,
                                                                    TypeDefConflictException,
                                                                    InvalidTypeDefException,
                                                                    FunctionNotSupportedException,
                                                                    UserNotAuthorizedException
    {
        final String  methodName = "addTypeDefGallery";
        final String  galleryParameterName = "newTypes";

        /*
         * Validate parameters
         */
        this.validateRepositoryConnector(methodName);
        parentConnector.validateRepositoryIsActive(methodName);

        repositoryValidator.validateUserId(repositoryName, userId, methodName);
        repositoryValidator.validateTypeDefGallery(repositoryName, galleryParameterName, newTypes, methodName);

        /*
         * Perform operation: the gallery is passed to the real repository connector one type at a time
         * to ensure the proper management and caching of types.
         */
        List<AttributeTypeDef>   attributeTypeDefs = newTypes.getAttributeTypeDefs();

        if (attributeTypeDefs != null)
        {
            for (AttributeTypeDef   attributeTypeDef : attributeTypeDefs)
            {
                this.addAttributeTypeDef(userId, attributeTypeDef);
            }
        }

        List<TypeDef>   typeDefs = newTypes.getTypeDefs();

        if (typeDefs != null)
        {
            for (TypeDef   typeDef : typeDefs)
            {
                this.addTypeDef(userId, typeDef);
            }
        }
    }


    /**
     * Create a definition of a new TypeDef.
     *
     * @param userId unique identifier for requesting user.
     * @param newTypeDef TypeDef structure describing the new TypeDef.
     * @throws InvalidParameterException the new TypeDef is null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                  the metadata collection is stored.
     * @throws TypeDefNotSupportedException the repository is not able to support this TypeDef.
     * @throws TypeDefKnownException the TypeDef is already stored in the repository.
     * @throws TypeDefConflictException the new TypeDef conflicts with an existing TypeDef.
     * @throws InvalidTypeDefException the new TypeDef has invalid contents.
     * @throws FunctionNotSupportedException the repository does not support this call.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    public void addTypeDef(String    userId,
                           TypeDef   newTypeDef) throws InvalidParameterException,
                                                        RepositoryErrorException,
                                                        TypeDefNotSupportedException,
                                                        TypeDefKnownException,
                                                        TypeDefConflictException,
                                                        InvalidTypeDefException,
                                                        FunctionNotSupportedException,
                                                        UserNotAuthorizedException
    {
        final String  methodName = "addTypeDef";
        final String  typeDefParameterName = "newTypeDef";

        /*
         * Validate parameters
         */
        this.validateRepositoryConnector(methodName);
        parentConnector.validateRepositoryIsActive(methodName);

        repositoryValidator.validateUserId(repositoryName, userId, methodName);
        repositoryValidator.validateTypeDef(repositoryName, typeDefParameterName, newTypeDef, methodName);
        repositoryValidator.validateUnknownTypeDef(repositoryName, typeDefParameterName, newTypeDef, methodName);

        /*
         * Perform operation
         */

        realMetadataCollection.addTypeDef(userId, newTypeDef);

        if (localTypeDefManager != null)
        {
            localTypeDefManager.addTypeDef(repositoryName, newTypeDef);
        }

        if (outboundRepositoryEventProcessor != null)
        {
            outboundRepositoryEventProcessor.processNewTypeDefEvent(repositoryName,
                                                                    metadataCollectionId,
                                                                    localServerName,
                                                                    localServerType,
                                                                    localOrganizationName,
                                                                    newTypeDef);
        }
    }

    /**
     * Create a definition of a new AttributeTypeDef.
     *
     * @param userId unique identifier for requesting user.
     * @param newAttributeTypeDef TypeDef structure describing the new TypeDef.
     * @throws InvalidParameterException the new TypeDef is null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                  the metadata collection is stored.
     * @throws TypeDefNotSupportedException the repository is not able to support this TypeDef.
     * @throws TypeDefKnownException the TypeDef is already stored in the repository.
     * @throws TypeDefConflictException the new TypeDef conflicts with an existing TypeDef.
     * @throws InvalidTypeDefException the new TypeDef has invalid contents.
     * @throws FunctionNotSupportedException the repository does not support this call.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    public  void addAttributeTypeDef(String             userId,
                                     AttributeTypeDef   newAttributeTypeDef) throws InvalidParameterException,
                                                                                    RepositoryErrorException,
                                                                                    TypeDefNotSupportedException,
                                                                                    TypeDefKnownException,
                                                                                    TypeDefConflictException,
                                                                                    InvalidTypeDefException,
                                                                                    FunctionNotSupportedException,
                                                                                    UserNotAuthorizedException
    {
        final String  methodName           = "addAttributeTypeDef";
        final String  typeDefParameterName = "newAttributeTypeDef";

        /*
         * Validate parameters
         */
        this.validateRepositoryConnector(methodName);
        parentConnector.validateRepositoryIsActive(methodName);

        repositoryValidator.validateUserId(repositoryName, userId, methodName);
        repositoryValidator.validateAttributeTypeDef(repositoryName, typeDefParameterName, newAttributeTypeDef, methodName);
        repositoryValidator.validateUnknownAttributeTypeDef(repositoryName, typeDefParameterName, newAttributeTypeDef, methodName);

        /*
         * Perform operation
         */

        realMetadataCollection.addAttributeTypeDef(userId, newAttributeTypeDef);

        if (localTypeDefManager != null)
        {
            localTypeDefManager.addAttributeTypeDef(repositoryName, newAttributeTypeDef);
        }

        if (outboundRepositoryEventProcessor != null)
        {
            outboundRepositoryEventProcessor.processNewAttributeTypeDefEvent(repositoryName,
                                                                             metadataCollectionId,
                                                                             localServerName,
                                                                             localServerType,
                                                                             localOrganizationName,
                                                                             newAttributeTypeDef);
        }
    }


    /**
     * Verify that a definition of a TypeDef is either new or matches the definition already stored.
     *
     * @param userId unique identifier for requesting user.
     * @param typeDef TypeDef structure describing the TypeDef to test.
     * @return boolean true means the TypeDef matches the local definition false means the TypeDef is not known.
     * @throws InvalidParameterException the TypeDef is null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                  the metadata collection is stored.
     * @throws TypeDefNotSupportedException the repository is not able to support this TypeDef.
     * @throws TypeDefConflictException the new TypeDef conflicts with an existing TypeDef.
     * @throws InvalidTypeDefException the new TypeDef has invalid contents.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    public boolean verifyTypeDef(String    userId,
                                 TypeDef   typeDef) throws InvalidParameterException,
                                                           RepositoryErrorException,
                                                           TypeDefNotSupportedException,
                                                           TypeDefConflictException,
                                                           InvalidTypeDefException,
                                                           UserNotAuthorizedException
    {
        final String  methodName           = "verifyTypeDef";
        final String  typeDefParameterName = "typeDef";

        /*
         * Validate parameters
         */
        this.validateRepositoryConnector(methodName);
        parentConnector.validateRepositoryIsActive(methodName);

        repositoryValidator.validateUserId(repositoryName, userId, methodName);
        repositoryValidator.validateTypeDef(repositoryName, typeDefParameterName, typeDef, methodName);

        /*
         * Perform operation
         */

        return realMetadataCollection.verifyTypeDef(userId, typeDef);
    }


    /**
     * Verify that a definition of an AttributeTypeDef is either new or matches the definition already stored.
     *
     * @param userId unique identifier for requesting user.
     * @param attributeTypeDef TypeDef structure describing the TypeDef to test.
     * @return boolean true means the TypeDef matches the local definition false means the TypeDef is not known.
     * @throws InvalidParameterException the TypeDef is null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                  the metadata collection is stored.
     * @throws TypeDefNotSupportedException the repository is not able to support this TypeDef.
     * @throws TypeDefConflictException the new TypeDef conflicts with an existing TypeDef.
     * @throws InvalidTypeDefException the new TypeDef has invalid contents.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    public  boolean verifyAttributeTypeDef(String            userId,
                                           AttributeTypeDef  attributeTypeDef) throws InvalidParameterException,
                                                                                      RepositoryErrorException,
                                                                                      TypeDefNotSupportedException,
                                                                                      TypeDefConflictException,
                                                                                      InvalidTypeDefException,
                                                                                      UserNotAuthorizedException
    {
        final String  methodName           = "verifyAttributeTypeDef";
        final String  typeDefParameterName = "attributeTypeDef";

        /*
         * Validate parameters
         */
        this.validateRepositoryConnector(methodName);
        parentConnector.validateRepositoryIsActive(methodName);

        repositoryValidator.validateUserId(repositoryName, userId, methodName);
        repositoryValidator.validateAttributeTypeDef(repositoryName, typeDefParameterName, attributeTypeDef, methodName);

        /*
         * Perform operation
         */

        return realMetadataCollection.verifyAttributeTypeDef(userId, attributeTypeDef);
    }


    /**
     * Update one or more properties of the TypeDef.  The TypeDefPatch controls what types of updates
     * are safe to make to the TypeDef.
     *
     * @param userId unique identifier for requesting user.
     * @param typeDefPatch TypeDef patch describing change to TypeDef.
     * @return updated TypeDef
     * @throws InvalidParameterException the TypeDefPatch is null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                    the metadata collection is stored.
     * @throws TypeDefNotKnownException the requested TypeDef is not found in the metadata collection.
     * @throws PatchErrorException the TypeDef can not be updated because the supplied patch is incompatible
     *                               with the stored TypeDef.
     * @throws FunctionNotSupportedException the repository does not support this call.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    public TypeDef updateTypeDef(String       userId,
                                 TypeDefPatch typeDefPatch) throws InvalidParameterException,
                                                                   RepositoryErrorException,
                                                                   TypeDefNotKnownException,
                                                                   PatchErrorException,
                                                                   FunctionNotSupportedException,
                                                                   UserNotAuthorizedException
    {
        final String  methodName           = "updateTypeDef";

        /*
         * Validate parameters
         */
        this.validateRepositoryConnector(methodName);
        parentConnector.validateRepositoryIsActive(methodName);

        repositoryValidator.validateUserId(repositoryName, userId, methodName);
        repositoryValidator.validateTypeDefPatch(repositoryName, typeDefPatch, methodName);

        /*
         * Perform operation
         */

        TypeDef   updatedTypeDef = realMetadataCollection.updateTypeDef(userId, typeDefPatch);

        if (localTypeDefManager != null)
        {
            localTypeDefManager.updateTypeDef(repositoryName, updatedTypeDef);
        }

        if (outboundRepositoryEventProcessor != null)
        {
            outboundRepositoryEventProcessor.processUpdatedTypeDefEvent(repositoryName,
                                                                        metadataCollectionId,
                                                                        localServerName,
                                                                        localServerType,
                                                                        localOrganizationName,
                                                                        typeDefPatch);
        }

        return updatedTypeDef;
    }


    /**
     * Delete the TypeDef.  This is only possible if the TypeDef has never been used to create instances or any
     * instances of this TypeDef have been purged from the metadata collection.
     *
     * @param userId unique identifier for requesting user.
     * @param obsoleteTypeDefGUID String unique identifier for the TypeDef.
     * @param obsoleteTypeDefName String unique name for the TypeDef.
     * @throws InvalidParameterException the one of TypeDef identifiers is null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                    the metadata collection is stored.
     * @throws TypeDefNotKnownException the requested TypeDef is not found in the metadata collection.
     * @throws TypeDefInUseException the TypeDef can not be deleted because there are instances of this type in the
     *                                 the metadata collection.  These instances need to be purged before the
     *                                 TypeDef can be deleted.
     * @throws FunctionNotSupportedException the repository does not support this call.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    public void deleteTypeDef(String    userId,
                              String    obsoleteTypeDefGUID,
                              String    obsoleteTypeDefName) throws InvalidParameterException,
                                                                    RepositoryErrorException,
                                                                    TypeDefNotKnownException,
                                                                    TypeDefInUseException,
                                                                    FunctionNotSupportedException,
                                                                    UserNotAuthorizedException
    {
        final String    methodName        = "deleteTypeDef";
        final String    guidParameterName = "obsoleteTypeDefGUID";
        final String    nameParameterName = "obsoleteTypeDefName";

        /*
         * Validate parameters
         */
        this.validateRepositoryConnector(methodName);
        parentConnector.validateRepositoryIsActive(methodName);

        repositoryValidator.validateUserId(repositoryName, userId, methodName);
        repositoryValidator.validateTypeDefIds(repositoryName,
                                               guidParameterName,
                                               nameParameterName,
                                               obsoleteTypeDefGUID,
                                               obsoleteTypeDefName,
                                               methodName);

        /*
         * Perform operation
         */

        realMetadataCollection.deleteTypeDef(userId,
                                             obsoleteTypeDefGUID,
                                             obsoleteTypeDefName);

        if (localTypeDefManager != null)
        {
            localTypeDefManager.deleteTypeDef(repositoryName,
                                              obsoleteTypeDefGUID,
                                              obsoleteTypeDefName);
        }

        if (outboundRepositoryEventProcessor != null)
        {
            outboundRepositoryEventProcessor.processDeletedTypeDefEvent(repositoryName,
                                                                        metadataCollectionId,
                                                                        localServerName,
                                                                        localServerType,
                                                                        localOrganizationName,
                                                                        obsoleteTypeDefGUID,
                                                                        obsoleteTypeDefName);
        }
    }


    /**
     * Delete an AttributeTypeDef.  This is only possible if the AttributeTypeDef has never been used to create
     * instances or any instances of this AttributeTypeDef have been purged from the metadata collection.
     *
     * @param userId unique identifier for requesting user.
     * @param obsoleteTypeDefGUID String unique identifier for the AttributeTypeDef.
     * @param obsoleteTypeDefName String unique name for the AttributeTypeDef.
     * @throws InvalidParameterException the one of AttributeTypeDef identifiers is null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                    the metadata collection is stored.
     * @throws TypeDefNotKnownException the requested AttributeTypeDef is not found in the metadata collection.
     * @throws TypeDefInUseException the AttributeTypeDef can not be deleted because there are instances of this type in the
     *                                 the metadata collection.  These instances need to be purged before the
     *                                 AttributeTypeDef can be deleted.
     * @throws FunctionNotSupportedException the repository does not support this call.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    public void deleteAttributeTypeDef(String    userId,
                                       String    obsoleteTypeDefGUID,
                                       String    obsoleteTypeDefName) throws InvalidParameterException,
                                                                             RepositoryErrorException,
                                                                             TypeDefNotKnownException,
                                                                             TypeDefInUseException,
                                                                             FunctionNotSupportedException,
                                                                             UserNotAuthorizedException
    {
        final String    methodName        = "deleteAttributeTypeDef";
        final String    guidParameterName = "obsoleteTypeDefGUID";
        final String    nameParameterName = "obsoleteTypeDefName";

        /*
         * Validate parameters
         */
        this.validateRepositoryConnector(methodName);
        parentConnector.validateRepositoryIsActive(methodName);

        repositoryValidator.validateUserId(repositoryName, userId, methodName);
        repositoryValidator.validateAttributeTypeDefIds(repositoryName,
                                                        guidParameterName,
                                                        nameParameterName,
                                                        obsoleteTypeDefGUID,
                                                        obsoleteTypeDefName,
                                                        methodName);

        /*
         * Perform operation
         */

        realMetadataCollection.deleteAttributeTypeDef(userId,
                                                      obsoleteTypeDefGUID,
                                                      obsoleteTypeDefName);

        if (localTypeDefManager != null)
        {
            localTypeDefManager.deleteAttributeTypeDef(repositoryName,
                                                       obsoleteTypeDefGUID,
                                                       obsoleteTypeDefName);
        }

        if (outboundRepositoryEventProcessor != null)
        {
            outboundRepositoryEventProcessor.processDeletedAttributeTypeDefEvent(repositoryName,
                                                                                 metadataCollectionId,
                                                                                 localServerName,
                                                                                 localServerType,
                                                                                 localOrganizationName,
                                                                                 obsoleteTypeDefGUID,
                                                                                 obsoleteTypeDefName);
        }
    }


    /**
     * Change the guid or name of an existing TypeDef to a new value.  This is used if two different
     * TypeDefs are discovered to have the same guid.  This is extremely unlikely but not impossible so
     * the open metadata protocol has provision for this.
     *
     * @param userId unique identifier for requesting user.
     * @param originalTypeDefGUID the original guid of the TypeDef.
     * @param originalTypeDefName the original name of the TypeDef.
     * @param newTypeDefGUID the new identifier for the TypeDef.
     * @param newTypeDefName new name for this TypeDef.
     * @return typeDef new values for this TypeDef, including the new guid/name.
     * @throws InvalidParameterException one of the parameters is invalid or null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                    the metadata collection is stored.
     * @throws TypeDefNotKnownException the TypeDef identified by the original guid/name is not found
     *                                    in the metadata collection.
     * @throws FunctionNotSupportedException the repository does not support this call.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    public  TypeDef reIdentifyTypeDef(String     userId,
                                      String     originalTypeDefGUID,
                                      String     originalTypeDefName,
                                      String     newTypeDefGUID,
                                      String     newTypeDefName) throws InvalidParameterException,
                                                                        RepositoryErrorException,
                                                                        TypeDefNotKnownException,
                                                                        FunctionNotSupportedException,
                                                                        UserNotAuthorizedException
    {
        final String    methodName                = "reIdentifyTypeDef";
        final String    originalGUIDParameterName = "originalTypeDefGUID";
        final String    originalNameParameterName = "originalTypeDefName";
        final String    newGUIDParameterName      = "newTypeDefGUID";
        final String    newNameParameterName      = "newTypeDefName";

        /*
         * Validate parameters
         */
        this.validateRepositoryConnector(methodName);
        parentConnector.validateRepositoryIsActive(methodName);

        repositoryValidator.validateUserId(repositoryName, userId, methodName);
        repositoryValidator.validateTypeDefIds(repositoryName,
                                               originalGUIDParameterName,
                                               originalNameParameterName,
                                               originalTypeDefGUID,
                                               originalTypeDefName,
                                               methodName);
        repositoryValidator.validateTypeDefIds(repositoryName,
                                               newGUIDParameterName,
                                               newNameParameterName,
                                               newTypeDefGUID,
                                               newTypeDefName,
                                               methodName);

        /*
         * Perform operation
         */

        TypeDef   originalTypeDef = realMetadataCollection.getTypeDefByGUID(userId, originalTypeDefGUID);

        TypeDef   newTypeDef = realMetadataCollection.reIdentifyTypeDef(userId,
                                                                        originalTypeDefGUID,
                                                                        originalTypeDefName,
                                                                        newTypeDefGUID,
                                                                        newTypeDefName);

        if (localTypeDefManager != null)
        {
            localTypeDefManager.reIdentifyTypeDef(repositoryName,
                                                  originalTypeDefGUID,
                                                  originalTypeDefName,
                                                  newTypeDef);
        }

        if (outboundRepositoryEventProcessor != null)
        {
            outboundRepositoryEventProcessor.processReIdentifiedTypeDefEvent(repositoryName,
                                                                             metadataCollectionId,
                                                                             localServerName,
                                                                             localServerType,
                                                                             localOrganizationName,
                                                                             originalTypeDef,
                                                                             newTypeDef);
        }

        return newTypeDef;
    }


    /**
     * Change the guid or name of an existing TypeDef to a new value.  This is used if two different
     * TypeDefs are discovered to have the same guid.  This is extremely unlikely but not impossible so
     * the open metadata protocol has provision for this.
     *
     * @param userId unique identifier for requesting user.
     * @param originalAttributeTypeDefGUID the original guid of the AttributeTypeDef.
     * @param originalAttributeTypeDefName the original name of the AttributeTypeDef.
     * @param newAttributeTypeDefGUID the new identifier for the AttributeTypeDef.
     * @param newAttributeTypeDefName new name for this AttributeTypeDef.
     * @return attributeTypeDef new values for this AttributeTypeDef, including the new guid/name.
     * @throws InvalidParameterException one of the parameters is invalid or null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                    the metadata collection is stored.
     * @throws TypeDefNotKnownException the AttributeTypeDef identified by the original guid/name is not
     *                                    found in the metadata collection.
     * @throws FunctionNotSupportedException the repository does not support this call.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    public  AttributeTypeDef reIdentifyAttributeTypeDef(String     userId,
                                                        String     originalAttributeTypeDefGUID,
                                                        String     originalAttributeTypeDefName,
                                                        String     newAttributeTypeDefGUID,
                                                        String     newAttributeTypeDefName) throws InvalidParameterException,
                                                                                                   RepositoryErrorException,
                                                                                                   TypeDefNotKnownException,
                                                                                                   FunctionNotSupportedException,
                                                                                                   UserNotAuthorizedException
    {
        final String    methodName                = "reIdentifyAttributeTypeDef";
        final String    originalGUIDParameterName = "originalAttributeTypeDefGUID";
        final String    originalNameParameterName = "originalAttributeTypeDefName";
        final String    newGUIDParameterName      = "newAttributeTypeDefGUID";
        final String    newNameParameterName      = "newAttributeTypeDefName";

        /*
         * Validate parameters
         */
        this.validateRepositoryConnector(methodName);
        parentConnector.validateRepositoryIsActive(methodName);

        repositoryValidator.validateUserId(repositoryName, userId, methodName);
        repositoryValidator.validateTypeDefIds(repositoryName,
                                               originalGUIDParameterName,
                                               originalNameParameterName,
                                               originalAttributeTypeDefGUID,
                                               originalAttributeTypeDefName,
                                               methodName);
        repositoryValidator.validateTypeDefIds(repositoryName,
                                               newGUIDParameterName,
                                               newNameParameterName,
                                               newAttributeTypeDefGUID,
                                               newAttributeTypeDefName,
                                               methodName);

        /*
         * Perform operation
         */

        AttributeTypeDef   originalAttributeTypeDef = realMetadataCollection.getAttributeTypeDefByGUID(userId, originalAttributeTypeDefGUID);

        AttributeTypeDef   newAttributeTypeDef = realMetadataCollection.reIdentifyAttributeTypeDef(userId,
                                                                                                   originalAttributeTypeDefGUID,
                                                                                                   originalAttributeTypeDefName,
                                                                                                   newAttributeTypeDefGUID,
                                                                                                   newAttributeTypeDefName);

        if (localTypeDefManager != null)
        {
            localTypeDefManager.reIdentifyAttributeTypeDef(repositoryName,
                                                           originalAttributeTypeDefGUID,
                                                           originalAttributeTypeDefName,
                                                           newAttributeTypeDef);
        }

        if (outboundRepositoryEventProcessor != null)
        {
            outboundRepositoryEventProcessor.processReIdentifiedAttributeTypeDefEvent(repositoryName,
                                                                                      metadataCollectionId,
                                                                                      localServerName,
                                                                                      localServerType,
                                                                                      localOrganizationName,
                                                                                      originalAttributeTypeDef,
                                                                                      newAttributeTypeDef);
        }

        return newAttributeTypeDef;
    }



    /* ===================================================
     * Group 3: Locating entity and relationship instances
     */


    /**
     * Returns the entity if the entity is stored in the metadata collection, otherwise null.
     *
     * @param userId unique identifier for requesting user.
     * @param guid String unique identifier for the entity
     * @return the entity details if the entity is found in the metadata collection; otherwise return null
     * @throws InvalidParameterException the guid is null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                  the metadata collection is stored.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    public EntityDetail  isEntityKnown(String    userId,
                                       String    guid) throws InvalidParameterException,
                                                              RepositoryErrorException,
                                                              UserNotAuthorizedException
    {
        final String  methodName = "isEntityKnown";
        final String  guidParameterName = "guid";

        /*
         * Validate parameters
         */
        this.validateRepositoryConnector(methodName);
        parentConnector.validateRepositoryIsActive(methodName);

        repositoryValidator.validateUserId(repositoryName, userId, methodName);
        repositoryValidator.validateGUID(repositoryName, guidParameterName, guid, methodName);

        /*
         * Perform operation
         */

        EntityDetail entity = realMetadataCollection.isEntityKnown(userId, guid);

        if (entity != null)
        {
            /*
             * Ensure the provenance of the entity is correctly set.  A repository may not support the storing of
             * the metadata collection id in the repository (or uses null to mean "local").  When the entity
             * detail is sent out, it must have its home metadata collection id set up.  So LocalOMRSMetadataCollection
             * fixes up the provenance.
             */
            if (entity.getMetadataCollectionId() == null)
            {
                entity.setMetadataCollectionId(metadataCollectionId);
                entity.setInstanceProvenanceType(InstanceProvenanceType.LOCAL_COHORT);
            }
        }

        return entity;
    }


    /**
     * Return the header and classifications for a specific entity.  The returned entity summary may be from
     * a full entity object or an entity proxy.
     *
     * @param userId unique identifier for requesting user.
     * @param guid String unique identifier for the entity
     * @return EntitySummary structure
     * @throws InvalidParameterException the guid is null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                  the metadata collection is stored.
     * @throws EntityNotKnownException the requested entity instance is not known in the metadata collection.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    public EntitySummary getEntitySummary(String    userId,
                                          String    guid) throws InvalidParameterException,
                                                                 RepositoryErrorException,
                                                                 EntityNotKnownException,
                                                                 UserNotAuthorizedException
    {
        final String  methodName        = "getEntitySummary";
        final String  guidParameterName = "guid";

        /*
         * Validate parameters
         */
        this.validateRepositoryConnector(methodName);
        parentConnector.validateRepositoryIsActive(methodName);

        repositoryValidator.validateUserId(repositoryName, userId, methodName);
        repositoryValidator.validateGUID(repositoryName, guidParameterName, guid, methodName);

        /*
         * Perform operation
         */

        EntitySummary entity =  realMetadataCollection.getEntitySummary(userId, guid);

        if (entity != null)
        {
            /*
             * Ensure the provenance of the entity is correctly set.  A repository may not support the storing of
             * the metadata collection id in the repository (or uses null to mean "local").  When the entity
             * detail is sent out, it must have its home metadata collection id set up.  So LocalOMRSMetadataCollection
             * fixes up the provenance.
             */
            if (entity.getMetadataCollectionId() == null)
            {
                entity.setMetadataCollectionId(metadataCollectionId);
                entity.setInstanceProvenanceType(InstanceProvenanceType.LOCAL_COHORT);
            }
        }

        return entity;
    }


    /**
     * Return the header, classifications and properties of a specific entity.
     *
     * @param userId unique identifier for requesting user.
     * @param guid String unique identifier for the entity.
     * @return EntityDetail structure.
     * @throws InvalidParameterException the guid is null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                 the metadata collection is stored.
     * @throws EntityNotKnownException the requested entity instance is not known in the metadata collection.
     * @throws EntityProxyOnlyException the requested entity instance is only a proxy in the metadata collection.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    public EntityDetail getEntityDetail(String    userId,
                                        String    guid) throws InvalidParameterException,
                                                               RepositoryErrorException,
                                                               EntityNotKnownException,
                                                               EntityProxyOnlyException,
                                                               UserNotAuthorizedException
    {
        final String  methodName        = "getEntityDetail";
        final String  guidParameterName = "guid";

        /*
         * Validate parameters
         */
        this.validateRepositoryConnector(methodName);
        parentConnector.validateRepositoryIsActive(methodName);

        repositoryValidator.validateUserId(repositoryName, userId, methodName);
        repositoryValidator.validateGUID(repositoryName, guidParameterName, guid, methodName);

        /*
         * Perform operation
         */

        EntityDetail   entity = realMetadataCollection.getEntityDetail(userId, guid);

        if (entity != null)
        {
            /*
             * Ensure the provenance of the entity is correctly set.  A repository may not support the storing of
             * the metadata collection id in the repository (or uses null to mean "local").  When the entity
             * detail is sent out, it must have its home metadata collection id set up.  So LocalOMRSMetadataCollection
             * fixes up the provenance.
             */
            if (entity.getMetadataCollectionId() == null)
            {
                entity.setMetadataCollectionId(metadataCollectionId);
                entity.setInstanceProvenanceType(InstanceProvenanceType.LOCAL_COHORT);
            }
        }

        return entity;
    }


    /**
     * Return a historical version of an entity.  This includes the header, classifications and properties of the entity.
     *
     * @param userId unique identifier for requesting user.
     * @param guid String unique identifier for the entity.
     * @param asOfTime the time used to determine which version of the entity that is desired.
     * @return EntityDetail structure.
     * @throws InvalidParameterException the guid or date is null or the date is for a future time.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                 the metadata collection is stored.
     * @throws EntityNotKnownException the requested entity instance is not known in the metadata collection
     *                                   at the time requested.
     * @throws EntityProxyOnlyException the requested entity instance is only a proxy in the metadata collection.
     * @throws FunctionNotSupportedException the repository does not support the asOfTime parameter.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    public  EntityDetail getEntityDetail(String    userId,
                                         String    guid,
                                         Date      asOfTime) throws InvalidParameterException,
                                                                    RepositoryErrorException,
                                                                    EntityNotKnownException,
                                                                    EntityProxyOnlyException,
                                                                    FunctionNotSupportedException,
                                                                    UserNotAuthorizedException
    {
        final String  methodName        = "getEntityDetail";
        final String  guidParameterName = "guid";
        final String  asOfTimeParameter = "asOfTime";

        /*
         * Validate parameters
         */
        this.validateRepositoryConnector(methodName);
        parentConnector.validateRepositoryIsActive(methodName);

        repositoryValidator.validateUserId(repositoryName, userId, methodName);
        repositoryValidator.validateGUID(repositoryName, guidParameterName, guid, methodName);
        repositoryValidator.validateAsOfTime(repositoryName, asOfTimeParameter, asOfTime, methodName);

        /*
         * Perform operation
         */

        EntityDetail   entity = realMetadataCollection.getEntityDetail(userId, guid, asOfTime);

        if (entity != null)
        {
            /*
             * Ensure the provenance of the entity is correctly set.  A repository may not support the storing of
             * the metadata collection id in the repository (or uses null to mean "local").  When the entity
             * detail is sent out, it must have its home metadata collection id set up.  So LocalOMRSMetadataCollection
             * fixes up the provenance.
             */
            if (entity.getMetadataCollectionId() == null)
            {
                entity.setMetadataCollectionId(metadataCollectionId);
                entity.setInstanceProvenanceType(InstanceProvenanceType.LOCAL_COHORT);
            }
        }

        return entity;
    }


    /**
     * Return the relationships for a specific entity.
     *
     * @param userId unique identifier for requesting user.
     * @param entityGUID String unique identifier for the entity.
     * @param relationshipTypeGUID String GUID of the the type of relationship required (null for all).
     * @param fromRelationshipElement the starting element number of the relationships to return.
     *                                This is used when retrieving elements
     *                                beyond the first page of results. Zero means start from the first element.
     * @param limitResultsByStatus By default, relationships in all statuses are returned.  However, it is possible
     *                             to specify a list of statuses (eg ACTIVE) to restrict the results to.  Null means all
     *                             status values.
     * @param asOfTime Requests a historical query of the relationships for the entity.  Null means return the
     *                 present values.
     * @param sequencingProperty String name of the property that is to be used to sequence the results.
     *                           Null means do not sequence on a property name (see SequencingOrder).
     * @param sequencingOrder Enum defining how the results should be ordered.
     * @param pageSize -- the maximum number of result classifications that can be returned on this request.  Zero means
     *                 unrestricted return results size.
     * @return Relationships list.  Null means no relationships associated with the entity.
     * @throws InvalidParameterException a parameter is invalid or null.
     * @throws TypeErrorException the type guid passed on the request is not known by the
     *                              metadata collection.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                  the metadata collection is stored.
     * @throws EntityNotKnownException the requested entity instance is not known in the metadata collection.
     * @throws PropertyErrorException the sequencing property is not valid for the attached classifications.
     * @throws PagingErrorException the paging/sequencing parameters are set up incorrectly.
     * @throws FunctionNotSupportedException the repository does not support the asOfTime parameter.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    public List<Relationship> getRelationshipsForEntity(String                     userId,
                                                        String                     entityGUID,
                                                        String                     relationshipTypeGUID,
                                                        int                        fromRelationshipElement,
                                                        List<InstanceStatus>       limitResultsByStatus,
                                                        Date                       asOfTime,
                                                        String                     sequencingProperty,
                                                        SequencingOrder            sequencingOrder,
                                                        int                        pageSize) throws InvalidParameterException,
                                                                                                    TypeErrorException,
                                                                                                    RepositoryErrorException,
                                                                                                    EntityNotKnownException,
                                                                                                    PropertyErrorException,
                                                                                                    PagingErrorException,
                                                                                                    FunctionNotSupportedException,
                                                                                                    UserNotAuthorizedException
    {
        final String  methodName = "getRelationshipsForEntity";
        final String  guidParameterName = "entityGUID";
        final String  asOfTimeParameter = "asOfTime";
        final String  typeGUIDParameter = "relationshipTypeGUID";
        final String  pageSizeParameter = "pageSize";

        /*
         * Validate parameters
         */
        this.validateRepositoryConnector(methodName);
        parentConnector.validateRepositoryIsActive(methodName);

        repositoryValidator.validateUserId(repositoryName, userId, methodName);
        repositoryValidator.validateGUID(repositoryName, guidParameterName, entityGUID, methodName);
        repositoryValidator.validateOptionalTypeGUID(repositoryName, typeGUIDParameter, relationshipTypeGUID, methodName);
        repositoryValidator.validateAsOfTime(repositoryName, asOfTimeParameter, asOfTime, methodName);
        repositoryValidator.validatePageSize(repositoryName, pageSizeParameter, pageSize, methodName);

        /*
         * Perform operation
         */

        return realMetadataCollection.getRelationshipsForEntity(userId,
                                                                entityGUID,
                                                                relationshipTypeGUID,
                                                                fromRelationshipElement,
                                                                limitResultsByStatus,
                                                                asOfTime,
                                                                sequencingProperty,
                                                                sequencingOrder,
                                                                pageSize);
    }


    /**
     * Return a list of entities that match the supplied properties according to the match criteria.  The results
     * can be returned over many pages.
     *
     * @param userId unique identifier for requesting user.
     * @param entityTypeGUID String unique identifier for the entity type of interest (null means any entity type).
     * @param matchProperties List of entity properties to match to (null means match on entityTypeGUID only).
     * @param matchCriteria Enum defining how the properties should be matched to the entities in the repository.
     * @param fromEntityElement the starting element number of the entities to return.
     *                                This is used when retrieving elements
     *                                beyond the first page of results. Zero means start from the first element.
     * @param limitResultsByStatus By default, entities in all statuses are returned.  However, it is possible
     *                             to specify a list of statuses (eg ACTIVE) to restrict the results to.  Null means all
     *                             status values.
     * @param limitResultsByClassification List of classifications that must be present on all returned entities.
     * @param asOfTime Requests a historical query of the entity.  Null means return the present values.
     * @param sequencingProperty String name of the entity property that is to be used to sequence the results.
     *                           Null means do not sequence on a property name (see SequencingOrder).
     * @param sequencingOrder Enum defining how the results should be ordered.
     * @param pageSize the maximum number of result entities that can be returned on this request.  Zero means
     *                 unrestricted return results size.
     * @return a list of entities matching the supplied criteria null means no matching entities in the metadata
     * collection.
     * @throws InvalidParameterException a parameter is invalid or null.
     * @throws TypeErrorException the type guid passed on the request is not known by the
     *                              metadata collection.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                    the metadata collection is stored.
     * @throws PropertyErrorException the properties specified are not valid for any of the requested types of
     *                                  entity.
     * @throws PagingErrorException the paging/sequencing parameters are set up incorrectly.
     * @throws FunctionNotSupportedException the repository does not support the asOfTime parameter.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    public  List<EntityDetail> findEntitiesByProperty(String                    userId,
                                                      String                    entityTypeGUID,
                                                      InstanceProperties        matchProperties,
                                                      MatchCriteria             matchCriteria,
                                                      int                       fromEntityElement,
                                                      List<InstanceStatus>      limitResultsByStatus,
                                                      List<String>              limitResultsByClassification,
                                                      Date                      asOfTime,
                                                      String                    sequencingProperty,
                                                      SequencingOrder           sequencingOrder,
                                                      int                       pageSize) throws InvalidParameterException,
                                                                                                 TypeErrorException,
                                                                                                 RepositoryErrorException,
                                                                                                 PropertyErrorException,
                                                                                                 PagingErrorException,
                                                                                                 FunctionNotSupportedException,
                                                                                                 UserNotAuthorizedException
    {
        final String  methodName                   = "findEntitiesByProperty";
        final String  matchCriteriaParameterName   = "matchCriteria";
        final String  matchPropertiesParameterName = "matchProperties";
        final String  guidParameterName            = "entityTypeGUID";
        final String  asOfTimeParameter            = "asOfTime";
        final String  pageSizeParameter            = "pageSize";

        /*
         * Validate parameters
         */
        this.validateRepositoryConnector(methodName);
        parentConnector.validateRepositoryIsActive(methodName);

        repositoryValidator.validateUserId(repositoryName, userId, methodName);
        repositoryValidator.validateOptionalTypeGUID(repositoryName, guidParameterName, entityTypeGUID, methodName);
        repositoryValidator.validateAsOfTime(repositoryName, asOfTimeParameter, asOfTime, methodName);
        repositoryValidator.validatePageSize(repositoryName, pageSizeParameter, pageSize, methodName);
        repositoryValidator.validateMatchCriteria(repositoryName,
                                                  matchCriteriaParameterName,
                                                  matchPropertiesParameterName,
                                                  matchCriteria,
                                                  matchProperties,
                                                  methodName);

        /*
         * Perform operation
         */

        return realMetadataCollection.findEntitiesByProperty(userId,
                                                             entityTypeGUID,
                                                             matchProperties,
                                                             matchCriteria,
                                                             fromEntityElement,
                                                             limitResultsByStatus,
                                                             limitResultsByClassification,
                                                             asOfTime,
                                                             sequencingProperty,
                                                             sequencingOrder,
                                                             pageSize);
    }


    /**
     * Return a list of entities that have the requested type of classification attached.
     *
     * @param userId unique identifier for requesting user.
     * @param entityTypeGUID unique identifier for the type of entity requested.  Null mans any type of entity.
     * @param classificationName name of the classification a null is not valid.
     * @param matchClassificationProperties list of classification properties used to narrow the search.
     * @param matchCriteria Enum defining how the properties should be matched to the classifications in the repository.
     * @param fromEntityElement the starting element number of the entities to return.
     *                                This is used when retrieving elements
     *                                beyond the first page of results. Zero means start from the first element.
     * @param limitResultsByStatus By default, entities in all statuses are returned.  However, it is possible
     *                             to specify a list of statuses (eg ACTIVE) to restrict the results to.  Null means all
     *                             status values.
     * @param asOfTime Requests a historical query of the entity.  Null means return the present values.
     * @param sequencingProperty String name of the entity property that is to be used to sequence the results.
     *                           Null means do not sequence on a property name (see SequencingOrder).
     * @param sequencingOrder Enum defining how the results should be ordered.
     * @param pageSize the maximum number of result entities that can be returned on this request.  Zero means
     *                 unrestricted return results size.
     * @return a list of entities matching the supplied criteria null means no matching entities in the metadata
     * collection.
     * @throws InvalidParameterException a parameter is invalid or null.
     * @throws TypeErrorException the type guid passed on the request is not known by the
     *                              metadata collection.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                    the metadata collection is stored.
     * @throws ClassificationErrorException the classification request is not known to the metadata collection.
     * @throws PropertyErrorException the properties specified are not valid for the requested type of
     *                                  classification.
     * @throws PagingErrorException the paging/sequencing parameters are set up incorrectly.
     * @throws FunctionNotSupportedException the repository does not support the asOfTime parameter.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    public  List<EntityDetail> findEntitiesByClassification(String                    userId,
                                                            String                    entityTypeGUID,
                                                            String                    classificationName,
                                                            InstanceProperties        matchClassificationProperties,
                                                            MatchCriteria             matchCriteria,
                                                            int                       fromEntityElement,
                                                            List<InstanceStatus>      limitResultsByStatus,
                                                            Date                      asOfTime,
                                                            String                    sequencingProperty,
                                                            SequencingOrder           sequencingOrder,
                                                            int                       pageSize) throws InvalidParameterException,
                                                                                                       TypeErrorException,
                                                                                                       RepositoryErrorException,
                                                                                                       ClassificationErrorException,
                                                                                                       PropertyErrorException,
                                                                                                       PagingErrorException,
                                                                                                       FunctionNotSupportedException,
                                                                                                       UserNotAuthorizedException
    {
        final String  methodName                   = "findEntitiesByClassification";
        final String  classificationParameterName  = "classificationName";
        final String  entityTypeGUIDParameterName  = "entityTypeGUID";

        final String  matchCriteriaParameterName   = "matchCriteria";
        final String  matchPropertiesParameterName = "matchClassificationProperties";
        final String  asOfTimeParameter            = "asOfTime";
        final String  pageSizeParameter            = "pageSize";


        /*
         * Validate parameters
         */
        this.validateRepositoryConnector(methodName);
        parentConnector.validateRepositoryIsActive(methodName);

        repositoryValidator.validateUserId(repositoryName, userId, methodName);
        repositoryValidator.validateOptionalTypeGUID(repositoryName, entityTypeGUIDParameterName, entityTypeGUID, methodName);
        repositoryValidator.validateAsOfTime(repositoryName, asOfTimeParameter, asOfTime, methodName);
        repositoryValidator.validatePageSize(repositoryName, pageSizeParameter, pageSize, methodName);

        /*
         * Validate TypeDef
         */
        if (entityTypeGUID != null)
        {
            TypeDef entityTypeDef = repositoryHelper.getTypeDef(repositoryName,
                                                                entityTypeGUIDParameterName,
                                                                entityTypeGUID,
                                                                methodName);

            repositoryValidator.validateTypeDefForInstance(repositoryName,
                                                           entityTypeGUIDParameterName,
                                                           entityTypeDef,
                                                           methodName);

            repositoryValidator.validateClassification(repositoryName,
                                                       classificationParameterName,
                                                       classificationName,
                                                       entityTypeDef.getName(),
                                                       methodName);
        }

        repositoryValidator.validateMatchCriteria(repositoryName,
                                                  matchCriteriaParameterName,
                                                  matchPropertiesParameterName,
                                                  matchCriteria,
                                                  matchClassificationProperties,
                                                  methodName);

        /*
         * Perform operation
         */

        return realMetadataCollection.findEntitiesByClassification(userId,
                                                                   entityTypeGUID,
                                                                   classificationName,
                                                                   matchClassificationProperties,
                                                                   matchCriteria,
                                                                   fromEntityElement,
                                                                   limitResultsByStatus,
                                                                   asOfTime,
                                                                   sequencingProperty,
                                                                   sequencingOrder,
                                                                   pageSize);
    }


    /**
     * Return a list of entities whose string based property values match the search criteria.  The
     * search criteria may include regex style wild cards.
     *
     * @param userId unique identifier for requesting user.
     * @param entityTypeGUID GUID of the type of entity to search for. Null means all types will
     *                       be searched (could be slow so not recommended).
     * @param searchCriteria String expression contained in any of the property values within the entities
     *                       of the supplied type.
     * @param fromEntityElement the starting element number of the entities to return.
     *                                This is used when retrieving elements
     *                                beyond the first page of results. Zero means start from the first element.
     * @param limitResultsByStatus By default, entities in all statuses are returned.  However, it is possible
     *                             to specify a list of statuses (eg ACTIVE) to restrict the results to.  Null means all
     *                             status values.
     * @param limitResultsByClassification List of classifications that must be present on all returned entities.
     * @param asOfTime Requests a historical query of the entity.  Null means return the present values.
     * @param sequencingProperty String name of the property that is to be used to sequence the results.
     *                           Null means do not sequence on a property name (see SequencingOrder).
     * @param sequencingOrder Enum defining how the results should be ordered.
     * @param pageSize the maximum number of result entities that can be returned on this request.  Zero means
     *                 unrestricted return results size.
     * @return a list of entities matching the supplied criteria null means no matching entities in the metadata
     * collection.
     * @throws InvalidParameterException a parameter is invalid or null.
     * @throws TypeErrorException the type guid passed on the request is not known by the
     *                              metadata collection.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                    the metadata collection is stored.
     * @throws PropertyErrorException the sequencing property specified is not valid for any of the requested types of
     *                                  entity.
     * @throws PagingErrorException the paging/sequencing parameters are set up incorrectly.
     * @throws FunctionNotSupportedException the repository does not support the asOfTime parameter.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    public  List<EntityDetail> findEntitiesByPropertyValue(String                userId,
                                                           String                entityTypeGUID,
                                                           String                searchCriteria,
                                                           int                   fromEntityElement,
                                                           List<InstanceStatus>  limitResultsByStatus,
                                                           List<String>          limitResultsByClassification,
                                                           Date                  asOfTime,
                                                           String                sequencingProperty,
                                                           SequencingOrder       sequencingOrder,
                                                           int                   pageSize) throws InvalidParameterException,
                                                                                                  TypeErrorException,
                                                                                                  RepositoryErrorException,
                                                                                                  PropertyErrorException,
                                                                                                  PagingErrorException,
                                                                                                  FunctionNotSupportedException,
                                                                                                  UserNotAuthorizedException
    {
        final String  methodName = "findEntitiesByPropertyValue";
        final String  searchCriteriaParameterName = "searchCriteria";
        final String  asOfTimeParameter = "asOfTime";
        final String  guidParameterName = "entityTypeGUID";
        final String  pageSizeParameter = "pageSize";

        /*
         * Validate parameters
         */
        this.validateRepositoryConnector(methodName);
        parentConnector.validateRepositoryIsActive(methodName);

        repositoryValidator.validateUserId(repositoryName, userId, methodName);
        repositoryValidator.validateSearchCriteria(repositoryName, searchCriteriaParameterName, searchCriteria, methodName);
        repositoryValidator.validateOptionalTypeGUID(repositoryName, guidParameterName, entityTypeGUID, methodName);
        repositoryValidator.validateAsOfTime(repositoryName, asOfTimeParameter, asOfTime, methodName);
        repositoryValidator.validatePageSize(repositoryName, pageSizeParameter, pageSize, methodName);

        /*
         * Process operation
         */

        return realMetadataCollection.findEntitiesByPropertyValue(userId,
                                                                  entityTypeGUID,
                                                                  searchCriteria,
                                                                  fromEntityElement,
                                                                  limitResultsByStatus,
                                                                  limitResultsByClassification,
                                                                  asOfTime,
                                                                  sequencingProperty,
                                                                  sequencingOrder,
                                                                  pageSize);
    }


    /**
     * Returns a boolean indicating if the relationship is stored in the metadata collection.
     *
     * @param userId unique identifier for requesting user.
     * @param guid String unique identifier for the relationship.
     * @return relationship details if the relationship is found in the metadata collection; otherwise return null.
     * @throws InvalidParameterException the guid is null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                  the metadata collection is stored.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    public Relationship  isRelationshipKnown(String    userId,
                                             String    guid) throws InvalidParameterException,
                                                                    RepositoryErrorException,
                                                                    UserNotAuthorizedException
    {
        final String  methodName = "isRelationshipKnown";
        final String  guidParameterName = "guid";

        /*
         * Validate parameters
         */
        this.validateRepositoryConnector(methodName);
        parentConnector.validateRepositoryIsActive(methodName);

        repositoryValidator.validateUserId(repositoryName, userId, methodName);
        repositoryValidator.validateGUID(repositoryName, guidParameterName, guid, methodName);

        /*
         * Process operation
         */

        return realMetadataCollection.isRelationshipKnown(userId, guid);
    }


    /**
     * Return a requested relationship.
     *
     * @param userId unique identifier for requesting user.
     * @param guid String unique identifier for the relationship.
     * @return a relationship structure.
     * @throws InvalidParameterException the guid is null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                    the metadata collection is stored.
     * @throws RelationshipNotKnownException the metadata collection does not have a relationship with
     *                                         the requested GUID stored.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    public Relationship getRelationship(String    userId,
                                        String    guid) throws InvalidParameterException,
                                                               RepositoryErrorException,
                                                               RelationshipNotKnownException,
                                                               UserNotAuthorizedException
    {
        final String  methodName = "getRelationship";
        final String  guidParameterName = "guid";

        /*
         * Validate parameters
         */
        this.validateRepositoryConnector(methodName);
        parentConnector.validateRepositoryIsActive(methodName);

        repositoryValidator.validateUserId(repositoryName, userId, methodName);
        repositoryValidator.validateGUID(repositoryName, guidParameterName, guid, methodName);

        /*
         * Process operation
         */

        return realMetadataCollection.getRelationship(userId, guid);
    }


    /**
     * Return a historical version of a relationship.
     *
     * @param userId unique identifier for requesting user.
     * @param guid String unique identifier for the relationship.
     * @param asOfTime the time used to determine which version of the entity that is desired.
     * @return Relationship structure.
     * @throws InvalidParameterException the guid or date is null or the date is for a future time.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                 the metadata collection is stored.
     * @throws RelationshipNotKnownException the requested entity instance is not known in the metadata collection
     *                                   at the time requested.
     * @throws FunctionNotSupportedException the repository does not support the asOfTime parameter.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    public  Relationship getRelationship(String    userId,
                                         String    guid,
                                         Date      asOfTime) throws InvalidParameterException,
                                                                    RepositoryErrorException,
                                                                    RelationshipNotKnownException,
                                                                    FunctionNotSupportedException,
                                                                    UserNotAuthorizedException
    {
        final String  methodName = "getRelationship";
        final String  guidParameterName = "guid";
        final String  asOfTimeParameter = "asOfTime";

        /*
         * Validate parameters
         */
        this.validateRepositoryConnector(methodName);
        parentConnector.validateRepositoryIsActive(methodName);

        repositoryValidator.validateUserId(repositoryName, userId, methodName);
        repositoryValidator.validateGUID(repositoryName, guidParameterName, guid, methodName);
        repositoryValidator.validateAsOfTime(repositoryName, asOfTimeParameter, asOfTime, methodName);

        /*
         * Perform operation
         */

        return realMetadataCollection.getRelationship(userId, guid, asOfTime);
    }


    /**
     * Return a list of relationships that match the requested properties by hte matching criteria.   The results
     * can be broken into pages.
     *
     * @param userId unique identifier for requesting user.
     * @param relationshipTypeGUID unique identifier (guid) for the new relationship's type.
     * @param matchProperties list of  properties used to narrow the search.
     * @param matchCriteria Enum defining how the properties should be matched to the relationships in the repository.
     * @param fromRelationshipElement the starting element number of the entities to return.
     *                                This is used when retrieving elements
     *                                beyond the first page of results. Zero means start from the first element.
     * @param limitResultsByStatus By default, relationships in all statuses are returned.  However, it is possible
     *                             to specify a list of statuses (eg ACTIVE) to restrict the results to.  Null means all
     *                             status values.
     * @param asOfTime Requests a historical query of the relationships for the entity.  Null means return the
     *                 present values.
     * @param sequencingProperty String name of the property that is to be used to sequence the results.
     *                           Null means do not sequence on a property name (see SequencingOrder).
     * @param sequencingOrder Enum defining how the results should be ordered.
     * @param pageSize the maximum number of result relationships that can be returned on this request.  Zero means
     *                 unrestricted return results size.
     * @return a list of relationships.  Null means no matching relationships.
     * @throws InvalidParameterException one of the parameters is invalid or null.
     * @throws TypeErrorException the type guid passed on the request is not known by the
     *                              metadata collection.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                    the metadata collection is stored.
     * @throws PropertyErrorException the properties specified are not valid for any of the requested types of
     *                                  relationships.
     * @throws PagingErrorException the paging/sequencing parameters are set up incorrectly.
     * @throws FunctionNotSupportedException the repository does not support the asOfTime parameter.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    public  List<Relationship> findRelationshipsByProperty(String                    userId,
                                                           String                    relationshipTypeGUID,
                                                           InstanceProperties        matchProperties,
                                                           MatchCriteria             matchCriteria,
                                                           int                       fromRelationshipElement,
                                                           List<InstanceStatus>      limitResultsByStatus,
                                                           Date                      asOfTime,
                                                           String                    sequencingProperty,
                                                           SequencingOrder           sequencingOrder,
                                                           int                       pageSize) throws InvalidParameterException,
                                                                                                      TypeErrorException,
                                                                                                      RepositoryErrorException,
                                                                                                      PropertyErrorException,
                                                                                                      PagingErrorException,
                                                                                                      FunctionNotSupportedException,
                                                                                                      UserNotAuthorizedException
    {
        final String  methodName = "findRelationshipsByProperty";
        final String  matchCriteriaParameterName = "matchCriteria";
        final String  matchPropertiesParameterName = "matchProperties";
        final String  guidParameterName = "relationshipTypeGUID";
        final String  asOfTimeParameter = "asOfTime";
        final String  pageSizeParameter = "pageSize";

        /*
         * Validate parameters
         */
        this.validateRepositoryConnector(methodName);
        parentConnector.validateRepositoryIsActive(methodName);

        repositoryValidator.validateUserId(repositoryName, userId, methodName);
        repositoryValidator.validateOptionalTypeGUID(repositoryName, guidParameterName, relationshipTypeGUID, methodName);
        repositoryValidator.validateAsOfTime(repositoryName, asOfTimeParameter, asOfTime, methodName);
        repositoryValidator.validatePageSize(repositoryName, pageSizeParameter, pageSize, methodName);
        repositoryValidator.validateMatchCriteria(repositoryName,
                                                  matchCriteriaParameterName,
                                                  matchPropertiesParameterName,
                                                  matchCriteria,
                                                  matchProperties,
                                                  methodName);

        /*
         * Perform operation
         */

        return realMetadataCollection.findRelationshipsByProperty(userId,
                                                                  relationshipTypeGUID,
                                                                  matchProperties,
                                                                  matchCriteria,
                                                                  fromRelationshipElement,
                                                                  limitResultsByStatus,
                                                                  asOfTime,
                                                                  sequencingProperty,
                                                                  sequencingOrder,
                                                                  pageSize);
    }


    /**
     * Return a list of relationships whose string based property values match the search criteria.  The
     * search criteria may include regex style wild cards.
     *
     * @param userId unique identifier for requesting user.
     * @param relationshipTypeGUID GUID of the type of entity to search for. Null means all types will
     *                       be searched (could be slow so not recommended).
     * @param searchCriteria String expression contained in any of the property values within the entities
     *                       of the supplied type.
     * @param fromRelationshipElement Element number of the results to skip to when building the results list
     *                                to return.  Zero means begin at the start of the results.  This is used
     *                                to retrieve the results over a number of pages.
     * @param limitResultsByStatus By default, relationships in all statuses are returned.  However, it is possible
     *                             to specify a list of statuses (eg ACTIVE) to restrict the results to.  Null means all
     *                             status values.
     * @param asOfTime Requests a historical query of the relationships for the entity.  Null means return the
     *                 present values.
     * @param sequencingProperty String name of the property that is to be used to sequence the results.
     *                           Null means do not sequence on a property name (see SequencingOrder).
     * @param sequencingOrder Enum defining how the results should be ordered.
     * @param pageSize the maximum number of result relationships that can be returned on this request.  Zero means
     *                 unrestricted return results size.
     * @return a list of relationships.  Null means no matching relationships.
     * @throws InvalidParameterException one of the parameters is invalid or null.
     * @throws TypeErrorException the type guid passed on the request is not known by the
     *                              metadata collection.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                  the metadata collection is stored.
     * @throws PropertyErrorException there is a problem with one of the other parameters.
     * @throws PagingErrorException the paging/sequencing parameters are set up incorrectly.
     * @throws FunctionNotSupportedException the repository does not support the asOfTime parameter.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    public  List<Relationship> findRelationshipsByPropertyValue(String                    userId,
                                                                String                    relationshipTypeGUID,
                                                                String                    searchCriteria,
                                                                int                       fromRelationshipElement,
                                                                List<InstanceStatus>      limitResultsByStatus,
                                                                Date                      asOfTime,
                                                                String                    sequencingProperty,
                                                                SequencingOrder           sequencingOrder,
                                                                int                       pageSize) throws InvalidParameterException,
                                                                                                           TypeErrorException,
                                                                                                           RepositoryErrorException,
                                                                                                           PropertyErrorException,
                                                                                                           PagingErrorException,
                                                                                                           FunctionNotSupportedException,
                                                                                                           UserNotAuthorizedException
    {
        final String  methodName = "findRelationshipsByPropertyValue";
        final String  asOfTimeParameter = "asOfTime";
        final String  guidParameter = "relationshipTypeGUID";
        final String  pageSizeParameter = "pageSize";

        /*
         * Validate parameters
         */
        this.validateRepositoryConnector(methodName);
        parentConnector.validateRepositoryIsActive(methodName);

        repositoryValidator.validateUserId(repositoryName, userId, methodName);
        repositoryValidator.validateOptionalTypeGUID(repositoryName, guidParameter, relationshipTypeGUID, methodName);
        repositoryValidator.validateAsOfTime(repositoryName, asOfTimeParameter, asOfTime, methodName);
        repositoryValidator.validatePageSize(repositoryName, pageSizeParameter, pageSize, methodName);

        /*
         * Perform operation
         */

        return realMetadataCollection.findRelationshipsByPropertyValue(userId,
                                                                       relationshipTypeGUID,
                                                                       searchCriteria,
                                                                       fromRelationshipElement,
                                                                       limitResultsByStatus,
                                                                       asOfTime,
                                                                       sequencingProperty,
                                                                       sequencingOrder,
                                                                       pageSize);
    }


    /**
     * Return all of the relationships and intermediate entities that connect the startEntity with the endEntity.
     *
     * @param userId unique identifier for requesting user.
     * @param startEntityGUID The entity that is used to anchor the query.
     * @param endEntityGUID the other entity that defines the scope of the query.
     * @param limitResultsByStatus By default, relationships in all statuses are returned.  However, it is possible
     *                             to specify a list of statuses (eg ACTIVE) to restrict the results to.  Null means all
     *                             status values.
     * @param asOfTime Requests a historical query of the relationships for the entity.  Null means return the
     *                 present values.
     * @return InstanceGraph the sub-graph that represents the returned linked entities and their relationships.
     * @throws InvalidParameterException one of the parameters is invalid or null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                  the metadata collection is stored.
     * @throws EntityNotKnownException the entity identified by either the startEntityGUID or the endEntityGUID
     *                                   is not found in the metadata collection.
     * @throws PropertyErrorException there is a problem with one of the other parameters.
     * @throws FunctionNotSupportedException the repository does not support the asOfTime parameter.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    public InstanceGraph getLinkingEntities(String                    userId,
                                            String                    startEntityGUID,
                                            String                    endEntityGUID,
                                            List<InstanceStatus>      limitResultsByStatus,
                                            Date                      asOfTime) throws InvalidParameterException,
                                                                                       RepositoryErrorException,
                                                                                       EntityNotKnownException,
                                                                                       PropertyErrorException,
                                                                                       FunctionNotSupportedException,
                                                                                       UserNotAuthorizedException
    {
        final String methodName                   = "getLinkingEntities";
        final String startEntityGUIDParameterName = "startEntityGUID";
        final String endEntityGUIDParameterName   = "entityGUID";
        final String asOfTimeParameter            = "asOfTime";

        /*
         * Validate parameters
         */
        this.validateRepositoryConnector(methodName);
        parentConnector.validateRepositoryIsActive(methodName);

        repositoryValidator.validateUserId(repositoryName, userId, methodName);
        repositoryValidator.validateGUID(repositoryName, startEntityGUIDParameterName, startEntityGUID, methodName);
        repositoryValidator.validateGUID(repositoryName, endEntityGUIDParameterName, endEntityGUID, methodName);
        repositoryValidator.validateAsOfTime(repositoryName, asOfTimeParameter, asOfTime, methodName);

        /*
         * Perform operation
         */

        return realMetadataCollection.getLinkingEntities(userId,
                                                         startEntityGUID,
                                                         endEntityGUID,
                                                         limitResultsByStatus,
                                                         asOfTime);
    }


    /**
     * Return the entities and relationships that radiate out from the supplied entity GUID.
     * The results are scoped both the instance type guids and the level.
     *
     * @param userId unique identifier for requesting user.
     * @param entityGUID the starting point of the query.
     * @param entityTypeGUIDs list of entity types to include in the query results.  Null means include
     *                          all entities found, irrespective of their type.
     * @param relationshipTypeGUIDs list of relationship types to include in the query results.  Null means include
     *                                all relationships found, irrespective of their type.
     * @param limitResultsByStatus By default, relationships in all statuses are returned.  However, it is possible
     *                             to specify a list of statuses (eg ACTIVE) to restrict the results to.  Null means all
     *                             status values.
     * @param limitResultsByClassification List of classifications that must be present on all returned entities.
     * @param asOfTime Requests a historical query of the relationships for the entity.  Null means return the
     *                 present values.
     * @param level the number of the relationships out from the starting entity that the query will traverse to
     *              gather results.
     * @return InstanceGraph the sub-graph that represents the returned linked entities and their relationships.
     * @throws InvalidParameterException one of the parameters is invalid or null.
     * @throws TypeErrorException one or more of the type guids passed on the request is not known by the
     *                              metadata collection.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                  the metadata collection is stored.
     * @throws EntityNotKnownException the entity identified by the entityGUID is not found in the metadata collection.
     * @throws PropertyErrorException there is a problem with one of the other parameters.
     * @throws FunctionNotSupportedException the repository does not support the asOfTime parameter.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    public  InstanceGraph getEntityNeighborhood(String               userId,
                                                String               entityGUID,
                                                List<String>         entityTypeGUIDs,
                                                List<String>         relationshipTypeGUIDs,
                                                List<InstanceStatus> limitResultsByStatus,
                                                List<String>         limitResultsByClassification,
                                                Date                 asOfTime,
                                                int                  level) throws InvalidParameterException,
                                                                                   TypeErrorException,
                                                                                   RepositoryErrorException,
                                                                                   EntityNotKnownException,
                                                                                   PropertyErrorException,
                                                                                   FunctionNotSupportedException,
                                                                                   UserNotAuthorizedException
    {
        final String methodName                                  = "getEntityNeighborhood";
        final String entityGUIDParameterName                     = "entityGUID";
        final String entityTypeGUIDParameterName                 = "entityTypeGUIDs";
        final String relationshipTypeGUIDParameterName           = "relationshipTypeGUIDs";
        final String limitedResultsByClassificationParameterName = "limitResultsByClassification";
        final String asOfTimeParameter                           = "asOfTime";

        /*
         * Validate parameters
         */
        this.validateRepositoryConnector(methodName);
        parentConnector.validateRepositoryIsActive(methodName);

        repositoryValidator.validateUserId(repositoryName, userId, methodName);
        repositoryValidator.validateGUID(repositoryName, entityGUIDParameterName, entityGUID, methodName);
        repositoryValidator.validateAsOfTime(repositoryName, asOfTimeParameter, asOfTime, methodName);

        if (entityTypeGUIDs != null)
        {
            for (String guid : entityTypeGUIDs)
            {
                repositoryValidator.validateTypeGUID(repositoryName, entityTypeGUIDParameterName, guid, methodName);
            }
        }

        if (relationshipTypeGUIDs != null)
        {
            for (String guid : relationshipTypeGUIDs)
            {
                repositoryValidator.validateTypeGUID(repositoryName, relationshipTypeGUIDParameterName, guid, methodName);
            }
        }

        if (limitResultsByClassification != null)
        {
            for (String classificationName : limitResultsByClassification)
            {
                repositoryValidator.validateClassificationName(repositoryName,
                                                               limitedResultsByClassificationParameterName,
                                                               classificationName,
                                                               methodName);
            }
        }

        /*
         * Perform operation
         */

        return realMetadataCollection.getEntityNeighborhood(userId,
                                                            entityGUID,
                                                            entityTypeGUIDs,
                                                            relationshipTypeGUIDs,
                                                            limitResultsByStatus,
                                                            limitResultsByClassification,
                                                            asOfTime,
                                                            level);
    }


    /**
     * Return the list of entities that are of the types listed in instanceTypes and are connected, either directly or
     * indirectly to the entity identified by startEntityGUID.
     *
     * @param userId unique identifier for requesting user.
     * @param startEntityGUID unique identifier of the starting entity
     * @param instanceTypes list of guids of types to search for.  Null means any type.
     * @param fromEntityElement starting element for results list.  Used in paging.  Zero means first element.
     * @param limitResultsByStatus By default, relationships in all statuses are returned.  However, it is possible
     *                             to specify a list of statuses (eg ACTIVE) to restrict the results to.  Null means all
     *                             status values.
     * @param limitResultsByClassification List of classifications that must be present on all returned entities.
     * @param asOfTime Requests a historical query of the relationships for the entity.  Null means return the
     *                 present values.
     * @param sequencingProperty String name of the property that is to be used to sequence the results.
     *                           Null means do not sequence on a property name (see SequencingOrder).
     * @param sequencingOrder Enum defining how the results should be ordered.
     * @param pageSize the maximum number of result entities that can be returned on this request.  Zero means
     *                 unrestricted return results size.
     * @return list of entities either directly or indirectly connected to the start entity
     * @throws InvalidParameterException one of the parameters is invalid or null.
     * @throws TypeErrorException one of the requested type guids passed on the request is not known by the
     *                              metadata collection.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                  the metadata collection is stored.
     * @throws EntityNotKnownException the entity identified by the startEntityGUID
     *                                   is not found in the metadata collection.
     * @throws PropertyErrorException the sequencing property specified is not valid for any of the requested types of
     *                                  entity.
     * @throws PagingErrorException the paging/sequencing parameters are set up incorrectly.
     * @throws FunctionNotSupportedException the repository does not support the asOfTime parameter.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    public  List<EntityDetail> getRelatedEntities(String               userId,
                                                  String               startEntityGUID,
                                                  List<String>         instanceTypes,
                                                  int                  fromEntityElement,
                                                  List<InstanceStatus> limitResultsByStatus,
                                                  List<String>         limitResultsByClassification,
                                                  Date                 asOfTime,
                                                  String               sequencingProperty,
                                                  SequencingOrder      sequencingOrder,
                                                  int                  pageSize) throws InvalidParameterException,
                                                                                        TypeErrorException,
                                                                                        RepositoryErrorException,
                                                                                        EntityNotKnownException,
                                                                                        PropertyErrorException,
                                                                                        PagingErrorException,
                                                                                        FunctionNotSupportedException,
                                                                                        UserNotAuthorizedException
    {
        final String  methodName = "getRelatedEntities";
        final String  entityGUIDParameterName  = "startEntityGUID";
        final String  instanceTypeParameter = "InstanceTypes";
        final String  asOfTimeParameter = "asOfTime";
        final String  pageSizeParameter = "pageSize";

        /*
         * Validate parameters
         */
        this.validateRepositoryConnector(methodName);
        parentConnector.validateRepositoryIsActive(methodName);

        repositoryValidator.validateUserId(repositoryName, userId, methodName);
        repositoryValidator.validateGUID(repositoryName, entityGUIDParameterName, startEntityGUID, methodName);
        repositoryValidator.validateAsOfTime(repositoryName, asOfTimeParameter, asOfTime, methodName);
        repositoryValidator.validatePageSize(repositoryName, pageSizeParameter, pageSize, methodName);

        if (instanceTypes != null)
        {
            for (String guid : instanceTypes)
            {
                repositoryValidator.validateTypeGUID(repositoryName, instanceTypeParameter, guid, methodName);
            }
        }

        /*
         * Perform operation
         */

        return realMetadataCollection.getRelatedEntities(userId,
                                                         startEntityGUID,
                                                         instanceTypes,
                                                         fromEntityElement,
                                                         limitResultsByStatus,
                                                         limitResultsByClassification,
                                                         asOfTime,
                                                         sequencingProperty,
                                                         sequencingOrder,
                                                         pageSize);
    }


    /* ======================================================
     * Group 4: Maintaining entity and relationship instances
     */

    /**
     * Create a new entity and put it in the requested state.  The new entity is returned.
     *
     * @param userId unique identifier for requesting user.
     * @param entityTypeGUID unique identifier (guid) for the new entity's type.
     * @param initialProperties initial list of properties for the new entity null means no properties.
     * @param initialClassifications initial list of classifications for the new entity null means no classifications.
     * @param initialStatus initial status typically DRAFT, PREPARED or ACTIVE.
     * @return EntityDetail showing the new header plus the requested properties and classifications.  The entity will
     * not have any relationships at this stage.
     * @throws InvalidParameterException one of the parameters is invalid or null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                    the metadata collection is stored.
     * @throws TypeErrorException the requested type is not known, or not supported in the metadata repository
     *                              hosting the metadata collection.
     * @throws PropertyErrorException one or more of the requested properties are not defined, or have different
     *                                  characteristics in the TypeDef for this entity's type.
     * @throws ClassificationErrorException one or more of the requested classifications are either not known or
     *                                           not defined for this entity type.
     * @throws StatusNotSupportedException the metadata repository hosting the metadata collection does not support
     *                                       the requested status.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    public EntityDetail addEntity(String                     userId,
                                  String                     entityTypeGUID,
                                  InstanceProperties         initialProperties,
                                  List<Classification>       initialClassifications,
                                  InstanceStatus             initialStatus) throws InvalidParameterException,
                                                                                   RepositoryErrorException,
                                                                                   TypeErrorException,
                                                                                   PropertyErrorException,
                                                                                   ClassificationErrorException,
                                                                                   StatusNotSupportedException,
                                                                                   UserNotAuthorizedException
    {
        final String  methodName                    = "addEntity";
        final String  entityGUIDParameterName       = "entityTypeGUID";
        final String  propertiesParameterName       = "initialProperties";
        final String  classificationsParameterName  = "initialClassifications";
        final String  initialStatusParameterName    = "initialStatus";

        /*
         * Validate parameters
         */
        this.validateRepositoryConnector(methodName);
        parentConnector.validateRepositoryIsActive(methodName);

        repositoryValidator.validateUserId(repositoryName, userId, methodName);
        repositoryValidator.validateTypeGUID(repositoryName, entityGUIDParameterName, entityTypeGUID, methodName);

        TypeDef  typeDef = repositoryHelper.getTypeDef(repositoryName, entityGUIDParameterName, entityTypeGUID, methodName);

        repositoryValidator.validateTypeDefForInstance(repositoryName, entityGUIDParameterName, typeDef, methodName);
        repositoryValidator.validateClassificationList(repositoryName,
                                                       classificationsParameterName,
                                                       initialClassifications,
                                                       typeDef.getName(),
                                                       methodName);

        repositoryValidator.validatePropertiesForType(repositoryName,
                                                      propertiesParameterName,
                                                      typeDef,
                                                      initialProperties,
                                                      methodName);

        repositoryValidator.validateInstanceStatus(repositoryName,
                                                   initialStatusParameterName,
                                                   initialStatus,
                                                   typeDef,
                                                   methodName);

        /*
         * Validation complete, ok to create new instance
         */

        EntityDetail   entity = realMetadataCollection.addEntity(userId,
                                                                 entityTypeGUID,
                                                                 initialProperties,
                                                                 initialClassifications,
                                                                 initialStatus);

        if (entity != null)
        {
            /*
             * Ensure the provenance of the entity is correctly set.  A repository may not support the storing of
             * the metadata collection id in the repository (or uses null to mean "local").  When the entity
             * detail is sent out, it must have its home metadata collection id set up.  So LocalOMRSMetadataCollection
             * fixes up the provenance.
             */
            if (entity.getMetadataCollectionId() == null)
            {
                entity.setMetadataCollectionId(metadataCollectionId);
                entity.setInstanceProvenanceType(InstanceProvenanceType.LOCAL_COHORT);
            }

            /*
             * OK to send out
             */
            if (outboundRepositoryEventProcessor != null)
            {
                outboundRepositoryEventProcessor.processNewEntityEvent(repositoryName,
                                                                       metadataCollectionId,
                                                                       localServerName,
                                                                       localServerType,
                                                                       localOrganizationName,
                                                                       entity);
            }
        }

        return entity;
    }


    /**
     * Create an entity proxy in the metadata collection.  This is used to store relationships that span metadata
     * repositories.
     *
     * @param userId unique identifier for requesting user.
     * @param entityProxy details of entity to add.
     * @throws InvalidParameterException the entity proxy is null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                    the metadata collection is stored.
     * @throws FunctionNotSupportedException the repository does not support entity proxies as first class elements.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    public void addEntityProxy(String       userId,
                               EntityProxy  entityProxy) throws InvalidParameterException,
                                                                RepositoryErrorException,
                                                                FunctionNotSupportedException,
                                                                UserNotAuthorizedException
    {
        final String  methodName         = "addEntityProxy";
        final String  proxyParameterName = "entityProxy";

        /*
         * Validate parameters
         */
        this.validateRepositoryConnector(methodName);
        parentConnector.validateRepositoryIsActive(methodName);

        repositoryValidator.validateUserId(repositoryName, userId, methodName);

        repositoryValidator.validateEntityProxy(repositoryName,
                                                metadataCollectionId,
                                                proxyParameterName,
                                                entityProxy,
                                                methodName);

        /*
         * Validation complete
         *
         * EntityProxies are used to store a relationship where the entity at one end of the relationship is
         * not stored locally.  Its type may not be supported locally either.
         */
        realMetadataCollection.addEntityProxy(userId, entityProxy);
    }


    /**
     * Update the status for a specific entity.
     *
     * @param userId unique identifier for requesting user.
     * @param entityGUID unique identifier (guid) for the requested entity.
     * @param newStatus new InstanceStatus for the entity.
     * @return EntityDetail showing the current entity header, properties and classifications.
     * @throws InvalidParameterException one of the parameters is invalid or null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                  the metadata collection is stored.
     * @throws EntityNotKnownException the entity identified by the guid is not found in the metadata collection.
     * @throws StatusNotSupportedException the metadata repository hosting the metadata collection does not support
     *                                      the requested status.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    public EntityDetail updateEntityStatus(String           userId,
                                           String           entityGUID,
                                           InstanceStatus   newStatus) throws InvalidParameterException,
                                                                              RepositoryErrorException,
                                                                              EntityNotKnownException,
                                                                              StatusNotSupportedException,
                                                                              UserNotAuthorizedException
    {
        final String  methodName               = "updateEntityStatus";
        final String  entityGUIDParameterName  = "entityGUID";

        /*
         * Validate parameters
         */
        this.validateRepositoryConnector(methodName);
        parentConnector.validateRepositoryIsActive(methodName);

        repositoryValidator.validateUserId(repositoryName, userId, methodName);
        repositoryValidator.validateGUID(repositoryName, entityGUIDParameterName, entityGUID, methodName);

        /*
         * Locate entity
         */
        EntityDetail   currentEntity;
        try
        {
            currentEntity = realMetadataCollection.getEntityDetail(userId, entityGUID);
        }
        catch (EntityProxyOnlyException  error)
        {
            /*
             * There is a serious logic error as the entity stored in what should be the home repository
             * is only an entity proxy.
             */
            OMRSErrorCode errorCode    = OMRSErrorCode.ENTITY_PROXY_IN_HOME;
            String        errorMessage = errorCode.getErrorMessageId()
                    + errorCode.getFormattedErrorMessage(repositoryName, entityGUID, methodName);

            throw new RepositoryErrorException(errorCode.getHTTPErrorCode(),
                                               this.getClass().getName(),
                                               methodName,
                                               errorMessage,
                                               errorCode.getSystemAction(),
                                               errorCode.getUserAction(),
                                               error);
        }

        EntityDetail   newEntity = realMetadataCollection.updateEntityStatus(userId, entityGUID, newStatus);

        notifyOfUpdatedEntity(currentEntity, newEntity);

        return newEntity;
    }


    /**
     * Update selected properties in an entity.
     *
     * @param userId unique identifier for requesting user.
     * @param entityGUID String unique identifier (guid) for the entity.
     * @param properties a list of properties to change.
     * @return EntityDetail showing the resulting entity header, properties and classifications.
     * @throws InvalidParameterException one of the parameters is invalid or null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                  the metadata collection is stored.
     * @throws EntityNotKnownException the entity identified by the guid is not found in the metadata collection
     * @throws PropertyErrorException one or more of the requested properties are not defined, or have different
     *                                characteristics in the TypeDef for this entity's type
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    public EntityDetail updateEntityProperties(String               userId,
                                               String               entityGUID,
                                               InstanceProperties   properties) throws InvalidParameterException,
                                                                                       RepositoryErrorException,
                                                                                       EntityNotKnownException,
                                                                                       PropertyErrorException,
                                                                                       UserNotAuthorizedException
    {
        final String  methodName = "updateEntityProperties";
        final String  entityGUIDParameterName  = "entityGUID";

        /*
         * Validate parameters
         */
        this.validateRepositoryConnector(methodName);
        parentConnector.validateRepositoryIsActive(methodName);

        repositoryValidator.validateUserId(repositoryName, userId, methodName);
        repositoryValidator.validateGUID(repositoryName, entityGUIDParameterName, entityGUID, methodName);

        /*
         * Locate entity
         */
        EntityDetail   currentEntity;
        try
        {
            currentEntity = realMetadataCollection.getEntityDetail(userId, entityGUID);
        }
        catch (EntityProxyOnlyException  error)
        {
            /*
             * There is a serious logic error as the entity stored in what should be the home repository
             * is only an entity proxy.
             */
            OMRSErrorCode errorCode    = OMRSErrorCode.ENTITY_PROXY_IN_HOME;
            String        errorMessage = errorCode.getErrorMessageId()
                                       + errorCode.getFormattedErrorMessage(repositoryName, entityGUID, methodName);

            throw new RepositoryErrorException(errorCode.getHTTPErrorCode(),
                                               this.getClass().getName(),
                                               methodName,
                                               errorMessage,
                                               errorCode.getSystemAction(),
                                               errorCode.getUserAction(),
                                               error);
        }

        EntityDetail   newEntity = realMetadataCollection.updateEntityProperties(userId, entityGUID, properties);

        notifyOfUpdatedEntity(currentEntity, newEntity);

        return newEntity;
    }


    /**
     * Send out details of an updated entity.
     *
     * @param oldEntity original values
     * @param newEntity updated entity
     */
    private void notifyOfUpdatedEntity(EntityDetail     oldEntity,
                                       EntityDetail     newEntity)
    {
        if (newEntity != null)
        {
            /*
             * Ensure the provenance of the entity is correctly set.  A repository may not support the storing of
             * the metadata collection id in the repository (or uses null to mean "local").  When the entity
             * detail is sent out, it must have its home metadata collection id set up.  So LocalOMRSMetadataCollection
             * fixes up the provenance.
             */
            if (newEntity.getMetadataCollectionId() == null)
            {
                newEntity.setMetadataCollectionId(metadataCollectionId);
                newEntity.setInstanceProvenanceType(InstanceProvenanceType.LOCAL_COHORT);
            }

            /*
             * OK to send out
             */
            if (outboundRepositoryEventProcessor != null)
            {
                outboundRepositoryEventProcessor.processUpdatedEntityEvent(repositoryName,
                                                                           metadataCollectionId,
                                                                           localServerName,
                                                                           localServerType,
                                                                           localOrganizationName,
                                                                           oldEntity,
                                                                           newEntity);
            }
        }
    }


    /**
     * Undo the last update to an entity and return the previous content.
     *
     * @param userId unique identifier for requesting user.
     * @param entityGUID String unique identifier (guid) for the entity.
     * @return EntityDetail showing the resulting entity header, properties and classifications.
     * @throws InvalidParameterException the guid is null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                  the metadata collection is stored.
     * @throws EntityNotKnownException the entity identified by the guid is not found in the metadata collection.
     * @throws FunctionNotSupportedException the repository does not support undo.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    public EntityDetail undoEntityUpdate(String    userId,
                                         String    entityGUID) throws InvalidParameterException,
                                                                      RepositoryErrorException,
                                                                      EntityNotKnownException,
                                                                      FunctionNotSupportedException,
                                                                      UserNotAuthorizedException
    {
        final String  methodName = "undoEntityUpdate";
        final String  entityGUIDParameterName  = "entityGUID";

        /*
         * Validate parameters
         */
        this.validateRepositoryConnector(methodName);
        parentConnector.validateRepositoryIsActive(methodName);

        repositoryValidator.validateUserId(repositoryName, userId, methodName);
        repositoryValidator.validateGUID(repositoryName, entityGUIDParameterName, entityGUID, methodName);

        /*
         * Validation complete, ok to restore entity
         */

        EntityDetail   entity = realMetadataCollection.undoEntityUpdate(userId, entityGUID);

        if (entity != null)
        {
            /*
             * Ensure the provenance of the entity is correctly set.  A repository may not support the storing of
             * the metadata collection id in the repository (or uses null to mean "local").  When the entity
             * detail is sent out, it must have its home metadata collection id set up.  So LocalOMRSMetadataCollection
             * fixes up the provenance.
             */
            if (entity.getMetadataCollectionId() == null)
            {
                entity.setMetadataCollectionId(metadataCollectionId);
                entity.setInstanceProvenanceType(InstanceProvenanceType.LOCAL_COHORT);
            }

            /*
             * OK to send out
             */
            if (outboundRepositoryEventProcessor != null)
            {
                outboundRepositoryEventProcessor.processUndoneEntityEvent(repositoryName,
                                                                          metadataCollectionId,
                                                                          localServerName,
                                                                          localServerType,
                                                                          localOrganizationName,
                                                                          entity);
            }
        }

        return entity;
    }


    /**
     * Delete an entity.  The entity is soft deleted.  This means it is still in the graph but it is no longer returned
     * on queries.  All relationships to the entity are also soft-deleted and will no longer be usable.
     * To completely eliminate the entity from the graph requires a call to the purgeEntity() method after the delete call.
     * The restoreEntity() method will switch an entity back to Active status to restore the entity to normal use.
     *
     * @param userId unique identifier for requesting user.
     * @param typeDefGUID unique identifier of the type of the entity to delete.
     * @param typeDefName unique name of the type of the entity to delete.
     * @param obsoleteEntityGUID String unique identifier (guid) for the entity
     * @return deleted entity
     * @throws InvalidParameterException one of the parameters is invalid or null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                    the metadata collection is stored.
     * @throws EntityNotKnownException the entity identified by the guid is not found in the metadata collection.
     * @throws FunctionNotSupportedException the metadata repository hosting the metadata collection does not support
     *                                       soft-deletes (use purgeEntity() to remove the entity permanently).
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    public EntityDetail   deleteEntity(String userId,
                                       String typeDefGUID,
                                       String typeDefName,
                                       String obsoleteEntityGUID) throws InvalidParameterException,
                                                                         RepositoryErrorException,
                                                                         EntityNotKnownException,
                                                                         FunctionNotSupportedException,
                                                                         UserNotAuthorizedException
    {
        final String  methodName               = "deleteEntity";
        final String  typeDefGUIDParameterName = "typeDefGUID";
        final String  typeDefNameParameterName = "typeDefName";
        final String  entityGUIDParameterName  = "obsoleteEntityGUID";

        /*
         * Validate parameters
         */
        this.validateRepositoryConnector(methodName);
        parentConnector.validateRepositoryIsActive(methodName);

        repositoryValidator.validateUserId(repositoryName, userId, methodName);
        repositoryValidator.validateTypeDefIds(repositoryName,
                                               typeDefGUIDParameterName,
                                               typeDefNameParameterName,
                                               typeDefGUID,
                                               typeDefName,
                                               methodName);
        repositoryValidator.validateGUID(repositoryName, entityGUIDParameterName, obsoleteEntityGUID, methodName);

        /*
         * Locate Entity
         */

        EntityDetail entity = realMetadataCollection.deleteEntity(userId,
                                                                  typeDefGUID,
                                                                  typeDefName,
                                                                  obsoleteEntityGUID);

        if (outboundRepositoryEventProcessor != null)
        {
            outboundRepositoryEventProcessor.processDeletedEntityEvent(repositoryName,
                                                                       metadataCollectionId,
                                                                       localServerName,
                                                                       localServerType,
                                                                       localOrganizationName,
                                                                       entity);
        }

        return entity;
    }


    /**
     * Permanently removes a deleted entity from the metadata collection.  This request can not be undone.
     *
     * @param userId unique identifier for requesting user.
     * @param typeDefGUID unique identifier of the type of the entity to purge.
     * @param typeDefName unique name of the type of the entity to purge.
     * @param deletedEntityGUID String unique identifier (guid) for the entity.
     * @throws InvalidParameterException one of the parameters is invalid or null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                  the metadata collection is stored.
     * @throws EntityNotKnownException the entity identified by the guid is not found in the metadata collection
     * @throws EntityNotDeletedException the entity is not in DELETED status and so can not be purged
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    public void purgeEntity(String    userId,
                            String    typeDefGUID,
                            String    typeDefName,
                            String    deletedEntityGUID) throws InvalidParameterException,
                                                                RepositoryErrorException,
                                                                EntityNotKnownException,
                                                                EntityNotDeletedException,
                                                                UserNotAuthorizedException
    {
        final String  methodName               = "purgeEntity";
        final String  typeDefGUIDParameterName = "typeDefGUID";
        final String  typeDefNameParameterName = "typeDefName";
        final String  entityGUIDParameterName  = "deletedEntityGUID";

        /*
         * Validate parameters
         */
        this.validateRepositoryConnector(methodName);
        parentConnector.validateRepositoryIsActive(methodName);

        repositoryValidator.validateUserId(repositoryName, userId, methodName);
        repositoryValidator.validateTypeDefIds(repositoryName,
                                               typeDefGUIDParameterName,
                                               typeDefNameParameterName,
                                               typeDefGUID,
                                               typeDefName,
                                               methodName);
        repositoryValidator.validateGUID(repositoryName, entityGUIDParameterName, deletedEntityGUID, methodName);

        /*
         * Purge entity
         */

        realMetadataCollection.purgeEntity(userId,
                                           typeDefGUID,
                                           typeDefName,
                                           deletedEntityGUID);

        if (outboundRepositoryEventProcessor != null)
        {
            outboundRepositoryEventProcessor.processPurgedEntityEvent(repositoryName,
                                                                      metadataCollectionId,
                                                                      localServerName,
                                                                      localServerType,
                                                                      localOrganizationName,
                                                                      typeDefGUID,
                                                                      typeDefName,
                                                                      deletedEntityGUID);
        }
    }


    /**
     * Restore the requested entity to the state it was before it was deleted.
     *
     * @param userId unique identifier for requesting user.
     * @param deletedEntityGUID String unique identifier (guid) for the entity.
     * @return EntityDetail showing the restored entity header, properties and classifications.
     * @throws InvalidParameterException the guid is null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     * the metadata collection is stored.
     * @throws EntityNotKnownException the entity identified by the guid is not found in the metadata collection
     * @throws EntityNotDeletedException the entity is currently not in DELETED status and so it can not be restored
     * @throws FunctionNotSupportedException the repository does not support soft-deletes.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    public EntityDetail restoreEntity(String    userId,
                                      String    deletedEntityGUID) throws InvalidParameterException,
                                                                          RepositoryErrorException,
                                                                          EntityNotKnownException,
                                                                          EntityNotDeletedException,
                                                                          FunctionNotSupportedException,
                                                                          UserNotAuthorizedException
    {
        final String  methodName              = "restoreEntity";
        final String  entityGUIDParameterName = "deletedEntityGUID";

        /*
         * Validate parameters
         */
        this.validateRepositoryConnector(methodName);
        parentConnector.validateRepositoryIsActive(methodName);

        repositoryValidator.validateUserId(repositoryName, userId, methodName);
        repositoryValidator.validateGUID(repositoryName, entityGUIDParameterName, deletedEntityGUID, methodName);

        /*
         * Restore entity
         */

        EntityDetail   entity = realMetadataCollection.restoreEntity(userId, deletedEntityGUID);

        if (entity != null)
        {
            /*
             * Ensure the provenance of the entity is correctly set.  A repository may not support the storing of
             * the metadata collection id in the repository (or uses null to mean "local").  When the entity
             * detail is sent out, it must have its home metadata collection id set up.  So LocalOMRSMetadataCollection
             * fixes up the provenance.
             */
            if (entity.getMetadataCollectionId() == null)
            {
                entity.setMetadataCollectionId(metadataCollectionId);
                entity.setInstanceProvenanceType(InstanceProvenanceType.LOCAL_COHORT);
            }

            /*
             * OK to send out
             */
            if (outboundRepositoryEventProcessor != null)
            {
                outboundRepositoryEventProcessor.processRestoredEntityEvent(repositoryName,
                                                                            metadataCollectionId,
                                                                            localServerName,
                                                                            localServerType,
                                                                            localOrganizationName,
                                                                            entity);
            }
        }

        return entity;
    }


    /**
     * Add the requested classification to a specific entity.
     *
     * @param userId unique identifier for requesting user.
     * @param entityGUID String unique identifier (guid) for the entity.
     * @param classificationName String name for the classification.
     * @param classificationProperties list of properties to set in the classification.
     * @return EntityDetail showing the resulting entity header, properties and classifications.
     * @throws InvalidParameterException one of the parameters is invalid or null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                  the metadata collection is stored.
     * @throws EntityNotKnownException the entity identified by the guid is not found in the metadata collection
     * @throws ClassificationErrorException the requested classification is either not known or not valid
     *                                         for the entity.
     * @throws PropertyErrorException one or more of the requested properties are not defined, or have different
     *                                characteristics in the TypeDef for this classification type
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    public EntityDetail classifyEntity(String               userId,
                                       String               entityGUID,
                                       String               classificationName,
                                       InstanceProperties   classificationProperties) throws InvalidParameterException,
                                                                                             RepositoryErrorException,
                                                                                             EntityNotKnownException,
                                                                                             ClassificationErrorException,
                                                                                             PropertyErrorException,
                                                                                             UserNotAuthorizedException
    {
        final String  methodName                  = "classifyEntity";
        final String  entityGUIDParameterName     = "entityGUID";

        /*
         * Validate parameters
         */
        this.validateRepositoryConnector(methodName);
        parentConnector.validateRepositoryIsActive(methodName);

        repositoryValidator.validateUserId(repositoryName, userId, methodName);
        repositoryValidator.validateGUID(repositoryName, entityGUIDParameterName, entityGUID, methodName);

        /*
         * Locate entity
         */

        EntityDetail   entity = realMetadataCollection.classifyEntity(userId,
                                                                      entityGUID,
                                                                      classificationName,
                                                                      classificationProperties);

        if (entity != null)
        {
            /*
             * Ensure the provenance of the entity is correctly set.  A repository may not support the storing of
             * the metadata collection id in the repository (or uses null to mean "local").  When the entity
             * detail is sent out, it must have its home metadata collection id set up.  So LocalOMRSMetadataCollection
             * fixes up the provenance.
             */
            if (entity.getMetadataCollectionId() == null)
            {
                entity.setMetadataCollectionId(metadataCollectionId);
                entity.setInstanceProvenanceType(InstanceProvenanceType.LOCAL_COHORT);
            }

            /*
             * OK to send out
             */
            if (outboundRepositoryEventProcessor != null)
            {
                outboundRepositoryEventProcessor.processClassifiedEntityEvent(repositoryName,
                                                                              metadataCollectionId,
                                                                              localServerName,
                                                                              localServerType,
                                                                              localOrganizationName,
                                                                              entity);
            }
        }

        return entity;
    }


    /**
     * Remove a specific classification from an entity.
     *
     * @param userId unique identifier for requesting user.
     * @param entityGUID String unique identifier (guid) for the entity.
     * @param classificationName String name for the classification.
     * @return EntityDetail showing the resulting entity header, properties and classifications.
     * @throws InvalidParameterException one of the parameters is invalid or null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                  the metadata collection is stored.
     * @throws EntityNotKnownException the entity identified by the guid is not found in the metadata collection
     * @throws ClassificationErrorException the requested classification is not set on the entity.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    public EntityDetail declassifyEntity(String    userId,
                                         String    entityGUID,
                                         String    classificationName) throws InvalidParameterException,
                                                                              RepositoryErrorException,
                                                                              EntityNotKnownException,
                                                                              ClassificationErrorException,
                                                                              UserNotAuthorizedException
    {
        final String  methodName                  = "declassifyEntity";
        final String  entityGUIDParameterName     = "entityGUID";
        final String  classificationParameterName = "classificationName";

        /*
         * Validate parameters
         */
        this.validateRepositoryConnector(methodName);
        parentConnector.validateRepositoryIsActive(methodName);

        repositoryValidator.validateUserId(repositoryName, userId, methodName);
        repositoryValidator.validateGUID(repositoryName, entityGUIDParameterName, entityGUID, methodName);
        repositoryValidator.validateClassificationName(repositoryName,
                                                       classificationParameterName,
                                                       classificationName,
                                                       methodName);

        /*
         * Locate entity
         */

        EntityDetail   entity = realMetadataCollection.declassifyEntity(userId,
                                                                        entityGUID,
                                                                        classificationName);

        if (entity != null)
        {
            /*
             * Ensure the provenance of the entity is correctly set.  A repository may not support the storing of
             * the metadata collection id in the repository (or uses null to mean "local").  When the entity
             * detail is sent out, it must have its home metadata collection id set up.  So LocalOMRSMetadataCollection
             * fixes up the provenance.
             */
            if (entity.getMetadataCollectionId() == null)
            {
                entity.setMetadataCollectionId(metadataCollectionId);
                entity.setInstanceProvenanceType(InstanceProvenanceType.LOCAL_COHORT);
            }

            /*
             * OK to send out
             */
            if (outboundRepositoryEventProcessor != null)
            {
                outboundRepositoryEventProcessor.processDeclassifiedEntityEvent(repositoryName,
                                                                                metadataCollectionId,
                                                                                localServerName,
                                                                                localServerType,
                                                                                localOrganizationName,
                                                                                entity);
            }
        }

        return entity;
    }


    /**
     * Update one or more properties in one of an entity's classifications.
     *
     * @param userId unique identifier for requesting user.
     * @param entityGUID String unique identifier (guid) for the entity.
     * @param classificationName String name for the classification.
     * @param properties list of properties for the classification.
     * @return EntityDetail showing the resulting entity header, properties and classifications.
     * @throws InvalidParameterException one of the parameters is invalid or null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                  the metadata collection is stored.
     * @throws EntityNotKnownException the entity identified by the guid is not found in the metadata collection
     * @throws ClassificationErrorException the requested classification is not attached to the classification.
     * @throws PropertyErrorException one or more of the requested properties are not defined, or have different
     *                                characteristics in the TypeDef for this classification type
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    public EntityDetail updateEntityClassification(String               userId,
                                                   String               entityGUID,
                                                   String               classificationName,
                                                   InstanceProperties   properties) throws InvalidParameterException,
                                                                                           RepositoryErrorException,
                                                                                           EntityNotKnownException,
                                                                                           ClassificationErrorException,
                                                                                           PropertyErrorException,
                                                                                           UserNotAuthorizedException
    {
        final String  methodName = "updateEntityClassification";
        final String  entityGUIDParameterName     = "entityGUID";
        final String  classificationParameterName = "classificationName";
        final String  propertiesParameterName = "properties";

        /*
         * Validate parameters
         */
        this.validateRepositoryConnector(methodName);
        parentConnector.validateRepositoryIsActive(methodName);

        repositoryValidator.validateUserId(repositoryName, userId, methodName);
        repositoryValidator.validateGUID(repositoryName, entityGUIDParameterName, entityGUID, methodName);
        repositoryValidator.validateClassificationName(repositoryName, classificationParameterName, classificationName, methodName);


        try
        {
            repositoryValidator.validateClassificationProperties(repositoryName,
                                                                 classificationName,
                                                                 propertiesParameterName,
                                                                 properties,
                                                                 methodName);
        }
        catch (PropertyErrorException  error)
        {
            throw error;
        }
        catch (Throwable   error)
        {
            OMRSErrorCode errorCode = OMRSErrorCode.UNKNOWN_CLASSIFICATION;

            throw new ClassificationErrorException(errorCode.getHTTPErrorCode(),
                                                   this.getClass().getName(),
                                                   methodName,
                                                   error.getMessage(),
                                                   errorCode.getSystemAction(),
                                                   errorCode.getUserAction());
        }


        /*
         * Locate entity
         */

        EntityDetail   entity = realMetadataCollection.updateEntityClassification(userId,
                                                                                  entityGUID,
                                                                                  classificationName,
                                                                                  properties);

        if (entity != null)
        {
            /*
             * Ensure the provenance of the entity is correctly set.  A repository may not support the storing of
             * the metadata collection id in the repository (or uses null to mean "local").  When the entity
             * detail is sent out, it must have its home metadata collection id set up.  So LocalOMRSMetadataCollection
             * fixes up the provenance.
             */
            if (entity.getMetadataCollectionId() == null)
            {
                entity.setMetadataCollectionId(metadataCollectionId);
                entity.setInstanceProvenanceType(InstanceProvenanceType.LOCAL_COHORT);
            }

            /*
             * OK to send out
             */
            if (outboundRepositoryEventProcessor != null)
            {
                outboundRepositoryEventProcessor.processReclassifiedEntityEvent(repositoryName,
                                                                                metadataCollectionId,
                                                                                localServerName,
                                                                                localServerType,
                                                                                localOrganizationName,
                                                                                entity);
            }
        }

        return entity;
    }



    /**
     * Add a new relationship between two entities to the metadata collection.
     *
     * @param userId unique identifier for requesting user.
     * @param relationshipTypeGUID unique identifier (guid) for the new relationship's type.
     * @param initialProperties initial list of properties for the new entity null means no properties.
     * @param entityOneGUID the unique identifier of one of the entities that the relationship is connecting together.
     * @param entityTwoGUID the unique identifier of the other entity that the relationship is connecting together.
     * @param initialStatus initial status typically DRAFT, PREPARED or ACTIVE.
     * @return Relationship structure with the new header, requested entities and properties.
     * @throws InvalidParameterException one of the parameters is invalid or null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                 the metadata collection is stored.
     * @throws TypeErrorException the requested type is not known, or not supported in the metadata repository
     *                            hosting the metadata collection.
     * @throws PropertyErrorException one or more of the requested properties are not defined, or have different
     *                                  characteristics in the TypeDef for this relationship's type.
     * @throws EntityNotKnownException one of the requested entities is not known in the metadata collection.
     * @throws StatusNotSupportedException the metadata repository hosting the metadata collection does not support
     *                                     the requested status.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    public Relationship addRelationship(String               userId,
                                        String               relationshipTypeGUID,
                                        InstanceProperties   initialProperties,
                                        String               entityOneGUID,
                                        String               entityTwoGUID,
                                        InstanceStatus       initialStatus) throws InvalidParameterException,
                                                                                   RepositoryErrorException,
                                                                                   TypeErrorException,
                                                                                   PropertyErrorException,
                                                                                   EntityNotKnownException,
                                                                                   StatusNotSupportedException,
                                                                                   UserNotAuthorizedException
    {
        final String  methodName = "addRelationship";
        final String  guidParameterName = "relationshipTypeGUID";
        final String  propertiesParameterName       = "initialProperties";
        final String  initialStatusParameterName    = "initialStatus";

        /*
         * Validate parameters
         */
        this.validateRepositoryConnector(methodName);
        parentConnector.validateRepositoryIsActive(methodName);

        repositoryValidator.validateUserId(repositoryName, userId, methodName);
        repositoryValidator.validateTypeGUID(repositoryName, guidParameterName, relationshipTypeGUID, methodName);

        TypeDef  typeDef = repositoryHelper.getTypeDef(repositoryName, guidParameterName, relationshipTypeGUID, methodName);

        repositoryValidator.validateTypeDefForInstance(repositoryName, guidParameterName, typeDef, methodName);


        repositoryValidator.validatePropertiesForType(repositoryName,
                                                      propertiesParameterName,
                                                      typeDef,
                                                      initialProperties,
                                                      methodName);

        repositoryValidator.validateInstanceStatus(repositoryName,
                                                   initialStatusParameterName,
                                                   initialStatus,
                                                   typeDef,
                                                   methodName);

        /*
         * Validation complete ok to create new instance
         */

        Relationship   relationship = realMetadataCollection.addRelationship(userId,
                                                                             relationshipTypeGUID,
                                                                             initialProperties,
                                                                             entityOneGUID,
                                                                             entityTwoGUID,
                                                                             initialStatus);

        if (relationship != null)
        {
            /*
             * Ensure the provenance of the relationship is correctly set.  A repository may not support the storing of
             * the metadata collection id in the repository (or uses null to mean "local").  When the relationship
             * detail is sent out, it must have its home metadata collection id set up.  So LocalOMRSMetadataCollection
             * fixes up the provenance.
             */
            if (relationship.getMetadataCollectionId() == null)
            {
                relationship.setMetadataCollectionId(metadataCollectionId);
                relationship.setInstanceProvenanceType(InstanceProvenanceType.LOCAL_COHORT);
            }

            /*
             * OK to send out
             */
            if (outboundRepositoryEventProcessor != null)
            {
                outboundRepositoryEventProcessor.processNewRelationshipEvent(repositoryName,
                                                                             metadataCollectionId,
                                                                             localServerName,
                                                                             localServerType,
                                                                             localOrganizationName,
                                                                             relationship);
            }
        }

        return relationship;
    }


    /**
     * Update the status of a specific relationship.
     *
     * @param userId unique identifier for requesting user.
     * @param relationshipGUID String unique identifier (guid) for the relationship.
     * @param newStatus new InstanceStatus for the relationship.
     * @return Resulting relationship structure with the new status set.
     * @throws InvalidParameterException one of the parameters is invalid or null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                  the metadata collection is stored.
     * @throws RelationshipNotKnownException the requested relationship is not known in the metadata collection.
     * @throws StatusNotSupportedException the metadata repository hosting the metadata collection does not support
     *                                     the requested status.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    public Relationship updateRelationshipStatus(String           userId,
                                                 String           relationshipGUID,
                                                 InstanceStatus   newStatus) throws InvalidParameterException,
                                                                                    RepositoryErrorException,
                                                                                    RelationshipNotKnownException,
                                                                                    StatusNotSupportedException,
                                                                                    UserNotAuthorizedException
    {
        final String  methodName          = "updateRelationshipStatus";
        final String  guidParameterName   = "relationshipGUID";

        /*
         * Validate parameters
         */
        this.validateRepositoryConnector(methodName);
        parentConnector.validateRepositoryIsActive(methodName);

        repositoryValidator.validateUserId(repositoryName, userId, methodName);
        repositoryValidator.validateGUID(repositoryName, guidParameterName, relationshipGUID, methodName);

        /*
         * Locate relationship
         */
        Relationship   currentRelationship = realMetadataCollection.getRelationship(userId, relationshipGUID);
        Relationship   newRelationship     = realMetadataCollection.updateRelationshipStatus(userId,
                                                                                             relationshipGUID,
                                                                                             newStatus);
        notifyOfUpdatedRelationship(currentRelationship, newRelationship);

        return newRelationship;
    }


    /**
     * Update the properties of a specific relationship.
     *
     * @param userId unique identifier for requesting user.
     * @param relationshipGUID String unique identifier (guid) for the relationship.
     * @param properties list of the properties to update.
     * @return Resulting relationship structure with the new properties set.
     * @throws InvalidParameterException one of the parameters is invalid or null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                  the metadata collection is stored.
     * @throws RelationshipNotKnownException the requested relationship is not known in the metadata collection.
     * @throws PropertyErrorException one or more of the requested properties are not defined, or have different
     *                                characteristics in the TypeDef for this relationship's type.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    public Relationship updateRelationshipProperties(String               userId,
                                                     String               relationshipGUID,
                                                     InstanceProperties   properties) throws InvalidParameterException,
                                                                                             RepositoryErrorException,
                                                                                             RelationshipNotKnownException,
                                                                                             PropertyErrorException,
                                                                                             UserNotAuthorizedException
    {
        final String  methodName = "updateRelationshipProperties";
        final String  guidParameterName = "relationshipGUID";

        /*
         * Validate parameters
         */
        this.validateRepositoryConnector(methodName);
        parentConnector.validateRepositoryIsActive(methodName);

        repositoryValidator.validateUserId(repositoryName, userId, methodName);
        repositoryValidator.validateGUID(repositoryName, guidParameterName, relationshipGUID, methodName);

        /*
         * Locate relationship
         */
        Relationship   currentRelationship = realMetadataCollection.getRelationship(userId, relationshipGUID);
        Relationship   newRelationship = realMetadataCollection.updateRelationshipProperties(userId,
                                                                                          relationshipGUID,
                                                                                          properties);

        notifyOfUpdatedRelationship(currentRelationship, newRelationship);

        return newRelationship;
    }


    /**
     * Send out a notification that a relationship has changed.
     *
     * @param oldRelationship original relationship
     * @param newRelationship changed relationship
     */
    private void notifyOfUpdatedRelationship(Relationship  oldRelationship,
                                             Relationship  newRelationship)
    {
        if (newRelationship != null)
        {
            /*
             * Ensure the provenance of the relationship is correctly set.  A repository may not support the storing of
             * the metadata collection id in the repository (or uses null to mean "local").  When the relationship
             * detail is sent out, it must have its home metadata collection id set up.  So LocalOMRSMetadataCollection
             * fixes up the provenance.
             */
            if (newRelationship.getMetadataCollectionId() == null)
            {
                newRelationship.setMetadataCollectionId(metadataCollectionId);
                newRelationship.setInstanceProvenanceType(InstanceProvenanceType.LOCAL_COHORT);
            }

            /*
             * OK to send out
             */
            if (outboundRepositoryEventProcessor != null)
            {
                outboundRepositoryEventProcessor.processUpdatedRelationshipEvent(repositoryName,
                                                                                 metadataCollectionId,
                                                                                 localServerName,
                                                                                 localServerType,
                                                                                 localOrganizationName,
                                                                                 oldRelationship,
                                                                                 newRelationship);
            }
        }
    }


    /**
     * Undo the latest change to a relationship (either a change of properties or status).
     *
     * @param userId unique identifier for requesting user.
     * @param relationshipGUID String unique identifier (guid) for the relationship.
     * @return Relationship structure with the new current header, requested entities and properties.
     * @throws InvalidParameterException the guid is null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                  the metadata collection is stored.
     * @throws RelationshipNotKnownException the requested relationship is not known in the metadata collection.
     * @throws FunctionNotSupportedException the repository does not support undo.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    public Relationship undoRelationshipUpdate(String    userId,
                                               String    relationshipGUID) throws InvalidParameterException,
                                                                                  RepositoryErrorException,
                                                                                  RelationshipNotKnownException,
                                                                                  FunctionNotSupportedException,
                                                                                  UserNotAuthorizedException
    {
        final String  methodName = "undoRelationshipUpdate";
        final String  guidParameterName = "relationshipGUID";

        /*
         * Validate parameters
         */
        this.validateRepositoryConnector(methodName);
        parentConnector.validateRepositoryIsActive(methodName);

        repositoryValidator.validateUserId(repositoryName, userId, methodName);
        repositoryValidator.validateGUID(repositoryName, guidParameterName, relationshipGUID, methodName);

        /*
         * Restore previous version
         */

        Relationship   relationship = realMetadataCollection.undoRelationshipUpdate(userId,
                                                                                    relationshipGUID);

        if (relationship != null)
        {
            /*
             * Ensure the provenance of the relationship is correctly set.  A repository may not support the storing of
             * the metadata collection id in the repository (or uses null to mean "local").  When the relationship
             * detail is sent out, it must have its home metadata collection id set up.  So LocalOMRSMetadataCollection
             * fixes up the provenance.
             */
            if (relationship.getMetadataCollectionId() == null)
            {
                relationship.setMetadataCollectionId(metadataCollectionId);
                relationship.setInstanceProvenanceType(InstanceProvenanceType.LOCAL_COHORT);
            }

            /*
             * OK to send out
             */
            if (outboundRepositoryEventProcessor != null)
            {
                outboundRepositoryEventProcessor.processUndoneRelationshipEvent(repositoryName,
                                                                                metadataCollectionId,
                                                                                localServerName,
                                                                                localServerType,
                                                                                localOrganizationName,
                                                                                relationship);
            }
        }

        return relationship;
    }

    /**
     * Delete a specific relationship.  This is a soft-delete which means the relationship's status is updated to
     * DELETED and it is no longer available for queries.  To remove the relationship permanently from the
     * metadata collection, use purgeRelationship().
     *
     * @param userId unique identifier for requesting user.
     * @param typeDefGUID unique identifier of the type of the relationship to delete.
     * @param typeDefName unique name of the type of the relationship to delete.
     * @param obsoleteRelationshipGUID String unique identifier (guid) for the relationship.
     * @return deleted relationship
     * @throws InvalidParameterException one of the parameters is null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                    the metadata collection is stored.
     * @throws RelationshipNotKnownException the requested relationship is not known in the metadata collection.
     * @throws FunctionNotSupportedException the metadata repository hosting the metadata collection does not support
     *                                     soft-deletes.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    public Relationship deleteRelationship(String userId,
                                           String typeDefGUID,
                                           String typeDefName,
                                           String obsoleteRelationshipGUID) throws InvalidParameterException,
                                                                                   RepositoryErrorException,
                                                                                   RelationshipNotKnownException,
                                                                                   FunctionNotSupportedException,
                                                                                   UserNotAuthorizedException
    {
        final String  methodName = "deleteRelationship";
        final String  guidParameterName = "typeDefGUID";
        final String  nameParameterName = "typeDefName";
        final String  relationshipParameterName = "obsoleteRelationshipGUID";

        /*
         * Validate parameters
         */
        this.validateRepositoryConnector(methodName);
        parentConnector.validateRepositoryIsActive(methodName);

        repositoryValidator.validateUserId(repositoryName, userId, methodName);
        repositoryValidator.validateGUID(repositoryName, relationshipParameterName, obsoleteRelationshipGUID, methodName);
        repositoryValidator.validateTypeDefIds(repositoryName,
                                               guidParameterName,
                                               nameParameterName,
                                               typeDefGUID,
                                               typeDefName,
                                               methodName);

        /*
         * Delete relationship
         */

        Relationship relationship = realMetadataCollection.deleteRelationship(userId,
                                                                              typeDefGUID,
                                                                              typeDefName,
                                                                              obsoleteRelationshipGUID);

        if (outboundRepositoryEventProcessor != null)
        {
            outboundRepositoryEventProcessor.processDeletedRelationshipEvent(repositoryName,
                                                                             metadataCollectionId,
                                                                             localServerName,
                                                                             localServerType,
                                                                             localOrganizationName,
                                                                             relationship);
        }

        return relationship;
    }


    /**
     * Permanently delete the relationship from the repository.  There is no means to undo this request.
     *
     * @param userId unique identifier for requesting user.
     * @param typeDefGUID unique identifier of the type of the relationship to purge.
     * @param typeDefName unique name of the type of the relationship to purge.
     * @param deletedRelationshipGUID String unique identifier (guid) for the relationship.
     * @throws InvalidParameterException one of the parameters is null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                    the metadata collection is stored.
     * @throws RelationshipNotKnownException the requested relationship is not known in the metadata collection.
     * @throws RelationshipNotDeletedException the requested relationship is not in DELETED status.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    public void purgeRelationship(String    userId,
                                  String    typeDefGUID,
                                  String    typeDefName,
                                  String    deletedRelationshipGUID) throws InvalidParameterException,
                                                                            RepositoryErrorException,
                                                                            RelationshipNotKnownException,
                                                                            RelationshipNotDeletedException,
                                                                            UserNotAuthorizedException
    {
        final String  methodName = "purgeRelationship";
        final String  guidParameterName = "typeDefGUID";
        final String  nameParameterName = "typeDefName";
        final String  relationshipParameterName = "deletedRelationshipGUID";

        /*
         * Validate parameters
         */
        this.validateRepositoryConnector(methodName);
        parentConnector.validateRepositoryIsActive(methodName);

        repositoryValidator.validateUserId(repositoryName, userId, methodName);
        repositoryValidator.validateGUID(repositoryName, relationshipParameterName, deletedRelationshipGUID, methodName);
        repositoryValidator.validateTypeDefIds(repositoryName,
                                               guidParameterName,
                                               nameParameterName,
                                               typeDefGUID,
                                               typeDefName,
                                               methodName);

        /*
         * Locate relationship
         */

        realMetadataCollection.purgeRelationship(userId, typeDefGUID, typeDefName, deletedRelationshipGUID);

        if (outboundRepositoryEventProcessor != null)
        {
            outboundRepositoryEventProcessor.processPurgedRelationshipEvent(repositoryName,
                                                                            metadataCollectionId,
                                                                            localServerName,
                                                                            localServerType,
                                                                            localOrganizationName,
                                                                            typeDefGUID,
                                                                            typeDefName,
                                                                            deletedRelationshipGUID);
        }
    }


    /**
     * Restore a deleted relationship into the metadata collection.  The new status will be ACTIVE and the
     * restored details of the relationship are returned to the caller.
     *
     * @param userId unique identifier for requesting user.
     * @param deletedRelationshipGUID String unique identifier (guid) for the relationship.
     * @return Relationship structure with the restored header, requested entities and properties.
     * @throws InvalidParameterException the guid is null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     * the metadata collection is stored.
     * @throws RelationshipNotKnownException the requested relationship is not known in the metadata collection.
     * @throws RelationshipNotDeletedException the requested relationship is not in DELETED status.
     * @throws FunctionNotSupportedException the repository does not support soft-deletes.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    public Relationship restoreRelationship(String    userId,
                                            String    deletedRelationshipGUID) throws InvalidParameterException,
                                                                                      RepositoryErrorException,
                                                                                      RelationshipNotKnownException,
                                                                                      RelationshipNotDeletedException,
                                                                                      FunctionNotSupportedException,
                                                                                      UserNotAuthorizedException
    {
        final String  methodName = "restoreRelationship";
        final String  guidParameterName = "deletedRelationshipGUID";

        /*
         * Validate parameters
         */
        this.validateRepositoryConnector(methodName);
        parentConnector.validateRepositoryIsActive(methodName);

        repositoryValidator.validateUserId(repositoryName, userId, methodName);
        repositoryValidator.validateGUID(repositoryName, guidParameterName, deletedRelationshipGUID, methodName);

        /*
         * Locate relationship
         */

        Relationship   relationship = realMetadataCollection.restoreRelationship(userId, deletedRelationshipGUID);

        if (relationship != null)
        {
            /*
             * Ensure the provenance of the relationship is correctly set.  A repository may not support the storing of
             * the metadata collection id in the repository (or uses null to mean "local").  When the relationship
             * detail is sent out, it must have its home metadata collection id set up.  So LocalOMRSMetadataCollection
             * fixes up the provenance.
             */
            if (relationship.getMetadataCollectionId() == null)
            {
                relationship.setMetadataCollectionId(metadataCollectionId);
                relationship.setInstanceProvenanceType(InstanceProvenanceType.LOCAL_COHORT);
            }

            /*
             * OK to send out
             */
            if (outboundRepositoryEventProcessor != null)
            {
                outboundRepositoryEventProcessor.processRestoredRelationshipEvent(repositoryName,
                                                                                  metadataCollectionId,
                                                                                  localServerName,
                                                                                  localServerType,
                                                                                  localOrganizationName,
                                                                                  relationship);
            }
        }

        return relationship;
    }


    /* ======================================================================
     * Group 5: Change the control information in entities and relationships
     */


    /**
     * Change the guid of an existing entity to a new value.  This is used if two different
     * entities are discovered to have the same guid.  This is extremely unlikely but not impossible so
     * the open metadata protocol has provision for this.
     *
     * @param userId unique identifier for requesting user.
     * @param typeDefGUID the guid of the TypeDef for the entity used to verify the entity identity.
     * @param typeDefName the name of the TypeDef for the entity used to verify the entity identity.
     * @param entityGUID the existing identifier for the entity.
     * @param newEntityGUID the new guid for the entity.
     * @return entity new values for this entity, including the new guid.
     * @throws InvalidParameterException one of the parameters is invalid or null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                    the metadata collection is stored.
     * @throws EntityNotKnownException the entity identified by the guid is not found in the metadata collection.
     * @throws FunctionNotSupportedException the repository does not support the re-identification of instances.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    public EntityDetail reIdentifyEntity(String    userId,
                                         String    typeDefGUID,
                                         String    typeDefName,
                                         String    entityGUID,
                                         String    newEntityGUID) throws InvalidParameterException,
                                                                         RepositoryErrorException,
                                                                         EntityNotKnownException,
                                                                         FunctionNotSupportedException,
                                                                         UserNotAuthorizedException
    {
        final String  methodName = "reIdentifyEntity";
        final String  guidParameterName = "typeDefGUID";
        final String  nameParameterName = "typeDefName";
        final String  instanceParameterName = "deletedRelationshipGUID";

        /*
         * Validate parameters
         */
        this.validateRepositoryConnector(methodName);
        parentConnector.validateRepositoryIsActive(methodName);

        parentConnector.validateRepositoryIsActive(methodName);
        repositoryValidator.validateUserId(repositoryName, userId, methodName);
        repositoryValidator.validateGUID(repositoryName, instanceParameterName, newEntityGUID, methodName);
        repositoryValidator.validateTypeDefIds(repositoryName,
                                               guidParameterName,
                                               nameParameterName,
                                               typeDefGUID,
                                               typeDefName,
                                               methodName);

        /*
         * Update entity
         */

        EntityDetail   entity = realMetadataCollection.reIdentifyEntity(userId,
                                                                        typeDefGUID,
                                                                        typeDefName,
                                                                        entityGUID,
                                                                        newEntityGUID);

        if (entity != null)
        {
            /*
             * Ensure the provenance of the entity is correctly set.  A repository may not support the storing of
             * the metadata collection id in the repository (or uses null to mean "local").  When the entity
             * detail is sent out, it must have its home metadata collection id set up.  So LocalOMRSMetadataCollection
             * fixes up the provenance.
             */
            if (entity.getMetadataCollectionId() == null)
            {
                entity.setMetadataCollectionId(metadataCollectionId);
                entity.setInstanceProvenanceType(InstanceProvenanceType.LOCAL_COHORT);
            }

            /*
             * OK to send out
             */
            if (outboundRepositoryEventProcessor != null)
            {
                outboundRepositoryEventProcessor.processReIdentifiedEntityEvent(repositoryName,
                                                                                metadataCollectionId,
                                                                                localServerName,
                                                                                localServerType,
                                                                                localOrganizationName,
                                                                                entityGUID,
                                                                                entity);
            }
        }

        return entity;
    }


    /**
     * Change the type of an existing entity.  Typically this action is taken to move an entity's
     * type to either a super type (so the subtype can be deleted) or a new subtype (so additional properties can be
     * added.)  However, the type can be changed to any compatible type and the properties adjusted.
     *
     * @param userId unique identifier for requesting user.
     * @param entityGUID the unique identifier for the entity to change.
     * @param currentTypeDefSummary the current details of the TypeDef for the entity used to verify the entity identity
     * @param newTypeDefSummary details of this entity's new TypeDef.
     * @return entity new values for this entity, including the new type information.
     * @throws InvalidParameterException one of the parameters is invalid or null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                    the metadata collection is stored.
     * @throws TypeErrorException the requested type is not known, or not supported in the metadata repository
     *                            hosting the metadata collection.
     * @throws PropertyErrorException The properties in the instance are incompatible with the requested type.
     * @throws ClassificationErrorException the entity's classifications are not valid for the new type.
     * @throws EntityNotKnownException the entity identified by the guid is not found in the metadata collection.     *
     * @throws FunctionNotSupportedException the repository does not support the re-typing of instances.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    public EntityDetail reTypeEntity(String         userId,
                                     String         entityGUID,
                                     TypeDefSummary currentTypeDefSummary,
                                     TypeDefSummary newTypeDefSummary) throws InvalidParameterException,
                                                                              RepositoryErrorException,
                                                                              TypeErrorException,
                                                                              PropertyErrorException,
                                                                              ClassificationErrorException,
                                                                              EntityNotKnownException,
                                                                              FunctionNotSupportedException,
                                                                              UserNotAuthorizedException
    {
        final String  methodName = "reTypeEntity";
        final String  entityParameterName = "entityGUID";

        /*
         * Validate parameters
         */
        this.validateRepositoryConnector(methodName);
        parentConnector.validateRepositoryIsActive(methodName);

        repositoryValidator.validateUserId(repositoryName, userId, methodName);
        repositoryValidator.validateGUID(repositoryName, entityParameterName, entityGUID, methodName);

        /*
         * Update entity
         */

        EntityDetail   entity = realMetadataCollection.reTypeEntity(userId,
                                                                    entityGUID,
                                                                    currentTypeDefSummary,
                                                                    newTypeDefSummary);

        if (entity != null)
        {
            /*
             * Ensure the provenance of the entity is correctly set.  A repository may not support the storing of
             * the metadata collection id in the repository (or uses null to mean "local").  When the entity
             * detail is sent out, it must have its home metadata collection id set up.  So LocalOMRSMetadataCollection
             * fixes up the provenance.
             */
            if (entity.getMetadataCollectionId() == null)
            {
                entity.setMetadataCollectionId(metadataCollectionId);
                entity.setInstanceProvenanceType(InstanceProvenanceType.LOCAL_COHORT);
            }

            /*
             * OK to send out
             */
            if (outboundRepositoryEventProcessor != null)
            {
                outboundRepositoryEventProcessor.processReTypedEntityEvent(repositoryName,
                                                                           metadataCollectionId,
                                                                           localServerName,
                                                                           localServerType,
                                                                           localOrganizationName,
                                                                           currentTypeDefSummary,
                                                                           entity);
            }
        }

        return entity;
    }


    /**
     * Change the home of an existing entity.  This action is taken for example, if the original home repository
     * becomes permanently unavailable, or if the user community updating this entity move to working
     * from a different repository in the open metadata repository cohort.
     *
     * @param userId unique identifier for requesting user.
     * @param entityGUID the unique identifier for the entity to change.
     * @param typeDefGUID the guid of the TypeDef for the entity used to verify the entity identity.
     * @param typeDefName the name of the TypeDef for the entity used to verify the entity identity.
     * @param homeMetadataCollectionId the identifier of the metadata collection where this entity currently is homed.
     * @param newHomeMetadataCollectionId unique identifier for the new home metadata collection/repository.
     * @return entity new values for this entity, including the new home information.
     * @throws InvalidParameterException one of the parameters is invalid or null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                    the metadata collection is stored.
     * @throws EntityNotKnownException the entity identified by the guid is not found in the metadata collection.
     * @throws FunctionNotSupportedException the repository does not support the re-homing of instances.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    public EntityDetail reHomeEntity(String    userId,
                                     String    entityGUID,
                                     String    typeDefGUID,
                                     String    typeDefName,
                                     String    homeMetadataCollectionId,
                                     String    newHomeMetadataCollectionId) throws InvalidParameterException,
                                                                                   RepositoryErrorException,
                                                                                   EntityNotKnownException,
                                                                                   FunctionNotSupportedException,
                                                                                   UserNotAuthorizedException
    {
        final String methodName                = "reHomeEntity";
        final String guidParameterName         = "typeDefGUID";
        final String nameParameterName         = "typeDefName";
        final String entityParameterName       = "entityGUID";
        final String homeParameterName         = "homeMetadataCollectionId";
        final String newHomeParameterName      = "newHomeMetadataCollectionId";

        /*
         * Validate parameters
         */
        this.validateRepositoryConnector(methodName);
        parentConnector.validateRepositoryIsActive(methodName);

        repositoryValidator.validateUserId(repositoryName, userId, methodName);
        repositoryValidator.validateGUID(repositoryName, entityParameterName, entityGUID, methodName);
        repositoryValidator.validateTypeDefIds(repositoryName,
                                               guidParameterName,
                                               nameParameterName,
                                               typeDefGUID,
                                               typeDefName,
                                               methodName);
        repositoryValidator.validateHomeMetadataGUID(repositoryName, homeParameterName, homeMetadataCollectionId, methodName);
        repositoryValidator.validateHomeMetadataGUID(repositoryName, newHomeParameterName, newHomeMetadataCollectionId, methodName);

        /*
         * Update entity
         */

        EntityDetail   entity = realMetadataCollection.reHomeEntity(userId,
                                                                    entityGUID,
                                                                    typeDefGUID,
                                                                    typeDefName,
                                                                    homeMetadataCollectionId,
                                                                    newHomeMetadataCollectionId);

        if (entity != null)
        {
            /*
             * Ensure the provenance of the entity is correctly set.  A repository may not support the storing of
             * the metadata collection id in the repository (or uses null to mean "local").  When the entity
             * detail is sent out, it must have its home metadata collection id set up.  So LocalOMRSMetadataCollection
             * fixes up the provenance.
             */
            if (entity.getMetadataCollectionId() == null)
            {
                entity.setMetadataCollectionId(metadataCollectionId);
                entity.setInstanceProvenanceType(InstanceProvenanceType.LOCAL_COHORT);
            }

            /*
             * OK to send out
             */
            if (outboundRepositoryEventProcessor != null)
            {
                outboundRepositoryEventProcessor.processReHomedEntityEvent(repositoryName,
                                                                           metadataCollectionId,
                                                                           localServerName,
                                                                           localServerType,
                                                                           localOrganizationName,
                                                                           metadataCollectionId,
                                                                           entity);
            }
        }

        return entity;
    }


    /**
     * Change the guid of an existing relationship.  This is used if two different
     * relationships are discovered to have the same guid.  This is extremely unlikely but not impossible so
     * the open metadata protocol has provision for this.
     *
     * @param userId unique identifier for requesting user.
     * @param typeDefGUID the guid of the TypeDef for the relationship used to verify the relationship identity.
     * @param typeDefName the name of the TypeDef for the relationship used to verify the relationship identity.
     * @param relationshipGUID the existing identifier for the relationship.
     * @param newRelationshipGUID the new identifier for the relationship.
     * @return relationship new values for this relationship, including the new guid.
     * @throws InvalidParameterException one of the parameters is invalid or null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                    the metadata collection is stored.
     * @throws RelationshipNotKnownException the relationship identified by the guid is not found in the
     *                                         metadata collection.
     * @throws FunctionNotSupportedException the repository does not support the re-identification of instances.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    public Relationship reIdentifyRelationship(String    userId,
                                               String    typeDefGUID,
                                               String    typeDefName,
                                               String    relationshipGUID,
                                               String    newRelationshipGUID) throws InvalidParameterException,
                                                                                     RepositoryErrorException,
                                                                                     RelationshipNotKnownException,
                                                                                     FunctionNotSupportedException,
                                                                                     UserNotAuthorizedException
    {
        final String methodName                   = "reIdentifyRelationship";
        final String guidParameterName            = "typeDefGUID";
        final String nameParameterName            = "typeDefName";
        final String relationshipParameterName    = "relationshipGUID";
        final String newRelationshipParameterName = "newHomeMetadataCollectionId";

        /*
         * Validate parameters
         */
        this.validateRepositoryConnector(methodName);
        parentConnector.validateRepositoryIsActive(methodName);

        repositoryValidator.validateUserId(repositoryName, userId, methodName);
        repositoryValidator.validateGUID(repositoryName, relationshipParameterName, relationshipGUID, methodName);
        repositoryValidator.validateTypeDefIds(repositoryName,
                                               guidParameterName,
                                               nameParameterName,
                                               typeDefGUID,
                                               typeDefName,
                                               methodName);
        repositoryValidator.validateGUID(repositoryName, newRelationshipParameterName, newRelationshipGUID, methodName);

        /*
         * Validation complete, ok to make changes
         */

        Relationship   relationship = realMetadataCollection.reIdentifyRelationship(userId,
                                                                                    typeDefGUID,
                                                                                    typeDefName,
                                                                                    relationshipGUID,
                                                                                    newRelationshipGUID);

        if (relationship != null)
        {
            /*
             * Ensure the provenance of the relationship is correctly set.  A repository may not support the storing of
             * the metadata collection id in the repository (or uses null to mean "local").  When the relationship
             * detail is sent out, it must have its home metadata collection id set up.  So LocalOMRSMetadataCollection
             * fixes up the provenance.
             */
            if (relationship.getMetadataCollectionId() == null)
            {
                relationship.setMetadataCollectionId(metadataCollectionId);
                relationship.setInstanceProvenanceType(InstanceProvenanceType.LOCAL_COHORT);
            }

            /*
             * OK to send out
             */
            if (outboundRepositoryEventProcessor != null)
            {
                outboundRepositoryEventProcessor.processReIdentifiedRelationshipEvent(repositoryName,
                                                                                      metadataCollectionId,
                                                                                      localServerName,
                                                                                      localServerType,
                                                                                      localOrganizationName,
                                                                                      relationshipGUID,
                                                                                      relationship);
            }
        }

        return relationship;
    }


    /**
     * Change the type of an existing relationship.  Typically this action is taken to move a relationship's
     * type to either a super type (so the subtype can be deleted) or a new subtype (so additional properties can be
     * added.)  However, the type can be changed to any compatible type.
     *
     * @param userId unique identifier for requesting user.
     * @param relationshipGUID the unique identifier for the relationship.
     * @param currentTypeDefSummary the details of the TypeDef for the relationship used to verify the relationship identity.
     * @param newTypeDefSummary new details for this relationship's TypeDef.
     * @return relationship new values for this relationship, including the new type information.
     * @throws InvalidParameterException one of the parameters is invalid or null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                    the metadata collection is stored.
     * @throws TypeErrorException the requested type is not known, or not supported in the metadata repository
     *                            hosting the metadata collection.
     * @throws PropertyErrorException The properties in the instance are incompatible with the requested type.
     * @throws RelationshipNotKnownException the relationship identified by the guid is not found in the
     *                                         metadata collection.
     * @throws FunctionNotSupportedException the repository does not support the re-typing of instances.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    public Relationship reTypeRelationship(String         userId,
                                           String         relationshipGUID,
                                           TypeDefSummary currentTypeDefSummary,
                                           TypeDefSummary newTypeDefSummary) throws InvalidParameterException,
                                                                                    RepositoryErrorException,
                                                                                    TypeErrorException,
                                                                                    PropertyErrorException,
                                                                                    RelationshipNotKnownException,
                                                                                    FunctionNotSupportedException,
                                                                                    UserNotAuthorizedException
    {
        final String methodName = "reTypeRelationship";
        final String relationshipParameterName = "relationshipGUID";
        final String currentTypeDefParameterName = "currentTypeDefSummary";

        /*
         * Validate parameters
         */
        this.validateRepositoryConnector(methodName);
        parentConnector.validateRepositoryIsActive(methodName);

        repositoryValidator.validateUserId(repositoryName, userId, methodName);
        repositoryValidator.validateGUID(repositoryName, relationshipParameterName, relationshipGUID, methodName);
        repositoryValidator.validateType(repositoryName, currentTypeDefParameterName, currentTypeDefSummary, TypeDefCategory.RELATIONSHIP_DEF, methodName);
        repositoryValidator.validateType(repositoryName, currentTypeDefParameterName, newTypeDefSummary, TypeDefCategory.RELATIONSHIP_DEF, methodName);

        /*
         * Validation complete, ok to make changes
         */

        Relationship   relationship = realMetadataCollection.reTypeRelationship(userId,
                                                                                relationshipGUID,
                                                                                currentTypeDefSummary,
                                                                                newTypeDefSummary);

        if (relationship != null)
        {
            /*
             * Ensure the provenance of the relationship is correctly set.  A repository may not support the storing of
             * the metadata collection id in the repository (or uses null to mean "local").  When the relationship
             * detail is sent out, it must have its home metadata collection id set up.  So LocalOMRSMetadataCollection
             * fixes up the provenance.
             */
            if (relationship.getMetadataCollectionId() == null)
            {
                relationship.setMetadataCollectionId(metadataCollectionId);
                relationship.setInstanceProvenanceType(InstanceProvenanceType.LOCAL_COHORT);
            }

            /*
             * OK to send out
             */
            if (outboundRepositoryEventProcessor != null)
            {
                outboundRepositoryEventProcessor.processReTypedRelationshipEvent(repositoryName,
                                                                                 metadataCollectionId,
                                                                                 localServerName,
                                                                                 localServerType,
                                                                                 localOrganizationName,
                                                                                 currentTypeDefSummary,
                                                                                 relationship);
            }
        }

        return relationship;
    }


    /**
     * Change the home of an existing relationship.  This action is taken for example, if the original home repository
     * becomes permanently unavailable, or if the user community updating this relationship move to working
     * from a different repository in the open metadata repository cohort.
     *
     * @param userId unique identifier for requesting user.
     * @param relationshipGUID the unique identifier for the relationship.
     * @param typeDefGUID the guid of the TypeDef for the relationship used to verify the relationship identity.
     * @param typeDefName the name of the TypeDef for the relationship used to verify the relationship identity.
     * @param homeMetadataCollectionId the existing identifier for this relationship's home.
     * @param newHomeMetadataCollectionId unique identifier for the new home metadata collection/repository.
     * @return relationship new values for this relationship, including the new home information.
     * @throws InvalidParameterException one of the parameters is invalid or null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                    the metadata collection is stored.
     * @throws RelationshipNotKnownException the relationship identified by the guid is not found in the
     *                                         metadata collection.
     * @throws FunctionNotSupportedException the repository does not support the re-homing of instances.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    public Relationship reHomeRelationship(String    userId,
                                           String    relationshipGUID,
                                           String    typeDefGUID,
                                           String    typeDefName,
                                           String    homeMetadataCollectionId,
                                           String    newHomeMetadataCollectionId) throws InvalidParameterException,
                                                                                         RepositoryErrorException,
                                                                                         RelationshipNotKnownException,
                                                                                         FunctionNotSupportedException,
                                                                                         UserNotAuthorizedException
    {
        final String  methodName               = "reHomeRelationship";
        final String guidParameterName         = "typeDefGUID";
        final String nameParameterName         = "typeDefName";
        final String relationshipParameterName = "relationshipGUID";
        final String homeParameterName         = "homeMetadataCollectionId";
        final String newHomeParameterName      = "newHomeMetadataCollectionId";

        /*
         * Validate parameters
         */
        this.validateRepositoryConnector(methodName);
        parentConnector.validateRepositoryIsActive(methodName);

        repositoryValidator.validateUserId(repositoryName, userId, methodName);
        repositoryValidator.validateGUID(repositoryName, relationshipParameterName, relationshipGUID, methodName);
        repositoryValidator.validateTypeDefIds(repositoryName,
                                               guidParameterName,
                                               nameParameterName,
                                               typeDefGUID,
                                               typeDefName,
                                               methodName);
        repositoryValidator.validateHomeMetadataGUID(repositoryName, homeParameterName, homeMetadataCollectionId, methodName);
        repositoryValidator.validateHomeMetadataGUID(repositoryName, newHomeParameterName, newHomeMetadataCollectionId, methodName);

        /*
         * Update relationship
         */

        Relationship   relationship = realMetadataCollection.reHomeRelationship(userId,
                                                                                relationshipGUID,
                                                                                typeDefGUID,
                                                                                typeDefName,
                                                                                homeMetadataCollectionId,
                                                                                newHomeMetadataCollectionId);

        if (relationship != null)
        {
            /*
             * Ensure the provenance of the relationship is correctly set.  A repository may not support the storing of
             * the metadata collection id in the repository (or uses null to mean "local").  When the relationship
             * detail is sent out, it must have its home metadata collection id set up.  So LocalOMRSMetadataCollection
             * fixes up the provenance.
             */
            if (relationship.getMetadataCollectionId() == null)
            {
                relationship.setMetadataCollectionId(metadataCollectionId);
                relationship.setInstanceProvenanceType(InstanceProvenanceType.LOCAL_COHORT);
            }

            /*
             * OK to send out
             */
            if (outboundRepositoryEventProcessor != null)
            {
                outboundRepositoryEventProcessor.processReHomedRelationshipEvent(repositoryName,
                                                                                 metadataCollectionId,
                                                                                 localServerName,
                                                                                 localServerType,
                                                                                 localOrganizationName,
                                                                                 homeMetadataCollectionId,
                                                                                 relationship);
            }
        }

        return relationship;
    }



    /* ======================================================================
     * Group 6: Local house-keeping of reference metadata instances
     */


    /**
     * Save the entity as a reference copy.  The id of the home metadata collection is already set up in the
     * entity.
     *
     * @param userId unique identifier for requesting server.
     * @param entity details of the entity to save
     * @throws InvalidParameterException the entity is null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                    the metadata collection is stored.
     * @throws TypeErrorException the requested type is not known, or not supported in the metadata repository
     *                            hosting the metadata collection.
     * @throws PropertyErrorException one or more of the requested properties are not defined, or have different
     *                                  characteristics in the TypeDef for this entity's type.
     * @throws HomeEntityException the entity belongs to the local repository so creating a reference
     *                               copy would be invalid.
     * @throws EntityConflictException the new entity conflicts with an existing entity.
     * @throws InvalidEntityException the new entity has invalid contents.
     * @throws FunctionNotSupportedException the repository does not support reference copies of instances.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    public void saveEntityReferenceCopy(String userId,
                                        EntityDetail   entity) throws InvalidParameterException,
                                                                      RepositoryErrorException,
                                                                      TypeErrorException,
                                                                      PropertyErrorException,
                                                                      HomeEntityException,
                                                                      EntityConflictException,
                                                                      InvalidEntityException,
                                                                      FunctionNotSupportedException,
                                                                      UserNotAuthorizedException
    {
        final String  methodName = "saveEntityReferenceCopy";

        /*
         * Validate parameters
         */
        this.validateRepositoryConnector(methodName);
        parentConnector.validateRepositoryIsActive(methodName);

        realMetadataCollection.saveEntityReferenceCopy(userId, entity);
    }


    /**
     * Remove a reference copy of the the entity from the local repository.  This method can be used to
     * remove reference copies from the local cohort, repositories that have left the cohort,
     * or entities that have come from open metadata archives.
     *
     * @param userId unique identifier for requesting server.
     * @param entityGUID the unique identifier for the entity.
     * @param typeDefGUID the guid of the TypeDef for the relationship used to verify the relationship identity.
     * @param typeDefName the name of the TypeDef for the relationship used to verify the relationship identity.
     * @param homeMetadataCollectionId identifier of the metadata collection that is the home to this entity.
     * @throws InvalidParameterException one of the parameters is invalid or null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                    the metadata collection is stored.
     * @throws EntityNotKnownException the entity identified by the guid is not found in the metadata collection.
     * @throws HomeEntityException the entity belongs to the local repository so creating a reference
     *                               copy would be invalid.
     * @throws FunctionNotSupportedException the repository does not support reference copies of instances.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    public void purgeEntityReferenceCopy(String userId,
                                         String    entityGUID,
                                         String    typeDefGUID,
                                         String    typeDefName,
                                         String    homeMetadataCollectionId) throws InvalidParameterException,
                                                                                    RepositoryErrorException,
                                                                                    EntityNotKnownException,
                                                                                    HomeEntityException,
                                                                                    FunctionNotSupportedException,
                                                                                    UserNotAuthorizedException
    {
        final String  methodName               = "purgeEntityReferenceCopy";
        final String guidParameterName         = "typeDefGUID";
        final String nameParameterName         = "typeDefName";
        final String entityParameterName       = "entityGUID";
        final String homeParameterName         = "homeMetadataCollectionId";

        /*
         * Validate parameters
         */
        this.validateRepositoryConnector(methodName);
        parentConnector.validateRepositoryIsActive(methodName);

        repositoryValidator.validateGUID(repositoryName, entityParameterName, entityGUID, methodName);
        repositoryValidator.validateTypeDefIds(repositoryName,
                                               guidParameterName,
                                               nameParameterName,
                                               typeDefGUID,
                                               typeDefName,
                                               methodName);
        repositoryValidator.validateHomeMetadataGUID(repositoryName, homeParameterName, homeMetadataCollectionId, methodName);

        /*
         * Remove entity
         */

        realMetadataCollection.purgeEntityReferenceCopy(userId,
                                                        entityGUID,
                                                        typeDefGUID,
                                                        typeDefName,
                                                        homeMetadataCollectionId);
    }


    /**
     * The local repository has requested that the repository that hosts the home metadata collection for the
     * specified entity sends out the details of this entity so the local repository can create a reference copy.
     *
     * @param userId unique identifier for requesting server.
     * @param entityGUID unique identifier of requested entity
     * @param typeDefGUID unique identifier of requested entity's TypeDef
     * @param typeDefName unique name of requested entity's TypeDef
     * @param homeMetadataCollectionId identifier of the metadata collection that is the home to this entity.
     * @throws InvalidParameterException one of the parameters is invalid or null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                    the metadata collection is stored.
     * @throws EntityNotKnownException the entity identified by the guid is not found in the metadata collection.
     * @throws HomeEntityException the entity belongs to the local repository so creating a reference
     *                               copy would be invalid.
     * @throws FunctionNotSupportedException the repository does not support reference copies of instances.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    public void refreshEntityReferenceCopy(String userId,
                                           String    entityGUID,
                                           String    typeDefGUID,
                                           String    typeDefName,
                                           String    homeMetadataCollectionId) throws InvalidParameterException,
                                                                                      RepositoryErrorException,
                                                                                      EntityNotKnownException,
                                                                                      HomeEntityException,
                                                                                      FunctionNotSupportedException,
                                                                                      UserNotAuthorizedException
    {
        final String methodName                = "refreshEntityReferenceCopy";
        final String guidParameterName         = "typeDefGUID";
        final String nameParameterName         = "typeDefName";
        final String entityParameterName       = "entityGUID";
        final String homeParameterName         = "homeMetadataCollectionId";

        /*
         * Validate parameters
         */
        this.validateRepositoryConnector(methodName);
        parentConnector.validateRepositoryIsActive(methodName);

        repositoryValidator.validateGUID(repositoryName, entityParameterName, entityGUID, methodName);
        repositoryValidator.validateTypeDefIds(repositoryName,
                                               guidParameterName,
                                               nameParameterName,
                                               typeDefGUID,
                                               typeDefName,
                                               methodName);
        repositoryValidator.validateHomeMetadataGUID(repositoryName, homeParameterName, homeMetadataCollectionId, methodName);

        /*
         * Send refresh message
         */
        // todo note real repository is not involved
    }


    /**
     * Save the relationship as a reference copy.  The id of the home metadata collection is already set up in the
     * relationship.
     *
     * @param userId unique identifier for requesting server.
     * @param relationship relationship to save
     * @throws InvalidParameterException the relationship is null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                    the metadata collection is stored.
     * @throws TypeErrorException the requested type is not known, or not supported in the metadata repository
     *                            hosting the metadata collection.
     * @throws EntityNotKnownException one of the entities identified by the relationship is not found in the
     *                                   metadata collection.
     * @throws PropertyErrorException one or more of the requested properties are not defined, or have different
     *                                  characteristics in the TypeDef for this relationship's type.
     * @throws HomeRelationshipException the relationship belongs to the local repository so creating a reference
     *                                     copy would be invalid.
     * @throws RelationshipConflictException the new relationship conflicts with an existing relationship.
     * @throws InvalidRelationshipException the new relationship has invalid contents.
     * @throws FunctionNotSupportedException the repository does not support reference copies of instances.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    public void saveRelationshipReferenceCopy(String userId,
                                              Relationship   relationship) throws InvalidParameterException,
                                                                                  RepositoryErrorException,
                                                                                  TypeErrorException,
                                                                                  EntityNotKnownException,
                                                                                  PropertyErrorException,
                                                                                  HomeRelationshipException,
                                                                                  RelationshipConflictException,
                                                                                  InvalidRelationshipException,
                                                                                  FunctionNotSupportedException,
                                                                                  UserNotAuthorizedException
    {
        final String  methodName = "saveRelationshipReferenceCopy";
        final String  instanceParameterName = "relationship";

        /*
         * Validate parameters
         */
        this.validateRepositoryConnector(methodName);
        parentConnector.validateRepositoryIsActive(methodName);

        repositoryValidator.validateReferenceInstanceHeader(repositoryName,
                                                            metadataCollectionId,
                                                            instanceParameterName,
                                                            relationship,
                                                            methodName);

        /*
         * Update relationship
         */

        realMetadataCollection.saveRelationshipReferenceCopy(userId, relationship);
    }


    /**
     * Remove the reference copy of the relationship from the local repository. This method can be used to
     * remove reference copies from the local cohort, repositories that have left the cohort,
     * or relationships that have come from open metadata archives.
     *
     * @param userId unique identifier for requesting serverName.
     * @param relationshipGUID the unique identifier for the relationship.
     * @param typeDefGUID the guid of the TypeDef for the relationship used to verify the relationship identity.
     * @param typeDefName the name of the TypeDef for the relationship used to verify the relationship identity.
     * @param homeMetadataCollectionId unique identifier for the home repository for this relationship.
     * @throws InvalidParameterException one of the parameters is invalid or null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                    the metadata collection is stored.
     * @throws RelationshipNotKnownException the relationship identifier is not recognized.
     * @throws HomeRelationshipException the relationship belongs to the local repository so creating a reference
     *                                     copy would be invalid.
     * @throws FunctionNotSupportedException the repository does not support reference copies of instances.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    public void purgeRelationshipReferenceCopy(String userId,
                                               String    relationshipGUID,
                                               String    typeDefGUID,
                                               String    typeDefName,
                                               String    homeMetadataCollectionId) throws InvalidParameterException,
                                                                                          RepositoryErrorException,
                                                                                          RelationshipNotKnownException,
                                                                                          HomeRelationshipException,
                                                                                          FunctionNotSupportedException,
                                                                                          UserNotAuthorizedException
    {
        final String methodName                = "purgeRelationshipReferenceCopy";
        final String guidParameterName         = "typeDefGUID";
        final String nameParameterName         = "typeDefName";
        final String relationshipParameterName = "relationshipGUID";
        final String homeParameterName         = "homeMetadataCollectionId";

        /*
         * Validate parameters
         */
        this.validateRepositoryConnector(methodName);
        parentConnector.validateRepositoryIsActive(methodName);

        repositoryValidator.validateGUID(repositoryName, relationshipParameterName, relationshipGUID, methodName);
        repositoryValidator.validateTypeDefIds(repositoryName,
                                               guidParameterName,
                                               nameParameterName,
                                               typeDefGUID,
                                               typeDefName,
                                               methodName);
        repositoryValidator.validateHomeMetadataGUID(repositoryName, homeParameterName, homeMetadataCollectionId, methodName);


        /*
         * Purge relationship
         */

        realMetadataCollection.purgeRelationshipReferenceCopy(userId,
                                                              relationshipGUID,
                                                              typeDefGUID,
                                                              typeDefName,
                                                              homeMetadataCollectionId);
    }


    /**
     * The local repository has requested that the repository that hosts the home metadata collection for the
     * specified relationship sends out the details of this relationship so the local repository can create a
     * reference copy.
     *
     * @param userId unique identifier for requesting server.
     * @param relationshipGUID unique identifier of the relationship
     * @param typeDefGUID the guid of the TypeDef for the relationship used to verify the relationship identity.
     * @param typeDefName the name of the TypeDef for the relationship used to verify the relationship identity.
     * @param homeMetadataCollectionId unique identifier for the home repository for this relationship.
     * @throws InvalidParameterException one of the parameters is invalid or null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                    the metadata collection is stored.
     * @throws RelationshipNotKnownException the relationship identifier is not recognized.
     * @throws HomeRelationshipException the relationship belongs to the local repository so creating a reference
     *                                     copy would be invalid.
     * @throws FunctionNotSupportedException the repository does not support reference copies of instances.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    public void refreshRelationshipReferenceCopy(String userId,
                                                 String    relationshipGUID,
                                                 String    typeDefGUID,
                                                 String    typeDefName,
                                                 String    homeMetadataCollectionId) throws InvalidParameterException,
                                                                                            RepositoryErrorException,
                                                                                            RelationshipNotKnownException,
                                                                                            HomeRelationshipException,
                                                                                            FunctionNotSupportedException,
                                                                                            UserNotAuthorizedException
    {
        final String methodName                = "refreshRelationshipReferenceCopy";
        final String guidParameterName         = "typeDefGUID";
        final String nameParameterName         = "typeDefName";
        final String relationshipParameterName = "relationshipGUID";
        final String homeParameterName         = "homeMetadataCollectionId";

        /*
         * Validate parameters
         */
        this.validateRepositoryConnector(methodName);
        parentConnector.validateRepositoryIsActive(methodName);

        repositoryValidator.validateGUID(repositoryName, relationshipParameterName, relationshipGUID, methodName);
        repositoryValidator.validateTypeDefIds(repositoryName,
                                               guidParameterName,
                                               nameParameterName,
                                               typeDefGUID,
                                               typeDefName,
                                               methodName);
        repositoryValidator.validateHomeMetadataGUID(repositoryName, homeParameterName, homeMetadataCollectionId, methodName);

        /*
         * Process refresh request
         */
        // todo not sure this logic is right

        if (outboundRepositoryEventProcessor == null)
        {
            realMetadataCollection.refreshRelationshipReferenceCopy(userId,
                                                                    relationshipGUID,
                                                                    typeDefGUID,
                                                                    typeDefName,
                                                                    homeMetadataCollectionId);
        }
        else
        {
            if (homeMetadataCollectionId.equals(metadataCollectionId))
            {
                Relationship   relationship = realMetadataCollection.getRelationship(userId, relationshipGUID);

                outboundRepositoryEventProcessor.processRefreshRelationshipEvent(repositoryName,
                                                                                 metadataCollectionId,
                                                                                 localServerName,
                                                                                 localServerType,
                                                                                 localOrganizationName,
                                                                                 relationship);
            }
            else
            {
                outboundRepositoryEventProcessor.processRefreshRelationshipRequest(repositoryName,
                                                                                   metadataCollectionId,
                                                                                   localServerName,
                                                                                   localServerType,
                                                                                   localOrganizationName,
                                                                                   typeDefGUID,
                                                                                   typeDefName,
                                                                                   relationshipGUID,
                                                                                   homeMetadataCollectionId );
            }
        }

    }
}
