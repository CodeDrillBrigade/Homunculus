package org.cdb.homunculus.dao.impl

import com.mongodb.client.model.Filters
import kotlinx.coroutines.flow.Flow
import org.cdb.homunculus.annotations.Index
import org.cdb.homunculus.components.DBClient
import org.cdb.homunculus.dao.ReportDao
import org.cdb.homunculus.models.Report
import org.cdb.homunculus.models.embed.ReportStatus
import org.cdb.homunculus.utils.StringNormalizer
import java.util.regex.Pattern

class ReportDaoImpl(client: DBClient) : ReportDao(client) {
	override fun get(status: ReportStatus): Flow<Report> = collection.find(Filters.eq(Report::status.name, status.name))

	@Index(name = "by_fuzzy_name", property = "normalizedName", unique = false)
	override fun getByFuzzyName(query: String): Flow<Report> =
		collection.find(
			Filters.and(
				listOfNotNull(
					Filters.regex(Report::normalizedName.name, Pattern.compile("^${StringNormalizer.normalize(query)}.*")),
				),
			),
		)
}
