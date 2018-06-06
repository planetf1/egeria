/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.frameworks.connectors;

import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectionCheckedException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.connectors.properties.Connection;

/**
 * The ConnectorProvider is a formal plug-in interface for the Open Connector Framework (OCF).  It provides a factory
 * class for a specific type of connector.  Therefore is it typical to find the ConnectorProvider and Connector
 * implementation written as a pair.
 *
 * The ConnectorProvider uses the properties stored in a Connection object to initialize itself and its Connector instances.
 * the Connection object has the endpoint properties for the server that the connector must communicate with
 * as well as optional additional properties that may be needed for a particular type of connector.
 *
 * It is suggested that the ConnectorProvider validates the contents of the connection and throws
 * ConnectionErrorExceptions if the connection has missing or invalid properties.  If there are errors detected in the
 * instantiations or initialization of the connector, then these should be thrown as ConnectorErrorExceptions.
 */
public abstract class ConnectorProvider
{
    /**
     * Creates a new instance of a connector based on the information in the supplied connection.
     *
     * @param connection   connection that should have all of the properties needed by the Connector Provider
     *                   to create a connector instance.
     * @return Connector   instance of the connector.
     * @throws ConnectionCheckedException if there are missing or invalid properties in the connection
     * @throws ConnectorCheckedException if there are issues instantiating or initializing the connector
     */
    public abstract Connector getConnector(Connection connection) throws ConnectionCheckedException, ConnectorCheckedException;
}