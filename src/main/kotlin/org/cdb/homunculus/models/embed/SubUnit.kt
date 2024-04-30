package org.cdb.homunculus.models.embed

import kotlinx.serialization.Serializable

@Serializable
data class SubUnit(
	val quantity: Int,
	val subUnit: SubSubUnit?,
)
