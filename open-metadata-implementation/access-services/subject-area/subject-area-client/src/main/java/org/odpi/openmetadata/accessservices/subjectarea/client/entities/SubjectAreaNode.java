/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.client.entities;

import org.odpi.openmetadata.accessservices.subjectarea.client.SubjectAreaEntityClient;
import org.odpi.openmetadata.accessservices.subjectarea.client.SubjectAreaRestClient;
import org.odpi.openmetadata.accessservices.subjectarea.client.entities.categories.SubjectAreaCategory;
import org.odpi.openmetadata.accessservices.subjectarea.client.entities.glossaries.SubjectAreaGlossary;
import org.odpi.openmetadata.accessservices.subjectarea.client.entities.projects.SubjectAreaProject;
import org.odpi.openmetadata.accessservices.subjectarea.client.entities.terms.SubjectAreaTerm;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.category.Category;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.category.SubjectAreaDefinition;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.glossary.Glossary;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.graph.Line;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.graph.Node;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.project.Project;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.term.Term;
import org.reflections.Reflections;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * The OMAS client library implementation of the Subject Area OMAS.
 * This interface provides entities {@link Node} authoring interface for subject area experts.
 * A standard set of customers is described in {@link SubjectAreaCategory}, {@link SubjectAreaTerm},
 * {@link SubjectAreaProject}, {@link SubjectAreaGlossary}
 */
public class SubjectAreaNode implements SubjectAreaCategory, SubjectAreaTerm, SubjectAreaProject, SubjectAreaGlossary {
    private Map<Class<?>, SubjectAreaEntityClient<?>> cache = new HashMap<>();
    private static final String DEFAULT_SCAN_PACKAGE = SubjectAreaNode.class.getPackage().getName();

    /**
     * @param packagesToScan - search packages for finding classes placed by annotation {@link SubjectAreaNodeClient}
     * @param subjectAreaRestClient - rest client for Subject Area OMAS REST APIs
     * */
    @SuppressWarnings("rawtypes")
    public SubjectAreaNode(SubjectAreaRestClient subjectAreaRestClient, String... packagesToScan) {
        Set<String> packages = new HashSet<>(Arrays.asList(packagesToScan));
        packages.add(DEFAULT_SCAN_PACKAGE);

        Reflections reflections = new Reflections(packages);
        Set<Class<?>> clientClasses = reflections.getTypesAnnotatedWith(SubjectAreaNodeClient.class);
        for (Class<?> declaredClass : clientClasses) {
            try {
                if (AbstractSubjectAreaEntity.class.isAssignableFrom(declaredClass)) {
                    Constructor<?> ctor = declaredClass.getDeclaredConstructor(SubjectAreaRestClient.class);
                    ctor.setAccessible(true);
                    final AbstractSubjectAreaEntity newInstance =
                            (AbstractSubjectAreaEntity) ctor.newInstance(subjectAreaRestClient);
                    cache.put(newInstance.type(), newInstance);
                }
            } catch (NoSuchMethodException
                    | IllegalAccessException
                    | InstantiationException
                    | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     *  The constructor uses the current package to scan "org.odpi.openmetadata.accessservices.subjectarea.client.entities"
     *  to search for classes placed by annotation {@link SubjectAreaNodeClient}.
     *
     * @param subjectAreaRestClient - rest client for Subject Area OMAS REST APIs
     */
    public SubjectAreaNode(SubjectAreaRestClient subjectAreaRestClient) {
        this(subjectAreaRestClient, DEFAULT_SCAN_PACKAGE);
    }

    @Override
    public SubjectAreaEntityClient<Category> category() {
        return getClient(Category.class);
    }

    @Override
    public SubjectAreaEntityClient<SubjectAreaDefinition> subjectAreaDefinition() {
        return getClient(SubjectAreaDefinition.class);
    }

    @Override
    public SubjectAreaEntityClient<Glossary> glossary() {
        return getClient(Glossary.class);
    }

    @Override
    public SubjectAreaEntityClient<Project> project() {
        return getClient(Project.class);
    }

    @Override
    public SubjectAreaEntityClient<Term> term() {
        return getClient(Term.class);
    }

    /**
     * @param <T> - {@link Line} type of object
     * @param clazz - the class for which you want to get the client from cache
     *
     * @return SubjectAreaEntityClient or null if this client is not present
     * */
    @SuppressWarnings("unchecked")
    public <T extends Node> SubjectAreaEntityClient<T> getClient(Class<T> clazz) {
        return (SubjectAreaEntityClient<T>) cache.get(clazz);
    }
}
