package org.cdb.homunculus.models.filters

import com.mongodb.client.model.Filters
import kotlinx.serialization.Serializable
import org.bson.conversions.Bson

@Serializable
class NotFilter(
	private val filter: Filter,
) : Filter {
	override fun toBson(): Bson = Filters.not(filter.toBson())
}
