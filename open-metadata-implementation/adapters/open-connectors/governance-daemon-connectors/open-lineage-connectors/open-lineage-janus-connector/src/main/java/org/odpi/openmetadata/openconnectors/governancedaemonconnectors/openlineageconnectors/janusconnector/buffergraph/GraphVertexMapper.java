///* SPDX-License-Identifier: Apache-2.0 */
///* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.buffergraph;

import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.apache.tinkerpop.gremlin.structure.VertexProperty;
import org.odpi.openmetadata.accessservices.assetlineage.model.LineageEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.GraphConstants.*;

public class GraphVertexMapper {

    private static final Logger log = LoggerFactory.getLogger(GraphVertexMapper.class);


    public void mapEntityToVertex(LineageEntity lineageEntity, Vertex vertex){

        mapEntitySummaryToVertex(lineageEntity, vertex);

        Map<String,String> instanceProperties = lineageEntity.getProperties();
        if (instanceProperties != null) {

            for(Map.Entry<String,String> entry: instanceProperties.entrySet()){
                String key = PROPERTY_KEY_PREFIX_VERTEX_INSTANCE_PROPERTY +entry.getKey();
                vertex.property(key,entry.getValue());
            }
        }

    }



    public void mapEntitySummaryToVertex(LineageEntity lineageEntity, Vertex vertex){

        vertex.property(PROPERTY_KEY_ENTITY_GUID, lineageEntity.getGuid());
        vertex.property(PROPERTY_KEY_LABEL, lineageEntity.getTypeDefName());
        vertex.property(PROPERTY_KEY_ENTITY_VERSION, lineageEntity.getVersion());

        if (lineageEntity.getCreatedBy() != null) {
            vertex.property(PROPERTY_KEY_ENTITY_CREATED_BY, lineageEntity.getCreatedBy());
        }
        else {
            removeProperty(vertex, PROPERTY_KEY_ENTITY_CREATED_BY);
        }

        if (lineageEntity.getCreateTime() != null) {
            vertex.property(PROPERTY_KEY_ENTITY_CREATE_TIME, lineageEntity.getCreateTime());
        }
        else {
            removeProperty(vertex, PROPERTY_KEY_ENTITY_CREATE_TIME);
        }

        if (lineageEntity.getUpdatedBy() != null) {
            vertex.property(PROPERTY_KEY_ENTITY_UPDATED_BY, lineageEntity.getUpdatedBy());
        }
        else {
            removeProperty(vertex, PROPERTY_KEY_ENTITY_UPDATED_BY);
        }

        if (lineageEntity.getUpdateTime() != null) {
            vertex.property(PROPERTY_KEY_ENTITY_UPDATE_TIME, lineageEntity.getUpdateTime());
        }
        else {
            removeProperty(vertex, PROPERTY_KEY_ENTITY_UPDATE_TIME);
        }
    }

    private void removeProperty(Vertex vertex, String qualifiedPropName) {
        // no value has been specified - remove the property from the vertex
        VertexProperty vp = vertex.property(qualifiedPropName);
        if (vp != null) {
            vp.remove();
        }
    }

}
