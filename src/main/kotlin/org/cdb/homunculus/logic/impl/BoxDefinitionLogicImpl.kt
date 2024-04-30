package org.cdb.homunculus.logic.impl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import org.cdb.homunculus.dao.BoxDefinitionDao
import org.cdb.homunculus.exceptions.NotFoundException
import org.cdb.homunculus.logic.BoxDefinitionLogic
import org.cdb.homunculus.models.embed.BoxDefinition
import org.cdb.homunculus.models.identifiers.EntityId
import org.cdb.homunculus.models.identifiers.Identifier

class BoxDefinitionLogicImpl(
	private val boxDefinitionDao: BoxDefinitionDao,
) : BoxDefinitionLogic {
	override suspend fun create(box: BoxDefinition): Identifier =
		getAll().firstOrNull {
			it contentEquals box
		}?.id ?: boxDefinitionDao.save(box)

	override suspend fun get(boxDefinitionId: EntityId): BoxDefinition =
		boxDefinitionDao.getById(boxDefinitionId) ?: throw NotFoundException("BoxDefinition $boxDefinitionId does not exist")

	override fun getAll(): Flow<BoxDefinition> = boxDefinitionDao.get()
}
