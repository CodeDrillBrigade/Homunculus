package org.cdb.homunculus.models.config

import io.ktor.server.config.ApplicationConfig

data class MailerConfig(
	val smtpHost: String,
	val smtpPort: String,
	val username: String,
	val password: String
) {

	companion object {
		fun fromConfig(config: ApplicationConfig) =
			MailerConfig(
				username = config.property("mailer.username").getString(),
				password = config.property("mailer.password").getString(),
				smtpHost = config.property("mailer.host").getString(),
				smtpPort = config.property("mailer.port").getString()
			)
	}

}
