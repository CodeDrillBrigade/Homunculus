package org.cdb.homunculus.logic.impl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.cdb.homunculus.dao.MaterialDao
import org.cdb.homunculus.dao.TagDao
import org.cdb.homunculus.logic.TagLogic
import org.cdb.homunculus.models.embed.Tag
import org.cdb.homunculus.models.identifiers.EntityId
import org.cdb.homunculus.models.identifiers.Identifier
import org.cdb.homunculus.utils.StringNormalizer
import org.cdb.homunculus.utils.exist

class TagLogicImpl(
	private val tagDao: TagDao,
	private val materialDao: MaterialDao,
) : TagLogic {
	override suspend fun create(tag: Tag): Identifier =
		tagDao.save(
			tag.copy(
				normalizedName = StringNormalizer.normalize(tag.name),
			),
		)

	override suspend fun get(tagId: EntityId): Tag = exist({ tagDao.getById(tagId) }) { "Tag $tagId does not exist" }

	override suspend fun getAll(): Flow<Tag> = tagDao.get()

	override suspend fun delete(tagId: EntityId) {
		exist({ tagDao.getById(tagId) }) { "Tag $tagId does not exist" }
		tagDao.delete(tagId)
		materialDao.getByTagId(tagId).collect { material ->
			materialDao.update(
				material.copy(
					tags = material.tags - tagId,
				),
			)
		}
	}

	override suspend fun modify(tag: Tag) {
		exist({ tagDao.getById(tag.id) }) { "Tag ${tag.id} does not exist" }
		tagDao.update(
			tag.copy(
				normalizedName = StringNormalizer.normalize(tag.name),
			),
		)
	}

	override suspend fun searchIds(query: String): Flow<EntityId> = tagDao.getByFuzzyName(query).map { it.id }

	override fun getByIds(ids: Set<EntityId>): Flow<Tag> = tagDao.getByIds(ids)
}
