package org.cdb.labwitch.dao.impl

import com.mongodb.client.model.Filters
import org.cdb.labwitch.components.DBClient
import org.cdb.labwitch.dao.RoleDao
import org.cdb.labwitch.models.embed.Role
import org.cdb.labwitch.models.identifiers.Identifier
import org.cdb.labwitch.models.security.Permissions

class RoleDaoImpl(client: DBClient) : RoleDao(client) {
    override suspend fun get(id: Identifier): Role? = get(Filters.eq("_id", id.id))
    
    override suspend fun retrieveAll(): List<Role> = getAll()
    
    override suspend fun save(id: Identifier)
    {
        // ?
    }
}
