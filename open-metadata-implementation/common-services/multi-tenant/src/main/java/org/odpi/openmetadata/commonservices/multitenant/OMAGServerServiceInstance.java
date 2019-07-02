/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.multitenant;

import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.multitenant.ffdc.OMAGServerInstanceErrorCode;
import org.odpi.openmetadata.commonservices.multitenant.ffdc.exceptions.NewInstanceException;
import org.odpi.openmetadata.metadatasecurity.server.OpenMetadataServerSecurityVerifier;

/**
 * OMAGServerServiceInstance represents an instance of a service in an OMAG Server.
 * It is also responsible for registering itself in the instance map.
 */
public abstract class OMAGServerServiceInstance
{
    protected String serverName;
    protected String serviceName;

    protected InvalidParameterHandler invalidParameterHandler = new InvalidParameterHandler();

    private OMAGServerPlatformInstanceMap platformInstanceMap = new OMAGServerPlatformInstanceMap();

    protected OpenMetadataServerSecurityVerifier securityVerifier = new OpenMetadataServerSecurityVerifier();

    /**
     * Default constructor
     *
     * @param serverName name of the new server
     * @param serviceName name of the new service instance
     */
    public OMAGServerServiceInstance(String   serverName,
                                     String   serviceName)
    {
        this.serverName = serverName;
        this.serviceName = serviceName;

        if (serverName != null)
        {
            platformInstanceMap.addServiceInstanceToPlatform(serverName, serviceName, this);
        }
    }


    /**
     * Set the server name.
     *
     * @param serverName name of this OMAG server
     */
    protected void setServerName(String serverName)
    {
        this.serverName = serverName;

        if (serverName != null)
        {
            platformInstanceMap.addServiceInstanceToPlatform(serverName, serviceName, this);
        }
    }


    /**
     * Set up a new security verifier (the handler runs with a default verifier until this
     * method is called).
     *
     * The security verifier provides authorization checks for access and maintenance
     * changes to open metadata.  Authorization checks are enabled through the
     * OpenMetadataServerSecurityConnector.
     *
     * @param securityVerifier new security verifier
     */
    public void setSecurityVerifier(OpenMetadataServerSecurityVerifier securityVerifier)
    {
        if (securityVerifier != null)
        {
            this.securityVerifier = securityVerifier;
        }
    }



    /**
     * Override the default maximum paging size.
     *
     * @param maxPagingSize new value
     */
    public void setMaxPagingSize(int maxPagingSize)
    {
        invalidParameterHandler.setMaxPagingSize(maxPagingSize);
    }


    /**
     * Return the server name.
     *
     * @return serverName name of the server for this instance
     * @throws NewInstanceException a problem occurred during initialization
     */
    public String getServerName() throws NewInstanceException
    {
        final String methodName = "getServerName";

        if (serverName != null)
        {
            return serverName;
        }
        else
        {
            OMAGServerInstanceErrorCode errorCode    = OMAGServerInstanceErrorCode.SERVER_NAME_NOT_AVAILABLE;
            String                      errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(methodName);

            throw new NewInstanceException(errorCode.getHTTPErrorCode(),
                                           this.getClass().getName(),
                                           methodName,
                                           errorMessage,
                                           errorCode.getSystemAction(),
                                           errorCode.getUserAction());
        }
    }


    /**
     * Return this service's official name.
     *
     * @return name
     */
    public String getServiceName()
    {
        return serviceName;
    }


    /**
     * Return the handler for validating parameters.
     *
     * @return invalid parameter handler
     */
    public InvalidParameterHandler getInvalidParameterHandler()
    {
        return invalidParameterHandler;
    }


    /**
     * Unregister this instance from the instance map.
     */
    public void shutdown()
    {
        platformInstanceMap.removeServiceInstanceFromPlatform(serverName, serviceName);
    }
}
