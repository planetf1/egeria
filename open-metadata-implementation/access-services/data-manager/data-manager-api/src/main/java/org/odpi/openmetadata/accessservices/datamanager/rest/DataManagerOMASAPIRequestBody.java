/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.datamanager.rest;

import com.fasterxml.jackson.annotation.*;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * DataManagerOMASAPIRequestBody provides a common header for Data Manager OMAS request bodies for its REST API.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "class")
@JsonSubTypes({
        @JsonSubTypes.Type(value = NewDataFileRequestBody.class, name = "NewDataFileRequestBody"),
        @JsonSubTypes.Type(value = NewFileSystemRequestBody.class, name = "NewFileSystemRequestBody"),
        @JsonSubTypes.Type(value = PathNameRequestBody.class, name = "PathNameRequestBody")

              })
public abstract class DataManagerOMASAPIRequestBody implements java.io.Serializable
{
    private static final long    serialVersionUID = 1L;


    /**
     * Default constructor
     */
    public DataManagerOMASAPIRequestBody()
    {
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public DataManagerOMASAPIRequestBody(DataManagerOMASAPIRequestBody template)
    {
    }


    /**
     * JSON-like toString
     *
     * @return string containing the class name
     */
    @Override
    public String toString()
    {
        return "DataManagerOMASAPIRequestBody{}";
    }
}
