/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.ocf.metadatamanagement.admin;

import org.odpi.openmetadata.commonservices.multitenant.ffdc.exceptions.NewInstanceException;
import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.auditlog.OCFMetadataAuditCode;
import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.server.OCFMetadataInstanceHandler;
import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.server.OCFMetadataServicesInstance;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLog;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;


/**
 * OCFMetadataOperationalServices initializes the REST Services that support the Open Connector Framework (OCF)
 * connected asset properties calls.
 */
public class OCFMetadataOperationalServices
{
    private String       serverName;
    private OMRSAuditLog auditLog;

    public OCFMetadataOperationalServices(String                   serverName,
                                          OMRSRepositoryConnector  repositoryConnector,
                                          OMRSAuditLog             auditLog) throws NewInstanceException
    {
        this.serverName = serverName;
        this.auditLog = auditLog;

        final String         actionDescription = "initialize";
        OCFMetadataAuditCode auditCode;

        auditCode = OCFMetadataAuditCode.SERVICE_INITIALIZING;
        auditLog.logRecord(actionDescription,
                           auditCode.getLogMessageId(),
                           auditCode.getSeverity(),
                           auditCode.getFormattedLogMessage(),
                           null,
                           auditCode.getSystemAction(),
                           auditCode.getUserAction());

        try
        {
            auditCode = OCFMetadataAuditCode.SERVICE_INITIALIZED;
            auditLog.logRecord(actionDescription,
                               auditCode.getLogMessageId(),
                               auditCode.getSeverity(),
                               auditCode.getFormattedLogMessage(serverName),
                               null,
                               auditCode.getSystemAction(),
                               auditCode.getUserAction());

            new OCFMetadataServicesInstance(repositoryConnector, auditLog);
        }
        catch (Throwable error)
        {
            auditCode = OCFMetadataAuditCode.SERVICE_INSTANCE_FAILURE;
            auditLog.logRecord(actionDescription,
                               auditCode.getLogMessageId(),
                               auditCode.getSeverity(),
                               auditCode.getFormattedLogMessage(error.getMessage()),
                               null,
                               auditCode.getSystemAction(),
                               auditCode.getUserAction());
        }
    }


    /**
     * Shutdown the service.
     */
    public void shutdown()
    {
        final String          actionDescription = "shutdown";
        OCFMetadataAuditCode  auditCode = OCFMetadataAuditCode.SERVICE_SHUTDOWN;

        this.auditLog.logRecord(actionDescription,
                           auditCode.getLogMessageId(),
                           auditCode.getSeverity(),
                           auditCode.getFormattedLogMessage(serverName),
                           null,
                           auditCode.getSystemAction(),
                           auditCode.getUserAction());

        new OCFMetadataInstanceHandler().removeServerServiceInstance(serverName);
    }
}
