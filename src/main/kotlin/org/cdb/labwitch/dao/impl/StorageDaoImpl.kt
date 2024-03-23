package org.cdb.labwitch.dao.impl

import com.mongodb.client.model.Filters
import org.cdb.labwitch.components.DBClient
import org.cdb.labwitch.dao.StorageDao
import org.cdb.labwitch.models.StorageRoom
import org.cdb.labwitch.models.identifiers.Identifier

class StorageDaoImpl(client: DBClient) : StorageDao(client) {
    override suspend fun get(id: Identifier): StorageRoom? = get(Filters.eq("_id", id.id))
}
