package org.cdb.labwitch.controller

import io.ktor.server.application.call
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Routing
import io.ktor.server.routing.route
import kotlinx.coroutines.flow.toList
import org.cdb.labwitch.logic.BoxLogic
import org.cdb.labwitch.models.Box
import org.cdb.labwitch.models.identifiers.EntityId
import org.cdb.labwitch.models.identifiers.HierarchicalId
import org.cdb.labwitch.models.security.Permissions
import org.cdb.labwitch.requests.authenticatedGet
import org.cdb.labwitch.requests.authenticatedPost
import org.koin.ktor.ext.inject

fun Routing.boxController() =
	route("/box") {
		val boxLogic by inject<BoxLogic>()

		authenticatedGet("/{boxId}") {
			val boxDefinitionId = checkNotNull(call.parameters["boxId"]) { "Box Id must not be null" }
			call.respond(boxLogic.get(EntityId(boxDefinitionId)))
		}

		authenticatedGet("") {
			call.respond(boxLogic.getAll().toList())
		}

		authenticatedGet("/withMaterial/{materialId}") {
			val materialId = checkNotNull(call.parameters["materialId"]) { "Material Id must not be null" }
			call.respond(boxLogic.getByMaterial(EntityId(materialId)))
		}

		authenticatedGet("/onShelf/{shelfId}") {
			val shelfId = checkNotNull(call.parameters["shelfId"]) { "Shelf Id must not be null" }
			call.respond(boxLogic.getByPosition(HierarchicalId(shelfId)))
		}

		authenticatedPost("", permissions = setOf(Permissions.MANAGE_MATERIALS)) {
			val boxToCreate = call.receive<Box>()
			call.respond(boxLogic.create(boxToCreate, it.userId))
		}
	}
