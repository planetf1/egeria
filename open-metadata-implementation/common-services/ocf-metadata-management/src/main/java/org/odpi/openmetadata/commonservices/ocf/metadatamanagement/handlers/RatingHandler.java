/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.ocf.metadatamanagement.handlers;

import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.builders.RatingBuilder;
import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.converters.RatingConverter;
import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.mappers.AssetMapper;
import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.mappers.RatingMapper;
import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.mappers.ReferenceableMapper;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryHandler;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Rating;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.StarRating;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityProxy;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.ArrayList;
import java.util.List;


/**
 * RatingHandler manages the Rating entity.  The Rating entity describes the star rating and review text
 * type of feedback
 */
public class RatingHandler
{
    private String                  serviceName;
    private String                  serverName;
    private OMRSRepositoryHelper    repositoryHelper;
    private RepositoryHandler       repositoryHandler;
    private InvalidParameterHandler invalidParameterHandler;


    /**
     * Construct the handler information needed to interact with the repository services
     *
     * @param serviceName      name of this service
     * @param serverName       name of the local server
     * @param invalidParameterHandler handler for managing parameter errors
     * @param repositoryHandler     manages calls to the repository services
     * @param repositoryHelper provides utilities for manipulating the repository services objects
     */
    public RatingHandler(String                  serviceName,
                         String                  serverName,
                         InvalidParameterHandler invalidParameterHandler,
                         RepositoryHandler       repositoryHandler,
                         OMRSRepositoryHelper    repositoryHelper)
    {
        this.serviceName = serviceName;
        this.serverName = serverName;
        this.invalidParameterHandler = invalidParameterHandler;
        this.repositoryHandler = repositoryHandler;
        this.repositoryHelper = repositoryHelper;
    }


    /**
     * Work out whether the requesting user is able to see the attached feedback.
     *
     * @param userId calling user
     * @param relationship relationship to the feedback content
     * @param methodName calling method
     * @return boolean - true if allowed
     */
    private boolean  visibleToUser(String        userId,
                                   Relationship  relationship,
                                   String        methodName)
    {
        if (userId.equals(relationship.getCreatedBy()))
        {
            return true;
        }

        return repositoryHelper.getBooleanProperty(serviceName,
                                                   RatingMapper.IS_PUBLIC_PROPERTY_NAME,
                                                   relationship.getProperties(),
                                                   methodName);
    }


    /**
     * Count the number of Ratings attached to an anchor entity.
     *
     * @param userId     calling user
     * @param anchorGUID identifier for the entity that the rating is attached to
     * @param methodName calling method
     * @return count of attached objects
     * @throws InvalidParameterException  the parameters are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    public int countRatings(String   userId,
                            String   anchorGUID,
                            String   methodName) throws InvalidParameterException,
                                                        PropertyServerException,
                                                        UserNotAuthorizedException
    {
        final String guidParameterName      = "anchorGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(anchorGUID, guidParameterName, methodName);

        return repositoryHandler.countAttachedRelationshipsByType(userId,
                                                                  anchorGUID,
                                                                  ReferenceableMapper.REFERENCEABLE_TYPE_NAME,
                                                                  RatingMapper.REFERENCEABLE_TO_RATING_TYPE_GUID,
                                                                  RatingMapper.REFERENCEABLE_TO_RATING_TYPE_NAME,
                                                                  methodName);
    }



    /**
     * Return the Ratings attached to an anchor entity.
     *
     * @param userId     calling user
     * @param anchorGUID identifier for the entity that the feedback is attached to
     * @param methodName calling method
     * @return list of objects or null if none found
     * @throws InvalidParameterException  the input properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    public List<Rating>  getRatings(String   userId,
                                    String   anchorGUID,
                                    int      startingFrom,
                                    int      pageSize,
                                    String   methodName) throws InvalidParameterException,
                                                                PropertyServerException,
                                                                UserNotAuthorizedException
    {
        final String guidParameterName      = "anchorGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(anchorGUID, guidParameterName, methodName);
        invalidParameterHandler.validatePaging(startingFrom, pageSize, methodName);

        List<Relationship>  relationships = repositoryHandler.getPagedRelationshipsByType(userId,
                                                                                          anchorGUID,
                                                                                          ReferenceableMapper.REFERENCEABLE_TYPE_NAME,
                                                                                          RatingMapper.REFERENCEABLE_TO_RATING_TYPE_GUID,
                                                                                          RatingMapper.REFERENCEABLE_TO_RATING_TYPE_NAME,
                                                                                          startingFrom,
                                                                                          pageSize,
                                                                                          methodName);
        if (relationships != null)
        {
            List<Rating>  results = new ArrayList<>();

            for (Relationship relationship : relationships)
            {
                if (relationship != null)
                {
                    if (this.visibleToUser(userId, relationship, methodName))
                    {
                        EntityProxy entityProxy = relationship.getEntityTwoProxy();

                        if (entityProxy != null)
                        {
                            final String  entityParameterName = "entityProxyTwo.getGUID";
                            EntityDetail entity = repositoryHandler.getEntityByGUID(userId,
                                                                                    entityProxy.getGUID(),
                                                                                    entityParameterName,
                                                                                    RatingMapper.RATING_TYPE_NAME,
                                                                                    methodName);

                            RatingConverter converter = new RatingConverter(entity,
                                                                            relationship,
                                                                            repositoryHelper,
                                                                            serviceName);
                            results.add(converter.getBean());
                        }
                    }
                }
            }

            if (results.isEmpty())
            {
                return null;
            }
            else
            {
                return results;
            }
        }

        return null;
    }


    /**
     * Add or replace and existing Rating for this user.
     *
     * @param userId      userId of user making request.
     * @param anchorGUID   unique identifier for the anchor entity (Referenceable).
     * @param starRating  StarRating enumeration for not recommended, one to five stars.
     * @param review      user review of asset.  This can be null.
     * @param isPublic   indicates whether the feedback should be shared or only be visible to the originating user
     *
     * @throws InvalidParameterException  the endpoint bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    public void   saveRating(String     userId,
                             String     anchorGUID,
                             StarRating starRating,
                             String     review,
                             boolean    isPublic) throws InvalidParameterException,
                                                          PropertyServerException,
                                                          UserNotAuthorizedException
    {
        final String methodName = "saveRating";

        this.removeRating(userId, anchorGUID);

        RatingBuilder builder = new RatingBuilder(starRating,
                                                  review,
                                                  isPublic,
                                                  repositoryHelper,
                                                  serviceName,
                                                  serverName);

        repositoryHandler.addUniqueAttachedEntityToAnchor(userId,
                                                          anchorGUID,
                                                          ReferenceableMapper.REFERENCEABLE_TYPE_NAME,
                                                          RatingMapper.REFERENCEABLE_TO_RATING_TYPE_GUID,
                                                          RatingMapper.REFERENCEABLE_TO_RATING_TYPE_NAME,
                                                          builder.getRelationshipInstanceProperties(methodName),
                                                          RatingMapper.RATING_TYPE_GUID,
                                                          RatingMapper.RATING_TYPE_NAME,
                                                          builder.getEntityInstanceProperties(methodName),
                                                          methodName);

    }


    /**
     * Remove the requested rating.
     *
     * @param userId       calling user
     * @param anchorGUID   anchor object where rating is attached
     * @throws InvalidParameterException  the entity guid is not known
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    public void removeRating(String userId,
                             String anchorGUID) throws InvalidParameterException,
                                                       PropertyServerException,
                                                       UserNotAuthorizedException
    {
        final String methodName        = "removeRating";

        repositoryHandler.deleteUniqueEntityTypeFromAnchor(userId,
                                                           anchorGUID,
                                                           ReferenceableMapper.REFERENCEABLE_TYPE_NAME,
                                                           RatingMapper.REFERENCEABLE_TO_RATING_TYPE_GUID,
                                                           RatingMapper.REFERENCEABLE_TO_RATING_TYPE_NAME,
                                                           RatingMapper.RATING_TYPE_GUID,
                                                           RatingMapper.RATING_TYPE_NAME,
                                                           methodName);
    }


    /**
     * Retrieve the requested rating object.
     *
     * @param userId       calling user
     * @param relationship relationship between referenceable and rating
     * @return new bean
     * @throws InvalidParameterException  the parameters are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    public Rating getRating(String       userId,
                            Relationship relationship) throws InvalidParameterException,
                                                              PropertyServerException,
                                                              UserNotAuthorizedException
    {
        final String methodName        = "getRating";
        final String guidParameterName = "referenceableRatingRelationship.end2.guid";

        if (relationship != null)
        {
            EntityProxy entityProxy = relationship.getEntityTwoProxy();

            if (entityProxy != null)
            {
                EntityDetail entity = repositoryHandler.getEntityByGUID(userId,
                                                                        entityProxy.getGUID(),
                                                                        guidParameterName,
                                                                        RatingMapper.RATING_TYPE_NAME,
                                                                        methodName);

                RatingConverter converter = new RatingConverter(entity,
                                                                relationship,
                                                                repositoryHelper,
                                                                serviceName);

                return converter.getBean();
            }
        }

        return null;
    }


    /**
     * Adds a star rating and optional review text to the asset.  If the user has already attached
     * a rating then the original one is over-ridden.
     *
     * @param userId      userId of user making request.
     * @param assetGUID   unique identifier for the asset.
     * @param starRating  StarRating enumeration for not recommended, one to five stars.
     * @param review      user review of asset.  This can be null.
     * @param isPublic    indicates whether the feedback should be shared or only be visible to the originating user
     * @param methodName  calling method
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem adding the asset properties to the property server.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void addRatingToAsset(String     userId,
                                 String     assetGUID,
                                 StarRating starRating,
                                 String     review,
                                 boolean    isPublic,
                                 String     methodName) throws InvalidParameterException,
                                                               PropertyServerException,
                                                               UserNotAuthorizedException
    {
        final String guidParameter = "assetGUID";
        final String ratingParameter = "starRating";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(assetGUID, guidParameter, methodName);
        invalidParameterHandler.validateEnum(starRating, ratingParameter, methodName);

        repositoryHandler.validateEntityGUID(userId, assetGUID, AssetMapper.ASSET_TYPE_NAME, methodName, guidParameter);

        this.saveRating(userId, assetGUID, starRating, review, isPublic);
    }


    /**
     * Removes of a review that was added to the asset by this user.
     *
     * @param userId      userId of user making request.
     * @param assetGUID   unique identifier for the asset where the rating is attached.
     * @param methodName  calling method
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem updating the asset properties in the property server.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void removeRatingFromAsset(String     userId,
                                      String     assetGUID,
                                      String     methodName) throws InvalidParameterException,
                                                                    PropertyServerException,
                                                                    UserNotAuthorizedException
    {
        final String guidParameter = "reviewGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(assetGUID, guidParameter, methodName);

        repositoryHandler.validateEntityGUID(userId, assetGUID, AssetMapper.ASSET_TYPE_NAME, methodName, guidParameter);

        this.removeRating(userId, assetGUID);
    }
}
