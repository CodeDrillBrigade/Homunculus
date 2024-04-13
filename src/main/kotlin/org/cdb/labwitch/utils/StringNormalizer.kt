package org.cdb.labwitch.utils

import kotlinx.serialization.json.Json
import org.cdb.labwitch.models.types.ShortText

object StringNormalizer {
	private val latinMap =
		this::class.java.getResource("latin_map.json")?.readText()?.let {
			Json.decodeFromString<Map<Char, String>>(it)
		} ?: throw IllegalStateException("Cannot load latin map for normalizer")

	fun normalize(input: String): String {
		return input.lowercase().map { char ->
			latinMap[char] ?: char.toString()
		}.joinToString("").replace(Regex("[^a-z0-9]"), "")
	}

	fun normalize(input: ShortText) = normalize(input.text)
}
