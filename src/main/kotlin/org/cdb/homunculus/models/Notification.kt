package org.cdb.homunculus.models

import org.cdb.homunculus.models.filters.Filter
import org.cdb.homunculus.models.identifiers.EntityId

interface Notification {
	val threshold: Int
	val recipients: Set<EntityId>
	val templateId: String?

	fun buildFilter(): Filter
}
