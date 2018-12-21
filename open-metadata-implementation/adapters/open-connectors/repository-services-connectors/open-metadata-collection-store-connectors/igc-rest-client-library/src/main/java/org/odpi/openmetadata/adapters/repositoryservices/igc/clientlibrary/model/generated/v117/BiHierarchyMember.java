/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.generated.v117;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.*;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

/**
 * POJO for the 'bi_hierarchy_member' asset type in IGC, displayed as 'BI Hierarchy Member' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class BiHierarchyMember extends Reference {

    public static String getIgcTypeId() { return "bi_hierarchy_member"; }
    public static String getIgcTypeDisplayName() { return "BI Hierarchy Member"; }

    /**
     * The 'bi_level' property, displayed as 'BI Level' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link BiLevel} object.
     */
    protected Reference bi_level;

    /**
     * The 'child_level' property, displayed as 'Child Level' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link BiLevel} objects.
     */
    protected ReferenceList child_level;

    /**
     * The 'sorting_order' property, displayed as 'Sorting Order' in the IGC UI.
     * <br><br>
     * Can be one of the following values:
     * <ul>
     *     <li>ASCENDING (displayed in the UI as 'ASCENDING')</li>
     *     <li>DESCENDING (displayed in the UI as 'DESCENDING')</li>
     *     <li>NONE (displayed in the UI as 'NONE')</li>
     * </ul>
     */
    protected String sorting_order;

    /**
     * The 'sequence' property, displayed as 'Sequence' in the IGC UI.
     */
    protected Number sequence;


    /** @see #bi_level */ @JsonProperty("bi_level")  public Reference getBiLevel() { return this.bi_level; }
    /** @see #bi_level */ @JsonProperty("bi_level")  public void setBiLevel(Reference bi_level) { this.bi_level = bi_level; }

    /** @see #child_level */ @JsonProperty("child_level")  public ReferenceList getChildLevel() { return this.child_level; }
    /** @see #child_level */ @JsonProperty("child_level")  public void setChildLevel(ReferenceList child_level) { this.child_level = child_level; }

    /** @see #sorting_order */ @JsonProperty("sorting_order")  public String getSortingOrder() { return this.sorting_order; }
    /** @see #sorting_order */ @JsonProperty("sorting_order")  public void setSortingOrder(String sorting_order) { this.sorting_order = sorting_order; }

    /** @see #sequence */ @JsonProperty("sequence")  public Number getSequence() { return this.sequence; }
    /** @see #sequence */ @JsonProperty("sequence")  public void setSequence(Number sequence) { this.sequence = sequence; }

    public static final Boolean canBeCreated() { return false; }
    public static final Boolean includesModificationDetails() { return false; }
    public static final ArrayList<String> NON_RELATIONAL_PROPERTIES = new ArrayList<String>() {{
        add("sorting_order");
        add("sequence");
    }};
    public static final List<String> getNonRelationshipProperties() { return NON_RELATIONAL_PROPERTIES; }
    public static final Boolean isBiHierarchyMember(Object obj) { return (obj.getClass() == BiHierarchyMember.class); }

}
