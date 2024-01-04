package org.cdb.labwitch.configuration

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.response.*
import org.cdb.labwitch.components.JWTManager
import org.cdb.labwitch.models.config.JWTConfig

const val AUTH_CTX = "auth-ctx"
const val REFRESH_CTX = "refresh-ctx"

/**
 * Configures the http properties of the server
 *
 * @receiver a ktor [Application]
 */
fun Application.configureHTTP() {
    install(CORS) {
        allowHeader(HttpHeaders.ContentType)
        anyHost()
    }

    val jwtConfig = JWTConfig.fromConfig(environment.config)
    val jwtManager = JWTManager(jwtConfig)
    install(Authentication) {
        jwt(AUTH_CTX) {
            realm = jwtManager.config.realm
            verifier(jwtManager.authJWTVerifier())

            validate { credential ->
                jwtManager.credentialToPrincipal(credential)
            }

            challenge { defaultScheme, realm ->
                call.respond(HttpStatusCode.Unauthorized, "Token is not valid or has expired")
            }
        }

        jwt(REFRESH_CTX) {
            realm = jwtManager.config.realm
            verifier(jwtManager.refreshJWTVerifier())

            validate { credential ->
                jwtManager.refreshCredentialToPrincipal(credential)
            }

            challenge { defaultScheme, realm ->
                call.respond(HttpStatusCode.Unauthorized, "Token is not valid or has expired")
            }
        }
    }
}