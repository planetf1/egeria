/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adminservices.rest;

import com.fasterxml.jackson.annotation.*;
import org.odpi.openmetadata.commonservices.ffdc.rest.FFDCResponseBase;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * AdminServicesAPIResponse provides a common header for admin services managed response to its REST API.
 * It manages information about exceptions.  If no exception has been raised exceptionClassName is null.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "class")
@JsonSubTypes
        ({
                @JsonSubTypes.Type(value = SuccessMessageResponse.class, name = "SuccessMessageResponse"),
                @JsonSubTypes.Type(value = ConnectionResponse.class, name = "ConnectionResponse"),
                @JsonSubTypes.Type(value = OMAGServerConfigResponse.class, name = "OMAGServerConfigResponse")
        })
public abstract class AdminServicesAPIResponse extends FFDCResponseBase
{
    /**
     * Default constructor
     */
    public AdminServicesAPIResponse()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public AdminServicesAPIResponse(AdminServicesAPIResponse  template)
    {
        super(template);
    }




    /**
     * JSON-like toString
     *
     * @return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "AdminServicesAPIResponse{" +
                ", relatedHTTPCode=" + getRelatedHTTPCode() +
                ", exceptionClassName='" + getExceptionClassName() + '\'' +
                ", exceptionErrorMessage='" + getExceptionErrorMessage() + '\'' +
                ", exceptionSystemAction='" + getExceptionSystemAction() + '\'' +
                ", exceptionUserAction='" + getExceptionUserAction() + '\'' +
                ", exceptionProperties=" + getExceptionProperties() +
                '}';
    }
}
