package org.cdb.homunculus.logic

import org.cdb.homunculus.exceptions.NotFoundException
import org.cdb.homunculus.models.User
import org.cdb.homunculus.models.identifiers.EntityId

interface UserLogic {
	/**
	 * Creates a new user in the system.
	 *
	 * @param user the [User] to create.
	 * @return the created [User].
	 */
	suspend fun registerUser(user: User): User

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
	 * @return the [User].
	 * @throws NotFoundException if no user with such an email exist.
	 */
	suspend fun getByEmail(email: String): User

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
}
