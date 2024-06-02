package org.cdb.homunculus.controller

import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.coroutines.flow.toList
import org.cdb.homunculus.logic.TagLogic
import org.cdb.homunculus.models.embed.Tag
import org.cdb.homunculus.models.identifiers.EntityId
import org.cdb.homunculus.models.security.Permissions
import org.cdb.homunculus.requests.authenticatedGet
import org.cdb.homunculus.requests.authenticatedPost
import org.koin.ktor.ext.inject

fun Routing.tagController() =
	route("/tag") {
		val tagLogic by inject<TagLogic>()

		authenticatedGet("") {
			call.respond(tagLogic.getAll().toList())
		}

		authenticatedGet("/{tagId}") {
			val tagId = checkNotNull(call.parameters["tagId"]) { "Tag Id must not be null" }
			call.respond(tagLogic.get(EntityId(tagId)))
		}

		authenticatedPost("/byIds") {
			val tagIds = call.receive<Set<EntityId>>()
			require(tagIds.isNotEmpty()) { "Tag Ids must not be null or empty" }
			call.respond(tagLogic.getByIds(tagIds))
		}

		authenticatedPost("", permissions = setOf(Permissions.MANAGE_METADATA)) {
			val tagToCreate = call.receive<Tag>()
			call.respond(tagLogic.create(tagToCreate))
		}
	}
