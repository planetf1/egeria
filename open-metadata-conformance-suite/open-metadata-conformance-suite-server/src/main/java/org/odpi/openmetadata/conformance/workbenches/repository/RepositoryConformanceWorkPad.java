/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.conformance.workbenches.repository;

import org.odpi.openmetadata.adminservices.configuration.properties.RepositoryConformanceWorkbenchConfig;
import org.odpi.openmetadata.conformance.beans.*;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLog;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.AttributeTypeDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * RepositoryConformanceWorkPad provides the thread safe place to assemble results from the repository workbench.
 */
public class RepositoryConformanceWorkPad extends OpenMetadataConformanceWorkbenchWorkPad
{
    private static final String workbenchId            = "repository-workbench";
    private static final String workbenchName          = "Open Metadata Repository Test Workbench";
    private static final String workbenchVersionNumber = "V1.0 SNAPSHOT";
    private static final String workbenchDocURL        = "https://odpi.github.io/egeria/open-metadata-conformance-suite/docs/" + workbenchId;
    private static final String tutType                = "Open Metadata Repository";

    private OMRSAuditLog            auditLog;

    private String                  tutServerName               = null;
    private String                  tutMetadataCollectionId     = null;
    private String                  tutServerType               = null;
    private String                  tutOrganization             = null;

    private OMRSRepositoryConnector tutRepositoryConnector      = null;

    private String                  localMetadataCollectionId   = null;
    private OMRSRepositoryConnector localRepositoryConnector    = null;

    private Map<String, AttributeTypeDef>    supportedAttributeTypeDefsByGUIDFromRESTAPI = new HashMap<>();
    private Map<String, AttributeTypeDef>    supportedAttributeTypeDefsByGUIDFromEvents  = new HashMap<>();
    private Map<String, AttributeTypeDef>    supportedAttributeTypeDefsByName            = new HashMap<>();

    private Map<String, TypeDef>    supportedTypeDefsByGUIDFromRESTAPI = new HashMap<>();
    private Map<String, TypeDef>    supportedTypeDefsByGUIDFromEvents  = new HashMap<>();
    private Map<String, TypeDef>    supportedTypeDefsByName            = new HashMap<>();


    private Map<String, List<String>>              entitySubTypes          = new HashMap<>();
    private Map<String, List<String>>              relationshipEndTypes    = new HashMap<>();
    private Map<String, List<List<String>>>        entityRelationshipTypes = new HashMap<>();
    private Map<String, List<List<EntityDetail>>>  entityInstances         = new HashMap<>();
    private Map<String, List<List<Relationship>>>  relationshipInstances   = new HashMap<>();

    /**
     * Constructor receives key information from the configuration services.
     *
     * @param localServerUserId userId that this server should use on requests
     * @param localServerPassword password that this server should use on requests
     * @param maxPageSize maximum number of elements that can be returned on a single call
     * @param auditLog audit log for administrator messages
     * @param configuration configuration for this work pad/workbench
     */
    public RepositoryConformanceWorkPad(String                                localServerUserId,
                                        String                                localServerPassword,
                                        int                                   maxPageSize,
                                        OMRSAuditLog                          auditLog,
                                        RepositoryConformanceWorkbenchConfig  configuration)
    {
        super(workbenchId,
              workbenchName,
              workbenchVersionNumber,
              workbenchDocURL,
              localServerUserId,
              localServerPassword,
              tutType,
              maxPageSize);

        this.auditLog = auditLog;

        if (configuration != null)
        {
            this.tutServerName = configuration.getTutRepositoryServerName();
            super.tutName = this.tutServerName;
        }
    }

    /**
     * Return the audit log for this server.
     *
     * @return audit log object.
     */
    public OMRSAuditLog getAuditLog()
    {
        return auditLog;
    }


    /**
     * Return the name of the server being tested.
     *
     * @return server name
     */
    public String getTutServerName()
    {
        return tutServerName;
    }


    /**
     * Return the server type of the technology under test.  This is extracted from the registration
     * events.
     *
     * @return string type name
     */
    public synchronized String getTutServerType()
    {
        return tutServerType;
    }


    /**
     * Set up the server type of the technology under test.  This is extracted from the registration
     * events.
     *
     * @param tutServerType string type name
     */
    public synchronized void setTutServerType(String tutServerType)
    {
        this.tutServerType = tutServerType;
    }


    /**
     * Return the owning organization of the technology under test.  This is extracted from the registration
     * event.
     *
     * @return string organization name
     */
    public synchronized String getTutOrganization()
    {
        return tutOrganization;
    }


    /**
     * Set up the owning organization of the technology under test.  This is extracted from the registration
     * event.
     *
     * @param tutOrganization string organization name
     */
    public synchronized void setTutOrganization(String tutOrganization)
    {
        this.tutOrganization = tutOrganization;
    }


    /**
     * Return the metadata collection id of the technology under test (or null if it is not known).
     * This value is populated from the registration events.
     *
     * @return string id
     */
    public synchronized String getTutMetadataCollectionId()
    {
        return tutMetadataCollectionId;
    }


    /**
     * Set up the metadata collection id of the technology under test.
     * This value is populated from the registration events.
     *
     * @param tutMetadataCollectionId string id
     */
    public synchronized void setTutMetadataCollectionId(String tutMetadataCollectionId)
    {
        this.tutMetadataCollectionId = tutMetadataCollectionId;
    }


    /**
     * Set up the supported attributeTypeDefs for validation.  This list comes from the REST API.
     *
     * @param supportedAttributeTypeDefs list of attributeTypeDefs
     */
    public synchronized void setSupportedAttributeTypeDefsFromRESTAPI(List<AttributeTypeDef> supportedAttributeTypeDefs)
    {
        this.supportedAttributeTypeDefsByGUIDFromRESTAPI = new HashMap<>();

        if (supportedAttributeTypeDefs != null)
        {
            for (AttributeTypeDef   attributeTypeDef: supportedAttributeTypeDefs)
            {
                if (attributeTypeDef != null)
                {
                    this.supportedAttributeTypeDefsByGUIDFromRESTAPI.put(attributeTypeDef.getGUID(), attributeTypeDef);
                    this.supportedAttributeTypeDefsByName.put(attributeTypeDef.getName(), attributeTypeDef);
                }
            }
        }
    }


    /**
     * Add a TypeDef to the list of supported type definitions.  This value comes from the events.
     *
     * @param attributeTypeDef type definition object
     */
    public synchronized void addSupportedAttributeTypeDefFromEvent(AttributeTypeDef   attributeTypeDef)
    {
        if (attributeTypeDef != null)
        {
            this.supportedAttributeTypeDefsByGUIDFromEvents.put(attributeTypeDef.getGUID(), attributeTypeDef);
            this.supportedAttributeTypeDefsByName.put(attributeTypeDef.getName(), attributeTypeDef);
        }
    }


    /**
     * Return a type definition object by name (or null if not known)
     *
     * @param name name of type definition object
     * @return attributeTypeDef object
     */
    public synchronized AttributeTypeDef  getAttributeTypeDefByName(String   name)
    {
        return this.supportedAttributeTypeDefsByName.get(name);
    }


    /**
     * Return a specific type definition received from the REST API.
     *
     * @param guid unique identifier of the required type definition
     * @return type definition object or null if not known
     */
    public synchronized AttributeTypeDef  getAttributeTypeDefFromRESTAPI(String   guid)
    {
        return this.supportedAttributeTypeDefsByGUIDFromRESTAPI.get(guid);
    }


    /**
     * Return a specific type definition received in an OMRS event.
     *
     * @param guid unique identifier of the required type definition
     * @return type definition object or null if not known
     */
    public synchronized AttributeTypeDef  getAttributeTypeDefFromEvents(String   guid)
    {
        return this.supportedAttributeTypeDefsByGUIDFromEvents.get(guid);
    }



    /**
     * Set up the supported typeDefs for validation.  This list comes from the REST API.
     *
     * @param supportedTypeDefs list of typeDefs
     */
    public synchronized void setSupportedTypeDefsFromRESTAPI(List<TypeDef> supportedTypeDefs)
    {
        this.supportedTypeDefsByGUIDFromRESTAPI = new HashMap<>();

        if (supportedTypeDefs != null)
        {
            for (TypeDef   typeDef: supportedTypeDefs)
            {
                if (typeDef != null)
                {
                    this.supportedTypeDefsByGUIDFromRESTAPI.put(typeDef.getGUID(), typeDef);
                    this.supportedTypeDefsByName.put(typeDef.getName(), typeDef);
                }
            }
        }
    }


    /**
     * Add a TypeDef to the list of supported type definitions.  This value comes from the events.
     *
     * @param typeDef type definition object
     */
    public synchronized void addSupportedTypeDefFromEvent(TypeDef   typeDef)
    {
        if (typeDef != null)
        {
            this.supportedTypeDefsByGUIDFromEvents.put(typeDef.getGUID(), typeDef);
            this.supportedTypeDefsByName.put(typeDef.getName(), typeDef);
        }
    }


    /**
     * Return a type definition object by name (or null if not known)
     *
     * @param name name of type definition object
     * @return typeDef object
     */
    public synchronized TypeDef  getTypeDefByName(String   name)
    {
        return this.supportedTypeDefsByName.get(name);
    }


    /**
     * Return a specific type definition received from the REST API.
     *
     * @param guid unique identifier of the required type definition
     * @return type definition object or null if not known
     */
    public synchronized TypeDef  getTypeDefFromRESTAPI(String   guid)
    {
        return this.supportedTypeDefsByGUIDFromRESTAPI.get(guid);
    }


    /**
     * Return a specific type definition received in an OMRS event.
     *
     * @param guid unique identifier of the required type definition
     * @return type definition object or null if not known
     */
    public synchronized TypeDef  getTypeDefFromEvents(String   guid)
    {
        return this.supportedTypeDefsByGUIDFromEvents.get(guid);
    }


    /**
     * Return the repository connector for the technology under test.
     *
     * @return OMRSRepositoryConnector
     */
    public synchronized OMRSRepositoryConnector getTutRepositoryConnector()
    {
        return tutRepositoryConnector;
    }


    /**
     * Set up the repository connector for the technology under test.
     *
     * @param tutRepositoryConnector OMRSRepositoryConnector
     */
    public synchronized void setTutRepositoryConnector(OMRSRepositoryConnector tutRepositoryConnector)
    {
        this.tutRepositoryConnector = tutRepositoryConnector;
    }


    /**
     * Return the metadata collection id for the local repository.
     *
     * @return string id
     */
    public synchronized String getLocalMetadataCollectionId()
    {
        return localMetadataCollectionId;
    }


    /**
     * Set up the metadata collection id for the local repository.
     *
     * @param localMetadataCollectionId string id
     */
    public synchronized void setLocalMetadataCollectionId(String localMetadataCollectionId)
    {
        this.localMetadataCollectionId = localMetadataCollectionId;
    }


    /**
     * Return the connector to the local repository.
     *
     * @return OMRSRepositoryConnector
     */
    public synchronized OMRSRepositoryConnector getLocalRepositoryConnector()
    {
        return localRepositoryConnector;
    }


    /**
     * Set up the connector to the local repository.
     *
     * @param localRepositoryConnector access to the local repository (updated to generate events for the
     *                                 technology under test to respond to)
     */
    public synchronized void setLocalRepositoryConnector(OMRSRepositoryConnector localRepositoryConnector)
    {
        this.localRepositoryConnector = localRepositoryConnector;
    }


    /**
     * Accumulate the evidences for each profile
     *
     * @return the test evidence organized by profile and requirement withing profile
     */
    public synchronized List<OpenMetadataConformanceProfileResults> getProfileResults()
    {
        List<OpenMetadataConformanceProfileResults>  resultsList = new ArrayList<>();

        RepositoryConformanceProfile[]            profiles     = RepositoryConformanceProfile.values();
        RepositoryConformanceProfileRequirement[] requirements = RepositoryConformanceProfileRequirement.values();

        for (RepositoryConformanceProfile profile : profiles)
        {
            OpenMetadataConformanceProfileResults  profileResults = new OpenMetadataConformanceProfileResults();

            profileResults.setId(profile.getProfileId());
            profileResults.setName(profile.getProfileName());
            profileResults.setDocumentationURL(profile.getProfileDocumentationURL());
            profileResults.setDescription(profile.getProfileDescription());
            profileResults.setProfilePriority(profile.getProfilePriority());

            List<OpenMetadataConformanceTestEvidence> profileTestEvidence = new ArrayList<>();

            if (testEvidenceList != null)
            {
                for (OpenMetadataConformanceTestEvidence testEvidenceItem : testEvidenceList)
                {
                    if ((testEvidenceItem != null) && (testEvidenceItem.getProfileId().intValue() == profileResults.getId().intValue()))
                    {
                        profileTestEvidence.add(testEvidenceItem);
                    }
                }
            }

            if (profileTestEvidence.isEmpty())
            {
                profileResults.setConformanceStatus(OpenMetadataConformanceStatus.UNKNOWN_STATUS);
            }
            else
            {
                List<OpenMetadataConformanceTestEvidence>       positiveTestEvidence = new ArrayList<>();
                List<OpenMetadataConformanceTestEvidence>       negativeTestEvidence = new ArrayList<>();

                profileResults.setConformanceStatus(super.processEvidence(profileTestEvidence,
                                                                          positiveTestEvidence,
                                                                          negativeTestEvidence));

                List<OpenMetadataConformanceRequirementResults> requirementResultsList = new ArrayList<>();
                OpenMetadataConformanceRequirementResults       requirementResults;

                for (RepositoryConformanceProfileRequirement requirement : requirements) {

                    /*
                     * If (and only if) this requirement is relevant to the current profile, process it...
                     */
                    if (requirement.getProfileId().equals(profile.getProfileId())) {

                        requirementResults = new OpenMetadataConformanceRequirementResults();

                        requirementResults.setId(requirement.getRequirementId());
                        requirementResults.setName(requirement.getName());
                        requirementResults.setDescription(requirement.getDescription());
                        requirementResults.setDocumentationURL(requirement.getDocumentationURL());

                        List<OpenMetadataConformanceTestEvidence> requirementTestEvidence = new ArrayList<>();

                        for (OpenMetadataConformanceTestEvidence testEvidenceItem : profileTestEvidence) {
                            if (testEvidenceItem != null) {
                                if (testEvidenceItem.getRequirementId().intValue() == requirementResults.getId().intValue()) {
                                    requirementTestEvidence.add(testEvidenceItem);
                                }
                            }
                        }

                        positiveTestEvidence = new ArrayList<>();
                        negativeTestEvidence = new ArrayList<>();

                        requirementResults.setConformanceStatus(super.processEvidence(requirementTestEvidence,
                                positiveTestEvidence,
                                negativeTestEvidence));

                        if (!positiveTestEvidence.isEmpty()) {
                            requirementResults.setPositiveTestEvidence(positiveTestEvidence);
                        }

                        if (!negativeTestEvidence.isEmpty()) {
                            requirementResults.setNegativeTestEvidence(negativeTestEvidence);
                        }

                        requirementResultsList.add(requirementResults);
                    }
                }

                profileResults.setRequirementResults(requirementResultsList);
            }

            resultsList.add(profileResults);
        }


        if (resultsList.isEmpty())
        {
            return null;
        }
        else
        {
            return resultsList;
        }
    }


    /**
     * Add the specified subtype to the list for the named entity type
     * @param entityTypeName
     * @param subTypeName
     */
    public void addEntitySubType(String entityTypeName, String subTypeName) {

        List<String> subTypeList = this.entitySubTypes.get(entityTypeName);
        if (subTypeList == null) {
            List<String> newList = new ArrayList<>();
            newList.add(subTypeName);
            this.entitySubTypes.put(entityTypeName,newList);
        }
        else {
            subTypeList.add(subTypeName);
        }
    }

    /**
     * Return the list of subtypes of the named entity type
     * @param entityTypeName
     * @return
     */
    public List<String> getEntitySubTypes(String entityTypeName) {

        List<String> subTypeList = this.entitySubTypes.get(entityTypeName);
        return subTypeList;
    }

    /**
     * Add the specified relationship type to the appropriate end-specific relationship type list, for the entity type specified
     * @param entityTypeName
     * @param relationshipTypeName
     */
    public void addEntityRelationshipType(String entityTypeName, String relationshipTypeName, int end) {

        List<List<String>> bothEndLists = this.entityRelationshipTypes.get(entityTypeName);
        if (bothEndLists == null) {
            List<String> end1List = new ArrayList<>();
            List<String> end2List = new ArrayList<>();
            bothEndLists = new ArrayList<>();
            bothEndLists.add(end1List);
            bothEndLists.add(end2List);
            this.entityRelationshipTypes.put(entityTypeName,bothEndLists);
        }
        if (end == 1) {
            List<String> end1List = bothEndLists.get(0);
            end1List.add(relationshipTypeName);
        }
        else if (end == 2) {
            List<String> end1List = bothEndLists.get(1);
            end1List.add(relationshipTypeName);
        }
    }

    /**
     * Return the list of endtypes for the named relationship type
     * @param entityTypeName
     * @return
     */
    public List<List<String>> getEntityRelationshipTypes(String entityTypeName) {

        List<List<String>> relTypeLists = this.entityRelationshipTypes.get(entityTypeName);
        return relTypeLists;
    }

    /**
     * Return the set of supported entity type names
     * @return
     */
    public Set<String> getEntityTypeNames() {

        Set<String> keySet = this.entityRelationshipTypes.keySet();
        return keySet;
    }



    /**
     * Set the pair of end types as the list for the named relationship type
     * @param relationshipTypeName
     * @param end1TypeName
     * @param end2TypeName
     */
    public void addRelationshipEndTypes(String relationshipTypeName, String end1TypeName, String end2TypeName) {

        List<String> endTypeList= new ArrayList<>();
        endTypeList.add(end1TypeName);
        endTypeList.add(end2TypeName);
        this.relationshipEndTypes.put(relationshipTypeName, endTypeList);

    }

    /**
     * Return the list of endtypes for the named relationship type
     * @param relationshipTypeName
     * @return
     */
    public List<String> getRelationshipEndTypes(String relationshipTypeName) {

        List<String> endTypeList = this.relationshipEndTypes.get(relationshipTypeName);
        return endTypeList;
    }

    /**
     * Return the set of supported relationship type names
     * @return
     */
    public Set<String> getRelationshipTypeNames() {

        Set<String> keySet = this.relationshipEndTypes.keySet();
        return keySet;
    }




    /**
     * Remember the sets of instances for a given entity type. This is to support
     * @param entityTypeName
     * @param set_0
     * @param set_1
     * @param set_2
     */
    public void addEntityInstanceSets(String entityTypeName, List<EntityDetail> set_0, List<EntityDetail> set_1, List<EntityDetail> set_2) {

        List<List<EntityDetail>> setsList = new ArrayList<>();
        setsList.add(set_0);
        setsList.add(set_1);
        setsList.add(set_2);
        this.entityInstances.put(entityTypeName,setsList);
    }

    /**
     * Retrieve entity instances for the given type for the given instance set
     * @param entityTypeName
     */
    public List<EntityDetail> getEntityInstanceSet(String entityTypeName, int setId) {

        if (this.entityInstances.get(entityTypeName) != null) {
            return this.entityInstances.get(entityTypeName).get(setId);
        }
        else
            return null;
    }

    /**
     * Clean up entity instances for the given type.
     * @param entityTypeName
     */
    public void removeEntityInstanceSets(String entityTypeName) {

        this.entityInstances.remove(entityTypeName);

    }



    /**
     * Remember the sets of instances for a given relationship type. This is to support
     * @param relationshipTypeName
     * @param set_0
     * @param set_1
     * @param set_2
     */
    public void addRelationshipInstanceSets(String relationshipTypeName, List<Relationship> set_0, List<Relationship> set_1, List<Relationship> set_2) {

        List<List<Relationship>> setsList = new ArrayList<>();
        setsList.add(set_0);
        setsList.add(set_1);
        setsList.add(set_2);
        this.relationshipInstances.put(relationshipTypeName,setsList);
    }

    /**
     * Retrieve relationship instances for the given type for the given instance set
     * @param relationshipTypeName
     */
    public List<Relationship> getRelationshipInstanceSet(String relationshipTypeName, int setId) {

        if (this.relationshipInstances.get(relationshipTypeName) != null) {
            return this.relationshipInstances.get(relationshipTypeName).get(setId);
        }
        else
            return null;
    }

    /**
     * Clean up relationship instances for the given type.
     * @param relationshipTypeName
     */
    public void removeRelationshipInstanceSets(String relationshipTypeName) {

        this.relationshipInstances.remove(relationshipTypeName);

    }


    /**
     * toString() JSON-style
     *
     * @return string description
     */
    @Override
    public String toString()
    {
        return "RepositoryConformanceWorkPad{" +
                "workbenchId='" + workbenchId + '\'' +
                ", workbenchName='" + workbenchName + '\'' +
                ", workbenchVersionNumber='" + workbenchVersionNumber + '\'' +
                ", workbenchDocURL='" + workbenchDocURL + '\'' +
                ", localServerUserId='" + localServerUserId + '\'' +
                ", localServerPassword='" + localServerPassword + '\'' +
                ", tutName='" + tutName + '\'' +
                ", tutType='" + tutType + '\'' +
                ", maxPageSize=" + maxPageSize +
                '}';
    }
}
