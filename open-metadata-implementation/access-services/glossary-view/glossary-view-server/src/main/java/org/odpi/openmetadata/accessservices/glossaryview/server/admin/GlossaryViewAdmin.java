/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.glossaryview.server.admin;

import org.odpi.openmetadata.adminservices.configuration.properties.AccessServiceConfig;
import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceAdmin;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLog;
import org.odpi.openmetadata.repositoryservices.connectors.omrstopic.OMRSTopicConnector;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;

/**
 * Called by the OMAG Server to initialize and terminate the Glossary View OMAS.
 * The initialization call provides this OMAS with resources from the Open Metadata Repository Services.
 */
public class GlossaryViewAdmin extends AccessServiceAdmin
{

    private OMRSAuditLog auditLog;
    private GlossaryViewServiceInstance instance;
    private String serverName;

    /**
     * Initialize the access service.
     *
     * @param accessServiceConfig           specific configuration properties for this access service.
     * @param enterpriseOMRSTopicConnector  connector for receiving OMRS Events from the cohorts
     * @param repositoryConnector           connector for querying the cohort repositories
     * @param auditLog                      audit log component for logging messages.
     * @param serverUserName                user id to use on OMRS calls where there is no end user.
     */
    @Override
    public void initialize(AccessServiceConfig accessServiceConfig, OMRSTopicConnector enterpriseOMRSTopicConnector,
                           OMRSRepositoryConnector repositoryConnector, OMRSAuditLog auditLog, String serverUserName) {
        final String actionDescription = "initialize Glossary View OMAS";
        this.auditLog = auditLog;

        GlossaryViewAuditCode auditCode;
        try {
            auditCode = GlossaryViewAuditCode.SERVICE_INITIALIZING;
            auditLog.logRecord(actionDescription, auditCode.getLogMessageId(), auditCode.getSeverity(),
                    auditCode.getFormattedLogMessage(), null, auditCode.getSystemAction(),
                    auditCode.getUserAction());

            instance = new GlossaryViewServiceInstance(repositoryConnector, auditLog);
            serverName = instance.getServerName();

            auditCode = GlossaryViewAuditCode.SERVICE_INITIALIZED;
            auditLog.logRecord(actionDescription, auditCode.getLogMessageId(), auditCode.getSeverity(),
                    auditCode.getFormattedLogMessage(serverName), null, auditCode.getSystemAction(),
                    auditCode.getUserAction());

        } catch (Throwable error) {
            auditCode = GlossaryViewAuditCode.SERVICE_INSTANCE_FAILURE;
            auditLog.logRecord(actionDescription, auditCode.getLogMessageId(), auditCode.getSeverity(),
                    auditCode.getFormattedLogMessage(error.getMessage()), null,
                    auditCode.getSystemAction(), auditCode.getUserAction());
        }
    }

    /**
     * Shutdown the access service.
     */
    @Override
    public void shutdown() {

        if (instance != null) {
            instance.shutdown();
        }

        if (auditLog != null) {
            final String actionDescription = "shutdown";
            GlossaryViewAuditCode auditCode = GlossaryViewAuditCode.SERVICE_SHUTDOWN;

            auditLog.logRecord(actionDescription, auditCode.getLogMessageId(), auditCode.getSeverity(),
                    auditCode.getFormattedLogMessage(serverName), null, auditCode.getSystemAction(),
                    auditCode.getUserAction());
        }
    }
}
