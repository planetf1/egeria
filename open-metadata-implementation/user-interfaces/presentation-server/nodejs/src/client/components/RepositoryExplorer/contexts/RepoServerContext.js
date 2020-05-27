/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */


import React, { createContext, useState, useContext } from "react";

import PropTypes from "prop-types";

import { IdentificationContext } from "../../../contexts/IdentificationContext";



export const RepoServerContext = createContext();

export const RepoServerContextConsumer = RepoServerContext.Consumer;






const RepoServerContextProvider = (props) => {

  


  const identificationContext = useContext(IdentificationContext);
  

  /*
   * It is possible to set up defaults for the context here .... although not actually wanted in production....
   */
  const [repositoryServerName, setRepositoryServerName]                         = useState("Schema_Server");  
  const [repositoryServerURLRoot, setRepositoryServerURLRoot]                   = useState("http://localhost:8082");  
  const [repositoryServerEnterpriseOption, setRepositoryServerEnterpriseOption] = useState(false);  

  console.log("RepoServerContext: being rendered, serverName: "+repositoryServerName);
  //console.log("RepoServerContext: being rendered, serverName: "+repositoryServerName+" ent option: "+repositoryServerEnterpriseOption.toString());

  /*
   *  Set all three of the repository server and scope attributes
   */
  //const setServerDetails = async (details) => {
//    await setRepositoryServerName(details.serverName);
//    setRepositoryServerURLRoot(details.serverURLRoot);
//    setRepositoryServerEnterpriseOption(details.enterpriseOption);
//  };
   
  // TODO - if this works you will need to add similar mewthods for serverName and serverURLRoot
  const getRepositoryServerEnterpriseOption = () => {
    console.log("RepoServerContext: getRepositoryServerEnterpriseOption will return "+repositoryServerEnterpriseOption.toString());
    return repositoryServerEnterpriseOption;
  }

  const getRepositoryServerName = () => {
    console.log("RepoServerContext: getRepositoryServerName will return "+repositoryServerName);
    return repositoryServerName;
  }

  /*
   * Define the basic body parameters that are mandatory across requests to the Repository Explorer API
   */
  const baseBody = {
    serverName       : repositoryServerName,
    serverURLRoot    : repositoryServerURLRoot,
    enterpriseOption : repositoryServerEnterpriseOption
  };

  /*
   * This method wil POST to the repository server appending the supplied URI to a multi-tenant URL.
   * It should be called with the tail portion of the URI, the operation-specific body parameters and 
   * an operation-specific callback function, 
   * e.g.
   * repositoryPOST("types", null, _loadTypeInfo)      - there are no operation-specific body parms for this operation
   * repositoryPOST("types", { searchText: <String> , typeName : <String> , gen : <Integer> }, _findEntitiesByPropertyValue)
   * 
   * The caller does not need to specfiy the serverName, serverURLRoot or enterpriseOption. The other components using
   * this context have access to repositoryServerName, etc. but there is no point passing them in every time wehen they 
   * are already in the context.
   */ 
  const repositoryPOST = (uri, bodyParms, callback) => {

    const url =  identificationContext.getRestURL("rex") + "/" + uri;
    //console.log("url is "+url);

    // Add any (optional) bodyParms to the baseBody
    const body = Object.assign(baseBody, bodyParms);
  
    fetch(url, {
      method     : "POST",
      headers    : { Accept: "application/json", "Content-Type": "application/json" },
      body       : JSON.stringify(body)
    })    
    .then(res => res.json())
    .then(res => callback(res))
    
  };

  

  return (
    <RepoServerContext.Provider
      value={{
        //setServerDetails,
        repositoryServerName, 
        setRepositoryServerName,
        getRepositoryServerName,
        repositoryServerURLRoot, 
        setRepositoryServerURLRoot,
        repositoryServerEnterpriseOption, 
        setRepositoryServerEnterpriseOption,      
        getRepositoryServerEnterpriseOption,      
        repositoryPOST              
      }}
    >      
    {props.children}
    </RepoServerContext.Provider>
  );
};

RepoServerContextProvider.propTypes = {
  children: PropTypes.node  
};

export default RepoServerContextProvider;

