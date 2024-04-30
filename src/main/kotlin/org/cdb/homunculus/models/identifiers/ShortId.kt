package org.cdb.homunculus.models.identifiers

import kotlinx.serialization.Serializable
import java.util.UUID

@JvmInline
@Serializable
value class ShortId(override val id: String) : Identifier {
	companion object {
		private val uuidRegex = "^[0-9a-fA-F]{8}\$".toRegex()

		fun generate() = ShortId(UUID.randomUUID().toString().substring(0, 8))
	}

	init {
		require(uuidRegex.matches(id)) { "The provided id is not a Short ID" }
	}
}
