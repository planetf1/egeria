/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adminservices.spring;

import org.odpi.openmetadata.adminservices.OMAGServerAdminForViewServices;
import org.odpi.openmetadata.adminservices.configuration.properties.OMAGServerClientConfig;
import org.odpi.openmetadata.adminservices.configuration.properties.ViewServiceConfig;
import org.odpi.openmetadata.adminservices.rest.ViewServiceConfigResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.RegisteredOMAGServicesResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.VoidResponse;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * ConfigViewServicesResource provides the configuration for setting up the Open Metadata View
 * Services (OMVSs).
 */
@RestController
@RequestMapping("/open-metadata/admin-services/users/{userId}/servers/{serverName}/view-services")
public class ConfigViewServicesResource
{
    private OMAGServerAdminForViewServices adminAPI = new OMAGServerAdminForViewServices();


    /**
     * Return the list of view services that are configured for this server.
     *
     * @param userId calling user
     * @param serverName name of server
     * @return list of view service descriptions
     */
    @GetMapping(path = "/configuration")
    public RegisteredOMAGServicesResponse getConfiguredViewServices(@PathVariable String userId,
                                                                    @PathVariable String serverName)
    {
        return adminAPI.getConfiguredViewServices(userId, serverName);
    }


    /**
     * Configure a single view service.
     *
     * @param userId  user that is issuing the request.
     * @param serverName       local server name.
     * @param clientConfig     URL root and server name that are used to access the downstream OMAG Server.
     * @param serviceURLMarker string indicating which view service it is configuring
     * @return void response or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGConfigurationErrorException the event bus has not been configured or
     * OMAGInvalidParameterException invalid serverName parameter.
     */
    @PostMapping(path = "/{serviceURLMarker}")
    public VoidResponse configureViewService(@PathVariable  String                 userId,
                                             @PathVariable  String                 serverName,
                                             @PathVariable  String                 serviceURLMarker,
                                             @RequestBody   OMAGServerClientConfig clientConfig)
    {
        return adminAPI.configureViewService(userId, serverName, serviceURLMarker, clientConfig);
    }


    /**
     * Disable a view service.  This removes all configuration for the view service.
     *
     * @param userId  user that is issuing the request.
     * @param serverName  local server name.
     * @param serviceURLMarker string indicating which view service
     * @return view service config response or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException invalid serverName parameter or
     * OMAGConfigurationErrorException unusual state in the admin server.
     */
    @GetMapping(path = "/{serviceURLMarker}")
    public ViewServiceConfigResponse getViewService(@PathVariable String userId,
                                                    @PathVariable String serverName,
                                                    @PathVariable String serviceURLMarker)
    {
        return adminAPI.getViewServiceConfig(userId, serverName, serviceURLMarker);
    }



    /**
     * Disable a view service.  This removes all configuration for the view service.
     *
     * @param userId  user that is issuing the request.
     * @param serverName  local server name.
     * @param serviceURLMarker string indicating which view service
     * @return void response or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException invalid serverName parameter or
     * OMAGConfigurationErrorException unusual state in the admin server.
     */
    @DeleteMapping(path = "/{serviceURLMarker}")
    public VoidResponse clearViewService(@PathVariable String userId,
                                         @PathVariable String serverName,
                                         @PathVariable String serviceURLMarker)
    {
        return adminAPI.clearViewService(userId, serverName, serviceURLMarker);
    }


    /**
     * Enable all view services that are registered with this server platform.
     * The view services are set up to use the default event bus.
     *
     * @param userId      user that is issuing the request.
     * @param serverName  local server name.
     * @param clientConfig URL root and server name that are used to access the downstream OMAG Server.
     * @return void response or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGConfigurationErrorException the event bus has not been configured or
     * OMAGInvalidParameterException invalid serverName parameter.
     */
    @PostMapping()
    public VoidResponse configureAllViewServices(@PathVariable                  String              userId,
                                                 @PathVariable                  String              serverName,
                                                 @RequestBody                   OMAGServerClientConfig clientConfig)
    {
        return adminAPI.configureAllViewServices(userId, serverName, clientConfig);
    }

    /**
     * Disable the view services.  This removes all configuration for the view services.
     *
     * @param userId  user that is issuing the request.
     * @param serverName  local server name.
     * @return void response or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException invalid serverName parameter or
     * OMAGConfigurationErrorException unusual state in the admin server.
     */
    @DeleteMapping()
    public VoidResponse clearAllViewServices(@PathVariable String          userId,
                                             @PathVariable String          serverName)
    {
        return adminAPI.clearAllViewServices(userId, serverName);
    }

    /**
     * Set up the configuration for selected open metadata view services (OMVSs).  This overrides
     * the current default values.
     *
     * @param userId                user that is issuing the request.
     * @param serverName            local server name.
     * @param viewServicesConfig    list of configuration properties for each view service.
     * @return void response or
     * OMAGNotAuthorizedException     the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException invalid serverName or viewServicesConfig parameter or
     * OMAGConfigurationErrorException unusual state in the admin server.
     */
    @PostMapping(path = "/configuration")
    public VoidResponse setViewServicesConfig(@PathVariable String                    userId,
                                              @PathVariable String                    serverName,
                                              @RequestBody  List<ViewServiceConfig> viewServicesConfig)
    {
        return adminAPI.setViewServicesConfig(userId, serverName, viewServicesConfig);
    }
}
