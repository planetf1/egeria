/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetconsumer.rest;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.InformalTag;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 *  TagListResponse returns a list of tags from the server.   The list may be too long to
 *  retrieve in a single call so there is support for paging of replies.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class TagListResponse extends AssetConsumerOMASAPIResponse
{
    private List<InformalTag> tags                = null;
    private int               startingFromElement = 0;


    /**
     * Default constructor
     */
    public TagListResponse()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public TagListResponse(TagListResponse template)
    {
        super(template);

        if (template != null)
        {
            this.startingFromElement = template.getStartingFromElement();
            this.tags = template.getTags();
        }
    }


    /**
     * Return the list of glossary terms in the response.
     *
     * @return list of glossary terms
     */
    public List<InformalTag> getTags()
    {
        if (tags == null)
        {
            return null;
        }
        else if (tags.isEmpty())
        {
            return null;
        }
        else
        {
            List<InformalTag>  clonedList = new ArrayList<>();

            for (InformalTag  existingElement : tags)
            {
                clonedList.add(new InformalTag(existingElement));
            }

            return clonedList;
        }
    }


    /**
     * Set up the list of glossary terms for the response.
     *
     * @param tags list
     */
    public void setTags(List<InformalTag> tags)
    {
        this.tags = tags;
    }


    /**
     * Return the starting element number from the server side list that this response contains.
     *
     * @return int
     */
    public int getStartingFromElement()
    {
        return startingFromElement;
    }


    /**
     * Set up the starting element number from the server side list that this response contains.
     *
     * @param startingFromElement int
     */
    public void setStartingFromElement(int startingFromElement)
    {
        this.startingFromElement = startingFromElement;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "TagListResponse{" +
                "tags=" + tags +
                ", startingFromElement=" + startingFromElement +
                ", relatedHTTPCode=" + getRelatedHTTPCode() +
                ", exceptionClassName='" + getExceptionClassName() + '\'' +
                ", exceptionErrorMessage='" + getExceptionErrorMessage() + '\'' +
                ", exceptionSystemAction='" + getExceptionSystemAction() + '\'' +
                ", exceptionUserAction='" + getExceptionUserAction() + '\'' +
                ", exceptionProperties=" + getExceptionProperties() +
                '}';
    }


    /**
     * Return comparison result based on the content of the properties.
     *
     * @param objectToCompare test object
     * @return result of comparison
     */
    @Override
    public boolean equals(Object objectToCompare)
    {
        if (this == objectToCompare)
        {
            return true;
        }
        if (objectToCompare == null || getClass() != objectToCompare.getClass())
        {
            return false;
        }
        if (!super.equals(objectToCompare))
        {
            return false;
        }
        TagListResponse that = (TagListResponse) objectToCompare;
        return getStartingFromElement() == that.getStartingFromElement() &&
                Objects.equals(getTags(), that.getTags());
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), getTags(), getStartingFromElement());
    }
}
