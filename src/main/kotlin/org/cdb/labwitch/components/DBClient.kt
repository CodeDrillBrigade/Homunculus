package org.cdb.labwitch.components

import com.mongodb.kotlin.client.coroutine.MongoClient
import com.mongodb.kotlin.client.coroutine.MongoCollection
import org.cdb.labwitch.models.StoredEntity

/**
 * Instantiates a client for the database that will be used by all the other classes
 */
class DBClient {

    private val username = System.getenv("DB_USERNAME")
    private val password = System.getenv("DB_PASSWORD")
    private val ip = System.getenv("DB_IP")
    private val port = System.getenv("DB_PORT")
    private val databaseName = System.getenv("DB_NAME")
    private val client = MongoClient.create("mongodb://${username}:${password}@${ip}:${port}/${databaseName}")
    val db = client.getDatabase(databaseName)

    /**
     * Get a collection of a type of [StoredEntity]. The collection name is the simple name of the concrete entity.
     *
     * @param E the type of entity that the collection contains.
     * @return a [MongoCollection] of [E].
     */
    inline fun <reified E: StoredEntity> getCollection(): MongoCollection<E> = db.getCollection<E>(
        E::class.simpleName ?: throw IllegalArgumentException("Cannot find collection for ${E::class}")
    )

}