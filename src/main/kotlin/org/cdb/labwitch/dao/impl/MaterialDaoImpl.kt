package org.cdb.labwitch.dao.impl

import com.mongodb.client.model.Filters.eq
import org.cdb.labwitch.components.DBClient
import org.cdb.labwitch.dao.MaterialDao
import org.cdb.labwitch.models.Material

class MaterialDaoImpl(client: DBClient) : MaterialDao(client) {
    override suspend fun get(id: String): Material? {
        return get(eq("_id", id))
    }

    override suspend fun getByName(name: String): Material? {
        return get(eq(Material::name.name, name))
    }
} // fine classe
