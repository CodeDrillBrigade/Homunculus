
package org.cdb.labwitch.models

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
	val expirationDate: Date?
) : StoredEntity

data class SubUnit(
	val quantity: Int,
	val subUnit: SubSubUnit?
)

data class SubSubUnit(
	val value: Float,
	val metric: Metric
)

enum class Metric
{
	// TODO
}

enum class Status
{
	// TODO
}