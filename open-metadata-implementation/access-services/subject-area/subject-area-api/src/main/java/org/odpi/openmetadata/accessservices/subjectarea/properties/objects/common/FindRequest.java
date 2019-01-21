/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.properties.objects.common;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Date;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * FindRequest is used by the Subject Area OMAS to specify paging information for find calls.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class FindRequest
{
    private String               sequencingProperty   = null;
    private SequencingOrder      sequencingOrder      = null;
    private int                  offset               = 0;
    private int                  pageSize             = 0;
    private Date                 asOfTime;
    /**
     * Default constructor
     */
    public FindRequest()
    {
    }

    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public FindRequest(FindRequest template)
    {
        if (template != null)
        {
            this.sequencingProperty = template.getSequencingProperty();
            this.sequencingOrder = template.getSequencingOrder();
            this.offset = template.getOffset();
            this.pageSize = getPageSize();
        }
    }


    /**
     * Return the name of the property that should be used to sequence the results.
     *
     * @return property name
     */
    public String getSequencingProperty()
    {
        return sequencingProperty;
    }


    /**
     * Set up the name of the property that should be used to sequence the results.
     *
     * @param sequencingProperty property name
     */
    public void setSequencingProperty(String sequencingProperty)
    {
        this.sequencingProperty = sequencingProperty;
    }


    /**
     * Return the sequencing order for the results.
     *
     * @return sequencing order enum
     */
    public SequencingOrder getSequencingOrder()
    {
        return sequencingOrder;
    }


    /**
     * Set up the sequencing order for the results.
     *
     * @param sequencingOrder sequencing order enum
     */
    public void setSequencingOrder(SequencingOrder sequencingOrder)
    {
        this.sequencingOrder = sequencingOrder;
    }


    /**
     * Return the starting element number for this set of results.  This is used when retrieving elements
     * beyond the first page of results. Zero means the results start from the first element.
     *
     * @return offset number
     */
    public int getOffset()
    {
        return offset;
    }


    /**
     * Set up the starting element number for this set of results.  This is used when retrieving elements
     * beyond the first page of results. Zero means the results start from the first element.
     *
     * @param offset offset number
     */
    public void setOffset(int offset)
    {
        this.offset = offset;
    }


    /**
     * Return the maximum number of elements that can be returned on this request.
     *0 means there is not limit to the page size
     * @return page size
     */
    public int getPageSize()
    {
        return pageSize;
    }


    /**
     * Set up the maximum number of elements that can be returned on this request.
     *
     * @param pageSize integer number
     */
    public void setPageSize(int pageSize)
    {
        this.pageSize = pageSize;
    }

    /**
     * Get the time at which the find should be issued.
     * @return Date at which the find should be issued
     */
    public Date getAsOfTime() {
        return asOfTime;
    }

    /**
     * set up the time at which the find should be made. null means current time.
     * @param asOfTime Date at which the find should be issued
     */
    public void setAsOfTime(Date asOfTime) {
        this.asOfTime = asOfTime;
    }

    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "OMRSAPIPagedFindRequest{" +
                "sequencingProperty='" + sequencingProperty + '\'' +
                ", sequencingOrder=" + sequencingOrder +
                ", offset=" + offset +
                ", pageSize=" + pageSize +
                '}';
    }


    /**
     * Compare the values of the supplied object with those stored in the current object.
     *
     * @param objectToCompare supplied object
     * @return boolean result of comparison
     */
    @Override
    public boolean equals(Object objectToCompare)
    {
        if (this == objectToCompare)
        {
            return true;
        }
        if (!(objectToCompare instanceof FindRequest))
        {
            return false;
        }
        if (!super.equals(objectToCompare))
        {
            return false;
        }
       FindRequest that = (FindRequest) objectToCompare;
        return getOffset() == that.getOffset() &&
                getPageSize() == that.getPageSize() &&
                Objects.equals(getSequencingProperty(), that.getSequencingProperty()) &&
                getSequencingOrder() == that.getSequencingOrder();
    }


    /**
     * Create a hash code for this element type.
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {

        return Objects.hash(super.hashCode(),
                getSequencingProperty(),
                getSequencingOrder(),
                getOffset(),
                getPageSize());
    }
}
