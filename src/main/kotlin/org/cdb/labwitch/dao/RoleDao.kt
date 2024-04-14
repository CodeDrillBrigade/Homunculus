package org.cdb.labwitch.dao

import com.mongodb.kotlin.client.coroutine.MongoCollection
import org.cdb.labwitch.components.DBClient
import org.cdb.labwitch.models.embed.Role
import org.cdb.labwitch.models.identifiers.EntityId

abstract class RoleDao(client: DBClient) : GenericDao<Role>(client) {
	override val collection: MongoCollection<Role> = client.getCollection()

	override fun wrapIdentifier(id: String): EntityId = EntityId(id)

}
