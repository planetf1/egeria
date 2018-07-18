/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.adapters.repositoryservices.cohortregistrystore.file;

import org.odpi.openmetadata.frameworks.connectors.properties.ConnectorTypeProperties;
import org.testng.annotations.Test;

import static org.testng.Assert.assertTrue;

/**
 * Ensures FileBasedRegistryStoreProvider correctly initializes its superclass.
 */
public class TestFileBasedRegistryStoreProvider
{
    @Test public void testProviderInitialization()
    {
        FileBasedRegistryStoreProvider  provider = new FileBasedRegistryStoreProvider();

        assertTrue(provider.getConnectorClassName().equals(FileBasedRegistryStoreConnector.class.getName()));

        ConnectorTypeProperties   connectorTypeProperties = provider.getConnectorTypeProperties();

        assertTrue(connectorTypeProperties != null);

        assertTrue(connectorTypeProperties.getConnectorProviderClassName().equals(FileBasedRegistryStoreProvider.class.getName()));
    }
}
