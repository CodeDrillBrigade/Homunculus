package org.cdb.homunculus.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.cdb.homunculus.models.embed.AlertStatus
import org.cdb.homunculus.models.filters.AndFilter
import org.cdb.homunculus.models.filters.Filter
import org.cdb.homunculus.models.identifiers.EntityId

@Serializable
data class Alert(
	@SerialName("_id") override val id: EntityId = EntityId.generate(),
	val name: String,
	val description: String? = null,
	val status: AlertStatus = AlertStatus.ACTIVE,
	val materialFilter: Filter,
	val excludeFilter: Filter? = null,
	override val threshold: Int,
	override val recipients: Set<EntityId>,
	override val templateId: String? = null,
	val normalizedName: String? = null,
) : StoredEntity, Notification {
	override fun buildFilter(): Filter =
		AndFilter(
			listOfNotNull(
				materialFilter,
				excludeFilter?.not(),
			),
		)
}
