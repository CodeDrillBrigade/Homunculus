package org.cdb.homunculus.models.security

import kotlinx.serialization.Serializable

@Serializable
data class AuthToken(
	val token: String,
	val expirationDate: Long
)
