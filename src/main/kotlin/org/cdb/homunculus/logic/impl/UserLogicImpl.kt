package org.cdb.homunculus.logic.impl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import org.cdb.homunculus.components.Mailer
import org.cdb.homunculus.components.PasswordEncoder
import org.cdb.homunculus.dao.UserDao
import org.cdb.homunculus.exceptions.NotFoundException
import org.cdb.homunculus.logic.UserLogic
import org.cdb.homunculus.models.User
import org.cdb.homunculus.models.embed.UserStatus
import org.cdb.homunculus.models.identifiers.EntityId
import org.cdb.homunculus.models.security.AuthToken
import java.util.Date
import java.util.UUID
import kotlin.time.Duration.Companion.days

class UserLogicImpl(
	private val userDao: UserDao,
	private val passwordEncoder: PasswordEncoder,
	private val mailer: Mailer,
) : UserLogic {
	override suspend fun inviteUser(
		user: User,
		inviterId: EntityId,
	) {
		val inviter =
			checkNotNull(userDao.getById(inviterId)) {
				"Inviter does not exist: $inviterId"
			}
		requireNotNull(user.email) {
			"You must specify an email to invite a user."
		}
		require(userDao.getByEmail(user.email) == null) {
			"A user with the provided email already exists: ${user.email}"
		}
		val registrationToken = UUID.randomUUID().toString()
		val userWithHashedCredentials =
			user.removeExpiredTokens().copy(
				passwordHash = null,
				status = UserStatus.REGISTERING,
				authenticationTokens =
					mapOf(
						"registration" to
							AuthToken(
								token = passwordEncoder.hashAndSaltPassword(registrationToken),
								expirationDate = Date(System.currentTimeMillis() + 1L.days.inWholeMilliseconds),
							),
					),
			)
		checkNotNull(userDao.save(userWithHashedCredentials)) { "User creation failed" }
		mailer.sendInvitationEmail(user.email, registrationToken, inviter.name ?: inviter.username)
	}

	override suspend fun modify(user: User) {
		val currentUser =
			userDao.getById(user.id)?.removeExpiredTokens()
				?: throw NotFoundException("User ${user.id} not found")
		require(
			currentUser.username == user.username ||
				userDao.getByUsername(user.username) == null,
		) { "The provided username already exist: ${user.username}" }
		val newPasswordHash =
			when {
				user.passwordHash != null && !passwordEncoder.isHashed(user.passwordHash) -> passwordEncoder.hashAndSaltPassword(user.passwordHash)
				user.passwordHash != null && passwordEncoder.isHashed(user.passwordHash) -> user.passwordHash
				else -> currentUser.passwordHash
			}
		userDao.update(
			currentUser.copy(
				username = user.username,
				passwordHash = newPasswordHash,
				status = user.status ?: currentUser.status,
				name = user.name ?: currentUser.name,
				surname = user.surname ?: currentUser.surname,
				email = user.email ?: currentUser.email,
			),
		)
	}

	override suspend fun get(userId: EntityId): User = userDao.getById(userId) ?: throw NotFoundException("User $userId not found")

	override suspend fun getByEmail(email: String): User = userDao.getByEmail(email) ?: throw NotFoundException("User $email not found")

	override suspend fun getByUsername(username: String): User =
		userDao.getByUsername(username) ?: throw NotFoundException("User $username not found")

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

	override fun getByUsernameEmailName(query: String): Flow<User> =
		userDao.get().filter { user ->
			listOfNotNull(user.username, user.email, user.name, user.surname).any {
				it.lowercase().startsWith(query.lowercase())
			} && user.status == UserStatus.ACTIVE
		}

	override fun getByIds(ids: Set<EntityId>): Flow<User> = userDao.getByIds(ids)
}
