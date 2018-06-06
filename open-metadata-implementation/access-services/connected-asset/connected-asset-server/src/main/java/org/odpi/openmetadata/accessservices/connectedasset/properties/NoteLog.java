/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.accessservices.connectedasset.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.ArrayList;
import java.util.List;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * NoteLog manages a list of notes for an asset
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class NoteLog extends Referenceable
{
    /*
     * Attributes of an note log
     */
    private String     displayName = null;
    private String     description = null;
    private List<Note> notes       = null;


    /**
     * Default Constructor
     */
    public NoteLog()
    {
        super();
    }


    /**
     * Copy/clone constructor.
     *
     * @param templateNotelog - note log to copy
     */
    public NoteLog(NoteLog templateNotelog)
    {
        super(templateNotelog);

        if (templateNotelog != null)
        {
            /*
             * Copy the values from the supplied template.
             */
            displayName = templateNotelog.getDisplayName();
            description = templateNotelog.getDescription();

            List<Note>     templateNotes = templateNotelog.getNotes();
            if (templateNotes != null)
            {
                notes = new ArrayList<>(templateNotes);
            }
        }
    }

    /**
     * Returns the stored display name property for the note log.
     * If no display name is available then null is returned.
     *
     * @return displayName
     */
    public String getDisplayName()
    {
        return displayName;
    }


    /**
     * Updates the display name property stored for the note log.
     * If a null is supplied it clears the display name.
     *
     * @param  newDisplayName - consumable name
     */
    public void setDisplayName(String  newDisplayName)
    {
        displayName = newDisplayName;
    }


    /**
     * Returns the stored description property for the note log.
     * If no description is provided then null is returned.
     *
     * @return description
     */
    public String getDescription()
    {
        return description;
    }


    /**
     * Updates the description property stored for the note log.
     * If a null is supplied it clears any saved description.
     *
     * @param  newDescription - description
     */
    public void setDescription(String  newDescription) { description = newDescription; }


    /**
     * Return the list of notes defined for this note log.
     *
     * @return Notes - list of notes
     */
    public List<Note> getNotes()
    {
        if (notes == null)
        {
            return notes;
        }
        else
        {
            return new ArrayList<>(notes);
        }
    }

    /**
     * Set up the list of notes for this note log.
     *
     * @param notes - Notes list
     */
    public void setNotes(List<Note> notes) { this.notes = notes; }
}