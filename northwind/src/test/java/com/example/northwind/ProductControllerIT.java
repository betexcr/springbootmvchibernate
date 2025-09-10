package com.example.northwind;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ProductControllerIT {
	@LocalServerPort
	int port;
	@Autowired
	TestRestTemplate restTemplate;

	@Test
	void listProducts_shouldReturnOk() {
		ResponseEntity<String> response = restTemplate.getForEntity("http://localhost:" + port + "/api/products", String.class);
		assert(response.getStatusCode().is2xxSuccessful());
	}
}
