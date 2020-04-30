/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.archiveutilities.designmodels.owlcanonicalglossarymodel;

import org.apache.log4j.varia.NullAppender;
import org.odpi.openmetadata.repositoryservices.archiveutilities.OMRSArchiveWriter;


/**
 * CanonicalGlossaryOwlArchiveWriter creates a physical open metadata archive file for the data model and glossary
 * content.
 */
public class CanonicalGlossaryOwlArchiveWriter extends OMRSArchiveWriter
{
    private  String openMetadataArchiveFileName;

    private String modelLocation;


    /**
     * Default constructor
     *
     * @param modelLocation directory name for the model's JSON-LD files.
     */
    CanonicalGlossaryOwlArchiveWriter(String modelLocation)
    {
        this.modelLocation = modelLocation;
        // parser checks that the last 5 characters of the supplied file name are .json
        this.openMetadataArchiveFileName = modelLocation.substring(0,modelLocation.length()-5) + "Archive.json";
    }


    /**
     * Generates and writes out an open metadata archive containing all of the elements extracted from the supplied jsonld file.
     */
    void writeOpenMetadataArchive()
    {
        try
        {
            CanonicalGlossaryOwlParser         modelParser         = new CanonicalGlossaryOwlParser(modelLocation);
            CanonicalGlossaryOwlModelArchiveBuilder modelArchiveBuilder = new CanonicalGlossaryOwlModelArchiveBuilder(modelParser);

            super.writeOpenMetadataArchive(this.openMetadataArchiveFileName,
                                           modelArchiveBuilder.getOpenMetadataArchive());
        }
        catch (Throwable  error)
        {
            System.out.println("error is " + error.toString());
        }
    }


    /**
     * Main program to initiate the archive writer for the Canonical glossary Owl model
     *
     * @param args list of arguments - first one should be the directory where the model
     *             content is located.  Any other arguments passed are ignored.
     */
    public static void main(String[] args)
    {
        org.apache.log4j.BasicConfigurator.configure(new NullAppender());

        if (args.length == 0)
        {
            System.err.println("USAGE: filename");
            System.exit(-1);
        }
        if (!args[0].endsWith(".json")) {
            System.err.println("Supplied filename must have a json extension");
            System.exit(-1);
        }

        try
        {
            CanonicalGlossaryOwlArchiveWriter archiveWriter = new CanonicalGlossaryOwlArchiveWriter(args[0]);
            archiveWriter.writeOpenMetadataArchive();
        }
        catch (Throwable error)
        {
            System.err.println("Exception: " + error.toString());
            System.exit(-1);
        }
    }
}
