package org.cdb.homunculus.models.embed

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.cdb.homunculus.models.StoredEntity
import org.cdb.homunculus.models.identifiers.EntityId
import org.cdb.homunculus.models.security.Permissions

/**
 * Defines a role in the system,
 */
@Serializable
data class Role(
	@SerialName("_id") override val id: EntityId,
	val name: String,
	val description: String,
	val permissions: Set<Permissions>,
) : StoredEntity
