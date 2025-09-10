package com.example.northwind.integration;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

public class OrderDetailIntegrationTest extends BaseIntegrationTest {

    private String adminToken;
    private String staffToken;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        adminToken = loginAndGetToken("admin", "admin123");
        staffToken = loginAndGetToken("staff", "staff123");
        objectMapper = new ObjectMapper();
    }

    @Test
    void testGetAllOrderDetails_AsStaff_ShouldReturnOrderDetails() throws Exception {
        HttpEntity<String> entity = new HttpEntity<>(createAuthHeaders(staffToken));
        ResponseEntity<String> response = restTemplate.exchange(
            getApiUrl("/orders/10248/details"), HttpMethod.GET, entity, String.class);
        
        assertResponseStatus(response, 200);
        
        JsonNode jsonNode = objectMapper.readTree(response.getBody());
        assertTrue(jsonNode.has("content"));
        assertTrue(jsonNode.get("content").isArray());
        assertTrue(jsonNode.get("content").size() > 0);
    }

    @Test
    void testGetAllOrderDetails_AsAdmin_ShouldReturnOrderDetails() throws Exception {
        HttpEntity<String> entity = new HttpEntity<>(createAuthHeaders(adminToken));
        ResponseEntity<String> response = restTemplate.exchange(
            getApiUrl("/orders/10248/details"), HttpMethod.GET, entity, String.class);
        
        assertResponseStatus(response, 200);
        
        JsonNode jsonNode = objectMapper.readTree(response.getBody());
        assertTrue(jsonNode.has("content"));
        assertTrue(jsonNode.get("content").isArray());
        assertTrue(jsonNode.get("content").size() > 0);
    }

    @Test
    void testGetAllOrderDetails_WithoutAuth_ShouldReturn401() {
        ResponseEntity<String> response = restTemplate.getForEntity(
            getApiUrl("/orders/10248/details"), String.class);
        
        assertResponseStatus(response, 401);
    }

    @Test
    void testGetOrderDetailById_AsStaff_ShouldReturnOrderDetail() throws Exception {
        HttpEntity<String> entity = new HttpEntity<>(createAuthHeaders(staffToken));
        ResponseEntity<String> response = restTemplate.exchange(
            getApiUrl("/orders/10248/details/11"), HttpMethod.GET, entity, String.class);
        
        assertResponseStatus(response, 200);
        
        JsonNode jsonNode = objectMapper.readTree(response.getBody());
        assertEquals(10248, jsonNode.get("orderId").asInt());
        assertEquals(11, jsonNode.get("productId").asInt());
        assertTrue(jsonNode.has("unitPrice"));
        assertTrue(jsonNode.has("quantity"));
        assertTrue(jsonNode.has("discount"));
    }

    @Test
    void testGetOrderDetailById_NonExistent_ShouldReturn404() {
        HttpEntity<String> entity = new HttpEntity<>(createAuthHeaders(staffToken));
        ResponseEntity<String> response = restTemplate.exchange(
            getApiUrl("/orders/10248/details/99999"), HttpMethod.GET, entity, String.class);
        
        assertResponseStatus(response, 404);
    }

    @Test
    void testCreateOrderDetail_AsAdmin_ShouldSucceed() throws Exception {
        String orderDetailJson = """
            {
                "productId": 1,
                "unitPrice": 15.50,
                "quantity": 10,
                "discount": 0.05
            }
            """;

        HttpEntity<String> entity = new HttpEntity<>(orderDetailJson, createAuthHeaders(adminToken));
        ResponseEntity<String> response = restTemplate.exchange(
            getApiUrl("/orders/10248/details"), HttpMethod.POST, entity, String.class);
        
        assertResponseStatus(response, 201);
        
        JsonNode jsonNode = objectMapper.readTree(response.getBody());
        assertEquals(10248, jsonNode.get("orderId").asInt());
        assertEquals(1, jsonNode.get("productId").asInt());
        assertEquals(15.50, jsonNode.get("unitPrice").asDouble(), 0.01);
        assertEquals(10, jsonNode.get("quantity").asInt());
        assertEquals(0.05, jsonNode.get("discount").asDouble(), 0.01);
    }

    @Test
    void testCreateOrderDetail_AsStaff_ShouldSucceed() throws Exception {
        String orderDetailJson = """
            {
                "productId": 2,
                "unitPrice": 25.75,
                "quantity": 5,
                "discount": 0.10
            }
            """;

        HttpEntity<String> entity = new HttpEntity<>(orderDetailJson, createAuthHeaders(staffToken));
        ResponseEntity<String> response = restTemplate.exchange(
            getApiUrl("/orders/10248/details"), HttpMethod.POST, entity, String.class);
        
        assertResponseStatus(response, 201);
        
        JsonNode jsonNode = objectMapper.readTree(response.getBody());
        assertEquals(10248, jsonNode.get("orderId").asInt());
        assertEquals(2, jsonNode.get("productId").asInt());
        assertEquals(25.75, jsonNode.get("unitPrice").asDouble(), 0.01);
        assertEquals(5, jsonNode.get("quantity").asInt());
        assertEquals(0.10, jsonNode.get("discount").asDouble(), 0.01);
    }

    @Test
    void testCreateOrderDetail_WithoutAuth_ShouldReturn401() {
        String orderDetailJson = """
            {
                "productId": 3,
                "unitPrice": 35.00,
                "quantity": 3,
                "discount": 0.0
            }
            """;

        HttpEntity<String> entity = new HttpEntity<>(orderDetailJson);
        ResponseEntity<String> response = restTemplate.exchange(
            getApiUrl("/orders/10248/details"), HttpMethod.POST, entity, String.class);
        
        assertResponseStatus(response, 401);
    }

    @Test
    void testUpdateOrderDetail_AsAdmin_ShouldSucceed() throws Exception {
        // First create an order detail
        String createJson = """
            {
                "productId": 4,
                "unitPrice": 10.00,
                "quantity": 2,
                "discount": 0.0
            }
            """;

        HttpEntity<String> createEntity = new HttpEntity<>(createJson, createAuthHeaders(adminToken));
        ResponseEntity<String> createResponse = restTemplate.exchange(
            getApiUrl("/orders/10248/details"), HttpMethod.POST, createEntity, String.class);
        
        JsonNode createdOrderDetail = objectMapper.readTree(createResponse.getBody());
        int productId = createdOrderDetail.get("productId").asInt();

        // Update the order detail
        String updateJson = """
            {
                "productId": 4,
                "unitPrice": 20.00,
                "quantity": 4,
                "discount": 0.15
            }
            """;

        HttpEntity<String> updateEntity = new HttpEntity<>(updateJson, createAuthHeaders(adminToken));
        ResponseEntity<String> updateResponse = restTemplate.exchange(
            getApiUrl("/orders/10248/details/" + productId), HttpMethod.PUT, updateEntity, String.class);
        
        assertResponseStatus(updateResponse, 200);
        
        JsonNode updatedOrderDetail = objectMapper.readTree(updateResponse.getBody());
        assertEquals(20.00, updatedOrderDetail.get("unitPrice").asDouble(), 0.01);
        assertEquals(4, updatedOrderDetail.get("quantity").asInt());
        assertEquals(0.15, updatedOrderDetail.get("discount").asDouble(), 0.01);
    }

    @Test
    void testDeleteOrderDetail_AsAdmin_ShouldSucceed() throws Exception {
        // First create an order detail
        String createJson = """
            {
                "productId": 5,
                "unitPrice": 12.50,
                "quantity": 1,
                "discount": 0.0
            }
            """;

        HttpEntity<String> createEntity = new HttpEntity<>(createJson, createAuthHeaders(adminToken));
        ResponseEntity<String> createResponse = restTemplate.exchange(
            getApiUrl("/orders/10248/details"), HttpMethod.POST, createEntity, String.class);
        
        JsonNode createdOrderDetail = objectMapper.readTree(createResponse.getBody());
        int productId = createdOrderDetail.get("productId").asInt();

        // Delete the order detail
        HttpEntity<String> deleteEntity = new HttpEntity<>(createAuthHeaders(adminToken));
        ResponseEntity<String> deleteResponse = restTemplate.exchange(
            getApiUrl("/orders/10248/details/" + productId), HttpMethod.DELETE, deleteEntity, String.class);
        
        assertResponseStatus(deleteResponse, 204);

        // Verify order detail is deleted
        HttpEntity<String> getEntity = new HttpEntity<>(createAuthHeaders(staffToken));
        ResponseEntity<String> getResponse = restTemplate.exchange(
            getApiUrl("/orders/10248/details/" + productId), HttpMethod.GET, getEntity, String.class);
        assertResponseStatus(getResponse, 404);
    }

    @Test
    void testGetOrderDetailsWithPagination_ShouldReturnPaginatedResults() throws Exception {
        HttpEntity<String> entity = new HttpEntity<>(createAuthHeaders(staffToken));
        ResponseEntity<String> response = restTemplate.exchange(
            getApiUrl("/orders/10248/details?page=0&size=3"), HttpMethod.GET, entity, String.class);
        
        assertResponseStatus(response, 200);
        
        JsonNode jsonNode = objectMapper.readTree(response.getBody());
        assertTrue(jsonNode.has("content"));
        assertTrue(jsonNode.has("totalElements"));
        assertTrue(jsonNode.has("totalPages"));
        assertTrue(jsonNode.has("size"));
        assertTrue(jsonNode.has("number"));
        
        assertEquals(3, jsonNode.get("size").asInt());
        assertEquals(0, jsonNode.get("number").asInt());
        assertTrue(jsonNode.get("content").size() <= 3);
    }

    @Test
    void testGetOrderDetailsWithSorting_ShouldReturnSortedResults() throws Exception {
        HttpEntity<String> entity = new HttpEntity<>(createAuthHeaders(staffToken));
        ResponseEntity<String> response = restTemplate.exchange(
            getApiUrl("/orders/10248/details?sort=unitPrice,desc"), HttpMethod.GET, entity, String.class);
        
        assertResponseStatus(response, 200);
        
        JsonNode jsonNode = objectMapper.readTree(response.getBody());
        assertTrue(jsonNode.has("content"));
        assertTrue(jsonNode.get("content").isArray());
        
        // Verify sorting (should be in descending order by unitPrice)
        JsonNode content = jsonNode.get("content");
        if (content.size() > 1) {
            double firstPrice = content.get(0).get("unitPrice").asDouble();
            double secondPrice = content.get(1).get("unitPrice").asDouble();
            assertTrue(firstPrice >= secondPrice, 
                "Order details should be sorted in descending order by unit price");
        }
    }

    @Test
    void testCreateOrderDetailWithInvalidData_ShouldReturn400() {
        String invalidOrderDetailJson = """
            {
                "productId": 0,
                "unitPrice": -5.00,
                "quantity": -1,
                "discount": 1.5
            }
            """;

        HttpEntity<String> entity = new HttpEntity<>(invalidOrderDetailJson, createAuthHeaders(adminToken));
        ResponseEntity<String> response = restTemplate.exchange(
            getApiUrl("/orders/10248/details"), HttpMethod.POST, entity, String.class);
        
        assertResponseStatus(response, 400);
    }

    @Test
    void testCreateOrderDetailWithNonExistentProduct_ShouldReturn400() {
        String orderDetailJson = """
            {
                "productId": 99999,
                "unitPrice": 10.00,
                "quantity": 1,
                "discount": 0.0
            }
            """;

        HttpEntity<String> entity = new HttpEntity<>(orderDetailJson, createAuthHeaders(adminToken));
        ResponseEntity<String> response = restTemplate.exchange(
            getApiUrl("/orders/10248/details"), HttpMethod.POST, entity, String.class);
        
        assertResponseStatus(response, 400);
    }

    @Test
    void testCreateOrderDetailWithNonExistentOrder_ShouldReturn404() {
        String orderDetailJson = """
            {
                "productId": 1,
                "unitPrice": 10.00,
                "quantity": 1,
                "discount": 0.0
            }
            """;

        HttpEntity<String> entity = new HttpEntity<>(orderDetailJson, createAuthHeaders(adminToken));
        ResponseEntity<String> response = restTemplate.exchange(
            getApiUrl("/orders/99999/details"), HttpMethod.POST, entity, String.class);
        
        assertResponseStatus(response, 404);
    }

    @Test
    void testGetOrderDetailsForNonExistentOrder_ShouldReturn404() {
        HttpEntity<String> entity = new HttpEntity<>(createAuthHeaders(staffToken));
        ResponseEntity<String> response = restTemplate.exchange(
            getApiUrl("/orders/99999/details"), HttpMethod.GET, entity, String.class);
        
        assertResponseStatus(response, 404);
    }

    @Test
    void testCreateDuplicateOrderDetail_ShouldReturn400() throws Exception {
        // First create an order detail
        String orderDetailJson = """
            {
                "productId": 6,
                "unitPrice": 15.00,
                "quantity": 2,
                "discount": 0.0
            }
            """;

        HttpEntity<String> entity = new HttpEntity<>(orderDetailJson, createAuthHeaders(adminToken));
        ResponseEntity<String> firstResponse = restTemplate.exchange(
            getApiUrl("/orders/10248/details"), HttpMethod.POST, entity, String.class);
        
        assertResponseStatus(firstResponse, 201);

        // Try to create the same order detail again (should fail due to composite key constraint)
        ResponseEntity<String> secondResponse = restTemplate.exchange(
            getApiUrl("/orders/10248/details"), HttpMethod.POST, entity, String.class);
        
        assertResponseStatus(secondResponse, 400);
    }
}

