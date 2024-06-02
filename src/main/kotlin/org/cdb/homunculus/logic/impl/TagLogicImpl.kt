package org.cdb.homunculus.logic.impl

import kotlinx.coroutines.flow.Flow
import org.cdb.homunculus.dao.TagDao
import org.cdb.homunculus.exceptions.NotFoundException
import org.cdb.homunculus.logic.TagLogic
import org.cdb.homunculus.models.embed.Tag
import org.cdb.homunculus.models.identifiers.EntityId
import org.cdb.homunculus.models.identifiers.Identifier

class TagLogicImpl(
	private val tagDao: TagDao,
) : TagLogic {
	override suspend fun create(tag: Tag): Identifier = tagDao.save(tag)

	override suspend fun get(tagId: EntityId): Tag = tagDao.getById(tagId) ?: throw NotFoundException("Tag $tagId not found")

	override suspend fun getAll(): Flow<Tag> = tagDao.get()

	override fun getByIds(ids: Set<EntityId>): Flow<Tag> = tagDao.getByIds(ids)
}
