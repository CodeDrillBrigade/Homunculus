import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	kotlin("jvm") version "1.9.10"
	alias(libs.plugins.ktorPlugin)
	alias(libs.plugins.kotlinxSerialization)
	alias(libs.plugins.ktlint)
}

group = "org.cdb"
version = "0.0.1"

/* ******************
tasks.withType<KotlinCompile> {
	dependsOn("ktlintFormat")
}
***********************/

application {
	mainClass.set("org.cdb.labwitch.LabWitchAppKt")
}

dependencies {
	implementation(libs.bundles.ktor)
	implementation(libs.bundles.koin)
	implementation(libs.bundles.mongo)
	implementation(libs.logback)
	implementation(libs.jbCrypt)
}

tasks.withType<Test> {
	useJUnitPlatform()
}
