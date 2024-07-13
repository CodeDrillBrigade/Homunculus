package org.cdb.homunculus.logic

import kotlinx.coroutines.flow.Flow
import org.cdb.homunculus.exceptions.NotFoundException
import org.cdb.homunculus.models.Report
import org.cdb.homunculus.models.dto.NotificationStub
import org.cdb.homunculus.models.embed.ReportStatus
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

	/**
	 * Retrieves the [EntityId]s of all the [Report] where [Report.normalizedName] start with the provided [query].
	 *
	 * @param query the prefix for the properties to search.
	 * @return a [Set] of the [EntityId]s of the matching [Report]s.
	 */
	suspend fun searchIds(query: String): Flow<EntityId>

	/**
	 * Retrieves multiple [Report]s by their [Report.id].
	 *
	 * @param ids the ids of the [Report]s to retrieve. All the ids that do not correspond to an actual material are ignored.
	 * @return a [Flow] of [Report]s.
	 */
	fun getByIds(ids: Set<EntityId>): Flow<Report>

	/**
	 * Sets the [Report.status] of the alert which id is passed as parameter to [status].
	 *
	 * @param reportId the [Report.id] of the alert to update.
	 * @param status the status to set.
	 */
	suspend fun setStatus(
		reportId: EntityId,
		status: ReportStatus,
	)

	/**
	 * Hard-deletes and [Report].
	 *
	 * @param reportId the [Report.id] of the alert to update.
	 */
	suspend fun delete(reportId: EntityId)

	/**
	 * Returns all the [Report]s that can accept a material.
	 *
	 * @param materialId the id of the material to check.
	 * @return a [Flow] of [NotificationStub].
	 */
	fun listByAcceptedMaterial(materialId: EntityId): Flow<NotificationStub>

	/**
	 * Updates the [Report]s which id are present in [reportIds] adding [materialId] to [Report.excludeFilter].
	 *
	 * @param materialId the id of the material to exclude.
	 * @param reportIds the ids of the [Report]s to update.
	 * @return a [Flow] containing the updated [Report]s
	 */
	fun addMaterialToExclusions(
		materialId: EntityId,
		reportIds: Set<EntityId>,
	): Flow<Report>
}
