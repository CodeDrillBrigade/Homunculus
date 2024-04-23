package org.cdb.labwitch.configuration

import io.ktor.server.application.Application
import io.ktor.server.application.ApplicationStarted
import io.ktor.server.application.createApplicationPlugin
import io.ktor.server.application.hooks.MonitoringEvent
import io.ktor.server.application.install
import io.ktor.server.application.log
import kotlinx.coroutines.runBlocking
import org.cdb.labwitch.annotations.Index
import org.cdb.labwitch.dao.GenericDao
import org.koin.core.annotation.KoinInternalApi
import org.koin.core.definition.Kind
import org.koin.ktor.plugin.koin
import kotlin.reflect.full.functions
import kotlin.reflect.full.isSubclassOf

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
							dao.initIndex(index.property, index.name)?.let {
								application.log.info("Created index $it on ${dao::class.simpleName}")
							}
						}
					}
				}
			}
			application.log.info("Database configuration completed")
		}
	}

fun Application.initialization() {
	install(databaseInitializationPlugin)
}
