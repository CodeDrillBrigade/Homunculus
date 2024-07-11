package org.cdb.homunculus.controller

import io.ktor.server.application.call
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Routing
import io.ktor.server.routing.route
import kotlinx.coroutines.flow.toList
import org.cdb.homunculus.logic.ReportLogic
import org.cdb.homunculus.models.Report
import org.cdb.homunculus.models.embed.ReportStatus
import org.cdb.homunculus.models.identifiers.EntityId
import org.cdb.homunculus.models.security.Permissions
import org.cdb.homunculus.requests.authenticatedDelete
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

		authenticatedPost("/byIds") {
			val reportIds = call.receive<Set<EntityId>>()
			require(reportIds.isNotEmpty()) { "Report Ids must not be null or empty" }
			call.respond(reportLogic.getByIds(reportIds))
		}

		authenticatedGet("/idsByName") {
			val query = requireNotNull(call.request.queryParameters["query"]) { "query must not be null." }
			call.respond(reportLogic.searchIds(query))
		}

		authenticatedDelete("/{reportId}", permissions = setOf(Permissions.MANAGE_NOTIFICATIONS)) {
			val reportId = checkNotNull(call.parameters["reportId"]) { "Report Id must not be null" }
			call.respond(reportLogic.delete(EntityId(reportId)))
		}

		authenticatedPut("/{reportId}/status", permissions = setOf(Permissions.MANAGE_NOTIFICATIONS)) {
			val reportId = checkNotNull(call.parameters["reportId"]) { "Report Id must not be null" }
			val status =
				requireNotNull(
					call.request.queryParameters["status"]?.let { ReportStatus.valueOf(it) },
				) { "Status must not be null." }
			call.respond(reportLogic.setStatus(EntityId(reportId), status))
		}
	}
