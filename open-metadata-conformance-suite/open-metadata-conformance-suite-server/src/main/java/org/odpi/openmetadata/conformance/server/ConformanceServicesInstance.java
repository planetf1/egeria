/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.conformance.server;


import org.odpi.openmetadata.adminservices.configuration.properties.ConformanceSuiteConfig;
import org.odpi.openmetadata.conformance.workbenches.OpenMetadataConformanceWorkbench;
import org.odpi.openmetadata.conformance.beans.TechnologyUnderTestWorkPad;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLog;

import java.util.List;

/**
 * ConformanceServicesInstance provides the references to the active services for an instance of an OMAG Server.
 */
public class ConformanceServicesInstance
{
    private OMRSAuditLog               auditLog;                       /* Initialized in constructor */
    private ConformanceSuiteConfig     conformanceSuiteConfig;         /* Initialized in constructor */

    private TechnologyUnderTestWorkPad workPad;                        /* Initialized in constructor */
    private List<OpenMetadataConformanceWorkbench> runningWorkbenches; /* Initialized in constructor */

    /**
     * Default constructor
     */
    public ConformanceServicesInstance(TechnologyUnderTestWorkPad             workPad,
                                       List<OpenMetadataConformanceWorkbench> runningWorkbenches,
                                       OMRSAuditLog                           auditLog,
                                       ConformanceSuiteConfig                 conformanceSuiteConfig)
    {
        this.conformanceSuiteConfig = conformanceSuiteConfig;
        this.runningWorkbenches = runningWorkbenches;
        this.auditLog = auditLog;
        this.workPad = workPad;
    }


    /**
     * Return the list of workbench threads.
     *
     * @return list of workbenches
     */
    public List<OpenMetadataConformanceWorkbench> getWorkbenches()
    {
        return runningWorkbenches;
    }


    /**
     * Return the audit log for this conformance suite server.
     *
     * @return audit log object
     */
    public OMRSAuditLog getAuditLog()
    {
        return auditLog;
    }


    /**
     * Return the configuration for the conformance suite.
     *
     * @return config object.
     */
    public ConformanceSuiteConfig getConformanceSuiteConfig()
    {
        return conformanceSuiteConfig;
    }


    /**
     * Return the work pad for the compliance suite.
     *
     * @return work pad object
     */
    public TechnologyUnderTestWorkPad getWorkPad()
    {
        return workPad;
    }
}
