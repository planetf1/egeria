/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

// This is a generated file - do not edit - changes should be made to the templates amd/or generator to generate this file with changes.

package org.odpi.openmetadata.fvt.opentypes.entities.GlossaryTerm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;

import  org.odpi.openmetadata.fvt.opentypes.references.GlossaryTermToGlossaryTerm.UsedInContextsReference;
import org.odpi.openmetadata.fvt.opentypes.relationships.UsedInContext.UsedInContext;
import org.odpi.openmetadata.fvt.opentypes.relationships.UsedInContext.UsedInContextMapper;
import  org.odpi.openmetadata.fvt.opentypes.references.GlossaryTermToGlossaryTerm.ContextRelevantTermsReference;
import org.odpi.openmetadata.fvt.opentypes.relationships.UsedInContext.UsedInContext;
import org.odpi.openmetadata.fvt.opentypes.relationships.UsedInContext.UsedInContextMapper;
import  org.odpi.openmetadata.fvt.opentypes.references.GlossaryTermToGlossaryTerm.TranslationsReference;
import org.odpi.openmetadata.fvt.opentypes.relationships.Translation.Translation;
import org.odpi.openmetadata.fvt.opentypes.relationships.Translation.TranslationMapper;
import  org.odpi.openmetadata.fvt.opentypes.references.GlossaryTermToGlossaryTerm.PreferredTermsReference;
import org.odpi.openmetadata.fvt.opentypes.relationships.PreferredTerm.PreferredTerm;
import org.odpi.openmetadata.fvt.opentypes.relationships.PreferredTerm.PreferredTermMapper;
import  org.odpi.openmetadata.fvt.opentypes.references.GlossaryTermToGlossaryTerm.AlternateTermsReference;
import org.odpi.openmetadata.fvt.opentypes.relationships.PreferredTerm.PreferredTerm;
import org.odpi.openmetadata.fvt.opentypes.relationships.PreferredTerm.PreferredTermMapper;
import  org.odpi.openmetadata.fvt.opentypes.references.GlossaryTermToGlossaryTerm.ValidValuesReference;
import org.odpi.openmetadata.fvt.opentypes.relationships.ValidValue.ValidValue;
import org.odpi.openmetadata.fvt.opentypes.relationships.ValidValue.ValidValueMapper;
import  org.odpi.openmetadata.fvt.opentypes.references.GlossaryTermToGlossaryTerm.ValidValueForReference;
import org.odpi.openmetadata.fvt.opentypes.relationships.ValidValue.ValidValue;
import org.odpi.openmetadata.fvt.opentypes.relationships.ValidValue.ValidValueMapper;
import  org.odpi.openmetadata.fvt.opentypes.references.GlossaryTermToGlossaryTerm.ReplacementTermsReference;
import org.odpi.openmetadata.fvt.opentypes.relationships.ReplacementTerm.ReplacementTerm;
import org.odpi.openmetadata.fvt.opentypes.relationships.ReplacementTerm.ReplacementTermMapper;
import  org.odpi.openmetadata.fvt.opentypes.references.GlossaryTermToGlossaryTerm.ReplacedTermsReference;
import org.odpi.openmetadata.fvt.opentypes.relationships.ReplacementTerm.ReplacementTerm;
import org.odpi.openmetadata.fvt.opentypes.relationships.ReplacementTerm.ReplacementTermMapper;
import  org.odpi.openmetadata.fvt.opentypes.references.GlossaryTermToGlossaryCategory.CategoriesReference;
import org.odpi.openmetadata.fvt.opentypes.relationships.TermCategorization.TermCategorization;
import org.odpi.openmetadata.fvt.opentypes.relationships.TermCategorization.TermCategorizationMapper;
import  org.odpi.openmetadata.fvt.opentypes.references.GlossaryTermToGlossaryTerm.TypesReference;
import org.odpi.openmetadata.fvt.opentypes.relationships.TermTYPEDBYRelationship.TermTYPEDBYRelationship;
import org.odpi.openmetadata.fvt.opentypes.relationships.TermTYPEDBYRelationship.TermTYPEDBYRelationshipMapper;
import  org.odpi.openmetadata.fvt.opentypes.references.GlossaryTermToGlossaryTerm.AttributesTypedByReference;
import org.odpi.openmetadata.fvt.opentypes.relationships.TermTYPEDBYRelationship.TermTYPEDBYRelationship;
import org.odpi.openmetadata.fvt.opentypes.relationships.TermTYPEDBYRelationship.TermTYPEDBYRelationshipMapper;
import  org.odpi.openmetadata.fvt.opentypes.references.GlossaryTermToGlossaryTerm.SynonymsReference;
import org.odpi.openmetadata.fvt.opentypes.relationships.Synonym.Synonym;
import org.odpi.openmetadata.fvt.opentypes.relationships.Synonym.SynonymMapper;
import  org.odpi.openmetadata.fvt.opentypes.references.GlossaryTermToGlossaryTerm.AntonymsReference;
import org.odpi.openmetadata.fvt.opentypes.relationships.Antonym.Antonym;
import org.odpi.openmetadata.fvt.opentypes.relationships.Antonym.AntonymMapper;
import  org.odpi.openmetadata.fvt.opentypes.references.GlossaryTermToGlossaryTerm.IsAReference;
import org.odpi.openmetadata.fvt.opentypes.relationships.ISARelationship.ISARelationship;
import org.odpi.openmetadata.fvt.opentypes.relationships.ISARelationship.ISARelationshipMapper;
import  org.odpi.openmetadata.fvt.opentypes.references.GlossaryTermToGlossaryTerm.ClassifiesReference;
import org.odpi.openmetadata.fvt.opentypes.relationships.ISARelationship.ISARelationship;
import org.odpi.openmetadata.fvt.opentypes.relationships.ISARelationship.ISARelationshipMapper;
import  org.odpi.openmetadata.fvt.opentypes.references.GlossaryTermToGlossaryTerm.SubtypesReference;
import org.odpi.openmetadata.fvt.opentypes.relationships.TermISATypeOFRelationship.TermISATypeOFRelationship;
import org.odpi.openmetadata.fvt.opentypes.relationships.TermISATypeOFRelationship.TermISATypeOFRelationshipMapper;
import  org.odpi.openmetadata.fvt.opentypes.references.GlossaryTermToGlossaryTerm.SupertypesReference;
import org.odpi.openmetadata.fvt.opentypes.relationships.TermISATypeOFRelationship.TermISATypeOFRelationship;
import org.odpi.openmetadata.fvt.opentypes.relationships.TermISATypeOFRelationship.TermISATypeOFRelationshipMapper;
import  org.odpi.openmetadata.fvt.opentypes.references.GlossaryTermToReferenceable.AssignedElementsReference;
import org.odpi.openmetadata.fvt.opentypes.relationships.SemanticAssignment.SemanticAssignment;
import org.odpi.openmetadata.fvt.opentypes.relationships.SemanticAssignment.SemanticAssignmentMapper;
import  org.odpi.openmetadata.fvt.opentypes.references.GlossaryTermToGlossaryTerm.SeeAlsoReference;
import org.odpi.openmetadata.fvt.opentypes.relationships.RelatedTerm.RelatedTerm;
import org.odpi.openmetadata.fvt.opentypes.relationships.RelatedTerm.RelatedTermMapper;
import  org.odpi.openmetadata.fvt.opentypes.references.GlossaryTermToExternalGlossaryLink.ExternalGlossaryTermsReference;
import org.odpi.openmetadata.fvt.opentypes.relationships.LibraryTermReference.LibraryTermReference;
import org.odpi.openmetadata.fvt.opentypes.relationships.LibraryTermReference.LibraryTermReferenceMapper;
import  org.odpi.openmetadata.fvt.opentypes.references.GlossaryTermToGlossaryTerm.AttributesReference;
import org.odpi.openmetadata.fvt.opentypes.relationships.TermHASARelationship.TermHASARelationship;
import org.odpi.openmetadata.fvt.opentypes.relationships.TermHASARelationship.TermHASARelationshipMapper;
import  org.odpi.openmetadata.fvt.opentypes.references.GlossaryTermToGlossaryTerm.ObjectsReference;
import org.odpi.openmetadata.fvt.opentypes.relationships.TermHASARelationship.TermHASARelationship;
import org.odpi.openmetadata.fvt.opentypes.relationships.TermHASARelationship.TermHASARelationshipMapper;
import  org.odpi.openmetadata.fvt.opentypes.references.GlossaryTermToGlossary.AnchorReference;
import org.odpi.openmetadata.fvt.opentypes.relationships.TermAnchor.TermAnchor;
import org.odpi.openmetadata.fvt.opentypes.relationships.TermAnchor.TermAnchorMapper;
import  org.odpi.openmetadata.fvt.opentypes.references.ReferenceableToRelatedMedia.RelatedMediaReference;
import org.odpi.openmetadata.fvt.opentypes.relationships.MediaReference.MediaReference;
import org.odpi.openmetadata.fvt.opentypes.relationships.MediaReference.MediaReferenceMapper;
import  org.odpi.openmetadata.fvt.opentypes.references.ReferenceableToReferenceable.SupportingResourcesReference;
import org.odpi.openmetadata.fvt.opentypes.relationships.ResourceList.ResourceList;
import org.odpi.openmetadata.fvt.opentypes.relationships.ResourceList.ResourceListMapper;
import  org.odpi.openmetadata.fvt.opentypes.references.ReferenceableToCertificationType.CertificationsReference;
import org.odpi.openmetadata.fvt.opentypes.relationships.Certification.Certification;
import org.odpi.openmetadata.fvt.opentypes.relationships.Certification.CertificationMapper;
import  org.odpi.openmetadata.fvt.opentypes.references.ReferenceableToNoteLog.NoteLogsReference;
import org.odpi.openmetadata.fvt.opentypes.relationships.AttachedNoteLog.AttachedNoteLog;
import org.odpi.openmetadata.fvt.opentypes.relationships.AttachedNoteLog.AttachedNoteLogMapper;
import  org.odpi.openmetadata.fvt.opentypes.references.ReferenceableToPersonRole.GovernedByRolesReference;
import org.odpi.openmetadata.fvt.opentypes.relationships.GovernanceRoleAssignment.GovernanceRoleAssignment;
import org.odpi.openmetadata.fvt.opentypes.relationships.GovernanceRoleAssignment.GovernanceRoleAssignmentMapper;
import  org.odpi.openmetadata.fvt.opentypes.references.ReferenceableToComment.CommentsReference;
import org.odpi.openmetadata.fvt.opentypes.relationships.AttachedComment.AttachedComment;
import org.odpi.openmetadata.fvt.opentypes.relationships.AttachedComment.AttachedCommentMapper;
import  org.odpi.openmetadata.fvt.opentypes.references.ReferenceableToCrowdSourcingContributor.ContributorsReference;
import org.odpi.openmetadata.fvt.opentypes.relationships.CrowdSourcingContribution.CrowdSourcingContribution;
import org.odpi.openmetadata.fvt.opentypes.relationships.CrowdSourcingContribution.CrowdSourcingContributionMapper;
import  org.odpi.openmetadata.fvt.opentypes.references.ReferenceableToLicenseType.LicensesReference;
import org.odpi.openmetadata.fvt.opentypes.relationships.License.License;
import org.odpi.openmetadata.fvt.opentypes.relationships.License.LicenseMapper;
import  org.odpi.openmetadata.fvt.opentypes.references.ReferenceableToToDo.RelatedActionsReference;
import org.odpi.openmetadata.fvt.opentypes.relationships.Actions.Actions;
import org.odpi.openmetadata.fvt.opentypes.relationships.Actions.ActionsMapper;
import  org.odpi.openmetadata.fvt.opentypes.references.ReferenceableToRating.StarRatingsReference;
import org.odpi.openmetadata.fvt.opentypes.relationships.AttachedRating.AttachedRating;
import org.odpi.openmetadata.fvt.opentypes.relationships.AttachedRating.AttachedRatingMapper;
import  org.odpi.openmetadata.fvt.opentypes.references.ReferenceableToReferenceable.RelatedFromObjectAnnotationsReference;
import org.odpi.openmetadata.fvt.opentypes.relationships.RelationshipAnnotation.RelationshipAnnotation;
import org.odpi.openmetadata.fvt.opentypes.relationships.RelationshipAnnotation.RelationshipAnnotationMapper;
import  org.odpi.openmetadata.fvt.opentypes.references.ReferenceableToToDo.ActionsReference;
import org.odpi.openmetadata.fvt.opentypes.relationships.ToDoSource.ToDoSource;
import org.odpi.openmetadata.fvt.opentypes.relationships.ToDoSource.ToDoSourceMapper;
import  org.odpi.openmetadata.fvt.opentypes.references.ReferenceableToDataClass.DataClassesAssignedToElementReference;
import org.odpi.openmetadata.fvt.opentypes.relationships.DataClassAssignment.DataClassAssignment;
import org.odpi.openmetadata.fvt.opentypes.relationships.DataClassAssignment.DataClassAssignmentMapper;
import  org.odpi.openmetadata.fvt.opentypes.references.ReferenceableToExternalId.ManagedResourcesReference;
import org.odpi.openmetadata.fvt.opentypes.relationships.ExternalIdScope.ExternalIdScope;
import org.odpi.openmetadata.fvt.opentypes.relationships.ExternalIdScope.ExternalIdScopeMapper;
import  org.odpi.openmetadata.fvt.opentypes.references.ReferenceableToExternalId.AlsoKnownAsReference;
import org.odpi.openmetadata.fvt.opentypes.relationships.ExternalIdLink.ExternalIdLink;
import org.odpi.openmetadata.fvt.opentypes.relationships.ExternalIdLink.ExternalIdLinkMapper;
import  org.odpi.openmetadata.fvt.opentypes.references.ReferenceableToReferenceable.ResourceListAnchorsReference;
import org.odpi.openmetadata.fvt.opentypes.relationships.ResourceList.ResourceList;
import org.odpi.openmetadata.fvt.opentypes.relationships.ResourceList.ResourceListMapper;
import  org.odpi.openmetadata.fvt.opentypes.references.ReferenceableToReferenceable.RelatedToObjectAnnotationsReference;
import org.odpi.openmetadata.fvt.opentypes.relationships.RelationshipAnnotation.RelationshipAnnotation;
import org.odpi.openmetadata.fvt.opentypes.relationships.RelationshipAnnotation.RelationshipAnnotationMapper;
import  org.odpi.openmetadata.fvt.opentypes.references.ReferenceableToExternalReference.ExternalReferenceReference;
import org.odpi.openmetadata.fvt.opentypes.relationships.ExternalReferenceLink.ExternalReferenceLink;
import org.odpi.openmetadata.fvt.opentypes.relationships.ExternalReferenceLink.ExternalReferenceLinkMapper;
import  org.odpi.openmetadata.fvt.opentypes.references.ReferenceableToProject.ImpactingProjectsReference;
import org.odpi.openmetadata.fvt.opentypes.relationships.ProjectScope.ProjectScope;
import org.odpi.openmetadata.fvt.opentypes.relationships.ProjectScope.ProjectScopeMapper;
import  org.odpi.openmetadata.fvt.opentypes.references.ReferenceableToGlossaryTerm.MeaningReference;
import org.odpi.openmetadata.fvt.opentypes.relationships.SemanticAssignment.SemanticAssignment;
import org.odpi.openmetadata.fvt.opentypes.relationships.SemanticAssignment.SemanticAssignmentMapper;
import  org.odpi.openmetadata.fvt.opentypes.references.ReferenceableToCollection.FoundInCollectionsReference;
import org.odpi.openmetadata.fvt.opentypes.relationships.CollectionMembership.CollectionMembership;
import org.odpi.openmetadata.fvt.opentypes.relationships.CollectionMembership.CollectionMembershipMapper;
import  org.odpi.openmetadata.fvt.opentypes.references.ReferenceableToPropertyFacet.FacetsReference;
import org.odpi.openmetadata.fvt.opentypes.relationships.ReferenceableFacet.ReferenceableFacet;
import org.odpi.openmetadata.fvt.opentypes.relationships.ReferenceableFacet.ReferenceableFacetMapper;
import  org.odpi.openmetadata.fvt.opentypes.references.ReferenceableToMeeting.MeetingsReference;
import org.odpi.openmetadata.fvt.opentypes.relationships.Meetings.Meetings;
import org.odpi.openmetadata.fvt.opentypes.relationships.Meetings.MeetingsMapper;
import  org.odpi.openmetadata.fvt.opentypes.references.ReferenceableToInformalTag.TagsReference;
import org.odpi.openmetadata.fvt.opentypes.relationships.AttachedTag.AttachedTag;
import org.odpi.openmetadata.fvt.opentypes.relationships.AttachedTag.AttachedTagMapper;
import  org.odpi.openmetadata.fvt.opentypes.references.ReferenceableToLike.LikesReference;
import org.odpi.openmetadata.fvt.opentypes.relationships.AttachedLike.AttachedLike;
import org.odpi.openmetadata.fvt.opentypes.relationships.AttachedLike.AttachedLikeMapper;

import java.io.Serializable;
import java.util.*;
import org.odpi.openmetadata.fvt.opentypes.common.*;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * References for entity  GlossaryTerm. References are relationships represented as an attribute. Exposing relationships information
 * as references can make the relationships information more digestable.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class GlossaryTermReferences implements Serializable {
    private static final Logger log = LoggerFactory.getLogger(GlossaryTermReferences.class);
    private static final String className = GlossaryTermReferences.class.getName();

    public static final String[] REFERENCE_NAMES_SET_VALUES = new String[] {
             "usedInContexts",
             "contextRelevantTerms",
             "translations",
             "preferredTerms",
             "alternateTerms",
             "validValues",
             "validValueFor",
             "replacementTerms",
             "replacedTerms",
             "categories",
             "types",
             "attributesTypedBy",
             "synonyms",
             "antonyms",
             "isA",
             "classifies",
             "subtypes",
             "supertypes",
             "assignedElements",
             "seeAlso",
             "externalGlossaryTerms",
             "attributes",
             "objects",
             "anchor",
             "relatedMedia",
             "supportingResources",
             "certifications",
             "noteLogs",
             "governedByRoles",
             "comments",
             "contributors",
             "licenses",
             "relatedActions",
             "starRatings",
             "relatedFromObjectAnnotations",
             "actions",
             "dataClassesAssignedToElement",
             "managedResources",
             "alsoKnownAs",
             "resourceListAnchors",
             "relatedToObjectAnnotations",
             "externalReference",
             "impactingProjects",
             "meaning",
             "foundInCollections",
             "facets",
             "meetings",
             "tags",
             "likes",
             // Terminate the list
             null
    };

     public static final String[] RELATIONSHIP_NAMES_SET_VALUES = new String[] {
             "UsedInContext",
             "UsedInContext",
             "Translation",
             "PreferredTerm",
             "PreferredTerm",
             "ValidValue",
             "ValidValue",
             "ReplacementTerm",
             "ReplacementTerm",
             "TermCategorization",
             "TermTYPEDBYRelationship",
             "TermTYPEDBYRelationship",
             "Synonym",
             "Antonym",
             "ISARelationship",
             "ISARelationship",
             "TermISATypeOFRelationship",
             "TermISATypeOFRelationship",
             "SemanticAssignment",
             "RelatedTerm",
             "LibraryTermReference",
             "TermHASARelationship",
             "TermHASARelationship",
             "TermAnchor",
             "MediaReference",
             "ResourceList",
             "Certification",
             "AttachedNoteLog",
             "GovernanceRoleAssignment",
             "AttachedComment",
             "CrowdSourcingContribution",
             "License",
             "Actions",
             "AttachedRating",
             "RelationshipAnnotation",
             "ToDoSource",
             "DataClassAssignment",
             "ExternalIdScope",
             "ExternalIdLink",
             "ResourceList",
             "RelationshipAnnotation",
             "ExternalReferenceLink",
             "ProjectScope",
             "SemanticAssignment",
             "CollectionMembership",
             "ReferenceableFacet",
             "Meetings",
             "AttachedTag",
             "AttachedLike",
              // Terminate the list
              null
     };
     /**
       * We are passed a set of omrs relationships that are associated with a entity GlossaryTerm
       * Each of these relationships should map to a reference (a uniquely named attribute in GlossaryTerm).
       *
       * Relationships have cardinality. There are 2 sorts of cardinality relevant here, whether the relationships can be related to one or many
       * entities.
       *
       * @param lines the relationships lines.
       * @param entityGuid The GUID of the entity.
       */
     public GlossaryTermReferences(String entityGuid, Set<Line> lines)  {

        if (lines !=null) {
         for (Line relationships: lines) {
            for (int i=0;i< RELATIONSHIP_NAMES_SET_VALUES.length; i++) {
               if (relationships.getName().equals(RELATIONSHIP_NAMES_SET_VALUES[i])) {
                    String referenceName = REFERENCE_NAMES_SET_VALUES[i];

                    if ("usedInContexts".equals(referenceName)) {
                         UsedInContext usedInContext_relationship = (UsedInContext)relationships;
                         UsedInContextsReference usedInContextsReference = new UsedInContextsReference(entityGuid,usedInContext_relationship);
                         if ( usedInContexts== null ) {
                              usedInContexts = new HashSet();
                         }
                          usedInContexts.add(usedInContextsReference);
                    }
                    if ("contextRelevantTerms".equals(referenceName)) {
                         UsedInContext usedInContext_relationship = (UsedInContext)relationships;
                         ContextRelevantTermsReference contextRelevantTermsReference = new ContextRelevantTermsReference(entityGuid,usedInContext_relationship);
                         if ( contextRelevantTerms== null ) {
                              contextRelevantTerms = new HashSet();
                         }
                          contextRelevantTerms.add(contextRelevantTermsReference);
                    }
                    if ("translations".equals(referenceName)) {
                         Translation translation_relationship = (Translation)relationships;
                         TranslationsReference translationsReference = new TranslationsReference(entityGuid,translation_relationship);
                         if ( translations== null ) {
                              translations = new HashSet();
                         }
                          translations.add(translationsReference);
                    }
                    if ("preferredTerms".equals(referenceName)) {
                         PreferredTerm preferredTerm_relationship = (PreferredTerm)relationships;
                         PreferredTermsReference preferredTermsReference = new PreferredTermsReference(entityGuid,preferredTerm_relationship);
                         if ( preferredTerms== null ) {
                              preferredTerms = new HashSet();
                         }
                          preferredTerms.add(preferredTermsReference);
                    }
                    if ("alternateTerms".equals(referenceName)) {
                         PreferredTerm preferredTerm_relationship = (PreferredTerm)relationships;
                         AlternateTermsReference alternateTermsReference = new AlternateTermsReference(entityGuid,preferredTerm_relationship);
                         if ( alternateTerms== null ) {
                              alternateTerms = new HashSet();
                         }
                          alternateTerms.add(alternateTermsReference);
                    }
                    if ("validValues".equals(referenceName)) {
                         ValidValue validValue_relationship = (ValidValue)relationships;
                         ValidValuesReference validValuesReference = new ValidValuesReference(entityGuid,validValue_relationship);
                         if ( validValues== null ) {
                              validValues = new HashSet();
                         }
                          validValues.add(validValuesReference);
                    }
                    if ("validValueFor".equals(referenceName)) {
                         ValidValue validValue_relationship = (ValidValue)relationships;
                         ValidValueForReference validValueForReference = new ValidValueForReference(entityGuid,validValue_relationship);
                         if ( validValueFor== null ) {
                              validValueFor = new HashSet();
                         }
                          validValueFor.add(validValueForReference);
                    }
                    if ("replacementTerms".equals(referenceName)) {
                         ReplacementTerm replacementTerm_relationship = (ReplacementTerm)relationships;
                         ReplacementTermsReference replacementTermsReference = new ReplacementTermsReference(entityGuid,replacementTerm_relationship);
                         if ( replacementTerms== null ) {
                              replacementTerms = new HashSet();
                         }
                          replacementTerms.add(replacementTermsReference);
                    }
                    if ("replacedTerms".equals(referenceName)) {
                         ReplacementTerm replacementTerm_relationship = (ReplacementTerm)relationships;
                         ReplacedTermsReference replacedTermsReference = new ReplacedTermsReference(entityGuid,replacementTerm_relationship);
                         if ( replacedTerms== null ) {
                              replacedTerms = new HashSet();
                         }
                          replacedTerms.add(replacedTermsReference);
                    }
                    if ("categories".equals(referenceName)) {
                         TermCategorization termCategorization_relationship = (TermCategorization)relationships;
                         CategoriesReference categoriesReference = new CategoriesReference(entityGuid,termCategorization_relationship);
                         if ( categories== null ) {
                              categories = new HashSet();
                         }
                          categories.add(categoriesReference);
                    }
                    if ("types".equals(referenceName)) {
                         TermTYPEDBYRelationship termTYPEDBYRelationship_relationship = (TermTYPEDBYRelationship)relationships;
                         TypesReference typesReference = new TypesReference(entityGuid,termTYPEDBYRelationship_relationship);
                         if ( types== null ) {
                              types = new HashSet();
                         }
                          types.add(typesReference);
                    }
                    if ("attributesTypedBy".equals(referenceName)) {
                         TermTYPEDBYRelationship termTYPEDBYRelationship_relationship = (TermTYPEDBYRelationship)relationships;
                         AttributesTypedByReference attributesTypedByReference = new AttributesTypedByReference(entityGuid,termTYPEDBYRelationship_relationship);
                         if ( attributesTypedBy== null ) {
                              attributesTypedBy = new HashSet();
                         }
                          attributesTypedBy.add(attributesTypedByReference);
                    }
                    if ("synonyms".equals(referenceName)) {
                         Synonym synonym_relationship = (Synonym)relationships;
                         SynonymsReference synonymsReference = new SynonymsReference(entityGuid,synonym_relationship);
                         if ( synonyms== null ) {
                              synonyms = new HashSet();
                         }
                          synonyms.add(synonymsReference);
                    }
                    if ("antonyms".equals(referenceName)) {
                         Antonym antonym_relationship = (Antonym)relationships;
                         AntonymsReference antonymsReference = new AntonymsReference(entityGuid,antonym_relationship);
                         if ( antonyms== null ) {
                              antonyms = new HashSet();
                         }
                          antonyms.add(antonymsReference);
                    }
                    if ("isA".equals(referenceName)) {
                         ISARelationship iSARelationship_relationship = (ISARelationship)relationships;
                         IsAReference isAReference = new IsAReference(entityGuid,iSARelationship_relationship);
                         if ( isA== null ) {
                              isA = new HashSet();
                         }
                          isA.add(isAReference);
                    }
                    if ("classifies".equals(referenceName)) {
                         ISARelationship iSARelationship_relationship = (ISARelationship)relationships;
                         ClassifiesReference classifiesReference = new ClassifiesReference(entityGuid,iSARelationship_relationship);
                         if ( classifies== null ) {
                              classifies = new HashSet();
                         }
                          classifies.add(classifiesReference);
                    }
                    if ("subtypes".equals(referenceName)) {
                         TermISATypeOFRelationship termISATypeOFRelationship_relationship = (TermISATypeOFRelationship)relationships;
                         SubtypesReference subtypesReference = new SubtypesReference(entityGuid,termISATypeOFRelationship_relationship);
                         if ( subtypes== null ) {
                              subtypes = new HashSet();
                         }
                          subtypes.add(subtypesReference);
                    }
                    if ("supertypes".equals(referenceName)) {
                         TermISATypeOFRelationship termISATypeOFRelationship_relationship = (TermISATypeOFRelationship)relationships;
                         SupertypesReference supertypesReference = new SupertypesReference(entityGuid,termISATypeOFRelationship_relationship);
                         if ( supertypes== null ) {
                              supertypes = new HashSet();
                         }
                          supertypes.add(supertypesReference);
                    }
                    if ("assignedElements".equals(referenceName)) {
                         SemanticAssignment semanticAssignment_relationship = (SemanticAssignment)relationships;
                         AssignedElementsReference assignedElementsReference = new AssignedElementsReference(entityGuid,semanticAssignment_relationship);
                         if ( assignedElements== null ) {
                              assignedElements = new HashSet();
                         }
                          assignedElements.add(assignedElementsReference);
                    }
                    if ("seeAlso".equals(referenceName)) {
                         RelatedTerm relatedTerm_relationship = (RelatedTerm)relationships;
                         SeeAlsoReference seeAlsoReference = new SeeAlsoReference(entityGuid,relatedTerm_relationship);
                         if ( seeAlso== null ) {
                              seeAlso = new HashSet();
                         }
                          seeAlso.add(seeAlsoReference);
                    }
                    if ("externalGlossaryTerms".equals(referenceName)) {
                         LibraryTermReference libraryTermReference_relationship = (LibraryTermReference)relationships;
                         ExternalGlossaryTermsReference externalGlossaryTermsReference = new ExternalGlossaryTermsReference(entityGuid,libraryTermReference_relationship);
                         if ( externalGlossaryTerms== null ) {
                              externalGlossaryTerms = new HashSet();
                         }
                          externalGlossaryTerms.add(externalGlossaryTermsReference);
                    }
                    if ("attributes".equals(referenceName)) {
                         TermHASARelationship termHASARelationship_relationship = (TermHASARelationship)relationships;
                         AttributesReference attributesReference = new AttributesReference(entityGuid,termHASARelationship_relationship);
                         if ( attributes== null ) {
                              attributes = new HashSet();
                         }
                          attributes.add(attributesReference);
                    }
                    if ("objects".equals(referenceName)) {
                         TermHASARelationship termHASARelationship_relationship = (TermHASARelationship)relationships;
                         ObjectsReference objectsReference = new ObjectsReference(entityGuid,termHASARelationship_relationship);
                         if ( objects== null ) {
                              objects = new HashSet();
                         }
                          objects.add(objectsReference);
                    }
                    if ("relatedMedia".equals(referenceName)) {
                         MediaReference mediaReference_relationship = (MediaReference)relationships;
                         RelatedMediaReference relatedMediaReference = new RelatedMediaReference(entityGuid,mediaReference_relationship);
                         if ( relatedMedia== null ) {
                              relatedMedia = new HashSet();
                         }
                          relatedMedia.add(relatedMediaReference);
                    }
                    if ("supportingResources".equals(referenceName)) {
                         ResourceList resourceList_relationship = (ResourceList)relationships;
                         SupportingResourcesReference supportingResourcesReference = new SupportingResourcesReference(entityGuid,resourceList_relationship);
                         if ( supportingResources== null ) {
                              supportingResources = new HashSet();
                         }
                          supportingResources.add(supportingResourcesReference);
                    }
                    if ("certifications".equals(referenceName)) {
                         Certification certification_relationship = (Certification)relationships;
                         CertificationsReference certificationsReference = new CertificationsReference(entityGuid,certification_relationship);
                         if ( certifications== null ) {
                              certifications = new HashSet();
                         }
                          certifications.add(certificationsReference);
                    }
                    if ("noteLogs".equals(referenceName)) {
                         AttachedNoteLog attachedNoteLog_relationship = (AttachedNoteLog)relationships;
                         NoteLogsReference noteLogsReference = new NoteLogsReference(entityGuid,attachedNoteLog_relationship);
                         if ( noteLogs== null ) {
                              noteLogs = new HashSet();
                         }
                          noteLogs.add(noteLogsReference);
                    }
                    if ("governedByRoles".equals(referenceName)) {
                         GovernanceRoleAssignment governanceRoleAssignment_relationship = (GovernanceRoleAssignment)relationships;
                         GovernedByRolesReference governedByRolesReference = new GovernedByRolesReference(entityGuid,governanceRoleAssignment_relationship);
                         if ( governedByRoles== null ) {
                              governedByRoles = new HashSet();
                         }
                          governedByRoles.add(governedByRolesReference);
                    }
                    if ("comments".equals(referenceName)) {
                         AttachedComment attachedComment_relationship = (AttachedComment)relationships;
                         CommentsReference commentsReference = new CommentsReference(entityGuid,attachedComment_relationship);
                         if ( comments== null ) {
                              comments = new HashSet();
                         }
                          comments.add(commentsReference);
                    }
                    if ("contributors".equals(referenceName)) {
                         CrowdSourcingContribution crowdSourcingContribution_relationship = (CrowdSourcingContribution)relationships;
                         ContributorsReference contributorsReference = new ContributorsReference(entityGuid,crowdSourcingContribution_relationship);
                         if ( contributors== null ) {
                              contributors = new HashSet();
                         }
                          contributors.add(contributorsReference);
                    }
                    if ("licenses".equals(referenceName)) {
                         License license_relationship = (License)relationships;
                         LicensesReference licensesReference = new LicensesReference(entityGuid,license_relationship);
                         if ( licenses== null ) {
                              licenses = new HashSet();
                         }
                          licenses.add(licensesReference);
                    }
                    if ("relatedActions".equals(referenceName)) {
                         Actions actions_relationship = (Actions)relationships;
                         RelatedActionsReference relatedActionsReference = new RelatedActionsReference(entityGuid,actions_relationship);
                         if ( relatedActions== null ) {
                              relatedActions = new HashSet();
                         }
                          relatedActions.add(relatedActionsReference);
                    }
                    if ("starRatings".equals(referenceName)) {
                         AttachedRating attachedRating_relationship = (AttachedRating)relationships;
                         StarRatingsReference starRatingsReference = new StarRatingsReference(entityGuid,attachedRating_relationship);
                         if ( starRatings== null ) {
                              starRatings = new HashSet();
                         }
                          starRatings.add(starRatingsReference);
                    }
                    if ("relatedFromObjectAnnotations".equals(referenceName)) {
                         RelationshipAnnotation relationshipAnnotation_relationship = (RelationshipAnnotation)relationships;
                         RelatedFromObjectAnnotationsReference relatedFromObjectAnnotationsReference = new RelatedFromObjectAnnotationsReference(entityGuid,relationshipAnnotation_relationship);
                         if ( relatedFromObjectAnnotations== null ) {
                              relatedFromObjectAnnotations = new HashSet();
                         }
                          relatedFromObjectAnnotations.add(relatedFromObjectAnnotationsReference);
                    }
                    if ("actions".equals(referenceName)) {
                         ToDoSource toDoSource_relationship = (ToDoSource)relationships;
                         ActionsReference actionsReference = new ActionsReference(entityGuid,toDoSource_relationship);
                         if ( actions== null ) {
                              actions = new HashSet();
                         }
                          actions.add(actionsReference);
                    }
                    if ("dataClassesAssignedToElement".equals(referenceName)) {
                         DataClassAssignment dataClassAssignment_relationship = (DataClassAssignment)relationships;
                         DataClassesAssignedToElementReference dataClassesAssignedToElementReference = new DataClassesAssignedToElementReference(entityGuid,dataClassAssignment_relationship);
                         if ( dataClassesAssignedToElement== null ) {
                              dataClassesAssignedToElement = new HashSet();
                         }
                          dataClassesAssignedToElement.add(dataClassesAssignedToElementReference);
                    }
                    if ("managedResources".equals(referenceName)) {
                         ExternalIdScope externalIdScope_relationship = (ExternalIdScope)relationships;
                         ManagedResourcesReference managedResourcesReference = new ManagedResourcesReference(entityGuid,externalIdScope_relationship);
                         if ( managedResources== null ) {
                              managedResources = new HashSet();
                         }
                          managedResources.add(managedResourcesReference);
                    }
                    if ("alsoKnownAs".equals(referenceName)) {
                         ExternalIdLink externalIdLink_relationship = (ExternalIdLink)relationships;
                         AlsoKnownAsReference alsoKnownAsReference = new AlsoKnownAsReference(entityGuid,externalIdLink_relationship);
                         if ( alsoKnownAs== null ) {
                              alsoKnownAs = new HashSet();
                         }
                          alsoKnownAs.add(alsoKnownAsReference);
                    }
                    if ("resourceListAnchors".equals(referenceName)) {
                         ResourceList resourceList_relationship = (ResourceList)relationships;
                         ResourceListAnchorsReference resourceListAnchorsReference = new ResourceListAnchorsReference(entityGuid,resourceList_relationship);
                         if ( resourceListAnchors== null ) {
                              resourceListAnchors = new HashSet();
                         }
                          resourceListAnchors.add(resourceListAnchorsReference);
                    }
                    if ("externalReference".equals(referenceName)) {
                         ExternalReferenceLink externalReferenceLink_relationship = (ExternalReferenceLink)relationships;
                         ExternalReferenceReference externalReferenceReference = new ExternalReferenceReference(entityGuid,externalReferenceLink_relationship);
                         if ( externalReference== null ) {
                              externalReference = new HashSet();
                         }
                          externalReference.add(externalReferenceReference);
                    }
                    if ("impactingProjects".equals(referenceName)) {
                         ProjectScope projectScope_relationship = (ProjectScope)relationships;
                         ImpactingProjectsReference impactingProjectsReference = new ImpactingProjectsReference(entityGuid,projectScope_relationship);
                         if ( impactingProjects== null ) {
                              impactingProjects = new HashSet();
                         }
                          impactingProjects.add(impactingProjectsReference);
                    }
                    if ("meaning".equals(referenceName)) {
                         SemanticAssignment semanticAssignment_relationship = (SemanticAssignment)relationships;
                         MeaningReference meaningReference = new MeaningReference(entityGuid,semanticAssignment_relationship);
                         if ( meaning== null ) {
                              meaning = new HashSet();
                         }
                          meaning.add(meaningReference);
                    }
                    if ("foundInCollections".equals(referenceName)) {
                         CollectionMembership collectionMembership_relationship = (CollectionMembership)relationships;
                         FoundInCollectionsReference foundInCollectionsReference = new FoundInCollectionsReference(entityGuid,collectionMembership_relationship);
                         if ( foundInCollections== null ) {
                              foundInCollections = new HashSet();
                         }
                          foundInCollections.add(foundInCollectionsReference);
                    }
                    if ("facets".equals(referenceName)) {
                         ReferenceableFacet referenceableFacet_relationship = (ReferenceableFacet)relationships;
                         FacetsReference facetsReference = new FacetsReference(entityGuid,referenceableFacet_relationship);
                         if ( facets== null ) {
                              facets = new HashSet();
                         }
                          facets.add(facetsReference);
                    }
                    if ("meetings".equals(referenceName)) {
                         Meetings meetings_relationship = (Meetings)relationships;
                         MeetingsReference meetingsReference = new MeetingsReference(entityGuid,meetings_relationship);
                         if ( meetings== null ) {
                              meetings = new HashSet();
                         }
                          meetings.add(meetingsReference);
                    }
                    if ("tags".equals(referenceName)) {
                         AttachedTag attachedTag_relationship = (AttachedTag)relationships;
                         TagsReference tagsReference = new TagsReference(entityGuid,attachedTag_relationship);
                         if ( tags== null ) {
                              tags = new HashSet();
                         }
                          tags.add(tagsReference);
                    }
                    if ("likes".equals(referenceName)) {
                         AttachedLike attachedLike_relationship = (AttachedLike)relationships;
                         LikesReference likesReference = new LikesReference(entityGuid,attachedLike_relationship);
                         if ( likes== null ) {
                              likes = new HashSet();
                         }
                          likes.add(likesReference);
                    }

                    if ("anchor".equals(referenceName)) {
                         TermAnchor termAnchor_relationship = (TermAnchor)relationships;
                         anchor = new AnchorReference(entityGuid, termAnchor_relationship);
                    }
                    if ("relatedToObjectAnnotations".equals(referenceName)) {
                         RelationshipAnnotation relationshipAnnotation_relationship = (RelationshipAnnotation)relationships;
                         relatedToObjectAnnotations = new RelatedToObjectAnnotationsReference(entityGuid, relationshipAnnotation_relationship);
                    }
                 }
             }
         }
        }
     }

    public static final Set<String> REFERENCE_NAMES_SET = new HashSet(new HashSet<>(Arrays.asList(REFERENCE_NAMES_SET_VALUES)));
    // there may be duplicate strings in RELATIONSHIP_NAMES_SET_VALUES, the following line deduplicates the Strings into a Set.
    public static final Set<String> RELATIONSHIP_NAMES_SET = new HashSet(new HashSet<>(Arrays.asList(RELATIONSHIP_NAMES_SET_VALUES)));
// Singular properties
    private AnchorReference anchor;
    private RelatedToObjectAnnotationsReference relatedToObjectAnnotations;
// Set properties

    private Set<UsedInContextsReference> usedInContexts;
    private Set<ContextRelevantTermsReference> contextRelevantTerms;
    private Set<TranslationsReference> translations;
    private Set<PreferredTermsReference> preferredTerms;
    private Set<AlternateTermsReference> alternateTerms;
    private Set<ValidValuesReference> validValues;
    private Set<ValidValueForReference> validValueFor;
    private Set<ReplacementTermsReference> replacementTerms;
    private Set<ReplacedTermsReference> replacedTerms;
    private Set<CategoriesReference> categories;
    private Set<TypesReference> types;
    private Set<AttributesTypedByReference> attributesTypedBy;
    private Set<SynonymsReference> synonyms;
    private Set<AntonymsReference> antonyms;
    private Set<IsAReference> isA;
    private Set<ClassifiesReference> classifies;
    private Set<SubtypesReference> subtypes;
    private Set<SupertypesReference> supertypes;
    private Set<AssignedElementsReference> assignedElements;
    private Set<SeeAlsoReference> seeAlso;
    private Set<ExternalGlossaryTermsReference> externalGlossaryTerms;
    private Set<AttributesReference> attributes;
    private Set<ObjectsReference> objects;
    private Set<RelatedMediaReference> relatedMedia;
    private Set<SupportingResourcesReference> supportingResources;
    private Set<CertificationsReference> certifications;
    private Set<NoteLogsReference> noteLogs;
    private Set<GovernedByRolesReference> governedByRoles;
    private Set<CommentsReference> comments;
    private Set<ContributorsReference> contributors;
    private Set<LicensesReference> licenses;
    private Set<RelatedActionsReference> relatedActions;
    private Set<StarRatingsReference> starRatings;
    private Set<RelatedFromObjectAnnotationsReference> relatedFromObjectAnnotations;
    private Set<ActionsReference> actions;
    private Set<DataClassesAssignedToElementReference> dataClassesAssignedToElement;
    private Set<ManagedResourcesReference> managedResources;
    private Set<AlsoKnownAsReference> alsoKnownAs;
    private Set<ResourceListAnchorsReference> resourceListAnchors;
    private Set<ExternalReferenceReference> externalReference;
    private Set<ImpactingProjectsReference> impactingProjects;
    private Set<MeaningReference> meaning;
    private Set<FoundInCollectionsReference> foundInCollections;
    private Set<FacetsReference> facets;
    private Set<MeetingsReference> meetings;
    private Set<TagsReference> tags;
    private Set<LikesReference> likes;

// List properties

    // setters and setters
    public AnchorReference getAnchorReference() {
        return anchor;   }

    public void setAnchorReference(AnchorReference anchor) {
        this.anchor = anchor; }
    public RelatedToObjectAnnotationsReference getRelatedToObjectAnnotationsReference() {
        return relatedToObjectAnnotations;   }

    public void setRelatedToObjectAnnotationsReference(RelatedToObjectAnnotationsReference relatedToObjectAnnotations) {
        this.relatedToObjectAnnotations = relatedToObjectAnnotations; }

// Sets
    public Set<UsedInContextsReference> getUsedInContextsReferences() {
        return usedInContexts;
    }

    public void setUsedInContextsReferences(Set<UsedInContextsReference> usedInContexts) {
        this.usedInContexts =usedInContexts;
    }
    public Set<ContextRelevantTermsReference> getContextRelevantTermsReferences() {
        return contextRelevantTerms;
    }

    public void setContextRelevantTermsReferences(Set<ContextRelevantTermsReference> contextRelevantTerms) {
        this.contextRelevantTerms =contextRelevantTerms;
    }
    public Set<TranslationsReference> getTranslationsReferences() {
        return translations;
    }

    public void setTranslationsReferences(Set<TranslationsReference> translations) {
        this.translations =translations;
    }
    public Set<PreferredTermsReference> getPreferredTermsReferences() {
        return preferredTerms;
    }

    public void setPreferredTermsReferences(Set<PreferredTermsReference> preferredTerms) {
        this.preferredTerms =preferredTerms;
    }
    public Set<AlternateTermsReference> getAlternateTermsReferences() {
        return alternateTerms;
    }

    public void setAlternateTermsReferences(Set<AlternateTermsReference> alternateTerms) {
        this.alternateTerms =alternateTerms;
    }
    public Set<ValidValuesReference> getValidValuesReferences() {
        return validValues;
    }

    public void setValidValuesReferences(Set<ValidValuesReference> validValues) {
        this.validValues =validValues;
    }
    public Set<ValidValueForReference> getValidValueForReferences() {
        return validValueFor;
    }

    public void setValidValueForReferences(Set<ValidValueForReference> validValueFor) {
        this.validValueFor =validValueFor;
    }
    public Set<ReplacementTermsReference> getReplacementTermsReferences() {
        return replacementTerms;
    }

    public void setReplacementTermsReferences(Set<ReplacementTermsReference> replacementTerms) {
        this.replacementTerms =replacementTerms;
    }
    public Set<ReplacedTermsReference> getReplacedTermsReferences() {
        return replacedTerms;
    }

    public void setReplacedTermsReferences(Set<ReplacedTermsReference> replacedTerms) {
        this.replacedTerms =replacedTerms;
    }
    public Set<CategoriesReference> getCategoriesReferences() {
        return categories;
    }

    public void setCategoriesReferences(Set<CategoriesReference> categories) {
        this.categories =categories;
    }
    public Set<TypesReference> getTypesReferences() {
        return types;
    }

    public void setTypesReferences(Set<TypesReference> types) {
        this.types =types;
    }
    public Set<AttributesTypedByReference> getAttributesTypedByReferences() {
        return attributesTypedBy;
    }

    public void setAttributesTypedByReferences(Set<AttributesTypedByReference> attributesTypedBy) {
        this.attributesTypedBy =attributesTypedBy;
    }
    public Set<SynonymsReference> getSynonymsReferences() {
        return synonyms;
    }

    public void setSynonymsReferences(Set<SynonymsReference> synonyms) {
        this.synonyms =synonyms;
    }
    public Set<AntonymsReference> getAntonymsReferences() {
        return antonyms;
    }

    public void setAntonymsReferences(Set<AntonymsReference> antonyms) {
        this.antonyms =antonyms;
    }
    public Set<IsAReference> getIsAReferences() {
        return isA;
    }

    public void setIsAReferences(Set<IsAReference> isA) {
        this.isA =isA;
    }
    public Set<ClassifiesReference> getClassifiesReferences() {
        return classifies;
    }

    public void setClassifiesReferences(Set<ClassifiesReference> classifies) {
        this.classifies =classifies;
    }
    public Set<SubtypesReference> getSubtypesReferences() {
        return subtypes;
    }

    public void setSubtypesReferences(Set<SubtypesReference> subtypes) {
        this.subtypes =subtypes;
    }
    public Set<SupertypesReference> getSupertypesReferences() {
        return supertypes;
    }

    public void setSupertypesReferences(Set<SupertypesReference> supertypes) {
        this.supertypes =supertypes;
    }
    public Set<AssignedElementsReference> getAssignedElementsReferences() {
        return assignedElements;
    }

    public void setAssignedElementsReferences(Set<AssignedElementsReference> assignedElements) {
        this.assignedElements =assignedElements;
    }
    public Set<SeeAlsoReference> getSeeAlsoReferences() {
        return seeAlso;
    }

    public void setSeeAlsoReferences(Set<SeeAlsoReference> seeAlso) {
        this.seeAlso =seeAlso;
    }
    public Set<ExternalGlossaryTermsReference> getExternalGlossaryTermsReferences() {
        return externalGlossaryTerms;
    }

    public void setExternalGlossaryTermsReferences(Set<ExternalGlossaryTermsReference> externalGlossaryTerms) {
        this.externalGlossaryTerms =externalGlossaryTerms;
    }
    public Set<AttributesReference> getAttributesReferences() {
        return attributes;
    }

    public void setAttributesReferences(Set<AttributesReference> attributes) {
        this.attributes =attributes;
    }
    public Set<ObjectsReference> getObjectsReferences() {
        return objects;
    }

    public void setObjectsReferences(Set<ObjectsReference> objects) {
        this.objects =objects;
    }
    public Set<RelatedMediaReference> getRelatedMediaReferences() {
        return relatedMedia;
    }

    public void setRelatedMediaReferences(Set<RelatedMediaReference> relatedMedia) {
        this.relatedMedia =relatedMedia;
    }
    public Set<SupportingResourcesReference> getSupportingResourcesReferences() {
        return supportingResources;
    }

    public void setSupportingResourcesReferences(Set<SupportingResourcesReference> supportingResources) {
        this.supportingResources =supportingResources;
    }
    public Set<CertificationsReference> getCertificationsReferences() {
        return certifications;
    }

    public void setCertificationsReferences(Set<CertificationsReference> certifications) {
        this.certifications =certifications;
    }
    public Set<NoteLogsReference> getNoteLogsReferences() {
        return noteLogs;
    }

    public void setNoteLogsReferences(Set<NoteLogsReference> noteLogs) {
        this.noteLogs =noteLogs;
    }
    public Set<GovernedByRolesReference> getGovernedByRolesReferences() {
        return governedByRoles;
    }

    public void setGovernedByRolesReferences(Set<GovernedByRolesReference> governedByRoles) {
        this.governedByRoles =governedByRoles;
    }
    public Set<CommentsReference> getCommentsReferences() {
        return comments;
    }

    public void setCommentsReferences(Set<CommentsReference> comments) {
        this.comments =comments;
    }
    public Set<ContributorsReference> getContributorsReferences() {
        return contributors;
    }

    public void setContributorsReferences(Set<ContributorsReference> contributors) {
        this.contributors =contributors;
    }
    public Set<LicensesReference> getLicensesReferences() {
        return licenses;
    }

    public void setLicensesReferences(Set<LicensesReference> licenses) {
        this.licenses =licenses;
    }
    public Set<RelatedActionsReference> getRelatedActionsReferences() {
        return relatedActions;
    }

    public void setRelatedActionsReferences(Set<RelatedActionsReference> relatedActions) {
        this.relatedActions =relatedActions;
    }
    public Set<StarRatingsReference> getStarRatingsReferences() {
        return starRatings;
    }

    public void setStarRatingsReferences(Set<StarRatingsReference> starRatings) {
        this.starRatings =starRatings;
    }
    public Set<RelatedFromObjectAnnotationsReference> getRelatedFromObjectAnnotationsReferences() {
        return relatedFromObjectAnnotations;
    }

    public void setRelatedFromObjectAnnotationsReferences(Set<RelatedFromObjectAnnotationsReference> relatedFromObjectAnnotations) {
        this.relatedFromObjectAnnotations =relatedFromObjectAnnotations;
    }
    public Set<ActionsReference> getActionsReferences() {
        return actions;
    }

    public void setActionsReferences(Set<ActionsReference> actions) {
        this.actions =actions;
    }
    public Set<DataClassesAssignedToElementReference> getDataClassesAssignedToElementReferences() {
        return dataClassesAssignedToElement;
    }

    public void setDataClassesAssignedToElementReferences(Set<DataClassesAssignedToElementReference> dataClassesAssignedToElement) {
        this.dataClassesAssignedToElement =dataClassesAssignedToElement;
    }
    public Set<ManagedResourcesReference> getManagedResourcesReferences() {
        return managedResources;
    }

    public void setManagedResourcesReferences(Set<ManagedResourcesReference> managedResources) {
        this.managedResources =managedResources;
    }
    public Set<AlsoKnownAsReference> getAlsoKnownAsReferences() {
        return alsoKnownAs;
    }

    public void setAlsoKnownAsReferences(Set<AlsoKnownAsReference> alsoKnownAs) {
        this.alsoKnownAs =alsoKnownAs;
    }
    public Set<ResourceListAnchorsReference> getResourceListAnchorsReferences() {
        return resourceListAnchors;
    }

    public void setResourceListAnchorsReferences(Set<ResourceListAnchorsReference> resourceListAnchors) {
        this.resourceListAnchors =resourceListAnchors;
    }
    public Set<ExternalReferenceReference> getExternalReferenceReferences() {
        return externalReference;
    }

    public void setExternalReferenceReferences(Set<ExternalReferenceReference> externalReference) {
        this.externalReference =externalReference;
    }
    public Set<ImpactingProjectsReference> getImpactingProjectsReferences() {
        return impactingProjects;
    }

    public void setImpactingProjectsReferences(Set<ImpactingProjectsReference> impactingProjects) {
        this.impactingProjects =impactingProjects;
    }
    public Set<MeaningReference> getMeaningReferences() {
        return meaning;
    }

    public void setMeaningReferences(Set<MeaningReference> meaning) {
        this.meaning =meaning;
    }
    public Set<FoundInCollectionsReference> getFoundInCollectionsReferences() {
        return foundInCollections;
    }

    public void setFoundInCollectionsReferences(Set<FoundInCollectionsReference> foundInCollections) {
        this.foundInCollections =foundInCollections;
    }
    public Set<FacetsReference> getFacetsReferences() {
        return facets;
    }

    public void setFacetsReferences(Set<FacetsReference> facets) {
        this.facets =facets;
    }
    public Set<MeetingsReference> getMeetingsReferences() {
        return meetings;
    }

    public void setMeetingsReferences(Set<MeetingsReference> meetings) {
        this.meetings =meetings;
    }
    public Set<TagsReference> getTagsReferences() {
        return tags;
    }

    public void setTagsReferences(Set<TagsReference> tags) {
        this.tags =tags;
    }
    public Set<LikesReference> getLikesReferences() {
        return likes;
    }

    public void setLikesReferences(Set<LikesReference> likes) {
        this.likes =likes;
    }

// Lists

 public StringBuilder toString(StringBuilder sb) {
        if (sb == null) {
            sb = new StringBuilder();
        }

        sb.append("GlossaryTermReferences{");
        sb.append("usedInContextsReference='").append(usedInContexts.toString());
        sb.append("contextRelevantTermsReference='").append(contextRelevantTerms.toString());
        sb.append("translationsReference='").append(translations.toString());
        sb.append("preferredTermsReference='").append(preferredTerms.toString());
        sb.append("alternateTermsReference='").append(alternateTerms.toString());
        sb.append("validValuesReference='").append(validValues.toString());
        sb.append("validValueForReference='").append(validValueFor.toString());
        sb.append("replacementTermsReference='").append(replacementTerms.toString());
        sb.append("replacedTermsReference='").append(replacedTerms.toString());
        sb.append("categoriesReference='").append(categories.toString());
        sb.append("typesReference='").append(types.toString());
        sb.append("attributesTypedByReference='").append(attributesTypedBy.toString());
        sb.append("synonymsReference='").append(synonyms.toString());
        sb.append("antonymsReference='").append(antonyms.toString());
        sb.append("isAReference='").append(isA.toString());
        sb.append("classifiesReference='").append(classifies.toString());
        sb.append("subtypesReference='").append(subtypes.toString());
        sb.append("supertypesReference='").append(supertypes.toString());
        sb.append("assignedElementsReference='").append(assignedElements.toString());
        sb.append("seeAlsoReference='").append(seeAlso.toString());
        sb.append("externalGlossaryTermsReference='").append(externalGlossaryTerms.toString());
        sb.append("attributesReference='").append(attributes.toString());
        sb.append("objectsReference='").append(objects.toString());
        sb.append("relatedMediaReference='").append(relatedMedia.toString());
        sb.append("supportingResourcesReference='").append(supportingResources.toString());
        sb.append("certificationsReference='").append(certifications.toString());
        sb.append("noteLogsReference='").append(noteLogs.toString());
        sb.append("governedByRolesReference='").append(governedByRoles.toString());
        sb.append("commentsReference='").append(comments.toString());
        sb.append("contributorsReference='").append(contributors.toString());
        sb.append("licensesReference='").append(licenses.toString());
        sb.append("relatedActionsReference='").append(relatedActions.toString());
        sb.append("starRatingsReference='").append(starRatings.toString());
        sb.append("relatedFromObjectAnnotationsReference='").append(relatedFromObjectAnnotations.toString());
        sb.append("actionsReference='").append(actions.toString());
        sb.append("dataClassesAssignedToElementReference='").append(dataClassesAssignedToElement.toString());
        sb.append("managedResourcesReference='").append(managedResources.toString());
        sb.append("alsoKnownAsReference='").append(alsoKnownAs.toString());
        sb.append("resourceListAnchorsReference='").append(resourceListAnchors.toString());
        sb.append("externalReferenceReference='").append(externalReference.toString());
        sb.append("impactingProjectsReference='").append(impactingProjects.toString());
        sb.append("meaningReference='").append(meaning.toString());
        sb.append("foundInCollectionsReference='").append(foundInCollections.toString());
        sb.append("facetsReference='").append(facets.toString());
        sb.append("meetingsReference='").append(meetings.toString());
        sb.append("tagsReference='").append(tags.toString());
        sb.append("likesReference='").append(likes.toString());
        sb.append("anchorReference='").append(anchor.toString());
        sb.append("relatedToObjectAnnotationsReference='").append(relatedToObjectAnnotations.toString());

        sb.append('}');

        return sb;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) { return true; }
        if (o == null || getClass() != o.getClass()) { return false; }
        if (!super.equals(o)) { return false; }
         GlossaryTermReferences typedThat = (GlossaryTermReferences) o;
      // compare single cardinality attributes
         if (this.usedInContexts != null && !Objects.equals(this.usedInContexts,typedThat.usedInContexts)) {
                            return false;
                 }
         if (this.contextRelevantTerms != null && !Objects.equals(this.contextRelevantTerms,typedThat.contextRelevantTerms)) {
                            return false;
                 }
         if (this.translations != null && !Objects.equals(this.translations,typedThat.translations)) {
                            return false;
                 }
         if (this.preferredTerms != null && !Objects.equals(this.preferredTerms,typedThat.preferredTerms)) {
                            return false;
                 }
         if (this.alternateTerms != null && !Objects.equals(this.alternateTerms,typedThat.alternateTerms)) {
                            return false;
                 }
         if (this.validValues != null && !Objects.equals(this.validValues,typedThat.validValues)) {
                            return false;
                 }
         if (this.validValueFor != null && !Objects.equals(this.validValueFor,typedThat.validValueFor)) {
                            return false;
                 }
         if (this.replacementTerms != null && !Objects.equals(this.replacementTerms,typedThat.replacementTerms)) {
                            return false;
                 }
         if (this.replacedTerms != null && !Objects.equals(this.replacedTerms,typedThat.replacedTerms)) {
                            return false;
                 }
         if (this.categories != null && !Objects.equals(this.categories,typedThat.categories)) {
                            return false;
                 }
         if (this.types != null && !Objects.equals(this.types,typedThat.types)) {
                            return false;
                 }
         if (this.attributesTypedBy != null && !Objects.equals(this.attributesTypedBy,typedThat.attributesTypedBy)) {
                            return false;
                 }
         if (this.synonyms != null && !Objects.equals(this.synonyms,typedThat.synonyms)) {
                            return false;
                 }
         if (this.antonyms != null && !Objects.equals(this.antonyms,typedThat.antonyms)) {
                            return false;
                 }
         if (this.isA != null && !Objects.equals(this.isA,typedThat.isA)) {
                            return false;
                 }
         if (this.classifies != null && !Objects.equals(this.classifies,typedThat.classifies)) {
                            return false;
                 }
         if (this.subtypes != null && !Objects.equals(this.subtypes,typedThat.subtypes)) {
                            return false;
                 }
         if (this.supertypes != null && !Objects.equals(this.supertypes,typedThat.supertypes)) {
                            return false;
                 }
         if (this.assignedElements != null && !Objects.equals(this.assignedElements,typedThat.assignedElements)) {
                            return false;
                 }
         if (this.seeAlso != null && !Objects.equals(this.seeAlso,typedThat.seeAlso)) {
                            return false;
                 }
         if (this.externalGlossaryTerms != null && !Objects.equals(this.externalGlossaryTerms,typedThat.externalGlossaryTerms)) {
                            return false;
                 }
         if (this.attributes != null && !Objects.equals(this.attributes,typedThat.attributes)) {
                            return false;
                 }
         if (this.objects != null && !Objects.equals(this.objects,typedThat.objects)) {
                            return false;
                 }
         if (this.anchor != null && !Objects.equals(this.anchor,typedThat.anchor)) {
                            return false;
                 }
         if (this.relatedMedia != null && !Objects.equals(this.relatedMedia,typedThat.relatedMedia)) {
                            return false;
                 }
         if (this.supportingResources != null && !Objects.equals(this.supportingResources,typedThat.supportingResources)) {
                            return false;
                 }
         if (this.certifications != null && !Objects.equals(this.certifications,typedThat.certifications)) {
                            return false;
                 }
         if (this.noteLogs != null && !Objects.equals(this.noteLogs,typedThat.noteLogs)) {
                            return false;
                 }
         if (this.governedByRoles != null && !Objects.equals(this.governedByRoles,typedThat.governedByRoles)) {
                            return false;
                 }
         if (this.comments != null && !Objects.equals(this.comments,typedThat.comments)) {
                            return false;
                 }
         if (this.contributors != null && !Objects.equals(this.contributors,typedThat.contributors)) {
                            return false;
                 }
         if (this.licenses != null && !Objects.equals(this.licenses,typedThat.licenses)) {
                            return false;
                 }
         if (this.relatedActions != null && !Objects.equals(this.relatedActions,typedThat.relatedActions)) {
                            return false;
                 }
         if (this.starRatings != null && !Objects.equals(this.starRatings,typedThat.starRatings)) {
                            return false;
                 }
         if (this.relatedFromObjectAnnotations != null && !Objects.equals(this.relatedFromObjectAnnotations,typedThat.relatedFromObjectAnnotations)) {
                            return false;
                 }
         if (this.actions != null && !Objects.equals(this.actions,typedThat.actions)) {
                            return false;
                 }
         if (this.dataClassesAssignedToElement != null && !Objects.equals(this.dataClassesAssignedToElement,typedThat.dataClassesAssignedToElement)) {
                            return false;
                 }
         if (this.managedResources != null && !Objects.equals(this.managedResources,typedThat.managedResources)) {
                            return false;
                 }
         if (this.alsoKnownAs != null && !Objects.equals(this.alsoKnownAs,typedThat.alsoKnownAs)) {
                            return false;
                 }
         if (this.resourceListAnchors != null && !Objects.equals(this.resourceListAnchors,typedThat.resourceListAnchors)) {
                            return false;
                 }
         if (this.relatedToObjectAnnotations != null && !Objects.equals(this.relatedToObjectAnnotations,typedThat.relatedToObjectAnnotations)) {
                            return false;
                 }
         if (this.externalReference != null && !Objects.equals(this.externalReference,typedThat.externalReference)) {
                            return false;
                 }
         if (this.impactingProjects != null && !Objects.equals(this.impactingProjects,typedThat.impactingProjects)) {
                            return false;
                 }
         if (this.meaning != null && !Objects.equals(this.meaning,typedThat.meaning)) {
                            return false;
                 }
         if (this.foundInCollections != null && !Objects.equals(this.foundInCollections,typedThat.foundInCollections)) {
                            return false;
                 }
         if (this.facets != null && !Objects.equals(this.facets,typedThat.facets)) {
                            return false;
                 }
         if (this.meetings != null && !Objects.equals(this.meetings,typedThat.meetings)) {
                            return false;
                 }
         if (this.tags != null && !Objects.equals(this.tags,typedThat.tags)) {
                            return false;
                 }
         if (this.likes != null && !Objects.equals(this.likes,typedThat.likes)) {
                            return false;
                 }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode()
    ,this.usedInContexts
    ,this.contextRelevantTerms
    ,this.translations
    ,this.preferredTerms
    ,this.alternateTerms
    ,this.validValues
    ,this.validValueFor
    ,this.replacementTerms
    ,this.replacedTerms
    ,this.categories
    ,this.types
    ,this.attributesTypedBy
    ,this.synonyms
    ,this.antonyms
    ,this.isA
    ,this.classifies
    ,this.subtypes
    ,this.supertypes
    ,this.assignedElements
    ,this.seeAlso
    ,this.externalGlossaryTerms
    ,this.attributes
    ,this.objects
    ,this.anchor
    ,this.relatedMedia
    ,this.supportingResources
    ,this.certifications
    ,this.noteLogs
    ,this.governedByRoles
    ,this.comments
    ,this.contributors
    ,this.licenses
    ,this.relatedActions
    ,this.starRatings
    ,this.relatedFromObjectAnnotations
    ,this.actions
    ,this.dataClassesAssignedToElement
    ,this.managedResources
    ,this.alsoKnownAs
    ,this.resourceListAnchors
    ,this.relatedToObjectAnnotations
    ,this.externalReference
    ,this.impactingProjects
    ,this.meaning
    ,this.foundInCollections
    ,this.facets
    ,this.meetings
    ,this.tags
    ,this.likes
       );
    }

    @Override
    public String toString() {
        return toString(new StringBuilder()).toString();
    }
}
