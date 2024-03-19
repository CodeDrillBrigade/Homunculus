package org.cdb.labwitch.components

import com.mongodb.kotlin.client.coroutine.MongoClient
import com.mongodb.kotlin.client.coroutine.MongoCollection
import org.cdb.labwitch.models.StoredEntity
import org.cdb.labwitch.models.config.MongoDBCredentials

/**
 * Instantiates a client for the database that will be used by all the other classes
 */
class DBClient(
    dbCredentials: MongoDBCredentials,
) {
    private val client = MongoClient.create(dbCredentials.toConnectionString())
    val db = client.getDatabase(dbCredentials.databaseName)

    /**
     * Get a collection of a type of [StoredEntity]. The collection name is the simple name of the concrete entity.
     *
     * @param E the type of entity that the collection contains.
     * @return a [MongoCollection] of [E].
     */
    inline fun <reified E : StoredEntity> getCollection(): MongoCollection<E> =
        db.getCollection<E>(
            E::class.simpleName ?: throw IllegalArgumentException("Cannot find collection for ${E::class}"),
        )
}
