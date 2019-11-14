/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataengine.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Attribute implements Serializable {
    private static final long serialVersionUID = 1L;
    private String qualifiedName;
    private String displayName;
    private String minCardinality;
    private String maxCardinality;
    private String allowsDuplicateValues;
    private String orderedValues;
    private int position;
    private String defaultValueOverride;
    private String dataType;
    private String defaultValue;

    public Attribute() {
    }

    public Attribute(String qualifiedName, String displayName, String minCardinality, String maxCardinality,
                     String allowsDuplicateValues, String orderedValues, int position, String defaultValueOverride,
                     String dataType, String defaultValue) {
        this.qualifiedName = qualifiedName;
        this.displayName = displayName;
        this.minCardinality = minCardinality;
        this.maxCardinality = maxCardinality;
        this.allowsDuplicateValues = allowsDuplicateValues;
        this.orderedValues = orderedValues;
        this.position = position;
        this.defaultValueOverride = defaultValueOverride;
        this.dataType = dataType;
        this.defaultValue = defaultValue;
    }

    public String getQualifiedName() {
        return qualifiedName;
    }

    public void setQualifiedName(String qualifiedName) {
        this.qualifiedName = qualifiedName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getMinCardinality() {
        return minCardinality;
    }

    public void setMinCardinality(String minCardinality) {
        this.minCardinality = minCardinality;
    }

    public String getMaxCardinality() {
        return maxCardinality;
    }

    public void setMaxCardinality(String maxCardinality) {
        this.maxCardinality = maxCardinality;
    }

    public String getAllowsDuplicateValues() {
        return allowsDuplicateValues;
    }

    public void setAllowsDuplicateValues(String allowsDuplicateValues) {
        this.allowsDuplicateValues = allowsDuplicateValues;
    }

    public String getOrderedValues() {
        return orderedValues;
    }

    public void setOrderedValues(String orderedValues) {
        this.orderedValues = orderedValues;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getDefaultValueOverride() {
        return defaultValueOverride;
    }

    public void setDefaultValueOverride(String defaultValueOverride) {
        this.defaultValueOverride = defaultValueOverride;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    @Override
    public String toString() {
        return "Attribute{" +
                "qualifiedName='" + qualifiedName + '\'' +
                ", displayName='" + displayName + '\'' +
                ", minCardinality='" + minCardinality + '\'' +
                ", maxCardinality='" + maxCardinality + '\'' +
                ", allowsDuplicateValues='" + allowsDuplicateValues + '\'' +
                ", orderedValues='" + orderedValues + '\'' +
                ", position='" + position + '\'' +
                ", defaultValueOverride='" + defaultValueOverride + '\'' +
                ", dataType='" + dataType + '\'' +
                ", defaultValue='" + defaultValue + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Attribute attribute = (Attribute) o;
        return Objects.equals(qualifiedName, attribute.qualifiedName) &&
                Objects.equals(displayName, attribute.displayName) &&
                Objects.equals(minCardinality, attribute.minCardinality) &&
                Objects.equals(maxCardinality, attribute.maxCardinality) &&
                Objects.equals(allowsDuplicateValues, attribute.allowsDuplicateValues) &&
                Objects.equals(orderedValues, attribute.orderedValues) &&
                Objects.equals(position, attribute.position) &&
                Objects.equals(defaultValueOverride, attribute.defaultValueOverride) &&
                Objects.equals(dataType, attribute.dataType) &&
                Objects.equals(defaultValue, attribute.defaultValue);
    }

    @Override
    public int hashCode() {
        return Objects.hash(qualifiedName, displayName, minCardinality, maxCardinality, allowsDuplicateValues,
                orderedValues, position, defaultValueOverride, dataType, defaultValue);
    }
}
