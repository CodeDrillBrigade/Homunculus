package org.cdb.labwitch.configuration

import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.routing.*
import org.cdb.labwitch.controller.*

fun Application.configureRouting() {
	install(ContentNegotiation) {
		json()
	}
	routing {
		authController()
		boxController()
		boxDefinitionController()
		materialController()
		storageController()
		tagController()
		userController()
		roleController()
	}
}
