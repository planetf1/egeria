/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.repositoryservices.connectors.stores.auditlogstore;


import org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.PagingErrorException;

import java.util.Date;
import java.util.List;

/**
 * OMRSAuditLogStore is the specialized data API for an Audit Log connector.
 */
public interface OMRSAuditLogStore
{
    /**
     * Store the audit log record in the audit log store.
     *
     * @param logRecord  log record to store
     * @return unique identifier assigned to the log record
     * @throws InvalidParameterException indicates that the logRecord parameter is invalid.
     */
    String storeLogRecord(OMRSAuditLogRecord logRecord) throws InvalidParameterException;


    /**
     * Retrieve a specific audit log record.
     *
     * @param logRecordId  unique identifier for the log record
     * @return requested audit log record
     * @throws InvalidParameterException indicates that the logRecordId parameter is invalid.
     */
    OMRSAuditLogRecord  getAuditLogRecord(String     logRecordId) throws InvalidParameterException;


    /**
     * Retrieve a list of log records written in a specified time period.  The offset and maximumRecords
     * parameters support a paging
     *
     * @param startDate  start of time period
     * @param endDate  end of time period
     * @param offset  offset of full collection to begin the return results
     * @param maximumRecords  maximum number of log records to return
     * @return list of log records from the specified time period
     * @throws InvalidParameterException indicates that the start and/or end date parameters are invalid.
     * @throws PagingErrorException indicates that the offset or the maximumRecords parameters are invalid.
     */
    List<OMRSAuditLogRecord> getAuditLogRecordsByTimeStamp(Date    startDate,
                                                           Date    endDate,
                                                           int     offset,
                                                           int     maximumRecords) throws InvalidParameterException,
                                                                                          PagingErrorException;

    /**
     * Retrieve a list of log records that have specific severity.  The offset and maximumRecords
     * parameters support a paging model.
     *
     * @param severity  the severity value of messages to return
     * @param startDate  start of time period
     * @param endDate  end of time period
     * @param offset  offset of full collection to begin the return results
     * @param maximumRecords  maximum number of log records to return
     * @return list of log records from the specified time period
     * @throws InvalidParameterException indicates that the severity, start and/or end date parameters are invalid.
     * @throws PagingErrorException indicates that the offset or the maximumRecords parameters are invalid.
     */
    List<OMRSAuditLogRecord> getAuditLogRecordsBySeverity(String   severity,
                                                          Date     startDate,
                                                          Date     endDate,
                                                          int      offset,
                                                          int      maximumRecords) throws InvalidParameterException,
                                                                                          PagingErrorException;

    /**
     * Retrieve a list of log records written by a specific component.  The offset and maximumRecords
     * parameters support a paging model.
     *
     * @param component  name of the component to retrieve events from
     * @param startDate  start of time period
     * @param endDate  end of time period
     * @param offset  offset of full collection to begin the return results
     * @param maximumRecords  maximum number of log records to return
     * @return list of log records from the specified time period
     * @throws InvalidParameterException indicates that the component, start and/or end date parameters are invalid.
     * @throws PagingErrorException indicates that the offset or the maximumRecords parameters are invalid.
     */
    List<OMRSAuditLogRecord> getAuditLogRecordsByComponent(String   component,
                                                           Date     startDate,
                                                           Date     endDate,
                                                           int      offset,
                                                           int      maximumRecords) throws InvalidParameterException,
                                                                                           PagingErrorException;
}
