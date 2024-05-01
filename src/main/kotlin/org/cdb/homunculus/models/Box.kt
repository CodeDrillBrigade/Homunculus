package org.cdb.homunculus.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.cdb.homunculus.models.embed.BoxUnit
import org.cdb.homunculus.models.embed.UsageLog
import org.cdb.homunculus.models.identifiers.EntityId
import org.cdb.homunculus.models.identifiers.HierarchicalId
import org.cdb.homunculus.serialization.DateSerializer
import org.cdb.homunculus.serialization.SortedSetSerializer
import java.util.Date
import java.util.SortedSet

@Serializable
data class Box(
	@SerialName("_id") override val id: EntityId = EntityId.generate(),
	val material: EntityId,
	val quantity: BoxUnit,
	val position: HierarchicalId,
	val batchNumber: String,
	@Serializable(with = DateSerializer::class) val expirationDate: Date? = null,
	@Serializable(with = DateSerializer::class) val deleted: Date? = null,
	val description: String? = null,
	@Serializable(with = SortedSetSerializer::class) val usageLogs: SortedSet<UsageLog> = sortedSetOf(),
) : StoredEntity
