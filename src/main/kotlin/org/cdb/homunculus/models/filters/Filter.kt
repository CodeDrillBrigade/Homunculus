package org.cdb.homunculus.models.filters

import kotlinx.serialization.Serializable
import org.bson.conversions.Bson

@Serializable
sealed interface Filter {
	fun toBson(): Bson
}
