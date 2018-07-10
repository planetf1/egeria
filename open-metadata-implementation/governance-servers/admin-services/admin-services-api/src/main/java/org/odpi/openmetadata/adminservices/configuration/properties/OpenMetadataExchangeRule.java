/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.adminservices.configuration.properties;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * OpenMetadataExchangeRule controls the sending/receiving of metadata instances on the metadata highway.
 * <ul>
 *     <li>
 *         REGISTRATION_ONLY means do not send/receive reference metadata - just perform the minimal registration
 *         exchanges.
 *     </li>
 *     <li>
 *         JUST_TYPEDEFS means only send/receive/validate type definitions (TypeDefs).
 *     </li>
 *     <li>
 *         SELECTED_TYPES means that in addition to TypeDefs events, only metadata instances of the types
 *         supplied in the related list of TypeDefs (see typesToSend, typesToSave and typesToFederate) should be processed.
 *     </li>
 *     <li>
 *         LEARNED_TYPES means that the local repository requests reference copies of metadata based on the requests of
 *         the local user community.
 *     </li>
 *     <li>
 *         ALL means send/receive all types of metadata that are supported by the local repository.
 *     </li>
 * </ul>
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public enum OpenMetadataExchangeRule implements Serializable
{
    REGISTRATION_ONLY (0,  "Registration Only", "Only registration exchange; no TypeDefs or metadata instances."),
    JUST_TYPEDEFS     (1,  "Just TypeDefs",     "Only registration and type definitions (TypeDefs) exchange."),
    SELECTED_TYPES    (2,  "Selected Types",    "Registration plus all type definitions (TypeDefs) and metadata " +
                                                "instances (Entities and Relationships) of selected types."),
    LEARNED_TYPES     (3,  "Learned Types",     "Registration plus all type definitions (TypeDefs) and metadata " +
                                                "instances (Entities and Relationships) of types " +
                                                "requested by local users to this server."),
    ALL               (99, "All",               "Registration plus all type definitions (TypeDefs) and metadata " +
                                                "instances (Entities and Relationships).");

    private static final long serialVersionUID = 1L;

    private  int    ordinal;
    private  String name;
    private  String replicationRuleDescription;

    /**
     * Constructor for the metadata instance replication rule.
     *
     * @param ordinal the code number of this metadata instance replication rule.
     * @param name the name of this metadata instance replication rule.
     * @param replicationRuleDescription the description of this metadata instance replication rule.
     */
    OpenMetadataExchangeRule(int ordinal, String name, String replicationRuleDescription)
    {
        this.ordinal = ordinal;
        this.name = name;
        this.replicationRuleDescription = replicationRuleDescription;
    }


    /**
     * Return the code number of this metadata instance replication rule.
     *
     * @return int replication rule code number
     */
    public int getOrdinal()
    {
        return ordinal;
    }


    /**
     * Return the name of this metadata instance replication rule.
     *
     * @return String replication rule name
     */
    public String getName()
    {
        return name;
    }


    /**
     * Return the description of this metadata instance replication rule.
     *
     * @return String replication rule description
     */
    public String getDescription()
    {
        return replicationRuleDescription;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "OpenMetadataExchangeRule{" +
                "ordinal=" + ordinal +
                ", name='" + name + '\'' +
                ", replicationRuleDescription='" + replicationRuleDescription + '\'' +
                '}';
    }
}
