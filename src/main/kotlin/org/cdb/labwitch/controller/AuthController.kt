package org.cdb.labwitch.controller

import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.cdb.labwitch.components.toJWTRefreshClaims
import org.cdb.labwitch.configuration.REFRESH_CTX
import org.cdb.labwitch.exceptions.JWTException
import org.cdb.labwitch.logic.AuthenticationLogic
import org.cdb.labwitch.models.security.AuthCredentials
import org.koin.ktor.ext.inject

fun Routing.authController() =
    route("/auth") {
        val authLogic by inject<AuthenticationLogic>()

        post("/login") {
            val credentials = call.receive<AuthCredentials>()
            call.respond(authLogic.login(credentials.username, credentials.password))
        }

        authenticate(REFRESH_CTX) {
            post("/refresh") {
                val claims =
                    call.principal<JWTPrincipal>()?.payload?.toJWTRefreshClaims()
                        ?: throw JWTException("No JWT passed in the request")
                call.respond(authLogic.refresh(claims.userId))
            }
        }
    }
