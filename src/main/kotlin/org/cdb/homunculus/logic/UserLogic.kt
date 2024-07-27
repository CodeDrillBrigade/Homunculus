package org.cdb.homunculus.logic

import kotlinx.coroutines.flow.Flow
import org.cdb.homunculus.exceptions.NotFoundException
import org.cdb.homunculus.models.User
import org.cdb.homunculus.models.embed.Role
import org.cdb.homunculus.models.embed.UserStatus
import org.cdb.homunculus.models.identifiers.EntityId

interface UserLogic {
	/**
	 * Invites a new user to the system.
	 * It creates a bare [User] with just email and a temporary token that lasts one day and then
	 * sends the invitation email to the user.
	 *
	 * @param user the [User] to create.
	 * @param inviterId the id of the user doing the invitation.
	 * @throws IllegalArgumentException if the provided user does not have an email.
	 */
	suspend fun inviteUser(
		user: User,
		inviterId: EntityId,
	)

	/**
	 * Updates a [User] in the system. it will automatically ignore any update
	 * to [User.authenticationTokens], and [User.roles].
	 *
	 * @param user the user to update.
	 * @throws NotFoundException if there is no user with such an id in the system.
	 */
	suspend fun modify(user: User)

	/**
	 * Retrieves a [User] by [User.id].
	 *
	 * @param userId the id of the [User].
	 * @return the [User].
	 * @throws NotFoundException if no user with such an id exist.
	 */
	suspend fun get(userId: EntityId): User

	/**
	 * Retrieves a [User] by [User.email].
	 *
	 * @param email the email of the user.
	 * @param excludeRegistering if true, the users where [User.status] is [UserStatus.REGISTERING] will be excluded.
	 * @return the [User].
	 * @throws NotFoundException if no user with such an email exist.
	 */
	suspend fun getByEmail(
		email: String,
		excludeRegistering: Boolean,
	): User

	/**
	 * Retrieves a [User] by [User.username].
	 *
	 * @param username the username of the user.
	 * @return the [User].
	 * @throws NotFoundException if no user with such an username exist.
	 */
	suspend fun getByUsername(username: String): User

	/**
	 * Changes the [User.passwordHash] for a [User]. It also removes any expired [User.authenticationTokens].
	 *
	 * @param userId the id of the user for which to change the password.
	 * @param newPassword the new password.
	 * @return true if the update was successful, false otherwise.
	 * @throws NotFoundException if no user is found with the provided id.
	 */
	suspend fun changePassword(
		userId: EntityId,
		newPassword: String,
	): Boolean

	/**
	 * Sets the [User.role] of the user with id [userId] to [roleId].
	 *
	 * @param userId the id of the [User] to update.
	 * @param roleId the id of the [Role] to set.
	 */
	suspend fun setRole(
		userId: EntityId,
		roleId: EntityId,
	)

	/**
	 * Retrieves all the [User]s where any of [User.username], [User.email], [User.name], or [User.surname] start with [query].
	 *
	 * @param query the prefix to search.
	 * @return a [Flow] of [User]s.
	 */
	fun getByUsernameEmailName(query: String): Flow<User>

	/**
	 * Retrieves multiple [User]s by their [User.id].
	 *
	 * @param ids the ids of the [User]s to retrieve. All the ids that do not correspond to an actual material are ignored.
	 * @return a [Flow] of [User]s.
	 */
	fun getByIds(ids: Set<EntityId>): Flow<User>
}
