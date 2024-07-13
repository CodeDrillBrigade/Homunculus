package org.cdb.homunculus.logic

import kotlinx.coroutines.flow.Flow
import org.cdb.homunculus.exceptions.NotFoundException
import org.cdb.homunculus.models.Material
import org.cdb.homunculus.models.filters.Filter
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

	/**
	 * Retrieves the [EntityId]s of all the [Material] where [Material.normalizedName], [Material.brand], or [Material.referenceCode] start
	 * with the provided [query].
	 * If one or more [tagIds] are specified, then only the Materials with the specified [Material.tags] are considered.
	 *
	 * @param query the prefix for the properties to search.
	 * @param tagIds the ids of the tags that a [Material] must have to be included in the results.
	 * @param limit the maximum number of elements to return. If null, all the elements will be returned.
	 * @return a [Set] of the [EntityId]s of the matching [Material]s.
	 */
	suspend fun searchIds(
		query: String,
		tagIds: Set<EntityId>?,
		limit: Int?,
	): Set<EntityId>

	/**
	 * Retrieves the first [limit] different [Material.name]s of all the [Material] where [Material.normalizedName], [Material.brand],
	 * or [Material.referenceCode] start with the provided [query].
	 * If one or more [tagIds] are specified, then only the Materials with the specified [Material.tags] are considered.
	 *
	 * @param query the prefix for the properties to search.
	 * @param tagIds the ids of the tags that a [Material] must have to be included in the results.
	 * @param limit the maximum number of unique elements to return. If null, all the elements will be returned.
	 * @return a [Set] of the matching [Material.name]s.
	 */
	suspend fun searchNames(
		query: String,
		tagIds: Set<EntityId>?,
		limit: Int?,
	): Set<String>

	/**
	 * Retrieves multiple [Material]s by their [Material.id].
	 *
	 * @param ids the ids of the [Material]s to retrieve. All the ids that do not correspond to an actual material are ignored.
	 * @return a [Flow] of [Material]s.
	 */
	fun getByIds(ids: Set<EntityId>): Flow<Material>

	/**
	 * Retrieves the last [limit] [Material]s in descending order by [Material.creationDate].
	 *
	 * @param limit the number of elements to retrieve.
	 * @return a [Flow] of [Material].
	 */
	fun getLastCreated(limit: Int): Flow<Material>

	/**
	 * Retrieves all the [Material]s that match the provided [filter].
	 *
	 * @param filter the [Filter] to apply, it will be converted to a Bson query.
	 * @return a [Flow] of [Material]s.
	 */
	fun filter(filter: Filter): Flow<Material>
}
