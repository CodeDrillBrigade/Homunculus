package org.cdb.labwitch

import io.ktor.server.application.*
import org.cdb.labwitch.configuration.configureExceptions
import org.cdb.labwitch.configuration.configureHTTP
import org.cdb.labwitch.configuration.configureKoin
import org.cdb.labwitch.configuration.configureRouting

fun main(args: Array<String>) = io.ktor.server.cio.EngineMain.main(args)

@Suppress("unused")
fun Application.module() {
	configureHTTP()
	configureKoin()
	configureRouting()
	configureExceptions()
}
