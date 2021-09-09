package com.etaskify.ms.auth.config


import com.google.common.collect.ImmutableSet
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import springfox.documentation.builders.PathSelectors
import springfox.documentation.builders.RequestHandlerSelectors
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spring.web.plugins.Docket
import springfox.documentation.swagger2.annotations.EnableSwagger2

@Configuration
@EnableSwagger2
class SwaggerConfig {

    @Bean
    fun swaggerSettings(): Docket {
        return Docket(DocumentationType.SWAGGER_2)
                .produces(ImmutableSet.of(APPLICATION_JSON_VALUE))
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.etaskify"))
                .paths(PathSelectors.any())
                .build()
    }

}
