package org.cdb.homunculus.logic

import org.cdb.homunculus.exceptions.UnauthorizedException
import org.cdb.homunculus.models.User
import org.cdb.homunculus.models.identifiers.EntityId
import org.cdb.homunculus.models.security.AuthResponse

/**
 * Defines the operation to authenticate and de-authenticate a user.
 */
interface AuthenticationLogic {
	/**
	 * Logins a user based on an identifier and password. The identifier can be either the [User.username] or the [User.email]
	 *
	 * @param identifier the user identifier.
	 * @param password the [User.passwordHash] or a valid [User.authenticationTokens].
	 * @return an [AuthResponse] if the authentication succeeded.
	 * @throws UnauthorizedException if it is not possible to authenticate the user
	 */
	suspend fun login(
		identifier: String,
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

	/**
	 * @param method the HTTP method the token is valid for.
	 * @param path the path the token is valid for.
	 * @return a single-use token valid for a single operation for the following 5 minutes.
	 */
	fun generateOTT(
		method: String,
		path: String,
	): String

	/**
	 * Checks the validity of [token]. If it is valid, it will be invalidated.
	 * @param method the HTTP method the token is valid for.
	 * @param path the path the token is valid for.
	 * @return true if the token is valid, false otherwise.
	 */
	fun consumeOTT(
		token: String,
		method: String,
		path: String,
	): Boolean
}
