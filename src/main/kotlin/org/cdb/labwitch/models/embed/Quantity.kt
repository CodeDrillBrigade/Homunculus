package org.cdb.labwitch.models.embed

import org.cdb.labwitch.models.Metric
import org.cdb.labwitch.models.SubUnit


data class Quantity(
	val quantity: Int,
	val metric: Metric,
	val subUnit: SubUnit?
)