package org.cdb.homunculus.models.identifiers

import kotlinx.serialization.Serializable
import java.util.UUID

@JvmInline
@Serializable
value class EntityId(override val id: String) : Identifier {
	companion object {
		private val uuidRegex = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}\$".toRegex()

		fun generate() = EntityId(UUID.randomUUID().toString())
	}

	init {
		require(uuidRegex.matches(id)) { "The provided id is not a UUID" }
	}
}
