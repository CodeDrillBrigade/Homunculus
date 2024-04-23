package org.cdb.labwitch.dao

import com.mongodb.kotlin.client.coroutine.MongoCollection
import org.cdb.labwitch.components.DBClient
import org.cdb.labwitch.models.StorageRoom
import org.cdb.labwitch.models.identifiers.ShortId

abstract class StorageDao(client: DBClient) : GenericDao<StorageRoom>(client) {
	override val collection: MongoCollection<StorageRoom> = client.getCollection()

	override fun wrapIdentifier(id: String): ShortId = ShortId(id)
}
