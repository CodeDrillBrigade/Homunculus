
package org.cdb.labwitch.models

import kotlinx.serialization.SerialName
import org.cdb.labwitch.models.embed.Metric
import org.cdb.labwitch.models.embed.Status
import org.cdb.labwitch.models.embed.SubUnit
import org.cdb.labwitch.models.embed.UsageLog
import org.cdb.labwitch.models.identifiers.EntityId
import java.util.Date
import java.util.SortedSet

data class Box(
    @SerialName("_id") override val id: EntityId,
    val materialName: String,
    val quantity: Int,
    val metric: Metric,
    val note: String?,
    val subUnit: SubUnit?,
    val status: Status,
    val position: Int,
    val expirationDate: Date?,
    val usageLog: SortedSet<UsageLog>,
) : StoredEntity
