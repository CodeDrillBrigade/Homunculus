package org.cdb.homunculus.models

import kotlinx.serialization.Serializable
import org.cdb.homunculus.models.embed.Contact

@Serializable
data class UserCreationData(
	val username: String,
	val password: String,
	val name: String,
	val surname: String,
	val contacts: List<Contact> = emptyList(),
)
