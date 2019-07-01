/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.glossaryview.rest;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class GlossaryViewEntityDetailFactory {

    private final static String DEFAULT = "Default";
    private final static String GLOSSARY = "Glossary";
    private final static String CATEGORY = "GlossaryCategory";
    private final static String TERM = "GlossaryTerm";
    private final static String EXTERNAL_GLOSSARY_LINK = "ExternalGlossaryLink";

    private final static Map<String, Supplier<GlossaryViewEntityDetail>> workers = new HashMap<>();
    static{
        workers.put(DEFAULT, () -> new GlossaryViewEntityDetail());
        workers.put(GLOSSARY, () -> new Glossary());
        workers.put(CATEGORY, () -> new Category());
        workers.put(TERM, () -> new Term());
        workers.put(EXTERNAL_GLOSSARY_LINK, () -> new GlossaryViewEntityDetail());
    }

    public static GlossaryViewEntityDetail build(String entityType){
        if(!workers.containsKey(entityType)){
            return workers.get(DEFAULT).get();
        }
        return workers.get(entityType).get();
    }

}
