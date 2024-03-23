package org.cdb.labwitch.logic

import kotlinx.coroutines.flow.Flow
import org.cdb.labwitch.models.StorageRoom
import org.cdb.labwitch.models.identifiers.EntityId
import org.cdb.labwitch.models.identifiers.Identifier

interface StorageLogic {
    /**
     * Creates a new [StorageRoom].
     *
     * @param storage the [StorageRoom] to create.
     * @return the [EntityId] of the newly created entity.
     */
    suspend fun create(storage: StorageRoom): Identifier

    /**
     * Retrieves all the [StorageRoom]s in the database.
     *
     * @return a [Flow] of [StorageRoom]s.
     */
    fun getAll(): Flow<StorageRoom>
}
