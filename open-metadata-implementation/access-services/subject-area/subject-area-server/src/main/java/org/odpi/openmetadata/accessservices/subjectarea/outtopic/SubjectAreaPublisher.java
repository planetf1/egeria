/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.outtopic;

import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.glossary.Glossary;
import org.odpi.openmetadata.accessservices.subjectarea.server.mappers.entities.GlossaryMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryValidator;

/**
 * SubjectAreaPublisher is responsible for publishing org.odpi.openmetadata.accessservices.subjectarea.common.events about glossary artifacts.  It is called
 * when an interesting OMRS Event is added to the Enterprise OMRS Topic.  It adds org.odpi.openmetadata.accessservices.subjectarea.common.events to the Subject Area OMAS
 * out topic.
 */
public class SubjectAreaPublisher
{
   private static final String assetPropertyNameDescription   = "description";

    private static final Logger log = LoggerFactory.getLogger(SubjectAreaPublisher.class);

    private Connection              SubjectAreaOutTopic;
    private OMRSRepositoryHelper    repositoryHelper;
    private OMRSRepositoryValidator repositoryValidator;
    private String                  componentName;


    /**
     * The constructor is given the connection to the out topic for Subject Area OMAS
     * along with classes for testing and manipulating instances.
     *
     * @param SubjectAreaOutTopic - connection to the out topic
     * @param repositoryHelper - provides methods for working with metadata instances
     * @param repositoryValidator - provides validation of metadata instance
     * @param componentName - name of component
     */
    public SubjectAreaPublisher(Connection               SubjectAreaOutTopic,
                                OMRSRepositoryHelper    repositoryHelper,
                                OMRSRepositoryValidator repositoryValidator,
                                String                  componentName)
    {
        this.SubjectAreaOutTopic = SubjectAreaOutTopic;
        this.repositoryHelper = repositoryHelper;
        this.repositoryValidator = repositoryValidator;
        this.componentName = componentName;
    }


    /**
     * Determine whether a new entity is a Glossary artifact or dependant entity.  If it is then publish an Subject Area Event about it.
     *
     * @param entity - entity object that has just been created.
     */
    public void processNewEntity(EntityDetail entity)
    {
        InstanceType type = entity.getType();
        String typeDefName = type.getTypeDefName();

        try {
            if (typeDefName.equals("Glossary")) {
                org.odpi.openmetadata.accessservices.subjectarea.generated.entities.Glossary.Glossary generatedGlossary=org.odpi.openmetadata.accessservices.subjectarea.generated.entities.Glossary.GlossaryMapper.mapOmrsEntityDetailToGlossary(entity);
                Glossary glossary= GlossaryMapper.mapOMRSBeantoGlossary(generatedGlossary);
                // TODO create OMAS event.
            }
        } catch (InvalidParameterException e) {
            // TODO create a more informative message
            log.error(e.getErrorMessage());
        }
    }


    /**
     * Determine whether an updated entity is a Glossary artifact or dependant entity .  If it is then publish an Subject Area Event about it.
     *
     * @param entity - entity object that has just been updated.
     */
    public void processUpdatedEntity(EntityDetail   entity)
    {

    }


    /**
     * Determine whether an updated entity is a Glossary artifact or dependant entity.  If it is then publish an Subject Area Event about it.
     *
     * @param originalEntity - original values for entity object - available when basic property updates have
     *                       occurred on the entity.
     * @param newEntity - entity object that has just been updated.
     */
    public void processUpdatedEntity(EntityDetail   originalEntity,
                                     EntityDetail   newEntity)
    {

    }


    /**
     * Determine whether a deleted entity is a Glossary artifact or dependant entity.  If it is then publish an Subject Area Event about it.
     *
     * @param entity - entity object that has just been deleted.
     */
    public void processDeletedEntity(EntityDetail   entity)
    {

    }


    /**
     * Determine whether a restored entity is a Glossary artifact or dependant entity.  If it is then publish an Subject Area Event about it.
     *
     * @param entity - entity object that has just been restored.
     */
    public void processRestoredEntity(EntityDetail   entity)
    {


    }


    /**
     * Determine whether a new relationship is related to an Asset.
     * If it is then publish an Subject Area Event about it.
     *
     * @param relationship - relationship object that has just been created.
     */
    public void processNewRelationship(Relationship relationship)
    {
        // todo
    }


    /**
     * Determine whether an updated relationship is related to an Asset.
     * If it is then publish an Subject Area Event about it.
     *
     * @param relationship - relationship object that has just been updated.
     */
    public void processUpdatedRelationship(Relationship   relationship)
    {
        // todo
    }


    /**
     * Determine whether an updated relationship is related to an Asset.
     * If it is then publish an Subject Area Event about it.
     *
     * @param originalRelationship  - original values of relationship.
     * @param newRelationship - relationship object that has just been updated.
     */
    public void processUpdatedRelationship(Relationship   originalRelationship,
                                           Relationship   newRelationship)
    {
        // todo
    }


    /**
     * Determine whether a deleted relationship is related to an Asset.
     * If it is then publish an Subject Area Event about it.
     *
     * @param relationship - relationship object that has just been deleted.
     */
    public void processDeletedRelationship(Relationship   relationship)
    {
        // todo
    }


    /**
     * Determine whether a restored relationship is related to an Asset.
     * If it is then publish an Subject Area Event about it.
     *
     * @param relationship - relationship object that has just been restored.
     */
    public void processRestoredRelationship(Relationship   relationship)
    {
        // todo
    }




}
