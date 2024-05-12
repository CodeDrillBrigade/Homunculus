package org.cdb.homunculus.configuration

import io.ktor.server.application.*
import org.cdb.homunculus.components.DBClient
import org.cdb.homunculus.components.JWTManager
import org.cdb.homunculus.components.Mailer
import org.cdb.homunculus.components.PasswordEncoder
import org.cdb.homunculus.components.impl.BCryptPasswordEncoder
import org.cdb.homunculus.dao.BoxDao
import org.cdb.homunculus.dao.BoxDefinitionDao
import org.cdb.homunculus.dao.MaterialDao
import org.cdb.homunculus.dao.ProcessDao
import org.cdb.homunculus.dao.RoleDao
import org.cdb.homunculus.dao.StorageDao
import org.cdb.homunculus.dao.TagDao
import org.cdb.homunculus.dao.UserDao
import org.cdb.homunculus.dao.impl.BoxDaoImpl
import org.cdb.homunculus.dao.impl.BoxDefinitionDaoImpl
import org.cdb.homunculus.dao.impl.MaterialDaoImpl
import org.cdb.homunculus.dao.impl.ProcessDaoImpl
import org.cdb.homunculus.dao.impl.RoleDaoImpl
import org.cdb.homunculus.dao.impl.StorageDaoImpl
import org.cdb.homunculus.dao.impl.TagDaoImpl
import org.cdb.homunculus.dao.impl.UserDaoImpl
import org.cdb.homunculus.logic.AuthenticationLogic
import org.cdb.homunculus.logic.BoxDefinitionLogic
import org.cdb.homunculus.logic.BoxLogic
import org.cdb.homunculus.logic.MaterialLogic
import org.cdb.homunculus.logic.ProcessLogic
import org.cdb.homunculus.logic.StorageLogic
import org.cdb.homunculus.logic.TagLogic
import org.cdb.homunculus.logic.UserLogic
import org.cdb.homunculus.logic.impl.AuthenticationLogicImpl
import org.cdb.homunculus.logic.impl.BoxDefinitionLogicImpl
import org.cdb.homunculus.logic.impl.BoxLogicImpl
import org.cdb.homunculus.logic.impl.MaterialLogicImpl
import org.cdb.homunculus.logic.impl.ProcessLogicImpl
import org.cdb.homunculus.logic.impl.StorageLogicImpl
import org.cdb.homunculus.logic.impl.TagLogicImpl
import org.cdb.homunculus.logic.impl.UserLogicImpl
import org.cdb.homunculus.models.config.JWTConfig
import org.cdb.homunculus.models.config.MailerConfig
import org.cdb.homunculus.models.config.MongoDBCredentials
import org.koin.dsl.module
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger

fun applicationModules(
	dbCredentials: MongoDBCredentials,
	jwtConfig: JWTConfig,
	mailerConfig: MailerConfig,
) = module {
	single<JWTManager> { JWTManager(jwtConfig) }
	single<DBClient> { DBClient(dbCredentials) }
	single<PasswordEncoder> { BCryptPasswordEncoder() }
	single<Mailer> { Mailer(mailerConfig, logger) }

	// DAOs
	single<BoxDao> { BoxDaoImpl(get()) }
	single<BoxDefinitionDao> { BoxDefinitionDaoImpl(get()) }
	single<MaterialDao> { MaterialDaoImpl(get()) }
	single<ProcessDao> { ProcessDaoImpl(get()) }
	single<RoleDao> { RoleDaoImpl(get()) }
	single<StorageDao> { StorageDaoImpl(get()) }
	single<TagDao> { TagDaoImpl(get()) }
	single<UserDao> { UserDaoImpl(get()) }

	// Logics
	single<AuthenticationLogic> { AuthenticationLogicImpl(get(), get(), get(), get()) }
	single<BoxLogic> { BoxLogicImpl(get()) }
	single<BoxDefinitionLogic> { BoxDefinitionLogicImpl(get()) }
	single<MaterialLogic> { MaterialLogicImpl(get()) }
	single<ProcessLogic> { ProcessLogicImpl(get(), get(), get(), get()) }
	single<StorageLogic> { StorageLogicImpl(get()) }
	single<TagLogic> { TagLogicImpl(get()) }
	single<UserLogic> { UserLogicImpl(get(), get()) }
}

/**
 * Loads into the server all the logic classes.
 *
 * @receiver a ktor [Application]
 */
fun Application.configureKoin() {
	val dbCredentials = MongoDBCredentials.fromConfig(environment.config)
	val jwtConfig = JWTConfig.fromConfig(environment.config)
	val mailerConfig = MailerConfig.fromConfig(environment.config)

	install(Koin) {
		slf4jLogger()
		modules(applicationModules(dbCredentials, jwtConfig, mailerConfig))
	}
}
