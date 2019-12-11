/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.conformance.tests.repository.instances;

import org.odpi.openmetadata.conformance.tests.repository.RepositoryConformanceTestCase;
import org.odpi.openmetadata.conformance.workbenches.repository.RepositoryConformanceProfileRequirement;
import org.odpi.openmetadata.conformance.workbenches.repository.RepositoryConformanceWorkPad;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.OMRSMetadataCollection;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.MatchCriteria;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityProxy;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceStatus;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.EntityDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.RelationshipDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.FunctionNotSupportedException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.RelationshipNotKnownException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;


/**
 * Test that all defined entities can be created, retrieved, updated and deleted.
 */
public class TestSupportedRelationshipReferenceCopyLifecycle extends RepositoryConformanceTestCase
{
    private static final String testCaseId = "repository-relationship-reference-copy-lifecycle";
    private static final String testCaseName = "Repository relationship reference copy lifecycle test case";

    /* Type */
    private static final String assertion0      = testCaseId + "-00";
    private static final String assertionMsg0   = " relationship type definition matches known type  ";

    private static final String assertion1      = testCaseId + "-01";
    private static final String assertionMsg1   = " reference relationship created; repository supports storage of reference copies.";
    private static final String assertion2      = testCaseId + "-02";
    private static final String assertionMsg2   = " reference relationship can be retrieved as Relationship.";
    private static final String assertion3      = testCaseId + "-03";
    private static final String assertionMsg3   = " reference relationship matches the relationship that was saved.";
    private static final String assertion4      = testCaseId + "-04";
    private static final String assertionMsg4   = " reference relationship status cannot be updated.";
    private static final String assertion5      = testCaseId + "-05";
    private static final String assertionMsg5   = " reference relationship properties cannot be updated.";
    private static final String assertion6      = testCaseId + "-06";
    private static final String assertionMsg6   = " reference relationship type cannot be changed.";
    private static final String assertion7      = testCaseId + "-07";
    private static final String assertionMsg7   = " reference relationship identity cannot be changed.";
    private static final String assertion8      = testCaseId + "-08";
    private static final String assertionMsg8   = " reference relationship copy purged at TUT.";
    private static final String assertion9      = testCaseId + "-09";
    private static final String assertionMsg9   = " reference relationship refresh requested by TUT.";
    private static final String assertion10     = testCaseId + "-10";
    private static final String assertionMsg10  = " reference relationship refreshed.";
    private static final String assertion11     = testCaseId + "-11";
    private static final String assertionMsg11  = " refreshed reference relationship matches original.";
    private static final String assertion12     = testCaseId + "-12";
    private static final String assertionMsg12  = " reference relationship purged.";

    private static final String assertion13     = testCaseId + "-13";
    private static final String assertionMsg13  = " repository supports types for relationship and ends.";
    private static final String assertion14     = testCaseId + "-14";
    private static final String assertionMsg14  = " master relationship created.";
    private static final String assertion15     = testCaseId + "-15";
    private static final String assertionMsg15  = " reference relationship created with mappingProperties.";
    private static final String assertion16     = testCaseId + "-16";
    private static final String assertionMsg16  = " reference relationship retrieved with mappingProperties.";

    private static final String assertion17     = testCaseId + "-17";
    private static final String assertionMsg17  = " reference relationship re-homed.";


    private RepositoryConformanceWorkPad  workPad;
    private String                        metadataCollectionId;
    private RelationshipDef               relationshipDef;
    private Map<String, EntityDef>        entityDefs;
    private String                        testTypeName;

    /*
     * A propagation timeout is used to limit how long the testcase will wait for
     * the propagation of an OMRS instance event and consequent processing at the TUT (or CTS).
     * Each time the testcase waits it does so in a 100ms polling loop, to minimise overall delay.
     * The wait loops will wait for pollCount iterations of pollPeriod, so a pollCount of x10
     * results in a 1000ms (1s) timeout.
     *
     */
    private Integer           pollCount   = 50;
    private Integer           pollPeriod  = 100;   // milliseconds



    /**
     * Typical constructor sets up superclass and discovered information needed for tests
     *
     * @param workPad place for parameters and results
     * @param relationshipDef type of valid relationships
     */
    public TestSupportedRelationshipReferenceCopyLifecycle(RepositoryConformanceWorkPad workPad,
                                                           Map<String, EntityDef>       entityDefs,
                                                           RelationshipDef              relationshipDef)
    {
        super(workPad,
              RepositoryConformanceProfileRequirement.REFERENCE_COPY_STORAGE.getProfileId(),
              RepositoryConformanceProfileRequirement.REFERENCE_COPY_STORAGE.getRequirementId());

        this.workPad              = workPad;
        this.metadataCollectionId = workPad.getTutMetadataCollectionId();
        this.relationshipDef      = relationshipDef;
        this.entityDefs           = entityDefs;

        this.testTypeName = this.updateTestIdByType(relationshipDef.getName(),
                                                    testCaseId,
                                                    testCaseName);

        /*
         * Enforce minimum pollPeriod and pollCount.
         */
        this.pollPeriod = Math.max(this.pollPeriod, 100);
        this.pollCount  = Math.max(this.pollCount, 1);
    }


    /**
     * Method implemented by the actual test case.
     *
     * @throws Exception something went wrong with the test.
     */
    protected void run() throws Exception
    {


        OMRSMetadataCollection metadataCollection = super.getMetadataCollection();

        /*
         * Check that the relationship type matches the known type from the repository helper
         */
        OMRSRepositoryConnector cohortRepositoryConnector = null;
        OMRSRepositoryHelper repositoryHelper = null;
        if (workPad != null) {
            cohortRepositoryConnector = workPad.getTutRepositoryConnector();
            repositoryHelper = cohortRepositoryConnector.getRepositoryHelper();
        }

        RelationshipDef knownRelationshipDef = (RelationshipDef) repositoryHelper.getTypeDefByName(workPad.getLocalServerUserId(), relationshipDef.getName());
        verifyCondition((relationshipDef.equals(knownRelationshipDef)),
                assertion0,
                testTypeName + assertionMsg0,
                RepositoryConformanceProfileRequirement.CONSISTENT_TYPES.getProfileId(),
                RepositoryConformanceProfileRequirement.CONSISTENT_TYPES.getRequirementId());

        String endOneEntityDefGUID = relationshipDef.getEndDef1().getEntityType().getGUID();
        String endTwoEntityDefGUID = relationshipDef.getEndDef2().getEntityType().getGUID();
        EntityDef endOneEntityDef = (EntityDef) metadataCollection.getTypeDefByGUID(workPad.getLocalServerUserId(), endOneEntityDefGUID);
        EntityDef endTwoEntityDef = (EntityDef) metadataCollection.getTypeDefByGUID(workPad.getLocalServerUserId(), endTwoEntityDefGUID);


        EntityDef knownEnd1EntityDef = (EntityDef) repositoryHelper.getTypeDefByName(workPad.getLocalServerUserId(), endOneEntityDef.getName());
        verifyCondition((endOneEntityDef.equals(knownEnd1EntityDef)),
                assertion0,
                testTypeName + assertionMsg0,
                RepositoryConformanceProfileRequirement.CONSISTENT_TYPES.getProfileId(),
                RepositoryConformanceProfileRequirement.CONSISTENT_TYPES.getRequirementId());


        EntityDef knownEnd2EntityDef = (EntityDef) repositoryHelper.getTypeDefByName(workPad.getLocalServerUserId(), endTwoEntityDef.getName());
        verifyCondition((endTwoEntityDef.equals(knownEnd2EntityDef)),
                assertion0,
                testTypeName + assertionMsg0,
                RepositoryConformanceProfileRequirement.CONSISTENT_TYPES.getProfileId(),
                RepositoryConformanceProfileRequirement.CONSISTENT_TYPES.getRequirementId());



        /*
         * This test will:
         *
         * Create a pair of entities of the types defined by the relationship type, in the local (CTS) repository.
         * This will cause instance events to be flowed to the TUT, which should then (by default) save reference copies of the entities.
         *
         * Create a local relationship of the defined type, in the local (CTS) repository.
         * This will cause an instance event to be flowed to the TUT, which should then (by default) save a reference copy of the relationship.
         *
         * Attempt to retrieve the relationship reference copy from the TUT.
         *
         * If this results in relationship not known, assert that the TUT does not support reference copies, so set the discovered property to disabled, and abandon the remainder of the test.
         *
         * Else, if the reference copy is known - add to discovered properties and continue with the remaining test requirements.
         *
         * The following tests are run against the reference copy:
         *
         * Validate that the reference copy can be retrieved by getRelationship
         * Validate that the reference copy 'matches' the local relationship.
         *
         * Verify that it is not possible to update the status of the reference copy.
         * Verify that it is not possible to update the properties of the reference copy.
         * Verify that it is not possible to re-type the reference copy.
         * Verify that it is not possible to re-identify the reference copy.
         *
         * Purge the reference copy (only) and then request a refresh and ensure that a new ref copy is created.
         *
         * Delete and purge the original local relationship, at the CTS.
         * Because the CTS server is using local in-memory repository a soft delete must precede the purge.
         * This should flow an instance event to the TUT causing the ref copy to be purged.
         * Attempt to get the ref copy. This should fail.
         *
         * THE NEXT TEST IS PERFORMED LAST AS IT PLACES THE COHORT IN AN INVALID STATE.
         * Create another original relationship, causing the creation of a reference copy in the TUT.
         * Verify that it IS possible to re-home the reference copy.
         * Delete and purge the original relationship and REMOTELY delete and purge the TUT's copy of the relationship (which is no longer a reference copy).
         * Note that this last part must be performed on the TUT.
         *
         * Finally, clean up the entities and relationships created during this test.
         *
         */


        /*
         * This test needs a connector to the local repository - i.e. the in-memory repository running locally to the CTS server.
         */
        OMRSMetadataCollection ctsMetadataCollection = repositoryConformanceWorkPad.getLocalRepositoryConnector().getMetadataCollection();


        EntityDetail entityOne;
        EntityDetail entityTwo;
        Relationship newRelationship;


        /*
         * Create the local entities.
         */


        entityOne = ctsMetadataCollection.addEntity(workPad.getLocalServerUserId(),
                endOneEntityDef.getGUID(),
                super.getAllPropertiesForInstance(workPad.getLocalServerUserId(), endOneEntityDef),
                null,
                null);

        entityTwo = ctsMetadataCollection.addEntity(workPad.getLocalServerUserId(),
                endTwoEntityDef.getGUID(),
                super.getAllPropertiesForInstance(workPad.getLocalServerUserId(), endTwoEntityDef),
                null,
                null);




        /*
         * Create the local relationship.
         *
         * Generate property values for all the type's defined properties, including inherited properties
         * This ensures that any properties defined as mandatory by Egeria property cardinality are provided
         * thereby getting into the connector-logic beyond the property validation. It also creates a
         * relationship that is logically complete - versus an instance with just the locally-defined properties.
         */

        newRelationship = ctsMetadataCollection.addRelationship(workPad.getLocalServerUserId(),
                relationshipDef.getGUID(),
                super.getAllPropertiesForInstance(workPad.getLocalServerUserId(), relationshipDef),
                entityOne.getGUID(),
                entityTwo.getGUID(),
                null);





        /*
         * This test does not verify the content of the relationship - that is tested in the relationship-lifecycle tests
         */


        /*
         * Try to get the ref copy of each of the entities from the TUT - this is to help synchronise the testcase to the speed of the cohort.
         */

        EntityDetail refCopyEntityOne = null;
        EntityDetail refCopyEntityTwo = null;

        Integer remainingCount = this.pollCount;
        while (refCopyEntityOne == null && remainingCount > 0) {
            refCopyEntityOne = metadataCollection.isEntityKnown(workPad.getLocalServerUserId(), entityOne.getGUID());
            Thread.sleep(this.pollPeriod);
            remainingCount--;
        }

        remainingCount = this.pollCount;
        while (refCopyEntityTwo == null && remainingCount > 0) {

            refCopyEntityTwo = metadataCollection.isEntityKnown(workPad.getLocalServerUserId(), entityTwo.getGUID());
            Thread.sleep(this.pollPeriod);
            remainingCount--;
        }


        /*
         * There should be a reference copy of the entity stored in the TUT.
         * Retrieve the ref copy from the TUT - if it does not exist, assert that ref copies are not a discovered property
         */

        Relationship refRelationship = null;

        remainingCount = this.pollCount;
        while (refRelationship == null && remainingCount > 0) {
            refRelationship = metadataCollection.isRelationshipKnown(workPad.getLocalServerUserId(), newRelationship.getGUID());
            Thread.sleep(this.pollPeriod);
            remainingCount--;
        }



        /*
         * If this proves to be a performance problem it might be preferable to refactor the testcase to create all local
         * instances and batch the GUIDs. On completion of the batch, look for the reference copies.
         */

        if (refRelationship != null) {
            /*
             * If we retrieved the reference copy of the relationship - we can assert that the TUT supports reference copies.
             */
            assertCondition((true),
                    assertion1,
                    testTypeName + assertionMsg1,
                    RepositoryConformanceProfileRequirement.REFERENCE_COPY_STORAGE.getProfileId(),
                    RepositoryConformanceProfileRequirement.REFERENCE_COPY_STORAGE.getRequirementId());

        }
        else {

            /*
             * Report that reference storage requirement is not supported.
             */
            super.addNotSupportedAssertion(assertion1,
                    assertionMsg1,
                    RepositoryConformanceProfileRequirement.REFERENCE_COPY_STORAGE.getProfileId(),
                    RepositoryConformanceProfileRequirement.REFERENCE_COPY_STORAGE.getRequirementId());


            /*
             * Terminate the test
             */
            return;

        }



        /*
         * Validate that the reference copy can be retrieved from the TUT and that the retrieved reference copy 'matches' what was saved.
         */


        Relationship retrievedReferenceCopy = metadataCollection.getRelationship(workPad.getLocalServerUserId(), newRelationship.getGUID());

        assertCondition((retrievedReferenceCopy != null),
                assertion2,
                testTypeName + assertionMsg2,
                RepositoryConformanceProfileRequirement.REFERENCE_COPY_STORAGE.getProfileId(),
                RepositoryConformanceProfileRequirement.REFERENCE_COPY_STORAGE.getRequirementId());


        /*
         * Verify that the retrieved reference copy matches the original relationship
         */

        verifyCondition((newRelationship.equals(retrievedReferenceCopy)),
                assertion3,
                testTypeName + assertionMsg3,
                RepositoryConformanceProfileRequirement.REFERENCE_COPY_STORAGE.getProfileId(),
                RepositoryConformanceProfileRequirement.REFERENCE_COPY_STORAGE.getRequirementId());



        /*
         * If the relationship def has any valid status values (including DELETED), attempt
         * to modify the status of the retrieved reference copy - this should fail
         */

        for (InstanceStatus validInstanceStatus : relationshipDef.getValidInstanceStatusList()) {

            try {

                Relationship updatedRelationship = metadataCollection.updateRelationshipStatus(workPad.getLocalServerUserId(), retrievedReferenceCopy.getGUID(), validInstanceStatus);

                assertCondition((false),
                        assertion4,
                        testTypeName + assertionMsg4,
                        RepositoryConformanceProfileRequirement.REFERENCE_COPY_LOCKING.getProfileId(),
                        RepositoryConformanceProfileRequirement.REFERENCE_COPY_LOCKING.getRequirementId());

            }
            catch (InvalidParameterException e) {

                /*
                 * We are not expecting the status update to work - it should have thrown an InvalidParameterException
                 */

                assertCondition((true),
                        assertion4,
                        testTypeName + assertionMsg4,
                        RepositoryConformanceProfileRequirement.REFERENCE_COPY_LOCKING.getProfileId(),
                        RepositoryConformanceProfileRequirement.REFERENCE_COPY_LOCKING.getRequirementId());
            }

        }



        /*
         * Attempt to modify one or more property of the retrieved reference copy. This is illegal so it should fail.
         */


        if ((retrievedReferenceCopy.getProperties() != null) &&
                (retrievedReferenceCopy.getProperties().getInstanceProperties() != null) &&
                (!retrievedReferenceCopy.getProperties().getInstanceProperties().isEmpty())) {
            InstanceProperties minRelationshipProps = super.getMinPropertiesForInstance(workPad.getLocalServerUserId(), relationshipDef);

            try {

                Relationship minPropertiesRelationship = metadataCollection.updateRelationshipProperties(workPad.getLocalServerUserId(), retrievedReferenceCopy.getGUID(), minRelationshipProps);

                assertCondition((false),
                        assertion5,
                        testTypeName + assertionMsg5,
                        RepositoryConformanceProfileRequirement.REFERENCE_COPY_LOCKING.getProfileId(),
                        RepositoryConformanceProfileRequirement.REFERENCE_COPY_LOCKING.getRequirementId());

            }
            catch (InvalidParameterException e) {

                /*
                 * We are not expecting the status update to work - it should have thrown an InvalidParameterException
                 */

                assertCondition((true),
                        assertion5,
                        testTypeName + assertionMsg5,
                        RepositoryConformanceProfileRequirement.REFERENCE_COPY_LOCKING.getProfileId(),
                        RepositoryConformanceProfileRequirement.REFERENCE_COPY_LOCKING.getRequirementId());
            }
        }



        /*
         * Verify that it is not possible to re-type the reference copy.
         * This test is performed using the same type as the original - the repository should not get as far as
         * even looking at the type or considering changing it. For simplicity of testcode this test therefore
         * uses the original type.
         * This test is performed against the TUT.
         */

        try {

            Relationship reTypedRelationship = metadataCollection.reTypeRelationship(workPad.getLocalServerUserId(),
                    newRelationship.getGUID(),
                    relationshipDef,
                    relationshipDef); // see comment above about using original type

            assertCondition((false),
                    assertion6,
                    testTypeName + assertionMsg6,
                    RepositoryConformanceProfileRequirement.REFERENCE_COPY_LOCKING.getProfileId(),
                    RepositoryConformanceProfileRequirement.REFERENCE_COPY_LOCKING.getRequirementId());

        } catch (InvalidParameterException e) {

            /*
             * We are not expecting the type update to work - it should have thrown an InvalidParameterException
             */

            assertCondition((true),
                    assertion6,
                    testTypeName + assertionMsg6,
                    RepositoryConformanceProfileRequirement.REFERENCE_COPY_LOCKING.getProfileId(),
                    RepositoryConformanceProfileRequirement.REFERENCE_COPY_LOCKING.getRequirementId());
        }



        /*
         * Verify that it is not possible to re-identify the reference copy.
         * This test is performed using a different GUID to the original. The actual value should not be looked at
         * by the repository - it should reject the re-identify attempt prior to that.
         * This test is performed against the TUT.
         */

        try {

            Relationship reIdentifiedRelationship = metadataCollection.reIdentifyRelationship(workPad.getLocalServerUserId(),
                    relationshipDef.getGUID(),
                    relationshipDef.getName(),
                    newRelationship.getGUID(),
                    UUID.randomUUID().toString());


            assertCondition((false),
                    assertion7,
                    testTypeName + assertionMsg7,
                    RepositoryConformanceProfileRequirement.REFERENCE_COPY_LOCKING.getProfileId(),
                    RepositoryConformanceProfileRequirement.REFERENCE_COPY_LOCKING.getRequirementId());

        } catch (InvalidParameterException e) {

            /*
             * We are not expecting the identity update to work - it should have thrown an InvalidParameterException
             */

            assertCondition((true),
                    assertion7,
                    testTypeName + assertionMsg7,
                    RepositoryConformanceProfileRequirement.REFERENCE_COPY_LOCKING.getProfileId(),
                    RepositoryConformanceProfileRequirement.REFERENCE_COPY_LOCKING.getRequirementId());
        }


        /*
         * Purge the reference copy and verify that by requesting a refresh, a new ref copy is created.
         * This test is performed against the TUT.
         */

        try {

            metadataCollection.purgeRelationshipReferenceCopy(workPad.getLocalServerUserId(), refRelationship);

            /*
             * Note that the ref copy could be purged
             */
            assertCondition((true),
                    assertion8,
                    testTypeName + assertionMsg8,
                    RepositoryConformanceProfileRequirement.REFERENCE_COPY_DELETE.getProfileId(),
                    RepositoryConformanceProfileRequirement.REFERENCE_COPY_DELETE.getRequirementId());

        }
        catch (Exception e)
        {

            /*
             * If for any reason the ref copy could not be purged, fail the test
             */

            assertCondition((false),
                    assertion8,
                    testTypeName + assertionMsg8,
                    RepositoryConformanceProfileRequirement.REFERENCE_COPY_DELETE.getProfileId(),
                    RepositoryConformanceProfileRequirement.REFERENCE_COPY_DELETE.getRequirementId());
        }

        try {
            metadataCollection.refreshEntityReferenceCopy(workPad.getLocalServerUserId(),
                    refRelationship.getGUID(),
                    relationshipDef.getGUID(),
                    relationshipDef.getName(),
                    ctsMetadataCollection.getMetadataCollectionId(workPad.getLocalServerUserId()));

            /*
             * Note that the refresh request failed, fail the test
             */
            assertCondition((true),
                    assertion9,
                    testTypeName + assertionMsg9,
                    RepositoryConformanceProfileRequirement.REFERENCE_COPY_STORAGE.getProfileId(),
                    RepositoryConformanceProfileRequirement.REFERENCE_COPY_STORAGE.getRequirementId());
        }
        catch (Exception e)
        {

            /*
             * If for any reason the refresh request failed, fail the test
             */

            assertCondition((false),
                    assertion9,
                    testTypeName + assertionMsg9,
                    RepositoryConformanceProfileRequirement.REFERENCE_COPY_STORAGE.getProfileId(),
                    RepositoryConformanceProfileRequirement.REFERENCE_COPY_STORAGE.getRequirementId());
        }

        /*
         * Wait and verify that a new ref copy is created....
         */
        /*
         * There should be a reference copy of the relationship stored in the TUT
         */

        Relationship refreshedRelationshipRefCopy = null;


        /*
         * Retrieve the ref copy from the TUT - if it does not exist, assert that ref copies are not a discovered property
         * Have to be prepared to wait until event has propagated and TUT has created a reference copy of the relationship.
         */
        remainingCount = this.pollCount;
        while (refreshedRelationshipRefCopy == null && remainingCount>0 ) {

            refreshedRelationshipRefCopy = metadataCollection.isRelationshipKnown(workPad.getLocalServerUserId(), newRelationship.getGUID());
            Thread.sleep(this.pollPeriod);
            remainingCount--;
        }


        /*
         * Verify that the reference copy can be retrieved form the TUT and matches the original...
         */
        refreshedRelationshipRefCopy = metadataCollection.getRelationship(workPad.getLocalServerUserId(), newRelationship.getGUID());

        assertCondition((refreshedRelationshipRefCopy != null),
                assertion10,
                testTypeName + assertionMsg10,
                RepositoryConformanceProfileRequirement.REFERENCE_COPY_STORAGE.getProfileId(),
                RepositoryConformanceProfileRequirement.REFERENCE_COPY_STORAGE.getRequirementId());


        /*
         * Verify that the retrieved reference copy matches the original relationship
         */

        verifyCondition((newRelationship.equals(refreshedRelationshipRefCopy)),
                assertion11,
                testTypeName + assertionMsg11,
                RepositoryConformanceProfileRequirement.REFERENCE_COPY_STORAGE.getProfileId(),
                RepositoryConformanceProfileRequirement.REFERENCE_COPY_STORAGE.getRequirementId());



        /*
         * Delete (soft then hard) the CTS local entity - these operations are performed on the local (CTS) repo.
         * They should cause an OMRS instance event to flow to the TUT and for the ref copy to be purged
         */

        try {
            Relationship deletedRelationship = ctsMetadataCollection.deleteRelationship(workPad.getLocalServerUserId(),
                    newRelationship.getType().getTypeDefGUID(),
                    newRelationship.getType().getTypeDefName(),
                    newRelationship.getGUID());
        } catch (FunctionNotSupportedException exception) {

            /*
             * This is OK - we can NO OP and just proceed to purgeEntity
             */
        }

        ctsMetadataCollection.purgeRelationship(workPad.getLocalServerUserId(),
                newRelationship.getType().getTypeDefGUID(),
                newRelationship.getType().getTypeDefName(),
                newRelationship.getGUID());



        /*
         * Test that the reference copy has been removed from the TUT repository
         */

        /*
         * Since it may take time to propagate the purge event, retry until the relationship is no longer known at the TUT.
         */

        Relationship survivingRelRefCopy;
        remainingCount = this.pollCount;
        do {
            survivingRelRefCopy = metadataCollection.isRelationshipKnown(workPad.getLocalServerUserId(), newRelationship.getGUID());
            Thread.sleep(this.pollPeriod);
            remainingCount--;
        } while (survivingRelRefCopy != null && remainingCount > 0);


        try {
            metadataCollection.getRelationship(workPad.getLocalServerUserId(), newRelationship.getGUID());

            assertCondition((false),
                    assertion12,
                    testTypeName + assertionMsg12,
                    RepositoryConformanceProfileRequirement.REFERENCE_COPY_DELETE.getProfileId(),
                    RepositoryConformanceProfileRequirement.REFERENCE_COPY_DELETE.getRequirementId());

        } catch (RelationshipNotKnownException exception) {

            assertCondition((true),
                    assertion12,
                    testTypeName + assertionMsg12,
                    RepositoryConformanceProfileRequirement.REFERENCE_COPY_DELETE.getProfileId(),
                    RepositoryConformanceProfileRequirement.REFERENCE_COPY_DELETE.getRequirementId());
        }






        /*
         * ======================================================================================================
         * The remaining tests in this test case use a different approach to creating the relationship to be saved.
         * Instead of creating a master instance at the CTS server and relying on OMRS event propagation to
         * trigger the save of the reference copy at the TUT, the copy is fabricated in the test code and is
         * saved directly using the saveRelationshipReferenceCopy API.
         */


        /*
         * For the next test, the local save approach is used because the test code needs access to mappingProperties.
         */


        /*
         * In this testcase the repository is believed to support the relationship type defined by
         * relationshipDef - but may not support all of the entity inheritance hierarchy - it may only
         * support a subset of entity types. So although the relationship type may have end definitions
         * each specifying a given entity type - the repository may only support certain sub-types of the
         * specified type. This is OK, and the testcase needs to only try to use entity types that are
         * supported by the repository being tested. To do this it needs to start with the specified
         * end type, e.g. Referenceable, and walk down the hierarchy looking for each subtype that
         * is supported by the repository (i.e. is in the entityDefs map). The test is run for
         * each combination of end1Type and end2Type - but only for types that are within the
         * active set for this repository.
         */

        String end1DefName = relationshipDef.getEndDef1().getEntityType().getName();
        List<String> end1DefTypeNames = new ArrayList<>();
        end1DefTypeNames.add(end1DefName);
        if (this.workPad.getEntitySubTypes(end1DefName) != null) {
            end1DefTypeNames.addAll(this.workPad.getEntitySubTypes(end1DefName));
        }


        String end2DefName = relationshipDef.getEndDef2().getEntityType().getName();
        List<String> end2DefTypeNames = new ArrayList<>();
        end2DefTypeNames.add(end2DefName);
        if (this.workPad.getEntitySubTypes(end2DefName) != null) {
            end2DefTypeNames.addAll(this.workPad.getEntitySubTypes(end2DefName));
        }

        /*
         * Filter the possible types to only include types that are supported by the repository
         */

        List<String> end1SupportedTypeNames = new ArrayList<>();
        for (String end1TypeName : end1DefTypeNames) {
            if (entityDefs.get(end1TypeName) != null)
                end1SupportedTypeNames.add(end1TypeName);
        }

        List<String> end2SupportedTypeNames = new ArrayList<>();
        for (String end2TypeName : end2DefTypeNames) {
            if (entityDefs.get(end2TypeName) != null)
                end2SupportedTypeNames.add(end2TypeName);
        }

        /*
         * Check that neither list is empty
         */
        if (end1SupportedTypeNames.isEmpty() || end2SupportedTypeNames.isEmpty()) {

            /*
             * There are no supported types for at least one of the ends - the repository cannot test this relationship type.
             */
            assertCondition((false),
                    assertion13,
                    testTypeName + assertionMsg13,
                    RepositoryConformanceProfileRequirement.RELATIONSHIP_LIFECYCLE.getProfileId(),
                    RepositoryConformanceProfileRequirement.RELATIONSHIP_LIFECYCLE.getRequirementId());
        }

        /*
         * It is not practical to iterate over all combinations of feasible (supported) end types as it takes too long to run.
         * For now, this test verifies relationship operation over a limited set of end types. The limitation is extreme in
         * that it ONLY takes the first available type for each end. This is undesirable for two reasons - one is that it
         * provides less test coverage; the other is that the types chosen depend on the order in the lists and this could
         * vary, making results non-repeatable. For now though, it seems these limitations are necessary.
         *
         * A full permutation across end types would use the following nested loops...
         *  for (String end1TypeName : end1SupportedTypeNames) {
         *     for (String end2TypeName : end2SupportedTypeNames) {
         *          test logic as below...
         *      }
         *  }
         */


        String end1TypeName = end1SupportedTypeNames.get(0);
        String end2TypeName = end2SupportedTypeNames.get(0);

        /*
         * To accommodate repositories that do not support the creation of instances, wrap the creation of the relationship
         * in a try..catch to check for FunctionNotSupportedException. If the connector throws this, then give up
         * on the test by setting the discovered property to disabled and returning.
         */


        EntityDetail end1;
        EntityDetail end2;
        Relationship relationshipWithMappingProperties;

        try {
            /*
             * Create a relationship reference copy of the relationship type.
             * To do this, a local relationship is created, copied and deleted/purged. The copy is modified (so that it
             * appears to come from an unknown/defunct remote metadata collection). It is then saved as a reference copy
             */


            EntityDef end1Type = entityDefs.get(end1TypeName);
            end1 = this.addEntityToRepository(workPad.getLocalServerUserId(), metadataCollection, end1Type);
            EntityDef end2Type = entityDefs.get(end2TypeName);
            end2 = this.addEntityToRepository(workPad.getLocalServerUserId(), metadataCollection, end2Type);


            relationshipWithMappingProperties = metadataCollection.addRelationship(workPad.getLocalServerUserId(),
                    relationshipDef.getGUID(),
                    super.getPropertiesForInstance(relationshipDef.getPropertiesDefinition()),
                    end1.getGUID(),
                    end2.getGUID(),
                    null);

            assertCondition((true),
                    assertion14,
                    testTypeName + assertionMsg14,
                    RepositoryConformanceProfileRequirement.RELATIONSHIP_LIFECYCLE.getProfileId(),
                    RepositoryConformanceProfileRequirement.RELATIONSHIP_LIFECYCLE.getRequirementId());

        }
        catch (FunctionNotSupportedException exception) {

            /*
             * If running against a read-only repository/connector that cannot add
             * entities or relationships catch FunctionNotSupportedException and give up the test.
             *
             * Report the inability to create instances and give up on the testcase....
             */

            super.addNotSupportedAssertion(assertion14,
                    assertionMsg14,
                    RepositoryConformanceProfileRequirement.RELATIONSHIP_LIFECYCLE.getProfileId(),
                    RepositoryConformanceProfileRequirement.RELATIONSHIP_LIFECYCLE.getRequirementId());

            return;
        }


        /*
         * This test does not verify the content of the relationship - that is tested in the relationship-lifecycle tests
         */


        /*
         * Make a copy of the relationship under a different variable name - not strictly necessary but makes things clearer - then modify it so
         * it appears to be from a remote metadata collection.
         */

        Relationship remoteRelationshipWithMappingProperties = relationshipWithMappingProperties;

        /*
         * Hard delete the new entity - we have no further use for it
         * If the repository under test supports soft delete, the entity must be deleted before being purged
         */

        try {
            metadataCollection.deleteRelationship(workPad.getLocalServerUserId(),
                    relationshipWithMappingProperties.getType().getTypeDefGUID(),
                    relationshipWithMappingProperties.getType().getTypeDefName(),
                    relationshipWithMappingProperties.getGUID());
        } catch (FunctionNotSupportedException exception) {

            /*
             * This is OK - we can NO OP and just proceed to purgeEntity
             */
        }

        metadataCollection.purgeRelationship(workPad.getLocalServerUserId(),
                relationshipWithMappingProperties.getType().getTypeDefGUID(),
                relationshipWithMappingProperties.getType().getTypeDefName(),
                relationshipWithMappingProperties.getGUID());

        // Beyond this point, there should be no further references to relationshipWithMappingProperties
        relationshipWithMappingProperties = null;


        /*
         * Modify the 'remote' entity so that it looks like it came from a different home repository
         */
        String REMOTE_PREFIX = "remote";

        String localRelationshipGUID = remoteRelationshipWithMappingProperties.getGUID();
        String remoteRelationshipGUID = REMOTE_PREFIX + localRelationshipGUID.substring(REMOTE_PREFIX.length());
        remoteRelationshipWithMappingProperties.setGUID(remoteRelationshipGUID);

        String localMetadataCollectionName = remoteRelationshipWithMappingProperties.getMetadataCollectionName();
        String remoteMetadataCollectionName = REMOTE_PREFIX + "-metadataCollection-not-" + localMetadataCollectionName;
        remoteRelationshipWithMappingProperties.setMetadataCollectionName(remoteMetadataCollectionName);

        String localMetadataCollectionId = remoteRelationshipWithMappingProperties.getMetadataCollectionId();
        String remoteMetadataCollectionId = REMOTE_PREFIX + localMetadataCollectionId.substring(REMOTE_PREFIX.length());
        remoteRelationshipWithMappingProperties.setMetadataCollectionId(remoteMetadataCollectionId);

        /*
         * Set mapping properties on the synthetic remote entity...
         */

        Map<String, Serializable> mappingProperties = new HashMap<>();
        mappingProperties.put("stringMappingPropertyKey", "stringMappingPropertyValue");
        mappingProperties.put("integerMappingPropertyKey", new Integer(12));

        /*
         * Save a reference copy of the 'remote' entity
         */

        try {

            metadataCollection.saveRelationshipReferenceCopy(workPad.getLocalServerUserId(), remoteRelationshipWithMappingProperties);


            assertCondition((true),
                    assertion15,
                    testTypeName + assertionMsg15,
                    RepositoryConformanceProfileRequirement.REFERENCE_COPY_STORAGE.getProfileId(),
                    RepositoryConformanceProfileRequirement.REFERENCE_COPY_STORAGE.getRequirementId());


            Relationship retrievedReferenceCopyWithMappingProperties = metadataCollection.getRelationship(workPad.getLocalServerUserId(), remoteRelationshipGUID);

            assertCondition((retrievedReferenceCopyWithMappingProperties.equals(remoteRelationshipWithMappingProperties)),
                    assertion16,
                    assertionMsg16 + relationshipDef.getName(),
                    RepositoryConformanceProfileRequirement.REFERENCE_COPY_STORAGE.getProfileId(),
                    RepositoryConformanceProfileRequirement.REFERENCE_COPY_STORAGE.getRequirementId());




            /*
             * Purge the reference copy - this is just to clean up rather than part of the test.
             */

            metadataCollection.purgeRelationshipReferenceCopy(workPad.getLocalServerUserId(),
                    remoteRelationshipWithMappingProperties.getGUID(),
                    remoteRelationshipWithMappingProperties.getType().getTypeDefGUID(),
                    remoteRelationshipWithMappingProperties.getType().getTypeDefName(),
                    remoteRelationshipWithMappingProperties.getMetadataCollectionId());


        }
        catch (FunctionNotSupportedException e) {

            super.addNotSupportedAssertion(assertion15,
                    assertionMsg15,
                    RepositoryConformanceProfileRequirement.REFERENCE_COPY_STORAGE.getProfileId(),
                    RepositoryConformanceProfileRequirement.REFERENCE_COPY_STORAGE.getRequirementId());

        }








        /*
         * Verify that it IS possible to re-home a reference copy.
         *
         * ===============================================================================================
         * |    NOTE THAT ALTHOUGH THIS OPERATION IS POSSIBLE, THE ONLY WAY TO TEST IT IS TO ISSUE       |
         * |    THE RE-HOME REQUEST BEFORE THE ORIGINAL RELATIONSHIP IS DELETED. THIS IS NOT A NORMAL    |
         * |    STATE FOR THE INSTANCE - IF THE RELATIONSHIP IS RE-HOMED THERE WILL BE TWO MASTERS       |
         * |    IN THE COHORT.                                                                           |
         * |    DO NOT ATTEMPT ANY FURTHER TESTS ON THE RELATIONSHIP OR REFERENCE COPY AFTER THIS, OTHER |
         * |    THAN THE DELETE OF THE ORIGINAL RELATIONSHIP AND REMOTE PURGE OF THE REFERENCE COPY.     |
         * ===============================================================================================
         */

        Relationship masterRelationship = ctsMetadataCollection.addRelationship(workPad.getLocalServerUserId(),
                relationshipDef.getGUID(),
                super.getAllPropertiesForInstance(workPad.getLocalServerUserId(), relationshipDef),
                entityOne.getGUID(),
                entityTwo.getGUID(),
                null);


        /*
         * Retrieve the ref copy from the TUT.
         * This is actually redundant, although it could be useful if you need to gate the remainder of the test - e.g. to provide a suitable delay for OMRS propagation
         */
        Relationship refCopyRelationship = null;

        remainingCount = this.pollCount;
        while (refCopyRelationship == null && remainingCount > 0) {
            refCopyRelationship = metadataCollection.isRelationshipKnown(workPad.getLocalServerUserId(), masterRelationship.getGUID());
            Thread.sleep(this.pollPeriod);
            remainingCount--;
        }


        /*
         * It is only possible to rehome by claiming an instance - i.e. it is a pull request - so in this test the TUT must lay claim to the relationship
         * If you try to initiate the re-home from the CTS when the rehomed event is processed on the TUT it will spot that it is being asked to
         * operate on a ref copy that has its own metadataCollectionId - and will hence throw an exception.
         */

        try {

            Relationship newMasterRelationship = metadataCollection.reHomeRelationship(workPad.getLocalServerUserId(),
                    masterRelationship.getGUID(),
                    relationshipDef.getGUID(),
                    relationshipDef.getName(),
                    ctsMetadataCollection.getMetadataCollectionId(workPad.getLocalServerUserId()),
                    metadataCollectionId,
                    repositoryConformanceWorkPad.getTutRepositoryConnector().getMetadataCollectionName());


            assertCondition((true),
                    assertion17,
                    testTypeName + assertionMsg17,
                    RepositoryConformanceProfileRequirement.UPDATE_INSTANCE_HOME.getProfileId(),
                    RepositoryConformanceProfileRequirement.UPDATE_INSTANCE_HOME.getRequirementId());



            /*
             * Now clean up. This is not a purge of the ref copy - the test will delete and purge the CTS masterRelationship below (
             * i.e. regardless of whether the rehome worked or not). That should clean up any lingering ref copy at the TUT.
             * But if the rehome worked we must remotely delete and purge the TUT instance - as it thinks it is no longer a ref copy.
             * Note that these operations are issued to the TUT.
             */

            try {
                Relationship deletedMasterRelationship = metadataCollection.deleteRelationship(workPad.getLocalServerUserId(),
                        masterRelationship.getType().getTypeDefGUID(),
                        masterRelationship.getType().getTypeDefName(),
                        masterRelationship.getGUID());
            } catch (FunctionNotSupportedException exception) {

                /*
                 * This is OK - we can NO OP and just proceed to purgeEntity
                 */
            }

            metadataCollection.purgeRelationship(workPad.getLocalServerUserId(),
                    masterRelationship.getType().getTypeDefGUID(),
                    masterRelationship.getType().getTypeDefName(),
                    masterRelationship.getGUID());


        } catch (InvalidParameterException e) {

            /*
             * We are expecting the rehome to work.
             */

            assertCondition((false),
                    assertion17,
                    testTypeName + assertionMsg17,
                    RepositoryConformanceProfileRequirement.UPDATE_INSTANCE_HOME.getProfileId(),
                    RepositoryConformanceProfileRequirement.UPDATE_INSTANCE_HOME.getRequirementId());
        }

        /*
         * Now clean up the CTS instance. i.e. delete and purge the CTS masterRelationship.
         */



        /*
         * Delete (soft then hard) the CTS local relationship - these operations are performed on the local (CTS) repo.
         * They will cause an OMRS instance event to flow to the TUT and for the ref copy to be purged, but since
         * we put the cohort into a multi-master state the TUT is likely to experience a state violation. That's
         * why this test is at the end.
         * Note that these operations are issued to the CTS.
         */

        try {
            Relationship deletedMasterRelationship = ctsMetadataCollection.deleteRelationship(workPad.getLocalServerUserId(),
                    masterRelationship.getType().getTypeDefGUID(),
                    masterRelationship.getType().getTypeDefName(),
                    masterRelationship.getGUID());
        } catch (FunctionNotSupportedException exception) {

            /*
             * This is OK - we can NO OP and just proceed to purgeEntity
             */
        }

        ctsMetadataCollection.purgeRelationship(workPad.getLocalServerUserId(),
                masterRelationship.getType().getTypeDefGUID(),
                masterRelationship.getType().getTypeDefName(),
                masterRelationship.getGUID());



        /*
         * Tidy up the remaining local test objects by purging the local entities
         * If the repository under test supports soft delete, each entity must be deleted before being purged
         * This should also clean up the corresponding ref copies of the test entities at the TUT.
         * These operations are to the CTS
         */

        try {
            EntityDetail deletedEntityOne = ctsMetadataCollection.deleteEntity(workPad.getLocalServerUserId(),
                    entityOne.getType().getTypeDefGUID(),
                    entityOne.getType().getTypeDefName(),
                    entityOne.getGUID());

            EntityDetail deletedEntityTwo = ctsMetadataCollection.deleteEntity(workPad.getLocalServerUserId(),
                    entityTwo.getType().getTypeDefGUID(),
                    entityTwo.getType().getTypeDefName(),
                    entityTwo.getGUID());
        } catch (FunctionNotSupportedException exception) {

            /*
             * This is OK - we can NO OP and just proceed to purgeEntity
             */
        }


        ctsMetadataCollection.purgeEntity(workPad.getLocalServerUserId(),
                entityOne.getType().getTypeDefGUID(),
                entityOne.getType().getTypeDefName(),
                entityOne.getGUID());

        ctsMetadataCollection.purgeEntity(workPad.getLocalServerUserId(),
                entityTwo.getType().getTypeDefGUID(),
                entityTwo.getType().getTypeDefName(),
                entityTwo.getGUID());

        super.setSuccessMessage("Reference copies of relationships can be managed through their lifecycle");

    }



    /**
     * Method to clean any instance created by the test case.
     *
     * @throws Exception something went wrong with the test.
     */
    public void cleanup() throws Exception
    {
        OMRSMetadataCollection metadataCollection = super.getMetadataCollection();

        /*
         * Find any relationships of the given type def and delete them....
         */

        int fromElement = 0;
        int pageSize = 50; // chunk size - loop below will repeatedly get chunks
        int resultSize = 0;

        do {


            InstanceProperties emptyMatchProperties = new InstanceProperties();


            List<Relationship> relationships = metadataCollection.findRelationshipsByProperty(workPad.getLocalServerUserId(),
                    relationshipDef.getGUID(),
                    emptyMatchProperties,
                    MatchCriteria.ANY,
                    fromElement,
                    null,
                    null,
                    null,
                    null,
                    pageSize);


            if (relationships == null) {
                /*
                 * There are no instances of this type reported by the repository.
                 */
                return;

            }

            /*
             * Report how many relationships were left behind at the end of the test run
             */

            System.out.println("At completion of testcase "+testTypeName+", there were " + relationships.size() + " relationships found");

            for (Relationship relationship : relationships) {


                /*
                 * Local variables for end2
                 */
                EntityProxy end1;
                EntityProxy end2;


                end1 = relationship.getEntityOneProxy();
                end2 = relationship.getEntityTwoProxy();


                try {

                    /*
                     * Delete the relationship and then both end entities.
                     * Deleting either entity first would delete the relationship, but
                     * this sequence is a little more orderly.
                     */

                    metadataCollection.deleteRelationship(workPad.getLocalServerUserId(),
                            relationship.getType().getTypeDefGUID(),
                            relationship.getType().getTypeDefName(),
                            relationship.getGUID());


                    metadataCollection.deleteEntity(workPad.getLocalServerUserId(),
                            end1.getType().getTypeDefGUID(),
                            end1.getType().getTypeDefName(),
                            end1.getGUID());

                    metadataCollection.deleteEntity(workPad.getLocalServerUserId(),
                            end2.getType().getTypeDefGUID(),
                            end2.getType().getTypeDefName(),
                            end2.getGUID());


                } catch (FunctionNotSupportedException exception) {
                    // NO OP - can proceed to purge
                }

                metadataCollection.purgeRelationship(workPad.getLocalServerUserId(),
                        relationship.getType().getTypeDefGUID(),
                        relationship.getType().getTypeDefName(),
                        relationship.getGUID());

                metadataCollection.purgeEntity(workPad.getLocalServerUserId(),
                        end1.getType().getTypeDefGUID(),
                        end1.getType().getTypeDefName(),
                        end1.getGUID());

                metadataCollection.purgeEntity(workPad.getLocalServerUserId(),
                        end2.getType().getTypeDefGUID(),
                        end2.getType().getTypeDefName(),
                        end2.getGUID());


                System.out.println("Relationship wth GUID " + relationship.getGUID() + " removed");
            }

        } while (resultSize >= pageSize);

    }


}
