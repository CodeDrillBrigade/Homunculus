package org.cdb.homunculus.components

import org.cdb.homunculus.models.config.MailerConfig
import java.util.Properties
import javax.mail.Authenticator
import javax.mail.PasswordAuthentication
import javax.mail.Session

class Mailer(
	private val config: MailerConfig
) {

	private val properties = Properties().apply {
		put("mail.smtp.host", config.smtpHost)
		put("mail.smtp.port", config.smtpPort)
		put("mail.smtp.auth", "true")
		put("mail.smtp.starttls.enable", "true")
	}

	private val session: Session
		get() = Session.getInstance(properties, object : Authenticator() {
		override fun getPasswordAuthentication(): PasswordAuthentication {
			return PasswordAuthentication(config.username, config.password)
		}
	})

}
