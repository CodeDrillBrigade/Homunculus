package org.cdb.labwitch.dao

import com.mongodb.kotlin.client.coroutine.MongoCollection
import org.cdb.labwitch.components.DBClient
import org.cdb.labwitch.models.User

abstract class UserDao(client: DBClient) : GenericDao<User>(client) {

    override val collection: MongoCollection<User> = client.getCollection()

    /**
     * Retrieves a [User] by id.
     *
     * @param id the id of the user to retrieve.
     * @return the [User], if one exists with the specified id, and null otherwise.
     */
    abstract suspend fun get(id: String): User?
}