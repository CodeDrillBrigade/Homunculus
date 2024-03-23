package org.cdb.labwitch.models

import kotlinx.serialization.SerialName
import org.cdb.labwitch.models.types.ShortText

/**
 * This class represents a room where multiple [Cabinet]s are.
 * It is identified by a short id.
 *
 * @param id the id of the room (max. 8 characters).
 * @param room a human-readable name for the room.
 * @param description a [ShortText] description for the room.
 * @param cabinets a [List] of [Cabinet]s.
 */
data class StorageRoom(
    @SerialName("_id") val id: String,
    val room: String,
    val description: ShortText,
    val cabinets: List<Cabinet> = emptyList(),
)

/**
 * This class represents a cabinet in a [StorageRoom].
 * It can have multiple [Shelf] where the boxes are stored.
 *
 * @param id the id of the cabinet. It consists of <STORAGE_ROOM_ID>|<SHORT_ID>.
 * @param cabinet a human-readable name for the room.
 * @param description a [ShortText] description for the room.
 * @param shelves a [List] of [Shelf]s.
 */
data class Cabinet(
    val id: String,
    val cabinet: String,
    val description: ShortText,
    val shelves: List<Shelf> = emptyList(),
)

/**
 * This class represents a shelf in a [Cabinet], where the material is stored.
 *
 * @param id the id of the cabinet. It consists of <STORAGE_ROOM_ID>|<CABINET_ID>|<SHORT_ID>.
 * @param shelf a human-readable name for the room.
 */
data class Shelf(
    val id: String,
    val shelf: String,
)
