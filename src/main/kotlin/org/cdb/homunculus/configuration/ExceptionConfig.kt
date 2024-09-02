package org.cdb.homunculus.configuration

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*
import io.ktor.util.logging.KtorSimpleLogger
import io.ktor.util.logging.error
import org.cdb.homunculus.exceptions.NotFoundException
import org.cdb.homunculus.exceptions.UnauthorizedException
import org.cdb.homunculus.models.security.StatusResponse
import java.io.IOException

private val logger = KtorSimpleLogger("com.example.RequestTracePlugin")

fun Application.configureExceptions() {
	fun Exception.toErrorResponse(status: HttpStatusCode) =
		StatusResponse(
			false,
			message ?: this::class.qualifiedName ?: "Something wrong occurred",
			status.value,
		)

	install(StatusPages) {
		exception<Throwable> { call, cause ->
			when (cause) {
				is AccessDeniedException -> call.respond(HttpStatusCode.Forbidden, cause.toErrorResponse(HttpStatusCode.Forbidden))
				is IllegalAccessException -> call.respond(HttpStatusCode.Forbidden, cause.toErrorResponse(HttpStatusCode.Forbidden))
				is IOException -> call.respond(HttpStatusCode.BadRequest, cause.toErrorResponse(HttpStatusCode.BadRequest))
				is IllegalArgumentException -> call.respond(HttpStatusCode.BadRequest, cause.toErrorResponse(HttpStatusCode.BadRequest))
				is UnauthorizedException -> call.respond(HttpStatusCode.Unauthorized, cause.toErrorResponse(HttpStatusCode.Unauthorized))
				is NotFoundException -> call.respond(HttpStatusCode.NotFound, cause.toErrorResponse(HttpStatusCode.NotFound))
				else -> {
					logger.error(cause)
					call.respond(
						HttpStatusCode.InternalServerError,
						StatusResponse(false, cause.message ?: "Something went wrong", HttpStatusCode.InternalServerError.value),
					)
				}
			}
		}
	}
}
