/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.accessservices.assetowner.ffdc;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

/**
 * Verify the AssetOwnerErrorCode enum contains unique message ids, non-null names and descriptions and can be
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

    private void testSingleErrorCodeValues(AssetOwnerErrorCode  testValue)
    {
        String                  testInfo;

        assertTrue(isUniqueOrdinal(testValue.getErrorMessageId()));
        assertTrue(testValue.getErrorMessageId().contains("ASSET-OWNER"));
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
        testSingleErrorCodeValues(AssetOwnerErrorCode.SERVER_URL_NOT_SPECIFIED);
        testSingleErrorCodeValues(AssetOwnerErrorCode.SERVER_URL_MALFORMED);
        testSingleErrorCodeValues(AssetOwnerErrorCode.NULL_USER_ID);
        testSingleErrorCodeValues(AssetOwnerErrorCode.NULL_GUID);
        testSingleErrorCodeValues(AssetOwnerErrorCode.NULL_NAME);
        testSingleErrorCodeValues(AssetOwnerErrorCode.NO_CONNECTED_ASSET);
        testSingleErrorCodeValues(AssetOwnerErrorCode.TOO_MANY_CONNECTIONS);
        testSingleErrorCodeValues(AssetOwnerErrorCode.USER_NOT_AUTHORIZED);
        testSingleErrorCodeValues(AssetOwnerErrorCode.PROPERTY_SERVER_ERROR);
        testSingleErrorCodeValues(AssetOwnerErrorCode.NULL_ENUM);
        testSingleErrorCodeValues(AssetOwnerErrorCode.SERVER_NOT_AVAILABLE);
        testSingleErrorCodeValues(AssetOwnerErrorCode.OMRS_NOT_INITIALIZED);
        testSingleErrorCodeValues(AssetOwnerErrorCode.OMRS_NOT_AVAILABLE);
        testSingleErrorCodeValues(AssetOwnerErrorCode.NO_METADATA_COLLECTION);
        testSingleErrorCodeValues(AssetOwnerErrorCode.CONNECTION_NOT_FOUND);
        testSingleErrorCodeValues(AssetOwnerErrorCode.PROXY_CONNECTION_FOUND);
        testSingleErrorCodeValues(AssetOwnerErrorCode.ASSET_NOT_FOUND);
        testSingleErrorCodeValues(AssetOwnerErrorCode.UNKNOWN_ASSET);
        testSingleErrorCodeValues(AssetOwnerErrorCode.NULL_CONNECTION_RETURNED);
        testSingleErrorCodeValues(AssetOwnerErrorCode.NULL_CONNECTOR_RETURNED);
        testSingleErrorCodeValues(AssetOwnerErrorCode.NULL_RESPONSE_FROM_API);
        testSingleErrorCodeValues(AssetOwnerErrorCode.CLIENT_SIDE_REST_API_ERROR);
        testSingleErrorCodeValues(AssetOwnerErrorCode.SERVICE_NOT_INITIALIZED);
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
            jsonString = objectMapper.writeValueAsString(AssetOwnerErrorCode.CLIENT_SIDE_REST_API_ERROR);
        }
        catch (Throwable  exc)
        {
            assertTrue(false, "Exception: " + exc.getMessage());
        }

        try
        {
            assertTrue(objectMapper.readValue(jsonString, AssetOwnerErrorCode.class) == AssetOwnerErrorCode.CLIENT_SIDE_REST_API_ERROR);
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
        assertTrue(AssetOwnerErrorCode.OMRS_NOT_INITIALIZED.toString().contains("AssetOwnerErrorCode"));
    }


    /**
     * Test that equals is working.
     */
    @Test public void testEquals()
    {
        assertTrue(AssetOwnerErrorCode.SERVER_URL_NOT_SPECIFIED.equals(AssetOwnerErrorCode.SERVER_URL_NOT_SPECIFIED));
        assertFalse(AssetOwnerErrorCode.SERVER_URL_NOT_SPECIFIED.equals(AssetOwnerErrorCode.SERVICE_NOT_INITIALIZED));
    }


    /**
     * Test that hashcode is working.
     */
    @Test public void testHashcode()
    {
        assertTrue(AssetOwnerErrorCode.SERVER_URL_NOT_SPECIFIED.hashCode() == AssetOwnerErrorCode.SERVER_URL_NOT_SPECIFIED.hashCode());
        assertFalse(AssetOwnerErrorCode.SERVER_URL_NOT_SPECIFIED.hashCode() == AssetOwnerErrorCode.SERVICE_NOT_INITIALIZED.hashCode());
    }
}
