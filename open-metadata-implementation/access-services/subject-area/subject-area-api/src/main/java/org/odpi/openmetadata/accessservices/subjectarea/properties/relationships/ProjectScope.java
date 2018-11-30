/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
// This is a generated file - do not edit - changes should be made to the templates amd/or generator to generate this file with changes.

package org.odpi.openmetadata.accessservices.subjectarea.properties.relationships;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.*;
import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;
import org.odpi.openmetadata.accessservices.subjectarea.properties.enums.*;

//omrs
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
//omrs beans
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.line.Line;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.line.LineType;

/**
 * ProjectScope is a relationship between an entity of type Project and an entity of type Referenceable.
 * The ends of the relationship are stored as entity proxies, where there is a 'proxy' name by which the entity type is known.
 * The first entity proxy has projectsImpactingAssets as the proxy name for entity type Project.
 * The second entity proxy has projectScope as the proxy name for entity type Referenceable.
 *
 * Each entity proxy also stores the entities guid.

 The documentation, assets and definitions that are affected by the project.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class ProjectScope extends Line {
    private static final Logger log = LoggerFactory.getLogger(ProjectScope.class);
    private static final String className = ProjectScope.class.getName();

   //public java.util.Set<String> propertyNames = new HashSet<>();
      public static final String[] PROPERTY_NAMES_SET_VALUES = new String[] {
          "scopeDescription",

      // Terminate the list
          null
      };
      public static final String[] ATTRIBUTE_NAMES_SET_VALUES = new String[] {
          "scopeDescription",

       // Terminate the list
          null
      };
      public static final String[] ENUM_NAMES_SET_VALUES = new String[] {

           // Terminate the list
            null
      };
      public static final String[] MAP_NAMES_SET_VALUES = new String[] {

           // Terminate the list
           null
      };
      public static final java.util.Set<String> PROPERTY_NAMES_SET = new HashSet(new HashSet<>(Arrays.asList(PROPERTY_NAMES_SET_VALUES)));
      public static final java.util.Set<String> ATTRIBUTE_NAMES_SET = new HashSet(new HashSet<>(Arrays.asList(ATTRIBUTE_NAMES_SET_VALUES)));
      public static final java.util.Set<String> ENUM_NAMES_SET = new HashSet(new HashSet<>(Arrays.asList(ENUM_NAMES_SET_VALUES)));
      public static final java.util.Set<String> MAP_NAMES_SET = new HashSet(new HashSet<>(Arrays.asList(MAP_NAMES_SET_VALUES)));


    public ProjectScope() {
        initialise();
    }

    private void initialise()
    {
       name = "ProjectScope";
       // set the LineType if this is a LineType enum value.
       try {
           lineType = LineType.valueOf(name);
        }
        catch (IllegalArgumentException e) {
           lineType = LineType.Other;
        }
        entity1Name = "projectsImpactingAssets";
        entity1Type = "Project";
        entity2Name = "projectScope";
        entity2Type = "Referenceable";
        typeDefGuid = "bc63ac45-b4d0-4fba-b583-92859de77dd8";
    }

    public ProjectScope(Line template) {
        super(template);
        initialise();
    }

    public ProjectScope(Relationship omrsRelationship) {
        super(omrsRelationship);
        name = "ProjectScope";
       // set the LineType if this is a LineType enum value.
       try {
           lineType = LineType.valueOf(name);
        }
        catch (IllegalArgumentException e) {
           lineType = LineType.Other;
        }
    }

    InstanceProperties obtainInstanceProperties() {
          final String methodName = "obtainInstanceProperties";
          if (log.isDebugEnabled()) {
                 log.debug("==> Method: " + methodName);
          }
          InstanceProperties instanceProperties = new InstanceProperties();
          EnumPropertyValue enumPropertyValue=null;
          MapPropertyValue mapPropertyValue=null;
          PrimitivePropertyValue primitivePropertyValue=null;
          primitivePropertyValue = new PrimitivePropertyValue();
          // TODO  description + change null to value
          primitivePropertyValue.setPrimitiveValue(null);
          instanceProperties.setProperty("scopeDescription",primitivePropertyValue);
          if (log.isDebugEnabled()) {
                 log.debug("<== Method: " + methodName);
          }
          return instanceProperties;
    }

         private Date scopeDescription;
        /**
            * {@literal Description of how each item is being changed by the project. }
            * @return {@code Date }
            */
         public Date getScopeDescription() {
             return this.scopeDescription;
         }
         public void setScopeDescription(Date scopeDescription)  {
            this.scopeDescription = scopeDescription;
        }

      @Override
         public StringBuilder toString(StringBuilder sb)
         {
             if (sb == null)
             {
                 sb = new StringBuilder();
             }
             sb.append(" ProjectScope=");
             sb.append(super.toString(sb));
             sb.append(" ProjectScope Attributes{");
             sb.append("scopeDescription=" + this.scopeDescription +",");
             sb.append("}");
             return sb;
         }
         @Override
         public String toString() {
             return toString(new StringBuilder()).toString();
         }


}
