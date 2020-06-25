/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */


import React, { useContext, useEffect }           from "react";

import { TypesContext }                from "../../contexts/TypesContext";  

import { FocusContext }                from "../../contexts/FocusContext";

import "./focus-controls.scss"


/*
 * When FocusControls is rendered it populates the type selectors with types that are 
 * available from the TypesContext.
 * 
 * The entity, relationship and classification type selectors allow the user to select a single type.
 * The type selected for entity type is used as the focus type.
 * The type for either relationship or classification sets the view type.
 * 
 */


export default function FocusControls() {
  
  const typesContext                = useContext(TypesContext);

  const focusContext                = useContext(FocusContext);

  const entityTypes                 = typesContext.getEntityTypes();
  const relationshipTypes           = typesContext.getRelationshipTypes();
  const classificationTypes         = typesContext.getClassificationTypes();

  let entityTypesSorted             = null;
  let relationshipTypesSorted       = null;
  let classificationTypesSorted     = null;

  if (entityTypes) {
    entityTypesSorted = Object.keys(entityTypes).sort();
  }
  if (relationshipTypes) {
    relationshipTypesSorted = Object.keys(relationshipTypes).sort();
  }
  if (classificationTypes) {
    classificationTypesSorted = Object.keys(classificationTypes).sort();
  }
 


  /* 
   * When the focus is changed - this resets the view selectors
   */
  const entityChangeHandler = (e) => {
    const typeName = e.target.value;
    resetRelTypeSelector();    
    resetClsTypeSelector();
    focusContext.typeSelected("Entity",typeName);
  };

  /* 
   * When the view is changed - this resets the other view selector
   */
  const relationshipSelectorHandler = (e) => {        
    const typeName = e.target.value;
    resetClsTypeSelector();
    focusContext.typeSelected("Relationship",typeName);
  };

  const classificationSelectorHandler = (e) => {    
    const typeName = e.target.value;
    resetRelTypeSelector();     
    focusContext.typeSelected("Classification",typeName);
  };

  /*
   * Reset the entity type selector
   */
  const resetEntTypeSelector = () => {
    const selector = document.getElementById('entityTypeSelector');
    selector.value = "none";
  };

  /*
   * Reset the relationship type selector
   */
  const resetRelTypeSelector = () => {
    const selector = document.getElementById('relationshipTypeSelector');
    selector.value = "none";
  };

  /*
   * Reset the classification type selector
   */
  const resetClsTypeSelector = () => {
    const selector = document.getElementById('classificationTypeSelector');
    selector.value = "none";
  };


  /* 
   * A focus change can be externally initiated, in which case the focus controls
   * need to be updated to reflect the new focus and view state
   * 
   */
  const updateSelectorValues = () => {
    let selector;
    switch (focusContext.view.category) {

      case "Entity":
        selector = document.getElementById("entityTypeSelector");
        selector.value = focusContext.view.typeName;
        resetRelTypeSelector();     
        resetClsTypeSelector();
        break;

      case "Relationship":
        selector = document.getElementById("relationshipTypeSelector");
        selector.value = focusContext.view.typeName;
        resetClsTypeSelector();
        break;

      case "Classification":
        selector = document.getElementById("classificationTypeSelector");
        selector.value = focusContext.view.typeName;
        resetRelTypeSelector();
        break;
    }
  };

  /*
   * On focus change...
   */
  useEffect(
    () => {      
      if (focusContext.view.typeName !== "")
        updateSelectorValues();
      else {
        resetEntTypeSelector();
        resetRelTypeSelector();
        resetClsTypeSelector();
      }
    },
    [focusContext.view]
  )
 
 


  return (

    <div className="filterControls">

      <p>Select Type to display</p>
                
      <label htmlFor="entityTypeSelector">Entity Types:  </label>                                                                                                  
      <select className="typeSelector" id="entityTypeSelector" name="entityTypeSelector" onChange={entityChangeHandler}>
        {
        entityTypesSorted === null && 
          ( <option value="dummy" disabled defaultValue>No types to display</option> )
        }
        {
          entityTypesSorted !== null &&
          ( <option value="none" defaultValue>Focus on entity type...</option> )
        }
        {
          entityTypesSorted !== null && 
          (
            entityTypesSorted.map(typeName => 
              ( <option key={typeName} value={typeName} > {typeName} </option> )
            )      
          )                                         
        }      
      </select>
          
      <br/>
          
      <label htmlFor="relationshipTypeSelector">Relationship Types:  </label>                                                                                                      
      <select className="typeSelector" id="relationshipTypeSelector" name="relationshipTypeSelector" onChange={relationshipSelectorHandler}>
        {
          relationshipTypesSorted === null && 
          ( <option value="dummy" disabled defaultValue>No types to display</option> )
        }
        {
          relationshipTypesSorted !== null &&
          ( <option value="none" defaultValue>View relationship type...</option> )
        }
        {
          relationshipTypesSorted !== null && 
          (
            relationshipTypesSorted.map(typeName => 
              ( <option key={typeName} value={typeName}> {typeName} </option> )
            )      
          )                                         
        }      
      </select>
          
      <br/>

      <label htmlFor="classificationTypeSelector">Classification Types:  </label>                                                                                                      
      <select className="typeSelector" id="classificationTypeSelector" name="classificationTypeSelector" onChange={classificationSelectorHandler}>
        {
          classificationTypesSorted === null && 
          ( <option value="dummy" disabled defaultValue>No types to display</option> )
        }
        {
          classificationTypesSorted !== null &&
          ( <option value="none" defaultValue>View classification type...</option> )
        }
        {
          classificationTypesSorted !== null && 
          (
            classificationTypesSorted.map(typeName => 
              ( <option key={typeName} value={typeName}> {typeName} </option> )
            )      
          )                                         
        }      
      </select>

    </div>

  );

}


