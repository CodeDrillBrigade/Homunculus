package org.cdb.homunculus.models.embed

import kotlinx.serialization.Serializable
import org.cdb.homunculus.models.identifiers.EntityId
import org.cdb.homunculus.serialization.DateSerializer
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
	override fun compareTo(other: UsageLog): Int = other.date.compareTo(date)
}
