package org.cdb.labwitch.models.embed

import org.cdb.labwitch.models.types.UserId
import java.util.Date

/**
 * This class represent an addition or a removal for a material.
 * Whenever a user updates a Box, also the corresponding usage log should be updated.
 */
data class UsageLog(
	val date: Date,
	val user: UserId,
	val operation: Operation,
	val quantity: Quantity,
) : Comparable<UsageLog> {
	override fun compareTo(other: UsageLog): Int = date.compareTo(other.date)
}
