package org.cdb.homunculus.logic

import kotlinx.coroutines.flow.Flow
import org.cdb.homunculus.exceptions.NotFoundException
import org.cdb.homunculus.models.StorageRoom
import org.cdb.homunculus.models.embed.storage.Cabinet
import org.cdb.homunculus.models.embed.storage.Shelf
import org.cdb.homunculus.models.identifiers.EntityId
import org.cdb.homunculus.models.identifiers.Identifier
import org.cdb.homunculus.models.identifiers.ShortId

interface StorageLogic {
	/**
	 * Updates an existing [StorageRoom] adding a [Cabinet] to it. If [cabinet] contains no shelves, then a default shelf is added
	 * automatically.
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
	 * @throws NotFoundException if the [StorageRoom] or the [Cabinet] do not exist.
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
	 * Replaces a [StorageRoom] with the one passed as parameter.
	 *
	 * @param storage the [StorageRoom] to update.
	 * @throws NotFoundException if there is no existing room with the id of [storage].
	 */
	suspend fun update(storage: StorageRoom)

	/**
	 * Replaces a [Cabinet] in the room which [storageRoomId] is passed as parameter.
	 *
	 * @param storageRoomId the id of the [StorageRoom] where the cabinet is.
	 * @param cabinet the [Cabinet] to replace.
	 * @throws NotFoundException if there is no existing room with [storageRoomId] as id or no cabinet with the specified id in
	 * the room.
	 */
	suspend fun update(
		storageRoomId: ShortId,
		cabinet: Cabinet,
	)

	/**
	 * Replaces a [Shelf] in the cabinet of the room which ids are passed as parameters.
	 *
	 * @param storageRoomId the id of the [StorageRoom] where the cabinet is.
	 * @param cabinetId the is of the [Cabinet] where the shelf is.
	 * @param shelf the [Shelf] to replace.
	 * @throws NotFoundException if there is no existing room with [storageRoomId] as id or no cabinet with the specified id in
	 * the room or no such shelf in the cabinet.
	 */
	suspend fun update(
		storageRoomId: ShortId,
		cabinetId: ShortId,
		shelf: Shelf,
	)

	/**
	 * Retrieves all the [StorageRoom]s in the database.
	 *
	 * @return a [Flow] of [StorageRoom]s.
	 */
	fun getAll(): Flow<StorageRoom>

	/**
	 * Deletes a [StorageRoom] by its [entityId].
	 *
	 * @throws NotFoundException if there is no existing room with [entityId] as id.
	 */
	suspend fun delete(entityId: ShortId)

	/**
	 * Deletes a [Cabinet] in a room.
	 *
	 * @param storageRoomId the id of the [StorageRoom] to delete.
	 * @param cabinetId the id of the [Cabinet] to delete.
	 * @throws NotFoundException if there is no existing room with [storageRoomId] as id or no cabinet with the specified [cabinetId] in
	 * the room.
	 */
	suspend fun delete(
		storageRoomId: ShortId,
		cabinetId: ShortId,
	)

	/**
	 * Deletes a [Shelf] in a cabinet.
	 *
	 * @param storageRoomId the id of the [StorageRoom] to delete.
	 * @param cabinetId the id of the [Cabinet] to delete.
	 * @param shelfId the id of the [Shelf] to delete.
	 * @throws NotFoundException if there is no existing room with [storageRoomId] as id or no cabinet with the specified [cabinetId] in
	 * the room or no shelf with the specified [shelfId] in the room.
	 */
	suspend fun delete(
		storageRoomId: ShortId,
		cabinetId: ShortId,
		shelfId: ShortId,
	)
}
