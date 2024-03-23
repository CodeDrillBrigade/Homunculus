package org.cdb.labwitch.dao

import com.mongodb.kotlin.client.coroutine.MongoCollection
import org.cdb.labwitch.components.DBClient
import org.cdb.labwitch.models.StorageRoom
import org.cdb.labwitch.models.identifiers.Identifier
import org.cdb.labwitch.models.identifiers.ShortId

abstract class StorageDao(client: DBClient) : GenericDao<StorageRoom>(client) {
    override val collection: MongoCollection<StorageRoom> = client.getCollection()

    override fun wrapIdentifier(id: String): ShortId = ShortId(id)

    /**
     * Retrieves a [StorageRoom] by id.
     *
     * @param id the [ShortId] of the [StorageRoom] to retrieve.
     * @return the [StorageRoom], if one exists with the specified id, and null otherwise.
     */
    abstract suspend fun get(id: Identifier): StorageRoom?
}
