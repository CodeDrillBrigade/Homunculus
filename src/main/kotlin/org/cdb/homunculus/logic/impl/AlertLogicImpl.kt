package org.cdb.homunculus.logic.impl

import kotlinx.coroutines.flow.Flow
import org.cdb.homunculus.dao.AlertDao
import org.cdb.homunculus.logic.AlertLogic
import org.cdb.homunculus.models.Alert
import org.cdb.homunculus.models.identifiers.EntityId
import org.cdb.homunculus.models.identifiers.Identifier
import org.cdb.homunculus.utils.exist

class AlertLogicImpl(
	private val alertDao: AlertDao,
) : AlertLogic {
	override suspend fun create(alert: Alert): Identifier = checkNotNull(alertDao.save(alert)) { "Error during alert creation" }

	override suspend fun modify(alert: Alert) {
		exist({ alertDao.getById(alert.id) }) { "Alert ${alert.id} not found" }
		alertDao.update(alert)
	}

	override suspend fun get(alertId: EntityId): Alert = exist({ alertDao.getById(alertId) }) { "Alert $alertId not found" }

	override fun getAll(): Flow<Alert> = alertDao.get()
}
