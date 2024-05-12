package org.cdb.homunculus.components

import org.cdb.homunculus.models.config.MailerConfig
import org.koin.core.logger.Logger
import java.net.URLEncoder
import java.util.Properties
import javax.mail.Authenticator
import javax.mail.Message
import javax.mail.PasswordAuthentication
import javax.mail.Session
import javax.mail.Transport
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage

class Mailer(
	private val config: MailerConfig,
	private val logger: Logger,
) {
	private val properties =
		Properties().apply {
			put("mail.smtp.host", config.smtpHost)
			put("mail.smtp.port", config.smtpPort)
			put("mail.smtp.auth", "true")
			put("mail.smtp.starttls.enable", "true")
		}

	private val session: Session
		get() =
			Session.getInstance(
				properties,
				object : Authenticator() {
					override fun getPasswordAuthentication(): PasswordAuthentication {
						return PasswordAuthentication(config.username, config.password)
					}
				},
			)

	fun sendPasswordResetEmail(
		email: String,
		processId: String,
	) {
		try {
			val message =
				MimeMessage(session).apply {
					setFrom(InternetAddress("homunculus@kaironbot.net"))
					addRecipient(Message.RecipientType.TO, InternetAddress(email))
					subject = "Reset your Homunculus password"
					setText(
						"""
						Hello,
						apparently you forgot your password. That's a shame, follow this link to recover it:
						${config.homunculusUrl}/passwordReset?email=${URLEncoder.encode(
							email,
							Charsets.UTF_8,
						)}&secret=${URLEncoder.encode(processId, Charsets.UTF_8)}
						Best of luck,
						your personal Homunculus.
						""".trimIndent(),
					)
				}
			Transport.send(message)
			logger.info("Password reset email to $email sent successfully!")
		} catch (e: Exception) {
			logger.error("There was an error while sending a password reset email to $email:\n ${e.stackTraceToString()}")
		}
	}
}
