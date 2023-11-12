package org.cdb.labwitch.logic

import org.cdb.labwitch.models.security.AuthResponse

/**
 * Defines the operation to authenticate and de-authenticate a user.
 */
interface AuthenticationLogic {

    /**
     * Logins a user based on username and password.
     *
     * @param username the username.
     * @param password the password.
     * @return an [AuthResponse] if the authentication succeeded.
     * @throws //TODO
     */
    suspend fun login(username: String, password: String): AuthResponse

}