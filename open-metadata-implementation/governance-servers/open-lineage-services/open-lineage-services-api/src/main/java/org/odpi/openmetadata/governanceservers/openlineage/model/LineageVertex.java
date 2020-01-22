/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.governanceservers.openlineage.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.util.Map;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "class"
)
public class LineageVertex {

    private String nodeID;
    private String nodeType;
    private String displayName;
    private String guid;
    private Map<String, String> properties;

    public LineageVertex(){}

    public LineageVertex(String nodeID, String nodeType) {
        this.nodeID = nodeID;
        this.nodeType = nodeType;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public String getNodeType() {
        return nodeType;
    }

    public String getGuid() {
        return guid;
    }

    public String getNodeID() {
        return nodeID;
    }

    public String getDisplayName() {
        return displayName;
    }

    public Map<String, String> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, String> properties) {
        this.properties = properties;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LineageVertex that = (LineageVertex) o;
        return nodeID.equals(that.nodeID) &&
                nodeType.equals(that.nodeType) &&
                Objects.equals(displayName, that.displayName) &&
                Objects.equals(guid, that.guid) &&
                Objects.equals(properties, that.properties);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nodeID, nodeType, displayName, guid, properties);
    }
}
