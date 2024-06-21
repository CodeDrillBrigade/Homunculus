package org.cdb.homunculus.utils

import org.cdb.homunculus.exceptions.NotFoundException

fun guard(
	condition: Boolean,
	message: () -> String,
) {
	if (!condition) {
		throw IllegalAccessException(message())
	}
}

suspend fun <T> exist(
	retriever: suspend () -> T?,
	message: () -> String,
): T = retriever() ?: throw NotFoundException(message())
