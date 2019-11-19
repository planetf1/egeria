/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataengine.server.listeners;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.collections4.CollectionUtils;
import org.odpi.openmetadata.accessservices.dataengine.event.DataEngineRegistrationEvent;
import org.odpi.openmetadata.accessservices.dataengine.event.LineageMappingsEvent;
import org.odpi.openmetadata.accessservices.dataengine.event.PortAliasEvent;
import org.odpi.openmetadata.accessservices.dataengine.event.PortImplementationEvent;
import org.odpi.openmetadata.accessservices.dataengine.event.ProcessToPortListEvent;
import org.odpi.openmetadata.accessservices.dataengine.event.ProcessesEvent;
import org.odpi.openmetadata.accessservices.dataengine.ffdc.DataEngineErrorCode;
import org.odpi.openmetadata.accessservices.dataengine.ffdc.DataEngineException;
import org.odpi.openmetadata.accessservices.dataengine.server.admin.DataEngineServicesInstance;
import org.odpi.openmetadata.accessservices.dataengine.server.service.DataEngineRESTServices;
import org.odpi.openmetadata.commonservices.ffdc.rest.FFDCResponseBase;
import org.odpi.openmetadata.commonservices.multitenant.ffdc.exceptions.NewInstanceException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLog;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLogRecordSeverity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

public class DataEngineEventProcessor {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final Logger log = LoggerFactory.getLogger(DataEngineEventProcessor.class);

    private final OMRSAuditLog auditLog;
    private final String serverName;

    private DataEngineRESTServices dataEngineRESTServices = new DataEngineRESTServices();

    public DataEngineEventProcessor(DataEngineServicesInstance instance, OMRSAuditLog auditLog) throws
                                                                                                NewInstanceException {
        this.auditLog = auditLog;
        this.serverName = instance.getServerName();
    }

    public void processDataEngineRegistrationEvent(String dataEngineEvent) {
        final String methodName = "processDataEngineRegistrationEvent";

        log.debug("Calling method: {}", methodName);
        try {
            DataEngineRegistrationEvent dataEngineRegistrationEvent = OBJECT_MAPPER.readValue(dataEngineEvent,
                    DataEngineRegistrationEvent.class);
            dataEngineRESTServices.createExternalDataEngine(dataEngineRegistrationEvent.getUserId(), serverName,
                    dataEngineRegistrationEvent.getSoftwareServerCapability());

        } catch (JsonProcessingException | UserNotAuthorizedException | PropertyServerException | InvalidParameterException e) {
            logException(dataEngineEvent, methodName, e);
        }
    }

    public void processPortAliasEvent(String dataEngineEvent) {
        final String methodName = "processPortAliasEvent";

        log.debug("Calling method: {}", methodName);
        try {
            PortAliasEvent portAliasEvent = OBJECT_MAPPER.readValue(dataEngineEvent, PortAliasEvent.class);

            dataEngineRESTServices.createOrUpdatePortAliasWithDelegation(portAliasEvent.getUserId(), serverName,
                    portAliasEvent.getPort(), portAliasEvent.getExternalSourceName());

        } catch (JsonProcessingException | PropertyServerException | UserNotAuthorizedException | InvalidParameterException e) {
            logException(dataEngineEvent, methodName, e);
        }
    }

    public void processPortImplementationEvent(String dataEngineEvent) {
        final String methodName = "processPortImplementationEvent";

        log.debug("Calling method: {}", methodName);
        try {
            PortImplementationEvent portImplementationEvent = OBJECT_MAPPER.readValue(dataEngineEvent,
                    PortImplementationEvent.class);

            dataEngineRESTServices.createOrUpdatePortImplementationWithSchemaType(portImplementationEvent.getUserId(),
                    serverName, portImplementationEvent.getPortImplementation(),
                    portImplementationEvent.getExternalSourceName());

        } catch (JsonProcessingException | PropertyServerException | UserNotAuthorizedException | InvalidParameterException e) {
            logException(dataEngineEvent, methodName, e);
        }
    }

    public void processProcessToPortListEvent(String dataEngineEvent) {
        final String methodName = "processProcessToPortListEvent";

        log.debug("Calling method: {}", methodName);

        try {
            ProcessToPortListEvent processToPortListEvent = OBJECT_MAPPER.readValue(dataEngineEvent,
                    ProcessToPortListEvent.class);

            dataEngineRESTServices.addPortsToProcess(processToPortListEvent.getUserId(), serverName,
                    processToPortListEvent.getProcessGUID(), processToPortListEvent.getPorts(),
                    processToPortListEvent.getExternalSourceName());

        } catch (JsonProcessingException | PropertyServerException | UserNotAuthorizedException | InvalidParameterException e) {
            logException(dataEngineEvent, methodName, e);
        }
    }

    public void processLineageMappingsEvent(String dataEngineEvent) {
        final String methodName = "processLineageMappingsEvent";

        log.debug("Calling method: {}", methodName);

        try {
            LineageMappingsEvent lineageMappingsEvent = OBJECT_MAPPER.readValue(dataEngineEvent,
                    LineageMappingsEvent.class);

            if (CollectionUtils.isEmpty(lineageMappingsEvent.getLineageMappings())) {
                return;
            }

            FFDCResponseBase response = new FFDCResponseBase();
            dataEngineRESTServices.addLineageMappings(lineageMappingsEvent.getUserId(), serverName,
                    lineageMappingsEvent.getLineageMappings(), response, lineageMappingsEvent.getExternalSourceName());

            validateResponse(response, dataEngineEvent, methodName);

        } catch (JsonProcessingException | UserNotAuthorizedException | InvalidParameterException | PropertyServerException | DataEngineException e) {
            logException(dataEngineEvent, methodName, e);
        }
    }

    public void processProcessesEvent(String dataEngineEvent) {
        final String methodName = "processProcessesEvent";

        log.debug("Calling method: {}", methodName);
        try {
            ProcessesEvent processesEvent = OBJECT_MAPPER.readValue(dataEngineEvent, ProcessesEvent.class);

//            FFDCResponseBase response = new FFDCResponseBase();
//            dataEngineRESTServices.createOrUpdateProcesses(processesEvent.getUserId(), )
//          //  createOrUpdateProcesses(processesEvent);
//


        } catch (JsonProcessingException e) {
            log.debug("Exception in parsing event from in Data Engine In Topic", e);
            logException(dataEngineEvent, methodName, e);
        }
    }

    private void logException(String dataEngineEvent, String methodName, Exception e) {
        log.debug("Exception in processing {} from in Data Engine In Topic", methodName);
        DataEngineErrorCode errorCode = DataEngineErrorCode.PARSE_EVENT_EXCEPTION;
        auditLog.logException(methodName, errorCode.getErrorMessageId(), OMRSAuditLogRecordSeverity.EXCEPTION,
                errorCode.getFormattedErrorMessage(dataEngineEvent, e.getMessage()), e.getMessage(),
                errorCode.getSystemAction(), errorCode.getUserAction(), e);
    }

    private void validateResponse(FFDCResponseBase response, String dataEngineEvent, String methodName) throws
                                                                                                        DataEngineException {
        // we need this extra validation because the FFDCResponseBase object captures the potential exceptions
        // thrown during a parallel processing
        if (response.getRelatedHTTPCode() != HttpStatus.OK.value()) {
            DataEngineErrorCode errorCode = DataEngineErrorCode.DATA_ENGINE_EXCEPTION;
            String errorMessage = errorCode.getErrorMessageId()
                    + errorCode.getFormattedErrorMessage(dataEngineEvent);
            throw new DataEngineException(errorCode.getHTTPErrorCode(), this.getClass().getName(), methodName,
                    errorMessage, errorCode.getSystemAction(), errorCode.getUserAction(), dataEngineEvent);
        }
    }
}
