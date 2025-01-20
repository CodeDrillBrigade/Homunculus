package org.cdb.homunculus.dao

import com.mongodb.kotlin.client.coroutine.FindFlow
import com.mongodb.kotlin.client.coroutine.MongoCollection
import kotlinx.coroutines.flow.Flow
import org.bson.conversions.Bson
import org.cdb.homunculus.components.DBClient
import org.cdb.homunculus.models.Box
import org.cdb.homunculus.models.Material
import org.cdb.homunculus.models.identifiers.EntityId

abstract class MaterialDao(client: DBClient) : GenericDao<Material>(client) {
	override val collection: MongoCollection<Material> = client.getCollection()

	override fun wrapIdentifier(id: String): EntityId = EntityId(id)

	/**
	 * Retrieves all the [Material]s where [Material.normalizedName] is not null and starts with [query].
	 *
	 * @param query the prefix for [Material.normalizedName] to search for.
	 * @param includeDeleted whether to include the Boxes where [Box.deletionDate] is not null.
	 * @param limit the maximum number of elements to return. If null, all the elements will be returned.
	 * @param skip the number of elements to skip.
	 * @return a [Flow] of [Material] that match the condition.
	 */
	abstract fun getByFuzzyName(
		query: String,
		includeDeleted: Boolean,
		limit: Int?,
		skip: Int?,
	): Flow<Material>

	/**
	 * Retrieves the last [limit] [Material]s in descending order by [Material.creationDate].
	 *
	 * @param limit the number of elements to retrieve.
	 * @return a [Flow] of [Material].
	 */
	abstract fun getLastCreated(limit: Int): Flow<Material>

	/**
	 * Retrieves all the [Material]s where [Material.referenceCode] is not null and starts with [query].
	 *
	 * @param query the prefix for [Material.referenceCode] to search for.
	 * @param includeDeleted whether to include the Boxes where [Box.deletionDate] is not null.
	 * @param limit the maximum number of elements to return. If null, all the elements will be returned.
	 * @param skip the number of elements to skip.
	 * @return a [Flow] of [Material] that match the condition.
	 */
	abstract fun searchByReferenceCode(
		query: String,
		includeDeleted: Boolean,
		limit: Int?,
		skip: Int?,
	): Flow<Material>

	/**
	 * Retrieves all the [Material]s where [Material.referenceCode] is equal to [referenceCode].
	 *
	 * @param referenceCode the [Material.referenceCode] to search for.
	 * @param includeDeleted whether to include the Boxes where [Box.deletionDate] is not null.
	 * @return a [Flow] of [Material] that match the condition.
	 */
	abstract fun getByReferenceCode(
		referenceCode: String,
		includeDeleted: Boolean,
	): Flow<Material>

	/**
	 * Retrieves all the [Material]s where [Material.brand] starts with [query].
	 *
	 * @param query the prefix for [Material.brand] to search for.
	 * @param includeDeleted whether to include the Boxes where [Box.deletionDate] is not null.
	 * @param limit the maximum number of elements to return. If null, all the elements will be returned.
	 * @param skip the number of elements to skip.
	 * @return a [Flow] of [Material] that match the condition.
	 */
	abstract fun getByBrand(
		query: String,
		includeDeleted: Boolean,
		limit: Int?,
		skip: Int?,
	): Flow<Material>

	/**
	 * Retrieves all the [Material]s where [Material.tags] contains [tagId].
	 *
	 * @param tagId the id of the tag.
	 * @return a [Flow] of [Material]s.
	 */
	abstract fun getByTagId(tagId: EntityId): Flow<Material>

	/**
	 * Retrieves all the [Material]s, allowing for sorting.
	 *
	 * @param sort a [Bson] filter for the sorting or null if no sort is required.
	 * @return a [Flow] of [Material]
	 */
	abstract fun get(sort: Bson?): Flow<Material>
}
