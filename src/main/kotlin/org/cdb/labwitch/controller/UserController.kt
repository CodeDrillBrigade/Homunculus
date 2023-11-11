package org.cdb.labwitch.controller

import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.cdb.labwitch.logic.UserLogic
import org.cdb.labwitch.models.UserCreationData
import org.koin.ktor.ext.inject

fun Routing.userController() = route("/user") {
    val userLogic by inject<UserLogic>()

    get("") {
        val userId = call.request.queryParameters["id"]!!
        call.respond(userLogic.get(userId))
    }

    post("") {
        val creationData = call.receive<UserCreationData>()
        val createdUser = userLogic.registerUser(creationData).copy(passwordHash = "*")
        call.respond(createdUser)
    }
}