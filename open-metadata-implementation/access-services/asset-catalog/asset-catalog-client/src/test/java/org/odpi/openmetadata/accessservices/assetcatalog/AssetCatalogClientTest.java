/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.accessservices.assetcatalog;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.odpi.openmetadata.accessservices.assetcatalog.model.AssetDescription;
import org.odpi.openmetadata.accessservices.assetcatalog.model.Classification;
import org.odpi.openmetadata.accessservices.assetcatalog.model.Element;
import org.odpi.openmetadata.accessservices.assetcatalog.model.Relationship;
import org.odpi.openmetadata.accessservices.assetcatalog.model.Term;
import org.odpi.openmetadata.accessservices.assetcatalog.model.rest.body.SearchParameters;
import org.odpi.openmetadata.accessservices.assetcatalog.model.rest.responses.AssetDescriptionResponse;
import org.odpi.openmetadata.accessservices.assetcatalog.model.rest.responses.AssetResponse;
import org.odpi.openmetadata.accessservices.assetcatalog.model.rest.responses.ClassificationsResponse;
import org.odpi.openmetadata.accessservices.assetcatalog.model.rest.responses.RelationshipResponse;
import org.odpi.openmetadata.accessservices.assetcatalog.model.rest.responses.RelationshipsResponse;
import org.odpi.openmetadata.adapters.connectors.restclients.RESTClientConnector;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceStatus;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

public class
AssetCatalogClientTest {

    private static final String SERVER_URL = "http://localhost:8081";
    private static final String SERVER_NAME = "TestServer";
    private static final String USER_ID = "zebra91";
    private static final String ASSET_ID = "66d7f872-19bd-439c-98ae-c3fe49d8f420";
    private static final String ASSET_TYPE = "GlossaryTerm";
    private static final String SEARCH_CRITERIA = "employee";
    private static final String SECOND_ASSET_ID = "66d7f872-19bd-439c-98ae-3232430022";
    private static final String SECOND_ASSET_TYPE = "RelationalColumn";
    private static final String RELATIONSHIP_TYPE = "SemanticAssignment";
    private static final String CLASSIFICATION_NAME = "Confidentiality";
    private static final Integer FROM = 0;
    private static final Integer PAGE_SIZE = 10;

    private AssetCatalog assetCatalog;

    @Mock
    private RESTClientConnector connector;

    @Before
    public void before() throws Exception {
        MockitoAnnotations.initMocks(this);

        assetCatalog = new AssetCatalog(SERVER_NAME, SERVER_URL);
        Field connectorField = ReflectionUtils.findField(AssetCatalog.class, "clientConnector");
        if (connectorField != null) {
            connectorField.setAccessible(true);
            ReflectionUtils.setField(connectorField, assetCatalog, connector);
            connectorField.setAccessible(false);
        }
    }

    @Test
    public void testGetAssetDetails() throws Exception {
        AssetDescriptionResponse response = mockAssetDescriptionResponse();

        when(connector.callGetRESTCall(eq("getAssetDetails"), eq(AssetDescriptionResponse.class), anyString(), eq(SERVER_NAME),
                eq(USER_ID), eq(response.getAssetDescriptionList().get(0).getGuid()), eq(ASSET_TYPE))).thenReturn(response);

        AssetDescriptionResponse assetDetails = assetCatalog.getAssetDetails(USER_ID,
                response.getAssetDescriptionList().get(0).getGuid(),
                ASSET_TYPE);

        Assert.assertEquals(response.getAssetDescriptionList().get(0).getGuid(), assetDetails.getAssetDescriptionList().get(0).getGuid());
    }

    @Test
    public void testGetAssetUniverse() throws Exception {
        AssetDescriptionResponse response = mockAssetDescriptionResponse();

        when(connector.callGetRESTCall(eq("getAssetUniverse"), eq(AssetDescriptionResponse.class), anyString(), eq(SERVER_NAME),
                eq(USER_ID), eq(response.getAssetDescriptionList().get(0).getGuid()), eq(ASSET_TYPE))).thenReturn(response);

        AssetDescriptionResponse assetDetails = assetCatalog.getAssetUniverse(USER_ID,
                response.getAssetDescriptionList().get(0).getGuid(),
                ASSET_TYPE);

        Assert.assertEquals(response.getAssetDescriptionList().get(0).getGuid(), assetDetails.getAssetDescriptionList().get(0).getGuid());
    }

    @Test
    public void testGetAssetRelationships() throws Exception {
        RelationshipsResponse response = mockRelationshipsResponse();

        when(connector.callGetRESTCall(eq("getAssetRelationships"), eq(RelationshipsResponse.class),
                anyString(),
                eq(SERVER_NAME),
                eq(USER_ID),
                eq(ASSET_ID),
                eq(ASSET_TYPE),
                eq(RELATIONSHIP_TYPE),
                eq(FROM),
                eq(PAGE_SIZE))).thenReturn(response);

        RelationshipsResponse relationshipsResponse = assetCatalog.getAssetRelationships(
                USER_ID,
                ASSET_ID,
                ASSET_TYPE,
                RELATIONSHIP_TYPE,
                FROM,
                PAGE_SIZE);

        Assert.assertEquals(RELATIONSHIP_TYPE, relationshipsResponse.getRelationships().get(0).getTypeDefName());
    }

    @Test
    public void testGetClassificationsForAsset() throws Exception {
        ClassificationsResponse response = mockClassificationsResponse();

        when(connector.callGetRESTCall(eq("getClassificationsForAsset"),
                eq(ClassificationsResponse.class),
                anyString(),
                eq(SERVER_NAME),
                eq(USER_ID),
                eq(ASSET_ID),
                eq(ASSET_TYPE),
                eq(CLASSIFICATION_NAME))).thenReturn(response);

        ClassificationsResponse classificationsResponse = assetCatalog.getClassificationsForAsset(
                USER_ID,
                ASSET_ID,
                ASSET_TYPE,
                CLASSIFICATION_NAME);

        Assert.assertEquals(CLASSIFICATION_NAME, classificationsResponse.getClassifications().get(0).getName());
    }

    @Test
    public void testGetLinkingAssets() throws Exception {
        AssetDescriptionResponse response = mockAssetDescriptionResponse();

        when(connector.callGetRESTCall(eq("getLinkingAssets"),
                eq(AssetDescriptionResponse.class),
                anyString(),
                eq(SERVER_NAME),
                eq(USER_ID),
                eq(ASSET_ID),
                eq(SECOND_ASSET_ID))).thenReturn(response);

        AssetDescriptionResponse assetDescriptionResponse = assetCatalog.getLinkingAssets(USER_ID,
                ASSET_ID,
                SECOND_ASSET_ID);

        Assert.assertEquals(ASSET_ID, assetDescriptionResponse.getAssetDescriptionList().get(0).getGuid());
    }

    @Test
    public void testGetLinkingRelationships() throws Exception {
        RelationshipsResponse response = mockRelationshipsResponse();

        when(connector.callGetRESTCall(eq("getLinkingRelationships"),
                eq(RelationshipsResponse.class),
                anyString(),
                eq(SERVER_NAME),
                eq(USER_ID),
                eq(ASSET_ID),
                eq(SECOND_ASSET_ID))).thenReturn(response);

        RelationshipsResponse relationshipsResponse = assetCatalog.getLinkingRelationships(USER_ID,
                ASSET_ID,
                SECOND_ASSET_ID);

        Assert.assertEquals(ASSET_ID, relationshipsResponse.getRelationships().get(0).getFromEntity().getGuid());
        Assert.assertEquals(SECOND_ASSET_ID, relationshipsResponse.getRelationships().get(0).getToEntity().getGuid());
    }

    @Test
    public void testGetAssetsFromNeighborhood() throws Exception {
        AssetDescriptionResponse response = mockAssetDescriptionResponse();
        SearchParameters searchParameters = mockSearchParameters();

        when(connector.callPostRESTCall(eq("getAssetsFromNeighborhood"),
                eq(AssetDescriptionResponse.class),
                anyString(),
                eq(SERVER_NAME),
                eq(USER_ID),
                eq(ASSET_ID),
                eq(searchParameters))).thenReturn(response);

        AssetDescriptionResponse assetsFromNeighborhood = assetCatalog.getAssetsFromNeighborhood(USER_ID,
                ASSET_ID,
                searchParameters);

        Assert.assertEquals(ASSET_ID, assetsFromNeighborhood.getAssetDescriptionList().get(0).getGuid());
    }

    @Test
    public void testSearchByType() throws Exception {
        AssetResponse response = mockAssetResponse();
        SearchParameters searchParameters = mockSearchParameters();

        when(connector.callPostRESTCall(eq("searchByType"),
                eq(AssetResponse.class),
                anyString(),
                eq(SERVER_NAME),
                eq(USER_ID),
                eq(SEARCH_CRITERIA),
                eq(searchParameters))).thenReturn(response);

        AssetResponse assetResponse = assetCatalog.searchByType(USER_ID, SEARCH_CRITERIA, searchParameters);

        Assert.assertEquals(ASSET_ID, assetResponse.getAssets().get(0).getGuid());
    }

    @Test
    public void testGetAssetContext() throws Exception {
        AssetResponse response = mockAssetResponse();

        when(connector.callGetRESTCall(eq("getAssetContext"),
                eq(AssetResponse.class),
                anyString(),
                eq(SERVER_NAME),
                eq(USER_ID),
                eq(ASSET_ID),
                eq(ASSET_TYPE))).thenReturn(response);

        AssetResponse assetResponse = assetCatalog.getAssetContext(USER_ID,
                ASSET_ID,
                ASSET_TYPE);

        Assert.assertEquals(ASSET_ID, assetResponse.getAssets().get(0).getGuid());
    }

    @Test
    public void testGetRelationshipBetweenEntities() throws Exception {
        RelationshipResponse response = mockRelationshipResponse();

        when(connector.callGetRESTCall(eq("getRelationshipBetweenEntities"),
                eq(RelationshipResponse.class),
                anyString(),
                eq(SERVER_NAME),
                eq(USER_ID),
                eq(ASSET_ID),
                eq(SECOND_ASSET_ID),
                eq(RELATIONSHIP_TYPE))).thenReturn(response);

        RelationshipResponse relationshipBetweenEntities = assetCatalog.getRelationshipBetweenEntities(
                USER_ID,
                ASSET_ID,
                SECOND_ASSET_ID,
                RELATIONSHIP_TYPE);

        Assert.assertEquals(RELATIONSHIP_TYPE, relationshipBetweenEntities.getRelationship().getTypeDefName());
        Assert.assertEquals(ASSET_ID, relationshipBetweenEntities.getRelationship().getFromEntity().getGuid());
        Assert.assertEquals(SECOND_ASSET_ID, relationshipBetweenEntities.getRelationship().getToEntity().getGuid());
    }


    private AssetResponse mockAssetResponse() {
        AssetResponse assetResponse = new AssetResponse();
        assetResponse.setAssets(Collections.singletonList(mockTerm()));
        return assetResponse;
    }

    private Term mockTerm() {
        Term term = new Term();
        term.setGuid(ASSET_ID);
        term.setTypeDefName(ASSET_TYPE);
        return term;
    }

    private RelationshipResponse mockRelationshipResponse() {
        RelationshipResponse relationshipsResponse = new RelationshipResponse();
        relationshipsResponse.setRelationship(mockRelationship());
        return relationshipsResponse;
    }

    private SearchParameters mockSearchParameters() {
        SearchParameters searchParameters = new SearchParameters();

        searchParameters.setLevel(2);
        searchParameters.setFrom(0);
        searchParameters.setPageSize(10);

        return searchParameters;
    }

    private ClassificationsResponse mockClassificationsResponse() {
        ClassificationsResponse classificationsResponse = new ClassificationsResponse();
        classificationsResponse.setClassifications(mockClassifications());
        return classificationsResponse;
    }


    private RelationshipsResponse mockRelationshipsResponse() {
        RelationshipsResponse relationshipsResponse = new RelationshipsResponse();
        relationshipsResponse.setRelationships(mockRelationships());
        return relationshipsResponse;
    }


    private AssetDescriptionResponse mockAssetDescriptionResponse() {
        AssetDescriptionResponse expectedResponse = new AssetDescriptionResponse();
        expectedResponse.setRelatedHTTPCode(200);

        AssetDescription assetDescription = mockAssetDescription();
        expectedResponse.setAssetDescriptionList(Collections.singletonList(assetDescription));

        return expectedResponse;
    }

    private AssetDescription mockAssetDescription() {
        AssetDescription assetDescription = new AssetDescription();

        assetDescription.setGuid(ASSET_ID);
        assetDescription.setTypeDefName(ASSET_TYPE);

        assetDescription.setClassifications(mockClassifications());
        assetDescription.setRelationships(mockRelationships());

        return assetDescription;
    }

    private List<Relationship> mockRelationships() {
        return Collections.singletonList(mockRelationship());
    }

    private Relationship mockRelationship() {
        Relationship relationship = new Relationship();

        relationship.setFromEntity(mockAsset(ASSET_ID, ASSET_TYPE));
        relationship.setToEntity(mockAsset(SECOND_ASSET_ID, SECOND_ASSET_TYPE));
        relationship.setTypeDefName(RELATIONSHIP_TYPE);

        return relationship;
    }

    private Element mockAsset(String defaultAssetId, String typeName) {
        Element asset = new Element();

        asset.setGuid(defaultAssetId);
        asset.setCreatedBy("admin");
        asset.setTypeDefName(typeName);

        return asset;
    }

    private List<Classification> mockClassifications() {
        return Collections.singletonList(mockClassification(CLASSIFICATION_NAME));
    }

    private Classification mockClassification(String classificationName) {
        Classification classification = new Classification();

        classification.setName(classificationName);
        classification.setCreatedBy("admin");
        classification.setStatus(InstanceStatus.ACTIVE.getName());

        return classification;
    }
}