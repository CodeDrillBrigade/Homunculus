package org.cdb.labwitch.models.embed

import kotlinx.serialization.Serializable
import org.cdb.labwitch.models.identifiers.EntityId
import org.cdb.labwitch.serialization.DateSerializer
import java.util.Date

/**
 * This class represent an addition or a removal for a material.
 * Whenever a user updates a Box, also the corresponding usage log should be updated.
 */
@Serializable
data class UsageLog(
	@Serializable(with = DateSerializer::class) val date: Date,
	val user: EntityId,
	val operation: Operation,
	val quantity: BoxUnit,
) : Comparable<UsageLog> {
	override fun compareTo(other: UsageLog): Int = date.compareTo(other.date)
}
