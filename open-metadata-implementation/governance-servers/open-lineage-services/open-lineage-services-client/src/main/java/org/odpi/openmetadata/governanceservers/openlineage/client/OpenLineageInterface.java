/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.governanceservers.openlineage.client;

import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.governanceservers.openlineage.model.GraphName;
import org.odpi.openmetadata.governanceservers.openlineage.model.LineageVerticesAndEdges;
import org.odpi.openmetadata.governanceservers.openlineage.model.Scope;
import org.odpi.openmetadata.governanceservers.openlineage.model.View;

import java.security.InvalidParameterException;

public interface OpenLineageInterface {

    /**
     * Returns the graph that the user will initially see when querying lineage. In the future, this method will be
     * extended to condense large paths to prevent cluttering of the users screen. The user will be able to extended
     * the condensed path by querying a different method.
     *
     * @param userId calling user.
     * @param graphName MAIN, BUFFER, MOCK, HISTORY.
     * @param scope ULTIMATE_SOURCE, ULTIMATE_DESTINATION, GLOSSARY.
     * @param view TABLE_VIEW, COLUMN_VIEW.
     * @param guid The guid of the node of which the lineage is queried of.
     * @return A subgraph containing all relevant paths, in graphSON format.
     * @throws InvalidParameterException one of the parameters is null or invalid
     */
    public LineageVerticesAndEdges lineage(String userId, GraphName graphName, Scope scope, View view, String guid)
            throws org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException, PropertyServerException;
}