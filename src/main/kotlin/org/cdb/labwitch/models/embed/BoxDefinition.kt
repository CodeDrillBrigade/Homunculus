package org.cdb.labwitch.models.embed

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.cdb.labwitch.models.StoredEntity
import org.cdb.labwitch.models.identifiers.EntityId

@Serializable
data class BoxDefinition(
	@SerialName("_id") override val id: EntityId = EntityId.generate(),
	val name: String,
	val description: String? = null,
	val unit: Unit,
) : StoredEntity
