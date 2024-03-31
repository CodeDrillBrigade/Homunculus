package org.cdb.labwitch.dao.impl

import com.mongodb.client.model.Filters
import org.cdb.labwitch.components.DBClient
import org.cdb.labwitch.dao.BoxDefinitionDao
import org.cdb.labwitch.models.embed.BoxDefinition
import org.cdb.labwitch.models.identifiers.Identifier

class BoxDefinitionDaoImpl(client: DBClient) : BoxDefinitionDao(client) {
	override suspend fun get(id: Identifier): BoxDefinition? = get(Filters.eq("_id", id.id))
}
