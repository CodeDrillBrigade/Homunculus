package org.cdb.homunculus.logic.impl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import org.cdb.homunculus.dao.BoxDao
import org.cdb.homunculus.exceptions.NotFoundException
import org.cdb.homunculus.logic.BoxLogic
import org.cdb.homunculus.models.Box
import org.cdb.homunculus.models.embed.Operation
import org.cdb.homunculus.models.embed.UsageLog
import org.cdb.homunculus.models.identifiers.EntityId
import org.cdb.homunculus.models.identifiers.HierarchicalId
import org.cdb.homunculus.models.identifiers.Identifier
import java.util.Date

class BoxLogicImpl(
	private val boxDao: BoxDao,
) : BoxLogic {
	override suspend fun create(
		box: Box,
		userId: EntityId,
	): Identifier {
		val boxWithLog =
			box.copy(
				usageLogs =
					sortedSetOf(
						UsageLog(
							date = Date(),
							user = userId,
							operation = Operation.ADD,
							quantity = box.quantity,
						),
					),
			)
		return checkNotNull(boxDao.save(boxWithLog)) {
			"Error during box creation"
		}
	}

	override suspend fun get(boxId: EntityId): Box = boxDao.getById(boxId) ?: throw NotFoundException("Box $boxId not found")

	override fun getAll(): Flow<Box> = boxDao.getAll()

	override fun getByMaterial(materialId: EntityId): Flow<Box> = boxDao.getByMaterial(materialId)

	override fun getByPosition(shelfId: HierarchicalId): Flow<Box> = boxDao.getByPosition(shelfId).filter { it.deleted == null }

	override suspend fun delete(id: EntityId): EntityId {
		val box = boxDao.getById(id) ?: throw NotFoundException("Box $id not found")
		return boxDao.update(
			box.copy(
				deleted = Date(),
			),
		)?.id ?: throw IllegalStateException("Cannot delete the box with id $id")
	}

	override suspend fun updateQuantity(
		boxId: EntityId,
		update: UsageLog,
	): EntityId {
		val box = boxDao.getById(boxId) ?: throw NotFoundException("Box $boxId not found")
		val newQuantity =
			if (update.operation == Operation.ADD) {
				box.quantity.quantity + update.quantity.quantity
			} else {
				box.quantity.quantity - update.quantity.quantity
			}
		require(newQuantity >= 0) { "Quantity cannot be less than 0" }
		return boxDao.update(
			box.copy(
				quantity =
					box.quantity.copy(
						quantity = newQuantity,
					),
				usageLogs =
					box.usageLogs.apply {
						add(update)
					},
			),
		)?.id ?: throw IllegalStateException("Cannot update the quantity for box $boxId")
	}
}
