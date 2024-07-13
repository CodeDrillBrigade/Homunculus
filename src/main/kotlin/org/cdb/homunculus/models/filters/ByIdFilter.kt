package org.cdb.homunculus.models.filters

import com.mongodb.client.model.Filters
import kotlinx.serialization.Serializable
import org.bson.conversions.Bson
import org.cdb.homunculus.models.Filterable

@Serializable
data class ByIdFilter(
	private val id: String,
) : Filter {
	override fun toBson(): Bson = Filters.eq("_id", id)

	override fun canAccept(filterable: Filterable): Boolean = filterable.id.id == id

	override fun not(): Filter = NotFilter(this)
}
