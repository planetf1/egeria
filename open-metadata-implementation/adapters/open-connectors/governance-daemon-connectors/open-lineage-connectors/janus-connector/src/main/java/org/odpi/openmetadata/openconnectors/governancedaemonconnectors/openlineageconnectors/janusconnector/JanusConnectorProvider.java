package org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector;
/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
import org.odpi.openmetadata.frameworks.connectors.ConnectorProviderBase;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ConnectorType;
import org.odpi.openmetadata.governanceservers.openlineage.OpenLineageProviderBase;

public class JanusConnectorProvider extends OpenLineageProviderBase {

    static final String connectorTypeGUID = "e2f657d6-e5bd-11e9-81b4-2a2ae2dbcce4";
    static final String connectorTypeName = "Janus Graph Connector";
    static final String connectorTypeDescription = "Connector supports storing and retrieving entities for lineage from Janus Graph.";

    /**
     * Constructor used to initialize the ConnectorProviderBase with the Java class name of the specific
     * registry store implementation.
     */
    public JanusConnectorProvider() {
        super();
        Class connectorClass = JanusConnector.class;
        super.setConnectorClassName(connectorClass.getName());

        ConnectorType connectorType = new ConnectorType();
        connectorType.setType(ConnectorType.getConnectorTypeType());
        connectorType.setGUID(connectorTypeGUID);
        connectorType.setQualifiedName(connectorTypeName);
        connectorType.setDisplayName(connectorTypeName);
        connectorType.setDescription(connectorTypeDescription);
        connectorType.setConnectorProviderClassName(this.getClass().getName());

        super.connectorTypeBean = connectorType;
    }


}
