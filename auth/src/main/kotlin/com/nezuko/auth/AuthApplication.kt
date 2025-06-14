package com.nezuko.auth

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication

@SpringBootApplication
@ConfigurationPropertiesScan("com.nezuko.auth.properties")
class AuthApplication

fun main(args: Array<String>) {
	runApplication<AuthApplication>(*args)
}
