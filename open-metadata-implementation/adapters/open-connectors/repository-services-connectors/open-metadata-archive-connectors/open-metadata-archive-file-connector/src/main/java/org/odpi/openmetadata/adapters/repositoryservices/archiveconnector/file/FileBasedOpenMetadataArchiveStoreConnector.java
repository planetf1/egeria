/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.adapters.repositoryservices.archiveconnector.file;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.log4j.Logger;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.connectors.properties.Connection;
import org.odpi.openmetadata.frameworks.connectors.properties.Endpoint;
import org.apache.commons.io.FileUtils;
import org.odpi.openmetadata.repositoryservices.connectors.stores.archivestore.OpenMetadataArchiveStoreConnector;
import org.odpi.openmetadata.repositoryservices.connectors.stores.archivestore.properties.OpenMetadataArchive;

import java.io.File;
import java.io.IOException;

public class FileBasedOpenMetadataArchiveStoreConnector extends OpenMetadataArchiveStoreConnector
{
    /*
     * This is the default name of the open metadata archive file that is used if there is no file name in the connection.
     */
    private static final String defaultFilename = "open.metadata.archive";

    /*
     * Variables used in writing to the file.
     */
    private String archiveStoreName = null;

    /*
     * Variables used for logging and debug.
     */
    private static final Logger log = Logger.getLogger(FileBasedOpenMetadataArchiveStoreConnector.class);


    /**
     * Default constructor
     */
    public FileBasedOpenMetadataArchiveStoreConnector()
    {
    }


    @Override
    public void initialize(String connectorInstanceId, Connection connection)
    {
        super.initialize(connectorInstanceId, connection);

        Endpoint endpoint = connection.getEndpoint();

        if (endpoint != null)
        {
            archiveStoreName = endpoint.getAddress();
        }

        if (archiveStoreName == null)
        {
            archiveStoreName = defaultFilename;
        }
    }


    /**
     * Return the contents of the archive.
     *
     * @return OpenMetadataArchive object
     */
    public OpenMetadataArchive getArchiveContents()
    {
        File                archiveStoreFile     = new File(archiveStoreName);
        OpenMetadataArchive newOpenMetadataArchive;

        try
        {
            if (log.isDebugEnabled())
            {
                log.debug("Retrieving server configuration properties");
            }

            String configStoreFileContents = FileUtils.readFileToString(archiveStoreFile, "UTF-8");

            ObjectMapper objectMapper = new ObjectMapper();

            newOpenMetadataArchive = objectMapper.readValue(configStoreFileContents, OpenMetadataArchive.class);
        }
        catch (IOException ioException)
        {
            /*
             * The config file is not found, create a new one ...
             */

            if (log.isDebugEnabled())
            {
                log.debug("New server config Store", ioException);
            }

            newOpenMetadataArchive = new OpenMetadataArchive();
        }

        return newOpenMetadataArchive;
    }


    /**
     * Set new contents into the archive.  This overrides any content previously stored.
     *
     * @param archiveContents   OpenMetadataArchive object
     */
    public void setArchiveContents(OpenMetadataArchive   archiveContents)
    {
        File    archiveStoreFile = new File(archiveStoreName);

        try
        {
            if (log.isDebugEnabled())
            {
                log.debug("Writing open metadata archive store properties: " + archiveContents);
            }

            if (archiveContents == null)
            {
                archiveStoreFile.delete();
            }
            else
            {
                ObjectMapper objectMapper = new ObjectMapper();

                String archiveStoreFileContents = objectMapper.writeValueAsString(archiveContents);

                FileUtils.writeStringToFile(archiveStoreFile, archiveStoreFileContents, false);
            }
        }
        catch (IOException   ioException)
        {
            if (log.isDebugEnabled())
            {
                log.debug("Unusable Server config Store :(", ioException);
            }
        }
    }


    /**
     * Indicates that the connector is completely configured and can begin processing.
     *
     * @throws ConnectorCheckedException there is a problem within the connector.
     */
    public void start() throws ConnectorCheckedException
    {
        super.start();
    }


    /**
     * Free up any resources held since the connector is no longer needed.
     *
     * @throws ConnectorCheckedException there is a problem within the connector.
     */
    public  void disconnect() throws ConnectorCheckedException
    {
        super.disconnect();

        if (log.isDebugEnabled())
        {
            log.debug("Closing Config Store.");
        }
    }
}
