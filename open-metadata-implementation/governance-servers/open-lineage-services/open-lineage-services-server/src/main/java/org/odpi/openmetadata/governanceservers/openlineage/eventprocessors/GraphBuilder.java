/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.governanceservers.openlineage.eventprocessors;

import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.structure.Graph;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.apache.tinkerpop.gremlin.structure.io.graphml.GraphMLWriter;
import org.apache.tinkerpop.gremlin.tinkergraph.structure.TinkerGraph;
import org.odpi.openmetadata.accessservices.assetlineage.model.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityProxy;
import org.odpi.openmetadata.repositoryservices.events.OMRSInstanceEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import static org.odpi.openmetadata.governanceservers.openlineage.util.Constants.*;

public class GraphBuilder {

    private static final Logger log = LoggerFactory.getLogger(GraphBuilder.class);
    private Graph graph;
    private GraphTraversalSource g;

    public GraphBuilder() {
        this.graph = TinkerGraph.open();
        this.g = graph.traversal();
    }


    public void addAsset(AssetContext event) {
        Term relationalColumn = event.getAssets().get(0);
        AssetElement database = event.getAssets().get(0).getElements().get(0);

        Element relationalTableType = database.getContext().get(0);
        Element relationalTable = database.getContext().get(1);
        Element relationalDBSchemaType = database.getContext().get(2);
        Element deployedDatabaseSchema = database.getContext().get(3);

        Connection connection = database.getConnections().get(0);

        //TODO Open Metadata format should not be used in graph, e.g. column/table types should be properties instead. TBD.

        Vertex relationalColumnVertex = addVertex(relationalColumn);
        Vertex databaseVertex = addVertex(database);
        Vertex relationalTableTypeVertex = addVertex(relationalTableType);
        Vertex relationalTableVertex = addVertex(relationalTable);
        Vertex relationalDBSchemaTypeVertex = addVertex(relationalDBSchemaType);
        Vertex deployedDatabaseSchemaVertex = addVertex(deployedDatabaseSchema);

        addEdge(DATA_CONTENT_FOR_DATA_SET, databaseVertex, deployedDatabaseSchemaVertex);
        addEdge(ASSET_SCHEMA_TYPE, deployedDatabaseSchemaVertex, relationalDBSchemaTypeVertex);
        addEdge(ATTRIBUTE_FOR_SCHEMA, relationalDBSchemaTypeVertex, relationalTableVertex);
        addEdge(SCHEMA_ATTRIBUTE_TYPE, relationalTableVertex, relationalTableTypeVertex);
        addEdge(ATTRIBUTE_FOR_SCHEMA, relationalTableTypeVertex, relationalColumnVertex);
    }

    /**
     * Connection is part of the event json
     * @param connection
     */
    private void addConnection(Connection connection) {
        String GUID = connection.getGuid();
        String qualifiedName = connection.getQualifiedName();

        Vertex v1 = g.addV(GUID).next();
        v1.property(QUALIFIED_NAME, qualifiedName);
    }

    private Vertex addVertex(Element assetElement) {
        String GUID = assetElement.getGuid();
        String type = assetElement.getType();
        String qualifiedName = assetElement.getQualifiedName();

        Vertex v1 = g.addV(GUID).next();
        v1.property(QUALIFIED_NAME, qualifiedName);
        v1.property(TYPE, type);
        return v1;
    }

    private void addEdge(String relationship, Vertex v1, Vertex v2) {
        v1.addEdge(relationship, v2);
    }


    /**
     * DEPRECATED
     *  Add vertices and edges for semantic relationships
     * @param omrsInstanceEvent 1:1 copy of the OMRS topic event
     */
    public void addNewRelationship(OMRSInstanceEvent omrsInstanceEvent) {
        EntityProxy proxy1 = omrsInstanceEvent.getRelationship().getEntityOneProxy();
        EntityProxy proxy2 = omrsInstanceEvent.getRelationship().getEntityTwoProxy();

        String GUID1 = proxy1.getGUID();
        String GUID2 = proxy2.getGUID();

        Vertex v1 = g.V().hasLabel(GUID1).next();
        Vertex v2 = g.V().hasLabel(GUID2).next();

        v1.addEdge(SEMANTIC_ASSIGNMENT, v2);
    }

    public void exportGraph() {
        File file = new File(GRAPHML);

        FileOutputStream fos;

        try {
            fos = new FileOutputStream(file, true);
            try {
                GraphMLWriter.build().create().writeGraph(fos, graph);
                log.info("Graph saved to lineageGraph.graphml");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}