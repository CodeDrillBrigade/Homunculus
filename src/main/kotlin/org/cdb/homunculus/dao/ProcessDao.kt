package org.cdb.homunculus.dao

import com.mongodb.kotlin.client.coroutine.MongoCollection
import org.cdb.homunculus.components.DBClient
import org.cdb.homunculus.models.Process
import org.cdb.homunculus.models.identifiers.EntityId

abstract class ProcessDao(client: DBClient) : GenericDao<Process>(client) {
	override val collection: MongoCollection<Process> = client.getCollection()

	override fun wrapIdentifier(id: String): EntityId = EntityId(id)
}
