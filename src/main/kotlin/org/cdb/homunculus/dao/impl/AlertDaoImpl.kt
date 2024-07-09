package org.cdb.homunculus.dao.impl

import com.mongodb.client.model.Filters
import kotlinx.coroutines.flow.Flow
import org.cdb.homunculus.annotations.Index
import org.cdb.homunculus.components.DBClient
import org.cdb.homunculus.dao.AlertDao
import org.cdb.homunculus.models.Alert
import org.cdb.homunculus.models.embed.AlertStatus
import org.cdb.homunculus.utils.StringNormalizer
import java.util.regex.Pattern

class AlertDaoImpl(client: DBClient) : AlertDao(client) {
	override fun get(status: AlertStatus): Flow<Alert> = collection.find(Filters.eq(Alert::status.name, status.name))

	@Index(name = "by_fuzzy_name", property = "normalizedName", unique = false)
	override fun getByFuzzyName(query: String): Flow<Alert> =
		collection.find(
			Filters.and(
				listOfNotNull(
					Filters.regex(Alert::normalizedName.name, Pattern.compile("^${StringNormalizer.normalize(query)}.*")),
				),
			),
		)
}
