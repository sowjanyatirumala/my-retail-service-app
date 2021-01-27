package com.myretail.myretailservicewebapp.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import springfox.documentation.builders.ApiInfoBuilder
import springfox.documentation.builders.PathSelectors
import springfox.documentation.builders.RequestHandlerSelectors
import springfox.documentation.service.ApiInfo
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spring.web.plugins.Docket
import springfox.documentation.swagger2.annotations.EnableSwagger2

@Configuration
@EnableSwagger2
class SwaggerConfig {

    @Value('${swagger.enabled}')
    Boolean swaggerEnabled

    @Value('${swagger.title}')
    String swaggerTitle

    @Value('${swagger.description}')
    String swaggerDescription

    ApiInfo apiInfo() {

        return new ApiInfoBuilder()
                .title(swaggerTitle)
                .description(swaggerDescription)
                .build()
    }

    @Bean
    Docket productApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .useDefaultResponseMessages(false)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.myretail.myretailservicewebapp.endpoints"))
                .paths(PathSelectors.ant('/api/**'))
                .build()
                .enable(swaggerEnabled)
                .apiInfo(apiInfo())
    }
}
