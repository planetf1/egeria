/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataengine.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class PortImplementation extends Port {
    private static final long serialVersionUID = 1L;
    @JsonProperty("schema")
    private SchemaType schemaType;

    public PortImplementation() {
    }

    public PortImplementation(String displayName, String qualifiedName, PortType portType, SchemaType schemaType) {
        super(displayName, qualifiedName, portType);
        this.schemaType = schemaType;
    }

    public PortImplementation(String displayName, String qualifiedName, PortType portType, SchemaType schemaType,
                              UpdateSemantic updateSemantic) {
        super(displayName, qualifiedName, portType, updateSemantic);
        this.schemaType = schemaType;
    }

    public SchemaType getSchemaType() {
        return schemaType;
    }

    public void setSchemaType(SchemaType schemaType) {
        this.schemaType = schemaType;
    }

    @Override
    public String toString() {
        return "PortImplementation{" +
                "schemaType=" + schemaType +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        PortImplementation that = (PortImplementation) o;
        return Objects.equals(schemaType, that.schemaType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), schemaType);
    }
}
