package org.cdb.labwitch.logic.impl

import kotlinx.coroutines.flow.Flow
import org.cdb.labwitch.dao.RoleDao
import org.cdb.labwitch.exceptions.NotFoundException
import org.cdb.labwitch.logic.RoleLogic
import org.cdb.labwitch.models.embed.Role
import org.cdb.labwitch.models.identifiers.EntityId
import org.cdb.labwitch.models.identifiers.Identifier

class RoleLogicImpl(private val roleDao: RoleDao) : RoleLogic {
	override suspend fun get(id: EntityId): Role {
		return roleDao.getById(id) ?: throw NotFoundException("Role with id $id does not exist")
	}

	override suspend fun addRole(role: Role): Identifier {
		return roleDao.save(role)
	}

	override fun getAll(): Flow<Role> {
		return roleDao.get()
	}
}
