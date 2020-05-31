/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.digitalservice.rest;

import org.testng.annotations.Test;

import static org.testng.Assert.assertTrue;

/**
 * Test the overridden methods of DevOpsOMASAPIResponse
 */
public class OMASAPIResponseTest
{
    @Test public void TestToString()
    {
        MockAPIResponse  testObject = new MockAPIResponse();

        assertTrue(testObject.toString().contains("DevOpsOMASAPIResponse"));
        assertTrue(new MockAPIResponse(testObject).toString().contains("DevOpsOMASAPIResponse"));
    }
}
