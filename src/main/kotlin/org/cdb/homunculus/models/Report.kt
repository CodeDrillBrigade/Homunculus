package org.cdb.homunculus.models

import kotlinx.datetime.TimeZone
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.cdb.homunculus.models.embed.ActivationMoment
import org.cdb.homunculus.models.embed.ReportStatus
import org.cdb.homunculus.models.filters.AndFilter
import org.cdb.homunculus.models.filters.Filter
import org.cdb.homunculus.models.identifiers.EntityId
import org.cdb.homunculus.utils.getOffsetInHours

@Serializable
data class Report(
	@SerialName("_id") override val id: EntityId = EntityId.generate(),
	val name: String,
	val description: String? = null,
	val status: ReportStatus = ReportStatus.ACTIVE,
	val repeatAt: List<ActivationMoment>,
	val timezone: TimeZone,
	val materialFilter: Filter,
	val excludeFilter: Filter? = null,
	override val threshold: Int,
	override val recipients: Set<EntityId>,
	override val templateId: String? = null,
	val normalizedName: String? = null,
) : StoredEntity, Notification {
	val cronConfigs: List<String>
		get() =
			repeatAt.map {
				"0 17 ${it.hour - timezone.getOffsetInHours()} * * * 0o ${it.day.index}w"
			}

	override fun buildFilter(): Filter =
		AndFilter(
			listOfNotNull(
				materialFilter,
				excludeFilter?.not(),
			),
		)
}
