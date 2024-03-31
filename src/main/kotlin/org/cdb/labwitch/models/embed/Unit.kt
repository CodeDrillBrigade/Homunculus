package org.cdb.labwitch.models.embed

import kotlinx.serialization.Serializable
import kotlin.Unit

@Serializable
data class Unit(
	val quantity: Int,
	val metric: Metric? = null,
	val unit: Unit? = null,
)
