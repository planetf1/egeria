package org.odpi.openmetadata.accessservices.dataplatform.listeners;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.odpi.openmetadata.accessservices.dataplatform.contentmanager.OMEntityDao;
import org.odpi.openmetadata.accessservices.dataplatform.eventprocessor.EventPublisher;
import org.odpi.openmetadata.accessservices.dataplatform.events.InformationViewEvent;
import org.odpi.openmetadata.accessservices.dataplatform.ffdc.DataPlatformErrorCode;
import org.odpi.openmetadata.accessservices.dataplatform.utils.Constants;
import org.odpi.openmetadata.accessservices.dataplatform.views.InformationViewAssetHandler;
import org.odpi.openmetadata.accessservices.dataplatform.views.ViewHandler;
import org.odpi.openmetadata.accessservices.dataplatform.views.beans.InformationViewAsset;
import org.odpi.openmetadata.accessservices.dataplatform.views.beans.View;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLog;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLogRecordSeverity;
import org.odpi.openmetadata.repositoryservices.connectors.openmetadatatopic.OpenMetadataTopicListener;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class InformationViewEventListener implements OpenMetadataTopicListener {


    private static final Logger log = LoggerFactory.getLogger(InformationViewEventListener.class);
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private final OMEntityDao omEntityDao;
    private final OMRSAuditLog auditLog;
    private EventPublisher eventPublisher;
    private OMRSRepositoryHelper helper;

    public InformationViewEventListener(OMEntityDao omEntityDao, OMRSAuditLog auditLog, EventPublisher eventPublisher, OMRSRepositoryHelper helper) {
        this.omEntityDao = omEntityDao;
        this.auditLog = auditLog;
        this.eventPublisher = eventPublisher;
        this.helper = helper;
    }

    /**
     * @param eventAsString contains all the information needed to create or update new assets from data platform like connection details, database
     *                      name, schema name, table name, derived columns details
     */
    @Override
    public void processEvent(String eventAsString) {

        InformationViewEvent event = null;
        try {
            event = OBJECT_MAPPER.readValue(eventAsString, InformationViewEvent.class);
        } catch (Exception e) {
            DataPlatformErrorCode auditCode = DataPlatformErrorCode.PARSE_EVENT;

            auditLog.logException("processEvent",
                    auditCode.getErrorMessageId(),
                    OMRSAuditLogRecordSeverity.EXCEPTION,
                    auditCode.getErrorMessage(),
                    "event {" + eventAsString + "}",
                    auditCode.getSystemAction(),
                    auditCode.getUserAction(),
                    e);

        }
        if (event != null) {
            try {
                log.info("Started processing information view event in DataPlatform OMAS");

                InformationViewAssetHandler informationViewAssetHandler = new InformationViewAssetHandler(event, omEntityDao);
                ViewHandler viewsBuilder = new ViewHandler(event, omEntityDao, helper);
                ExecutorService executor = Executors.newCachedThreadPool();
                Future<InformationViewAsset> informationViewFuture = executor.submit(informationViewAssetHandler);
                Future<View> viewCreationFuture = executor.submit(viewsBuilder);

                InformationViewAsset informationViewAsset = informationViewFuture.get();
                View view = viewCreationFuture.get();
                executor.shutdown();

                if (view.getViewEntity() != null) {
                    omEntityDao.addRelationship(Constants.ATTRIBUTE_FOR_SCHEMA,
                            informationViewAsset.getRelationalDbSchemaType().getGUID(),
                            view.getViewEntity().getGUID(),
                            Constants.DATA_PLATFORM_OMAS_NAME,
                            new InstanceProperties());
                    event.getTableSource().setTableGuid(view.getViewEntity().getGUID());
                }
                eventPublisher.sendEvent(event);
            } catch (Exception e) {
                log.error("Exception processing event from in topic", e);
                DataPlatformErrorCode auditCode = DataPlatformErrorCode.PROCESS_EVENT_EXCEPTION;

                auditLog.logException("processEvent",
                        auditCode.getErrorMessageId(),
                        OMRSAuditLogRecordSeverity.EXCEPTION,
                        auditCode.getFormattedErrorMessage(eventAsString, e.getMessage()),
                        e.getMessage(),
                        auditCode.getSystemAction(),
                        auditCode.getUserAction(),
                        e);
            }
        }
    }

}
