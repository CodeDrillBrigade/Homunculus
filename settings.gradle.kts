pluginManagement {
	repositories {
		mavenLocal()
		gradlePluginPortal()
		mavenCentral()
	}
}

dependencyResolutionManagement {
	repositoriesMode.set(RepositoriesMode.PREFER_SETTINGS)
	repositories {
		mavenLocal()
		mavenCentral()
	}

	versionCatalogs {
		create("libs") {
			from(files("./libs.versions.toml"))
		}
	}
}

rootProject.name = "LabWitchery-backend"
