/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.governanceservers.openlineage.graph;

import org.odpi.openmetadata.accessservices.assetlineage.model.GraphContext;
import org.odpi.openmetadata.accessservices.assetlineage.model.LineageEntity;
import org.odpi.openmetadata.accessservices.assetlineage.model.LineageRelationship;
import org.odpi.openmetadata.frameworks.connectors.ConnectorBase;
import org.odpi.openmetadata.governanceservers.openlineage.ffdc.OpenLineageException;

import java.util.Set;

public abstract class LineageGraphConnectorBase extends ConnectorBase implements LineageGraph {

    @Override
    public abstract void initializeGraphDB() throws OpenLineageException;

    @Override
    public abstract void addEntity(Set<GraphContext> graphContext);

    @Override
    public abstract void updateEntity(LineageEntity lineageEntity);

    @Override
    public abstract void updateRelationship(LineageRelationship lineageRelationship);

    @Override
    public abstract void deleteEntity(String guid,String version);

}
