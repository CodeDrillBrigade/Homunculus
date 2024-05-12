package org.cdb.homunculus.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.cdb.homunculus.models.embed.ProcessStatus
import org.cdb.homunculus.models.embed.ProcessType
import org.cdb.homunculus.models.identifiers.EntityId
import org.cdb.homunculus.serialization.DateSerializer
import java.util.Date

@Serializable
data class Process(
	@SerialName("_id") override val id: EntityId = EntityId.generate(),
	val type: ProcessType,
	val status: ProcessStatus,
	@Serializable(with = DateSerializer::class) val started: Date,
	val userId: EntityId? = null,
) : StoredEntity
