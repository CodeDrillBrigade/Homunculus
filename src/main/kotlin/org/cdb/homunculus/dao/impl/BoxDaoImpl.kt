package org.cdb.homunculus.dao.impl

import com.mongodb.client.model.Filters.eq
import kotlinx.coroutines.flow.Flow
import org.cdb.homunculus.annotations.Index
import org.cdb.homunculus.components.DBClient
import org.cdb.homunculus.dao.BoxDao
import org.cdb.homunculus.models.Box
import org.cdb.homunculus.models.identifiers.EntityId
import org.cdb.homunculus.models.identifiers.HierarchicalId

class BoxDaoImpl(client: DBClient) : BoxDao(client) {
	override fun getByMaterial(materialId: EntityId): Flow<Box> = find(eq(Box::material.name, materialId))

	@Index(name = "by_shelf_id", property = "position", unique = false)
	override fun getByPosition(shelfId: HierarchicalId): Flow<Box> = find(eq(Box::position.name, shelfId))

	override fun getAll(): Flow<Box> = get()
}
