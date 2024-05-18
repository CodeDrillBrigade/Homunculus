package org.cdb.homunculus.controller

import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.cdb.homunculus.logic.UserLogic
import org.cdb.homunculus.models.User
import org.cdb.homunculus.models.dto.PasswordDto
import org.cdb.homunculus.models.identifiers.EntityId
import org.cdb.homunculus.models.security.Permissions
import org.cdb.homunculus.requests.authenticatedGet
import org.cdb.homunculus.requests.authenticatedPost
import org.cdb.homunculus.requests.authenticatedPut
import org.cdb.homunculus.utils.guard
import org.koin.ktor.ext.inject

fun Routing.userController() =
	route("/user") {
		val userLogic by inject<UserLogic>()

		authenticatedGet("") {
			call.respond(userLogic.get(it.userId).redactSecrets())
		}

		authenticatedGet("/{userId}") {
			val userId = checkNotNull(call.parameters["userId"]) { "User Id must not be null" }
			call.respond(userLogic.get(EntityId(userId)).redactSecrets())
		}

		authenticatedGet("/permissions") {
			val permissions =
				Permissions.entries.filter { p ->
					it.permissions[p.index]
				}
			call.respond(permissions)
		}

		authenticatedGet("/byEmail/{email}") {
			val email = checkNotNull(call.parameters["email"]) { "Email must not be null" }
			call.respond(userLogic.getByEmail(email).redactSecrets())
		}

		authenticatedGet("/byUsername/{username}") {
			val username = checkNotNull(call.parameters["username"]) { "Username must not be null" }
			call.respond(userLogic.getByUsername(username).redactSecrets())
		}

		authenticatedPost("", permissions = setOf(Permissions.ADMIN)) {
			val creationData = call.receive<User>()
			userLogic.inviteUser(creationData, it.userId)
			call.respond("ok")
		}

		authenticatedPut("") {
			val userToUpdate = call.receive<User>()
			guard(
				it.userId == userToUpdate.id || it.permissions[Permissions.ADMIN.index],
			) { "You are not allowed to update this user" }
			userLogic.modify(userToUpdate)
			call.respond("ok")
		}

		authenticatedPut("/{userId}/password") {
			val userId = checkNotNull(call.parameters["userId"]) { "User Id must not be null" }
			val passwordDto = call.receive<PasswordDto>()
			require(passwordDto.isValid()) { "The provided password is not valid" }
			guard(
				it.userId.id == userId || it.permissions[Permissions.ADMIN.index],
			) { "You are not allowed to change the password for the user $userId" }
			call.respond(userLogic.changePassword(EntityId(userId), passwordDto.password))
		}
	}
