import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	kotlin("jvm") version "1.9.10"
	alias(libs.plugins.ktorPlugin)
	alias(libs.plugins.kotlinxSerialization)
	alias(libs.plugins.ktlint)
}

group = "org.cdb"
version = "0.0.1"

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs = listOf("-Xjsr305=strict")
		jvmTarget = "19"
	}
}

java {
	sourceCompatibility = JavaVersion.VERSION_19
	targetCompatibility = JavaVersion.VERSION_19
}

tasks.withType<KotlinCompile> {
	dependsOn("ktlintFormat")
}

application {
	mainClass.set("org.cdb.homunculus.HomunculusAppKt")
}

dependencies {
	implementation(libs.bundles.ktor)
	implementation(libs.bundles.koin)
	implementation(libs.bundles.mongo)
	implementation(libs.logback)
	implementation(libs.jbCrypt)
	implementation(libs.kotlinxDatetime)
	implementation(libs.krontab)
	implementation(libs.caffeine)
	implementation(libs.tika)
	implementation(libs.apachePoi)
	implementation(libs.apachePoiOoxml)
}

tasks.withType<Test> {
	useJUnitPlatform()
}
