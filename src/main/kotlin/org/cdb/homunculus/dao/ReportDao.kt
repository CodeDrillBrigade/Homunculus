package org.cdb.homunculus.dao

import com.mongodb.kotlin.client.coroutine.MongoCollection
import kotlinx.coroutines.flow.Flow
import org.cdb.homunculus.components.DBClient
import org.cdb.homunculus.models.Report
import org.cdb.homunculus.models.embed.ReportStatus
import org.cdb.homunculus.models.identifiers.EntityId

abstract class ReportDao(client: DBClient) : GenericDao<Report>(client) {
	override val collection: MongoCollection<Report> = client.getCollection()

	override fun wrapIdentifier(id: String): EntityId = EntityId(id)

	/**
	 * @return a [Flow] containing all the [Report]s in the database where [Report.status] matches [status].
	 */
	abstract fun get(status: ReportStatus): Flow<Report>

	/**
	 * Retrieves all the [Report]s where [Report.normalizedName] is not null and starts with [query].
	 *
	 * @param query the prefix for [Report.normalizedName] to search for.
	 * @return a [Flow] of [Report] that match the condition.
	 */
	abstract fun getByFuzzyName(query: String): Flow<Report>
}
