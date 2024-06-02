package org.cdb.homunculus.dao.impl

import com.mongodb.client.model.Filters
import com.mongodb.client.model.Sorts
import kotlinx.coroutines.flow.Flow
import org.cdb.homunculus.annotations.Index
import org.cdb.homunculus.components.DBClient
import org.cdb.homunculus.dao.MaterialDao
import org.cdb.homunculus.models.Box
import org.cdb.homunculus.models.Material
import org.cdb.homunculus.utils.StringNormalizer
import org.cdb.homunculus.utils.limit
import java.util.regex.Pattern

class MaterialDaoImpl(client: DBClient) : MaterialDao(client) {
	@Index(name = "by_fuzzy_name", property = "normalizedName", unique = false)
	override fun getByFuzzyName(
		query: String,
		includeDeleted: Boolean,
		limit: Int?,
	): Flow<Material> =
		collection.find(
			Filters.and(
				listOfNotNull(
					Filters.regex(Material::normalizedName.name, Pattern.compile("^${StringNormalizer.normalize(query)}.*")),
					Filters.exists(Box::deletionDate.name, false).takeIf { !includeDeleted },
				),
			),
		).limit(limit)

	override fun getLastCreated(limit: Int): Flow<Material> =
		collection.find()
			.sort(Sorts.descending(Material::creationDate.name))
			.limit(limit)

	@Index(name = "by_reference_code", property = "referenceCode", unique = false)
	override fun getByReferenceCode(
		query: String,
		includeDeleted: Boolean,
		limit: Int?,
	): Flow<Material> =
		collection.find(
			Filters.and(
				listOfNotNull(
					Filters.regex(Material::referenceCode.name, Pattern.compile("^${query.lowercase()}.*", Pattern.CASE_INSENSITIVE)),
					Filters.exists(Box::deletionDate.name, false).takeIf { !includeDeleted },
				),
			),
		).limit(limit)

	@Index(name = "by_brand", property = "brand", unique = false)
	override fun getByBrand(
		query: String,
		includeDeleted: Boolean,
		limit: Int?,
	): Flow<Material> =
		collection.find(
			Filters.and(
				listOfNotNull(
					Filters.regex(Material::brand.name, Pattern.compile("^${query.lowercase()}.*", Pattern.CASE_INSENSITIVE)),
					Filters.exists(Box::deletionDate.name, false).takeIf { !includeDeleted },
				),
			),
		).limit(limit)
}
