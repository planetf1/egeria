/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.discoveryengine.builders;


import org.odpi.openmetadata.accessservices.discoveryengine.mappers.ConnectorTypeMapper;
import org.odpi.openmetadata.frameworks.discovery.ffdc.InvalidParameterException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.List;
import java.util.Map;

/**
 * ConnectorTypeBuilder is able to build the properties for an ConnectorType entity from an ConnectorType bean.
 */
public class ConnectorTypeBuilder extends ReferenceableBuilder
{
    private String       displayName;
    private String       description;
    private String       connectorProviderClassName        = null;
    private List<String> recognizedAdditionalProperties    = null;
    private List<String> recognizedSecuredProperties       = null;
    private List<String> recognizedConfigurationProperties = null;



    /**
     * Constructor when basic properties are known.
     *
     * @param qualifiedName unique name
     * @param repositoryHelper helper methods
     * @param displayName display name of discovery engine
     * @param description description of discovery engine
     * @param serviceName name of this OMAS
     * @param serverName name of local server
     */
    public ConnectorTypeBuilder(String               qualifiedName,
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
     * Constructor supporting all properties.
     *
     * @param qualifiedName unique name
     * @param displayName new value for the display name.
     * @param description new description for the discovery engine.
     * @param connectorProviderClassName class name of the connector provider.
     * @param recognizedAdditionalProperties property name for additionalProperties in a linked Connection object.
     * @param recognizedSecuredProperties property name for securedProperties in a linked Connection object.
     * @param recognizedConfigurationProperties property name for configurationProperties in a linked Connection object.
     * @param additionalProperties additional properties
     * @param extendedProperties  properties from the subtype.
     * @param repositoryHelper helper methods
     * @param serviceName name of this OMAS
     * @param serverName name of local server
     */
    public ConnectorTypeBuilder(String               qualifiedName,
                                String               displayName,
                                String               description,
                                String               connectorProviderClassName,
                                List<String>         recognizedAdditionalProperties,
                                List<String>         recognizedSecuredProperties,
                                List<String>         recognizedConfigurationProperties,
                                Map<String, String>  additionalProperties,
                                Map<String, Object>  extendedProperties,
                                OMRSRepositoryHelper repositoryHelper,
                                String               serviceName,
                                String               serverName)
    {
        super(qualifiedName,
              additionalProperties,
              extendedProperties,
              repositoryHelper,
              serviceName,
              serverName);

        this.displayName = displayName;
        this.description = description;
        this.connectorProviderClassName = connectorProviderClassName;
        this.recognizedAdditionalProperties = recognizedAdditionalProperties;
        this.recognizedSecuredProperties = recognizedSecuredProperties;
        this.recognizedConfigurationProperties = recognizedConfigurationProperties;
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
                                                                      ConnectorTypeMapper.DISPLAY_NAME_PROPERTY_NAME,
                                                                      displayName,
                                                                      methodName);
        }

        if (description != null)
        {
            properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                      properties,
                                                                      ConnectorTypeMapper.DESCRIPTION_PROPERTY_NAME,
                                                                      description,
                                                                      methodName);
        }

        if (connectorProviderClassName != null)
        {
            properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                      properties,
                                                                      ConnectorTypeMapper.CONNECTOR_PROVIDER_PROPERTY_NAME,
                                                                      connectorProviderClassName,
                                                                      methodName);
        }

        if (recognizedAdditionalProperties != null)
        {
            properties = repositoryHelper.addStringArrayPropertyToInstance(serviceName,
                                                                           properties,
                                                                           ConnectorTypeMapper.RECOGNIZED_ADD_PROPS_PROPERTY_NAME,
                                                                           recognizedAdditionalProperties,
                                                                           methodName);
        }

        if (recognizedSecuredProperties != null)
        {
            properties = repositoryHelper.addStringArrayPropertyToInstance(serviceName,
                                                                           properties,
                                                                           ConnectorTypeMapper.RECOGNIZED_SEC_PROPS_PROPERTY_NAME,
                                                                           recognizedSecuredProperties,
                                                                           methodName);
        }

        if (recognizedConfigurationProperties != null)
        {
            properties = repositoryHelper.addStringArrayPropertyToInstance(serviceName,
                                                                           properties,
                                                                           ConnectorTypeMapper.RECOGNIZED_CONFIG_PROPS_PROPERTY_NAME,
                                                                           recognizedConfigurationProperties,
                                                                           methodName);
        }


        return properties;
    }


    /**
     * Return the supplied bean properties that represent a name in an InstanceProperties object.
     *
     * @param methodName name of the calling method
     * @return InstanceProperties object
     */
    public InstanceProperties getQualifiedNameInstanceProperties(String  methodName)
    {
        return super.getNameInstanceProperties(methodName);
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
            properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                      properties,
                                                                      ConnectorTypeMapper.DISPLAY_NAME_PROPERTY_NAME,
                                                                      displayName,
                                                                      methodName);
        }

        return properties;
    }
}
