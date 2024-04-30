package org.cdb.homunculus.configuration

import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.routing.*
import org.cdb.homunculus.controller.authController
import org.cdb.homunculus.controller.boxController
import org.cdb.homunculus.controller.boxDefinitionController
import org.cdb.homunculus.controller.materialController
import org.cdb.homunculus.controller.storageController
import org.cdb.homunculus.controller.tagController
import org.cdb.homunculus.controller.userController

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
	}
}
