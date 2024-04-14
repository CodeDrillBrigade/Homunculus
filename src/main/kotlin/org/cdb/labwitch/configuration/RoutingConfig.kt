package org.cdb.labwitch.configuration

import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.routing.*
import org.cdb.labwitch.controller.authController
import org.cdb.labwitch.controller.boxController
import org.cdb.labwitch.controller.boxDefinitionController
import org.cdb.labwitch.controller.materialController
import org.cdb.labwitch.controller.storageController
import org.cdb.labwitch.controller.tagController
import org.cdb.labwitch.controller.userController

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
