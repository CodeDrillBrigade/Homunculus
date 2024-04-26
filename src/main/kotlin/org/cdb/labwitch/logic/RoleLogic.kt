package org.cdb.labwitch.logic

import kotlinx.coroutines.flow.Flow
import org.cdb.labwitch.models.embed.Role
import org.cdb.labwitch.models.identifiers.EntityId
import org.cdb.labwitch.models.identifiers.Identifier

interface RoleLogic {
	suspend fun addRole(role: Role): Identifier

	fun getAll(): Flow<Role>

	suspend fun get(id: EntityId): Role
}
