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
import org.cdb.homunculus.requests.authenticatedDelete
import org.cdb.homunculus.requests.authenticatedGet
import org.cdb.homunculus.requests.authenticatedPost
import org.cdb.homunculus.requests.authenticatedPut
import org.koin.ktor.ext.inject

fun Routing.materialController() =
	route("/material") {
		val materialLogic by inject<MaterialLogic>()

		authenticatedGet("") {
			call.respond(materialLogic.getAll().toList())
		}

		authenticatedGet("/{materialId}") {
			val materialId = requireNotNull(call.parameters["materialId"]) { "Material Id must not be null" }
			call.respond(materialLogic.get(EntityId(materialId)))
		}

		authenticatedDelete("/{materialId}", permissions = setOf(Permissions.MANAGE_MATERIALS)) {
			val materialId = requireNotNull(call.parameters["materialId"]) { "Material Id must not be null" }
			call.respond(materialLogic.delete(EntityId(materialId)))
		}

		authenticatedGet("/byFuzzyName/{query}") {
			val query = requireNotNull(call.parameters["query"]) { "query must not be null." }
			val limit = call.request.queryParameters["limit"]?.toIntOrNull()
			call.respond(materialLogic.findByFuzzyName(query, limit))
		}

		authenticatedGet("/recentlyCreated") {
			val limit = requireNotNull(call.request.queryParameters["limit"]?.toIntOrNull()) { "limit is not a valid int" }
			call.respond(materialLogic.getLastCreated(limit))
		}

		authenticatedPost("/idsByNameBrandCode") {
			val query = requireNotNull(call.request.queryParameters["query"]) { "query must not be null." }
			val tagIds = call.receiveNullable<Set<EntityId>?>()
			call.respond(materialLogic.searchIds(query, tagIds, null))
		}

		authenticatedGet("/namesByNameBrandCode") {
			val query = requireNotNull(call.request.queryParameters["query"]) { "query must not be null." }
			val limit = call.request.queryParameters["limit"]?.toIntOrNull()
			call.respond(materialLogic.searchNames(query, tagIds = null, limit = limit))
		}

		authenticatedPost("/byIds") {
			val materialIds = call.receive<Set<EntityId>>()
			require(materialIds.isNotEmpty()) { "Material Ids must not be null or empty" }
			call.respond(materialLogic.getByIds(materialIds))
		}

		authenticatedPost("", permissions = setOf(Permissions.MANAGE_MATERIALS)) {
			val materialToCreate = call.receive<Material>()
			call.respond(materialLogic.create(materialToCreate))
		}

		authenticatedPut("", permissions = setOf(Permissions.MANAGE_MATERIALS)) {
			val materialToUpdate = call.receive<Material>()
			call.respond(materialLogic.modify(materialToUpdate))
		}
	}
