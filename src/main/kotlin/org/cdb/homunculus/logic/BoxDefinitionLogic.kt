package org.cdb.homunculus.logic

import kotlinx.coroutines.flow.Flow
import org.cdb.homunculus.models.embed.BoxDefinition
import org.cdb.homunculus.models.identifiers.EntityId
import org.cdb.homunculus.models.identifiers.Identifier

interface BoxDefinitionLogic {
	/**
	 * Creates a new [BoxDefinition].
	 * If a [BoxDefinition] with the same parameters already exists in the system, it will not create a new one. Instead, it will return
	 * the existing identifier.
	 *
	 * @param box the [BoxDefinition] to create.
	 * @return the [EntityId] of the newly created entity.
	 */
	suspend fun create(box: BoxDefinition): Identifier

	/**
	 * Retrieves a [BoxDefinition].
	 *
	 * @param boxDefinitionId the [EntityId] of the material to find
	 * @return the [BoxDefinition] with the specified id.
	 * @throws NotFoundException if no [BoxDefinition] exists with the specified id.
	 */
	suspend fun get(boxDefinitionId: EntityId): BoxDefinition

	/**
	 * Retrieves all the [BoxDefinition]s in the database.
	 *
	 * @return a [Flow] of [BoxDefinition]s.
	 */
	fun getAll(): Flow<BoxDefinition>
}
