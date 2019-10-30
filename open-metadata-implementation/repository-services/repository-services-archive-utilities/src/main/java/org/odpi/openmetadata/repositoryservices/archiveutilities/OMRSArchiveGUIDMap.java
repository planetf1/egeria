/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.archiveutilities;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * OMRSArchiveGUIDMap is a utility to create a persisted list of GUIDs used by an archive builder.
 * This helps the archive builder use the same GUIDs each time the archive is built.  GUIDs for
 * new elements are created automatically.
 */
public class OMRSArchiveGUIDMap
{
    private static final Logger log = LoggerFactory.getLogger(OMRSArchiveGUIDMap.class);

    private String              guidMapFileName;
    private Map<String, String> idToGUIDMap;


    /**
     * Constructor for the GUIDMap.
     *
     * @param guidMapFileName name of the file name where the GUIDs are stashed.
     */
    public OMRSArchiveGUIDMap(String guidMapFileName)
    {
        this.guidMapFileName = guidMapFileName;

        this.loadGUIDs();
    }


    /**
     * Load up the saved GUIds into a new HashMap.
     */
    private void loadGUIDs()
    {
        File         idFile = new File(guidMapFileName);
        ObjectMapper objectMapper = new ObjectMapper();

        try
        {
            log.debug("Retrieving Id to GUID Map");

            String idFileContents = FileUtils.readFileToString(idFile, "UTF-8");

            idToGUIDMap = objectMapper.readValue(idFileContents, Map.class);
        }
        catch (IOException   ioException)
        {
            idToGUIDMap = new HashMap<>();
        }

        log.debug("Id to GUID Map contains: " + idToGUIDMap);
    }


    /**
     * Retrieve the guid for an element based on its id.
     *
     * @param id id of element
     * @return guid mapped to Id
     */
    public String  getGUID(String id)
    {
        String guid = idToGUIDMap.get(id);

        if (guid == null)
        {
            guid = UUID.randomUUID().toString();

            idToGUIDMap.put(id, guid);
        }

        return guid;
    }


    /**
     * Save the map to a file
     */
    public void  saveGUIDs()
    {
        File         idFile = new File(guidMapFileName);
        ObjectMapper objectMapper = new ObjectMapper();

        try
        {
            if (idToGUIDMap.isEmpty())
            {
                log.debug("Deleting id file because map is empty: " + guidMapFileName);

                idFile.delete();
            }
            else
            {
                log.debug("Writing id file " + guidMapFileName);

                String mapContents = objectMapper.writeValueAsString(idToGUIDMap);

                FileUtils.writeStringToFile(idFile, mapContents, false);
            }

        }
        catch (Throwable  exc)
        {
            log.error("Unusable Map Store :(", exc);

        }
    }


    /**
     * Return the size of the map.
     *
     * @return int (zero if the map is null)
     */
    public int getSize()
    {
        if (idToGUIDMap != null)
        {
            return idToGUIDMap.size();
        }
        else
        {
            return 0;
        }
    }
}
