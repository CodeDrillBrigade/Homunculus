package org.cdb.labwitch.models

import org.cdb.labwitch.models.embed.BoxDefinition
import org.cdb.labwitch.models.embed.Note
import org.cdb.labwitch.models.embed.Tag
import org.cdb.labwitch.utils.UserId
import java.util.*

data class Material(
    override val id: String,
    val name: String,
    val boxDefinition: BoxDefinition,
    val brand: String,
    val tags: Set<Tag>,
    val description: String,
    val noteList: List<Note>,
    override val modificationLog: Map<Date, UserId> = emptyMap(),
) : Traceable
