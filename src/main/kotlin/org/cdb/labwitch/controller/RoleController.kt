package org.cdb.labwitch.controller

import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.coroutines.flow.toList
import org.cdb.labwitch.logic.RoleLogic
import org.cdb.labwitch.models.embed.Role
import org.cdb.labwitch.models.security.Permissions
import org.cdb.labwitch.requests.authenticatedGet
import org.cdb.labwitch.requests.authenticatedPost
import org.koin.ktor.ext.inject

fun Routing.roleController() =
	route("/role") {
		val roleLogic by inject<RoleLogic>()
		
		authenticatedGet(""/*, permissions = setOf(Permissions.ADMIN)*/) {
			call.respond(roleLogic.getAll().toList())
		}
		
		authenticatedGet("/{roleID}") {
			call.respond(checkNotNull(call.parameters["roleID"]) { "Role ID must not be null" })
		}
		
		authenticatedPost("", permissions = setOf(Permissions.ADMIN)) {
			call.respond(roleLogic.addRole(call.receive<Role>()))
		}
	}
