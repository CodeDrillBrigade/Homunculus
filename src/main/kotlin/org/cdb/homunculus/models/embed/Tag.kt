package org.cdb.homunculus.models.embed

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.cdb.homunculus.models.StoredEntity
import org.cdb.homunculus.models.identifiers.EntityId
import org.cdb.homunculus.models.types.HexColor
import org.cdb.homunculus.models.types.ShortText

@Serializable
data class Tag(
	@SerialName("_id") override val id: EntityId = EntityId.generate(),
	val name: ShortText,
	val color: HexColor,
) : StoredEntity
