package org.cdb.homunculus.components

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.Serializable
import org.cdb.homunculus.models.Alert
import org.cdb.homunculus.models.Material
import org.cdb.homunculus.models.Notification
import org.cdb.homunculus.models.config.MailerConfig
import java.net.URLEncoder

class Mailer(
	private val config: MailerConfig,
) {
	companion object {
		@Serializable
		private data class MailInput(
			val id: String,
			val email: String,
			val attributes: Map<String, String>,
		)
	}

	private val httpClient =
		HttpClient(CIO) {
			install(ContentNegotiation) {
				json()
			}
		}

	suspend fun sendPasswordResetEmail(
		email: String,
		processId: String,
	) {
		httpClient.post("${config.hermesUrl}/v1/mail") {
			contentType(ContentType.Application.Json)
			setBody(
				MailInput(
					id = config.resetPasswordTemplateId,
					email = email,
					attributes =
						mapOf(
							"url" to config.homunculusUrl,
							"email" to URLEncoder.encode(email, "UTF-8"),
							"processId" to URLEncoder.encode(processId, "UTF-8"),
						),
				),
			)
		}
	}

	suspend fun sendInvitationEmail(
		email: String,
		tmpToken: String,
		inviterName: String,
	) {
		httpClient.post("${config.hermesUrl}/v1/mail") {
			contentType(ContentType.Application.Json)
			setBody(
				MailInput(
					id = config.inviteTemplateId,
					email = email,
					attributes =
						mapOf(
							"url" to config.homunculusUrl,
							"inviterName" to inviterName,
							"email" to URLEncoder.encode(email, "UTF-8"),
							"tmpToken" to URLEncoder.encode(tmpToken, "UTF-8"),
						),
				),
			)
		}
	}

	suspend fun sendReportEmail(
		materialsWithThresholds: Map<Material, Int>,
		recipientEmail: String,
	) {
		val materialsAsString =
			buildString {
				materialsWithThresholds.entries.groupBy { it.value }.forEach { (threshold, materials) ->
					append("Less than $threshold units:\n")
					materials.forEach { material ->
						append("\t - ")
						append(material.key.name)
						append(" (${material.key.brand}")
						material.key.referenceCode?.also { append(", #$it") }
						append(").")
					}
					append("\n")
				}
			}
		httpClient.post("${config.hermesUrl}/v1/mail") {
			contentType(ContentType.Application.Json)
			setBody(
				MailInput(
					id = config.reportTemplateId,
					email = recipientEmail,
					attributes =
						mapOf(
							"materials" to materialsAsString,
						),
				),
			)
		}
	}

	suspend fun sendAlertEmail(
		materials: List<Material>,
		alert: Alert,
		recipientEmails: Set<String>,
	) = sendNotification(materials, alert, config.alertTemplateId, recipientEmails)

	private suspend fun sendNotification(
		materials: List<Material>,
		notification: Notification,
		defaultTemplateId: String,
		recipientEmails: Set<String>,
	) {
		val materialsAsString =
			materials.joinToString(separator = "\n") { material ->
				buildString {
					append("\t - ")
					append(material.name)
					append(" (${material.brand}")
					material.referenceCode?.also { append(", #$it") }
					append(").")
				}
			}
		recipientEmails.forEach { recipient ->
			httpClient.post("${config.hermesUrl}/v1/mail") {
				contentType(ContentType.Application.Json)
				setBody(
					MailInput(
						id = notification.templateId ?: defaultTemplateId,
						email = recipient,
						attributes =
							mapOf(
								"materials" to materialsAsString,
								"threshold" to "${notification.threshold}",
							),
					),
				)
			}
		}
	}
}
