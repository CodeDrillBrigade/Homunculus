package org.cdb.labwitch.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.cdb.labwitch.models.embed.Contact
import org.cdb.labwitch.models.identifiers.EntityId

/**
 * Describes a user in the system.
 *
 * @param id an [EntityId].
 * @param username the username for the user.
 * @param passwordHash the hashed and salted password for the user.
 * @param name the name of the user.
 * @param surname the surname of the user.
 * @param roles a [Set] of [EntityId] of roles assigned to this user.
 * @param contacts a [List] of [Contact]s for the user.
 */
@Serializable
data class User(
    @SerialName("_id") override val id: EntityId,
    val username: String,
    val passwordHash: String,
    val name: String,
    val surname: String,
    val roles: Set<EntityId> = emptySet(),
    val contacts: List<Contact> = emptyList(),
) : StoredEntity
