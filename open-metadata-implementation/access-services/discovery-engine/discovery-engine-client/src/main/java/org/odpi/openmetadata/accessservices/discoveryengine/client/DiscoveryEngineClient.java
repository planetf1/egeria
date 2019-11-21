/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.discoveryengine.client;

import org.odpi.openmetadata.commonservices.ffdc.rest.GUIDResponse;
import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.client.ConnectedAssetClientBase;
import org.odpi.openmetadata.commonservices.odf.metadatamanagement.client.ODFRESTClient;
import org.odpi.openmetadata.commonservices.odf.metadatamanagement.rest.*;
import org.odpi.openmetadata.frameworks.connectors.Connector;
import org.odpi.openmetadata.frameworks.connectors.ffdc.*;
import org.odpi.openmetadata.frameworks.connectors.properties.AssetUniverse;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Classification;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.frameworks.discovery.properties.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * DiscoveryEngineClient provides the client-side operational REST APIs for a running Discovery Engine
 */
public class DiscoveryEngineClient extends ConnectedAssetClientBase
{
    private ODFRESTClient restClient;               /* Initialized in constructor */

    private static final String  serviceURLName = "discovery-engine";

    /**
     * Constructor sets up the key parameters for accessing the asset store.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformRootURL the network address of the server running the OMAS REST servers
     * @param restClient client for calling REST APIs
     * @throws InvalidParameterException unable to initialize the client due to bad parameters
     */
    public DiscoveryEngineClient(String        serverName,
                                 String        serverPlatformRootURL,
                                 ODFRESTClient restClient) throws InvalidParameterException
    {
        super(serverName, serverPlatformRootURL);

        this.restClient = restClient;
    }


    /**
     * Return the connection information for the asset.  This is used to create the connector.  The connector
     * is an Open Connector Framework (OCF) connector that provides access to the asset's data and metadata properties.
     *
     * @param userId calling user
     * @param assetGUID unique identifier (guid) for the asset
     * @return Connection bean
     * @throws InvalidParameterException the asset guid is not recognized
     * @throws UserNotAuthorizedException the user is not authorized to access the asset and/or connection
     * @throws PropertyServerException there was a problem in the store whether the asset/connection properties are kept.
     */
    Connection getConnectionForAsset(String    userId,
                                     String    assetGUID) throws InvalidParameterException,
                                                                 UserNotAuthorizedException,
                                                                 PropertyServerException
    {
        final String   methodName = "getConnectionForAsset";
        final String   guidParameterName = "assetGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(assetGUID, guidParameterName, methodName);

        return super.getConnectionForAsset(restClient, serviceURLName, userId, assetGUID);
    }


    /**
     * Returns the connector corresponding to the supplied connection.
     *
     * @param userId       userId of user making request.
     * @param connection   the connection object that contains the properties needed to create the connection.
     *
     * @return Connector   connector instance
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws ConnectionCheckedException there are errors in the configuration of the connection which is preventing
     *                                      the creation of a connector.
     * @throws ConnectorCheckedException there are errors in the initialization of the connector.
     */
    public Connector getConnectorForConnection(String     userId,
                                               Connection connection) throws InvalidParameterException,
                                                                             ConnectionCheckedException,
                                                                             ConnectorCheckedException
    {
        final  String  methodName = "getConnectorForConnection";

        invalidParameterHandler.validateUserId(userId, methodName);

        return super.getConnectorForConnection(restClient, serviceURLName, userId, connection, methodName);
    }



    /**
     * Returns a comprehensive collection of properties about the requested asset.
     *
     * @param userId         userId of user making request.
     * @param assetGUID      unique identifier for asset.
     *
     * @return a comprehensive collection of properties about the asset.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem retrieving the asset properties from the property servers).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    AssetUniverse getAssetProperties(String userId,
                                     String assetGUID) throws InvalidParameterException,
                                                              PropertyServerException,
                                                              UserNotAuthorizedException
    {
        return super.getAssetProperties(serviceURLName, userId, assetGUID);
    }


    /**
     * Log an audit message about this asset.
     *
     * @param userId         userId of user making request.
     * @param assetGUID      unique identifier for asset.
     * @param message        message to log
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem retrieving the asset properties from the property servers).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void logAssetAuditMessage(String    userId,
                                     String    assetGUID,
                                     String    message) throws InvalidParameterException,
                                                               PropertyServerException,
                                                               UserNotAuthorizedException
    {
    }


    /**
     * Create a new discovery analysis report and chain it to its asset, discovery engine and discovery service.
     *
     * @param userId calling user
     * @param qualifiedName unique name for the report
     * @param displayName short name for the report
     * @param description description of the report
     * @param creationDate date of the report
     * @param analysisParameters analysis parameters passed to the discovery service
     * @param discoveryRequestStatus current status of the discovery processing
     * @param assetGUID unique identifier of the asset being analysed
     * @param discoveryEngineGUID unique identifier of the discovery engine that is running the discovery service
     * @param discoveryServiceGUID unique identifier of the discovery service creating the report
     * @param additionalProperties additional properties for the report
     * @param classifications classifications to attach to the report
     *
     * @return The new discovery report.
     * @throws InvalidParameterException one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to access the asset and/or report
     * @throws PropertyServerException there was a problem in the store whether the asset/report properties are kept.
     */
    public  DiscoveryAnalysisReport  createDiscoveryAnalysisReport(String                  userId,
                                                                   String                  qualifiedName,
                                                                   String                  displayName,
                                                                   String                  description,
                                                                   Date                    creationDate,
                                                                   Map<String, String>     analysisParameters,
                                                                   DiscoveryRequestStatus  discoveryRequestStatus,
                                                                   String                  assetGUID,
                                                                   String                  discoveryEngineGUID,
                                                                   String                  discoveryServiceGUID,
                                                                   Map<String, String>     additionalProperties,
                                                                   List<Classification>    classifications) throws InvalidParameterException,
                                                                                                                   UserNotAuthorizedException,
                                                                                                                   PropertyServerException
    {
        final String   methodName = "createDiscoveryAnalysisReport";
        final String   nameParameterName = "qualifiedName";
        final String   assetGUIDParameterName = "assetGUID";
        final String   discoveryEngineGUIDParameterName = "discoveryEngineGUID";
        final String   discoveryServiceGUIDParameterName = "discoveryServiceGUID";
        final String   urlTemplate = "/servers/{0}/open-metadata/access-services/discovery-engine/users/{1}/assets/{2}/discovery-analysis-reports";

        invalidParameterHandler.validateName(qualifiedName, nameParameterName, methodName);
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(assetGUID, assetGUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(discoveryEngineGUID, discoveryEngineGUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(discoveryServiceGUID, discoveryServiceGUIDParameterName, methodName);

        DiscoveryAnalysisReportRequestBody requestBody = new DiscoveryAnalysisReportRequestBody();

        requestBody.setQualifiedName(qualifiedName);
        requestBody.setDisplayName(displayName);
        requestBody.setDescription(description);
        requestBody.setCreationDate(creationDate);
        requestBody.setAnalysisParameters(analysisParameters);
        requestBody.setDiscoveryRequestStatus(discoveryRequestStatus);
        requestBody.setDiscoveryEngineGUID(discoveryEngineGUID);
        requestBody.setDiscoveryServiceGUID(discoveryServiceGUID);
        requestBody.setAdditionalProperties(additionalProperties);
        requestBody.setClassifications(classifications);

        DiscoveryAnalysisReportResponse restResult = restClient.callDiscoveryAnalysisReportPostRESTCall(methodName,
                                                                                                        serverPlatformRootURL + urlTemplate,
                                                                                                        requestBody,
                                                                                                        serverName,
                                                                                                        userId,
                                                                                                        assetGUID);

        return restResult.getAnalysisReport();
    }


    /**
     * Update the properties of the discovery analysis report.
     *
     * @param userId calling user.
     * @param updatedReport updated report - this will replace what was previous stored.
     * @return the new values stored in the repository
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException there was a problem that occurred within the property server.
     */
    public DiscoveryAnalysisReport  updateDiscoveryAnalysisReport(String                  userId,
                                                                  DiscoveryAnalysisReport updatedReport) throws InvalidParameterException,
                                                                                                                UserNotAuthorizedException,
                                                                                                                PropertyServerException
    {
        final String   methodName = "updateDiscoveryAnalysisReport";
        final String   reportParameterName = "updatedReport";
        final String   reportGUIDParameterName = "updatedReport.getGUID()";
        final String   urlTemplate = "/servers/{0}/open-metadata/access-services/discovery-engine/users/{1}/discovery-analysis-reports/{2}";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateObject(updatedReport, reportParameterName, methodName);
        invalidParameterHandler.validateGUID(updatedReport.getGUID(), reportGUIDParameterName, methodName);

        DiscoveryAnalysisReportResponse restResult = restClient.callDiscoveryAnalysisReportPostRESTCall(methodName,
                                                                                                        serverPlatformRootURL + urlTemplate,
                                                                                                        updatedReport,
                                                                                                        serverName,
                                                                                                        userId,
                                                                                                        updatedReport.getGUID());

        return restResult.getAnalysisReport();
    }


    /**
     * Request the status of an executing discovery request.
     *
     * @param userId identifier of calling user
     * @param discoveryReportGUID identifier of the discovery request.
     *
     * @return status enum
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException there was a problem that occurred within the property server.
     */
    public DiscoveryRequestStatus getDiscoveryStatus(String   userId,
                                                     String   discoveryReportGUID) throws InvalidParameterException,
                                                                                          UserNotAuthorizedException,
                                                                                          PropertyServerException
    {
        DiscoveryAnalysisReport report = this.getDiscoveryAnalysisReport(userId, discoveryReportGUID);

        if (report != null)
        {
            return report.getDiscoveryRequestStatus();
        }

        return null;
    }


    /**
     * Request the status of an executing discovery request.
     *
     * @param userId identifier of calling user
     * @param discoveryReportGUID identifier of the discovery request.
     * @param newStatus  status enum
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException there was a problem that occurred within the property server.
     */
    public void setDiscoveryStatus(String                  userId,
                                   String                  discoveryReportGUID,
                                   DiscoveryRequestStatus  newStatus) throws InvalidParameterException,
                                                                             UserNotAuthorizedException,
                                                                             PropertyServerException
    {
        DiscoveryAnalysisReport report = this.getDiscoveryAnalysisReport(userId, discoveryReportGUID);

        if (report != null)
        {
            report.setDiscoveryRequestStatus(newStatus);

            this.updateDiscoveryAnalysisReport(userId, report);
        }
    }


    /**
     * Request the discovery report for a discovery request that has completed.
     *
     * @param userId identifier of calling user
     * @param discoveryReportGUID identifier of the discovery report.
     *
     * @return discovery report
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException there was a problem that occurred within the property server.
     */
    public DiscoveryAnalysisReport getDiscoveryAnalysisReport(String   userId,
                                                              String   discoveryReportGUID) throws InvalidParameterException,
                                                                                                   UserNotAuthorizedException,
                                                                                                   PropertyServerException
    {
        final String   methodName = "getDiscoveryAnalysisReport";
        final String   reportGUIDParameterName = "discoveryReportGUID";
        final String   urlTemplate = "/servers/{0}/open-metadata/access-services/discovery-engine/users/{1}/discovery-analysis-reports/{2}";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(discoveryReportGUID, reportGUIDParameterName, methodName);

        DiscoveryAnalysisReportResponse restResult = restClient.callDiscoveryAnalysisReportGetRESTCall(methodName,
                                                                                                       serverPlatformRootURL + urlTemplate,
                                                                                                       serverName,
                                                                                                       userId,
                                                                                                       discoveryReportGUID);

        return restResult.getAnalysisReport();
    }


    /**
     * Return the list of annotations from previous runs of the discovery service that are set to a specific status.
     * If status is null then annotations that have been reviewed, approved and/or actioned are returned from
     * discovery reports that are not waiting or in progress.
     *
     * @param userId calling user
     * @param assetGUID unique identifier of the asset
     * @param status status value to use on the query
     * @param startingFrom starting position in the list.
     * @param maximumResults maximum number of elements that can be returned
     * @return list of annotation (or null if none are registered)
     * @throws InvalidParameterException one of the parameters is invalid
     * @throws UserNotAuthorizedException the user id not authorized to issue this request
     * @throws PropertyServerException there was a problem retrieving annotations from the annotation store.
     */
    List<Annotation> getAnnotationsForAssetByStatus(String           userId,
                                                    String           assetGUID,
                                                    AnnotationStatus status,
                                                    int              startingFrom,
                                                    int              maximumResults) throws InvalidParameterException,
                                                                                            UserNotAuthorizedException,
                                                                                            PropertyServerException
    {
        final String   methodName = "getAnnotationsForAssetByStatus";
        final String   assetGUIDParameterName = "assetGUID";
        final String   urlTemplate = "/servers/{0}/open-metadata/access-services/discovery-engine/users/{1}/assets/{2}/annotations?startingFrom={3}&maximumResults={4}";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(assetGUID, assetGUIDParameterName, methodName);
        invalidParameterHandler.validatePaging(startingFrom, maximumResults, methodName);

        StatusRequestBody requestBody = new StatusRequestBody();

        requestBody.setAnnotationStatus(status);

        AnnotationListResponse restResult = restClient.callAnnotationListPostRESTCall(methodName,
                                                                                      serverPlatformRootURL + urlTemplate,
                                                                                      requestBody,
                                                                                      serverName,
                                                                                      userId,
                                                                                      assetGUID,
                                                                                      Integer.toString(startingFrom),
                                                                                      Integer.toString(maximumResults));

        return restResult.getAnnotations();
    }


    /**
     * Return the annotations linked directly to the report.
     *
     * @param userId identifier of calling user
     * @param discoveryReportGUID identifier of the discovery request.
     * @param startingFrom initial position in the stored list.
     * @param maximumResults maximum number of definitions to return on this call.
     *
     * @return list of annotations
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException there was a problem that occurred within the property server.
     */
    public List<Annotation> getDiscoveryReportAnnotations(String   userId,
                                                          String   discoveryReportGUID,
                                                          int      startingFrom,
                                                          int      maximumResults) throws InvalidParameterException,
                                                                                          UserNotAuthorizedException,
                                                                                          PropertyServerException
    {
        final String   methodName = "getDiscoveryReportAnnotations";

        return this.getDiscoveryReportAnnotations(userId, discoveryReportGUID, startingFrom, maximumResults, methodName);
    }


    /**
     * Return the annotations linked directly to the report.
     *
     * @param userId identifier of calling user
     * @param discoveryReportGUID identifier of the discovery request.
     * @param startingFrom initial position in the stored list.
     * @param maximumResults maximum number of definitions to return on this call.
     * @param methodName calling method
     *
     * @return list of annotations
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException there was a problem that occurred within the property server.
     */
    List<Annotation> getDiscoveryReportAnnotations(String   userId,
                                                   String   discoveryReportGUID,
                                                   int      startingFrom,
                                                   int      maximumResults,
                                                   String   methodName) throws InvalidParameterException,
                                                                               UserNotAuthorizedException,
                                                                               PropertyServerException
    {
        final String   reportGUIDParameterName = "discoveryReportGUID";
        final String   urlTemplate = "/servers/{0}/open-metadata/access-services/discovery-engine/users/{1}/discovery-analysis-reports/{2}/annotations?startingFrom={3}&maximumResults={4}";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(discoveryReportGUID, reportGUIDParameterName, methodName);
        invalidParameterHandler.validatePaging(startingFrom, maximumResults, methodName);

        AnnotationListResponse restResult = restClient.callAnnotationListGetRESTCall(methodName,
                                                                                     serverPlatformRootURL + urlTemplate,
                                                                                     serverName,
                                                                                     userId,
                                                                                     discoveryReportGUID,
                                                                                     Integer.toString(startingFrom),
                                                                                     Integer.toString(maximumResults));

        return restResult.getAnnotations();
    }


    /**
     * Return any annotations attached to this annotation.
     *
     * @param userId identifier of calling user
     * @param discoveryReportGUID identifier of the discovery report.
     * @param annotationGUID anchor annotation
     * @param startingFrom starting position in the list
     * @param maximumResults maximum number of annotations that can be returned.
     *
     * @return list of Annotation objects
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException there was a problem that occurred within the property server.
     */
    public  List<Annotation>  getExtendedAnnotations(String   userId,
                                                     String   discoveryReportGUID,
                                                     String   annotationGUID,
                                                     int      startingFrom,
                                                     int      maximumResults) throws InvalidParameterException,
                                                                                     UserNotAuthorizedException,
                                                                                     PropertyServerException
    {
        final String   methodName = "getExtendedAnnotations";
        final String   annotationGUIDParameterName = "annotationGUID";
        final String   urlTemplate = "/servers/{0}/open-metadata/access-services/discovery-engine/users/{1}/annotations/{2}/extended-annotations?startingFrom={3}&maximumResults={4}";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(annotationGUID, annotationGUIDParameterName, methodName);
        invalidParameterHandler.validatePaging(startingFrom, maximumResults, methodName);

        AnnotationListResponse restResult = restClient.callAnnotationListGetRESTCall(methodName,
                                                                                     serverPlatformRootURL + urlTemplate,
                                                                                     serverName,
                                                                                     userId,
                                                                                     annotationGUID,
                                                                                     Integer.toString(startingFrom),
                                                                                     Integer.toString(maximumResults));

        return restResult.getAnnotations();
    }


    /**
     * Retrieve a single annotation by unique identifier.  This call is typically used to retrieve the latest values
     * for an annotation.
     *
     * @param userId identifier of calling user
     * @param discoveryReportGUID identifier of the discovery report.
     * @param annotationGUID unique identifier of the annotation
     *
     * @return Annotation object
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException there was a problem that occurred within the property server.
     */
    public  Annotation        getAnnotation(String   userId,
                                            String   discoveryReportGUID,
                                            String   annotationGUID) throws InvalidParameterException,
                                                                            UserNotAuthorizedException,
                                                                            PropertyServerException
    {
        final String   methodName = "getAnnotation";
        final String   annotationGUIDParameterName = "annotationGUID";
        final String   urlTemplate = "/servers/{0}/open-metadata/access-services/discovery-engine/users/{1}/annotations/{2}";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(annotationGUID, annotationGUIDParameterName, methodName);

        AnnotationResponse restResult = restClient.callAnnotationGetRESTCall(methodName,
                                                                             serverPlatformRootURL + urlTemplate,
                                                                             serverName,
                                                                             userId,
                                                                             annotationGUID);

        return restResult.getAnnotation();
    }


    /**
     * Add a new annotation to the annotation store as a top level annotation linked directly off of the report.
     *
     * @param userId identifier of calling user
     * @param discoveryReportGUID unique identifier of the discovery analysis report
     * @param annotation annotation object
     * @return unique identifier of new annotation
     * @throws InvalidParameterException the annotation is invalid
     * @throws UserNotAuthorizedException the user id not authorized to issue this request
     * @throws PropertyServerException there was a problem adding the annotation to the annotation store.
     */
    String  addAnnotationToDiscoveryReport(String     userId,
                                           String     discoveryReportGUID,
                                           Annotation annotation) throws InvalidParameterException,
                                                                         UserNotAuthorizedException,
                                                                         PropertyServerException
    {
        final String   methodName = "addAnnotationToDiscoveryReport";
        final String   annotationParameterName = "annotation";
        final String   reportGUIDParameterName = "discoveryReportGUID";
        final String   urlTemplate = "/servers/{0}/open-metadata/access-services/discovery-engine/users/{1}/discovery-analysis-reports/{2}/annotations";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(discoveryReportGUID, reportGUIDParameterName, methodName);
        invalidParameterHandler.validateObject(annotation, annotationParameterName, methodName);

        GUIDResponse restResult = restClient.callGUIDPostRESTCall(methodName,
                                                                  serverPlatformRootURL + urlTemplate,
                                                                  annotation,
                                                                  serverName,
                                                                  userId,
                                                                  discoveryReportGUID);

        return restResult.getGUID();
    }


    /**
     * Add a new annotation and link it to an existing annotation.
     *
     * @param userId identifier of calling user
     * @param discoveryReportGUID identifier of the discovery report.
     * @param anchorAnnotationGUID unique identifier of the annotation that this new one is to be attached to
     * @param annotation annotation object
     * @return unique identifier of new annotation
     * @throws InvalidParameterException one of the parameters is invalid
     * @throws UserNotAuthorizedException the user id not authorized to issue this request
     * @throws PropertyServerException there was a problem saving annotations in the annotation store.
     */
    String  addAnnotationToAnnotation(String     userId,
                                      String     discoveryReportGUID,
                                      String     anchorAnnotationGUID,
                                      Annotation annotation) throws InvalidParameterException,
                                                                    UserNotAuthorizedException,
                                                                    PropertyServerException
    {
        final String   methodName = "addAnnotationToAnnotation";
        final String   annotationGUIDParameterName = "anchorAnnotationGUID";
        final String   annotationParameterName = "annotation";
        final String   urlTemplate = "/servers/{0}/open-metadata/access-services/discovery-engine/users/{1}/discovery-analysis-reports/{2}/annotations/{3}/extended-annotations";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(anchorAnnotationGUID, annotationGUIDParameterName, methodName);
        invalidParameterHandler.validateObject(annotation, annotationParameterName, methodName);

        GUIDResponse restResult = restClient.callGUIDPostRESTCall(methodName,
                                                                              serverPlatformRootURL + urlTemplate,
                                                                              annotation,
                                                                              serverName,
                                                                              userId,
                                                                              discoveryReportGUID,
                                                                              anchorAnnotationGUID);

        return restResult.getGUID();
    }


    /**
     * Link an existing annotation to another object.  The anchor object must be a Referenceable.
     *
     * @param userId identifier of calling user
     * @param discoveryReportGUID identifier of the discovery report.
     * @param anchorGUID unique identifier that the annotation is to be linked to
     * @param annotationGUID unique identifier of the annotation
     * @throws InvalidParameterException one of the parameters is invalid
     * @throws UserNotAuthorizedException the user id not authorized to issue this request
     * @throws PropertyServerException there was a problem updating annotations in the annotation store.
     */
    void    linkAnnotation(String userId,
                           String discoveryReportGUID,
                           String anchorGUID,
                           String annotationGUID) throws InvalidParameterException,
                                                         UserNotAuthorizedException,
                                                         PropertyServerException
    {
        final String   methodName = "linkAnnotation";
        final String   anchorGUIDParameterName = "anchorGUID";
        final String   annotationGUIDParameterName = "annotationGUID";
        final String   urlTemplate = "/servers/{0}/open-metadata/access-services/discovery-engine/users/{1}/discovery-analysis-reports/{2}/annotations/{3}/related-instances/{4}";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(anchorGUID, anchorGUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(annotationGUID, annotationGUIDParameterName, methodName);

        restClient.callVoidPostRESTCall(methodName,
                                        serverPlatformRootURL + urlTemplate,
                                        nullRequestBody,
                                        serverName,
                                        userId,
                                        discoveryReportGUID,
                                        annotationGUID,
                                        anchorGUID);
    }


    /**
     * Remove the relationship between an annotation and another object.
     *
     * @param userId identifier of calling user
     * @param discoveryReportGUID identifier of the discovery report.
     * @param anchorGUID unique identifier that the annotation is to be unlinked from
     * @param annotationGUID unique identifier of the annotation
     * @throws InvalidParameterException one of the parameters is invalid
     * @throws UserNotAuthorizedException the user id not authorized to issue this request
     * @throws PropertyServerException there was a problem updating annotations in the annotation store.
     */
    void    unlinkAnnotation(String userId,
                             String discoveryReportGUID,
                             String anchorGUID,
                             String annotationGUID) throws InvalidParameterException,
                                                           UserNotAuthorizedException,
                                                           PropertyServerException
    {
        final String   methodName = "unlinkAnnotation";
        final String   anchorGUIDParameterName = "anchorGUID";
        final String   annotationGUIDParameterName = "annotationGUID";
        final String   urlTemplate = "/servers/{0}/open-metadata/access-services/discovery-engine/users/{1}/discovery-analysis-reports/{2}/annotations/{3}/related-instances{4}/delete";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(anchorGUID, anchorGUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(annotationGUID, annotationGUIDParameterName, methodName);

        restClient.callVoidPostRESTCall(methodName,
                                        serverPlatformRootURL + urlTemplate,
                                        nullRequestBody,
                                        serverName,
                                        userId,
                                        discoveryReportGUID,
                                        annotationGUID,
                                        anchorGUID);
    }


    /**
     * Replace the current properties of an annotation.
     *
     * @param userId identifier of calling user
     * @param discoveryReportGUID identifier of the discovery report.
     * @param annotation new properties
     *
     * @return fully filled out annotation
     * @throws InvalidParameterException one of the parameters is invalid
     * @throws UserNotAuthorizedException the user id not authorized to issue this request
     * @throws PropertyServerException there was a problem updating the annotation in the annotation store.
     */
    Annotation  updateAnnotation(String     userId,
                                 String     discoveryReportGUID,
                                 Annotation annotation) throws InvalidParameterException,
                                                               UserNotAuthorizedException,
                                                               PropertyServerException
    {
        final String   methodName = "updateAnnotation";
        final String   annotationParameterName = "annotation";
        final String   annotationGUIDParameterName = "annotation.getGUID()";

        final String   urlTemplate = "/servers/{0}/open-metadata/access-services/discovery-engine/users/{1}/discovery-analysis-reports/{2}/annotations/{3}";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateObject(annotation, annotationParameterName, methodName);
        invalidParameterHandler.validateGUID(annotation.getGUID(), annotationGUIDParameterName, methodName);

        AnnotationResponse restResult = restClient.callAnnotationPostRESTCall(methodName,
                                                                              serverPlatformRootURL + urlTemplate,
                                                                              annotation,
                                                                              serverName,
                                                                              userId,
                                                                              discoveryReportGUID,
                                                                              annotation.getGUID());
        return restResult.getAnnotation();
    }


    /**
     * Remove an annotation from the annotation store.
     *
     * @param userId identifier of calling user
     * @param discoveryReportGUID identifier of the discovery report.
     * @param annotationGUID unique identifier of the annotation
     * @throws InvalidParameterException one of the parameters is invalid
     * @throws UserNotAuthorizedException the user id not authorized to issue this request
     * @throws PropertyServerException there was a problem deleting the annotation from the annotation store.
     */
    void  deleteAnnotation(String   userId,
                           String   discoveryReportGUID,
                           String   annotationGUID) throws InvalidParameterException,
                                                           UserNotAuthorizedException,
                                                           PropertyServerException
    {
        final String   methodName = "deleteAnnotation";
        final String   annotationGUIDParameterName = "annotationGUID";
        final String   urlTemplate = "/servers/{0}/open-metadata/access-services/discovery-engine/users/{1}/discovery-analysis-reports/{2}/annotations/{3}/delete";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(annotationGUID, annotationGUIDParameterName, methodName);

        restClient.callVoidPostRESTCall(methodName,
                                        serverPlatformRootURL + urlTemplate,
                                        nullRequestBody,
                                        serverName,
                                        userId,
                                        discoveryReportGUID,
                                        annotationGUID);
    }


    /**
     * Return the list of data fields from previous runs of the discovery service.
     *
     * @param startingFrom starting position in the list.
     * @param maximumResults maximum number of elements that can be returned
     * @return list of data fields (or null if none are registered)
     * @throws InvalidParameterException one of the parameters is invalid
     * @throws UserNotAuthorizedException the user id not authorized to issue this request
     * @throws PropertyServerException there was a problem retrieving data fields from the annotation store.
     */
    List<DataField>  getPreviousDataFieldsForAsset(String   userId,
                                                   String   discoveryReportGUID,
                                                   int      startingFrom,
                                                   int      maximumResults) throws InvalidParameterException,
                                                                                   UserNotAuthorizedException,
                                                                                   PropertyServerException
    {
        return null;
    }


    /**
     * Return the current list of data fields for this discovery run.
     *
     * @param startingFrom starting position in the list.
     * @param maximumResults maximum number of elements that can be returned
     * @return list of data fields (or null if none are registered)
     * @throws InvalidParameterException one of the parameters is invalid
     * @throws UserNotAuthorizedException the user id not authorized to issue this request
     * @throws PropertyServerException there was a problem retrieving data fields from the annotation store.
     */
    List<Annotation>  getNewDataFieldsForAsset(String    userId,
                                               String    discoveryReportGUID,
                                               int       startingFrom,
                                               int       maximumResults) throws InvalidParameterException,
                                                                                UserNotAuthorizedException,
                                                                                PropertyServerException
    {
        return null;
    }


    /**
     * Return any annotations attached to this annotation.
     *
     * @param anchorDataFieldGUID anchor data field identifier
     * @param startingFrom starting position in the list
     * @param maximumResults maximum number of annotations that can be returned.
     *
     * @return list of DataField objects
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException there was a problem that occurred within the property server.
     */
    List<DataField>  getNestedDataFields(String   userId,
                                         String   discoveryReportGUID,
                                         String   anchorDataFieldGUID,
                                         int      startingFrom,
                                         int      maximumResults) throws InvalidParameterException,
                                                                         UserNotAuthorizedException,
                                                                         PropertyServerException
    {
        return null;
    }


    /**
     * Return a specific data field stored in the annotation store (previous or new).
     *
     * @param dataFieldGUID unique identifier of the data field
     * @return data field object
     * @throws InvalidParameterException one of the parameters is invalid
     * @throws UserNotAuthorizedException the user id not authorized to issue this request
     * @throws PropertyServerException there was a problem retrieving the data field from the annotation store.
     */
    DataField  getDataField(String   userId,
                            String   discoveryReportGUID,
                            String   dataFieldGUID) throws InvalidParameterException,
                                                           UserNotAuthorizedException,
                                                           PropertyServerException
    {
        return null;
    }


    /**
     * Add a new data field to the Annotation store linked off of an annotation (typically SchemaAnalysisAnnotation).
     *
     * @param userId identifier of calling user
     * @param discoveryReportGUID identifier of the discovery report.
     * @param annotationGUID unique identifier of the annotation that the data field is to be linked to
     * @param dataField dataField object
     * @return unique identifier of new data field
     * @throws InvalidParameterException the dataField is invalid or the annotation GUID points to an annotation
     *                                   that can not be associated with a data field.
     * @throws UserNotAuthorizedException the user id not authorized to issue this request
     * @throws PropertyServerException there was a problem  adding the data field to the Annotation store.
     */
    String  addDataFieldToDiscoveryReport(String    userId,
                                          String    discoveryReportGUID,
                                          String    annotationGUID,
                                          DataField dataField) throws InvalidParameterException,
                                                                      UserNotAuthorizedException,
                                                                      PropertyServerException
    {
        return null;
    }


    /**
     * Add a new data field and link it to an existing data field.
     *
     * @param userId identifier of calling user
     * @param discoveryReportGUID identifier of the discovery report.
     * @param anchorDataFieldGUID unique identifier of the data field that this new one is to be attached to
     * @param dataField data field object
     * @return unique identifier of new data field
     * @throws InvalidParameterException one of the parameters is invalid
     * @throws UserNotAuthorizedException the user id not authorized to issue this request
     * @throws PropertyServerException there was a problem saving data fields in the annotation store.
     */
    String  addDataFieldToDataField(String    userId,
                                    String    discoveryReportGUID,
                                    String    anchorDataFieldGUID,
                                    DataField dataField) throws InvalidParameterException,
                                                                UserNotAuthorizedException,
                                                                PropertyServerException
    {
        return null;
    }


    /**
     * Add a new annotation and link it to an existing data field.
     *
     * @param userId identifier of calling user
     * @param discoveryReportGUID identifier of the discovery report.
     * @param anchorDataFieldGUID unique identifier of the data field that this new one is to be attached to
     * @param annotation data field object
     * @return unique identifier of new annotation
     * @throws InvalidParameterException one of the parameters is invalid
     * @throws UserNotAuthorizedException the user id not authorized to issue this request
     * @throws PropertyServerException there was a problem saving data fields in the annotation store.
     */
    String  addAnnotationToDataField(String     userId,
                                     String     discoveryReportGUID,
                                     String     anchorDataFieldGUID,
                                     Annotation annotation) throws InvalidParameterException,
                                                                   UserNotAuthorizedException,
                                                                   PropertyServerException
    {
        return null;
    }


    /**
     * Replace the current properties of a data field.
     *
     * @param userId identifier of calling user
     * @param discoveryReportGUID identifier of the discovery report.
     * @param dataField new properties
     *
     * @return fully filled out data field
     * @throws InvalidParameterException one of the parameters is invalid
     * @throws UserNotAuthorizedException the user id not authorized to issue this request
     * @throws PropertyServerException there was a problem updating the data field in the annotation store.
     */
    DataField  updateDataField(String    userId,
                               String    discoveryReportGUID,
                               DataField dataField) throws InvalidParameterException,
                                                           UserNotAuthorizedException,
                                                           PropertyServerException
    {
        return null;
    }


    /**
     * Remove a data field from the annotation store.
     *
     * @param userId identifier of calling user
     * @param discoveryReportGUID identifier of the discovery report.
     * @param dataFieldGUID unique identifier of the data field
     * @throws InvalidParameterException one of the parameters is invalid
     * @throws UserNotAuthorizedException the user id not authorized to issue this request
     * @throws PropertyServerException there was a problem deleting the data field from the annotation store.
     */
    void  deleteDataField(String   userId,
                          String   discoveryReportGUID,
                          String   dataFieldGUID) throws InvalidParameterException,
                                                         UserNotAuthorizedException,
                                                         PropertyServerException
    {
    }
}
