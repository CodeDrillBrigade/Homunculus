package org.cdb.labwitch.models;

import kotlinx.serialization.Serializable;
import org.cdb.labwitch.models.embed.BoxDefinition
import org.cdb.labwitch.models.embed.Note
import org.cdb.labwitch.models.embed.Tag


@Serializable
data class MaterialCreationData(
	val id: String,
	val name: String,
	val boxDefinition: BoxDefinition,
	val brand: String,
	val tags: Set<Tag>,
	val description: String,
	val noteList : List<Note>
)