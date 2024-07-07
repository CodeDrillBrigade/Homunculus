package org.cdb.homunculus.logic.impl

import kotlinx.coroutines.flow.Flow
import org.cdb.homunculus.components.impl.NotificationManagerImpl
import org.cdb.homunculus.dao.ReportDao
import org.cdb.homunculus.logic.ReportLogic
import org.cdb.homunculus.models.Report
import org.cdb.homunculus.models.embed.ReportStatus
import org.cdb.homunculus.models.identifiers.EntityId
import org.cdb.homunculus.models.identifiers.Identifier
import org.cdb.homunculus.utils.exist

class ReportLogicImpl(
	private val reportDao: ReportDao,
	private val notificationManager: NotificationManagerImpl,
) : ReportLogic {
	override suspend fun create(report: Report): Identifier {
		val reportId = checkNotNull(reportDao.save(report)) { "Error during report creation" }
		reportDao.getById(reportId)?.let {
			notificationManager.addReport(it)
		} ?: throw IllegalStateException("Cannot load new report")
		return reportId
	}

	override suspend fun modify(report: Report) {
		val oldReport = exist({ reportDao.getById(report.id) }) { "Report ${report.id} not found" }
		val updatedReport = checkNotNull(reportDao.update(report)) { "An error occurred while updating the report ${report.id}" }
		if (oldReport.status == ReportStatus.INACTIVE && updatedReport.status == ReportStatus.ACTIVE) {
			notificationManager.updateReport(updatedReport)
		} else if (updatedReport.status == ReportStatus.INACTIVE) {
			notificationManager.removeReport(updatedReport.id)
		}
	}

	override suspend fun get(reportId: EntityId): Report = exist({ reportDao.getById(reportId) }) { "Report $reportId not found" }

	override fun getAll(): Flow<Report> = reportDao.get()
}
