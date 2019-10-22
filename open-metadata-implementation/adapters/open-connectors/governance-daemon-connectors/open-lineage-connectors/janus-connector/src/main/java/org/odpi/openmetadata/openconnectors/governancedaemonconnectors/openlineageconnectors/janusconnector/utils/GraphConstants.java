/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils;

import java.util.HashMap;
import java.util.Map;

public class GraphConstants {

    /*
     *  Short names for core properties
     */

    public static final String PROPERTY_NAME_GUID                             = "guid";
    public static final String PROPERTY_NAME_QUALIFIED_NAME                   = "qualifiedName";
    public static final String PROPERTY_NAME_NAME                             = "name";
    public static final String PROPERTY_NAME_VERSION                          = "version";
    public static final String PROPERTY_NAME_CREATED_BY                       = "createdBy";
    public static final String PROPERTY_NAME_CREATE_TIME                      = "createTime";
    public static final String PROPERTY_NAME_UPDATED_BY                       = "updatedBy";
    public static final String PROPERTY_NAME_UPDATE_TIME                      = "updateTime";
    public static final String PROPERTY_NAME_LABEL                            = "label";
    public static final String PROPERTY_NAME_PROXY                            = "proxy";
    public static final String PROPERTY_NAME_GLOSSARY_TERM                    = "glossaryTerm";
    public static final String PROPERTY_NAME_DISPLAY_NAME = "displayName";


    public static final String PROPERTY_KEY_PREFIX_ElEMENT = "ve";

    public static final String PROPERTY_KEY_ENTITY_GUID                    = PROPERTY_KEY_PREFIX_ElEMENT + PROPERTY_NAME_GUID;
    public static final String PROPERTY_KEY_NAME_QUALIFIED_NAME            = PROPERTY_KEY_PREFIX_ElEMENT + PROPERTY_NAME_QUALIFIED_NAME;
    public static final String PROPERTY_KEY_ENTITY_NAME                    = PROPERTY_KEY_PREFIX_ElEMENT + PROPERTY_NAME_NAME;
    public static final String PROPERTY_KEY_LABEL                          = PROPERTY_KEY_PREFIX_ElEMENT + PROPERTY_NAME_LABEL;
    public static final String PROPERTY_KEY_PROXY                          = PROPERTY_KEY_PREFIX_ElEMENT + PROPERTY_NAME_PROXY;
    public static final String PROPERTY_KEY_GLOSSARY_TERM                  = PROPERTY_KEY_PREFIX_ElEMENT + PROPERTY_NAME_GLOSSARY_TERM;
    public static final String PROPERTY_KEY_DISPLAY_NAME                   = PROPERTY_KEY_PREFIX_ElEMENT + PROPERTY_NAME_DISPLAY_NAME;


    public static final String NODE_LABEL_PROCESS = "process";
    public static final String NODE_LABEL_SUB_PROCESS = "subProcess";
    public static final String NODE_LABEL_TABLE = "table";
    public static final String NODE_LABEL_COLUMN = "column";
    public static final String NODE_LABEL_GLOSSARYTERM = "glossaryTerm";
    public static final String NODE_LABEL_CONDENSED = "condensedNode";

    public static final Map<String,String> corePropertyTypes = new HashMap<String,String>() {{
        put(PROPERTY_NAME_GUID,                           "java.lang.String");
        put(PROPERTY_NAME_NAME,                           "java.lang.String");
        put(PROPERTY_NAME_VERSION,                        "java.lang.Long");
        put(PROPERTY_NAME_CREATED_BY,                     "java.lang.String");
        put(PROPERTY_NAME_CREATE_TIME,                    "java.lang.Date");
        put(PROPERTY_NAME_UPDATED_BY,                     "java.lang.String");
        put(PROPERTY_NAME_UPDATE_TIME,                    "java.lang.Date");
        put(PROPERTY_NAME_LABEL,                          "java.lang.String");
        put(PROPERTY_NAME_PROXY,                          "java.lang.Boolean");

    }};

    public static final String EDGE_LABEL_INCLUDED_IN = "includedIn";
    public static final String EDGE_LABEL_COLUMN_AND_PROCESS = "processColumn";
    public static final String EDGE_LABEL_TABLE_AND_PROCESS = "processTable";
    public static final String EDGE_LABEL_SEMANTIC = "semantic-assignment";
    public static final String EDGE_LABEL_GLOSSARYTERM_TO_GLOSSARYTERM = "synonym";
    public static final String EDGE_LABEL_SUBPROCESS_TO_PROCESS = "sub-process";
    public static final String EDGE_LABEL_CONDENSED = "condensed";



    //Column
//    public static final String PROPERTY_KEY_DISPLAY_NAME = "displayName";
    public static final String PROPERTY_KEY_HOST_DISPLAY_NAME = "displayname";
    public static final String PROPERTY_KEY_DATABASE_DISPLAY_NAME = "databaseDisplayname";
    public static final String PROPERTY_KEY_SCHEMA_DISPLAY_NAME = "schemaDisplayname";
    public static final String PROPERTY_KEY_TABLE_DISPLAY_NAME = "tableDisplayname";

    //Process
    public static final String PROPERTY_KEY_CREATE_TIME= "createTime";
    public static final String PROPERTY_KEY_UPDATE_TIME = "updateTime";
    public static final String PROPERTY_KEY_FORMULA= "formula";
    public static final String PROPERTY_KEY_PROCESS_DESCRIPTION_URI= "descriptionURI";
    public static final String PROPERTY_KEY_VERSION= "version";
    public static final String PROPERTY_KEY_PROCESS_TYPE = "processType";
    public static final String PROPERTY_KEY_PARENT_PROCESS_GUID = "parent.process.guid";

    public static final String PROPERTY_KEY_GLOSSARY = "glossary";

    public static final String PROPERTY_KEY_PREFIX_RELATIONSHIP                      = "ed";


    public static final String PROPERTY_KEY_RELATIONSHIP_GUID                        = PROPERTY_KEY_PREFIX_RELATIONSHIP+PROPERTY_NAME_GUID;
    public static final String PROPERTY_KEY_RELATIONSHIP_VERSION                     = PROPERTY_KEY_PREFIX_RELATIONSHIP+PROPERTY_NAME_VERSION;
    public static final String PROPERTY_KEY_RELATIONSHIP_CREATED_BY                  = PROPERTY_KEY_PREFIX_RELATIONSHIP+PROPERTY_NAME_CREATED_BY;
    public static final String PROPERTY_KEY_RELATIONSHIP_CREATE_TIME                 = PROPERTY_KEY_PREFIX_RELATIONSHIP+PROPERTY_NAME_CREATE_TIME;
    public static final String PROPERTY_KEY_RELATIONSHIP_UPDATED_BY                  = PROPERTY_KEY_PREFIX_RELATIONSHIP+PROPERTY_NAME_UPDATED_BY;
    public static final String PROPERTY_KEY_RELATIONSHIP_UPDATE_TIME                 = PROPERTY_KEY_PREFIX_RELATIONSHIP+PROPERTY_NAME_UPDATE_TIME;
    public static final String PROPERTY_KEY_RELATIONSHIP_LABEL                       = PROPERTY_KEY_PREFIX_RELATIONSHIP+PROPERTY_NAME_LABEL;

    // Map of names to property key names
    public static final Map<String, String> corePropertiesRelationship = new HashMap<String,String>() {{
        put(PROPERTY_NAME_GUID, PROPERTY_KEY_RELATIONSHIP_GUID);
        put(PROPERTY_NAME_VERSION, PROPERTY_KEY_RELATIONSHIP_VERSION);
        put(PROPERTY_NAME_CREATED_BY, PROPERTY_KEY_RELATIONSHIP_CREATED_BY);
        put(PROPERTY_NAME_CREATE_TIME, PROPERTY_KEY_RELATIONSHIP_CREATE_TIME);
        put(PROPERTY_NAME_UPDATED_BY, PROPERTY_KEY_RELATIONSHIP_UPDATED_BY);
        put(PROPERTY_NAME_UPDATE_TIME, PROPERTY_KEY_RELATIONSHIP_UPDATE_TIME);
        put(PROPERTY_NAME_LABEL, PROPERTY_KEY_RELATIONSHIP_LABEL);

    }};
}