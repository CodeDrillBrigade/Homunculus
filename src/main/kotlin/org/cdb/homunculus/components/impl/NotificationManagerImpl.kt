package org.cdb.homunculus.components.impl

import com.github.benmanes.caffeine.cache.Cache
import com.github.benmanes.caffeine.cache.Caffeine
import com.github.benmanes.caffeine.cache.RemovalCause
import dev.inmo.krontab.doInfinity
import io.ktor.client.statement.bodyAsText
import io.ktor.util.collections.ConcurrentMap
import io.ktor.util.logging.Logger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
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
import org.cdb.homunculus.models.Material
import org.cdb.homunculus.models.Notification
import org.cdb.homunculus.models.Report
import org.cdb.homunculus.models.embed.AlertStatus
import org.cdb.homunculus.models.embed.ReportStatus
import org.cdb.homunculus.models.embed.UserStatus
import org.cdb.homunculus.models.filters.Filter
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
	private val reportJobsCache = ConcurrentMap<String, Job>()
	private val reportsByCronConfig = ConcurrentMap<String, Set<EntityId>>()

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

	@Suppress("unused")
	private val watchdog =
		executorScope.launch {
			doInfinity("0 * * * * 0o") {
				try {
					val jobsToRestart = reportJobsCache.filterValues { it.isCancelled || it.isCancelled }.keys
					jobsToRestart.forEach {
						logger.info("Restarting job for cron $it")
						reportJobsCache.remove(it)
						startReportJob(it)
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

	private fun startReportJob(cronConfig: String): Job =
		executorScope.launch {
			doInfinity(cronConfig) {
				try {
					logger.info("Start handling cron $cronConfig")
					reportsByCronConfig[cronConfig]?.fold(mutableMapOf<String, Map<Material, Int>>()) { acc, reportId ->
						logger.info("Handling report $reportId")
						val report = reportDao.getById(reportId)
						if (report != null && isNotificationTriggered(report)) {
							logger.info("Dispatching report ${report.id}")
							val matchingMaterials = distinctMaterialsByFilter(report.buildFilter())
							val recipientEmails =
								userDao.getByIds(report.recipients)
									.filter { it.status == UserStatus.ACTIVE }
									.mapNotNull { it.email }
									.toSet()
							recipientEmails.forEach { email ->
								acc[email] = acc.getOrDefault(email, emptyMap()) + matchingMaterials.associateWith { report.threshold }
							}
						} else {
							logger.info("$reportId is not triggered")
						}
						acc
					}?.entries?.forEach { (email, materials) ->
						val result = mailer.sendReportEmail(materials, email)
						if (result.status.value >= 400) {
							logger.error("Mail to $email failed with code ${result.status.value}: ${result.bodyAsText()}")
						}
					}
				} catch (e: Exception) {
					logger.error("Error while handling report at cron $cronConfig", e)
				}
			}
		}

	override fun addReport(report: Report) {
		report.cronConfigs.forEach { config ->
			if (reportsByCronConfig.containsKey(config)) {
				reportsByCronConfig[config] = reportsByCronConfig.getValue(config) + report.id
			} else {
				reportsByCronConfig[config] = setOf(report.id)
				reportJobsCache[config] = startReportJob(config)
			}
			logger.info("Starting jobs for report ${report.id} at cron: $config")
		}
	}

	override suspend fun removeReport(report: Report) {
		logger.info("Removing jobs for report ${report.id}")
		report.cronConfigs.forEach { config ->
			reportsByCronConfig[config] = reportsByCronConfig.getOrDefault(config, emptySet()) - report.id
			if (reportsByCronConfig[config]?.isEmpty() == true) {
				reportJobsCache[config]?.cancelAndJoin()
				reportJobsCache.remove(config)
			}
		}
	}

	override suspend fun updateReport(report: Report) {
		removeReport(report)
		addReport(report)
	}

	override suspend fun loadReports() {
		reportDao.get(ReportStatus.ACTIVE).collect { report ->
			addReport(report)
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
				val matchingMaterials = distinctMaterialsByFilter(alert.buildFilter())
				val recipientEmails =
					userDao.getByIds(alert.recipients)
						.filter { it.status == UserStatus.ACTIVE }
						.mapNotNull { it.email }
						.toSet()
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
		val matchingMaterials = distinctMaterialsIdsByFilter(notification.buildFilter())
		val totalRemaining =
			boxDao.getByMaterials(matchingMaterials, includeDeleted = false).map {
				it.quantity.quantity
			}.toList().sum()
		return totalRemaining <= notification.threshold
	}

	private suspend fun shouldAlertBeRefreshed(alert: Alert): Boolean {
		val matchingMaterials = distinctMaterialsIdsByFilter(alert.buildFilter())
		val totalRemaining =
			boxDao.getByMaterials(matchingMaterials, includeDeleted = false).map {
				it.quantity.quantity
			}.toList().sum()
		return totalRemaining > alert.threshold
	}

	private suspend fun distinctMaterialsByFilter(filter: Filter) =
		materialDao.find(filter.toBson())
			.filter { it.deletionDate == null }
			.toList()
			.distinctBy { it.id }

	private suspend fun distinctMaterialsIdsByFilter(filter: Filter) =
		materialDao.find(filter.toBson()).filter { it.deletionDate == null }.map { it.id }.toList().toSet()
}
