package org.cdb.labwitch.logic.impl

import kotlinx.coroutines.flow.Flow
import org.cdb.labwitch.dao.BoxDao
import org.cdb.labwitch.exceptions.NotFoundException
import org.cdb.labwitch.logic.BoxLogic
import org.cdb.labwitch.models.Box
import org.cdb.labwitch.models.identifiers.EntityId
import org.cdb.labwitch.models.identifiers.HierarchicalId
import org.cdb.labwitch.models.identifiers.Identifier

class BoxLogicImpl(
	private val boxDao: BoxDao
) : BoxLogic {

	override suspend fun create(box: Box): Identifier = checkNotNull(boxDao.save(box)) {
		"Error during box creation"
	}

	override suspend fun get(boxId: EntityId): Box = boxDao.getById(boxId) ?: throw NotFoundException("Box $boxId not found")

	override fun getAll(): Flow<Box> = boxDao.getAll()

	override fun getByMaterial(materialId: EntityId): Flow<Box> = boxDao.getByMaterial(materialId)

	override fun getByPosition(shelfId: HierarchicalId): Flow<Box> = boxDao.getByPosition(shelfId)
}
