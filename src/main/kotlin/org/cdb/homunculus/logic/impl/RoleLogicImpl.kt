package org.cdb.homunculus.logic.impl

import kotlinx.coroutines.flow.Flow
import org.cdb.homunculus.dao.RoleDao
import org.cdb.homunculus.logic.RoleLogic
import org.cdb.homunculus.models.embed.Role
import org.cdb.homunculus.models.identifiers.EntityId
import org.cdb.homunculus.utils.exist

class RoleLogicImpl(
	private val roleDao: RoleDao,
) : RoleLogic {
	override suspend fun getAll(): Flow<Role> = roleDao.get()

	override suspend fun get(roleId: EntityId): Role =
		exist({ roleDao.getById(roleId) }) {
			"Role $roleId does not exist"
		}
}
