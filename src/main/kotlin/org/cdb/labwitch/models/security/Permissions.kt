package org.cdb.labwitch.models.security

import kotlinx.serialization.Serializable

/**
 * Permission can be assigned to roles to allow users to access certain functionalities.
 * Warning: never change the index of a permission.
 */
@Serializable
enum class Permissions(val index: Int) {
    ADMIN(0),
}
