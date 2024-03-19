
package org.cdb.labwitch.models

import org.cdb.labwitch.models.embed.Metric
import org.cdb.labwitch.models.embed.Status
import org.cdb.labwitch.models.embed.SubUnit
import java.util.Date

data class Box(
    override val id: String,
    val materialName: String,
    val quantity: Int,
    val metric: Metric,
    val note: String?,
    val subUnit: SubUnit?,
    val status: Status,
    val position: Int,
    val expirationDate: Date?,
) : StoredEntity
