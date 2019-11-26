/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adminservices.configuration.properties;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * RepositoryConformanceWorkbenchConfig provides the config that drives the RepositoryWorkbench within the
 * Open Metadata Conformance Suite.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class RepositoryConformanceWorkbenchConfig extends AdminServicesConfigHeader
{
    private String   tutRepositoryServerName = null;
    private int      pageSizeForTests = 50;


    /**
     * Default constructor does nothing.
     */
    public RepositoryConformanceWorkbenchConfig()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public RepositoryConformanceWorkbenchConfig(RepositoryConformanceWorkbenchConfig template)
    {
        super(template);

        if (template != null)
        {
            tutRepositoryServerName = template.getTutRepositoryServerName();
            pageSizeForTests = template.getPageSizeForTests();
        }
    }


    /**
     * Return the name of the server that the repository workbench is to test.
     *
     * @return server name
     */
    public String getTutRepositoryServerName()
    {
        return tutRepositoryServerName;
    }


    /**
     * Set up the name of the server that the repository workbench is to test.
     *
     * @param tutRepositoryServerName server name
     */
    public void setTutRepositoryServerName(String tutRepositoryServerName)
    {
        this.tutRepositoryServerName = tutRepositoryServerName;
    }


    /**
     * Return the page size that should be used for testing the server under test.
     *
     * @return page size
     */
    public int getPageSizeForTests()
    {
        return pageSizeForTests;
    }


    /**
     * Set up the page size to use for testing the server under test.
     *
     * @param pageSizeForTests page size
     */
    public void setPageSizeForTests(int pageSizeForTests)
    {
        this.pageSizeForTests = pageSizeForTests;
    }


    /**
     * Standard toString method.
     *
     * @return JSON style description of variables.
     */
    @Override
    public String toString()
    {
        return "ConformanceSuiteConfig{" +
                "tutRepositoryServerName='" + tutRepositoryServerName + '\'' +
                "pageSizeForTests='" + pageSizeForTests + '\'' +
                '}';
    }


    /**
     * Validate that an object is equal depending on their stored values.
     *
     * @param objectToCompare object
     * @return boolean result
     */
    @Override
    public boolean equals(Object objectToCompare)
    {
        if (this == objectToCompare)
        {
            return true;
        }
        if (objectToCompare == null || getClass() != objectToCompare.getClass())
        {
            return false;
        }
        RepositoryConformanceWorkbenchConfig that = (RepositoryConformanceWorkbenchConfig) objectToCompare;
        return Objects.equals(getTutRepositoryServerName(), that.getTutRepositoryServerName())
                && Objects.equals(getPageSizeForTests(), that.getPageSizeForTests());
    }


    /**
     * Return a hash code based on the values of this object.
     *
     * @return in hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(getTutRepositoryServerName(), getPageSizeForTests());
    }
}
