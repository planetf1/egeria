/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.multitenant;

import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLog;

/**
 * It is responsible for registering itself in the instance map.
 */
public class OMVSServiceInstance extends AuditableServerServiceInstance
{
    protected String metadataServerName = null;
    protected String metadataServerURL = null;
    /**
     * Set up the OMVS service instance
     *
     * @param serviceName name of this service
     * @param auditLog logging destination
     * @param localServerUserId userId used for server initiated actions
     * @param maxPageSize maximum page size
     */
    public OMVSServiceInstance(String                  serviceName,
                               OMRSAuditLog            auditLog,
                               String                  localServerUserId,
                               int                     maxPageSize,
                               String metadataServerName,
                               String metadataServerURL ) {
        super(null, serviceName, auditLog, localServerUserId, maxPageSize);
        this.metadataServerName = metadataServerName;
        this.metadataServerURL = metadataServerURL;
    }

    /**
     * Return the server name. Used during OMVS initialization which is why the exception
     * is different.
     *
     * @return serverName name of the server for this instance
     */
    public String getServerName()
    {
        return serverName;

    }

    /**
     * the metadata server name
     * @return the metadata server name
     */
    public String getMetadataServerName() {
        return metadataServerName;
    }

    /**
     * the metadata server URL
     * @return the metadata server URL
     */
    public String getMetadataServerURL() {
        return metadataServerURL;
    }
}
