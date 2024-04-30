package org.cdb.homunculus.serialization

import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.util.SortedSet

class SortedSetSerializer<T : Comparable<T>>(
	private val elementSerializer: KSerializer<T>,
) : KSerializer<SortedSet<T>> {
	private val listSerializer = ListSerializer(elementSerializer)

	override val descriptor: SerialDescriptor = listSerializer.descriptor

	override fun deserialize(decoder: Decoder): SortedSet<T> = listSerializer.deserialize(decoder).toSortedSet()

	override fun serialize(
		encoder: Encoder,
		value: SortedSet<T>,
	) = listSerializer.serialize(encoder, value.toList())
}
