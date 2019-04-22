/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.discoveryengine.client;

import org.odpi.openmetadata.accessservices.discoveryengine.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.accessservices.discoveryengine.rest.*;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.OwnerType;
import org.odpi.openmetadata.frameworks.discovery.DiscoveryConfigurationServer;
import org.odpi.openmetadata.frameworks.discovery.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.discovery.properties.DiscoveryEngineProperties;
import org.odpi.openmetadata.frameworks.discovery.properties.DiscoveryServiceProperties;
import org.odpi.openmetadata.frameworks.discovery.properties.RegisteredDiscoveryService;

import java.util.List;
import java.util.Map;

public class DiscoveryConfigurationClient extends DiscoveryConfigurationServer
{
    private String     serverName;               /* Initialized in constructor */
    private String     omasServerURL;            /* Initialized in constructor */
    private RESTClient restClient;               /* Initialized in constructor */

    private InvalidParameterHandler invalidParameterHandler = new InvalidParameterHandler();
    private RESTExceptionHandler    exceptionHandler        = new RESTExceptionHandler();
    private NullRequestBody         nullRequestBody         = new NullRequestBody();


    /**
     * Create a new client with no authentication embedded in the HTTP request.
     *
     * @param serverName name of the server to connect to
     * @param newServerURL the network address of the server running the OMAS REST servers
     */
    public DiscoveryConfigurationClient(String     serverName,
                                        String     newServerURL)
    {
        this.serverName = serverName;
        this.omasServerURL = newServerURL;
        this.restClient = new RESTClient(serverName, omasServerURL);
    }


    /**
     * Create a new client that passes userId and password in each HTTP request.  This is the
     * userId/password of the calling server.  The end user's userId is sent on each request.
     *
     * @param serverName name of the server to connect to
     * @param omasServerURL the network address of the server running the OMAS REST servers
     * @param userId caller's userId embedded in all HTTP requests
     * @param password caller's userId embedded in all HTTP requests
     */
    public DiscoveryConfigurationClient(String     serverName,
                                        String     omasServerURL,
                                        String     userId,
                                        String     password)
    {
        this.serverName = serverName;
        this.omasServerURL = omasServerURL;
        this.restClient = new RESTClient(serverName, omasServerURL, userId, password);
    }


    /**
     * Create a new discovery engine definition.
     *
     * @param userId identifier of calling user
     * @param qualifiedName unique name for the discovery engine.
     * @param displayName display name for messages and user interfaces.
     * @param description description of the types of discovery services that will be associated with
     *                    this discovery engine.
     *
     * @return unique identifier (guid) of the discovery engine definition.  This is for use on other requests.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem storing the discovery engine definition.
     */
    public  String  createDiscoveryEngine(String  userId,
                                          String  qualifiedName,
                                          String  displayName,
                                          String  description) throws InvalidParameterException,
                                                                      UserNotAuthorizedException,
                                                                      PropertyServerException
    {
        final String   methodName = "createDiscoveryEngine";
        final String   nameParameterName = "qualifiedName";
        final String   urlTemplate = "/servers/{0}/open-metadata/access-services/discovery-engine/users/{1}/discovery-engines";

        invalidParameterHandler.validateOMASServerURL(omasServerURL,methodName);
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(qualifiedName, nameParameterName, methodName);

        NewDiscoveryEngineRequestBody requestBody = new NewDiscoveryEngineRequestBody();
        requestBody.setQualifiedName(qualifiedName);
        requestBody.setDisplayName(displayName);
        requestBody.setDescription(description);

        GUIDResponse restResult = restClient.callGUIDPostRESTCall(methodName,
                                                                  omasServerURL + urlTemplate,
                                                                  requestBody,
                                                                  serverName,
                                                                  userId);

        exceptionHandler.detectAndThrowInvalidParameterException(methodName, restResult);
        exceptionHandler.detectAndThrowUserNotAuthorizedException(methodName, restResult);
        exceptionHandler.detectAndThrowPropertyServerException(methodName, restResult);

        return restResult.getGUID();
    }


    /**
     * Return the properties from a discovery engine definition.
     *
     * @param userId identifier of calling user
     * @param guid unique identifier (guid) of the discovery engine definition.
     * @return properties from the discovery engine definition.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem retrieving the discovery engine definition.
     */
    public  DiscoveryEngineProperties getDiscoveryEngineByGUID(String    userId,
                                                               String    guid) throws InvalidParameterException,
                                                                                      UserNotAuthorizedException,
                                                                                      PropertyServerException
    {
        final String   methodName = "getDiscoveryEngineByGUID";
        final String   guidParameterName = "guid";
        final String   urlTemplate = "/servers/{0}/open-metadata/access-services/discovery-engine/users/{1}/discovery-engines/{2}";

        invalidParameterHandler.validateOMASServerURL(omasServerURL,methodName);
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(guid, guidParameterName, methodName);

        DiscoveryEnginePropertiesResponse restResult = restClient.callDiscoveryEnginePropertiesGetRESTCall(methodName,
                                                                                                           omasServerURL + urlTemplate,
                                                                                                           serverName,
                                                                                                           userId,
                                                                                                           guid);

        exceptionHandler.detectAndThrowInvalidParameterException(methodName, restResult);
        exceptionHandler.detectAndThrowUserNotAuthorizedException(methodName, restResult);
        exceptionHandler.detectAndThrowPropertyServerException(methodName, restResult);

        return restResult.getDiscoveryEngineProperties();
    }


    /**
     * Return the properties from a discovery engine definition.
     *
     * @param userId identifier of calling user
     * @param name qualified name or display name (if unique).
     * @return properties from the discovery engine definition.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem retrieving the discovery engine definition.
     */
    public  DiscoveryEngineProperties getDiscoveryEngineByName(String    userId,
                                                               String    name) throws InvalidParameterException,
                                                                                      UserNotAuthorizedException,
                                                                                      PropertyServerException
    {
        final String   methodName = "getDiscoveryEngineByName";
        final String   nameParameterName = "name";
        final String   urlTemplate = "/servers/{0}/open-metadata/access-services/discovery-engine/users/{1}/discovery-engines/by-name/{2}";

        invalidParameterHandler.validateOMASServerURL(omasServerURL,methodName);
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(name, nameParameterName, methodName);

        DiscoveryEnginePropertiesResponse restResult = restClient.callDiscoveryEnginePropertiesGetRESTCall(methodName,
                                                                                                           omasServerURL + urlTemplate,
                                                                                                           serverName,
                                                                                                           userId,
                                                                                                           name);

        exceptionHandler.detectAndThrowInvalidParameterException(methodName, restResult);
        exceptionHandler.detectAndThrowUserNotAuthorizedException(methodName, restResult);
        exceptionHandler.detectAndThrowPropertyServerException(methodName, restResult);

        return restResult.getDiscoveryEngineProperties();
    }


    /**
     * Return the list of discovery engine definitions that are stored.
     *
     * @param userId identifier of calling user
     * @param startingFrom initial position in the stored list.
     * @param maximumResults maximum number of definitions to return on this call.
     * @return list of discovery engine definitions.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem retrieving the discovery engine definitions.
     */
    public  List<DiscoveryEngineProperties> getAllDiscoveryEngines(String  userId,
                                                                   int     startingFrom,
                                                                   int     maximumResults) throws InvalidParameterException,
                                                                                                  UserNotAuthorizedException,
                                                                                                  PropertyServerException
    {
        final String   methodName = "getAllDiscoveryEngines";
        final String   urlTemplate = "/servers/{0}/open-metadata/access-services/discovery-engine/users/{1}/discovery-engines?startingFrom={2}&maximumResults={3}";

        invalidParameterHandler.validateOMASServerURL(omasServerURL,methodName);
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validatePaging(startingFrom, maximumResults, methodName);

        DiscoveryEngineListResponse restResult = restClient.callDiscoveryEngineListGetRESTCall(methodName,
                                                                                               omasServerURL + urlTemplate,
                                                                                               serverName,
                                                                                               userId,
                                                                                               Integer.toString(startingFrom),
                                                                                               Integer.toString(maximumResults));

        exceptionHandler.detectAndThrowInvalidParameterException(methodName, restResult);
        exceptionHandler.detectAndThrowUserNotAuthorizedException(methodName, restResult);
        exceptionHandler.detectAndThrowPropertyServerException(methodName, restResult);

        return restResult.getDiscoveryEngines();
    }


    /**
     * Update the properties of an existing discovery engine definition.  Use the current value to
     * keep a property value the same, or use the new value.  Null means remove the property from
     * the definition.
     *
     * @param userId identifier of calling user
     * @param guid unique identifier of the discovery engine - used to locate the definition.
     * @param qualifiedName new value for unique name of discovery engine.
     * @param displayName new value for the display name.
     * @param description new description for the discovery engine.
     * @param typeDescription new description of the type ofg discovery engine.
     * @param version new version number for the discovery engine implementation.
     * @param patchLevel new patch level for the discovery engine implementation.
     * @param source new source description for the implementation of the discovery engine.
     * @param additionalProperties additional properties for the discovery engine.
     * @param extendedProperties properties to populate the subtype of the discovery engine.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem storing the discovery engine definition.
     */
    public  void    updateDiscoveryEngine(String                userId,
                                          String                guid,
                                          String                qualifiedName,
                                          String                displayName,
                                          String                description,
                                          String                typeDescription,
                                          String                version,
                                          String                patchLevel,
                                          String                source,
                                          Map<String, String>   additionalProperties,
                                          Map<String, Object>   extendedProperties) throws InvalidParameterException,
                                                                                           UserNotAuthorizedException,
                                                                                           PropertyServerException
    {
        final String   methodName = "updateDiscoveryEngine";
        final String   guidParameterName = "guid";
        final String   nameParameterName = "qualifiedName";
        final String   urlTemplate = "/servers/{0}/open-metadata/access-services/discovery-engine/users/{1}/discovery-engines/{2}";

        invalidParameterHandler.validateOMASServerURL(omasServerURL,methodName);
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(guid, guidParameterName, methodName);
        invalidParameterHandler.validateName(qualifiedName, nameParameterName, methodName);

        UpdateDiscoveryEngineRequestBody requestBody = new UpdateDiscoveryEngineRequestBody();
        requestBody.setQualifiedName(qualifiedName);
        requestBody.setDisplayName(displayName);
        requestBody.setDescription(description);
        requestBody.setTypeDescription(typeDescription);
        requestBody.setVersion(version);
        requestBody.setPatchLevel(patchLevel);
        requestBody.setSource(source);
        requestBody.setAdditionalProperties(additionalProperties);
        requestBody.setExtendedProperties(extendedProperties);

        VoidResponse restResult = restClient.callVoidPostRESTCall(methodName,
                                                                  omasServerURL + urlTemplate,
                                                                  requestBody,
                                                                  serverName,
                                                                  userId,
                                                                  guid);

        exceptionHandler.detectAndThrowInvalidParameterException(methodName, restResult);
        exceptionHandler.detectAndThrowUserNotAuthorizedException(methodName, restResult);
        exceptionHandler.detectAndThrowPropertyServerException(methodName, restResult);
    }


    /**
     * Remove the properties of the discovery engine.  Both the guid and the qualified name is supplied
     * to validate that the correct discovery engine is being deleted.
     *
     * @param userId identifier of calling user
     * @param guid unique identifier of the discovery engine - used to locate the definition.
     * @param qualifiedName unique name for the discovery engine.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem retrieving the discovery engine definition.
     */
    public  void    deleteDiscoveryEngine(String  userId,
                                          String  guid,
                                          String  qualifiedName) throws InvalidParameterException,
                                                                        UserNotAuthorizedException,
                                                                        PropertyServerException
    {
        final String   methodName = "deleteDiscoveryEngine";
        final String   guidParameterName = "guid";
        final String   nameParameterName = "qualifiedName";
        final String   urlTemplate = "/servers/{0}/open-metadata/access-services/discovery-engine/users/{1}/discovery-engines/{2}";

        invalidParameterHandler.validateOMASServerURL(omasServerURL,methodName);
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(guid, guidParameterName, methodName);
        invalidParameterHandler.validateName(qualifiedName, nameParameterName, methodName);

        DeleteRequestBody requestBody = new DeleteRequestBody();
        requestBody.setQualifiedName(qualifiedName);

        VoidResponse restResult = restClient.callVoidPostRESTCall(methodName,
                                                                  omasServerURL + urlTemplate,
                                                                  requestBody,
                                                                  serverName,
                                                                  userId,
                                                                  guid);

        exceptionHandler.detectAndThrowInvalidParameterException(methodName, restResult);
        exceptionHandler.detectAndThrowUserNotAuthorizedException(methodName, restResult);
        exceptionHandler.detectAndThrowPropertyServerException(methodName, restResult);
    }


    /**
     * Create a discovery service definition.  The same discovery service can be associated with multiple
     * discovery engines.
     *
     * @param userId identifier of calling user
     * @param qualifiedName  unique name for the discovery service.
     * @param displayName   display name for the discovery service.
     * @param description  description of the analysis provided by the discovery service.
     * @param connection   connection to instanciate the discovery service implementation.
     *
     * @return unique identifier of the discovery service.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem storing the discovery service definition.
     */
    public  String  createDiscoveryService(String     userId,
                                           String     qualifiedName,
                                           String     displayName,
                                           String     description,
                                           Connection connection) throws InvalidParameterException,
                                                                         UserNotAuthorizedException,
                                                                         PropertyServerException
    {
        final String   methodName = "createDiscoveryService";
        final String   nameParameterName = "qualifiedName";
        final String   connectionParameterName = "connection";
        final String   urlTemplate = "/servers/{0}/open-metadata/access-services/discovery-engine/users/{1}/discovery-services";

        invalidParameterHandler.validateOMASServerURL(omasServerURL,methodName);
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(qualifiedName, nameParameterName, methodName);
        invalidParameterHandler.validateConnection(connection, connectionParameterName, methodName);

        NewDiscoveryServiceRequestBody requestBody = new NewDiscoveryServiceRequestBody();
        requestBody.setQualifiedName(qualifiedName);
        requestBody.setDisplayName(displayName);
        requestBody.setDescription(description);
        requestBody.setConnection(connection);

        GUIDResponse restResult = restClient.callGUIDPostRESTCall(methodName,
                                                                  omasServerURL + urlTemplate,
                                                                  requestBody,
                                                                  serverName,
                                                                  userId);

        exceptionHandler.detectAndThrowInvalidParameterException(methodName, restResult);
        exceptionHandler.detectAndThrowUserNotAuthorizedException(methodName, restResult);
        exceptionHandler.detectAndThrowPropertyServerException(methodName, restResult);

        return restResult.getGUID();
    }


    /**
     * Return the properties from a discovery service definition.
     *
     * @param userId identifier of calling user
     * @param guid unique identifier (guid) of the discovery service definition.
     *
     * @return properties of the discovery service.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem retrieving the discovery service definition.
     */
    public  DiscoveryServiceProperties getDiscoveryServiceByGUID(String    userId,
                                                                 String    guid) throws InvalidParameterException,
                                                                                        UserNotAuthorizedException,
                                                                                        PropertyServerException
    {
        final String   methodName = "getDiscoveryServiceByGUID";
        final String   guidParameterName = "guid";
        final String   urlTemplate = "/servers/{0}/open-metadata/access-services/discovery-engine/users/{1}/discovery-services/{2}";

        invalidParameterHandler.validateOMASServerURL(omasServerURL,methodName);
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(guid, guidParameterName, methodName);

        DiscoveryServicePropertiesResponse restResult = restClient.callDiscoveryServicePropertiesGetRESTCall(methodName,
                                                                                                             omasServerURL + urlTemplate,
                                                                                                             serverName,
                                                                                                             userId,
                                                                                                             guid);

        exceptionHandler.detectAndThrowInvalidParameterException(methodName, restResult);
        exceptionHandler.detectAndThrowUserNotAuthorizedException(methodName, restResult);
        exceptionHandler.detectAndThrowPropertyServerException(methodName, restResult);

        return restResult.getDiscoveryServiceProperties();
    }


    /**
     * Return the properties from a discovery service definition.
     *
     * @param userId identifier of calling user
     * @param name qualified name or display name (if unique).
     *
     * @return properties from the discovery engine definition.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem retrieving the discovery engine definition.
     */
    public  DiscoveryServiceProperties getDiscoveryServiceByName(String    userId,
                                                                 String    name) throws InvalidParameterException,
                                                                                        UserNotAuthorizedException,
                                                                                        PropertyServerException
    {
        final String   methodName = "getDiscoveryServiceByName";
        final String   nameParameterName = "name";
        final String   urlTemplate = "/servers/{0}/open-metadata/access-services/discovery-engine/users/{1}/discovery-services/by-name/{2}";

        invalidParameterHandler.validateOMASServerURL(omasServerURL,methodName);
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(name, nameParameterName, methodName);

        DiscoveryServicePropertiesResponse restResult = restClient.callDiscoveryServicePropertiesGetRESTCall(methodName,
                                                                                                             omasServerURL + urlTemplate,
                                                                                                             serverName,
                                                                                                             userId,
                                                                                                             name);

        exceptionHandler.detectAndThrowInvalidParameterException(methodName, restResult);
        exceptionHandler.detectAndThrowUserNotAuthorizedException(methodName, restResult);
        exceptionHandler.detectAndThrowPropertyServerException(methodName, restResult);

        return restResult.getDiscoveryServiceProperties();
    }


    /**
     * Return the list of discovery services definitions that are stored.
     *
     * @param userId identifier of calling user
     * @param startingFrom initial position in the stored list.
     * @param maximumResults maximum number of definitions to return on this call.
     *
     * @return list of discovery service definitions.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem retrieving the discovery service definitions.
     */
    public  List<DiscoveryServiceProperties> getAllDiscoveryServices(String  userId,
                                                                     int     startingFrom,
                                                                     int     maximumResults) throws InvalidParameterException,
                                                                                                    UserNotAuthorizedException,
                                                                                                    PropertyServerException
    {
        final String   methodName = "getAllDiscoveryServices";
        final String   urlTemplate = "/servers/{0}/open-metadata/access-services/discovery-engine/users/{1}/discovery-services?startingFrom={2}&maximumResults={3}";

        invalidParameterHandler.validateOMASServerURL(omasServerURL,methodName);
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validatePaging(startingFrom, maximumResults, methodName);

        DiscoveryServiceListResponse restResult = restClient.callDiscoveryServiceListGetRESTCall(methodName,
                                                                                                 omasServerURL + urlTemplate,
                                                                                                 serverName,
                                                                                                 userId,
                                                                                                 Integer.toString(startingFrom),
                                                                                                 Integer.toString(maximumResults));

        exceptionHandler.detectAndThrowInvalidParameterException(methodName, restResult);
        exceptionHandler.detectAndThrowUserNotAuthorizedException(methodName, restResult);
        exceptionHandler.detectAndThrowPropertyServerException(methodName, restResult);

        return restResult.getDiscoveryServices();
    }


    /**
     * Return the list of discovery engines that a specific discovery service is registered with.
     *
     * @param userId identifier of calling user
     * @param discoveryServiceGUID discovery service to search for.
     *
     * @return list of discovery engine unique identifiers (guids)
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem retrieving the discovery service and/or discovery engine definitions.
     */
    public  List<String>  getDiscoveryServiceRegistrations(String   userId,
                                                           String   discoveryServiceGUID) throws InvalidParameterException,
                                                                                                 UserNotAuthorizedException,
                                                                                                 PropertyServerException
    {
        final  String   methodName = "getAllDiscoveryServices";
        final  String   guidParameter = "discoveryServiceGUID";
        final  String   urlTemplate = "/servers/{0}/open-metadata/access-services/discovery-engine/users/{1}/discovery-services/{2}/registrations";

        invalidParameterHandler.validateOMASServerURL(omasServerURL,methodName);
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(discoveryServiceGUID, guidParameter, methodName);

        GUIDListResponse restResult = restClient.callGUIDListGetRESTCall(methodName,
                                                                         omasServerURL + urlTemplate,
                                                                         serverName,
                                                                         userId,
                                                                         discoveryServiceGUID);

        exceptionHandler.detectAndThrowInvalidParameterException(methodName, restResult);
        exceptionHandler.detectAndThrowUserNotAuthorizedException(methodName, restResult);
        exceptionHandler.detectAndThrowPropertyServerException(methodName, restResult);

        return restResult.getGUIDs();
    }


    /**
     * Update the properties of an existing discovery service definition.  Use the current value to
     * keep a property value the same, or use the new value.  Null means remove the property from
     * the definition.
     *
     * @param userId identifier of calling user
     * @param guid unique identifier of the discovery service - used to locate the definition.
     * @param qualifiedName new value for unique name of discovery service.
     * @param displayName new value for the display name.
     * @param shortDescription new value for the short description.
     * @param description new value for the description.
     * @param owner new owner of the discovery service.
     * @param ownerType new type for the owner of the discovery service.
     * @param latestChange short description of this update.
     * @param zoneMembership new list of zones for this discovery service.
     * @param connection connection used to create an instance of this discovery service.
     * @param additionalProperties additional properties for the discovery engine.
     * @param extendedProperties properties to populate the subtype of the discovery service.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem storing the discovery service definition.
     */
    public  void    updateDiscoveryService(String                userId,
                                           String                guid,
                                           String                qualifiedName,
                                           String                displayName,
                                           String                shortDescription,
                                           String                description,
                                           String                owner,
                                           OwnerType             ownerType,
                                           List<String>          zoneMembership,
                                           String                latestChange,
                                           Connection            connection,
                                           Map<String, String>   additionalProperties,
                                           Map<String, Object>   extendedProperties) throws InvalidParameterException,
                                                                                            UserNotAuthorizedException,
                                                                                            PropertyServerException
    {
        final String   methodName = "updateDiscoveryService";
        final String   guidParameterName = "guid";
        final String   nameParameterName = "qualifiedName";
        final String   connectionParameterName = "connection";
        final String   urlTemplate = "/servers/{0}/open-metadata/access-services/discovery-engine/users/{1}/discovery-services/{2}";

        invalidParameterHandler.validateOMASServerURL(omasServerURL,methodName);
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(guid, guidParameterName, methodName);
        invalidParameterHandler.validateName(qualifiedName, nameParameterName, methodName);
        invalidParameterHandler.validateConnection(connection, connectionParameterName, methodName);

        UpdateDiscoveryServiceRequestBody requestBody = new UpdateDiscoveryServiceRequestBody();
        requestBody.setQualifiedName(qualifiedName);
        requestBody.setDisplayName(displayName);
        requestBody.setShortDescription(shortDescription);
        requestBody.setDescription(description);
        requestBody.setOwner(owner);
        requestBody.setOwnerType(ownerType);
        requestBody.setZoneMembership(zoneMembership);
        requestBody.setLatestChange(latestChange);
        requestBody.setConnection(connection);
        requestBody.setAdditionalProperties(additionalProperties);
        requestBody.setExtendedProperties(extendedProperties);

        VoidResponse restResult = restClient.callVoidPostRESTCall(methodName,
                                                                  omasServerURL + urlTemplate,
                                                                  requestBody,
                                                                  serverName,
                                                                  userId,
                                                                  guid);

        exceptionHandler.detectAndThrowInvalidParameterException(methodName, restResult);
        exceptionHandler.detectAndThrowUserNotAuthorizedException(methodName, restResult);
        exceptionHandler.detectAndThrowPropertyServerException(methodName, restResult);
    }


    /**
     * Remove the properties of the discovery service.  Both the guid and the qualified name is supplied
     * to validate that the correct discovery service is being deleted.  The discovery service is also
     * unregistered from its discovery engines.
     *
     * @param userId identifier of calling user
     * @param guid unique identifier of the discovery service - used to locate the definition.
     * @param qualifiedName unique name for the discovery service.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem retrieving the discovery service definition.
     */
    public  void    deleteDiscoveryService(String  userId,
                                           String  guid,
                                           String  qualifiedName) throws InvalidParameterException,
                                                                         UserNotAuthorizedException,
                                                                         PropertyServerException
    {
        final String   methodName = "deleteDiscoveryService";
        final String   guidParameterName = "guid";
        final String   nameParameterName = "qualifiedName";
        final String   urlTemplate = "/servers/{0}/open-metadata/access-services/discovery-engine/users/{1}/discovery-services/{2}";

        invalidParameterHandler.validateOMASServerURL(omasServerURL,methodName);
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(guid, guidParameterName, methodName);
        invalidParameterHandler.validateName(qualifiedName, nameParameterName, methodName);

        DeleteRequestBody requestBody = new DeleteRequestBody();
        requestBody.setQualifiedName(qualifiedName);

        VoidResponse restResult = restClient.callVoidPostRESTCall(methodName,
                                                                  omasServerURL + urlTemplate,
                                                                  requestBody,
                                                                  serverName,
                                                                  userId,
                                                                  guid);

        exceptionHandler.detectAndThrowInvalidParameterException(methodName, restResult);
        exceptionHandler.detectAndThrowUserNotAuthorizedException(methodName, restResult);
        exceptionHandler.detectAndThrowPropertyServerException(methodName, restResult);
    }


    /**
     * Register a discovery service with a specific discovery engine.
     *
     * @param userId identifier of calling user
     * @param discoveryEngineGUID unique identifier of the discovery engine.
     * @param discoveryServiceGUID unique identifier of the discovery service.
     * @param assetTypes list of asset types that this discovery service is able to process.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem retrieving the discovery service and/or discovery engine definitions.
     */
    public  void  registerDiscoveryServiceWithEngine(String        userId,
                                                     String        discoveryEngineGUID,
                                                     String        discoveryServiceGUID,
                                                     List<String>  assetTypes) throws InvalidParameterException,
                                                                                      UserNotAuthorizedException,
                                                                                      PropertyServerException
    {
        final String methodName = "registerDiscoveryServiceWithEngine";
        final String discoveryEngineGUIDParameter = "discoveryEngineGUID";
        final String discoveryServiceGUIDParameter = "discoveryServiceGUID";
        final String assetTypesParameter = "assetTypes";
        final String urlTemplate = "/servers/{0}/open-metadata/access-services/discovery-engine/users/{1}/discovery-engines/{2}/discovery-services";

        invalidParameterHandler.validateOMASServerURL(omasServerURL,methodName);
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(discoveryEngineGUID, discoveryEngineGUIDParameter, methodName);
        invalidParameterHandler.validateGUID(discoveryServiceGUID, discoveryServiceGUIDParameter, methodName);
        invalidParameterHandler.validateStringArray(assetTypes, assetTypesParameter, methodName);

        DiscoveryServiceRegistrationRequestBody requestBody = new DiscoveryServiceRegistrationRequestBody();
        requestBody.setDiscoveryServiceGUID(discoveryServiceGUID);
        requestBody.setAssetTypes(assetTypes);

        VoidResponse restResult = restClient.callVoidPostRESTCall(methodName,
                                                                  omasServerURL + urlTemplate,
                                                                  requestBody,
                                                                  serverName,
                                                                  userId,
                                                                  discoveryEngineGUID);

        exceptionHandler.detectAndThrowInvalidParameterException(methodName, restResult);
        exceptionHandler.detectAndThrowUserNotAuthorizedException(methodName, restResult);
        exceptionHandler.detectAndThrowPropertyServerException(methodName, restResult);
    }


    /**
     * Retrieve a specific discovery service registered with a discovery engine.
     *
     * @param userId identifier of calling user
     * @param discoveryEngineGUID unique identifier of the discovery engine.
     * @param discoveryServiceGUID unique identifier of the discovery service.
     *
     * @return details of the discovery service and the asset types it is registered for.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem retrieving the discovery service and/or discovery engine definitions.
     */
    public RegisteredDiscoveryService getRegisteredDiscoveryService(String  userId,
                                                                    String  discoveryEngineGUID,
                                                                    String  discoveryServiceGUID) throws InvalidParameterException,
                                                                                                         UserNotAuthorizedException,
                                                                                                         PropertyServerException
    {
        final String methodName = "getRegisteredDiscoveryService";
        final String discoveryEngineGUIDParameter = "discoveryEngineGUID";
        final String discoveryServiceGUIDParameter = "discoveryServiceGUID";
        final String urlTemplate = "/servers/{0}/open-metadata/access-services/discovery-engine/users/{1}/discovery-engines/{2}/discovery-services/{3}";

        invalidParameterHandler.validateOMASServerURL(omasServerURL,methodName);
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(discoveryEngineGUID, discoveryEngineGUIDParameter, methodName);
        invalidParameterHandler.validateGUID(discoveryServiceGUID, discoveryServiceGUIDParameter, methodName);

        RegisteredDiscoveryServiceResponse restResult = restClient.callRegisteredDiscoveryServiceGetRESTCall(methodName,
                                                                                                             omasServerURL + urlTemplate,
                                                                                                             serverName,
                                                                                                             userId,
                                                                                                             discoveryEngineGUID,
                                                                                                             discoveryServiceGUID);

        exceptionHandler.detectAndThrowInvalidParameterException(methodName, restResult);
        exceptionHandler.detectAndThrowUserNotAuthorizedException(methodName, restResult);
        exceptionHandler.detectAndThrowPropertyServerException(methodName, restResult);

        return restResult.getRegisteredDiscoveryService();
    }


    /**
     * Retrieve the identifiers of the discovery services registered with a discovery engine.
     *
     * @param userId identifier of calling user
     * @param discoveryEngineGUID unique identifier of the discovery engine.
     * @param startingFrom initial position in the stored list.
     * @param maximumResults maximum number of definitions to return on this call.
     *
     * @return list of unique identifiers
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem retrieving the discovery service and/or discovery engine definitions.
     */
    public  List<String>  getRegisteredDiscoveryServices(String  userId,
                                                         String  discoveryEngineGUID,
                                                         int     startingFrom,
                                                         int     maximumResults) throws InvalidParameterException,
                                                                                        UserNotAuthorizedException,
                                                                                        PropertyServerException
    {
        final String methodName = "getRegisteredDiscoveryServices";
        final String discoveryEngineGUIDParameter = "discoveryEngineGUID";
        final String urlTemplate = "/servers/{0}/open-metadata/access-services/discovery-engine/users/{1}/discovery-engines/{2}/discovery-services?startingFrom={3}&maximumResults={4}";

        invalidParameterHandler.validateOMASServerURL(omasServerURL,methodName);
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(discoveryEngineGUID, discoveryEngineGUIDParameter, methodName);
        invalidParameterHandler.validatePaging(startingFrom, maximumResults, methodName);

        GUIDListResponse restResult = restClient.callGUIDListGetRESTCall(methodName,
                                                                         omasServerURL + urlTemplate,
                                                                         serverName,
                                                                         userId,
                                                                         discoveryEngineGUID,
                                                                         startingFrom,
                                                                         maximumResults);

        exceptionHandler.detectAndThrowInvalidParameterException(methodName, restResult);
        exceptionHandler.detectAndThrowUserNotAuthorizedException(methodName, restResult);
        exceptionHandler.detectAndThrowPropertyServerException(methodName, restResult);

        return restResult.getGUIDs();
    }


    /**
     * Unregister a discovery service from the discovery engine.
     *
     * @param userId identifier of calling user
     * @param discoveryEngineGUID unique identifier of the discovery engine.
     * @param discoveryServiceGUID unique identifier of the discovery service.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem retrieving the discovery service and/or discovery engine definitions.
     */
    public  void  unregisterDiscoveryServiceFromEngine(String        userId,
                                                       String        discoveryEngineGUID,
                                                       String        discoveryServiceGUID) throws InvalidParameterException,
                                                                                                  UserNotAuthorizedException,
                                                                                                  PropertyServerException
    {
        final String methodName = "unregisterDiscoveryServiceFromEngine";
        final String discoveryEngineGUIDParameter = "discoveryEngineGUID";
        final String discoveryServiceGUIDParameter = "discoveryServiceGUID";
        final String urlTemplate = "/servers/{0}/open-metadata/access-services/discovery-engine/users/{1}/discovery-engines/{2}/discovery-services/{3}/delete";

        invalidParameterHandler.validateOMASServerURL(omasServerURL,methodName);
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(discoveryEngineGUID, discoveryEngineGUIDParameter, methodName);
        invalidParameterHandler.validateGUID(discoveryServiceGUID, discoveryServiceGUIDParameter, methodName);

        VoidResponse restResult = restClient.callVoidPostRESTCall(methodName,
                                                                  omasServerURL + urlTemplate,
                                                                  nullRequestBody,
                                                                  serverName,
                                                                  userId,
                                                                  discoveryEngineGUID,
                                                                  discoveryServiceGUID);

        exceptionHandler.detectAndThrowInvalidParameterException(methodName, restResult);
        exceptionHandler.detectAndThrowUserNotAuthorizedException(methodName, restResult);
        exceptionHandler.detectAndThrowPropertyServerException(methodName, restResult);
    }
}
