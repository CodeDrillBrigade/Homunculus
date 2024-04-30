package org.cdb.homunculus.models.embed

data class Quantity(
	val quantity: Int,
	val metric: Metric,
	val subUnit: SubUnit?,
)
