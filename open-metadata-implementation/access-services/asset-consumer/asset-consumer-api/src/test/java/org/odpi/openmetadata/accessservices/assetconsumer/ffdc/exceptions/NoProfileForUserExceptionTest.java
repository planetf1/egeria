/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetconsumer.ffdc.exceptions;

import org.testng.annotations.Test;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;


/**
 * Validate that the NoProfileForUserException is properly populated and supports toString, hashCode and
 * equals.
 */
public class NoProfileForUserExceptionTest
{
    private int       reportedHTTPCode           = 404;
    private String    reportingClassName         = "TestClassName";
    private String    reportingActionDescription = "TestActionDescription";
    private String    reportedErrorMessage       = "TestErrorMessage";
    private String    reportedSystemAction       = "TestSystemAction";
    private String    reportedUserAction         = "TestUserAction";
    private String    userId                     = "TestUserId";
    private Throwable reportedCaughtException    = new Exception("TestReportedCaughtException");

    /**
     * Constructor
     */
    public NoProfileForUserExceptionTest()
    {
    }


    /**
     * Test that a new exception is properly populated
     */
    @Test public void testNewException()
    {
        NoProfileForUserException exception = new NoProfileForUserException(reportedHTTPCode,
                                                                            reportingClassName,
                                                                            reportingActionDescription,
                                                                            reportedErrorMessage,
                                                                            reportedSystemAction,
                                                                            reportedUserAction,
                                                                            null);

        assertTrue(exception.getReportedHTTPCode() == reportedHTTPCode);
        assertTrue(exception.getReportingClassName().equals(reportingClassName));
        assertTrue(exception.getReportingActionDescription().equals(reportingActionDescription));
        assertTrue(exception.getErrorMessage().equals(reportedErrorMessage));
        assertTrue(exception.getReportedSystemAction().equals(reportedSystemAction));
        assertTrue(exception.getReportedUserAction().equals(reportedUserAction));
        assertTrue(exception.getReportedCaughtException() == null);
        assertTrue(exception.getUserId() == null);
    }


    /**
     * Test that a caught exception is properly wrapped
     */
    @Test public void testWrappingException()
    {
        NoProfileForUserException exception = new NoProfileForUserException(reportedHTTPCode,
                                                                            reportingClassName,
                                                                            reportingActionDescription,
                                                                            reportedErrorMessage,
                                                                            reportedSystemAction,
                                                                            reportedUserAction,
                                                                            reportedCaughtException,
                                                                            userId);

        assertTrue(exception.getReportedHTTPCode() == reportedHTTPCode);
        assertTrue(exception.getReportingClassName().equals(reportingClassName));
        assertTrue(exception.getReportingActionDescription().equals(reportingActionDescription));
        assertTrue(exception.getErrorMessage().equals(reportedErrorMessage));
        assertTrue(exception.getReportedSystemAction().equals(reportedSystemAction));
        assertTrue(exception.getReportedUserAction().equals(reportedUserAction));
        assertFalse(exception.getReportedCaughtException().equals(null));
        assertTrue(exception.getReportedCaughtException().getMessage().equals("TestReportedCaughtException"));
        assertTrue(exception.getUserId().equals(userId));
    }


    /**
     * Validate that string, equals and hashCode work off of the values of the exception
     */
    @Test public void testHashCode()
    {
        NoProfileForUserException exception = new NoProfileForUserException(reportedHTTPCode,
                                                                            reportingClassName,
                                                                            reportingActionDescription,
                                                                            reportedErrorMessage,
                                                                            reportedSystemAction,
                                                                            reportedUserAction,
                                                                            userId);

        NoProfileForUserException exception2 = new NoProfileForUserException(reportedHTTPCode,
                                                                             reportingClassName,
                                                                             reportingActionDescription,
                                                                             reportedErrorMessage,
                                                                             reportedSystemAction,
                                                                             reportedUserAction,
                                                                             reportedCaughtException,
                                                                             userId);

        NoProfileForUserException exception3 = new NoProfileForUserException(reportedHTTPCode,
                                                                             reportingClassName,
                                                                             reportingActionDescription,
                                                                             reportedErrorMessage,
                                                                             reportedSystemAction,
                                                                             reportedUserAction,
                                                                             reportedCaughtException,
                                                                             userId);


        assertTrue(exception.hashCode() == exception.hashCode());
        assertFalse(exception.hashCode() == exception2.hashCode());

        assertTrue(exception.equals(exception));
        assertFalse(exception.equals(reportedCaughtException));
        assertFalse(exception.equals(exception2));
        assertTrue(exception2.equals(exception3));

        assertTrue(exception.toString().contains("NoProfileForUserException"));

        assertTrue(exception.toString().equals(exception.toString()));
        assertFalse(exception.toString().equals(exception2.toString()));

        assertTrue(exception.getUserId().equals(exception2.getUserId()));
    }
}
