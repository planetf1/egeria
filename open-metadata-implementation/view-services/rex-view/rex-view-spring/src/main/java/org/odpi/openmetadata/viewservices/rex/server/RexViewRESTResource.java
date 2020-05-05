/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria category. */
package org.odpi.openmetadata.viewservices.rex.server;


import org.odpi.openmetadata.viewservices.rex.api.rest.RexEntityDetailResponse;
import org.odpi.openmetadata.viewservices.rex.api.rest.RexEntityRequestBody;
import org.odpi.openmetadata.viewservices.rex.api.rest.RexPreTraversalResponse;
import org.odpi.openmetadata.viewservices.rex.api.rest.RexRelationshipRequestBody;
import org.odpi.openmetadata.viewservices.rex.api.rest.RexRelationshipResponse;
import org.odpi.openmetadata.viewservices.rex.api.rest.RexSearchBody;
import org.odpi.openmetadata.viewservices.rex.api.rest.RexSearchResponse;
import org.odpi.openmetadata.viewservices.rex.api.rest.RexTraversalRequestBody;
import org.odpi.openmetadata.viewservices.rex.api.rest.RexTraversalResponse;
import org.odpi.openmetadata.viewservices.rex.api.rest.RexTypesRequestBody;
import org.odpi.openmetadata.viewservices.rex.api.rest.TypeExplorerResponse;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.tags.Tag;



/**
 * The RexViewRESTResource provides the Spring API endpoints of the Repository Explorer Open Metadata View Service (OMVS).
 * This interface provides an interfaces for enterprise architects.
 */

@RestController
@RequestMapping("/servers/{serverName}/open-metadata/view-services/rex/users/{userId}")

@Tag(name="Rex OMVS", description="Explore instance data in a repository or cohort for visualization of graphs of related items.", externalDocs=@ExternalDocumentation(description="Rex View Services (OMVS)",url="https://egeria.odpi.org/open-metadata-implementation/view-services/rex-view/"))

public class RexViewRESTResource {

    private RexViewRESTServices restAPI = new RexViewRESTServices();


    /**
     * Default constructor
     */
    public RexViewRESTResource() {
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
    public TypeExplorerResponse getTypeExplorer(@PathVariable String serverName,
                                                @PathVariable String userId,
                                                @RequestBody RexTypesRequestBody body) {
        return restAPI.getTypeExplorer(serverName, userId, body);

    }


    /**
     *  This method gets an entity detail.
     *  <p>
     *  When retrieving a single entity we return the whole EntityDetail object. This is
     *  because the entity is being used as the user focus object and will be displayed in
     *  the details pane.
     *  <p>
     *  The method used is POST because the parameters supplied by the UI to the VS are conveyed in
     *  the request body.
     *
     *
     * @param serverName   name of the server running the view-service.
     * @param userId       user account under which to conduct operation.
     * @param body         request body containing parameters to formulate repository request
     * @return response object containing the repository's type information or exception information
     */
    @PostMapping("/instances/entity")
    public RexEntityDetailResponse getEntity(@PathVariable String               serverName,
                                             @PathVariable String               userId,
                                             @RequestBody  RexEntityRequestBody body) {
        return restAPI.getEntity(serverName, userId, body);

    }

    /**
     *  This method gets a relationship.
     *  <p>
     *  When retrieving a single relationship we return the whole Relationship object. This is
     *  because the relationship is being used as the user focus object and will be displayed in
     *  the details pane.
     *  <p>
     *  The method used is POST because the parameters supplied by the UI to the VS are conveyed in
     *  the request body.
     *
     *
     * @param serverName   name of the server running the view-service.
     * @param userId       user account under which to conduct operation.
     * @param body         request body containing parameters to formulate repository request
     * @return response object containing the repository's type information or exception information
     */
    @PostMapping("/instances/relationship")
    public RexRelationshipResponse getRelationship(@PathVariable String                     serverName,
                                                   @PathVariable String                     userId,
                                                   @RequestBody  RexRelationshipRequestBody body) {
        return restAPI.getRelationship(serverName, userId, body);

    }


    /**
     *  This method searches for entities based on property value.
     *  <p>
     *  When searching for entities, we return a list of EntityDigest objects. This is
     *  because the digests are used to display a list of search hits to the user
     *  from which they can select which entities they would like to add to the graph.
     *  <p>
     *  The method used is POST because the parameters supplied by the UI to the VS are conveyed in
     *  the request body.
     *
     *
     * @param serverName   name of the server running the view-service.
     * @param userId       user account under which to conduct operation.
     * @param body         request body containing parameters to formulate repository request
     * @return response object containing the repository's type information or exception information
     */
    @PostMapping("/instances/entities/by-property-value")
    public RexSearchResponse findEntities(@PathVariable String         serverName,
                                          @PathVariable String         userId,
                                          @RequestBody  RexSearchBody   body) {
        return restAPI.findEntities(serverName, userId, body);

    }

    /**
     *  This method searches for relationships based on property value.
     *  <p>
     *  When searching for relationships, we return a list of RelationshipDigest objects. This is
     *  because the digests are used to display a list of search hits to the user
     *  from which they can select which relationships they would like to add to the graph.
     *  <p>
     *  The method used is POST because the parameters supplied by the UI to the VS are conveyed in
     *  the request body.
     *
     *
     * @param serverName   name of the server running the view-service.
     * @param userId       user account under which to conduct operation.
     * @param body         request body containing parameters to formulate repository request
     * @return response object containing the repository's type information or exception information
     */
    @PostMapping("/instances/relationships/by-property-value")
    public RexSearchResponse findRelationships(@PathVariable String         serverName,
                                               @PathVariable String         userId,
                                               @RequestBody  RexSearchBody  body) {
        return restAPI.findRelationships(serverName, userId, body);

    }

    /**
     *  This method retrieves the neighborhood around a starting entity for pre-traversal
     *  <p>
     *  When exploring an entity neighborhood we return an InstanceGraph which contains
     *  the entities and relationships that were traversed.
     *  <p>
     *  The method used is POST because the parameters supplied by the UI to the VS are conveyed in
     *  the request body.
     *
     *
     * @param serverName   name of the server running the view-service.
     * @param userId       user account under which to conduct operation.
     * @param body         request body containing parameters to formulate repository request
     * @return response object containing the InstanceGraph for the traversal or exception information
     */
    @PostMapping("/instances/rex-pre-traversal")
    public RexPreTraversalResponse rexPreTraversal(@PathVariable String                 serverName,
                                                   @PathVariable String                 userId,
                                                   @RequestBody RexTraversalRequestBody body) {
        return restAPI.rexPreTraversal(serverName, userId, body);



    }

    /**
     *  This method retrieves the neighborhood around a starting entity.
     *  <p>
     *  When exploring an entity neighborhood we return an InstanceGraph which contains
     *  the entities and relationships that were traversed.
     *  <p>
     *  The method used is POST because the parameters supplied by the UI to the VS are conveyed in
     *  the request body.
     *
     *
     * @param serverName   name of the server running the view-service.
     * @param userId       user account under which to conduct operation.
     * @param body         request body containing parameters to formulate repository request
     * @return response object containing the InstanceGraph for the traversal or exception information
     */
    @PostMapping("/instances/rex-traversal")
    public RexTraversalResponse rexTraversal(@PathVariable String                 serverName,
                                             @PathVariable String                 userId,
                                             @RequestBody RexTraversalRequestBody body) {
        return restAPI.rexTraversal(serverName, userId, body);



    }
}
