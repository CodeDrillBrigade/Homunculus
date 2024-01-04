package org.cdb.labwitch.logic.impl

import org.cdb.labwitch.components.JWTManager
import org.cdb.labwitch.components.PasswordEncoder
import org.cdb.labwitch.dao.UserDao
import org.cdb.labwitch.exceptions.NotFoundException
import org.cdb.labwitch.logic.AuthenticationLogic
import org.cdb.labwitch.models.security.AuthResponse
import org.cdb.labwitch.models.security.JWTClaims
import org.cdb.labwitch.models.security.JWTRefreshClaims

class AuthenticationLogicImpl(
    private val userDao: UserDao,
    private val passwordEncoder: PasswordEncoder,
    private val jwtManager: JWTManager
) : AuthenticationLogic {

    override suspend fun login(username: String, password: String): AuthResponse {
        val user = userDao.getByUsername(username)
            ?: throw NotFoundException("User $username does not exist")
        return if(passwordEncoder.checkHash(password, user.passwordHash)) {
            AuthResponse(
                jwt = jwtManager.generateAuthJWT(JWTClaims(user.id)),
                refreshJwt = jwtManager.generateRefreshJWT(JWTRefreshClaims(user.id))
            )
        } else throw IllegalStateException("Wrong password")
    }

    override suspend fun refresh(username: String): AuthResponse {
        val user = userDao.getByUsername(username)
            ?: throw NotFoundException("User $username does not exist")
        return AuthResponse(
            jwt = jwtManager.generateAuthJWT(JWTClaims(user.id)),
            refreshJwt = null
        )
    }

}