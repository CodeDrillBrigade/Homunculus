package org.cdb.homunculus.models.security

import kotlinx.serialization.Serializable

/**
 * Permission can be assigned to roles to allow users to access certain functionalities.
 * Warning: never change the index of a permission.
 */
@Serializable
enum class Permissions(val index: Int) {
	ADMIN(0),
	MANAGE_STORAGE(1),
	MANAGE_MATERIALS(2),
	MANAGE_METADATA(3),
}
