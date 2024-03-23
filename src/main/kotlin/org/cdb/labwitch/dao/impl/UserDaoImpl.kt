package org.cdb.labwitch.dao.impl

import com.mongodb.client.model.Filters.eq
import org.cdb.labwitch.components.DBClient
import org.cdb.labwitch.dao.UserDao
import org.cdb.labwitch.models.User
import org.cdb.labwitch.models.types.EntityId

class UserDaoImpl(client: DBClient) : UserDao(client) {
    override suspend fun get(id: EntityId): User? = get(eq("_id", id.id))

    override suspend fun getByUsername(username: String): User? = get(eq(User::username.name, username))
}
