/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataplatform.properties.schema;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.accessservices.dataplatform.properties.Source;

import java.util.List;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * The type TabularSchema.
 */
@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class TabularSchema extends Source {

    private String name;
    private String disaplayName;
    private List<TabularColumn> tabularColumns;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDisaplayName() {
        return disaplayName;
    }

    public void setDisaplayName(String disaplayName) {
        this.disaplayName = disaplayName;
    }

    public List<TabularColumn> getTabularColumns() {
        return tabularColumns;
    }

    public void setTabularColumns(List<TabularColumn> tabularColumns) {
        this.tabularColumns = tabularColumns;
    }

    @Override
    public String toString() {
        return "TabularSchema{" +
                "name='" + name + '\'' +
                ", disaplayName='" + disaplayName + '\'' +
                ", tabularColumns=" + tabularColumns +
                ", additionalProperties=" + additionalProperties +
                ", qualifiedName='" + qualifiedName + '\'' +
                ", guid='" + guid + '\'' +
                "} " + super.toString();
    }
}
