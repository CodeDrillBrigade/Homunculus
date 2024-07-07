package org.cdb.homunculus.dao

import com.mongodb.kotlin.client.coroutine.MongoCollection
import kotlinx.coroutines.flow.Flow
import org.cdb.homunculus.components.DBClient
import org.cdb.homunculus.models.Alert
import org.cdb.homunculus.models.embed.AlertStatus
import org.cdb.homunculus.models.identifiers.EntityId

abstract class AlertDao(client: DBClient) : GenericDao<Alert>(client) {
	override val collection: MongoCollection<Alert> = client.getCollection()

	override fun wrapIdentifier(id: String): EntityId = EntityId(id)

	/**
	 * @return a [Flow] containing all the [Alert]s in the database where [Alert.status] matches [status].
	 */
	abstract fun get(status: AlertStatus): Flow<Alert>
}
