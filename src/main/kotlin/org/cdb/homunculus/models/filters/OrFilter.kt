package org.cdb.homunculus.models.filters

import com.mongodb.client.model.Filters
import kotlinx.serialization.Serializable
import org.bson.conversions.Bson

@Serializable
class OrFilter(
	private val filters: List<Filter>,
) : Filter {
	override fun toBson(): Bson = Filters.or(filters.map { it.toBson() })
}
