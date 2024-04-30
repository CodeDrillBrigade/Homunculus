package org.cdb.homunculus.models.types

import kotlinx.serialization.Serializable

@JvmInline
@Serializable
value class ShortText(val text: String) {
	init {
		require(text.length <= 300) {
			"A ShortText cannot be greater than 300 chracters"
		}
	}
}
