package org.cdb.homunculus.models.filters

import com.mongodb.client.model.Filters
import kotlinx.serialization.Serializable
import org.bson.conversions.Bson

@Serializable
class ByTagsFilter(
	private val tagIds: List<String>,
) : Filter {
	override fun toBson(): Bson = Filters.`in`("tags", tagIds)
}
