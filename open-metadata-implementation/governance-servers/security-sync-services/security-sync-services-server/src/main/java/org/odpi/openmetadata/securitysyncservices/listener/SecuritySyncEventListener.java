/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.securitysyncservices.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.odpi.openmetadata.accessservices.governanceengine.api.events.GovernanceEngineEvent;
import org.odpi.openmetadata.repositoryservices.connectors.openmetadatatopic.OpenMetadataTopicListener;
import org.odpi.openmetadata.securitysyncservices.processor.SecuritySyncEventProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SecuritySyncEventListener implements OpenMetadataTopicListener {

    private static final Logger log = LoggerFactory.getLogger(SecuritySyncEventListener.class);
    private static ObjectMapper objectMapper = new ObjectMapper();
    private SecuritySyncEventProcessor securitySyncEventProcessor;

    public SecuritySyncEventListener(SecuritySyncEventProcessor securitySyncEventProcessor) {
        this.securitySyncEventProcessor = securitySyncEventProcessor;
    }

    @Override
    public void processEvent(String receivedEvent) {
        log.info("[Security Sync] Event Received");

        try {
            GovernanceEngineEvent event = objectMapper.readValue(receivedEvent, GovernanceEngineEvent.class);
            switch (event.getEventType()) {
                case NEW_CLASSIFIED_ASSET:
                    securitySyncEventProcessor.processClassifiedGovernedAssetEvent(event.getGovernedAsset());
                    break;
                case RE_CLASSIFIED_ASSET:
                    securitySyncEventProcessor.processReClassifiedGovernedAssetEvent(event.getGovernedAsset());
                    break;
                case DE_CLASSIFIED_ASSET:
                    securitySyncEventProcessor.processDeClassifiedGovernedAssetEvent(event.getGovernedAsset());
                break;
                case DELETED_ASSET:
                    securitySyncEventProcessor.processDeletedGovernedAssetEvent(event.getGovernedAsset());
                    break;
                default:
                    log.debug("Unknown event type");
                    break;
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}