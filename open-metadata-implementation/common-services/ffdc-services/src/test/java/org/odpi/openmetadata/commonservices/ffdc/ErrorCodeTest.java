/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.ffdc;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

/**
 * Verify the OMAGCommonErrorCode enum contains unique message ids, non-null names and descriptions and can be
 * serialized to JSON and back again.
 */
public class ErrorCodeTest
{
    private List<String> existingMessageIds = new ArrayList<>();

    /**
     * Validate that a supplied ordinal is unique.
     *
     * @param ordinal value to test
     * @return boolean result
     */
    private boolean isUniqueOrdinal(String  ordinal)
    {
        if (existingMessageIds.contains(ordinal))
        {
            return false;
        }
        else
        {
            existingMessageIds.add(ordinal);
            return true;
        }
    }

    private void testSingleErrorCodeValues(OMAGCommonErrorCode  testValue)
    {
        String                  testInfo;

        assertTrue(isUniqueOrdinal(testValue.getErrorMessageId()));
        assertTrue(testValue.getErrorMessageId().contains("OMAG-COMMON"));
        assertTrue(testValue.getErrorMessageId().endsWith(" "));
        assertTrue(testValue.getHTTPErrorCode() != 0);
        testInfo = testValue.getUnformattedErrorMessage();
        assertTrue(testInfo != null);
        assertFalse(testInfo.isEmpty());
        testInfo = testValue.getFormattedErrorMessage("Field1", "Field2", "Field3", "Field4", "Field5", "Field6");
        assertTrue(testInfo != null);
        assertFalse(testInfo.isEmpty());
        testInfo = testValue.getSystemAction();
        assertTrue(testInfo != null);
        assertFalse(testInfo.isEmpty());
        testInfo = testValue.getUserAction();
        assertTrue(testInfo != null);
        assertFalse(testInfo.isEmpty());
    }


    /**
     * Validated the values of the enum.
     */
    @Test public void testAllErrorCodeValues()
    {
        testSingleErrorCodeValues(OMAGCommonErrorCode.SERVER_URL_NOT_SPECIFIED);
        testSingleErrorCodeValues(OMAGCommonErrorCode.SERVER_URL_MALFORMED);
        testSingleErrorCodeValues(OMAGCommonErrorCode.SERVER_NAME_NOT_SPECIFIED);
        testSingleErrorCodeValues(OMAGCommonErrorCode.NULL_USER_ID);
        testSingleErrorCodeValues(OMAGCommonErrorCode.NULL_GUID);
        testSingleErrorCodeValues(OMAGCommonErrorCode.NULL_NAME);
        testSingleErrorCodeValues(OMAGCommonErrorCode.NULL_ARRAY_PARAMETER);
        testSingleErrorCodeValues(OMAGCommonErrorCode.NEGATIVE_START_FROM);
        testSingleErrorCodeValues(OMAGCommonErrorCode.NEGATIVE_PAGE_SIZE);
        testSingleErrorCodeValues(OMAGCommonErrorCode.MAX_PAGE_SIZE);
        testSingleErrorCodeValues(OMAGCommonErrorCode.NULL_CONNECTION_PARAMETER);
        testSingleErrorCodeValues(OMAGCommonErrorCode.NULL_ENUM);
        testSingleErrorCodeValues(OMAGCommonErrorCode.NULL_TEXT);
        testSingleErrorCodeValues(OMAGCommonErrorCode.NULL_LOCAL_SERVER_NAME);
        testSingleErrorCodeValues(OMAGCommonErrorCode.NULL_OBJECT);
        testSingleErrorCodeValues(OMAGCommonErrorCode.UNEXPECTED_EXCEPTION);
        testSingleErrorCodeValues(OMAGCommonErrorCode.NO_REQUEST_BODY);
        testSingleErrorCodeValues(OMAGCommonErrorCode.UNRECOGNIZED_TYPE_NAME);
        testSingleErrorCodeValues(OMAGCommonErrorCode.BAD_SUB_TYPE_NAME);
        testSingleErrorCodeValues(OMAGCommonErrorCode.NOT_IN_THE_ZONE);
        testSingleErrorCodeValues(OMAGCommonErrorCode.UNKNOWN_ELEMENT);
        testSingleErrorCodeValues(OMAGCommonErrorCode.NULL_SEARCH_STRING);
        testSingleErrorCodeValues(OMAGCommonErrorCode.CANNOT_DELETE_ELEMENT_IN_USE);
        testSingleErrorCodeValues(OMAGCommonErrorCode.INSTANCE_WRONG_TYPE_FOR_GUID);
        testSingleErrorCodeValues(OMAGCommonErrorCode.METHOD_NOT_IMPLEMENTED);
        testSingleErrorCodeValues(OMAGCommonErrorCode.CLIENT_SIDE_REST_API_ERROR);
    }



    /**
     * Validate that an object generated from a JSON String has the same content as the object used to
     * create the JSON String.
     */
    @Test public void testJSON()
    {
        ObjectMapper objectMapper = new ObjectMapper();
        String       jsonString   = null;

        try
        {
            jsonString = objectMapper.writeValueAsString(OMAGCommonErrorCode.SERVER_URL_NOT_SPECIFIED);
        }
        catch (Throwable  exc)
        {
            assertTrue(false, "Exception: " + exc.getMessage());
        }

        try
        {
            assertTrue(objectMapper.readValue(jsonString, OMAGCommonErrorCode.class) == OMAGCommonErrorCode.SERVER_URL_NOT_SPECIFIED);
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
        assertTrue(OMAGCommonErrorCode.SERVER_URL_NOT_SPECIFIED.toString().contains("OMAGCommonErrorCode"));
    }


    /**
     * Test that equals is working.
     */
    @Test public void testEquals()
    {
        assertTrue(OMAGCommonErrorCode.SERVER_URL_NOT_SPECIFIED.equals(OMAGCommonErrorCode.SERVER_URL_NOT_SPECIFIED));
        assertFalse(OMAGCommonErrorCode.SERVER_URL_NOT_SPECIFIED.equals(OMAGCommonErrorCode.UNEXPECTED_EXCEPTION));
    }


    /**
     * Test that hashcode is working.
     */
    @Test public void testHashcode()
    {
        assertTrue(OMAGCommonErrorCode.SERVER_URL_NOT_SPECIFIED.hashCode() == OMAGCommonErrorCode.SERVER_URL_NOT_SPECIFIED.hashCode());
        assertFalse(OMAGCommonErrorCode.SERVER_URL_NOT_SPECIFIED.hashCode() == OMAGCommonErrorCode.UNEXPECTED_EXCEPTION.hashCode());
    }
}
