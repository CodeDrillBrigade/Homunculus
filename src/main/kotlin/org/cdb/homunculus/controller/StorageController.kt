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
import org.cdb.homunculus.requests.authenticatedDelete
import org.cdb.homunculus.requests.authenticatedGet
import org.cdb.homunculus.requests.authenticatedPost
import org.cdb.homunculus.requests.authenticatedPut
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

		authenticatedPut("", permissions = setOf(Permissions.MANAGE_STORAGE)) {
			val storageToUpdate = call.receive<StorageRoom>()
			call.respond(storageLogic.update(storageToUpdate))
		}

		authenticatedDelete("/{storageRoomId}", permissions = setOf(Permissions.MANAGE_STORAGE)) {
			val storageRoomId = requireNotNull(call.parameters["storageRoomId"]) { "Storage Room Id must not be null" }
			call.respond(storageLogic.delete(ShortId(storageRoomId)))
		}

		authenticatedPost("/{storageRoomId}/cabinet", permissions = setOf(Permissions.MANAGE_STORAGE)) {
			val storageRoomId = requireNotNull(call.parameters["storageRoomId"]) { "Storage Room Id must not be null" }
			val cabinetToCreate = call.receive<Cabinet>()
			call.respond(storageLogic.addCabinet(ShortId(storageRoomId), cabinetToCreate))
		}

		authenticatedPut("/{storageRoomId}/cabinet", permissions = setOf(Permissions.MANAGE_STORAGE)) {
			val storageRoomId = requireNotNull(call.parameters["storageRoomId"]) { "Storage Room Id must not be null" }
			val cabinetToUpdate = call.receive<Cabinet>()
			call.respond(storageLogic.update(ShortId(storageRoomId), cabinetToUpdate))
		}

		authenticatedDelete("/{storageRoomId}/cabinet/{cabinetId}", permissions = setOf(Permissions.MANAGE_STORAGE)) {
			val storageRoomId = requireNotNull(call.parameters["storageRoomId"]) { "Storage Room Id must not be null" }
			val cabinetId = requireNotNull(call.parameters["cabinetId"]) { "Cabinet Id must not be null" }
			call.respond(storageLogic.delete(ShortId(storageRoomId), ShortId(cabinetId)))
		}

		authenticatedPost("/{storageRoomId}/cabinet/{cabinetId}/shelf", permissions = setOf(Permissions.MANAGE_STORAGE)) {
			val storageRoomId = requireNotNull(call.parameters["storageRoomId"]) { "Storage Room Id must not be null" }
			val cabinetId = requireNotNull(call.parameters["cabinetId"]) { "Cabinet Id must not be null" }
			val shelfToCreate = call.receive<Shelf>()
			call.respond(storageLogic.addShelf(ShortId(storageRoomId), ShortId(cabinetId), shelfToCreate))
		}

		authenticatedPut("/{storageRoomId}/cabinet/{cabinetId}/shelf", permissions = setOf(Permissions.MANAGE_STORAGE)) {
			val storageRoomId = requireNotNull(call.parameters["storageRoomId"]) { "Storage Room Id must not be null" }
			val cabinetId = requireNotNull(call.parameters["cabinetId"]) { "Cabinet Id must not be null" }
			val shelfToUpdate = call.receive<Shelf>()
			call.respond(storageLogic.update(ShortId(storageRoomId), ShortId(cabinetId), shelfToUpdate))
		}

		authenticatedDelete("/{storageRoomId}/cabinet/{cabinetId}/shelf/{shelfId}", permissions = setOf(Permissions.MANAGE_STORAGE)) {
			val storageRoomId = requireNotNull(call.parameters["storageRoomId"]) { "Storage Room Id must not be null" }
			val cabinetId = requireNotNull(call.parameters["cabinetId"]) { "Cabinet Id must not be null" }
			val shelfId = requireNotNull(call.parameters["shelfId"]) { "ShelfId must not be null" }
			call.respond(storageLogic.delete(ShortId(storageRoomId), ShortId(cabinetId), ShortId(shelfId)))
		}
	}
