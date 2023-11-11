import io.ktor.plugin.features.*

val ktor_version: String by project
val kotlin_version: String by project
val logback_version: String by project
val koin_ktor_version: String by project

plugins {
	kotlin("jvm") version "1.9.10"
	id("io.ktor.plugin") version "2.3.4"
	id("org.jetbrains.kotlin.plugin.serialization") version "1.9.10"
}

group = "org.cdb"
version = "0.0.1"

application {
	mainClass.set("org.cdb.labwitch.LabWitchAppKt")
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("io.ktor:ktor-server-core-jvm")
	implementation("io.ktor:ktor-server-cors-jvm")
	implementation("io.ktor:ktor-server-content-negotiation-jvm")
	implementation("io.ktor:ktor-serialization-jackson-jvm")
	implementation("io.ktor:ktor-server-call-logging-jvm")
	implementation("io.ktor:ktor-server-cio-jvm")
	implementation("io.ktor:ktor-serialization-kotlinx-json")

	implementation("io.ktor:ktor-client-cio-jvm")
	implementation("io.ktor:ktor-client-core-jvm")
	implementation("io.ktor:ktor-client-content-negotiation-jvm")
	implementation("io.ktor:ktor-server-auth")
	implementation("io.ktor:ktor-server-auth-jwt")

	implementation("org.mongodb:mongodb-driver-kotlin-coroutine:4.11.0")
	implementation("org.mindrot:jbcrypt:0.4")

	implementation("io.insert-koin:koin-ktor:$koin_ktor_version")
	implementation("io.insert-koin:koin-logger-slf4j:$koin_ktor_version")

	implementation("ch.qos.logback:logback-classic:$logback_version")

	testImplementation("io.ktor:ktor-server-tests-jvm")
	testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version")
}

tasks.withType<Test> {
	useJUnitPlatform()
}
