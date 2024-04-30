package org.cdb.homunculus.components

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.interfaces.Payload
import io.ktor.server.auth.jwt.*
import org.cdb.homunculus.exceptions.JWTException
import org.cdb.homunculus.exceptions.UnauthorizedException
import org.cdb.homunculus.models.config.JWTConfig
import org.cdb.homunculus.models.identifiers.EntityId
import org.cdb.homunculus.models.security.JWTClaims
import org.cdb.homunculus.models.security.JWTRefreshClaims
import org.cdb.homunculus.utils.DynamicBitArray
import java.util.*
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.hours

/**
 * This component manages the authentication and refresh JWT creation and verification
 */
class JWTManager(
	val config: JWTConfig,
) {
	companion object {
		const val USER_ID = "uId"
		const val PERMISSIONS = "p"
		private val authJWTDuration = 1L.hours.inWholeMilliseconds
		private val refreshJWTDuration = 30L.days.inWholeMilliseconds
	}

	/**
	 * Generates an authentication JWT from the claims passed as parameter.
	 * The duration of the token is set to 1 hour.
	 *
	 * @param jwtClaims the [JWTClaims] to put in the token.
	 * @return a base64-encoded JWT.
	 */
	fun generateAuthJWT(jwtClaims: JWTClaims): String =
		JWT.create()
			.withAudience(config.audience)
			.withIssuer(config.issuer)
			.withClaim(USER_ID, jwtClaims.userId.id)
			.withClaim(PERMISSIONS, jwtClaims.permissions.toBase64String())
			.withExpiresAt(Date(System.currentTimeMillis() + authJWTDuration))
			.sign(Algorithm.HMAC256(config.authSecret))

	/**
	 * Generates a refresh JWT from the claims passed as parameter.
	 * The duration of the token is set to 1 hour.
	 *
	 * @param jwtClaims the [JWTRefreshClaims] to put in the token.
	 * @return a base64-encoded JWT.
	 */
	fun generateRefreshJWT(jwtClaims: JWTRefreshClaims): String =
		JWT.create()
			.withAudience(config.audience)
			.withIssuer(config.issuer)
			.withClaim(USER_ID, jwtClaims.userId.id)
			.withExpiresAt(Date(System.currentTimeMillis() + refreshJWTDuration))
			.sign(Algorithm.HMAC256(config.refreshSecret))

	/**
	 * Converts a [JWTCredential] to a [JWTPrincipal], ensuring that the payload is in the correct format.
	 *
	 * @param credential a [JWTCredential].
	 * @return a [JWTPrincipal].
	 */
	fun credentialToPrincipal(credential: JWTCredential): JWTPrincipal =
		if (credential.payload.getClaim(USER_ID).asString() != "") {
			JWTPrincipal(credential.payload)
		} else {
			throw UnauthorizedException("Wrong JWT format")
		}

	/**
	 * @return a [JWTVerifier] for the authentication jwt.
	 */
	fun authJWTVerifier(): JWTVerifier =
		JWT
			.require(Algorithm.HMAC256(config.authSecret))
			.withAudience(config.audience)
			.withIssuer(config.issuer)
			.build()

	/**
	 * @return a [JWTVerifier] for the refresh jwt.
	 */
	fun refreshJWTVerifier(): JWTVerifier =
		JWT
			.require(Algorithm.HMAC256(config.refreshSecret))
			.withAudience(config.audience)
			.withIssuer(config.issuer)
			.build()

	private fun Payload.isRefreshJwtValid() = getClaim(USER_ID).asString().isNotBlank()

	/**
	 * Converts a [JWTCredential] to a [JWTPrincipal], ensuring that the payload is in the correct format.
	 *
	 * @param credential a [JWTCredential].
	 * @return a [JWTPrincipal].
	 */
	fun refreshCredentialToPrincipal(credential: JWTCredential): JWTPrincipal =
		if (credential.payload.isRefreshJwtValid()) {
			JWTPrincipal(credential.payload)
		} else {
			throw UnauthorizedException("Wrong refresh JWT format")
		}
}

/**
 * Converts a [Payload] to [JWTClaims].
 *
 * @receiver payload a [Payload].
 * @return a [JWTClaims]
 * @throws JWTException if it the JWT is in the wrong format.
 */
fun Payload.toJWTClaims(): JWTClaims =
	try {
		JWTClaims(
			userId = EntityId(getClaim(JWTManager.USER_ID).asString()),
			permissions = DynamicBitArray.fromBase64String(getClaim(JWTManager.PERMISSIONS).asString()),
		)
	} catch (e: Exception) {
		throw JWTException(e.message ?: "Wrong JWT format")
	}

/**
 * Converts a [Payload] to [JWTRefreshClaims].
 *
 * @receiver payload a [Payload].
 * @return a [JWTRefreshClaims]
 * @throws JWTException if the JWT is in the wrong format.
 */
fun Payload.toJWTRefreshClaims(): JWTRefreshClaims =
	try {
		JWTRefreshClaims(
			userId = EntityId(getClaim(JWTManager.USER_ID).asString()),
		)
	} catch (e: Exception) {
		throw JWTException(e.message ?: "Wrong JWT format")
	}
