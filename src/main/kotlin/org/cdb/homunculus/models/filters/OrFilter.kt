package org.cdb.homunculus.models.filters

import com.mongodb.client.model.Filters
import kotlinx.serialization.Serializable
import org.bson.conversions.Bson
import org.cdb.homunculus.models.Filterable

@Serializable
data class OrFilter(
	private val filters: List<Filter>,
) : Filter {
	override fun toBson(): Bson = Filters.or(filters.map { it.toBson() })

	override fun canAccept(filterable: Filterable): Boolean = filters.any { it.canAccept(filterable) }

	override fun not() =
		AndFilter(
			filters = filters.map { it.not() },
		)

	fun addFilter(filter: Filter) =
		if (!filters.contains(filter)) {
			copy(filters = filters + filter)
		} else {
			this
		}
}
