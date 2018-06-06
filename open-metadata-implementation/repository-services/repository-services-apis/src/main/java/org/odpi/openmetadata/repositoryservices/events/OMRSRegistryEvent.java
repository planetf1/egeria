/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.repositoryservices.events;

import org.apache.log4j.Logger;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.repositoryservices.events.v1.OMRSEventV1;
import org.odpi.openmetadata.repositoryservices.events.v1.OMRSEventV1RegistrySection;

import java.util.Date;


/**
 * OMRSRegistryEvent provides the conversion between the properties of a registry event and the serialized event body.
 * It supports conversion in either direction through its inner class called OMRSRegistryEventPayload:
 * <ul>
 *     <li>
 *         Converting from registry event properties to an OMRSEvent body for sending outbound events.
 *     </li>
 *     <li>
 *         Converting from an event body to registry event properties for inbound events.
 *     </li>
 * </ul>
 * OMRSRegistryEvent also provides a specialized interface to allow the cohort registry to work effectively
 * with registry events.
 */
public class OMRSRegistryEvent extends OMRSEvent
{
    /*
     * The type of the registry event that defines how the rest of the values should be interpreted.
     */
    private  OMRSRegistryEventType       registryEventType               = OMRSRegistryEventType.UNKNOWN_REGISTRY_EVENT;

    /*
     * Registration information describing a specific repository.
     */
    private  Date                        registrationTimestamp           = null;
    private  Connection                  remoteConnection                = null;

    /*
     * Specific variables only used in error reporting.  It defines the subset of error codes from OMRSEvent
     * that are specific to registry events.
     */
    private  OMRSRegistryEventErrorCode  errorCode                       = OMRSRegistryEventErrorCode.NOT_IN_USE;


    private static final Logger log = Logger.getLogger(OMRSRegistryEvent.class);


    /**
     * Inbound event constructor that takes the object created by the Jackson JSON mapper and unpacks the
     * properties into the registry event.
     *
     * @param inboundEvent - incoming Event.
     */
    public OMRSRegistryEvent(OMRSEventV1 inboundEvent)
    {
        super(inboundEvent);

        OMRSEventV1RegistrySection registrySection = inboundEvent.getRegistryEventSection();

        if (registrySection != null)
        {
            this.registryEventType     = registrySection.getRegistryEventType();
            this.registrationTimestamp = registrySection.getRegistrationTimestamp();
            this.remoteConnection      = registrySection.getRemoteConnection();
        }

        if (super.genericErrorCode != null)
        {
            switch (errorCode)
            {
                case BAD_REMOTE_CONNECTION:
                    this.errorCode = OMRSRegistryEventErrorCode.BAD_REMOTE_CONNECTION;
                    break;

                case CONFLICTING_COLLECTION_ID:
                    this.errorCode = OMRSRegistryEventErrorCode.CONFLICTING_COLLECTION_ID;
                    break;

                default:
                    this.errorCode = OMRSRegistryEventErrorCode.UNKNOWN_ERROR_CODE;
            }
        }
    }


    /**
     * Constructor for a normal outbound event.  It sets the event type and the other parameters
     * used in a registry event payload.
     *
     * @param registryEventType - type of event (REGISTRATION_EVENT, REFRESH_REGISTRATION_REQUEST, RE_REGISTRATION_EVENT)
     * @param registrationTimestamp - time that the local repository registered.
     * @param remoteConnection - remote connection to this local repository.
     */
    public OMRSRegistryEvent(OMRSRegistryEventType          registryEventType,
                             Date                           registrationTimestamp,
                             Connection                     remoteConnection)
    {
        super(OMRSEventCategory.REGISTRY);

        this.registryEventType           = registryEventType;
        this.registrationTimestamp       = registrationTimestamp;
        this.remoteConnection            = remoteConnection;
    }


    /**
     * Constructor for an UnRegistration Event.
     *
     * @param registryEventType - the type of event
     */
    public OMRSRegistryEvent(OMRSRegistryEventType          registryEventType)
    {
        super(OMRSEventCategory.REGISTRY);

        this.registryEventType           = registryEventType;
    }


    /**
     * Constructor for the REGISTRATION_ERROR_EVENT outbound event.
     *
     * @param errorCode - detailed error code
     * @param errorMessage - Optional error message
     * @param targetMetadataCollectionId - the identifier of the server that sent bad information.
     * @param remoteConnection - remote connection to the target repository. (Optional - only supplied if
     *                              relevant to the reported error; otherwise null.)
     */
    public OMRSRegistryEvent(OMRSRegistryEventErrorCode errorCode,
                             String                     errorMessage,
                             String                     targetMetadataCollectionId,
                             Connection                 remoteConnection)
    {
        super(OMRSEventCategory.REGISTRY,
              errorCode.getErrorCodeEncoding(),
              errorMessage,
              targetMetadataCollectionId,
              remoteConnection);

        this.registryEventType = OMRSRegistryEventType.REGISTRATION_ERROR_EVENT;
    }


    /**
     * Return the specific registry event type.
     *
     * @return registry event type enum
     */
    public OMRSRegistryEventType getRegistryEventType()
    {
        return registryEventType;
    }


    /**
     * Return the date/time that the repository registered with the open metadata repository cohort.
     * If this is a normal registry event then this timestamp is the registration time for the local repository.
     * If this an error event, then this is the registration time for the target repository.
     *
     * @return Date object for timestamp
     */
    public Date getRegistrationTimestamp()
    {
        return registrationTimestamp;
    }


    /**
     * Return the remote connection used to create a connector used to call the repository across the network.
     * If this is a normal registry event then this connection is for the local repository.
     * If this an error event, then this is the connection for the target repository.
     *
     * @return Connection object
     */
    public Connection getRemoteConnection()
    {
        return remoteConnection;
    }


    /**
     * Return the error code for the event.  This property is only used for error events.
     *
     * @return OMRSRegistryEventErrorCode enum
     */
    public OMRSRegistryEventErrorCode getErrorCode()
    {
        return errorCode;
    }


    /**
     * Returns an OMRSEvent populated with details from this RegistryEvent
     *
     * @return OMRSEvent (Version 1) object
     */
    public OMRSEventV1  getOMRSEventV1()
    {
        OMRSEventV1     omrsEvent = super.getOMRSEventV1();

        OMRSEventV1RegistrySection registrySection  = new OMRSEventV1RegistrySection();

        registrySection.setRegistryEventType(this.registryEventType);
        registrySection.setRegistrationTimestamp(this.registrationTimestamp);
        registrySection.setRemoteConnection(this.remoteConnection);

        omrsEvent.setRegistryEventSection(registrySection);

        return omrsEvent;
    }


    /**
     * Standard toString method.
     *
     * @return JSON style description of variables.
     */
    @Override
    public String toString()
    {
        return "OMRSRegistryEvent{" +
                "registryEventType=" + registryEventType +
                ", registrationTimestamp=" + registrationTimestamp +
                ", remoteConnection=" + remoteConnection +
                ", errorCode=" + errorCode +
                ", eventTimestamp=" + eventTimestamp +
                ", eventDirection=" + eventDirection +
                ", eventCategory=" + eventCategory +
                ", eventOriginator=" + eventOriginator +
                ", genericErrorCode=" + genericErrorCode +
                ", errorMessage='" + errorMessage + '\'' +
                ", targetMetadataCollectionId='" + targetMetadataCollectionId + '\'' +
                ", targetRemoteConnection=" + targetRemoteConnection +
                ", targetTypeDefSummary=" + targetTypeDefSummary +
                ", targetAttributeTypeDef=" + targetAttributeTypeDef +
                ", targetInstanceGUID='" + targetInstanceGUID + '\'' +
                ", otherOrigin=" + otherOrigin +
                ", otherMetadataCollectionId='" + otherMetadataCollectionId + '\'' +
                ", otherTypeDefSummary=" + otherTypeDefSummary +
                ", otherTypeDef=" + otherTypeDef +
                ", otherAttributeTypeDef=" + otherAttributeTypeDef +
                ", otherInstanceGUID='" + otherInstanceGUID + '\'' +
                '}';
    }
}
