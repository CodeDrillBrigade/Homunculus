package org.cdb.homunculus.logic.impl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import org.cdb.homunculus.components.Mailer
import org.cdb.homunculus.components.PasswordEncoder
import org.cdb.homunculus.dao.RoleDao
import org.cdb.homunculus.dao.UserDao
import org.cdb.homunculus.exceptions.NotFoundException
import org.cdb.homunculus.logic.UserLogic
import org.cdb.homunculus.models.User
import org.cdb.homunculus.models.embed.UserStatus
import org.cdb.homunculus.models.identifiers.EntityId
import org.cdb.homunculus.models.security.AuthToken
import org.cdb.homunculus.utils.exist
import java.util.Date
import java.util.UUID
import kotlin.time.Duration.Companion.days

class UserLogicImpl(
	private val roleDao: RoleDao,
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
		val retrieved = userDao.getByEmail(user.email)
		val (userOrRetrievedUser, existing) =
			when {
				retrieved == null -> user to false
				retrieved.status == UserStatus.REGISTERING ->
					retrieved.copy(
						role = user.role,
					) to true
				else -> throw IllegalArgumentException("A user with the provided email already exists: ${user.email}")
			}
		val registrationToken = UUID.randomUUID().toString()
		val userWithHashedCredentials =
			userOrRetrievedUser.removeExpiredTokens().copy(
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
		if (existing) {
			checkNotNull(userDao.update(userWithHashedCredentials)) { "User creation failed" }
		} else {
			checkNotNull(userDao.save(userWithHashedCredentials)) { "User creation failed" }
		}
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
		require(
			user.email == null || currentUser.email == user.email ||
				userDao.getByEmail(user.email) == null,
		) { "The provided email already exist: ${user.email}" }
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
				profilePicture = user.profilePicture ?: currentUser.profilePicture,
			),
		)
	}

	override suspend fun get(userId: EntityId): User = exist({ userDao.getById(userId) }) { "User $userId not found" }

	override suspend fun getByEmail(
		email: String,
		excludeRegistering: Boolean,
	): User =
		exist({
			userDao.getByEmail(email)?.takeIf {
				!excludeRegistering || it.status != UserStatus.REGISTERING
			}
		}) { "User $email not found" }

	override suspend fun getByUsername(username: String): User = exist({ userDao.getByUsername(username) }) { "User $username not found" }

	override suspend fun changePassword(
		userId: EntityId,
		newPassword: String,
	): Boolean {
		val user = exist({ userDao.getById(userId)?.removeExpiredTokens() }) { "User $userId not found" }
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

	override suspend fun setRole(
		userId: EntityId,
		roleId: EntityId,
	) {
		val user = exist({ userDao.getById(userId)?.removeExpiredTokens() }) { "User $userId not found" }
		val role = exist({ roleDao.getById(roleId) }) { "Role $roleId not found" }
		userDao.update(
			user.copy(
				role = role.id,
			),
		)
	}

	override fun getByUsernameEmailName(query: String): Flow<User> =
		userDao.get().filter { user ->
			listOfNotNull(user.username, user.email, user.name, user.surname).any {
				it.lowercase().startsWith(query.lowercase())
			} && user.status == UserStatus.ACTIVE
		}

	override fun getByIds(ids: Set<EntityId>): Flow<User> = userDao.getByIds(ids)
}
