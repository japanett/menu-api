package com.japanet.menuapi

import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.runApplication
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters
import org.springframework.data.jpa.repository.config.EnableJpaAuditing

@EnableJpaAuditing
@SpringBootApplication
@EnableAutoConfiguration
@EntityScan(basePackageClasses = [MenuapiApplication::class, Jsr310JpaConverters::class])
class MenuapiApplication

fun main(args: Array<String>) {
	runApplication<MenuapiApplication>(*args)
}
