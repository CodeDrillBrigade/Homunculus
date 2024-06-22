package org.cdb.homunculus.models.filters

import com.mongodb.client.model.Filters
import kotlinx.serialization.Serializable
import org.bson.conversions.Bson

@Serializable
class ByNameFilter(
	private val name: String,
) : Filter {
	override fun toBson(): Bson = Filters.eq("name", name)
}
