/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adminservices.spring;

import org.odpi.openmetadata.adminservices.OMAGServerDataPlatformService;
import org.odpi.openmetadata.adminservices.configuration.properties.DataPlatformServicesConfig;
import org.odpi.openmetadata.commonservices.ffdc.rest.VoidResponse;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/open-metadata/admin-services/users/{userId}/servers/{serverName}")
public class DataPlatformServiceResource {


    private OMAGServerDataPlatformService adminAPI = new OMAGServerDataPlatformService();

    @RequestMapping(method = RequestMethod.POST, path = "/data-platform-service/configuration")
    public VoidResponse setAccessServicesConfig(@PathVariable String userId,
                                                @PathVariable String serverName,
                                                @RequestBody DataPlatformServicesConfig dataPlatformServicesConfig)
    {
        return adminAPI.setDataPlatformServiceConfig(userId, serverName, dataPlatformServicesConfig);
    }

    @RequestMapping(method = RequestMethod.POST, path = "/data-platform-service")
    public VoidResponse enableDataPlatformService(@PathVariable String userId,
                                                  @PathVariable String serverName)
    {
        return adminAPI.enableDataPlatformService(userId, serverName);
    }
}
