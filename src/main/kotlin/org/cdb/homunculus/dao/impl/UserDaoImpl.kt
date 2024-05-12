package org.cdb.homunculus.dao.impl

import com.mongodb.client.model.Filters.eq
import org.cdb.homunculus.annotations.Index
import org.cdb.homunculus.components.DBClient
import org.cdb.homunculus.dao.UserDao
import org.cdb.homunculus.models.User

class UserDaoImpl(client: DBClient) : UserDao(client) {
	@Index(name = "by_username", property = "username", unique = true)
	override suspend fun getByUsername(username: String): User? = get(eq(User::username.name, username))

	@Index(name = "by_email", property = "email", unique = true)
	override suspend fun getByEmail(email: String): User? = get(eq(User::email.name, email))
}
