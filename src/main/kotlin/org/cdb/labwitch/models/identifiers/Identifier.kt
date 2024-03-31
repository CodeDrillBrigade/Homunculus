package org.cdb.labwitch.models.identifiers

import kotlinx.serialization.Serializable

@Serializable
sealed interface Identifier {
	val id: String
}
