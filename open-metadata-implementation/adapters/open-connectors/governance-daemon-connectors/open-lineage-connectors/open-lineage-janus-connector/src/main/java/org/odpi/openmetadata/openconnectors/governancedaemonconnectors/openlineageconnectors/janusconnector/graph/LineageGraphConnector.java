/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.graph;

import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__;
import org.apache.tinkerpop.gremlin.structure.Column;
import org.apache.tinkerpop.gremlin.structure.Edge;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.janusgraph.core.JanusGraph;
import org.odpi.openmetadata.accessservices.assetlineage.model.GraphContext;
import org.odpi.openmetadata.accessservices.assetlineage.model.LineageEntity;
import org.odpi.openmetadata.accessservices.assetlineage.model.LineageRelationship;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.governanceservers.openlineage.buffergraph.LineageGraphConnectorBase;
import org.odpi.openmetadata.governanceservers.openlineage.ffdc.OpenLineageException;
import org.odpi.openmetadata.governanceservers.openlineage.ffdc.OpenLineageServerErrorCode;
import org.odpi.openmetadata.governanceservers.openlineage.model.LineageVerticesAndEdges;
import org.odpi.openmetadata.governanceservers.openlineage.model.Scope;
import org.odpi.openmetadata.governanceservers.openlineage.responses.LineageResponse;
import org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.factory.GraphFactory;
import org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.model.JanusConnectorErrorCode;
import org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.model.ffdc.JanusConnectorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__.bothE;
import static org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__.hasLabel;
import static org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__.inV;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.Constants.ASSET_SCHEMA_TYPE;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.Constants.ATTRIBUTE_FOR_SCHEMA;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.Constants.DATA_FILE;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.Constants.LINEAGE_MAPPING;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.Constants.NESTED_SCHEMA_ATTRIBUTE;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.Constants.PORT_DELEGATION;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.Constants.PORT_IMPLEMENTATION;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.Constants.PORT_SCHEMA;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.Constants.PROCESS_PORT;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.Constants.RELATIONAL_TABLE;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.GraphConstants.EDGE_LABEL_DATAFLOW_WITHOUT_PROCESS;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.GraphConstants.EDGE_LABEL_DATAFLOW_WITH_PROCESS;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.GraphConstants.EDGE_LABEL_INCLUDED_IN;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.GraphConstants.EDGE_LABEL_SEMANTIC;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.GraphConstants.NODE_LABEL_SUB_PROCESS;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.GraphConstants.PROPERTY_KEY_DISPLAY_NAME;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.GraphConstants.PROPERTY_KEY_ENTITY_CREATED_BY;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.GraphConstants.PROPERTY_KEY_ENTITY_CREATE_TIME;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.GraphConstants.PROPERTY_KEY_ENTITY_GUID;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.GraphConstants.PROPERTY_KEY_ENTITY_NODE_ID;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.GraphConstants.PROPERTY_KEY_ENTITY_UPDATED_BY;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.GraphConstants.PROPERTY_KEY_ENTITY_UPDATE_TIME;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.GraphConstants.PROPERTY_KEY_ENTITY_VERSION;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.GraphConstants.PROPERTY_KEY_INSTANCEPROP_DISPLAY_NAME;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.GraphConstants.PROPERTY_KEY_LABEL;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.GraphConstants.PROPERTY_KEY_METADATA_ID;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.GraphConstants.PROPERTY_KEY_PREFIX_ELEMENT;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.GraphConstants.PROPERTY_KEY_PREFIX_INSTANCE_PROPERTY;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.GraphConstants.PROPERTY_KEY_RELATIONSHIP_CREATED_BY;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.GraphConstants.PROPERTY_KEY_RELATIONSHIP_CREATE_TIME;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.GraphConstants.PROPERTY_KEY_RELATIONSHIP_DISPLAY_NAME;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.GraphConstants.PROPERTY_KEY_RELATIONSHIP_GUID;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.GraphConstants.PROPERTY_KEY_RELATIONSHIP_UPDATED_BY;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.GraphConstants.PROPERTY_KEY_RELATIONSHIP_UPDATE_TIME;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.GraphConstants.PROPERTY_KEY_RELATIONSHIP_VERSION;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.GraphConstants.PROPERTY_NAME_PORT_TYPE;

public class LineageGraphConnector extends LineageGraphConnectorBase {

    private static final Logger log = LoggerFactory.getLogger(LineageGraphConnector.class);
    private JanusGraph lineageGraph;
    private GraphVertexMapper graphVertexMapper = new GraphVertexMapper();
    private LineageGraphConnectorHelper helper;

    /**
     * Instantiates the graph based on the configuration passed.
     *
     */
    public void initializeGraphDB() throws OpenLineageException {
        GraphFactory graphFactory = new GraphFactory();

        try {
            this.lineageGraph = graphFactory.openGraph(connectionProperties);
        } catch (JanusConnectorException error) {
            log.error("The Buffer graph could not be initialized due to an error", error);
            throw new OpenLineageException(500,
                    error.getReportingClassName(),
                    error.getReportingActionDescription(),
                    error.getReportedErrorMessage(),
                    error.getReportedSystemAction(),
                    error.getReportedUserAction()
            );
        }
        this.helper = new LineageGraphConnectorHelper(lineageGraph);
    }

    @Override
    public void schedulerTask(){
        GraphTraversalSource g = lineageGraph.traversal();
        try {
            List<Vertex> vertices = g.V().has(PROPERTY_KEY_LABEL, "Process").toList();
            List<String> guidList = vertices.stream().map(v -> (String) v.property(PROPERTY_KEY_ENTITY_GUID).value()).collect(Collectors.toList());

            guidList.forEach(process -> findInputColumns(g,process));
            g.tx().commit();
        }catch (Exception e){
            log.error("Something went wrong when trying to map a process from bufferGraph to the mainGraph. The error is {}",e);
            g.tx().rollback();
        }
    }

    /**
     * Finds the paths to the input columns from all the processes in the graph.
     * @param g - Graph traversal object
     * @param guid - The unique identifier of a Process
     */
    private void findInputColumns(GraphTraversalSource g,String guid) {
        List<Vertex> inputPathsForColumns = g.V().has(PROPERTY_KEY_ENTITY_GUID, guid).out(PROCESS_PORT).out(PORT_DELEGATION)
                .has(PORT_IMPLEMENTATION, PROPERTY_NAME_PORT_TYPE, "INPUT_PORT")
                .out(PORT_SCHEMA).in(ATTRIBUTE_FOR_SCHEMA).out(LINEAGE_MAPPING)
                .or(__.out(ATTRIBUTE_FOR_SCHEMA).out(ASSET_SCHEMA_TYPE)
                                .has(PROPERTY_KEY_LABEL,DATA_FILE),
                        __.out(NESTED_SCHEMA_ATTRIBUTE).has(PROPERTY_KEY_LABEL,RELATIONAL_TABLE)).toList();

        Vertex process = g.V().has(PROPERTY_KEY_ENTITY_GUID, guid).next();
        inputPathsForColumns.forEach(columnIn -> findOutputColumn(g, columnIn, process));
    }

    /**
     * Finds the output column of a Process based on the input.
     * @param g - Graph traversal object
     * @param columnIn - THe vertex of the schema element before processing.
     * @param process - The vertex of the process.
     */
    private void findOutputColumn(GraphTraversalSource g,Vertex columnIn,Vertex process){
        List<Vertex> schemaElementVertex = g.V()
                .has(PROPERTY_KEY_ENTITY_GUID, columnIn.property(PROPERTY_KEY_ENTITY_GUID).value())
                .in(LINEAGE_MAPPING)
                .toList();

        Vertex vertexToStart = null;
        if (schemaElementVertex != null) {
            Vertex columnOut = null;
            vertexToStart = getProcessForTheSchemaElement(g,schemaElementVertex,process);
            if (vertexToStart != null) {
                columnOut = findPathForOutputAsset(vertexToStart, g, columnIn);
            }
            moveColumnProcessColumn(columnIn, columnOut, process);
        }
    }

    /**
     * Returns the vertex from where the searching for the output column will start.
     * @param g - Graph traversal object
     * @param schemaElementVertex - THe vertex of the column before processing.
     * @param process - The vertex of the process.
     * @return Return the vertex of the initial column
     */
    private Vertex getProcessForTheSchemaElement(GraphTraversalSource g,List<Vertex> schemaElementVertex,Vertex process){
        Vertex vertexToStart = null;
        for (Vertex v : schemaElementVertex) {
            List<Vertex> initialProcess = g.V(v.id())
                    .bothE(ATTRIBUTE_FOR_SCHEMA)
                    .otherV().inE(PORT_SCHEMA).otherV()
                    .inE(PORT_DELEGATION).otherV()
                    .inE(PROCESS_PORT).otherV()
                    .has(PROPERTY_KEY_ENTITY_GUID, process.property(PROPERTY_KEY_ENTITY_GUID).value()).toList();

            if (!initialProcess.isEmpty()) {
                vertexToStart = v;
                break;
            }
        }
        return vertexToStart;
    }

    /**
     * Initiates the process of copying the input and output vertices to the MainGraph.
     * @param columnIn - The vertex of the input schema element
     * @param columnOut - THe vertex of the output schema element
     * @param process - The vertex of the process.
     */
    private void moveColumnProcessColumn(Vertex columnIn,Vertex columnOut,Vertex process){
        if (columnOut != null) {
            String columnOutGuid = columnOut.values(PROPERTY_KEY_ENTITY_GUID).next().toString();
            String columnInGuid = columnIn.values(PROPERTY_KEY_ENTITY_GUID).next().toString();
            if (!columnOutGuid.isEmpty() && !columnInGuid.isEmpty()) {
                test(columnIn,columnOut,process);
            }
        }
    }

    private void test(Vertex columnIn,Vertex columnOut,Vertex process){

        GraphTraversalSource g = lineageGraph.traversal();

        final String processGuid = process.value(PROPERTY_KEY_ENTITY_GUID);
        final String processName = process.value(PROPERTY_KEY_INSTANCEPROP_DISPLAY_NAME);

        //check if subprocess node exist
        Iterator<Vertex> t = g.V(columnIn.id()).outE(EDGE_LABEL_DATAFLOW_WITH_PROCESS).has("processGuid",processGuid).inV().has(PROPERTY_KEY_ENTITY_GUID,columnOut.property(PROPERTY_KEY_ENTITY_GUID).value());

        if(!t.hasNext()) {
            Vertex subProcess = g.addV(NODE_LABEL_SUB_PROCESS)
                    .property(PROPERTY_KEY_ENTITY_NODE_ID, UUID.randomUUID().toString())
                    .property("processGuid", processGuid)
                    .property(PROPERTY_KEY_DISPLAY_NAME, processName)
                    .next();

            columnIn.addEdge(EDGE_LABEL_DATAFLOW_WITH_PROCESS, subProcess);
            subProcess.addEdge(EDGE_LABEL_DATAFLOW_WITH_PROCESS, columnOut);
            subProcess.addEdge(EDGE_LABEL_INCLUDED_IN, process);
            g.tx().commit();

            addTableNode(columnIn, columnOut, process);
        }
    }

    private void addTableNode(Vertex columnInVertex, Vertex columnOutVertex, Vertex process){
        GraphTraversalSource bufferG = lineageGraph.traversal();


        Vertex tableIn = getTable(bufferG,columnInVertex);
        Vertex tableOut = getTable(bufferG,columnOutVertex);
//
//        if (tableIn == null || tableOut == null){
//            bufferG.tx().rollback();
//            mainG.tx().rollback();
//            return;
//        }

        addTableRelationships(bufferG,tableIn,process,columnInVertex);
        addTableRelationships(bufferG,tableOut,process,columnOutVertex);

//        addColumns(bufferG,mainG,tableOut);

        bufferG.tx().commit();
    }

    private Vertex getTable(GraphTraversalSource bufferG,Vertex asset){
        Iterator<Vertex> table = bufferG.V().has(PROPERTY_KEY_ENTITY_GUID,asset.property(PROPERTY_KEY_ENTITY_GUID).value())
                .emit().repeat(bothE().otherV().simplePath()).times(2).or(hasLabel(RELATIONAL_TABLE),hasLabel(DATA_FILE));

        if (!table.hasNext()){
            return null;
        }

        return table.next();
    }

    private void addTableRelationships(GraphTraversalSource bufferG,Vertex table,Vertex process,Vertex column){

        Iterator<Vertex> tableVertex = bufferG.V(table.id()).outE(EDGE_LABEL_DATAFLOW_WITH_PROCESS).otherV();
        if(!tableVertex.hasNext()){
            table.addEdge(EDGE_LABEL_DATAFLOW_WITH_PROCESS,process);
        }

    }
    /**
     * Creates a new vertex if it does not exist
     * @param graphContext - graph Collection that contains vertices and edges to be stored
     */
    @Override
    public void addEntity(Set<GraphContext> graphContext){

        GraphTraversalSource g =  lineageGraph.traversal();
        graphContext.forEach(entry -> {
            try {
                addVerticesAndRelationship(g, entry);
            } catch (JanusConnectorException e) {
                log.error("An exception happened when trying to create vertices and relationships in BufferGraph. The error is", e);
            }
        });

    }


    private void addVerticesAndRelationship(GraphTraversalSource g, GraphContext nodeToNode)  throws JanusConnectorException{
        LineageEntity fromEntity = nodeToNode.getFromVertex();
        LineageEntity toEntity = nodeToNode.getToVertex();

        Vertex vertexFrom = addVertex(g,fromEntity);
        Vertex vertexTo = addVertex(g,toEntity);

        if(vertexFrom != null && vertexTo != null){
            addRelationship(nodeToNode.getRelationshipGuid(),nodeToNode.getRelationshipType(),vertexFrom,vertexTo);
        }
    }

    /**
     * Creates a new vertex in the graph.
     * @param g - Graph traversal object
     * @param lineageEntity - Entity to be created
     */
    private  Vertex addVertex(GraphTraversalSource g,LineageEntity lineageEntity) throws JanusConnectorException {


        //TODO test the updated queries
//        Iterator<Vertex> vertexIt = g.V()
//                                     .has(PROPERTY_KEY_ENTITY_GUID, lineageEntity.getGuid())
//                                     .has(PROPERTY_KEY_ENTITY_VERSION,lineageEntity.getVersion());
//        Vertex vertex;

        Iterator<Vertex> vertexIt = g.V()
                .has(PROPERTY_KEY_ENTITY_GUID, lineageEntity.getGuid());
        Vertex vertex;
        if(!vertexIt.hasNext()){

//        Vertex vertex;
            vertex = g.addV(lineageEntity.getTypeDefName()).next();
            addPropertiesToVertex(g,vertex,lineageEntity);
            g.tx().commit();
            return vertex;

        }
        else {
            vertex = vertexIt.next();
            if (log.isDebugEnabled()) {
                log.debug("found existing vertex {} when trying to add it in bufferGraph", vertex);
            }
            g.tx().rollback();
            return vertex;
        }
    }

    /**
     * Creates new Relationships and it's properties in bufferGraph and mainGraph related to Lineage.
     *
     */
    private void addRelationship(String relationshipGuid,String relationshipType,Vertex fromVertex,Vertex toVertex) throws JanusConnectorException{
        String methodName = "addRelationship";
        GraphTraversalSource g = lineageGraph.traversal();

        if (relationshipType == null) {
            log.debug("Relationship type name is missing, relationship cannot be created");
            throwException(JanusConnectorErrorCode.RELATIONSHIP_TYPE_NAME_NOT_KNOWN,relationshipGuid,methodName);
        }

        try {
            Iterator<Edge> edgeIt = g.E().has(PROPERTY_KEY_RELATIONSHIP_GUID, relationshipGuid);
            if (edgeIt.hasNext()) {
                g.tx().rollback();
                log.debug("found existing edge {} when trying to add a relationship", edgeIt);
                return;
            }

            fromVertex.addEdge(relationshipType, toVertex).property(PROPERTY_KEY_RELATIONSHIP_GUID,relationshipGuid);
            g.tx().commit();
        }
        catch (Exception e){
            g.tx().rollback();
            log.debug("Something went wrong when trying to create a relationship on the graph check the exception: {}",e);

        }

    }

    /**
     * Add properties to vertex.
     * @param g - Graph traversal object
     * @param lineageEntity - LineageEntity object to be created
     */
    private void addPropertiesToVertex(GraphTraversalSource g,Vertex vertex, LineageEntity lineageEntity) throws JanusConnectorException{
        final String methodName = "addPropertiesToVertex";

        try {
            graphVertexMapper.mapEntityToVertex(lineageEntity, vertex);
        }catch (Exception e) {
            log.error("Caught exception from entity mapper {}", e.getMessage());
            g.tx().rollback();
            throwException(JanusConnectorErrorCode.ENTITY_NOT_CREATED,lineageEntity.getGuid(),methodName);
        }
    }

    /**
     * Updates the properties of a vertex
     * @param lineageEntity - LineageEntity object that has the updated values
     */
    @Override
    public void updateEntity(LineageEntity lineageEntity){
        GraphTraversalSource g = lineageGraph.traversal();

        Iterator<Vertex> vertex = g.V().has(PROPERTY_KEY_ENTITY_GUID,lineageEntity.getGuid());
        if(!vertex.hasNext()){
            log.debug("when trying to update, vertex with guid {} was not found  ", lineageEntity.getGuid());
            g.tx().rollback();
            return;
        }
        addOrUpdatePropertiesVertex(g,vertex.next(),lineageEntity);
    }

    /**
     * Updates the properties of an edge
     * @param lineageRelationship - lineageRelationship object that has the updated values
     */
    @Override
    public void updateRelationship(LineageRelationship lineageRelationship){
        GraphTraversalSource g = lineageGraph.traversal();

        Iterator<Edge> edge = g.E().has(PROPERTY_KEY_RELATIONSHIP_GUID,lineageRelationship.getGuid());
        if(!edge.hasNext()){
            log.debug("when trying to update, edge with guid {} was not found", lineageRelationship.getGuid());
            g.tx().rollback();
            return;
        }
        addOrUpdatePropertiesEdge(g,lineageRelationship);
    }

    @Override
    public void deleteEntity(String guid,String version){
        GraphTraversalSource g = lineageGraph.traversal();

        Iterator<Vertex> vertex = checkIfVertexExist(g,guid,version);
        //TODO add check when we will have classifications to delete classifications first
        if(!vertex.hasNext()){
            g.tx().rollback();
            log.debug("Vertex with guid did not delete {}",guid);
            return;
        }

        g.V().has(PROPERTY_KEY_ENTITY_GUID,guid).drop();
        g.tx().commit();
        log.debug("Vertex with guid {} deleted",guid);
    }

    /**
     * Adds or updates properties of a vertex.
     * @param g - Graph traversal object
     * @param vertex - vertex to be updated
     * @param lineageEntity - LineageEntity object that has the updates values
     */
    private void addOrUpdatePropertiesVertex(GraphTraversalSource g,Vertex vertex,LineageEntity lineageEntity){

        Map<String, Object> properties  = lineageEntity.getProperties().entrySet().stream().collect(Collectors.toMap(
                e -> PROPERTY_KEY_PREFIX_ELEMENT+PROPERTY_KEY_PREFIX_INSTANCE_PROPERTY+e.getKey(),
                Map.Entry::getValue));

        properties.put(PROPERTY_KEY_ENTITY_GUID,lineageEntity.getGuid());
        properties.put(PROPERTY_KEY_ENTITY_CREATE_TIME,lineageEntity.getCreateTime());
        properties.put(PROPERTY_KEY_ENTITY_CREATED_BY,lineageEntity.getCreatedBy());
        properties.put(PROPERTY_KEY_ENTITY_UPDATE_TIME,lineageEntity.getUpdateTime());
        properties.put(PROPERTY_KEY_ENTITY_UPDATED_BY,lineageEntity.getUpdatedBy());
        properties.put(PROPERTY_KEY_DISPLAY_NAME,lineageEntity.getTypeDefName());
        properties.put(PROPERTY_KEY_ENTITY_VERSION,lineageEntity.getVersion());
        properties.put(PROPERTY_KEY_METADATA_ID,lineageEntity.getMetadataCollectionId());

        try {
            g.inject(properties)
                    .unfold()
                    .as("properties")
                    .V(vertex.id())
                    .as("v")
                    .sideEffect(__.select("properties")
                            .unfold()
                            .as("kv")
                            .select("v")
                            .property(__.select("kv").by(Column.keys), __.select("kv").by(Column.values))) ;
            g.tx().commit();

            //TODO addcheck for type check in order to update(if it database or schema it needs extra checks)
//            MainGraphMapper mainGraphMapper  = new MainGraphMapper(bufferGraph,mainGraph);
//            mainGraphMapper.updateVertex(vertex,properties);
        }
        catch (Exception e){
            log.error("An exception happened during update of the properties with exception: {}",e);
            g.tx().rollback();
        }
    }

    /**
     * Adds or updates properties of an edge.
     * @param g - Graph traversal object
     * @param lineageRelationship - LineageEntity object that has the updates values
     */
    private void addOrUpdatePropertiesEdge(GraphTraversalSource g,LineageRelationship lineageRelationship){

        Map<String, Object> properties  = lineageRelationship.getProperties().entrySet().stream().collect(Collectors.toMap(
                e -> PROPERTY_KEY_PREFIX_ELEMENT+PROPERTY_KEY_PREFIX_INSTANCE_PROPERTY+e.getKey(),
                Map.Entry::getValue
        ));

        properties.put(PROPERTY_KEY_RELATIONSHIP_GUID,lineageRelationship.getGuid());
        properties.put(PROPERTY_KEY_RELATIONSHIP_CREATE_TIME,lineageRelationship.getCreateTime());
        properties.put(PROPERTY_KEY_RELATIONSHIP_CREATED_BY,lineageRelationship.getCreatedBy());
        properties.put(PROPERTY_KEY_RELATIONSHIP_UPDATE_TIME,lineageRelationship.getUpdateTime());
        properties.put(PROPERTY_KEY_RELATIONSHIP_UPDATED_BY,lineageRelationship.getUpdatedBy());
        properties.put(PROPERTY_KEY_RELATIONSHIP_DISPLAY_NAME,lineageRelationship.getTypeDefName());
        properties.put(PROPERTY_KEY_RELATIONSHIP_VERSION,lineageRelationship.getVersion());


        try {

            g.inject(properties)
                    .as("properties")
                    .V(lineageRelationship.getFirstEndGUID())
                    .outE()
                    .where(inV().hasId(lineageRelationship.getSecondEndGUID()))
                    .as("edge")
                    .sideEffect(__.select("properties")
                            .unfold()
                            .as("kv")
                            .select("edge")
                            .property(__.select("kv").by(Column.keys), __.select("kv").by(Column.values))) ;
            g.tx().commit();
        }
        catch (Exception e){
            log.debug("An exception happened during update of the properties with error: {}",e);
            g.tx().rollback();
        }

    }

    /**
     * Returns the vertex of the schema element in the output of a process.
     * @param endingVertex - The vertex that is being checked if it is the output schema element
     * @param g - Graph traversal object
     * @param startingVertex - The vertex of the input schema element
     * @return Return a vertex of the output schema element
     */
    private Vertex findPathForOutputAsset(Vertex endingVertex, GraphTraversalSource g,Vertex startingVertex)  {
        final String VERTEX = "vertex";
        //add null check for endingVertex
        try{
            Iterator<Vertex> end =  g.V(endingVertex.id())
                    .or(__.out(ATTRIBUTE_FOR_SCHEMA).out(ASSET_SCHEMA_TYPE)
                                    .has(PROPERTY_KEY_LABEL,DATA_FILE).store(VERTEX),
                            __.out(NESTED_SCHEMA_ATTRIBUTE).has(PROPERTY_KEY_LABEL,RELATIONAL_TABLE)
                                    .store(VERTEX)).select(VERTEX).unfold();

            if (!end.hasNext()) {
                List<Vertex> next = g.V(endingVertex.id()).both(LINEAGE_MAPPING).toList();
                Vertex nextVertex = null;
                for(Vertex vert: next){
                    if(vert.equals(startingVertex)){
                        continue;
                    }
                    nextVertex = vert;
                }
                return findPathForOutputAsset(nextVertex, g,endingVertex);
            }
            return endingVertex;
        }
        catch (Exception e){
            if (log.isDebugEnabled()) {
                log.debug("Vertex does not exist with guid {} and display name {}",startingVertex.id(),startingVertex.property(PROPERTY_KEY_DISPLAY_NAME).value());
            }
            return null;
        }
    }

    private Iterator<Vertex> checkIfVertexExist(GraphTraversalSource g, String guid, String version){

        return g.V().has(PROPERTY_KEY_ENTITY_GUID,guid).has(PROPERTY_KEY_ENTITY_VERSION,version);
    }

    private void throwException(JanusConnectorErrorCode errorCode,String guid,String methodName) throws JanusConnectorException {

        String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(guid, methodName,
                this.getClass().getName());

        throw new JanusConnectorException(this.getClass().getName(),
                methodName,
                errorMessage,
                errorCode.getSystemAction(),
                errorCode.getUserAction());
    }

    @Override
    public void disconnect() throws ConnectorCheckedException {
        this.lineageGraph.close();
        super.disconnect();
    }

    /**
     * {@inheritDoc}
     */
    public LineageResponse lineage(Scope scope, String guid, String displayNameMustContain, boolean includeProcesses) throws OpenLineageException {
        String methodName = "lineage";

        GraphTraversalSource g = lineageGraph.traversal();
        try {
            g.V().has(PROPERTY_KEY_ENTITY_NODE_ID, guid).next();
        } catch (NoSuchElementException e) {
            log.debug("Requested element was not found", e);
            OpenLineageServerErrorCode errorCode = OpenLineageServerErrorCode.NODE_NOT_FOUND;
            throw new OpenLineageException(errorCode.getHTTPErrorCode(),
                    this.getClass().getName(),
                    methodName,
                    errorCode.getFormattedErrorMessage(),
                    errorCode.getSystemAction(),
                    errorCode.getUserAction());
        }


        List<String> edgeLabels = new ArrayList<>();
        edgeLabels.add(EDGE_LABEL_SEMANTIC);
        edgeLabels.add(includeProcesses ? EDGE_LABEL_DATAFLOW_WITH_PROCESS : EDGE_LABEL_DATAFLOW_WITHOUT_PROCESS);

        LineageVerticesAndEdges lineageVerticesAndEdges = null;

        switch (scope) {
            case SOURCE_AND_DESTINATION:
                lineageVerticesAndEdges = helper.sourceAndDestination(guid, edgeLabels.toArray(new String[edgeLabels.size()]));
                break;
            case END_TO_END:
                lineageVerticesAndEdges = helper.endToEnd(guid, edgeLabels.toArray(new String[edgeLabels.size()]));
                break;
            case ULTIMATE_SOURCE:
                lineageVerticesAndEdges = helper.ultimateSource(guid, edgeLabels.toArray(new String[edgeLabels.size()]));
                break;
            case ULTIMATE_DESTINATION:
                lineageVerticesAndEdges = helper.ultimateDestination(guid, edgeLabels.toArray(new String[edgeLabels.size()]));
                break;
            case GLOSSARY:
                lineageVerticesAndEdges = helper.glossary(guid);
                break;
        }
        if (!displayNameMustContain.isEmpty())
            helper.filterDisplayName(lineageVerticesAndEdges, displayNameMustContain);
        return new LineageResponse(lineageVerticesAndEdges);
    }


}

