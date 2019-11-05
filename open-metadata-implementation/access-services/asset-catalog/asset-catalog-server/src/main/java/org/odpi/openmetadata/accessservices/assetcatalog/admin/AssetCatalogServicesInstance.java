/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetcatalog.admin;

import org.odpi.openmetadata.accessservices.assetcatalog.exception.AssetCatalogErrorCode;
import org.odpi.openmetadata.accessservices.assetcatalog.handlers.AssetCatalogHandler;
import org.odpi.openmetadata.accessservices.assetcatalog.handlers.RelationshipHandler;
import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceDescription;
import org.odpi.openmetadata.commonservices.multitenant.OCFOMASServiceInstance;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLog;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;

import java.util.List;

/**
 * AssetCatalogServicesInstance caches references to OMRS objects for a specific server.
 * It is also responsible for registering itself in the instance map.
 */
class AssetCatalogServicesInstance extends OCFOMASServiceInstance {

    private static final AccessServiceDescription description = AccessServiceDescription.ASSET_CATALOG_OMAS;

    private AssetCatalogHandler assetCatalogHandler;
    private RelationshipHandler relationshipHandler;

    /**
     * Set up the local repository connector that will service the REST Calls.
     *
     * @param repositoryConnector link to the repository responsible for servicing the REST calls.
     * @throws org.odpi.openmetadata.commonservices.multitenant.ffdc.exceptions.NewInstanceException a problem occurred during initialization
     */
    AssetCatalogServicesInstance(OMRSRepositoryConnector repositoryConnector,
                                 List<String> supportedZones, OMRSAuditLog auditLog,
                                 String localServerUserId) throws org.odpi.openmetadata.commonservices.multitenant.ffdc.exceptions.NewInstanceException {
        super(description.getAccessServiceName(), repositoryConnector, auditLog, localServerUserId, repositoryConnector.getMaxPageSize());
        super.supportedZones = supportedZones;

        if (repositoryHandler != null) {

            assetCatalogHandler = new AssetCatalogHandler(serverName, invalidParameterHandler, repositoryHandler, repositoryHelper, errorHandler);
            relationshipHandler = new RelationshipHandler(invalidParameterHandler, repositoryHandler, repositoryHelper);

        } else {
            final String methodName = "new ServiceInstance";

            AssetCatalogErrorCode errorCode = AssetCatalogErrorCode.OMRS_NOT_INITIALIZED;
            String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(methodName);

            throw new org.odpi.openmetadata.commonservices.multitenant.ffdc.exceptions.NewInstanceException(errorCode.getHttpErrorCode(), this.getClass().getName(), methodName,
                    errorMessage, errorCode.getSystemAction(), errorCode.getUserAction());
        }
    }


    /**
     * Return the handler for assets requests
     *
     * @return handler object
     */
    AssetCatalogHandler getAssetCatalogHandler() {
        return assetCatalogHandler;
    }


    /**
     * Return the handler for relationships requests
     *
     * @return handler object
     */
    RelationshipHandler getRelationshipHandler() {
        return relationshipHandler;
    }

}
