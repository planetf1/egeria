/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.governanceservers.dataengineproxy.processor;

import org.odpi.openmetadata.accessservices.dataengine.client.DataEngineImpl;
import org.odpi.openmetadata.adminservices.configuration.properties.DataEngineProxyConfig;
import org.odpi.openmetadata.frameworks.connectors.ffdc.*;
import org.odpi.openmetadata.governanceservers.dataengineproxy.auditlog.DataEngineProxyAuditCode;
import org.odpi.openmetadata.openconnectors.governancedaemonconnectors.dataengineproxy.DataEngineConnectorBase;
import org.odpi.openmetadata.openconnectors.governancedaemonconnectors.dataengineproxy.DataEngineConnectorErrorCode;
import org.odpi.openmetadata.openconnectors.governancedaemonconnectors.dataengineproxy.model.*;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Class to handle periodically polling a Data Engine for changes, for those data engines that do not
 * provide any event-based mechanism to notify on changes.
 */
public class DataEngineProxyChangePoller implements Runnable {

    private static final Logger log = LoggerFactory.getLogger(DataEngineProxyChangePoller.class);

    private OMRSAuditLog auditLog;
    private DataEngineProxyConfig dataEngineProxyConfig;
    private DataEngineImpl dataEngineOMASClient;
    private DataEngineConnectorBase connector;
    private String engineGuid;

    /**
     * Default constructor
     *
     * @param connector             Data Engine Connector through which to connect to the data engine to poll
     * @param dataEngineProxyConfig configuration of the Data Engine (Proxy)
     * @param dataEngineOMASClient  Data Engine OMAS client through which to push any changes into Egeria
     * @param auditLog              audit log through which to record activities
     */
    public DataEngineProxyChangePoller(DataEngineConnectorBase connector,
                                       DataEngineProxyConfig dataEngineProxyConfig,
                                       DataEngineImpl dataEngineOMASClient,
                                       OMRSAuditLog auditLog) {

        final String methodName = "DataEngineProxyChangePoller";

        this.connector = connector;
        this.dataEngineProxyConfig = dataEngineProxyConfig;
        this.dataEngineOMASClient = dataEngineOMASClient;
        this.auditLog = auditLog;

        DataEngineProxyAuditCode auditCode;

        // Start the connector
        if (connector != null) {
            try {
                connector.start();
                DataEngineSoftwareServerCapability dataEngineDetails = connector.getDataEngineDetails();
                engineGuid = dataEngineOMASClient.createSoftwareServerCapability(dataEngineDetails.getUserId(), dataEngineDetails.getSoftwareServerCapability());
            } catch (InvalidParameterException | PropertyServerException e) {
                DataEngineConnectorErrorCode errorCode = DataEngineConnectorErrorCode.OMAS_CONNECTION_ERROR;
                String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage();
                throw new OCFRuntimeException(
                        errorCode.getHTTPErrorCode(),
                        this.getClass().getName(),
                        methodName,
                        errorMessage,
                        errorCode.getSystemAction(),
                        errorCode.getUserAction(),
                        e
                );
            } catch (UserNotAuthorizedException e) {
                DataEngineConnectorErrorCode errorCode = DataEngineConnectorErrorCode.USER_NOT_AUTHORIZED;
                String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage();
                throw new OCFRuntimeException(
                        errorCode.getHTTPErrorCode(),
                        this.getClass().getName(),
                        methodName,
                        errorMessage,
                        errorCode.getSystemAction(),
                        errorCode.getUserAction(),
                        e
                );
            } catch (ConnectorCheckedException e) {
                log.error("Error in starting the Data Engine Proxy connector.", e);
            }
        } else {
            DataEngineConnectorErrorCode errorCode = DataEngineConnectorErrorCode.NO_CONFIG;
            String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage();
            throw new OCFRuntimeException(
                    errorCode.getHTTPErrorCode(),
                    this.getClass().getName(),
                    methodName,
                    errorMessage,
                    errorCode.getSystemAction(),
                    errorCode.getUserAction()
            );
        }

        if (connector != null && connector.isActive()) {
            auditCode = DataEngineProxyAuditCode.SERVICE_INITIALIZED;
            this.auditLog.logRecord("Initializing",
                    auditCode.getLogMessageId(),
                    auditCode.getSeverity(),
                    auditCode.getFormattedLogMessage(connector.getConnection().getConnectorType().getConnectorProviderClassName()),
                    null,
                    auditCode.getSystemAction(),
                    auditCode.getUserAction());
        } else {
            DataEngineConnectorErrorCode errorCode = DataEngineConnectorErrorCode.NO_CONFIG;
            String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage();
            throw new OCFRuntimeException(
                    errorCode.getHTTPErrorCode(),
                    this.getClass().getName(),
                    methodName,
                    errorMessage,
                    errorCode.getSystemAction(),
                    errorCode.getUserAction()
            );
        }

    }

    /**
     * Poll for Process changes.
     */
    @Override
    public void run() {

        final String methodName = "ProcessPollThread::run";

        while (true) {
            try {
                Date changesLastSynced = connector.getChangesLastSynced();
                Date changesCutoff = new Date();
                if (log.isInfoEnabled()) { log.info("Polling for changes since: {}", changesLastSynced); }
                List<DataEngineSchemaType> changedSchemaTypes = connector.getChangedSchemaTypes(changesLastSynced, changesCutoff);
                if (changedSchemaTypes != null) {
                    for (DataEngineSchemaType changedSchemaType : changedSchemaTypes) {
                        dataEngineOMASClient.createOrUpdateSchemaType(changedSchemaType.getUserId(), changedSchemaType.getSchemaType());
                    }
                }
                List<DataEnginePortImplementation> changedPortImplementations = connector.getChangedPortImplementations(changesLastSynced, changesCutoff);
                if (changedPortImplementations != null) {
                    for (DataEnginePortImplementation changedPortImplementation : changedPortImplementations) {
                        dataEngineOMASClient.createOrUpdatePortImplementation(changedPortImplementation.getUserId(), changedPortImplementation.getPortImplementation());
                    }
                }
                List<DataEnginePortAlias> changedPortAliases = connector.getChangedPortAliases(changesLastSynced, changesCutoff);
                if (changedPortAliases != null) {
                    for (DataEnginePortAlias changedPortAlias : changedPortAliases) {
                        dataEngineOMASClient.createOrUpdatePortAlias(changedPortAlias.getUserId(), changedPortAlias.getPortAlias());
                    }
                }
                if (log.isInfoEnabled()) { log.info(" ... getting changed processes."); }
                List<DataEngineProcess> changedProcesses = connector.getChangedProcesses(changesLastSynced, changesCutoff);
                if (changedProcesses != null) {
                    for (DataEngineProcess changedProcess : changedProcesses) {
                        dataEngineOMASClient.createOrUpdateProcess(changedProcess.getUserId(), changedProcess.getProcess());
                    }
                    if (log.isInfoEnabled()) { log.info(" ... completing process changes."); }
                }
                if (log.isInfoEnabled()) { log.info(" ... getting changed lineage mappings."); }
                List<DataEngineLineageMappings> changedLineageMappings = connector.getChangedLineageMappings(changesLastSynced, changesCutoff);
                if (changedLineageMappings != null) {
                    for (DataEngineLineageMappings changedLineageMapping : changedLineageMappings) {
                        dataEngineOMASClient.addLineageMappings(changedLineageMapping.getUserId(), new ArrayList<>(changedLineageMapping.getLineageMappings()));
                    }
                }
                connector.setChangesLastSynced(changesCutoff);
                Thread.sleep(dataEngineProxyConfig.getPollIntervalInSeconds() * 1000);
            } catch (InterruptedException e) {
                log.error("Thread was interrupted.", e);
                break;
            } catch (InvalidParameterException | PropertyServerException e) {
                log.error("Exception caught!", e);
                DataEngineConnectorErrorCode errorCode = DataEngineConnectorErrorCode.OMAS_CONNECTION_ERROR;
                String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage();
                throw new OCFRuntimeException(
                        errorCode.getHTTPErrorCode(),
                        this.getClass().getName(),
                        methodName,
                        errorMessage,
                        errorCode.getSystemAction(),
                        errorCode.getUserAction(),
                        e
                );
            } catch (UserNotAuthorizedException e) {
                log.error("Exception caught!", e);
                DataEngineConnectorErrorCode errorCode = DataEngineConnectorErrorCode.USER_NOT_AUTHORIZED;
                String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage();
                throw new OCFRuntimeException(
                        errorCode.getHTTPErrorCode(),
                        this.getClass().getName(),
                        methodName,
                        errorMessage,
                        errorCode.getSystemAction(),
                        errorCode.getUserAction(),
                        e
                );
            } catch (Exception e) {
                log.error("Fatal error occurred during processing.", e);
            }
        }

    }

}
