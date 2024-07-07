package org.cdb.homunculus.logic

import kotlinx.coroutines.flow.Flow
import org.cdb.homunculus.exceptions.NotFoundException
import org.cdb.homunculus.models.Alert
import org.cdb.homunculus.models.identifiers.EntityId
import org.cdb.homunculus.models.identifiers.Identifier

interface AlertLogic {
	/**
	 * Creates a new [Alert].
	 *
	 * @param alert the [Alert] to create.
	 * @return the [Alert.id].
	 */
	suspend fun create(alert: Alert): Identifier

	/**
	 * Updates an existing [Alert].
	 *
	 * @param alert the alert to update.
	 * @throws NotFoundException if there is no [Alert] with such an id in the system.
	 */
	suspend fun modify(alert: Alert)

	/**
	 * Retrieves an [Alert] by its [Alert.id].
	 *
	 * @param alertId the [EntityId] of the alert to find
	 * @return the [Alert] with the specified id.
	 * @throws NotFoundException if no [Alert] exists with the specified id.
	 */
	suspend fun get(alertId: EntityId): Alert

	/**
	 * Retrieves all the [Alert]s in the database.
	 *
	 * @return a [Flow] of [Alert].
	 */
	fun getAll(): Flow<Alert>
}
