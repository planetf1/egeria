/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.rest.services;

import org.odpi.openmetadata.commonservices.multitenant.OMAGServerServiceInstanceHandler;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.RepositoryErrorException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException;


/**
 * OMRSRepositoryServicesInstanceHandler provides the mapping for inbound REST requests to the appropriate instances
 * for the requested server.  The map is maintained in a static so it is scoped to the class loader.
 *
 * Instances of this class call the synchronized static methods to work with the map.
 */
public class OMRSRepositoryServicesInstanceHandler extends OMAGServerServiceInstanceHandler
{

    /**
     * Constructor
     */
    public OMRSRepositoryServicesInstanceHandler(String   serviceName)
    {
        super(serviceName);
    }


    /**
     * Get the object containing the properties for this server.
     *
     * @param userId calling user
     * @param serverName name of this server
     * @return specific service instance
     * @throws InvalidParameterException the server name is not known
     * @throws UserNotAuthorizedException the user is not authorized to issue the request.
     * @throws RepositoryErrorException the service name is not know - indicating a logic error
     */
    public OMRSRepositoryServicesInstance getInstance(String   userId,
                                                      String   serverName) throws InvalidParameterException,
                                                                                  UserNotAuthorizedException,
                                                                                  RepositoryErrorException
    {
        try
        {
            return (OMRSRepositoryServicesInstance) super.getServerServiceInstance(userId, serverName);
        }
        catch (org.odpi.openmetadata.commonservices.ffdc.exceptions.UserNotAuthorizedException  error)
        {
            throw new org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException(error.getReportedHTTPCode(),
                                                                                                         error.getReportingClassName(),
                                                                                                         error.getReportingActionDescription(),
                                                                                                         error.getErrorMessage(),
                                                                                                         error.getReportedSystemAction(),
                                                                                                         error.getReportedUserAction(),
                                                                                                         userId);
        }
        catch (org.odpi.openmetadata.commonservices.ffdc.exceptions.InvalidParameterException  error)
        {
            throw new org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException(error.getReportedHTTPCode(),
                                                                                                        error.getReportingClassName(),
                                                                                                        error.getReportingActionDescription(),
                                                                                                        error.getErrorMessage(),
                                                                                                        error.getReportedSystemAction(),
                                                                                                        error.getReportedUserAction());
        }
        catch (org.odpi.openmetadata.commonservices.ffdc.exceptions.PropertyServerException error)
        {
            throw new org.odpi.openmetadata.repositoryservices.ffdc.exception.RepositoryErrorException(error.getReportedHTTPCode(),
                                                                                                       error.getReportingClassName(),
                                                                                                       error.getReportingActionDescription(),
                                                                                                       error.getErrorMessage(),
                                                                                                       error.getReportedSystemAction(),
                                                                                                       error.getReportedUserAction());
        }
    }


    /**
     * Remove the instance for this server.
     *
     * @param serverName name of the server
     */
    public void removeInstance(String   serverName)
    {
        super.removeServerServiceInstance(serverName);
    }
}
