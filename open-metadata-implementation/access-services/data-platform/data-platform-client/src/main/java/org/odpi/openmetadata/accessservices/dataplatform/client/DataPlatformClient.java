/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataplatform.client;

import org.odpi.openmetadata.accessservices.dataplatform.DataPlatformInterface;
import org.odpi.openmetadata.accessservices.dataplatform.properties.SoftwareServerCapability;
import org.odpi.openmetadata.accessservices.dataplatform.properties.asset.DeployedDatabaseSchema;
import org.odpi.openmetadata.accessservices.dataplatform.responses.DeployedDatabaseSchemaRequestBody;
import org.odpi.openmetadata.accessservices.dataplatform.responses.RegistrationRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
import org.odpi.openmetadata.commonservices.ffdc.rest.GUIDResponse;
import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.client.OCFRESTClient;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;

/**
 * DataPlatformClient provides the client implementation for the Data Platform OMAS.
 */

public class DataPlatformClient extends OCFRESTClient implements DataPlatformInterface {

    private static final String QUALIFIED_NAME_PARAMETER = "qualifiedName";
    private static final String SOFTWARE_SERVER_CAPABILITY_URL_TEMPLATE = "/servers/{0}/open-metadata/access-services" +
            "/data-platform/users/{1}/software-server-capabilities";
    private static final String DEPLOYED_DATABASE_SCHEMA_URL_TEMPLATE = "/servers/{0}/open-metadata/access-services" +
            "/data-platform/users/{1}/deployed-database-schema";


    private String serverName;
    private String serverPlatformRootURL;

    private InvalidParameterHandler invalidParameterHandler = new InvalidParameterHandler();
    private RESTExceptionHandler exceptionHandler = new RESTExceptionHandler();

    /**
     * Constructor for no authentication.
     *
     * @param serverName            name of the OMAG Server to call
     * @param serverPlatformURLRoot URL root of the server platform where the OMAG Server is running.
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     *                                   REST API calls.
     */
    public DataPlatformClient(String serverName, String serverPlatformURLRoot) throws InvalidParameterException {
        super(serverName, serverPlatformURLRoot);
        this.serverName = serverName;
        this.serverPlatformRootURL = serverPlatformURLRoot;
    }


    /**
     * Constructor for simple userId and password authentication.
     *
     * @param serverName            name of the OMAG Server to call
     * @param serverPlatformURLRoot URL root of the server platform where the OMAG Server is running.
     * @param userId                user id for the HTTP request
     * @param password              password for the HTTP request
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     *                                   REST API calls.
     */
    public DataPlatformClient(String serverName, String serverPlatformURLRoot, String userId, String password) throws InvalidParameterException {
        super(serverName, serverPlatformURLRoot, userId, password);
        this.serverName = serverName;
        this.serverPlatformRootURL = serverPlatformURLRoot;
    }

    /**
     * Create the software server capability entity for registering data platforms.
     *
     * @param userId                   the name of the calling user
     * @param softwareServerCapability the software server capability bean
     * @return unique identifier of the server in the repository
     * @throws InvalidParameterException  the bean properties are invalid
     * @throws PropertyServerException    problem accessing the property server
     */
    @Override
    public GUIDResponse createSoftwareServerCapability(String userId, SoftwareServerCapability softwareServerCapability) throws InvalidParameterException, PropertyServerException {

            final String methodName = "createSoftwareServerCapability";

            invalidParameterHandler.validateUserId(userId, methodName);

            RegistrationRequestBody requestBody = new RegistrationRequestBody();
            requestBody.setSoftwareServerCapability(softwareServerCapability);

            return callGUIDPostRESTCall(userId, methodName, SOFTWARE_SERVER_CAPABILITY_URL_TEMPLATE, requestBody);
    }

    /**
     * Create deployed database schema asset.
     *
     * @param userId                 the user id
     * @param deployedDatabaseSchema the deployed database schema
     * @return the string
     * @throws InvalidParameterException  the invalid parameter exception
     * @throws PropertyServerException    the property server exception
     */
    @Override
    public GUIDResponse createDeployedDatabaseSchema(String userId, DeployedDatabaseSchema deployedDatabaseSchema) throws InvalidParameterException, PropertyServerException {

        final String methodName = "createDeployedDatabaseSchema";

        invalidParameterHandler.validateUserId(userId, methodName);

        DeployedDatabaseSchemaRequestBody requestBody = new DeployedDatabaseSchemaRequestBody();
        requestBody.setDeployedDatabaseSchema(deployedDatabaseSchema);

        return callGUIDPostRESTCall(userId, methodName, DEPLOYED_DATABASE_SCHEMA_URL_TEMPLATE, requestBody);
    }
}
