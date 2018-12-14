/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.generated.v117;

import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;
import java.util.ArrayList;

/**
 * POJO for the 'model_eval_metric' asset type in IGC, displayed as 'Model Eval Metric' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class ModelEvalMetric extends Reference {

    public static String getIgcTypeId() { return "model_eval_metric"; }

    /**
     * The 'of_model' property, displayed as 'Of Model' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link AnalyticsModel} object.
     */
    protected Reference of_model;

    /**
     * The 'metric_type' property, displayed as 'Metric Type' in the IGC UI.
     */
    protected String metric_type;

    /**
     * The 'value' property, displayed as 'Value' in the IGC UI.
     */
    protected String value;

    /**
     * The 'eval_date' property, displayed as 'Eval Date' in the IGC UI.
     */
    protected Date eval_date;

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


    /** @see #of_model */ @JsonProperty("of_model")  public Reference getOfModel() { return this.of_model; }
    /** @see #of_model */ @JsonProperty("of_model")  public void setOfModel(Reference of_model) { this.of_model = of_model; }

    /** @see #metric_type */ @JsonProperty("metric_type")  public String getMetricType() { return this.metric_type; }
    /** @see #metric_type */ @JsonProperty("metric_type")  public void setMetricType(String metric_type) { this.metric_type = metric_type; }

    /** @see #value */ @JsonProperty("value")  public String getValue() { return this.value; }
    /** @see #value */ @JsonProperty("value")  public void setValue(String value) { this.value = value; }

    /** @see #eval_date */ @JsonProperty("eval_date")  public Date getEvalDate() { return this.eval_date; }
    /** @see #eval_date */ @JsonProperty("eval_date")  public void setEvalDate(Date eval_date) { this.eval_date = eval_date; }

    /** @see #created_by */ @JsonProperty("created_by")  public String getCreatedBy() { return this.created_by; }
    /** @see #created_by */ @JsonProperty("created_by")  public void setCreatedBy(String created_by) { this.created_by = created_by; }

    /** @see #created_on */ @JsonProperty("created_on")  public Date getCreatedOn() { return this.created_on; }
    /** @see #created_on */ @JsonProperty("created_on")  public void setCreatedOn(Date created_on) { this.created_on = created_on; }

    /** @see #modified_by */ @JsonProperty("modified_by")  public String getModifiedBy() { return this.modified_by; }
    /** @see #modified_by */ @JsonProperty("modified_by")  public void setModifiedBy(String modified_by) { this.modified_by = modified_by; }

    /** @see #modified_on */ @JsonProperty("modified_on")  public Date getModifiedOn() { return this.modified_on; }
    /** @see #modified_on */ @JsonProperty("modified_on")  public void setModifiedOn(Date modified_on) { this.modified_on = modified_on; }


    public static final Boolean isModelEvalMetric(Object obj) { return (obj.getClass() == ModelEvalMetric.class); }

}
