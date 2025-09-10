package com.example.northwind.integration;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

public class SupplierIntegrationTest extends BaseIntegrationTest {

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
    void testGetAllSuppliers_ShouldReturnSuppliers() throws Exception {
        ResponseEntity<String> response = restTemplate.getForEntity(
            getApiUrl("/suppliers"), String.class);
        
        assertResponseStatus(response, 200);
        
        JsonNode jsonNode = objectMapper.readTree(response.getBody());
        assertTrue(jsonNode.has("content"));
        assertTrue(jsonNode.get("content").isArray());
        assertTrue(jsonNode.get("content").size() > 0);
    }

    @Test
    void testGetSupplierById_ShouldReturnSupplier() throws Exception {
        ResponseEntity<String> response = restTemplate.getForEntity(
            getApiUrl("/suppliers/1"), String.class);
        
        assertResponseStatus(response, 200);
        
        JsonNode jsonNode = objectMapper.readTree(response.getBody());
        assertEquals(1, jsonNode.get("id").asInt());
        assertTrue(jsonNode.has("companyName"));
        assertTrue(jsonNode.has("contactName"));
    }

    @Test
    void testGetSupplierById_NonExistent_ShouldReturn404() {
        ResponseEntity<String> response = restTemplate.getForEntity(
            getApiUrl("/suppliers/99999"), String.class);
        
        assertResponseStatus(response, 404);
    }

    @Test
    void testCreateSupplier_AsAdmin_ShouldSucceed() throws Exception {
        String supplierJson = """
            {
                "companyName": "Test Supplier Co.",
                "contactName": "John Doe",
                "contactTitle": "Sales Manager",
                "address": "123 Test Street",
                "city": "Test City",
                "region": "Test Region",
                "postalCode": "12345",
                "country": "Test Country",
                "phone": "555-0123",
                "fax": "555-0124",
                "homePage": "http://www.testsupplier.com"
            }
            """;

        HttpEntity<String> entity = new HttpEntity<>(supplierJson, createAuthHeaders(adminToken));
        ResponseEntity<String> response = restTemplate.exchange(
            getApiUrl("/suppliers"), HttpMethod.POST, entity, String.class);
        
        assertResponseStatus(response, 201);
        
        JsonNode jsonNode = objectMapper.readTree(response.getBody());
        assertTrue(jsonNode.has("id"));
        assertEquals("Test Supplier Co.", jsonNode.get("companyName").asText());
        assertEquals("John Doe", jsonNode.get("contactName").asText());
        assertEquals("Test City", jsonNode.get("city").asText());
    }

    @Test
    void testCreateSupplier_AsStaff_ShouldSucceed() throws Exception {
        String supplierJson = """
            {
                "companyName": "Staff Supplier Co.",
                "contactName": "Jane Smith",
                "contactTitle": "Account Manager",
                "address": "456 Staff Avenue",
                "city": "Staff City",
                "region": "Staff Region",
                "postalCode": "54321",
                "country": "Staff Country",
                "phone": "555-0567",
                "fax": "555-0568",
                "homePage": "http://www.staffsupplier.com"
            }
            """;

        HttpEntity<String> entity = new HttpEntity<>(supplierJson, createAuthHeaders(staffToken));
        ResponseEntity<String> response = restTemplate.exchange(
            getApiUrl("/suppliers"), HttpMethod.POST, entity, String.class);
        
        assertResponseStatus(response, 201);
        
        JsonNode jsonNode = objectMapper.readTree(response.getBody());
        assertEquals("Staff Supplier Co.", jsonNode.get("companyName").asText());
        assertEquals("Jane Smith", jsonNode.get("contactName").asText());
    }

    @Test
    void testCreateSupplier_WithoutAuth_ShouldReturn401() {
        String supplierJson = """
            {
                "companyName": "Unauthorized Supplier",
                "contactName": "Unauthorized User",
                "contactTitle": "Manager",
                "address": "789 Unauthorized St",
                "city": "Unauthorized City",
                "region": "Unauthorized Region",
                "postalCode": "99999",
                "country": "Unauthorized Country",
                "phone": "555-0999",
                "fax": "555-0998",
                "homePage": "http://www.unauthorized.com"
            }
            """;

        HttpEntity<String> entity = new HttpEntity<>(supplierJson);
        ResponseEntity<String> response = restTemplate.exchange(
            getApiUrl("/suppliers"), HttpMethod.POST, entity, String.class);
        
        assertResponseStatus(response, 401);
    }

    @Test
    void testUpdateSupplier_AsAdmin_ShouldSucceed() throws Exception {
        // First create a supplier
        String createJson = """
            {
                "companyName": "Original Supplier",
                "contactName": "Original Contact",
                "contactTitle": "Original Title",
                "address": "Original Address",
                "city": "Original City",
                "region": "Original Region",
                "postalCode": "00000",
                "country": "Original Country",
                "phone": "555-0000",
                "fax": "555-0001",
                "homePage": "http://www.original.com"
            }
            """;

        HttpEntity<String> createEntity = new HttpEntity<>(createJson, createAuthHeaders(adminToken));
        ResponseEntity<String> createResponse = restTemplate.exchange(
            getApiUrl("/suppliers"), HttpMethod.POST, createEntity, String.class);
        
        JsonNode createdSupplier = objectMapper.readTree(createResponse.getBody());
        int supplierId = createdSupplier.get("id").asInt();

        // Update the supplier
        String updateJson = """
            {
                "companyName": "Updated Supplier",
                "contactName": "Updated Contact",
                "contactTitle": "Updated Title",
                "address": "Updated Address",
                "city": "Updated City",
                "region": "Updated Region",
                "postalCode": "11111",
                "country": "Updated Country",
                "phone": "555-1111",
                "fax": "555-1112",
                "homePage": "http://www.updated.com"
            }
            """;

        HttpEntity<String> updateEntity = new HttpEntity<>(updateJson, createAuthHeaders(adminToken));
        ResponseEntity<String> updateResponse = restTemplate.exchange(
            getApiUrl("/suppliers/" + supplierId), HttpMethod.PUT, updateEntity, String.class);
        
        assertResponseStatus(updateResponse, 200);
        
        JsonNode updatedSupplier = objectMapper.readTree(updateResponse.getBody());
        assertEquals("Updated Supplier", updatedSupplier.get("companyName").asText());
        assertEquals("Updated Contact", updatedSupplier.get("contactName").asText());
        assertEquals("Updated City", updatedSupplier.get("city").asText());
    }

    @Test
    void testDeleteSupplier_AsAdmin_ShouldSucceed() throws Exception {
        // First create a supplier
        String createJson = """
            {
                "companyName": "To Delete Supplier",
                "contactName": "Delete Contact",
                "contactTitle": "Delete Title",
                "address": "Delete Address",
                "city": "Delete City",
                "region": "Delete Region",
                "postalCode": "22222",
                "country": "Delete Country",
                "phone": "555-2222",
                "fax": "555-2223",
                "homePage": "http://www.delete.com"
            }
            """;

        HttpEntity<String> createEntity = new HttpEntity<>(createJson, createAuthHeaders(adminToken));
        ResponseEntity<String> createResponse = restTemplate.exchange(
            getApiUrl("/suppliers"), HttpMethod.POST, createEntity, String.class);
        
        JsonNode createdSupplier = objectMapper.readTree(createResponse.getBody());
        int supplierId = createdSupplier.get("id").asInt();

        // Delete the supplier
        HttpEntity<String> deleteEntity = new HttpEntity<>(createAuthHeaders(adminToken));
        ResponseEntity<String> deleteResponse = restTemplate.exchange(
            getApiUrl("/suppliers/" + supplierId), HttpMethod.DELETE, deleteEntity, String.class);
        
        assertResponseStatus(deleteResponse, 204);

        // Verify supplier is deleted
        ResponseEntity<String> getResponse = restTemplate.getForEntity(
            getApiUrl("/suppliers/" + supplierId), String.class);
        assertResponseStatus(getResponse, 404);
    }

    @Test
    void testGetSuppliersWithPagination_ShouldReturnPaginatedResults() throws Exception {
        ResponseEntity<String> response = restTemplate.getForEntity(
            getApiUrl("/suppliers?page=0&size=3"), String.class);
        
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
    void testGetSuppliersWithSorting_ShouldReturnSortedResults() throws Exception {
        ResponseEntity<String> response = restTemplate.getForEntity(
            getApiUrl("/suppliers?sort=companyName,asc"), String.class);
        
        assertResponseStatus(response, 200);
        
        JsonNode jsonNode = objectMapper.readTree(response.getBody());
        assertTrue(jsonNode.has("content"));
        assertTrue(jsonNode.get("content").isArray());
        
        // Verify sorting (should be in ascending order by companyName)
        JsonNode content = jsonNode.get("content");
        if (content.size() > 1) {
            String firstName = content.get(0).get("companyName").asText();
            String secondName = content.get(1).get("companyName").asText();
            assertTrue(firstName.compareTo(secondName) <= 0, 
                "Suppliers should be sorted in ascending order by company name");
        }
    }

    @Test
    void testCreateSupplierWithInvalidData_ShouldReturn400() {
        String invalidSupplierJson = """
            {
                "companyName": "",
                "contactName": "Valid Contact",
                "contactTitle": "Valid Title",
                "address": "Valid Address",
                "city": "Valid City",
                "region": "Valid Region",
                "postalCode": "33333",
                "country": "Valid Country",
                "phone": "555-3333",
                "fax": "555-3334",
                "homePage": "http://www.valid.com"
            }
            """;

        HttpEntity<String> entity = new HttpEntity<>(invalidSupplierJson, createAuthHeaders(adminToken));
        ResponseEntity<String> response = restTemplate.exchange(
            getApiUrl("/suppliers"), HttpMethod.POST, entity, String.class);
        
        assertResponseStatus(response, 400);
    }

    @Test
    void testSearchSuppliers_ShouldReturnFilteredResults() throws Exception {
        ResponseEntity<String> response = restTemplate.getForEntity(
            getApiUrl("/suppliers?q=exotic"), String.class);
        
        assertResponseStatus(response, 200);
        
        JsonNode jsonNode = objectMapper.readTree(response.getBody());
        assertTrue(jsonNode.has("content"));
        assertTrue(jsonNode.get("content").isArray());
        
        // Should find suppliers containing "exotic"
        boolean foundExotic = false;
        for (JsonNode supplier : jsonNode.get("content")) {
            String companyName = supplier.get("companyName").asText().toLowerCase();
            if (companyName.contains("exotic")) {
                foundExotic = true;
                break;
            }
        }
        assertTrue(foundExotic, "Should find suppliers containing 'exotic'");
    }
}

