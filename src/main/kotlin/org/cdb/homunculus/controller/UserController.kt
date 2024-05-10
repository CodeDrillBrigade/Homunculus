package org.cdb.homunculus.controller

import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.cdb.homunculus.logic.UserLogic
import org.cdb.homunculus.models.UserCreationData
import org.cdb.homunculus.models.identifiers.EntityId
import org.cdb.homunculus.models.security.Permissions
import org.cdb.homunculus.requests.authenticatedGet
import org.cdb.homunculus.requests.authenticatedPost
import org.koin.ktor.ext.inject

fun Routing.userController() =
	route("/user") {
		val userLogic by inject<UserLogic>()

		authenticatedGet("") {
			call.respond(userLogic.get(it.userId).copy(passwordHash = "*"))
		}

		authenticatedGet("/{userId}") {
			val userId = checkNotNull(call.parameters["userId"]) { "User Id must not be null" }
			call.respond(userLogic.get(EntityId(userId)).copy(passwordHash = "*"))
		}

		authenticatedPost("", permissions = setOf(Permissions.ADMIN)) {
			val creationData = call.receive<UserCreationData>()
			val createdUser = userLogic.registerUser(creationData).copy(passwordHash = "*")
			call.respond(createdUser)
		}
	}
