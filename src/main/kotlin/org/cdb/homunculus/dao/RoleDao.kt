package org.cdb.homunculus.dao

import com.mongodb.kotlin.client.coroutine.MongoCollection
import org.cdb.homunculus.components.DBClient
import org.cdb.homunculus.models.embed.Role
import org.cdb.homunculus.models.identifiers.EntityId

abstract class RoleDao(client: DBClient) : GenericDao<Role>(client) {
	override val collection: MongoCollection<Role> = client.getCollection()

	override fun wrapIdentifier(id: String): EntityId = EntityId(id)
}
