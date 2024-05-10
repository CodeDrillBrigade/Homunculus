package org.cdb.homunculus.logic.impl

import kotlinx.coroutines.flow.Flow
import org.cdb.homunculus.dao.MaterialDao
import org.cdb.homunculus.exceptions.NotFoundException
import org.cdb.homunculus.logic.MaterialLogic
import org.cdb.homunculus.models.Material
import org.cdb.homunculus.models.identifiers.EntityId
import org.cdb.homunculus.models.identifiers.Identifier
import org.cdb.homunculus.utils.StringNormalizer

class MaterialLogicImpl(
	private val materialDao: MaterialDao,
) : MaterialLogic {
	override suspend fun create(material: Material): Identifier =
		materialDao.save(material.copy(normalizedName = StringNormalizer.normalize(material.name)))

	override suspend fun get(materialId: EntityId): Material =
		materialDao.getById(materialId) ?: throw NotFoundException("Material $materialId not found")

	override fun getAll(): Flow<Material> = materialDao.get()

	override fun findByFuzzyName(
		query: String,
		limit: Int?,
	): Flow<Material> =
		materialDao.byFuzzyName(
			StringNormalizer.normalize(query),
			limit,
		)
}