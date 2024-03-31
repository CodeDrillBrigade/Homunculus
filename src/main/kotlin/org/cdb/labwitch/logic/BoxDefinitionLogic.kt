package org.cdb.labwitch.logic

import org.cdb.labwitch.models.embed.BoxDefinition
import org.cdb.labwitch.models.identifiers.EntityId
import org.cdb.labwitch.models.identifiers.Identifier

interface BoxDefinitionLogic {
	/**
	 * Creates a new [BoxDefinition].
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
}
