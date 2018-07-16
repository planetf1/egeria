/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.compliance.ffdc;

/**
 * AssertionFailureException is used when a test case fails an assertion.
 */
public class AssertionFailureException extends ComplianceException
{
    private String assertionId;


    /**
     * Typical constructor accepts information about the failure of the test.
     *
     * @param assertionId identifier of the assertion.
     * @param assertionMessage associated message.
     */
    public AssertionFailureException(String    assertionId,
                                     String    assertionMessage)
    {
        super("Failed Assertion: " + assertionMessage);
        this.assertionId = assertionId;
    }


    /**
     * Return the identifier of the assertion that failed.
     *
     * @return string identifier
     */
    public String getAssertionId()
    {
        return assertionId;
    }
}
