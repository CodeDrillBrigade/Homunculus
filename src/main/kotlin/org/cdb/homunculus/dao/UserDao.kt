package org.cdb.homunculus.dao

import com.mongodb.kotlin.client.coroutine.MongoCollection
import org.cdb.homunculus.components.DBClient
import org.cdb.homunculus.models.User
import org.cdb.homunculus.models.identifiers.EntityId

abstract class UserDao(client: DBClient) : GenericDao<User>(client) {
	override val collection: MongoCollection<User> = client.getCollection()

	override fun wrapIdentifier(id: String): EntityId = EntityId(id)

	/**
	 * Retrieves a [User] by username.
	 *
	 * @param username the username of the user to retrieve.
	 * @return the [User], if one exists with the specified username, and null otherwise.
	 */
	abstract suspend fun getByUsername(username: String): User?
}
