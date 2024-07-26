package org.cdb.homunculus.logic

import org.cdb.homunculus.exceptions.NotFoundException
import org.cdb.homunculus.models.ProfilePicture
import org.cdb.homunculus.models.identifiers.EntityId
import org.cdb.homunculus.models.identifiers.Identifier
import org.cdb.homunculus.models.types.MimeType

interface ProfilePictureLogic {
	/**
	 * Retrieves am [ProfilePicture].
	 *
	 * @param profilePictureId the [EntityId] of the attachment to find
	 * @return the [ProfilePicture] with the specified id.
	 * @throws NotFoundException if no [ProfilePicture] exists with the specified id.
	 */
	suspend fun get(profilePictureId: EntityId): ProfilePicture

	/**
	 * Creates a new [ProfilePicture].
	 *
	 * @param content the image file content as a [ByteArray].
	 * @param mimeType the [MimeType] of the image.
	 * @return the [EntityId] of the newly created entity.
	 */
	suspend fun createProfilePicture(
		content: ByteArray,
		mimeType: MimeType,
	): Identifier

	/**
	 * Updates an existing [ProfilePicture].
	 *
	 * @param pictureId the [EntityId] of the [ProfilePicture] to update.
	 * @param content the image file content as a [ByteArray].
	 * @param mimeType the [MimeType] of the image.
	 * @throws NotFoundException if there is no [ProfilePicture] with such an id in the system.
	 */
	suspend fun modifyProfilePicture(
		pictureId: EntityId,
		content: ByteArray,
		mimeType: MimeType,
	)
}
