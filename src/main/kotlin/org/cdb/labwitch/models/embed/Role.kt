package org.cdb.labwitch.models.embed

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.cdb.labwitch.models.StoredEntity
import org.cdb.labwitch.models.security.Permissions
import org.cdb.labwitch.models.types.EntityId

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
