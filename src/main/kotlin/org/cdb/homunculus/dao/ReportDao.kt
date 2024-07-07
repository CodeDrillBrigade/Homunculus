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
}
