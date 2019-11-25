/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.userinterface.uichassis.springboot.service;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.governanceservers.openlineage.client.OpenLineageClient;
import org.odpi.openmetadata.governanceservers.openlineage.model.GraphName;
import org.odpi.openmetadata.governanceservers.openlineage.model.LineageVerticesAndEdges;
import org.odpi.openmetadata.governanceservers.openlineage.model.Scope;
import org.odpi.openmetadata.governanceservers.openlineage.model.View;
import org.odpi.openmetadata.userinterface.uichassis.springboot.beans.Node;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;


public class OpenLineageServiceTest {

    public static final String RESPONSE_JSON = "./src/test/resources/openLineageServicesResponse.json";
    public static final String guid = "test-guid";
    private static String USER_ID = "userId";
    private static ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static LineageVerticesAndEdges lineageVerticesAndEdges;

    @Mock
    private OpenLineageClient openLineageClient;

    @InjectMocks
    private OpenLineageService openLineageService;

    @Value("${open.lineage.graph.source}")
    private GraphName graphName;


    @BeforeEach
    void setup() {
        MockitoAnnotations.initMocks(this);
        ReflectionTestUtils.setField(openLineageService, "graphName", GraphName.MAIN);
    }

    @BeforeAll
    static void readResource() throws IOException {
        String payload = FileUtils.readFileToString(new File(RESPONSE_JSON), "UTF-8");
        lineageVerticesAndEdges = OBJECT_MAPPER.readValue(payload, LineageVerticesAndEdges.class);
    }

    @Test
    @DisplayName("Ultimate Source")
    public void testUltimateSource() throws PropertyServerException, InvalidParameterException {
        when(openLineageClient.lineage(eq(USER_ID), eq(GraphName.MAIN), eq(Scope.ULTIMATE_SOURCE), eq(View.COLUMN_VIEW), eq(guid))).thenReturn(lineageVerticesAndEdges);
        Map<String, List> ultimateSource = openLineageService.getUltimateSource(USER_ID, View.COLUMN_VIEW, guid);
        checkResponse(ultimateSource);
    }

    @Test
    @DisplayName("End To End")
    public void testEndToEnd() throws PropertyServerException, InvalidParameterException {
        when(openLineageClient.lineage(eq(USER_ID), eq(GraphName.MAIN), eq(Scope.END_TO_END), eq(View.COLUMN_VIEW), eq(guid))).thenReturn(lineageVerticesAndEdges);
        Map<String, List> response = openLineageService.getEndToEndLineage(USER_ID, View.COLUMN_VIEW, guid);
        checkResponse(response);
    }


    @Test
    @DisplayName("Ultimate Destination")
    public void testUltimateDestination() throws PropertyServerException, InvalidParameterException {
        when(openLineageClient.lineage(eq(USER_ID), eq(GraphName.MAIN), eq(Scope.ULTIMATE_DESTINATION), eq(View.COLUMN_VIEW), eq(guid))).thenReturn(lineageVerticesAndEdges);
        Map<String, List> response = openLineageService.getUltimateDestination(USER_ID, View.COLUMN_VIEW, guid);
        checkResponse(response);
    }

    @Test
    @DisplayName("Source and Destination")
    public void testSourceAndDestination() throws PropertyServerException, InvalidParameterException {
        when(openLineageClient.lineage(eq(USER_ID), eq(GraphName.MAIN), eq(Scope.SOURCE_AND_DESTINATION), eq(View.COLUMN_VIEW), eq(guid))).thenReturn(lineageVerticesAndEdges);
        Map<String, List> response = openLineageService.getSourceAndDestination(USER_ID, View.COLUMN_VIEW, guid);
        checkResponse(response);
    }

    @Test
    @DisplayName("GlossaryLineage")
    public void testGlossaryLineage() throws PropertyServerException, InvalidParameterException {
        when(openLineageClient.lineage(eq(USER_ID), eq(GraphName.MAIN), eq(Scope.GLOSSARY), eq(View.COLUMN_VIEW), eq(guid))).thenReturn(lineageVerticesAndEdges);
        Map<String, List> response = openLineageService.getGlossaryLineage(USER_ID, View.COLUMN_VIEW, guid);
        checkResponse(response);
    }

    private void checkResponse(Map<String, List> ultimateSource) {
        assertNotNull("Response is null", ultimateSource);
        assertEquals("Response should contain onlu nodes and edges",2, ultimateSource.size());
        assertTrue("Response should contain nodes", ultimateSource.containsKey("nodes"));
        assertTrue("Response should contain edges", ultimateSource.containsKey("edges"));
        List nodes = ultimateSource.get("nodes");
        assertNotNull("List of nodes is null", nodes);
        List<String> nodesIds = (List)nodes.stream().map(e -> ((Node) e).getId()).collect(Collectors.toList());
        assertEquals("Response should contain 3 nodes", 3, nodes.size());
        assertTrue("Response doesn't contain all nodes", nodesIds.containsAll(Arrays.asList("p0","p30", "p2")));
    }
}
