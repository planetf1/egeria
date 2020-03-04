/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adminservices.client;

import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGConfigurationErrorException;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGInvalidParameterException;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGNotAuthorizedException;

import java.util.Map;

/**
 * MetadataServerConfigurationClient provides the configuration client for a metadata server.
 * A metadata server is a metadata access point with a native open metadata repository.
 */
public class MetadataServerConfigurationClient extends MetadataAccessPointConfigurationClient
{
    /**
     * Create a new client with no authentication embedded in the HTTP request.
     *
     * @param adminUserId           administrator's (end user's) userId to associate with calls.
     * @param serverName            name of the server to connect to
     * @param serverPlatformRootURL the network address of the server running the admin services
     * @throws OMAGInvalidParameterException there is a problem creating the client-side components to issue any
     *                                       REST API calls.
     */
    public MetadataServerConfigurationClient(String adminUserId,
                                             String serverName,
                                             String serverPlatformRootURL) throws OMAGInvalidParameterException
    {
        super(adminUserId, serverName, serverPlatformRootURL);
    }


    /**
     * Create a new client that passes a connection userId and password in each HTTP request.  This is the
     * userId/password of the calling server.  The end user's userId is passed as the admin userId.
     *
     * @param adminUserId           administrator's (end user's) userId to associate with calls.
     * @param serverName            name of the server to connect to
     * @param serverPlatformRootURL the network address of the server running the admin services
     * @param connectionUserId      caller's system userId embedded in all HTTP requests
     * @param connectionPassword    caller's system password embedded in all HTTP requests
     * @throws OMAGInvalidParameterException there is a problem creating the client-side components to issue any
     *                                       REST API calls.
     */
    public MetadataServerConfigurationClient(String adminUserId,
                                             String serverName,
                                             String serverPlatformRootURL,
                                             String connectionUserId,
                                             String connectionPassword) throws OMAGInvalidParameterException
    {
        super(adminUserId, serverName, serverPlatformRootURL, connectionUserId, connectionPassword);
    }


    /**
     * Set up an in memory local repository.  This repository uses hashmaps to store content.  It is useful
     * for demos, testing and POCs.
     *
     * @throws OMAGNotAuthorizedException the supplied userId is not authorized to issue this command.
     * @throws OMAGInvalidParameterException invalid parameter.
     * @throws OMAGConfigurationErrorException unusual state in the admin server.
     */
    public void setInMemLocalRepository() throws OMAGNotAuthorizedException,
                                                 OMAGConfigurationErrorException,
                                                 OMAGInvalidParameterException
    {
        final String methodName  = "setInMemLocalRepository";
        final String urlTemplate = "/open-metadata/admin-services/users/{0}/servers/{1}/local-repository/mode/in-memory-repository";

        restClient.callVoidPostRESTCall(methodName,
                                        serverPlatformRootURL + urlTemplate,
                                        nullRequestBody,
                                        adminUserId,
                                        serverName);
    }



    /**
     * Set up a graph store as the local repository.  This graph store uses JanusGraph.  It is scalable with
     * different back ends and can be run in a HA context with multiple versions of the same server deployed
     * to the same repository.
     *
     * @param storageProperties  properties used to configure the back end storage for the graph
     * @throws OMAGNotAuthorizedException the supplied userId is not authorized to issue this command.
     * @throws OMAGInvalidParameterException invalid parameter.
     * @throws OMAGConfigurationErrorException unusual state in the admin server.
     */
    public void setGraphLocalRepository(Map<String, Object> storageProperties) throws OMAGNotAuthorizedException,
                                                                                      OMAGConfigurationErrorException,
                                                                                      OMAGInvalidParameterException
    {
        final String methodName  = "setGraphLocalRepository";
        final String urlTemplate = "/open-metadata/admin-services/users/{0}/servers/{1}/local-repository/mode/local-graph-repository";

        restClient.callVoidPostRESTCall(methodName,
                                        serverPlatformRootURL + urlTemplate,
                                        storageProperties,
                                        adminUserId,
                                        serverName);
    }


    /**
     * Set up an read only local repository.  This repository manages metadata in memory but does not
     * support the ability to store new metadata.  This means it can safely be used to host read only content
     * from an open metadata archive within a production cohort.
     *
     * @throws OMAGNotAuthorizedException the supplied userId is not authorized to issue this command.
     * @throws OMAGInvalidParameterException invalid parameter.
     * @throws OMAGConfigurationErrorException unusual state in the admin server.
     */
    public void setReadOnlyLocalRepository() throws OMAGNotAuthorizedException,
                                                 OMAGConfigurationErrorException,
                                                 OMAGInvalidParameterException
    {
        final String methodName  = "setReadOnlyLocalRepository";
        final String urlTemplate = "/open-metadata/admin-services/users/{0}/servers/{1}/local-repository/mode/read-only-repository";

        restClient.callVoidPostRESTCall(methodName,
                                        serverPlatformRootURL + urlTemplate,
                                        nullRequestBody,
                                        adminUserId,
                                        serverName);
    }
}
