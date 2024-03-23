package org.cdb.labwitch.logic.impl

import org.cdb.labwitch.components.PasswordEncoder
import org.cdb.labwitch.dao.UserDao
import org.cdb.labwitch.logic.UserLogic
import org.cdb.labwitch.models.User
import org.cdb.labwitch.models.UserCreationData
import org.cdb.labwitch.models.types.EntityId

class UserLogicImpl(
    private val userDao: UserDao,
    private val passwordEncoder: PasswordEncoder,
) : UserLogic {
    override suspend fun registerUser(creationData: UserCreationData): User {
        val userToCreate =
            User(
                id = EntityId.generate(),
                passwordHash = passwordEncoder.hashAndSaltPassword(creationData.password),
                username = creationData.username,
                name = creationData.name,
                surname = creationData.surname,
                contacts = creationData.contacts,
            )
        val createdId =
            checkNotNull(userDao.save(userToCreate)) {
                "User creation failed"
            }
        return checkNotNull(userDao.get(createdId)) { "User retrieval failed" }
    }

    override suspend fun get(userId: EntityId): User =
        requireNotNull(userDao.get(userId)) {
            "User is not found"
        }
}
