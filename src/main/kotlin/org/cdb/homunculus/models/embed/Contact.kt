package org.cdb.homunculus.models.embed

import kotlinx.serialization.Serializable

@Serializable
data class Contact(
	val value: String,
	val type: ContactType,
)
