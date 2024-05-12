package org.cdb.homunculus.logic.impl

import org.cdb.homunculus.components.Mailer
import org.cdb.homunculus.components.PasswordEncoder
import org.cdb.homunculus.dao.ProcessDao
import org.cdb.homunculus.dao.UserDao
import org.cdb.homunculus.logic.ProcessLogic
import org.cdb.homunculus.models.embed.ProcessStatus
import org.cdb.homunculus.models.embed.ProcessType
import org.cdb.homunculus.models.identifiers.EntityId
import org.cdb.homunculus.models.security.AuthToken
import java.util.Date
import java.util.UUID
import kotlin.time.Duration.Companion.hours
import org.cdb.homunculus.models.Process as HProcess

class ProcessLogicImpl(
	private val processDao: ProcessDao,
	private val userDao: UserDao,
	private val mailer: Mailer,
	private val passwordEncoder: PasswordEncoder,
) : ProcessLogic {
	override suspend fun initiatePasswordResetProcess(email: String) {
		val user = userDao.getByEmail(email)
		if (user != null) {
			val processId =
				processDao.save(
					HProcess(
						type = ProcessType.PASSWORD_RESET,
						status = ProcessStatus.CREATED,
						started = Date(),
						userId = user.id,
					),
				)
			mailer.sendPasswordResetEmail(email, processId.id)
		}
	}

	override suspend fun completePasswordResetProcess(processId: EntityId): String {
		val process =
			requireNotNull(processDao.getById(processId)) { "Invalid process" }.also {
				require(it.status != ProcessStatus.COMPLETED) { "Invalid process" }
			}
		val user =
			process.userId?.let {
				userDao.getById(it)?.removeExpiredTokens()
			} ?: throw IllegalStateException("User not found")
		val rawToken = UUID.randomUUID().toString()
		processDao.update(
			process.copy(
				status = ProcessStatus.COMPLETED,
			),
		)
		userDao.update(
			user.copy(
				authenticationTokens =
					user.authenticationTokens + (
						"recover" to
							AuthToken(
								token = passwordEncoder.hashAndSaltPassword(rawToken),
								expirationDate = Date(System.currentTimeMillis() + 1L.hours.inWholeMilliseconds),
							)
					),
			),
		)
		return rawToken
	}
}
