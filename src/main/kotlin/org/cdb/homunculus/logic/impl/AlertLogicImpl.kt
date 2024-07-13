package org.cdb.homunculus.logic.impl

import io.ktor.util.logging.Logger
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import org.cdb.homunculus.dao.AlertDao
import org.cdb.homunculus.dao.MaterialDao
import org.cdb.homunculus.logic.AlertLogic
import org.cdb.homunculus.models.Alert
import org.cdb.homunculus.models.dto.NotificationStub
import org.cdb.homunculus.models.embed.AlertStatus
import org.cdb.homunculus.models.filters.ByIdFilter
import org.cdb.homunculus.models.filters.OrFilter
import org.cdb.homunculus.models.identifiers.EntityId
import org.cdb.homunculus.models.identifiers.Identifier
import org.cdb.homunculus.utils.StringNormalizer
import org.cdb.homunculus.utils.exist

class AlertLogicImpl(
	private val alertDao: AlertDao,
	private val materialDao: MaterialDao,
	private val logger: Logger,
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

	override fun listByAcceptedMaterial(materialId: EntityId): Flow<NotificationStub> =
		flow {
			val material = exist({ materialDao.getById(materialId) }) { "Material $materialId does not exist" }
			alertDao.get().filter {
				it.status != AlertStatus.INACTIVE && it.buildFilter().canAccept(material)
			}.map {
				NotificationStub(
					id = it.id,
					name = it.name,
				)
			}.let { emitAll(it) }
		}

	override fun addMaterialToExclusions(
		materialId: EntityId,
		alertIds: Set<EntityId>,
	): Flow<Alert> =
		alertDao.getByIds(alertIds).mapNotNull { alert ->
			when (alert.excludeFilter) {
				null ->
					alert.copy(
						excludeFilter =
							OrFilter(
								filters = listOf(ByIdFilter(id = materialId.id)),
							),
					)
				is OrFilter ->
					alert.copy(
						excludeFilter = alert.excludeFilter.addFilter(ByIdFilter(id = materialId.id)),
					)
				else -> {
					logger.warn("Invalid Alert filter type: ${alert.excludeFilter::class.simpleName}")
					null
				}
			}
		}.mapNotNull {
			alertDao.update(it)
		}
}
