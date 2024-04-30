package org.cdb.labwitch.logic

import kotlinx.coroutines.flow.Flow
import org.cdb.labwitch.models.Box
import org.cdb.labwitch.models.identifiers.EntityId
import org.cdb.labwitch.models.identifiers.HierarchicalId
import org.cdb.labwitch.models.identifiers.Identifier

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
	 * Soft-deletes a [Box] by setting [Box.deleted] to the current timestamp.
	 *
	 * @param id the [EntityId] of the box to delete.
	 * @return the id of the updated box.
	 * @throws NotFoundException if tno box with the specified id exists.
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
}
