package org.cdb.labwitch.models

import kotlinx.serialization.SerialName
import org.cdb.labwitch.models.embed.Note
import org.cdb.labwitch.models.identifiers.EntityId

data class Material(
	@SerialName("_id") override val id: EntityId = EntityId.generate(),
	val name: String,
	val boxDefinition: EntityId,
	val brand: String,
	val description: String? = null,
	val tags: Set<EntityId> = emptySet(),
	val noteList: List<Note> = emptyList(),
	val normalizedName: String? = null,
) : StoredEntity
