package org.cdb.homunculus.models

import org.cdb.homunculus.models.types.UserId
import java.util.Date

/**
 * Specialization of [StoredEntity] that includes a history of all the users that modified a specific instance of the
 * entity.
 */
interface Traceable : StoredEntity {
	val modificationLog: Map<Date, UserId>
}
