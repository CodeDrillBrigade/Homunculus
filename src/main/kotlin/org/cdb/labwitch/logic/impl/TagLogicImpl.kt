package org.cdb.labwitch.logic.impl

import kotlinx.coroutines.flow.Flow
import org.cdb.labwitch.dao.TagDao
import org.cdb.labwitch.exceptions.NotFoundException
import org.cdb.labwitch.logic.TagLogic
import org.cdb.labwitch.models.embed.Tag
import org.cdb.labwitch.models.identifiers.EntityId
import org.cdb.labwitch.models.identifiers.Identifier

class TagLogicImpl(
	private val tagDao: TagDao,
) : TagLogic {
	override suspend fun create(tag: Tag): Identifier = tagDao.save(tag)

	override suspend fun get(tagId: EntityId): Tag = tagDao.getById(tagId) ?: throw NotFoundException("Tag $tagId not found")

	override suspend fun getAll(): Flow<Tag> = tagDao.get()
}
