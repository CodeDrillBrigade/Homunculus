package org.cdb.labwitch.models.embed

import org.cdb.labwitch.models.User
import java.util.Date

data class Note(
	val user: User,
	val date: Date,
	val message: String
)