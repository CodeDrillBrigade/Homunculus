package org.cdb.homunculus.controller

import io.ktor.server.application.call
import io.ktor.server.response.respond
import io.ktor.server.routing.Routing
import io.ktor.server.routing.route
import org.cdb.homunculus.logic.RoleLogic
import org.cdb.homunculus.models.identifiers.EntityId
import org.cdb.homunculus.requests.authenticatedGet
import org.koin.ktor.ext.inject

fun Routing.roleController() =
	route("/role") {
		val roleLogic by inject<RoleLogic>()

		authenticatedGet("") {
			call.respond(roleLogic.getAll())
		}

		authenticatedGet("/{roleId}") {
			val roleId = checkNotNull(call.parameters["roleId"]) { "Role Id must not be null" }
			call.respond(roleLogic.get(EntityId(roleId)))
		}
	}
