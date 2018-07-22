/* SPDX-License-Identifier: Apache-2.0 */

// This is a generated file - do not edit - changes should be made to the templates amd/or generator to generate this file with changes.

package org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.UsedInContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.*;

import org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.UsedInContext.UsedInContext;
import org.odpi.openmetadata.accessservices.subjectarea.properties.enums.*;
import org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.InvalidParameterException;
import org.odpi.openmetadata.accessservices.subjectarea.properties.enums.Status;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.common.SystemAttributes;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.line.Line;
import org.odpi.openmetadata.accessservices.subjectarea.utilities.SubjectAreaUtils;

import java.util.*;

/**
 * Static mapping methods to map between UsedInContext and the omrs Relationship.
 */
public class UsedInContextMapper {
       private static final Logger log = LoggerFactory.getLogger(UsedInContextMapper.class);
       private static final String className = UsedInContextMapper.class.getName();

       public static UsedInContext mapOmrsRelationshipToUsedInContext(Relationship omrsRelationship)  throws InvalidParameterException {
     
               if ("UsedInContext".equals(omrsRelationship.getType().getTypeDefName())) {
                   UsedInContext usedInContext = new UsedInContext(omrsRelationship);

                   SystemAttributes systemAttributes = new SystemAttributes();
                   InstanceStatus instanceStatus =  omrsRelationship.getStatus();
                   Status omrsBeanStatus = SubjectAreaUtils.convertInstanceStatusToStatus(instanceStatus);
                   systemAttributes.setStatus(omrsBeanStatus);
                   systemAttributes.setCreatedBy(omrsRelationship.getCreatedBy());
                   systemAttributes.setUpdatedBy(omrsRelationship.getUpdatedBy());
                   systemAttributes.setCreateTime(omrsRelationship.getCreateTime());
                   systemAttributes.setUpdateTime(omrsRelationship.getUpdateTime());
                   systemAttributes.setVersion(omrsRelationship.getVersion());
                   systemAttributes.setGUID(omrsRelationship.getGUID());
                   usedInContext.setSystemAttributes(systemAttributes);

                   InstanceProperties omrsRelationshipProperties = omrsRelationship.getProperties();
                   Iterator omrsPropertyIterator = omrsRelationshipProperties.getPropertyNames();
                   while (omrsPropertyIterator.hasNext()) {
                       String name = (String) omrsPropertyIterator.next();
                       //TODO check if this is a property we expect or whether the type has been added to.
                       // this is a property we expect
                       InstancePropertyValue value = omrsRelationshipProperties.getPropertyValue(name);
                       // supplied guid matches the expected type
                       Object actualValue;
                       switch (value.getInstancePropertyCategory()) {
                                               case PRIMITIVE:
                                                   PrimitivePropertyValue primitivePropertyValue = (PrimitivePropertyValue) value;
                                                   actualValue = primitivePropertyValue.getPrimitiveValue();
                                                   if (UsedInContext.ATTRIBUTE_NAMES_SET.contains(name)) {
                                                      if (name.equals("description")) {
                                                           usedInContext.setDescription((String)actualValue);
                                                      }
                                                      if (name.equals("expression")) {
                                                           usedInContext.setExpression((String)actualValue);
                                                      }
                                                      if (name.equals("steward")) {
                                                           usedInContext.setSteward((String)actualValue);
                                                      }
                                                      if (name.equals("source")) {
                                                           usedInContext.setSource((String)actualValue);
                                                      }
                                                   } else {
                                                       // put out the omrs value object
                                                       if (usedInContext.getExtraAttributes() ==null) {
                                                            Map<String, Object> extraAttributes = new HashMap();
                                                            usedInContext.setExtraAttributes(extraAttributes);
                                                        }
                                                      usedInContext.getExtraAttributes().put(name, primitivePropertyValue);
                                                   }
                                                   break;
                                               case ENUM:
                                                   EnumPropertyValue enumPropertyValue = (EnumPropertyValue) value;
                                                   String symbolicName = enumPropertyValue.getSymbolicName();
                                                   if (UsedInContext.ENUM_NAMES_SET.contains(name)) {
                                                        if (name.equals("status")) {
                                                              org.odpi.openmetadata.accessservices.subjectarea.properties.enums.TermRelationshipStatus status = org.odpi.openmetadata.accessservices.subjectarea.properties.enums.TermRelationshipStatus.valueOf(symbolicName);
                                                             usedInContext.setStatus(status);
                                                        }
                                                   } else {
                                                       // put out the omrs value object
                                                        if (usedInContext.getExtraAttributes() ==null) {
                                                            Map<String, Object> extraAttributes = new HashMap();
                                                            usedInContext.setExtraAttributes(extraAttributes);
                                                        }

                                                        usedInContext.getExtraAttributes().put(name, enumPropertyValue);
                                                    }
                       
                                                   break;
                                               case MAP:
                                                    if (usedInContext.MAP_NAMES_SET.contains(name)) {
                                                        MapPropertyValue mapPropertyValue = (MapPropertyValue) value;
                                                        InstanceProperties instancePropertyForMap = (InstanceProperties) mapPropertyValue.getMapValues();

                                                    }
                                                    break;
                                               case ARRAY:
                                               case STRUCT:
                                               case UNKNOWN:
                                                   // error
                                                   break;
                                           }
                       
                                       }   // end while
                                       return usedInContext;

                   } else {
                       // TODO wrong type for this guid
                   }
                   return null;
       }
       public static Relationship mapUsedInContextToOmrsRelationship(UsedInContext usedInContext) {
           Relationship omrsRelationship = Line.createOmrsRelationship(usedInContext);

           SystemAttributes systemAttributes = usedInContext.getSystemAttributes();
           if (systemAttributes!=null) {
               if (systemAttributes.getCreatedBy()!=null)
                   omrsRelationship.setCreatedBy(systemAttributes.getCreatedBy());
               if (systemAttributes.getUpdatedBy()!=null)
                   omrsRelationship.setUpdatedBy(systemAttributes.getUpdatedBy());
               if (systemAttributes.getCreateTime()!=null)
                   omrsRelationship.setCreateTime(systemAttributes.getCreateTime());
               if (systemAttributes.getUpdateTime()!=null)
                   omrsRelationship.setUpdateTime(systemAttributes.getUpdateTime());
               if (systemAttributes.getVersion()!=null)
                   omrsRelationship.setVersion(systemAttributes.getVersion());
               if (systemAttributes.getStatus()!=null) {
                   InstanceStatus instanceStatus = SubjectAreaUtils.convertStatusToStatusInstance(systemAttributes.getStatus());
                   omrsRelationship.setStatus(instanceStatus);
               }
           }

           InstanceProperties instanceProperties = new InstanceProperties();
           // primitives

            if (usedInContext.getDescription()!=null) {
                PrimitivePropertyValue primitivePropertyValue = new PrimitivePropertyValue();
                primitivePropertyValue.setPrimitiveDefCategory(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_STRING);
                primitivePropertyValue.setPrimitiveValue(usedInContext.getDescription());
                instanceProperties.setProperty("description", primitivePropertyValue);
            }
            if (usedInContext.getExpression()!=null) {
                PrimitivePropertyValue primitivePropertyValue = new PrimitivePropertyValue();
                primitivePropertyValue.setPrimitiveDefCategory(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_STRING);
                primitivePropertyValue.setPrimitiveValue(usedInContext.getExpression());
                instanceProperties.setProperty("expression", primitivePropertyValue);
            }
            if (usedInContext.getSteward()!=null) {
                PrimitivePropertyValue primitivePropertyValue = new PrimitivePropertyValue();
                primitivePropertyValue.setPrimitiveDefCategory(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_STRING);
                primitivePropertyValue.setPrimitiveValue(usedInContext.getSteward());
                instanceProperties.setProperty("steward", primitivePropertyValue);
            }
            if (usedInContext.getSource()!=null) {
                PrimitivePropertyValue primitivePropertyValue = new PrimitivePropertyValue();
                primitivePropertyValue.setPrimitiveDefCategory(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_STRING);
                primitivePropertyValue.setPrimitiveValue(usedInContext.getSource());
                instanceProperties.setProperty("source", primitivePropertyValue);
            }
            if (usedInContext.getStatus()!=null) {
                TermRelationshipStatus enumType = usedInContext.getStatus();
                EnumPropertyValue enumPropertyValue = new EnumPropertyValue();
                enumPropertyValue.setOrdinal(enumType.ordinal());
                enumPropertyValue.setSymbolicName(enumType.name());
                instanceProperties.setProperty("status", enumPropertyValue);
            }
            omrsRelationship.setProperties(instanceProperties);

           return omrsRelationship;
       }
}
