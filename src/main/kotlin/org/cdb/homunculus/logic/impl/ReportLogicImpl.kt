package org.cdb.homunculus.logic.impl

import io.ktor.util.logging.Logger
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import org.cdb.homunculus.components.NotificationManager
import org.cdb.homunculus.dao.MaterialDao
import org.cdb.homunculus.dao.ReportDao
import org.cdb.homunculus.logic.ReportLogic
import org.cdb.homunculus.models.Report
import org.cdb.homunculus.models.dto.NotificationStub
import org.cdb.homunculus.models.embed.ReportStatus
import org.cdb.homunculus.models.filters.ByIdFilter
import org.cdb.homunculus.models.filters.OrFilter
import org.cdb.homunculus.models.identifiers.EntityId
import org.cdb.homunculus.models.identifiers.Identifier
import org.cdb.homunculus.utils.StringNormalizer
import org.cdb.homunculus.utils.exist

class ReportLogicImpl(
	private val reportDao: ReportDao,
	private val materialDao: MaterialDao,
	private val notificationManager: NotificationManager,
	private val logger: Logger,
) : ReportLogic {
	override suspend fun create(report: Report): Identifier {
		val reportId =
			checkNotNull(
				reportDao.save(
					report.copy(
						normalizedName = StringNormalizer.normalize(report.name),
					),
				),
			) { "Error during report creation" }
		reportDao.getById(reportId)?.let {
			notificationManager.addReport(it)
		} ?: throw IllegalStateException("Cannot load new report")
		return reportId
	}

	override suspend fun modify(report: Report) {
		exist({ reportDao.getById(report.id) }) { "Report ${report.id} not found" }
		val updatedReport =
			checkNotNull(
				reportDao.update(
					report.copy(
						normalizedName = StringNormalizer.normalize(report.name),
					),
				),
			) { "An error occurred while updating the report ${report.id}" }
		if (updatedReport.status == ReportStatus.ACTIVE) {
			notificationManager.updateReport(updatedReport)
		} else if (updatedReport.status == ReportStatus.INACTIVE) {
			notificationManager.removeReport(updatedReport)
		}
	}

	override suspend fun get(reportId: EntityId): Report = exist({ reportDao.getById(reportId) }) { "Report $reportId not found" }

	override fun getAll(): Flow<Report> = reportDao.get()

	override fun getByIds(ids: Set<EntityId>): Flow<Report> = reportDao.getByIds(ids)

	override suspend fun searchIds(query: String): Flow<EntityId> = reportDao.getByFuzzyName(query).map { it.id }

	override suspend fun setStatus(
		reportId: EntityId,
		status: ReportStatus,
	) {
		val currentReport = exist({ reportDao.getById(reportId) }) { "Report $reportId not found" }
		val updatedReport =
			checkNotNull(
				reportDao.update(
					currentReport.copy(
						status = status,
					),
				),
			) { "An error occurred while updating the report $reportId" }
		if (currentReport.status == ReportStatus.INACTIVE && updatedReport.status == ReportStatus.ACTIVE) {
			notificationManager.addReport(updatedReport)
		} else if (updatedReport.status == ReportStatus.INACTIVE) {
			notificationManager.removeReport(updatedReport)
		}
	}

	override suspend fun delete(reportId: EntityId) {
		val currentReport = exist({ reportDao.getById(reportId) }) { "Report $reportId not found" }
		reportDao.delete(reportId)
		notificationManager.removeReport(currentReport)
	}

	override fun listByAcceptedMaterial(materialId: EntityId): Flow<NotificationStub> =
		flow {
			val material = exist({ materialDao.getById(materialId) }) { "Material $materialId does not exist" }
			reportDao.get().filter {
				it.status != ReportStatus.INACTIVE && it.buildFilter().canAccept(material)
			}.map {
				NotificationStub(
					id = it.id,
					name = it.name,
				)
			}.let { emitAll(it) }
		}

	override fun addMaterialToExclusions(
		materialId: EntityId,
		reportIds: Set<EntityId>,
	): Flow<Report> =
		reportDao.getByIds(reportIds).mapNotNull { report ->
			when (report.excludeFilter) {
				null ->
					report.copy(
						excludeFilter =
							OrFilter(
								filters = listOf(ByIdFilter(id = materialId.id)),
							),
					)
				is OrFilter ->
					report.copy(
						excludeFilter = report.excludeFilter.addFilter(ByIdFilter(id = materialId.id)),
					)
				else -> {
					logger.warn("Invalid Report filter type: ${report.excludeFilter::class.simpleName}")
					null
				}
			}
		}.mapNotNull {
			reportDao.update(it)
		}
}
