package org.cdb.labwitch

import io.ktor.server.application.*
import org.cdb.labwitch.configuration.configureHTTP
import org.cdb.labwitch.configuration.configureKoin
import org.cdb.labwitch.configuration.configureRouting

// fun main() {
//     embeddedServer(CIO, port = 8080, host = "0.0.0.0", module = Application::module)
//         .start(wait = true)
// }

fun main(args: Array<String>) = io.ktor.server.cio.EngineMain.main(args)

fun Application.module() {
    configureHTTP()
    configureKoin()
    configureRouting()
}