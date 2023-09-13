package com.japanet.menuapi.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import springfox.documentation.builders.ApiInfoBuilder
import springfox.documentation.builders.PathSelectors
import springfox.documentation.builders.RequestHandlerSelectors
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spring.web.plugins.Docket
import springfox.documentation.swagger2.annotations.EnableSwagger2

@Configuration
@EnableSwagger2
class SwaggerConfig {

    companion object {
        const val TITLE = "Menu-API"
    }

    @Bean
    open fun v1(): Docket = Docket(DocumentationType.SWAGGER_2)
        .groupName("v1")
        .select()
        .apis(RequestHandlerSelectors.basePackage("com.japanet.menuapi.controller.v1"))
        .paths(PathSelectors.any()).build()
        .apiInfo(ApiInfoBuilder()
            .title(TITLE)
            .build()
        )
}
