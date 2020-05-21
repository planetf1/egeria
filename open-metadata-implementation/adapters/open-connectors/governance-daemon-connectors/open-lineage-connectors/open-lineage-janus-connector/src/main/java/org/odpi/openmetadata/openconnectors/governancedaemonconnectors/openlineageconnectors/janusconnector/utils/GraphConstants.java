/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class GraphConstants {

    private GraphConstants() {
    }

    private static final String JAVA_STRING = "java.lang.String";

    public static final String PROPERTY_KEY_PREFIX_ELEMENT = "vertex--";
    public static final String PROPERTY_KEY_PREFIX_RELATIONSHIP = "edge--";
    public static final String PROPERTY_KEY_PREFIX_INSTANCE_PROPERTY = "InstanceProp";
    public static final String PROPERTY_KEY_PREFIX_VERTEX_INSTANCE_PROPERTY = PROPERTY_KEY_PREFIX_ELEMENT + PROPERTY_KEY_PREFIX_INSTANCE_PROPERTY;

    public static final String PROPERTY_NAME_PORT_TYPE = PROPERTY_KEY_PREFIX_VERTEX_INSTANCE_PROPERTY + "portType";
    public static final String PROPERTY_NAME_DISPLAY_NAME = "displayName";
    public static final String PROPERTY_NAME_INSTANCEPROP_DISPLAY_NAME = PROPERTY_KEY_PREFIX_INSTANCE_PROPERTY + PROPERTY_NAME_DISPLAY_NAME;
    public static final String PROPERTY_NAME_NODE_ID = "nodeID";
    public static final String PROPERTY_VALUE_NODE_ID_CONDENSED_SOURCE = "condensedSource";
    public static final String PROPERTY_VALUE_NODE_ID_CONDENSED_DESTINATION = "condensedDestination";
    public static final String PROPERTY_NAME_GUID = "guid";
    public static final String PROPERTY_NAME_QUALIFIED_NAME = "qualifiedName";
    public static final String PROPERTY_NAME_VERSION = "version";
    public static final String PROPERTY_NAME_CREATED_BY = "createdBy";
    public static final String PROPERTY_NAME_CREATE_TIME = "createTime";
    public static final String PROPERTY_NAME_UPDATED_BY = "updatedBy";
    public static final String PROPERTY_NAME_UPDATE_TIME = "updateTime";
    public static final String PROPERTY_NAME_LABEL = "label";
    public static final String PROPERTY_NAME_PROXY = "proxy";
    public static final String PROPERTY_NAME_GLOSSARY_TERM = "glossaryTerm";
    public static final String PROPERTY_NAME_HOST_DISPLAY_NAME = "displayname";
    public static final String PROPERTY_NAME_DATABASE_DISPLAY_NAME = "databaseDisplayname";
    public static final String PROPERTY_NAME_SCHEMA_DISPLAY_NAME = "schemaDisplayNname";
    public static final String PROPERTY_NAME_TABLE_DISPLAY_NAME = "tableDisplayName";
    public static final String PROPERTY_NAME_FORMULA = "formula";
    public static final String PROPERTY_NAMEPROCESS_DESCRIPTION_URI = "descriptionURI";
    public static final String PROPERTY_NAME_PROCESS_TYPE = "processType";
    public static final String PROPERTY_NAME_PARENT_PROCESS_GUID = "parent.process.guid";
    public static final String PROPERTY_NAME_GLOSSARY = "glossary";

    public static final String CONDENSED_NODE_DISPLAY_NAME = "...";

    public static final String NODE_LABEL_TABLE = "table";
    public static final String NODE_LABEL_COLUMN = "column";
    public static final String NODE_LABEL_GLOSSARYTERM = "glossaryTerm";
    public static final String NODE_LABEL_CONDENSED = "condensedNode";
    public static final String NODE_LABEL_PROCESS = "process";
    public static final String NODE_LABEL_SUB_PROCESS = "subProcess";

    public static final String EDGE_LABEL_SEMANTIC = "semanticAssignment";
    public static final String EDGE_LABEL_GLOSSARYTERM_TO_GLOSSARYTERM = "synonym";
    public static final String EDGE_LABEL_CONDENSED = "condensed";
    public static final String EDGE_LABEL_INCLUDED_IN = "includedIn";
    public static final String EDGE_LABEL_DATAFLOW_WITHOUT_PROCESS = "DataFlowWithoutProcess";
    public static final String EDGE_LABEL_DATAFLOW_WITH_PROCESS = "DataFlowWithProcess";


    public static final String PROPERTY_KEY_ENTITY_NODE_ID = PROPERTY_KEY_PREFIX_ELEMENT + PROPERTY_NAME_NODE_ID;
    public static final String PROPERTY_KEY_ENTITY_GUID = PROPERTY_KEY_PREFIX_ELEMENT + PROPERTY_NAME_GUID;
    public static final String PROPERTY_KEY_NAME_QUALIFIED_NAME = PROPERTY_KEY_PREFIX_ELEMENT + PROPERTY_NAME_QUALIFIED_NAME;
    public static final String PROPERTY_KEY_LABEL = PROPERTY_KEY_PREFIX_ELEMENT + PROPERTY_NAME_LABEL;
    public static final String PROPERTY_KEY_PROXY = PROPERTY_KEY_PREFIX_ELEMENT + PROPERTY_NAME_PROXY;
    public static final String PROPERTY_KEY_GLOSSARY_TERM = PROPERTY_KEY_PREFIX_ELEMENT + PROPERTY_NAME_GLOSSARY_TERM;
    public static final String PROPERTY_KEY_DISPLAY_NAME = PROPERTY_KEY_PREFIX_ELEMENT + PROPERTY_NAME_DISPLAY_NAME;
    public static final String PROPERTY_KEY_INSTANCEPROP_DISPLAY_NAME = PROPERTY_KEY_PREFIX_ELEMENT + PROPERTY_NAME_INSTANCEPROP_DISPLAY_NAME;

    public static final String PROPERTY_KEY_ENTITY_VERSION = PROPERTY_KEY_PREFIX_ELEMENT + PROPERTY_NAME_VERSION;
    public static final String PROPERTY_KEY_ENTITY_CREATED_BY = PROPERTY_KEY_PREFIX_ELEMENT + PROPERTY_NAME_CREATED_BY;
    public static final String PROPERTY_KEY_ENTITY_CREATE_TIME = PROPERTY_KEY_PREFIX_ELEMENT + PROPERTY_NAME_CREATE_TIME;
    public static final String PROPERTY_KEY_ENTITY_UPDATED_BY = PROPERTY_KEY_PREFIX_ELEMENT + PROPERTY_NAME_UPDATED_BY;
    public static final String PROPERTY_KEY_ENTITY_UPDATE_TIME = PROPERTY_KEY_PREFIX_ELEMENT + PROPERTY_NAME_UPDATE_TIME;
    public static final String PROPERTY_KEY_SCHEMA_DISPLAY_NAME = PROPERTY_KEY_PREFIX_ELEMENT + PROPERTY_NAME_SCHEMA_DISPLAY_NAME;
    public static final String PROPERTY_KEY_TABLE_DISPLAY_NAME = PROPERTY_KEY_PREFIX_ELEMENT + PROPERTY_NAME_TABLE_DISPLAY_NAME;

    public static final String PROPERTY_KEY_RELATIONSHIP_GUID = PROPERTY_KEY_PREFIX_RELATIONSHIP + PROPERTY_NAME_GUID;
    public static final String PROPERTY_KEY_RELATIONSHIP_VERSION = PROPERTY_KEY_PREFIX_RELATIONSHIP + PROPERTY_NAME_VERSION;
    public static final String PROPERTY_KEY_RELATIONSHIP_CREATED_BY = PROPERTY_KEY_PREFIX_RELATIONSHIP + PROPERTY_NAME_CREATED_BY;
    public static final String PROPERTY_KEY_RELATIONSHIP_CREATE_TIME = PROPERTY_KEY_PREFIX_RELATIONSHIP + PROPERTY_NAME_CREATE_TIME;
    public static final String PROPERTY_KEY_RELATIONSHIP_UPDATED_BY = PROPERTY_KEY_PREFIX_RELATIONSHIP + PROPERTY_NAME_UPDATED_BY;
    public static final String PROPERTY_KEY_RELATIONSHIP_UPDATE_TIME = PROPERTY_KEY_PREFIX_RELATIONSHIP + PROPERTY_NAME_UPDATE_TIME;
    public static final String PROPERTY_KEY_RELATIONSHIP_LABEL = PROPERTY_KEY_PREFIX_RELATIONSHIP + PROPERTY_NAME_LABEL;

    private static final HashSet<String> returnedPropertiesWhiteList = new HashSet<>();

    static {
        returnedPropertiesWhiteList.add(PROPERTY_KEY_ENTITY_GUID);
        returnedPropertiesWhiteList.add(PROPERTY_KEY_DISPLAY_NAME);
        returnedPropertiesWhiteList.add(PROPERTY_KEY_INSTANCEPROP_DISPLAY_NAME);
        returnedPropertiesWhiteList.add(PROPERTY_KEY_NAME_QUALIFIED_NAME);
        returnedPropertiesWhiteList.add(PROPERTY_KEY_GLOSSARY_TERM);
        returnedPropertiesWhiteList.add(PROPERTY_KEY_ENTITY_CREATED_BY);
        returnedPropertiesWhiteList.add(PROPERTY_KEY_SCHEMA_DISPLAY_NAME);
        returnedPropertiesWhiteList.add(PROPERTY_KEY_TABLE_DISPLAY_NAME);
        returnedPropertiesWhiteList.add(PROPERTY_KEY_ENTITY_CREATE_TIME);
        returnedPropertiesWhiteList.add(PROPERTY_KEY_ENTITY_UPDATED_BY);
        returnedPropertiesWhiteList.add(PROPERTY_KEY_ENTITY_UPDATE_TIME);
    }

    public static final ImmutableSet<String> immutableReturnedPropertiesWhiteList = ImmutableSet.copyOf(returnedPropertiesWhiteList);

    private static final Map<String, String> corePropertyTypes = new HashMap<>();

    static {
        corePropertyTypes.put(PROPERTY_NAME_GUID, JAVA_STRING);
        corePropertyTypes.put(PROPERTY_NAME_VERSION, "java.lang.Long");
        corePropertyTypes.put(PROPERTY_NAME_CREATED_BY, JAVA_STRING);
        corePropertyTypes.put(PROPERTY_NAME_CREATE_TIME, "java.lang.Date");
        corePropertyTypes.put(PROPERTY_NAME_UPDATED_BY, JAVA_STRING);
        corePropertyTypes.put(PROPERTY_NAME_UPDATE_TIME, "java.lang.Date");
        corePropertyTypes.put(PROPERTY_NAME_LABEL, JAVA_STRING);
        corePropertyTypes.put(PROPERTY_NAME_PROXY, "java.lang.Boolean");
        corePropertyTypes.put(PROPERTY_NAME_NODE_ID, JAVA_STRING);

    }

    public static final ImmutableMap<String, String> immutableCorePropertyTypes = ImmutableMap.copyOf(corePropertyTypes);

}