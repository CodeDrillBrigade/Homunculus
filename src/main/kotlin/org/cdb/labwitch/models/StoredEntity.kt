package org.cdb.labwitch.models

/**
 * This interface defines all the properties that a data class should have in order to be correctly
 * stored in the db.
 *
 * @param id a unique id (preferably a UUID v4).
 */
interface StoredEntity {
    val id: String
}