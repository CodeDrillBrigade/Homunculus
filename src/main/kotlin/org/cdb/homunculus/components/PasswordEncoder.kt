package org.cdb.homunculus.components

/**
 * Functional interface that defines the method to hash a password and verify the hash.
 */
interface PasswordEncoder {
	/**
	 * Hashes a password with a random salt.
	 *
	 * @param password the password to hash.
	 * @return the generated hash.
	 */
	fun hashAndSaltPassword(password: String): String

	/**
	 * Verifies a plain-text password against a hash.
	 *
	 * @param password the plain-text password to verify.
	 * @param hash the hash to verify against.
	 * @return true if the hash corresponds to the password, false otherwise
	 */
	fun checkHash(
		password: String,
		hash: String,
	): Boolean
}
