/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.frameworks.connectors.properties.beans;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * The EmbeddedConnection is used within a VirtualConnection.  It contains a connection and additional properties
 * the VirtualConnection uses when working with the EmbeddedConnection.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class EmbeddedConnection extends PropertyBase
{
    /*
     * Attributes of an embedded connection
     */
    protected String              displayName        = null;
    protected Map<String, Object> arguments          = null;
    protected Connection          embeddedConnection = null;


    /**
     * Default constructor
     */
    public EmbeddedConnection()
    {
        super();
    }


    /**
     * Copy/clone constructor.
     *
     * @param templateEmbeddedConnection element to copy
     */
    public EmbeddedConnection(EmbeddedConnection templateEmbeddedConnection)
    {
        /*
         * Save the parent asset description.
         */
        super(templateEmbeddedConnection);

        if (templateEmbeddedConnection != null)
        {
            displayName = templateEmbeddedConnection.getDisplayName();
            arguments = templateEmbeddedConnection.getArguments();
            embeddedConnection = templateEmbeddedConnection.getEmbeddedConnection();
        }
    }


    /**
     * Return the printable name of the embedded connection.
     *
     * @return String name
     */
    public String getDisplayName()
    {
        return displayName;
    }


    /**
     * Set up the printable name of the embedded connection.
     *
     * @param displayName String name
     */
    public void setDisplayName(String displayName)
    {
        this.displayName = displayName;
    }


    /**
     * Return the arguments for the embedded connection.
     *
     * @return property map
     */
    public Map<String, Object> getArguments()
    {
        if (arguments == null)
        {
            return null;
        }
        else if (arguments.isEmpty())
        {
            return null;
        }
        else
        {
            return new HashMap<>(arguments);
        }
    }


    /**
     * Set up the arguments for the embedded connection.
     *
     * @param arguments property map
     */
    public void setArguments(Map<String, Object> arguments)
    {
        this.arguments = arguments;
    }


    /**
     * Return the embedded connection.
     *
     * @return Connection object.
     */
    public Connection getEmbeddedConnection()
    {
        if (embeddedConnection == null)
        {
            return embeddedConnection;
        }
        else
        {
            return new Connection(embeddedConnection);
        }
    }


    /**
     * Set up the embedded connection
     *
     * @param embeddedConnection Connection object
     */
    public void setEmbeddedConnection(Connection embeddedConnection)
    {
        this.embeddedConnection = embeddedConnection;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "EmbeddedConnection{" +
                "displayName='" + displayName + '\'' +
                ", arguments=" + arguments +
                ", embeddedConnection=" + embeddedConnection +
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
        if (!(objectToCompare instanceof EmbeddedConnection))
        {
            return false;
        }
        EmbeddedConnection that = (EmbeddedConnection) objectToCompare;
        return Objects.equals(getDisplayName(), that.getDisplayName()) &&
                Objects.equals(getArguments(), that.getArguments()) &&
                Objects.equals(getEmbeddedConnection(), that.getEmbeddedConnection());
    }


    /**
     * Create a hash code for this element type.
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {

        return Objects.hash(getDisplayName(), getArguments(), getEmbeddedConnection());
    }
}