package org.cdb.labwitch.models.embed

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import org.cdb.labwitch.models.identifiers.EntityId
import org.cdb.labwitch.models.types.ShortText
import java.util.Date

@Serializable
data class Note(
	val user: EntityId,
	@Contextual
	val date: Date,
	val message: ShortText,
)
