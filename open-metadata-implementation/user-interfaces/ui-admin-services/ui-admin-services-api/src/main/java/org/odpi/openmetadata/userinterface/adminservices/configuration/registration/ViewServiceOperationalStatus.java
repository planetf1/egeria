/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.userinterface.adminservices.configuration.registration;


import java.io.Serializable;

/**
 * ViewServiceOperationalStatus sets up whether an open metadata view service (OMVS) is enabled or not.
 */
public enum ViewServiceOperationalStatus implements Serializable
{
    NOT_IMPLEMENTED  (0, "Not Implemented", "Code for this view service is not available."),
    ENABLED          (1, "Enabled",         "The view service is available and running."),
    DISABLED         (2, "Disabled",        "The view service has been disabled.");

    private static final long serialVersionUID = 1L;

    private int            typeCode;
    private String         typeName;
    private String         typeDescription;


    /**
     * Default Constructor
     *
     * @param typeCode ordinal for this enum
     * @param typeName symbolic name for this enum
     * @param typeDescription short description for this enum
     */
    ViewServiceOperationalStatus(int     typeCode, String   typeName, String   typeDescription)
    {
        /*
         * Save the values supplied
         */
        this.typeCode = typeCode;
        this.typeName = typeName;
        this.typeDescription = typeDescription;
    }


    /**
     * Return the code for this enum instance
     *
     * @return int type code
     */
    public int getTypeCode()
    {
        return typeCode;
    }


    /**
     * Return the default name for this enum instance.
     *
     * @return String default name
     */
    public String getTypeName()
    {
        return typeName;
    }


    /**
     * Return the default description for the type for this enum instance.
     *
     * @return String default description
     */
    public String getTypeDescription()
    {
        return typeDescription;
    }
}
