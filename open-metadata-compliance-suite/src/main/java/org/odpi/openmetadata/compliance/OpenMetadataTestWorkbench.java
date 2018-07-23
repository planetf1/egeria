/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.compliance;

import org.odpi.openmetadata.compliance.beans.*;


/**
 * The OpenMetadataTestWorkbench drives the execution of a batch of tests.
 */
public abstract class OpenMetadataTestWorkbench
{
    private   String                     workbenchName;             /* setup by the subclass */
    private   String                     versionNumber;             /* setup by the subclass */
    private   String                     workbenchDocumentationURL; /* setup by the subclass */
    protected String                     serverURLRoot;             /* provided by caller */


    /**
     * Constructor taking in the URL root of the server under test.
     *
     * @param workbenchName name of this workbench
     * @param versionNumber version umber of workbench
     * @param workbenchDocumentationURL url to the documentation for this workbench
     * @param serverURLRoot string
     */
    public OpenMetadataTestWorkbench(String                     workbenchName,
                                     String                     versionNumber,
                                     String                     workbenchDocumentationURL,
                                     String                     serverURLRoot)
    {
        this.workbenchName = workbenchName;
        this.versionNumber = versionNumber;
        this.workbenchDocumentationURL = workbenchDocumentationURL;
        this.serverURLRoot = serverURLRoot;
    }


    /**
     * Return the workbench name.
     *
     * @return name
     */
    public String getWorkbenchName()
    {
        return workbenchName;
    }


    /**
     * Return the string version number of the workbench.
     *
     * @return version number
     */
    public String getVersionNumber()
    {
        return versionNumber;
    }


    /**
     * Return the link to the documentation for this workbench
     *
     * @return string url
     */
    public String getWorkbenchDocumentationURL()
    {
        return workbenchDocumentationURL;
    }


    /**
     * Method that runs the tests - it is implemented by the subclass.
     *
     * @return test results
     */
    public abstract OpenMetadataTestWorkbenchResults  runTests();
}
