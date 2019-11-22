/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.userinterface.uichassis.springboot.service;


import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.governanceservers.openlineage.client.OpenLineageClient;
import org.odpi.openmetadata.governanceservers.openlineage.model.GraphName;
import org.odpi.openmetadata.governanceservers.openlineage.model.LineageVertex;
import org.odpi.openmetadata.governanceservers.openlineage.model.LineageVerticesAndEdges;
import org.odpi.openmetadata.governanceservers.openlineage.model.Scope;
import org.odpi.openmetadata.governanceservers.openlineage.model.View;
import org.odpi.openmetadata.userinterface.uichassis.springboot.beans.Edge;
import org.odpi.openmetadata.userinterface.uichassis.springboot.beans.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * This class responsibility is to interact with Open Lineage Services(OLS), process the returned response and return it in a format understood by view
 */
@Service
public class OpenLineageService {

    public static final String EDGES_LABEL = "edges";
    public static final String NODES_LABEL = "nodes";
    public static final String GLOSSARY_TERM = "glossaryTerm";
    private final OpenLineageClient openLineageClient;
    private com.fasterxml.jackson.databind.ObjectMapper mapper;
    @Value("${open.lineage.graph.source}")
    private GraphName graphName;
    private static final Logger LOG = LoggerFactory.getLogger(OpenLineageService.class);

    /**
     *
     * @param openLineageClient client to connect to open lineage services
     */
    @Autowired
    public OpenLineageService(OpenLineageClient openLineageClient) {
        this.openLineageClient = openLineageClient;
        mapper = new com.fasterxml.jackson.databind.ObjectMapper();
    }

    /**
     *
     * @param userId id of the user triggering the request
     * @param view level of granularity, eg down to column or table level
     * @param guid unique identifier if the asset
     * @return map of nodes and edges describing the ultimate sources for the asset
     */
    public Map<String, List> getUltimateSource(String userId, View view, String guid)  {
        LineageVerticesAndEdges response = null;
        try {
            response = openLineageClient.lineage(userId, graphName, Scope.ULTIMATE_SOURCE, view, guid);
        } catch (InvalidParameterException e) {
            LOG.error(e.getMessage(), e);
            throw new OpenLineageServiceException(e.getMessage(), e);
        } catch (PropertyServerException e) {
            LOG.error(e.getMessage(), e);
            throw new OpenLineageServiceException(e.getMessage(), e);
        }
        return processResponse(response);
    }

    /**
     *
     * @param userId id of the user triggering the request
     * @param view level of granularity, eg down to column or table level
     * @param guid unique identifier if the asset
     * @return map of nodes and edges describing the end to end flow
     */
    public Map<String, List> getEndToEndLineage(String userId, View view, String guid)  {
        LineageVerticesAndEdges response = null;
        try {
            response = openLineageClient.lineage(userId, graphName, Scope.END_TO_END, view, guid);
        } catch (InvalidParameterException e) {
            LOG.error(e.getMessage(), e);
            throw new OpenLineageServiceException(e.getMessage(), e);
        } catch (PropertyServerException e) {
            LOG.error(e.getMessage(), e);
            throw new OpenLineageServiceException(e.getMessage(), e);
        }
        return processResponse(response);
    }

    /**
     *
     * @param userId id of the user triggering the request
     * @param view level of granularity, eg down to column or table level
     * @param guid unique identifier if the asset
     * @return map of nodes and edges describing the ultimate destinations of the asset
     */
    public Map<String, List> getUltimateDestination(String userId, View view, String guid)  {
        LineageVerticesAndEdges response = null;
        try {
            response = openLineageClient.lineage(userId, graphName, Scope.ULTIMATE_DESTINATION, view, guid);
        } catch (InvalidParameterException e) {
            LOG.error(e.getMessage(), e);
            throw new OpenLineageServiceException(e.getMessage(), e);
        } catch (PropertyServerException e) {
            LOG.error(e.getMessage(), e);
            throw new OpenLineageServiceException(e.getMessage(), e);
        }
        return processResponse(response);

    }

    /**
     *
     * @param userId id of the user triggering the request
     * @param view level of granularity, eg down to column or table level
     * @param guid unique identifier if the asset
     * @return map of nodes and edges describing the glossary terms linked to the asset
     */
    public Map<String, List> getGlossaryLineage(String userId, View view, String guid)  {
        LineageVerticesAndEdges response = null;
        try {
            response = openLineageClient.lineage(userId, graphName, Scope.GLOSSARY, view, guid);
        } catch (InvalidParameterException e) {
            LOG.error(e.getMessage(), e);
            throw new OpenLineageServiceException(e.getMessage(), e);
        } catch (PropertyServerException e) {
            LOG.error(e.getMessage(), e);
            throw new OpenLineageServiceException(e.getMessage(), e);
        }
        return processResponse(response);
    }

    /**
     *
     * @param userId id of the user triggering the request
     * @param view level of granularity, eg down to column or table level
     * @param guid unique identifier if the asset
     * @return map of nodes and edges describing the ultimate sources and destinations of the asset
     */
    public Map<String, List> getSourceAndDestination(String userId, View view, String guid)  {
        LineageVerticesAndEdges response =
                null;
        try {
            response = openLineageClient.lineage(userId, graphName, Scope.SOURCE_AND_DESTINATION, view, guid);
        } catch (InvalidParameterException e) {
            LOG.error(e.getMessage(), e);
            throw new OpenLineageServiceException(e.getMessage(), e);
        } catch (PropertyServerException e) {
            LOG.error(e.getMessage(), e);
            throw new OpenLineageServiceException(e.getMessage(), e);
        }
        return processResponse(response);
    }

    /**
     *
     * @param response string returned from Open Lineage Services to be processed
     * @return map of nodes and edges describing the end to end flow
     */
    private Map<String, List> processResponse(LineageVerticesAndEdges response)  {
        Map<String, List> graphData = new HashMap<>();
        List<Edge> listEdges = new ArrayList<>();
        List<Node> listNodes = new ArrayList<>();

        LOG.debug("Received response from open lineage service: {}", response);
        if (response == null || CollectionUtils.isEmpty(response.getLineageVertices())) {
            graphData.put(EDGES_LABEL, listEdges);
            graphData.put(NODES_LABEL, listNodes);
        }

        listNodes = Optional.ofNullable(response.getLineageVertices())
                .map(Collection::stream)
                .orElseGet(Stream::empty)
                .map(v -> createNode(v))
                .collect(Collectors.toList());

        listEdges = Optional.ofNullable(response.getLineageEdges())
                .map(e -> e.stream())
                .orElseGet(Stream::empty)
                .map(e -> {Edge newEdge = new Edge( e.getSourceNodeID(),
                                                    e.getDestinationNodeID());
                                                    newEdge.setLabel(e.getEdgeType());
                                                    return newEdge;})
                .collect(Collectors.toList());

        graphData.put(EDGES_LABEL, listEdges);
        graphData.put(NODES_LABEL, listNodes);

        return graphData;
    }

    /**
     * This method will create a new node in ui specific format based on the properties of the currentNode to be processed
     * @param currentNode current node to be processed
     * @return the node in the format to be understand by the ui
     *
     */
    private Node createNode(LineageVertex currentNode) {
        String displayName = currentNode.getDisplayName();
        String glossaryTerm = "";
        if(!CollectionUtils.isEmpty(currentNode.getAttributes()) ){
            glossaryTerm = currentNode.getAttributes().get(GLOSSARY_TERM);
        }
        if(!StringUtils.isEmpty(glossaryTerm)){
            displayName = displayName + "\n" + glossaryTerm;
        }
        Node node = new Node(currentNode.getNodeID(), displayName);
        node.setGroup(currentNode.getDisplayName());
        node.setProperties(currentNode.getAttributes());
        return node;
    }


}