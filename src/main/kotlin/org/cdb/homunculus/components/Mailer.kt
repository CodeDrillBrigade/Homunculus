package org.cdb.homunculus.components

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
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

	private suspend fun sendNotification(
		materialsWithRemainingBoxes: Map<Material, Int>,
		notification: Notification?,
		defaultTemplateId: String,
		recipientEmails: Set<String>,
	): List<HttpResponse> {
		val materialsAsString =
			buildString {
				materialsWithRemainingBoxes.entries.forEach { (material, remaining) ->
					append("- ")
					append("$remaining REMAINING: ")
					append(material.name)
					append(" (${material.brand}")
					material.referenceCode?.also { append(", #$it") }
					append(").\n")
				}
			}
		return recipientEmails.map {
			httpClient.post("${config.hermesUrl}/v1/mail") {
				contentType(ContentType.Application.Json)
				setBody(
					MailInput(
						id = notification?.templateId ?: defaultTemplateId,
						email = it,
						attributes =
							mapOf(
								"materials" to materialsAsString,
							),
					),
				)
			}
		}
	}

	suspend fun sendAlertEmail(
		materialsWithRemainingBoxes: Map<Material, Int>,
		alert: Alert,
		recipientEmails: Set<String>,
	) = sendNotification(materialsWithRemainingBoxes, alert, config.alertTemplateId, recipientEmails)

	suspend fun sendReportEmail(
		materialsWithRemainingBoxes: Map<Material, Int>,
		recipientEmail: String,
	) = sendNotification(materialsWithRemainingBoxes, null, config.reportTemplateId, setOf(recipientEmail)).first()
}
