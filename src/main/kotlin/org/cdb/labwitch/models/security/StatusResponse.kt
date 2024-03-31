package org.cdb.labwitch.models.security

import kotlinx.serialization.Serializable

@Serializable
data class StatusResponse(
	val status: Boolean,
	val message: String? = null,
	val code: Int? = null,
)
