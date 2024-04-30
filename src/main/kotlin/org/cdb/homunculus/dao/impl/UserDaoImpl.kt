package org.cdb.homunculus.dao.impl

import com.mongodb.client.model.Filters.eq
import org.cdb.homunculus.components.DBClient
import org.cdb.homunculus.dao.UserDao
import org.cdb.homunculus.models.User

class UserDaoImpl(client: DBClient) : UserDao(client) {
	override suspend fun getByUsername(username: String): User? = get(eq(User::username.name, username))
}
