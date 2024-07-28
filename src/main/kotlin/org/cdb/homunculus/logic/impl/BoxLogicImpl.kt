package org.cdb.homunculus.logic.impl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapNotNull
import org.cdb.homunculus.components.NotificationManager
import org.cdb.homunculus.dao.BoxDao
import org.cdb.homunculus.exceptions.NotFoundException
import org.cdb.homunculus.logic.BoxLogic
import org.cdb.homunculus.models.Box
import org.cdb.homunculus.models.embed.Operation
import org.cdb.homunculus.models.embed.UsageLog
import org.cdb.homunculus.models.identifiers.EntityId
import org.cdb.homunculus.models.identifiers.HierarchicalId
import org.cdb.homunculus.models.identifiers.Identifier
import org.cdb.homunculus.utils.exist
import java.util.Date

class BoxLogicImpl(
	private val boxDao: BoxDao,
	private val notificationManager: NotificationManager,
) : BoxLogic {
	override suspend fun create(
		box: Box,
		userId: EntityId,
	): Identifier {
		val boxWithLog =
			box.copy(
				creationDate = Date(),
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
		}.also {
			notificationManager.checkMaterial(box.material)
		}
	}

	override suspend fun get(boxId: EntityId): Box = exist({ boxDao.getById(boxId) }) { "Box $boxId not found" }

	override fun getAll(): Flow<Box> = boxDao.get()

	override fun getByMaterial(materialId: EntityId): Flow<Box> = boxDao.getByMaterial(materialId, false)

	override fun deleteByMaterial(materialId: EntityId): Flow<Box> =
		getByMaterial(materialId).mapNotNull {
			boxDao.update(
				it.copy(
					deletionDate = Date(),
				),
			)
		}

	override fun getByPosition(shelfId: HierarchicalId): Flow<Box> = boxDao.getByPosition(shelfId, false)

	override fun findByBatchNumber(query: String): Flow<Box> = boxDao.getByBatchNumber(query, false)

	override suspend fun delete(id: EntityId): EntityId {
		val box = boxDao.getById(id) ?: throw NotFoundException("Box $id not found")
		return boxDao.update(
			box.copy(
				deletionDate = Date(),
			),
		)?.id?.also {
			notificationManager.checkMaterial(box.material)
		} ?: throw IllegalStateException("Cannot delete the box with id $id")
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
		)?.id?.also {
			notificationManager.checkMaterial(box.material)
		} ?: throw IllegalStateException("Cannot update the quantity for box $boxId")
	}

	override suspend fun modify(box: Box): EntityId {
		val currentBox = boxDao.getById(box.id) ?: throw NotFoundException("Box ${box.id} not found")
		return checkNotNull(
			boxDao.update(
				currentBox.copy(
					position = box.position,
					batchNumber = box.batchNumber,
					expirationDate = box.expirationDate,
					deletionDate = box.deletionDate,
					description = box.description,
				),
			)?.id,
		) { "An error occurred while updating box ${box.id}" }
	}
}
