package org.cdb.labwitch.dao

import com.mongodb.kotlin.client.coroutine.MongoCollection
import org.cdb.labwitch.components.DBClient
import org.cdb.labwitch.models.Box
import org.cdb.labwitch.models.identifiers.EntityId
import org.cdb.labwitch.models.identifiers.Identifier

abstract class BoxDao(client: DBClient) : GenericDao<Box>(client) {
    override val collection: MongoCollection<Box> = client.getCollection()

    override fun wrapIdentifier(id: String): EntityId = EntityId(id)

    abstract suspend fun get(id: Identifier): Box?

    abstract suspend fun getByMaterial(materialName: String): Box?
}
