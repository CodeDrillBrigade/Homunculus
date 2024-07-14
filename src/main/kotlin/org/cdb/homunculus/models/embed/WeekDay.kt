package org.cdb.homunculus.models.embed

import kotlinx.serialization.Serializable

@Serializable
enum class WeekDay(val index: Int) {
	MONDAY(1),
	TUESDAY(2),
	WEDNESDAY(3),
	THURSDAY(4),
	FRIDAY(5),
	SATURDAY(6),
	SUNDAY(0),
}
