package org.cdb.homunculus.logic

import org.cdb.homunculus.models.identifiers.EntityId
import org.cdb.homunculus.models.security.AuthResponse

/**
 * Defines the operation to authenticate and de-authenticate a user.
 */
interface AuthenticationLogic {
	/**
	 * Logins a user based on username and password.
	 *
	 * @param username the username.
	 * @param password the password.
	 * @return an [AuthResponse] if the authentication succeeded.
	 * @throws //TODO
	 */
	suspend fun login(
		username: String,
		password: String,
	): AuthResponse

	/**
	 * Refreshes the JWT of a user.
	 *
	 * @param username the username.
	 * @return an [AuthResponse] with a null [AuthResponse.refreshJwt].
	 */
	suspend fun refresh(username: EntityId): AuthResponse
}
