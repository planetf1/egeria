/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.softwaredeveloper.admin;

import org.odpi.openmetadata.accessservices.softwaredeveloper.auditlog.SoftwareDeveloperAuditCode;
import org.odpi.openmetadata.accessservices.softwaredeveloper.listener.SoftwareDeveloperOMRSTopicListener;
import org.odpi.openmetadata.accessservices.softwaredeveloper.server.SoftwareDeveloperServicesInstance;
import org.odpi.openmetadata.adminservices.configuration.properties.AccessServiceConfig;
import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceAdmin;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGConfigurationErrorException;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLog;
import org.odpi.openmetadata.repositoryservices.connectors.omrstopic.OMRSTopicConnector;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;

import java.util.List;

/**
 * SoftwareDeveloperAdmin manages the start up and shutdown of the Software Developer OMAS.   During start up,
 * it validates the parameters and options it receives and sets up the service as requested.
 */
public class SoftwareDeveloperAdmin extends AccessServiceAdmin
{
    private OMRSAuditLog                      auditLog   = null;
    private SoftwareDeveloperServicesInstance instance   = null;
    private String                            serverName = null;

    /**
     * Default constructor
     */
    public SoftwareDeveloperAdmin()
    {
    }


    /**
     * Initialize the access service.
     *
     * @param accessServiceConfig  specific configuration properties for this access service.
     * @param omrsTopicConnector  connector for receiving OMRS Events from the cohorts
     * @param repositoryConnector  connector for querying the cohort repositories
     * @param auditLog  audit log component for logging messages.
     * @param serverUserName  user id to use on OMRS calls where there is no end user.
     * @throws OMAGConfigurationErrorException invalid parameters in the configuration properties.
     */
    public void initialize(AccessServiceConfig     accessServiceConfig,
                           OMRSTopicConnector      omrsTopicConnector,
                           OMRSRepositoryConnector repositoryConnector,
                           OMRSAuditLog            auditLog,
                           String                  serverUserName) throws OMAGConfigurationErrorException
    {
        final String               actionDescription = "initialize";
        SoftwareDeveloperAuditCode auditCode;

        auditCode = SoftwareDeveloperAuditCode.SERVICE_INITIALIZING;
        auditLog.logRecord(actionDescription,
                           auditCode.getLogMessageId(),
                           auditCode.getSeverity(),
                           auditCode.getFormattedLogMessage(),
                           null,
                           auditCode.getSystemAction(),
                           auditCode.getUserAction());

        this.auditLog = auditLog;

        try
        {
            List<String>  supportedZones = this.extractSupportedZones(accessServiceConfig.getAccessServiceOptions(),
                                                                      accessServiceConfig.getAccessServiceName(),
                                                                      auditLog);

            this.instance = new SoftwareDeveloperServicesInstance(repositoryConnector,
                                                                  supportedZones,
                                                                  auditLog,
                                                                  serverUserName,
                                                                  repositoryConnector.getMaxPageSize());
            this.serverName = instance.getServerName();

            /*
             * Only set up the listening and event publishing if requested in the config.
             */
            if (accessServiceConfig.getAccessServiceOutTopic() != null)
            {
                SoftwareDeveloperOMRSTopicListener omrsTopicListener;

                omrsTopicListener = new SoftwareDeveloperOMRSTopicListener(accessServiceConfig.getAccessServiceOutTopic(),
                                                                           repositoryConnector.getRepositoryHelper(),
                                                                           repositoryConnector.getRepositoryValidator(),
                                                                           accessServiceConfig.getAccessServiceName(),
                                                                           supportedZones,
                                                                           auditLog);
                super.registerWithEnterpriseTopic(accessServiceConfig.getAccessServiceName(),
                                                  serverName,
                                                  omrsTopicConnector,
                                                  omrsTopicListener,
                                                  auditLog);
            }

            auditCode = SoftwareDeveloperAuditCode.SERVICE_INITIALIZED;
            auditLog.logRecord(actionDescription,
                               auditCode.getLogMessageId(),
                               auditCode.getSeverity(),
                               auditCode.getFormattedLogMessage(serverName),
                               accessServiceConfig.toString(),
                               auditCode.getSystemAction(),
                               auditCode.getUserAction());
        }
        catch (OMAGConfigurationErrorException error)
        {
            throw error;
        }
        catch (Throwable error)
        {
            auditCode = SoftwareDeveloperAuditCode.SERVICE_INSTANCE_FAILURE;
            auditLog.logException(actionDescription,
                                  auditCode.getLogMessageId(),
                                  auditCode.getSeverity(),
                                  auditCode.getFormattedLogMessage(error.getMessage()),
                                  accessServiceConfig.toString(),
                                  auditCode.getSystemAction(),
                                  auditCode.getUserAction(),
                                  error);
        }
    }


    /**
     * Shutdown the access service.
     */
    public void shutdown()
    {
        final String               actionDescription = "shutdown";
        SoftwareDeveloperAuditCode auditCode;

        if (instance != null)
        {
            this.instance.shutdown();
        }

        auditCode = SoftwareDeveloperAuditCode.SERVICE_SHUTDOWN;
        auditLog.logRecord(actionDescription,
                           auditCode.getLogMessageId(),
                           auditCode.getSeverity(),
                           auditCode.getFormattedLogMessage(serverName),
                           null,
                           auditCode.getSystemAction(),
                           auditCode.getUserAction());
    }
}
