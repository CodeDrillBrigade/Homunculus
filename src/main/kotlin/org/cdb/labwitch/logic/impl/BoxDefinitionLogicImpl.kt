package org.cdb.labwitch.logic.impl

import org.cdb.labwitch.dao.BoxDefinitionDao
import org.cdb.labwitch.exceptions.NotFoundException
import org.cdb.labwitch.logic.BoxDefinitionLogic
import org.cdb.labwitch.models.embed.BoxDefinition
import org.cdb.labwitch.models.identifiers.EntityId
import org.cdb.labwitch.models.identifiers.Identifier

class BoxDefinitionLogicImpl(
	private val boxDefinitionDao: BoxDefinitionDao,
) : BoxDefinitionLogic {
	override suspend fun create(box: BoxDefinition): Identifier = boxDefinitionDao.save(box)

	override suspend fun get(boxDefinitionId: EntityId): BoxDefinition =
		boxDefinitionDao.get(boxDefinitionId) ?: throw NotFoundException("BoxDefinition $boxDefinitionId does not exist")
}
