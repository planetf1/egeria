/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.subjectarea.properties.classifications;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.accessservices.subjectarea.properties.enums.ActivityType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * Identifies that this glossary term describes an activity.
 */

@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class ActivityDescription extends Classification {
    private static final Logger log = LoggerFactory.getLogger( ActivityDescription.class);
    private static final String className =  ActivityDescription.class.getName();
    private Map<String, String> extraAttributes;


    private static final String[] PROPERTY_NAMES_SET_VALUES = new String[] {
        "activityType",

    // Terminate the list
        null
    };
    private static final String[] ATTRIBUTE_NAMES_SET_VALUES = new String[] {

     // Terminate the list
        null
    };
    private static final String[] ENUM_NAMES_SET_VALUES = new String[] {
         "activityType",

         // Terminate the list
          null
    };
    private static final String[] MAP_NAMES_SET_VALUES = new String[] {

         // Terminate the list
         null
    };
    // note the below definitions needs to be fully qualified
    private static final java.util.Set<String> PROPERTY_NAMES_SET = new HashSet(new HashSet<>(Arrays.asList(PROPERTY_NAMES_SET_VALUES)));
    private static final java.util.Set<String> ATTRIBUTE_NAMES_SET = new HashSet(new HashSet<>(Arrays.asList(ATTRIBUTE_NAMES_SET_VALUES)));
    private static final java.util.Set<String> ENUM_NAMES_SET = new HashSet(new HashSet<>(Arrays.asList(ENUM_NAMES_SET_VALUES)));
    private static final java.util.Set<String> MAP_NAMES_SET = new HashSet(new HashSet<>(Arrays.asList(MAP_NAMES_SET_VALUES)));
    /**
     * Default constructor
     */
    public ActivityDescription() {
            super.classificationName="ActivityDescription";
    }

       private ActivityType activityType;
       /**
        * {@literal Classification of the activity. }
        * @return {$$PropertyTypeJavadoc$$ }
        */
       public ActivityType getActivityType() {
           return this.activityType;
       }
       public void setActivityType(ActivityType activityType)  {
           this.activityType = activityType;
       }



    /**
      * Get the extra attributes - ones that are in addition to the standard types.
      * @return extra attributes
      */
    public Map<String, String> getAdditionalProperties() {
          return extraAttributes;
    }
    public void setAdditionalProperties(Map<String, String> additionalProperties) {
          this.extraAttributes = additionalProperties;
    }
}
