package org.cdb.labwitch.models.embed

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import org.cdb.labwitch.models.User
import java.util.Date

@Serializable
data class Note(
	val user: User,
	@Contextual
	val date: Date,
	val message: String
)