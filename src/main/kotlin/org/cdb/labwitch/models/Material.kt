package org.cdb.labwitch.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.cdb.labwitch.models.embed.Note
import org.cdb.labwitch.models.identifiers.EntityId
import org.cdb.labwitch.models.types.ShortText

@Serializable
data class Material(
	@SerialName("_id") override val id: EntityId = EntityId.generate(),
	val name: ShortText,
	val boxDefinition: EntityId,
	val brand: String,
	val description: String? = null,
	val tags: Set<EntityId> = emptySet(),
	val noteList: List<Note> = emptyList(),
	val normalizedName: String? = null,
) : StoredEntity
