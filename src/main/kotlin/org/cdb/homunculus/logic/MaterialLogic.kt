package org.cdb.homunculus.logic

import kotlinx.coroutines.flow.Flow
import org.cdb.homunculus.exceptions.NotFoundException
import org.cdb.homunculus.models.Material
import org.cdb.homunculus.models.identifiers.EntityId
import org.cdb.homunculus.models.identifiers.Identifier

interface MaterialLogic {
	/**
	 * Creates a new [Material].
	 * It also set the [Material.normalizedName] to the normalized version of the [Material.name], for efficient
	 * fuzzy searching.
	 *
	 * @param material the [Material] to create.
	 * @return the [EntityId] of the newly created entity.
	 */
	suspend fun create(material: Material): Identifier

	/**
	 * Retrieves a [Material].
	 *
	 * @param materialId the [EntityId] of the material to find
	 * @return the [Material] with the specified id.
	 * @throws NotFoundException if no [Material] exists with the specified id.
	 */
	suspend fun get(materialId: EntityId): Material

	/**
	 * Retrieves all the [Material]s in the database, excluding the ones where [Material.deletionDate] is set.
	 *
	 * @return a [Flow] of [Material].
	 */
	fun getAll(): Flow<Material>

	/**
	 * Retrieves all the [Material]s based on a match between the normalized [query] and [Material.normalizedName], excluding the ones
	 * where [Material.deletionDate] is set.
	 *
	 * @param query the query, to be normalized.
	 * @param limit the maximum number of elements to return. If null, all the elements will be returned.
	 * @return a [Flow] of [Material] where [Material.normalizedName] starts with the normalized [query].
	 */
	fun findByFuzzyName(
		query: String,
		limit: Int?,
	): Flow<Material>

	/**
	 * Soft-deletes a [Material] by setting [Material.deletionDate]
	 *
	 * @param id the [EntityId] of the box to delete.
	 * @return the id of the updated box.
	 * @throws NotFoundException if no box with the specified id exists.
	 */
	suspend fun delete(id: EntityId): EntityId

	/**
	 * Updates a [Material] in the system.
	 * This method cannot be used to update [Material.normalizedName], [Material.boxDefinition] or [Material.noteList]
	 *
	 * @param material the material to update.
	 * @throws NotFoundException if there is no user with such an id in the system.
	 */
	suspend fun modify(material: Material)
}
