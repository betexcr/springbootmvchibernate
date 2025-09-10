package com.example.northwind;

import com.example.northwind.integration.BaseIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

class ProductControllerIT extends BaseIntegrationTest {

	@Test
	void listProducts_shouldReturnOk() {
		ResponseEntity<String> response = restTemplate.getForEntity(getApiUrl("/products"), String.class);
		assert(response.getStatusCode().is2xxSuccessful());
	}
}
