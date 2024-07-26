package org.cdb.homunculus.dao

import com.mongodb.kotlin.client.coroutine.MongoCollection
import org.cdb.homunculus.components.DBClient
import org.cdb.homunculus.models.ProfilePicture
import org.cdb.homunculus.models.identifiers.EntityId

abstract class ProfilePictureDao(client: DBClient) : GenericDao<ProfilePicture>(client) {
	override val collection: MongoCollection<ProfilePicture> = client.getCollection()

	override fun wrapIdentifier(id: String): EntityId = EntityId(id)
}
