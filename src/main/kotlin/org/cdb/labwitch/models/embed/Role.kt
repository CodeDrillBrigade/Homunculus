package org.cdb.labwitch.models.embed

import kotlinx.serialization.Serializable

/**
 * Defines a role in the system,
 */
@Serializable
data class Role(
    val id: String,
    val name: String,
    val description: String
)