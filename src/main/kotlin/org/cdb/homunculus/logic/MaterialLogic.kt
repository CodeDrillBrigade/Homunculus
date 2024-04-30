package org.cdb.homunculus.logic

import kotlinx.coroutines.flow.Flow
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
	 * Retrieves all the [Material]s in the database.
	 *
	 * @return a [Flow] of [Material].
	 */
	fun getAll(): Flow<Material>

	/**
	 * Retrieves all the [Material]s based on a match between the normalized [query] and [Material.normalizedName].
	 *
	 * @param query the query, to be normalized.
	 * @param limit the maximum number of elements to return. If null, all the elements will be returned.
	 * @return a [Flow] of [Material] where [Material.normalizedName] starts with the normalized [query].
	 */
	fun findByFuzzyName(
		query: String,
		limit: Int?,
	): Flow<Material>
}
