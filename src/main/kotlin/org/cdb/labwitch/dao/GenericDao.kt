package org.cdb.labwitch.dao

import com.mongodb.client.model.Filters
import com.mongodb.kotlin.client.coroutine.MongoCollection
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import org.bson.conversions.Bson
import org.cdb.labwitch.components.DBClient
import org.cdb.labwitch.models.StoredEntity
import org.cdb.labwitch.models.identifiers.Identifier

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
	 * Retrieves all the [T] in the db.
	 *
	 * @return a [Flow] of [T]
	 */
	fun get(): Flow<T> = collection.find()

	/**
	 * Wraps a String in the correct type of identifier from the specific concrete DAO.
	 *
	 * @param id an id from MongoDB.
	 * @return an [Identifier].
	 */
	protected abstract fun wrapIdentifier(id: String): Identifier

	/**
	 * Creates a new entity [T] in the database.
	 *
	 * @param entity a [T] to create.
	 * @return the [Identifier], if successfully created.
	 */
	suspend fun save(entity: T): Identifier =
		collection.insertOne(entity).insertedId?.asString()?.value?.let { wrapIdentifier(it) }
			?: throw IllegalStateException("There was an error while creating the entity.")

	/**
	 * Replace an existing entity [T] in the database with the version passed as parameter.
	 *
	 * @param entity the new version of the entity [T].
	 * @return the updated entity, if the operation was successful, and false otherwise.
	 */
	suspend fun update(entity: T): T? = collection.findOneAndReplace(Filters.eq("_id", entity.id), entity)
}
