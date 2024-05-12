package org.cdb.homunculus.logic

import org.cdb.homunculus.models.identifiers.EntityId

interface ProcessLogic {
	/**
	 * Starts a password reset [Process], sending the email with a link to the user.
	 *
	 * @param email the email of the user.
	 */
	suspend fun initiatePasswordResetProcess(email: String)

	/**
	 * Completes a reset password process by removing the old password from the user and
	 * assigning a new temporary token.
	 *
	 * @param processId the id of the password reset [Process]
	 * @return the new temporary token
	 * @throws IllegalArgumentException if the process does not exist or was already completed.
	 */
	suspend fun completePasswordResetProcess(processId: EntityId): String
}
