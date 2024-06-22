package org.cdb.homunculus.models.filters

import com.mongodb.client.model.Filters
import kotlinx.serialization.Serializable
import org.bson.conversions.Bson

@Serializable
class AndFilter(
	private val filters: List<Filter>,
) : Filter {
	override fun toBson(): Bson = Filters.and(filters.map { it.toBson() })
}
