package org.cdb.labwitch.models.embed

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.cdb.labwitch.models.identifiers.EntityId
import org.cdb.labwitch.models.types.HexColor

@Serializable
data class Tag(
	@SerialName("_id") val id: EntityId,
	val tagName: String,
	val color: HexColor,
)
