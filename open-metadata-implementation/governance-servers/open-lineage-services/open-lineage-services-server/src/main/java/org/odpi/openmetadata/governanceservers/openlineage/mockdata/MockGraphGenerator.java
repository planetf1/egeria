/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.governanceservers.openlineage.mockdata;

import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import static org.odpi.openmetadata.governanceservers.openlineage.admin.OpenLineageOperationalServices.mockGraph;
import static org.odpi.openmetadata.governanceservers.openlineage.util.GraphConstants.*;

/**
 * The Open Lineage Services MockGraphGenerator is meant for creating huge lineage data sets, either for performance
 * testing or demoing lineage with realistic data sizes. The user can specify how many tables there will be, columns per
 * table, number of ETL processes, and the number of glossary terms.
 */
public class MockGraphGenerator {

    private static final Logger log = LoggerFactory.getLogger(MockGraphGenerator.class);

    private int numberGlossaryTerms;
    private int numberTables;
    private int processesPerFlow;
    private int tablesPerFlow;
    private int columnsPerTable;
    private int numberProcesses;
    private int numberFlows;


    public MockGraphGenerator() {
        setProperties();
    }

    /**
     * The parameters for the graph that is to be generated are hardcoded for now.
     * A "flow" constitutes of a path between columns of different tables, connected together by process nodes.
     * I.E. columnnode1 -> processnode1 -> columnnode2 -> processnode2 -> columnnode3.
     * The length of this path is specified by the number of processes within the flow.
     */
    private void setProperties() {
        this.numberGlossaryTerms = 20;
        this.numberFlows = 1;
        this.processesPerFlow = 2;
        this.columnsPerTable = 2;

        this.tablesPerFlow = processesPerFlow + 1;
        this.numberTables = numberFlows * tablesPerFlow;
        this.numberProcesses = numberFlows * processesPerFlow;
    }

    public void generate() {
        try {
            generateVerbose();
            log.info("Generated mock graph");
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    /**
     * Generate the graph based on the parameters specified in setProperties().
     */
    private void generateVerbose() {
        List<Vertex> columnNodesPerTable;
        List<Vertex> glossaryNodes = new ArrayList<>();
        List<Vertex> hostNodes = new ArrayList<>();
        List<Vertex> tableNodes = new ArrayList<>();
        List<List<Vertex>> columnNodes = new ArrayList<>();

        GraphTraversalSource g = mockGraph.traversal();

        //Create all Glossary Term nodes
        for (int i = 0; i < numberGlossaryTerms; i++) {
            Vertex glossaryVertex = g.addV(NODE_LABEL_GLOSSARYTERM).next();
            glossaryVertex.property(PROPERTY_KEY_ENTITY_GUID, "g" + i);
            glossaryNodes.add(glossaryVertex);
        }

        //Create all Process nodes
        List<Vertex> processNodes = new ArrayList<>();
        for (int i = 0; i < numberProcesses; i++) {
            Vertex processVertex = g.addV(NODE_LABEL_PROCESS).next();
            processVertex.property(PROPERTY_KEY_ENTITY_GUID, "p" + i);
            processNodes.add(processVertex);
        }

        //Create all Table nodes and a Host node for each table.
        for (int j = 0; j < numberTables; j++) {
            Vertex tableVertex = g.addV(NODE_LABEL_TABLE).next();
            tableVertex.property(PROPERTY_KEY_ENTITY_GUID, "t" + j);
            tableNodes.add(tableVertex);

            Vertex hostVertex = g.addV(NODE_LABEL_HOST).next();
            hostVertex.property(PROPERTY_KEY_ENTITY_GUID, "h" + j);

            tableVertex.addEdge(EDGE_LABEL_INCLUDED_IN, hostVertex);
            hostNodes.add(hostVertex);

            //Create all Column nodes.
            columnNodesPerTable = new ArrayList<>();
            for (int i = 0; i < columnsPerTable; i++) {
                Vertex columnVertex = g.addV(NODE_LABEL_COLUMN).next();
                columnVertex.property(PROPERTY_KEY_ENTITY_GUID, "t" + j + "c" + i);
                columnVertex.addEdge(EDGE_LABEL_INCLUDED_IN, tableVertex);

                //Randomly connect Column nodes with Glossary Term nodes.
                if (numberGlossaryTerms != 0) {
                    int randomNum = ThreadLocalRandom.current().nextInt(0, numberGlossaryTerms);
                    Vertex glossaryNode = glossaryNodes.get(randomNum);
                    columnVertex.addEdge(EDGE_LABEL_SEMANTIC, glossaryNode);
                }
                columnNodesPerTable.add(columnVertex);
            }
            columnNodes.add(columnNodesPerTable);
        }

        //Create the lineage flows by connecting columns to processes and connecting processes to the columns of the next table.

        //For each flow
        for (int flowIndex = 0; flowIndex < numberFlows; flowIndex++) {

            //For each table in a flow
            for (int tableIndex = 0; tableIndex < tablesPerFlow - 1; tableIndex++) {

                final Vertex process = processNodes.get(flowIndex * processesPerFlow + tableIndex);

                final Vertex host1 = hostNodes.get(flowIndex * tablesPerFlow + tableIndex);
                final Vertex host2 = hostNodes.get(flowIndex * tablesPerFlow + tableIndex + 1);

                final Vertex table1 = tableNodes.get(flowIndex * tablesPerFlow + tableIndex);
                final Vertex table2 = tableNodes.get(flowIndex * tablesPerFlow + tableIndex + 1);

                final List<Vertex> columnsOfTable1 = columnNodes.get(flowIndex * tablesPerFlow + tableIndex);
                final List<Vertex> columnsOfTable2 = columnNodes.get(flowIndex * tablesPerFlow + tableIndex + 1);

                host1.addEdge(EDGE_LABEL_HOST_AND_PROCESS, process);
                table1.addEdge(EDGE_LABEL_TABLE_AND_PROCESS, process);

                process.addEdge(EDGE_LABEL_HOST_AND_PROCESS, host2);
                process.addEdge(EDGE_LABEL_TABLE_AND_PROCESS, table2);


                //For each column in a table
                for (int columnIndex = 0; columnIndex < columnsPerTable; columnIndex++) {

                    final Vertex column1 = columnsOfTable1.get(columnIndex);
                    final Vertex column2 = columnsOfTable2.get(columnIndex);

                    column1.addEdge(EDGE_LABEL_COLUMN_AND_PROCESS, process);
                    process.addEdge(EDGE_LABEL_COLUMN_AND_PROCESS, column2);
                }
            }
        }
        g.tx().commit();
    }

}