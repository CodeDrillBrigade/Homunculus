package org.cdb.labwitch.logic.impl

import kotlinx.coroutines.flow.Flow
import org.cdb.labwitch.dao.StorageDao
import org.cdb.labwitch.exceptions.NotFoundException
import org.cdb.labwitch.logic.StorageLogic
import org.cdb.labwitch.models.StorageRoom
import org.cdb.labwitch.models.embed.storage.Cabinet
import org.cdb.labwitch.models.embed.storage.Shelf
import org.cdb.labwitch.models.identifiers.ShortId

class StorageLogicImpl(
    private val storageDao: StorageDao,
) : StorageLogic {
    override suspend fun addCabinet(
        storageRoomId: ShortId,
        cabinet: Cabinet,
    ): StorageRoom {
        val storageRoom = storageDao.get(storageRoomId) ?: throw NotFoundException("Storage room with id $storageRoomId not found")
        return storageDao.update(
            storageRoom.copy(
                cabinets = storageRoom.cabinets + cabinet,
            ),
        ) ?: throw IllegalStateException("There was an error while updating the cabinet")
    }

    override suspend fun addShelf(
        storageRoomId: ShortId,
        cabinetId: ShortId,
        shelf: Shelf,
    ): StorageRoom {
        val storageRoom = storageDao.get(storageRoomId) ?: throw NotFoundException("Storage room with id $storageRoomId not found")
        val cabinetIndex =
            storageRoom.cabinets.indexOfFirst { it.id == cabinetId }.takeIf { it >= 0 }
                ?: throw NotFoundException("Cabinet $cabinetId not found in $storageRoomId")
        val newCabinets =
            storageRoom.cabinets.subList(0, cabinetIndex) +
                storageRoom.cabinets[cabinetIndex].let {
                    it.copy(shelves = it.shelves + shelf)
                } + storageRoom.cabinets.subList((cabinetIndex + 1).coerceAtMost(storageRoom.cabinets.size), storageRoom.cabinets.size)
        return storageDao.update(
            storageRoom.copy(
                cabinets = newCabinets,
            ),
        ) ?: throw IllegalStateException("There was an error while updating the cabinet")
    }

    override suspend fun create(storage: StorageRoom) = storageDao.save(storage)

    override fun getAll(): Flow<StorageRoom> = storageDao.get()
}
