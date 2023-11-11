package org.cdb.labwitch.configuration

import io.ktor.server.application.*
import org.cdb.labwitch.components.DBClient
import org.cdb.labwitch.components.PasswordEncoder
import org.cdb.labwitch.components.impl.BCryptPasswordEncoder
import org.cdb.labwitch.dao.UserDao
import org.cdb.labwitch.dao.impl.UserDaoImpl
import org.cdb.labwitch.logic.UserLogic
import org.cdb.labwitch.logic.impl.UserLogicImpl
import org.koin.dsl.module
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger

fun applicationModules(
    username: String,
    password: String,
    ip: String,
    port: String,
    databaseName: String
) = module {
    single<DBClient> { DBClient(username, password, ip, port, databaseName) }
    single<PasswordEncoder> { BCryptPasswordEncoder() }
    single<UserDao> { UserDaoImpl(get()) }
    single<UserLogic> { UserLogicImpl(get(), get()) }
}

/**
 * Loads into the server all the logic classes.
 *
 * @receiver a ktor [Application]
 */
fun Application.configureKoin() {

    val username = environment.config.property("mongodb.username").getString()
    val password = environment.config.property("mongodb.password").getString()
    val ip = environment.config.property("mongodb.ip").getString()
    val port = environment.config.property("mongodb.port").getString()
    val databaseName = environment.config.property("mongodb.databaseName").getString()

    install(Koin) {
        slf4jLogger()
        modules(applicationModules(username, password, ip, port, databaseName))
    }
}