/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.fvt;

import org.odpi.openmetadata.accessservices.subjectarea.client.SubjectAreaNodeClient;
import org.odpi.openmetadata.accessservices.subjectarea.client.SubjectAreaRestClient;
import org.odpi.openmetadata.accessservices.subjectarea.client.nodes.terms.SubjectAreaTermClient;
import org.odpi.openmetadata.accessservices.subjectarea.properties.classifications.Confidence;
import org.odpi.openmetadata.accessservices.subjectarea.properties.classifications.Confidentiality;
import org.odpi.openmetadata.accessservices.subjectarea.properties.classifications.Criticality;
import org.odpi.openmetadata.accessservices.subjectarea.properties.classifications.Retention;
import org.odpi.openmetadata.accessservices.subjectarea.properties.enums.ConfidenceLevel;
import org.odpi.openmetadata.accessservices.subjectarea.properties.enums.CriticalityLevel;
import org.odpi.openmetadata.accessservices.subjectarea.properties.enums.RetentionBasis;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.common.FindRequest;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.common.GovernanceActions;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.glossary.Glossary;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.graph.Line;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.nodesummary.GlossarySummary;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.term.Term;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.SequencingOrder;

import java.io.IOException;
import java.util.*;

/**
 * FVT resource to call subject area term client API
 */
public class TermFVT {
    private static final String DEFAULT_TEST_GLOSSARY_NAME = "Test Glossary for term FVT";
    private static final String DEFAULT_TEST_TERM_NAME = "Test term A";
    private static final String DEFAULT_TEST_TERM_NAME_UPDATED = "Test term A updated";
    private SubjectAreaNodeClient<Term> subjectAreaTerm = null;
    private GlossaryFVT glossaryFVT =null;
    private String userId =null;
    private int existingTermCount = 0;
    /*
     * Keep track of all the created guids in this set, by adding create and restore guids and removing when deleting.
     * At the end of the test it will delete any remaining guids.
     *
     * Note this FVT is called by other FVTs. Who ever constructs the FVT should run deleteRemainingTerms.
     */
    private Set<String> createdTermsSet = new HashSet<>();

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
    public TermFVT(String url,String serverName,String userId) throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        SubjectAreaRestClient client = new SubjectAreaRestClient(serverName, url);
        subjectAreaTerm = new SubjectAreaTermClient(client);
        System.out.println("Create a glossary");
        glossaryFVT = new GlossaryFVT(url,serverName,userId);
        this.userId=userId;
        existingTermCount = findTerms(".*").size();
        System.out.println("existingTermCount " + existingTermCount);
    }
    public static void runWith2Servers(String url) throws SubjectAreaFVTCheckedException, InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        runIt(url, FVTConstants.SERVER_NAME1, FVTConstants.USERID);
        runIt(url, FVTConstants.SERVER_NAME2, FVTConstants.USERID);
    }

    public static void runIt(String url, String serverName, String userId) throws  SubjectAreaFVTCheckedException, InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        System.out.println("TermFVT runIt started");
        TermFVT fvt =new TermFVT(url,serverName,userId);
        fvt.run();
        fvt.deleteRemaining();
        System.out.println("TermFVT runIt stopped");
    }
    public static int getTermCount(String url, String serverName, String userId) throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException, SubjectAreaFVTCheckedException  {
        TermFVT fvt = new TermFVT(url, serverName, userId);
        return fvt.findTerms(".*").size();
    }

    public void run() throws SubjectAreaFVTCheckedException, InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        Glossary glossary= glossaryFVT.createGlossary(DEFAULT_TEST_GLOSSARY_NAME);
        System.out.println("Create a term1");
        String glossaryGuid = glossary.getSystemAttributes().getGUID();
        Term term1 = createTerm(DEFAULT_TEST_TERM_NAME, glossaryGuid);
        FVTUtils.validateNode(term1);
        System.out.println("Create a term2 using glossary userId");
        Term term2 = createTerm(DEFAULT_TEST_TERM_NAME, glossaryGuid);
        FVTUtils.validateNode(term2);
        System.out.println("Create a term2 using glossary userId");

        Term termForUpdate = new Term();
        termForUpdate.setName(DEFAULT_TEST_TERM_NAME_UPDATED);
        System.out.println("Get term1");
        String guid = term1.getSystemAttributes().getGUID();
        Term gotTerm = getTermByGUID(guid);
        FVTUtils.validateNode(gotTerm);
        System.out.println("Update term1");
        Term updatedTerm = updateTerm(guid, termForUpdate);
        FVTUtils.validateNode(updatedTerm);
        System.out.println("Get term1 again");
        gotTerm = getTermByGUID(guid);
        FVTUtils.validateNode(gotTerm);
        System.out.println("Delete term1");
        deleteTerm(guid);
        System.out.println("Restore term1");
        //FVTUtils.validateNode(gotTerm);
        gotTerm = restoreTerm(guid);
        FVTUtils.validateNode(gotTerm);
        System.out.println("Delete term1 again");
        deleteTerm(guid);
        //FVTUtils.validateNode(gotTerm);
        System.out.println("Purge term1");
        purgeTerm(guid);
        System.out.println("Create term3 with governance actions");
        GovernanceActions governanceActions = createGovernanceActions();
        Term term3 = createTermWithGovernanceActions(DEFAULT_TEST_TERM_NAME, glossaryGuid,governanceActions);
        FVTUtils.validateNode(term3);
        if (!governanceActions.getConfidence().getLevel().equals(term3.getGovernanceActions().getConfidence().getLevel())){
            throw new SubjectAreaFVTCheckedException("ERROR: Governance actions confidence not returned  as expected");
        }
        if (!governanceActions.getConfidentiality().getLevel().equals(term3.getGovernanceActions().getConfidentiality().getLevel())) {
            throw new SubjectAreaFVTCheckedException("ERROR: Governance actions confidentiality not returned  as expected");
        }
        if (!governanceActions.getRetention().getBasis().equals(term3.getGovernanceActions().getRetention().getBasis())) {
            throw new SubjectAreaFVTCheckedException("ERROR: Governance actions retention not returned  as expected");
        }
        if (!governanceActions.getCriticality().getLevel().equals(term3.getGovernanceActions().getCriticality().getLevel())) {
            throw new SubjectAreaFVTCheckedException("ERROR: Governance actions criticality not returned  as expected. ");
        }
        GovernanceActions governanceActions2 = create2ndGovernanceActions();
        System.out.println("Update term3 with and change governance actions");
        Term term3ForUpdate = new Term();
        term3ForUpdate.setName(DEFAULT_TEST_TERM_NAME_UPDATED);
        term3ForUpdate.setGovernanceActions(governanceActions2);

        Term updatedTerm3 = updateTerm(term3.getSystemAttributes().getGUID(), term3ForUpdate);
        FVTUtils.validateNode(updatedTerm3);
        if (!governanceActions2.getConfidence().getLevel().equals(updatedTerm3.getGovernanceActions().getConfidence().getLevel())){
            throw new SubjectAreaFVTCheckedException("ERROR: Governance actions confidence not returned  as expected");
        }
        if (!governanceActions2.getConfidentiality().getLevel().equals(updatedTerm3.getGovernanceActions().getConfidentiality().getLevel())) {
            throw new SubjectAreaFVTCheckedException("ERROR: Governance actions confidentiality not returned  as expected");
        }
        if (updatedTerm3.getGovernanceActions().getRetention() !=null) {
            throw new SubjectAreaFVTCheckedException("ERROR: Governance actions retention not null as expected");
        }
        // https://github.com/odpi/egeria/issues/3457  the below line when uncommented causes an error with the graph repo.
//        if (updatedTerm3.getGovernanceActions().getCriticality().getLevel() !=null) {
//            throw new SubjectAreaFVTCheckedException("ERROR: Governance actions criticality not returned as expected. It is " + updatedTerm3.getGovernanceActions().getCriticality().getLevel().getName());
//        }
        String spacedTermName = "This is a Term with spaces in name";
        int allcount  = subjectAreaTerm.findAll(userId).size();
        int yyycount = findTerms("yyy").size();
        int zzzcount = findTerms("zzz").size();
        int spacedTermcount = findTerms( spacedTermName).size();

        System.out.println("create terms to find");
        Term termForFind1 = getTermForInput("abc",glossaryGuid);
        termForFind1.setQualifiedName("yyy");
        termForFind1 = issueCreateTerm(termForFind1);
        FVTUtils.validateNode(termForFind1);
        Term termForFind2 = createTerm("yyy",glossaryGuid);
        FVTUtils.validateNode(termForFind2);
        Term termForFind3 = createTerm("zzz",glossaryGuid);
        FVTUtils.validateNode(termForFind3);
        Term termForFind4 = createTerm("This is a Term with spaces in name",glossaryGuid);
        FVTUtils.validateNode(termForFind4);

        List<Term>  results = findTerms("zzz");
        if (results.size() !=zzzcount+1 ) {
            throw new SubjectAreaFVTCheckedException("ERROR: zzz Expected " + zzzcount+1+ " back on the find got " +results.size());
        }
        results = findTerms("yyy");
        if (results.size() !=yyycount + 2) {
            throw new SubjectAreaFVTCheckedException("ERROR: yyy Expected " + yyycount+1 + " back on the find got " +results.size());
        }
        results = findTerms(null); //it's find all terms
        if (results.size() !=allcount + 4 ) {
            throw new SubjectAreaFVTCheckedException("ERROR: allcount Expected " + allcount + 4 + " back on the find got " +results.size());
        }

        results = subjectAreaTerm.findAll(userId); //it's find all terms
        if (results.size() !=allcount + 4 ) {
            throw new SubjectAreaFVTCheckedException("ERROR: allcount2 Expected " + allcount + 4 + " back on the find got " +results.size());
        }
        //soft delete a term and check it is not found
        deleteTerm(termForFind2.getSystemAttributes().getGUID());
        //FVTUtils.validateNode(deleted4);
        results = findTerms("yyy");
        if (results.size() !=yyycount +1 ) {
            throw new SubjectAreaFVTCheckedException("ERROR: yyy2 Expected " +yyycount +1  + " back on the find got " +results.size());
        }

       // search for a term with a name with spaces in
        results = findTerms(spacedTermName);
        if (results.size() != spacedTermcount +1 ) {
            throw new SubjectAreaFVTCheckedException("ERROR: Expected spaced " + spacedTermcount+1 + " back on the find got "  +results.size());
        }
        Term term = results.get(0);
        long now = new Date().getTime();
        Date fromTermTime = new Date(now+6*1000*60*60*24);
        Date toTermTime = new Date(now+7*1000*60*60*24);

        term.setEffectiveFromTime(fromTermTime);
        term.setEffectiveToTime(toTermTime);
        Term updatedFutureTerm = updateTerm(term.getSystemAttributes().getGUID(),term);
        if (updatedFutureTerm.getEffectiveFromTime().getTime()!=fromTermTime.getTime()) {
            throw new SubjectAreaFVTCheckedException("ERROR: Expected term from time to update");
        }
        if (updatedFutureTerm.getEffectiveToTime().getTime()!=toTermTime.getTime()) {
            throw new SubjectAreaFVTCheckedException("ERROR: Expected term to time to update");
        }
        Date fromGlossaryTime = new Date(now+8*1000*60*60*24);
        Date toGlossaryTime = new Date(now+9*1000*60*60*24);
        glossary.setEffectiveFromTime(fromGlossaryTime);
        glossary.setEffectiveToTime(toGlossaryTime);
        Glossary updatedFutureGlossary= glossaryFVT.updateGlossary(glossaryGuid,glossary);

        if (updatedFutureGlossary.getEffectiveFromTime().getTime()!=fromGlossaryTime.getTime()) {
            throw new SubjectAreaFVTCheckedException("ERROR: Expected glossary from time to update");
        }
        if (updatedFutureGlossary.getEffectiveToTime().getTime()!=toGlossaryTime.getTime()) {
            throw new SubjectAreaFVTCheckedException("ERROR: Expected glossary to time to update");
        }

        Term newTerm = getTermByGUID(term.getSystemAttributes().getGUID());

        GlossarySummary glossarySummary =  newTerm.getGlossary();

        if (glossarySummary.getFromEffectivityTime().getTime()!=fromGlossaryTime.getTime()) {
            throw new SubjectAreaFVTCheckedException("ERROR: Expected glossary summary from time to update");
        }
        if (glossarySummary.getToEffectivityTime().getTime()!=toGlossaryTime.getTime()) {
            throw new SubjectAreaFVTCheckedException("ERROR: Expected glossary summary to time to update");
        }

        if (glossarySummary.getRelationshipguid() ==null) {
            throw new SubjectAreaFVTCheckedException("ERROR: Expected glossary summary non null relationship");
        }
        if (glossarySummary.getFromRelationshipEffectivityTime() !=null) {
            throw new SubjectAreaFVTCheckedException("ERROR: Expected glossary summary null relationship from time");
        }
        if (glossarySummary.getToRelationshipEffectivityTime() !=null) {
            throw new SubjectAreaFVTCheckedException("ERROR: Expected glossary summary null relationship to time");
        }
        Term term5 = new Term();
        term5.setSpineObject(true);
        term5.setName("Term5");
        glossarySummary = new GlossarySummary();
        glossarySummary.setGuid(glossaryGuid);
        term5.setGlossary(glossarySummary);
        Term createdTerm5 = issueCreateTerm(term5);
        if (createdTerm5.isSpineObject() == false) {
            throw new SubjectAreaFVTCheckedException("ERROR: Expected isSpineObject to be true ");
        }
        Term term6 = new Term();
        term6.setSpineAttribute(true);
        term6.setName("Term6");
        glossarySummary = new GlossarySummary();
        glossarySummary.setGuid(glossaryGuid);
        term6.setGlossary(glossarySummary);
        Term createdTerm6 = issueCreateTerm(term6);
        if (createdTerm6.isSpineAttribute() == false) {
            throw new SubjectAreaFVTCheckedException("ERROR: Expected isSpineAttribute to be true ");
        }
        Term term7 = new Term();
        term7.setObjectIdentifier(true);
        term7.setName("Term7");
        glossarySummary = new GlossarySummary();
        glossarySummary.setGuid(glossaryGuid);
        term7.setGlossary(glossarySummary);
        Term createdTerm7 = issueCreateTerm(term7);
        if (createdTerm7.isObjectIdentifier() == false) {
            throw new SubjectAreaFVTCheckedException("ERROR: Expected isObjectIdentifier to be true ");
        }

    }

    public  Term createTerm(String termName, String glossaryGuid) throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        Term term = getTermForInput(termName, glossaryGuid);
        return issueCreateTerm(term);
    }

    public Term issueCreateTerm(Term term) throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        Term newTerm = subjectAreaTerm.create(this.userId, term);
        if (newTerm != null)
        {
            String guid = newTerm.getSystemAttributes().getGUID();
            System.out.println("Created Term " + newTerm.getName() + " with guid " + guid);
            createdTermsSet.add(guid);
        }
        return newTerm;
    }

    private Term getTermForInput(String termName, String glossaryGuid) {
        Term term = new Term();
        term.setName(termName);
        GlossarySummary glossarySummary = new GlossarySummary();
        glossarySummary.setGuid(glossaryGuid);
        term.setGlossary(glossarySummary);
        return term;
    }

    public  Term createTermWithGovernanceActions(String termName, String glossaryGuid,GovernanceActions governanceActions) throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        Term term = getTermForInput(termName, glossaryGuid);
        term.setGovernanceActions(governanceActions);
        Term newTerm = issueCreateTerm(term);
        return newTerm;
    }

    private GovernanceActions createGovernanceActions() {
        GovernanceActions governanceActions = new GovernanceActions();
        Confidentiality confidentiality = new Confidentiality();
        confidentiality.setLevel(6);
        governanceActions.setConfidentiality(confidentiality);

        Confidence confidence = new Confidence();
        confidence.setLevel(ConfidenceLevel.Authoritative);
        governanceActions.setConfidence(confidence);

        Criticality criticality = new Criticality();
        criticality.setLevel(CriticalityLevel.Catastrophic);
        governanceActions.setCriticality(criticality);

        Retention retention = new Retention();
        retention.setBasis(RetentionBasis.ProjectLifetime);
        governanceActions.setRetention(retention);
        return governanceActions;
    }
    private GovernanceActions create2ndGovernanceActions() {
        GovernanceActions governanceActions = new GovernanceActions();
        Confidentiality confidentiality = new Confidentiality();
        confidentiality.setLevel(5);
        governanceActions.setConfidentiality(confidentiality);

        Confidence confidence = new Confidence();
        confidence.setLevel(ConfidenceLevel.AdHoc);
        governanceActions.setConfidence(confidence);
        // remove this classification level
        Criticality criticality = new Criticality();
        criticality.setLevel(null);
        governanceActions.setCriticality(criticality);
        // remove retention by nulling it
        governanceActions.setRetention(null);
        return governanceActions;
    }


    public Term getTermByGUID(String guid) throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        Term term = subjectAreaTerm.getByGUID(this.userId, guid);
        if (term != null)
        {
            System.out.println("Got Term " + term.getName() + " with userId " + term.getSystemAttributes().getGUID() + " and status " + term.getSystemAttributes().getStatus());
        }
        return term;
    }
    public List<Term> findTerms(String criteria) throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        FindRequest findRequest = new FindRequest();
        findRequest.setSearchCriteria(criteria);
        List<Term> terms = subjectAreaTerm.find(this.userId, findRequest);
        return terms;
    }

    public Term updateTerm(String guid, Term term) throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        Term updatedTerm = subjectAreaTerm.update(this.userId, guid, term);
        if (updatedTerm != null)
        {
            System.out.println("Updated Term name to " + updatedTerm.getName());
        }
        return updatedTerm;
    }
    public Term restoreTerm(String guid) throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        Term restoredTerm = subjectAreaTerm.restore(this.userId, guid);
        if (restoredTerm != null)
        {
            System.out.println("Restored Term " + restoredTerm.getName());
            createdTermsSet.add(guid);
        }
        return restoredTerm;
    }
    public Term updateTermToFuture(String guid, Term term) throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        long now = new Date().getTime();

       term.setEffectiveFromTime(new Date(now+6*1000*60*60*24));
       term.setEffectiveToTime(new Date(now+7*1000*60*60*24));

        Term updatedTerm = subjectAreaTerm.update(this.userId, guid, term);
        if (updatedTerm != null)
        {
            System.out.println("Updated Term name to " + updatedTerm.getName());
        }
        return updatedTerm;
    }

    public void deleteTerm(String guid) throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
            subjectAreaTerm.delete(this.userId, guid);
            createdTermsSet.remove(guid);
            System.out.println("Delete succeeded");
    }

    /**
     * Purge - we should not need to decrement the createdTermsSet as the soft delete should have done this
     * @param guid
     * @throws InvalidParameterException
     * @throws PropertyServerException
     * @throws UserNotAuthorizedException
     */
    public void purgeTerm(String guid) throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        subjectAreaTerm.purge(this.userId, guid);
        System.out.println("Purge succeeded");
    }

    public List<Line> getTermRelationships(Term term) throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        return subjectAreaTerm.getAllRelationships(this.userId, term.getSystemAttributes().getGUID());
    }

    public List<Line> getTermRelationships(Term term, Date asOfTime, int offset, int pageSize, SequencingOrder sequenceOrder, String sequenceProperty) throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        FindRequest findRequest = new FindRequest();
        findRequest.setAsOfTime(asOfTime);
        findRequest.setOffset(offset);
        findRequest.setPageSize(pageSize);
        findRequest.setSequencingOrder(sequenceOrder);
        findRequest.setSequencingProperty(sequenceProperty);
        return subjectAreaTerm.getRelationships(this.userId, term.getSystemAttributes().getGUID(),findRequest);
    }
    void deleteRemaining() throws UserNotAuthorizedException, PropertyServerException, InvalidParameterException, SubjectAreaFVTCheckedException {
        deleteRemainingTerms();
        glossaryFVT.deleteRemainingGlossaries();
    }
    void deleteRemainingTerms() throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException, SubjectAreaFVTCheckedException {
        Iterator<String> iter =  createdTermsSet.iterator();
        while (iter.hasNext()) {
            String guid = iter.next();
            iter.remove();
            deleteTerm(guid);
        }
        List<Term> terms = findTerms(".*");
        if (terms.size() != existingTermCount) {
            throw new SubjectAreaFVTCheckedException("ERROR: Expected " +existingTermCount + " Terms to be found, got " + terms.size());
        }
    }
}
