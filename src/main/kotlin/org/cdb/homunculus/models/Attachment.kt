package org.cdb.homunculus.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.cdb.homunculus.models.identifiers.EntityId
import org.cdb.homunculus.models.types.Base64String
import org.cdb.homunculus.models.types.MimeType

@Serializable
sealed interface Attachment : StoredEntity

@Serializable
data class ProfilePicture(
	@SerialName("_id") override val id: EntityId = EntityId.generate(),
	val content: Base64String,
	val type: MimeType,
) : Attachment
