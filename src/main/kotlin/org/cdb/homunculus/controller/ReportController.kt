package org.cdb.homunculus.controller

import io.ktor.server.application.call
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Routing
import io.ktor.server.routing.route
import kotlinx.coroutines.flow.toList
import org.cdb.homunculus.logic.ReportLogic
import org.cdb.homunculus.models.Report
import org.cdb.homunculus.models.identifiers.EntityId
import org.cdb.homunculus.models.security.Permissions
import org.cdb.homunculus.requests.authenticatedGet
import org.cdb.homunculus.requests.authenticatedPost
import org.cdb.homunculus.requests.authenticatedPut
import org.koin.ktor.ext.inject

fun Routing.reportController() =
	route("/report") {
		val reportLogic by inject<ReportLogic>()

		authenticatedGet("/{reportId}") {
			val reportId = checkNotNull(call.parameters["reportId"]) { "Report Id must not be null" }
			call.respond(reportLogic.get(EntityId(reportId)))
		}

		authenticatedGet("") {
			call.respond(reportLogic.getAll().toList())
		}

		authenticatedPost("", permissions = setOf(Permissions.MANAGE_NOTIFICATIONS)) {
			val reportToCreate = call.receive<Report>()
			call.respond(reportLogic.create(reportToCreate))
		}

		authenticatedPut("", permissions = setOf(Permissions.MANAGE_NOTIFICATIONS)) {
			val reportToUpdate = call.receive<Report>()
			call.respond(reportLogic.modify(reportToUpdate))
		}
	}
