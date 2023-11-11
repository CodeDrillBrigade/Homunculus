package org.cdb.labwitch.controller

import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.cdb.labwitch.logic.AuthenticationLogic
import org.cdb.labwitch.models.security.AuthCredentials
import org.koin.ktor.ext.inject

fun Routing.authController() = route("/auth") {
    val authLogic by inject<AuthenticationLogic>()

    post("/login") {
        val credentials = call.receive<AuthCredentials>()
        call.respond(authLogic.login(credentials.username, credentials.password))
    }
}