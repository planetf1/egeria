/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryeventmapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.odpi.openmetadata.frameworks.connectors.Connector;
import org.odpi.openmetadata.frameworks.connectors.ConnectorBase;
import org.odpi.openmetadata.frameworks.connectors.VirtualConnectorExtension;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditCode;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLog;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditingComponent;
import org.odpi.openmetadata.repositoryservices.connectors.openmetadatatopic.OpenMetadataTopicConnector;
import org.odpi.openmetadata.repositoryservices.connectors.openmetadatatopic.OpenMetadataTopicListener;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryValidator;
import org.odpi.openmetadata.repositoryservices.ffdc.OMRSErrorCode;

import java.util.ArrayList;
import java.util.List;

/**
 * OMRSRepositoryEventMapperBase provides a base class for implementors of OMRSRepositoryEventMapper.
 */
public abstract class OMRSRepositoryEventMapperConnector extends ConnectorBase implements OMRSRepositoryEventMapper
{
    private static final Logger       log           = LoggerFactory.getLogger(OMRSRepositoryEventMapperConnector.class);
    private static final OMRSAuditLog auditLog      = new OMRSAuditLog(OMRSAuditingComponent.LOCAL_REPOSITORY_EVENT_MAPPER);

    protected OMRSRepositoryEventProcessor repositoryEventProcessor  = null;
    protected String                       repositoryEventMapperName = null;
    protected OMRSRepositoryConnector      repositoryConnector       = null;
    protected OMRSRepositoryHelper         repositoryHelper          = null;
    protected OMRSRepositoryValidator      repositoryValidator       = null;
    protected String                       localMetadataCollectionId = null;
    protected String                       localServerName           = null;
    protected String                       localServerType           = null;
    protected String                       localOrganizationName     = null;


    private List<OpenMetadataTopicConnector> eventBusConnectors = new ArrayList<>();


    /**
     * Default constructor for OCF ConnectorBase.
     */
    public OMRSRepositoryEventMapperConnector()
    {
        super();
    }


    /**
     * Pass additional information to the connector needed to process events.
     *
     * @param repositoryEventMapperName repository event mapper name used for the source of the OMRS events.
     * @param repositoryConnector ths is the connector to the local repository that the event mapper is processing
     *                            events from.  The repository connector is used to retrieve additional information
     *                            necessary to fill out the OMRS Events.
     */
    public void initialize(String                      repositoryEventMapperName,
                           OMRSRepositoryConnector     repositoryConnector)
    {
        this.repositoryEventMapperName = repositoryEventMapperName;
        this.repositoryConnector = repositoryConnector;
    }


    /**
     * Set up a repository helper object for the repository connector to use.
     *
     * @param repositoryHelper helper object for building TypeDefs and metadata instances.
     */
    public void setRepositoryHelper(OMRSRepositoryHelper repositoryHelper)
    {
        this.repositoryHelper = repositoryHelper;
    }


    /**
     * Set up a repository validator for the repository connector to use.
     *
     * @param repositoryValidator validator object to check the validity of TypeDefs and metadata instances.
     */
    public void setRepositoryValidator(OMRSRepositoryValidator repositoryValidator)
    {
        this.repositoryValidator = repositoryValidator;
    }


    /**
     * Set up the name of the server where the metadata collection resides.
     *
     * @param serverName String name
     */
    public void  setServerName(String      serverName)
    {
        this.localServerName = serverName;
    }


    /**
     * Set up the descriptive string describing the type of the server.  This might be the
     * name of the product, or similar identifier.
     *
     * @param serverType String server type
     */
    public void setServerType(String serverType)
    {
        this.localServerType = serverType;
    }


    /**
     * Set up the name of the organization that runs/owns the server.
     *
     * @param organizationName String organization name
     */
    public void setOrganizationName(String organizationName)
    {
        this.localOrganizationName = organizationName;
    }


    /**
     * Set up the unique Id for this metadata collection.
     *
     * @param metadataCollectionId String unique Id
     */
    public void setMetadataCollectionId(String         metadataCollectionId)
    {
        this.localMetadataCollectionId = metadataCollectionId;
    }


    /**
     * Set up the repository event listener for this connector to use.  The connector should pass
     * each type or instance metadata change reported by its metadata repository's metadata on to the
     * repository event listener.
     *
     * @param repositoryEventProcessor listener responsible for distributing notifications of local
     *                                changes to metadata types and instances to the rest of the
     *                                open metadata repository cluster.
     */
    public void setRepositoryEventProcessor(OMRSRepositoryEventProcessor repositoryEventProcessor)
    {
        this.repositoryEventProcessor = repositoryEventProcessor;
    }


    /**
     * Indicates that the connector is completely configured and can begin processing.
     *
     * @throws ConnectorCheckedException there is a problem within the connector.
     */
    public void start() throws ConnectorCheckedException
    {
        super.start();

        final String            methodName = "Start Event Mapper";

        /*
         * If the event mapper is not using embedded connectors then this list is empty.
         */
        for (OpenMetadataTopicConnector eventBusConnector : eventBusConnectors)
        {
            eventBusConnector.start();
        }

        OMRSAuditCode auditCode = OMRSAuditCode.EVENT_MAPPER_LISTENER_STARTED;
        auditLog.logRecord(methodName,
                           auditCode.getLogMessageId(),
                           auditCode.getSeverity(),
                           auditCode.getFormattedLogMessage(repositoryEventMapperName),
                           this.getConnection().toString(),
                           auditCode.getSystemAction(),
                           auditCode.getUserAction());
    }


    /**
     * Free up any resources held since the connector is no longer needed.
     *
     * @throws ConnectorCheckedException there is a problem within the connector.
     */
    public  void disconnect() throws ConnectorCheckedException
    {
        final String            methodName = "Disconnect Event Mapper";

        super.disconnect();

        /*
         * If the event mapper is not using embedded connectors then this list is empty.
         */
        for (OpenMetadataTopicConnector eventBusConnector : eventBusConnectors)
        {
            eventBusConnector.disconnect();
        }

        OMRSAuditCode auditCode = OMRSAuditCode.EVENT_MAPPER_LISTENER_DISCONNECTED;
        auditLog.logRecord(methodName,
                           auditCode.getLogMessageId(),
                           auditCode.getSeverity(),
                           auditCode.getFormattedLogMessage(repositoryEventMapperName),
                           this.getConnection().toString(),
                           auditCode.getSystemAction(),
                           auditCode.getUserAction());
    }
}
