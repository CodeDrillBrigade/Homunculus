package org.cdb.homunculus.models

import org.cdb.homunculus.models.identifiers.EntityId
import org.cdb.homunculus.models.identifiers.Identifier

/**
 * This interface defines all the properties that a data class should have in order to be correctly
 * stored in the db.
 *
 * @param id a unique [EntityId].
 */
interface StoredEntity {
	val id: Identifier
}
