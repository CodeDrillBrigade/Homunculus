package org.cdb.labwitch.models.embed

data class BoxDefinition(
	val quantity: Int,
	val metric: Metric,
	val subUnit: SubUnit?,
	val brand: String,
	val tags: Set<Tag>,
	val description: String,
	val noteList: List<Note>
)