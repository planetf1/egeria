/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.frameworks.connectors.properties.beans;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.testng.annotations.Test;

import java.util.*;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

/**
 * Validate that the DerivedSchemaElement bean can be cloned, compared, serialized,
 * deserialized and printed as a String.
 */
public class TestDerivedSchemaElement
{
    private ElementType                     type                 = new ElementType();
    private List<Classification>            classifications      = new ArrayList<>();
    private Map<String, Object>             additionalProperties = new HashMap<>();
    private List<SchemaImplementationQuery> queries              = new ArrayList<>();


    /**
     * Default constructor
     */
    public TestDerivedSchemaElement()
    {

    }


    /**
     * Set up an example object to test.
     *
     * @return filled in object
     */
    private DerivedSchemaElement getTestObject()
    {
        DerivedSchemaElement testObject = new DerivedSchemaElement();

        testObject.setType(type);
        testObject.setGUID("TestGUID");
        testObject.setURL("TestURL");
        testObject.setClassifications(classifications);

        testObject.setQualifiedName("TestQualifiedName");
        testObject.setAdditionalProperties(additionalProperties);

        testObject.setVersionNumber("TestVersionNumber");
        testObject.setAuthor("TestAuthor");
        testObject.setUsage("TestUsage");
        testObject.setEncodingStandard("TestEncodingStandard");

        testObject.setDataType("TestDataType");
        testObject.setDefaultValue("TestDefaultValue");

        testObject.setFormula("TestFormula");
        testObject.setQueries(queries);

        return testObject;
    }


    /**
     * Validate that the object that comes out of the test has the same content as the original test object.
     *
     * @param resultObject object returned by the test
     */
    private void validateResultObject(DerivedSchemaElement  resultObject)
    {
        assertTrue(resultObject.getType().equals(type));
        assertTrue(resultObject.getGUID().equals("TestGUID"));
        assertTrue(resultObject.getURL().equals("TestURL"));
        assertTrue(resultObject.getClassifications() == null);

        assertTrue(resultObject.getQualifiedName().equals("TestQualifiedName"));
        assertTrue(resultObject.getAdditionalProperties() == null);

        assertTrue(resultObject.getVersionNumber().equals("TestVersionNumber"));
        assertTrue(resultObject.getAuthor().equals("TestAuthor"));
        assertTrue(resultObject.getUsage().equals("TestUsage"));
        assertTrue(resultObject.getEncodingStandard().equals("TestEncodingStandard"));

        assertTrue(resultObject.getDataType().equals("TestDataType"));
        assertTrue(resultObject.getDefaultValue().equals("TestDefaultValue"));

        assertTrue(resultObject.getFormula().equals("TestFormula"));
        assertTrue(resultObject.getQueries().equals(queries));
    }


    /**
     * Validate that the object is initialized properly
     */
    @Test public void testNullObject()
    {
        DerivedSchemaElement    nullObject = new DerivedSchemaElement();

        assertTrue(nullObject.getType() == null);
        assertTrue(nullObject.getGUID() == null);
        assertTrue(nullObject.getURL() == null);
        assertTrue(nullObject.getClassifications() == null);

        assertTrue(nullObject.getQualifiedName() == null);
        assertTrue(nullObject.getAdditionalProperties() == null);

        assertTrue(nullObject.getVersionNumber() == null);
        assertTrue(nullObject.getAuthor() == null);
        assertTrue(nullObject.getUsage() == null);
        assertTrue(nullObject.getEncodingStandard() == null);

        assertTrue(nullObject.getDataType() == null);
        assertTrue(nullObject.getDefaultValue() == null);

        assertTrue(nullObject.getFormula() == null);
        assertTrue(nullObject.getQueries() == null);

        nullObject = new DerivedSchemaElement(null);

        assertTrue(nullObject.getType() == null);
        assertTrue(nullObject.getGUID() == null);
        assertTrue(nullObject.getURL() == null);
        assertTrue(nullObject.getClassifications() == null);

        assertTrue(nullObject.getQualifiedName() == null);
        assertTrue(nullObject.getAdditionalProperties() == null);

        assertTrue(nullObject.getVersionNumber() == null);
        assertTrue(nullObject.getAuthor() == null);
        assertTrue(nullObject.getUsage() == null);
        assertTrue(nullObject.getEncodingStandard() == null);

        assertTrue(nullObject.getDataType() == null);
        assertTrue(nullObject.getDefaultValue() == null);

        assertTrue(nullObject.getFormula() == null);
        assertTrue(nullObject.getQueries() == null);
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

        DerivedSchemaElement  sameObject = getTestObject();
        assertTrue(sameObject.equals(sameObject));

        DerivedSchemaElement  differentObject = getTestObject();
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
        validateResultObject(new DerivedSchemaElement(getTestObject()));
    }


    /**
     * Test that a an object cloned through the superclass cloneSchemaElement has the same content as
     * the original
     */
    @Test public void testAbstractClone()
    {
        SchemaElement schemaElement = getTestObject();

        validateResultObject((DerivedSchemaElement) schemaElement.cloneSchemaElement());
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
            validateResultObject(objectMapper.readValue(jsonString, DerivedSchemaElement.class));
        }
        catch (Throwable  exc)
        {
            assertTrue(false, "Exception: " + exc.getMessage());
        }

        /*
         * Through superclass
         */
        PrimitiveSchemaElement  primitiveSchemaElement = getTestObject();

        try
        {
            jsonString = objectMapper.writeValueAsString(primitiveSchemaElement);
        }
        catch (Throwable  exc)
        {
            assertTrue(false, "Exception: " + exc.getMessage());
        }

        try
        {
            validateResultObject((DerivedSchemaElement) objectMapper.readValue(jsonString, PrimitiveSchemaElement.class));
        }
        catch (Throwable  exc)
        {
            assertTrue(false, "Exception: " + exc.getMessage());
        }

        /*
         * Through superclass
         */
        SchemaElement  schemaElement = getTestObject();

        try
        {
            jsonString = objectMapper.writeValueAsString(schemaElement);
        }
        catch (Throwable  exc)
        {
            assertTrue(false, "Exception: " + exc.getMessage());
        }

        try
        {
            validateResultObject((DerivedSchemaElement) objectMapper.readValue(jsonString, SchemaElement.class));
        }
        catch (Throwable  exc)
        {
            assertTrue(false, "Exception: " + exc.getMessage());
        }

        /*
         * Through superclass
         */
        Referenceable  referenceable = getTestObject();

        try
        {
            jsonString = objectMapper.writeValueAsString(referenceable);
        }
        catch (Throwable  exc)
        {
            assertTrue(false, "Exception: " + exc.getMessage());
        }

        try
        {
            validateResultObject((DerivedSchemaElement) objectMapper.readValue(jsonString, Referenceable.class));
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
            validateResultObject((DerivedSchemaElement) objectMapper.readValue(jsonString, ElementHeader.class));
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
            validateResultObject((DerivedSchemaElement) objectMapper.readValue(jsonString, PropertyBase.class));
        }
        catch (Throwable  exc)
        {
            assertTrue(false, "Exception: " + exc.getMessage());
        }
    }


    /**
     * Validate that an object generated from a JSON String has the same content as the object used to
     * create the JSON String.
     */
    @Test public void testAbstractJSON()
    {
        ObjectMapper objectMapper = new ObjectMapper();
        String       jsonString   = null;

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
            validateResultObject((DerivedSchemaElement)objectMapper.readValue(jsonString, SchemaElement.class));
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
        assertTrue(getTestObject().toString().contains("DerivedSchemaElement"));
    }
}
