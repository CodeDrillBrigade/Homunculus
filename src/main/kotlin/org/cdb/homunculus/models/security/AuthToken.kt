package org.cdb.homunculus.models.security

import kotlinx.serialization.Serializable
import org.cdb.homunculus.serialization.DateSerializer
import java.util.Date

@Serializable
data class AuthToken(
	val token: String,
	@Serializable(with = DateSerializer::class) val expirationDate: Date,
)
