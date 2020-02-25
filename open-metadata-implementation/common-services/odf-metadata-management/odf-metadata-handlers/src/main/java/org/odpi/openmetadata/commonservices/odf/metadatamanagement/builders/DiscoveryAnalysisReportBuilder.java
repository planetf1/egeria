/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.odf.metadatamanagement.builders;

import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.builders.ReferenceableBuilder;
import org.odpi.openmetadata.commonservices.odf.metadatamanagement.mappers.DiscoveryAnalysisReportMapper;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.discovery.properties.DiscoveryRequestStatus;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.Date;
import java.util.Map;

/**
 * DiscoveryAnalysisReportBuilder supports the creation of the entities and
 * relationships that describe an Open Discovery Framework (ODF) DiscoveryAnalysisReport.
 */
public class DiscoveryAnalysisReportBuilder extends ReferenceableBuilder
{
    private String                 displayName;
    private String                 description;
    private Date                   creationDate           = null;
    private Map<String, String>    analysisParameters     = null;
    private DiscoveryRequestStatus discoveryRequestStatus = null;



    /**
     * Minimal constructor - used for find requests
     *
     * @param qualifiedName unique name
     * @param displayName new value for the display name.
     * @param repositoryHelper helper methods
     * @param serviceName name of this OMAS
     * @param serverName name of local server
     */
    public DiscoveryAnalysisReportBuilder(String               qualifiedName,
                                          String               displayName,
                                          OMRSRepositoryHelper repositoryHelper,
                                          String               serviceName,
                                          String               serverName)
    {
        super(qualifiedName, repositoryHelper, serviceName, serverName);

        this.displayName = displayName;
        this.description = null;
    }


    /**
     * Creation constructor - used for create/add requests
     *
     * @param qualifiedName unique name
     * @param displayName new value for the display name.
     * @param description new description for the discovery engine.
     * @param repositoryHelper helper methods
     * @param serviceName name of this OMAS
     * @param serverName name of local server
     */
    public DiscoveryAnalysisReportBuilder(String               qualifiedName,
                                          String               displayName,
                                          String               description,
                                          OMRSRepositoryHelper repositoryHelper,
                                          String               serviceName,
                                          String               serverName)
    {
        super(qualifiedName, repositoryHelper, serviceName, serverName);

        this.displayName = displayName;
        this.description = description;
    }


    /**
     * Constructor supporting all properties - used for updates.
     *
     * @param qualifiedName unique name
     * @param displayName new value for the display name.
     * @param description long description
     * @param creationDate date that the report ran
     * @param discoveryRequestStatus status of the discovery request (is it still running?)
     * @param analysisParameters list of zones that this discovery service belongs to.
     * @param additionalProperties additional properties
     * @param extendedProperties  properties from the subtype.
     * @param repositoryHelper helper methods
     * @param serviceName name of this OMAS
     * @param serverName name of local server
     */
    public DiscoveryAnalysisReportBuilder(String                 qualifiedName,
                                          String                 displayName,
                                          String                 description,
                                          Date                   creationDate,
                                          Map<String, String>    analysisParameters,
                                          DiscoveryRequestStatus discoveryRequestStatus,
                                          Map<String, String>    additionalProperties,
                                          Map<String, Object>    extendedProperties,
                                          OMRSRepositoryHelper   repositoryHelper,
                                          String                 serviceName,
                                          String                 serverName)
    {
        super(qualifiedName,
              additionalProperties,
              extendedProperties,
              repositoryHelper,
              serviceName,
              serverName);

        this.displayName = displayName;
        this.description = description;
        this.creationDate = creationDate;
        this.analysisParameters = analysisParameters;
        this.discoveryRequestStatus = discoveryRequestStatus;
    }


    /**
     * Return the supplied bean properties in an InstanceProperties object.
     *
     * @param methodName name of the calling method
     * @return InstanceProperties object
     * @throws InvalidParameterException there is a problem with the properties
     */
    public InstanceProperties getInstanceProperties(String  methodName) throws InvalidParameterException
    {
        InstanceProperties properties = super.getInstanceProperties(methodName);

        if (displayName != null)
        {
            properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                      properties,
                                                                      DiscoveryAnalysisReportMapper.DISPLAY_NAME_PROPERTY_NAME,
                                                                      displayName,
                                                                      methodName);
        }

        if (description != null)
        {
            properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                      properties,
                                                                      DiscoveryAnalysisReportMapper.DESCRIPTION_PROPERTY_NAME,
                                                                      description,
                                                                      methodName);
        }

        if (creationDate != null)
        {
            properties = repositoryHelper.addDatePropertyToInstance(serviceName,
                                                                    properties,
                                                                    DiscoveryAnalysisReportMapper.CREATION_DATE_PROPERTY_NAME,
                                                                    creationDate,
                                                                    methodName);
        }

        if (analysisParameters != null)
        {
            properties = repositoryHelper.addStringMapPropertyToInstance(serviceName,
                                                                         properties,
                                                                         DiscoveryAnalysisReportMapper.ANALYSIS_PARAMS_PROPERTY_NAME,
                                                                         analysisParameters,
                                                                         methodName);
        }

        if (discoveryRequestStatus != null)
        {
            properties = this.addDiscoveryServiceRequestStatusToProperties(properties, methodName);
        }

        return properties;
    }


    /**
     * Return the supplied bean properties that represent a name in an InstanceProperties object.
     *
     * @param methodName name of the calling method
     * @return InstanceProperties object
     */
    public InstanceProperties getNameInstanceProperties(String  methodName)
    {
        InstanceProperties properties = super.getNameInstanceProperties(methodName);

        if (displayName != null)
        {
            String literalName = repositoryHelper.getExactMatchRegex(displayName);

            properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                      properties,
                                                                      DiscoveryAnalysisReportMapper.DISPLAY_NAME_PROPERTY_NAME,
                                                                      literalName,
                                                                      methodName);
        }

        return properties;
    }


    /**
     * Add the DiscoveryServiceRequestStatus enum to the properties.
     * NOTE: DiscoveryServiceRequestStatus replaces DiscoveryRequestStatus
     *
     * @param properties current properties
     * @param methodName calling method
     * @return updated properties
     */
    private InstanceProperties addDiscoveryServiceRequestStatusToProperties(InstanceProperties properties,
                                                                            String             methodName)
    {
        InstanceProperties resultingProperties = properties;

        switch (discoveryRequestStatus)
        {
            case WAITING:
                resultingProperties = repositoryHelper.addEnumPropertyToInstance(serviceName,
                                                                                 resultingProperties,
                                                                                 DiscoveryAnalysisReportMapper.DISCOVERY_SERVICE_STATUS_PROPERTY_NAME,
                                                                                 0,
                                                                                 "Waiting",
                                                                                 "Discovery request is waiting to execute.",
                                                                                 methodName);
                break;

            case ACTIVATING:
                resultingProperties = repositoryHelper.addEnumPropertyToInstance(serviceName,
                                                                                 resultingProperties,
                                                                                 DiscoveryAnalysisReportMapper.DISCOVERY_SERVICE_STATUS_PROPERTY_NAME,
                                                                                 1,
                                                                                 "Activating",
                                                                                 "Discovery service is being initialized in the discovery engine.",
                                                                                 methodName);
                break;


            case IN_PROGRESS:
                resultingProperties = repositoryHelper.addEnumPropertyToInstance(serviceName,
                                                                                 resultingProperties,
                                                                                 DiscoveryAnalysisReportMapper.DISCOVERY_SERVICE_STATUS_PROPERTY_NAME,
                                                                                 2,
                                                                                 "InProgress",
                                                                                 "Discovery request is executing.",
                                                                                 methodName);
                break;

            case FAILED:
                resultingProperties = repositoryHelper.addEnumPropertyToInstance(serviceName,
                                                                                 resultingProperties,
                                                                                 DiscoveryAnalysisReportMapper.DISCOVERY_SERVICE_STATUS_PROPERTY_NAME,
                                                                                 3,
                                                                                 "Failed",
                                                                                 "Discovery request has failed.",
                                                                                 methodName);
                break;

            case COMPLETED:
                resultingProperties = repositoryHelper.addEnumPropertyToInstance(serviceName,
                                                                                 resultingProperties,
                                                                                 DiscoveryAnalysisReportMapper.DISCOVERY_SERVICE_STATUS_PROPERTY_NAME,
                                                                                 4,
                                                                                 "Completed",
                                                                                 "Discovery request has completed successfully.",
                                                                                 methodName);
                break;

            case OTHER:
                resultingProperties = repositoryHelper.addEnumPropertyToInstance(serviceName,
                                                                                 resultingProperties,
                                                                                 DiscoveryAnalysisReportMapper.DISCOVERY_SERVICE_STATUS_PROPERTY_NAME,
                                                                                 98,
                                                                                 "Other",
                                                                                 "Discovery service has a status that is not covered by this enum.",
                                                                                 methodName);
                break;

            case UNKNOWN_STATUS:
                resultingProperties = repositoryHelper.addEnumPropertyToInstance(serviceName,
                                                                                 resultingProperties,
                                                                                 DiscoveryAnalysisReportMapper.DISCOVERY_SERVICE_STATUS_PROPERTY_NAME,
                                                                                 99,
                                                                                 "Unknown",
                                                                                 "Discovery request status is unknown.",
                                                                                 methodName);
                break;
        }

        return resultingProperties;
    }
}
