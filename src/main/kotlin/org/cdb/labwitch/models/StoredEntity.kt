package org.cdb.labwitch.models

import org.cdb.labwitch.models.types.EntityId

/**
 * This interface defines all the properties that a data class should have in order to be correctly
 * stored in the db.
 *
 * @param id a unique [EntityId].
 */
interface StoredEntity {
    val id: EntityId
}
