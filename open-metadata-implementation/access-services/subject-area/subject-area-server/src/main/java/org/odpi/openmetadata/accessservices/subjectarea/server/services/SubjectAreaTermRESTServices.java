/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.server.services;


import org.odpi.openmetadata.accessservices.subjectarea.ffdc.SubjectAreaErrorCode;
import org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.*;
import org.odpi.openmetadata.accessservices.subjectarea.generated.entities.GlossaryTerm.GlossaryTerm;
import org.odpi.openmetadata.accessservices.subjectarea.generated.entities.GlossaryTerm.GlossaryTermReferences;
import org.odpi.openmetadata.accessservices.subjectarea.generated.entities.Referenceable.Referenceable;
import org.odpi.openmetadata.accessservices.subjectarea.generated.references.GlossaryTermToGlossary.AnchorReference;
import org.odpi.openmetadata.accessservices.subjectarea.generated.references.GlossaryTermToGlossaryCategory.CategoriesReference;
import org.odpi.openmetadata.accessservices.subjectarea.generated.references.GlossaryTermToReferenceable.AssignedElementsReference;
import org.odpi.openmetadata.accessservices.subjectarea.generated.references.ReferenceableToRelatedMedia.RelatedMediaReference;
import org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.TermAnchor.TermAnchor;
import org.odpi.openmetadata.accessservices.subjectarea.generated.server.SubjectAreaBeansToAccessOMRS;
import org.odpi.openmetadata.accessservices.subjectarea.properties.classifications.Classification;
import org.odpi.openmetadata.accessservices.subjectarea.properties.enums.Status;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.glossary.Glossary;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.line.Line;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.node.NodeType;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.nodesummary.*;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.term.Term;
import org.odpi.openmetadata.accessservices.subjectarea.properties.relationships.RelatedTermRelationship;
import org.odpi.openmetadata.accessservices.subjectarea.properties.relationships.Antonym;
import org.odpi.openmetadata.accessservices.subjectarea.properties.relationships.Synonym;
import org.odpi.openmetadata.accessservices.subjectarea.properties.relationships.TermHASARelationship;
import org.odpi.openmetadata.accessservices.subjectarea.responses.*;
import org.odpi.openmetadata.accessservices.subjectarea.server.mappers.entities.TermMapper;
import org.odpi.openmetadata.accessservices.subjectarea.server.mappers.relationships.*;
import org.odpi.openmetadata.accessservices.subjectarea.utilities.SubjectAreaUtils;
import org.odpi.openmetadata.accessservices.subjectarea.validators.InputValidator;
import org.odpi.openmetadata.accessservices.subjectarea.validators.RestValidator;
import org.odpi.openmetadata.repositoryservices.archivemanager.OMRSArchiveAccessor;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceGraph;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceStatus;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.EntityDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.RelationshipDef;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;


/**
 * TheSubjectAreaTermRESTServices provides the server-side implementation of the SubjectAreaDefinition Open Metadata
 * Access Service (OMAS) for Terms.  This interface provides term authoring interfaces for subject area experts.
 */

public class SubjectAreaTermRESTServices extends SubjectAreaRESTServices
{


    private static final Logger log = LoggerFactory.getLogger(SubjectAreaTermRESTServices.class);

    private static final String className = SubjectAreaTermRESTServices.class.getName();

    /**
     * Default constructor
     */
    public SubjectAreaTermRESTServices()
    {
        //SubjectAreaRESTServices registers this omas.
    }

    /**
     * Create a Term
     * <p>
     * The name needs to be specified - as this is the main identifier for the term. The name should be unique for canonical glossaries. This API does not police the uniqueness in this case.
     * <p>
     * The qualifiedName can be specified and will be honoured. If it is specified then the caller may wish to ensure that it is
     * unique. If this qualifiedName is not specified then one will be generated as GlossaryTerm concatinated with the the guid.
     * <p>
     * Failure to create the Terms classifications, link to its glossary or its icon, results in the create failing and the term being deleted
     *
     * @param userId       userId
     * @param suppliedTerm term to create
     * @return response, when successful contains the created term.
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service.
     * <li> InvalidParameterException            one of the parameters is null or invalid.
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised
     * <li> ClassificationException              Error processing a classification
     * <li> FunctionNotSupportedException        Function not supported
     * <li> StatusNotSupportedException          A status value is not supported
     * </ul>
     */
    public SubjectAreaOMASAPIResponse createTerm(String userId, Term suppliedTerm)
    {
        final String methodName = "createTerm";
        if (log.isDebugEnabled())
        {
            log.debug("==> Method: " + methodName + ",userId=" + userId);
        }
        SubjectAreaOMASAPIResponse response = null;
        GlossaryTerm glossaryTerm = null;
        Glossary associatedGlossary = null;
        try
        {
            InputValidator.validateUserIdNotNull(className, methodName, userId);
            InputValidator.validateNodeType(className, methodName, suppliedTerm.getNodeType(), NodeType.Term);
        } catch (InvalidParameterException e)
        {
            response = OMASExceptionToResponse.convertInvalidParameterException(e);
        }
        SubjectAreaBeansToAccessOMRS service = new SubjectAreaBeansToAccessOMRS();
        SubjectAreaGlossaryRESTServices glossaryRESTServices = new SubjectAreaGlossaryRESTServices();
        if (response == null)
        {
            service.setOMRSAPIHelper(this.oMRSAPIHelper);
            SubjectAreaTermRESTServices termRESTServices = new SubjectAreaTermRESTServices();
            termRESTServices.setOMRSAPIHelper(this.oMRSAPIHelper);

            glossaryRESTServices.setOMRSAPIHelper(this.oMRSAPIHelper);

            try
            {
                glossaryTerm = TermMapper.mapTermToOMRSBean(suppliedTerm);
            } catch (InvalidParameterException e)
            {
                response = OMASExceptionToResponse.convertInvalidParameterException(e);
            }
        }
        // need to check we have a name
        final String suppliedTermName = suppliedTerm.getName();
        if (response == null && (suppliedTermName == null || suppliedTermName.equals("")))
        {
            SubjectAreaErrorCode errorCode = SubjectAreaErrorCode.GLOSSARY_TERM_CREATE_WITHOUT_NAME;
            String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(className, methodName);
            log.error(errorMessage);
            InvalidParameterException e = new InvalidParameterException(errorCode.getHTTPErrorCode(), className, methodName, errorMessage, errorCode.getSystemAction(), errorCode.getUserAction());
            response = OMASExceptionToResponse.convertInvalidParameterException(e);
        }

        // should we remove this restriction?
        if (response == null && (suppliedTerm.getCategories() != null && !suppliedTerm.getCategories().isEmpty()))
        {
            SubjectAreaErrorCode errorCode = SubjectAreaErrorCode.GLOSSARY_TERM_CREATE_WITH_CATEGORIES;
            String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(className, methodName, suppliedTermName);
            log.error(errorMessage);
            InvalidParameterException e = new InvalidParameterException(errorCode.getHTTPErrorCode(), className, methodName, errorMessage, errorCode.getSystemAction(), errorCode.getUserAction());
            response = OMASExceptionToResponse.convertInvalidParameterException(e);

        }
        // should we remove this restriction?
        if (response == null && (suppliedTerm.getProjects() != null && !suppliedTerm.getProjects().isEmpty()))
        {
            SubjectAreaErrorCode errorCode = SubjectAreaErrorCode.GLOSSARY_TERM_CREATE_WITH_PROJECTS;
            String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(className, methodName, suppliedTermName);
            log.error(errorMessage);
            InvalidParameterException e = new InvalidParameterException(errorCode.getHTTPErrorCode(), className, methodName, errorMessage, errorCode.getSystemAction(), errorCode.getUserAction());
            response = OMASExceptionToResponse.convertInvalidParameterException(e);

        }
        // We do not allow terms to be associated with assets in this OMAS
        // there is no setAssets on Term - but in theory a Term returned from a get could be fed into a create - which should be rejected.
        if (response == null && (suppliedTerm.getAssets() != null && !suppliedTerm.getAssets().isEmpty()))
        {
            SubjectAreaErrorCode errorCode = SubjectAreaErrorCode.GLOSSARY_TERM_CREATE_WITH_ASSETS;
            String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(className, methodName, suppliedTermName);
            log.error(errorMessage);
            InvalidParameterException e = new InvalidParameterException(errorCode.getHTTPErrorCode(), className, methodName, errorMessage, errorCode.getSystemAction(), errorCode.getUserAction());
            response = OMASExceptionToResponse.convertInvalidParameterException(e);

        }
        GlossarySummary suppliedGlossary = suppliedTerm.getGlossary();
        if (response == null)
        {

            SubjectAreaOMASAPIResponse glossaryResponse = RestValidator.validateGlossarySummaryDuringCreation(methodName, suppliedGlossary, glossaryRESTServices, userId);
            if (glossaryResponse.getResponseCategory().equals(ResponseCategory.Category.Glossary))
            {
                // store the associated glossary
                associatedGlossary = ((GlossaryResponse) glossaryResponse).getGlossary();
            } else
            {
                // error
                response = glossaryResponse;
            }

        }
        GlossaryTerm newGlossaryTerm = null;
        List<Classification> classifications = new ArrayList<>();
        String termGuid = null;

        if (response == null)
        {
            try
            {
                newGlossaryTerm = service.createGlossaryTerm(userId, glossaryTerm);
                termGuid = newGlossaryTerm.getSystemAttributes().getGUID();
            } catch (InvalidParameterException e)
            {
                response = OMASExceptionToResponse.convertInvalidParameterException(e);
            } catch (UserNotAuthorizedException e)
            {
                response = OMASExceptionToResponse.convertUserNotAuthorizedException(e);
            } catch (MetadataServerUncontactableException e)
            {
                response = OMASExceptionToResponse.convertMetadataServerUncontactableException(e);
            } catch (ClassificationException e)
            {
                response = OMASExceptionToResponse.convertClassificationException(e);
            } catch (StatusNotSupportedException e)
            {
                response = OMASExceptionToResponse.convertStatusNotsupportedException(e);
            }
        }
        if (response == null)
        {
            // Knit the Term to the supplied glossary
            String glossaryGuid = associatedGlossary.getSystemAttributes().getGUID();
            TermAnchor termAnchor = new TermAnchor();
            termAnchor.setEntity1Guid(glossaryGuid);
            termAnchor.setEntity2Guid(termGuid);

            try
            {
                service.createTermAnchorRelationship(userId, termAnchor);
            } catch (InvalidParameterException e)
            {
                response = OMASExceptionToResponse.convertInvalidParameterException(e);
            } catch (UserNotAuthorizedException e)
            {
                response = OMASExceptionToResponse.convertUserNotAuthorizedException(e);
            } catch (MetadataServerUncontactableException e)
            {
                response = OMASExceptionToResponse.convertMetadataServerUncontactableException(e);
            } catch (UnrecognizedGUIDException e)
            {
                response = OMASExceptionToResponse.convertUnrecognizedGUIDException(e);
            } catch (StatusNotSupportedException e)
            {
                response = OMASExceptionToResponse.convertStatusNotSupportedException(e);
            }
        }
        if (response == null)
        {
            // We could perform other relationship creation here. I suggest not - and we encourage users to use relationship creation API
            response = getTermByGuid(userId, termGuid);
        }
        if (log.isDebugEnabled())
        {
            log.debug("<== successful method : " + methodName + ",userId=" + userId + ", response=" + response);
        }
        return response;
    }

    /**
     * Get a Term
     *
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the term to get
     * @return response which when successful contains the term with the requested guid
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * <li> FunctionNotSupportedException        Function not supported</li>
     * </ul>
     */

    public SubjectAreaOMASAPIResponse getTermByGuid(String userId, String guid)
    {
        final String methodName = "getTermByGuid";
        if (log.isDebugEnabled())
        {
            log.debug("==> Method: " + methodName + ",userId=" + userId + ",guid=" + guid);
        }
        SubjectAreaOMASAPIResponse response = null;
        try
        {
            InputValidator.validateUserIdNotNull(className, methodName, userId);
            InputValidator.validateGUIDNotNull(className, methodName, guid, "guid");
        } catch (InvalidParameterException e)
        {
            response = OMASExceptionToResponse.convertInvalidParameterException(e);
        }
        if (response == null)
        {
            SubjectAreaBeansToAccessOMRS subjectAreaOmasREST = new SubjectAreaBeansToAccessOMRS();
            subjectAreaOmasREST.setOMRSAPIHelper(this.oMRSAPIHelper);
            GlossaryTerm glossaryTerm = null;
            try
            {
                glossaryTerm = subjectAreaOmasREST.getGlossaryTermById(userId, guid);
                Term gotTerm = TermMapper.mapOMRSBeantoTerm(glossaryTerm);
                List<Classification> classifications = glossaryTerm.getClassifications();
                // set the GlossaryTerm classifications into the Node
                gotTerm.setClassifications(classifications);

                Set<Line> termRelationships = subjectAreaOmasREST.getGlossaryTermRelationships(userId, guid);
                GlossaryTermReferences glossaryTermReferences = new GlossaryTermReferences(guid, termRelationships);
                if (response == null)
                {
                    // set icon
                    Set<RelatedMediaReference> relatedMediaReferenceSet = glossaryTermReferences.getRelatedMediaReferences();
                    Set<IconSummary> icons = SubjectAreaUtils.getIconSummaries(userId, relatedMediaReferenceSet);
                    if (icons != null)
                    {
                        gotTerm.setIcons(icons);
                    }
                    // set assets
                    Set<AssetSummary> assets = new HashSet();
                    Set<AssignedElementsReference> assignedElementsReferences = glossaryTermReferences.getAssignedElementsReferences();
                    if (assignedElementsReferences != null && !assignedElementsReferences.isEmpty())
                    {
                        for (AssignedElementsReference assignedElementsReference : assignedElementsReferences)
                        {
                            Referenceable referenceable = assignedElementsReference.getReferenceable();
                            AssetSummary asset = new AssetSummary();
                            asset.setGuid(referenceable.getSystemAttributes().getGUID());
                            asset.setQualifiedName(referenceable.getQualifiedName());
                            assets.add(asset);
                        }
                        gotTerm.setAssets(assets);
                    }

                    // set categories
                    Set<CategorySummary> categories = new HashSet();
                    Set<CategoriesReference> categoriesReferences = glossaryTermReferences.getCategoriesReferences();
                    if (categoriesReferences != null && !categoriesReferences.isEmpty())
                    {
                        for (CategoriesReference categoryReference : categoriesReferences)
                        {
                            CategorySummary category = new CategorySummary();
                            category.setGuid(categoryReference.getRelatedEndGuid());
                            category.setRelationshipguid(categoryReference.getRelationshipGuid());
                            category.setQualifiedName(categoryReference.getGlossaryCategory().getQualifiedName());
                            category.setName(categoryReference.getGlossaryCategory().getDisplayName());
                            categories.add(category);
                        }

                        gotTerm.setCategories(categories);
                    }


                    // set terms
//                    Set<TermSummary> terms = glossaryTermReferences.getTermSummaries();
//                    gotTerm.setTerms(terms);


                    AnchorReference anchorReference = glossaryTermReferences.getAnchorReference();
                    if (anchorReference != null)
                    {

                        //get the glossary - we need this for the name and qualified name
                        org.odpi.openmetadata.accessservices.subjectarea.generated.entities.Glossary.Glossary glossary = subjectAreaOmasREST.getGlossaryById(userId, anchorReference.getRelatedEndGuid());
                        // set glossary summary
                        GlossarySummary glossarySummary = new GlossarySummary();
                        glossarySummary.setName(glossary.getDisplayName());
                        glossarySummary.setQualifiedName(glossary.getQualifiedName());
                        glossarySummary.setGuid(anchorReference.getRelatedEndGuid());
                        glossarySummary.setRelationshipguid(anchorReference.getRelationshipGuid());
                        glossarySummary.setRelationshipType(anchorReference.getRelationship_Type());
                        gotTerm.setGlossary(glossarySummary);
                    }

                    response = new TermResponse(gotTerm);
                }
            } catch (InvalidParameterException e)
            {
                response = OMASExceptionToResponse.convertInvalidParameterException(e);
            } catch (UserNotAuthorizedException e)
            {
                response = OMASExceptionToResponse.convertUserNotAuthorizedException(e);
            } catch (MetadataServerUncontactableException e)
            {
                response = OMASExceptionToResponse.convertMetadataServerUncontactableException(e);
            } catch (UnrecognizedGUIDException e)
            {
                response = OMASExceptionToResponse.convertUnrecognizedGUIDException(e);
            } catch (FunctionNotSupportedException e)
            {
                response = OMASExceptionToResponse.convertFunctionNotSupportedException(e);
            }
        }

        if (log.isDebugEnabled())
        {
            log.debug("<== successful method : " + methodName + ",userId=" + userId + ", Response=" + response);
        }
        return response;
    }

    /**
     * Update a Term
     * <p>
     * Status is not updated using this call.
     *
     * @param userId       userId under which the request is performed
     * @param guid         guid of the term to update
     * @param suppliedTerm term to be updated
     * @param isReplace    flag to indicate that this update is a replace. When not set only the supplied (non null) fields are updated.
     * @return a response which when successful contains the updated term
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> FunctionNotSupportedException        Function not supported</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service.</li>
     * </ul>
     */
    public SubjectAreaOMASAPIResponse updateTerm(String userId, String guid, Term suppliedTerm, boolean isReplace)
    {
        final String methodName = "updateTerm";
        if (log.isDebugEnabled())
        {
            log.debug("==> Method: " + methodName + ",userId=" + userId);
        }
        SubjectAreaOMASAPIResponse response = null;
        try
        {
            InputValidator.validateUserIdNotNull(className, methodName, userId);
            InputValidator.validateNodeType(className, methodName, suppliedTerm.getNodeType(), NodeType.Term);
            InputValidator.validateGUIDNotNull(className, methodName, guid, "guid");
        } catch (InvalidParameterException e)
        {
            response = OMASExceptionToResponse.convertInvalidParameterException(e);
        }
        if (response == null)
        {
            SubjectAreaBeansToAccessOMRS service = new SubjectAreaBeansToAccessOMRS();
            service.setOMRSAPIHelper(this.oMRSAPIHelper);
            response = getTermByGuid(userId, guid);
            if (response.getResponseCategory().equals(ResponseCategory.Term))
            {
                org.odpi.openmetadata.accessservices.subjectarea.properties.objects.term.Term originalTerm = ((TermResponse) response).getTerm();
                if (originalTerm.getSystemAttributes() != null)
                {
                    Status status = originalTerm.getSystemAttributes().getStatus();
                    SubjectAreaOMASAPIResponse deleteCheckResponse = SubjectAreaUtils.checkStatusNotDeleted(status, SubjectAreaErrorCode.GLOSSARY_UPDATE_FAILED_ON_DELETED_GLOSSARY);
                    if (deleteCheckResponse != null)
                    {
                        response = deleteCheckResponse;
                    }
                }
                if (suppliedTerm.getSystemAttributes() != null)
                {
                    Status status = suppliedTerm.getSystemAttributes().getStatus();
                    SubjectAreaOMASAPIResponse deleteCheckResponse = SubjectAreaUtils.checkStatusNotDeleted(status, SubjectAreaErrorCode.STATUS_UPDATE_TO_DELETED_NOT_ALLOWED);
                    if (deleteCheckResponse != null)
                    {
                        response = deleteCheckResponse;
                    }
                }
                if (response != null)
                {
                    Term updateTerm = originalTerm;
                    if (isReplace)
                    {
                        // copy over attributes
                        updateTerm.setName(suppliedTerm.getName());
                        updateTerm.setQualifiedName(suppliedTerm.getQualifiedName());
                        updateTerm.setDescription(suppliedTerm.getDescription());
                        updateTerm.setUsage(suppliedTerm.getUsage());
                        updateTerm.setAbbreviation(suppliedTerm.getAbbreviation());
                        updateTerm.setExamples(suppliedTerm.getExamples());
                        updateTerm.setSpineObject(suppliedTerm.isSpineObject());
                        updateTerm.setSpineAttribute(suppliedTerm.isSpineAttribute());
                        updateTerm.setObjectIdentifier(suppliedTerm.isObjectIdentifier());
                        updateTerm.setAdditionalProperties(suppliedTerm.getAdditionalProperties());
                        //TODO handle governance classifications and other classifications
                    } else
                    {
                        // copy over attributes if specified
                        if (suppliedTerm.getName() != null)
                        {
                            updateTerm.setName(suppliedTerm.getName());
                        }
                        if (suppliedTerm.getQualifiedName() != null)
                        {
                            updateTerm.setQualifiedName(suppliedTerm.getQualifiedName());
                        }
                        if (suppliedTerm.getDescription() != null)
                        {
                            updateTerm.setDescription(suppliedTerm.getDescription());
                        }
                        if (suppliedTerm.getUsage() != null)
                        {
                            updateTerm.setUsage(suppliedTerm.getUsage());
                        }
                        if (suppliedTerm.getAbbreviation() != null)
                        {
                            updateTerm.setAbbreviation(suppliedTerm.getAbbreviation());
                        }
                        if (suppliedTerm.getAdditionalProperties() != null)
                        {
                            updateTerm.setAdditionalProperties(suppliedTerm.getAdditionalProperties());
                        }
                        if (suppliedTerm.getExamples()!=null)
                        {
                            updateTerm.setExamples(suppliedTerm.getExamples());
                        }
                        if (suppliedTerm.isSpineObject())
                        {
                            updateTerm.setSpineObject(suppliedTerm.isSpineObject());
                        }
                        if (suppliedTerm.isSpineObject())
                        {
                            updateTerm.setSpineAttribute(suppliedTerm.isSpineAttribute());
                        }
                        if (suppliedTerm.isObjectIdentifier())
                        {
                            updateTerm.setObjectIdentifier(suppliedTerm.isObjectIdentifier());
                        }
                        //TODO handle governance classifications and other classifications
                    }
                    org.odpi.openmetadata.accessservices.subjectarea.generated.entities.GlossaryTerm.GlossaryTerm generatedTerm = null;
                    try
                    {
                        generatedTerm = TermMapper.mapTermToOMRSBean(updateTerm);
                        org.odpi.openmetadata.accessservices.subjectarea.generated.entities.GlossaryTerm.GlossaryTerm updatedGeneratedTerm = null;
                        try
                        {
                            updatedGeneratedTerm = service.updateGlossaryTerm(userId, generatedTerm);
                        } catch (MetadataServerUncontactableException e)
                        {
                            response = OMASExceptionToResponse.convertMetadataServerUncontactableException(e);
                        } catch (UserNotAuthorizedException e)
                        {
                            response = OMASExceptionToResponse.convertUserNotAuthorizedException(e);
                        } catch (UnrecognizedGUIDException e)
                        {
                            response = OMASExceptionToResponse.convertUnrecognizedGUIDException(e);
                        }
                        org.odpi.openmetadata.accessservices.subjectarea.properties.objects.term.Term updatedTerm = TermMapper.mapOMRSBeantoTerm(updatedGeneratedTerm);
                        response = new TermResponse(updatedTerm);
                    } catch (InvalidParameterException e)
                    {
                        response = OMASExceptionToResponse.convertInvalidParameterException(e);
                    }
                }
            }
        }
        if (log.isDebugEnabled())
        {
            log.debug("<== successful method : " + methodName + ",userId=" + userId + ",response=" + response);
        }
        return response;

    }


    /**
     * Delete a Term instance
     * <p>
     * There are 2 types of deletion, a soft delete and a hard delete (also known as a purge). All repositories support hard deletes. Soft deletes support
     * is optional. Soft delete is the default.
     * <p>
     * A soft delete means that the term instance will exist in a deleted state in the repository after the delete operation. This means
     * that it is possible to undo the delete.
     * A hard delete means that the term will not exist after the operation.
     * when not successful the following Exception responses can occur
     *
     * @param userId  userId under which the request is performed
     * @param guid    guid of the term to be deleted.
     * @param isPurge true indicates a hard delete, false is a soft delete.
     * @return a void response
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> FunctionNotSupportedException        Function not supported this indicates that a soft delete was issued but the repository does not support it.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service. There is a problem retrieving properties from the metadata repository.</li>
     * <li> EntityNotDeletedException            a soft delete was issued but the term was not deleted.</li>
     * <li> GUIDNotPurgedException               a hard delete was issued but the term was not purged</li>
     * </ul>
     */
    public SubjectAreaOMASAPIResponse deleteTerm(String userId, String guid, Boolean isPurge)
    {
        final String methodName = "deleteTerm";
        if (log.isDebugEnabled())
        {
            log.debug("==> Method: " + methodName + ",userId=" + userId + ", guid=" + guid);
        }
        SubjectAreaOMASAPIResponse response = null;
        try
        {
            InputValidator.validateUserIdNotNull(className, methodName, userId);
            InputValidator.validateGUIDNotNull(className, methodName, guid, "guid");
        } catch (InvalidParameterException e)
        {
            response = OMASExceptionToResponse.convertInvalidParameterException(e);
        }
        if (response == null)
        {
            SubjectAreaBeansToAccessOMRS service = new SubjectAreaBeansToAccessOMRS();
            service.setOMRSAPIHelper(oMRSAPIHelper);
            if (isPurge)
            {
                try
                {
                    service.purgeGlossaryTermByGuid(userId, guid);
                    response = new VoidResponse();
                } catch (MetadataServerUncontactableException e)
                {
                    response = OMASExceptionToResponse.convertMetadataServerUncontactableException(e);
                } catch (UserNotAuthorizedException e)
                {
                    response = OMASExceptionToResponse.convertUserNotAuthorizedException(e);
                } catch (InvalidParameterException e)
                {
                    response = OMASExceptionToResponse.convertInvalidParameterException(e);
                } catch (UnrecognizedGUIDException e)
                {
                    response = OMASExceptionToResponse.convertUnrecognizedGUIDException(e);
                } catch (EntityNotDeletedException e)
                {
                    response = OMASExceptionToResponse.convertEntityNotDeletedException(e);
                } catch (GUIDNotPurgedException e)
                {
                    response = OMASExceptionToResponse.convertGUIDNotPurgedException(e);
                }

            } else
            {
                org.odpi.openmetadata.accessservices.subjectarea.generated.entities.GlossaryTerm.GlossaryTerm deletedGeneratedTerm = null;
                try
                {
                    EntityDetail entityDetail = service.deleteGlossaryTermByGuid(userId, guid);
                    deletedGeneratedTerm = org.odpi.openmetadata.accessservices.subjectarea.generated.entities.GlossaryTerm.GlossaryTermMapper.mapOmrsEntityDetailToGlossaryTerm(entityDetail);
                    org.odpi.openmetadata.accessservices.subjectarea.properties.objects.term.Term deletedTerm = TermMapper.mapOMRSBeantoTerm(deletedGeneratedTerm);
                    response = new TermResponse(deletedTerm);
                } catch (MetadataServerUncontactableException e)
                {
                    response = OMASExceptionToResponse.convertMetadataServerUncontactableException(e);
                } catch (UserNotAuthorizedException e)
                {
                    response = OMASExceptionToResponse.convertUserNotAuthorizedException(e);
                } catch (FunctionNotSupportedException e)
                {
                    response = OMASExceptionToResponse.convertFunctionNotSupportedException(e);
                } catch (InvalidParameterException e)
                {
                    response = OMASExceptionToResponse.convertInvalidParameterException(e);
                } catch (UnrecognizedGUIDException e)
                {
                    response = OMASExceptionToResponse.convertUnrecognizedGUIDException(e);
                }
            }
        }
        if (log.isDebugEnabled())
        {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
        return response;
    }
}