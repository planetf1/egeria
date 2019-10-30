/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataengine.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Port implements Serializable {
    private static final long serialVersionUID = 1L;
    private String displayName;
    private String qualifiedName;
    @JsonProperty("type")
    private PortType portType;
    private UpdateSemantic updateSemantic;

    Port(String displayName, String qualifiedName, PortType portType) {
        this.displayName = displayName;
        this.qualifiedName = qualifiedName;
        this.portType = portType;
    }

    Port(String displayName, String qualifiedName, PortType portType, UpdateSemantic updateSemantic) {
        this(displayName, qualifiedName, portType);
        this.updateSemantic = updateSemantic;
    }

    Port() {
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getQualifiedName() {
        return qualifiedName;
    }

    public void setQualifiedName(String qualifiedName) {
        this.qualifiedName = qualifiedName;
    }

    public PortType getPortType() {
        return portType;
    }

    public void setPortType(PortType portType) {
        this.portType = portType;
    }

    public UpdateSemantic getUpdateSemantic() {
        if (updateSemantic == null) {
            return UpdateSemantic.REPLACE;
        }

        return updateSemantic;
    }

    public void setUpdateSemantic(UpdateSemantic updateSemantic) {
        this.updateSemantic = updateSemantic;
    }

    @Override
    public String toString() {
        return "Port{" +
                "displayName='" + displayName + '\'' +
                ", qualifiedName='" + qualifiedName + '\'' +
                ", portType=" + portType +
                ", updateSemantic=" + updateSemantic +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Port port = (Port) o;
        return Objects.equals(displayName, port.displayName) &&
                Objects.equals(qualifiedName, port.qualifiedName) &&
                portType == port.portType &&
                updateSemantic == port.updateSemantic;
    }

    @Override
    public int hashCode() {
        return Objects.hash(displayName, qualifiedName, portType, updateSemantic);
    }
}
