package org.cdb.homunculus.dao.impl

import com.mongodb.client.model.Filters.eq
import kotlinx.coroutines.flow.Flow
import org.cdb.homunculus.annotations.Index
import org.cdb.homunculus.components.DBClient
import org.cdb.homunculus.dao.UserDao
import org.cdb.homunculus.models.User

class UserDaoImpl(client: DBClient) : UserDao(client) {
	@Index(name = "by_username", property = "username", unique = true)
	override suspend fun getByUsername(username: String): User? = get(eq(User::username.name, username))

	@Index(name = "by_email", property = "email", unique = false)
	override fun findByEmail(email: String): Flow<User> = find(eq(User::email.name, email))
}
