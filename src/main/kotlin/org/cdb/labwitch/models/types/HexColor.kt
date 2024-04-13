package org.cdb.labwitch.models.types

import kotlinx.serialization.Serializable

@JvmInline
@Serializable
value class HexColor(val color: String) {
	init {
		require("^#([a-fA-F0-9]{6}|[a-fA-F0-9]{3})\$".toRegex().matches(color)) {
			"$color is not a valid Hex color"
		}
	}
}
