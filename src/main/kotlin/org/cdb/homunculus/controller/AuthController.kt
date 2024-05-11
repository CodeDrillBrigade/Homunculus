package org.cdb.homunculus.controller

import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.plugins.ratelimit.RateLimitName
import io.ktor.server.plugins.ratelimit.rateLimit
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.cdb.homunculus.components.toJWTRefreshClaims
import org.cdb.homunculus.configuration.LOGIN_RATE_LIMIT
import org.cdb.homunculus.configuration.REFRESH_CTX
import org.cdb.homunculus.exceptions.JWTException
import org.cdb.homunculus.logic.AuthenticationLogic
import org.cdb.homunculus.models.security.AuthCredentials
import org.koin.ktor.ext.inject

fun Routing.authController() =
	route("/auth") {
		val authLogic by inject<AuthenticationLogic>()

		rateLimit(RateLimitName(LOGIN_RATE_LIMIT)) {
			post("/login") {
				val credentials = call.receive<AuthCredentials>()
				call.respond(authLogic.login(credentials.username, credentials.password))
			}
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
