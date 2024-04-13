package org.cdb.labwitch.controller

import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.coroutines.flow.toList
import org.cdb.labwitch.logic.TagLogic
import org.cdb.labwitch.models.embed.Tag
import org.cdb.labwitch.models.identifiers.EntityId
import org.cdb.labwitch.models.security.Permissions
import org.cdb.labwitch.requests.authenticatedGet
import org.cdb.labwitch.requests.authenticatedPost
import org.koin.ktor.ext.inject

fun Routing.tagController() =
	route("/tag") {
		val tagLogic by inject<TagLogic>()

		authenticatedGet("") {
			call.respond(tagLogic.getAll().toList())
		}

		authenticatedGet("/{tagId}") {
			val materialId = checkNotNull(call.parameters["tagId"]) { "Tag Id must not be null" }
			call.respond(tagLogic.get(EntityId(materialId)))
		}

		authenticatedPost("", permissions = setOf(Permissions.MANAGE_METADATA)) {
			val tagToCreate = call.receive<Tag>()
			call.respond(tagLogic.create(tagToCreate))
		}
	}
