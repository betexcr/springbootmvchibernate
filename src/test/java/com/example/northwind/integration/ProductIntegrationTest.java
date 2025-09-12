package com.example.northwind.integration;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

public class ProductIntegrationTest extends BaseIntegrationTest {

    private String adminToken;
    private String staffToken;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        adminToken = loginAndGetToken("admin", "x");
        staffToken = loginAndGetToken("staff", "x");
        objectMapper = new ObjectMapper();
    }

    @Test
    void testGetAllProducts_ShouldReturnProducts() throws Exception {
        ResponseEntity<String> response = restTemplate.getForEntity(
            getApiUrl("/products"), String.class);
        
        assertResponseStatus(response, 200);
        
        JsonNode jsonNode = objectMapper.readTree(response.getBody());
        assertTrue(jsonNode.has("content"));
        assertTrue(jsonNode.get("content").isArray());
        assertTrue(jsonNode.get("content").size() > 0);
    }

    @Test
    void testGetProductById_ShouldReturnProduct() throws Exception {
        ResponseEntity<String> response = restTemplate.getForEntity(
            getApiUrl("/products/1"), String.class);
        
        assertResponseStatus(response, 200);
        
        JsonNode jsonNode = objectMapper.readTree(response.getBody());
        assertEquals(1, jsonNode.get("id").asInt());
        assertTrue(jsonNode.has("productName"));
        assertTrue(jsonNode.has("unitPrice"));
    }

    @Test
    void testGetProductById_NonExistent_ShouldReturn404() {
        ResponseEntity<String> response = restTemplate.getForEntity(
            getApiUrl("/products/99999"), String.class);
        
        assertResponseStatus(response, 404);
    }

    @Test
    void testCreateProduct_AsAdmin_ShouldSucceed() throws Exception {
        String productJson = """
            {
                "productName": "Test Product",
                "supplierId": 1,
                "categoryId": 1,
                "quantityPerUnit": "24 boxes",
                "unitPrice": 19.99,
                "unitsInStock": 100,
                "unitsOnOrder": 0,
                "reorderLevel": 10,
                "discontinued": false
            }
            """;

        HttpEntity<String> entity = new HttpEntity<>(productJson, createAuthHeaders(adminToken));
        ResponseEntity<String> response = restTemplate.exchange(
            getApiUrl("/products"), HttpMethod.POST, entity, String.class);
        
        assertResponseStatus(response, 201);
        
        JsonNode jsonNode = objectMapper.readTree(response.getBody());
        assertTrue(jsonNode.has("id"));
        assertEquals("Test Product", jsonNode.get("productName").asText());
        assertEquals(19.99, jsonNode.get("unitPrice").asDouble(), 0.01);
    }

    @Test
    void testCreateProduct_AsStaff_ShouldSucceed() throws Exception {
        String productJson = """
            {
                "productName": "Staff Product",
                "supplierId": 1,
                "categoryId": 1,
                "quantityPerUnit": "12 boxes",
                "unitPrice": 15.99,
                "unitsInStock": 50,
                "unitsOnOrder": 0,
                "reorderLevel": 5,
                "discontinued": false
            }
            """;

        HttpEntity<String> entity = new HttpEntity<>(productJson, createAuthHeaders(staffToken));
        ResponseEntity<String> response = restTemplate.exchange(
            getApiUrl("/products"), HttpMethod.POST, entity, String.class);
        
        assertResponseStatus(response, 201);
        
        JsonNode jsonNode = objectMapper.readTree(response.getBody());
        assertEquals("Staff Product", jsonNode.get("productName").asText());
    }

    @Test
    void testCreateProduct_WithoutAuth_ShouldReturn401() {
        String productJson = """
            {
                "productName": "Unauthorized Product",
                "supplierId": 1,
                "categoryId": 1,
                "quantityPerUnit": "1 box",
                "unitPrice": 10.00,
                "unitsInStock": 10,
                "unitsOnOrder": 0,
                "reorderLevel": 1,
                "discontinued": false
            }
            """;

        HttpEntity<String> entity = new HttpEntity<>(productJson);
        ResponseEntity<String> response = restTemplate.exchange(
            getApiUrl("/products"), HttpMethod.POST, entity, String.class);
        
        assertResponseStatus(response, 401);
    }

    @Test
    void testUpdateProduct_AsAdmin_ShouldSucceed() throws Exception {
        // First create a product
        String createJson = """
            {
                "productName": "Original Product",
                "supplierId": 1,
                "categoryId": 1,
                "quantityPerUnit": "24 boxes",
                "unitPrice": 19.99,
                "unitsInStock": 100,
                "unitsOnOrder": 0,
                "reorderLevel": 10,
                "discontinued": false
            }
            """;

        HttpEntity<String> createEntity = new HttpEntity<>(createJson, createAuthHeaders(adminToken));
        ResponseEntity<String> createResponse = restTemplate.exchange(
            getApiUrl("/products"), HttpMethod.POST, createEntity, String.class);
        
        JsonNode createdProduct = objectMapper.readTree(createResponse.getBody());
        int productId = createdProduct.get("id").asInt();

        // Update the product
        String updateJson = """
            {
                "productName": "Updated Product",
                "supplierId": 1,
                "categoryId": 1,
                "quantityPerUnit": "12 boxes",
                "unitPrice": 25.99,
                "unitsInStock": 50,
                "unitsOnOrder": 10,
                "reorderLevel": 5,
                "discontinued": false
            }
            """;

        HttpEntity<String> updateEntity = new HttpEntity<>(updateJson, createAuthHeaders(adminToken));
        ResponseEntity<String> updateResponse = restTemplate.exchange(
            getApiUrl("/products/" + productId), HttpMethod.PUT, updateEntity, String.class);
        
        assertResponseStatus(updateResponse, 200);
        
        JsonNode updatedProduct = objectMapper.readTree(updateResponse.getBody());
        assertEquals("Updated Product", updatedProduct.get("productName").asText());
        assertEquals(25.99, updatedProduct.get("unitPrice").asDouble(), 0.01);
        assertEquals(50, updatedProduct.get("unitsInStock").asInt());
    }

    @Test
    void testDeleteProduct_AsAdmin_ShouldSucceed() throws Exception {
        // First create a product
        String createJson = """
            {
                "productName": "To Delete Product",
                "supplierId": 1,
                "categoryId": 1,
                "quantityPerUnit": "1 box",
                "unitPrice": 10.00,
                "unitsInStock": 10,
                "unitsOnOrder": 0,
                "reorderLevel": 1,
                "discontinued": false
            }
            """;

        HttpEntity<String> createEntity = new HttpEntity<>(createJson, createAuthHeaders(adminToken));
        ResponseEntity<String> createResponse = restTemplate.exchange(
            getApiUrl("/products"), HttpMethod.POST, createEntity, String.class);
        
        JsonNode createdProduct = objectMapper.readTree(createResponse.getBody());
        int productId = createdProduct.get("id").asInt();

        // Delete the product
        HttpEntity<String> deleteEntity = new HttpEntity<>(createAuthHeaders(adminToken));
        ResponseEntity<String> deleteResponse = restTemplate.exchange(
            getApiUrl("/products/" + productId), HttpMethod.DELETE, deleteEntity, String.class);
        
        assertResponseStatus(deleteResponse, 204);

        // Verify product is deleted
        ResponseEntity<String> getResponse = restTemplate.getForEntity(
            getApiUrl("/products/" + productId), String.class);
        assertResponseStatus(getResponse, 404);
    }

    @Test
    void testSearchProducts_ShouldReturnFilteredResults() throws Exception {
        ResponseEntity<String> response = restTemplate.getForEntity(
            getApiUrl("/products?q=chai"), String.class);
        
        assertResponseStatus(response, 200);
        
        JsonNode jsonNode = objectMapper.readTree(response.getBody());
        assertTrue(jsonNode.has("content"));
        assertTrue(jsonNode.get("content").isArray());
        
        // Should find products containing "chai"
        boolean foundChai = false;
        for (JsonNode product : jsonNode.get("content")) {
            String productName = product.get("productName").asText().toLowerCase();
            if (productName.contains("chai")) {
                foundChai = true;
                break;
            }
        }
        assertTrue(foundChai, "Should find products containing 'chai'");
    }

    @Test
    void testGetProductsByCategory_ShouldReturnFilteredResults() throws Exception {
        ResponseEntity<String> response = restTemplate.getForEntity(
            getApiUrl("/products?categoryId=1"), String.class);
        
        assertResponseStatus(response, 200);
        
        JsonNode jsonNode = objectMapper.readTree(response.getBody());
        assertTrue(jsonNode.has("content"));
        assertTrue(jsonNode.get("content").isArray());
        
        // All products should belong to category 1
        for (JsonNode product : jsonNode.get("content")) {
            assertEquals(1, product.get("categoryId").asInt());
        }
    }

    @Test
    void testGetProductsBySupplier_ShouldReturnFilteredResults() throws Exception {
        ResponseEntity<String> response = restTemplate.getForEntity(
            getApiUrl("/products?supplierId=1"), String.class);
        
        assertResponseStatus(response, 200);
        
        JsonNode jsonNode = objectMapper.readTree(response.getBody());
        assertTrue(jsonNode.has("content"));
        assertTrue(jsonNode.get("content").isArray());
        
        // All products should belong to supplier 1
        for (JsonNode product : jsonNode.get("content")) {
            assertEquals(1, product.get("supplierId").asInt());
        }
    }

    @Test
    void testGetProductsWithPagination_ShouldReturnPaginatedResults() throws Exception {
        ResponseEntity<String> response = restTemplate.getForEntity(
            getApiUrl("/products?page=0&size=5"), String.class);
        
        assertResponseStatus(response, 200);
        
        JsonNode jsonNode = objectMapper.readTree(response.getBody());
        assertTrue(jsonNode.has("content"));
        assertTrue(jsonNode.has("totalElements"));
        assertTrue(jsonNode.has("totalPages"));
        assertTrue(jsonNode.has("size"));
        assertTrue(jsonNode.has("number"));
        
        assertEquals(5, jsonNode.get("size").asInt());
        assertEquals(0, jsonNode.get("number").asInt());
        assertTrue(jsonNode.get("content").size() <= 5);
    }
}

