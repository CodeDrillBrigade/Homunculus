package org.cdb.homunculus.models.filters

import com.mongodb.client.model.Filters
import kotlinx.serialization.Serializable
import org.bson.conversions.Bson

@Serializable
class ByIdFilter(
	private val id: String,
) : Filter {
	override fun toBson(): Bson = Filters.eq("_id", id)
}
