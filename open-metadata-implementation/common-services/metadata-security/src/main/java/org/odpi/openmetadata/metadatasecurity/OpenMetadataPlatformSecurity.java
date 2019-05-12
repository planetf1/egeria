/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.metadatasecurity;

import org.odpi.openmetadata.commonservices.ffdc.exceptions.UserNotAuthorizedException;


/**
 * OpenMetadataPlatformSecurity provides the interface for a plugin connector that validates whether a calling
 * user can access any service on an OMAG Server Platform.  It is called within the context of a specific
 * OMAG Server Platform request.
 * Each OMAG Server Platform can define its own plugin connector implementation and will have its own instance
 * of the connector.
 */
public interface OpenMetadataPlatformSecurity
{
    /**
     * Check that the calling user is authorized to issue a (any) request to the OMAG Server Platform.
     *
     * @param userId calling user
     *
     * @throws UserNotAuthorizedException the user is not authorized to access this platform
     */
    void  validateUserForPlatform(String   userId) throws UserNotAuthorizedException;


    /**
     * Check that the calling user is authorized to issue administration requests to the OMAG Server Platform.
     *
     * @param userId calling user
     *
     * @throws UserNotAuthorizedException the user is not authorized to change configuration on this platform
     */
    void  validateUserAsAdminForPlatform(String   userId) throws UserNotAuthorizedException;



    /**
     * Check that the calling user is authorized to issue operator requests to the OMAG Server Platform.
     *
     * @param userId calling user
     *
     * @throws UserNotAuthorizedException the user is not authorized to issue operator commands to this platform
     */
    void  validateUserAsOperatorForPlatform(String   userId) throws UserNotAuthorizedException;


    /**
     * Check that the calling user is authorized to issue operator requests to the OMAG Server Platform.
     *
     * @param userId calling user
     *
     * @throws UserNotAuthorizedException the user is not authorized to issue diagnostic commands to this platform
     */
    void  validateUserAsInvestigatorForPlatform(String   userId) throws UserNotAuthorizedException;
}
