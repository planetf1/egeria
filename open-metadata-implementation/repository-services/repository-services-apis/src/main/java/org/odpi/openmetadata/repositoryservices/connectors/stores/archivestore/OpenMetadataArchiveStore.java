/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.connectors.stores.archivestore;


import org.odpi.openmetadata.repositoryservices.connectors.stores.archivestore.properties.OpenMetadataArchive;

/**
 * <p>
 * OpenMetadataArchiveStore is the interface for a connector to an open metadata archive.  The open metadata archive
 * is a collection of type definitions (TypeDefs) and metadata instances (Entities and Relationships) that can be
 * loaded into an open metadata repository.
 * </p>
 * <p>
 *     An open metadata archive has 3 sections:
 * </p>
 * <ul>
 *     <li>
 *         Archive properties
 *     </li>
 *     <li>
 *         Type store: an ordered list of types
 *     </li>
 *     <li>
 *         Instance store: a list of entities and relationships
 *     </li>
 * </ul>
 */
public interface OpenMetadataArchiveStore
{
    /**
     * Return the contents of the archive.
     *
     * @return OpenMetadataArchive object
     */
    OpenMetadataArchive getArchiveContents();


    /**
     * Set new contents into the archive.  This overrides any content previously stored.
     *
     * @param archiveContents  OpenMetadataArchive object
     */
    void setArchiveContents(OpenMetadataArchive   archiveContents);
}
