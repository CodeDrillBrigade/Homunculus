package org.cdb.homunculus.controller

import io.ktor.http.content.PartData
import io.ktor.http.content.streamProvider
import io.ktor.server.application.call
import io.ktor.server.request.receiveMultipart
import io.ktor.server.response.respond
import io.ktor.server.routing.Routing
import io.ktor.server.routing.route
import org.cdb.homunculus.logic.ProfilePictureLogic
import org.cdb.homunculus.models.identifiers.EntityId
import org.cdb.homunculus.models.types.MimeType
import org.cdb.homunculus.requests.authenticatedGet
import org.cdb.homunculus.requests.authenticatedPost
import org.cdb.homunculus.requests.authenticatedPut
import org.cdb.homunculus.utils.FileUtils
import org.koin.ktor.ext.inject

fun Routing.profilePictureController() =
	route("/profilePicture") {
		val profilePictureLogic by inject<ProfilePictureLogic>()

		authenticatedGet("/{profilePictureId}") {
			val profilePictureId = checkNotNull(call.parameters["profilePictureId"]) { "Profile Picture Id must not be null" }
			call.respond(profilePictureLogic.get(EntityId(profilePictureId)))
		}

		authenticatedPost("") {
			val tokenPart =
				call.receiveMultipart().readPart()?.takeIf { part -> part is PartData.FileItem } as? PartData.FileItem
					?: throw IllegalArgumentException("No file uploaded")
			val (token, mimeType) = extractAndValidatePictureContent(tokenPart)
			tokenPart.dispose()
			call.respond(profilePictureLogic.createProfilePicture(token, mimeType))
		}

		authenticatedPut("/{profilePictureId}") {
			val profilePictureId = checkNotNull(call.parameters["profilePictureId"]) { "Profile Picture Id must not be null" }
			val tokenPart =
				call.receiveMultipart().readPart()?.takeIf { part -> part is PartData.FileItem } as? PartData.FileItem
					?: throw IllegalArgumentException("No file uploaded")
			val (token, mimeType) = extractAndValidatePictureContent(tokenPart)
			tokenPart.dispose()
			call.respond(profilePictureLogic.modifyProfilePicture(EntityId(profilePictureId), token, mimeType))
		}
	}

private fun extractAndValidatePictureContent(tokenPart: PartData.FileItem): Pair<ByteArray, MimeType> {
	val imageBytes = tokenPart.streamProvider().readAllBytes()
	val mimeType = FileUtils.inferMimeType(imageBytes)
	if (!MimeType.isImage(mimeType)) {
		throw IllegalStateException("Type not supported: ${mimeType.mimeType}")
	}
	if (imageBytes.size > 1_000_000) {
		throw IllegalArgumentException("Image file is too big")
	}
	return imageBytes to mimeType
}
