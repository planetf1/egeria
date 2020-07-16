/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.fvt;

import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.glossary.Glossary;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.term.Term;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;

import java.io.IOException;

/**
 * FVT resource to call subject area client APIs to test the effectivity dates
 */
public class EffectiveDatesFVT
{

    private static final String DEFAULT_TEST_PAST_GLOSSARY_NAME = "Test past Glossary for term FVT";
    private static final String DEFAULT_TEST_FUTURE_GLOSSARY_NAME = "Test future Glossary for term FVT";
    private static final String DEFAULT_TEST_TERM_NAME = "Test term A";
    private GlossaryFVT glossaryFVT =null;
    private TermFVT termFVT=null;


    public static void main(String args[])
    {
        try
        {
            String url = RunAllFVTOn2Servers.getUrl(args);
            runWith2Servers(url);
        } catch (IOException e1)
        {
            System.out.println("Error getting user input");
        } catch (SubjectAreaFVTCheckedException e) {
            System.out.println("ERROR: " + e.getMessage() );
        } catch (UserNotAuthorizedException | InvalidParameterException | PropertyServerException e) {
            System.out.println("ERROR: " + e.getReportedErrorMessage() + " Suggested action: " + e.getReportedUserAction());
        }

    }
    public EffectiveDatesFVT(String url, String serverName,String userId) throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        System.out.println("Create a glossary");
        glossaryFVT = new GlossaryFVT(url,serverName,userId);
        termFVT= new TermFVT(url,serverName,userId);
    }
    public void deleteRemaining() throws UserNotAuthorizedException, PropertyServerException, InvalidParameterException, SubjectAreaFVTCheckedException {
        termFVT.deleteRemainingTerms();
        glossaryFVT.deleteRemainingGlossaries();
    }
    public static void runWith2Servers(String url) throws SubjectAreaFVTCheckedException, InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        runIt(url, FVTConstants.SERVER_NAME1, FVTConstants.USERID);
        runIt(url, FVTConstants.SERVER_NAME2, FVTConstants.USERID);
    }
    synchronized public static void runIt(String url, String serverName, String userId) throws InvalidParameterException, SubjectAreaFVTCheckedException, PropertyServerException, UserNotAuthorizedException {
        System.out.println("EffectiveDatesFVT runIt started");
        EffectiveDatesFVT fvt =new EffectiveDatesFVT(url,serverName, userId);
        fvt.run();
        fvt.deleteRemaining();
        System.out.println("EffectiveDatesFVT runIt stopped");
    }

    public void run() throws SubjectAreaFVTCheckedException, InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        try
        {
            glossaryFVT.createPastToGlossary(DEFAULT_TEST_PAST_GLOSSARY_NAME);
        } catch (InvalidParameterException e) {
            System.out.println("Expected creation of a Glossary with to in the past failed");
        }
        try
        {
            glossaryFVT.createPastFromGlossary(DEFAULT_TEST_PAST_GLOSSARY_NAME);
        } catch (InvalidParameterException e) {
            System.out.println("Expected creation of a Glossary with from in the past failed");
        }
        try
        {
           glossaryFVT.createInvalidEffectiveDateGlossary(DEFAULT_TEST_PAST_GLOSSARY_NAME);
        } catch (InvalidParameterException e) {
            System.out.println("Expected creation of a Glossary with invalid Effectivity dates failed");
        }
        Glossary futureGloss = glossaryFVT.createFutureGlossary(DEFAULT_TEST_FUTURE_GLOSSARY_NAME);
        FVTUtils.validateNode(futureGloss);
        Term term5 =termFVT.createTerm(DEFAULT_TEST_TERM_NAME, futureGloss.getSystemAttributes().getGUID());
        FVTUtils.validateNode(term5);
        checkTermGlossaryEffectivity(futureGloss, term5);

        Term gotTerm5 = termFVT.getTermByGUID(term5.getSystemAttributes().getGUID());
        FVTUtils.validateNode(gotTerm5);
        checkTermGlossaryEffectivity(futureGloss, term5);

        // update the term so that its effective dates not longer are compatible with the glossary
        Term futureTerm = termFVT.updateTermToFuture(gotTerm5.getSystemAttributes().getGUID(), term5);
        FVTUtils.validateNode(futureTerm);
        checkTermGlossaryEffectivity(futureGloss, futureTerm);
        futureTerm = termFVT.getTermByGUID(term5.getSystemAttributes().getGUID());
        FVTUtils.validateNode(futureTerm);
        checkTermGlossaryEffectivity(futureGloss, futureTerm);
    }
    private void checkTermGlossaryEffectivity(Glossary glossary, Term term) throws SubjectAreaFVTCheckedException {
        if (term.getGlossary()==null) {
            // error always expect a glossary
            throw new SubjectAreaFVTCheckedException("ERROR: Term expected no associated future Glossary");
        }
        if (term.getGlossary()==null) {
            // error
            throw new SubjectAreaFVTCheckedException("ERROR: Term with no effectivity constraints expected an associated future Glossary");
        }
        if (term.getGlossary()==null) {
            // error
            throw new SubjectAreaFVTCheckedException("ERROR: Term with no effectivity constraints expected an associated future Glossary");
        }
        if (glossary.getEffectiveFromTime().getTime() != term.getGlossary().getFromEffectivityTime().getTime()) {
            // error
            throw new SubjectAreaFVTCheckedException("ERROR: Term's Glossary from Time does not match the glossaries");
        }
        if (glossary.getEffectiveToTime().getTime() != term.getGlossary().getToEffectivityTime().getTime()) {
            // error
            throw new SubjectAreaFVTCheckedException("ERROR: Term's Glossary to Time does not match the glossaries");
        }
    }
}
