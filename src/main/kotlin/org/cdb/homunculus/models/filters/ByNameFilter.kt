package org.cdb.homunculus.models.filters

import com.mongodb.client.model.Filters
import kotlinx.serialization.Serializable
import org.bson.conversions.Bson
import org.cdb.homunculus.models.Filterable

@Serializable
data class ByNameFilter(
	private val name: String,
) : Filter {
	override fun toBson(): Bson = Filters.eq("name", name)

	override fun canAccept(filterable: Filterable): Boolean = filterable.name.text == name

	override fun not(): Filter = NotFilter(this)
}
