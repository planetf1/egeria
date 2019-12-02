/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.discoveryserver.handlers;

import org.odpi.openmetadata.accessservices.discoveryengine.client.*;
import org.odpi.openmetadata.commonservices.odf.metadatamanagement.client.ODFRESTClient;
import org.odpi.openmetadata.discoveryserver.auditlog.DiscoveryServerAuditCode;
import org.odpi.openmetadata.frameworks.connectors.ConnectorBroker;
import org.odpi.openmetadata.frameworks.connectors.ffdc.*;
import org.odpi.openmetadata.frameworks.discovery.*;
import org.odpi.openmetadata.frameworks.discovery.ffdc.DiscoveryEngineException;
import org.odpi.openmetadata.frameworks.discovery.properties.*;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLog;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The DiscoveryEngineHandler is responsible for running discovery services on demand.  It is initialized
 * with the configuration for the discovery services it supports along with the clients to the
 * asset properties store and annotations store.
 */
public class DiscoveryEngineHandler
{
    private String                       serverName;               /* Initialized in constructor */
    private String                       serverUserId;             /* Initialized in constructor */
    private OMRSAuditLog                 auditLog;                 /* Initialized in constructor */
    private DiscoveryEngineClient        discoveryEngineClient;    /* Initialized in constructor */
    private int                          maxPageSize;             /* Initialized in constructor */

    private String                    discoveryEngineGUID;
    private DiscoveryEngineProperties discoveryEngineProperties;

    private Map<String, DiscoveryServiceCache>  discoveryServiceLookupTable = new HashMap<>();

    /**
     * Create a client-side object for calling a discovery engine.
     *
     * @param discoveryEngineGUID the unique identifier of the discovery engine.
     * @param serverPlatformRootURL the root url of the platform where the discovery engine is running.
     * @param serverName the name of the discovery server where the discovery engine is running
     * @param serverUserId user id for the server to use
     * @param configurationClient client to retrieve the configuration
     * @param restClient REST client for direct REST Calls
     * @param auditLog logging destination
     * @param maxPageSize maximum number of results that can be returned in a single request
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user id not allowed to access configuration
     * @throws PropertyServerException problem in configuration server
     */
    public DiscoveryEngineHandler(String                       discoveryEngineGUID,
                                  String                       serverPlatformRootURL,
                                  String                       serverName,
                                  String                       serverUserId,
                                  DiscoveryConfigurationClient configurationClient,
                                  ODFRESTClient                restClient,
                                  OMRSAuditLog                 auditLog,
                                  int                          maxPageSize) throws InvalidParameterException,
                                                                                   UserNotAuthorizedException,
                                                                                   PropertyServerException
    {
        this.discoveryEngineGUID       = discoveryEngineGUID;
        this.discoveryEngineProperties = configurationClient.getDiscoveryEngineByGUID(serverUserId, discoveryEngineGUID);

        int      startingFrom = 0;
        boolean  moreToReceive = true;

        while (moreToReceive)
        {
            List<String> registeredDiscoveryServices = configurationClient.getRegisteredDiscoveryServices(serverUserId,
                                                                                                          discoveryEngineGUID,
                                                                                                          startingFrom,
                                                                                                          maxPageSize);

            if ((registeredDiscoveryServices != null) && (! registeredDiscoveryServices.isEmpty()))
            {
                for (String registeredDiscoveryServiceGUID : registeredDiscoveryServices)
                {
                    if (registeredDiscoveryServiceGUID != null)
                    {
                        RegisteredDiscoveryService discoveryService = configurationClient.getRegisteredDiscoveryService(serverUserId,
                                                                                                                        discoveryEngineGUID,
                                                                                                                        registeredDiscoveryServiceGUID);

                        if (discoveryService != null)
                        {
                            if (discoveryService.getAssetTypes() != null)
                            {
                                for (String assetType : discoveryService.getAssetTypes())
                                {
                                    DiscoveryServiceCache discoveryServiceCache = new DiscoveryServiceCache(discoveryService);
                                    discoveryServiceLookupTable.put(assetType, discoveryServiceCache);
                                }
                            }
                        }
                    }
                }

                if (registeredDiscoveryServices.size() < maxPageSize)
                {
                    moreToReceive = false;
                }
                else
                {
                    startingFrom = startingFrom + maxPageSize;
                }
            }
            else
            {
                moreToReceive = false;
            }
        }

        this.serverName = serverName;
        this.serverUserId = serverUserId;
        this.auditLog = auditLog;
        this.maxPageSize = maxPageSize;
        this.discoveryEngineClient = new DiscoveryEngineClient(serverName, serverPlatformRootURL, restClient);

    }


    /**
     * Request the execution of a discovery service to explore a specific asset.
     *
     * @param assetGUID identifier of the asset to analyze.
     * @param assetType identifier of the type of asset to analyze - this determines which discovery service to run.
     * @param analysisParameters name value properties to control the analysis
     * @param annotationTypes list of the types of annotations to produce (and no others)
     *
     * @return unique id for the discovery request.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException there was a problem detected by the discovery engine.
     */
    public  String discoverAsset(String              assetGUID,
                                 String              assetType,
                                 Map<String, String> analysisParameters,
                                 List<String>        annotationTypes) throws InvalidParameterException,
                                                                             UserNotAuthorizedException,
                                                                             PropertyServerException
    {
        Date creationTime = new Date();
        DiscoveryServiceCache   discoveryServiceCache = discoveryServiceLookupTable.get(assetType);

        if (discoveryServiceCache != null)
        {
            DiscoveryAnalysisReport discoveryReport = discoveryEngineClient.createDiscoveryAnalysisReport(serverUserId,
                                                                                                          "DiscoveryAnalysisReport:" + assetType + ":" + assetGUID + ":" + creationTime.toString(),
                                                                                                          "Discovery Analysis Report for " + assetGUID,
                                                                                                          "This is the " + assetType + " discovery analysis report for asset " + assetGUID + " generated at " + creationTime.toString() +
                                                                                                                          " by the " + discoveryServiceCache.getDiscoveryServiceName() + " discovery service running on discovery engine " +
                                                                                                                          discoveryEngineProperties.getDisplayName() + " (" + discoveryEngineGUID + ").",
                                                                                                          creationTime,
                                                                                                          analysisParameters,
                                                                                                          DiscoveryRequestStatus.WAITING,
                                                                                                          assetGUID,
                                                                                                          discoveryEngineGUID,
                                                                                                          discoveryServiceCache.getDiscoveryServiceGUID(),
                                                                                                          null,
                                                                                                          null);
            DiscoveryAnnotationStore annotationStore = new DiscoveryAnnotationStoreClient(serverUserId,
                                                                                          assetGUID,
                                                                                          discoveryReport.getGUID(),
                                                                                          discoveryEngineClient);
            DiscoveryAssetStore assetStore = new DiscoveryAssetStoreClient(assetGUID,
                                                                           serverUserId,
                                                                           discoveryEngineClient);

            DiscoveryAssetCatalogStore assetCatalogStore = new DiscoveryAssetCatalogStoreClient(serverUserId,
                                                                                                discoveryEngineClient,
                                                                                                maxPageSize);
            DiscoveryContext discoveryContext = new DiscoveryContext(serverUserId,
                                                                     assetGUID,
                                                                     discoveryReport.getGUID(),
                                                                     analysisParameters,
                                                                     annotationTypes,
                                                                     assetStore,
                                                                     annotationStore,
                                                                     assetCatalogStore);

            DiscoveryServiceHandler discoveryServiceHandler = new DiscoveryServiceHandler(discoveryEngineProperties,
                                                                                          assetType,
                                                                                          discoveryServiceCache.getDiscoveryServiceName(),
                                                                                          discoveryServiceCache.getNextDiscoveryService(),
                                                                                          discoveryContext,
                                                                                          auditLog,
                                                                                          discoveryEngineClient,
                                                                                          serverUserId);
            Thread thread = new Thread(discoveryServiceHandler, discoveryServiceCache.getDiscoveryServiceName() + assetGUID + new Date().toString());
            thread.start();

            return discoveryReport.getGUID();
        }

        return null;
    }


    /**
     * Request the discovery report for a discovery request that has completed.
     *
     * @param discoveryRequestGUID identifier of the discovery request.
     *
     * @return discovery report
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws DiscoveryEngineException there was a problem detected by the discovery engine.
     */
    public DiscoveryAnalysisReport getDiscoveryReport(String   discoveryRequestGUID) throws InvalidParameterException,
                                                                                            UserNotAuthorizedException,
                                                                                            DiscoveryEngineException
    {
        try
        {
            return discoveryEngineClient.getDiscoveryAnalysisReport(serverUserId, discoveryRequestGUID);
        }
        catch (PropertyServerException  error)
        {
            throw new DiscoveryEngineException(error);
        }
    }


    /**
     * Return the annotations linked direction to the report.
     *
     * @param discoveryRequestGUID identifier of the discovery request.
     * @param startingFrom initial position in the stored list.
     * @param maximumResults maximum number of definitions to return on this call.
     *
     * @return list of annotations
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws DiscoveryEngineException there was a problem detected by the discovery engine.
     */
    public  List<Annotation> getDiscoveryReportAnnotations(String   discoveryRequestGUID,
                                                           int      startingFrom,
                                                           int      maximumResults) throws InvalidParameterException,
                                                                                           UserNotAuthorizedException,
                                                                                           DiscoveryEngineException
    {
        try
        {
            return discoveryEngineClient.getDiscoveryReportAnnotations(serverUserId,
                                                                       discoveryRequestGUID,
                                                                       startingFrom,
                                                                       maximumResults);
        }
        catch (PropertyServerException  error)
        {
            throw new DiscoveryEngineException(error);
        }
    }


    /**
     * Return any annotations attached to this annotation.
     *
     * @param discoveryRequestGUID identifier of the discovery request.
     * @param annotationGUID anchor annotation
     * @param startingFrom starting position in the list
     * @param maximumResults maximum number of annotations that can be returned.
     *
     * @return list of Annotation objects
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws DiscoveryEngineException there was a problem detected by the discovery engine.
     */
    public  List<Annotation>  getExtendedAnnotations(String   discoveryRequestGUID,
                                                     String   annotationGUID,
                                                     int      startingFrom,
                                                     int      maximumResults) throws InvalidParameterException,
                                                                                     UserNotAuthorizedException,
                                                                                     DiscoveryEngineException
    {
        try
        {
            return discoveryEngineClient.getExtendedAnnotations(serverUserId,
                                                                discoveryRequestGUID,
                                                                annotationGUID,
                                                                startingFrom,
                                                                maximumResults);
        }
        catch (PropertyServerException  error)
        {
            throw new DiscoveryEngineException(error);
        }
    }


    /**
     * Retrieve a single annotation by unique identifier.  This call is typically used to retrieve the latest values
     * for an annotation.
     *
     * @param discoveryRequestGUID identifier of the discovery request.
     * @param annotationGUID unique identifier of the annotation
     *
     * @return Annotation object
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws DiscoveryEngineException there was a problem detected by the discovery engine.
     */
    public  Annotation        getAnnotation(String   discoveryRequestGUID,
                                            String   annotationGUID) throws InvalidParameterException,
                                                                            UserNotAuthorizedException,
                                                                            DiscoveryEngineException
    {
        try
        {
            return discoveryEngineClient.getAnnotation(serverUserId, discoveryRequestGUID, annotationGUID);
        }
        catch (PropertyServerException  error)
        {
            throw new DiscoveryEngineException(error);
        }
    }


    /**
     * Confirms termination of the discovery engine.
     */
    public void terminate()
    {
        final String             actionDescription = "terminate";
        DiscoveryServerAuditCode auditCode;

        auditCode = DiscoveryServerAuditCode.ENGINE_SHUTDOWN;
        auditLog.logRecord(actionDescription,
                           auditCode.getLogMessageId(),
                           auditCode.getSeverity(),
                           auditCode.getFormattedLogMessage(discoveryEngineGUID, serverName),
                           null,
                           auditCode.getSystemAction(),
                           auditCode.getUserAction());
    }


    /**
     * DiscoveryServiceCache maintains the information about a registered discovery service.
     */
    private class  DiscoveryServiceCache
    {
        private DiscoveryService            nextDiscoveryService;
        private DiscoveryServiceProperties  properties;


        /**
         * Sets up the cache
         *
         * @param properties registered properties of the discovery services
         * @throws InvalidParameterException there is a problem with the connection used to create the
         * discovery service instance
         * @throws InvalidParameterException bad connection
         * @throws PropertyServerException problem with the discovery service connector
         */
        DiscoveryServiceCache(DiscoveryServiceProperties  properties) throws InvalidParameterException,
                                                                             PropertyServerException
        {
            this.properties = properties;
            getNextDiscoveryService(); /* validate that the connection works */
        }


        /**
         * Simple getter for the discovery service name - used in messages.
         *
         * @return name
         */
        String  getDiscoveryServiceName()
        {
            return properties.getQualifiedName();
        }


        /**
         * Simple getter for the discovery service's unique identifier (GUID).
         *
         * @return string guid
         */
        String getDiscoveryServiceGUID()
        {
            return properties.getGUID();
        }


        /**
         * Return a discovery service connector instance using the registered properties for the discovery service.
         *
         * @return connector
         * @throws InvalidParameterException bad connection
         * @throws PropertyServerException problem with the discovery service connector
         */
        synchronized DiscoveryService  getNextDiscoveryService() throws InvalidParameterException,
                                                                        PropertyServerException
        {
            DiscoveryService  returnValue = nextDiscoveryService;

            try
            {
                ConnectorBroker  connectorBroker = new ConnectorBroker();

                nextDiscoveryService = (DiscoveryService)connectorBroker.getConnector(properties.getConnection());
            }
            catch (ConnectionCheckedException  error)
            {
                throw new InvalidParameterException(error, properties.getQualifiedName() + "DiscoveryService Connection");
            }
            catch (ConnectorCheckedException error)
            {
                throw new PropertyServerException(error);
            }

            return returnValue;
        }
    }
}
