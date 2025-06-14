package buildsrc.convention

import com.google.protobuf.gradle.ProtobufExtension
import com.google.protobuf.gradle.id
import com.google.protobuf.gradle.proto
import gradle.kotlin.dsl.accessors._ffcf889acdea48f400c29ebf21a196f7.implementation
import gradle.kotlin.dsl.accessors._ffcf889acdea48f400c29ebf21a196f7.runtimeOnly
import org.gradle.api.Project
import org.gradle.api.tasks.SourceSetContainer
import org.gradle.kotlin.dsl.DependencyHandlerScope
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.liquibase.gradle.LiquibaseExtension


fun Project.configureKotlin() {
    pluginManager.apply("org.jetbrains.kotlin.jvm")
    pluginManager.apply("org.jetbrains.kotlin.kapt")

    dependencies {
        add("implementation", "com.fasterxml.jackson.module:jackson-module-kotlin")
        add("implementation", "io.projectreactor.kotlin:reactor-kotlin-extensions")
        add("implementation", "org.jetbrains.kotlin:kotlin-reflect")
        add("implementation", "org.jetbrains.kotlinx:kotlinx-coroutines-reactor")
        add("implementation", "io.github.microutils:kotlin-logging-jvm:3.0.5")
    }
}

fun Project.configureSpring() {
    pluginManager.apply("org.springframework.boot")
    pluginManager.apply("org.jetbrains.kotlin.plugin.spring")
    pluginManager.apply("io.spring.dependency-management")

    dependencies {
        add("implementation", "org.springframework.boot:spring-boot-configuration-processor")
        add("implementation", "org.springframework.boot:spring-boot-starter-webflux")
        add("kapt", "org.springframework.boot:spring-boot-configuration-processor")
    }
}

fun DependencyHandlerScope.includeKotlin() {
    implementation("org.springframework.boot:spring-boot-configuration-processor")
    add("kapt", "org.springframework.boot:spring-boot-configuration-processor")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")
}


fun DependencyHandlerScope.includeJwt() {
    implementation("io.jsonwebtoken:jjwt-api:0.12.6")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:0.12.6")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.12.6")
}

fun DependencyHandlerScope.includeSecurity() {
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.security:spring-security-config")
    implementation("org.springframework.security:spring-security-crypto")
}

fun DependencyHandlerScope.includePostgresql() {
    implementation("org.postgresql:postgresql")
    implementation("org.springframework.data:spring-data-r2dbc")
    implementation("org.springframework.boot:spring-boot-starter-data-r2dbc")
    implementation("org.postgresql:r2dbc-postgresql:1.0.7.RELEASE")
}


fun Project.configureLiquibase(
    module: String,
    changeLogFile: String = "src/main/resources/migration/db.changelog-master.yaml",
    dbUrl: String,
    username: String = "admin",
    password: String = "1234",
    driver: String = "org.postgresql.Driver",
) {
    pluginManager.apply("org.liquibase.gradle")
    plugins.apply("org.liquibase.gradle")

    dependencies {
        add("liquibaseRuntime", "org.liquibase:liquibase-core:4.32.0")
        add("liquibaseRuntime", "org.liquibase:liquibase-groovy-dsl:4.0.1")
        add("liquibaseRuntime", "org.postgresql:postgresql")
        add("liquibaseRuntime", "info.picocli:picocli:4.7.7")
    }

    println("Applied Liquibase plugin: ${pluginManager.hasPlugin("org.liquibase.gradle")}")

    extensions.configure<LiquibaseExtension> {
        activities.register("main") {
            this.arguments = mapOf(
                "changelogFile" to "$module/$changeLogFile",
                "url" to dbUrl,
                "username" to username,
                "password" to password,
                "driver" to driver
            )
        }
        runList = "main"
    }
}

fun Project.configureProtobuf(module: String) {
    pluginManager.apply("com.google.protobuf")

    // Подключаем зависимости
    dependencies {
        add("implementation", "com.google.protobuf:protobuf-kotlin:4.31.1")
        add("implementation", "com.google.protobuf:protobuf-java:4.31.1")
        add("implementation", "io.grpc:grpc-kotlin-stub:1.4.3")
        add("implementation", "io.grpc:grpc-services")
        add("implementation", "io.grpc:grpc-netty-shaded:1.73.0")
        add("implementation", "net.devh:grpc-spring-boot-starter:3.1.0.RELEASE")
    }

    extensions.configure<SourceSetContainer> {
        named("main") {
            proto {
                srcDir("../proto/$module")  // путь к proto файлам
                srcDirs.forEach { println("add proto: $it") }
            }
        }
    }


    extensions.configure<ProtobufExtension> {
        protoc {
            artifact = "com.google.protobuf:protoc:4.31.1"
        }
        plugins {
            id("grpc") {
                artifact = "io.grpc:protoc-gen-grpc-java:1.73.0"
            }
            id("grpckt") {
                artifact = "io.grpc:protoc-gen-grpc-kotlin:1.4.3:jdk8@jar"
            }
        }


        generateProtoTasks {
            all().forEach {
                it.plugins {
                    id("grpc") {
                        option("jakarta_omit")
                        option("@generated=omit")
                    }
                    id("grpckt") {

                    }
                }
                it.builtins {
                    id("kotlin")
                }
            }
        }
    }
}