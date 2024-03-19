package org.cdb.labwitch.dao

import com.mongodb.kotlin.client.coroutine.MongoCollection
import org.cdb.labwitch.components.DBClient
import org.cdb.labwitch.models.Box

abstract class BoxDao(client: DBClient) : GenericDao<Box>(client) {
    override val collection: MongoCollection<Box> = client.getCollection()

    abstract suspend fun get(id: String): Box?

    abstract suspend fun getByMaterial(materialName: String): Box?
} // fine classe
