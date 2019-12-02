/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetconsumer.server;

import org.odpi.openmetadata.accessservices.assetconsumer.handlers.*;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallLogger;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallToken;
import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.rest.GlossaryTermListResponse;
import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.rest.GlossaryTermResponse;
import org.odpi.openmetadata.accessservices.assetconsumer.rest.LogRecordRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
import org.odpi.openmetadata.commonservices.ffdc.rest.GUIDListResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.NullRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.VoidResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.GUIDResponse;
import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.handlers.*;
import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.rest.*;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Asset;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;


/**
 * The AssetConsumerRESTServices provides the server-side implementation of the Asset Consumer Open Metadata
 * Assess Service (OMAS).  This interface provides connections to assets and APIs for adding feedback
 * on the asset.
 */
public class AssetConsumerRESTServices
{
    private static AssetConsumerInstanceHandler   instanceHandler     = new AssetConsumerInstanceHandler();
    private        RESTExceptionHandler           restExceptionHandler = new RESTExceptionHandler();

    private static RESTCallLogger                  restCallLogger       = new RESTCallLogger(LoggerFactory.getLogger(AssetConsumerRESTServices.class),
                                                                                             instanceHandler.getServiceName());
    /**
     * Default constructor
     */
    public AssetConsumerRESTServices()
    {
    }


    /*
     * ===========================================
     * AssetConsumerAssetInterface
     * ===========================================
     */

    /**
     * Returns the unique identifier for the asset connected to the connection.
     *
     * @param serverName name of the server instances for this request
     * @param userId the userId of the requesting user.
     * @param connectionName  unique name for the connection.
     *
     * @return unique identifier of asset or
     * InvalidParameterException - one of the parameters is null or invalid or
     * PropertyServerException - there is a problem retrieving the connected asset properties from the property server or
     * UnrecognizedConnectionGUIDException - the supplied GUID is not recognized by the property server or
     * NoConnectedAssetException - there is no asset associated with this connection or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    public GUIDResponse getAssetForConnectionName(String   serverName,
                                                  String   userId,
                                                  String   connectionName)
    {
        final String        methodName = "getAssetForConnectionName";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GUIDResponse  response = new GUIDResponse();
        OMRSAuditLog  auditLog = null;

        try
        {
            AssetHandler handler = instanceHandler.getAssetHandler(userId, serverName, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            response.setGUID(handler.getAssetForConnectionName(userId, connectionName, methodName));
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureThrowable(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Return a list of assets with the requested search string in their name, qualified name
     * or description.  The search string is interpreted as a regular expression (RegEx).
     *
     * @param serverName name of the server instances for this request
     * @param userId calling user
     * @param searchString string to search for in text
     * @param startFrom starting element (used in paging through large result sets)
     * @param pageSize maximum number of results to return
     *
     * @return list of unique identifiers for assets that match the search string or
     * InvalidParameterException the searchString is invalid or
     * PropertyServerException there is a problem access in the property server or
     * UserNotAuthorizedException the user does not have access to the properties
     */
    public GUIDListResponse findAssets(String   serverName,
                                       String   userId,
                                       String   searchString,
                                       int      startFrom,
                                       int      pageSize)
    {
        final String methodName    = "findAssets";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GUIDListResponse response = new GUIDListResponse();
        OMRSAuditLog     auditLog = null;

        try
        {
            AssetHandler handler = instanceHandler.getAssetHandler(userId, serverName, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            response.setGUIDs(handler.findAssetGUIDs(userId, searchString, startFrom, pageSize, methodName));
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureThrowable(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Return a list of assets with the requested name.  This is an exact match search.
     *
     * @param serverName name of the server instances for this request
     * @param userId calling user
     * @param name name to search for
     * @param startFrom starting element (used in paging through large result sets)
     * @param pageSize maximum number of results to return
     *
     * @return list of unique identifiers for Assets with the requested name or
     * InvalidParameterException the name is invalid or
     * PropertyServerException there is a problem access in the property server or
     * UserNotAuthorizedException the user does not have access to the properties
     */
    public GUIDListResponse getAssetsByName(String   serverName,
                                            String   userId,
                                            String   name,
                                            int      startFrom,
                                            int      pageSize)
    {
        final String methodName = "getAssetsByName";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GUIDListResponse response = new GUIDListResponse();
        OMRSAuditLog     auditLog = null;

        try
        {
            AssetHandler handler = instanceHandler.getAssetHandler(userId, serverName, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            response.setGUIDs(handler.getAssetGUIDsByName(userId, name, startFrom, pageSize, methodName));
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureThrowable(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /*
     * ===========================================
     * AssetConsumer Connection Interface
     * ===========================================
     */


    /**
     * Returns the connection object corresponding to the supplied connection name.
     *
     * @param serverName name of the server instances for this request
     * @param userId userId of user making request.
     * @param name   this may be the qualifiedName or displayName of the connection.
     *
     * @return connection object or
     * InvalidParameterException - one of the parameters is null or invalid or
     * UnrecognizedConnectionNameException - there is no connection defined for this name or
     * AmbiguousConnectionNameException - there is more than one connection defined for this name or
     * PropertyServerException - there is a problem retrieving information from the property (metadata) server or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    public ConnectionResponse getConnectionByName(String   serverName,
                                                  String   userId,
                                                  String   name)
    {
        final String        methodName = "getConnectionByName";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        ConnectionResponse  response = new ConnectionResponse();
        OMRSAuditLog        auditLog = null;

        try
        {
            ConnectionHandler connectionHandler = instanceHandler.getConnectionHandler(userId, serverName, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            response.setConnection(connectionHandler.getConnectionByName(userId, name, methodName));
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureThrowable(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /*
     * ===========================================
     * AssetConsumerFeedbackInterface
     * ===========================================
     */


    /**
     * Adds a star rating and optional review text to the asset.
     *
     * @param serverName name of the server instances for this request
     * @param userId      String - userId of user making request.
     * @param guid        String - unique id for the asset.
     * @param requestBody containing the StarRating and user review of asset.
     *
     * @return void or
     * InvalidParameterException - one of the parameters is null or invalid or
     * PropertyServerException - there is a problem adding the asset properties to
     *                                   the metadata repository or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    public VoidResponse addRatingToAsset(String            serverName,
                                         String            userId,
                                         String            guid,
                                         RatingRequestBody requestBody)
    {
        final String        methodName = "addRatingToAsset";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse  response = new VoidResponse();
        OMRSAuditLog  auditLog = null;

        try
        {
            if (requestBody != null)
            {
                AssetHandler handler = instanceHandler.getAssetHandler(userId, serverName, methodName);

                auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
                handler.addRatingToAsset(userId,
                                         guid,
                                         requestBody.getStarRating(),
                                         requestBody.getReview(),
                                         requestBody.isPublic(),
                                         methodName);

            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
            }
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureThrowable(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Removes a star rating that was added to the asset by this user.
     *
     * @param serverName name of the server instances for this request
     * @param userId      String - userId of user making request.
     * @param guid        String - unique id for the rating object
     * @param requestBody null request body needed to satisfy the HTTP Post request
     *
     * @return void or
     * InvalidParameterException - one of the parameters is null or invalid or
     * PropertyServerException - there is a problem updating the asset properties in
     *                                   the metadata repository or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    public VoidResponse removeRatingFromAsset(String          serverName,
                                              String          userId,
                                              String          guid,
                                              NullRequestBody requestBody)
    {
        final String        methodName = "removeRatingFromAsset";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse  response = new VoidResponse();
        OMRSAuditLog  auditLog = null;

        try
        {
            AssetHandler handler = instanceHandler.getAssetHandler(userId, serverName, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            handler.removeRatingFromAsset(userId, guid, methodName);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureThrowable(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Adds a "Like" to the asset.
     *
     * @param serverName name of the server instances for this request
     * @param userId      String - userId of user making request.
     * @param guid        String - unique id for the asset.
     * @param requestBody feedback request body .
     *
     * @return void or
     * InvalidParameterException - one of the parameters is null or invalid or
     * PropertyServerException - there is a problem adding the asset properties to
     *                                   the metadata repository or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    public VoidResponse addLikeToAsset(String              serverName,
                                       String              userId,
                                       String              guid,
                                       FeedbackRequestBody requestBody)
    {
        final String        methodName = "addLikeToAsset";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse  response = new VoidResponse();
        OMRSAuditLog  auditLog = null;

        try
        {
            if (requestBody != null)
            {
                AssetHandler handler = instanceHandler.getAssetHandler(userId, serverName, methodName);

                auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
                handler.addLikeToAsset(userId, guid, requestBody.isPublic(), methodName);
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
            }
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureThrowable(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Removes a "Like" added to the asset by this user.
     *
     * @param serverName name of the server instances for this request
     * @param userId  String - userId of user making request.
     * @param guid  String - unique id for the like object
     * @param requestBody null request body needed to satisfy the HTTP Post request
     *
     * @return void or
     * InvalidParameterException - one of the parameters is null or invalid or
     * PropertyServerException - there is a problem updating the asset properties in
     *                                   the metadata repository or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    public VoidResponse removeLikeFromAsset(String          serverName,
                                            String          userId,
                                            String          guid,
                                            NullRequestBody requestBody)
    {
        final String        methodName = "removeLikeFromAsset";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse  response = new VoidResponse();
        OMRSAuditLog  auditLog = null;

        try
        {
            AssetHandler handler = instanceHandler.getAssetHandler(userId, serverName, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            handler.removeLikeFromAsset(userId, guid, methodName);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureThrowable(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Adds a comment to the asset.
     *
     * @param serverName name of the server instances for this request
     * @param userId  String - userId of user making request.
     * @param guid  String - unique id for the asset.
     * @param requestBody containing type of comment enum and the text of the comment.
     *
     * @return guid for new comment object or
     * InvalidParameterException - one of the parameters is null or invalid or
     * PropertyServerException - there is a problem adding the asset properties to
     *                                   the metadata repository or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    public GUIDResponse addCommentToAsset(String             serverName,
                                          String             userId,
                                          String             guid,
                                          CommentRequestBody requestBody)
    {
        final String        methodName = "addCommentToAsset";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GUIDResponse  response = new GUIDResponse();
        OMRSAuditLog  auditLog = null;

        try
        {
            if (requestBody != null)
            {
                AssetHandler handler = instanceHandler.getAssetHandler(userId, serverName, methodName);

                auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
                response.setGUID(handler.addCommentToAsset(userId,
                                                           guid,
                                                           requestBody.getCommentType(),
                                                           requestBody.getCommentText(),
                                                           requestBody.isPublic(),
                                                           methodName));
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
            }
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureThrowable(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Adds a reply to a comment.
     *
     * @param serverName name of the server instances for this request
     * @param userId       String - userId of user making request.
     * @param assetGUID     String - unique id of asset that this chain of comments is linked.
     * @param commentGUID  String - unique id for an existing comment.  Used to add a reply to a comment.
     * @param requestBody  containing type of comment enum and the text of the comment.
     *
     * @return guid for new comment object or
     * InvalidParameterException - one of the parameters is null or invalid or
     * PropertyServerException - there is a problem adding the asset properties to
     *                                   the metadata repository or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    public GUIDResponse addCommentReply(String             serverName,
                                        String             userId,
                                        String             assetGUID,
                                        String             commentGUID,
                                        CommentRequestBody requestBody)
    {
        final String        methodName = "addCommentReply";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GUIDResponse  response = new GUIDResponse();
        OMRSAuditLog  auditLog = null;

        try
        {
            if (requestBody != null)
            {
                AssetHandler handler = instanceHandler.getAssetHandler(userId, serverName, methodName);

                auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
                response.setGUID(handler.addCommentReply(userId,
                                                         assetGUID,
                                                         commentGUID,
                                                         requestBody.getCommentType(),
                                                         requestBody.getCommentText(),
                                                         requestBody.isPublic(),
                                                         methodName));
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
            }
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureThrowable(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Update an existing comment.
     *
     * @param serverName   name of the server instances for this request.
     * @param userId       userId of user making request.
     * @param assetGUID    unique identifier for the asset that the comment is attached to (directly or indirectly).
     * @param commentGUID  unique identifier for the comment to change.
     * @param requestBody  containing type of comment enum and the text of the comment.
     *
     * @return void or
     * InvalidParameterException one of the parameters is null or invalid.
     * PropertyServerException There is a problem updating the asset properties in the metadata repository.
     * UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse   updateComment(String              serverName,
                                        String              userId,
                                        String              assetGUID,
                                        String              commentGUID,
                                        CommentRequestBody  requestBody)
    {
        final String        methodName = "updateComment";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse  response = new VoidResponse();
        OMRSAuditLog  auditLog = null;

        try
        {
            if (requestBody != null)
            {
                AssetHandler handler = instanceHandler.getAssetHandler(userId, serverName, methodName);

                auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
                handler.updateAssetComment(userId,
                                           assetGUID,
                                           commentGUID,
                                           requestBody.getCommentType(),
                                           requestBody.getCommentText(),
                                           requestBody.isPublic(),
                                           methodName);
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
            }
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureThrowable(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Removes a comment added to the asset by this user.
     *
     * @param serverName name of the server instances for this request
     * @param userId  String - userId of user making request.
     * @param assetGUID  String - unique id for the asset object
     * @param commentGUID  String - unique id for the comment object
     * @param requestBody null request body needed to satisfy the HTTP Post request
     *
     * @return void or
     * InvalidParameterException - one of the parameters is null or invalid or
     * PropertyServerException - there is a problem updating the asset properties in
     *                                   the metadata repository or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    public VoidResponse removeCommentFromAsset(String          serverName,
                                               String          userId,
                                               String          assetGUID,
                                               String          commentGUID,
                                               NullRequestBody requestBody)
    {
        final String        methodName = "removeAssetComment";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse  response = new VoidResponse();
        OMRSAuditLog  auditLog = null;

        try
        {
            AssetHandler handler = instanceHandler.getAssetHandler(userId, serverName, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            handler.removeAssetComment(userId, assetGUID, commentGUID, methodName);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureThrowable(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /*
     * ===========================================
     * AssetConsumerGlossaryInterface
     * ===========================================
     */


    /**
     * Return the full definition (meaning) of a term using the unique identifier of the glossary term.
     *
     * @param serverName name of the server instances for this request
     * @param userId userId of the user making the request.
     * @param guid unique identifier of the meaning.
     *
     * @return glossary term object or
     * InvalidParameterException the userId is null or invalid or
     * NoProfileForUserException the user does not have a profile or
     * PropertyServerException there is a problem retrieving information from the property server(s) or
     * UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public GlossaryTermResponse getMeaning(String   serverName,
                                           String   userId,
                                           String   guid)
    {
        final String        methodName = "getMeaning";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GlossaryTermResponse response = new GlossaryTermResponse();
        OMRSAuditLog         auditLog = null;

        try
        {
            GlossaryTermHandler glossaryTermHandler = instanceHandler.getGlossaryTermHandler(userId, serverName, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            response.setGlossaryTerm(glossaryTermHandler.getMeaning(userId, guid, methodName));
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureThrowable(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Return the full definition (meaning) of the terms exactly matching the supplied name.
     *
     * @param serverName name of the server instances for this request
     * @param userId the name of the calling user.
     * @param term name of term.
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     *
     * @return list of glossary terms or
     * InvalidParameterException - one of the parameters is invalid or
     * PropertyServerException - there is a problem retrieving information from the property server(s) or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    public GlossaryTermListResponse getMeaningByName(String  serverName,
                                                     String  userId,
                                                     String  term,
                                                     int     startFrom,
                                                     int     pageSize)
    {
        final String  methodName = "getMeaningByName";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GlossaryTermListResponse response = new GlossaryTermListResponse();
        OMRSAuditLog             auditLog = null;

        try
        {
            GlossaryTermHandler glossaryTermHandler = instanceHandler.getGlossaryTermHandler(userId, serverName, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            response.setMeanings(glossaryTermHandler.getMeaningByName(userId, term, startFrom, pageSize, methodName));
            response.setStartingFromElement(startFrom);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureThrowable(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Return the full definition (meaning) of the terms matching the supplied name.
     *
     * @param serverName name of the server instances for this request
     * @param userId the name of the calling user.
     * @param term name of term.  This may include wild card characters.
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     *
     * @return list of glossary terms or
     * InvalidParameterException - one of the parameters is invalid or
     * PropertyServerException - there is a problem retrieving information from the property server(s) or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    public GlossaryTermListResponse findMeanings(String  serverName,
                                                 String  userId,
                                                 String  term,
                                                 int     startFrom,
                                                 int     pageSize)
    {
        final String methodName = "findMeanings";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GlossaryTermListResponse response = new GlossaryTermListResponse();
        OMRSAuditLog             auditLog = null;

        try
        {
            GlossaryTermHandler glossaryTermHandler = instanceHandler.getGlossaryTermHandler(userId, serverName, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            response.setMeanings(glossaryTermHandler.findMeanings(userId, term, startFrom, pageSize, methodName));
            response.setStartingFromElement(startFrom);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureThrowable(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }

    /**
     * Return the list of unique identifiers for assets that are linked to a specific (meaning) either directly or via
     * fields in the schema.
     *
     * @param serverName name of the server instances for this request
     * @param userId the name of the calling user.
     * @param termGUID unique identifier of term.
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     *
     * @return asset guid list or
     * InvalidParameterException the userId is null or invalid or
     * PropertyServerException there is a problem retrieving information from the property server(s) or
     * UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public GUIDListResponse getAssetsByMeaning(String serverName,
                                               String userId,
                                               String termGUID,
                                               int    startFrom,
                                               int    pageSize)
    {
        final String   methodName  = "getAssetsByMeaning";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GUIDListResponse  response = new GUIDListResponse();
        OMRSAuditLog      auditLog = null;

        try
        {
            AssetHandler   handler = instanceHandler.getAssetHandler(userId, serverName, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            response.setGUIDs(handler.getAssetsByMeaning(userId, termGUID, startFrom, pageSize, methodName));
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureThrowable(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /*
     * ===========================================
     * AssetConsumerLoggingInterface
     * ===========================================
     */


    /**
     * Creates an Audit log record for the asset.  This log record is stored in the Asset's Audit Log.
     *
     * @param serverName name of the server instances for this request
     * @param userId  String - userId of user making request.
     * @param guid  String - unique id for the asset.
     * @param requestBody containing:
     * connectorInstanceId  (String - (optional) id of connector in use (if any)),
     * connectionName  (String - (optional) name of the connection (extracted from the connector)),
     * connectorType  (String - (optional) type of connector in use (if any)),
     * contextId  (String - (optional) function name, or processId of the activity that the caller is performing),
     * message  (log record content).
     *
     * @return void or
     * InvalidParameterException - one of the parameters is null or invalid or
     * PropertyServerException - there is a problem adding the log message to the audit log for this asset or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    public VoidResponse addLogMessageToAsset(String               serverName,
                                             String               userId,
                                             String               guid,
                                             LogRecordRequestBody requestBody)
    {
        final String        methodName = "addLogMessageToAsset";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse  response = new VoidResponse();
        OMRSAuditLog  auditLog = null;


        try
        {
            if (requestBody != null)
            {
                LoggingHandler loggingHandler = instanceHandler.getLoggingHandler(userId, serverName, methodName);

                auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
                loggingHandler.addLogMessageToAsset(userId,
                                                    guid,
                                                    requestBody.getConnectorInstanceId(),
                                                    requestBody.getConnectionName(),
                                                    requestBody.getConnectorType(),
                                                    requestBody.getContextId(),
                                                    requestBody.getMessage());
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
            }
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureThrowable(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /*
     * ===========================================
     * AssetConsumerTaggingInterface
     * ===========================================
     */


    /**
     * Creates a new informal tag and returns the unique identifier for it.
     *
     * @param serverName   name of the server instances for this request
     * @param userId       userId of user making request
     * @param requestBody  contains the name of the tag and (optional) description of the tag
     *
     * @return guid for new tag or
     * InvalidParameterException - one of the parameters is invalid or
     * PropertyServerException - there is a problem retrieving information from the property server(s) or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    public GUIDResponse createTag(String         serverName,
                                   String         userId,
                                   TagRequestBody requestBody)
    {
        final String   methodName = "createTag";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GUIDResponse  response = new GUIDResponse();
        OMRSAuditLog  auditLog = null;

        try
        {
            if (requestBody != null)
            {
                InformalTagHandler handler = instanceHandler.getInformalTagHandler(userId, serverName, methodName);

                auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
                response.setGUID(handler.createTag(userId,
                                                   requestBody.getTagName(),
                                                   requestBody.getTagDescription(),
                                                   requestBody.isPublic(),
                                                   methodName));
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
            }
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureThrowable(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Updates the description of an existing tag (either private or public).
     *
     * @param serverName   name of the server instances for this request
     * @param userId       userId of user making request.
     * @param tagGUID      unique id for the tag.
     * @param requestBody  contains the name of the tag and (optional) description of the tag.
     *
     * @return void or
     * InvalidParameterException - one of the parameters is invalid or
     * PropertyServerException - there is a problem retrieving information from the property server(s) or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    public VoidResponse   updateTagDescription(String         serverName,
                                               String         userId,
                                               String         tagGUID,
                                               TagRequestBody requestBody)
    {
        final String   methodName = "updateTagDescription";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse  response = new VoidResponse();
        OMRSAuditLog  auditLog = null;

        try
        {
            if (requestBody != null)
            {
                InformalTagHandler   handler = instanceHandler.getInformalTagHandler(userId, serverName, methodName);

                auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
                handler.updateTagDescription(userId, tagGUID, requestBody.getTagDescription(), methodName);
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
            }
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureThrowable(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Removes a tag from the repository.  All of the relationships to referenceables are lost.
     *
     * @param serverName   name of the server instances for this request
     * @param userId    userId of user making request.
     * @param tagGUID   unique id for the tag.
     * @param requestBody  null request body.
     *
     * @return void or
     * InvalidParameterException - one of the parameters is invalid or
     * PropertyServerException - there is a problem retrieving information from the property server(s) or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    public VoidResponse   deleteTag(String          serverName,
                                    String          userId,
                                    String          tagGUID,
                                    NullRequestBody requestBody)
    {
        final String   methodName = "deleteTag";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse  response = new VoidResponse();
        OMRSAuditLog  auditLog = null;

        try
        {
            InformalTagHandler   handler = instanceHandler.getInformalTagHandler(userId, serverName, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            handler.deleteTag(userId, tagGUID, methodName);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureThrowable(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Return the tag for the supplied unique identifier (guid).
     *
     * @param serverName   name of the server instances for this request
     * @param userId userId of the user making the request.
     * @param guid unique identifier of the tag.
     *
     * @return Tag object or
     * InvalidParameterException - one of the parameters is invalid or
     * PropertyServerException - there is a problem retrieving information from the property server(s) or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    public TagResponse getTag(String serverName,
                              String userId,
                              String guid)
    {
        final String   methodName = "getTag";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        TagResponse  response = new TagResponse();
        OMRSAuditLog auditLog = null;

        try
        {
            InformalTagHandler   handler = instanceHandler.getInformalTagHandler(userId, serverName, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            response.setTag(handler.getTag(userId, guid, methodName));
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureThrowable(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Return the list of tags exactly matching the supplied name.
     *
     * @param serverName name of the server instances for this request
     * @param userId the name of the calling user.
     * @param tagName name of tag.
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     *
     * @return tag list or
     * InvalidParameterException - one of the parameters is invalid or
     * PropertyServerException - there is a problem retrieving information from the property server(s) or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    public TagsResponse getTagsByName(String serverName,
                                      String userId,
                                      String tagName,
                                      int    startFrom,
                                      int    pageSize)
    {
        final String   methodName = "getTagsByName";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        TagsResponse response = new TagsResponse();
        OMRSAuditLog auditLog = null;

        try
        {
            InformalTagHandler   handler = instanceHandler.getInformalTagHandler(userId, serverName, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            response.setTags(handler.getTagsByName(userId, tagName, startFrom, pageSize, methodName));
            response.setStartingFromElement(startFrom);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureThrowable(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Return the list of the calling user's private tags exactly matching the supplied name.
     *
     * @param serverName name of the server instances for this request
     * @param userId the name of the calling user.
     * @param tagName name of tag.
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     *
     * @return tag list or
     * InvalidParameterException - one of the parameters is invalid or
     * PropertyServerException - there is a problem retrieving information from the property server(s) or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    public TagsResponse getMyTagsByName(String serverName,
                                        String userId,
                                        String tagName,
                                        int    startFrom,
                                        int    pageSize)
    {
        final String   methodName = "getMyTagsByName";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        TagsResponse response = new TagsResponse();
        OMRSAuditLog auditLog = null;

        try
        {
            InformalTagHandler   handler = instanceHandler.getInformalTagHandler(userId, serverName, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            response.setTags(handler.getMyTagsByName(userId, tagName, startFrom, pageSize, methodName));
            response.setStartingFromElement(startFrom);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureThrowable(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Return the list of tags containing the supplied string in either the name or description.
     *
     * @param serverName name of the server instances for this request
     * @param userId the name of the calling user.
     * @param tagName name of tag.  This may include wild card characters.
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     *
     * @return tag list or
     * InvalidParameterException - one of the parameters is invalid or
     * PropertyServerException - there is a problem retrieving information from the property server(s) or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    public TagsResponse findTags(String serverName,
                                 String userId,
                                 String tagName,
                                 int    startFrom,
                                 int    pageSize)
    {
        final String   methodName = "findTags";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        TagsResponse response = new TagsResponse();
        OMRSAuditLog auditLog = null;

        try
        {
            InformalTagHandler   handler = instanceHandler.getInformalTagHandler(userId, serverName, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            response.setTags(handler.findTags(userId, tagName, startFrom, pageSize, methodName));
            response.setStartingFromElement(startFrom);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureThrowable(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Return the list of the calling user's private tags containing the supplied string in either the name or description.
     *
     * @param serverName name of the server instances for this request
     * @param userId the name of the calling user.
     * @param tagName name of tag.  This may include wild card characters.
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     *
     * @return tag list or
     * InvalidParameterException - one of the parameters is invalid or
     * PropertyServerException - there is a problem retrieving information from the property server(s) or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    public TagsResponse findMyTags(String serverName,
                                 String userId,
                                 String tagName,
                                 int    startFrom,
                                 int    pageSize)
    {
        final String   methodName = "findMyTags";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        TagsResponse response = new TagsResponse();
        OMRSAuditLog auditLog = null;

        try
        {
            InformalTagHandler   handler = instanceHandler.getInformalTagHandler(userId, serverName, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            response.setTags(handler.findMyTags(userId, tagName, startFrom, pageSize, methodName));
            response.setStartingFromElement(startFrom);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureThrowable(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Adds a tag (either private of public) to an asset.
     *
     * @param serverName   name of the server instances for this request
     * @param userId       userId of user making request.
     * @param assetGUID    unique id for the asset.
     * @param tagGUID      unique id of the tag.
     * @param requestBody  feedback request body.
     *
     * @return void or
     * InvalidParameterException - one of the parameters is invalid or
     * PropertyServerException - there is a problem retrieving information from the property server(s) or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    public VoidResponse   addTagToAsset(String              serverName,
                                        String              userId,
                                        String              assetGUID,
                                        String              tagGUID,
                                        FeedbackRequestBody requestBody)
    {
        final String   methodName  = "addTagToAsset";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        boolean  isPublic = false;

        if (requestBody != null)
        {
            isPublic = requestBody.isPublic();
        }

        VoidResponse  response = new VoidResponse();
        OMRSAuditLog  auditLog = null;

        try
        {
            AssetHandler   handler = instanceHandler.getAssetHandler(userId, serverName, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            handler.addTagToAsset(userId, assetGUID, tagGUID, isPublic, methodName);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureThrowable(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Removes a tag from the asset that was added by this user.
     *
     * @param serverName   name of the server instances for this request
     * @param userId    userId of user making request.
     * @param assetGUID unique id for the asset.
     * @param tagGUID   unique id for the tag.
     * @param requestBody  null request body needed for correct protocol exchange.
     *
     * @return void or
     * InvalidParameterException - one of the parameters is invalid or
     * PropertyServerException - there is a problem retrieving information from the property server(s) or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    public VoidResponse   removeTagFromAsset(String          serverName,
                                             String          userId,
                                             String          assetGUID,
                                             String          tagGUID,
                                             NullRequestBody requestBody)
    {
        final String   methodName  = "removeTagFromAsset";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse  response = new VoidResponse();
        OMRSAuditLog  auditLog = null;

        try
        {
            AssetHandler   handler = instanceHandler.getAssetHandler(userId, serverName, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            handler.removeTagFromAsset(userId, assetGUID, tagGUID, methodName);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureThrowable(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Return the list of unique identifiers for assets that are linked to a specific tag either directly, or via one
     * of its schema elements.
     *
     * @param serverName   name of the server instances for this request
     * @param userId the name of the calling user.
     * @param tagGUID unique identifier of tag.
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     *
     * @return asset guid list or
     * InvalidParameterException the userId is null or invalid or
     * PropertyServerException there is a problem retrieving information from the property server(s) or
     * UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public GUIDListResponse getAssetsByTag(String serverName,
                                           String userId,
                                           String tagGUID,
                                           int    startFrom,
                                           int    pageSize)
    {
        final String   methodName  = "getAssetsByTag";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GUIDListResponse  response = new GUIDListResponse();
        OMRSAuditLog      auditLog = null;

        try
        {
            AssetHandler   handler = instanceHandler.getAssetHandler(userId, serverName, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            response.setGUIDs(handler.getAssetsByTag(userId, tagGUID, startFrom, pageSize, methodName));
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureThrowable(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }
}