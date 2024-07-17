package org.cdb.homunculus.dao

import com.mongodb.kotlin.client.coroutine.MongoCollection
import kotlinx.coroutines.flow.Flow
import org.cdb.homunculus.components.DBClient
import org.cdb.homunculus.models.embed.Tag
import org.cdb.homunculus.models.identifiers.EntityId

abstract class TagDao(client: DBClient) : GenericDao<Tag>(client) {
	override val collection: MongoCollection<Tag> = client.getCollection()

	override fun wrapIdentifier(id: String): EntityId = EntityId(id)

	/**
	 * Retrieves all the [Tag]s where [Tag.normalizedName] is not null and starts with [query].
	 *
	 * @param query the prefix for [Tag.normalizedName] to search for.
	 * @return a [Flow] of [Tag] that match the condition.
	 */
	abstract fun getByFuzzyName(query: String): Flow<Tag>
}
