package org.cdb.labwitch.controller

import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.coroutines.flow.toList
import org.cdb.labwitch.logic.MaterialLogic
import org.cdb.labwitch.models.Material
import org.cdb.labwitch.models.identifiers.EntityId
import org.cdb.labwitch.models.security.Permissions
import org.cdb.labwitch.requests.authenticatedGet
import org.cdb.labwitch.requests.authenticatedPost
import org.koin.ktor.ext.inject

fun Routing.materialController() =
	route("/material") {
		val materialLogic by inject<MaterialLogic>()

		authenticatedGet("") {
			call.respond(materialLogic.getAll().toList())
		}

		authenticatedGet("/{materialId}") {
			val materialId = checkNotNull(call.parameters["materialId"]) { "Material Id must not be null" }
			call.respond(materialLogic.get(EntityId(materialId)))
		}

		authenticatedPost("", permissions = setOf(Permissions.MANAGE_MATERIALS)) {
			val materialToCreate = call.receive<Material>()
			call.respond(materialLogic.create(materialToCreate))
		}
	}
