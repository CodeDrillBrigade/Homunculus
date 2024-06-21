package org.cdb.homunculus.logic.impl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.fold
import kotlinx.coroutines.flow.take
import org.cdb.homunculus.dao.MaterialDao
import org.cdb.homunculus.exceptions.NotFoundException
import org.cdb.homunculus.logic.MaterialLogic
import org.cdb.homunculus.models.Material
import org.cdb.homunculus.models.identifiers.EntityId
import org.cdb.homunculus.models.identifiers.Identifier
import org.cdb.homunculus.utils.StringNormalizer
import java.util.Date

class MaterialLogicImpl(
	private val materialDao: MaterialDao,
) : MaterialLogic {
	override suspend fun create(material: Material): Identifier =
		materialDao.save(material.copy(creationDate = Date(), normalizedName = StringNormalizer.normalize(material.name)))

	override suspend fun get(materialId: EntityId): Material =
		materialDao.getById(materialId) ?: throw NotFoundException("Material $materialId not found")

	override fun getAll(): Flow<Material> = materialDao.get().filter { it.deletionDate == null }

	override fun findByFuzzyName(
		query: String,
		limit: Int?,
	): Flow<Material> =
		materialDao.getByFuzzyName(
			StringNormalizer.normalize(query),
			includeDeleted = false,
			limit = limit,
			skip = null,
		)

	override suspend fun delete(id: EntityId): EntityId {
		val material = materialDao.getById(id) ?: throw NotFoundException("Material $id not found")
		return materialDao.update(
			material.copy(
				deletionDate = Date(),
			),
		)?.id ?: throw IllegalStateException("Cannot delete the material with id $id")
	}

	override suspend fun modify(material: Material) {
		val currentMaterial = materialDao.getById(material.id) ?: throw NotFoundException("Material ${material.id} not found")
		materialDao.update(
			currentMaterial.copy(
				name = material.name,
				brand = material.brand,
				referenceCode = material.referenceCode ?: currentMaterial.referenceCode,
				description = material.description ?: currentMaterial.description,
				tags = material.tags,
				deletionDate = material.deletionDate ?: currentMaterial.deletionDate,
			),
		)
	}

	private fun search(
		query: String,
		tagIds: Set<EntityId>?,
		limit: Int?,
	): Flow<Material> =
		flow {
			emitAll(materialDao.getByFuzzyName(query, includeDeleted = false, limit = null, skip = null))
			emitAll(materialDao.getByReferenceCode(query, includeDeleted = false, limit = null, skip = null))
			emitAll(materialDao.getByBrand(query, includeDeleted = false, limit = null, skip = null))
		}.filter {
			tagIds == null || it.tags.intersect(tagIds).isNotEmpty()
		}.let {
			if (limit != null) {
				it.take(limit)
			} else {
				it
			}
		}

	override suspend fun searchIds(
		query: String,
		tagIds: Set<EntityId>?,
		limit: Int?,
	): Set<EntityId> = search(query, tagIds, limit).fold(mutableSetOf()) { acc, it -> acc.apply { add(it.id) } }

	override suspend fun searchNames(
		query: String,
		tagIds: Set<EntityId>?,
		limit: Int?,
	): Set<String> = search(query, tagIds, limit).fold(mutableSetOf()) { acc, it -> acc.apply { add(it.name.text) } }

	override fun getByIds(ids: Set<EntityId>): Flow<Material> = materialDao.getByIds(ids)

	override fun getLastCreated(limit: Int): Flow<Material> = materialDao.getLastCreated(limit)
}
