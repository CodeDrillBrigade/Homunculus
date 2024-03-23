package org.cdb.labwitch.configuration

import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.routing.*
import org.cdb.labwitch.controller.authController
import org.cdb.labwitch.controller.storageController
import org.cdb.labwitch.controller.userController

fun Application.configureRouting() {
    install(ContentNegotiation) {
        json()
    }
    routing {
        authController()
        storageController()
        userController()
    }
}
