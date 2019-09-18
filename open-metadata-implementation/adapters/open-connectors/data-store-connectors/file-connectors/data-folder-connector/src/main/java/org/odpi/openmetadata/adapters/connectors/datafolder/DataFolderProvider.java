/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.datafolder;

import org.odpi.openmetadata.frameworks.connectors.ConnectorProviderBase;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ConnectorType;


/**
 * DataFolderProvider is the OCF connector provider for the data folder connector.
 */
public class DataFolderProvider extends ConnectorProviderBase
{
    static final String  connectorTypeGUID = "1ef9cbe2-9119-4ac0-b9ac-d838f0ed9caf";
    static final String  connectorTypeName = "Data Folder Connector";
    static final String  connectorTypeDescription = "Connector supports reading of data files grouped under a single folder.";

    /**
     * Constructor used to initialize the ConnectorProviderBase with the Java class name of the specific
     * registry store implementation.
     */
    public DataFolderProvider()
    {
        Class    connectorClass = DataFolderConnector.class;

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
