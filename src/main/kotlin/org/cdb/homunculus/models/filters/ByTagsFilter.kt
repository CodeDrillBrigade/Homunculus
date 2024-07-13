package org.cdb.homunculus.models.filters

import com.mongodb.client.model.Filters
import kotlinx.serialization.Serializable
import org.bson.conversions.Bson
import org.cdb.homunculus.models.Filterable

@Serializable
data class ByTagsFilter(
	private val tagIds: List<String>,
) : Filter {
	override fun toBson(): Bson = Filters.`in`("tags", tagIds)

	override fun canAccept(filterable: Filterable): Boolean =
		filterable.tags.any {
			tagIds.contains(it.id)
		}

	override fun not(): Filter = NotFilter(this)
}
