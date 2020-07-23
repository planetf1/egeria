/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria category. */
package org.odpi.openmetadata.viewservices.tex.server;


import org.odpi.openmetadata.viewservices.tex.api.rest.TexTypesRequestBody;
import org.odpi.openmetadata.viewservices.tex.api.rest.TypeExplorerResponse;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.tags.Tag;



/**
 * The TexViewRESTResource provides the Spring API endpoints of the Type Explorer Open Metadata View Service (OMVS).
 * This interface provides an interfaces for enterprise architects.
 */

@RestController
@RequestMapping("/servers/{serverName}/open-metadata/view-services/tex/users/{userId}")

@Tag(name="Tex OMVS", description="Explore type information in a repository or cohort for visualization of graphs of related types.", externalDocs=@ExternalDocumentation(description="Tex View Service (OMVS)",url="https://egeria.odpi.org/open-metadata-implementation/view-services/tex-view/"))

public class TexViewRESTResource {

    private TexViewRESTServices restAPI = new TexViewRESTServices();


    /**
     * Default constructor
     */
    public TexViewRESTResource() {
    }


    /**
     * Load type information
     * <p>
     * Load type information from the repository server. This is used to populate filters.
     *
     * @param serverName   name of the server running the view-service.
     * @param userId       user account under which to conduct operation.
     * @param body         request body containing parameters to formulate repository request
     * @return response object containing the repository's type information or exception information
     */
    @PostMapping("/types")
    public TypeExplorerResponse getTypeExplorer(@PathVariable String              serverName,
                                                @PathVariable String              userId,
                                                @RequestBody  TexTypesRequestBody body) {
        return restAPI.getTypeExplorer(serverName, userId, body);

    }

}
