package org.cdb.labwitch.controller

import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.cdb.labwitch.dao.UserDao
import org.cdb.labwitch.logic.UserLogic
import org.cdb.labwitch.models.UserCreationData
import org.koin.ktor.ext.inject

fun Routing.userController() = route("/user") {
    val userLogic by inject<UserLogic>()

    authenticate("auth-jwt") {
        get("") {
                val principal = call.principal<JWTPrincipal>()
                val userId = principal!!.payload.getClaim("uId").asString()
                call.respond(userLogic.get(userId).copy(passwordHash = "*"))
            }
    }

    post("") {
        val creationData = call.receive<UserCreationData>()
        val createdUser = userLogic.registerUser(creationData).copy(passwordHash = "*")
        call.respond(createdUser)
    }
}