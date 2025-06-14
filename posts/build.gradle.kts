import buildsrc.convention.*

plugins {
    kotlin("jvm")
    kotlin("kapt")
    kotlin("plugin.spring")
    id("org.springframework.boot")
    id("io.spring.dependency-management")
    id("org.liquibase.gradle")
    id("com.google.protobuf")
}

group = "com.nezuko"
version = "unspecified"

repositories {
    mavenCentral()
    gradlePluginPortal()
}
configureSpring()
configureKotlin()

configureLiquibase(
    module = "posts",
    dbUrl = "jdbc:postgresql://localhost:5433/posts"
)


configureProtobuf(module = "posts")

dependencies {
    includeKotlin()
    includePostgresql()
    implementation(project(":utils"))

    testImplementation(kotlin("test"))
}


tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict")
    }
}