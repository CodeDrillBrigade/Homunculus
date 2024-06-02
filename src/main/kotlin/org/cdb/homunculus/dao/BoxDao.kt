package org.cdb.homunculus.dao

import com.mongodb.kotlin.client.coroutine.MongoCollection
import kotlinx.coroutines.flow.Flow
import org.cdb.homunculus.components.DBClient
import org.cdb.homunculus.models.Box
import org.cdb.homunculus.models.identifiers.EntityId
import org.cdb.homunculus.models.identifiers.HierarchicalId

abstract class BoxDao(client: DBClient) : GenericDao<Box>(client) {
	override val collection: MongoCollection<Box> = client.getCollection()

	override fun wrapIdentifier(id: String): EntityId = EntityId(id)

	/**
	 * Retrieves all the boxes with the specified [Box.material].
	 *
	 * @param materialId the [EntityId] of the material
	 * @param includeDeleted whether to include the Boxes where [Box.deletionDate] is not null.
	 * @return a [Flow] of [Box]es.
	 */
	abstract fun getByMaterial(
		materialId: EntityId,
		includeDeleted: Boolean,
	): Flow<Box>

	/**
	 * Retrieves all the [Box]es where [Box.batchNumber] is not null and starts with [query].
	 *
	 * @param query the prefix for [Box.batchNumber] to search for.
	 * @param includeDeleted whether to include the Boxes where [Box.deletionDate] is not null.
	 * @return a [Flow] of [Box] that match the condition.
	 */
	abstract fun getByBatchNumber(
		query: String,
		includeDeleted: Boolean,
	): Flow<Box>

	/**
	 * Retrieves all the boxes in a specified shelf.
	 *
	 * @param shelfId the id of the shelf.
	 * @param includeDeleted whether to include the Boxes where [Box.deletionDate] is not null.
	 * @return a [Flow] of [Box]es.
	 */
	abstract fun getByPosition(
		shelfId: HierarchicalId,
		includeDeleted: Boolean,
	): Flow<Box>
}
