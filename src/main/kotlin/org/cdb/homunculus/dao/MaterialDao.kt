package org.cdb.homunculus.dao

import com.mongodb.kotlin.client.coroutine.MongoCollection
import kotlinx.coroutines.flow.Flow
import org.cdb.homunculus.components.DBClient
import org.cdb.homunculus.models.Material
import org.cdb.homunculus.models.identifiers.EntityId

abstract class MaterialDao(client: DBClient) : GenericDao<Material>(client) {
	override val collection: MongoCollection<Material> = client.getCollection()

	override fun wrapIdentifier(id: String): EntityId = EntityId(id)

	/**
	 * Retrieves all the [Material]s where [Material.normalizedName] is not null and starts with [query].
	 *
	 * @param query the prefix for [Material.normalizedName] to search for.
	 * @param limit the maximum number of elements to return. If null, all the elements will be returned.
	 * @return a [Flow] of [Material] that match the condition.
	 */
	abstract fun byFuzzyName(
		query: String,
		limit: Int?,
	): Flow<Material>
}
