package org.cdb.homunculus.controller

import io.ktor.server.application.call
import io.ktor.server.plugins.ratelimit.RateLimitName
import io.ktor.server.plugins.ratelimit.rateLimit
import io.ktor.server.response.respond
import io.ktor.server.routing.Routing
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import org.cdb.homunculus.configuration.FORGOT_PASSWORD_CONFIRM_RATE_LIMIT
import org.cdb.homunculus.configuration.FORGOT_PASSWORD_RATE_LIMIT
import org.cdb.homunculus.logic.ProcessLogic
import org.cdb.homunculus.models.identifiers.EntityId
import org.koin.ktor.ext.inject

fun Routing.processController() =
	route("/process") {
		val processLogic by inject<ProcessLogic>()

		rateLimit(RateLimitName(FORGOT_PASSWORD_RATE_LIMIT)) {
			post("/passwordReset") {
				val email = checkNotNull(call.parameters["email"]) { "Email must not be null" }
				processLogic.initiatePasswordResetProcess(email)
				call.respond("ok")
			}
		}

		rateLimit(RateLimitName(FORGOT_PASSWORD_CONFIRM_RATE_LIMIT)) {
			post("/restConfirm") {
				val secret = checkNotNull(call.parameters["secret"]) { "Secret must not be null" }
				call.respond(processLogic.completePasswordResetProcess(EntityId(secret)))
			}
		}
	}
