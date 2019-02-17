/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.userinterface.accessservices;

import org.odpi.openmetadata.accessservices.assetcatalog.client.AssetCatalog;
import org.odpi.openmetadata.http.HttpHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@SpringBootApplication
@Configuration

public class EgeriaUIApplication {

    private static final Logger LOG = LoggerFactory.getLogger(EgeriaUIApplication.class);

    @Value("${strict.ssl}")
    Boolean strictSSL;

    public static void main(String[] args) {
        SpringApplication.run(EgeriaUIApplication.class, args);
    }

    @Bean
    public InitializingBean getInitialize()
    {
        return () -> {
            if (!strictSSL)
            {
                HttpHelper.noStrictSSL();
            }
        };
    }


    @Bean
    public AssetCatalog getAssetCatalog(@Value("${omas.server.url}") String serverUrl,
                                        @Value("${omas.server.name}") String serverName) {
        return new AssetCatalog(serverName, serverUrl);
    }

}
