package org.cdb.homunculus.logic.impl

import org.cdb.homunculus.components.PasswordEncoder
import org.cdb.homunculus.dao.UserDao
import org.cdb.homunculus.logic.UserLogic
import org.cdb.homunculus.models.User
import org.cdb.homunculus.models.UserCreationData
import org.cdb.homunculus.models.identifiers.EntityId

class UserLogicImpl(
	private val userDao: UserDao,
	private val passwordEncoder: PasswordEncoder,
) : UserLogic {
	override suspend fun registerUser(creationData: UserCreationData): User {
		val userToCreate =
			User(
				id = EntityId.generate(),
				passwordHash = passwordEncoder.hashAndSaltPassword(creationData.password),
				username = creationData.username,
				name = creationData.name,
				surname = creationData.surname,
				contacts = creationData.contacts,
			)
		val createdId =
			checkNotNull(userDao.save(userToCreate)) {
				"User creation failed"
			}
		return checkNotNull(userDao.getById(createdId)) { "User retrieval failed" }
	}

	override suspend fun get(userId: EntityId): User =
		requireNotNull(userDao.getById(userId)) {
			"User is not found"
		}
}
