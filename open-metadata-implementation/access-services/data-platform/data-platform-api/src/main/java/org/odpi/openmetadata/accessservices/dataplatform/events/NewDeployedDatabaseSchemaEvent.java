/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataplatform.events;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.odpi.openmetadata.accessservices.dataplatform.properties.connection.DataPlatform;
import org.odpi.openmetadata.accessservices.dataplatform.properties.asset.DeployedDatabaseSchema;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;


@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "class")
public class NewDeployedDatabaseSchemaEvent extends DataPlatformEventHeader {

    private DeployedDatabaseSchema deployedDatabaseSchema;
    private DataPlatform dataPlatform;

    public DeployedDatabaseSchema getDeployedDatabaseSchema() {
        return deployedDatabaseSchema;
    }

    public void setDeployedDatabaseSchema(DeployedDatabaseSchema deployedDatabaseSchema) {
        this.deployedDatabaseSchema = deployedDatabaseSchema;
    }

    public DataPlatform getDataPlatform() {
        return dataPlatform;
    }

    public void setDataPlatform(DataPlatform dataPlatform) {
        this.dataPlatform = dataPlatform;
    }

    @Override
    public String toString() {
        return "NewDeployedDatabaseSchemaEvent{" +
                "deployedDatabaseSchema=" + deployedDatabaseSchema +
                ", dataPlatform=" + dataPlatform +
                "} " + super.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NewDeployedDatabaseSchemaEvent that = (NewDeployedDatabaseSchemaEvent) o;
        return Objects.equals(deployedDatabaseSchema, that.deployedDatabaseSchema) &&
                Objects.equals(dataPlatform, that.dataPlatform);
    }

    @Override
    public int hashCode() {
        return Objects.hash(deployedDatabaseSchema, dataPlatform);
    }
}
