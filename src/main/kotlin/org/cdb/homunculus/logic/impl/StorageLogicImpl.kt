package org.cdb.homunculus.logic.impl

import kotlinx.coroutines.flow.Flow
import org.cdb.homunculus.dao.StorageDao
import org.cdb.homunculus.exceptions.NotFoundException
import org.cdb.homunculus.logic.StorageLogic
import org.cdb.homunculus.models.StorageRoom
import org.cdb.homunculus.models.embed.storage.Cabinet
import org.cdb.homunculus.models.embed.storage.Shelf
import org.cdb.homunculus.models.identifiers.ShortId
import org.cdb.homunculus.utils.exist

class StorageLogicImpl(
	private val storageDao: StorageDao,
) : StorageLogic {
	override suspend fun addCabinet(
		storageRoomId: ShortId,
		cabinet: Cabinet,
	): StorageRoom {
		val storageRoom = exist({ storageDao.getById(storageRoomId) }) { "Storage room with id $storageRoomId not found" }
		return storageDao.update(
			storageRoom.copy(
				cabinets =
					storageRoom.cabinets +
						cabinet.copy(
							shelves = cabinet.shelves.takeIf { it.isNotEmpty() } ?: listOf(Shelf(name = "Default shelf")),
						),
			),
		) ?: throw IllegalStateException("There was an error while updating the cabinet")
	}

	override suspend fun addShelf(
		storageRoomId: ShortId,
		cabinetId: ShortId,
		shelf: Shelf,
	): StorageRoom {
		val storageRoom = exist({ storageDao.getById(storageRoomId) }) { "Storage room with id $storageRoomId not found" }
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

	override suspend fun update(storage: StorageRoom) {
		exist({ storageDao.getById(storage.id) }) { "Storage room with id ${storage.id} not found" }
		storageDao.update(storage)
	}

	override suspend fun update(
		storageRoomId: ShortId,
		cabinet: Cabinet,
	) {
		val room = exist({ storageDao.getById(storageRoomId) }) { "Storage room with id $storageRoomId not found" }
		room.cabinets.firstOrNull { it.id == cabinet.id }
			?: throw NotFoundException("There is no cabinet with id ${cabinet.id} in room ${room.id}")
		storageDao.update(
			room.copy(
				cabinets = room.cabinets.filter { it.id != cabinet.id } + cabinet,
			),
		)
	}

	override suspend fun update(
		storageRoomId: ShortId,
		cabinetId: ShortId,
		shelf: Shelf,
	) {
		val room = exist({ storageDao.getById(storageRoomId) }) { "Storage room with id $storageRoomId not found" }
		val cabinet =
			room.cabinets.firstOrNull { it.id == cabinetId }
				?: throw NotFoundException("There is no cabinet with id $cabinetId in room ${room.id}")
		cabinet.shelves.firstOrNull { it.id == shelf.id }
			?: throw NotFoundException("There is no shelf with id ${shelf.id} in cabinet ${cabinet.id}")
		storageDao.update(
			room.copy(
				cabinets =
					room.cabinets.filter { it.id != cabinet.id } +
						cabinet.copy(
							shelves = cabinet.shelves.filter { it.id != shelf.id } + shelf,
						),
			),
		)
	}

	override suspend fun delete(entityId: ShortId) {
		exist({ storageDao.getById(entityId) }) { "Storage room with id $entityId not found" }
		storageDao.delete(entityId)
	}

	override suspend fun delete(
		storageRoomId: ShortId,
		cabinetId: ShortId,
	) {
		val room = exist({ storageDao.getById(storageRoomId) }) { "Storage room with id $storageRoomId not found" }
		room.cabinets.firstOrNull { it.id == cabinetId }
			?: throw NotFoundException("There is no cabinet with id $cabinetId in room ${room.id}")
		storageDao.update(
			room.copy(
				cabinets = room.cabinets.filter { it.id != cabinetId },
			),
		)
	}

	override suspend fun delete(
		storageRoomId: ShortId,
		cabinetId: ShortId,
		shelfId: ShortId,
	) {
		val room = exist({ storageDao.getById(storageRoomId) }) { "Storage room with id $storageRoomId not found" }
		val cabinet =
			room.cabinets.firstOrNull { it.id == cabinetId }
				?: throw NotFoundException("There is no cabinet with id $cabinetId in room ${room.id}")
		cabinet.shelves.firstOrNull { it.id == shelfId }
			?: throw NotFoundException("There is no shelf with id $shelfId in cabinet ${cabinet.id}")
		storageDao.update(
			room.copy(
				cabinets =
					room.cabinets.filter { it.id != cabinet.id } +
						cabinet.copy(
							shelves = cabinet.shelves.filter { it.id != shelfId },
						),
			),
		)
	}

	override fun getAll(): Flow<StorageRoom> = storageDao.get()
}
