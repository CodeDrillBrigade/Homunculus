package org.cdb.labwitch.logic

import kotlinx.coroutines.flow.Flow
import org.cdb.labwitch.models.StorageRoom
import org.cdb.labwitch.models.embed.storage.Cabinet
import org.cdb.labwitch.models.embed.storage.Shelf
import org.cdb.labwitch.models.identifiers.EntityId
import org.cdb.labwitch.models.identifiers.Identifier
import org.cdb.labwitch.models.identifiers.ShortId

interface StorageLogic {
    /**
     * Updates an existing [StorageRoom] adding a [Cabinet] to it.
     *
     * @param storageRoomId the id of the [StorageRoom] to update.
     * @param cabinet the [Cabinet] to add.
     * @return the updated [StorageRoom].
     * @throws NotFoundException if the [StorageRoom] does not exist.
     */
    suspend fun addCabinet(
        storageRoomId: ShortId,
        cabinet: Cabinet,
    ): StorageRoom

    /**
     * Updates an existing [StorageRoom]'s [Cabinet] adding a [Shelf] to it.
     *
     * @param storageRoomId the id of the [StorageRoom] to update.
     * @param cabinetId the id of the [Cabinet] to update.
     * @param shelf the [Shelf] to add.
     * @return the updated [StorageRoom].
     * @throws NotFoundException if the [StorageRoom] or hte [Cabinet] do not exist.
     */
    suspend fun addShelf(
        storageRoomId: ShortId,
        cabinetId: ShortId,
        shelf: Shelf,
    ): StorageRoom

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
