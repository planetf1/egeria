/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataengine.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * PortType defines the different port types used for open metadata. It is used in a port implementation
 * definition.
 * <ul>
 * <li>INPUT_PORT - Input Port.</li>
 * <li>OUTPUT_PORT - Output Port.</li>
 * <li>INOUT_PORT - Input Output Port.</li>
 * <li>OUTIN_PORT - Output Input Port.</li>
 * <li>OTHER - None of the above.</li>
 * </ul>
 */
@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public enum PortType implements Serializable {
    INPUT_PORT(0, "INPUT_PORT", "Input Port."),
    OUTPUT_PORT(1, "OUTPUT_PORT", "Output Port."),
    INOUT_PORT(2,"INOUT_PORT", "Input Output Port."),
    OUTIN_PORT(3, "OUTIN_PORT", "Output Input Port."),
    OTHER(99, "OTHER", "None of the above.");

    private static final long serialVersionUID = 1L;

    private int ordinal;
    private String name;
    private String description;


    /**
     * Default constructor for the enumeration.
     *
     * @param ordinal     numerical representation of the enumeration
     * @param name        default string name of the instance provenance type
     * @param description default string description of the instance provenance type
     */
    PortType(int ordinal, String name, String description) {
        this.ordinal = ordinal;
        this.name = name;
        this.description = description;
    }


    /**
     * Return the numeric representation of the instance provenance type.
     *
     * @return int ordinal
     */
    public int getOrdinal() {
        return ordinal;
    }


    /**
     * Return the default name of the instance provenance type.
     *
     * @return String name
     */
    public String getName() {
        return name;
    }


    /**
     * Return the default description of the instance provenance type.
     *
     * @return String description
     */
    public String getDescription() {
        return description;
    }


    /**
     * toString() JSON-style
     *
     * @return string description
     */
    @Override
    public String toString() {
        return "PortType{" +
                "ordinal=" + ordinal +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }}

