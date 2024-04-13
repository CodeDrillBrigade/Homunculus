package org.cdb.labwitch.logic.impl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import org.cdb.labwitch.dao.BoxDefinitionDao
import org.cdb.labwitch.exceptions.NotFoundException
import org.cdb.labwitch.logic.BoxDefinitionLogic
import org.cdb.labwitch.models.embed.BoxDefinition
import org.cdb.labwitch.models.identifiers.EntityId
import org.cdb.labwitch.models.identifiers.Identifier

class BoxDefinitionLogicImpl(
	private val boxDefinitionDao: BoxDefinitionDao,
) : BoxDefinitionLogic {
	override suspend fun create(box: BoxDefinition): Identifier =
		getAll().firstOrNull {
			it contentEquals box
		}?.id ?: boxDefinitionDao.save(box)

	override suspend fun get(boxDefinitionId: EntityId): BoxDefinition =
		boxDefinitionDao.get(boxDefinitionId) ?: throw NotFoundException("BoxDefinition $boxDefinitionId does not exist")

	override fun getAll(): Flow<BoxDefinition> = boxDefinitionDao.get()
}
