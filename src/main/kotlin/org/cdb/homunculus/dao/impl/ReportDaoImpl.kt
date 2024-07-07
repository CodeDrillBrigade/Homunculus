package org.cdb.homunculus.dao.impl

import com.mongodb.client.model.Filters
import kotlinx.coroutines.flow.Flow
import org.cdb.homunculus.components.DBClient
import org.cdb.homunculus.dao.ReportDao
import org.cdb.homunculus.models.Report
import org.cdb.homunculus.models.embed.ReportStatus

class ReportDaoImpl(client: DBClient) : ReportDao(client) {
	override fun get(status: ReportStatus): Flow<Report> = collection.find(Filters.eq(Report::status.name, status.name))
}
