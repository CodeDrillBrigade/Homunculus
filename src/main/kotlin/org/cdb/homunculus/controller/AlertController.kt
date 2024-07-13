package org.cdb.homunculus.controller

import io.ktor.server.application.call
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Routing
import io.ktor.server.routing.route
import kotlinx.coroutines.flow.toList
import org.cdb.homunculus.logic.AlertLogic
import org.cdb.homunculus.models.Alert
import org.cdb.homunculus.models.embed.AlertStatus
import org.cdb.homunculus.models.identifiers.EntityId
import org.cdb.homunculus.models.security.Permissions
import org.cdb.homunculus.requests.authenticatedDelete
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

		authenticatedPost("/byIds") {
			val alertIds = call.receive<Set<EntityId>>()
			require(alertIds.isNotEmpty()) { "Alert Ids must not be null or empty" }
			call.respond(alertLogic.getByIds(alertIds))
		}

		authenticatedGet("/idsByName") {
			val query = requireNotNull(call.request.queryParameters["query"]) { "query must not be null." }
			call.respond(alertLogic.searchIds(query))
		}

		authenticatedDelete("/{alertId}", permissions = setOf(Permissions.MANAGE_NOTIFICATIONS)) {
			val alertId = checkNotNull(call.parameters["alertId"]) { "Alert Id must not be null" }
			call.respond(alertLogic.delete(EntityId(alertId)))
		}

		authenticatedPut("/{alertId}/status", permissions = setOf(Permissions.MANAGE_NOTIFICATIONS)) {
			val alertId = checkNotNull(call.parameters["alertId"]) { "Alert Id must not be null" }
			val status =
				requireNotNull(
					call.request.queryParameters["status"]?.let { AlertStatus.valueOf(it) },
				) { "Status must not be null." }
			call.respond(alertLogic.setStatus(EntityId(alertId), status))
		}

		authenticatedGet("/byActivationMaterial") {
			val materialId = checkNotNull(call.parameters["materialId"]) { "Material Id must not be null" }
			call.respond(alertLogic.listByAcceptedMaterial(EntityId(materialId)))
		}

		authenticatedPost("/addToExclusions") {
			val materialId = checkNotNull(call.parameters["materialId"]) { "Material Id must not be null" }
			val alertIds = call.receive<Set<EntityId>>()
			call.respond(alertLogic.addMaterialToExclusions(EntityId(materialId), alertIds))
		}
	}
