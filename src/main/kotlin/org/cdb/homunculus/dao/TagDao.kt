package org.cdb.homunculus.dao

import com.mongodb.kotlin.client.coroutine.MongoCollection
import org.cdb.homunculus.components.DBClient
import org.cdb.homunculus.models.embed.Tag
import org.cdb.homunculus.models.identifiers.EntityId

abstract class TagDao(client: DBClient) : GenericDao<Tag>(client) {
	override val collection: MongoCollection<Tag> = client.getCollection()

	override fun wrapIdentifier(id: String): EntityId = EntityId(id)
}
