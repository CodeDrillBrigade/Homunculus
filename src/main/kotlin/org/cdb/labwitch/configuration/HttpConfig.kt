package org.cdb.labwitch.configuration

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.cors.routing.*

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
}