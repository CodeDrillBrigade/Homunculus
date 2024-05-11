package org.cdb.homunculus.logic

interface ProcessLogic {
	/**
	 * Starts a password reset [Process], sending the email with a link to the user.
	 *
	 * @param email the email of the user.
	 */
	suspend fun initiatePasswordResetProcess(email: String)
}
