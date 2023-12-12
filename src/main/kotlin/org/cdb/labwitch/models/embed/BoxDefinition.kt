package org.cdb.labwitch.models.embed

import kotlinx.serialization.Serializable

@Serializable
data class BoxDefinition(
	val quantity: Int,
	val metric: Metric,
	val subUnit: SubUnit?,
	val brand: String,
	val tags: Set<Tag>,
	val description: String,
	val noteList: List<Note>
)