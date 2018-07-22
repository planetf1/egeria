/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.odpi.openmetadata.accessservices.subjectarea.properties.objects.glossary;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.accessservices.subjectarea.properties.classifications.CanonicalVocabulary;
import org.odpi.openmetadata.accessservices.subjectarea.properties.classifications.Classification;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.common.GovernanceActions;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.governednode.GovernedNode;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.node.Node;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.node.NodeType;


import java.util.List;
import java.util.Set;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 *
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class Glossary extends GovernedNode{
    public Glossary() {
        nodeType = NodeType.Glossary;
    }
    String usage =null;
    String language =null;

    /**
     * Guidance on the usage of this glossary content.
     * @return the usage.
     */
    public String getUsage() {
        return usage;
    }

    public void setUsage(String usage) {
        this.usage = usage;
    }

    /**
     * The Natural Language used in the glossary.
     *
     * This is a String - there is a Jira raised to enhance Egeria to standardise the language values.
     * https://jira.odpi.org/browse/EGERIA-9
     * @return the natural language
     */
    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    /**
     * Consumable name for the glossary, suitable for reports and user interfaces.
     * @return the glossary name
     */
    @Override
    public String getName() {
        return super.getName();
    }
    @Override
    public void setName(String name) {
        super.setName(name);
    }

    @Override
    public NodeType getNodeType() {
        return super.getNodeType();
    }

    @Override
    public void setNodeType(NodeType nodeType) {
        super.setNodeType(nodeType);
    }

    /**
     * Description of the glossary
     * @return the glossary description.
     */
    @Override
    public String getDescription() {
        return super.getDescription();
    }

    /**
     * Unique identifying name for the glossary instance.
     * @return qualifiedName
     */
    @Override
    public String getQualifiedName() {
        return super.getQualifiedName();
    }
    @Override
    /**
     * The projects associated with this glossary.
     * @return
     */
    public Set<String> getProjects() {
        return super.getProjects();
    }
    /**
     * The Governance level associated with this glossary
     */

    /**
     * The classifications associated with this glossary
     */
    @Override
    public List<Classification> getClassifications() {
        return super.getClassifications();
    }
    /**
     * Retention
     * @return retention
     */
    @Override
    public GovernanceActions getGovernanceActions() {
        return super.getGovernanceActions();
    }
    @Override
    /**
     * The icon associated with this glossary.
     * @return the url of the icon.
     */
    public String getIcon() {
        return super.getIcon();
    }

    @Override
    public void processClassification (Classification classification) {
        if (classification.getClassificationName().equals("Taxonomy")) {
            if (nodeType == NodeType.CanonicalGlossary || nodeType == NodeType.TaxonomyAndCanonicalGlossary) {
                super.setNodeType(NodeType.TaxonomyAndCanonicalGlossary);
            } else {
                super.setNodeType(NodeType.Taxonomy);
            }
        } else if (classification.getClassificationName().equals(new CanonicalVocabulary().getClassificationName())) {
            if (nodeType == NodeType.Taxonomy || nodeType == NodeType.TaxonomyAndCanonicalGlossary) {
                super.setNodeType(NodeType.TaxonomyAndCanonicalGlossary);
            } else {
                super.setNodeType(NodeType.CanonicalGlossary);
            }
        }
    }

    @Override
    public StringBuilder toString(StringBuilder sb) {
        if (sb == null) {
            sb = new StringBuilder();
        }
        sb.append("Glossary=");
        sb.append(super.toString(sb));

        return sb;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Glossary term = (Glossary) o;
        Node node = (Node) o;
        if (!(node.equals((Node)o))) return false;
        return  true;
    }

    @Override
    public int hashCode() {
        return ((Node)this).hashCode();
    }
}
