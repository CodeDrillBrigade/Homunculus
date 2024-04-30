package org.cdb.homunculus.models.embed

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.cdb.homunculus.models.StoredEntity
import org.cdb.homunculus.models.identifiers.EntityId

@Serializable
data class BoxDefinition(
	@SerialName("_id") override val id: EntityId = EntityId.generate(),
	val name: String,
	val description: String? = null,
	val boxUnit: BoxUnit,
) : StoredEntity {
	infix fun contentEquals(other: BoxDefinition) = name == other.name && description == other.description && boxUnit == other.boxUnit
}
