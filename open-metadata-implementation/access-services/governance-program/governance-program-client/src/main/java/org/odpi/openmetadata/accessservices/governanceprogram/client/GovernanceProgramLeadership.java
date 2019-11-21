/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.governanceprogram.client;


import org.odpi.openmetadata.accessservices.governanceprogram.api.GovernanceLeadershipInterface;
import org.odpi.openmetadata.accessservices.governanceprogram.ffdc.exceptions.*;
import org.odpi.openmetadata.accessservices.governanceprogram.properties.ExternalReference;
import org.odpi.openmetadata.accessservices.governanceprogram.properties.GovernanceDomain;
import org.odpi.openmetadata.accessservices.governanceprogram.properties.GovernanceOfficer;
import org.odpi.openmetadata.accessservices.governanceprogram.properties.PersonalProfile;
import org.odpi.openmetadata.accessservices.governanceprogram.rest.*;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.ffdc.rest.GUIDResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.VoidResponse;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * GovernanceProgramLeadership provides the client-side interface for the Governance Program Open Metadata Access Service (OMAS).
 * This client, manages all of the interaction with an open metadata repository.  It is initialized with the URL
 * of the server that is running the Open Metadata Access Services.  This server is responsible for locating and
 * managing the governance program definitions exchanged with this client.
 */
public class GovernanceProgramLeadership  implements GovernanceLeadershipInterface
{
    private String                      serverName;       /* Initialized in constructor */
    private String                      omasServerURL;    /* Initialized in constructor */
    private GovernanceProgramRESTClient restClient;       /* Initialized in constructor */

    private InvalidParameterHandler invalidParameterHandler = new InvalidParameterHandler();
    private RESTExceptionHandler    exceptionHandler        = new RESTExceptionHandler();


    /**
     * Create a new client with no authentication embedded in the HTTP request.
     *
     * @param serverName name of the server to connect to
     * @param omasServerURL the network address of the server running the OMAS REST servers
     *
     * @throws InvalidParameterException bad input parameters
     */
    public GovernanceProgramLeadership(String     serverName,
                                       String     omasServerURL) throws InvalidParameterException
    {
        final String methodName = "Constructor (no security)";

        invalidParameterHandler.validateOMAGServerPlatformURL(omasServerURL, serverName, methodName);

        this.serverName = serverName;
        this.omasServerURL = omasServerURL;
        this.restClient = new GovernanceProgramRESTClient(serverName, omasServerURL);
    }


    /**
     * Create a new client that passes userId and password in each HTTP request.  This is the
     * userId/password of the calling server.  The end user's userId is sent on each request.
     *
     * @param serverName name of the server to connect to
     * @param omasServerURL the network address of the server running the OMAS REST servers
     * @param userId caller's userId embedded in all HTTP requests
     * @param password caller's userId embedded in all HTTP requests
     *
     * @throws InvalidParameterException bad input parameters
     */
    public GovernanceProgramLeadership(String     serverName,
                                       String     omasServerURL,
                                       String     userId,
                                       String     password) throws InvalidParameterException
    {
        final String methodName = "Constructor (with security)";

        invalidParameterHandler.validateOMAGServerPlatformURL(omasServerURL, serverName, methodName);

        this.serverName = serverName;
        this.omasServerURL = omasServerURL;
        this.restClient = new GovernanceProgramRESTClient(serverName, omasServerURL, userId, password);
    }


    /**
     * Create a personal profile for an individual who is to be appointed to a governance role but does not
     * have a profile in open metadata.
     *
     * @param userId the name of the calling user.
     * @param profileUserId userId of the individual whose profile this is.
     * @param employeeNumber personnel/serial/unique employee number of the individual.
     * @param fullName full name of the person.
     * @param knownName known name or nickname of the individual.
     * @param jobTitle job title of the individual.
     * @param jobRoleDescription job description of the individual.
     * @param additionalProperties  additional properties about the individual.
     * @return Unique identifier for the personal profile.
     * @throws InvalidParameterException the employee number or known name is null.
     * @throws PropertyServerException the server is not available.
     * @throws UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    public String createPersonalProfile(String              userId,
                                        String              profileUserId,
                                        String              employeeNumber,
                                        String              fullName,
                                        String              knownName,
                                        String              jobTitle,
                                        String              jobRoleDescription,
                                        Map<String, String> additionalProperties) throws InvalidParameterException,
                                                                                         PropertyServerException,
                                                                                         UserNotAuthorizedException
    {
        final String   methodName = "createPersonalProfile";
        final String   urlTemplate = "/servers/{0}/open-metadata/access-services/governance-program/users/{1}/leadership/personal-profiles";

        final String   profileUserIdParameterName = "profileUserId";
        final String   employeeNumberParameterName = "employeeNumber";
        final String   knownNameParameterName = "knownName";

        invalidParameterHandler.validateOMAGServerPlatformURL(omasServerURL, serverName, methodName);
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(profileUserId, profileUserIdParameterName, methodName);
        invalidParameterHandler.validateName(employeeNumber, employeeNumberParameterName, methodName);
        invalidParameterHandler.validateName(knownName, knownNameParameterName, methodName);

        PersonalDetailsRequestBody  requestBody = new PersonalDetailsRequestBody();
        requestBody.setUserId(profileUserId);
        requestBody.setEmployeeNumber(employeeNumber);
        requestBody.setFullName(fullName);
        requestBody.setKnownName(knownName);
        requestBody.setJobTitle(jobTitle);
        requestBody.setJobRoleDescription(jobRoleDescription);
        requestBody.setAdditionalProperties(additionalProperties);


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
     * Update properties for the personal properties.  Null values result in empty fields in the profile.
     *
     * @param userId the name of the calling user.
     * @param profileGUID unique identifier for the profile.
     * @param employeeNumber personnel/serial/unique employee number of the individual. Used to verify the profileGUID.
     * @param fullName full name of the person.
     * @param knownName known name or nickname of the individual.
     * @param jobTitle job title of the individual.
     * @param jobRoleDescription job description of the individual.
     * @param additionalProperties  additional properties about the individual.
     * @throws InvalidParameterException the known name is null or the employeeNumber does not match the profileGUID.
     * @throws PropertyServerException the server is not available.
     * @throws UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    public void   updatePersonalProfile(String              userId,
                                        String              profileGUID,
                                        String              employeeNumber,
                                        String              fullName,
                                        String              knownName,
                                        String              jobTitle,
                                        String              jobRoleDescription,
                                        Map<String, String> additionalProperties) throws InvalidParameterException,
                                                                                         PropertyServerException,
                                                                                         UserNotAuthorizedException
    {
        final String   methodName = "updatePersonalProfile";
        final String   urlTemplate = "/servers/{0}/open-metadata/access-services/governance-program/users/{1}/leadership/personal-profiles/{2}";

        final String   guidParameterName = "profileGUID";
        final String   employeeNumberParameterName = "employeeNumber";
        final String   knownNameParameterName = "knownName";


        invalidParameterHandler.validateOMAGServerPlatformURL(omasServerURL, serverName, methodName);
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(profileGUID, guidParameterName, methodName);
        invalidParameterHandler.validateName(employeeNumber, employeeNumberParameterName, methodName);
        invalidParameterHandler.validateName(knownName, knownNameParameterName, methodName);

        PersonalDetailsRequestBody  requestBody = new PersonalDetailsRequestBody();
        requestBody.setEmployeeNumber(employeeNumber);
        requestBody.setFullName(fullName);
        requestBody.setKnownName(knownName);
        requestBody.setJobTitle(jobTitle);
        requestBody.setJobRoleDescription(jobRoleDescription);
        requestBody.setAdditionalProperties(additionalProperties);


        VoidResponse restResult = restClient.callVoidPostRESTCall(methodName,
                                                                  omasServerURL + urlTemplate,
                                                                  requestBody,
                                                                  serverName,
                                                                  userId,
                                                                  profileGUID);

        exceptionHandler.detectAndThrowInvalidParameterException(methodName, restResult);
        exceptionHandler.detectAndThrowUserNotAuthorizedException(methodName, restResult);
        exceptionHandler.detectAndThrowPropertyServerException(methodName, restResult);
    }


    /**
     * Delete the personal profile.
     *
     * @param userId the name of the calling user.
     * @param profileGUID unique identifier for the profile.
     * @param employeeNumber personnel/serial/unique employee number of the individual.
     * @throws InvalidParameterException the employee number or full name is null.
     * @throws PropertyServerException the server is not available.
     * @throws UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    public void   deletePersonalProfile(String              userId,
                                        String              profileGUID,
                                        String              employeeNumber) throws InvalidParameterException,
                                                                                   PropertyServerException,
                                                                                   UserNotAuthorizedException
    {
        final String   methodName = "deletePersonalProfile";
        final String   urlTemplate = "/servers/{0}/open-metadata/access-services/governance-program/users/{1}/leadership/personal-profiles/{2}/delete";

        final String   guidParameterName = "profileGUID";
        final String   employeeNumberParameterName = "employeeNumber";

        invalidParameterHandler.validateOMAGServerPlatformURL(omasServerURL, serverName, methodName);
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(profileGUID, guidParameterName, methodName);
        invalidParameterHandler.validateName(employeeNumber, employeeNumberParameterName, methodName);

        PersonalProfileValidatorRequestBody  requestBody = new PersonalProfileValidatorRequestBody();
        requestBody.setEmployeeNumber(employeeNumber);

        VoidResponse restResult = restClient.callVoidPostRESTCall(methodName,
                                                                  omasServerURL + urlTemplate,
                                                                  requestBody,
                                                                  serverName,
                                                                  userId,
                                                                  profileGUID);

        exceptionHandler.detectAndThrowInvalidParameterException(methodName, restResult);
        exceptionHandler.detectAndThrowUserNotAuthorizedException(methodName, restResult);
        exceptionHandler.detectAndThrowPropertyServerException(methodName, restResult);
    }


    /**
     * Retrieve a personal profile by guid.
     *
     * @param userId the name of the calling user.
     * @param profileGUID unique identifier for the profile.
     * @return personal profile object.
     * @throws InvalidParameterException the unique identifier of the personal profile is either null or invalid.
     * @throws PropertyServerException the server is not available.
     * @throws UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    public PersonalProfile getPersonalProfileByGUID(String        userId,
                                                    String        profileGUID) throws InvalidParameterException,
                                                                                      PropertyServerException,
                                                                                      UserNotAuthorizedException
    {
        final String   methodName = "getPersonalProfileByGUID";
        final String   urlTemplate = "/servers/{0}/open-metadata/access-services/governance-program/users/{1}/leadership/personal-profiles/{2}";

        final String   guidParameterName = "profileGUID";

        invalidParameterHandler.validateOMAGServerPlatformURL(omasServerURL, serverName, methodName);
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(profileGUID, guidParameterName, methodName);

        PersonalProfileResponse restResult = restClient.callPersonalProfileGetRESTCall(methodName,
                                                                                       omasServerURL + urlTemplate,
                                                                                       serverName,
                                                                                       userId,
                                                                                       profileGUID);

        exceptionHandler.detectAndThrowInvalidParameterException(methodName, restResult);
        exceptionHandler.detectAndThrowUserNotAuthorizedException(methodName, restResult);
        exceptionHandler.detectAndThrowPropertyServerException(methodName, restResult);

        return restResult.getPersonalProfile();
    }


    /**
     * Retrieve a personal profile by personnel/serial/unique employee number of the individual.
     *
     * @param userId the name of the calling user.
     * @param employeeNumber personnel/serial/unique employee number of the individual.
     * @return personal profile object.
     * @throws InvalidParameterException the employee number is null.
     * @throws EmployeeNumberNotUniqueException more than one personal profile was found.
     * @throws PropertyServerException the server is not available.
     * @throws UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    public PersonalProfile getPersonalProfileByEmployeeNumber(String         userId,
                                                              String         employeeNumber) throws InvalidParameterException,
                                                                                                    EmployeeNumberNotUniqueException,
                                                                                                    PropertyServerException,
                                                                                                    UserNotAuthorizedException
    {
        final String   methodName = "getPersonalProfileByEmployeeNumber";
        final String   urlTemplate = "/servers/{0}/open-metadata/access-services/governance-program/users/{1}/leadership/personal-profiles/by-employee-number/{2}";

        final String   employeeNumberParameterName = "employeeNumber";

        invalidParameterHandler.validateOMAGServerPlatformURL(omasServerURL, serverName, methodName);
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(employeeNumber, employeeNumberParameterName, methodName);

        PersonalProfileResponse restResult = restClient.callPersonalProfileGetRESTCall(methodName,
                                                                                       omasServerURL + urlTemplate,
                                                                                       serverName,
                                                                                       userId,
                                                                                       employeeNumber);

        exceptionHandler.detectAndThrowInvalidParameterException(methodName, restResult);
        exceptionHandler.detectAndThrowEmployeeNumberNotUniqueException(methodName, restResult);
        exceptionHandler.detectAndThrowUserNotAuthorizedException(methodName, restResult);
        exceptionHandler.detectAndThrowPropertyServerException(methodName, restResult);

        return restResult.getPersonalProfile();
    }


    /**
     * Return a list of candidate personal profiles for an individual.  It matches on full name and known name.
     * The name may include wild card parameters.
     *
     * @param userId the name of the calling user.
     * @param name name of individual.
     * @return list of personal profile objects.
     * @throws InvalidParameterException the name is null.
     * @throws PropertyServerException the server is not available.
     * @throws UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    public List<PersonalProfile> getPersonalProfilesByName(String        userId,
                                                           String        name) throws InvalidParameterException,
                                                                                      PropertyServerException,
                                                                                      UserNotAuthorizedException
    {
        final String   methodName = "getPersonalProfilesByName";
        final String   urlTemplate = "/servers/{0}/open-metadata/access-services/governance-program/users/{1}/leadership/personal-profiles/by-name/{2}";

        final String   nameParameterName = "name";

        invalidParameterHandler.validateOMAGServerPlatformURL(omasServerURL, serverName, methodName);
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(name, nameParameterName, methodName);

        PersonalProfileListResponse restResult = restClient.callPersonalProfileListGetRESTCall(methodName,
                                                                                               omasServerURL + urlTemplate,
                                                                                               serverName,
                                                                                               userId,
                                                                                               name);

        exceptionHandler.detectAndThrowInvalidParameterException(methodName, restResult);
        exceptionHandler.detectAndThrowUserNotAuthorizedException(methodName, restResult);
        exceptionHandler.detectAndThrowPropertyServerException(methodName, restResult);

        return restResult.getPersonalProfiles();
    }


    /**
     * Create the governance officer appointment.
     *
     * @param userId the name of the calling user.
     * @param governanceDomain  the governance domain for the governance officer.
     * @param appointmentId  the unique identifier of the governance officer.
     * @param appointmentContext  the context in which the governance officer is appointed.
     *                            This may be an organizational scope, location, or scope of assets.
     * @param title job title for the governance officer.
     * @param additionalProperties additional properties for the governance officer.
     * @param externalReferences links to addition information.  This could be, for example, the home page
     *                           for the governance officer, or details of the role.
     * @return Unique identifier (guid) of the governance officer.
     * @throws InvalidParameterException the governance domain, title or appointment id is null.
     * @throws PropertyServerException the server is not available.
     * @throws UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    public String createGovernanceOfficer(String                     userId,
                                          GovernanceDomain           governanceDomain,
                                          String                     appointmentId,
                                          String                     appointmentContext,
                                          String                     title,
                                          Map<String, String>        additionalProperties,
                                          List<ExternalReference>    externalReferences) throws InvalidParameterException,
                                                                                                PropertyServerException,
                                                                                                UserNotAuthorizedException
    {
        final String   methodName = "createGovernanceOfficer";
        final String   urlTemplate = "/servers/{0}/open-metadata/access-services/governance-program/users/{1}/leadership/governance-officers";

        final String   appointmentIdParameterName = "appointmentId";
        final String   titleParameterName = "title";
        final String   governanceDomainParameterName = "governanceDomain";

        invalidParameterHandler.validateOMAGServerPlatformURL(omasServerURL, serverName, methodName);
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(appointmentId, appointmentIdParameterName, methodName);
        invalidParameterHandler.validateName(title, titleParameterName, methodName);
        exceptionHandler.validateGovernanceDomain(governanceDomain, governanceDomainParameterName, methodName);

        GovernanceOfficerDetailsRequestBody  requestBody = new GovernanceOfficerDetailsRequestBody();
        requestBody.setGovernanceDomain(governanceDomain);
        requestBody.setAppointmentId(appointmentId);
        requestBody.setAppointmentContext(appointmentContext);
        requestBody.setTitle(title);
        requestBody.setAdditionalProperties(additionalProperties);
        requestBody.setExternalReferences(externalReferences);

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
     * Update selected fields for the governance officer.
     *
     * @param userId the name of the calling user.
     * @param governanceOfficerGUID unique identifier (guid) of the governance officer.
     * @param governanceDomain  the governance domain for the governance officer.
     * @param appointmentId  the unique identifier of the governance officer.
     * @param appointmentContext  the context in which the governance officer is appointed.
     *                            This may be an organizational scope, location, or scope of assets.
     * @param title job title for the governance officer.
     * @param additionalProperties additional properties for the governance officer.
     * @param externalReferences links to addition information.  This could be, for example, the home page
     *                           for the governance officer, or details of the role.
     * @throws InvalidParameterException the title is null or the governanceDomain/appointmentId does not match the
     *                                   the existing values associated with the governanceOfficerGUID.
     * @throws PropertyServerException the server is not available.
     * @throws UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    public void   updateGovernanceOfficer(String                     userId,
                                          String                     governanceOfficerGUID,
                                          GovernanceDomain           governanceDomain,
                                          String                     appointmentId,
                                          String                     appointmentContext,
                                          String                     title,
                                          Map<String, String>        additionalProperties,
                                          List<ExternalReference>    externalReferences)  throws InvalidParameterException,
                                                                                                 PropertyServerException,
                                                                                                 UserNotAuthorizedException
    {
        final String   methodName = "updateGovernanceOfficer";
        final String   urlTemplate = "/servers/{0}/open-metadata/access-services/governance-program/users/{1}/leadership/governance-officers/{2}";

        final String   guidParameterName = "governanceOfficerGUID";
        final String   appointmentIdParameterName = "appointmentId";
        final String   titleParameterName = "title";
        final String   governanceDomainParameterName = "governanceDomain";

        invalidParameterHandler.validateOMAGServerPlatformURL(omasServerURL, serverName, methodName);
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(governanceOfficerGUID, guidParameterName, methodName);
        invalidParameterHandler.validateName(appointmentId, appointmentIdParameterName, methodName);
        invalidParameterHandler.validateName(title, titleParameterName, methodName);
        exceptionHandler.validateGovernanceDomain(governanceDomain, governanceDomainParameterName, methodName);

        GovernanceOfficerDetailsRequestBody  requestBody = new GovernanceOfficerDetailsRequestBody();
        requestBody.setGovernanceDomain(governanceDomain);
        requestBody.setAppointmentId(appointmentId);
        requestBody.setAppointmentContext(appointmentContext);
        requestBody.setTitle(title);
        requestBody.setAdditionalProperties(additionalProperties);
        requestBody.setExternalReferences(externalReferences);

        VoidResponse restResult = restClient.callVoidPostRESTCall(methodName,
                                                                  omasServerURL + urlTemplate,
                                                                  requestBody,
                                                                  serverName,
                                                                  userId,
                                                                  governanceOfficerGUID);

        exceptionHandler.detectAndThrowInvalidParameterException(methodName, restResult);
        exceptionHandler.detectAndThrowUserNotAuthorizedException(methodName, restResult);
        exceptionHandler.detectAndThrowPropertyServerException(methodName, restResult);
    }


    /**
     * Remove the requested governance officer.
     *
     * @param userId the name of the calling user.
     * @param governanceOfficerGUID unique identifier (guid) of the governance officer.
     * @param appointmentId  the unique identifier of the governance officer.
     * @param governanceDomain  the governance domain for the governance officer.
     * @throws InvalidParameterException the appointmentId or governance domain is either null or invalid.
     * @throws PropertyServerException the server is not available.
     * @throws UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    public void   deleteGovernanceOfficer(String              userId,
                                          String              governanceOfficerGUID,
                                          String              appointmentId,
                                          GovernanceDomain    governanceDomain) throws InvalidParameterException,
                                                                                       PropertyServerException,
                                                                                       UserNotAuthorizedException
    {
        final String   methodName = "deleteGovernanceOfficer";
        final String   urlTemplate = "/servers/{0}/open-metadata/access-services/governance-program/users/{1}/leadership/governance-officers/{2}/delete";

        final String   guidParameterName = "governanceOfficerGUID";
        final String   appointmentIdParameterName = "appointmentId";
        final String   governanceDomainParameterName = "governanceDomain";

        invalidParameterHandler.validateOMAGServerPlatformURL(omasServerURL, serverName, methodName);
        invalidParameterHandler.validateGUID(governanceOfficerGUID, guidParameterName, methodName);
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(appointmentId, appointmentIdParameterName, methodName);
        exceptionHandler.validateGovernanceDomain(governanceDomain, governanceDomainParameterName, methodName);

        GovernanceOfficerValidatorRequestBody  requestBody = new GovernanceOfficerValidatorRequestBody();
        requestBody.setGovernanceDomain(governanceDomain);
        requestBody.setAppointmentId(appointmentId);

        VoidResponse restResult = restClient.callVoidPostRESTCall(methodName,
                                                                  omasServerURL + urlTemplate,
                                                                  requestBody,
                                                                  serverName,
                                                                  userId,
                                                                  governanceOfficerGUID);

        exceptionHandler.detectAndThrowInvalidParameterException(methodName, restResult);
        exceptionHandler.detectAndThrowUserNotAuthorizedException(methodName, restResult);
        exceptionHandler.detectAndThrowPropertyServerException(methodName, restResult);
    }


    /**
     * Retrieve a governance officer description by unique guid.
     *
     * @param userId the name of the calling user.
     * @param governanceOfficerGUID unique identifier (guid) of the governance officer.
     * @return governance officer object
     * @throws InvalidParameterException the unique identifier of the governance officer is either null or invalid.
     * @throws PropertyServerException the server is not available.
     * @throws UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    public GovernanceOfficer getGovernanceOfficerByGUID(String     userId,
                                                        String     governanceOfficerGUID) throws InvalidParameterException,
                                                                                                 PropertyServerException,
                                                                                                 UserNotAuthorizedException
    {
        final String   methodName = "getGovernanceOfficerByGUID";
        final String   urlTemplate = "/servers/{0}/open-metadata/access-services/governance-program/users/{1}/leadership/governance-officers/{2}";

        final String   guidParameterName = "governanceOfficerGUID";

        invalidParameterHandler.validateOMAGServerPlatformURL(omasServerURL, serverName, methodName);
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(governanceOfficerGUID, guidParameterName, methodName);

        GovernanceOfficerResponse restResult = restClient.callGovernanceOfficerGetRESTCall(methodName,
                                                                                           omasServerURL + urlTemplate,
                                                                                           serverName,
                                                                                           userId,
                                                                                           governanceOfficerGUID);

        exceptionHandler.detectAndThrowInvalidParameterException(methodName, restResult);
        exceptionHandler.detectAndThrowUserNotAuthorizedException(methodName, restResult);
        exceptionHandler.detectAndThrowPropertyServerException(methodName, restResult);

        return restResult.getGovernanceOfficer();
    }


    /**
     * Retrieve a governance officer by unique appointment id.
     *
     * @param userId the name of the calling user.
     * @param appointmentId  the unique appointment identifier of the governance officer.
     * @return governance officer object
     * @throws InvalidParameterException the appointmentId or governance domain is either null or invalid.
     * @throws AppointmentIdNotUniqueException more than one governance officer entity was retrieved for this appointmentId.
     * @throws PropertyServerException the server is not available.
     * @throws UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    public GovernanceOfficer        getGovernanceOfficerByAppointmentId(String     userId,
                                                                        String     appointmentId) throws InvalidParameterException,
                                                                                                         AppointmentIdNotUniqueException,
                                                                                                         PropertyServerException,
                                                                                                         UserNotAuthorizedException
    {
        final String   methodName = "getGovernanceOfficerByAppointmentId";
        final String   urlTemplate = "/servers/{0}/open-metadata/access-services/governance-program/users/{1}/leadership/governance-officers/by-appointment-id/{2}";

        final String   appointmentIdParameterName = "appointmentId";

        invalidParameterHandler.validateOMAGServerPlatformURL(omasServerURL, serverName, methodName);
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(appointmentId, appointmentIdParameterName, methodName);

        GovernanceOfficerResponse restResult = restClient.callGovernanceOfficerGetRESTCall(methodName,
                                                                                           omasServerURL + urlTemplate,
                                                                                           serverName,
                                                                                           userId,
                                                                                           appointmentId);

        exceptionHandler.detectAndThrowInvalidParameterException(methodName, restResult);
        exceptionHandler.detectAndThrowAppointmentIdNotUniqueException(methodName, restResult);
        exceptionHandler.detectAndThrowUserNotAuthorizedException(methodName, restResult);
        exceptionHandler.detectAndThrowPropertyServerException(methodName, restResult);

        return restResult.getGovernanceOfficer();
    }


    /**
     * Return all of the defined governance officers.
     *
     * @param userId the name of the calling user.
     * @return list of governance officer objects
     * @throws InvalidParameterException the userId is either null or invalid.
     * @throws PropertyServerException the server is not available.
     * @throws UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    public List<GovernanceOfficer>  getGovernanceOfficers(String     userId) throws InvalidParameterException,
                                                                                    PropertyServerException,
                                                                                    UserNotAuthorizedException
    {
        final String   methodName = "getGovernanceOfficers";
        final String   urlTemplate = "/servers/{0}/open-metadata/access-services/governance-program/users/{1}/leadership/governance-officers";

        invalidParameterHandler.validateOMAGServerPlatformURL(omasServerURL, serverName, methodName);
        invalidParameterHandler.validateUserId(userId, methodName);

        GovernanceOfficerListResponse restResult = restClient.callGovernanceOfficerListGetRESTCall(methodName,
                                                                                                   omasServerURL + urlTemplate,
                                                                                                   serverName,
                                                                                                   userId);

        exceptionHandler.detectAndThrowInvalidParameterException(methodName, restResult);
        exceptionHandler.detectAndThrowUserNotAuthorizedException(methodName, restResult);
        exceptionHandler.detectAndThrowPropertyServerException(methodName, restResult);

        return restResult.getGovernanceOfficers();
    }


    /**
     * Return all of the currently appointed governance officers.
     *
     * @param userId the name of the calling user.
     * @return list of governance officer objects
     * @throws InvalidParameterException the userId is either null or invalid.
     * @throws PropertyServerException the server is not available.
     * @throws UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    public List<GovernanceOfficer>  getActiveGovernanceOfficers(String     userId) throws InvalidParameterException,
                                                                                          PropertyServerException,
                                                                                          UserNotAuthorizedException
    {
        final String   methodName = "getGovernanceOfficers";
        final String   urlTemplate = "/servers/{0}/open-metadata/access-services/governance-program/users/{1}/leadership/governance-officers/active";

        invalidParameterHandler.validateOMAGServerPlatformURL(omasServerURL, serverName, methodName);
        invalidParameterHandler.validateUserId(userId, methodName);

        GovernanceOfficerListResponse restResult = restClient.callGovernanceOfficerListGetRESTCall(methodName,
                                                                                                   omasServerURL + urlTemplate,
                                                                                                   serverName,
                                                                                                   userId);

        exceptionHandler.detectAndThrowInvalidParameterException(methodName, restResult);
        exceptionHandler.detectAndThrowUserNotAuthorizedException(methodName, restResult);
        exceptionHandler.detectAndThrowPropertyServerException(methodName, restResult);

        return restResult.getGovernanceOfficers();
    }


    /**
     * Return all of the defined governance officers for a specific governance domain.  In a small organization
     * there is typically only one governance officer.   However a large organization may have multiple governance
     * officers, each with a different scope.  The governance officer with a null scope is the overall leader.
     *
     * @param userId the name of the calling user.
     * @param governanceDomain domain of interest
     * @return list of governance officer objects
     * @throws InvalidParameterException the governance domain is either null or invalid.
     * @throws PropertyServerException the server is not available.
     * @throws UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    public List<GovernanceOfficer>  getGovernanceOfficersByDomain(String             userId,
                                                                  GovernanceDomain   governanceDomain) throws InvalidParameterException,
                                                                                                              PropertyServerException,
                                                                                                              UserNotAuthorizedException
    {
        final String   methodName = "getGovernanceOfficersByDomain";
        final String   urlTemplate = "/servers/{0}/open-metadata/access-services/governance-program/users/{1}/leadership/governance-officers/by-domain";

        final String   governanceDomainParameterName = "governanceDomain";

        invalidParameterHandler.validateOMAGServerPlatformURL(omasServerURL, serverName, methodName);
        invalidParameterHandler.validateUserId(userId, methodName);
        exceptionHandler.validateGovernanceDomain(governanceDomain, governanceDomainParameterName, methodName);

        GovernanceDomainRequestBody  requestBody = new GovernanceDomainRequestBody();
        requestBody.setGovernanceDomain(governanceDomain);

        GovernanceOfficerListResponse restResult = restClient.callGovernanceOfficerListPostRESTCall(methodName,
                                                                                                    omasServerURL + urlTemplate,
                                                                                                    requestBody,
                                                                                                    serverName,
                                                                                                    userId);

        exceptionHandler.detectAndThrowInvalidParameterException(methodName, restResult);
        exceptionHandler.detectAndThrowUserNotAuthorizedException(methodName, restResult);
        exceptionHandler.detectAndThrowPropertyServerException(methodName, restResult);

        return restResult.getGovernanceOfficers();
    }


    /**
     * Link a person to a governance officer.  Only one person may be appointed at any one time.
     *
     * @param userId the name of the calling user.
     * @param governanceOfficerGUID unique identifier (guid) of the governance officer.
     * @param profileGUID unique identifier for the profile.
     * @param startDate the official start date of the appointment - null means effective immediately.
     * @throws InvalidParameterException the unique identifier of the governance officer or profile is either null or invalid.
     * @throws PropertyServerException the server is not available.
     * @throws UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    public void appointGovernanceOfficer(String  userId,
                                         String  governanceOfficerGUID,
                                         String  profileGUID,
                                         Date    startDate) throws InvalidParameterException,
                                                                   PropertyServerException,
                                                                   UserNotAuthorizedException
    {
        final String   methodName = "appointGovernanceOfficer";
        final String   urlTemplate = "/servers/{0}/open-metadata/access-services/governance-program/users/{1}/leadership/governance-officers/{2}/appoint";

        final String   governanceOfficerGUIDParameterName = "governanceOfficerGUID";
        final String   profileGUIDParameterName = "profileGUID";

        invalidParameterHandler.validateOMAGServerPlatformURL(omasServerURL, serverName, methodName);
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(governanceOfficerGUID, governanceOfficerGUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(profileGUID, profileGUIDParameterName, methodName);

        AppointmentRequestBody  requestBody = new AppointmentRequestBody();
        requestBody.setGUID(profileGUID);
        requestBody.setEffectiveDate(startDate);

        VoidResponse restResult = restClient.callVoidPostRESTCall(methodName,
                                                                  omasServerURL + urlTemplate,
                                                                  requestBody,
                                                                  serverName,
                                                                  userId,
                                                                  governanceOfficerGUID);

        exceptionHandler.detectAndThrowInvalidParameterException(methodName, restResult);
        exceptionHandler.detectAndThrowUserNotAuthorizedException(methodName, restResult);
        exceptionHandler.detectAndThrowPropertyServerException(methodName, restResult);
    }


    /**
     * Unlink a person from a governance officer appointment.
     *
     * @param userId the name of the calling user.
     * @param governanceOfficerGUID unique identifier (guid) of the governance officer.
     * @param profileGUID unique identifier for the profile.
     * @param endDate the official end of the appointment - null means effective immediately.
     * @throws InvalidParameterException the profile is not linked to this governance officer.
     * @throws PropertyServerException the server is not available.
     * @throws UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    public void relieveGovernanceOfficer(String  userId,
                                         String  governanceOfficerGUID,
                                         String  profileGUID,
                                         Date    endDate) throws InvalidParameterException,
                                                                 PropertyServerException,
                                                                 UserNotAuthorizedException
    {
        final String   methodName = "relieveGovernanceOfficer";
        final String   urlTemplate = "/servers/{0}/open-metadata/access-services/governance-program/users/{1}/leadership/governance-officers/{2}/relieve";

        final String   governanceOfficerGUIDParameterName = "governanceOfficerGUID";
        final String   profileGUIDParameterName = "profileGUID";

        invalidParameterHandler.validateOMAGServerPlatformURL(omasServerURL, serverName, methodName);
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(governanceOfficerGUID, governanceOfficerGUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(profileGUID, profileGUIDParameterName, methodName);

        AppointmentRequestBody  requestBody = new AppointmentRequestBody();
        requestBody.setGUID(profileGUID);
        requestBody.setEffectiveDate(endDate);

        VoidResponse restResult = restClient.callVoidPostRESTCall(methodName,
                                                                  omasServerURL + urlTemplate,
                                                                  requestBody,
                                                                  serverName,
                                                                  userId,
                                                                  governanceOfficerGUID);

        exceptionHandler.detectAndThrowInvalidParameterException(methodName, restResult);
        exceptionHandler.detectAndThrowUserNotAuthorizedException(methodName, restResult);
        exceptionHandler.detectAndThrowPropertyServerException(methodName, restResult);
    }
}
