package org.cdb.labwitch.models

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import org.cdb.labwitch.models.embed.Metric
import org.cdb.labwitch.models.embed.Status
import org.cdb.labwitch.models.embed.SubUnit
import java.util.Date

@Serializable
data class BoxCreationData(
	val id: String,
	val materialName: String,
	val quantity: Int,
	val metric: Metric,
	val note: String?,
	val subUnit: SubUnit?,
	val status: Status,
	val position: Int,
	@Contextual
	val expirationDate: Date?
)