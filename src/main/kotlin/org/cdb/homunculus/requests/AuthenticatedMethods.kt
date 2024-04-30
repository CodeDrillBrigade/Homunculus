package org.cdb.homunculus.requests

import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.routing.*
import io.ktor.util.pipeline.*
import org.cdb.homunculus.components.toJWTClaims
import org.cdb.homunculus.configuration.AUTH_CTX
import org.cdb.homunculus.exceptions.JWTException
import org.cdb.homunculus.models.security.JWTClaims
import org.cdb.homunculus.models.security.Permissions

private suspend fun PipelineContext<Unit, ApplicationCall>.checkPermissionsAndExecute(
	permissions: Set<Permissions> = emptySet(),
	block: suspend PipelineContext<Unit, ApplicationCall>.(JWTClaims) -> Unit,
) {
	val claims =
		call.principal<JWTPrincipal>()?.payload?.toJWTClaims()
			?: throw JWTException("No JWT passed in the request")
	if (permissions.isEmpty() || claims.permissions[Permissions.ADMIN.index] || permissions.all { claims.permissions[it.index] }) {
		block(claims)
	} else {
		throw IllegalAccessException("You are not authorized to access this endpoint")
	}
}

/**
 * Extends the default behaviour of [get] by automatically parsing the payload of the JWT from the principal.
 */
fun Route.authenticatedGet(
	path: String,
	ctx: String = AUTH_CTX,
	permissions: Set<Permissions> = emptySet(),
	block: suspend PipelineContext<Unit, ApplicationCall>.(JWTClaims) -> Unit,
): Route =
	authenticate(ctx) {
		get(path) {
			checkPermissionsAndExecute(permissions, block)
		}
	}

/**
 * Extends the default behaviour of [post] by automatically parsing the payload of the JWT from the principal.
 */
fun Route.authenticatedPost(
	path: String,
	ctx: String = AUTH_CTX,
	permissions: Set<Permissions> = emptySet(),
	block: suspend PipelineContext<Unit, ApplicationCall>.(JWTClaims) -> Unit,
): Route =
	authenticate(ctx) {
		post(path) {
			checkPermissionsAndExecute(permissions, block)
		}
	}

/**
 * Extends the default behaviour of [get] by automatically parsing the payload of the JWT from the principal.
 */
fun Route.authenticatedDelete(
	path: String,
	ctx: String = AUTH_CTX,
	permissions: Set<Permissions> = emptySet(),
	block: suspend PipelineContext<Unit, ApplicationCall>.(JWTClaims) -> Unit,
): Route =
	authenticate(ctx) {
		delete(path) {
			checkPermissionsAndExecute(permissions, block)
		}
	}

/**
 * Extends the default behaviour of [put] by automatically parsing the payload of the JWT from the principal.
 */
fun Route.authenticatedPut(
	path: String,
	ctx: String = AUTH_CTX,
	permissions: Set<Permissions> = emptySet(),
	block: suspend PipelineContext<Unit, ApplicationCall>.(JWTClaims) -> Unit,
): Route =
	authenticate(ctx) {
		put(path) {
			checkPermissionsAndExecute(permissions, block)
		}
	}
