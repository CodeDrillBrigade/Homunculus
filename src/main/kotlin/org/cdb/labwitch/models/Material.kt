package org.cdb.labwitch.models

import java.util.*

class Material
(
	val name: String,
	val boxDefinition: BoxDefinition,
	val brand: String,
	val tags: Set<Tag>,
	val description: String,
	val noteList: List<X>
)

// ?
class Tag
(
	val tagName: String
)

class X
(
	val user: User,
	val date: Date,
	val message: String
)

class BoxDefinition(
	val quantity: Int,
	val metric: Metric,
	val subUnit: SubUnit?,
	val brand: String,
	val tags: Set<Tag>,
	val description: String,
	val noteList: List<Note>
)

class Note(
	val user: User,
	val date: Date,
	val message: String
)