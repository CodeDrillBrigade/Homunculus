package org.cdb.homunculus.models.embed

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import org.cdb.homunculus.models.identifiers.EntityId
import org.cdb.homunculus.models.types.ShortText
import java.util.Date

@Serializable
data class Note(
	val user: EntityId,
	@Contextual
	val date: Date,
	val message: ShortText,
)
