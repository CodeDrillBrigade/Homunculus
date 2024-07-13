package org.cdb.homunculus.models.dto

import kotlinx.serialization.Serializable
import org.cdb.homunculus.models.identifiers.EntityId

@Serializable
data class NotificationStub(
	val id: EntityId,
	val name: String,
)
