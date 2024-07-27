package org.cdb.homunculus.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.cdb.homunculus.models.embed.Role
import org.cdb.homunculus.models.embed.UserStatus
import org.cdb.homunculus.models.identifiers.EntityId
import org.cdb.homunculus.models.security.AuthToken
import java.util.Date

/**
 * Describes a user in the system.
 *
 * @param id an [EntityId].
 * @param username the username for the user.
 * @param passwordHash the hashed and salted password for the user.
 * @param status the [UserStatus].
 * @param name the name of the user.
 * @param surname the surname of the user.
 * @param role the [EntityId] of the [Role] assigned to this user.
 * @param authenticationTokens a [Map] of temporary [AuthToken] that can be used to login.
 */
@Serializable
data class User(
	@SerialName("_id") override val id: EntityId = EntityId.generate(),
	val username: String,
	val passwordHash: String? = null,
	val status: UserStatus? = UserStatus.ACTIVE,
	val name: String? = null,
	val surname: String? = null,
	val email: String? = null,
	val profilePicture: EntityId? = null,
	val role: EntityId? = null,
	val authenticationTokens: Map<String, AuthToken> = emptyMap(),
) : StoredEntity {
	fun redactSecrets() =
		copy(
			passwordHash = passwordHash?.let { "*" },
			authenticationTokens =
				authenticationTokens.filterValues { v ->
					v.expirationDate > Date()
				}.mapValues { (_, v) -> v.copy(token = "*") },
		)

	fun removeExpiredTokens() =
		copy(
			authenticationTokens =
				authenticationTokens.filterValues { v ->
					v.expirationDate > Date()
				},
		)
}
