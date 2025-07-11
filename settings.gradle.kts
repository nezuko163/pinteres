// The settings file is the entry point of every Gradle build.
// Its primary purpose is to define the subprojects.
// It is also used for some aspects of project-wide configuration, like managing plugins, dependencies, etc.
// https://docs.gradle.org/current/userguide/settings_file_basics.html

dependencyResolutionManagement {
    // Use Maven Central as the default repository (where Gradle will download dependencies) in all subprojects.
    @Suppress("UnstableApiUsage")
    repositories {
        mavenCentral()
    }
}

pluginManagement {
    plugins {
        kotlin("jvm") version "2.1.20" apply false
        id("org.jetbrains.kotlin.plugin.spring") version "2.1.20" apply false
        kotlin("kapt") version "2.1.20" apply false
        id("org.liquibase.gradle") version "2.2.0" apply false
        id("org.springframework.boot") version "3.5.0" apply false
        id("io.spring.dependency-management") version "1.1.7" apply false
        id("com.google.protobuf") version "0.9.4" apply false
    }
}

plugins {
    // Use the Foojay Toolchains plugin to automatically download JDKs required by subprojects.
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}

// Include the `app` and `utils` subprojects in the build.
// If there are changes in only one of the projects, Gradle will rebuild only the one that has changed.
// Learn more about structuring projects with Gradle - https://docs.gradle.org/8.7/userguide/multi_project_builds.html
include(":app")
include(":utils")

rootProject.name = "pinteres"
include("auth")
include("users")
include("posts")