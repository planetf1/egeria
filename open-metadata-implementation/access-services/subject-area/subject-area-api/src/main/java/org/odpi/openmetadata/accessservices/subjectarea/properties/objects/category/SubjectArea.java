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
package org.odpi.openmetadata.accessservices.subjectarea.properties.objects.category;

import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.node.NodeType;

/**
 * A type of Category called a Subject Area is one that describes a subject area or a domain.
 * For a category to me in a subject area - would be with in the children category hierarchies under a SubjectArea.
 */
public class SubjectArea extends Category{
    String subjectAreaName =null;

    public SubjectArea() {
        nodeType = NodeType.SubjectArea;
    }

    /**
     * The name of the subject area.
     * @return subject area name
     */
    public String getSubjectAreaName() {
        return subjectAreaName;
    }

    public void setSubjectAreaName(String subjectAreaName) {
        this.subjectAreaName = subjectAreaName;
    }
}
