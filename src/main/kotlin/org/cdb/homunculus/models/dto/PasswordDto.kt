package org.cdb.homunculus.models.dto

import kotlinx.serialization.Serializable

@Serializable
data class PasswordDto(
	val password: String,
) {
	fun isValid() = password.length >= 6
}
