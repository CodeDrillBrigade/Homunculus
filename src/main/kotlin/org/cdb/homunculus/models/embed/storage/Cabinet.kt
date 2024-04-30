package org.cdb.homunculus.models.embed.storage

import kotlinx.serialization.Serializable
import org.cdb.homunculus.models.StorageRoom
import org.cdb.homunculus.models.identifiers.ShortId
import org.cdb.homunculus.models.types.ShortText

/**
 * This class represents a cabinet in a [StorageRoom].
 * It can have multiple [Shelf] where the boxes are stored.
 *
 * @param id the id of the cabinet. It consists of <STORAGE_ROOM_ID>|<SHORT_ID>.
 * @param name a human-readable name for the room.
 * @param description a [ShortText] description for the cabinet.
 * @param shelves a [List] of [Shelf]s.
 */
@Serializable
data class Cabinet(
	val id: ShortId = ShortId.generate(),
	val name: String,
	val description: ShortText? = null,
	val shelves: List<Shelf> = emptyList(),
)
