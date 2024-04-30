package org.cdb.homunculus.models.embed

import kotlinx.serialization.Serializable

@Serializable
data class SubSubUnit(
	val value: Float,
	val metric: Metric,
)
