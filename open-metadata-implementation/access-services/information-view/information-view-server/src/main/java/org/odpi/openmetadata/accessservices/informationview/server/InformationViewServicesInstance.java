/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.informationview.server;


import org.odpi.openmetadata.accessservices.informationview.context.ContextBuilders;
import org.odpi.openmetadata.accessservices.informationview.registration.RegistrationHandler;
import org.odpi.openmetadata.accessservices.informationview.reports.DataViewHandler;
import org.odpi.openmetadata.accessservices.informationview.reports.ReportHandler;


/**
 * InformationViewServicesInstance caches references to OMRS objects for a specific server.
 * It is also responsible for registering itself in the instance map.
 */
public class InformationViewServicesInstance
{
    private ReportHandler reportHandler;
    private DataViewHandler dataViewHandler;
    private ContextBuilders contextBuilders;
    private RegistrationHandler registrationHandler;
    private String                 serverName;


    /**
     * Set up the local repository connector that will service the REST Calls.
     *
     * @param reportHandler link to the repository responsible for servicing the REST calls.
     */
    public InformationViewServicesInstance(ReportHandler reportHandler, DataViewHandler dataViewHandler, RegistrationHandler registrationHandler, ContextBuilders contextBuilders, String serverName) {
        this.reportHandler = reportHandler;
        this.dataViewHandler = dataViewHandler;
        this.registrationHandler = registrationHandler;
        this.contextBuilders = contextBuilders;
        this.serverName    = serverName;

        InformationViewServicesInstanceMap.setNewInstanceForJVM(serverName, this);
    }


    /**
     * Return the report creator for this server.
     *
     * @return ReportCreator object
     */
    public ReportHandler getReportHandler()  {
        return this.reportHandler;
    }


    public DataViewHandler getDataViewHandler(){
            return this.dataViewHandler;
    }


    public RegistrationHandler getRegistrationHandler(){return registrationHandler;}

    public ContextBuilders getContextBuilders() {
        return contextBuilders;
    }

    /**
     * Unregister this instance from the instance map.
     */
    public void shutdown() {
        InformationViewServicesInstanceMap.removeInstanceForJVM(serverName);
    }
}
