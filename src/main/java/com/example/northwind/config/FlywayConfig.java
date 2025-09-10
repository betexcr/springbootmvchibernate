package com.example.northwind.config;

import org.flywaydb.core.Flyway;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FlywayConfig {
	@Bean
	ApplicationRunner migrate(Flyway flyway) {
		return args -> flyway.migrate();
	}
}
