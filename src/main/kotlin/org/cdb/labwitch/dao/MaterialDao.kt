package org.cdb.labwitch.dao

import com.mongodb.kotlin.client.coroutine.MongoCollection
import org.cdb.labwitch.components.DBClient
import org.cdb.labwitch.models.Material
import org.cdb.labwitch.models.types.EntityId

abstract class MaterialDao(client: DBClient) : GenericDao<Material>(client) {
    override val collection: MongoCollection<Material> = client.getCollection()

    abstract suspend fun get(id: EntityId): Material?

    abstract suspend fun getByName(name: String): Material?
}
