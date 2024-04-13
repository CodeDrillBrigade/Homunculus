package org.cdb.labwitch.dao

import com.mongodb.kotlin.client.coroutine.MongoCollection
import org.cdb.labwitch.components.DBClient
import org.cdb.labwitch.models.embed.Tag
import org.cdb.labwitch.models.identifiers.EntityId
import org.cdb.labwitch.models.identifiers.Identifier

abstract class TagDao(client: DBClient) : GenericDao<Tag>(client) {
	override val collection: MongoCollection<Tag> = client.getCollection()

	override fun wrapIdentifier(id: String): EntityId = EntityId(id)

	/**
	 * Retrieves a [Tag] by id.
	 *
	 * @param id the [EntityId] of the [Tag] to retrieve.
	 * @return the [Tag], if one exists with the specified id, and null otherwise.
	 */
	abstract suspend fun get(id: Identifier): Tag?
}
