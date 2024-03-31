package org.cdb.labwitch.dao

import com.mongodb.kotlin.client.coroutine.MongoCollection
import org.cdb.labwitch.components.DBClient
import org.cdb.labwitch.models.embed.Role
import org.cdb.labwitch.models.identifiers.EntityId
import org.cdb.labwitch.models.identifiers.Identifier

abstract class RoleDao(client: DBClient) : GenericDao<Role>(client) {
	override val collection: MongoCollection<Role> = client.getCollection()

	override fun wrapIdentifier(id: String): EntityId = EntityId(id)

	/**
	 * Retrieves a [Role] by id.
	 *
	 * @param id the [EntityId] of the [Role] to retrieve.
	 * @return the [Role], if one exists with the specified id, and null otherwise.
	 */
	abstract suspend fun get(id: Identifier): Role?
}
