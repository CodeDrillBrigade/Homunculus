package org.cdb.homunculus.dao

import com.mongodb.client.model.Filters
import com.mongodb.client.model.IndexOptions
import com.mongodb.client.model.Indexes
import com.mongodb.kotlin.client.coroutine.MongoCollection
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import org.bson.conversions.Bson
import org.cdb.homunculus.components.DBClient
import org.cdb.homunculus.models.StoredEntity
import org.cdb.homunculus.models.identifiers.Identifier
import kotlin.reflect.KProperty

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
	 * Retrieves a single [T].
	 *
	 * @param filter a [Bson] that identifies the entity.
	 * @return the entity, if one exists with the specified id, and null otherwise.
	 */
	suspend fun get(filter: Bson): T? = collection.find(filter).firstOrNull()

	/**
	 * Retrieves a [T] by id.
	 *
	 * @param id the id of the entity.
	 * @return the entity, if one exists with the specified id, and null otherwise.
	 */
	suspend fun getById(id: Identifier): T? = collection.find(Filters.eq("_id", id.id)).firstOrNull()

	/**
	 * Retrieves multiple [T]s by id.
	 *
	 * @param ids a [Set] containing the [Identifier]s of the elements to retrieve. Non-existing ids are ignored..
	 * @return a [Flow] of [T] which id is contained in [ids].
	 */
	fun getByIds(ids: Set<Identifier>): Flow<T> = collection.find(Filters.`in`("_id", ids.map { it.id }))

	/**
	 * Retrieves all the entities matching the specified filter.
	 *
	 * @param filter a [Bson] filter.
	 * @return a [Flow] of [T].
	 */
	fun find(filter: Bson) = collection.find(filter)

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

	/**
	 * Creates an ascending index in the current collection for the specified [property], if such an index does not exist already.
	 *
	 * @param property the name of the [KProperty] that will be used in the creation of the index.
	 * @param name the index name.
	 * @param unique whether the index should enforce uniqueness.
	 * @return the name of the newly created index or null if the index already exists.
	 */
	suspend fun initIndex(
		property: String,
		name: String,
		unique: Boolean,
	): String? =
		if (collection.listIndexes().firstOrNull { it["name"] == name } == null) {
			collection.createIndex(
				Indexes.ascending(property),
				IndexOptions().name(name).unique(unique),
			)
		} else {
			null
		}
}
