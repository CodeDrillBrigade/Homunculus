package org.cdb.labwitch.models.config

import io.ktor.server.config.*

class JWTConfig(
    val authSecret: String,
    val issuer: String,
    val audience: String,
    val realm: String
) {

    companion object {
        fun fromConfig(config: ApplicationConfig) = JWTConfig(
            authSecret = config.property("ktor.jwt.authSecret").getString(),
            issuer = config.property("ktor.jwt.issuer").getString(),
            audience = config.property("ktor.jwt.audience").getString(),
            realm = config.property("ktor.jwt.realm").getString()
        )
    }

}