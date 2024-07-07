package org.cdb.homunculus.models.filters

import com.mongodb.client.model.Filters
import kotlinx.serialization.Serializable
import org.bson.conversions.Bson
import org.cdb.homunculus.models.Filterable

@Serializable
class AndFilter(
	private val filters: List<Filter>,
) : Filter {
	override fun toBson(): Bson = Filters.and(filters.map { it.toBson() })

	override fun canAccept(filterable: Filterable): Boolean = filters.all { it.canAccept(filterable) }

	override fun not() =
		OrFilter(
			filters = filters.map { it.not() },
		)
}
