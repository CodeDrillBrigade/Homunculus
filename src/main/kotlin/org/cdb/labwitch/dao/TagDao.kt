package org.cdb.labwitch.dao

import com.mongodb.kotlin.client.coroutine.MongoCollection
import org.cdb.labwitch.components.DBClient
import org.cdb.labwitch.models.embed.Tag
import org.cdb.labwitch.models.identifiers.EntityId

abstract class TagDao(client: DBClient) : GenericDao<Tag>(client) {
	override val collection: MongoCollection<Tag> = client.getCollection()

	override fun wrapIdentifier(id: String): EntityId = EntityId(id)
}
