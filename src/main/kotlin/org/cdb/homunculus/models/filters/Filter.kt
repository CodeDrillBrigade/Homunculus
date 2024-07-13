package org.cdb.homunculus.models.filters

import kotlinx.serialization.Serializable
import org.bson.conversions.Bson
import org.cdb.homunculus.models.Filterable

@Serializable
sealed interface Filter {
	fun toBson(): Bson

	fun canAccept(filterable: Filterable): Boolean

	fun not(): Filter
}
