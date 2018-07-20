/* SPDX-License-Identifier: Apache-2.0 */

// This is a generated file - do not edit - changes should be made to the templates amd/or generator to generate this file with changes.

package org.odpi.openmetadata.accessservices.subjectarea.generated.classifications.Retention;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

import org.odpi.openmetadata.accessservices.subjectarea.properties.classifications.Task;

// omrs
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.*;
import org.odpi.openmetadata.repositoryservices.archivemanager.OMRSArchiveAccessor;

// omrs bean
import org.odpi.openmetadata.accessservices.subjectarea.properties.enums.*;
import org.odpi.openmetadata.accessservices.subjectarea.server.properties.classifications.ClassificationFactory;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Classification;
import org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.InvalidParameterException;
import org.odpi.openmetadata.accessservices.subjectarea.properties.enums.Status;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.common.SystemAttributes;
import org.odpi.openmetadata.accessservices.subjectarea.utilities.SubjectAreaUtils;

/**
 * Static mapping methods to map between Retention and the omrs equivalents.
 */
public class RetentionMapper {
    private static final Logger log = LoggerFactory.getLogger( RetentionMapper.class);
    private static final String className = RetentionMapper.class.getName();
   /**
    * @param omrsClassification - the supplied omrs classification
    * @return equivalent Retention
    */
   static public org.odpi.openmetadata.accessservices.subjectarea.properties.classifications.Retention mapOmrsToOmas(Classification omrsClassification) throws InvalidParameterException{
        String classificationTypeName = omrsClassification.getName();
        if ("Retention".equals(classificationTypeName)) {
                org.odpi.openmetadata.accessservices.subjectarea.properties.classifications.Retention retention = new org.odpi.openmetadata.accessservices.subjectarea.properties.classifications.Retention();
                //set core attributes
                SystemAttributes systemAttributes = new SystemAttributes();

                InstanceStatus instanceStatus =  omrsClassification.getStatus();
                Status omrsBeanStatus = SubjectAreaUtils.convertInstanceStatusToStatus(instanceStatus);
                systemAttributes.setStatus(omrsBeanStatus);

                systemAttributes.setCreatedBy(omrsClassification.getCreatedBy());
                systemAttributes.setUpdatedBy(omrsClassification.getUpdatedBy());
                systemAttributes.setCreateTime(omrsClassification.getCreateTime());
                systemAttributes.setUpdateTime(omrsClassification.getUpdateTime());
                systemAttributes.setVersion(omrsClassification.getVersion());
                retention.setSystemAttributes(systemAttributes);


                // Set properties
                InstanceProperties omrsClassificationProperties = omrsClassification.getProperties();
                Iterator omrsPropertyIterator = omrsClassificationProperties.getPropertyNames();
                while (omrsPropertyIterator.hasNext()) {
                    String name = (String) omrsPropertyIterator.next();
                    //TODO check if this is a property we expect or whether the type has been added to.
                    // this is a property we expect
                    InstancePropertyValue value = omrsClassificationProperties.getPropertyValue(name);

                    // supplied guid matches the expected type

                    Object actualValue;
                    switch (value.getInstancePropertyCategory()) {
                        case PRIMITIVE:
                            PrimitivePropertyValue primitivePropertyValue = (PrimitivePropertyValue) value;
                            actualValue = primitivePropertyValue.getPrimitiveValue();
                            if (retention.ATTRIBUTE_NAMES_SET.contains(name)) {
                               if (name.equals("confidence")) {
                                  retention.setConfidence((Integer)actualValue);
                               }
                               if (name.equals("steward")) {
                                  retention.setSteward((String)actualValue);
                               }
                               if (name.equals("source")) {
                                  retention.setSource((String)actualValue);
                               }
                               if (name.equals("notes")) {
                                  retention.setNotes((String)actualValue);
                               }
                               if (name.equals("associatedGUID")) {
                                  retention.setAssociatedGUID((String)actualValue);
                               }
                               if (name.equals("archiveAfter")) {
                                  retention.setArchiveAfter((java.util.Date)actualValue);
                               }
                               if (name.equals("deleteAfter")) {
                                  retention.setDeleteAfter((java.util.Date)actualValue);
                               }
                            } else {
                                // put out the omrs value object
                                if (null==retention.getExtraAttributes())  {
                                     retention.setExtraAttributes(new HashMap<String, Object>());
                                }
                               retention.getExtraAttributes().put(name, primitivePropertyValue);
                            }
                            break;
                        case ENUM:
                            EnumPropertyValue enumPropertyValue = (EnumPropertyValue) value;
                            String symbolicName = enumPropertyValue.getSymbolicName();
                            if (retention.ENUM_NAMES_SET.contains(name)) {
                                 if (name.equals("status")) {
                                       org.odpi.openmetadata.accessservices.subjectarea.properties.enums.GovernanceClassificationStatus status = org.odpi.openmetadata.accessservices.subjectarea.properties.enums.GovernanceClassificationStatus.valueOf(symbolicName);
                                      retention.setStatus(status);
                                 }
                                 if (name.equals("basis")) {
                                       org.odpi.openmetadata.accessservices.subjectarea.properties.enums.RetentionBasis basis = org.odpi.openmetadata.accessservices.subjectarea.properties.enums.RetentionBasis.valueOf(symbolicName);
                                      retention.setBasis(basis);
                                 }
                            } else {
                                // put out the omrs value object
                                if (null==retention.getExtraAttributes())  {
                                     retention.setExtraAttributes(new HashMap<String, Object>());
                                }
                                 retention.getExtraAttributes().put(name, enumPropertyValue);
                             }

                            break;
                        case MAP:
                            if (retention.MAP_NAMES_SET.contains(name)) {
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

                 return retention;
            } else {
                // TODO wrong type
            }
            return null;
    }
    /**
     * Map (convert) the supplied Retention to an classificationDetail.
     * @param  retention  supplied Retention
     * @return  classificationDetail equivalent to retention
     */
    static public Classification mapBeanToOmrs(org.odpi.openmetadata.accessservices.subjectarea.properties.classifications.Retention retention) {
            Classification omrsClassification = new Classification();
            SystemAttributes systemAttributes = retention.getSystemAttributes();
            if (systemAttributes!=null) {
                   if (systemAttributes.getCreatedBy()!=null)
                        omrsClassification.setCreatedBy(systemAttributes.getCreatedBy());
                   if (systemAttributes.getUpdatedBy()!=null)
                        omrsClassification.setUpdatedBy(systemAttributes.getUpdatedBy());
                   if (systemAttributes.getCreateTime()!=null)
                        omrsClassification.setCreateTime(systemAttributes.getCreateTime());
                   if (systemAttributes.getUpdateTime()!=null)
                        omrsClassification.setUpdateTime(systemAttributes.getUpdateTime());
                   if (systemAttributes.getVersion()!=null)
                        omrsClassification.setVersion(systemAttributes.getVersion());
                   if (systemAttributes.getStatus()!=null) {
                        InstanceStatus instanceStatus = SubjectAreaUtils.convertStatusToStatusInstance(systemAttributes.getStatus());
                        omrsClassification.setStatus(instanceStatus);
                   }
            }

            InstanceProperties instanceProperties = new InstanceProperties();
            // primitives

            if (retention.getConfidence()!=null) {
                PrimitivePropertyValue primitivePropertyValue = new PrimitivePropertyValue();
                primitivePropertyValue.setPrimitiveDefCategory(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_INT);
                primitivePropertyValue.setPrimitiveValue(retention.getConfidence());
                instanceProperties.setProperty("confidence", primitivePropertyValue);
            }
            if (retention.getSteward()!=null) {
                PrimitivePropertyValue primitivePropertyValue = new PrimitivePropertyValue();
                primitivePropertyValue.setPrimitiveDefCategory(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_STRING);
                primitivePropertyValue.setPrimitiveValue(retention.getSteward());
                instanceProperties.setProperty("steward", primitivePropertyValue);
            }
            if (retention.getSource()!=null) {
                PrimitivePropertyValue primitivePropertyValue = new PrimitivePropertyValue();
                primitivePropertyValue.setPrimitiveDefCategory(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_STRING);
                primitivePropertyValue.setPrimitiveValue(retention.getSource());
                instanceProperties.setProperty("source", primitivePropertyValue);
            }
            if (retention.getNotes()!=null) {
                PrimitivePropertyValue primitivePropertyValue = new PrimitivePropertyValue();
                primitivePropertyValue.setPrimitiveDefCategory(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_STRING);
                primitivePropertyValue.setPrimitiveValue(retention.getNotes());
                instanceProperties.setProperty("notes", primitivePropertyValue);
            }
            if (retention.getAssociatedGUID()!=null) {
                PrimitivePropertyValue primitivePropertyValue = new PrimitivePropertyValue();
                primitivePropertyValue.setPrimitiveDefCategory(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_STRING);
                primitivePropertyValue.setPrimitiveValue(retention.getAssociatedGUID());
                instanceProperties.setProperty("associatedGUID", primitivePropertyValue);
            }
            if (retention.getArchiveAfter()!=null) {
                PrimitivePropertyValue primitivePropertyValue = new PrimitivePropertyValue();
                primitivePropertyValue.setPrimitiveDefCategory(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_DATE);
                primitivePropertyValue.setPrimitiveValue(retention.getArchiveAfter());
                instanceProperties.setProperty("archiveAfter", primitivePropertyValue);
            }
            if (retention.getDeleteAfter()!=null) {
                PrimitivePropertyValue primitivePropertyValue = new PrimitivePropertyValue();
                primitivePropertyValue.setPrimitiveDefCategory(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_DATE);
                primitivePropertyValue.setPrimitiveValue(retention.getDeleteAfter());
                instanceProperties.setProperty("deleteAfter", primitivePropertyValue);
            }
            if (retention.getStatus()!=null) {
                GovernanceClassificationStatus enumType = retention.getStatus();
                EnumPropertyValue enumPropertyValue = new EnumPropertyValue();
                enumPropertyValue.setOrdinal(enumType.ordinal());
                enumPropertyValue.setSymbolicName(enumType.name());
                instanceProperties.setProperty("status", enumPropertyValue);
            }
            if (retention.getBasis()!=null) {
                RetentionBasis enumType = retention.getBasis();
                EnumPropertyValue enumPropertyValue = new EnumPropertyValue();
                enumPropertyValue.setOrdinal(enumType.ordinal());
                enumPropertyValue.setSymbolicName(enumType.name());
                instanceProperties.setProperty("basis", enumPropertyValue);
            }
            omrsClassification.setProperties(instanceProperties);
            // set the type in the classification
            OMRSArchiveAccessor archiveAccessor = OMRSArchiveAccessor.getInstance();
            TypeDef typeDef = archiveAccessor.getEntityDefByName("Retention");
            InstanceType template = SubjectAreaUtils.createTemplateFromTypeDef(typeDef);
            InstanceType instanceType = new InstanceType(template);
            omrsClassification.setType(instanceType);
            return omrsClassification;
    }

}
