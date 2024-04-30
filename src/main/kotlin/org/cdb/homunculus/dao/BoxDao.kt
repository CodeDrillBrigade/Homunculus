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
	 * Retrieves all the boxes with the specified [materialId].
	 *
	 * @param materialId the [EntityId] of the material
	 * @return a [Flow] of [Box]es.
	 */
	abstract fun getByMaterial(materialId: EntityId): Flow<Box>

	/**
	 * Retrieves all the boxes in a specified shelf.
	 *
	 * @param shelfId the id of the shelf.
	 * @return a [Flow] of [Box]es.
	 */
	abstract fun getByPosition(shelfId: HierarchicalId): Flow<Box>

	/**
	 * @return a [Flow] containing all the [Box]es in the database.
	 */
	abstract fun getAll(): Flow<Box>
}
