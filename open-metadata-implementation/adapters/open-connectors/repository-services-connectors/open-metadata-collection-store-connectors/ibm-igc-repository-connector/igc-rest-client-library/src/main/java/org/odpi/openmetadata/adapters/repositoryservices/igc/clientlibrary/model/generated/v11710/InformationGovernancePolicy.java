/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.generated.v11710;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import javax.annotation.Generated;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.*;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

/**
 * POJO for the {@code information_governance_policy} asset type in IGC, displayed as '{@literal Information Governance Policy}' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@Generated("org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.IGCRestModelGenerator")
@JsonIgnoreProperties(ignoreUnknown=true)
public class InformationGovernancePolicy extends Reference {

    public static String getIgcTypeId() { return "information_governance_policy"; }
    public static String getIgcTypeDisplayName() { return "Information Governance Policy"; }

    /**
     * The {@code name} property, displayed as '{@literal Name}' in the IGC UI.
     */
    protected String name;

    /**
     * The {@code short_description} property, displayed as '{@literal Short Description}' in the IGC UI.
     */
    protected String short_description;

    /**
     * The {@code long_description} property, displayed as '{@literal Long Description}' in the IGC UI.
     */
    protected String long_description;

    /**
     * The {@code parent_policy} property, displayed as '{@literal Parent Policy}' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link InformationGovernancePolicy} object.
     */
    protected Reference parent_policy;

    /**
     * The {@code labels} property, displayed as '{@literal Labels}' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link Label} objects.
     */
    protected ReferenceList labels;

    /**
     * The {@code stewards} property, displayed as '{@literal Stewards}' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link AsclSteward} objects.
     */
    protected ReferenceList stewards;

    /**
     * The {@code language} property, displayed as '{@literal Language}' in the IGC UI.
     * <br><br>
     * Can be one of the following values:
     * <ul>
     *     <li>ar (displayed in the UI as 'عربية')</li>
     *     <li>pt-br (displayed in the UI as 'Portugu&ecirc;s/Brasil')</li>
     *     <li>zh (displayed in the UI as '简体中文')</li>
     *     <li>zh-tw (displayed in the UI as '繁體中文')</li>
     *     <li>en (displayed in the UI as 'English')</li>
     *     <li>fr (displayed in the UI as 'Fran&ccedil;ais')</li>
     *     <li>de (displayed in the UI as 'Deutsch')</li>
     *     <li>it (displayed in the UI as 'Italiano')</li>
     *     <li>he (displayed in the UI as 'עברית')</li>
     *     <li>ja (displayed in the UI as '日本語')</li>
     *     <li>ko (displayed in the UI as '한국어')</li>
     *     <li>ru (displayed in the UI as 'Русский')</li>
     *     <li>es (displayed in the UI as 'Espa&ntilde;ol')</li>
     * </ul>
     */
    protected String language;

    /**
     * The {@code translations} property, displayed as '{@literal Translations}' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link InformationGovernancePolicy} objects.
     */
    protected ReferenceList translations;

    /**
     * The {@code native_id} property, displayed as '{@literal Native ID}' in the IGC UI.
     */
    protected String native_id;

    /**
     * The {@code subpolicies} property, displayed as '{@literal Subpolicies}' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link InformationGovernancePolicy} objects.
     */
    protected ReferenceList subpolicies;

    /**
     * The {@code information_governance_rules} property, displayed as '{@literal Information Governance Rules}' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link InformationGovernanceRule} objects.
     */
    protected ReferenceList information_governance_rules;

    /**
     * The {@code in_collections} property, displayed as '{@literal In Collections}' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link Collection} objects.
     */
    protected ReferenceList in_collections;

    /**
     * The {@code workflow_current_state} property, displayed as '{@literal Workflow Current State}' in the IGC UI.
     */
    protected ArrayList<String> workflow_current_state;

    /**
     * The {@code workflow_stored_state} property, displayed as '{@literal Workflow Stored State}' in the IGC UI.
     * <br><br>
     * Can be one of the following values:
     * <ul>
     *     <li>DRAFT (displayed in the UI as 'DRAFT')</li>
     *     <li>WAITING_APPROVAL (displayed in the UI as 'WAITING_APPROVAL')</li>
     *     <li>APPROVED (displayed in the UI as 'APPROVED')</li>
     * </ul>
     */
    protected ArrayList<String> workflow_stored_state;

    /**
     * The {@code glossary_type} property, displayed as '{@literal Glossary Type}' in the IGC UI.
     * <br><br>
     * Can be one of the following values:
     * <ul>
     *     <li>PUBLISHED (displayed in the UI as 'PUBLISHED')</li>
     *     <li>DRAFT (displayed in the UI as 'DRAFT')</li>
     * </ul>
     */
    protected String glossary_type;

    /**
     * The {@code created_by} property, displayed as '{@literal Created By}' in the IGC UI.
     */
    protected String created_by;

    /**
     * The {@code created_on} property, displayed as '{@literal Created On}' in the IGC UI.
     */
    protected Date created_on;

    /**
     * The {@code modified_by} property, displayed as '{@literal Modified By}' in the IGC UI.
     */
    protected String modified_by;

    /**
     * The {@code modified_on} property, displayed as '{@literal Modified On}' in the IGC UI.
     */
    protected Date modified_on;


    /** @see #name */ @JsonProperty("name")  public String getTheName() { return this.name; }
    /** @see #name */ @JsonProperty("name")  public void setTheName(String name) { this.name = name; }

    /** @see #short_description */ @JsonProperty("short_description")  public String getShortDescription() { return this.short_description; }
    /** @see #short_description */ @JsonProperty("short_description")  public void setShortDescription(String short_description) { this.short_description = short_description; }

    /** @see #long_description */ @JsonProperty("long_description")  public String getLongDescription() { return this.long_description; }
    /** @see #long_description */ @JsonProperty("long_description")  public void setLongDescription(String long_description) { this.long_description = long_description; }

    /** @see #parent_policy */ @JsonProperty("parent_policy")  public Reference getParentPolicy() { return this.parent_policy; }
    /** @see #parent_policy */ @JsonProperty("parent_policy")  public void setParentPolicy(Reference parent_policy) { this.parent_policy = parent_policy; }

    /** @see #labels */ @JsonProperty("labels")  public ReferenceList getLabels() { return this.labels; }
    /** @see #labels */ @JsonProperty("labels")  public void setLabels(ReferenceList labels) { this.labels = labels; }

    /** @see #stewards */ @JsonProperty("stewards")  public ReferenceList getStewards() { return this.stewards; }
    /** @see #stewards */ @JsonProperty("stewards")  public void setStewards(ReferenceList stewards) { this.stewards = stewards; }

    /** @see #language */ @JsonProperty("language")  public String getLanguage() { return this.language; }
    /** @see #language */ @JsonProperty("language")  public void setLanguage(String language) { this.language = language; }

    /** @see #translations */ @JsonProperty("translations")  public ReferenceList getTranslations() { return this.translations; }
    /** @see #translations */ @JsonProperty("translations")  public void setTranslations(ReferenceList translations) { this.translations = translations; }

    /** @see #native_id */ @JsonProperty("native_id")  public String getNativeId() { return this.native_id; }
    /** @see #native_id */ @JsonProperty("native_id")  public void setNativeId(String native_id) { this.native_id = native_id; }

    /** @see #subpolicies */ @JsonProperty("subpolicies")  public ReferenceList getSubpolicies() { return this.subpolicies; }
    /** @see #subpolicies */ @JsonProperty("subpolicies")  public void setSubpolicies(ReferenceList subpolicies) { this.subpolicies = subpolicies; }

    /** @see #information_governance_rules */ @JsonProperty("information_governance_rules")  public ReferenceList getInformationGovernanceRules() { return this.information_governance_rules; }
    /** @see #information_governance_rules */ @JsonProperty("information_governance_rules")  public void setInformationGovernanceRules(ReferenceList information_governance_rules) { this.information_governance_rules = information_governance_rules; }

    /** @see #in_collections */ @JsonProperty("in_collections")  public ReferenceList getInCollections() { return this.in_collections; }
    /** @see #in_collections */ @JsonProperty("in_collections")  public void setInCollections(ReferenceList in_collections) { this.in_collections = in_collections; }

    /** @see #workflow_current_state */ @JsonProperty("workflow_current_state")  public ArrayList<String> getWorkflowCurrentState() { return this.workflow_current_state; }
    /** @see #workflow_current_state */ @JsonProperty("workflow_current_state")  public void setWorkflowCurrentState(ArrayList<String> workflow_current_state) { this.workflow_current_state = workflow_current_state; }

    /** @see #workflow_stored_state */ @JsonProperty("workflow_stored_state")  public ArrayList<String> getWorkflowStoredState() { return this.workflow_stored_state; }
    /** @see #workflow_stored_state */ @JsonProperty("workflow_stored_state")  public void setWorkflowStoredState(ArrayList<String> workflow_stored_state) { this.workflow_stored_state = workflow_stored_state; }

    /** @see #glossary_type */ @JsonProperty("glossary_type")  public String getGlossaryType() { return this.glossary_type; }
    /** @see #glossary_type */ @JsonProperty("glossary_type")  public void setGlossaryType(String glossary_type) { this.glossary_type = glossary_type; }

    /** @see #created_by */ @JsonProperty("created_by")  public String getCreatedBy() { return this.created_by; }
    /** @see #created_by */ @JsonProperty("created_by")  public void setCreatedBy(String created_by) { this.created_by = created_by; }

    /** @see #created_on */ @JsonProperty("created_on")  public Date getCreatedOn() { return this.created_on; }
    /** @see #created_on */ @JsonProperty("created_on")  public void setCreatedOn(Date created_on) { this.created_on = created_on; }

    /** @see #modified_by */ @JsonProperty("modified_by")  public String getModifiedBy() { return this.modified_by; }
    /** @see #modified_by */ @JsonProperty("modified_by")  public void setModifiedBy(String modified_by) { this.modified_by = modified_by; }

    /** @see #modified_on */ @JsonProperty("modified_on")  public Date getModifiedOn() { return this.modified_on; }
    /** @see #modified_on */ @JsonProperty("modified_on")  public void setModifiedOn(Date modified_on) { this.modified_on = modified_on; }

    public static Boolean canBeCreated() { return true; }
    public static Boolean includesModificationDetails() { return true; }
    private static final List<String> NON_RELATIONAL_PROPERTIES = Arrays.asList(
        "name",
        "short_description",
        "long_description",
        "language",
        "native_id",
        "workflow_current_state",
        "workflow_stored_state",
        "glossary_type",
        "created_by",
        "created_on",
        "modified_by",
        "modified_on"
    );
    private static final List<String> STRING_PROPERTIES = Arrays.asList(
        "name",
        "short_description",
        "long_description",
        "native_id",
        "workflow_current_state",
        "created_by",
        "modified_by"
    );
    private static final List<String> PAGED_RELATIONAL_PROPERTIES = Arrays.asList(
        "labels",
        "stewards",
        "translations",
        "subpolicies",
        "information_governance_rules",
        "in_collections"
    );
    private static final List<String> ALL_PROPERTIES = Arrays.asList(
        "name",
        "short_description",
        "long_description",
        "parent_policy",
        "labels",
        "stewards",
        "language",
        "translations",
        "native_id",
        "subpolicies",
        "information_governance_rules",
        "in_collections",
        "workflow_current_state",
        "workflow_stored_state",
        "glossary_type",
        "created_by",
        "created_on",
        "modified_by",
        "modified_on"
    );
    public static List<String> getNonRelationshipProperties() { return NON_RELATIONAL_PROPERTIES; }
    public static List<String> getStringProperties() { return STRING_PROPERTIES; }
    public static List<String> getPagedRelationshipProperties() { return PAGED_RELATIONAL_PROPERTIES; }
    public static List<String> getAllProperties() { return ALL_PROPERTIES; }
    public static Boolean isInformationGovernancePolicy(Object obj) { return (obj.getClass() == InformationGovernancePolicy.class); }

}
