package com.example.northwind.integration;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

public class OrderIntegrationTest extends BaseIntegrationTest {

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
    void testGetAllOrders_AsStaff_ShouldReturnOrders() throws Exception {
        HttpEntity<String> entity = new HttpEntity<>(createAuthHeaders(staffToken));
        ResponseEntity<String> response = restTemplate.exchange(
            getApiUrl("/orders"), HttpMethod.GET, entity, String.class);
        
        assertResponseStatus(response, 200);
        
        JsonNode jsonNode = objectMapper.readTree(response.getBody());
        assertTrue(jsonNode.has("content"));
        assertTrue(jsonNode.get("content").isArray());
        assertTrue(jsonNode.get("content").size() > 0);
    }

    @Test
    void testGetAllOrders_AsAdmin_ShouldReturnOrders() throws Exception {
        HttpEntity<String> entity = new HttpEntity<>(createAuthHeaders(adminToken));
        ResponseEntity<String> response = restTemplate.exchange(
            getApiUrl("/orders"), HttpMethod.GET, entity, String.class);
        
        assertResponseStatus(response, 200);
        
        JsonNode jsonNode = objectMapper.readTree(response.getBody());
        assertTrue(jsonNode.has("content"));
        assertTrue(jsonNode.get("content").isArray());
        assertTrue(jsonNode.get("content").size() > 0);
    }

    @Test
    void testGetAllOrders_WithoutAuth_ShouldReturn401() {
        ResponseEntity<String> response = restTemplate.getForEntity(
            getApiUrl("/orders"), String.class);
        
        assertResponseStatus(response, 401);
    }

    @Test
    void testGetOrderById_AsStaff_ShouldReturnOrder() throws Exception {
        HttpEntity<String> entity = new HttpEntity<>(createAuthHeaders(staffToken));
        ResponseEntity<String> response = restTemplate.exchange(
            getApiUrl("/orders/10248"), HttpMethod.GET, entity, String.class);
        
        assertResponseStatus(response, 200);
        
        JsonNode jsonNode = objectMapper.readTree(response.getBody());
        assertEquals(10248, jsonNode.get("id").asInt());
        assertTrue(jsonNode.has("customerId"));
        assertTrue(jsonNode.has("employeeId"));
        assertTrue(jsonNode.has("orderDate"));
    }

    @Test
    void testGetOrderById_NonExistent_ShouldReturn404() {
        HttpEntity<String> entity = new HttpEntity<>(createAuthHeaders(staffToken));
        ResponseEntity<String> response = restTemplate.exchange(
            getApiUrl("/orders/99999"), HttpMethod.GET, entity, String.class);
        
        assertResponseStatus(response, 404);
    }

    @Test
    void testCreateOrder_AsAdmin_ShouldSucceed() throws Exception {
        String orderJson = """
            {
                "customerId": "ALFKI",
                "employeeId": 1,
                "orderDate": "2024-01-01",
                "requiredDate": "2024-01-15",
                "shippedDate": null,
                "shipVia": 1,
                "freight": 10.50,
                "shipName": "Test Ship Name",
                "shipAddress": "123 Test Street",
                "shipCity": "Test City",
                "shipRegion": "Test Region",
                "shipPostalCode": "12345",
                "shipCountry": "Test Country"
            }
            """;

        HttpEntity<String> entity = new HttpEntity<>(orderJson, createAuthHeaders(adminToken));
        ResponseEntity<String> response = restTemplate.exchange(
            getApiUrl("/orders"), HttpMethod.POST, entity, String.class);
        
        assertResponseStatus(response, 201);
        
        JsonNode jsonNode = objectMapper.readTree(response.getBody());
        assertTrue(jsonNode.has("id"));
        assertEquals("ALFKI", jsonNode.get("customerId").asText());
        assertEquals(1, jsonNode.get("employeeId").asInt());
        assertEquals(10.50, jsonNode.get("freight").asDouble(), 0.01);
        assertEquals("Test City", jsonNode.get("shipCity").asText());
    }

    @Test
    void testCreateOrder_AsStaff_ShouldSucceed() throws Exception {
        String orderJson = """
            {
                "customerId": "ALFKI",
                "employeeId": 1,
                "orderDate": "2024-01-02",
                "requiredDate": "2024-01-16",
                "shippedDate": null,
                "shipVia": 1,
                "freight": 15.75,
                "shipName": "Staff Ship Name",
                "shipAddress": "456 Staff Avenue",
                "shipCity": "Staff City",
                "shipRegion": "Staff Region",
                "shipPostalCode": "54321",
                "shipCountry": "Staff Country"
            }
            """;

        HttpEntity<String> entity = new HttpEntity<>(orderJson, createAuthHeaders(staffToken));
        ResponseEntity<String> response = restTemplate.exchange(
            getApiUrl("/orders"), HttpMethod.POST, entity, String.class);
        
        assertResponseStatus(response, 201);
        
        JsonNode jsonNode = objectMapper.readTree(response.getBody());
        assertEquals("ALFKI", jsonNode.get("customerId").asText());
        assertEquals(1, jsonNode.get("employeeId").asInt());
        assertEquals(15.75, jsonNode.get("freight").asDouble(), 0.01);
        assertEquals("Staff City", jsonNode.get("shipCity").asText());
    }

    @Test
    void testCreateOrder_WithoutAuth_ShouldReturn401() {
        String orderJson = """
            {
                "customerId": "ALFKI",
                "employeeId": 1,
                "orderDate": "2024-01-03",
                "requiredDate": "2024-01-17",
                "shippedDate": null,
                "shipVia": 1,
                "freight": 20.00,
                "shipName": "Unauthorized Ship Name",
                "shipAddress": "789 Unauthorized St",
                "shipCity": "Unauthorized City",
                "shipRegion": "Unauthorized Region",
                "shipPostalCode": "99999",
                "shipCountry": "Unauthorized Country"
            }
            """;

        HttpEntity<String> entity = new HttpEntity<>(orderJson);
        ResponseEntity<String> response = restTemplate.exchange(
            getApiUrl("/orders"), HttpMethod.POST, entity, String.class);
        
        assertResponseStatus(response, 401);
    }

    @Test
    void testUpdateOrder_AsAdmin_ShouldSucceed() throws Exception {
        // First create an order
        String createJson = """
            {
                "customerId": "ALFKI",
                "employeeId": 1,
                "orderDate": "2024-01-01",
                "requiredDate": "2024-01-15",
                "shippedDate": null,
                "shipVia": 1,
                "freight": 10.50,
                "shipName": "Original Ship Name",
                "shipAddress": "Original Address",
                "shipCity": "Original City",
                "shipRegion": "Original Region",
                "shipPostalCode": "00000",
                "shipCountry": "Original Country"
            }
            """;

        HttpEntity<String> createEntity = new HttpEntity<>(createJson, createAuthHeaders(adminToken));
        ResponseEntity<String> createResponse = restTemplate.exchange(
            getApiUrl("/orders"), HttpMethod.POST, createEntity, String.class);
        
        JsonNode createdOrder = objectMapper.readTree(createResponse.getBody());
        int orderId = createdOrder.get("id").asInt();

        // Update the order
        String updateJson = """
            {
                "customerId": "ALFKI",
                "employeeId": 1,
                "orderDate": "2024-01-01",
                "requiredDate": "2024-01-15",
                "shippedDate": "2024-01-10",
                "shipVia": 2,
                "freight": 25.75,
                "shipName": "Updated Ship Name",
                "shipAddress": "Updated Address",
                "shipCity": "Updated City",
                "shipRegion": "Updated Region",
                "shipPostalCode": "11111",
                "shipCountry": "Updated Country"
            }
            """;

        HttpEntity<String> updateEntity = new HttpEntity<>(updateJson, createAuthHeaders(adminToken));
        ResponseEntity<String> updateResponse = restTemplate.exchange(
            getApiUrl("/orders/" + orderId), HttpMethod.PUT, updateEntity, String.class);
        
        assertResponseStatus(updateResponse, 200);
        
        JsonNode updatedOrder = objectMapper.readTree(updateResponse.getBody());
        assertEquals(25.75, updatedOrder.get("freight").asDouble(), 0.01);
        assertEquals("Updated Ship Name", updatedOrder.get("shipName").asText());
        assertEquals("Updated City", updatedOrder.get("shipCity").asText());
        assertNotNull(updatedOrder.get("shippedDate"));
    }

    @Test
    void testDeleteOrder_AsAdmin_ShouldSucceed() throws Exception {
        // First create an order
        String createJson = """
            {
                "customerId": "ALFKI",
                "employeeId": 1,
                "orderDate": "2024-01-01",
                "requiredDate": "2024-01-15",
                "shippedDate": null,
                "shipVia": 1,
                "freight": 10.50,
                "shipName": "To Delete Ship Name",
                "shipAddress": "Delete Address",
                "shipCity": "Delete City",
                "shipRegion": "Delete Region",
                "shipPostalCode": "22222",
                "shipCountry": "Delete Country"
            }
            """;

        HttpEntity<String> createEntity = new HttpEntity<>(createJson, createAuthHeaders(adminToken));
        ResponseEntity<String> createResponse = restTemplate.exchange(
            getApiUrl("/orders"), HttpMethod.POST, createEntity, String.class);
        
        JsonNode createdOrder = objectMapper.readTree(createResponse.getBody());
        int orderId = createdOrder.get("id").asInt();

        // Delete the order
        HttpEntity<String> deleteEntity = new HttpEntity<>(createAuthHeaders(adminToken));
        ResponseEntity<String> deleteResponse = restTemplate.exchange(
            getApiUrl("/orders/" + orderId), HttpMethod.DELETE, deleteEntity, String.class);
        
        assertResponseStatus(deleteResponse, 204);

        // Verify order is deleted
        HttpEntity<String> getEntity = new HttpEntity<>(createAuthHeaders(staffToken));
        ResponseEntity<String> getResponse = restTemplate.exchange(
            getApiUrl("/orders/" + orderId), HttpMethod.GET, getEntity, String.class);
        assertResponseStatus(getResponse, 404);
    }

    @Test
    void testGetOrdersWithPagination_ShouldReturnPaginatedResults() throws Exception {
        HttpEntity<String> entity = new HttpEntity<>(createAuthHeaders(staffToken));
        ResponseEntity<String> response = restTemplate.exchange(
            getApiUrl("/orders?page=0&size=5"), HttpMethod.GET, entity, String.class);
        
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

    @Test
    void testGetOrdersWithSorting_ShouldReturnSortedResults() throws Exception {
        HttpEntity<String> entity = new HttpEntity<>(createAuthHeaders(staffToken));
        ResponseEntity<String> response = restTemplate.exchange(
            getApiUrl("/orders?sort=orderDate,desc"), HttpMethod.GET, entity, String.class);
        
        assertResponseStatus(response, 200);
        
        JsonNode jsonNode = objectMapper.readTree(response.getBody());
        assertTrue(jsonNode.has("content"));
        assertTrue(jsonNode.get("content").isArray());
        
        // Verify sorting (should be in descending order by orderDate)
        JsonNode content = jsonNode.get("content");
        if (content.size() > 1) {
            String firstDate = content.get(0).get("orderDate").asText();
            String secondDate = content.get(1).get("orderDate").asText();
            assertTrue(firstDate.compareTo(secondDate) >= 0, 
                "Orders should be sorted in descending order by order date");
        }
    }

    @Test
    void testGetOrdersByCustomer_ShouldReturnFilteredResults() throws Exception {
        HttpEntity<String> entity = new HttpEntity<>(createAuthHeaders(staffToken));
        ResponseEntity<String> response = restTemplate.exchange(
            getApiUrl("/orders?customerId=ALFKI"), HttpMethod.GET, entity, String.class);
        
        assertResponseStatus(response, 200);
        
        JsonNode jsonNode = objectMapper.readTree(response.getBody());
        assertTrue(jsonNode.has("content"));
        assertTrue(jsonNode.get("content").isArray());
        
        // All orders should belong to customer ALFKI
        for (JsonNode order : jsonNode.get("content")) {
            assertEquals("ALFKI", order.get("customerId").asText());
        }
    }

    @Test
    void testGetOrdersByEmployee_ShouldReturnFilteredResults() throws Exception {
        HttpEntity<String> entity = new HttpEntity<>(createAuthHeaders(staffToken));
        ResponseEntity<String> response = restTemplate.exchange(
            getApiUrl("/orders?employeeId=1"), HttpMethod.GET, entity, String.class);
        
        assertResponseStatus(response, 200);
        
        JsonNode jsonNode = objectMapper.readTree(response.getBody());
        assertTrue(jsonNode.has("content"));
        assertTrue(jsonNode.get("content").isArray());
        
        // All orders should belong to employee 1
        for (JsonNode order : jsonNode.get("content")) {
            assertEquals(1, order.get("employeeId").asInt());
        }
    }

    @Test
    void testGetOrderSummary_ShouldReturnOrderWithDetails() throws Exception {
        HttpEntity<String> entity = new HttpEntity<>(createAuthHeaders(staffToken));
        ResponseEntity<String> response = restTemplate.exchange(
            getApiUrl("/orders/10248/summary"), HttpMethod.GET, entity, String.class);
        
        assertResponseStatus(response, 200);
        
        JsonNode jsonNode = objectMapper.readTree(response.getBody());
        assertEquals(10248, jsonNode.get("id").asInt());
        assertTrue(jsonNode.has("orderDetails"));
        assertTrue(jsonNode.get("orderDetails").isArray());
        assertTrue(jsonNode.has("totalAmount"));
        assertTrue(jsonNode.get("totalAmount").isNumber());
    }

    @Test
    void testGetOrderSummaryTotal_ShouldReturnTotalAmount() throws Exception {
        HttpEntity<String> entity = new HttpEntity<>(createAuthHeaders(staffToken));
        ResponseEntity<String> response = restTemplate.exchange(
            getApiUrl("/orders/10248/summary/total"), HttpMethod.GET, entity, String.class);
        
        assertResponseStatus(response, 200);
        
        JsonNode jsonNode = objectMapper.readTree(response.getBody());
        assertTrue(jsonNode.has("totalAmount"));
        assertTrue(jsonNode.get("totalAmount").isNumber());
        assertTrue(jsonNode.get("totalAmount").asDouble() > 0);
    }

    @Test
    void testCreateOrderWithInvalidData_ShouldReturn400() {
        String invalidOrderJson = """
            {
                "customerId": "",
                "employeeId": 1,
                "orderDate": "2024-01-01",
                "requiredDate": "2024-01-15",
                "shippedDate": null,
                "shipVia": 1,
                "freight": 10.50,
                "shipName": "Valid Ship Name",
                "shipAddress": "Valid Address",
                "shipCity": "Valid City",
                "shipRegion": "Valid Region",
                "shipPostalCode": "33333",
                "shipCountry": "Valid Country"
            }
            """;

        HttpEntity<String> entity = new HttpEntity<>(invalidOrderJson, createAuthHeaders(adminToken));
        ResponseEntity<String> response = restTemplate.exchange(
            getApiUrl("/orders"), HttpMethod.POST, entity, String.class);
        
        assertResponseStatus(response, 400);
    }
}

