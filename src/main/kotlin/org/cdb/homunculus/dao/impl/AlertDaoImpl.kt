package org.cdb.homunculus.dao.impl

import com.mongodb.client.model.Filters
import kotlinx.coroutines.flow.Flow
import org.cdb.homunculus.components.DBClient
import org.cdb.homunculus.dao.AlertDao
import org.cdb.homunculus.models.Alert
import org.cdb.homunculus.models.embed.AlertStatus

class AlertDaoImpl(client: DBClient) : AlertDao(client) {
	override fun get(status: AlertStatus): Flow<Alert> = collection.find(Filters.eq(Alert::status.name, status.name))
}
