package org.cdb.homunculus.models.filters

import com.mongodb.client.model.Filters
import kotlinx.serialization.Serializable
import org.bson.conversions.Bson
import org.cdb.homunculus.models.Filterable

@Serializable
class NotFilter(
	private val filter: Filter,
) : Filter {
	override fun toBson(): Bson = Filters.not(filter.toBson())

	override fun canAccept(filterable: Filterable): Boolean = !filter.canAccept(filterable)

	override fun not(): Filter = filter
}
