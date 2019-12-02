/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetlineage.server;


import org.odpi.openmetadata.accessservices.assetlineage.ffdc.AssetLineageErrorCode;
import org.odpi.openmetadata.accessservices.assetlineage.handlers.*;
import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceDescription;
import org.odpi.openmetadata.commonservices.multitenant.OCFOMASServiceInstance;
import org.odpi.openmetadata.commonservices.multitenant.ffdc.exceptions.NewInstanceException;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLog;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;

import java.util.List;

/**
 * AssetLineageServicesInstance caches references to OMRS objects for a specific server.
 * It is also responsible for registering itself in the instance map.
 */
public class AssetLineageServicesInstance extends OCFOMASServiceInstance {
    private static AccessServiceDescription myDescription = AccessServiceDescription.ASSET_LINEAGE_OMAS;
    private GlossaryHandler glossaryHandler;
    private AssetContextHandler assetContextHandler;
    private CommonHandler commonHandler;
    private ProcessContextHandler processContextHandler;
    private ClassificationHandler classificationHandler;

    /**
     * Set up the handlers for this server.
     *
     * @param repositoryConnector link to the repository responsible for servicing the REST calls.
     * @param supportedZones      list of zones that AssetLineage is allowed to serve Assets from.
     * @param auditLog            destination for audit log events.
     * @throws NewInstanceException a problem occurred during initialization
     */
    public AssetLineageServicesInstance(OMRSRepositoryConnector repositoryConnector,
                                        List<String> supportedZones,
                                        OMRSAuditLog auditLog) throws NewInstanceException {
        super(myDescription.getAccessServiceName() + " OMAS",
                repositoryConnector,
                auditLog);

        final String methodName = "new ServiceInstance";

        super.supportedZones = supportedZones;

        if (repositoryHandler != null)  {
            glossaryHandler = new GlossaryHandler(serviceName,
                    serverName,
                    invalidParameterHandler,
                    repositoryHelper,
                    repositoryHandler);

            assetContextHandler = new AssetContextHandler(serviceName,
                    serverName,
                    invalidParameterHandler,
                    repositoryHelper,
                    repositoryHandler);

            commonHandler = new CommonHandler(serviceName,
                    serverName,
                    invalidParameterHandler,
                    repositoryHelper,
                    repositoryHandler);

            processContextHandler = new ProcessContextHandler(serviceName,
                    serverName,
                    invalidParameterHandler,
                    repositoryHelper,
                    repositoryHandler);

            classificationHandler = new ClassificationHandler(serviceName,
                    serverName,
                    invalidParameterHandler,
                    repositoryHelper,
                    repositoryHandler);

        }else {
            AssetLineageErrorCode errorCode = AssetLineageErrorCode.OMRS_NOT_INITIALIZED;
            String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(methodName);

            throw new NewInstanceException(errorCode.getHTTPErrorCode(),
                    this.getClass().getName(),
                    methodName,
                    errorMessage,
                    errorCode.getSystemAction(),
                    errorCode.getUserAction());
        }
    }

    /**
     * Return the specialized glossary handler for Asset Lineage OMAS.
     *
     * @return glossary handler
     */
    GlossaryHandler getGlossaryHandler()
    {
        return glossaryHandler;
    }


    /**
     * Return the specialized context handler for Asset Lineage OMAS.
     *
     * @return context handler
     */
    AssetContextHandler getAssetContextHandler()
    {
        return assetContextHandler;
    }

    /**
     * Return the specialized common handler for Asset Lineage OMAS.
     *
     * @return common handler
     */
    CommonHandler getCommonHandler()
    {
        return commonHandler;
    }

    /**
     * Return the specialized process handler for Asset Lineage OMAS.
     *
     * @return process handler
     */
    ProcessContextHandler getProcessContextHandler()
    {
        return processContextHandler;
    }

    /**
     * Return the specialized classification handler for Asset Lineage OMAS.
     *
     * @return process handler
     */
    ClassificationHandler getClassificationHandler() {
        return classificationHandler;
    }

}


