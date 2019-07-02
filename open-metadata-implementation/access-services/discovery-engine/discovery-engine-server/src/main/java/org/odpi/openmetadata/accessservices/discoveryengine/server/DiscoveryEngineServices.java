/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.discoveryengine.server;

import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
import org.odpi.openmetadata.commonservices.ffdc.rest.GUIDResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.NullRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.VoidResponse;
import org.odpi.openmetadata.commonservices.odf.metadatamanagement.handlers.AnnotationHandler;
import org.odpi.openmetadata.commonservices.odf.metadatamanagement.handlers.DiscoveryAnalysisReportHandler;
import org.odpi.openmetadata.commonservices.odf.metadatamanagement.rest.*;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.discovery.properties.Annotation;
import org.odpi.openmetadata.frameworks.discovery.properties.AnnotationStatus;
import org.odpi.openmetadata.frameworks.discovery.properties.DiscoveryAnalysisReport;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * The DiscoveryEngineServices provides the server-side implementation of the services used by the discovery
 * engine as it is managing requests to execute open discovery services in the discovery server.
 * These services align with the interface definitions from the Open Discovery Framework (ODF).
 */
public class DiscoveryEngineServices
{
    private static DiscoveryEngineServiceInstanceHandler instanceHandler = new DiscoveryEngineServiceInstanceHandler();

    private static final Logger log = LoggerFactory.getLogger(DiscoveryEngineServices.class);

    private RESTExceptionHandler restExceptionHandler = new RESTExceptionHandler();


    /**
     * Default constructor
     */
    public DiscoveryEngineServices()
    {
    }


    /**
     * Create a new discovery analysis report and chain it to its asset, discovery engine and discovery service.
     *
     * @param serverName name of server instance to route request to
     * @param userId calling user
     * @param assetGUID unique identifier of the asset being analysed
     * @param requestBody  all of the other parameters
     *
     * @return The new discovery report or
     *
     *  InvalidParameterException one of the parameters is invalid or
     *  UserNotAuthorizedException the user is not authorized to access the asset and/or report or
     *  PropertyServerException there was a problem in the store whether the asset/report properties are kept.
     */
    public DiscoveryAnalysisReportResponse createDiscoveryAnalysisReport(String                             serverName,
                                                                         String                             userId,
                                                                         String                             assetGUID,
                                                                         DiscoveryAnalysisReportRequestBody requestBody)
    {
        final String   methodName = "createDiscoveryAnalysisReport";

        log.debug("Calling method: " + methodName);

        OMRSAuditLog                    auditLog = null;
        DiscoveryAnalysisReportResponse response = new DiscoveryAnalysisReportResponse();

        try
        {
            if (requestBody == null)
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
            }
            else
            {
                DiscoveryAnalysisReportHandler handler = instanceHandler.getDiscoveryAnalysisReportHandler(userId,
                                                                                                           serverName,
                                                                                                           methodName);

                auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

                response.setAnalysisReport(handler.createDiscoveryAnalysisReport(userId,
                                                                                 requestBody.getQualifiedName(),
                                                                                 requestBody.getDisplayName(),
                                                                                 requestBody.getDescription(),
                                                                                 requestBody.getCreationDate(),
                                                                                 requestBody.getAnalysisParameters(),
                                                                                 requestBody.getDiscoveryRequestStatus(),
                                                                                 assetGUID,
                                                                                 requestBody.getDiscoveryEngineGUID(),
                                                                                 requestBody.getDiscoveryServiceGUID(),
                                                                                 requestBody.getAdditionalProperties(),
                                                                                 requestBody.getClassifications(),
                                                                                 methodName));
            }
        }
        catch (InvalidParameterException error)
        {
            restExceptionHandler.captureInvalidParameterException(response, error);
        }
        catch (PropertyServerException error)
        {
            restExceptionHandler.capturePropertyServerException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            restExceptionHandler.captureUserNotAuthorizedException(response, error);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureThrowable(response, error, methodName, auditLog);
        }

        log.debug("Returning from method: " + methodName + " with response: " + response.toString());

        return response;
    }


    /**
     * Update the properties of the discovery analysis report.
     *
     * @param serverName name of server instance to route request to
     * @param userId calling user
     * @param discoveryReportGUID unique identifier of the report to update
     * @param requestBody updated report - this will replace what was previous stored
     *
     * @return the new values stored in the repository or
     *
     *  InvalidParameterException one of the parameters is null or invalid.
     *  UserNotAuthorizedException user not authorized to issue this request.
     *  PropertyServerException there was a problem that occurred within the property server.
     */
    public DiscoveryAnalysisReportResponse updateDiscoveryAnalysisReport(String                  serverName,
                                                                         String                  userId,
                                                                         String                  discoveryReportGUID,
                                                                         DiscoveryAnalysisReport requestBody)
    {
        final String   methodName = "updateDiscoveryAnalysisReport";

        log.debug("Calling method: " + methodName);

        OMRSAuditLog                    auditLog = null;
        DiscoveryAnalysisReportResponse response = new DiscoveryAnalysisReportResponse();

        try
        {
            if (requestBody == null)
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
            }

            DiscoveryAnalysisReportHandler handler = instanceHandler.getDiscoveryAnalysisReportHandler(userId,
                                                                                                       serverName,
                                                                                                       methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            response.setAnalysisReport(handler.updateDiscoveryAnalysisReport(userId,
                                                                             discoveryReportGUID,
                                                                             requestBody));
        }
        catch (InvalidParameterException error)
        {
            restExceptionHandler.captureInvalidParameterException(response, error);
        }
        catch (PropertyServerException error)
        {
            restExceptionHandler.capturePropertyServerException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            restExceptionHandler.captureUserNotAuthorizedException(response, error);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureThrowable(response, error, methodName, auditLog);
        }

        log.debug("Returning from method: " + methodName + " with response: " + response.toString());

        return response;
    }


    /**
     * Request the discovery report for a discovery request that has completed.
     *
     * @param serverName name of server instance to route request to
     * @param userId identifier of calling user
     * @param discoveryReportGUID identifier of the discovery request.
     *
     * @return discovery report or
     *
     *  InvalidParameterException one of the parameters is null or invalid.
     *  UserNotAuthorizedException user not authorized to issue this request.
     *  PropertyServerException there was a problem that occurred within the property server.
     */
    public DiscoveryAnalysisReportResponse getDiscoveryAnalysisReport(String   serverName,
                                                                      String   userId,
                                                                      String   discoveryReportGUID)
    {
        final String   methodName = "getDiscoveryAnalysisReport";

        log.debug("Calling method: " + methodName);

        OMRSAuditLog                    auditLog = null;
        DiscoveryAnalysisReportResponse response = new DiscoveryAnalysisReportResponse();

        try
        {
            DiscoveryAnalysisReportHandler handler = instanceHandler.getDiscoveryAnalysisReportHandler(userId,
                                                                                                       serverName,
                                                                                                       methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            response.setAnalysisReport(handler.getDiscoveryAnalysisReport(userId,
                                                                          discoveryReportGUID,
                                                                          methodName));
        }
        catch (InvalidParameterException error)
        {
            restExceptionHandler.captureInvalidParameterException(response, error);
        }
        catch (PropertyServerException error)
        {
            restExceptionHandler.capturePropertyServerException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            restExceptionHandler.captureUserNotAuthorizedException(response, error);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureThrowable(response, error, methodName, auditLog);
        }

        log.debug("Returning from method: " + methodName + " with response: " + response.toString());

        return response;
    }


    /**
     * Return the list of annotations from previous runs of the discovery service that are set to a specific status.
     * If status is null then annotations that have been reviewed, approved and/or actioned are returned from
     * discovery reports that are not waiting or in progress.
     *
     * @param serverName name of server instance to route request to
     * @param userId calling user
     * @param assetGUID unique identifier of the asset
     * @param startingFrom starting position in the list.
     * @param maximumResults maximum number of elements that can be returned
     * @param requestBody status value to use on the query
     *
     * @return list of annotation (or null if none are registered) or
     *
     *  InvalidParameterException one of the parameters is invalid
     *  UserNotAuthorizedException the user id not authorized to issue this request
     *  PropertyServerException there was a problem retrieving annotations from the annotation store.
     */
    public AnnotationListResponse getAnnotationsForAssetByStatus(String            serverName,
                                                                 String            userId,
                                                                 String            assetGUID,
                                                                 int               startingFrom,
                                                                 int               maximumResults,
                                                                 StatusRequestBody requestBody)
    {
        final String   methodName = "getAnnotationsForAssetByStatus";

        log.debug("Calling method: " + methodName);

        OMRSAuditLog           auditLog = null;
        AnnotationListResponse response = new AnnotationListResponse();
        AnnotationStatus       status   = null;

        if (requestBody != null)
        {
            status = requestBody.getAnnotationStatus();
        }

        try
        {
            DiscoveryAnalysisReportHandler handler = instanceHandler.getDiscoveryAnalysisReportHandler(userId,
                                                                                                       serverName,
                                                                                                       methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            response.setAnnotations(handler.getAnnotationsForAssetByStatus(userId,
                                                                           assetGUID,
                                                                           status,
                                                                           startingFrom,
                                                                           maximumResults,
                                                                           methodName));
        }
        catch (InvalidParameterException error)
        {
            restExceptionHandler.captureInvalidParameterException(response, error);
        }
        catch (PropertyServerException error)
        {
            restExceptionHandler.capturePropertyServerException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            restExceptionHandler.captureUserNotAuthorizedException(response, error);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureThrowable(response, error, methodName, auditLog);
        }

        log.debug("Returning from method: " + methodName + " with response: " + response.toString());

        return response;
    }


    /**
     * Return the annotations linked directly to the report.
     *
     * @param serverName name of server instance to route request to
     * @param userId identifier of calling user
     * @param discoveryReportGUID identifier of the discovery request.
     * @param startingFrom initial position in the stored list.
     * @param maximumResults maximum number of definitions to return on this call.
     *
     * @return list of annotations or
     *
     *  InvalidParameterException one of the parameters is null or invalid.
     *  UserNotAuthorizedException user not authorized to issue this request.
     *  PropertyServerException there was a problem that occurred within the property server.
     */
    public AnnotationListResponse getDiscoveryReportAnnotations(String   serverName,
                                                                String   userId,
                                                                String   discoveryReportGUID,
                                                                int      startingFrom,
                                                                int      maximumResults)
    {
        final String   methodName = "getDiscoveryReportAnnotations";

        log.debug("Calling method: " + methodName);

        OMRSAuditLog           auditLog = null;
        AnnotationListResponse response = new AnnotationListResponse();

        try
        {
            DiscoveryAnalysisReportHandler handler = instanceHandler.getDiscoveryAnalysisReportHandler(userId,
                                                                                                       serverName,
                                                                                                       methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            response.setAnnotations(handler.getDiscoveryReportAnnotations(userId,
                                                                          discoveryReportGUID,
                                                                          null,
                                                                          startingFrom,
                                                                          maximumResults,
                                                                          methodName));
        }
        catch (InvalidParameterException error)
        {
            restExceptionHandler.captureInvalidParameterException(response, error);
        }
        catch (PropertyServerException error)
        {
            restExceptionHandler.capturePropertyServerException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            restExceptionHandler.captureUserNotAuthorizedException(response, error);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureThrowable(response, error, methodName, auditLog);
        }

        log.debug("Returning from method: " + methodName + " with response: " + response.toString());

        return response;
    }


    /**
     * Return any annotations attached to this annotation.
     *
     * @param serverName name of server instance to route request to
     * @param userId identifier of calling user
     * @param annotationGUID anchor annotation
     * @param startingFrom starting position in the list
     * @param maximumResults maximum number of annotations that can be returned.
     *
     * @return list of Annotation objects or
     *
     *  InvalidParameterException one of the parameters is null or invalid.
     *  UserNotAuthorizedException user not authorized to issue this request.
     *  PropertyServerException there was a problem that occurred within the property server.
     */
    public  AnnotationListResponse  getExtendedAnnotations(String   serverName,
                                                           String   userId,
                                                           String   annotationGUID,
                                                           int      startingFrom,
                                                           int      maximumResults)
    {
        final String   methodName = "getExtendedAnnotations";

        log.debug("Calling method: " + methodName);

        OMRSAuditLog           auditLog = null;
        AnnotationListResponse response = new AnnotationListResponse();

        try
        {
            AnnotationHandler handler = instanceHandler.getAnnotationHandler(userId,
                                                                             serverName,
                                                                             methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            response.setAnnotations(handler.getExtendedAnnotations(userId,
                                                                   annotationGUID,
                                                                   null,
                                                                   startingFrom,
                                                                   maximumResults,
                                                                   methodName));
        }
        catch (InvalidParameterException error)
        {
            restExceptionHandler.captureInvalidParameterException(response, error);
        }
        catch (PropertyServerException error)
        {
            restExceptionHandler.capturePropertyServerException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            restExceptionHandler.captureUserNotAuthorizedException(response, error);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureThrowable(response, error, methodName, auditLog);
        }

        log.debug("Returning from method: " + methodName + " with response: " + response.toString());

        return response;
    }


    /**
     * Retrieve a single annotation by unique identifier.  This call is typically used to retrieve the latest values
     * for an annotation.
     *
     * @param serverName name of server instance to route request to
     * @param userId identifier of calling user
     * @param annotationGUID unique identifier of the annotation
     *
     * @return Annotation object or
     *
     *  InvalidParameterException one of the parameters is null or invalid.
     *  UserNotAuthorizedException user not authorized to issue this request.
     *  PropertyServerException there was a problem that occurred within the property server.
     */
    public  AnnotationResponse getAnnotation(String   serverName,
                                             String   userId,
                                             String   annotationGUID)
    {
        final String   methodName = "getAnnotation";

        log.debug("Calling method: " + methodName);

        OMRSAuditLog       auditLog = null;
        AnnotationResponse response = new AnnotationResponse();

        try
        {
            AnnotationHandler handler = instanceHandler.getAnnotationHandler(userId,
                                                                             serverName,
                                                                             methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            response.setAnnotation(handler.getAnnotation(userId,
                                                         annotationGUID,
                                                         methodName));
        }
        catch (InvalidParameterException error)
        {
            restExceptionHandler.captureInvalidParameterException(response, error);
        }
        catch (PropertyServerException error)
        {
            restExceptionHandler.capturePropertyServerException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            restExceptionHandler.captureUserNotAuthorizedException(response, error);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureThrowable(response, error, methodName, auditLog);
        }

        log.debug("Returning from method: " + methodName + " with response: " + response.toString());

        return response;
    }


    /**
     * Add a new annotation to the annotation store as a top level annotation linked directly off of the report.
     *
     * @param serverName name of server instance to route request to
     * @param userId identifier of calling user
     * @param discoveryReportGUID unique identifier of the discovery analysis report
     * @param requestBody annotation object
     *
     * @return unique identifier of new annotation or
     *
     *  InvalidParameterException the annotation is invalid
     *  UserNotAuthorizedException the user id not authorized to issue this request
     *  PropertyServerException there was a problem retrieving adding the annotation to the annotation store.
     */
    public  GUIDResponse  addAnnotationToDiscoveryReport(String     serverName,
                                                         String     userId,
                                                         String     discoveryReportGUID,
                                                         Annotation requestBody)
    {
        final String   methodName = "addAnnotationToDiscoveryReport";

        log.debug("Calling method: " + methodName);

        OMRSAuditLog auditLog = null;
        GUIDResponse response = new GUIDResponse();

        try
        {
            if (requestBody == null)
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
            }

            DiscoveryAnalysisReportHandler handler = instanceHandler.getDiscoveryAnalysisReportHandler(userId,
                                                                                                       serverName,
                                                                                                       methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            response.setGUID(handler.addAnnotationToDiscoveryReport(userId,
                                                                    discoveryReportGUID,
                                                                    requestBody,
                                                                    methodName));
        }
        catch (InvalidParameterException error)
        {
            restExceptionHandler.captureInvalidParameterException(response, error);
        }
        catch (PropertyServerException error)
        {
            restExceptionHandler.capturePropertyServerException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            restExceptionHandler.captureUserNotAuthorizedException(response, error);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureThrowable(response, error, methodName, auditLog);
        }

        log.debug("Returning from method: " + methodName + " with response: " + response.toString());

        return response;
    }


    /**
     * Add a new annotation and link it to an existing annotation.
     *
     * @param serverName name of server instance to route request to
     * @param userId identifier of calling user
     * @param anchorAnnotationGUID unique identifier of the annotation that this new one os to be attached to
     * @param requestBody annotation object
     *
     * @return fully filled out annotation or
     *
     *  InvalidParameterException one of the parameters is invalid
     *  UserNotAuthorizedException the user id not authorized to issue this request
     *  PropertyServerException there was a problem saving annotations in the annotation store.
     */
    public  AnnotationResponse  addAnnotationToAnnotation(String     serverName,
                                                          String     userId,
                                                          String     anchorAnnotationGUID,
                                                          Annotation requestBody)
    {
        final String   methodName = "addAnnotationToAnnotation";

        log.debug("Calling method: " + methodName);

        OMRSAuditLog       auditLog = null;
        AnnotationResponse response = new AnnotationResponse();

        try
        {
            AnnotationHandler handler = instanceHandler.getAnnotationHandler(userId,
                                                                             serverName,
                                                                             methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            response.setAnnotation(handler.addAnnotationToAnnotation(userId,
                                                                     anchorAnnotationGUID,
                                                                     requestBody,
                                                                     methodName));
        }
        catch (InvalidParameterException error)
        {
            restExceptionHandler.captureInvalidParameterException(response, error);
        }
        catch (PropertyServerException error)
        {
            restExceptionHandler.capturePropertyServerException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            restExceptionHandler.captureUserNotAuthorizedException(response, error);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureThrowable(response, error, methodName, auditLog);
        }

        log.debug("Returning from method: " + methodName + " with response: " + response.toString());

        return response;
    }


    /**
     * Link an existing annotation to another object.  The anchor object must be a Referenceable.
     *
     * @param serverName name of server instance to route request to
     * @param userId identifier of calling user
     * @param anchorGUID unique identifier that the annotation is to be linked to
     * @param annotationGUID unique identifier of the annotation
     * @param requestBody null request body to satisfy POST semantics
     *
     * @return void or
     *
     *  InvalidParameterException one of the parameters is invalid
     *  UserNotAuthorizedException the user id not authorized to issue this request
     *  PropertyServerException there was a problem updating annotations in the annotation store.
     */
    public VoidResponse  linkAnnotation(String          serverName, 
                                        String          userId, 
                                        String          anchorGUID, 
                                        String          annotationGUID, 
                                        NullRequestBody requestBody)
    {
        final String   methodName = "linkAnnotation";

        log.debug("Calling method: " + methodName);

        OMRSAuditLog auditLog = null;
        VoidResponse response = new VoidResponse();

        try
        {
            AnnotationHandler handler = instanceHandler.getAnnotationHandler(userId,
                                                                             serverName,
                                                                             methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            handler.linkAnnotation(userId, anchorGUID, annotationGUID, methodName);
        }
        catch (InvalidParameterException error)
        {
            restExceptionHandler.captureInvalidParameterException(response, error);
        }
        catch (PropertyServerException error)
        {
            restExceptionHandler.capturePropertyServerException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            restExceptionHandler.captureUserNotAuthorizedException(response, error);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureThrowable(response, error, methodName, auditLog);
        }

        log.debug("Returning from method: " + methodName + " with response: " + response.toString());

        return response;
    }


    /**
     * Remove the relationship between an annotation and another object.
     *
     * @param serverName name of server instance to route request to
     * @param userId identifier of calling user
     * @param anchorGUID unique identifier that the annotation is to be unlinked from
     * @param annotationGUID unique identifier of the annotation
     * @param requestBody null request body to satisfy POST semantics
     *
     * @return void or
     *
     *  InvalidParameterException one of the parameters is invalid
     *  UserNotAuthorizedException the user id not authorized to issue this request
     *  PropertyServerException there was a problem updating annotations in the annotation store.
     */
    public VoidResponse  unlinkAnnotation(String          serverName, 
                                          String          userId, 
                                          String          anchorGUID, 
                                          String          annotationGUID, 
                                          NullRequestBody requestBody)
    {
        final String   methodName = "unlinkAnnotation";

        log.debug("Calling method: " + methodName);

        OMRSAuditLog auditLog = null;
        VoidResponse response = new VoidResponse();

        try
        {
            AnnotationHandler handler = instanceHandler.getAnnotationHandler(userId,
                                                                             serverName,
                                                                             methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            handler.unlinkAnnotation(userId, anchorGUID, annotationGUID, methodName);
        }
        catch (InvalidParameterException error)
        {
            restExceptionHandler.captureInvalidParameterException(response, error);
        }
        catch (PropertyServerException error)
        {
            restExceptionHandler.capturePropertyServerException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            restExceptionHandler.captureUserNotAuthorizedException(response, error);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureThrowable(response, error, methodName, auditLog);
        }

        log.debug("Returning from method: " + methodName + " with response: " + response.toString());

        return response;
    }


    /**
     * Replace the current properties of an annotation.
     *
     * @param serverName name of server instance to route request to
     * @param userId identifier of calling user
     * @param annotationGUID identifier of the annotation to change
     * @param requestBody new properties
     *
     * @return fully filled out annotation or
     *
     *  InvalidParameterException one of the parameters is invalid
     *  UserNotAuthorizedException the user id not authorized to issue this request
     *  PropertyServerException there was a problem updating the annotation in the annotation store.
     */
    public AnnotationResponse  updateAnnotation(String     serverName, 
                                                String     userId, 
                                                String     annotationGUID, 
                                                Annotation requestBody)
    {
        final String   methodName = "updateAnnotation";

        log.debug("Calling method: " + methodName);

        OMRSAuditLog       auditLog = null;
        AnnotationResponse response = new AnnotationResponse();

        try
        {
            AnnotationHandler handler = instanceHandler.getAnnotationHandler(userId,
                                                                             serverName,
                                                                             methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            response.setAnnotation(handler.updateAnnotation(userId, annotationGUID, requestBody, methodName));
        }
        catch (InvalidParameterException error)
        {
            restExceptionHandler.captureInvalidParameterException(response, error);
        }
        catch (PropertyServerException error)
        {
            restExceptionHandler.capturePropertyServerException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            restExceptionHandler.captureUserNotAuthorizedException(response, error);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureThrowable(response, error, methodName, auditLog);
        }

        log.debug("Returning from method: " + methodName + " with response: " + response.toString());

        return response;
    }


    /**
     * Remove an annotation from the annotation store.
     *
     * @param serverName name of server instance to route request to
     * @param userId identifier of calling user
     * @param annotationGUID unique identifier of the annotation
     * @param requestBody null request body to satisfy POST semantics
     *
     * @return void or
     *
     *  InvalidParameterException one of the parameters is invalid
     *  UserNotAuthorizedException the user id not authorized to issue this request
     *  PropertyServerException there was a problem deleting the annotation from the annotation store.
     */
    public VoidResponse  deleteAnnotation(String          serverName, 
                                          String          userId, 
                                          String          annotationGUID, 
                                          NullRequestBody requestBody)
    {
        final String   methodName = "deleteAnnotation";

        log.debug("Calling method: " + methodName);

        OMRSAuditLog auditLog = null;
        VoidResponse response = new VoidResponse();

        try
        {
            AnnotationHandler handler = instanceHandler.getAnnotationHandler(userId,
                                                                             serverName,
                                                                             methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            handler.deleteAnnotation(userId, annotationGUID, methodName);
        }
        catch (InvalidParameterException error)
        {
            restExceptionHandler.captureInvalidParameterException(response, error);
        }
        catch (PropertyServerException error)
        {
            restExceptionHandler.capturePropertyServerException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            restExceptionHandler.captureUserNotAuthorizedException(response, error);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureThrowable(response, error, methodName, auditLog);
        }

        log.debug("Returning from method: " + methodName + " with response: " + response.toString());

        return response;
    }
}
