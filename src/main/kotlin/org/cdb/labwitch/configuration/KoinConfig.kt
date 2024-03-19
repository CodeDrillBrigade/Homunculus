package org.cdb.labwitch.configuration

import io.ktor.server.application.*
import org.cdb.labwitch.components.DBClient
import org.cdb.labwitch.components.JWTManager
import org.cdb.labwitch.components.PasswordEncoder
import org.cdb.labwitch.components.impl.BCryptPasswordEncoder
import org.cdb.labwitch.dao.UserDao
import org.cdb.labwitch.dao.impl.UserDaoImpl
import org.cdb.labwitch.logic.AuthenticationLogic
import org.cdb.labwitch.logic.UserLogic
import org.cdb.labwitch.logic.impl.AuthenticationLogicImpl
import org.cdb.labwitch.logic.impl.UserLogicImpl
import org.cdb.labwitch.models.config.JWTConfig
import org.cdb.labwitch.models.config.MongoDBCredentials
import org.koin.dsl.module
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger

fun applicationModules(
    dbCredentials: MongoDBCredentials,
    jwtConfig: JWTConfig,
) = module {
    single<JWTManager> { JWTManager(jwtConfig) }
    single<DBClient> { DBClient(dbCredentials) }
    single<PasswordEncoder> { BCryptPasswordEncoder() }
    single<UserDao> { UserDaoImpl(get()) }
    single<UserLogic> { UserLogicImpl(get(), get()) }
    single<AuthenticationLogic> { AuthenticationLogicImpl(get(), get(), get()) }
}

/**
 * Loads into the server all the logic classes.
 *
 * @receiver a ktor [Application]
 */
fun Application.configureKoin() {
    val dbCredentials = MongoDBCredentials.fromConfig(environment.config)
    val jwtConfig = JWTConfig.fromConfig(environment.config)

    install(Koin) {
        slf4jLogger()
        modules(applicationModules(dbCredentials, jwtConfig))
    }
}
