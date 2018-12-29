/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.generated.v115;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.*;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

/**
 * POJO for the 'xsd_foreign_key' asset type in IGC, displayed as 'XSD Foreign Key' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class XsdForeignKey extends Reference {

    public static String getIgcTypeId() { return "xsd_foreign_key"; }
    public static String getIgcTypeDisplayName() { return "XSD Foreign Key"; }

    /**
     * The 'name' property, displayed as 'Name' in the IGC UI.
     */
    protected String name;

    /**
     * The 'xsd_element' property, displayed as 'XSD Element' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link MainObject} object.
     */
    protected Reference xsd_element;

    /**
     * The 'namespace' property, displayed as 'Namespace' in the IGC UI.
     */
    protected String namespace;

    /**
     * The 'selector' property, displayed as 'Selector' in the IGC UI.
     */
    protected String selector;

    /**
     * The 'xsd_elements_or_attributes' property, displayed as 'XSD Elements or Attributes' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link MainObject} objects.
     */
    protected ReferenceList xsd_elements_or_attributes;

    /**
     * The 'referenced_xsd_keys' property, displayed as 'References XSD Keys' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link XsdUniqueKey} object.
     */
    protected Reference referenced_xsd_keys;

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

    /** @see #xsd_element */ @JsonProperty("xsd_element")  public Reference getXsdElement() { return this.xsd_element; }
    /** @see #xsd_element */ @JsonProperty("xsd_element")  public void setXsdElement(Reference xsd_element) { this.xsd_element = xsd_element; }

    /** @see #namespace */ @JsonProperty("namespace")  public String getNamespace() { return this.namespace; }
    /** @see #namespace */ @JsonProperty("namespace")  public void setNamespace(String namespace) { this.namespace = namespace; }

    /** @see #selector */ @JsonProperty("selector")  public String getSelector() { return this.selector; }
    /** @see #selector */ @JsonProperty("selector")  public void setSelector(String selector) { this.selector = selector; }

    /** @see #xsd_elements_or_attributes */ @JsonProperty("xsd_elements_or_attributes")  public ReferenceList getXsdElementsOrAttributes() { return this.xsd_elements_or_attributes; }
    /** @see #xsd_elements_or_attributes */ @JsonProperty("xsd_elements_or_attributes")  public void setXsdElementsOrAttributes(ReferenceList xsd_elements_or_attributes) { this.xsd_elements_or_attributes = xsd_elements_or_attributes; }

    /** @see #referenced_xsd_keys */ @JsonProperty("referenced_xsd_keys")  public Reference getReferencedXsdKeys() { return this.referenced_xsd_keys; }
    /** @see #referenced_xsd_keys */ @JsonProperty("referenced_xsd_keys")  public void setReferencedXsdKeys(Reference referenced_xsd_keys) { this.referenced_xsd_keys = referenced_xsd_keys; }

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
        "namespace",
        "selector",
        "created_by",
        "created_on",
        "modified_by",
        "modified_on"
    );
    private static final List<String> PAGED_RELATIONAL_PROPERTIES = Arrays.asList(
        "xsd_elements_or_attributes"
    );
    private static final List<String> ALL_PROPERTIES = Arrays.asList(
        "name",
        "xsd_element",
        "namespace",
        "selector",
        "xsd_elements_or_attributes",
        "referenced_xsd_keys",
        "created_by",
        "created_on",
        "modified_by",
        "modified_on"
    );
    public static List<String> getNonRelationshipProperties() { return NON_RELATIONAL_PROPERTIES; }
    public static List<String> getPagedRelationshipProperties() { return PAGED_RELATIONAL_PROPERTIES; }
    public static List<String> getAllProperties() { return ALL_PROPERTIES; }
    public static Boolean isXsdForeignKey(Object obj) { return (obj.getClass() == XsdForeignKey.class); }

}
