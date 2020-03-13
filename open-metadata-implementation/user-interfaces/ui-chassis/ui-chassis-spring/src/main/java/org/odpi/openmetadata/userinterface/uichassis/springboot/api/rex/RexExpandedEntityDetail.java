/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.userinterface.uichassis.springboot.api.rex;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;


@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class RexExpandedEntityDetail {

    // TODO - bring out yer dead....

    private EntityDetail    entityDetail;
    private RexEntityDigest entityDigest;
    private String          serverName;      // the name of the server that returned this object
    //private String  label;
    //private Integer gen;

    public RexExpandedEntityDetail(EntityDetail entityDetail, String label, String serverName) {
       this.entityDetail = entityDetail;
       this.serverName = serverName;
       // Server-side we do not know which gen this is for - so set to 0.
       this.entityDigest = new RexEntityDigest(entityDetail.getGUID(), label, 0, entityDetail.getMetadataCollectionName());
       //this.label = label;
       //this.gen = gen;
    }

    /*
     * Getters for Jackson
     */

    public EntityDetail getEntityDetail() { return entityDetail; }
    public String getServerName() { return serverName; }
    public RexEntityDigest getEntityDigest() { return entityDigest; }
    //public String getLabel() { return label; }
    //public Integer getGen() { return gen; }

    public void setEntityDetail(EntityDetail entityDetail) { this.entityDetail = entityDetail; }
    public void setServerName(String serverName) { this.serverName = serverName; }
    public void setEntityDigest(RexEntityDigest entityDigest) { this.entityDigest = entityDigest; }
    //public void setLabel(String label) { this.label = label; }
    //public void setGen(Integer gen) { this.gen = gen; }



    @Override
    public String toString()
    {
        return "RexExpandedEntityDetail{" +
                ", entityDetail=" + entityDetail +
                ", entityDigest=" + entityDigest +
                ", serverName=" + serverName +
                //", label=" + label +
                //", gen=" + gen +
                '}';
    }



}
