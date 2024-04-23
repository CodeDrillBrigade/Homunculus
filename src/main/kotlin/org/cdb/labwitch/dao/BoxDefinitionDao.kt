package org.cdb.labwitch.dao

import com.mongodb.kotlin.client.coroutine.MongoCollection
import org.cdb.labwitch.components.DBClient
import org.cdb.labwitch.models.embed.BoxDefinition
import org.cdb.labwitch.models.identifiers.EntityId

abstract class BoxDefinitionDao(client: DBClient) : GenericDao<BoxDefinition>(client) {
	override val collection: MongoCollection<BoxDefinition> = client.getCollection()

	override fun wrapIdentifier(id: String): EntityId = EntityId(id)
}
