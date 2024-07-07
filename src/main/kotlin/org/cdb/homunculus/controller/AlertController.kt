package org.cdb.homunculus.controller

import io.ktor.server.application.call
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Routing
import io.ktor.server.routing.route
import kotlinx.coroutines.flow.toList
import org.cdb.homunculus.logic.AlertLogic
import org.cdb.homunculus.models.Alert
import org.cdb.homunculus.models.identifiers.EntityId
import org.cdb.homunculus.models.security.Permissions
import org.cdb.homunculus.requests.authenticatedGet
import org.cdb.homunculus.requests.authenticatedPost
import org.cdb.homunculus.requests.authenticatedPut
import org.koin.ktor.ext.inject

fun Routing.alertController() =
	route("/alert") {
		val alertLogic by inject<AlertLogic>()

		authenticatedGet("/{alertId}") {
			val alertId = checkNotNull(call.parameters["alertId"]) { "Alert Id must not be null" }
			call.respond(alertLogic.get(EntityId(alertId)))
		}

		authenticatedGet("") {
			call.respond(alertLogic.getAll().toList())
		}

		authenticatedPost("", permissions = setOf(Permissions.MANAGE_NOTIFICATIONS)) {
			val alertToCreate = call.receive<Alert>()
			call.respond(alertLogic.create(alertToCreate))
		}

		authenticatedPut("", permissions = setOf(Permissions.MANAGE_NOTIFICATIONS)) {
			val alertToUpdate = call.receive<Alert>()
			call.respond(alertLogic.modify(alertToUpdate))
		}
	}
