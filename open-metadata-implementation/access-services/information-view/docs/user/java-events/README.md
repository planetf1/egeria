Receiving Information View OMAS(IV OMAS) Events using Java


Information View OMAS listens to 2 topics:
* cohort topic (topic name is defined in cohortOMRSTopicConnection)
* information view IN topic (topic name is defined in accessServiceInTopic for access service "Information View")

and publishes events to one topic:
* information view OUT topic (topic name is defined in accessServiceOutTopic for access service "Information View")

From cohort topic IV OMAS listens to the following OMRS events:
 
 *  NewRelationshipEvent with type SemanticAssignment between a RelationalColumn and a GlossaryTerm. Following events will be published on OUT topic:
    * a SemanticAssignment event
    * event containing the full context of the parent table of the referenced column: host address of the database, database name, schema name, table name, all columns (including primary and foreign keys defined, column type) and the business terms assigned (null if column doesn't have any business term) 
 
 *  UpdatedEntityEvent for a GlossaryTerm. Following events will be published on OUT topic:
    * an UpdatedEntityEvent
    * if the glossary term name was updated, it will publish events containing the full context of tables that contain a column linked to the referenced glossary term 
    
 *  DeletedRelationshipEvent with type SemanticAssignment between a RelationalColumn and a GlossaryTerm. Following events will be published on OUT topic: 
    *  event containing the full context of the parent table of the referenced column: host address of the database, database name, schema name, table name, all columns (including primary and foreign keys defined, column type) and the business terms assigned (null if column doesn't have any business term) 
  
        
From IN topic IV OMAS listens to events describing views: host address of the database, database name, schema name, table name, all derived columns (including the referenced real columns)



----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.