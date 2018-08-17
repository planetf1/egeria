/* SPDX-License-Identifier: Apache-2.0 */

// This is a generated file - do not edit - changes should be made to the templates amd/or generator to generate this file with changes.

package org.odpi.openmetadata.accessservices.subjectarea.generated.entities.SoftwareServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;

import org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.InvalidParameterException;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.line.Line;
import org.odpi.openmetadata.accessservices.subjectarea.generated.references.SoftwareServerToEndpoint.EndpointsReference;
import org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.ServerEndpoint.ServerEndpoint;
import org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.ServerEndpoint.ServerEndpointMapper;
import org.odpi.openmetadata.accessservices.subjectarea.generated.references.SoftwareServerToSoftwareServerCapability.CapabilitiesReference;
import org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.ServerSupportedCapability.ServerSupportedCapability;
import org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.ServerSupportedCapability.ServerSupportedCapabilityMapper;
import org.odpi.openmetadata.accessservices.subjectarea.generated.references.SoftwareServerToHost.HostReference;
import org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.ServerDeployment.ServerDeployment;
import org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.ServerDeployment.ServerDeploymentMapper;
import org.odpi.openmetadata.accessservices.subjectarea.generated.references.AssetToDataSet.SupportedDataSetsReference;
import org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.DataContentForDataSet.DataContentForDataSet;
import org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.DataContentForDataSet.DataContentForDataSetMapper;
import org.odpi.openmetadata.accessservices.subjectarea.generated.references.AssetToSoftwareServerCapability.ConsumedByReference;
import org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.AssetServerUse.AssetServerUse;
import org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.AssetServerUse.AssetServerUseMapper;
import org.odpi.openmetadata.accessservices.subjectarea.generated.references.AssetToSchemaType.SchemaReference;
import org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.AssetSchemaType.AssetSchemaType;
import org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.AssetSchemaType.AssetSchemaTypeMapper;
import org.odpi.openmetadata.accessservices.subjectarea.generated.references.AssetToConnection.ConnectionsReference;
import org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.ConnectionToAsset.ConnectionToAsset;
import org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.ConnectionToAsset.ConnectionToAssetMapper;
import org.odpi.openmetadata.accessservices.subjectarea.generated.references.ReferenceableToMeeting.MeetingsReference;
import org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.MeetingOnReferenceable.MeetingOnReferenceable;
import org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.MeetingOnReferenceable.MeetingOnReferenceableMapper;
import org.odpi.openmetadata.accessservices.subjectarea.generated.references.ReferenceableToInformalTag.TagsReference;
import org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.AttachedTag.AttachedTag;
import org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.AttachedTag.AttachedTagMapper;
import org.odpi.openmetadata.accessservices.subjectarea.generated.references.ReferenceableToExternalId.AlsoKnownAsReference;
import org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.ExternalIdLink.ExternalIdLink;
import org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.ExternalIdLink.ExternalIdLinkMapper;
import org.odpi.openmetadata.accessservices.subjectarea.generated.references.ReferenceableToExternalId.ManagedResourcesReference;
import org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.ExternalIdScope.ExternalIdScope;
import org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.ExternalIdScope.ExternalIdScopeMapper;
import org.odpi.openmetadata.accessservices.subjectarea.generated.references.ReferenceableToToDo.TodosReference;
import org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.ToDoOnReferenceable.ToDoOnReferenceable;
import org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.ToDoOnReferenceable.ToDoOnReferenceableMapper;
import org.odpi.openmetadata.accessservices.subjectarea.generated.references.ReferenceableToNoteLog.NoteLogsReference;
import org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.AttachedNoteLog.AttachedNoteLog;
import org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.AttachedNoteLog.AttachedNoteLogMapper;
import org.odpi.openmetadata.accessservices.subjectarea.generated.references.AssetToGovernanceZone.ZonesReference;
import org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.ZoneMembership.ZoneMembership;
import org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.ZoneMembership.ZoneMembershipMapper;
import org.odpi.openmetadata.accessservices.subjectarea.generated.references.ReferenceableToLicenseType.LicensesReference;
import org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.License.License;
import org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.License.LicenseMapper;
import org.odpi.openmetadata.accessservices.subjectarea.generated.references.ReferenceableToPropertyFacet.FacetsReference;
import org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.ReferenceableFacet.ReferenceableFacet;
import org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.ReferenceableFacet.ReferenceableFacetMapper;
import org.odpi.openmetadata.accessservices.subjectarea.generated.references.ReferenceableToLike.LikesReference;
import org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.AttachedLike.AttachedLike;
import org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.AttachedLike.AttachedLikeMapper;
import org.odpi.openmetadata.accessservices.subjectarea.generated.references.ReferenceableToToDo.ActionsReference;
import org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.ToDoSource.ToDoSource;
import org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.ToDoSource.ToDoSourceMapper;
import org.odpi.openmetadata.accessservices.subjectarea.generated.references.ReferenceableToGlossaryTerm.MeaningReference;
import org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.SemanticAssignment.SemanticAssignment;
import org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.SemanticAssignment.SemanticAssignmentMapper;
import org.odpi.openmetadata.accessservices.subjectarea.generated.references.ReferenceableToComment.CommentsReference;
import org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.AttachedComment.AttachedComment;
import org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.AttachedComment.AttachedCommentMapper;
import org.odpi.openmetadata.accessservices.subjectarea.generated.references.ReferenceableToRating.StarRatingsReference;
import org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.AttachedRating.AttachedRating;
import org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.AttachedRating.AttachedRatingMapper;
import org.odpi.openmetadata.accessservices.subjectarea.generated.references.ReferenceableToCollection.FoundInCollectionsReference;
import org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.CollectionMember.CollectionMember;
import org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.CollectionMember.CollectionMemberMapper;
import org.odpi.openmetadata.accessservices.subjectarea.generated.references.ReferenceableToExternalReference.ExternalReferenceReference;
import org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.ExternalReferenceLink.ExternalReferenceLink;
import org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.ExternalReferenceLink.ExternalReferenceLinkMapper;
import org.odpi.openmetadata.accessservices.subjectarea.generated.references.ReferenceableToRelatedMedia.RelatedMediaReference;
import org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.MediaReference.MediaReference;
import org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.MediaReference.MediaReferenceMapper;
import org.odpi.openmetadata.accessservices.subjectarea.generated.references.ReferenceableToCertificationType.CertificationsReference;
import org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.Certification.Certification;
import org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.Certification.CertificationMapper;
import org.odpi.openmetadata.accessservices.subjectarea.generated.references.ReferenceableToActorProfile.ContributorsReference;
import org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.Contributor.Contributor;
import org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.Contributor.ContributorMapper;
import org.odpi.openmetadata.accessservices.subjectarea.generated.references.ReferenceableToPerson.StaffReference;
import org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.StaffAssignment.StaffAssignment;
import org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.StaffAssignment.StaffAssignmentMapper;

import java.io.Serializable;
import java.util.*;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 *
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class SoftwareServerReferences implements Serializable {
    private static final Logger log = LoggerFactory.getLogger(SoftwareServerReferences.class);
    private static final String className = SoftwareServerReferences.class.getName();

    public static final String[] REFERENCE_NAMES_SET_VALUES = new String[] {
             "endpoints",
             "capabilities",
             "host",
             "supportedDataSets",
             "consumedBy",
             "schema",
             "connections",
             "meetings",
             "tags",
             "alsoKnownAs",
             "managedResources",
             "todos",
             "noteLogs",
             "zones",
             "licenses",
             "facets",
             "likes",
             "actions",
             "meaning",
             "comments",
             "starRatings",
             "foundInCollections",
             "externalReference",
             "relatedMedia",
             "certifications",
             "contributors",
             "staff",
             // Terminate the list
             null
    };

     public static final String[] RELATIONSHIP_NAMES_SET_VALUES = new String[] {
             "ServerEndpoint",
             "ServerSupportedCapability",
             "ServerDeployment",
             "DataContentForDataSet",
             "AssetServerUse",
             "AssetSchemaType",
             "ConnectionToAsset",
             "MeetingOnReferenceable",
             "AttachedTag",
             "ExternalIdLink",
             "ExternalIdScope",
             "ToDoOnReferenceable",
             "AttachedNoteLog",
             "ZoneMembership",
             "License",
             "ReferenceableFacet",
             "AttachedLike",
             "ToDoSource",
             "SemanticAssignment",
             "AttachedComment",
             "AttachedRating",
             "CollectionMember",
             "ExternalReferenceLink",
             "MediaReference",
             "Certification",
             "Contributor",
             "StaffAssignment",
              // Terminate the list
              null
     };
     /**
       * We are passed a set of omrs relationships that are associated with a entity SoftwareServer
       * Each of these relationships should map to a reference (a uniquely named attribute in SoftwareServer).
       *
       * Relationships have cardinality. There are 2 sorts of cardinality relevant here, whether the relationship can be related to one or many
       * entities.
       *
       * @param lines
       * @param entityGuid
       * @throws InvalidParameterException
       */
     public SoftwareServerReferences(String entityGuid, List<Line> lines) throws InvalidParameterException {
         for (Line relationship: lines) {
            for (int i=0;i< RELATIONSHIP_NAMES_SET_VALUES.length; i++) {
               if (relationship.getName().equals(RELATIONSHIP_NAMES_SET_VALUES[i])) {
                    String referenceName = REFERENCE_NAMES_SET_VALUES[i];

                    if ("endpoints".equals(referenceName)) {
                         ServerEndpoint serverEndpoint_relationship = (ServerEndpoint)relationship;
                         EndpointsReference endpointsReference = new EndpointsReference(entityGuid,serverEndpoint_relationship);
                         if ( endpoints== null ) {
                              endpoints = new HashSet();
                         }
                          endpoints.add(endpointsReference);
                    }
                    if ("capabilities".equals(referenceName)) {
                         ServerSupportedCapability serverSupportedCapability_relationship = (ServerSupportedCapability)relationship;
                         CapabilitiesReference capabilitiesReference = new CapabilitiesReference(entityGuid,serverSupportedCapability_relationship);
                         if ( capabilities== null ) {
                              capabilities = new HashSet();
                         }
                          capabilities.add(capabilitiesReference);
                    }
                    if ("supportedDataSets".equals(referenceName)) {
                         DataContentForDataSet dataContentForDataSet_relationship = (DataContentForDataSet)relationship;
                         SupportedDataSetsReference supportedDataSetsReference = new SupportedDataSetsReference(entityGuid,dataContentForDataSet_relationship);
                         if ( supportedDataSets== null ) {
                              supportedDataSets = new HashSet();
                         }
                          supportedDataSets.add(supportedDataSetsReference);
                    }
                    if ("consumedBy".equals(referenceName)) {
                         AssetServerUse assetServerUse_relationship = (AssetServerUse)relationship;
                         ConsumedByReference consumedByReference = new ConsumedByReference(entityGuid,assetServerUse_relationship);
                         if ( consumedBy== null ) {
                              consumedBy = new HashSet();
                         }
                          consumedBy.add(consumedByReference);
                    }
                    if ("connections".equals(referenceName)) {
                         ConnectionToAsset connectionToAsset_relationship = (ConnectionToAsset)relationship;
                         ConnectionsReference connectionsReference = new ConnectionsReference(entityGuid,connectionToAsset_relationship);
                         if ( connections== null ) {
                              connections = new HashSet();
                         }
                          connections.add(connectionsReference);
                    }
                    if ("meetings".equals(referenceName)) {
                         MeetingOnReferenceable meetingOnReferenceable_relationship = (MeetingOnReferenceable)relationship;
                         MeetingsReference meetingsReference = new MeetingsReference(entityGuid,meetingOnReferenceable_relationship);
                         if ( meetings== null ) {
                              meetings = new HashSet();
                         }
                          meetings.add(meetingsReference);
                    }
                    if ("tags".equals(referenceName)) {
                         AttachedTag attachedTag_relationship = (AttachedTag)relationship;
                         TagsReference tagsReference = new TagsReference(entityGuid,attachedTag_relationship);
                         if ( tags== null ) {
                              tags = new HashSet();
                         }
                          tags.add(tagsReference);
                    }
                    if ("alsoKnownAs".equals(referenceName)) {
                         ExternalIdLink externalIdLink_relationship = (ExternalIdLink)relationship;
                         AlsoKnownAsReference alsoKnownAsReference = new AlsoKnownAsReference(entityGuid,externalIdLink_relationship);
                         if ( alsoKnownAs== null ) {
                              alsoKnownAs = new HashSet();
                         }
                          alsoKnownAs.add(alsoKnownAsReference);
                    }
                    if ("managedResources".equals(referenceName)) {
                         ExternalIdScope externalIdScope_relationship = (ExternalIdScope)relationship;
                         ManagedResourcesReference managedResourcesReference = new ManagedResourcesReference(entityGuid,externalIdScope_relationship);
                         if ( managedResources== null ) {
                              managedResources = new HashSet();
                         }
                          managedResources.add(managedResourcesReference);
                    }
                    if ("todos".equals(referenceName)) {
                         ToDoOnReferenceable toDoOnReferenceable_relationship = (ToDoOnReferenceable)relationship;
                         TodosReference todosReference = new TodosReference(entityGuid,toDoOnReferenceable_relationship);
                         if ( todos== null ) {
                              todos = new HashSet();
                         }
                          todos.add(todosReference);
                    }
                    if ("noteLogs".equals(referenceName)) {
                         AttachedNoteLog attachedNoteLog_relationship = (AttachedNoteLog)relationship;
                         NoteLogsReference noteLogsReference = new NoteLogsReference(entityGuid,attachedNoteLog_relationship);
                         if ( noteLogs== null ) {
                              noteLogs = new HashSet();
                         }
                          noteLogs.add(noteLogsReference);
                    }
                    if ("zones".equals(referenceName)) {
                         ZoneMembership zoneMembership_relationship = (ZoneMembership)relationship;
                         ZonesReference zonesReference = new ZonesReference(entityGuid,zoneMembership_relationship);
                         if ( zones== null ) {
                              zones = new HashSet();
                         }
                          zones.add(zonesReference);
                    }
                    if ("licenses".equals(referenceName)) {
                         License license_relationship = (License)relationship;
                         LicensesReference licensesReference = new LicensesReference(entityGuid,license_relationship);
                         if ( licenses== null ) {
                              licenses = new HashSet();
                         }
                          licenses.add(licensesReference);
                    }
                    if ("facets".equals(referenceName)) {
                         ReferenceableFacet referenceableFacet_relationship = (ReferenceableFacet)relationship;
                         FacetsReference facetsReference = new FacetsReference(entityGuid,referenceableFacet_relationship);
                         if ( facets== null ) {
                              facets = new HashSet();
                         }
                          facets.add(facetsReference);
                    }
                    if ("likes".equals(referenceName)) {
                         AttachedLike attachedLike_relationship = (AttachedLike)relationship;
                         LikesReference likesReference = new LikesReference(entityGuid,attachedLike_relationship);
                         if ( likes== null ) {
                              likes = new HashSet();
                         }
                          likes.add(likesReference);
                    }
                    if ("actions".equals(referenceName)) {
                         ToDoSource toDoSource_relationship = (ToDoSource)relationship;
                         ActionsReference actionsReference = new ActionsReference(entityGuid,toDoSource_relationship);
                         if ( actions== null ) {
                              actions = new HashSet();
                         }
                          actions.add(actionsReference);
                    }
                    if ("meaning".equals(referenceName)) {
                         SemanticAssignment semanticAssignment_relationship = (SemanticAssignment)relationship;
                         MeaningReference meaningReference = new MeaningReference(entityGuid,semanticAssignment_relationship);
                         if ( meaning== null ) {
                              meaning = new HashSet();
                         }
                          meaning.add(meaningReference);
                    }
                    if ("comments".equals(referenceName)) {
                         AttachedComment attachedComment_relationship = (AttachedComment)relationship;
                         CommentsReference commentsReference = new CommentsReference(entityGuid,attachedComment_relationship);
                         if ( comments== null ) {
                              comments = new HashSet();
                         }
                          comments.add(commentsReference);
                    }
                    if ("starRatings".equals(referenceName)) {
                         AttachedRating attachedRating_relationship = (AttachedRating)relationship;
                         StarRatingsReference starRatingsReference = new StarRatingsReference(entityGuid,attachedRating_relationship);
                         if ( starRatings== null ) {
                              starRatings = new HashSet();
                         }
                          starRatings.add(starRatingsReference);
                    }
                    if ("foundInCollections".equals(referenceName)) {
                         CollectionMember collectionMember_relationship = (CollectionMember)relationship;
                         FoundInCollectionsReference foundInCollectionsReference = new FoundInCollectionsReference(entityGuid,collectionMember_relationship);
                         if ( foundInCollections== null ) {
                              foundInCollections = new HashSet();
                         }
                          foundInCollections.add(foundInCollectionsReference);
                    }
                    if ("externalReference".equals(referenceName)) {
                         ExternalReferenceLink externalReferenceLink_relationship = (ExternalReferenceLink)relationship;
                         ExternalReferenceReference externalReferenceReference = new ExternalReferenceReference(entityGuid,externalReferenceLink_relationship);
                         if ( externalReference== null ) {
                              externalReference = new HashSet();
                         }
                          externalReference.add(externalReferenceReference);
                    }
                    if ("relatedMedia".equals(referenceName)) {
                         MediaReference mediaReference_relationship = (MediaReference)relationship;
                         RelatedMediaReference relatedMediaReference = new RelatedMediaReference(entityGuid,mediaReference_relationship);
                         if ( relatedMedia== null ) {
                              relatedMedia = new HashSet();
                         }
                          relatedMedia.add(relatedMediaReference);
                    }
                    if ("certifications".equals(referenceName)) {
                         Certification certification_relationship = (Certification)relationship;
                         CertificationsReference certificationsReference = new CertificationsReference(entityGuid,certification_relationship);
                         if ( certifications== null ) {
                              certifications = new HashSet();
                         }
                          certifications.add(certificationsReference);
                    }
                    if ("contributors".equals(referenceName)) {
                         Contributor contributor_relationship = (Contributor)relationship;
                         ContributorsReference contributorsReference = new ContributorsReference(entityGuid,contributor_relationship);
                         if ( contributors== null ) {
                              contributors = new HashSet();
                         }
                          contributors.add(contributorsReference);
                    }
                    if ("staff".equals(referenceName)) {
                         StaffAssignment staffAssignment_relationship = (StaffAssignment)relationship;
                         StaffReference staffReference = new StaffReference(entityGuid,staffAssignment_relationship);
                         if ( staff== null ) {
                              staff = new HashSet();
                         }
                          staff.add(staffReference);
                    }

                    if ("host".equals(referenceName)) {
                         ServerDeployment serverDeployment_relationship = (ServerDeployment)relationship;
                         host = new HostReference(entityGuid, serverDeployment_relationship);
                    }
                    if ("schema".equals(referenceName)) {
                         AssetSchemaType assetSchemaType_relationship = (AssetSchemaType)relationship;
                         schema = new SchemaReference(entityGuid, assetSchemaType_relationship);
                    }
                 }
             }
         }
     }

    public static final Set<String> REFERENCE_NAMES_SET = new HashSet(new HashSet<>(Arrays.asList(REFERENCE_NAMES_SET_VALUES)));
    // there may be duplicate strings in RELATIONSHIP_NAMES_SET_VALUES, the following line deduplicates the Strings into a Set.
    public static final Set<String> RELATIONSHIP_NAMES_SET = new HashSet(new HashSet<>(Arrays.asList(RELATIONSHIP_NAMES_SET_VALUES)));
// Singular properties
    private HostReference host;
    private SchemaReference schema;
// Set properties

    private Set<EndpointsReference> endpoints;
    private Set<CapabilitiesReference> capabilities;
    private Set<SupportedDataSetsReference> supportedDataSets;
    private Set<ConsumedByReference> consumedBy;
    private Set<ConnectionsReference> connections;
    private Set<MeetingsReference> meetings;
    private Set<TagsReference> tags;
    private Set<AlsoKnownAsReference> alsoKnownAs;
    private Set<ManagedResourcesReference> managedResources;
    private Set<TodosReference> todos;
    private Set<NoteLogsReference> noteLogs;
    private Set<ZonesReference> zones;
    private Set<LicensesReference> licenses;
    private Set<FacetsReference> facets;
    private Set<LikesReference> likes;
    private Set<ActionsReference> actions;
    private Set<MeaningReference> meaning;
    private Set<CommentsReference> comments;
    private Set<StarRatingsReference> starRatings;
    private Set<FoundInCollectionsReference> foundInCollections;
    private Set<ExternalReferenceReference> externalReference;
    private Set<RelatedMediaReference> relatedMedia;
    private Set<CertificationsReference> certifications;
    private Set<ContributorsReference> contributors;
    private Set<StaffReference> staff;

// List properties

    // setters and setters
    public HostReference getHostReference() {
        return host;   }

    public void setHostReference(HostReference host) {
        this.host = host; }
    public SchemaReference getSchemaReference() {
        return schema;   }

    public void setSchemaReference(SchemaReference schema) {
        this.schema = schema; }

// Sets
    public Set<EndpointsReference> getEndpointsReferences() {
        return endpoints;
    }

    public void setEndpointsReferences(Set<EndpointsReference> endpoints) {
        this.endpoints =endpoints;
    }
    public Set<CapabilitiesReference> getCapabilitiesReferences() {
        return capabilities;
    }

    public void setCapabilitiesReferences(Set<CapabilitiesReference> capabilities) {
        this.capabilities =capabilities;
    }
    public Set<SupportedDataSetsReference> getSupportedDataSetsReferences() {
        return supportedDataSets;
    }

    public void setSupportedDataSetsReferences(Set<SupportedDataSetsReference> supportedDataSets) {
        this.supportedDataSets =supportedDataSets;
    }
    public Set<ConsumedByReference> getConsumedByReferences() {
        return consumedBy;
    }

    public void setConsumedByReferences(Set<ConsumedByReference> consumedBy) {
        this.consumedBy =consumedBy;
    }
    public Set<ConnectionsReference> getConnectionsReferences() {
        return connections;
    }

    public void setConnectionsReferences(Set<ConnectionsReference> connections) {
        this.connections =connections;
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
    public Set<AlsoKnownAsReference> getAlsoKnownAsReferences() {
        return alsoKnownAs;
    }

    public void setAlsoKnownAsReferences(Set<AlsoKnownAsReference> alsoKnownAs) {
        this.alsoKnownAs =alsoKnownAs;
    }
    public Set<ManagedResourcesReference> getManagedResourcesReferences() {
        return managedResources;
    }

    public void setManagedResourcesReferences(Set<ManagedResourcesReference> managedResources) {
        this.managedResources =managedResources;
    }
    public Set<TodosReference> getTodosReferences() {
        return todos;
    }

    public void setTodosReferences(Set<TodosReference> todos) {
        this.todos =todos;
    }
    public Set<NoteLogsReference> getNoteLogsReferences() {
        return noteLogs;
    }

    public void setNoteLogsReferences(Set<NoteLogsReference> noteLogs) {
        this.noteLogs =noteLogs;
    }
    public Set<ZonesReference> getZonesReferences() {
        return zones;
    }

    public void setZonesReferences(Set<ZonesReference> zones) {
        this.zones =zones;
    }
    public Set<LicensesReference> getLicensesReferences() {
        return licenses;
    }

    public void setLicensesReferences(Set<LicensesReference> licenses) {
        this.licenses =licenses;
    }
    public Set<FacetsReference> getFacetsReferences() {
        return facets;
    }

    public void setFacetsReferences(Set<FacetsReference> facets) {
        this.facets =facets;
    }
    public Set<LikesReference> getLikesReferences() {
        return likes;
    }

    public void setLikesReferences(Set<LikesReference> likes) {
        this.likes =likes;
    }
    public Set<ActionsReference> getActionsReferences() {
        return actions;
    }

    public void setActionsReferences(Set<ActionsReference> actions) {
        this.actions =actions;
    }
    public Set<MeaningReference> getMeaningReferences() {
        return meaning;
    }

    public void setMeaningReferences(Set<MeaningReference> meaning) {
        this.meaning =meaning;
    }
    public Set<CommentsReference> getCommentsReferences() {
        return comments;
    }

    public void setCommentsReferences(Set<CommentsReference> comments) {
        this.comments =comments;
    }
    public Set<StarRatingsReference> getStarRatingsReferences() {
        return starRatings;
    }

    public void setStarRatingsReferences(Set<StarRatingsReference> starRatings) {
        this.starRatings =starRatings;
    }
    public Set<FoundInCollectionsReference> getFoundInCollectionsReferences() {
        return foundInCollections;
    }

    public void setFoundInCollectionsReferences(Set<FoundInCollectionsReference> foundInCollections) {
        this.foundInCollections =foundInCollections;
    }
    public Set<ExternalReferenceReference> getExternalReferenceReferences() {
        return externalReference;
    }

    public void setExternalReferenceReferences(Set<ExternalReferenceReference> externalReference) {
        this.externalReference =externalReference;
    }
    public Set<RelatedMediaReference> getRelatedMediaReferences() {
        return relatedMedia;
    }

    public void setRelatedMediaReferences(Set<RelatedMediaReference> relatedMedia) {
        this.relatedMedia =relatedMedia;
    }
    public Set<CertificationsReference> getCertificationsReferences() {
        return certifications;
    }

    public void setCertificationsReferences(Set<CertificationsReference> certifications) {
        this.certifications =certifications;
    }
    public Set<ContributorsReference> getContributorsReferences() {
        return contributors;
    }

    public void setContributorsReferences(Set<ContributorsReference> contributors) {
        this.contributors =contributors;
    }
    public Set<StaffReference> getStaffReferences() {
        return staff;
    }

    public void setStaffReferences(Set<StaffReference> staff) {
        this.staff =staff;
    }

// Lists

 public StringBuilder toString(StringBuilder sb) {
        if (sb == null) {
            sb = new StringBuilder();
        }

        sb.append("SoftwareServerReferences{");
        sb.append("endpointsReference='").append(endpoints.toString());
        sb.append("capabilitiesReference='").append(capabilities.toString());
        sb.append("supportedDataSetsReference='").append(supportedDataSets.toString());
        sb.append("consumedByReference='").append(consumedBy.toString());
        sb.append("connectionsReference='").append(connections.toString());
        sb.append("meetingsReference='").append(meetings.toString());
        sb.append("tagsReference='").append(tags.toString());
        sb.append("alsoKnownAsReference='").append(alsoKnownAs.toString());
        sb.append("managedResourcesReference='").append(managedResources.toString());
        sb.append("todosReference='").append(todos.toString());
        sb.append("noteLogsReference='").append(noteLogs.toString());
        sb.append("zonesReference='").append(zones.toString());
        sb.append("licensesReference='").append(licenses.toString());
        sb.append("facetsReference='").append(facets.toString());
        sb.append("likesReference='").append(likes.toString());
        sb.append("actionsReference='").append(actions.toString());
        sb.append("meaningReference='").append(meaning.toString());
        sb.append("commentsReference='").append(comments.toString());
        sb.append("starRatingsReference='").append(starRatings.toString());
        sb.append("foundInCollectionsReference='").append(foundInCollections.toString());
        sb.append("externalReferenceReference='").append(externalReference.toString());
        sb.append("relatedMediaReference='").append(relatedMedia.toString());
        sb.append("certificationsReference='").append(certifications.toString());
        sb.append("contributorsReference='").append(contributors.toString());
        sb.append("staffReference='").append(staff.toString());
        sb.append("hostReference='").append(host.toString());
        sb.append("schemaReference='").append(schema.toString());

        sb.append('}');

        return sb;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) { return true; }
        if (o == null || getClass() != o.getClass()) { return false; }
        if (!super.equals(o)) { return false; }
         SoftwareServerReferences typedThat = (SoftwareServerReferences) o;
      // compare single cardinality attributes
         if (this.endpoints != null && !Objects.equals(this.endpoints,typedThat.endpoints)) {
                            return false;
                 }
         if (this.capabilities != null && !Objects.equals(this.capabilities,typedThat.capabilities)) {
                            return false;
                 }
         if (this.host != null && !Objects.equals(this.host,typedThat.host)) {
                            return false;
                 }
         if (this.supportedDataSets != null && !Objects.equals(this.supportedDataSets,typedThat.supportedDataSets)) {
                            return false;
                 }
         if (this.consumedBy != null && !Objects.equals(this.consumedBy,typedThat.consumedBy)) {
                            return false;
                 }
         if (this.schema != null && !Objects.equals(this.schema,typedThat.schema)) {
                            return false;
                 }
         if (this.connections != null && !Objects.equals(this.connections,typedThat.connections)) {
                            return false;
                 }
         if (this.meetings != null && !Objects.equals(this.meetings,typedThat.meetings)) {
                            return false;
                 }
         if (this.tags != null && !Objects.equals(this.tags,typedThat.tags)) {
                            return false;
                 }
         if (this.alsoKnownAs != null && !Objects.equals(this.alsoKnownAs,typedThat.alsoKnownAs)) {
                            return false;
                 }
         if (this.managedResources != null && !Objects.equals(this.managedResources,typedThat.managedResources)) {
                            return false;
                 }
         if (this.todos != null && !Objects.equals(this.todos,typedThat.todos)) {
                            return false;
                 }
         if (this.noteLogs != null && !Objects.equals(this.noteLogs,typedThat.noteLogs)) {
                            return false;
                 }
         if (this.zones != null && !Objects.equals(this.zones,typedThat.zones)) {
                            return false;
                 }
         if (this.licenses != null && !Objects.equals(this.licenses,typedThat.licenses)) {
                            return false;
                 }
         if (this.facets != null && !Objects.equals(this.facets,typedThat.facets)) {
                            return false;
                 }
         if (this.likes != null && !Objects.equals(this.likes,typedThat.likes)) {
                            return false;
                 }
         if (this.actions != null && !Objects.equals(this.actions,typedThat.actions)) {
                            return false;
                 }
         if (this.meaning != null && !Objects.equals(this.meaning,typedThat.meaning)) {
                            return false;
                 }
         if (this.comments != null && !Objects.equals(this.comments,typedThat.comments)) {
                            return false;
                 }
         if (this.starRatings != null && !Objects.equals(this.starRatings,typedThat.starRatings)) {
                            return false;
                 }
         if (this.foundInCollections != null && !Objects.equals(this.foundInCollections,typedThat.foundInCollections)) {
                            return false;
                 }
         if (this.externalReference != null && !Objects.equals(this.externalReference,typedThat.externalReference)) {
                            return false;
                 }
         if (this.relatedMedia != null && !Objects.equals(this.relatedMedia,typedThat.relatedMedia)) {
                            return false;
                 }
         if (this.certifications != null && !Objects.equals(this.certifications,typedThat.certifications)) {
                            return false;
                 }
         if (this.contributors != null && !Objects.equals(this.contributors,typedThat.contributors)) {
                            return false;
                 }
         if (this.staff != null && !Objects.equals(this.staff,typedThat.staff)) {
                            return false;
                 }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode()
    ,this.endpoints
    ,this.capabilities
    ,this.host
    ,this.supportedDataSets
    ,this.consumedBy
    ,this.schema
    ,this.connections
    ,this.meetings
    ,this.tags
    ,this.alsoKnownAs
    ,this.managedResources
    ,this.todos
    ,this.noteLogs
    ,this.zones
    ,this.licenses
    ,this.facets
    ,this.likes
    ,this.actions
    ,this.meaning
    ,this.comments
    ,this.starRatings
    ,this.foundInCollections
    ,this.externalReference
    ,this.relatedMedia
    ,this.certifications
    ,this.contributors
    ,this.staff
       );
    }

    @Override
    public String toString() {
        return toString(new StringBuilder()).toString();
    }
}
