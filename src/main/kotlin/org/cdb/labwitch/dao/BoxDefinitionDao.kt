package org.cdb.labwitch.dao

import com.mongodb.kotlin.client.coroutine.MongoCollection
import org.cdb.labwitch.components.DBClient
import org.cdb.labwitch.models.embed.BoxDefinition
import org.cdb.labwitch.models.identifiers.EntityId
import org.cdb.labwitch.models.identifiers.Identifier

abstract class BoxDefinitionDao(client: DBClient) : GenericDao<BoxDefinition>(client) {
	override val collection: MongoCollection<BoxDefinition> = client.getCollection()

	override fun wrapIdentifier(id: String): EntityId = EntityId(id)

	/**
	 * Retrieves a [BoxDefinition] by id.
	 *
	 * @param id the [EntityId] of the [BoxDefinition] to retrieve.
	 * @return the [BoxDefinition], if one exists with the specified id, and null otherwise.
	 */
	abstract suspend fun get(id: Identifier): BoxDefinition?
}
