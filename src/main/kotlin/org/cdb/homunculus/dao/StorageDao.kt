package org.cdb.homunculus.dao

import com.mongodb.kotlin.client.coroutine.MongoCollection
import org.cdb.homunculus.components.DBClient
import org.cdb.homunculus.models.StorageRoom
import org.cdb.homunculus.models.identifiers.ShortId

abstract class StorageDao(client: DBClient) : GenericDao<StorageRoom>(client) {
	override val collection: MongoCollection<StorageRoom> = client.getCollection()

	override fun wrapIdentifier(id: String): ShortId = ShortId(id)
}
