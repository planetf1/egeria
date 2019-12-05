/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetlineage.event;

import com.fasterxml.jackson.annotation.*;

import java.io.Serializable;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * AssetLineageEventHeader provides a common base for all events from the Data Engine access service.
 * It implements Serializable and a version Id.
 */
@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "class")
@JsonSubTypes({
        @JsonSubTypes.Type(value = LineageEvent.class, name = "LineageEvent")
})
public abstract class AssetLineageEventHeader implements Serializable {

    private long eventVersionId = 1L;

    private AssetLineageEventType assetLineageEventType;

    public long getEventVersionId() {
        return eventVersionId;
    }

    public void setEventVersionId(long eventVersionId) {
        this.eventVersionId = eventVersionId;
    }

    public AssetLineageEventType getAssetLineageEventType() {
        return assetLineageEventType;
    }

    public void setAssetLineageEventType(AssetLineageEventType assetLineageEventType) {
        this.assetLineageEventType = assetLineageEventType;
    }

    @Override
    public String toString() {
        return "AssetLineageEventHeader{" +
                "eventVersionId=" + eventVersionId +
                '}';
    }
}
