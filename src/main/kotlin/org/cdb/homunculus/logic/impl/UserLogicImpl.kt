package org.cdb.homunculus.logic.impl

import org.cdb.homunculus.components.PasswordEncoder
import org.cdb.homunculus.dao.UserDao
import org.cdb.homunculus.exceptions.NotFoundException
import org.cdb.homunculus.logic.UserLogic
import org.cdb.homunculus.models.User
import org.cdb.homunculus.models.identifiers.EntityId

class UserLogicImpl(
	private val userDao: UserDao,
	private val passwordEncoder: PasswordEncoder,
) : UserLogic {
	override suspend fun registerUser(user: User): User {
		val userWithHashedCredentials =
			user.removeExpiredTokens().copy(
				passwordHash =
					user.passwordHash?.let {
						if (!passwordEncoder.isHashed(user.passwordHash)) {
							passwordEncoder.hashAndSaltPassword(user.passwordHash)
						} else {
							user.passwordHash
						}
					},
				authenticationTokens =
					user.authenticationTokens.mapValues { (_, v) ->
						if (!passwordEncoder.isHashed(v.token)) {
							v.copy(
								token = passwordEncoder.hashAndSaltPassword(v.token),
							)
						} else {
							v
						}
					},
			)
		val createdId =
			checkNotNull(userDao.save(userWithHashedCredentials)) {
				"User creation failed"
			}
		return checkNotNull(userDao.getById(createdId)) { "User retrieval failed" }
	}

	override suspend fun get(userId: EntityId): User = userDao.getById(userId) ?: throw NotFoundException("User $userId not found")

	override suspend fun getByEmail(email: String): User = userDao.getByEmail(email) ?: throw NotFoundException("User $email not found")

	override suspend fun changePassword(
		userId: EntityId,
		newPassword: String,
	): Boolean {
		val user = userDao.getById(userId)?.removeExpiredTokens() ?: throw NotFoundException("User $userId not found")
		return userDao.update(
			user.copy(
				passwordHash =
					if (!passwordEncoder.isHashed(newPassword)) {
						passwordEncoder.hashAndSaltPassword(newPassword)
					} else {
						newPassword
					},
			),
		) != null
	}
}
