package org.cdb.homunculus.configuration

import io.ktor.server.application.*
import io.ktor.util.logging.Logger
import org.cdb.homunculus.components.DBClient
import org.cdb.homunculus.components.JWTManager
import org.cdb.homunculus.components.Mailer
import org.cdb.homunculus.components.NotificationManager
import org.cdb.homunculus.components.PasswordEncoder
import org.cdb.homunculus.components.impl.BCryptPasswordEncoder
import org.cdb.homunculus.components.impl.NotificationManagerImpl
import org.cdb.homunculus.dao.AlertDao
import org.cdb.homunculus.dao.BoxDao
import org.cdb.homunculus.dao.BoxDefinitionDao
import org.cdb.homunculus.dao.MaterialDao
import org.cdb.homunculus.dao.ProcessDao
import org.cdb.homunculus.dao.ProfilePictureDao
import org.cdb.homunculus.dao.ReportDao
import org.cdb.homunculus.dao.RoleDao
import org.cdb.homunculus.dao.StorageDao
import org.cdb.homunculus.dao.TagDao
import org.cdb.homunculus.dao.UserDao
import org.cdb.homunculus.dao.impl.AlertDaoImpl
import org.cdb.homunculus.dao.impl.BoxDaoImpl
import org.cdb.homunculus.dao.impl.BoxDefinitionDaoImpl
import org.cdb.homunculus.dao.impl.MaterialDaoImpl
import org.cdb.homunculus.dao.impl.ProcessDaoImpl
import org.cdb.homunculus.dao.impl.ProfilePictureDaoImpl
import org.cdb.homunculus.dao.impl.ReportDaoImpl
import org.cdb.homunculus.dao.impl.RoleDaoImpl
import org.cdb.homunculus.dao.impl.StorageDaoImpl
import org.cdb.homunculus.dao.impl.TagDaoImpl
import org.cdb.homunculus.dao.impl.UserDaoImpl
import org.cdb.homunculus.logic.AlertLogic
import org.cdb.homunculus.logic.AuthenticationLogic
import org.cdb.homunculus.logic.BoxDefinitionLogic
import org.cdb.homunculus.logic.BoxLogic
import org.cdb.homunculus.logic.MaterialLogic
import org.cdb.homunculus.logic.ProcessLogic
import org.cdb.homunculus.logic.ProfilePictureLogic
import org.cdb.homunculus.logic.ReportLogic
import org.cdb.homunculus.logic.RoleLogic
import org.cdb.homunculus.logic.StorageLogic
import org.cdb.homunculus.logic.TagLogic
import org.cdb.homunculus.logic.UserLogic
import org.cdb.homunculus.logic.impl.AlertLogicImpl
import org.cdb.homunculus.logic.impl.AuthenticationLogicImpl
import org.cdb.homunculus.logic.impl.BoxDefinitionLogicImpl
import org.cdb.homunculus.logic.impl.BoxLogicImpl
import org.cdb.homunculus.logic.impl.MaterialLogicImpl
import org.cdb.homunculus.logic.impl.ProcessLogicImpl
import org.cdb.homunculus.logic.impl.ProfilePictureLogicImpl
import org.cdb.homunculus.logic.impl.ReportLogicImpl
import org.cdb.homunculus.logic.impl.RoleLogicImpl
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
	logger: Logger,
) = module {
	single<JWTManager> { JWTManager(jwtConfig) }
	single<DBClient> { DBClient(dbCredentials) }
	single<PasswordEncoder> { BCryptPasswordEncoder() }
	single<Mailer> { Mailer(mailerConfig) }

	// DAOs
	single<AlertDao> { AlertDaoImpl(get()) }
	single<ProfilePictureDao> { ProfilePictureDaoImpl(get()) }
	single<BoxDao> { BoxDaoImpl(get()) }
	single<BoxDefinitionDao> { BoxDefinitionDaoImpl(get()) }
	single<MaterialDao> { MaterialDaoImpl(get()) }
	single<ProcessDao> { ProcessDaoImpl(get()) }
	single<ReportDao> { ReportDaoImpl(get()) }
	single<RoleDao> { RoleDaoImpl(get()) }
	single<StorageDao> { StorageDaoImpl(get()) }
	single<TagDao> { TagDaoImpl(get()) }
	single<UserDao> { UserDaoImpl(get()) }

	// Components
	single<NotificationManager> { NotificationManagerImpl(get(), get(), get(), get(), get(), get(), logger) }

	// Logics
	single<AlertLogic> { AlertLogicImpl(get(), get(), logger) }
	single<ProfilePictureLogic> { ProfilePictureLogicImpl(get()) }
	single<AuthenticationLogic> { AuthenticationLogicImpl(get(), get(), get(), get()) }
	single<BoxLogic> { BoxLogicImpl(get(), get()) }
	single<BoxDefinitionLogic> { BoxDefinitionLogicImpl(get()) }
	single<MaterialLogic> { MaterialLogicImpl(get()) }
	single<ProcessLogic> { ProcessLogicImpl(get(), get(), get(), get()) }
	single<ReportLogic> { ReportLogicImpl(get(), get(), get(), logger) }
	single<RoleLogic> { RoleLogicImpl(get()) }
	single<StorageLogic> { StorageLogicImpl(get()) }
	single<TagLogic> { TagLogicImpl(get(), get()) }
	single<UserLogic> { UserLogicImpl(get(), get(), get(), get()) }
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
		modules(applicationModules(dbCredentials, jwtConfig, mailerConfig, log))
	}
}
