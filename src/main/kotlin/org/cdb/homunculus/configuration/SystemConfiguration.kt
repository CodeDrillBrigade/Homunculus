package org.cdb.homunculus.configuration

import io.ktor.server.application.Application
import io.ktor.server.application.ApplicationStarted
import io.ktor.server.application.createApplicationPlugin
import io.ktor.server.application.hooks.MonitoringEvent
import io.ktor.server.application.install
import io.ktor.server.application.log
import kotlinx.coroutines.flow.count
import kotlinx.coroutines.runBlocking
import org.cdb.homunculus.annotations.Index
import org.cdb.homunculus.components.PasswordEncoder
import org.cdb.homunculus.dao.GenericDao
import org.cdb.homunculus.dao.RoleDao
import org.cdb.homunculus.dao.UserDao
import org.cdb.homunculus.models.User
import org.cdb.homunculus.models.embed.Role
import org.cdb.homunculus.models.identifiers.EntityId
import org.cdb.homunculus.models.security.AuthToken
import org.cdb.homunculus.models.security.Permissions
import org.koin.core.annotation.KoinInternalApi
import org.koin.core.definition.Kind
import org.koin.ktor.plugin.koin
import java.util.Date
import java.util.UUID
import kotlin.reflect.full.functions
import kotlin.reflect.full.isSubclassOf
import kotlin.time.Duration.Companion.days

private const val FIXED_UUID = "aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa"

@OptIn(KoinInternalApi::class)
val databaseInitializationPlugin =
	createApplicationPlugin("DatabaseInitializer") {
		on(MonitoringEvent(ApplicationStarted)) { application ->
			application.log.info("Starting database configuration")
			application.koin {
				val daoList =
					koin.instanceRegistry.instances.map { it.value.beanDefinition }
						.filter { it.kind == Kind.Singleton }
						.filter { it.primaryType.isSubclassOf(GenericDao::class) }
						.map { koin.get(clazz = it.primaryType, qualifier = null, parameters = null) as GenericDao<*> }
				runBlocking {
					daoList.forEach { dao ->
						dao::class.functions.filter { function ->
							function.annotations.any { it.annotationClass == Index::class }
						}.flatMap { function ->
							function.annotations.filterIsInstance<Index>()
						}.forEach { index ->
							dao.initIndex(index.property, index.name, index.unique)?.let {
								application.log.info("Created index $it on ${dao::class.simpleName}")
							}
						}
					}
				}
			}
			application.log.info("Database configuration completed")
		}
	}

val systemInitializationPlugin =
	createApplicationPlugin("SystemInitializer") {
		on(MonitoringEvent(ApplicationStarted)) { application ->
			application.log.info("Starting system initialization")
			application.koin {
				runBlocking {
					val roleDao = koin.get<RoleDao>()
					if (roleDao.getById(EntityId(FIXED_UUID)) == null) {
						application.log.info("Admin role not found, creating")
						val adminRole =
							Role(
								id = EntityId(FIXED_UUID),
								name = "Admin",
								description = "Default admin role, cannot be modified or deleted",
								permissions = setOf(Permissions.ADMIN),
							)
						roleDao.save(adminRole)
					}

					val userDao = koin.get<UserDao>()
					if (userDao.get().count() == 0) {
						application.log.info("No users found in the database, creating the default admin")
						val passwordEncoder = koin.get<PasswordEncoder>()
						val temporaryPassword =
							application.environment.config.propertyOrNull("system.defaultAdminToken")?.getString()
								?: UUID.randomUUID().toString()
						val adminUser =
							User(
								id = EntityId(FIXED_UUID),
								username = "admin",
								passwordHash = null,
								name = "admin",
								surname = "admin",
								roles = setOf(EntityId(FIXED_UUID)),
								authenticationTokens =
									mapOf(
										"default" to
											AuthToken(
												token = passwordEncoder.hashAndSaltPassword(temporaryPassword),
												expirationDate = Date(System.currentTimeMillis() + 1L.days.inWholeMilliseconds),
											),
									),
							)
						userDao.save(adminUser)
						application.log.info("Created admin with username: admin and temporaryToken: $temporaryPassword")
					}
				}
			}
		}
	}

fun Application.initialization() {
	install(databaseInitializationPlugin)
	install(systemInitializationPlugin)
}
