package org.cdb.homunculus.dao.impl

import com.mongodb.client.model.Filters
import com.mongodb.client.model.Sorts
import kotlinx.coroutines.flow.Flow
import org.cdb.homunculus.annotations.Index
import org.cdb.homunculus.components.DBClient
import org.cdb.homunculus.dao.MaterialDao
import org.cdb.homunculus.models.Box
import org.cdb.homunculus.models.Material
import org.cdb.homunculus.models.identifiers.EntityId
import org.cdb.homunculus.utils.StringNormalizer
import org.cdb.homunculus.utils.limit
import org.cdb.homunculus.utils.skip
import java.util.regex.Pattern

class MaterialDaoImpl(client: DBClient) : MaterialDao(client) {
	@Index(name = "by_fuzzy_name", property = "normalizedName", unique = false)
	override fun getByFuzzyName(
		query: String,
		includeDeleted: Boolean,
		limit: Int?,
		skip: Int?,
	): Flow<Material> =
		collection.find(
			Filters.and(
				listOfNotNull(
					Filters.regex(Material::normalizedName.name, Pattern.compile(".*${StringNormalizer.normalize(query)}.*")),
					Filters.exists(Material::deletionDate.name, false).takeIf { !includeDeleted },
				),
			),
		).skip(skip).limit(limit)

	override fun getLastCreated(limit: Int): Flow<Material> =
		collection.find(
			Filters.exists(Material::deletionDate.name, false),
		).sort(Sorts.descending(Material::creationDate.name)).limit(limit)

	@Index(name = "by_reference_code", property = "referenceCode", unique = false)
	override fun searchByReferenceCode(
		query: String,
		includeDeleted: Boolean,
		limit: Int?,
		skip: Int?,
	): Flow<Material> =
		collection.find(
			Filters.and(
				listOfNotNull(
					Filters.regex(Material::referenceCode.name, Pattern.compile("^${query.lowercase()}.*", Pattern.CASE_INSENSITIVE)),
					Filters.exists(Box::deletionDate.name, false).takeIf { !includeDeleted },
				),
			),
		).skip(skip).limit(limit)

	override fun getByReferenceCode(
		referenceCode: String,
		includeDeleted: Boolean,
	): Flow<Material> =
		collection.find(
			Filters.and(
				listOfNotNull(
					Filters.eq(Material::referenceCode.name, referenceCode),
					Filters.exists(Box::deletionDate.name, false).takeIf { !includeDeleted },
				),
			),
		)

	@Index(name = "by_brand", property = "brand", unique = false)
	override fun getByBrand(
		query: String,
		includeDeleted: Boolean,
		limit: Int?,
		skip: Int?,
	): Flow<Material> =
		collection.find(
			Filters.and(
				listOfNotNull(
					Filters.regex(Material::brand.name, Pattern.compile("^${query.lowercase()}.*", Pattern.CASE_INSENSITIVE)),
					Filters.exists(Box::deletionDate.name, false).takeIf { !includeDeleted },
				),
			),
		).skip(skip).limit(limit)

	override fun getByTagId(tagId: EntityId): Flow<Material> = collection.find(Filters.`in`(Material::tags.name, tagId.id))
}
