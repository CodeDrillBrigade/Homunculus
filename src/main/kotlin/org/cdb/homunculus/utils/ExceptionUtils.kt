package org.cdb.homunculus.utils

fun guard(
	condition: Boolean,
	message: () -> String,
) {
	if (!condition) {
		throw IllegalAccessException(message())
	}
}
