package org.cdb.homunculus.logic.impl

import org.cdb.homunculus.components.JWTManager
import org.cdb.homunculus.components.PasswordEncoder
import org.cdb.homunculus.dao.RoleDao
import org.cdb.homunculus.dao.UserDao
import org.cdb.homunculus.exceptions.NotFoundException
import org.cdb.homunculus.logic.AuthenticationLogic
import org.cdb.homunculus.models.User
import org.cdb.homunculus.models.identifiers.EntityId
import org.cdb.homunculus.models.security.AuthResponse
import org.cdb.homunculus.models.security.JWTClaims
import org.cdb.homunculus.models.security.JWTRefreshClaims
import org.cdb.homunculus.utils.DynamicBitArray

class AuthenticationLogicImpl(
	private val userDao: UserDao,
	private val roleDao: RoleDao,
	private val passwordEncoder: PasswordEncoder,
	private val jwtManager: JWTManager,
) : AuthenticationLogic {
	private suspend fun User.permissionsAsBitArray(): DynamicBitArray =
		roles.mapNotNull {
			roleDao.getById(it)
		}.flatMap {
			it.permissions
		}.toSet().let { DynamicBitArray.fromPermissions(it) }

	override suspend fun login(
		username: String,
		password: String,
	): AuthResponse {
		val user =
			userDao.getByUsername(username)
				?: throw NotFoundException("User $username does not exist")
		return if (passwordEncoder.checkHash(password, user.passwordHash)) {
			AuthResponse(
				jwt = jwtManager.generateAuthJWT(JWTClaims(user.id, user.permissionsAsBitArray())),
				refreshJwt = jwtManager.generateRefreshJWT(JWTRefreshClaims(user.id)),
			)
		} else {
			throw IllegalStateException("Wrong password")
		}
	}

	override suspend fun refresh(username: EntityId): AuthResponse {
		val user =
			userDao.getById(username)
				?: throw NotFoundException("User $username does not exist")
		return AuthResponse(
			jwt = jwtManager.generateAuthJWT(JWTClaims(user.id, user.permissionsAsBitArray())),
			refreshJwt = null,
		)
	}
}
