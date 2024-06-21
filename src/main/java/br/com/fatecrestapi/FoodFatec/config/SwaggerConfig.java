package br.com.fatecrestapi.FoodFatec.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("foodfatec-rest-api")
                .packagesToScan("br.com.fatecrestapi.FoodFatec.controller")
                .build();
    }

    @Bean
    public OpenAPI springShopOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("Authentication API")
                        .description("Authentication API for the Food Fatec Project.")
                        .version("1.0")
                        .license(new License().name("Apache 2.0").url("http://springdoc.org")));
    }
}