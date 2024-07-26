package org.cdb.homunculus.utils

import org.apache.tika.Tika
import org.cdb.homunculus.models.types.MimeType

object FileUtils {
	private val tika = Tika()

	fun inferMimeType(file: ByteArray): MimeType = MimeType.safeValueOf(tika.detect(file))
}
