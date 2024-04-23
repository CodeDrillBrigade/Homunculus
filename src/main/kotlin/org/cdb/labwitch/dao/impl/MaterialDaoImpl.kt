package org.cdb.labwitch.dao.impl

import com.mongodb.client.model.Filters
import kotlinx.coroutines.flow.Flow
import org.cdb.labwitch.annotations.Index
import org.cdb.labwitch.components.DBClient
import org.cdb.labwitch.dao.MaterialDao
import org.cdb.labwitch.models.Material
import org.cdb.labwitch.utils.limit
import java.util.regex.Pattern

class MaterialDaoImpl(client: DBClient) : MaterialDao(client) {
	@Index(name = "by_fuzzy_name", property = "normalizedName")
	override fun byFuzzyName(
		query: String,
		limit: Int?,
	): Flow<Material> =
		collection.find(
			Filters.regex(Material::normalizedName.name, Pattern.compile("^$query.*")),
		).limit(limit)
}
