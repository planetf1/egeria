/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.frameworks.connectors;

import java.util.List;

/**
 * The VirtualConnectorExtension is an optional interface for a virtual connector.  A virtual connector is
 * a connector that uses embedded connectors to access the data assets.
 *
 */
public interface VirtualConnectorExtension
{
    /**
     * Set up the list of connectors that this virtual connector will use to support its interface.
     * The connectors are initialized waiting to start.  When start() is called on the
     * virtual connector, it needs to pass start() to each of the embedded connectors. Similarly for
     * disconnect().
     *
     * @param embeddedConnectors  list of connectors
     */
    void initializeEmbeddedConnectors(List<Connector> embeddedConnectors);
}
