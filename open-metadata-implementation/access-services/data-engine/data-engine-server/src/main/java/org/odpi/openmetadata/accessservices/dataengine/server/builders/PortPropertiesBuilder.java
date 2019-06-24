/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataengine.server.builders;

import org.odpi.openmetadata.accessservices.dataengine.model.PortType;
import org.odpi.openmetadata.accessservices.dataengine.server.mappers.PortPropertiesMapper;
import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.builders.ReferenceableBuilder;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

public class PortPropertiesBuilder extends ReferenceableBuilder {

    private String displayName;
    private PortType portType;

    PortPropertiesBuilder(String qualifiedName, OMRSRepositoryHelper repositoryHelper, String serviceName,
                          String serverName) {
        super(qualifiedName, repositoryHelper, serviceName, serverName);
    }

    public PortPropertiesBuilder(String qualifiedName, String displayName, PortType portType,
                                 OMRSRepositoryHelper repositoryHelper, String serviceName, String serverName) {
        super(qualifiedName, repositoryHelper, serviceName, serverName);
        this.displayName = displayName;
        this.portType = portType;
    }

    public InstanceProperties getInstanceProperties(String methodName) throws InvalidParameterException {
        InstanceProperties properties = super.getInstanceProperties(methodName);

        if (displayName != null) {
            properties = repositoryHelper.addStringPropertyToInstance(serviceName, properties,
                    PortPropertiesMapper.DISPLAY_NAME_PROPERTY_NAME, displayName, methodName);
        }

        if (portType != null) {
            properties = this.addPortTypeProperty(portType, properties);
        }

        return properties;
    }

    private InstanceProperties addPortTypeProperty(PortType portType, InstanceProperties properties) {
        return repositoryHelper.addEnumPropertyToInstance(serviceName, properties, PortPropertiesMapper.PORT_TYPE,
                portType.getOrdinal(), portType.getName(), portType.getDescription(), "addPortTypeProperty");
    }
}
