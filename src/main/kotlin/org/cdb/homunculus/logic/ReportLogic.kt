package org.cdb.homunculus.logic

import kotlinx.coroutines.flow.Flow
import org.cdb.homunculus.exceptions.NotFoundException
import org.cdb.homunculus.models.Report
import org.cdb.homunculus.models.identifiers.EntityId
import org.cdb.homunculus.models.identifiers.Identifier

interface ReportLogic {
	/**
	 * Creates a new [Report].
	 *
	 * @param report the [Report] to create.
	 * @return the [Report.id].
	 */
	suspend fun create(report: Report): Identifier

	/**
	 * Updates an existing [Report].
	 *
	 * @param report the report to update.
	 * @throws NotFoundException if there is no [Report] with such an id in the system.
	 */
	suspend fun modify(report: Report)

	/**
	 * Retrieves an [Report] by its [Report.id].
	 *
	 * @param reportId the [EntityId] of the report to find
	 * @return the [Report] with the specified id.
	 * @throws NotFoundException if no [Report] exists with the specified id.
	 */
	suspend fun get(reportId: EntityId): Report

	/**
	 * Retrieves all the [Report]s in the database.
	 *
	 * @return a [Flow] of [Report].
	 */
	fun getAll(): Flow<Report>
}
