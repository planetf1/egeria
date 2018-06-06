/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.accessservices.connectedasset.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.accessservices.connectedasset.ffdc.ConnectedAssetErrorCode;
import org.odpi.openmetadata.accessservices.connectedasset.ffdc.exceptions.ConnectedAssetRuntimeException;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;


/**
 * The AdditionalProperties class provides support for arbitrary properties to be added to a properties object.
 * It wraps a java.util.Map map object built around HashMap.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class AdditionalProperties extends PropertyBase
{
    private Map<String,Object>  additionalProperties = new HashMap<>();

    /**
     * Constructor for a new set of additional properties that are connected either directly or indirectly to an asset.
     */
    public AdditionalProperties()
    {
        super();
    }


    /**
     * Copy/clone Constructor for additional properties that are connected to an asset.
     *
     * @param templateProperties - template object to copy.
     */
    public AdditionalProperties(AdditionalProperties templateProperties)
    {
        super(templateProperties);

        /*
         * An empty properties object is created in the private variable declaration so nothing to do.
         */
        if (templateProperties != null)
        {
            /*
             * Process templateProperties if they are not null
             */
            Iterator<String> propertyNames = templateProperties.getPropertyNames();

            if (propertyNames != null)
            {
                while (propertyNames.hasNext())
                {
                    String newPropertyName = propertyNames.next();
                    Object newPropertyValue = templateProperties.getProperty(newPropertyName);

                    additionalProperties.put(newPropertyName, newPropertyValue);
                }
            }
        }
    }


    /**
     * Returns a list of the additional stored properties for the element.
     * If no stored properties are present then null is returned.
     *
     * @return list of additional properties
     */
    public Iterator<String> getPropertyNames()
    {
        return additionalProperties.keySet().iterator();
    }


    /**
     * Returns the requested additional stored property for the element.
     * If no stored property with that name is present then null is returned.
     *
     * @param name - String name of the property to return.
     * @return requested property value.
     */
    public Object getProperty(String name)
    {
        return additionalProperties.get(name);
    }


    /**
     * Adds or updates an additional property.
     * If a null is supplied for the property name, an OCF runtime exception is thrown.
     * If a null is supplied for the property value, the property is removed.
     *
     * @param  newPropertyName - name
     * @param  newPropertyValue - value
     */
    public void setProperty(String newPropertyName, Object newPropertyValue)
    {
        final String   methodName = "setProperty";

        if (newPropertyName == null)
        {
            /*
             * Build and throw exception.
             */
            ConnectedAssetErrorCode errorCode = ConnectedAssetErrorCode.NULL_PROPERTY_NAME;
            String       errorMessage = errorCode.getErrorMessageId()
                                      + errorCode.getFormattedErrorMessage(methodName, this.getClass().getName());

            throw new ConnectedAssetRuntimeException(errorCode.getHTTPErrorCode(),
                                                     this.getClass().getName(),
                                                     methodName,
                                                     errorMessage,
                                                     errorCode.getSystemAction(),
                                                     errorCode.getUserAction());
        }
        else if (newPropertyValue == null)
        {
            additionalProperties.remove(newPropertyName);
        }
        else
        {
            additionalProperties.put(newPropertyName, newPropertyValue);
        }
    }
}