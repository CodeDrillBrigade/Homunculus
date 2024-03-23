package org.cdb.labwitch.logic

import org.cdb.labwitch.models.User
import org.cdb.labwitch.models.UserCreationData
import org.cdb.labwitch.models.types.EntityId

interface UserLogic {
    /**
     * Creates a new user in the system.
     *
     * @param creationData a [UserCreationData].
     * @return the created [User]
     */
    suspend fun registerUser(creationData: UserCreationData): User

    /**
     * Retrieves a [User] by id.
     *
     * @param userId the id of the [User].
     * @return the [User].
     */
    suspend fun get(userId: EntityId): User
}
