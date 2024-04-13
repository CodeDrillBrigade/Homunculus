package org.cdb.labwitch.models.embed

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.cdb.labwitch.models.StoredEntity
import org.cdb.labwitch.models.identifiers.EntityId
import org.cdb.labwitch.models.types.HexColor
import org.cdb.labwitch.models.types.ShortText

@Serializable
data class Tag(
	@SerialName("_id") override val id: EntityId = EntityId.generate(),
	val name: ShortText,
	val color: HexColor,
) : StoredEntity
