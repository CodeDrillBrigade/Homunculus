package org.cdb.homunculus.configuration

import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.origin
import io.ktor.server.plugins.ratelimit.RateLimit
import io.ktor.server.plugins.ratelimit.RateLimitName
import kotlin.time.Duration.Companion.seconds

const val LOGIN_RATE_LIMIT = "login"
const val FORGOT_PASSWORD_RATE_LIMIT = "forgot-password"

fun Application.configureThrottling() {
	install(RateLimit) {
		register(RateLimitName(LOGIN_RATE_LIMIT)) {
			rateLimiter(limit = 30, refillPeriod = 60.seconds)
			requestKey { applicationCall ->
				applicationCall.request.origin.remoteHost
			}
		}

		register(RateLimitName(FORGOT_PASSWORD_RATE_LIMIT)) {
			rateLimiter(limit = 2, refillPeriod = 60.seconds)
			requestKey { applicationCall ->
				applicationCall.request.origin.remoteHost
			}
		}
	}
}
