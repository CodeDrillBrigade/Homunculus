package org.cdb.homunculus.controller

import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.coroutines.flow.toList
import org.cdb.homunculus.logic.BoxDefinitionLogic
import org.cdb.homunculus.models.embed.BoxDefinition
import org.cdb.homunculus.models.identifiers.EntityId
import org.cdb.homunculus.models.security.Permissions
import org.cdb.homunculus.requests.authenticatedGet
import org.cdb.homunculus.requests.authenticatedPut
import org.koin.ktor.ext.inject

fun Routing.boxDefinitionController() =
	route("/boxDefinition") {
		val boxDefinitionLogic by inject<BoxDefinitionLogic>()

		authenticatedGet("/{boxDefinitionId}") {
			val boxDefinitionId = checkNotNull(call.parameters["boxDefinitionId"]) { "Box Definition Id must not be null" }
			call.respond(boxDefinitionLogic.get(EntityId(boxDefinitionId)))
		}

		authenticatedGet("") {
			call.respond(boxDefinitionLogic.getAll().toList())
		}

		authenticatedPut("", permissions = setOf(Permissions.MANAGE_MATERIALS)) {
			val definitionToCreate = call.receive<BoxDefinition>()
			call.respond(boxDefinitionLogic.create(definitionToCreate))
		}
	}
