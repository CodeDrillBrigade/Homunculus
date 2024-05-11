package org.cdb.homunculus.logic.impl

import org.cdb.homunculus.components.JWTManager
import org.cdb.homunculus.components.PasswordEncoder
import org.cdb.homunculus.dao.RoleDao
import org.cdb.homunculus.dao.UserDao
import org.cdb.homunculus.exceptions.UnauthorizedException
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

	private fun User.matchPasswordOrToken(password: String): Boolean =
		listOfNotNull(
			passwordHash,
			*authenticationTokens.values.filter { it.expirationDate > System.currentTimeMillis() }.map { it.token }.toTypedArray(),
		).any { passwordEncoder.checkHash(password, it) }

	override suspend fun login(
		username: String,
		password: String,
	): AuthResponse =
		userDao.getByUsername(username)?.takeIf {
			it.matchPasswordOrToken(password)
		}?.let { user ->
			AuthResponse(
				jwt = jwtManager.generateAuthJWT(JWTClaims(user.id, user.permissionsAsBitArray())),
				refreshJwt = jwtManager.generateRefreshJWT(JWTRefreshClaims(user.id)),
			)
		} ?: throw UnauthorizedException("Invalid username or password")

	override suspend fun refresh(userId: EntityId): AuthResponse =
		userDao.getById(userId)?.let { user ->
			AuthResponse(
				jwt = jwtManager.generateAuthJWT(JWTClaims(user.id, user.permissionsAsBitArray())),
				refreshJwt = null,
			)
		} ?: throw UnauthorizedException("It is not possible to refresh the token for the user $userId")
}
