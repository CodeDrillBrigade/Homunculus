package org.cdb.homunculus.components.impl

import org.cdb.homunculus.components.PasswordEncoder
import org.mindrot.jbcrypt.BCrypt

/**
 * Implementation of [PasswordEncoder] based on [BCrypt]
 */
class BCryptPasswordEncoder : PasswordEncoder {
	override fun hashAndSaltPassword(password: String): String = BCrypt.hashpw(password, BCrypt.gensalt())

	override fun checkHash(
		password: String,
		hash: String,
	): Boolean = BCrypt.checkpw(password, hash)
}
