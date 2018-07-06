/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.repositoryservices.connectors.omrstopic;

import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.repositoryservices.events.OMRSEventProtocolVersion;
import org.odpi.openmetadata.repositoryservices.events.OMRSInstanceEvent;
import org.odpi.openmetadata.repositoryservices.events.OMRSRegistryEvent;
import org.odpi.openmetadata.repositoryservices.events.OMRSTypeDefEvent;
import org.odpi.openmetadata.repositoryservices.events.beans.v1.OMRSEventV1;

/**
 * OMRSTopic defines the interface to the messaging Topic for OMRS Events.
 * It implemented by the OMRSTopicConnector.
 */
public interface OMRSTopic
{
    /**
     * Setup the version of the protocol to use for events.
     *
     * @param eventProtocolVersion version enum
     */
    void setEventProtocolLevel(OMRSEventProtocolVersion   eventProtocolVersion);


    /**
     * Register a listener object.  This object will be supplied with all of the events
     * received on the topic.
     *
     * @param newListener object implementing the OMRSTopicListener interface
     */
    void registerListener(OMRSTopicListener  newListener);


    /**
     * Sends the supplied event to the topic.
     *
     * @param event OMRSRegistryEvent object containing the event properties.
     * @throws ConnectorCheckedException the connector is not able to communicate with the event bus
     */
    void sendRegistryEvent(OMRSRegistryEvent event) throws ConnectorCheckedException;


    /**
     * Sends the supplied event to the topic.
     *
     * @param event OMRSTypeDefEvent object containing the event properties.
     * @throws ConnectorCheckedException the connector is not able to communicate with the event bus
     */
    void sendTypeDefEvent(OMRSTypeDefEvent event) throws ConnectorCheckedException;


    /**
     * Sends the supplied event to the topic.
     *
     * @param event OMRSInstanceEvent object containing the event properties.
     * @throws ConnectorCheckedException the connector is not able to communicate with the event bus
     */
    void sendInstanceEvent(OMRSInstanceEvent event) throws ConnectorCheckedException;
}
