package org.cdb.homunculus.logic

import org.cdb.homunculus.models.identifiers.EntityId
import org.cdb.homunculus.models.security.AuthResponse
import org.cdb.homunculus.models.User
import org.cdb.homunculus.exceptions.UnauthorizedException

/**
 * Defines the operation to authenticate and de-authenticate a user.
 */
interface AuthenticationLogic {
	/**
	 * Logins a user based on username and password.
	 *
	 * @param username the username.
	 * @param password the [User.passwordHash] or a valid [User.authenticationTokens].
	 * @return an [AuthResponse] if the authentication succeeded.
	 * @throws UnauthorizedException if it is not possible to authenticate the user
	 */
	suspend fun login(
		username: String,
		password: String,
	): AuthResponse

	/**
	 * Refreshes the JWT of a user.
	 *
	 * @param userId the user id.
	 * @return an [AuthResponse] with a null [AuthResponse.refreshJwt].
	 * @throws UnauthorizedException if it is not possible to refresh the token for the user.
	 */
	suspend fun refresh(userId: EntityId): AuthResponse
}
