/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.multitenant;

import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLog;

/**
 * AuditableServerServiceInstance caches references to OMRS objects for a specific server.
 * It is also responsible for registering itself in the instance map.
 */
public abstract class AuditableServerServiceInstance extends OMAGServerServiceInstance
{
    private OMRSAuditLog            auditLog;

    /**
     * Constructor.
     *
     * @param serverName name of this server
     * @param serviceName name of this service
     * @param auditLog link to the repository responsible for servicing the REST calls.
     */
    public AuditableServerServiceInstance(String                  serverName,
                                          String                  serviceName,
                                          OMRSAuditLog            auditLog)
    {
        super(serverName, serviceName);

        this.auditLog = auditLog;
    }


    /**
     * Return the audit log for this access service.
     *
     * @return audit log
     */
    protected OMRSAuditLog getAuditLog()
    {
        return auditLog;
    }
}
