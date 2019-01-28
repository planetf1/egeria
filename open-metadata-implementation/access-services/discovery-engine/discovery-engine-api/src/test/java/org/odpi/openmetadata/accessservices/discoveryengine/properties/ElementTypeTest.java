/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.discoveryengine.properties;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.testng.annotations.Test;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

/**
 * Validate that the ElementType bean can be cloned, compared, serialized, deserialized and printed as a String.
 */
public class ElementTypeTest
{
    /**
     * Set up an example object to test.
     *
     * @return filled in object
     */
    private ElementType getTestObject()
    {
        ElementType testObject = new ElementType();

        testObject.setElementTypeId("TestTypeId");
        testObject.setElementTypeName("TestTypeName");
        testObject.setElementTypeVersion(5);
        testObject.setElementTypeDescription("TestTypeDescription");
        testObject.setElementSourceServer("TestSourceServer");
        testObject.setElementOrigin(ElementOrigin.CONTENT_PACK);
        testObject.setElementHomeMetadataCollectionId("TestHomeId");
        testObject.setElementLicense("TestLicense");

        return testObject;
    }


    /**
     * Validate that the object that comes out of the test has the same content as the original test object.
     *
     * @param resultObject object returned by the test
     */
    private void validateResultObject(ElementType resultObject)
    {
        assertTrue(resultObject.getElementTypeId().equals("TestTypeId"));
        assertTrue(resultObject.getElementTypeName().equals("TestTypeName"));
        assertTrue(resultObject.getElementTypeVersion() == 5);
        assertTrue(resultObject.getElementTypeDescription().equals("TestTypeDescription"));
        assertTrue(resultObject.getElementSourceServer().equals("TestSourceServer"));
        assertTrue(resultObject.getElementOrigin().equals(ElementOrigin.CONTENT_PACK));
        assertTrue(resultObject.getElementHomeMetadataCollectionId().equals("TestHomeId"));
        assertTrue(resultObject.getElementLicense().equals("TestLicense"));
    }


    /**
     * Validate that 2 different objects with the same content are evaluated as equal.
     * Also that different objects are considered not equal.
     */
    @Test
    public void testEquals()
    {
        assertFalse(getTestObject().equals(null));
        assertFalse(getTestObject().equals("DummyString"));
        assertTrue(getTestObject().equals(getTestObject()));

        ElementType sameObject = getTestObject();
        assertTrue(sameObject.equals(sameObject));

        ElementType differentObject = getTestObject();
        differentObject.setElementTypeId("Different");
        assertFalse(getTestObject().equals(differentObject));
    }


    /**
     * Validate that 2 different objects with the same content have the same hash code.
     */
    @Test
    public void testHashCode()
    {
        assertTrue(getTestObject().hashCode() == getTestObject().hashCode());
    }


    /**
     * Validate that an object cloned from another object has the same content as the original
     */
    @Test
    public void testClone()
    {
        validateResultObject(new ElementType(getTestObject()));
    }


    /**
     * Validate that an object generated from a JSON String has the same content as the object used to
     * create the JSON String.
     */
    @Test
    public void testJSON()
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
        catch (Throwable exc)
        {
            assertTrue(false, "Exception: " + exc.getMessage());
        }

        try
        {
            validateResultObject(objectMapper.readValue(jsonString, ElementType.class));
        }
        catch (Throwable exc)
        {
            assertTrue(false, "Exception: " + exc.getMessage());
        }

        /*
         * Through superclass
         */
        PropertyBase propertyBase = getTestObject();

        try
        {
            jsonString = objectMapper.writeValueAsString(propertyBase);
        }
        catch (Throwable exc)
        {
            assertTrue(false, "Exception: " + exc.getMessage());
        }

        try
        {
            validateResultObject((ElementType) objectMapper.readValue(jsonString, PropertyBase.class));
        }
        catch (Throwable exc)
        {
            assertTrue(false, "Exception: " + exc.getMessage());
        }
    }


    /**
     * Test that toString is overridden.
     */
    @Test
    public void testToString()
    {
        assertTrue(getTestObject().toString().contains("ElementType"));
    }
}
