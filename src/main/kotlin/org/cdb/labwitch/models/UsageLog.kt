package org.cdb.labwitch.models

import org.cdb.labwitch.models.embed.Operation
import org.cdb.labwitch.models.embed.Quantity
import java.util.Date

data class UsageLog
(
	val date: Date,
	val user: String,
	val box: String,
	val operation: Operation,
	val quantity: Quantity,
)
