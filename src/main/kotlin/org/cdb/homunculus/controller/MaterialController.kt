package org.cdb.homunculus.controller

import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.coroutines.flow.toList
import org.cdb.homunculus.logic.MaterialLogic
import org.cdb.homunculus.models.Material
import org.cdb.homunculus.models.identifiers.EntityId
import org.cdb.homunculus.models.security.Permissions
import org.cdb.homunculus.requests.authenticatedGet
import org.cdb.homunculus.requests.authenticatedPost
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

		authenticatedGet("/byFuzzyName/{query}") {
			val query = checkNotNull(call.parameters["query"]) { "query must not be null and longer than 3 characters." }
			val limit = call.request.queryParameters["limit"]?.toIntOrNull()
			call.respond(materialLogic.findByFuzzyName(query, limit))
		}

		authenticatedPost("", permissions = setOf(Permissions.MANAGE_MATERIALS)) {
			val materialToCreate = call.receive<Material>()
			call.respond(materialLogic.create(materialToCreate))
		}
	}
