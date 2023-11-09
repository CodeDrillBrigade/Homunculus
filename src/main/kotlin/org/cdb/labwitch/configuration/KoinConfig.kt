package org.cdb.labwitch.configuration

import io.ktor.server.application.*
import org.cdb.labwitch.components.DBClient
import org.cdb.labwitch.dao.UserDao
import org.cdb.labwitch.dao.impl.UserDaoImpl
import org.koin.dsl.module
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger

val applicationModules = module {
    single<DBClient> { DBClient() }
    single<UserDao> { UserDaoImpl(get()) }
}

/**
 * Loads into the server all the logic classes.
 *
 * @receiver a ktor [Application]
 */
fun Application.configureKoin() {
    install(Koin) {
        slf4jLogger()
        modules(applicationModules)
    }
}