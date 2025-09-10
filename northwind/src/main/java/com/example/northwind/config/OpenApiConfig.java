package com.example.northwind.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .servers(List.of(
                    new Server().url("https://northwind-api-815129345561.us-central1.run.app").description("Production server"),
                    new Server().url("http://localhost:8080").description("Local development server")
                ))
                .info(new Info()
                        .title("Northwind API Docs")
                        .description("RESTful API for the Northwind database - A comprehensive e-commerce API with products, categories, suppliers, customers, employees, and orders management.")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Northwind API Team")
                                .email("support@northwind-api.com")
                                .url("https://northwind-api-815129345561.us-central1.run.app"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")));
    }
}
