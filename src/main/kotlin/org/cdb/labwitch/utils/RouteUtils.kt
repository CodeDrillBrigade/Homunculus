package org.cdb.labwitch.utils

import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.routing.*
import io.ktor.util.pipeline.*
import org.cdb.labwitch.components.toJWTClaims
import org.cdb.labwitch.configuration.AUTH_CTX
import org.cdb.labwitch.exceptions.JWTException
import org.cdb.labwitch.models.security.JWTClaims

/**
 * Extends the default behaviour of [get] by automatically parsing the payload of the JWT from the principal.
 */
fun Route.authenticatedGet(
    path: String,
    ctx: String = AUTH_CTX,
    block: suspend PipelineContext<Unit, ApplicationCall>.(JWTClaims) -> Unit,
): Route =
    authenticate(ctx) {
        get(path) {
            val claims =
                call.principal<JWTPrincipal>()?.payload?.toJWTClaims()
                    ?: throw JWTException("No JWT passed in the request")
            block(claims)
        }
    }

/**
 * Extends the default behaviour of [post] by automatically parsing the payload of the JWT from the principal.
 */
fun Route.authenticatedPost(
    path: String,
    ctx: String = AUTH_CTX,
    block: suspend PipelineContext<Unit, ApplicationCall>.(JWTClaims) -> Unit,
): Route =
    authenticate(ctx) {
        post(path) {
            val claims =
                call.principal<JWTPrincipal>()?.payload?.toJWTClaims()
                    ?: throw JWTException("No JWT passed in the request")
            block(claims)
        }
    }
