/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.adapters.repositoryservices.rest.repositoryconnector;


import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnectorProviderBase;

/**
 * In the Open Connector Framework (OCF), a ConnectorProvider is a factory for a specific type of connector.
 * The OMRSRESTRepositoryConnectorProvider is the connector provider for the OMRSRESTRepositoryConnector.
 * It extends OMRSRepositoryConnectorProviderBase which in turn extends the OCF ConnectorProviderBase.
 * ConnectorProviderBase supports the creation of connector instances.
 *
 * The OMRSRESTRepositoryConnectorProvider must initialize ConnectorProviderBase with the Java class
 * name of the OMRS Connector implementation (by calling super.setConnectorClassName(className)).
 * Then the connector provider will work.
 */
public class OMRSRESTRepositoryConnectorProvider extends OMRSRepositoryConnectorProviderBase
{
    /**
     * Constructor used to initialize the ConnectorProviderBase with the Java class name of the specific
     * OMRS Connector implementation.
     */
    public OMRSRESTRepositoryConnectorProvider()
    {
        Class    connectorClass = OMRSRESTRepositoryConnector.class;

        super.setConnectorClassName(connectorClass.getName());
    }
}