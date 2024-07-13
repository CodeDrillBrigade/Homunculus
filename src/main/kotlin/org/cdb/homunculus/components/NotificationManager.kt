package org.cdb.homunculus.components

import org.cdb.homunculus.models.Report
import org.cdb.homunculus.models.identifiers.EntityId

interface NotificationManager {
	/**
	 * Registers a new [Report] to be periodically executed if the condition arise.
	 *
	 * @param report the [Report] to register.
	 */
	fun addReport(report: Report)

	/**
	 * Removes a previously registered [Report].
	 *
	 * @param report the [Report].
	 */
	suspend fun removeReport(report: Report)

	/**
	 * Updates a previously registered [Report].
	 *
	 * @param report the [Report] to update.
	 */
	suspend fun updateReport(report: Report)

	/**
	 * Loads all the active [Report]s from the database and registers them.
	 */
	suspend fun loadReports()

	/**
	 * Triggers the check for all the alerts related to the material.
	 *
	 * @param materialId the material id.
	 */
	fun checkMaterial(materialId: EntityId)
}
