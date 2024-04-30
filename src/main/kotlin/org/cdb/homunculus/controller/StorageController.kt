package org.cdb.homunculus.controller

import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.coroutines.flow.toList
import org.cdb.homunculus.logic.StorageLogic
import org.cdb.homunculus.models.StorageRoom
import org.cdb.homunculus.models.embed.storage.Cabinet
import org.cdb.homunculus.models.embed.storage.Shelf
import org.cdb.homunculus.models.identifiers.ShortId
import org.cdb.homunculus.models.security.Permissions
import org.cdb.homunculus.requests.authenticatedGet
import org.cdb.homunculus.requests.authenticatedPost
import org.koin.ktor.ext.inject

fun Routing.storageController() =
	route("/storage") {
		val storageLogic by inject<StorageLogic>()

		authenticatedGet("") {
			call.respond(storageLogic.getAll().toList())
		}

		authenticatedPost("", permissions = setOf(Permissions.MANAGE_STORAGE)) {
			val storageToCreate = call.receive<StorageRoom>()
			call.respond(storageLogic.create(storageToCreate))
		}

		authenticatedPost("/{storageRoomId}/cabinet", permissions = setOf(Permissions.MANAGE_STORAGE)) {
			val storageRoomId = checkNotNull(call.parameters["storageRoomId"]) { "Storage Room Id must not be null" }
			val cabinetToCreate = call.receive<Cabinet>()
			call.respond(storageLogic.addCabinet(ShortId(storageRoomId), cabinetToCreate))
		}

		authenticatedPost("/{storageRoomId}/cabinet/{cabinetId}/shelf", permissions = setOf(Permissions.MANAGE_STORAGE)) {
			val storageRoomId = checkNotNull(call.parameters["storageRoomId"]) { "Storage Room Id must not be null" }
			val cabinetId = checkNotNull(call.parameters["cabinetId"]) { "Cabinet Id must not be null" }
			val shelfToCreate = call.receive<Shelf>()
			call.respond(storageLogic.addShelf(ShortId(storageRoomId), ShortId(cabinetId), shelfToCreate))
		}
	}
