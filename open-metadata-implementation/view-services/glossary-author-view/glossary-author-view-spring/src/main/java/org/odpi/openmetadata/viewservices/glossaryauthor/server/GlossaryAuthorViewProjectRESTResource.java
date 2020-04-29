/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.glossaryauthor.server;


import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.common.SequencingOrder;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.project.Project;
import org.odpi.openmetadata.accessservices.subjectarea.responses.SubjectAreaOMASAPIResponse;
import org.odpi.openmetadata.viewservices.glossaryauthor.services.GlossaryAuthorViewProjectRESTServices;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * The GlossaryAuthorRESTServicesInstance provides the org.odpi.openmetadata.viewservices.glossaryauthor.server  implementation of the Glossary Author Open Metadata
 * View Service (OMVS) for projects.  This interface provides project authoring interfaces for subject area experts.
 */

@RestController
@RequestMapping("/servers/{serverName}/open-metadata/view-services/glossary-author/users/{userId}/projects")

@Tag(name="Glossary Author OMVS", description="Develop a definition of a subject area by authoring glossaries, including terms for use by a user interface.", externalDocs=@ExternalDocumentation(description="Glossary Author View Services (OMVS)",url="https://egeria.odpi.org/open-metadata-implementation/view-services/glossary-author-view/"))

public class GlossaryAuthorViewProjectRESTResource {

    private GlossaryAuthorViewProjectRESTServices restAPI = new GlossaryAuthorViewProjectRESTServices();


    /**
     * Default constructor
     */
    public GlossaryAuthorViewProjectRESTResource() {
    }


    /**
     * Create a Project.
     * <p>
     * Projects with the same name can be confusing. Best practise is to create projects that have unique names.
     * This Create call does not police that Project names are unique. So it is possible to create projects with the same name as each other.
     * <p>
     * Projects that are created using this call will be GlossaryProjects.
     *
     * @param serverName      name of the local server.
     * @param suppliedProject Project to create.
     * @return response, when successful contains the created project.
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised.</li>
     * <li> ClassificationException              Error processing a classification.</li>
     * <li> StatusNotSupportedException          A status value is not supported.</li>
     * </ul>
     */
    @PostMapping()
    public SubjectAreaOMASAPIResponse createProject(@PathVariable String serverName, @PathVariable String userId,
                                                    @RequestBody Project suppliedProject) {
        return restAPI.createProject(serverName, userId, suppliedProject);

    }

    /**
     * Get a project.
     *
     * @param serverName local UI server name
     * @param guid       guid of the project to get
     * @return response which when successful contains the project with the requested guid
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException the requesting user is not authorized to issue this request.</li>
     * <li> MetadataServerUncontactableException  not able to communicate with a Metadata respository service.</li>
     * <li> InvalidParameterException one of the parameters is null or invalid.</li>
     * <li> UnrecognizedGUIDException the supplied guid was not recognised</li>
     * <li> UnrecognizedGUIDException the supplied guid was not recognised</li>
     * <li> FunctionNotSupportedException   Function not supported</li>
     * </ul>
     */
    @GetMapping(path = "/{guid}")
    public SubjectAreaOMASAPIResponse getProject(@PathVariable String serverName, @PathVariable String userId, @PathVariable String guid) {
        return restAPI.getProject(serverName, userId, guid);

    }

    /**
     * Find Project
     *
     * @param serverName         local UI server name
     * @param searchCriteria     String expression matching Project property values .
     * @param asOfTime           the projects returned as they were at this time. null indicates at the current time.
     * @param offset             the starting element number for this set of results.  This is used when retrieving elements
     *                           beyond the first page of results. Zero means the results start from the first element.
     * @param pageSize           the maximum number of elements that can be returned on this request.
     *                           0 means there is no limit to the page size
     * @param sequencingOrder    the sequencing order for the results.
     * @param sequencingProperty the name of the property that should be used to sequence the results.
     * @return A list of projects meeting the search Criteria
     *
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> FunctionNotSupportedException        Function not supported.</li>
     * </ul>
     */
    @GetMapping()
    public SubjectAreaOMASAPIResponse findProject(
            @PathVariable String serverName, @PathVariable String userId,
            @RequestParam(value = "searchCriteria", required = false) String searchCriteria,
            @RequestParam(value = "asOfTime", required = false) Date asOfTime,
            @RequestParam(value = "offset", required = false) Integer offset,
            @RequestParam(value = "pageSize", required = false) Integer pageSize,
            @RequestParam(value = "sequencingOrder", required = false) SequencingOrder sequencingOrder,
            @RequestParam(value = "sequencingProperty", required = false) String sequencingProperty
                                                 ) {
        return restAPI.findProject(serverName, userId, asOfTime, searchCriteria, offset, pageSize, sequencingOrder, sequencingProperty);
    }

    /**
     * Get Project relationships
     *
     * @param serverName         local UI server name
     * @param guid               guid of the project to get
     * @param asOfTime           the relationships returned as they were at this time. null indicates at the current time. If specified, the date is in milliseconds since 1970-01-01 00:00:00.
     * @param offset             the starting element number for this set of results.  This is used when retrieving elements
     *                           beyond the first page of results. Zero means the results start from the first element.
     * @param pageSize           the maximum number of elements that can be returned on this request.
     *                           0 means there is not limit to the page size
     * @param sequencingOrder    the sequencing order for the results.
     * @param sequencingProperty the name of the property that should be used to sequence the results.
     * @return a response which when successful contains the project relationships
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service.</li>
     * </ul>
     */
    @GetMapping(path = "/{guid}/relationships")
    public SubjectAreaOMASAPIResponse getProjectRelationships(
            @PathVariable String serverName, @PathVariable String userId,
            @PathVariable String guid,
            @RequestParam(value = "asOfTime", required = false) Date asOfTime,
            @RequestParam(value = "offset", required = false) Integer offset,
            @RequestParam(value = "pageSize", required = false) Integer pageSize,
            @RequestParam(value = "sequencingOrder", required = false) SequencingOrder sequencingOrder,
            @RequestParam(value = "sequencingProperty", required = false) String sequencingProperty

                                                             ) {
        return restAPI.getProjectRelationships(serverName,
                                               userId,
                                               guid,
                                               asOfTime,
                                               offset,
                                               pageSize,
                                               sequencingOrder,
                                               sequencingProperty);

    }

    /**
     * Update a Project
     * <p>
     * Status is not updated using this call.
     *
     * @param serverName      local UI server name
     * @param guid            guid of the project to update
     * @param suppliedProject project to update
     * @param isReplace       flag to indicate that this update is a replace. When not set only the supplied (non null) fields are updated.
     * @return a response which when successful contains the updated project
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> FunctionNotSupportedException        Function not supported</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service.</li>
     * </ul>
     */
    @PutMapping(path = "/{guid}")
    public SubjectAreaOMASAPIResponse updateProject(
            @PathVariable String serverName, @PathVariable String userId,
            @PathVariable String guid,
            @RequestBody Project suppliedProject,
            @RequestParam(value = "isReplace", required = false) Boolean isReplace) {

        return restAPI.updateProject(serverName, userId, guid, suppliedProject, isReplace);


    }

    /**
     * Delete a Project instance
     * <p>
     * The deletion of a project is only allowed if there is no project content (i.e. no terms or categories).
     * <p>
     * There are 2 types of deletion, a soft delete and a hard delete (also known as a purge). All repositories support hard deletes. Soft deletes support
     * is optional. Soft delete is the default.
     * <p>
     * A soft delete means that the project instance will exist in a deleted state in the repository after the delete operation. This means
     * that it is possible to undo the delete.
     * A hard delete means that the project will not exist after the operation.
     * when not successful the following Exceptions can occur
     *
     * @param serverName local UI server name
     * @param guid       guid of the project to be deleted.
     * @param isPurge    true indicates a hard delete, false is a soft delete.
     * @return a void response
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> FunctionNotSupportedException        Function not supported this indicates that a soft delete was issued but the repository does not support it.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service. There is a problem retrieving properties from the metadata repository.</li>
     * <li> EntityNotDeletedException            a soft delete was issued but the project was not deleted.</li>
     * <li> GUIDNotPurgedException               a hard delete was issued but the project was not purged</li>
     * </ul>
     */
    @DeleteMapping(path = "/{guid}")
    public SubjectAreaOMASAPIResponse deleteProject(@PathVariable String serverName, @PathVariable String userId,
                                                    @PathVariable String guid,
                                                    @RequestParam(value = "isPurge", required = false) Boolean isPurge,
                                                    HttpServletRequest request) {
        return restAPI.deleteProject(serverName, userId, guid, isPurge);
    }

    /**
     * Restore a Project
     * <p>
     * Restore allows the deleted Project to be made active again. Restore allows deletes to be undone. Hard deletes are not stored in the repository so cannot be restored.
     *
     * @param serverName local UI server name
     * @param guid       guid of the project to restore
     * @return response which when successful contains the restored project
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> FunctionNotSupportedException        Function not supported this indicates that a soft delete was issued but the repository does not support it.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service. There is a problem retrieving properties from the metadata repository.</li>
     * </ul>
     */
    @PostMapping(path = "/{guid}")
    public SubjectAreaOMASAPIResponse restoreProject(@PathVariable String serverName, @PathVariable String userId,
                                                     @PathVariable String guid,
                                                     HttpServletRequest request) {
        return restAPI.restoreProject(serverName, userId, guid);

    }
}
