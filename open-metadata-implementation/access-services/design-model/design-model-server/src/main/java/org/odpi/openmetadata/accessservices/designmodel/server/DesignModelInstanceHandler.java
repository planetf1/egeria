/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.designmodel.server;

import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceDescription;
import org.odpi.openmetadata.commonservices.multitenant.OCFOMASServiceInstanceHandler;


/**
 * DesignModelInstanceHandler retrieves information from the instance map for the
 * access service instances.  The instance map is thread-safe.  Instances are added
 * and removed by the DesignModelAdmin class.
 */
class DesignModelInstanceHandler extends OCFOMASServiceInstanceHandler
{
    /**
     * Default constructor registers the access service
     */
    DesignModelInstanceHandler()
    {
        super(AccessServiceDescription.DESIGN_MODEL_OMAS.getAccessServiceName() + " OMAS");

        DesignModelRegistration.registerAccessService();
    }
}
