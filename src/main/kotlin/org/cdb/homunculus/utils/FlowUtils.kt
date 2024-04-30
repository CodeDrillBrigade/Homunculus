package org.cdb.homunculus.utils

import com.mongodb.kotlin.client.coroutine.FindFlow

fun <T : Any> FindFlow<T>.limit(limit: Int?): FindFlow<T> = limit?.let { this.limit(it) } ?: this
