/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.enterprise.repositoryconnector;

import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntitySummary;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.localrepository.repositoryconnector.LocalOMRSRepositoryConnector;
import org.odpi.openmetadata.repositoryservices.localrepository.repositoryconnector.OMRSInstanceRetrievalEventProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditCode;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLog;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditingComponent;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.OMRSMetadataCollection;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceHeader;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;
import org.odpi.openmetadata.repositoryservices.enterprise.connectormanager.OMRSConnectorConsumer;
import org.odpi.openmetadata.repositoryservices.enterprise.connectormanager.OMRSConnectorManager;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.RepositoryErrorException;
import org.odpi.openmetadata.repositoryservices.ffdc.OMRSErrorCode;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.OMRSRuntimeException;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * EnterpriseOMRSRepositoryConnector supports federating calls to multiple metadata repositories.  As a result,
 * its OMRSMetadataInstanceStore (EnterpriseOMRSMetadataCollection) returns metadata from all repositories in the
 * connected open metadata repository cohort(s).
 * <p>
 *     An instance of the EnterpriseOMRSRepositoryConnector is created by each Open Metadata Access Service (OMAS)
 *     using the OCF ConnectorBroker.  They use its metadata collection to retrieve and send the metadata they need.
 * </p>
 * <p>
 *     Each EnterpriseOMRSRepositoryConnector instance needs to maintain an up to date list of OMRS Connectors to all of the
 *     repositories in the connected open metadata repository cohort(s).  It does by registering as an OMRSConnectorConsumer
 *     with the OMRSConnectorManager to be notified when connectors to new open metadata repositories are available.
 * </p>
 */
public class EnterpriseOMRSRepositoryConnector extends OMRSRepositoryConnector implements OMRSConnectorConsumer
{
    private OMRSConnectorManager                connectorManager;
    private String                              connectorConsumerId;

    private LocalOMRSRepositoryConnector        localConnector            = null;
    private OMRSInstanceRetrievalEventProcessor localEventProcessor       = null;
    private String                              localMetadataCollectionId = null;
    private List<FederatedConnector>            remoteCohortConnectors    = new ArrayList<>();

    private String                              accessServiceName         = null;

    private static final Logger       log      = LoggerFactory.getLogger(EnterpriseOMRSRepositoryConnector.class);

    /**
     * Constructor used by the EnterpriseOMRSConnectorProvider.
     *
     * @param connectorManager provides notifications as repositories register and unregister with the
     *                         cohorts.
     */
    public EnterpriseOMRSRepositoryConnector(OMRSConnectorManager connectorManager)
    {
        super();

        String   methodName = "constructor";

        this.connectorManager = connectorManager;

        if (connectorManager != null)
        {
            this.connectorConsumerId = connectorManager.registerConnectorConsumer(this);
        }
        else
        {
            OMRSErrorCode errorCode = OMRSErrorCode.INVALID_COHORT_CONFIG;
            String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage();

            throw new OMRSRuntimeException(errorCode.getHTTPErrorCode(),
                                           this.getClass().getName(),
                                           methodName,
                                           errorMessage,
                                           errorCode.getSystemAction(),
                                           errorCode.getUserAction());
        }
    }


    /**
     * Set up the unique Id for this metadata collection.
     *
     * @param metadataCollectionId String unique Id
     */
    public void setMetadataCollectionId(String     metadataCollectionId)
    {
        super.metadataCollectionId = metadataCollectionId;

        if (metadataCollectionId != null)
        {
            super.metadataCollection = new EnterpriseOMRSMetadataCollection(this,
                                                                            super.serverName,
                                                                            repositoryHelper,
                                                                            repositoryValidator,
                                                                            metadataCollectionId);

        }
    }


    /**
     * Return the name of the access service using this connector.
     *
     * @return access service name
     */
    public String getAccessServiceName()
    {
        return accessServiceName;
    }


    /**
     * Set up the name of the access service using this connector.
     *
     * @param accessServiceName string name
     */
    public void setAccessServiceName(String accessServiceName)
    {
        this.accessServiceName = accessServiceName;
    }


    /**
     * Indicates that the connector is completely configured and can begin processing.
     *
     * @throws ConnectorCheckedException there is a problem within the connector.
     */
    public void start() throws ConnectorCheckedException
    {
        super.start();

        if (auditLog != null)
        {
            final String actionDescription = "start";

            OMRSAuditCode auditCode = OMRSAuditCode.STARTING_ENTERPRISE_CONNECTOR;
            auditLog.logRecord(actionDescription,
                               auditCode.getLogMessageId(),
                               auditCode.getSeverity(),
                               auditCode.getFormattedLogMessage(accessServiceName),
                               null,
                               auditCode.getSystemAction(),
                               auditCode.getUserAction());
        }
    }


    /**
     * Free up any resources held since the connector is no longer needed.
     *
     * @throws ConnectorCheckedException there is a problem disconnecting the connector.
     */
    public void disconnect() throws ConnectorCheckedException
    {
        super.disconnect();

        if (auditLog != null)
        {
            final String actionDescription = "disconnect";

            OMRSAuditCode auditCode = OMRSAuditCode.DISCONNECTING_ENTERPRISE_CONNECTOR;
            auditLog.logRecord(actionDescription,
                               auditCode.getLogMessageId(),
                               auditCode.getSeverity(),
                               auditCode.getFormattedLogMessage(accessServiceName),
                               null,
                               auditCode.getSystemAction(),
                               auditCode.getUserAction());
        }

        if ((connectorManager != null) && (connectorConsumerId != null))
        {
            connectorManager.unregisterConnectorConsumer(connectorConsumerId);
        }

        localConnector = null;
        remoteCohortConnectors = new ArrayList<>();
    }


    /**
     * Returns the connector to the repository where the supplied instance can be updated, ie its home repository.
     *
     * @param instance instance to test
     * @param methodName name of method making the request (used for logging)
     * @return repository connector
     * @throws RepositoryErrorException home metadata collection is null
     */
    protected OMRSRepositoryConnector  getHomeConnector(InstanceHeader instance,
                                                        String         methodName) throws RepositoryErrorException
    {
        this.validateRepositoryIsActive(methodName);

        repositoryValidator.validateHomeMetadataGUID(repositoryName, instance, methodName);

        String  instanceMetadataCollectionId = instance.getMetadataCollectionId();

        if (instanceMetadataCollectionId.equals(localMetadataCollectionId))
        {
            return localConnector;
        }

        for (FederatedConnector   remoteCohortConnector : remoteCohortConnectors)
        {
            if (remoteCohortConnector != null)
            {
                if (instanceMetadataCollectionId.equals(remoteCohortConnector.getMetadataCollectionId()))
                {
                    return remoteCohortConnector.getConnector();
                }
            }
        }

        return null;
    }


    /**
     * Returns the list of repository connectors that the EnterpriseOMRSRepositoryConnector is federating queries across.
     *
     * This method is used by this connector's metadata collection object on each request it processes.  This
     * means it always has the most up to date list of connectors to work with.
     *
     * @param methodName name of method making the request (used for logging)
     * @return OMRSRepositoryConnector List
     * @throws RepositoryErrorException the enterprise services are not available
     */
    protected List<OMRSRepositoryConnector> getCohortConnectors(String     methodName) throws RepositoryErrorException
    {
        this.validateRepositoryIsActive(methodName);

        List<OMRSRepositoryConnector> cohortConnectors = new ArrayList<>();

        /*
         * Make sure the local connector is first.
         */
        if (localConnector != null)
        {
            cohortConnectors.add(localConnector);
        }

        /*
         * Now add the remote connectors.
         */
        for (FederatedConnector federatedConnector : remoteCohortConnectors)
        {
            cohortConnectors.add(federatedConnector.getConnector());
        }

        if (! cohortConnectors.isEmpty())
        {
            return cohortConnectors;
        }
        else
        {
            OMRSErrorCode errorCode = OMRSErrorCode.NO_REPOSITORIES;
            String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(accessServiceName);

            throw new RepositoryErrorException(errorCode.getHTTPErrorCode(),
                                               this.getClass().getName(),
                                               methodName,
                                               errorMessage,
                                               errorCode.getSystemAction(),
                                               errorCode.getUserAction());
        }
    }


    /**
     * Pass an entity that has been retrieved from a remote open metadata repository so it can be validated and
     * (if the rules permit) cached in the local repository.
     *
     * @param sourceMetadataCollectionId unique identifier for the home collection
     * @param entity the retrieved entity.
     * @return Validated and processed entity.
     */
    EntitySummary processRetrievedEntitySummary(String        sourceMetadataCollectionId,
                                                EntitySummary entity)
    {
        EntitySummary   processedEntity = new EntitySummary(entity);

        /*
         * Ensure the metadata collection is set up correctly.
         */
        if (processedEntity.getMetadataCollectionId() == null)
        {
            processedEntity.setMetadataCollectionId(metadataCollectionId);
        }

        if (this.localEventProcessor != null)
        {
            this.localEventProcessor.processRetrievedEntitySummary(localMetadataCollectionId,
                                                                   sourceMetadataCollectionId,
                                                                   processedEntity);
        }

        return processedEntity;
    }


    /**
     * Pass an entity that has been retrieved from a remote open metadata repository so it can be validated and
     * (if the rules permit) cached in the local repository.
     *
     * @param sourceMetadataCollectionId unique identifier for the home collection
     * @param entity the retrieved entity.
     * @return Validated and processed entity.
     */
    EntityDetail processRetrievedEntityDetail(String        sourceMetadataCollectionId,
                                              EntityDetail  entity)
    {
        EntityDetail   processedEntity = new EntityDetail(entity);

        /*
         * Ensure the metadata collection is set up correctly.
         */
        if (processedEntity.getMetadataCollectionId() == null)
        {
            processedEntity.setMetadataCollectionId(metadataCollectionId);
        }

        if (this.localEventProcessor != null)
        {
            this.localEventProcessor.processRetrievedEntityDetail(localMetadataCollectionId,
                                                                  sourceMetadataCollectionId,
                                                                  processedEntity);
        }

        return processedEntity;
    }


    /**
     * Pass a list of entities that have been retrieved from a remote open metadata repository so they can be
     * validated and (if the rules permit) cached in the local repository.
     *
     * @param sourceMetadataCollectionId unique identifier for the metadata from the remote repository
     * @param entities the retrieved relationships
     * @return the validated and processed relationships
     */
    List<EntityDetail> processRetrievedEntities(String              sourceMetadataCollectionId,
                                                List<EntityDetail>  entities)
    {
        List<EntityDetail> processedEntities = new ArrayList<>();

        for (EntityDetail  entity : entities)
        {
            EntityDetail   processedEntity = this.processRetrievedEntityDetail(sourceMetadataCollectionId, entity);

            if (processedEntity != null)
            {
                processedEntities.add(processedEntity);
            }
        }

        return processedEntities;
    }


    /**
     * Pass a relationship that has been retrieved from a remote open metadata repository so it can be validated and
     * (if the rules permit) cached in the local repository.
     *
     * @param sourceMetadataCollectionId unique identifier for the metadata from the remote repository
     * @param relationship the retrieved relationship
     * @return the validated and processed relationship
     */
    public Relationship processRetrievedRelationship(String         sourceMetadataCollectionId,
                                                     Relationship   relationship)
    {
        Relationship   processedRelationship = new Relationship(relationship);

        /*
         * Ensure the metadata collection is set up correctly.
         */
        if (processedRelationship.getMetadataCollectionId() == null)
        {
            processedRelationship.setMetadataCollectionId(metadataCollectionId);
        }

        if (this.localEventProcessor != null)
        {
            this.localEventProcessor.processRetrievedRelationship(localMetadataCollectionId,
                                                                  sourceMetadataCollectionId,
                                                                  processedRelationship);
        }

        return processedRelationship;
    }


    /**
     * Pass a list of relationships that have been retrieved from a remote open metadata repository so they can be
     * validated and (if the rules permit) cached in the local repository.
     *
     * @param sourceMetadataCollectionId unique identifier for the metadata from the remote repository
     * @param relationships        the list of retrieved relationships
     * @return the validated and processed relationships
     */
    public List<Relationship> processRetrievedRelationships(String             sourceMetadataCollectionId,
                                                            List<Relationship> relationships)
    {
        List<Relationship> processedRelationships = new ArrayList<>();

        for (Relationship  relationship : relationships)
        {
            Relationship processedRelationship = this.processRetrievedRelationship(sourceMetadataCollectionId, relationship);

            if (processedRelationship != null)
            {
                processedRelationships.add(processedRelationship);
            }
        }

        return processedRelationships;
    }


    /**
     * Save the connector to the local repository.  This is passed from the OMRSConnectorManager.
     *
     * @param metadataCollectionId Unique identifier for the metadata collection.
     * @param localConnector OMRSRepositoryConnector object for the local repository.
     */
    public void setLocalConnector(String                       metadataCollectionId,
                                  LocalOMRSRepositoryConnector localConnector)
    {
        this.localMetadataCollectionId = metadataCollectionId;
        this.localConnector = localConnector;

        if (localConnector != null)
        {
            this.localEventProcessor = localConnector.getIncomingInstanceRetrievealEventProcessor();
        }
    }


    /**
     * Pass the connector to one of the remote repositories in the metadata repository cohort.
     *
     * @param metadataCollectionId Unique identifier for the metadata collection.
     * @param remoteConnector OMRSRepositoryConnector object providing access to the remote repository.
     */
    public void addRemoteConnector(String                  metadataCollectionId,
                                   OMRSRepositoryConnector remoteConnector)
    {
        if (remoteConnector != null)
        {
            remoteCohortConnectors.add(new FederatedConnector(metadataCollectionId, remoteConnector));
        }
    }


    /**
     * Pass the metadata collection id for a repository that has just left the metadata repository cohort.
     *
     * @param metadataCollectionId identifier of the metadata collection that is no longer available.
     */
    public void removeRemoteConnector(String  metadataCollectionId)
    {
        Iterator<FederatedConnector> iterator = remoteCohortConnectors.iterator();

        while(iterator.hasNext())
        {
            FederatedConnector registeredConnector = iterator.next();

            if (registeredConnector.getMetadataCollectionId().equals(metadataCollectionId))
            {
                this.disconnectConnector(registeredConnector);
                iterator.remove();
            }
        }
    }


    /**
     * The OMRS is about to shutdown.
     * Call disconnect on all registered remote connectors and stop calling them.
     * There is no need to disconnect the local connector - that is handled by the EnterpriseConnectorManager
     */
    public void disconnectAllConnectors()
    {
        try
        {
            super.disconnect();
        }
        catch (Throwable error)
        {
            /*
             * Nothing to do
             */
        }

        /*
         * Need to disconnect the remote connectors
         */

        if (remoteCohortConnectors != null)
        {
            for (FederatedConnector remoteConnector : remoteCohortConnectors)
            {
                if (remoteConnector != null)
                {
                    this.disconnectConnector(remoteConnector);
                }
            }
        }
    }


    /**
     * Issue a disconnect call on the supplied connector.
     *
     * @param connector connector to disconnect.
     */
    private void disconnectConnector(OMRSRepositoryConnector  connector)
    {
        if (connector != null)
        {
            try
            {
                connector.disconnect();
            }
            catch (Throwable  error)
            {
                log.error("Exception from disconnect of connector to metadata collection:" + connector.getMetadataCollectionId() + "  Error message was: " + error.getMessage());
            }
        }
    }


    /**
     * Issue a disconnect call on the supplied connector.
     *
     * @param federatedConnector connector to disconnect.
     */
    private void disconnectConnector(FederatedConnector  federatedConnector)
    {
        OMRSRepositoryConnector    connector = null;

        if (federatedConnector != null)
        {
            connector = federatedConnector.getConnector();
        }

        if (connector != null)
        {
            disconnectConnector(connector);
        }
    }


    /**
     * FederatedConnector is a private class for storing details of each of the connectors to the repositories
     * in the open metadata repository cohort.
     */
    private class FederatedConnector
    {
        private String                  metadataCollectionId;
        private OMRSRepositoryConnector connector;


        /**
         * Constructor to set up the details of a federated connector.
         *
         * @param metadataCollectionId unique identifier for the metadata collection accessed through the connector
         * @param connector connector for the repository
         */
        public FederatedConnector(String metadataCollectionId, OMRSRepositoryConnector connector)
        {
            this.metadataCollectionId = metadataCollectionId;
            this.connector = connector;
        }


        /**
         * Return the identifier for the metadata collection accessed through the connector.
         *
         * @return String identifier
         */
        public String getMetadataCollectionId()
        {
            return metadataCollectionId;
        }


        /**
         * Return the connector for the repository.
         *
         * @return OMRSRepositoryConnector object
         */
        public OMRSRepositoryConnector getConnector()
        {
            return connector;
        }


        /**
         * Return the metadata collection associated with the connector.
         *
         * @return OMRSMetadataInstanceStore object
         */
        public OMRSMetadataCollection getMetadataCollection()
        {
            if (connector != null)
            {
                try
                {
                    return connector.getMetadataCollection();
                }
                catch (Throwable   error)
                {
                    return null;
                }
            }

            return null;
        }
    }
}
