package org.cdb.labwitch.dao

import com.mongodb.kotlin.client.coroutine.MongoCollection
import org.cdb.labwitch.components.DBClient
import org.cdb.labwitch.models.Material
import org.cdb.labwitch.models.identifiers.EntityId
import org.cdb.labwitch.models.identifiers.Identifier

abstract class MaterialDao(client: DBClient) : GenericDao<Material>(client) {
    override val collection: MongoCollection<Material> = client.getCollection()

    override fun wrapIdentifier(id: String): EntityId = EntityId(id)

    abstract suspend fun get(id: Identifier): Material?

    abstract suspend fun getByName(name: String): Material?
}
