package org.cdb.labwitch.models.security

import kotlinx.serialization.Serializable

@Serializable
data class AuthCredentials(
	val username: String,
	val password: String,
)
