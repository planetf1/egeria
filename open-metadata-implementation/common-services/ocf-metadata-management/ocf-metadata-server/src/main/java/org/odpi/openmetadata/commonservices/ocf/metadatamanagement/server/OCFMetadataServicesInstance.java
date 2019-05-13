/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.ocf.metadatamanagement.server;

import org.odpi.openmetadata.adminservices.configuration.registration.CommonServicesDescription;
import org.odpi.openmetadata.commonservices.multitenant.OCFOMASServiceInstance;
import org.odpi.openmetadata.commonservices.multitenant.ffdc.exceptions.NewInstanceException;
import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.ffdc.OMAGOCFErrorCode;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLog;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;


/**
 * ConnectedAssetServicesInstance caches references to OMRS objects for a specific server.
 * It is also responsible for registering itself in the instance map.
 */
public class OCFMetadataServicesInstance extends OCFOMASServiceInstance
{

    /**
     * Set up the handlers for this server.
     *
     * @param repositoryConnector link to the repository responsible for servicing the REST calls.
     * @param auditLog destination for audit log events.
     * @throws NewInstanceException a problem occurred during initialization
     */
    public OCFMetadataServicesInstance(OMRSRepositoryConnector repositoryConnector,
                                       OMRSAuditLog            auditLog) throws NewInstanceException
    {
        super(CommonServicesDescription.OCF_METADATA_MANAGEMENT.getServiceName(), repositoryConnector, auditLog);

        final String methodName = "new ServiceInstance";

        if (repositoryHandler == null)
        {
            OMAGOCFErrorCode errorCode    = OMAGOCFErrorCode.OMRS_NOT_INITIALIZED;
            String           errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(methodName);

            throw new NewInstanceException(errorCode.getHTTPErrorCode(),
                                           this.getClass().getName(),
                                           methodName,
                                           errorMessage,
                                           errorCode.getSystemAction(),
                                           errorCode.getUserAction());

        }
    }
}
