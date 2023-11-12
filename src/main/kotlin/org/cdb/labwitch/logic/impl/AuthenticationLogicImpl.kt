package org.cdb.labwitch.logic.impl

import org.cdb.labwitch.components.JWTManager
import org.cdb.labwitch.components.PasswordEncoder
import org.cdb.labwitch.dao.UserDao
import org.cdb.labwitch.logic.AuthenticationLogic
import org.cdb.labwitch.models.security.AuthResponse
import org.cdb.labwitch.models.security.JWTClaims

class AuthenticationLogicImpl(
    private val userDao: UserDao,
    private val passwordEncoder: PasswordEncoder,
    private val jwtManager: JWTManager
) : AuthenticationLogic {

    override suspend fun login(username: String, password: String): AuthResponse {
        val user = userDao.getByUsername(username)!!
        return if(passwordEncoder.checkHash(password, user.passwordHash)) {
            AuthResponse(
                jwt = jwtManager.generateAuthJWT(JWTClaims(user.id))
            )
        } else throw IllegalStateException("Wrong password")
    }

}