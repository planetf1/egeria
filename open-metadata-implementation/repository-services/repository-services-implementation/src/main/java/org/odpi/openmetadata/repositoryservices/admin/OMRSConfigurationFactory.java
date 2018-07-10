/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.repositoryservices.admin;

import org.odpi.openmetadata.adapters.repositoryservices.ConnectorConfigurationFactory;
import org.odpi.openmetadata.adminservices.configuration.properties.*;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefSummary;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;


/**
 * OMRSConfigurationFactory sets up default configuration for the OMRS components.  It is used by the OMAG server
 * while it manages the changes made to the server configuration by the server administrator.  The aim is to
 * build up the RepositoryServicesConfig object that is used to initialize the OMRSOperationalServices.
 */
public class OMRSConfigurationFactory
{
    /*
     * Default property fillers
     */
    private static final String defaultEnterpriseMetadataCollectionName = " Enterprise Metadata Collection";

    private static final String defaultCohortName = "defaultCohort";

    private ConnectorConfigurationFactory connectorConfigurationFactory = new ConnectorConfigurationFactory();


    /**
     * Default constructor
     */
    public OMRSConfigurationFactory()
    {
    }


    /**
     * Return the protocol level to use for communications with local open metadata access services through the open metadata
     * enterprise repository services.
     *
     * @return protocol version
     */
    private OpenMetadataEventProtocolVersion getDefaultEnterpriseOMRSTopicProtocolVersion()
    {
        return OpenMetadataEventProtocolVersion.V1;
    }


    /**
     * Return the protocol level to use for communications with other members of the open metadata repository cohort.
     *
     * @return protocol version
     */
    private OpenMetadataEventProtocolVersion getDefaultCohortOMRSTopicProtocolVersion()
    {
        return OpenMetadataEventProtocolVersion.V1;
    }


    /**
     * Return the exchange rule set so that events for all local repository changes are sent.
     *
     * @return exchange rule
     */
    private OpenMetadataExchangeRule getDefaultEventsToSendRule()
    {
        return OpenMetadataExchangeRule.ALL;
    }


    /**
     * Return the default list of types to send as a null because the exchange rule above is set to ALL.
     *
     * @return null array list
     */
    private List<TypeDefSummary> getDefaultSelectedTypesToSend()
    {
        return null;
    }


    /**
     * Return the exchange rule set so that all received events are saved.
     *
     * @return exchange rule
     */
    private OpenMetadataExchangeRule getDefaultEventsToSaveRule()
    {
        return OpenMetadataExchangeRule.ALL;
    }


    /**
     * Return the default list of types to save as a null because the exchange rule above is set to ALL.
     *
     * @return null array list
     */
    private List<TypeDefSummary> getDefaultSelectedTypesToSave()
    {
        return null;
    }


    /**
     * Return the exchange rule set so that all incoming events are processed.
     *
     * @return exchange rule
     */
    private OpenMetadataExchangeRule getDefaultEventsToProcessRule()
    {
        return OpenMetadataExchangeRule.ALL;
    }


    /**
     * Return the default list of types to process as a null because the exchange rule above is set to ALL.
     *
     * @return null array list
     */
    private List<TypeDefSummary> getDefaultSelectedTypesToProcess()
    {
        return null;
    }


    /**
     * Returns the basic configuration for a local repository.
     *
     * @param localServerName name of the local server
     * @param localServerURL URL root of local server used for REST calls
     * @return LocalRepositoryConfig object
     */
    private LocalRepositoryConfig getDefaultLocalRepositoryConfig(String              localServerName,
                                                                  String              localServerURL)
    {
        LocalRepositoryConfig localRepositoryConfig = new LocalRepositoryConfig();

        localRepositoryConfig.setMetadataCollectionId(UUID.randomUUID().toString());
        localRepositoryConfig.setLocalRepositoryLocalConnection(connectorConfigurationFactory.getDefaultLocalRepositoryLocalConnection());
        localRepositoryConfig.setLocalRepositoryRemoteConnection(connectorConfigurationFactory.getDefaultLocalRepositoryRemoteConnection(
                localServerName,
                localServerURL));
        localRepositoryConfig.setEventsToSaveRule(this.getDefaultEventsToSaveRule());
        localRepositoryConfig.setSelectedTypesToSave(this.getDefaultSelectedTypesToSave());
        localRepositoryConfig.setEventsToSendRule(this.getDefaultEventsToSendRule());
        localRepositoryConfig.setSelectedTypesToSend(this.getDefaultSelectedTypesToSend());
        localRepositoryConfig.setEventMapperConnection(connectorConfigurationFactory.getDefaultEventMapperConnection());

        return localRepositoryConfig;
    }


    /**
     * Return the configuration for an in-memory local repository.
     *
     * @param localServerName name of the local server
     * @param localServerURL  URL root of local server used for REST calls
     * @return LocalRepositoryConfig object
     */
    public LocalRepositoryConfig getInMemoryLocalRepositoryConfig(String localServerName, String localServerURL)
    {
        LocalRepositoryConfig localRepositoryConfig = this.getDefaultLocalRepositoryConfig(localServerName,
                                                                                           localServerURL);

        localRepositoryConfig.setLocalRepositoryLocalConnection(connectorConfigurationFactory.getInMemoryLocalRepositoryLocalConnection(localServerName));

        return localRepositoryConfig;
    }


    /**
     * Return the configuration for a local repository that is using the built-in graph repository.
     *
     * @param localServerName name of local server
     * @param localServerURL  URL root of local server used for REST calls
     * @param additionalProperties name value property pairs for the topic connection
     * @param eventBusConnectorProvider class name of the event bus connector's provider
     * @param topicURLRoot root name for the topic URL
     * @param eventBusAdditionalProperties name value property pairs for the event bus connection
     * @return LocalRepositoryConfig object
     */
    public LocalRepositoryConfig getLocalGraphLocalRepositoryConfig(String              localServerName,
                                                                    String              localServerURL,
                                                                    Map<String, Object> additionalProperties,
                                                                    String              eventBusConnectorProvider,
                                                                    String              topicURLRoot,
                                                                    Map<String, Object> eventBusAdditionalProperties)
    {
        LocalRepositoryConfig localRepositoryConfig = this.getDefaultLocalRepositoryConfig(localServerName,
                                                                                           localServerURL);

        localRepositoryConfig.
                setLocalRepositoryLocalConnection(connectorConfigurationFactory.getLocalGraphRepositoryLocalConnection(localServerName));

        localRepositoryConfig.
                setEventMapperConnection(connectorConfigurationFactory.getLocalGraphRepositoryEventMapperConnection(localServerName,
                                                                                                                    additionalProperties,
                                                                                                                    eventBusConnectorProvider,
                                                                                                                    topicURLRoot,
                                                                                                                    eventBusAdditionalProperties));

        return localRepositoryConfig;
    }


    /**
     * Return the local repository configuration for a repository proxy.
     *
     * @param localServerName name of local server
     * @param localServerURL url used to call local server
     * @return LocalRepositoryConfig object
     */
    public LocalRepositoryConfig getRepositoryProxyLocalRepositoryConfig(String localServerName, String localServerURL)
    {
        LocalRepositoryConfig localRepositoryConfig = this.getDefaultLocalRepositoryConfig(localServerName,
                                                                                           localServerURL);

        localRepositoryConfig.setLocalRepositoryLocalConnection(null);

        return localRepositoryConfig;
    }


    /**
     * Return the default settings for the enterprise repository services' configuration.
     *
     * @param localServerName name of the local server
     * @return EnterpriseAccessConfig parameters
     */
    public EnterpriseAccessConfig getDefaultEnterpriseAccessConfig(String              localServerName)
    {
        EnterpriseAccessConfig enterpriseAccessConfig = new EnterpriseAccessConfig();

        enterpriseAccessConfig.setEnterpriseMetadataCollectionId(UUID.randomUUID().toString());
        enterpriseAccessConfig.setEnterpriseMetadataCollectionName(localServerName + defaultEnterpriseMetadataCollectionName);
        enterpriseAccessConfig.setEnterpriseOMRSTopicConnection(connectorConfigurationFactory.getDefaultEnterpriseOMRSTopicConnection(localServerName));
        enterpriseAccessConfig.setEnterpriseOMRSTopicProtocolVersion(this.getDefaultEnterpriseOMRSTopicProtocolVersion());

        return enterpriseAccessConfig;
    }


    /**
     * Return a CohortConfig object that is pre-configured with default values.
     *
     * @param localServerName name of the local server
     * @param cohortName      name of the cohort
     * @param additionalProperties name value property pairs for the topic connection
     * @param eventBusConnectorProvider class name of the event bus connector's provider
     * @param topicURLRoot root name for the topic URL
     * @param eventBusAdditionalProperties name value property pairs for the event bus connection
     * @return default values in a CohortConfig object
     */
    public CohortConfig getDefaultCohortConfig(String              localServerName,
                                               String              cohortName,
                                               Map<String, Object> additionalProperties,
                                               String              eventBusConnectorProvider,
                                               String              topicURLRoot,
                                               Map<String, Object> eventBusAdditionalProperties)
    {
        CohortConfig cohortConfig  = new CohortConfig();
        String       newCohortName = defaultCohortName;

        if (cohortName != null)
        {
            newCohortName = cohortName;
        }

        cohortConfig.setCohortName(newCohortName);
        cohortConfig.setCohortRegistryConnection(connectorConfigurationFactory.getDefaultCohortRegistryConnection(localServerName, newCohortName));
        cohortConfig.setCohortOMRSTopicConnection(connectorConfigurationFactory.getDefaultCohortOMRSTopicConnection(newCohortName,
                                                                                                                    additionalProperties,
                                                                                                                    eventBusConnectorProvider,
                                                                                                                    topicURLRoot,
                                                                                                                    eventBusAdditionalProperties));
        cohortConfig.setCohortOMRSTopicProtocolVersion(this.getDefaultCohortOMRSTopicProtocolVersion());
        cohortConfig.setEventsToProcessRule(this.getDefaultEventsToProcessRule());
        cohortConfig.setSelectedTypesToProcess(this.getDefaultSelectedTypesToProcess());

        return cohortConfig;
    }


    /**
     * Returns a repository services config with the audit log set up.
     *
     * @param localServerName name of the local server
     * @return minimally configured repository services config
     */
    public RepositoryServicesConfig getDefaultRepositoryServicesConfig(String localServerName)
    {
        RepositoryServicesConfig repositoryServicesConfig = new RepositoryServicesConfig();

        List<Connection>   auditLogStoreConnections = new ArrayList<>();

        auditLogStoreConnections.add(connectorConfigurationFactory.getDefaultAuditLogConnection(localServerName));

        repositoryServicesConfig.setAuditLogConnections(auditLogStoreConnections);

        return repositoryServicesConfig;
    }
}
