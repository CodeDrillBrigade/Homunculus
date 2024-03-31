package org.cdb.labwitch.models.embed.storage

import kotlinx.serialization.Serializable
import org.cdb.labwitch.models.identifiers.ShortId

/**
 * This class represents a shelf in a [Cabinet], where the material is stored.
 *
 * @param id the id of the cabinet. It consists of <STORAGE_ROOM_ID>|<CABINET_ID>|<SHORT_ID>.
 * @param name a human-readable name for the shelf.
 */
@Serializable
data class Shelf(
	val id: ShortId = ShortId.generate(),
	val name: String,
)
