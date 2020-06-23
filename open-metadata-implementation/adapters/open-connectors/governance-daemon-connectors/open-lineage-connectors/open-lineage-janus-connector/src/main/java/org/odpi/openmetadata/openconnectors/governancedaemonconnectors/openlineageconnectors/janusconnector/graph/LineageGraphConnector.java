/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.graph;

import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__;
import org.apache.tinkerpop.gremlin.structure.Column;
import org.apache.tinkerpop.gremlin.structure.Edge;
import org.apache.tinkerpop.gremlin.structure.Graph;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.odpi.openmetadata.accessservices.assetlineage.model.GraphContext;
import org.odpi.openmetadata.accessservices.assetlineage.model.LineageEntity;
import org.odpi.openmetadata.accessservices.assetlineage.model.LineageRelationship;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.governanceservers.openlineage.ffdc.OpenLineageException;
import org.odpi.openmetadata.governanceservers.openlineage.ffdc.OpenLineageServerErrorCode;
import org.odpi.openmetadata.governanceservers.openlineage.graph.LineageGraphConnectorBase;
import org.odpi.openmetadata.governanceservers.openlineage.model.LineageVerticesAndEdges;
import org.odpi.openmetadata.governanceservers.openlineage.model.Scope;
import org.odpi.openmetadata.governanceservers.openlineage.responses.LineageResponse;
import org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.factory.GraphGremlinBase;
import org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.factory.JanusGraphEmbedded;
import org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.factory.JanusGraphRemote;
import org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.model.JanusConnectorErrorCode;
import org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.model.ffdc.JanusConnectorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__.*;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.Constants.*;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.GraphConstants.*;

public class LineageGraphConnector extends LineageGraphConnectorBase {

    private static final Logger log = LoggerFactory.getLogger(LineageGraphConnector.class);
    private GraphGremlinBase lineageGraph;
    private LineageGraphConnectorHelper helper;
    private GraphTraversalSource g;
    /**
     * Instantiates the graph based on the configuration passed.
     */
    public void initializeGraphDB() throws OpenLineageException {

        try {

            if (connectionProperties.getConnectorType().getConnectorProviderClassName().equals(LineageGraphConnectorProvider.class.getName())) {
                lineageGraph = new JanusGraphEmbedded(connectionProperties);
            }

            if (connectionProperties.getConnectorType().getConnectorProviderClassName().equals(LineageGraphRemoteConnectorProvider.class.getName())) {
                lineageGraph = new JanusGraphRemote(connectionProperties);
            }

            this.g = lineageGraph.openGraph();
            this.helper = new LineageGraphConnectorHelper(g);

            System.out.println(g.V().count().next());

        } catch (Exception e){
            log.debug("exception ",e);

        }
//
//        catch (JanusConnectorException error) {
//            log.error("The Lineage graph could not be initialized due to an error", error);
//            throw new OpenLineageException(500,
//                    error.getReportingClassName(),
//                    error.getReportingActionDescription(),
//                    error.getReportedErrorMessage(),
//                    error.getReportedSystemAction(),
//                    error.getReportedUserAction()
//            );
//        }

    }

    @Override
    public void disconnect() throws ConnectorCheckedException {
        try {
            lineageGraph.closeGraph();
            super.disconnect();
        } catch (Exception e) {
            log.error("Exception while closing lineage graph: ",e);
            //TODO: throw ConnectorCheckedException
        }

    }

    @Override
    public void schedulerTask() {

        log.debug("g.V().count()="+ g.V().count().next());
        try {
            List<Vertex> vertices = g.V().has(PROPERTY_KEY_LABEL, PROCESS).toList();
            List<String> guidList = vertices.stream().map(v ->  g.V(v.id()).elementMap(PROPERTY_KEY_ENTITY_GUID)
                                                                   .toList().get(0)
                                                                   .get(PROPERTY_KEY_ENTITY_GUID).toString()).collect(Collectors.toList());

            guidList.forEach(process -> findInputColumns(g,process));
            if(lineageGraph.isSupportingTransactions()) {
                g.tx().commit();
            }
        }catch (Exception e){
            log.error("Something went wrong when trying to map a process. The error is: ",e);
            if(lineageGraph.isSupportingTransactions()) {
                g.tx().rollback();
            }
        }
    }

    /**
     * Finds the paths to the input columns from all the processes in the graph.
     *
     * @param g    - Graph traversal object
     * @param guid - The unique identifier of a Process
     */
    private void findInputColumns(GraphTraversalSource g,String guid) {
        List<Vertex> inputPathsForColumns = g.V().has(PROPERTY_KEY_ENTITY_GUID, guid).out(PROCESS_PORT).out(PORT_DELEGATION)
                                                 .has(PORT_IMPLEMENTATION, PROPERTY_NAME_PORT_TYPE, "INPUT_PORT")
                                                 .out(PORT_SCHEMA).in(ATTRIBUTE_FOR_SCHEMA).out(LINEAGE_MAPPING)
                                                 .or(__.out(ATTRIBUTE_FOR_SCHEMA).out(ASSET_SCHEMA_TYPE).has(PROPERTY_KEY_LABEL,DATA_FILE),
                                                    __.out(NESTED_SCHEMA_ATTRIBUTE).has(PROPERTY_KEY_LABEL,RELATIONAL_TABLE)).toList();

        Vertex process = g.V().has(PROPERTY_KEY_ENTITY_GUID, guid).next();
        inputPathsForColumns.forEach(columnIn -> findOutputColumn(g, columnIn, process));
    }

    /**
     * Finds the output column of a Process based on the input.
     *
     * @param g        - Graph traversal object
     * @param columnIn - THe vertex of the schema element before processing.
     * @param process  - The vertex of the process.
     */
    private void findOutputColumn(GraphTraversalSource g,Vertex columnIn,Vertex process){
        List<Vertex> schemaElementVertex = g.V()
                .has(PROPERTY_KEY_ENTITY_GUID, g.V(columnIn.id()).elementMap(PROPERTY_KEY_ENTITY_GUID).toList().get(0).get(PROPERTY_KEY_ENTITY_GUID))
                .in(LINEAGE_MAPPING)
                .toList();

        Vertex vertexToStart;
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
     *
     * @param g                   - Graph traversal object
     * @param schemaElementVertex - THe vertex of the column before processing.
     * @param process             - The vertex of the process.
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
                    .has(PROPERTY_KEY_ENTITY_GUID, g.V(process.id()).elementMap(PROPERTY_KEY_ENTITY_GUID).toList().get(0).get(PROPERTY_KEY_ENTITY_GUID)).toList();

            if (!initialProcess.isEmpty()) {
                vertexToStart = v;
                break;
            }
        }
        return vertexToStart;
    }

    /**
     * Initiates the process of copying the input and output vertices to the MainGraph.
     *
     * @param columnIn  - The vertex of the input schema element
     * @param columnOut - THe vertex of the output schema element
     * @param process   - The vertex of the process.
     */
    private void moveColumnProcessColumn(Vertex columnIn,Vertex columnOut,Vertex process){
        if (columnOut != null) {
            String columnOutGuid = g.V(columnOut.id()).elementMap(PROPERTY_KEY_ENTITY_GUID).toList().get(0).get(PROPERTY_KEY_ENTITY_GUID).toString();
            String columnInGuid = g.V(columnIn.id()).elementMap(PROPERTY_KEY_ENTITY_GUID).toList().get(0).get(PROPERTY_KEY_ENTITY_GUID).toString();
            if (!columnOutGuid.isEmpty() && !columnInGuid.isEmpty()) {
                addNodesAndEdgesForQuerying(columnIn, columnOut, process);
            }
        }
    }

    /**
     * Add nodes and edges that are going to be used for lineage UI
     *
     * @param columnIn  - The vertex of the input schema element
     * @param columnOut - THe vertex of the output schema element
     * @param process   - The vertex of the process.
     */
    private void addNodesAndEdgesForQuerying(Vertex columnIn,Vertex columnOut,Vertex process){

        final String processGuid = g.V(process.id()).elementMap(PROPERTY_KEY_ENTITY_GUID).toList().get(0).get(PROPERTY_KEY_ENTITY_GUID).toString();
        final String processName =  g.V(process.id()).elementMap(PROPERTY_KEY_INSTANCEPROP_DISPLAY_NAME).toList().get(0).get(PROPERTY_KEY_INSTANCEPROP_DISPLAY_NAME).toString();

        Iterator<Vertex> t = g.V(columnIn.id()).outE(EDGE_LABEL_DATAFLOW_WITH_PROCESS).inV().has("processGuid",processGuid);

        if(!t.hasNext()) {
            Vertex subProcess = g.addV(NODE_LABEL_SUB_PROCESS)
                                 .property(PROPERTY_KEY_ENTITY_NODE_ID, UUID.randomUUID().toString())
                                 .property("processGuid", processGuid)
                                 .property(PROPERTY_KEY_DISPLAY_NAME, processName)
                                 .next();

            g.V(columnIn.id()).addE(EDGE_LABEL_DATAFLOW_WITH_PROCESS).to(g.V(subProcess.id())).next();
            g.V(subProcess.id()).addE(EDGE_LABEL_DATAFLOW_WITH_PROCESS).to(g.V(columnOut.id())).next();
            g.V(subProcess.id()).addE(EDGE_LABEL_INCLUDED_IN).to(g.V(process.id())).next();

            addTableToProcessEdge(g,columnIn, process);
            addTableToProcessEdge(g,columnOut, process);

            if(lineageGraph.isSupportingTransactions()) {
                g.tx().commit();
            }
        }
    }

        private void addTableToProcessEdge(GraphTraversalSource g,Vertex column, Vertex process){

        Vertex table = getTable(column);

        Iterator<Vertex> tableVertex = g.V(table.id()).outE(EDGE_LABEL_DATAFLOW_WITH_PROCESS).otherV();
        if(!tableVertex.hasNext()){
            g.V(table.id()).addE(EDGE_LABEL_DATAFLOW_WITH_PROCESS).to(g.V(process.id())).next();

//            table.addEdge(EDGE_LABEL_DATAFLOW_WITH_PROCESS,process);
        }
    }

    private Vertex getTable(Vertex asset){
        Iterator<Vertex> table = g.V().has(PROPERTY_KEY_ENTITY_GUID,g.V(asset.id()).elementMap(PROPERTY_KEY_ENTITY_GUID).toList().get(0).get(PROPERTY_KEY_ENTITY_GUID))
                .emit().repeat(bothE().otherV().simplePath()).times(2).or(hasLabel(RELATIONAL_TABLE),hasLabel(DATA_FILE));

        if (!table.hasNext()){
            return null;
        }

        return table.next();
    }

    /**
     * Creates vertices and the relationships between them
     *
     * @param graphContext - graph Collection that contains vertices and edges to be stored
     */
    @Override
    public void storeToGraph(Set<GraphContext> graphContext){

        graphContext.forEach(entry -> {
            try {
                long start=System.currentTimeMillis();
                addVerticesAndRelationship(entry);
                log.debug(String.format("addVerticesAndRelationship executed in: %d", System.currentTimeMillis() - start));
                log.debug("graphContext.size: "+graphContext.size());
            } catch (Exception e) {
                log.error("An exception happened when trying to create vertices and relationships in LineageGraph. The error is", e);
                if(lineageGraph.isSupportingTransactions()) {
                    g.tx().rollback();
                }
            }
        });
    }

    private void addVerticesAndRelationship(GraphContext graphContext){
        LineageEntity fromEntity = graphContext.getFromVertex();
        LineageEntity toEntity = graphContext.getToVertex();

        upsertToGraph(fromEntity,toEntity,graphContext.getRelationshipType(),graphContext.getRelationshipGuid());

    }

    private void upsertToGraph(LineageEntity fromEntity,LineageEntity toEntity,String relationshipLabel,String relationshipGuid){

        Vertex from =   g.V().has(PROPERTY_KEY_ENTITY_GUID, fromEntity.getGuid())
                .fold()
                .coalesce(unfold(),
                        addV(fromEntity.getTypeDefName())
                                .property(PROPERTY_KEY_ENTITY_GUID,fromEntity.getGuid()))
                .next();

        Vertex to =  g.V().has(PROPERTY_KEY_ENTITY_GUID,toEntity.getGuid())
                .fold()
                .coalesce(unfold(),
                        addV(toEntity.getTypeDefName())
                                .property(PROPERTY_KEY_ENTITY_GUID,toEntity.getGuid()))
                .next();


        g.V(from.id()).as("from")
                .V(to.id())
                .coalesce(inE(relationshipLabel).where(outV().as("from")),
                        addE(relationshipLabel).from("from")).property(PROPERTY_KEY_RELATIONSHIP_GUID,relationshipGuid).next();
        addOrUpdatePropertiesVertex(from,fromEntity);
        addOrUpdatePropertiesVertex(to,toEntity);
        //TODO add relationship properties -> meaning add relationship properties on AssetLineage omas event
        if(lineageGraph.isSupportingTransactions()){
            g.tx().commit();
        }
    }

    /**
     * Adds or updates properties of a vertex.
     * @param vertex - vertex to be updated
     * @param lineageEntity - LineageEntity object that has the updates values
     */
    private void addOrUpdatePropertiesVertex(Vertex vertex,LineageEntity lineageEntity){

        Map<String, Object> properties  = lineageEntity.getProperties().entrySet().stream().collect(Collectors.toMap(
                e -> PROPERTY_KEY_PREFIX_ELEMENT+PROPERTY_KEY_PREFIX_INSTANCE_PROPERTY+e.getKey(),
                Map.Entry::getValue));

        properties.values().remove(null);
        properties.computeIfAbsent(PROPERTY_KEY_ENTITY_CREATE_TIME,val -> lineageEntity.getCreateTime());
        properties.computeIfAbsent(PROPERTY_KEY_ENTITY_CREATED_BY,val -> lineageEntity.getCreatedBy());
        properties.computeIfAbsent(PROPERTY_KEY_ENTITY_UPDATE_TIME,val -> lineageEntity.getUpdateTime());
        properties.computeIfAbsent(PROPERTY_KEY_ENTITY_UPDATED_BY,val -> lineageEntity.getUpdatedBy());
        properties.computeIfAbsent(PROPERTY_KEY_LABEL,val -> lineageEntity.getTypeDefName());
        properties.computeIfAbsent(PROPERTY_KEY_ENTITY_VERSION,val -> lineageEntity.getVersion());
        properties.computeIfAbsent(PROPERTY_KEY_METADATA_ID, val -> lineageEntity.getMetadataCollectionId());

        g.inject(properties)
                .unfold()
                .as("properties")
                .V(vertex.id())
                .as("v")
                .sideEffect(__.select("properties")
                        .unfold()
                        .as("kv")
                        .select("v")
                        .property(__.select("kv").by(Column.keys), __.select("kv").by(Column.values))).iterate();
    }

    /**
     * Updates the properties of a vertex
     *
     * @param lineageEntity - LineageEntity object that has the updated values
     */
    @Override
    public void updateEntity(LineageEntity lineageEntity){
        Iterator<Vertex> vertex = g.V().has(PROPERTY_KEY_ENTITY_GUID,lineageEntity.getGuid());
        if(!vertex.hasNext()){
            log.debug("when trying to update, vertex with guid {} was not found  ", lineageEntity.getGuid());
            if(lineageGraph.isSupportingTransactions()) {
                g.tx().rollback();
            }
            return;
        }

        try {
            addOrUpdatePropertiesVertex(vertex.next(), lineageEntity);
            if(lineageGraph.isSupportingTransactions()) {
                g.tx().commit();
            }
        }catch (Exception e){
            log.error("An exception happened during update of the properties with exception: ",e);
            if(lineageGraph.isSupportingTransactions()) {
                g.tx().rollback();
            }
        }
    }

    /**
     * Create or update the relationship between two edges
     * In case the vertexes are not created, they are firstly created
     *
     * @param lineageRelationship relationship to be updated or created
     */
    @Override
    public void upsertRelationship(LineageRelationship lineageRelationship) {

        LineageEntity firstEnd = lineageRelationship.getFirstEntity();
        LineageEntity secondEnd = lineageRelationship.getSecondEntity();

        upsertToGraph(firstEnd,secondEnd,lineageRelationship.getTypeDefName(),lineageRelationship.getGuid());

        addOrUpdatePropertiesEdge(lineageRelationship);
    }

    /**
     * Updates the properties of an edge
     *
     * @param lineageRelationship - lineageRelationship object that has the updated values
     */
    @Override
    public void updateRelationship(LineageRelationship lineageRelationship){
        Iterator<Edge> edge = g.E().has(PROPERTY_KEY_RELATIONSHIP_GUID,lineageRelationship.getGuid());
        if(!edge.hasNext()){
            log.debug("when trying to update, edge with guid {} was not found", lineageRelationship.getGuid());
            if(lineageGraph.isSupportingTransactions()) {
                g.tx().rollback();
            }
            return;
        }
        addOrUpdatePropertiesEdge(lineageRelationship);
    }

    /**
     * Updates the classification of a vertex
     * @param classificationContext - LineageEntity object that has the updated values
     */
    @Override
    public void updateClassification(Map<String, Set<GraphContext>> classificationContext){
        if(classificationContext.entrySet().size() != 1){
            log.debug("Unable to update classifications for no or multiple entities.");
            return;
        }

        Set<GraphContext> graphContexts = classificationContext.entrySet().iterator().next().getValue();
        for (GraphContext graphContext : graphContexts) {
            String classificationGuid = graphContext.getToVertex().getGuid();
            Iterator<Vertex> vertexIterator = g.V().has(PROPERTY_KEY_ENTITY_GUID, classificationGuid);
            if (!vertexIterator.hasNext()) {
                log.debug("Classification with guid {} not found", classificationGuid);
                if(lineageGraph.isSupportingTransactions()) {
                    g.tx().rollback();
                }
                continue;
            }

            Vertex storedClassification = vertexIterator.next();
            long storedClassificationVersion = (long) g.V(storedClassification.id()).elementMap(PROPERTY_KEY_ENTITY_VERSION)
                    .toList().get(0).get(PROPERTY_KEY_ENTITY_VERSION);
//            long storedClassificationVersion = (long) storedClassification.property(PROPERTY_KEY_ENTITY_VERSION).value();
            if (storedClassificationVersion < graphContext.getToVertex().getVersion()) {
                addOrUpdatePropertiesVertex(storedClassification, graphContext.getToVertex());
                break;
            }
        }
    }

    /**
     * Deletes a classification of a vertex
     *
     * @param entityGuid entity guid
     * @param classificationContext - any remaining classifications, empty map if none
     */
    @Override
    public void deleteClassification(String entityGuid, Map<String, Set<GraphContext>> classificationContext){
        if(classificationContext.entrySet().size() > 1){
            log.debug("Unable to delete classifications for multiple entities.");
            return;
        }

        Set<GraphContext> remainingClassifications = classificationContext.entrySet().iterator().next().getValue();

        Graph entityAndClassificationsGraph = (Graph) g.V().has(PROPERTY_KEY_ENTITY_GUID, entityGuid).bothE(EDGE_LABEL_CLASSIFICATION)
                .subgraph("s").cap("s").next();

        Iterator<Edge> edges = entityAndClassificationsGraph.edges();

        while(edges.hasNext()){
            Edge edge = edges.next();
//            String storedClassificationGuid = (String)edge.inVertex().property(PROPERTY_KEY_ENTITY_GUID).value();
            String storedClassificationGuid = (String) g.E(edge.id()).inV().elementMap(PROPERTY_KEY_ENTITY_GUID).toList().get(0).get(PROPERTY_KEY_ENTITY_GUID);

            boolean classificationExists =  remainingClassifications.stream()
                    .anyMatch(gc -> gc.getToVertex().getGuid().equals(storedClassificationGuid));
            if(!classificationExists){
                g.V().has(PROPERTY_KEY_ENTITY_GUID,storedClassificationGuid).drop();
                g.E(edge.id()).drop();
                if(lineageGraph.isSupportingTransactions()) {
                    g.tx().commit();
                }
                break;
            }
        }
    }

    @Override
    public void deleteEntity(String guid,Object version){
        Iterator<Vertex> vertex = checkIfVertexExist(guid,version);
        //TODO add check when we will have classifications to delete classifications first
        if(!vertex.hasNext()){
            if(lineageGraph.isSupportingTransactions()) {
                g.tx().rollback();
            }
            log.debug("Vertex with guid did not delete {}",guid);
            return;
        }

        g.V().has(PROPERTY_KEY_ENTITY_GUID,guid).drop();
        if(lineageGraph.isSupportingTransactions()) {
            g.tx().commit();
        }
        log.debug("Vertex with guid {} deleted",guid);
    }

    @Override
    public void deleteRelationship(String guid){
        Iterator<Edge> edge = g.E().has(PROPERTY_KEY_RELATIONSHIP_GUID,guid);
        if(!edge.hasNext()){
            if(lineageGraph.isSupportingTransactions()) {
                g.tx().rollback();
            }
            log.debug("Edge with guid did not delete {}",guid);
            return;
        }

        g.E(edge.next().id()).drop();
        if(lineageGraph.isSupportingTransactions()) {
            g.tx().commit();
        }
        log.debug("Edge with guid {} deleted",guid);
    }

    /**
     * Adds or updates properties of an edge.
     * @param lineageRelationship - LineageEntity object that has the updates values
     */
    private void addOrUpdatePropertiesEdge(LineageRelationship lineageRelationship){

        Map<String, Object> properties  = lineageRelationship.getProperties().entrySet().stream().collect(Collectors.toMap(
                e -> PROPERTY_KEY_PREFIX_ELEMENT+PROPERTY_KEY_PREFIX_INSTANCE_PROPERTY+e.getKey(),
                Map.Entry::getValue
        ));

        properties.values().remove(null);
        properties.computeIfAbsent(PROPERTY_KEY_ENTITY_CREATE_TIME,val -> lineageRelationship.getCreateTime());
        properties.computeIfAbsent(PROPERTY_KEY_ENTITY_CREATED_BY,val -> lineageRelationship.getCreatedBy());
        properties.computeIfAbsent(PROPERTY_KEY_ENTITY_UPDATE_TIME,val -> lineageRelationship.getUpdateTime());
        properties.computeIfAbsent(PROPERTY_KEY_ENTITY_UPDATED_BY,val -> lineageRelationship.getUpdatedBy());
        properties.computeIfAbsent(PROPERTY_KEY_LABEL,val -> lineageRelationship.getTypeDefName());
        properties.computeIfAbsent(PROPERTY_KEY_ENTITY_VERSION,val -> lineageRelationship.getVersion());
        properties.computeIfAbsent(PROPERTY_KEY_METADATA_ID, val ->lineageRelationship.getMetadataCollectionId());

        try {

            g.inject(properties)
                    .as("properties")
                    .V(lineageRelationship.getFirstEntity().getGuid())
                    .outE()
                    .where(inV().hasId(lineageRelationship.getSecondEntity().getGuid()))
                    .as("edge")
                    .sideEffect(__.select("properties")
                            .unfold()
                            .as("kv")
                            .select("edge")
                            .property(__.select("kv").by(Column.keys), __.select("kv").by(Column.values))).iterate();
            if(lineageGraph.isSupportingTransactions()) {
                g.tx().commit();
            }
        }
        catch (Exception e){
            log.debug("An exception happened during update of the properties with error:",e);
            if(lineageGraph.isSupportingTransactions()) {
                g.tx().rollback();
            }
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
        if(endingVertex == null) {return null;}

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

    private Iterator<Vertex> checkIfVertexExist(String guid, Object version){

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

    /**
     * {@inheritDoc}
     */
    public LineageResponse lineage(Scope scope, String guid, String displayNameMustContain, boolean includeProcesses) throws OpenLineageException {
        String methodName = "lineage";
        try {
            g.V().has(PROPERTY_KEY_ENTITY_GUID, guid).next();
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

        LineageVerticesAndEdges lineageVerticesAndEdges = null;

        switch (scope) {
            case SOURCE_AND_DESTINATION:
                lineageVerticesAndEdges = helper.sourceAndDestination(guid, includeProcesses);
                break;
            case END_TO_END:
                lineageVerticesAndEdges = helper.endToEnd(guid, includeProcesses, EDGE_LABEL_SEMANTIC_ASSIGNMENT,
                        EDGE_LABEL_DATAFLOW_WITH_PROCESS, EDGE_LABEL_CLASSIFICATION);
                break;
            case ULTIMATE_SOURCE:
                lineageVerticesAndEdges = helper.ultimateSource(guid, includeProcesses);
                break;
            case ULTIMATE_DESTINATION:
                lineageVerticesAndEdges = helper.ultimateDestination(guid, includeProcesses);
                break;
            case GLOSSARY:
                lineageVerticesAndEdges = helper.glossary(guid, includeProcesses);
                break;
        }
        if (!displayNameMustContain.isEmpty())
            helper.filterDisplayName(lineageVerticesAndEdges, displayNameMustContain);
        return new LineageResponse(lineageVerticesAndEdges);
    }


}

