package org.cdb.homunculus.logic

import kotlinx.coroutines.flow.Flow
import org.cdb.homunculus.models.embed.Tag
import org.cdb.homunculus.models.identifiers.EntityId
import org.cdb.homunculus.models.identifiers.Identifier

interface TagLogic {
	/**
	 * Creates a new [Tag].
	 *
	 * @param tag the [Tag] to create.
	 * @return the [EntityId] of the newly created entity.
	 */
	suspend fun create(tag: Tag): Identifier

	/**
	 * Retrieves a [Tag].
	 *
	 * @param tagId the [EntityId] of the material to find
	 * @return the [Tag] with the specified id.
	 * @throws NotFoundException if no [Tag] exists with the specified id.
	 */
	suspend fun get(tagId: EntityId): Tag

	/**
	 * Retrieves all the [Tag]s in the database.
	 *
	 * @return a [Flow] of [Tag]s.
	 */
	suspend fun getAll(): Flow<Tag>
}
