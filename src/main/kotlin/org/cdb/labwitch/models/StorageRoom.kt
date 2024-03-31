package org.cdb.labwitch.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.cdb.labwitch.models.embed.storage.Cabinet
import org.cdb.labwitch.models.identifiers.ShortId
import org.cdb.labwitch.models.types.ShortText

/**
 * This class represents a room where multiple [Cabinet]s are.
 * It is identified by a short id.
 *
 * @param id the id of the room (max. 8 characters).
 * @param name a human-readable name for the room.
 * @param description a [ShortText] description for the room.
 * @param cabinets a [List] of [Cabinet]s.
 */
@Serializable
data class StorageRoom(
	@SerialName("_id") override val id: ShortId = ShortId.generate(),
	val name: String,
	val description: ShortText? = null,
	val cabinets: List<Cabinet> = emptyList(),
) : StoredEntity
