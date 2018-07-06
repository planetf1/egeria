/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.frameworks.connectors.properties.beans;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.testng.annotations.Test;

import java.util.*;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

/**
 * Validate that the Annotation bean can be cloned, compared, serialized, deserialized and printed as a String.
 */
public class TestAnnotation
{
    private ElementType          type            = new ElementType();
    private Date                 creationDate    = new Date(27);
    private Date                 reviewDate      = new Date(1234);
    private List<Classification> classifications = new ArrayList<>();
    private Map<String, Object>  analysisParameters = new HashMap<>();
    private Map<String, Object>  additionalProperties = new HashMap<>();


    /**
     * Default constructor
     */
    public TestAnnotation()
    {
    }


    /**
     * Set up an example object to test.
     *
     * @return filled in object
     */
    private Annotation getTestObject()
    {
        Annotation testObject = new Annotation();

        testObject.setType(type);
        testObject.setGUID("TestGUID");
        testObject.setURL("TestURL");
        testObject.setClassifications(classifications);
        testObject.setReportName("TestReportName");
        testObject.setReportDescription("TestReportDescription");
        testObject.setCreationDate(creationDate);
        testObject.setAnalysisParameters(analysisParameters);
        testObject.setAnnotationType("TestAnnotationType");
        testObject.setSummary("TestSummary");
        testObject.setConfidenceLevel(5);
        testObject.setExpression("TestExpression");
        testObject.setExplanation("TestExplanation");
        testObject.setAnalysisStep("TestAnalysisStep");
        testObject.setJsonProperties("TestJsonProperties");
        testObject.setAnnotationStatus(AnnotationStatus.UNKNOWN_STATUS);
        testObject.setReviewDate(reviewDate);
        testObject.setSteward("TestSteward");
        testObject.setReviewComment("TestReviewComment");
        testObject.setAdditionalProperties(additionalProperties);

        return testObject;
    }


    /**
     * Validate that the object that comes out of the test has the same content as the original test object.
     *
     * @param resultObject object returned by the test
     */
    private void validateResultObject(Annotation  resultObject)
    {
        assertTrue(resultObject.getType().equals(type));
        assertTrue(resultObject.getGUID().equals("TestGUID"));
        assertTrue(resultObject.getURL().equals("TestURL"));
        assertTrue(resultObject.getClassifications() == null);

        assertTrue(resultObject.getReportName().equals("TestReportName"));
        assertTrue(resultObject.getReportDescription().equals("TestReportDescription"));
        assertTrue(resultObject.getCreationDate().equals(creationDate));
        assertTrue(resultObject.getAnalysisParameters().equals(analysisParameters));
        assertTrue(resultObject.getAnnotationType().equals("TestAnnotationType"));
        assertTrue(resultObject.getSummary().equals("TestSummary"));
        assertTrue(resultObject.getConfidenceLevel() == 5);
        assertTrue(resultObject.getExpression().equals("TestExpression"));
        assertTrue(resultObject.getExplanation().equals("TestExplanation"));
        assertTrue(resultObject.getAnalysisStep().equals("TestAnalysisStep"));
        assertTrue(resultObject.getJsonProperties().equals("TestJsonProperties"));
        assertTrue(resultObject.getAnnotationStatus().equals(AnnotationStatus.UNKNOWN_STATUS));
        assertTrue(resultObject.getReviewDate().equals(reviewDate));
        assertTrue(resultObject.getSteward().equals("TestSteward"));
        assertTrue(resultObject.getReviewComment().equals("TestReviewComment"));
        assertTrue(resultObject.getAdditionalProperties().equals(additionalProperties));
    }


    /**
     * Validate that the object is initialized properly
     */
    @Test public void testNullObject()
    {
        Annotation    nullObject = new Annotation();

        assertTrue(nullObject.getType() == null);
        assertTrue(nullObject.getGUID() == null);
        assertTrue(nullObject.getURL() == null);
        assertTrue(nullObject.getClassifications() == null);

        assertTrue(nullObject.getReportName() == null);
        assertTrue(nullObject.getReportDescription() == null);
        assertTrue(nullObject.getCreationDate() == null);
        assertTrue(nullObject.getAnalysisParameters() == null);
        assertTrue(nullObject.getAnnotationType() == null);
        assertTrue(nullObject.getSummary() == null);
        assertTrue(nullObject.getConfidenceLevel() == 0);
        assertTrue(nullObject.getExpression() == null);
        assertTrue(nullObject.getExplanation() == null);
        assertTrue(nullObject.getAnalysisStep() == null);
        assertTrue(nullObject.getJsonProperties() == null);
        assertTrue(nullObject.getAnnotationStatus() == null);
        assertTrue(nullObject.getReviewDate() == null);
        assertTrue(nullObject.getSteward() == null);
        assertTrue(nullObject.getReviewComment() == null);
        assertTrue(nullObject.getAdditionalProperties() == null);

        nullObject = new Annotation(null);

        assertTrue(nullObject.getType() == null);
        assertTrue(nullObject.getGUID() == null);
        assertTrue(nullObject.getURL() == null);
        assertTrue(nullObject.getClassifications() == null);

        assertTrue(nullObject.getReportName() == null);
        assertTrue(nullObject.getReportDescription() == null);
        assertTrue(nullObject.getCreationDate() == null);
        assertTrue(nullObject.getAnalysisParameters() == null);
        assertTrue(nullObject.getAnnotationType() == null);
        assertTrue(nullObject.getSummary() == null);
        assertTrue(nullObject.getConfidenceLevel() == 0);
        assertTrue(nullObject.getExpression() == null);
        assertTrue(nullObject.getExplanation() == null);
        assertTrue(nullObject.getAnalysisStep() == null);
        assertTrue(nullObject.getJsonProperties() == null);
        assertTrue(nullObject.getAnnotationStatus() == null);
        assertTrue(nullObject.getReviewDate() == null);
        assertTrue(nullObject.getSteward() == null);
        assertTrue(nullObject.getReviewComment() == null);
        assertTrue(nullObject.getAdditionalProperties() == null);
    }


    /**
     * Validate that 2 different objects with the same content are evaluated as equal.
     * Also that different objects are considered not equal.
     */
    @Test public void testEquals()
    {
        assertFalse(getTestObject().equals(null));
        assertFalse(getTestObject().equals("DummyString"));
        assertTrue(getTestObject().equals(getTestObject()));

        Annotation  sameObject = getTestObject();
        assertTrue(sameObject.equals(sameObject));

        Annotation  differentObject = getTestObject();
        differentObject.setGUID("Different");
        assertFalse(getTestObject().equals(differentObject));
    }


    /**
     *  Validate that 2 different objects with the same content have the same hash code.
     */
    @Test public void testHashCode()
    {
        assertTrue(getTestObject().hashCode() == getTestObject().hashCode());
    }


    /**
     *  Validate that an object cloned from another object has the same content as the original
     */
    @Test public void testClone()
    {
        validateResultObject(new Annotation(getTestObject()));
    }


    /**
     * Validate that an object generated from a JSON String has the same content as the object used to
     * create the JSON String.
     */
    @Test public void testJSON()
    {
        ObjectMapper objectMapper = new ObjectMapper();
        String       jsonString   = null;

        /*
         * This class
         */
        try
        {
            jsonString = objectMapper.writeValueAsString(getTestObject());
        }
        catch (Throwable  exc)
        {
            assertTrue(false, "Exception: " + exc.getMessage());
        }

        try
        {
            validateResultObject(objectMapper.readValue(jsonString, Annotation.class));
        }
        catch (Throwable  exc)
        {
            assertTrue(false, "Exception: " + exc.getMessage());
        }

        /*
         * Through superclass
         */
        ElementHeader  elementHeader = getTestObject();

        try
        {
            jsonString = objectMapper.writeValueAsString(elementHeader);
        }
        catch (Throwable  exc)
        {
            assertTrue(false, "Exception: " + exc.getMessage());
        }

        try
        {
            validateResultObject((Annotation)objectMapper.readValue(jsonString, ElementHeader.class));
        }
        catch (Throwable  exc)
        {
            assertTrue(false, "Exception: " + exc.getMessage());
        }

        /*
         * Through superclass
         */
        PropertyBase  propertyBase = getTestObject();

        try
        {
            jsonString = objectMapper.writeValueAsString(propertyBase);
        }
        catch (Throwable  exc)
        {
            assertTrue(false, "Exception: " + exc.getMessage());
        }

        try
        {
            validateResultObject((Annotation)objectMapper.readValue(jsonString, PropertyBase.class));
        }
        catch (Throwable  exc)
        {
            assertTrue(false, "Exception: " + exc.getMessage());
        }
    }


    /**
     * Test that toString is overridden.
     */
    @Test public void testToString()
    {
        assertTrue(getTestObject().toString().contains("Annotation"));
    }
}
