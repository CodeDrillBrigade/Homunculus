package org.cdb.homunculus.logic.impl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.cdb.homunculus.dao.AlertDao
import org.cdb.homunculus.logic.AlertLogic
import org.cdb.homunculus.models.Alert
import org.cdb.homunculus.models.embed.AlertStatus
import org.cdb.homunculus.models.identifiers.EntityId
import org.cdb.homunculus.models.identifiers.Identifier
import org.cdb.homunculus.utils.StringNormalizer
import org.cdb.homunculus.utils.exist

class AlertLogicImpl(
	private val alertDao: AlertDao,
) : AlertLogic {
	override suspend fun create(alert: Alert): Identifier =
		checkNotNull(
			alertDao.save(
				alert.copy(
					normalizedName = StringNormalizer.normalize(alert.name),
				),
			),
		) { "Error during alert creation" }

	override suspend fun modify(alert: Alert) {
		exist({ alertDao.getById(alert.id) }) { "Alert ${alert.id} not found" }
		alertDao.update(
			alert.copy(
				normalizedName = StringNormalizer.normalize(alert.name),
			),
		)
	}

	override suspend fun get(alertId: EntityId): Alert = exist({ alertDao.getById(alertId) }) { "Alert $alertId not found" }

	override fun getAll(): Flow<Alert> = alertDao.get()

	override fun getByIds(ids: Set<EntityId>): Flow<Alert> = alertDao.getByIds(ids)

	override suspend fun searchIds(query: String): Flow<EntityId> = alertDao.getByFuzzyName(query).map { it.id }

	override suspend fun setStatus(
		alertId: EntityId,
		status: AlertStatus,
	) {
		val currentAlert = exist({ alertDao.getById(alertId) }) { "Alert $alertId not found" }
		alertDao.update(
			currentAlert.copy(
				status = status,
			),
		)
	}

	override suspend fun delete(alertId: EntityId) {
		exist({ alertDao.getById(alertId) }) { "Alert $alertId not found" }
		alertDao.delete(alertId)
	}
}
