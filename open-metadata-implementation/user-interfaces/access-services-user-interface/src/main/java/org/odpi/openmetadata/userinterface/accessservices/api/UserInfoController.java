/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.userinterface.accessservices.api;

import org.odpi.openmetadata.userinterface.accessservices.auth.AuthService;
import org.odpi.openmetadata.userinterface.accessservices.auth.TokenUser;
import org.odpi.openmetadata.userinterface.accessservices.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/users")
public class UserInfoController {

    @Autowired
    private AuthService authService;

    @RequestMapping( value ="/current", method = RequestMethod.GET)
    public User getUser(HttpServletRequest request) throws HttpClientErrorException{
        Authentication auth = authService.getAuthentication(request);

        if(auth == null || auth.getDetails() == null || !(auth.getDetails() instanceof TokenUser)){
            throw new UserNotAuthorizedException("User is not authorized");
        }

        TokenUser tokenUser = (TokenUser) auth.getDetails();
        return tokenUser.getUser();
    }

}
