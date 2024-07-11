package org.cdb.homunculus.utils

import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.offsetAt

/**
 * @return the offset of a [TimeZone] in hours.
 */
fun TimeZone.getOffsetInHours(): Int {
	val offset = offsetAt(Clock.System.now())

	// Convert the offset to hours
	val offsetInSeconds = offset.totalSeconds
	val offsetInHours = offsetInSeconds / 3600

	return offsetInHours
}

fun TimeZone.getRelativeOffset(reference: TimeZone = TimeZone.currentSystemDefault()): Int {
	val referenceOffset = reference.getOffsetInHours()
	return referenceOffset - getOffsetInHours()
}
