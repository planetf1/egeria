/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.userinterface.adminservices.configuration.registration;

import java.io.Serializable;

/**
 * ViewServiceRegistration is used by a ui view to register its admin services interface
 */
public class ViewServiceRegistration implements Serializable
{
    private static final long     serialVersionUID    = 1L;

    private int                            viewServiceCode;
    private String                         viewServiceName;
    private String                         viewServiceURLMarker;
    private String                         viewServiceDescription;
    private String                         viewServiceWiki;
    private ViewServiceOperationalStatus viewServiceOperationalStatus;
    private String                         viewServiceAdminClassName;

    /**
     * Complete Constructor
     *
     * @param viewServiceCode ordinal for this view service
     * @param viewServiceName symbolic name for this view service
     * @param viewServiceDescription short description for this view service
     * @param viewServiceWiki wiki page for the view service for this view service
     * @param viewServiceOperationalStatus default initial operational status for the view service
     * @param viewServiceAdminClassName  name of ViewServiceAdmin implementation class for the view service
     */
    public ViewServiceRegistration(int                            viewServiceCode,
                                   String                         viewServiceName,
                                   String                         viewServiceURLMarker,
                                   String                         viewServiceDescription,
                                   String                         viewServiceWiki,
                                   ViewServiceOperationalStatus   viewServiceOperationalStatus,
                                   String                         viewServiceAdminClassName)
    {
        this.viewServiceCode = viewServiceCode;
        this.viewServiceName = viewServiceName;
        this.viewServiceURLMarker = viewServiceURLMarker;
        this.viewServiceDescription = viewServiceDescription;
        this.viewServiceWiki = viewServiceWiki;
        this.viewServiceOperationalStatus = viewServiceOperationalStatus;
        this.viewServiceAdminClassName = viewServiceAdminClassName;
    }


    /**
     * Enum Constructor
     *
     * @param ViewServiceDescription enum for this view service
     * @param ViewServiceOperationalStatus default initial operational status for the view service
     * @param viewServiceAdminClassName  name of ViewServiceAdmin implementation class for the view service
     */
    public ViewServiceRegistration(ViewServiceDescription ViewServiceDescription,
                                   ViewServiceOperationalStatus ViewServiceOperationalStatus,
                                   String                         viewServiceAdminClassName)
    {
        this(ViewServiceDescription.getViewServiceCode(),
             ViewServiceDescription.getViewServiceName(),
             ViewServiceDescription.getViewServiceURLMarker(),
             ViewServiceDescription.getViewServiceDescription(),
             ViewServiceDescription.getViewServiceWiki(), 
             ViewServiceOperationalStatus,
             viewServiceAdminClassName);
    }


    /**
     * Default constructor
     */
    public ViewServiceRegistration()
    {
    }

    /**
     * Return the code for this view service
     *
     * @return int type code
     */
    public int getViewServiceCode()
    {
        return viewServiceCode;
    }


    /**
     * Set up the code for this view service
     *
     * @param viewServiceCode  int type code
     */
    public void setViewServiceCode(int viewServiceCode)
    {
        this.viewServiceCode = viewServiceCode;
    }


    /**
     * Return the default name for this view service.
     *
     * @return String default name
     */
    public String getViewServiceName()
    {
        return viewServiceName;
    }



    /**
     * Set up the default name for this view service.
     *
     * @param viewServiceName  String default name
     */
    public void setViewServiceName(String viewServiceName)
    {
        this.viewServiceName = viewServiceName;
    }


    /**
     * Return the string that appears in the REST API URL that identifies the owning service.
     * Null means no REST APIs supported by this service.
     *
     * @return String default name
     */
    public String getViewServiceURLMarker()
    {
        return viewServiceURLMarker;
    }


    /**
     * Set up the string that appears in the REST API URL that identifies the owning service.
     * Null means no REST APIs supported by this service.
     *
     * @param viewServiceURLMarker url fragment
     */
    public void setServiceURLMarker(String viewServiceURLMarker)
    {
        this.viewServiceURLMarker = viewServiceURLMarker;
    }


    /**
     * Return the default description for the type for this view service.
     *
     * @return String default description
     */
    public String getViewServiceDescription()
    {
        return viewServiceDescription;
    }


    /**
     * Set up the default description for the type for this view service.
     *
     * @param viewServiceDescription  String default description
     */
    public void setViewServiceDescription(String viewServiceDescription)
    {
        this.viewServiceDescription = viewServiceDescription;
    }


    /**
     * Return the URL for the wiki page describing this view service.
     *
     * @return String URL name for the wiki page
     */
    public String getViewServiceWiki()
    {
        return viewServiceWiki;
    }


    /**
     * Set up the URL for the wiki page describing this view service.
     *
     * @param viewServiceWiki  String URL name for the wiki page
     */
    public void setViewServiceWiki(String viewServiceWiki)
    {
        this.viewServiceWiki = viewServiceWiki;
    }


    /**
     * Return the initial operational status for this view service.
     *
     * @return viewServiceOperationalStatus enum
     */
    public ViewServiceOperationalStatus getViewServiceOperationalStatus()
    {
        return viewServiceOperationalStatus;
    }


    /**
     * Set up the initial operational status for this view service.
     *
     * @param viewServiceOperationalStatus viewServiceOperationalStatus enum
     */
    public void setViewServiceOperationalStatus(ViewServiceOperationalStatus viewServiceOperationalStatus)
    {
        this.viewServiceOperationalStatus = viewServiceOperationalStatus;
    }

    /**
     * Return the class name of the admin class that should be called during initialization and
     * termination.
     *
     * @return class name
     */
    public String getViewServiceAdminClassName()
    {
        return viewServiceAdminClassName;
    }


    /**
     * Set up the class name of the admin class that should be called during initialization and
     * termination.
     *
     * @param viewServiceAdminClassName  class name
     */
    public void setViewServiceAdminClassName(String viewServiceAdminClassName)
    {
        this.viewServiceAdminClassName = viewServiceAdminClassName;
    }

}
