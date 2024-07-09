package org.cdb.homunculus.logic

import kotlinx.coroutines.flow.Flow
import org.cdb.homunculus.exceptions.NotFoundException
import org.cdb.homunculus.models.Alert
import org.cdb.homunculus.models.Material
import org.cdb.homunculus.models.embed.AlertStatus
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

	/**
	 * Retrieves the [EntityId]s of all the [Alert] where [Material.normalizedName] start with the provided [query].
	 *
	 * @param query the prefix for the properties to search.
	 * @return a [Set] of the [EntityId]s of the matching [Alert]s.
	 */
	suspend fun searchIds(query: String): Flow<EntityId>

	/**
	 * Retrieves multiple [Alert]s by their [Alert.id].
	 *
	 * @param ids the ids of the [Alert]s to retrieve. All the ids that do not correspond to an actual material are ignored.
	 * @return a [Flow] of [Alert]s.
	 */
	fun getByIds(ids: Set<EntityId>): Flow<Alert>

	/**
	 * Sets the [Alert.status] of the alert which id is passed as parameter to [status].
	 *
	 * @param alertId the [Alert.id] of the alert to update.
	 * @param status the status to set.
	 */
	suspend fun setStatus(
		alertId: EntityId,
		status: AlertStatus,
	)

	/**
	 * Hard-deletes and [Alert].
	 *
	 * @param alertId the [Alert.id] of the alert to update.
	 */
	suspend fun delete(alertId: EntityId)
}
