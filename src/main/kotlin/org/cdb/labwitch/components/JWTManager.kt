package org.cdb.labwitch.components

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.server.auth.jwt.*
import org.cdb.labwitch.models.config.JWTConfig
import org.cdb.labwitch.models.security.JWTClaims
import java.util.*
import kotlin.time.Duration.Companion.hours

/**
 * This component manages the authentication and refresh JWT creation and verification
 */
class JWTManager(
    val config: JWTConfig
) {

    companion object {
        private const val USER_ID = "uId"
        private val authJWTDuration = 1L.hours.inWholeMilliseconds
    }

    /**
     * Generates an authentication JWT from the claims passed as parameter.
     * The duration of the token is set to 1 hour.
     *
     * @param jwtClaims the [JWTClaims] to put in the token.
     * @return a base64-encoded JWT.
     */
    fun generateAuthJWT(jwtClaims: JWTClaims): String = JWT.create()
        .withAudience(config.audience)
        .withIssuer(config.issuer)
        .withClaim(USER_ID, jwtClaims.userId)
        .withExpiresAt(Date(System.currentTimeMillis() + authJWTDuration))
        .sign(Algorithm.HMAC256(config.authSecret))

    /**
     * Converts a [JWTCredential] to a [JWTPrincipal], ensuring that the payload is in the correct format.
     *
     * @param credential a [JWTCredential].
     * @return a [JWTPrincipal].
     */
    fun credentialToPrincipal(credential: JWTCredential): JWTPrincipal =
        if (credential.payload.getClaim("USER_ID").asString() != "") {
            JWTPrincipal(credential.payload)
        } else throw IllegalStateException("Wrong format")

    /**
     * @return a [JWTVerifier] for the authentication jwt.
     */
    fun authJWTVerifier(): JWTVerifier = JWT
        .require(Algorithm.HMAC256(config.authSecret))
        .withAudience(config.audience)
        .withIssuer(config.issuer)
        .build()
}