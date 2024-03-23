package org.cdb.labwitch.controller

import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.coroutines.flow.toList
import org.cdb.labwitch.logic.StorageLogic
import org.cdb.labwitch.models.StorageRoom
import org.cdb.labwitch.models.security.Permissions
import org.cdb.labwitch.requests.authenticatedGet
import org.cdb.labwitch.requests.authenticatedPost
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
    }
