package org.cdb.labwitch.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.cdb.labwitch.models.embed.BoxUnit
import org.cdb.labwitch.models.embed.UsageLog
import org.cdb.labwitch.models.identifiers.EntityId
import org.cdb.labwitch.models.identifiers.HierarchicalId
import org.cdb.labwitch.serialization.DateSerializer
import java.util.Date
import java.util.SortedSet

@Serializable
data class Box(
	@SerialName("_id") override val id: EntityId = EntityId.generate(),
	val material: EntityId,
	val quantity: BoxUnit,
	val position: HierarchicalId,
	@Serializable(with = DateSerializer::class) val expirationDate: Date? = null,
	val description: String? = null,
	val usageLog: SortedSet<UsageLog> = sortedSetOf(),
) : StoredEntity
