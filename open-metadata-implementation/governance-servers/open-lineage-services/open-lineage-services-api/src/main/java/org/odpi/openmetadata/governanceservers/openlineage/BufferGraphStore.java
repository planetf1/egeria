/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.governanceservers.openlineage;

import org.odpi.openmetadata.accessservices.assetlineage.LineageEntity;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.governanceservers.openlineage.model.Scope;
import org.odpi.openmetadata.governanceservers.openlineage.model.View;
import org.odpi.openmetadata.accessservices.assetlineage.model.event.LineageEvent;

public interface BufferGraphStore extends OpenLineageGraphStore{

    /**
     * Process the serialized  information view event
     *
     * @param lineageEvent event
     */
    void addEntity(LineageEvent lineageEvent);

    /**
     * Task that the scheduler performs based on the interval
     *
     */
    void schedulerTask();

    void setMainGraphConnector(OpenLineageGraphStore mainGraphConnector);



}
