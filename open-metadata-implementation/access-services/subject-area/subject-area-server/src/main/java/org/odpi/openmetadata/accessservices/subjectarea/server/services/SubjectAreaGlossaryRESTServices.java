/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.odpi.openmetadata.accessservices.subjectarea.server.services;

import org.odpi.openmetadata.accessservices.subjectarea.generated.server.SubjectAreaBeansToAccessOMRS;
import org.odpi.openmetadata.accessservices.subjectarea.properties.enums.Status;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.glossary.Glossary;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.line.Line;
import org.odpi.openmetadata.accessservices.subjectarea.responses.*;
import org.odpi.openmetadata.accessservices.subjectarea.server.mappers.GlossaryMapper;
import org.odpi.openmetadata.accessservices.subjectarea.utilities.SubjectAreaUtils;
import org.odpi.openmetadata.accessservices.subjectarea.ffdc.SubjectAreaErrorCode;
import org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.*;
import org.odpi.openmetadata.accessservices.subjectarea.generated.entities.Glossary.GlossaryReferences;
import org.odpi.openmetadata.accessservices.subjectarea.generated.references.ReferenceableToRelatedMedia.RelatedMediaReference;


import org.odpi.openmetadata.accessservices.subjectarea.validators.InputValidator;
import org.odpi.openmetadata.accessservices.subjectarea.validators.RestValidator;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.MatchCriteria;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Classification;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.PrimitiveDefCategory;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;


/**
 * The SubjectAreaRESTServices provides the org.odpi.openmetadata.accessservices.subjectarea.server-side implementation of the SubjectArea Open Metadata
 * Assess Service (OMAS).  This interface provides glossary authoring interfaces for subject area experts.
 */

public class SubjectAreaGlossaryRESTServices extends SubjectAreaRESTServices {
    // these guids need to match the archive types. I did not want to introduce a dependancy on the archive types to get them.
    public static final String GLOSSARY_TYPE_GUID = "36f66863-9726-4b41-97ee-714fd0dc6fe4";
    public static final String TERM_ANCHOR_RELATIONSHIP_GUID = "1d43d661-bdc7-4a91-a996-3239b8f82e56";
    public static final String CATEGORY_ANCHOR_RELATIONSHIP_GUID = "c628938e-815e-47db-8d1c-59bb2e84e028";
    public static final String CATEGORY_HIERARCHY_LINK_GUID = "71e4b6fb-3412-4193-aff3-a16eccd87e8e";

    static private String accessServiceName = null;
    static private OMRSRepositoryConnector repositoryConnector = null;

    private static final Logger log = LoggerFactory.getLogger(SubjectAreaGlossaryRESTServices.class);

    private static final String className = SubjectAreaGlossaryRESTServices.class.getName();

    /**
     * Default constructor
     */
    public SubjectAreaGlossaryRESTServices() {
        super();
    }

    /**
     * Create a Glossary
     *
     * @param userId           userId under which the request is performed
     * @param suppliedGlossary
     * @return response, when successful contains the created glossary.
     * when not successful the following Exception responses can occur
     *  UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     *  MetadataServerUncontactableException not able to communicate with a Metadata respository service.
     *  InvalidParameterException            one of the parameters is null or invalid.
     *  UnrecognizedGUIDException            the supplied guid was not recognised
     *  ClassificationException              Error processing a classification
     *  FunctionNotSupportedException        Function not supported
     *  StatusNotSupportedException          A status value is not supported
     */
    public SubjectAreaOMASAPIResponse createGlossary(String userId, org.odpi.openmetadata.accessservices.subjectarea.properties.objects.glossary.Glossary suppliedGlossary) {
        final String methodName = "createGlossary";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId);
        }


        SubjectAreaOMASAPIResponse response = null;
        SubjectAreaGlossaryRESTServices glossaryRESTServices = new SubjectAreaGlossaryRESTServices();
        glossaryRESTServices.setOMRSAPIHelper(this.oMRSAPIHelper);
        SubjectAreaBeansToAccessOMRS service = new SubjectAreaBeansToAccessOMRS();
        service.setOMRSAPIHelper(oMRSAPIHelper);

        org.odpi.openmetadata.accessservices.subjectarea.generated.entities.Glossary.Glossary generatedGlossary = null;
        List<Classification> classifications = new ArrayList<>();
        String glossaryGuid = null;
        final String suppliedGlossaryName = suppliedGlossary.getName();
        try {
            generatedGlossary = GlossaryMapper.mapGlossaryToOMRSBean(suppliedGlossary, oMRSAPIHelper);
        } catch (InvalidParameterException e) {
            response = OMASExceptionToResponse.convertInvalidParameterException(e);
        }
        if (response == null) {
            // need to check we have a name
            if (suppliedGlossaryName == null || suppliedGlossaryName.equals("")) {
                SubjectAreaErrorCode errorCode = SubjectAreaErrorCode.GLOSSARY_CREATE_WITHOUT_NAME;
                String errorMessage = errorCode.getErrorMessageId()
                        + errorCode.getFormattedErrorMessage(className,
                        methodName);
                log.error(errorMessage);
                InvalidParameterException e = new InvalidParameterException(errorCode.getHTTPErrorCode(),
                        className,
                        methodName,
                        errorMessage,
                        errorCode.getSystemAction(),
                        errorCode.getUserAction());
                response = new InvalidParameterExceptionResponse(e);
            } else {
                SubjectAreaOMASAPIResponse getGlossaryResponse = glossaryRESTServices.getGlossaryByName(userId, suppliedGlossaryName);
                if (getGlossaryResponse.getResponseCategory().equals(ResponseCategory.Glossary)) {
                    GlossaryResponse glossaryResponse = (GlossaryResponse) getGlossaryResponse;
                    // glossary found
                    SubjectAreaErrorCode errorCode = SubjectAreaErrorCode.GLOSSARY_CREATE_FAILED_NAME_ALREADY_EXISTS;
                    String errorMessage = errorCode.getErrorMessageId()
                            + errorCode.getFormattedErrorMessage(
                            suppliedGlossaryName);
                    log.error(errorMessage);
                    InvalidParameterException e = new InvalidParameterException(errorCode.getHTTPErrorCode(),
                            className,
                            methodName,
                            errorMessage,
                            errorCode.getSystemAction(),
                            errorCode.getUserAction());
                    response = new InvalidParameterExceptionResponse(e);
                } else {
                    if (getGlossaryResponse.getResponseCategory().equals(ResponseCategory.UnrecognizedNameException)) {
                        // this means that there were no errors and a glossary was not found by this name so we are ok to create one.
                    } else {
                        // an error occurred so do not continue
                        response = getGlossaryResponse;
                    }
                }
            }
        }
        if (response == null) {
            org.odpi.openmetadata.accessservices.subjectarea.generated.entities.Glossary.Glossary newGeneratedGlossary = null;
            try {
                newGeneratedGlossary = service.createGlossary(userId, generatedGlossary);
                org.odpi.openmetadata.accessservices.subjectarea.properties.objects.glossary.Glossary newGlossary = GlossaryMapper.mapOMRSBeantoGlossary(newGeneratedGlossary);
                response = new GlossaryResponse(newGlossary);
            } catch (MetadataServerUncontactableException e) {
                response = OMASExceptionToResponse.convertMetadataServerUncontactableException(e);
            } catch (UserNotAuthorizedException e) {
                response = OMASExceptionToResponse.convertUserNotAuthorizedException(e);
            } catch (InvalidParameterException e) {
                response = OMASExceptionToResponse.convertInvalidParameterException(e);
            } catch (ClassificationException e) {
                response = OMASExceptionToResponse.convertClassificationException(e);
            } catch (StatusNotSupportedException e) {
                response = OMASExceptionToResponse.convertStatusNotsupportedException(e);
            }
        }

        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId + ", response =" + response);
        }
        return response;
    }

    /**
     * Get a glossary by guid.
     *
     * @param userId userId under which the request is performed
     * @param guid   guid of the glossary to get
     * @return response which when successful contains the glossary with the requested guid
     * when not successful the following Exception responses can occur
     *  UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     *  MetadataServerUncontactableException not able to communicate with a Metadata respository service.
     *  InvalidParameterException            one of the parameters is null or invalid.
     *  UnrecognizedGUIDException            the supplied guid was not recognised
     *  FunctionNotSupportedException        Function not supported
     */
    public SubjectAreaOMASAPIResponse getGlossaryByGuid(String userId, String guid) {
        final String methodName = "getGlossary";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId + ",guid=" + guid);
        }
        SubjectAreaOMASAPIResponse response = null;
        SubjectAreaBeansToAccessOMRS subjectAreaBeansToAccessOMRS = new SubjectAreaBeansToAccessOMRS();
        subjectAreaBeansToAccessOMRS.setOMRSAPIHelper(oMRSAPIHelper);
        org.odpi.openmetadata.accessservices.subjectarea.generated.entities.Glossary.Glossary generatedGlossary = null;
        try {
            generatedGlossary = subjectAreaBeansToAccessOMRS.getGlossaryById(userId, guid);
        } catch (MetadataServerUncontactableException e) {
            response = OMASExceptionToResponse.convertMetadataServerUncontactableException(e);
        } catch (UserNotAuthorizedException e) {
            response = OMASExceptionToResponse.convertUserNotAuthorizedException(e);
        } catch (InvalidParameterException e) {
            response = OMASExceptionToResponse.convertInvalidParameterException(e);
        } catch (UnrecognizedGUIDException e) {
            response = OMASExceptionToResponse.convertUnrecognizedGUIDException(e);
        }
        org.odpi.openmetadata.accessservices.subjectarea.properties.objects.glossary.Glossary gotGlossary = null;
        List<Line> glossaryRelationships = null;
        if (response == null) {
            gotGlossary = GlossaryMapper.mapOMRSBeantoGlossary(generatedGlossary);
            List<org.odpi.openmetadata.accessservices.subjectarea.properties.classifications.Classification> classifications = generatedGlossary.getClassifications();
            // set the org.odpi.openmetadata.accessservices.subjectarea.generated.entities.Glossary.Glossary classifications into the Node
            gotGlossary.setClassifications(classifications);

            try {
                glossaryRelationships = subjectAreaBeansToAccessOMRS.getGlossaryRelationships(userId, guid);
            } catch (MetadataServerUncontactableException e) {
                response = OMASExceptionToResponse.convertMetadataServerUncontactableException(e);
            } catch (UserNotAuthorizedException e) {
                response = OMASExceptionToResponse.convertUserNotAuthorizedException(e);
            } catch (InvalidParameterException e) {
                response = OMASExceptionToResponse.convertInvalidParameterException(e);
            } catch (FunctionNotSupportedException e) {
                response = OMASExceptionToResponse.convertFunctionNotSupportedException(e);
            } catch (UnrecognizedGUIDException e) {
                response = OMASExceptionToResponse.convertUnrecognizedGUIDException(e);
            }
        }
        if (response == null) {
            GlossaryReferences generatedGlossaryReferences = null;
            try {
                generatedGlossaryReferences = new GlossaryReferences(guid, glossaryRelationships);
            } catch (InvalidParameterException e) {
                response = OMASExceptionToResponse.convertInvalidParameterException(e);
            }
            // set icon
            Set<RelatedMediaReference> relatedMediaReferenceSet = generatedGlossaryReferences.getRelatedMediaReferences();
            String iconUrl = "";
            //SubjectAreaUtils.getIcon(userId, subjectAreaBeansToAccessOMRS, relatedMediaReferenceSet);

            if (iconUrl != null) {
                gotGlossary.setIcon(iconUrl);
            }
            response = new GlossaryResponse((gotGlossary));
        }
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId + ", Node=" + gotGlossary);
        }
        return response;
    }

    /**
     * Update a Glossary
     * <p>
     * If the caller has chosen to incorporate the glossary name in their Glossary Terms or Categories qualified name, renaming the glossary will cause those
     * qualified names to mismatch the Glossary name.
     * If the caller has chosen to incorporate the glossary qualifiedName in their Glossary Terms or Categories qualified name, changing the qualified name of the glossary will cause those
     * qualified names to mismatch the Glossary name.
     * Status is not updated using this call.
     *
     * @param userId           userId under which the request is performed
     * @param guid             guid of the glossary to update
     * @param suppliedGlossary glossary to be updated
     * @return a response which when successful contains the updated glossary
     * when not successful the following Exception responses can occur
     *  UnrecognizedGUIDException            the supplied guid was not recognised
     *  UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     *  FunctionNotSupportedException        Function not supported
     *  InvalidParameterException            one of the parameters is null or invalid.
     *  MetadataServerUncontactableException not able to communicate with a Metadata respository service.
     */
    public SubjectAreaOMASAPIResponse updateGlossary(String userId, String guid, Glossary suppliedGlossary, boolean isReplace) {
        final String methodName = "updateGlossary";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId);
        }
        SubjectAreaOMASAPIResponse response = null;
        SubjectAreaBeansToAccessOMRS service = new SubjectAreaBeansToAccessOMRS();
        service.setOMRSAPIHelper(this.oMRSAPIHelper);
        response = getGlossaryByGuid(userId, guid);
        if (response.getResponseCategory().equals(ResponseCategory.Glossary)) {
            org.odpi.openmetadata.accessservices.subjectarea.properties.objects.glossary.Glossary originalGlossary = ((GlossaryResponse) response).getGlossary();
            if (originalGlossary.getSystemAttributes() !=null) {
                Status status = originalGlossary.getSystemAttributes().getStatus();
                SubjectAreaOMASAPIResponse deleteCheckResponse = SubjectAreaUtils.checkStatusNotDeleted(status, SubjectAreaErrorCode.GLOSSARY_UPDATE_FAILED_ON_DELETED_GLOSSARY);
                if (deleteCheckResponse != null) {
                    response = deleteCheckResponse;
                }
            }
            if (suppliedGlossary.getSystemAttributes() !=null) {
                Status status = suppliedGlossary.getSystemAttributes().getStatus();
                SubjectAreaOMASAPIResponse deleteCheckResponse = SubjectAreaUtils.checkStatusNotDeleted(status, SubjectAreaErrorCode.STATUS_UPDATE_TO_DELETED_NOT_ALLOWED);
                if (deleteCheckResponse != null) {
                    response = deleteCheckResponse;
                }
            }
            if (response != null) {
                Glossary updateGlossary = originalGlossary;
                if (isReplace) {
                    // copy over attributes
                    updateGlossary.setName(suppliedGlossary.getName());
                    updateGlossary.setQualifiedName(suppliedGlossary.getQualifiedName());
                    updateGlossary.setDescription(suppliedGlossary.getDescription());
                    updateGlossary.setUsage(suppliedGlossary.getUsage());
                    updateGlossary.setAdditionalProperties(suppliedGlossary.getAdditionalProperties());
                    //TODO handle governance classifications and other classifications
                } else {
                    // copy over attributes if specified
                    if (suppliedGlossary.getName() != null) {
                        updateGlossary.setName(suppliedGlossary.getName());
                    }
                    if (suppliedGlossary.getQualifiedName() != null) {
                        updateGlossary.setQualifiedName(suppliedGlossary.getQualifiedName());
                    }
                    if (suppliedGlossary.getDescription() != null) {
                        updateGlossary.setDescription(suppliedGlossary.getDescription());
                    }
                    if (suppliedGlossary.getUsage() != null) {
                        updateGlossary.setUsage(suppliedGlossary.getUsage());
                    }
                    if (suppliedGlossary.getAdditionalProperties() != null) {
                        updateGlossary.setAdditionalProperties(suppliedGlossary.getAdditionalProperties());
                    }
                    //TODO handle governance classifications and other classifications
                }
                org.odpi.openmetadata.accessservices.subjectarea.generated.entities.Glossary.Glossary generatedGlossary = null;
                try {
                    generatedGlossary = GlossaryMapper.mapGlossaryToOMRSBean(updateGlossary, oMRSAPIHelper);
                    org.odpi.openmetadata.accessservices.subjectarea.generated.entities.Glossary.Glossary updatedGeneratedGlossary = null;
                    try {
                        updatedGeneratedGlossary = service.updateGlossary(userId, generatedGlossary);
                    } catch (MetadataServerUncontactableException e) {
                        response = OMASExceptionToResponse.convertMetadataServerUncontactableException(e);
                    } catch (UserNotAuthorizedException e) {
                        response = OMASExceptionToResponse.convertUserNotAuthorizedException(e);
                    } catch (UnrecognizedGUIDException e) {
                        response = OMASExceptionToResponse.convertUnrecognizedGUIDException(e);
                    }
                    org.odpi.openmetadata.accessservices.subjectarea.properties.objects.glossary.Glossary updatedGlossary = GlossaryMapper.mapOMRSBeantoGlossary(updatedGeneratedGlossary);
                    response = new GlossaryResponse(updatedGlossary);
                } catch (InvalidParameterException e) {
                    response = OMASExceptionToResponse.convertInvalidParameterException(e);


                }
            }
        }
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId + ",response=" + response);
        }
        return response;

    }

    /**
     * Get a Glossary by name
     * <p>
     * Glossaries should have unique names. If repositories were not able to contact each other on the network, it is possible that glossaries of the same
     * name might be added. If this has occured this operation may not retun the glossary you are interested in. The guid of the glossary is the way to
     * uniquely identify a glossary; a get for glossary by guid can be issued to find glossaries with particular guids.
     *
     * @param userId userId under which the request is performed
     * @param name   find the glossary with this name.
     * @return the requested glossary.
     *  InvalidParameterException            one of the parameters is null or invalid.
     *  UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     *  MetadataServerUncontactableException not able to communicate with a Metadata respository service.
     *  FunctionNotSupportedException        Function not supported
     */
    public SubjectAreaOMASAPIResponse getGlossaryByName(String userId, String name) {
        final String methodName = "getGlossaryByName";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId + ",name=" + name);
        }
        SubjectAreaOMASAPIResponse response = null;
        List<EntityDetail> omrsEntityDetails = null;
        try {
            RestValidator.validateUserIdNotNull(className, methodName, userId);
        } catch (InvalidParameterException e) {
            response = OMASExceptionToResponse.convertInvalidParameterException(e);
        }
        if (response == null) {
            MatchCriteria matchCriteria = MatchCriteria.ALL;
            // guid for the Glossary Type.
            String entityTypeGUID = GLOSSARY_TYPE_GUID;
            InstanceProperties matchProperties = new InstanceProperties();
            PrimitivePropertyValue nameValue = new PrimitivePropertyValue();
            nameValue.setPrimitiveValue(name);
            nameValue.setPrimitiveDefCategory(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_STRING);
            matchProperties.setProperty("displayName", nameValue);

            // Look for all statuses apart from DELETED.
            List<InstanceStatus> limitResultsByStatus = new ArrayList<>();
            limitResultsByStatus.add(InstanceStatus.ACTIVE);
            limitResultsByStatus.add(InstanceStatus.DRAFT);
            limitResultsByStatus.add(InstanceStatus.PREPARED);
            limitResultsByStatus.add(InstanceStatus.PROPOSED);
            limitResultsByStatus.add(InstanceStatus.UNKNOWN);

            try {
                omrsEntityDetails = oMRSAPIHelper.callFindEntitiesByProperty(
                        userId,
                        entityTypeGUID,
                        matchProperties,
                        matchCriteria,
                        0,
                        limitResultsByStatus,
                        null,
                        null,
                        null,
                        null,
                        0

                );
            } catch (MetadataServerUncontactableException e) {
                response = OMASExceptionToResponse.convertMetadataServerUncontactableException(e);
            } catch (UserNotAuthorizedException e) {
                response = OMASExceptionToResponse.convertUserNotAuthorizedException(e);
            } catch (FunctionNotSupportedException e) {
                response = OMASExceptionToResponse.convertFunctionNotSupportedException(e);
            } catch (InvalidParameterException e) {
                response = OMASExceptionToResponse.convertInvalidParameterException(e);
            }
        }

        org.odpi.openmetadata.accessservices.subjectarea.properties.objects.glossary.Glossary gotGlossary = null;
        if (response == null) {
            if (omrsEntityDetails != null && omrsEntityDetails.size() > 0) {
                org.odpi.openmetadata.accessservices.subjectarea.generated.entities.Glossary.Glossary gotGeneratedGlossary = null;
                try {
                    gotGeneratedGlossary = org.odpi.openmetadata.accessservices.subjectarea.generated.entities.Glossary.GlossaryMapper.mapOmrsEntityDetailToGlossary(omrsEntityDetails.get(0));
                } catch (InvalidParameterException e) {
                    response = OMASExceptionToResponse.convertInvalidParameterException(e);
                }
                if (response == null) {
                    gotGlossary = GlossaryMapper.mapOMRSBeantoGlossary(gotGeneratedGlossary);
                    response = new GlossaryResponse(gotGlossary);
                }
            } else {
                // did not find a glossary of this name
                SubjectAreaErrorCode errorCode = SubjectAreaErrorCode.GLOSSARY_NAME_DOES_NOT_EXIST;
                String errorMessage = errorCode.getErrorMessageId()
                        + errorCode.getFormattedErrorMessage(className,
                        name);
                UnrecognizedNameException e = new UnrecognizedNameException(errorCode.getHTTPErrorCode(),
                        className,
                        methodName,
                        errorMessage,
                        errorCode.getSystemAction(),
                        errorCode.getUserAction(),
                        name);
                response = new UnrecognizedNameExceptionResponse(e);

            }
        }
        //If there are more than one then pick the first one. We could be more sophisticated with this in the future maybe identify a primary icon.

        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId + ", response=" + response);
        }
        return response;
    }

    /**
     * Delete a Glossary instance
     * <p>
     * The deletion of a glossary is only allowed if there is no glossary content (i.e. no terms or categories).
     * <p>
     * There are 2 types of deletion, a soft delete and a hard delete (also known as a purge). All repositories support hard deletes. Soft deletes support
     * is optional. Soft delete is the default.
     * <p>
     * A soft delete means that the glossary instance will exist in a deleted state in the repository after the delete operation. This means
     * that it is possible to undo the delete.
     * A hard delete means that the glossary will not exist after the operation.
     * when not successful the following Exception responses can occur
     *
     * @param userId  userId under which the request is performed
     * @param guid    guid of the glossary to be deleted.
     * @param isPurge true indicates a hard delete, false is a soft delete.
     * @return a void response
     *  UnrecognizedGUIDException            the supplied guid was not recognised
     *  UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     *  FunctionNotSupportedException        Function not supported this indicates that a soft delete was issued but the repository does not support it.
     *  InvalidParameterException            one of the parameters is null or invalid.
     *  MetadataServerUncontactableException not able to communicate with a Metadata respository service. There is a problem retrieving properties from the metadata repository.
     *  EntityNotDeletedException            a soft delete was issued but the glossary was not deleted.
     *  GUIDNotPurgedException               a hard delete was issued but the glossary was not purged
     */
    public SubjectAreaOMASAPIResponse deleteGlossary(String userId, String guid, Boolean isPurge) {
        final String methodName = "deleteGlossary";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId + ", guid=" + guid);
        }
        SubjectAreaOMASAPIResponse response = null;
        // do not delete if there is glossary content (terms or categories)
        // look for all glossary content that is not deleted.
        List<InstanceStatus> statusList = new ArrayList<>();
        statusList.add(InstanceStatus.ACTIVE);
        statusList.add(InstanceStatus.DRAFT);
        statusList.add(InstanceStatus.PROPOSED);
        statusList.add(InstanceStatus.PREPARED);
        statusList.add(InstanceStatus.UNKNOWN);

        List<Relationship> terms = null;
        List<Relationship> categories = null;
        try {
            terms = oMRSAPIHelper.callGetRelationshipsForEntity(userId, guid, TERM_ANCHOR_RELATIONSHIP_GUID, 0, statusList, null, null, null, 1);
            categories = oMRSAPIHelper.callGetRelationshipsForEntity(userId, guid, CATEGORY_ANCHOR_RELATIONSHIP_GUID, 0, statusList, null, null, null, 1);
        } catch (MetadataServerUncontactableException e) {
            response = OMASExceptionToResponse.convertMetadataServerUncontactableException(e);
        } catch (UserNotAuthorizedException e) {
            response = OMASExceptionToResponse.convertUserNotAuthorizedException(e);
        } catch (InvalidParameterException e) {
            response = OMASExceptionToResponse.convertInvalidParameterException(e);
        } catch (FunctionNotSupportedException e) {
            response = OMASExceptionToResponse.convertFunctionNotSupportedException(e);
        } catch (UnrecognizedGUIDException e) {
            response = OMASExceptionToResponse.convertUnrecognizedGUIDException(e);
        }
        if (response == null) {
            SubjectAreaBeansToAccessOMRS service = new SubjectAreaBeansToAccessOMRS();
            service.setOMRSAPIHelper(oMRSAPIHelper);
            if (((terms == null) || terms.isEmpty()) &&
                    (categories == null || categories.isEmpty())) {
                if (isPurge) {
                    try {
                        service.purgeGlossaryByGuid(userId, guid);
                        response = new VoidResponse();
                    } catch (MetadataServerUncontactableException e) {
                        response = OMASExceptionToResponse.convertMetadataServerUncontactableException(e);
                    } catch (UserNotAuthorizedException e) {
                        response = OMASExceptionToResponse.convertUserNotAuthorizedException(e);
                    } catch (InvalidParameterException e) {
                        response = OMASExceptionToResponse.convertInvalidParameterException(e);
                    } catch (UnrecognizedGUIDException e) {
                        response = OMASExceptionToResponse.convertUnrecognizedGUIDException(e);
                    } catch (EntityNotDeletedException e) {
                        response = OMASExceptionToResponse.convertEntityNotDeletedException(e);
                    } catch (GUIDNotPurgedException e) {
                        response = OMASExceptionToResponse.convertGUIDNotPurgedException(e);
                    }

                } else {
                    org.odpi.openmetadata.accessservices.subjectarea.generated.entities.Glossary.Glossary deletedGeneratedGlossary = null;
                    try {
                        EntityDetail entityDetail = service.deleteGlossaryByGuid(userId, guid);
                        deletedGeneratedGlossary = org.odpi.openmetadata.accessservices.subjectarea.generated.entities.Glossary.GlossaryMapper.mapOmrsEntityDetailToGlossary(entityDetail);
                        org.odpi.openmetadata.accessservices.subjectarea.properties.objects.glossary.Glossary deletedGlossary = GlossaryMapper.mapOMRSBeantoGlossary(deletedGeneratedGlossary);
                        response = new GlossaryResponse(deletedGlossary);
                    } catch (MetadataServerUncontactableException e) {
                        response = OMASExceptionToResponse.convertMetadataServerUncontactableException(e);
                    } catch (UserNotAuthorizedException e) {
                        response = OMASExceptionToResponse.convertUserNotAuthorizedException(e);
                    } catch (FunctionNotSupportedException e) {
                        response = OMASExceptionToResponse.convertFunctionNotSupportedException(e);
                    } catch (InvalidParameterException e) {
                        response = OMASExceptionToResponse.convertInvalidParameterException(e);
                    } catch (UnrecognizedGUIDException e) {
                        response = OMASExceptionToResponse.convertUnrecognizedGUIDException(e);
                    }
                }
            } else {
                SubjectAreaErrorCode errorCode = SubjectAreaErrorCode.GLOSSARY_CONTENT_PREVENTED_DELETE;
                String errorMessage = errorCode.getErrorMessageId()
                        + errorCode.getFormattedErrorMessage(className,
                        methodName, guid);
                log.error(errorMessage);
                InvalidParameterException e = new InvalidParameterException(errorCode.getHTTPErrorCode(),
                        className,
                        methodName,
                        errorMessage,
                        errorCode.getSystemAction(),
                        errorCode.getUserAction());
                response = new InvalidParameterExceptionResponse(e);
            }
        } else {
            // error occurred while looking for existing terms of categories.
        }

        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
        return response;
    }


}
