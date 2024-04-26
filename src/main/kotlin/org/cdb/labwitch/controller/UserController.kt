package org.cdb.labwitch.controller

import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.cdb.labwitch.logic.UserLogic
import org.cdb.labwitch.models.UserCreationData
import org.cdb.labwitch.models.security.Permissions
import org.cdb.labwitch.requests.authenticatedGet
import org.cdb.labwitch.requests.authenticatedPost
import org.koin.ktor.ext.inject

fun Routing.userController() =
    route("/user") {
        val userLogic by inject<UserLogic>()

        authenticatedGet("") {
            call.respond(userLogic.get(it.userId).copy(passwordHash = "*"))
        }

        authenticatedPost("", permissions = setOf(Permissions.ADMIN)) {
            val creationData = call.receive<UserCreationData>()
            val createdUser = userLogic.registerUser(creationData).copy(passwordHash = "*")
            call.respond(createdUser)
        }

        authenticatedGet("/permissions") { jwt ->
            val permissions = Permissions.entries.filter { jwt.permissions[it.index] }
            call.respond(permissions)
        }
    }
