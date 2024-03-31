package org.cdb.labwitch.logic.impl

import org.cdb.labwitch.dao.BoxDao
import org.cdb.labwitch.logic.BoxLogic
import org.cdb.labwitch.models.Box
import org.cdb.labwitch.models.BoxCreationData
import org.cdb.labwitch.models.identifiers.EntityId

class BoxLogicImpl(private val boxDao: BoxDao) : BoxLogic {
	override suspend fun addBox(boxCreationData: BoxCreationData): Box {
		val newBox =
			Box(
				id = EntityId.generate(),
				materialName = boxCreationData.materialName,
				quantity = boxCreationData.quantity,
				metric = boxCreationData.metric,
				note = boxCreationData.note,
				subUnit = boxCreationData.subUnit,
				status = boxCreationData.status,
				position = boxCreationData.position,
				expirationDate = boxCreationData.expirationDate,
				usageLog = sortedSetOf(),
			)
		val createId =
			checkNotNull(boxDao.save(newBox)) {
				"Error during box creation"
			}
		return checkNotNull(boxDao.get(createId)) {
			"Error during retrieval of box"
		}
	}

	override suspend fun get(boxId: EntityId): Box {
		return requireNotNull(boxDao.get(boxId)) {
			"Box not found"
		}
	}
}
