package org.cdb.labwitch.logic

import org.cdb.labwitch.models.Material
import org.cdb.labwitch.models.identifiers.EntityId
import org.cdb.labwitch.models.identifiers.Identifier

interface MaterialLogic {
	/**
	 * Creates a new [Material].
	 * It also set the [Material.normalizedName] to the normalized version of the [Material.name], for efficient
	 * fuzzy searching.
	 *
	 * @param material the [Material] to create.
	 * @return the [EntityId] of the newly created entity.
	 */
	suspend fun create(material: Material): Identifier

	/**
	 * Retrieves a [Material].
	 *
	 * @param materialId the [EntityId] of the material to find
	 * @return the [Material] with the specified id.
	 * @throws NotFoundException if no [Material] exists with the specified id.
	 */
	suspend fun get(materialId: EntityId): Material
}
