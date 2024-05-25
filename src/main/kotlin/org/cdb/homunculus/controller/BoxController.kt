package org.cdb.homunculus.controller

import io.ktor.server.application.call
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Routing
import io.ktor.server.routing.route
import kotlinx.coroutines.flow.toList
import org.cdb.homunculus.logic.BoxLogic
import org.cdb.homunculus.models.Box
import org.cdb.homunculus.models.embed.UsageLog
import org.cdb.homunculus.models.identifiers.EntityId
import org.cdb.homunculus.models.identifiers.HierarchicalId
import org.cdb.homunculus.models.security.Permissions
import org.cdb.homunculus.requests.authenticatedDelete
import org.cdb.homunculus.requests.authenticatedGet
import org.cdb.homunculus.requests.authenticatedPost
import org.cdb.homunculus.requests.authenticatedPut
import org.cdb.homunculus.utils.guard
import org.koin.ktor.ext.inject

fun Routing.boxController() =
	route("/box") {
		val boxLogic by inject<BoxLogic>()

		authenticatedGet("/{boxId}") {
			val boxId = checkNotNull(call.parameters["boxId"]) { "Box Id must not be null" }
			call.respond(boxLogic.get(EntityId(boxId)))
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

		authenticatedPut("", permissions = setOf(Permissions.MANAGE_MATERIALS)) {
			val boxToUpdate = call.receive<Box>()
			call.respond(boxLogic.modify(boxToUpdate))
		}

		authenticatedDelete("/{boxId}", permissions = setOf(Permissions.MANAGE_MATERIALS)) {
			val boxId = checkNotNull(call.parameters["boxId"]) { "Box Id must not be null" }
			call.respond(boxLogic.delete(EntityId(boxId)))
		}

		authenticatedPost("/{boxId}/quantity") {
			val boxId = checkNotNull(call.parameters["boxId"]) { "Box Id must not be null" }
			val update = call.receive<UsageLog>()
			guard(
				it.permissions[Permissions.ADMIN.index] ||
					it.permissions[Permissions.MANAGE_MATERIALS.index] ||
					it.userId == update.user,
			) { "You are not allowed to update the quantity for another user" }
			call.respond(boxLogic.updateQuantity(EntityId(boxId), update))
		}
	}
