/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.utils;

import org.odpi.openmetadata.accessservices.subjectarea.ffdc.SubjectAreaErrorCode;
import org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.*;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.category.Category;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.category.SubjectAreaDefinition;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.glossary.Glossary;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.term.Term;
import org.odpi.openmetadata.accessservices.subjectarea.properties.relationships.Antonym;
import org.odpi.openmetadata.accessservices.subjectarea.properties.relationships.RelatedTermRelationship;
import org.odpi.openmetadata.accessservices.subjectarea.properties.relationships.Synonym;
import org.odpi.openmetadata.accessservices.subjectarea.properties.relationships.TermHASARelationship;
import org.odpi.openmetadata.accessservices.subjectarea.responses.*;

/**
 * Created by david on 10/08/2018.
 */
public class DetectUtils {
     private static  String className = "DetectUtils";
    /**
     * Throw an InvalidParameterException if it is encoded in the REST response.
     *
     * @param methodName - name of the method called
     * @param restResponse - response from the rest call.  This generated in the remote handlers.
     * @throws InvalidParameterException - encoded exception from the handlers
     */
    public static void detectAndThrowInvalidParameterException(String methodName,
                                                         SubjectAreaOMASAPIResponse restResponse) throws InvalidParameterException {
        if ((restResponse != null) && (restResponse.getResponseCategory() == ResponseCategory.InvalidParameterException)) {

            InvalidParameterExceptionResponse   invalidParameterExceptionResponse = ( InvalidParameterExceptionResponse) restResponse;
            throw new InvalidParameterException(restResponse.getRelatedHTTPCode(),
                    className,
                    methodName,
                    invalidParameterExceptionResponse.getExceptionErrorMessage(),
                    invalidParameterExceptionResponse.getExceptionSystemAction(),
                    invalidParameterExceptionResponse.getExceptionUserAction());
        }
    }

    /**
     * Throw an UnrecognizedGUIDException if it is encoded in the REST response.
     *
     * @param methodName - name of the method called
     * @param restResponse - response from the rest call.  This generated in the remote handlers.
     * @throws UnrecognizedGUIDException - encoded exception from the handlers
     */
    public static void detectAndThrowUnrecognizedGUIDException(String methodName,
                                                     SubjectAreaOMASAPIResponse restResponse) throws UnrecognizedGUIDException {
        if ((restResponse != null) && (restResponse.getResponseCategory() == ResponseCategory.UnrecognizedGUIDException)) {

            UnrecognizedGUIDExceptionResponse   unrecognizedGUIDExceptionResponse = ( UnrecognizedGUIDExceptionResponse) restResponse;
            throw new UnrecognizedGUIDException(restResponse.getRelatedHTTPCode(),
                    className,
                    methodName,
                    unrecognizedGUIDExceptionResponse.getExceptionErrorMessage(),
                    unrecognizedGUIDExceptionResponse.getExceptionSystemAction(),
                    unrecognizedGUIDExceptionResponse.getExceptionUserAction(),
                    unrecognizedGUIDExceptionResponse.getGuid()
            );
        }
    }

    /**
     * Throw an ClassificationException if it is encoded in the REST response.
     *
     * @param methodName - name of the method called
     * @param restResponse - response from the rest call.  This generated in the remote handlers.
     * @throws ClassificationException - encoded exception from the handlers
     */
    public static void detectAndThrowClassificationException(String methodName,
                                                        SubjectAreaOMASAPIResponse restResponse) throws ClassificationException {
        if ((restResponse != null) && (restResponse.getResponseCategory() == ResponseCategory.ClassificationException)) {

            ClassificationExceptionResponse   classificationExceptionResponse = ( ClassificationExceptionResponse) restResponse;
            throw new ClassificationException(restResponse.getRelatedHTTPCode(),
                    className,
                    methodName,
                    classificationExceptionResponse.getExceptionErrorMessage(),
            classificationExceptionResponse.getExceptionSystemAction(),
            classificationExceptionResponse.getExceptionUserAction());
        }
    }

    /**
     * Throw an EntityNotDeletedException if it is encoded in the REST response.
     *
     * @param methodName - name of the method called
     * @param restResponse - response from the rest call.  This generated in the remote handlers.
     * @throws EntityNotDeletedException - encoded exception from the handlers
     */
    public static void detectAndThrowEntityNotDeletedException(String methodName,
                                                      SubjectAreaOMASAPIResponse restResponse) throws EntityNotDeletedException {
        if ((restResponse != null) && (restResponse.getResponseCategory() == ResponseCategory.EntityNotDeletedException)) {

            EntityNotDeletedExceptionResponse   entityNotDeletedExceptionResponse = ( EntityNotDeletedExceptionResponse) restResponse;
            throw new EntityNotDeletedException(restResponse.getRelatedHTTPCode(),
                    className,
                    methodName,
                    entityNotDeletedExceptionResponse.getExceptionErrorMessage(),
                    entityNotDeletedExceptionResponse.getExceptionSystemAction(),
                    entityNotDeletedExceptionResponse.getExceptionUserAction(),
                    entityNotDeletedExceptionResponse.getGuid()
            );
        }
    }

    /**
     * Throw an FunctionNotSupportedException if it is encoded in the REST response.
     *
     * @param methodName - name of the method called
     * @param restResponse - response from the rest call.  This generated in the remote handlers.
     * @throws FunctionNotSupportedException - encoded exception from the handlers
     */
    public static void detectAndThrowFunctionNotSupportedException(String methodName,
                                                        SubjectAreaOMASAPIResponse restResponse) throws FunctionNotSupportedException {
        if ((restResponse != null) && (restResponse.getResponseCategory() == ResponseCategory.FunctionNotSupportedException)) {

            FunctionNotSupportedExceptionResponse   functionNotSupportedExceptionResponse = ( FunctionNotSupportedExceptionResponse) restResponse;
            throw new FunctionNotSupportedException(restResponse.getRelatedHTTPCode(),
                    className,
                    methodName,
                    functionNotSupportedExceptionResponse.getExceptionErrorMessage(),
            functionNotSupportedExceptionResponse.getExceptionSystemAction(),
            functionNotSupportedExceptionResponse.getExceptionUserAction());
        }
    }
    /**
     * Throw an GUIDNotPurgedException if it is encoded in the REST response.
     *
     * @param methodName - name of the method called
     * @param restResponse - response from the rest call.  This generated in the remote handlers.
     * @throws GUIDNotPurgedException - encoded exception from the handlers
     */
    public static void detectAndThrowGUIDNotPurgedException(String methodName,
                                                            SubjectAreaOMASAPIResponse restResponse) throws GUIDNotPurgedException {
        if ((restResponse != null) && (restResponse.getResponseCategory() == ResponseCategory.GUIDNotPurgedException)) {

            GUIDNotPurgedExceptionResponse   gUIDNotPurgedExceptionResponse = ( GUIDNotPurgedExceptionResponse) restResponse;
            throw new GUIDNotPurgedException(restResponse.getRelatedHTTPCode(),
                    className,
                    methodName,
                    gUIDNotPurgedExceptionResponse.getExceptionErrorMessage(),
                    gUIDNotPurgedExceptionResponse.getExceptionSystemAction(),
                    gUIDNotPurgedExceptionResponse.getExceptionUserAction(),
                    gUIDNotPurgedExceptionResponse.getGuid()
            );
        }
    }

    /**
     * Throw an RelationshipNotDeletedException if it is encoded in the REST response.
     *
     * @param methodName - name of the method called
     * @param restResponse - response from the rest call.  This generated in the remote handlers.
     * @throws RelationshipNotDeletedException - encoded exception from the handlers
     */
    public static void detectAndThrowRelationshipNotDeletedException(String methodName,
                                                            SubjectAreaOMASAPIResponse restResponse) throws RelationshipNotDeletedException {
        if ((restResponse != null) && (restResponse.getResponseCategory() == ResponseCategory.RelationshipNotDeletedException)) {

            RelationshipNotDeletedExceptionResponse   relationshipNotDeletedExceptionResponse = ( RelationshipNotDeletedExceptionResponse) restResponse;
            throw new RelationshipNotDeletedException(restResponse.getRelatedHTTPCode(),
                    className,
                    methodName,
                    relationshipNotDeletedExceptionResponse.getExceptionErrorMessage(),
                    relationshipNotDeletedExceptionResponse.getExceptionSystemAction(),
                    relationshipNotDeletedExceptionResponse.getExceptionUserAction(),
                    relationshipNotDeletedExceptionResponse.getGuid()
                    );
        }
    }

    /**
     * Throw an StatusNotSupportedException if it is encoded in the REST response.
     *
     * @param methodName - name of the method called
     * @param restResponse - response from the rest call.  This generated in the remote handlers.
     * @throws StatusNotSupportedException - encoded exception from the handlers
     */
    public static void detectAndThrowStatusNotSupportedException(String methodName,
                                                            SubjectAreaOMASAPIResponse restResponse) throws StatusNotSupportedException {
        if ((restResponse != null) && (restResponse.getResponseCategory() == ResponseCategory.StatusNotSupportedException)) {

            StatusNotsupportedExceptionResponse   statusNotSupportedExceptionResponse = ( StatusNotsupportedExceptionResponse) restResponse;
            throw new StatusNotSupportedException(restResponse.getRelatedHTTPCode(),
                    className,
                    methodName,
                    statusNotSupportedExceptionResponse.getExceptionErrorMessage(),
                    statusNotSupportedExceptionResponse.getExceptionSystemAction(),
                    statusNotSupportedExceptionResponse.getExceptionUserAction());
        }
    }

    /**
     * Throw an UnrecognizedNameException if it is encoded in the REST response.
     *
     * @param methodName - name of the method called
     * @param restResponse - response from the rest call.  This generated in the remote handlers.
     * @throws UnrecognizedNameException - encoded exception from the handlers
     */
    public static void detectAndThrowUnrecognizedNameException(String methodName,
                                                            SubjectAreaOMASAPIResponse restResponse) throws UnrecognizedNameException {
        if ((restResponse != null) && (restResponse.getResponseCategory() == ResponseCategory.UnrecognizedNameException)) {

            UnrecognizedNameExceptionResponse   unrecognizedNameExceptionResponse = ( UnrecognizedNameExceptionResponse) restResponse;
            throw new UnrecognizedNameException(restResponse.getRelatedHTTPCode(),
                    className,
                    methodName,
                    unrecognizedNameExceptionResponse.getExceptionErrorMessage(),
                    unrecognizedNameExceptionResponse.getExceptionSystemAction(),
                    unrecognizedNameExceptionResponse.getExceptionUserAction(),
                    unrecognizedNameExceptionResponse.getName()
                    );
        }
    }

    /**
     * Throw an UserNotAuthorizedException if it is encoded in the REST response.
     *
     * @param methodName - name of the method called
     * @param restResponse - response from the rest call.  This generated in the remote handlers.
     * @throws UserNotAuthorizedException - encoded exception from the handlers
     */
    public static void detectAndThrowUserNotAuthorizedException(String methodName,
                                                            SubjectAreaOMASAPIResponse restResponse) throws UserNotAuthorizedException {
        if ((restResponse != null) && (restResponse.getResponseCategory() == ResponseCategory.UserNotAuthorizedException)) {

            UserNotAuthorizedExceptionResponse   userNotAuthorizedExceptionResponse = ( UserNotAuthorizedExceptionResponse) restResponse;
            throw new UserNotAuthorizedException(restResponse.getRelatedHTTPCode(),
                    className,
                    methodName,
                    userNotAuthorizedExceptionResponse.getExceptionErrorMessage(),
                    userNotAuthorizedExceptionResponse.getExceptionSystemAction(),
                    userNotAuthorizedExceptionResponse.getExceptionUserAction(),
                    userNotAuthorizedExceptionResponse.getUserId()

                    );
        }
    }
    /**
     * Detect Void return. If we do not find one then throw an Exception
     * @param methodName - name of the method called
     * @param restResponse - response from the rest call.  This generated in the remote handlers.
     * @throws UnexpectedResponseException - if the response is not a glossary then throw this exception
     */
    public static void detectVoid(String methodName,
                                                   SubjectAreaOMASAPIResponse restResponse) throws UnexpectedResponseException {
        Glossary glossary = null;
        if ((restResponse != null) && !(restResponse.getResponseCategory() == ResponseCategory.Void)) {
            SubjectAreaErrorCode errorCode = SubjectAreaErrorCode.CLIENT_RECEIVED_AN_UNEXPECTED_RESPONSE_ERROR;
            String unexpectedResponseCategory = restResponse.getResponseCategory().name();
            String errorMessage = errorCode.getErrorMessageId()
                    + errorCode.getFormattedErrorMessage(
                    unexpectedResponseCategory
            );
            throw new UnexpectedResponseException(errorCode.getHTTPErrorCode(),
                    className,
                    methodName,
                    errorMessage,
                    errorCode.getSystemAction(),
                    errorCode.getUserAction(),
                    unexpectedResponseCategory
            );
        }
    }

    /**
     * Detect and return a Glossary object from the supplied response. If we do not find one then throw an Exception
     * @param methodName - name of the method called
     * @param restResponse - response from the rest call.  This generated in the remote handlers.
     * @return Glossary if the supplied response is a glossary response
     * @throws UnexpectedResponseException - if the response is not a glossary then throw this exception
     */
    public static Glossary detectAndReturnGlossary(String methodName,
                                                           SubjectAreaOMASAPIResponse restResponse) throws UnexpectedResponseException {
        Glossary glossary = null;
        if ((restResponse != null) && (restResponse.getResponseCategory() == ResponseCategory.Glossary)) {
            GlossaryResponse glossaryResponse = (GlossaryResponse)restResponse;
            glossary = glossaryResponse.getGlossary();
        } else {
            SubjectAreaErrorCode errorCode = SubjectAreaErrorCode.CLIENT_RECEIVED_AN_UNEXPECTED_RESPONSE_ERROR;
            String unexpectedResponseCategory = restResponse.getResponseCategory().name();
            String errorMessage = errorCode.getErrorMessageId()
                    + errorCode.getFormattedErrorMessage(
                    unexpectedResponseCategory
            );
            throw new UnexpectedResponseException(errorCode.getHTTPErrorCode(),
                    className,
                    methodName,
                    errorMessage,
                    errorCode.getSystemAction(),
                    errorCode.getUserAction(),
                    unexpectedResponseCategory
            );
        }
        return glossary;
    }
    /**
     * Detect and return a Term object from the supplied response. If we do not find one then throw an Exception
     * @param methodName - name of the method called
     * @param restResponse - response from the rest call.  This generated in the remote handlers.
     * @return Term if the supplied response is a term response
     * @throws UnexpectedResponseException - if the response is not a Term then throw this exception
     */
    public static Term detectAndReturnTerm(String methodName,
                                                   SubjectAreaOMASAPIResponse restResponse) throws UnexpectedResponseException {
        Term term = null;
        if ((restResponse != null) && (restResponse.getResponseCategory() == ResponseCategory.Term)) {
            TermResponse termResponse = (TermResponse)restResponse;
            term = termResponse.getTerm();
        } else {
            SubjectAreaErrorCode errorCode = SubjectAreaErrorCode.CLIENT_RECEIVED_AN_UNEXPECTED_RESPONSE_ERROR;
            String unexpectedResponseCategory = restResponse.getResponseCategory().name();
            String errorMessage = errorCode.getErrorMessageId()
                    + errorCode.getFormattedErrorMessage(
                    unexpectedResponseCategory
            );
            throw new UnexpectedResponseException(errorCode.getHTTPErrorCode(),
                    className,
                    methodName,
                    errorMessage,
                    errorCode.getSystemAction(),
                    errorCode.getUserAction(),
                    unexpectedResponseCategory
            );
        }
        return term;
    }
    /**
     * Detect and return a Category object from the supplied response. If we do not find one then throw an Exception
     * @param methodName - name of the method called
     * @param restResponse - response from the rest call.  This generated in the remote handlers.
     * @return Category if the supplied response is a category response
     * @throws UnexpectedResponseException - if the response is not a Category then throw this exception
     */
    public static Category detectAndReturnCategory(String methodName,
                                                   SubjectAreaOMASAPIResponse restResponse) throws UnexpectedResponseException {
        Category category = null;
        if ((restResponse != null) && (restResponse.getResponseCategory() == ResponseCategory.Category)) {
            CategoryResponse categoryResponse = (CategoryResponse)restResponse;
            category = categoryResponse.getCategory();
        } else {
            SubjectAreaErrorCode errorCode = SubjectAreaErrorCode.CLIENT_RECEIVED_AN_UNEXPECTED_RESPONSE_ERROR;
            String unexpectedResponseCategory = restResponse.getResponseCategory().name();
            String errorMessage = errorCode.getErrorMessageId()
                    + errorCode.getFormattedErrorMessage(
                    unexpectedResponseCategory
            );
            throw new UnexpectedResponseException(errorCode.getHTTPErrorCode(),
                    className,
                    methodName,
                    errorMessage,
                    errorCode.getSystemAction(),
                    errorCode.getUserAction(),
                    unexpectedResponseCategory
            );
        }
        return category;
    }
    public static SubjectAreaDefinition detectAndReturnSubjectAreaDefinition(String methodName, SubjectAreaOMASAPIResponse restResponse) throws UnexpectedResponseException
    {
        SubjectAreaDefinition subjectAreaDefinition = null;
        if ((restResponse != null) && (restResponse.getResponseCategory() == ResponseCategory.SubjectAreaDefinition)) {
            SubjectAreaDefinitionResponse subjectAreaDefinitionResponse = (SubjectAreaDefinitionResponse)restResponse;
            subjectAreaDefinition = subjectAreaDefinitionResponse.getSubjectAreaDefinition();
        } else {
            SubjectAreaErrorCode errorCode = SubjectAreaErrorCode.CLIENT_RECEIVED_AN_UNEXPECTED_RESPONSE_ERROR;
            String unexpectedResponseCategory = restResponse.getResponseCategory().name();
            String errorMessage = errorCode.getErrorMessageId()
                    + errorCode.getFormattedErrorMessage(
                    unexpectedResponseCategory
            );
            throw new UnexpectedResponseException(errorCode.getHTTPErrorCode(),
                    className,
                    methodName,
                    errorMessage,
                    errorCode.getSystemAction(),
                    errorCode.getUserAction(),
                    unexpectedResponseCategory
            );
        }
        return subjectAreaDefinition;
    }

    public static TermHASARelationship detectAndReturnTermHASARelationship(String methodName, SubjectAreaOMASAPIResponse restResponse) throws UnexpectedResponseException
    {
        TermHASARelationship termHASARelationship = null;
        if ((restResponse != null) && (restResponse.getResponseCategory() == ResponseCategory.TermHASARelationship)) {
            TermHASARelationshipResponse termHASARelationshipResponse = (TermHASARelationshipResponse)restResponse;
            termHASARelationship = termHASARelationshipResponse.getTermHASARelationship();
        } else {
            SubjectAreaErrorCode errorCode = SubjectAreaErrorCode.CLIENT_RECEIVED_AN_UNEXPECTED_RESPONSE_ERROR;
            String unexpectedResponseCategory = restResponse.getResponseCategory().name();
            String errorMessage = errorCode.getErrorMessageId()
                    + errorCode.getFormattedErrorMessage(
                    unexpectedResponseCategory
            );
            throw new UnexpectedResponseException(errorCode.getHTTPErrorCode(),
                    className,
                    methodName,
                    errorMessage,
                    errorCode.getSystemAction(),
                    errorCode.getUserAction(),
                    unexpectedResponseCategory
            );
        }
        return termHASARelationship;
    }

    public static RelatedTermRelationship detectAndReturnRelatedTerm(String methodName, SubjectAreaOMASAPIResponse restResponse) throws UnexpectedResponseException
    {
        RelatedTermRelationship relatedTermRelationship = null;
        if ((restResponse != null) && (restResponse.getResponseCategory() == ResponseCategory.RelatedTerm)) {
            RelatedTermRelationshipResponse relatedTermResponse = (RelatedTermRelationshipResponse)restResponse;
            relatedTermRelationship = relatedTermResponse.getRelatedTermRelationship();
        } else {
            SubjectAreaErrorCode errorCode = SubjectAreaErrorCode.CLIENT_RECEIVED_AN_UNEXPECTED_RESPONSE_ERROR;
            String unexpectedResponseCategory = restResponse.getResponseCategory().name();
            String errorMessage = errorCode.getErrorMessageId()
                    + errorCode.getFormattedErrorMessage(
                    unexpectedResponseCategory
            );
            throw new UnexpectedResponseException(errorCode.getHTTPErrorCode(),
                    className,
                    methodName,
                    errorMessage,
                    errorCode.getSystemAction(),
                    errorCode.getUserAction(),
                    unexpectedResponseCategory
            );
        }
        return relatedTermRelationship;
    }

    public static Synonym detectAndReturnSynonym(String methodName, SubjectAreaOMASAPIResponse restResponse) throws UnexpectedResponseException
    {
        Synonym synonym = null;
        if ((restResponse != null) && (restResponse.getResponseCategory() == ResponseCategory.SynonymRelationship)) {
            SynonymRelationshipResponse synonymResponse = (SynonymRelationshipResponse)restResponse;
            synonym = synonymResponse.getSynonym();
        } else {
            SubjectAreaErrorCode errorCode = SubjectAreaErrorCode.CLIENT_RECEIVED_AN_UNEXPECTED_RESPONSE_ERROR;
            String unexpectedResponseCategory = restResponse.getResponseCategory().name();
            String errorMessage = errorCode.getErrorMessageId()
                    + errorCode.getFormattedErrorMessage(
                    unexpectedResponseCategory
            );
            throw new UnexpectedResponseException(errorCode.getHTTPErrorCode(),
                    className,
                    methodName,
                    errorMessage,
                    errorCode.getSystemAction(),
                    errorCode.getUserAction(),
                    unexpectedResponseCategory
            );
        }
        return synonym;
    }

    public static Antonym detectAndReturnAntonym(String methodName, SubjectAreaOMASAPIResponse restResponse) throws UnexpectedResponseException
    {
        Antonym antonym = null;
        if ((restResponse != null) && (restResponse.getResponseCategory() == ResponseCategory.AntonymRelationship)) {
            AntonymRelationshipResponse antonymResponse = (AntonymRelationshipResponse)restResponse;
            antonym = antonymResponse.getAntonym();
        } else {
            SubjectAreaErrorCode errorCode = SubjectAreaErrorCode.CLIENT_RECEIVED_AN_UNEXPECTED_RESPONSE_ERROR;
            String unexpectedResponseCategory = restResponse.getResponseCategory().name();
            String errorMessage = errorCode.getErrorMessageId()
                    + errorCode.getFormattedErrorMessage(
                    unexpectedResponseCategory
            );
            throw new UnexpectedResponseException(errorCode.getHTTPErrorCode(),
                    className,
                    methodName,
                    errorMessage,
                    errorCode.getSystemAction(),
                    errorCode.getUserAction(),
                    unexpectedResponseCategory
            );
        }
        return antonym;
    }
}