package org.cdb.labwitch.models

import java.util.Date
import java.util.UUID

class UsageLog
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

class Quantity(
	val quantity: Int,
	val metric: Metric,
	val subUnit: SubUnit?
)