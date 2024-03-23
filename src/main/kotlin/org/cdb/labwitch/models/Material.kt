package org.cdb.labwitch.models

import kotlinx.serialization.SerialName
import org.cdb.labwitch.models.embed.BoxDefinition
import org.cdb.labwitch.models.embed.Note
import org.cdb.labwitch.models.embed.Tag
import org.cdb.labwitch.models.types.EntityId
import org.cdb.labwitch.models.types.UserId
import java.util.*

data class Material(
    @SerialName("_id") override val id: EntityId,
    val name: String,
    val boxDefinition: BoxDefinition,
    val brand: String,
    val tags: Set<Tag>,
    val description: String,
    val noteList: List<Note>,
    override val modificationLog: Map<Date, UserId> = emptyMap(),
) : Traceable
