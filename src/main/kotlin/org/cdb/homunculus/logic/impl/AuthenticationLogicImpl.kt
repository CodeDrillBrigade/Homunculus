package org.cdb.homunculus.logic.impl

import com.github.benmanes.caffeine.cache.Caffeine
import org.cdb.homunculus.components.JWTManager
import org.cdb.homunculus.components.PasswordEncoder
import org.cdb.homunculus.dao.RoleDao
import org.cdb.homunculus.dao.UserDao
import org.cdb.homunculus.exceptions.UnauthorizedException
import org.cdb.homunculus.logic.AuthenticationLogic
import org.cdb.homunculus.models.User
import org.cdb.homunculus.models.embed.UserStatus
import org.cdb.homunculus.models.identifiers.EntityId
import org.cdb.homunculus.models.security.AuthResponse
import org.cdb.homunculus.models.security.JWTClaims
import org.cdb.homunculus.models.security.JWTRefreshClaims
import org.cdb.homunculus.utils.DynamicBitArray
import java.util.Date
import java.util.UUID
import java.util.concurrent.TimeUnit

class AuthenticationLogicImpl(
	private val userDao: UserDao,
	private val roleDao: RoleDao,
	private val passwordEncoder: PasswordEncoder,
	private val jwtManager: JWTManager,
) : AuthenticationLogic {
	private val tokenCache = Caffeine.newBuilder().expireAfterWrite(10, TimeUnit.MINUTES).build<String, String>()

	private suspend fun User.permissionsAsBitArray(): DynamicBitArray =
		role?.let {
			roleDao.getById(it)
		}?.permissions?.let {
			DynamicBitArray.fromPermissions(it)
		} ?: DynamicBitArray.bitVectorOfSize(0)

	private fun User.matchPasswordOrToken(password: String): Boolean =
		listOfNotNull(
			passwordHash,
			*authenticationTokens.values.filter { it.expirationDate > Date() }.map { it.token }.toTypedArray(),
		).any { passwordEncoder.checkHash(password, it) }

	private suspend fun getFirstMatchingOrNull(
		identifier: String,
		password: String,
	): User? =
		listOfNotNull(
			userDao.getByUsername(identifier),
			userDao.getByEmail(identifier),
		).filterNot {
			it.status == UserStatus.INACTIVE
		}.firstOrNull {
			it.matchPasswordOrToken(password)
		}

	override suspend fun login(
		identifier: String,
		password: String,
	): AuthResponse =
		getFirstMatchingOrNull(identifier, password)?.let { user ->
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

	override fun generateOTT(
		method: String,
		path: String,
	): String =
		UUID.randomUUID().toString().substring(0, 6).also {
			tokenCache.put(it, "$method:$path")
		}

	override fun consumeOTT(
		token: String,
		method: String,
		path: String,
	): Boolean =
		tokenCache.getIfPresent(token)?.also {
			tokenCache.invalidate(token)
		} == "$method:$path"
}
