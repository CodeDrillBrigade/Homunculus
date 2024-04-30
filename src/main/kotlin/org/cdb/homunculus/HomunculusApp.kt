package org.cdb.homunculus

import io.ktor.server.application.*
import org.cdb.homunculus.configuration.configureExceptions
import org.cdb.homunculus.configuration.configureHTTP
import org.cdb.homunculus.configuration.configureKoin
import org.cdb.homunculus.configuration.configureRouting
import org.cdb.homunculus.configuration.initialization

fun main(args: Array<String>) = io.ktor.server.cio.EngineMain.main(args)

@Suppress("unused")
fun Application.module() {
	configureHTTP()
	configureKoin()
	configureRouting()
	configureExceptions()
	initialization()
}
