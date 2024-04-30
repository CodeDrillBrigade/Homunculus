package org.cdb.homunculus.dao

import com.mongodb.kotlin.client.coroutine.MongoCollection
import org.cdb.homunculus.components.DBClient
import org.cdb.homunculus.models.embed.BoxDefinition
import org.cdb.homunculus.models.identifiers.EntityId

abstract class BoxDefinitionDao(client: DBClient) : GenericDao<BoxDefinition>(client) {
	override val collection: MongoCollection<BoxDefinition> = client.getCollection()

	override fun wrapIdentifier(id: String): EntityId = EntityId(id)
}
