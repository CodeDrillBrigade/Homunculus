package org.cdb.homunculus.controller

import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.cdb.homunculus.logic.TagLogic
import org.cdb.homunculus.models.embed.Tag
import org.cdb.homunculus.models.identifiers.EntityId
import org.cdb.homunculus.models.security.Permissions
import org.cdb.homunculus.requests.authenticatedDelete
import org.cdb.homunculus.requests.authenticatedGet
import org.cdb.homunculus.requests.authenticatedPost
import org.cdb.homunculus.requests.authenticatedPut
import org.koin.ktor.ext.inject

fun Routing.tagController() =
	route("/tag") {
		val tagLogic by inject<TagLogic>()

		authenticatedGet("") {
			call.respond(tagLogic.getAll())
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

		authenticatedPut("", permissions = setOf(Permissions.MANAGE_METADATA)) {
			val tagToModify = call.receive<Tag>()
			call.respond(tagLogic.modify(tagToModify))
		}

		authenticatedDelete("/{tagId}", permissions = setOf(Permissions.MANAGE_METADATA)) {
			val tagId = checkNotNull(call.parameters["tagId"]) { "Tag Id must not be null" }
			call.respond(tagLogic.delete(EntityId(tagId)))
		}

		authenticatedGet("/idsByName") {
			val query = requireNotNull(call.request.queryParameters["query"]) { "query must not be null." }
			call.respond(tagLogic.searchIds(query))
		}
	}
