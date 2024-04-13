package org.cdb.labwitch.dao.impl

import com.mongodb.client.model.Filters
import org.cdb.labwitch.components.DBClient
import org.cdb.labwitch.dao.TagDao
import org.cdb.labwitch.models.embed.Tag
import org.cdb.labwitch.models.identifiers.Identifier

class TagDaoImpl(client: DBClient) : TagDao(client) {
	override suspend fun get(id: Identifier): Tag? = get(Filters.eq("_id", id.id))
}
