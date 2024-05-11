package org.cdb.homunculus.logic.impl

import kotlinx.coroutines.flow.count
import org.cdb.homunculus.components.Mailer
import org.cdb.homunculus.dao.ProcessDao
import org.cdb.homunculus.dao.UserDao
import org.cdb.homunculus.logic.ProcessLogic
import org.cdb.homunculus.models.embed.ProcessStatus
import org.cdb.homunculus.models.embed.ProcessType
import java.util.Date
import org.cdb.homunculus.models.Process as HProcess

class ProcessLogicImpl(
	private val processDao: ProcessDao,
	private val userDao: UserDao,
	private val mailer: Mailer,
) : ProcessLogic {
	override suspend fun initiatePasswordResetProcess(email: String) {
		val userExists = userDao.findByEmail(email).count() > 0
		if (userExists) {
			val process =
				processDao.save(
					HProcess(
						type = ProcessType.PASSWORD_RESET,
						status = ProcessStatus.CREATED,
						started = Date(),
					),
				)
			mailer.sendPasswordResetEmail(email, process.id)
		}
	}
}
