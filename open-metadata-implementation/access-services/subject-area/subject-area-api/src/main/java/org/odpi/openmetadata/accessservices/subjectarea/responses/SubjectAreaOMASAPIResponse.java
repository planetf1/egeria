/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.responses;

import com.fasterxml.jackson.annotation.*;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * SubjectAreaOMASAPIResponse provides a common header for Asset Consumer OMAS managed rest to its REST API.
 * It manages information about exceptions.  If no exception has been raised exceptionClassName is null.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "class")
@JsonSubTypes(
        {
                @JsonSubTypes.Type(value = CategoryResponse.class, name = "CategoryResponse"),
                @JsonSubTypes.Type(value = GlossaryResponse.class, name = "GlossaryResponse"),
                @JsonSubTypes.Type(value = SubjectAreaDefinitionResponse.class, name = "SubjectAreaDefinitionResponse"),
                @JsonSubTypes.Type(value = TermResponse.class, name = "TermResponse"),
                @JsonSubTypes.Type(value = VoidResponse.class, name = "VoidResponse"),
                @JsonSubTypes.Type(value = ProjectResponse.class, name = "ProjectResponse"),

                // Relationships

                // LibraryTermReferenceRelationship

                // term to term relationship responses
                @JsonSubTypes.Type(value = TermHASARelationshipResponse.class, name = "TermHASARelationshipResponse"),
                @JsonSubTypes.Type(value = RelatedTermRelationshipResponse.class, name = "RelatedTermRelationshipResponse"),
                @JsonSubTypes.Type(value = SynonymRelationshipResponse.class, name = "SynonymRelationshipResponse"),
                @JsonSubTypes.Type(value = AntonymRelationshipResponse.class, name = "AntonymRelationshipResponse"),
                @JsonSubTypes.Type(value = PreferredTermRelationshipResponse.class, name = "PreferredTermRelationshipResponse"),
                @JsonSubTypes.Type(value = TermReplacementRelationshipResponse.class, name = "TermReplacementRelationshipResponse"),
                @JsonSubTypes.Type(value = TermTranslationRelationshipResponse.class, name = "TermTranslationRelationshipResponse"),
                @JsonSubTypes.Type(value = TermValidValueRelationshipResponse.class, name = "TermValidValueRelationshipResponse"),
                @JsonSubTypes.Type(value = TermUsedInContextRelationshipResponse.class, name = "TermUsedInContextRelationshipResponse"),
                @JsonSubTypes.Type(value = TermISATYPEOFRelationshipResponse.class, name = "TermISATYPEOFRelationshipResponse"),
                @JsonSubTypes.Type(value = TermTYPEDBYRelationshipResponse.class, name = "TermTYPEDBYRelationshipResponse"),

                /*
                 Exception rest - note that each exception has the same 4 Exception orientated fields.
                 Ideally these should be in a superclass. Due to restrictions in the @JsonSubTypes processing it  is only possible to have
                 one level of inheritance at this time.
                 */
                @JsonSubTypes.Type(value = ClassificationExceptionResponse.class, name = "ClassificationExceptionResponse"),
                @JsonSubTypes.Type(value = EntityNotDeletedExceptionResponse.class, name = "EntityNotDeletedExceptionResponse") ,
                @JsonSubTypes.Type(value = FunctionNotSupportedExceptionResponse.class, name = "FunctionNotSupportedExceptionResponse") ,
                @JsonSubTypes.Type(value = GUIDNotPurgedExceptionResponse.class, name = "GUIDNotPurgedExceptionResponse") ,
                @JsonSubTypes.Type(value = InvalidParameterExceptionResponse.class, name = "InvalidParameterExceptionResponse") ,
                @JsonSubTypes.Type(value = MetadataServerUncontactableExceptionResponse.class, name = "MetadataServerUncontactableExceptionResponse") ,
                @JsonSubTypes.Type(value = PossibleClassificationsResponse.class, name = "PossibleClassificationsResponse") ,
                @JsonSubTypes.Type(value = PossibleRelationshipsResponse.class, name = "PossibleRelationshipsResponse") ,
                @JsonSubTypes.Type(value = RelationshipNotDeletedExceptionResponse.class, name = "RelationshipNotDeletedExceptionResponse") ,
                @JsonSubTypes.Type(value = StatusNotsupportedExceptionResponse.class, name = "StatusNotsupportedExceptionResponse") ,
                @JsonSubTypes.Type(value = UnrecognizedGUIDExceptionResponse.class, name = "UnrecognizedGUIDExceptionResponse") ,
                @JsonSubTypes.Type(value = UnrecognizedNameExceptionResponse.class, name = "UnrecognizedNameExceptionResponse") ,
                @JsonSubTypes.Type(value = UserNotAuthorizedExceptionResponse.class, name = "UserNotAuthorizedExceptionResponse")

        })
public abstract class SubjectAreaOMASAPIResponse
{
    protected int       relatedHTTPCode = 200;
    protected ResponseCategory responseCategory;

    /**
     * Default constructor
     */
    public SubjectAreaOMASAPIResponse()
    {
    }


    /**
     * Return the HTTP Code to use if forwarding response to HTTP client.
     *
     * @return integer HTTP status code
     */
    public int getRelatedHTTPCode()
    {
        return relatedHTTPCode;
    }


    /**
     * Set up the HTTP Code to use if forwarding response to HTTP client.
     *
     * @param relatedHTTPCode - integer HTTP status code
     */
    public void setRelatedHTTPCode(int relatedHTTPCode)
    {
        this.relatedHTTPCode = relatedHTTPCode;
    }

    public ResponseCategory getResponseCategory() {
        return responseCategory;
    }

    public void setResponseCategory(ResponseCategory responseCategory) {
        this.responseCategory = responseCategory;
    }

    @Override
    public String toString()
    {
        return "relatedHTTPCode=" + relatedHTTPCode +
                ", ResponseCategory='" + responseCategory + '\'' ;

    }
}
