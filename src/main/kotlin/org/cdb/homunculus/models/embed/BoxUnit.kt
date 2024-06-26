package org.cdb.homunculus.models.embed

import kotlinx.serialization.Serializable

@Serializable
data class BoxUnit(
	val quantity: Int,
	val metric: Metric? = null,
	val boxUnit: BoxUnit? = null,
)
