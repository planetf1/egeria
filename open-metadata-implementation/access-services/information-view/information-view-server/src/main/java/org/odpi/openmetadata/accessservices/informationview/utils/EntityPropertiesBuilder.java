/* SPDX-License-Identifier: Apache-2.0 */

package org.odpi.openmetadata.accessservices.informationview.utils;


import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;

public class EntityPropertiesBuilder {

    private InstanceProperties instanceProperties;


    /**
     * default constructor
     */
    public EntityPropertiesBuilder() {
        instanceProperties = new InstanceProperties();
    }

    /**
     * Returns the same builder
     *
     * @param key - name of the property
     * @param value - value of the property
     * @return the builder to use to return
     */
    public EntityPropertiesBuilder withStringProperty(String key, String value) {
        instanceProperties.setProperty(key, EntityPropertiesUtils.createPrimitiveStringPropertyValue(value));
        return this;
    }

    /**
     * Returns the same builder
     *
     * @param key - name of the property
     * @param value - value of the property
     * @return the builder to use to return
     */
    public EntityPropertiesBuilder withIntegerProperty(String key, Integer value) {
        instanceProperties.setProperty(key, EntityPropertiesUtils.createPrimitiveIntPropertyValue(value));
        return this;
    }

    /**
     * Returns the instance properties object
     *
     * @return properties
     */
    public InstanceProperties build() {
        return instanceProperties;
    }
}
