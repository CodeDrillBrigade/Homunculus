package org.cdb.homunculus.models.identifiers

import kotlinx.serialization.Serializable

@Serializable
sealed interface Identifier {
	val id: String
}
