package org.cdb.labwitch.dao.impl

import com.mongodb.client.model.Filters.eq
import kotlinx.coroutines.flow.Flow
import org.cdb.labwitch.components.DBClient
import org.cdb.labwitch.dao.BoxDao
import org.cdb.labwitch.models.Box
import org.cdb.labwitch.models.identifiers.EntityId
import org.cdb.labwitch.models.identifiers.HierarchicalId

class BoxDaoImpl(client: DBClient) : BoxDao(client) {

	override fun getByMaterial(materialId: EntityId): Flow<Box> = find(eq(Box::material.name, materialId))

	override fun getByPosition(shelfId: HierarchicalId): Flow<Box> = find(eq(Box::position.name, shelfId))

	override fun getAll(): Flow<Box> = get()

}
