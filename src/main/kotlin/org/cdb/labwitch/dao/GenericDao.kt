package org.cdb.labwitch.dao

import com.mongodb.kotlin.client.coroutine.MongoCollection
import kotlinx.coroutines.flow.firstOrNull
import org.bson.conversions.Bson
import org.cdb.labwitch.components.DBClient
import org.cdb.labwitch.models.StoredEntity

/**
 * Defines all the generic operations that a DAO should implement.
 */
abstract class GenericDao<T : StoredEntity>(
    protected val client: DBClient,
) {
    /**
     * The [MongoCollection] for this entity type.
     * Must be instantiated by the concrete class because the type is reified.
     */
    protected abstract val collection: MongoCollection<T>

    /**
     * Retrieves a [T] by id.
     *
     * @param idFilter a [Bson] that identifies the entity.
     * @return the entity, if one exists with the specified id, and null otherwise.
     */
    suspend fun get(idFilter: Bson): T? = collection.find(idFilter).firstOrNull()

    /**
     * Creates a new entity [T] in the database.
     *
     * @param entity a [T] to create.
     * @return the entity id, if successfully created
     */
    suspend fun save(entity: T): String? = collection.insertOne(entity).insertedId?.asString()?.value
}
