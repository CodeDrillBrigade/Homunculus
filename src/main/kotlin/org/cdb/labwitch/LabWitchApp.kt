package org.cdb.labwitch

import io.ktor.server.application.*
import io.ktor.server.cio.*
import io.ktor.server.engine.*
import org.cdb.labwitch.configuration.configureHTTP
import org.cdb.labwitch.configuration.configureKoin

fun main() {
    embeddedServer(CIO, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    configureHTTP()
    configureKoin()
}