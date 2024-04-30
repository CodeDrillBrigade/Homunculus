package org.cdb.homunculus.dao.impl

import com.mongodb.client.model.Filters
import kotlinx.coroutines.flow.Flow
import org.cdb.homunculus.annotations.Index
import org.cdb.homunculus.components.DBClient
import org.cdb.homunculus.dao.MaterialDao
import org.cdb.homunculus.models.Material
import org.cdb.homunculus.utils.limit
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
