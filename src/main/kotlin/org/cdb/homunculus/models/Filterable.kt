package org.cdb.homunculus.models

import org.cdb.homunculus.models.identifiers.EntityId
import org.cdb.homunculus.models.types.ShortText

interface Filterable : StoredEntity {
	val name: ShortText
	val tags: Set<EntityId>
}
