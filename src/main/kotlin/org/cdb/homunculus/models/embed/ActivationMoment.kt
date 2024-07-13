package org.cdb.homunculus.models.embed

import kotlinx.serialization.Serializable

@Serializable
data class ActivationMoment(
	val day: WeekDay,
	val hour: Int,
)
