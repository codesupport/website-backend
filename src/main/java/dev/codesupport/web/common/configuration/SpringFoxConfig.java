package dev.codesupport.web.common.configuration;

import com.google.common.annotations.VisibleForTesting;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collections;

/**
 * Configures Swagger API integration
 */
//unused - Called dynamically by the spring framework.
@SuppressWarnings("unused")
@Configuration
@EnableSwagger2
public class SpringFoxConfig {

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build()
                .apiInfo(apiInfo());
    }

    @VisibleForTesting
    ApiInfo apiInfo() {
        return new ApiInfo(
                "CodeSupport.dev API",
                "Supported endpoints for the CodeSupport.dev website backend API",
                "v1",
                "Terms of service",
                new Contact("@LamboCreeper#6510", "www.codesupport.dev", ""),
                "",
                "",
                Collections.emptyList()
        );
    }
}