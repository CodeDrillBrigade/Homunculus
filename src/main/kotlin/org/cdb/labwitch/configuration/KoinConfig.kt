package org.cdb.labwitch.configuration

import io.ktor.server.application.*
import org.cdb.labwitch.components.DBClient
import org.cdb.labwitch.components.JWTManager
import org.cdb.labwitch.components.PasswordEncoder
import org.cdb.labwitch.components.impl.BCryptPasswordEncoder
import org.cdb.labwitch.dao.BoxDefinitionDao
import org.cdb.labwitch.dao.MaterialDao
import org.cdb.labwitch.dao.RoleDao
import org.cdb.labwitch.dao.StorageDao
import org.cdb.labwitch.dao.TagDao
import org.cdb.labwitch.dao.UserDao
import org.cdb.labwitch.dao.impl.BoxDefinitionDaoImpl
import org.cdb.labwitch.dao.impl.MaterialDaoImpl
import org.cdb.labwitch.dao.impl.RoleDaoImpl
import org.cdb.labwitch.dao.impl.StorageDaoImpl
import org.cdb.labwitch.dao.impl.TagDaoImpl
import org.cdb.labwitch.dao.impl.UserDaoImpl
import org.cdb.labwitch.logic.AuthenticationLogic
import org.cdb.labwitch.logic.BoxDefinitionLogic
import org.cdb.labwitch.logic.MaterialLogic
import org.cdb.labwitch.logic.StorageLogic
import org.cdb.labwitch.logic.TagLogic
import org.cdb.labwitch.logic.UserLogic
import org.cdb.labwitch.logic.impl.AuthenticationLogicImpl
import org.cdb.labwitch.logic.impl.BoxDefinitionLogicImpl
import org.cdb.labwitch.logic.impl.MaterialLogicImpl
import org.cdb.labwitch.logic.impl.StorageLogicImpl
import org.cdb.labwitch.logic.impl.TagLogicImpl
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

	// DAOs
	single<BoxDefinitionDao> { BoxDefinitionDaoImpl(get()) }
	single<MaterialDao> { MaterialDaoImpl(get()) }
	single<RoleDao> { RoleDaoImpl(get()) }
	single<StorageDao> { StorageDaoImpl(get()) }
	single<TagDao> { TagDaoImpl(get()) }
	single<UserDao> { UserDaoImpl(get()) }

	// Logics
	single<AuthenticationLogic> { AuthenticationLogicImpl(get(), get(), get(), get()) }
	single<BoxDefinitionLogic> { BoxDefinitionLogicImpl(get()) }
	single<MaterialLogic> { MaterialLogicImpl(get()) }
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

	install(Koin) {
		slf4jLogger()
		modules(applicationModules(dbCredentials, jwtConfig))
	}
}
