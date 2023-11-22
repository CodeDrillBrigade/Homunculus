package org.cdb.labwitch.models

import kotlinx.serialization.Serializable
import org.cdb.labwitch.models.embed.Contact
import org.cdb.labwitch.models.embed.Role

/**
 * Describes a user in the system.
 */
@Serializable
data class User(
    override val id: String,
    val username: String,
    val passwordHash: String,
    val name: String,
    val surname: String,
    val roles: Set<Role> = emptySet(),
    val contacts: List<Contact> = emptyList()
) : StoredEntity
