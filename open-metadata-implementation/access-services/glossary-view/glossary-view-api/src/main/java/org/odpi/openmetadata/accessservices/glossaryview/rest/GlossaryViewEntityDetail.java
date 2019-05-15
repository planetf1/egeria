/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.glossaryview.rest;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.*;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * Simple POJO that holds the data of any entity queried from the OMRS, that needs to be returned to the client
 */
@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class GlossaryViewEntityDetail{

    private String entityType;
    private String createdBy;
    private String updatedBy;
    private Date createTime;
    private Date updateTime;
    private long version;
    private String guid;
    private String status;
    private Date effectiveFromTime;
    private Date effectiveToTime;

    private Map<String, String> properties;

    private List<GlossaryViewClassification> classifications;

    public GlossaryViewEntityDetail(){}

    public String getEntityType(){ return entityType; }

    public String getCreatedBy() {
        return createdBy;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public long getVersion() {
        return version;
    }

    public String getGuid() {
        return guid;
    }

    public String getStatus() {
        return status;
    }

    public Date getEffectiveFromTime() {
        return effectiveFromTime;
    }

    public Date getEffectiveToTime() {
        return effectiveToTime;
    }

    public Map<String, String> getProperties() {
        return properties;
    }

    public List<GlossaryViewClassification> getClassifications() {
        return classifications;
    }

    public GlossaryViewEntityDetail setEntityType(String entityType) {
        this.entityType = entityType;
        return this;
    }
    public GlossaryViewEntityDetail setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public GlossaryViewEntityDetail setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
        return this;
    }

    public GlossaryViewEntityDetail setCreateTime(Date createTime) {
        this.createTime = createTime;
        return this;
    }

    public GlossaryViewEntityDetail setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
        return this;
    }

    public GlossaryViewEntityDetail setVersion(long version) {
        this.version = version;
        return this;
    }

    public GlossaryViewEntityDetail setGuid(String guid) {
        this.guid = guid;
        return this;
    }

    public GlossaryViewEntityDetail setStatus(String status) {
        this.status = status;
        return this;
    }

    public GlossaryViewEntityDetail setEffectiveFromTime(Date effectiveFromTime) {
        this.effectiveFromTime = effectiveFromTime;
        return this;
    }

    public GlossaryViewEntityDetail setEffectiveToTime(Date effectiveToTime) {
        this.effectiveToTime = effectiveToTime;
        return this;
    }

    public GlossaryViewEntityDetail setProperties(Map<String, String> properties){
        this.properties = properties;
        return this;
    }

    public GlossaryViewEntityDetail putProperties(Map<String, String> properties){
        if (this.properties == null) {
            this.properties = new HashMap<>();
        }
        this.properties.putAll(properties);
        return this;
    }

    public GlossaryViewEntityDetail putProperty(String key, String value){
        if (this.properties == null) {
            this.properties = new HashMap<>();
        }
        this.properties.put(key, value);
        return this;
    }

    public GlossaryViewEntityDetail setClassifications(List<GlossaryViewClassification> classifications) {
        this.classifications = classifications;
        return this;
    }

    public GlossaryViewEntityDetail addClassifications(List<GlossaryViewClassification> classifications) {
        if(this.classifications == null){
            this.classifications = new ArrayList<>();
        }
        this.classifications.addAll(classifications);
        return this;
    }

    public GlossaryViewEntityDetail addClassification(GlossaryViewClassification classification) {
        if(this.classifications == null){
            this.classifications = new ArrayList<>();
        }
        this.classifications.add(classification);
        return this;
    }

}
