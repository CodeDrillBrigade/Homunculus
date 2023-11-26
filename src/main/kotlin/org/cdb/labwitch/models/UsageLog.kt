package org.cdb.labwitch.models

import java.util.Date
import java.util.UUID

data class UsageLog
(
	val date: Date,
	val user: UUID,
	val box: UUID,
	val operation: Operation,
	val quantity: Quantity,
)

enum class Operation
{
	// TODO
}

data class Quantity(
	val quantity: Int,
	val metric: Metric,
	val subUnit: SubUnit?
)