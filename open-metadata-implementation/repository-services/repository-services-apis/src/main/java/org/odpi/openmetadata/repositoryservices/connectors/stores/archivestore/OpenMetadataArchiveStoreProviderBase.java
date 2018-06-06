/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.repositoryservices.connectors.stores.archivestore;

import org.odpi.openmetadata.frameworks.connectors.ConnectorProviderBase;

/**
 * The OpenMetadataArchiveProviderStoreBase provides a base class for the connector provider supporting OMRS
 * open metadata archive stores.  It extends ConnectorProviderBase which does the creation of connector instances.
 * The subclasses of OpenMetadataArchiveStoreProviderBase must initialize ConnectorProviderBase with the Java class
 * name of the audit log connector implementation (by calling super.setConnectorClassName(className)).
 * Then the connector provider will work.
 */
public abstract class OpenMetadataArchiveStoreProviderBase extends ConnectorProviderBase
{
    /**
     * Default Constructor
     */
    public OpenMetadataArchiveStoreProviderBase()
    {
        /*
         * Nothing to do
         */
    }
}

