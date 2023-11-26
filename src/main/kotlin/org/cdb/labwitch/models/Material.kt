package org.cdb.labwitch.models

import java.util.*

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

data class Tag
(
	val tagName: String
)

data class BoxDefinition(
	val quantity: Int,
	val metric: Metric,
	val subUnit: SubUnit?,
	val brand: String,
	val tags: Set<Tag>,
	val description: String,
	val noteList: List<Note>
)

data class Note(
	val user: User,
	val date: Date,
	val message: String
)