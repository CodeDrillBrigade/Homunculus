package org.cdb.homunculus.utils

import kotlinx.serialization.json.Json
import org.cdb.homunculus.models.types.ShortText

object StringNormalizer {
	private val latinMap =
		this::class.java.getResource("latin_map.json")?.readText()?.let {
			Json.decodeFromString<Map<Char, String>>(it)
		} ?: throw IllegalStateException("Cannot load latin map for normalizer")

	/**
	 * @param input a [String].
	 * @return a lowercase, normalized version of [input] where all the non latin characters are converted to
	 * latin characters and special characters ([^a-z0-9]) are removed.
	 */
	fun normalize(input: String): String {
		return input.lowercase().map { char ->
			latinMap[char] ?: char.toString()
		}.joinToString("").replace(Regex("[^a-z0-9]"), "")
	}

	/**
	 * @param input a [ShortText].
	 * @return a lowercase, normalized version of [input] where all the non latin characters are converted to
	 * latin characters and special characters ([^a-z0-9]) are removed.
	 */
	fun normalize(input: ShortText) = normalize(input.text)
}
