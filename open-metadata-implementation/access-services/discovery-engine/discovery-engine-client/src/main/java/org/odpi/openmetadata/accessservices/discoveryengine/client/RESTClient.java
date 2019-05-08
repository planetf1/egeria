/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.discoveryengine.client;

import org.odpi.openmetadata.accessservices.discoveryengine.rest.*;
import org.odpi.openmetadata.commonservices.ffdc.rest.FFDCRESTClient;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;


/**
 * RESTClient is responsible for issuing calls to the OMAS REST APIs.
 */
class RESTClient extends FFDCRESTClient
{
    /**
     * Constructor for no authentication.
     *
     * @param serverName name of the OMAG Server to call
     * @param serverPlatformURLRoot URL root of the server platform where the OMAG Server is running.
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    RESTClient(String serverName,
               String serverPlatformURLRoot) throws InvalidParameterException
    {
        super(serverName, serverPlatformURLRoot);
    }


    /**
     * Constructor for simple userId and password authentication.
     *
     * @param serverName name of the OMAG Server to call
     * @param serverPlatformURLRoot URL root of the server platform where the OMAG Server is running.
     * @param userId user id for the HTTP request
     * @param password password for the HTTP request
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    RESTClient(String serverName,
               String serverPlatformURLRoot,
               String userId,
               String password) throws InvalidParameterException
    {
        super(serverName, serverPlatformURLRoot, userId, password);
    }


    /**
     * Issue a GET REST call that returns a DiscoveryEnginePropertiesResponse object.
     *
     * @param methodName  name of the method being called.
     * @param urlTemplate template of the URL for the REST API call with place-holders for the parameters.
     * @param params      a list of parameters that are slotted into the url template.
     *
     * @return DiscoveryEnginePropertiesResponse
     * @throws PropertyServerException something went wrong with the REST call stack.
     */
    DiscoveryEnginePropertiesResponse callDiscoveryEnginePropertiesGetRESTCall(String    methodName,
                                                                               String    urlTemplate,
                                                                               Object... params) throws PropertyServerException
    {
        return this.callGetRESTCall(methodName, DiscoveryEnginePropertiesResponse.class, urlTemplate, params);
    }


    /**
     * Issue a GET REST call that returns a DiscoveryEngineListResponse object.
     *
     * @param methodName  name of the method being called.
     * @param urlTemplate template of the URL for the REST API call with place-holders for the parameters.
     * @param params      a list of parameters that are slotted into the url template.
     *
     * @return DiscoveryEngineListResponse
     * @throws PropertyServerException something went wrong with the REST call stack.
     */
    DiscoveryEngineListResponse callDiscoveryEngineListGetRESTCall(String    methodName,
                                                                   String    urlTemplate,
                                                                   Object... params) throws PropertyServerException
    {
        return this.callGetRESTCall(methodName, DiscoveryEngineListResponse.class, urlTemplate, params);
    }


    /**
     * Issue a GET REST call that returns a DiscoveryServicePropertiesResponse object.
     *
     * @param methodName  name of the method being called.
     * @param urlTemplate template of the URL for the REST API call with place-holders for the parameters.
     * @param params      a list of parameters that are slotted into the url template.
     *
     * @return DiscoveryServicePropertiesResponse
     * @throws PropertyServerException something went wrong with the REST call stack.
     */
    DiscoveryServicePropertiesResponse callDiscoveryServicePropertiesGetRESTCall(String    methodName,
                                                                                 String    urlTemplate,
                                                                                 Object... params) throws PropertyServerException
    {
        return this.callGetRESTCall(methodName, DiscoveryServicePropertiesResponse.class, urlTemplate, params);
    }


    /**
     * Issue a GET REST call that returns a DiscoveryEngineListResponse object.
     *
     * @param methodName  name of the method being called.
     * @param urlTemplate template of the URL for the REST API call with place-holders for the parameters.
     * @param params      a list of parameters that are slotted into the url template.
     *
     * @return DiscoveryEngineListResponse
     * @throws PropertyServerException something went wrong with the REST call stack.
     */
    DiscoveryServiceListResponse callDiscoveryServiceListGetRESTCall(String    methodName,
                                                                     String    urlTemplate,
                                                                     Object... params) throws PropertyServerException
    {
        return this.callGetRESTCall(methodName, DiscoveryServiceListResponse.class, urlTemplate, params);
    }


    /**
     * Issue a GET REST call that returns a RegisteredDiscoveryServiceResponse object.
     *
     * @param methodName  name of the method being called.
     * @param urlTemplate template of the URL for the REST API call with place-holders for the parameters.
     * @param params      a list of parameters that are slotted into the url template.
     *
     * @return RegisteredDiscoveryServiceResponse
     * @throws PropertyServerException something went wrong with the REST call stack.
     */
    RegisteredDiscoveryServiceResponse callRegisteredDiscoveryServiceGetRESTCall(String    methodName,
                                                                                 String    urlTemplate,
                                                                                 Object... params) throws PropertyServerException
    {
        return this.callGetRESTCall(methodName, RegisteredDiscoveryServiceResponse.class, urlTemplate, params);
    }
}
