package org.cdb.labwitch.dao

import com.mongodb.kotlin.client.coroutine.MongoCollection
import org.cdb.labwitch.components.DBClient
import org.cdb.labwitch.models.User
import org.cdb.labwitch.models.identifiers.EntityId

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
