/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.communityprofile.server;

import org.odpi.openmetadata.accessservices.communityprofile.ffdc.CommunityProfileErrorCode;
import org.odpi.openmetadata.accessservices.communityprofile.handlers.MyProfileHandler;
import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceDescription;
import org.odpi.openmetadata.commonservices.multitenant.OCFOMASServiceInstance;
import org.odpi.openmetadata.commonservices.multitenant.ffdc.exceptions.NewInstanceException;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLog;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;

import java.util.List;

/**
 * CommunityProfileServicesInstance caches references to OMRS objects for a specific server.
 * It is also responsible for registering itself in the instance map.
 */
public class CommunityProfileServicesInstance extends OCFOMASServiceInstance
{
    private static AccessServiceDescription myDescription = AccessServiceDescription.COMMUNITY_PROFILE_OMAS;

    private MyProfileHandler myProfileHandler;

    /**
     * Set up the local repository connector that will service the REST Calls.
     *
     * @param repositoryConnector link to the repository responsible for servicing the REST calls.
     * @param supportedZones list of zones that the community profile is allowed to serve Assets from.
     * @param auditLog logging destination
     *
     * @throws NewInstanceException a problem occurred during initialization
     */
    public CommunityProfileServicesInstance(OMRSRepositoryConnector repositoryConnector,
                                            List<String> supportedZones,
                                            OMRSAuditLog auditLog) throws NewInstanceException
    {
        super(myDescription.getAccessServiceName(),
              repositoryConnector,
              auditLog);

        final String methodName = "new ServiceInstance";

        super.supportedZones = supportedZones;

        if (repositoryHandler != null)
        {
            this.myProfileHandler = new MyProfileHandler(serviceName,
                                                         serverName,
                                                         invalidParameterHandler,
                                                         repositoryHelper,
                                                         repositoryHandler,
                                                         errorHandler);
        }
        else
        {
            CommunityProfileErrorCode errorCode    = CommunityProfileErrorCode.OMRS_NOT_INITIALIZED;
            String                    errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(methodName);

            throw new NewInstanceException(errorCode.getHTTPErrorCode(),
                                           this.getClass().getName(),
                                           methodName,
                                           errorMessage,
                                           errorCode.getSystemAction(),
                                           errorCode.getUserAction());

        }
    }



    /**
     * Return the handler for my profile requests.
     *
     * @return handler object
     */
    MyProfileHandler getMyProfileHandler()
    {
        return myProfileHandler;
    }
}
