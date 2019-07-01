/*
 *  SPDX-License-Identifier: Apache-2.0
 *  Copyright Contributors to the ODPi Egeria project.
 */
package org.odpi.openmetadata.accessservices.securityofficer.spring;

import org.odpi.openmetadata.accessservices.securityofficer.api.model.SecurityClassification;
import org.odpi.openmetadata.accessservices.securityofficer.api.model.rest.SecurityOfficerOMASAPIResponse;
import org.odpi.openmetadata.accessservices.securityofficer.server.admin.services.SecurityOfficerService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/servers/{serverName}/open-metadata/access-services/security-officer/users/{userId}")
public class SecurityOfficerOMASResource {

    private SecurityOfficerService service = new SecurityOfficerService();

    /**
     * Returns the security tag for the given schema element
     *
     * @param serverName      name of the server instances for this request
     * @param userId          String - userId of user making request.
     * @param schemaElementId unique identifier of the schema element
     */
    @RequestMapping(method = RequestMethod.GET, path = "/security-tag/element/{schemaElementId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public SecurityOfficerOMASAPIResponse getSecurityTagByAssetIdentifier(@PathVariable String serverName, @PathVariable String userId, @PathVariable String schemaElementId) {
        return service.getSecurityTagByAssetId(serverName, userId, schemaElementId);
    }


    /**
     * Save or update the security tag for the given schema element
     *
     * @param serverName                name of the server instances for this request
     * @param userId                    String - userId of user making request.
     * @param securityTagClassification security tag assigned to the asset
     * @param schemaElementId           unique identifier of the schema element
     */
    @RequestMapping(method = RequestMethod.POST, path = "/security-tag/element/{schemaElementId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public SecurityOfficerOMASAPIResponse getSecurityTagByAssetIdentifier(@PathVariable String serverName, @PathVariable String userId,
                                                                          @PathVariable String schemaElementId,
                                                                          @RequestBody SecurityClassification securityTagClassification) {
        return service.updateSecurityTag(serverName, userId, schemaElementId, securityTagClassification);
    }
}
