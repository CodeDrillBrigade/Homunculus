package org.cdb.homunculus.models.embed

import kotlinx.serialization.Serializable

@Serializable
enum class ProcessStatus {
	CREATED,
	COMPLETED,
}
