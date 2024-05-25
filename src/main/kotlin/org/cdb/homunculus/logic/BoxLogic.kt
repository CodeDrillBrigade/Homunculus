package org.cdb.homunculus.logic

import kotlinx.coroutines.flow.Flow
import org.cdb.homunculus.exceptions.NotFoundException
import org.cdb.homunculus.models.Box
import org.cdb.homunculus.models.embed.UsageLog
import org.cdb.homunculus.models.identifiers.EntityId
import org.cdb.homunculus.models.identifiers.HierarchicalId
import org.cdb.homunculus.models.identifiers.Identifier

interface BoxLogic {
	/**
	 * Adds a new [Box] to the system
	 *
	 * @param box the [Box] to create.
	 * @param userId the id of the user creating the box.
	 * @return the [Box.id] of the newly created box.
	 * @throws IllegalStateException if the box cannot be created.
	 */
	suspend fun create(
		box: Box,
		userId: EntityId,
	): Identifier

	/**
	 * Soft-deletes a [Box] by setting [Box.deletionDate] to the current timestamp.
	 *
	 * @param id the [EntityId] of the box to delete.
	 * @return the id of the updated box.
	 * @throws NotFoundException if no box with the specified id exists.
	 */
	suspend fun delete(id: EntityId): EntityId

	/**
	 * Retrieves a [Box] via ID.
	 *
	 * @param [boxId] the [EntityId] of the box to search.
	 * @return the searched [Box].
	 * @throws NotFoundException if no box with the specified exists.
	 */
	suspend fun get(boxId: EntityId): Box

	/**
	 * Retrieves all the [Box]es in the database.
	 *
	 * @return a [Flow] of [Box]es.
	 */
	fun getAll(): Flow<Box>

	/**
	 * Retrieves all the [Box]es where [Box.material] is equal to [materialId].
	 *
	 * @param materialId the id of the material.
	 * @return a [Flow] of [Box].
	 */
	fun getByMaterial(materialId: EntityId): Flow<Box>

	/**
	 * Retrieves all the [Box]es where [Box.position] is equal to [shelfId].
	 * Only non-deleted Boxes, (i.e. with null [Box.deleted]) are returned.
	 *
	 * @param shelfId the id of the shelf.
	 * @return a [Flow] of [Box].
	 */
	fun getByPosition(shelfId: HierarchicalId): Flow<Box>

	/**
	 * Updates the [Box] with the specified [boxId] appending the [update] passed as
	 * parameter and updating the [Box.quantity] accordingly.
	 *
	 * @param boxId the [Box.id].
	 * @param update the [UsageLog].
	 * @return the id of the updated entity.
	 * @throws NotFoundException if o box with the specified id exists.
	 * @throws IllegalArgumentException if the quantity to remove is greater than the remaining quantity.
	 */
	suspend fun updateQuantity(
		boxId: EntityId,
		update: UsageLog,
	): EntityId

	/**
	 * Updates a [Box] in the system.
	 * This method will ignore [Box.usageLogs], [Box.quantity] or [Box.material].
	 *
	 * @param box the box to update.
	 * @throws NotFoundException if there is no user with such an id in the system.
	 */
	suspend fun modify(box: Box)
}
