/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.compliance.beans;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.compliance.OpenMetadataTestCase;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * OpenMetadataTestCaseResults is a bean for storing the result of a single test.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class OpenMetadataTestCaseResult extends OpenMetadataTestCaseSummary
{
    private String              successMessage       = null;

    private List<String>        successfulAssertions   = new ArrayList<>();
    private List<String>        unsuccessfulAssertions = new ArrayList<>();

    private Map<String, Object> discoveredProperties   = null;

    private ExceptionBean       complianceException  = null;


    /**
     * Default Constructor used when converting from JSON
     */
    public OpenMetadataTestCaseResult()
    {
        super();
    }


    /**
     * Constructor used when test cases are running since the superclass's properties can be
     * extracted from the test case.
     *
     * @param testCase running test
     */
    public OpenMetadataTestCaseResult(OpenMetadataTestCase   testCase)
    {
        super(testCase);
    }


    /**
     * Return the message to confirm the successful run of the test.  This property is null if the test failed.
     *
     * @return string message
     */
    public String getSuccessMessage()
    {
        return successMessage;
    }


    /**
     * Set up the message to confirm the successful run of the test.
     *
     * @param successMessage string message
     */
    public void setSuccessMessage(String successMessage)
    {
        this.successMessage = successMessage;
    }


    /**
     * Return the list of assertions that were true when the test ran.
     *
     * @return list of assertion messages
     */
    public List<String> getSuccessfulAssertions()
    {
        return successfulAssertions;
    }


    /**
     * Set up the list of assertions that were true when the test ran.
     *
     * @param successfulAssertions list of assertion messages
     */
    public void setSuccessfulAssertions(List<String> successfulAssertions)
    {
        this.successfulAssertions = successfulAssertions;
    }


    /**
     * Return the list of assertions that were false when the test ran.
     *
     * @return list of assertion messages
     */
    public List<String> getUnsuccessfulAssertions()
    {
        return unsuccessfulAssertions;
    }


    /**
     * Set up the list of assertions that were false when the test ran.
     *
     * @param unsuccessfulAssertions list of assertion messages
     */
    public void setUnsuccessfulAssertions(List<String> unsuccessfulAssertions)
    {
        this.unsuccessfulAssertions = unsuccessfulAssertions;
    }


    /**
     * Return details of an unexpected exception that interrupted the test.
     *
     * @return exception bean
     */
    public ExceptionBean getComplianceException()
    {
        return complianceException;
    }


    /**
     * Set up details of an unexpected exception that interrupted the test.
     *
     * @param complianceException bean with exception properties
     */
    public void setComplianceException(ExceptionBean complianceException)
    {
        this.complianceException = complianceException;
    }


    /**
     * Return the properties about the repository that were discovered during the test.
     *
     * @return property map
     */
    public Map<String, Object> getDiscoveredProperties()
    {
        return discoveredProperties;
    }


    /**
     * Set up the properties about the repository that were discovered during the test.
     *
     * @param discoveredProperties property map
     */
    public void setDiscoveredProperties(Map<String, Object> discoveredProperties)
    {
        this.discoveredProperties = discoveredProperties;
    }
}
