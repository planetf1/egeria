/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.generated.v11502sp3;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.*;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

/**
 * POJO for the 'bi_filter' asset type in IGC, displayed as 'BI Filter' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class BiFilter extends Reference {

    public static String getIgcTypeId() { return "bi_filter"; }
    public static String getIgcTypeDisplayName() { return "BI Filter"; }

    /**
     * The 'name' property, displayed as 'Name' in the IGC UI.
     */
    protected String name;

    /**
     * The 'short_description' property, displayed as 'Short Description' in the IGC UI.
     */
    protected String short_description;

    /**
     * The 'long_description' property, displayed as 'Long Description' in the IGC UI.
     */
    protected String long_description;

    /**
     * The 'context' property, displayed as 'Context' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link Olapobject} objects.
     */
    protected ReferenceList context;

    /**
     * The 'bi_model' property, displayed as 'BI Model' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link BiModel} object.
     */
    protected Reference bi_model;

    /**
     * The 'bi_collection' property, displayed as 'BI Collection' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link BiCollection} object.
     */
    protected Reference bi_collection;

    /**
     * The 'filter_expression' property, displayed as 'Filter Expression' in the IGC UI.
     */
    protected String filter_expression;

    /**
     * The 'created_by' property, displayed as 'Created By' in the IGC UI.
     */
    protected String created_by;

    /**
     * The 'created_on' property, displayed as 'Created On' in the IGC UI.
     */
    protected Date created_on;

    /**
     * The 'modified_by' property, displayed as 'Modified By' in the IGC UI.
     */
    protected String modified_by;

    /**
     * The 'modified_on' property, displayed as 'Modified On' in the IGC UI.
     */
    protected Date modified_on;


    /** @see #name */ @JsonProperty("name")  public String getTheName() { return this.name; }
    /** @see #name */ @JsonProperty("name")  public void setTheName(String name) { this.name = name; }

    /** @see #short_description */ @JsonProperty("short_description")  public String getShortDescription() { return this.short_description; }
    /** @see #short_description */ @JsonProperty("short_description")  public void setShortDescription(String short_description) { this.short_description = short_description; }

    /** @see #long_description */ @JsonProperty("long_description")  public String getLongDescription() { return this.long_description; }
    /** @see #long_description */ @JsonProperty("long_description")  public void setLongDescription(String long_description) { this.long_description = long_description; }

    /** @see #context */ @JsonProperty("context")  public ReferenceList getTheContext() { return this.context; }
    /** @see #context */ @JsonProperty("context")  public void setTheContext(ReferenceList context) { this.context = context; }

    /** @see #bi_model */ @JsonProperty("bi_model")  public Reference getBiModel() { return this.bi_model; }
    /** @see #bi_model */ @JsonProperty("bi_model")  public void setBiModel(Reference bi_model) { this.bi_model = bi_model; }

    /** @see #bi_collection */ @JsonProperty("bi_collection")  public Reference getBiCollection() { return this.bi_collection; }
    /** @see #bi_collection */ @JsonProperty("bi_collection")  public void setBiCollection(Reference bi_collection) { this.bi_collection = bi_collection; }

    /** @see #filter_expression */ @JsonProperty("filter_expression")  public String getFilterExpression() { return this.filter_expression; }
    /** @see #filter_expression */ @JsonProperty("filter_expression")  public void setFilterExpression(String filter_expression) { this.filter_expression = filter_expression; }

    /** @see #created_by */ @JsonProperty("created_by")  public String getCreatedBy() { return this.created_by; }
    /** @see #created_by */ @JsonProperty("created_by")  public void setCreatedBy(String created_by) { this.created_by = created_by; }

    /** @see #created_on */ @JsonProperty("created_on")  public Date getCreatedOn() { return this.created_on; }
    /** @see #created_on */ @JsonProperty("created_on")  public void setCreatedOn(Date created_on) { this.created_on = created_on; }

    /** @see #modified_by */ @JsonProperty("modified_by")  public String getModifiedBy() { return this.modified_by; }
    /** @see #modified_by */ @JsonProperty("modified_by")  public void setModifiedBy(String modified_by) { this.modified_by = modified_by; }

    /** @see #modified_on */ @JsonProperty("modified_on")  public Date getModifiedOn() { return this.modified_on; }
    /** @see #modified_on */ @JsonProperty("modified_on")  public void setModifiedOn(Date modified_on) { this.modified_on = modified_on; }

    public static Boolean canBeCreated() { return false; }
    public static Boolean includesModificationDetails() { return true; }
    private static final List<String> NON_RELATIONAL_PROPERTIES = Arrays.asList(
        "name",
        "short_description",
        "long_description",
        "filter_expression",
        "created_by",
        "created_on",
        "modified_by",
        "modified_on"
    );
    private static final List<String> PAGED_RELATIONAL_PROPERTIES = Arrays.asList(
        "context"
    );
    private static final List<String> ALL_PROPERTIES = Arrays.asList(
        "name",
        "short_description",
        "long_description",
        "context",
        "bi_model",
        "bi_collection",
        "filter_expression",
        "created_by",
        "created_on",
        "modified_by",
        "modified_on"
    );
    public static List<String> getNonRelationshipProperties() { return NON_RELATIONAL_PROPERTIES; }
    public static List<String> getPagedRelationshipProperties() { return PAGED_RELATIONAL_PROPERTIES; }
    public static List<String> getAllProperties() { return ALL_PROPERTIES; }
    public static Boolean isBiFilter(Object obj) { return (obj.getClass() == BiFilter.class); }

}
