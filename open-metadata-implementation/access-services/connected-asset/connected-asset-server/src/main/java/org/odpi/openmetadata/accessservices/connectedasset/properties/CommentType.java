/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.accessservices.connectedasset.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * The CommentType allows comments to be used to ask and answer questions as well as make suggestions and
 * provide useful information to other users.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public enum CommentType implements Serializable
{
    STANDARD_COMMENT(0, "Comment", "General comment about the asset."),
    QUESTION(1, "Question", "Asks a question to the people owning, managing or using the asset."),
    ANSWER(2, "Answer", "Answers a question (posted as a reply to the question)."),
    SUGGESTION(3, "Suggestion", "Provides a suggestion on how to improve the asset or its properties and description."),
    USAGE_EXPERIENCE(4, "Experience", "Describes situations where this asset has been used and related hints and tips.");

    private static final long     serialVersionUID = 1L;

    private int            commentTypeCode;
    private String         commentType;
    private String         commentTypeDescription;


    /**
     * Typical Constructor
     */
    CommentType(int     commentTypeCode, String   commentType, String   commentTypeDescription)
    {
        /*
         * Save the values supplied
         */
        this.commentTypeCode = commentTypeCode;
        this.commentType = commentType;
        this.commentTypeDescription = commentTypeDescription;
    }


    /**
     * Return the code for this enum instance
     *
     * @return int - comment type code
     */
    public int getCommentTypeCode()
    {
        return commentTypeCode;
    }


    /**
     * Return the default type name for this enum instance.
     *
     * @return String - default type name
     */
    public String getCommentType()
    {
        return commentType;
    }


    /**
     * Return the default description for the star rating for this enum instance.
     *
     * @return String - default description
     */
    public String getCommentTypeDescription()
    {
        return commentTypeDescription;
    }
}