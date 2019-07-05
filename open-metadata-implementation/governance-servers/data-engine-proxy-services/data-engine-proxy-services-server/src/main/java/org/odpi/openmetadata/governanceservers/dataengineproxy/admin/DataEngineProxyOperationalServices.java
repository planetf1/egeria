/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.governanceservers.dataengineproxy.admin;

import org.odpi.openmetadata.accessservices.dataengine.client.DataEngineImpl;
import org.odpi.openmetadata.adminservices.configuration.properties.DataEngineProxyConfig;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGConfigurationErrorException;
import org.odpi.openmetadata.frameworks.connectors.Connector;
import org.odpi.openmetadata.frameworks.connectors.ConnectorBroker;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectionCheckedException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.governanceservers.dataengineproxy.auditlog.DataEngineProxyAuditCode;
import org.odpi.openmetadata.openconnectors.governancedaemonconnectors.dataengineproxy.DataEngineConnectorBase;
import org.odpi.openmetadata.openconnectors.governancedaemonconnectors.dataengineproxy.DataEngineConnectorProvider;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class DataEngineProxyOperationalServices {

    private static final Logger log = LoggerFactory.getLogger(DataEngineProxyOperationalServices.class);

    private String localServerName;
    private String localServerType;
    private String localMetadataCollectionName;
    private String localOrganizationName;
    private String localServerUserId;
    private String localServerPassword;
    private String localServerURL;

    private OMRSAuditLog auditLog;
    private DataEngineConnectorBase dataEngineProxyConnector;

    /**
     * Constructor used at server startup.
     *
     * @param localServerName       name of the local server
     * @param localServerType       type of the local server
     * @param localOrganizationName name of the organization that owns the local server
     * @param localServerUserId     user id for this server to use if processing inbound messages
     * @param localServerPassword   password for this server to use if processing inbound messages
     * @param localServerURL        URL root for this server
     */
    public DataEngineProxyOperationalServices(String localServerName,
                                              String localServerType,
                                              String localOrganizationName,
                                              String localServerUserId,
                                              String localServerPassword,
                                              String localServerURL) {
        this.localServerName = localServerName;
        this.localServerType = localServerType;
        this.localOrganizationName = localOrganizationName;
        this.localServerUserId = localServerUserId;
        this.localServerPassword = localServerPassword;
        this.localServerURL = localServerURL;
    }

    /**
     * Initialize the data engine proxy server
     *
     * @param dataEngineProxyConfig Data Engine proxy server configuration.
     * @param auditLog              Audit Log instance.
     * @throws OMAGConfigurationErrorException there is no data engine (or OMAS) defined for this server,
     * or the requested data engine (or OMAS) is not recognized or is not configured properly.
     */
    public void initialize(DataEngineProxyConfig dataEngineProxyConfig, OMRSAuditLog auditLog) throws OMAGConfigurationErrorException {

        final String methodName = "initialize";
        final String actionDescription = "initialize";

        DataEngineProxyAuditCode auditCode = DataEngineProxyAuditCode.SERVICE_INITIALIZING;
        auditLog.logRecord(actionDescription,
                auditCode.getLogMessageId(),
                auditCode.getSeverity(),
                auditCode.getFormattedLogMessage(),
                null,
                auditCode.getSystemAction(),
                auditCode.getUserAction());

        this.auditLog = auditLog;

        if (dataEngineProxyConfig == null) {
            auditCode = DataEngineProxyAuditCode.NO_CONFIG_DOC;
            auditLog.logRecord(actionDescription,
                    auditCode.getLogMessageId(),
                    auditCode.getSeverity(),
                    auditCode.getFormattedLogMessage(localServerName),
                    null,
                    auditCode.getSystemAction(),
                    auditCode.getUserAction());
            throw new OMAGConfigurationErrorException(500,
                    this.getClass().getName(),
                    methodName,
                    auditCode.getFormattedLogMessage(localServerName),
                    auditCode.getSystemAction(),
                    auditCode.getUserAction());
        } else if (dataEngineProxyConfig.getAccessServiceRootURL() == null) {
            auditCode = DataEngineProxyAuditCode.NO_OMAS_SERVER_URL;
            auditLog.logRecord(actionDescription,
                    auditCode.getLogMessageId(),
                    auditCode.getSeverity(),
                    auditCode.getFormattedLogMessage(localServerName),
                    null,
                    auditCode.getSystemAction(),
                    auditCode.getUserAction());
            throw new OMAGConfigurationErrorException(500,
                    this.getClass().getName(),
                    methodName,
                    auditCode.getFormattedLogMessage(localServerName),
                    auditCode.getSystemAction(),
                    auditCode.getUserAction());
        } else if (dataEngineProxyConfig.getAccessServiceServerName() == null) {
            auditCode = DataEngineProxyAuditCode.NO_OMAS_SERVER_NAME;
            auditLog.logRecord(actionDescription,
                    auditCode.getLogMessageId(),
                    auditCode.getSeverity(),
                    auditCode.getFormattedLogMessage(localServerName),
                    null,
                    auditCode.getSystemAction(),
                    auditCode.getUserAction());
            throw new OMAGConfigurationErrorException(500,
                    this.getClass().getName(),
                    methodName,
                    auditCode.getFormattedLogMessage(localServerName),
                    auditCode.getSystemAction(),
                    auditCode.getUserAction());
        }


        /*
         * Create the client information
         */
        DataEngineImpl dataEngineClient;
        try {
            dataEngineClient = new DataEngineImpl(
                    dataEngineProxyConfig.getAccessServiceServerName(),
                    dataEngineProxyConfig.getAccessServiceRootURL(),
                    localServerUserId,
                    localServerPassword
            );
        } catch (InvalidParameterException error) {
            throw new OMAGConfigurationErrorException(error.getReportedHTTPCode(),
                    this.getClass().getName(),
                    methodName,
                    error.getErrorMessage(),
                    error.getReportedSystemAction(),
                    error.getReportedUserAction(),
                    error);
        }

        // Configure the connector
        Connection dataEngineProxy = dataEngineProxyConfig.getDataEngineProxyConnection();
        Map<String, Object> securedProperties = dataEngineProxy.getSecuredProperties();
        if (securedProperties == null) {
            securedProperties = new HashMap<>();
        }
        // Add the Data Engine OMAS Client connection to the secured properties of the connector
        securedProperties.put("dataEngineOMASClient", dataEngineClient);
        dataEngineProxy.setSecuredProperties(securedProperties);
        if (dataEngineProxy != null) {
            log.info("Found connection: " + dataEngineProxy);
            try {
                ConnectorBroker connectorBroker = new ConnectorBroker();
                dataEngineProxyConnector = (DataEngineConnectorBase) connectorBroker.getConnector(dataEngineProxy);
            } catch (ConnectionCheckedException | ConnectorCheckedException e) {
                log.error("Unable to initialize connector.", e);
                auditCode = DataEngineProxyAuditCode.ERROR_INITIALIZING_CONNECTION;
                auditLog.logRecord(actionDescription,
                        auditCode.getLogMessageId(),
                        auditCode.getSeverity(),
                        auditCode.getFormattedLogMessage(),
                        e.getErrorMessage(),
                        auditCode.getSystemAction(),
                        auditCode.getUserAction());
            }
        }

        // Start the connector
        if (dataEngineProxyConnector != null) {
            try {
                dataEngineProxyConnector.start();
            } catch (ConnectorCheckedException e) {
                log.error("Error in starting the Data Engine Proxy connector.", e);
            }
        }

        if (dataEngineProxyConnector != null && dataEngineProxyConnector.isActive()) {
            auditCode = DataEngineProxyAuditCode.SERVICE_INITIALIZED;
            auditLog.logRecord("Initializing",
                    auditCode.getLogMessageId(),
                    auditCode.getSeverity(),
                    auditCode.getFormattedLogMessage(localServerName),
                    null,
                    auditCode.getSystemAction(),
                    auditCode.getUserAction());
        }

        log.info("Data Engine Proxy has been started!");

    }

    /**
     * Shutdown the Data Engine Proxy Services.
     *
     * @param permanent boolean flag indicating whether this server permanently shutting down or not
     * @return boolean indicated whether the disconnect was successful.
     */
    public boolean disconnect(boolean permanent) {
        DataEngineProxyAuditCode auditCode;
        try {
            dataEngineProxyConnector.disconnect();
            auditCode = DataEngineProxyAuditCode.SERVICE_SHUTDOWN;
            auditLog.logRecord("Disconnecting",
                    auditCode.getLogMessageId(),
                    auditCode.getSeverity(),
                    auditCode.getFormattedLogMessage(localServerName),
                    null,
                    auditCode.getSystemAction(),
                    auditCode.getUserAction());
            return true;
        } catch (Exception e) {
            auditCode = DataEngineProxyAuditCode.ERROR_SHUTDOWN;
            auditLog.logRecord("Disconnecting",
                    auditCode.getLogMessageId(),
                    auditCode.getSeverity(),
                    auditCode.getFormattedLogMessage(),
                    null,
                    auditCode.getSystemAction(),
                    auditCode.getUserAction());
            return false;
        }
    }

}
