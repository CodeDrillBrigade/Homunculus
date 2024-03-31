package org.cdb.labwitch.dao.impl

import com.mongodb.client.model.Filters
import org.cdb.labwitch.components.DBClient
import org.cdb.labwitch.dao.RoleDao
import org.cdb.labwitch.models.embed.Role
import org.cdb.labwitch.models.identifiers.Identifier

class RoleDaoImpl(client: DBClient) : RoleDao(client) {
	override suspend fun get(id: Identifier): Role? = get(Filters.eq("_id", id.id))
}
