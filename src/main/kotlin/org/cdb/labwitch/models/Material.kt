package org.cdb.labwitch.models

import org.cdb.labwitch.models.embed.BoxDefinition
import org.cdb.labwitch.models.embed.Note
import org.cdb.labwitch.models.embed.Tag

data class Material
(
	override val id: String,
	val name: String,
	val boxDefinition: BoxDefinition,
	val brand: String,
	val tags: Set<Tag>,
	val description: String,
	val noteList: List<Note>
) : StoredEntity

