package com.example.northwind.integration;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@ActiveProfiles("test")
public abstract class BaseIntegrationTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15")
            .withDatabaseName("northwind_test")
            .withUsername("test")
            .withPassword("test");

    @LocalServerPort
    protected int port;

    protected TestRestTemplate restTemplate = new TestRestTemplate();

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    protected String getBaseUrl() {
        return "http://localhost:" + port;
    }

    protected String getApiUrl(String endpoint) {
        return getBaseUrl() + "/api" + endpoint;
    }

    protected HttpHeaders createAuthHeaders(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        headers.set("Content-Type", "application/json");
        return headers;
    }

    protected String loginAndGetToken(String username, String password) {
        String loginUrl = getApiUrl("/auth/login");
        String loginBody = String.format("{\"username\":\"%s\",\"password\":\"%s\"}", username, password);
        
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        HttpEntity<String> entity = new HttpEntity<>(loginBody, headers);
        
        ResponseEntity<String> response = restTemplate.exchange(
            loginUrl, HttpMethod.POST, entity, String.class);
        
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        
        // Extract token from response (assuming it returns {"token":"..."})
        String body = response.getBody();
        int tokenStart = body.indexOf("\"token\":\"") + 9;
        int tokenEnd = body.indexOf("\"", tokenStart);
        return body.substring(tokenStart, tokenEnd);
    }

    protected void assertResponseStatus(ResponseEntity<?> response, int expectedStatus) {
        assertEquals(expectedStatus, response.getStatusCode().value(), 
            "Expected status " + expectedStatus + " but got " + response.getStatusCode().value() + 
            ". Response body: " + response.getBody());
    }
}

