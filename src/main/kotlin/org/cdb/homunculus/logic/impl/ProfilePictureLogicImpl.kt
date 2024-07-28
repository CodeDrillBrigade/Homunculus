package org.cdb.homunculus.logic.impl

import org.cdb.homunculus.dao.ProfilePictureDao
import org.cdb.homunculus.logic.ProfilePictureLogic
import org.cdb.homunculus.models.ProfilePicture
import org.cdb.homunculus.models.identifiers.EntityId
import org.cdb.homunculus.models.identifiers.Identifier
import org.cdb.homunculus.models.types.MimeType
import org.cdb.homunculus.utils.exist
import java.util.Base64

class ProfilePictureLogicImpl(
	private val profilePictureDao: ProfilePictureDao,
) : ProfilePictureLogic {
	private val base64Encoder = Base64.getEncoder()

	override suspend fun get(profilePictureId: EntityId): ProfilePicture =
		exist({ profilePictureDao.getById(profilePictureId) }) { "Picture $profilePictureId does not exist" }

	override suspend fun createProfilePicture(
		content: ByteArray,
		mimeType: MimeType,
	): Identifier =
		profilePictureDao.save(
			ProfilePicture(
				id = EntityId.generate(),
				content = base64Encoder.encodeToString(content),
				type = mimeType,
			),
		)

	override suspend fun modifyProfilePicture(
		pictureId: EntityId,
		content: ByteArray,
		mimeType: MimeType,
	) {
		val currentPicture = exist({ profilePictureDao.getById(pictureId) }) { "Profile picture $pictureId does not exist" }
		profilePictureDao.update(
			currentPicture.copy(
				content = base64Encoder.encodeToString(content),
				type = mimeType,
			),
		)
	}
}
