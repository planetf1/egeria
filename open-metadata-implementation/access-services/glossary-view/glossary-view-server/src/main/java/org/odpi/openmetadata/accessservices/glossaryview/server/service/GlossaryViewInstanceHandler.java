/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.glossaryview.server.service;

import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceDescription;
import org.odpi.openmetadata.commonservices.multitenant.OCFOMASServiceInstanceHandler;

public class GlossaryViewInstanceHandler extends OCFOMASServiceInstanceHandler {

    public GlossaryViewInstanceHandler(){
        super(AccessServiceDescription.GLOSSARY_VIEW_OMAS.getAccessServiceName());
    }

}
