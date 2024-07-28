package org.cdb.homunculus.logic

import kotlinx.coroutines.flow.Flow
import org.cdb.homunculus.exceptions.NotFoundException
import org.cdb.homunculus.models.embed.Role
import org.cdb.homunculus.models.identifiers.EntityId

interface RoleLogic {
	/**
	 * Retrieves all the [Role]s in the database.
	 *
	 * @return a [Flow] of [Role]s.
	 */
	suspend fun getAll(): Flow<Role>

	/**
	 * Retrieves a [Role].
	 *
	 * @param roleId the [EntityId] of the role to find
	 * @return the [Role] with the specified id.
	 * @throws NotFoundException if no [Role] exists with the specified id.
	 */
	suspend fun get(roleId: EntityId): Role
}
