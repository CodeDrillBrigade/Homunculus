package org.cdb.homunculus.components.impl

import com.github.benmanes.caffeine.cache.Cache
import com.github.benmanes.caffeine.cache.Caffeine
import com.github.benmanes.caffeine.cache.RemovalCause
import dev.inmo.krontab.doInfinity
import io.ktor.util.logging.Logger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.flow.toSet
import kotlinx.coroutines.launch
import org.cdb.homunculus.components.Mailer
import org.cdb.homunculus.components.NotificationManager
import org.cdb.homunculus.dao.AlertDao
import org.cdb.homunculus.dao.BoxDao
import org.cdb.homunculus.dao.MaterialDao
import org.cdb.homunculus.dao.ReportDao
import org.cdb.homunculus.dao.UserDao
import org.cdb.homunculus.models.Alert
import org.cdb.homunculus.models.Notification
import org.cdb.homunculus.models.Report
import org.cdb.homunculus.models.embed.AlertStatus
import org.cdb.homunculus.models.embed.ReportStatus
import org.cdb.homunculus.models.identifiers.EntityId
import org.cdb.homunculus.utils.exist
import java.time.Duration

class NotificationManagerImpl(
	private val alertDao: AlertDao,
	private val boxDao: BoxDao,
	private val materialDao: MaterialDao,
	private val reportDao: ReportDao,
	private val userDao: UserDao,
	private val mailer: Mailer,
	private val logger: Logger,
) : NotificationManager {
	private val executorScope = CoroutineScope(Dispatchers.Default)
	private val reportJobsCache = mutableMapOf<EntityId, Job>()
	private val alertCheckChannel = Channel<EntityId>(capacity = Channel.UNLIMITED)

	private val recentlyUpdatedMaterials: Cache<EntityId, Long> =
		Caffeine.newBuilder()
			.expireAfterWrite(Duration.ofSeconds(10))
			.evictionListener { materialId: EntityId?, _: Long?, _: RemovalCause ->
				materialId?.also {
					executorScope.launch {
						handleAlertsForMaterial(it)
					}
				}
			}.build()

	private val watchdog =
		executorScope.launch {
			doInfinity("0 * * * * 0o") {
				try {
					val jobsToRestart = reportJobsCache.filterValues { it.isCancelled || it.isCancelled }.keys
					jobsToRestart.forEach { reportId ->
						reportJobsCache.remove(reportId)
						reportDao.getById(reportId)?.also {
							val job = startReportJob(it)
							reportJobsCache[reportId] = job
						}
					}
				} catch (e: Exception) {
					logger.error("Error while executing report watchdog", e)
				}
				try {
					recentlyUpdatedMaterials.cleanUp()
				} catch (e: Exception) {
					logger.error("Error while cleaning up cache", e)
				}
			}
		}

	private fun startReportJob(report: Report): Job =
		executorScope.launch {
			doInfinity(report.cronConfig) {
				try {
					logger.info("Handling report ${report.id}")
					if (isNotificationTriggered(report)) {
						logger.info("Dispatching report ${report.id}")
						val matchingMaterials = materialDao.find(report.buildFilter().toBson()).toList().distinctBy { it.id }
						val recipientEmails = userDao.getByIds(report.recipients).mapNotNull { it.email }.toSet()
						mailer.sendReportEmail(matchingMaterials, report, recipientEmails)
					}
				} catch (e: Exception) {
					logger.error("Error while handling report ${report.id}", e)
				}
			}
		}

	override fun addReport(report: Report) {
		val job = startReportJob(report)
		reportJobsCache[report.id] = job
	}

	override suspend fun removeReport(reportId: EntityId) {
		reportJobsCache[reportId]?.cancelAndJoin()
		reportJobsCache.remove(reportId)
	}

	override suspend fun updateReport(report: Report) {
		removeReport(report.id)
		addReport(report)
	}

	override suspend fun loadReports() {
		reportDao.get(ReportStatus.ACTIVE).collect { report ->
			val job = startReportJob(report)
			reportJobsCache[report.id] = job
			logger.info("Starting job for report ${report.id}")
		}
	}

	override fun checkMaterial(materialId: EntityId) {
		recentlyUpdatedMaterials.put(materialId, System.currentTimeMillis())
	}

	private suspend fun handleAlertsForMaterial(materialId: EntityId) {
		val material = exist({ materialDao.getById(materialId) }) { "Material $materialId not found" }
		alertDao.get(AlertStatus.ACTIVE)
			.filter { it.buildFilter().canAccept(material) && isNotificationTriggered(it) }
			.collect { alert ->
				val matchingMaterials = materialDao.find(alert.buildFilter().toBson()).toList().distinctBy { it.id }
				val recipientEmails = userDao.getByIds(alert.recipients).mapNotNull { it.email }.toSet()
				mailer.sendAlertEmail(matchingMaterials, alert, recipientEmails)
				alertDao.update(alert.copy(status = AlertStatus.TRIGGERED))
			}
		alertDao.get(AlertStatus.TRIGGERED)
			.filter { it.buildFilter().canAccept(material) && shouldAlertBeRefreshed(it) }
			.collect { alert ->
				alertDao.update(alert.copy(status = AlertStatus.ACTIVE))
			}
	}

	private suspend fun isNotificationTriggered(notification: Notification): Boolean {
		val filter = notification.buildFilter().toBson()
		val matchingMaterials = materialDao.find(filter).map { it.id }.toList().toSet()
		val totalRemaining =
			boxDao.getByMaterials(matchingMaterials, includeDeleted = false).map {
				it.quantity.quantity
			}.toList().sum()
		return totalRemaining <= notification.threshold
	}

	private suspend fun shouldAlertBeRefreshed(alert: Alert): Boolean {
		val filter = alert.buildFilter().toBson()
		val matchingMaterials = materialDao.find(filter).map { it.id }.toList().toSet()
		val totalRemaining =
			boxDao.getByMaterials(matchingMaterials, includeDeleted = false).map {
				it.quantity.quantity
			}.toList().sum()
		return totalRemaining > alert.threshold
	}
}
