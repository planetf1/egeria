/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.glossaryauthor.handlers;

import org.odpi.openmetadata.accessservices.subjectarea.client.relationships.SubjectAreaRelationship;
import org.odpi.openmetadata.accessservices.subjectarea.properties.relationships.*;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;

/**
 * The relationship handler is initialised with a SubjectAreaRelationship, that contains the server the call should be sent to.
 * The handler exposes methods for term functionality for the glossary author view
 */
public class RelationshipHandler {
    private SubjectAreaRelationship subjectAreaRelationship;

    /**
     * Constructor for the RelationshipHandler
     *
     * @param subjectAreaRelationship The SubjectAreaDefinition Open Metadata Access Service (OMAS) API for terms. This is the same as the
     *                                The SubjectAreaDefinition Open Metadata View Service (OMVS) API for terms.
     */
    public RelationshipHandler(SubjectAreaRelationship subjectAreaRelationship) {
        this.subjectAreaRelationship = subjectAreaRelationship;
    }

    /**
     * Create a Term HASA Relationship. A relationship between a spine object and a spine attribute.
     * Note that this method does not error if the relationship ends are not spine objects or spine attributes.
     * <p>
     * @param userId               userId under which the request is performed
     * @param termHASARelationship the HASA relationship
     * @return the created term HASA relationship
     *
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid
     * @throws PropertyServerException              Property server exception
     */
    public Hasa createTermHASARelationship(String userId, Hasa termHASARelationship) throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        return subjectAreaRelationship.hasa().create(userId, termHASARelationship);
    }

    /**
     * Get a Term HASA Relationship. A relationship between a spine object and a spine attribute.
     *
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the Hasa relationship to get
     * @return Hasa
     *
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid.
     * @throws PropertyServerException              Property server exception
     */
    public Hasa getTermHASARelationship(String userId, String guid) throws InvalidParameterException,
                                                                           UserNotAuthorizedException,
                                                                           PropertyServerException {
        return subjectAreaRelationship.hasa().getByGUID(userId, guid);
    }

    /**
     * Update a Term HASA Relationship. A relationship between a spine object and a spine attribute.
     * <p>
     *
     * @param userId               userId under which the request is performed
     * @param guid   guid of the Hasa relationship
     * @param termHASARelationship the HASA relationship
     * @return the updated term HASA relationship
     *
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid
     * @throws PropertyServerException              Property Server Exception
     */
    public Hasa updateTermHASARelationship(String userId, String guid, Hasa termHASARelationship) throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException {
        return subjectAreaRelationship.hasa().update(userId, guid, termHASARelationship);
    }

    /**
     * Replace a Term HASA Relationship.
     * <p>
     *
     * @param userId               userId under which the request is performed
     * @param guid   guid of the Hasa relationship
     * @param termHASARelationship the HASA relationship
     * @return the replaced term HASA relationship
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid
     * @throws PropertyServerException              Property server exception
     */
    public Hasa replaceTermHASARelationship(String userId, String guid, Hasa termHASARelationship) throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException {
        return subjectAreaRelationship.hasa().replace(userId, guid, termHASARelationship);
    }

    /**
     * Delete a Term HASA Relationship. A relationship between a spine object and a spine attribute.
     * A delete (also known as a soft delete) means that the relationship instance will exist in a deleted state in the repository after the delete operation. This means
     * that it is possible to undo the delete.
     *
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the Hasa relationship to delete
     * <p>
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid.
     * @throws PropertyServerException              Property server exception
     */
    public void deleteTermHASARelationship(String userId, String guid) throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException {
        subjectAreaRelationship.hasa().delete(userId, guid);
    }

    /**
     * Purge a Term HASA Relationship. A relationship between a spine object and a spine attribute.
     * A purge means that the relationship will not exist after the operation.
     *
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the Hasa relationship to delete
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid.
     * @throws PropertyServerException              Property server exception
     */
    public void purgeTermHASARelationship(String userId, String guid) throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException {
        subjectAreaRelationship.hasa().purge(userId, guid);
    }

    /**
     * Restore a Term HASA Relationship. A relationship between a spine object and a spine attribute.
     * <p>
     * Restore allows the deleted has a relationship to be made active again. Restore allows deletes to be undone. Hard deletes are not stored in the repository so cannot be restored.
     *
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the has a relationship to delete
     * @return the restored has a relationship
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid
     * @throws PropertyServerException              Property server exception
     */
    public Hasa restoreTermHASARelationship(String userId, String guid) throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException {
        return subjectAreaRelationship.hasa().restore(userId, guid);
    }

    /**
     * Create a RelatedTerm. A Related Term is a link between two similar Terms.
     *
     * <p>
     *
     * @param userId                  unique identifier for requesting user, under which the request is performed
     * @param relatedTermRelationship the RelatedTerm relationship
     *
     * @return the created RelatedTerm relationship
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid
     * @throws PropertyServerException              Property Server Exception
     */
    public RelatedTerm createRelatedTerm(String userId, RelatedTerm relatedTermRelationship) throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException {
        return subjectAreaRelationship.relatedTerm().create(userId, relatedTermRelationship);
    }

    /**
     * Get a RelatedTerm. A Related Term is a link between two similar Terms.
     *
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the RelatedTerm relationship to get
     * @return RelatedTerm
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid.
     * @throws PropertyServerException              Property server exception
     */
    public RelatedTerm getRelatedTerm(String userId, String guid) throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException {
        return subjectAreaRelationship.relatedTerm().getByGUID(userId, guid);
    }

    /**
     * Update a RelatedTerm Relationship.
     * <p>
     *
     * @param userId          userId under which the request is performed
     * @param termRelatedTerm the RelatedTerm relationship
     * @param guid   guid of the RelatedTerm relationship
     * @return the updated term RelatedTerm relationship
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid
     * @throws PropertyServerException              Property server exception
     */
    public RelatedTerm updateRelatedTerm(String userId, String guid, RelatedTerm termRelatedTerm) throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException {
        return subjectAreaRelationship.relatedTerm().update(userId, guid, termRelatedTerm);
    }

    /**
     * Replace an ReplacementTerm relationship, which is link to a glossary term that is replacing an obsolete glossary term.
     * <p>
     *
     * @param userId          userId under which the request is performed
     * @param guid   guid of the RelatedTerm relationship
     * @param termRelatedTerm the replacement related term relationship
     * @return ReplacementTerm replaced related Term relationship
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid
     * @throws PropertyServerException              Property server exception
     */
    public RelatedTerm replaceRelatedTerm(String userId, String guid, RelatedTerm termRelatedTerm) throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException {
        return subjectAreaRelationship.relatedTerm().replace(userId, guid, termRelatedTerm);
    }

    /**
     * Restore a Related Term relationship
     * <p>
     * Restore allows the deleted Synonym relationship to be made active again. Restore allows deletes to be undone. Hard deletes are not stored in the repository so cannot be restored.
     *
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the related term relationship to restore
     * @return the restored related term relationship
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid
     * @throws PropertyServerException              Property server exception
     */
    public RelatedTerm restoreRelatedTerm(String userId, String guid) throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException {
        return subjectAreaRelationship.relatedTerm().restore(userId, guid);
    }

    /**
     * Delete a RelatedTerm. A Related Term is a link between two similar Terms.
     * A delete (also known as a soft delete) means that the relationship instance will exist in a deleted state in the repository after the delete operation. This means
     * that it is possible to undo the delete.
     *
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the RelatedTerm relationship to delete
     * <p>
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid.
     * @throws PropertyServerException              Property server exception
     */
    public void deleteRelatedTerm(String userId, String guid) throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException {
        subjectAreaRelationship.relatedTerm().delete(userId, guid);
    }

    /**
     * Purge a RelatedTerm. A Related Term is a link between two similar Terms.
     * A purge means that the relationship will not exist after the operation.
     *
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the RelatedTerm relationship to delete
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid.
     * @throws PropertyServerException              Property server exception
     */
    public void purgeRelatedTerm(String userId, String guid) throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException {
        subjectAreaRelationship.relatedTerm().purge(userId, guid);
    }

    /**
     * Restore a related term relationship
     * <p>
     * Restore allows the deleted related term relationship to be made active again. Restore allows deletes to be undone. Hard deletes are not stored in the repository so cannot be restored.
     *
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the related term relationship to delete
     * @return the restored related term relationship
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid
     * @throws PropertyServerException              Property server exception
     */
    public RelatedTerm restoreRelatedTermRelationship(String userId, String guid) throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException {
        return subjectAreaRelationship.relatedTerm().restore(userId, guid);
    }


    /**
     * Create a synonym relationship. A link between glossary terms that have the same meaning.
     * <p>
     *
     * @param userId  userId under which the request is performed
     * @param synonym the Synonym relationship
     * @return the created Synonym relationship
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid
     * @throws PropertyServerException              Property server exception
     */
    public Synonym createSynonymRelationship(String userId, Synonym synonym) throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException {
        return subjectAreaRelationship.synonym().create(userId, synonym);
    }

    /**
     * Get a synonym relationship. A link between glossary terms that have the same meaning.
     *
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the Synonym relationship to get
     * @return Synonym
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid.
     * @throws PropertyServerException              Property server exception
     */
    public Synonym getSynonymRelationship(String userId, String guid) throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException {
        return subjectAreaRelationship.synonym().getByGUID(userId, guid);
    }

    /**
     * Update a Synonym relationship which is a link between glossary terms that have the same meaning
     * <p>
     *
     * @param userId  userId under which the request is performed
     * @param synonym the Synonym relationship
     * @param guid   guid of the Synonym relationship
     * @return updated Synonym relationship
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid
     * @throws PropertyServerException              Property server exception
     */
    public Synonym updateSynonymRelationship(String userId, String guid, Synonym synonym) throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException {
        return subjectAreaRelationship.synonym().update(userId, guid, synonym);
    }

    /**
     * Replace a Synonym relationship, which is a link between glossary terms that have the same meaning
     * <p>
     *
     * @param userId  userId under which the request is performed
     * @param guid   guid of the Synonym relationship
     * @param synonym the Synonym relationship
     * @return replaced synonym relationship
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid
     * @throws PropertyServerException              Property server exception
     */
    public Synonym replaceSynonymRelationship(String userId, String guid, Synonym synonym) throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException {
        return subjectAreaRelationship.synonym().replace(userId, guid, synonym);
    }

    /**
     * Delete a synonym relationship. A link between glossary terms that have the same meaning.
     * A delete (also known as a soft delete) means that the relationship instance will exist in a deleted state in the repository after the delete operation. This means
     * that it is possible to undo the delete.
     *
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the synonym relationship to delete
     * <p>
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid.
     * @throws PropertyServerException              Property server exception
     */
    public void deleteSynonymRelationship(String userId, String guid) throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException {
        subjectAreaRelationship.synonym().delete(userId, guid);
    }

    /**
     * Purge a synonym relationship. A link between glossary terms that have the same meaning.
     * A purge means that the relationship will not exist after the operation.
     *
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the Synonym relationship to delete
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid.
     * @throws PropertyServerException              Property server exception
     */
    public void purgeSynonymRelationship(String userId, String guid) throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException {
        subjectAreaRelationship.synonym().purge(userId, guid);
    }

    /**
     * Restore a Synonym relationship
     * <p>
     * Restore allows the deleted Synonym relationship to be made active again. Restore allows deletes to be undone. Hard deletes are not stored in the repository so cannot be restored.
     *
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the Synonym relationship to delete
     * @return the restored Synonym relationship
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid
     * @throws PropertyServerException              Property server exception
     */
    public Synonym restoreSynonymRelationship(String userId, String guid) throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException {
        return subjectAreaRelationship.synonym().restore(userId, guid);
    }


    /**
     * Create a antonym relationship. A link between glossary terms that have the opposite meaning.
     *
     * <p>
     *
     * @param userId  userId under which the request is performed
     * @param antonym the Antonym relationship
     * @return the created antonym relationship
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid
     * @throws PropertyServerException              Property server exception
     */
    public Antonym createAntonymRelationship(String userId, Antonym antonym) throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException {
        return subjectAreaRelationship.antonym().create(userId, antonym);
    }

    /**
     * Get a antonym relationship. A link between glossary terms that have the opposite meaning.
     *
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the Anonym relationship to get
     * @return Antonym relationship
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid.
     * @throws PropertyServerException              Property server exception
     */
    public Antonym getAntonymRelationship(String userId, String guid) throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException {
        return subjectAreaRelationship.antonym().getByGUID(userId, guid);
    }

    /**
     * Update a Antonym relationship which is a link between glossary terms that have the opposite meaning
     * <p>
     *
     * @param userId  userId under which the request is performed
     * @param guid   guid of the Anonym relationship
     * @param antonym the Antonym relationship
     * @return Antonym updated antonym
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid
     * @throws PropertyServerException              Property server exception
     */
    public Antonym updateAntonymRelationship(String userId, String guid, Antonym antonym) throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException {
        return subjectAreaRelationship.antonym().update(userId, guid, antonym);
    }

    /**
     * Replace an Antonym relationship which is a link between glossary terms that have the opposite meaning
     * <p>
     *
     * @param userId  userId under which the request is performed
     * @param guid   guid of the Anonym relationship
     * @param antonym the antonym relationship
     * @return Antonym replaced antonym
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid
     * @throws PropertyServerException              Property server exception
     */
    public Antonym replaceAntonymRelationship(String userId, String guid, Antonym antonym) throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException {
        return subjectAreaRelationship.antonym().replace(userId, guid, antonym);
    }

    /**
     * Delete a antonym relationship. A link between glossary terms that have the opposite meaning.
     * A delete (also known as a soft delete) means that the relationship instance will exist in a deleted state in the repository after the delete operation. This means
     * that it is possible to undo the delete.
     *
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the Antonym relationship to delete
     * <p>
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid.
     * @throws PropertyServerException              Property server exception
     */
    public void deleteAntonymRelationship(String userId, String guid) throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException {
        subjectAreaRelationship.antonym().delete(userId, guid);
    }

    /**
     * Purge a antonym relationship. A link between glossary terms that have the opposite meaning.
     * A purge means that the relationship will not exist after the operation.
     *
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the Antonym relationship to delete
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid.
     * @throws PropertyServerException              Property server exception
     */
    public void purgeAntonymRelationship(String userId, String guid) throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException {
        subjectAreaRelationship.antonym().purge(userId, guid);
    }

    /**
     * Restore a Antonym relationship
     * <p>
     * Restore allows the deleted Antonym relationship to be made active again. Restore allows deletes to be undone. Hard deletes are not stored in the repository so cannot be restored.
     *
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the Antonym relationship to delete
     * @return the restored Antonym relationship
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid
     * @throws PropertyServerException              Property server exception
     */
    public Antonym restoreAntonymRelationship(String userId, String guid) throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException {
        return subjectAreaRelationship.antonym().restore(userId, guid);
    }

    /**
     * Create a Translation relationship, which is link between glossary terms that provide different natural language translation of the same concept.
     *
     * <p>
     *
     * @param userId      userId under which the request is performed
     * @param translation the Translation relationship
     * @return the created translation relationship
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid
     * @throws PropertyServerException              Property server exception
     */
    public Translation createTranslationRelationship(String userId, Translation translation) throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException {
        return subjectAreaRelationship.translation().create(userId, translation);
    }

    /**
     * Get a translation relationship, which is link between glossary terms that provide different natural language translation of the same concept.
     *
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the Translation relationship to get
     * @return Translation
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid.
     * @throws PropertyServerException              Property server exception
     */
    public Translation getTranslationRelationship(String userId, String guid) throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException {
        return subjectAreaRelationship.translation().getByGUID(userId, guid);
    }

    /**
     * Update a Translation relationship, which is link between glossary terms that provide different natural language translation of the same concept.
     * <p>
     *
     * @param userId      userId under which the request is performed
     * @param guid   guid of the Translation relationship
     * @param translation the Translation relationship
     * @return Translation updated translation
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid
     * @throws PropertyServerException              Property server exception
     */
    public Translation updateTranslationRelationship(String userId, String guid, Translation translation) throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException {
        return subjectAreaRelationship.translation().update(userId, guid, translation);
    }

    /**
     * Replace an Translation relationship, which is link between glossary terms that provide different natural language translation of the same concept.
     * <p>
     *
     * @param userId      userId under which the request is performed
     * @param guid   guid of the Translation relationship
     * @param translation the translation relationship
     * @return Translation replaced translation
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid
     * @throws PropertyServerException              Property server exception
     */
    public Translation replaceTranslationRelationship(String userId, String guid, Translation translation) throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException {
        return subjectAreaRelationship.translation().replace(userId, guid, translation);
    }

    /**
     * Delete a translation relationship, which is link between glossary terms that provide different natural language translation of the same concept.
     * A delete (also known as a soft delete) means that the relationship instance will exist in a deleted state in the repository after the delete operation. This means
     * that it is possible to undo the delete.
     *
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the Translation relationship to delete
     * <p>
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid.
     * @throws PropertyServerException              Property server exception
     */
    public void deleteTranslationRelationship(String userId, String guid) throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException {
        subjectAreaRelationship.translation().delete(userId, guid);
    }

    /**
     * Purge a translation relationship, which is link between glossary terms that provide different natural language translation of the same concept.
     * A purge means that the relationship will not exist after the operation.
     *
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the Translation relationship to delete
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid.
     * @throws PropertyServerException              Property server exception
     */
    public void purgeTranslationRelationship(String userId, String guid) throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException {
        subjectAreaRelationship.translation().purge(userId, guid);
    }

    /**
     * Restore a Translation relationship
     * <p>
     * Restore allows the deleted Translation relationship to be made active again. Restore allows deletes to be undone. Hard deletes are not stored in the repository so cannot be restored.
     *
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the Translation relationship to delete
     * @return the restored Translation relationship
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid
     * @throws PropertyServerException              Property server exception
     */
    public Translation restoreTranslationRelationship(String userId, String guid) throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException {
        return subjectAreaRelationship.translation().restore(userId, guid);
    }

    /**
     * Create a UsedInContext relationship, which is link between glossary terms where on describes the context where the other one is valid to use.
     *
     * <p>
     *
     * @param userId        userId under which the request is performed
     * @param usedInContext the UsedInContext relationship
     * @return the created usedInContext relationship
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid
     * @throws PropertyServerException              Property server exception
     */
    public UsedInContext createUsedInContextRelationship(String userId, UsedInContext usedInContext) throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException {
        return subjectAreaRelationship.usedInContext().create(userId, usedInContext);
    }

    /**
     * Get a usedInContext relationship, which is link between glossary terms where on describes the context where the other one is valid to use.
     *
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the UsedInContext relationship to get
     * @return UsedInContext
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid.
     * @throws PropertyServerException              Property server exception
     */
    public UsedInContext getUsedInContextRelationship(String userId, String guid) throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException {
        return subjectAreaRelationship.usedInContext().getByGUID(userId, guid);
    }

    /**
     * Update a UsedInContext relationship, which is link between glossary terms where on describes the context where the other one is valid to use.
     * <p>
     *
     * @param userId        userId under which the request is performed
     * @param guid   guid of the UsedInContext relationship
     * @param usedInContext the UsedInContext relationship
     * @return UsedInContext updated usedInContext
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid
     * @throws PropertyServerException              Property server exception
     */
    public UsedInContext updateUsedInContextRelationship(String userId, String guid, UsedInContext usedInContext) throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException {
        return subjectAreaRelationship.usedInContext().update(userId, guid, usedInContext);
    }

    /**
     * Replace an UsedInContext relationship, which is link between glossary terms where on describes the context where the other one is valid to use.
     * <p>
     *
     * @param userId        userId under which the request is performed
     * @param guid   guid of the UsedInContext relationship
     * @param usedInContext the usedInContext relationship
     * @return UsedInContext replaced usedInContext
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid
     * @throws PropertyServerException              Property server exception
     */
    public UsedInContext replaceUsedInContextRelationship(String userId, String guid, UsedInContext usedInContext) throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException {
        return subjectAreaRelationship.usedInContext().replace(userId, guid, usedInContext);
    }

    /**
     * Delete a usedInContext relationship, which is link between glossary terms where on describes the context where the other one is valid to use.
     * A delete (also known as a soft delete) means that the relationship instance will exist in a deleted state in the repository after the delete operation. This means
     * that it is possible to undo the delete.
     *
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the UsedInContext relationship to delete
     * <p>
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid.
     * @throws PropertyServerException              Property server exception
     */
    public void deleteUsedInContextRelationship(String userId, String guid) throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException {
        subjectAreaRelationship.usedInContext().delete(userId, guid);
    }

    /**
     * Purge a usedInContext relationship, which is link between glossary terms where on describes the context where the other one is valid to use.
     * A purge means that the relationship will not exist after the operation.
     *
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the UsedInContext relationship to delete
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid.
     * @throws PropertyServerException              Property server exception
     */
    public void purgeUsedInContextRelationship(String userId, String guid) throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException {
        subjectAreaRelationship.usedInContext().purge(userId, guid);
    }

    /**
     * Restore a Used in context relationship
     * <p>
     * Restore allows the deletedUsed in context relationship to be made active again. Restore allows deletes to be undone. Hard deletes are not stored in the repository so cannot be restored.
     *
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the Used in context relationship to delete
     * @return the restored Used in context relationship
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid
     * @throws PropertyServerException              Property server exception
     */
    public UsedInContext restoreUsedInContextRelationship(String userId, String guid) throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException {
        return subjectAreaRelationship.usedInContext().restore(userId, guid);
    }

    /**
     * Create a PreferredTerm relationship, which is link to an alternative term that the organization prefer is used.
     *
     * <p>
     *
     * @param userId        userId under which the request is performed
     * @param preferredTerm the PreferredTerm relationship
     * @return the created preferredTerm relationship
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid
     * @throws PropertyServerException              Property server exception
     */
    public PreferredTerm createPreferredTermRelationship(String userId, PreferredTerm preferredTerm) throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException {
        return subjectAreaRelationship.preferredTerm().create(userId, preferredTerm);
    }

    /**
     * Get a preferredTerm relationship, which is link to an alternative term that the organization prefer is used.
     *
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the PreferredTerm relationship to get
     * @return PreferredTerm
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid.
     * @throws PropertyServerException              Property server exception
     */
    public PreferredTerm getPreferredTermRelationship(String userId, String guid) throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException {
        return subjectAreaRelationship.preferredTerm().getByGUID(userId, guid);
    }

    /**
     * Update a PreferredTerm relationship, which is link to an alternative term that the organization prefer is used.
     * <p>
     *
     * @param userId        userId under which the request is performed
     * @param guid   guid of the PreferredTerm relationship
     * @param preferredTerm the PreferredTerm relationship
     * @return PreferredTerm updated preferredTerm
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid
     * @throws PropertyServerException              Property server exception
     */
    public PreferredTerm updatePreferredTermRelationship(String userId, String guid, PreferredTerm preferredTerm) throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException {
        return subjectAreaRelationship.preferredTerm().update(userId, guid, preferredTerm);
    }

    /**
     * Replace an PreferredTerm relationship, which is link to an alternative term that the organization prefer is used.
     * <p>
     *
     * @param userId        userId under which the request is performed
     * @param guid   guid of the PreferredTerm relationship
     * @param preferredTerm the preferredTerm relationship
     * @return PreferredTerm replaced preferredTerm
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid
     * @throws PropertyServerException              Property server exception
     */
    public PreferredTerm replacePreferredTermRelationship(String userId, String guid, PreferredTerm preferredTerm) throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException {
        return subjectAreaRelationship.preferredTerm().replace(userId, guid, preferredTerm);
    }

    /**
     * Delete a preferredTerm relationship, which is link to an alternative term that the organization prefer is used.
     * A delete (also known as a soft delete) means that the relationship instance will exist in a deleted state in the repository after the delete operation. This means
     * that it is possible to undo the delete.
     *
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the PreferredTerm relationship to delete
     * <p>
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid.
     * @throws PropertyServerException              Property server exception
     */
    public void deletePreferredTermRelationship(String userId, String guid) throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException {
        subjectAreaRelationship.preferredTerm().delete(userId, guid);
    }

    /**
     * Purge a preferredTerm relationship, which is link to an alternative term that the organization prefer is used.
     * A purge means that the relationship will not exist after the operation.
     *
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the PreferredTerm relationship to delete
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid.
     * @throws PropertyServerException              Property server exception
     */
    public void purgePreferredTermRelationship(String userId, String guid) throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException {
        subjectAreaRelationship.preferredTerm().purge(userId, guid);
    }

    /**
     * Restore a preferred term relationship
     * <p>
     * Restore allows the deletedpreferred term relationship to be made active again. Restore allows deletes to be undone. Hard deletes are not stored in the repository so cannot be restored.
     *
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the preferred term relationship to delete
     * @return the restored preferred term relationship
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid
     * @throws PropertyServerException              Property server exception
     */
    public PreferredTerm restorePreferredTermRelationship(String userId, String guid) throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException {
        return subjectAreaRelationship.preferredTerm().restore(userId, guid);
    }

    /**
     * Create a ValidValue relationship, which is link between glossary terms where one defines one of the data values for the another.
     *
     * <p>
     *
     * @param userId     userId under which the request is performed
     * @param validValue the ValidValue relationship
     * @return the created validValue relationship
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid
     * @throws PropertyServerException              Property server exception
     */
    public ValidValue createValidValueRelationship(String userId, ValidValue validValue) throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException {
        return subjectAreaRelationship.validValue().create(userId, validValue);
    }

    /**
     * Get a validValue relationship, which is link between glossary terms where one defines one of the data values for the another.
     *
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the ValidValue relationship to get
     * @return ValidValue
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid.
     * @throws PropertyServerException              Property server exception
     */
    public ValidValue getValidValueRelationship(String userId, String guid) throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException {
        return subjectAreaRelationship.validValue().getByGUID(userId, guid);
    }

    /**
     * Update a ValidValue relationship, which is link between glossary terms where one defines one of the data values for the another.
     * <p>
     *
     * @param userId     userId under which the request is performed
     * @param guid   guid of the ValidValue relationship
     * @param validValue the ValidValue relationship
     * @return ValidValue updated validValue
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid
     * @throws PropertyServerException              Property server exception
     */
    public ValidValue updateValidValueRelationship(String userId, String guid, ValidValue validValue) throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException {
        return subjectAreaRelationship.validValue().update(userId, guid, validValue);
    }

    /**
     * Replace an ValidValue relationship, which is link between glossary terms where one defines one of the data values for the another.
     * <p>
     *
     * @param userId     userId under which the request is performed
     * @param guid   guid of the ValidValue relationship
     * @param validValue the validValue relationship
     * @return ValidValue replaced validValue
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid
     * @throws PropertyServerException              Property server exception
     */
    public ValidValue replaceValidValueRelationship(String userId, String guid, ValidValue validValue) throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException {
        return subjectAreaRelationship.validValue().replace(userId, guid, validValue);
    }

    /**
     * Delete a validValue relationship, which is link between glossary terms where one defines one of the data values for the another.
     * A delete (also known as a soft delete) means that the relationship instance will exist in a deleted state in the repository after the delete operation. This means
     * that it is possible to undo the delete.
     *
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the ValidValue relationship to delete
     * <p>
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid.
     * @throws PropertyServerException              Property server exception
     */
    public void deleteValidValueRelationship(String userId, String guid) throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException {
        subjectAreaRelationship.validValue().delete(userId, guid);
    }

    /**
     * Purge a validValue relationship, which is link between glossary terms where one defines one of the data values for the another.
     * A purge means that the relationship will not exist after the operation.
     *
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the ValidValue relationship to delete
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid.
     * @throws PropertyServerException              Property server exception
     */
    public void purgeValidValueRelationship(String userId, String guid) throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException {
        subjectAreaRelationship.validValue().purge(userId, guid);
    }

    /**
     * Restore a valid value relationship
     * <p>
     * Restore allows the deletedvalid value relationship to be made active again. Restore allows deletes to be undone. Hard deletes are not stored in the repository so cannot be restored.
     *
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the valid value relationship to delete
     * @return the restored valid value relationship
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid
     * @throws PropertyServerException              Property server exception
     */
    public ValidValue restoreValidValueRelationship(String userId, String guid) throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException {
        return subjectAreaRelationship.validValue().restore(userId, guid);
    }

    /**
     * Create a ReplacementTerm relationship, which is link to a glossary term that is replacing an obsolete glossary term.
     *
     * <p>
     *
     * @param userId          userId under which the request is performed
     * @param replacementTerm the ReplacementTerm relationship
     * @return the created replacementTerm relationship
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid
     * @throws PropertyServerException              Property server exception
     */
    public ReplacementTerm createReplacementTermRelationship(String userId, ReplacementTerm replacementTerm) throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException {
        return subjectAreaRelationship.replacementTerm().create(userId, replacementTerm);
    }

    /**
     * Get a replacementTerm relationship, which is link to a glossary term that is replacing an obsolete glossary term.
     *
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the ReplacementTerm relationship to get
     * @return ReplacementTerm
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid.
     * @throws PropertyServerException              Property server exception
     */
    public ReplacementTerm getReplacementTermRelationship(String userId, String guid) throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException {
        return subjectAreaRelationship.replacementTerm().getByGUID(userId, guid);
    }

    /**
     * Update a ReplacementTerm relationship, which is link to a glossary term that is replacing an obsolete glossary term.
     * <p>
     *
     * @param userId          userId under which the request is performed
     * @param guid   guid of the ReplacementTerm relationship
     * @param replacementTerm the ReplacementTerm relationship
     * @return ReplacementTerm updated replacementTerm
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid
     * @throws PropertyServerException              Property server exception
     */
    public ReplacementTerm updateReplacementTermRelationship(String userId, String guid, ReplacementTerm replacementTerm) throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException {
        return subjectAreaRelationship.replacementTerm().update(userId, guid, replacementTerm);
    }

    /**
     * Replace an ReplacementTerm relationship, which is link to a glossary term that is replacing an obsolete glossary term.
     * <p>
     *
     * @param userId          userId under which the request is performed
     * @param guid   guid of the ReplacementTerm relationship
     * @param replacementTerm the replacementTerm relationship
     * @return ReplacementTerm replaced replacementTerm
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid
     * @throws PropertyServerException              Property server exception
     */
    public ReplacementTerm replaceReplacementTermRelationship(String userId, String guid, ReplacementTerm replacementTerm) throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException {
        return subjectAreaRelationship.replacementTerm().replace(userId, guid, replacementTerm);
    }

    /**
     * Delete a replacementTerm relationship, which is link to a glossary term that is replacing an obsolete glossary term.
     * A delete (also known as a soft delete) means that the relationship instance will exist in a deleted state in the repository after the delete operation. This means
     * that it is possible to undo the delete.
     *
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the ReplacementTerm relationship to delete
     * <p>
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid.
     * @throws PropertyServerException              Property server exception
     */
    public void deleteReplacementTermRelationship(String userId, String guid) throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException {
        subjectAreaRelationship.replacementTerm().delete(userId, guid);
    }

    /**
     * Purge a replacementTerm relationship, which is link to a glossary term that is replacing an obsolete glossary term.
     * A purge means that the relationship will not exist after the operation.
     *
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the ReplacementTerm relationship to delete
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid.
     * @throws PropertyServerException              Property server exception
     */
    public void purgeReplacementTermRelationship(String userId, String guid) throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException {
        subjectAreaRelationship.replacementTerm().purge(userId, guid);
    }

    /**
     * Restore a replacement term relationship
     * <p>
     * Restore allows the deleted replacement term relationship to be made active again. Restore allows deletes to be undone. Hard deletes are not stored in the repository so cannot be restored.
     *
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the replacement term relationship to delete
     * @return the restored replacement term relationship
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid
     * @throws PropertyServerException              Property server exception
     */
    public ReplacementTerm restoreReplacementTermRelationship(String userId, String guid) throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException {
        return subjectAreaRelationship.replacementTerm().restore(userId, guid);
    }

    /**
     * Create a TypedBy relationship, which is defines the relationship between a spine attribute and its type.
     *
     * <p>
     *
     * @param userId                  userId under which the request is performed
     * @param termTYPEDBYRelationship the TypedBy relationship
     * @return the created termTYPEDBYRelationship relationship
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid
     * @throws PropertyServerException              Property server exception
     */
    public TypedBy createTermTYPEDBYRelationship(String userId, TypedBy termTYPEDBYRelationship) throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException {
        return subjectAreaRelationship.typedBy().create(userId, termTYPEDBYRelationship);
    }

    /**
     * Get a termTYPEDBYRelationship relationship, which is defines the relationship between a spine attribute and its type.
     *
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the termTYPEDBYRelationship relationship to get
     * @return TypedBy
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid.
     * @throws PropertyServerException              Property server exception
     */
    public TypedBy getTermTYPEDBYRelationship(String userId, String guid) throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException {
        return subjectAreaRelationship.typedBy().getByGUID(userId, guid);
    }

    /**
     * Update a TypedBy relationship, which is defines the relationship between a spine attribute and its type.
     * <p>
     *
     * @param userId                  userId under which the request is performed
     * @param guid   guid of the TypedBy relationship
     * @param termTYPEDBYRelationship the TypedBy relationship
     * @return TypedBy updated termTYPEDBYRelationship
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid
     * @throws PropertyServerException              Property server exception
     */
    public TypedBy updateTermTYPEDBYRelationship(String userId, String guid, TypedBy termTYPEDBYRelationship) throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException {
        return subjectAreaRelationship.typedBy().update(userId, guid, termTYPEDBYRelationship);
    }

    /**
     * Replace an TypedBy relationship, which is defines the relationship between a spine attribute and its type.
     * <p>
     *
     * @param userId                  userId under which the request is performed
     * @param guid   guid of the TypedBy relationship
     * @param termTYPEDBYRelationship the termTYPEDBYRelationship relationship
     * @return TypedBy replaced termTYPEDBYRelationship
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid
     * @throws PropertyServerException              Property server exception
     */
    public TypedBy replaceTermTYPEDBYRelationship(String userId, String guid, TypedBy termTYPEDBYRelationship) throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException {
        return subjectAreaRelationship.typedBy().replace(userId, guid, termTYPEDBYRelationship);
    }

    /**
     * Delete a termTYPEDBYRelationship relationship, which is defines the relationship between a spine attribute and its type.
     * A delete (also known as a soft delete) means that the relationship instance will exist in a deleted state in the repository after the delete operation. This means
     * that it is possible to undo the delete.
     *
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the termTYPEDBYRelationship relationship to delete
     * <p>
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid.
     * @throws PropertyServerException              Property server exception
     */
    public void deleteTermTYPEDBYRelationship(String userId, String guid) throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException {
        subjectAreaRelationship.typedBy().delete(userId, guid);
    }

    /**
     * Purge a termTYPEDBYRelationship relationship, which is defines the relationship between a spine attribute and its type.
     * A purge means that the relationship will not exist after the operation.
     *
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the TypedBy relationship to delete
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid.
     * @throws PropertyServerException              Property server exception
     */
    public void purgeTermTYPEDBYRelationship(String userId, String guid) throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException {
        subjectAreaRelationship.typedBy().purge(userId, guid);
    }

    /**
     * Restore a typed by relationship
     * <p>
     * Restore allows the deleted typed by relationship to be made active again. Restore allows deletes to be undone. Hard deletes are not stored in the repository so cannot be restored.
     *
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the typed by relationship to delete
     * @return the restored typed by relationship
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid
     * @throws PropertyServerException              Property server exception
     */
    public TypedBy restoreTypedByRelationship(String userId, String guid) throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException {
        return subjectAreaRelationship.typedBy().restore(userId, guid);
    }

    /**
     * Create a Isa relationship, which is link between a more general glossary term and a more specific definition.
     *
     * <p>
     *
     * @param userId userId under which the request is performed
     * @param isa    the Isa relationship
     * @return the created isa relationship
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid
     * @throws PropertyServerException              Property server exception
     */
    public Isa createIsaRelationship(String userId, Isa isa) throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException {
        return subjectAreaRelationship.isa().create(userId, isa);
    }

    /**
     * Get a isa relationship, which is link between a more general glossary term and a more specific definition.
     *
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the isa relationship to get
     * @return Isa
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid.
     * @throws PropertyServerException              Property server exception
     */
    public Isa getIsaRelationship(String userId, String guid) throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException {
        return subjectAreaRelationship.isa().getByGUID(userId, guid);
    }

    /**
     * Update a Isa relationship, which is link between a more general glossary term and a more specific definition.
     * <p>
     *
     * @param userId userId under which the request is performed
     * @param guid   guid of the isa relationship
     * @param isa    the Isa relationship
     * @return Isa updated isa
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid
     * @throws PropertyServerException              Property server exception
     */
    public Isa updateIsaRelationship(String userId, String guid, Isa isa) throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException {
        return subjectAreaRelationship.isa().update(userId, guid, isa);
    }

    /**
     * Replace an Isa relationship, which is link between a more general glossary term and a more specific definition.
     * <p>
     *
     * @param userId userId under which the request is performed
     * @param guid   guid of the isa relationship
     * @param isa    the isa relationship
     * @return Isa replaced isa
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid
     * @throws PropertyServerException              Property server exception
     */
    public Isa replaceIsaRelationship(String userId, String guid, Isa isa) throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException {
        return subjectAreaRelationship.isa().replace(userId, guid, isa);
    }

    /**
     * Delete a isa relationship, which is link between a more general glossary term and a more specific definition.
     * A delete (also known as a soft delete) means that the relationship instance will exist in a deleted state in the repository after the delete operation. This means
     * that it is possible to undo the delete.
     *
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the isa relationship to delete
     * <p>
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid.
     * @throws PropertyServerException              Property server exception
     */
    public void deleteIsaRelationship(String userId, String guid) throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException {
        subjectAreaRelationship.isa().delete(userId, guid);
    }

    /**
     * Purge a isa relationship, which is link between a more general glossary term and a more specific definition.
     * A purge means that the relationship will not exist after the operation.
     *
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the Isa relationship to delete
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid.
     * @throws PropertyServerException              Property server exception
     */
    public void purgeIsaRelationship(String userId, String guid) throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException {
        subjectAreaRelationship.isa().purge(userId, guid);
    }

    /**
     * Restore an is a relationship
     * <p>
     * Restore allows the deleted is a relationship to be made active again. Restore allows deletes to be undone. Hard deletes are not stored in the repository so cannot be restored.
     *
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the is a relationship to delete
     * @return the restored is a relationship
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid
     * @throws PropertyServerException              Property server exception
     */
    public Isa restoreIsaRelationship(String userId, String guid) throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException {
        return subjectAreaRelationship.isa().restore(userId, guid);
    }

    /**
     * Create a IsaTypeOf relationship, which is defines an inheritance relationship between two spine objects.
     *
     * <p>
     *
     * @param userId    userId under which the request is performed
     * @param isATypeOf the IsaTypeOf relationship
     * @return the created IsaTypeOf relationship
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid
     * @throws PropertyServerException              Property server exception
     */
    public IsaTypeOf createTermISATypeOFRelationship(String userId, IsaTypeOf isATypeOf) throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException {
        return subjectAreaRelationship.isaTypeOf().create(userId, isATypeOf);
    }

    /**
     * Get a IsaTypeOf relationship, which is defines an inheritance relationship between two spine objects.
     *
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the IsaTypeOf relationship to get
     * @return IsaTypeOf
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid.
     * @throws PropertyServerException              Property server exception
     */
    public IsaTypeOf getTermISATypeOFRelationship(String userId, String guid) throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException {
        return subjectAreaRelationship.isaTypeOf().getByGUID(userId, guid);
    }

    /**
     * Update a IsaTypeOf relationship, which is defines an inheritance relationship between two spine objects.
     * <p>
     *
     * @param userId    userId under which the request is performed
     * @param guid   guid of the IsaTypeOf relationship
     * @param isATypeOf the IsaTypeOf relationship
     * @return IsaTypeOf updated IsaTypeOf
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid
     * @throws PropertyServerException              Property server exception
     */
    public IsaTypeOf updateTermISATypeOFRelationship(String userId, String guid, IsaTypeOf isATypeOf) throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException {
        return subjectAreaRelationship.isaTypeOf().update(userId, guid, isATypeOf);
    }

    /**
     * Replace an IsaTypeOf relationship, which is defines an inheritance relationship between two spine objects.
     * <p>
     *
     * @param userId    userId under which the request is performed
     * @param guid   guid of the IsaTypeOf relationship
     * @param isATypeOf the IsaTypeOf relationship
     * @return IsaTypeOf replaced IsaTypeOf
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid
     * @throws PropertyServerException              Property server exception
     */
    public IsaTypeOf replaceTermISATypeOFRelationship(String userId, String guid, IsaTypeOf isATypeOf) throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException {
        return subjectAreaRelationship.isaTypeOf().replace(userId, guid, isATypeOf);
    }

    /**
     * Delete a IsaTypeOf relationship, which is defines an inheritance relationship between two spine objects.
     * A delete (also known as a soft delete) means that the relationship instance will exist in a deleted state in the repository after the delete operation. This means
     * that it is possible to undo the delete.
     *
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the IsaTypeOf relationship to delete
     * <p>
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid.
     * @throws PropertyServerException              Property server exception
     */
    public void deleteTermISATypeOFRelationship(String userId, String guid) throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException {
        subjectAreaRelationship.isaTypeOf().delete(userId, guid);
    }

    /**
     * Purge a IsaTypeOf relationship, which is defines an inheritance relationship between two spine objects.
     * A purge means that the relationship will not exist after the operation.
     *
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the IsaTypeOf relationship to delete
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid.
     * @throws PropertyServerException              Property server exception
     */
    public void purgeTermISATypeOFRelationship(String userId, String guid) throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException {
        subjectAreaRelationship.isaTypeOf().purge(userId, guid);
    }

    /**
     * Restore an is a type of relationship
     * <p>
     * Restore allows the deleted is a type of relationship to be made active again. Restore allows deletes to be undone. Hard deletes are not stored in the repository so cannot be restored.
     *
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the is a type of relationship to delete
     * @return the restored is a type of relationship
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid
     * @throws PropertyServerException              Property server exception
     */
    public IsaTypeOf restoreIsaTypeOfRelationship(String userId, String guid) throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException {
        return subjectAreaRelationship.isaTypeOf().restore(userId, guid);
    }

    /**
     * Create a Term Categorization Relationship. A relationship between a Category and a Term. This relationship allows terms to be categorized.
     * Note that this method does not error if the relationship ends are not spine objects or spine attributes.
     * <p>
     *
     * @param userId                         userId under which the request is performed
     * @param termCategorizationRelationship the term categorization relationship
     * @return the created term categorization relationship
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid
     * @throws PropertyServerException              Property server exception
     */
    public Categorization createTermCategorizationRelationship(String userId, Categorization termCategorizationRelationship) throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException {
        return subjectAreaRelationship.termCategorization().create(userId, termCategorizationRelationship);
    }

    /**
     * Get a Term Categorization Relationship. A relationship between a Category and a Term. This relationship allows terms to be categorized.
     *
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the TermCategorizationRelationship relationship to get
     * @return TermCategorizationRelationship
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid.
     * @throws PropertyServerException              Property server exception
     */
    public Categorization getTermCategorizationRelationship(String userId, String guid) throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException {
        return subjectAreaRelationship.termCategorization().getByGUID(userId, guid);
    }

    /**
     * Update a Term Categorization Relationship. A relationship between a Category and a Term. This relationship allows terms to be categorized.
     * <p>
     * @param userId                         userId under which the request is performed
     * @param guid   guid of the TermCategorizationRelationship
     * @param termCategorizationRelationship the term categorization relationship
     * @return the updated term categorization relationship
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid
     * @throws PropertyServerException              Property server exception
     */
    public Categorization updateTermCategorizationRelationship(String userId, String guid, Categorization termCategorizationRelationship) throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException {
        return subjectAreaRelationship.termCategorization().update(userId, guid, termCategorizationRelationship);
    }

    /**
     * Replace a Term HASA Relationship.
     * <p>
     *
     * @param userId                         userId under which the request is performed
     * @param guid   guid of the TermCategorizationRelationship
     * @param termCategorizationRelationship the term categorization relationship
     * @return the replaced term categorization relationship
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid
     * @throws PropertyServerException              Property server exception
     */
    public Categorization replaceTermCategorizationRelationship(String userId, String guid, Categorization termCategorizationRelationship) throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException {
        return subjectAreaRelationship.termCategorization().replace(userId, guid, termCategorizationRelationship);
    }

    /**
     * Delete a Term Categorization Relationship. A relationship between a Category and a Term. This relationship allows terms to be categorized.      * A delete (also known as a soft delete) means that the relationship instance will exist in a deleted state in the repository after the delete operation. This means
     * that it is possible to undo the delete.
     *
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the TermCategorizationRelationship relationship to delete
     * <p>
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid.
     * @throws PropertyServerException              Property server exception
     */
    public void deleteTermCategorizationRelationship(String userId, String guid) throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException {
         subjectAreaRelationship.termCategorization().delete(userId, guid);
    }

    /**
     * Purge a Term Categorization Relationship. A relationship between a Category and a Term. This relationship allows terms to be categorized.
     * A purge means that the relationship will not exist after the operation.
     *
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the TermCategorizationRelationship relationship to delete
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid.
     * @throws PropertyServerException              Property server exception
     */
    public void purgeTermCategorizationRelationship(String userId, String guid) throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException {
        subjectAreaRelationship.termCategorization().purge(userId, guid);
    }

    /**
     * Restore a Term Categorization Relationship. A relationship between a Category and a Term. This relationship allows terms to be categorized.
     * <p>
     * Restore allows the deleted Term Categorization relationship to be made active again. Restore allows deletes to be undone. Hard deletes are not stored in the repository so cannot be restored.
     *
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the Term Categorization relationship to delete
     * @return the restored has a relationship
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid
     * @throws PropertyServerException              Property server exception
     */
    public Categorization restoreTermCategorizationRelationship(String userId, String guid) throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException {
        return subjectAreaRelationship.termCategorization().restore(userId, guid);
    }

    /**
     * Create a Term Anchor Relationship. A relationship between a Glossary and a Term. This relationship allows terms to be owned by a glossary.
     * This method does not error if the relationship ends are not spine objects or spine attributes.
     * Terms created using the Glossary author OMVS cannot be created without a glossary and there can only be one glossary associated with a
     * Term. This method is to allow glossaries to be associated with Terms that have not been created via the Glossary Author OMVS or Subject Area OMAS or to recreate
     * the TermAnchor relationship if it has been purged.
     * <p>
     *
     * @param userId                 userId under which the request is performed
     * @param termAnchorRelationship the TermAnchor relationship
     * @return the created TermAnchor relationship
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid
     * @throws PropertyServerException              Property server exception
     */
    public TermAnchor createTermAnchorRelationship(String userId, TermAnchor termAnchorRelationship) throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException {
        return subjectAreaRelationship.termAnchor().create(userId, termAnchorRelationship);
    }

    /**
     * Get a Term Anchor Relationship. A relationship between a Glossary and a Term. This relationship allows terms to be owned by a glossary.
     *
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the TermAnchorRelationship relationship to get
     * @return TermAnchorRelationship
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid.
     * @throws PropertyServerException              Property server exception
     */
    public TermAnchor getTermAnchorRelationship(String userId, String guid) throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException {
        return subjectAreaRelationship.termAnchor().getByGUID(userId, guid);
    }

    /**
     * Update a Term Anchor Relationship.
     * <p>
     *
     * @param userId                 userId under which the request is performed
     * @param guid   guid of the TermAnchorRelationship relationship
     * @param termAnchorRelationship the TermAnchor relationship
     * @return the updated TermAnchor relationship
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid
     * @throws PropertyServerException              Property server exception
     */
    public TermAnchor updateTermAnchorRelationship(String userId, String guid, TermAnchor termAnchorRelationship) throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException {
        return subjectAreaRelationship.termAnchor().replace(userId, guid, termAnchorRelationship);
    }

    /**
     * Replace a Term Anchor Relationship.
     * <p>
     *
     * @param userId                 userId under which the request is performed
     * @param guid   guid of the TermAnchorRelationship relationship
     * @param termAnchorRelationship the TermAnchor relationship
     * @return the updated TermAnchor relationship
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid
     * @throws PropertyServerException              Property server exception
     */
    public TermAnchor replaceTermAnchorRelationship(String userId, String guid, TermAnchor termAnchorRelationship) throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException {
        return subjectAreaRelationship.termAnchor().replace(userId, guid, termAnchorRelationship);
    }

    /**
     * Delete a Term Anchor Relationship. A relationship between a Glossary and a Term. This relationship allows terms to be owned by a glossary.     * A delete (also known as a soft delete) means that the relationship instance will exist in a deleted state in the repository after the delete operation. This means
     * that it is possible to undo the delete.
     *
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the TermAnchorRelationship relationship to delete
     * <p>
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid.
     * @throws PropertyServerException              Property server exception
     */
    public void deleteTermAnchorRelationship(String userId, String guid) throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException {
        subjectAreaRelationship.termAnchor().delete(userId, guid);
    }

    /**
     * Purge a Term Anchor Relationship. A relationship between a Glossary and a Term. This relationship allows terms to be owned by a glossary.
     * A purge means that the relationship will not exist after the operation.
     *
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the TermAnchorRelationship relationship to delete
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid.
     * @throws PropertyServerException              Property server exception
     */
    public void purgeTermAnchorRelationship(String userId, String guid) throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException {
        subjectAreaRelationship.termAnchor().purge(userId, guid);
    }

    /**
     * Restore a Term Anchor Relationship. A relationship between a Glossary and a Term. This relationship allows terms to be owned by a glossary.
     * <p>
     * Restore allows the deleted Term Categorization relationship to be made active again. Restore allows deletes to be undone. Hard deletes are not stored in the repository so cannot be restored.
     *
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the Term Anchor relationship to delete
     * @return the restored Term Anchor relationship
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid
     * @throws PropertyServerException              Property server exception
     */
    public TermAnchor restoreTermAnchorRelationship(String userId, String guid) throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException {
        return subjectAreaRelationship.termAnchor().restore(userId, guid);
    }

    /**
     * Create a Category Anchor Relationship. A relationship between a Glossary and a Category. This relationship allows categoriess to be owned by a glossary.
     * Categories created using the Subject Area OMAS cannot be created without a glossary and there can only be one glossary associated with a
     * Category. This method is to allow glossaries to be associated with Categories that have not been created via the Subject Area OMAS or to recreate
     * the CategoryAnchor relationship if it has been purged.
     *
     * @param userId                     userId under which the request is performed
     * @param categoryAnchorRelationship the category anchor relationship
     * @return the created term categorization relationship
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid
     * @throws PropertyServerException              Property server exception
     */
    public CategoryAnchor createCategoryAnchorRelationship(String userId, CategoryAnchor categoryAnchorRelationship) throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException {
        return subjectAreaRelationship.categoryAnchor().create(userId, categoryAnchorRelationship);
    }

    /**
     * Get a Category Anchor Relationship. A relationship between a Glossary and a Category. This relationship allows categoriess to be owned by a glossary.
     *
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the CategoryAnchorRelationship relationship to get
     * @return CategoryAnchorRelationship
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid.
     * @throws PropertyServerException              Property server exception
     */
    public CategoryAnchor getCategoryAnchorRelationship(String userId, String guid) throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException {
        return subjectAreaRelationship.categoryAnchor().getByGUID(userId, guid);
    }

    /**
     * Update a Category Anchor Relationship.
     * <p>
     *
     * @param userId  userId under which the request is performed
     * @param guid   guid of the CategoryAnchorRelationship
     * @param categoryAnchorRelationship the category anchor relationship
     * @return the updated category anchor relationship
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid
     * @throws PropertyServerException              Property server exception
     */
    public CategoryAnchor updateCategoryAnchorRelationship(String userId, String guid, CategoryAnchor categoryAnchorRelationship) throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException {
        return subjectAreaRelationship.categoryAnchor().update(userId, guid, categoryAnchorRelationship);
    }

    /**
     * Replace a Category Anchor Relationship.
     * <p>
     *
     * @param userId                     userId under which the request is performed
     * @param guid   guid of the CategoryAnchorRelationship
     * @param categoryAnchorRelationship the category anchor relationship
     * @return the replaced category anchor relationship
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid
     * @throws PropertyServerException              Property server exception
     */
    public CategoryAnchor replaceCategoryAnchorRelationship(String userId, String guid, CategoryAnchor categoryAnchorRelationship) throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException {
        return subjectAreaRelationship.categoryAnchor().replace(userId, guid, categoryAnchorRelationship);
    }

    /**
     * Delete a Category Anchor Relationship. A relationship between a Glossary and a Category. This relationship allows categoriess to be owned by a glossary.     * A delete (also known as a soft delete) means that the relationship instance will exist in a deleted state in the repository after the delete operation. This means
     * that it is possible to undo the delete.
     *
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the CategoryAnchorRelationship relationship to delete
     * <p>
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid.
     * @throws PropertyServerException              Property server exception
     */
    public void deleteCategoryAnchorRelationship(String userId, String guid) throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException {
        subjectAreaRelationship.categoryAnchor().delete(userId, guid);
    }

    /**
     * Purge a Category Anchor Relationship. A relationship between a Glossary and a Category. This relationship allows categoriess to be owned by a glossary.
     * A purge means that the relationship will not exist after the operation.
     *
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the CategoryAnchorRelationship relationship to delete
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid.
     * @throws PropertyServerException              Property server exception
     */
    public void purgeCategoryAnchorRelationship(String userId, String guid) throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException {
        subjectAreaRelationship.categoryAnchor().purge(userId, guid);
    }

    /**
     * Restore a Category Anchor Relationship. A relationship between a Glossary and a Category. This relationship allows categoriess to be owned by a glossary.
     * <p>
     * Restore allows the deleted Category Anchor relationship to be made active again. Restore allows deletes to be undone. Hard deletes are not stored in the repository so cannot be restored.
     *
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the Category Anchor relationship to delete
     * @return the restored category anchor relationship
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid
     * @throws PropertyServerException              Property server exception
     */
    public CategoryAnchor restoreCategoryAnchorRelationship(String userId, String guid) throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException {
        return subjectAreaRelationship.categoryAnchor().restore(userId, guid);
    }

    /**
     * Create a ProjectScope relationship. A link between the project content and the project.
     * <p>
     *
     * @param userId       userId under which the request is performed
     * @param projectScope the ProjectScope relationship
     * @return the created ProjectScope relationship
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid
     * @throws PropertyServerException              Property server exception
     */
    public ProjectScope createProjectScopeRelationship(String userId, ProjectScope projectScope) throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException {
        return subjectAreaRelationship.projectScope().create(userId, projectScope);
    }

    /**
     * Get a ProjectScope relationship. A link between the project content and the project.
     *
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the ProjectScope relationship to get
     * @return ProjectScope
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid.
     * @throws PropertyServerException              Property server exception
     */
    public ProjectScope getProjectScopeRelationship(String userId, String guid) throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException {
        return subjectAreaRelationship.projectScope().getByGUID(userId, guid);
    }

    /**
     * Update a ProjectScope relationship which is a link between the project content and the project.
     * <p>
     *
     * @param userId                   userId under which the request is performed
     * @param guid   guid of the ProjectScope relationship
     * @param projectScopeRelationship the ProjectScope relationship
     * @return updated ProjectScope relationship
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid

     * @throws PropertyServerException              Property server exception
     */
    public ProjectScope updateProjectScopeRelationship(String userId, String guid, ProjectScope projectScopeRelationship) throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException {
        return subjectAreaRelationship.projectScope().update(userId, guid, projectScopeRelationship);
    }

    /**
     * Replace a ProjectScope relationship which is a link between the project content and the project.
     * <p>
     *
     * @param userId                   userId under which the request is performed
     * @param guid   guid of the ProjectScope relationship
     * @param projectScopeRelationship the ProjectScope relationship
     * @return replaced ProjectScope relationship
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid
     * @throws PropertyServerException              Property server exception
     */
    public ProjectScope replaceProjectScopeRelationship(String userId, String guid, ProjectScope projectScopeRelationship) throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException {
        return subjectAreaRelationship.projectScope().replace(userId, guid, projectScopeRelationship);
    }

    /**
     * Delete a ProjectScope relationship. A link between the project content and the project.
     * A delete (also known as a soft delete) means that the relationship instance will exist in a deleted state in the repository after the delete operation. This means
     * that it is possible to undo the delete.
     *
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the ProjectScope relationship to delete
     * <p>
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid.
     * @throws PropertyServerException              Property server exception
     */
    public void deleteProjectScopeRelationship(String userId, String guid) throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException {
        subjectAreaRelationship.projectScope().delete(userId, guid);
    }


    /**
     * Purge a ProjectScope relationship. A link between the project content and the project.
     * A purge means that the relationship will not exist after the operation.
     *
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the ProjectScope relationship to delete
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid.
     * @throws PropertyServerException              Property server exception
     */
    public void purgeProjectScopeRelationship(String userId, String guid) throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException {
        subjectAreaRelationship.projectScope().purge(userId, guid);
    }

    /**
     * Restore a ProjectScope relationship which  is a link between the project content and the project.
     * <p>
     * Restore allows the deleted ProjectScope relationship to be made active again. Restore allows deletes to be undone. Hard deletes are not stored in the repository so cannot be restored.
     *
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the ProjectScope relationship to restore
     * @return the restored ProjectScope relationship
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid
     * @throws PropertyServerException              Property server exception
     */
    public ProjectScope restoreProjectScopeRelationship(String userId, String guid) throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException {
        return subjectAreaRelationship.projectScope().restore(userId, guid);
    }


    /**
     * Get a SemanticAssignment relationship,  Links a glossary term to another element such as an asset or schema element to define its meaning.
     *
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the SemanticAssignment relationship to get
     * @return the SemanticAssignment relationship with the requested guid
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid
     * @throws PropertyServerException              Property server exception
     */
    public SemanticAssignment getSemanticAssignmentRelationship(String userId, String guid) throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException {
        return subjectAreaRelationship.semanticAssignment().getByGUID(userId, guid);
    }
}