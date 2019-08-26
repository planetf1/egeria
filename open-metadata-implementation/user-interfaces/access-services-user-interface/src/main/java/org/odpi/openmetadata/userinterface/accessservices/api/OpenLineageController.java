/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.userinterface.accessservices.api;


import org.odpi.openmetadata.accessservices.assetcatalog.exception.InvalidParameterException;
import org.odpi.openmetadata.governanceservers.openlineage.model.Graph;
import org.odpi.openmetadata.governanceservers.openlineage.model.Scope;
import org.odpi.openmetadata.userinterface.accessservices.service.OpenLineageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/api/lineage")
public class OpenLineageController {

    @Autowired
    private OpenLineageService openLineageService;

    @RequestMapping(method = RequestMethod.GET)
    public String getMockGraph(String userId){
        return openLineageService.generateMockGraph(userId);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/export")
    public Map<String, Object> exportGraph(String userId, @RequestParam Graph graph){
        Map<String, Object> exportedGraph;
        try {
            exportedGraph = openLineageService.exportGraph(userId);
        } catch (IOException e) {
            handleException(e);
            return null;
        }
        return exportedGraph;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/entities/{guid}/ultimate-source")
    public Map<String, Object> ultimateSourceGraph(String userId, @PathVariable("guid") String guid, @RequestParam Scope scope){
        Map<String, Object> exportedGraph;
        try {
            exportedGraph = openLineageService.getUltimateSource(userId, scope, guid);
        } catch (IOException e) {
            handleException(e);
            return null;
        }
        return exportedGraph;
    }
    @RequestMapping(method = RequestMethod.GET, value = "/entities/{guid}/end2end")
    public Map<String, Object> endToEndLineage(String userId, @PathVariable("guid") String guid, @RequestParam Scope scope){
        Map<String, Object> exportedGraph;
        try {
            exportedGraph = openLineageService.getEndToEndLineage(userId, scope, guid);
        } catch (IOException e) {
            handleException(e);
            return null;
        }
        return exportedGraph;
    }

    //TODO use global exception handler
    private void handleException(Exception e){
        if(e instanceof InvalidParameterException){
            throw new IllegalArgumentException(e.getMessage());
        }
        throw new RuntimeException("Unknown exception! " + e.getMessage());
    }

}
