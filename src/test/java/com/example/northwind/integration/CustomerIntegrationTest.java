package com.example.northwind.integration;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

public class CustomerIntegrationTest extends BaseIntegrationTest {

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
    void testGetAllCustomers_AsStaff_ShouldReturnCustomers() throws Exception {
        HttpEntity<String> entity = new HttpEntity<>(createAuthHeaders(staffToken));
        ResponseEntity<String> response = restTemplate.exchange(
            getApiUrl("/customers"), HttpMethod.GET, entity, String.class);
        
        assertResponseStatus(response, 200);
        
        JsonNode jsonNode = objectMapper.readTree(response.getBody());
        assertTrue(jsonNode.has("content"));
        assertTrue(jsonNode.get("content").isArray());
        assertTrue(jsonNode.get("content").size() > 0);
    }

    @Test
    void testGetAllCustomers_AsAdmin_ShouldReturnCustomers() throws Exception {
        HttpEntity<String> entity = new HttpEntity<>(createAuthHeaders(adminToken));
        ResponseEntity<String> response = restTemplate.exchange(
            getApiUrl("/customers"), HttpMethod.GET, entity, String.class);
        
        assertResponseStatus(response, 200);
        
        JsonNode jsonNode = objectMapper.readTree(response.getBody());
        assertTrue(jsonNode.has("content"));
        assertTrue(jsonNode.get("content").isArray());
        assertTrue(jsonNode.get("content").size() > 0);
    }

    @Test
    void testGetAllCustomers_WithoutAuth_ShouldReturn401() {
        ResponseEntity<String> response = restTemplate.getForEntity(
            getApiUrl("/customers"), String.class);
        
        assertResponseStatus(response, 401);
    }

    @Test
    void testGetCustomerById_AsStaff_ShouldReturnCustomer() throws Exception {
        HttpEntity<String> entity = new HttpEntity<>(createAuthHeaders(staffToken));
        ResponseEntity<String> response = restTemplate.exchange(
            getApiUrl("/customers/ALFKI"), HttpMethod.GET, entity, String.class);
        
        assertResponseStatus(response, 200);
        
        JsonNode jsonNode = objectMapper.readTree(response.getBody());
        assertEquals("ALFKI", jsonNode.get("id").asText());
        assertTrue(jsonNode.has("companyName"));
        assertTrue(jsonNode.has("contactName"));
    }

    @Test
    void testGetCustomerById_NonExistent_ShouldReturn404() {
        HttpEntity<String> entity = new HttpEntity<>(createAuthHeaders(staffToken));
        ResponseEntity<String> response = restTemplate.exchange(
            getApiUrl("/customers/INVALID"), HttpMethod.GET, entity, String.class);
        
        assertResponseStatus(response, 404);
    }

    @Test
    void testCreateCustomer_AsAdmin_ShouldSucceed() throws Exception {
        String customerJson = """
            {
                "id": "TEST1",
                "companyName": "Test Company",
                "contactName": "John Doe",
                "contactTitle": "Manager",
                "address": "123 Test Street",
                "city": "Test City",
                "region": "Test Region",
                "postalCode": "12345",
                "country": "Test Country",
                "phone": "555-0123",
                "fax": "555-0124"
            }
            """;

        HttpEntity<String> entity = new HttpEntity<>(customerJson, createAuthHeaders(adminToken));
        ResponseEntity<String> response = restTemplate.exchange(
            getApiUrl("/customers"), HttpMethod.POST, entity, String.class);
        
        assertResponseStatus(response, 201);
        
        JsonNode jsonNode = objectMapper.readTree(response.getBody());
        assertEquals("TEST1", jsonNode.get("id").asText());
        assertEquals("Test Company", jsonNode.get("companyName").asText());
        assertEquals("John Doe", jsonNode.get("contactName").asText());
        assertEquals("Test City", jsonNode.get("city").asText());
    }

    @Test
    void testCreateCustomer_AsStaff_ShouldSucceed() throws Exception {
        String customerJson = """
            {
                "id": "TEST2",
                "companyName": "Staff Company",
                "contactName": "Jane Smith",
                "contactTitle": "Director",
                "address": "456 Staff Avenue",
                "city": "Staff City",
                "region": "Staff Region",
                "postalCode": "54321",
                "country": "Staff Country",
                "phone": "555-0567",
                "fax": "555-0568"
            }
            """;

        HttpEntity<String> entity = new HttpEntity<>(customerJson, createAuthHeaders(staffToken));
        ResponseEntity<String> response = restTemplate.exchange(
            getApiUrl("/customers"), HttpMethod.POST, entity, String.class);
        
        assertResponseStatus(response, 201);
        
        JsonNode jsonNode = objectMapper.readTree(response.getBody());
        assertEquals("TEST2", jsonNode.get("id").asText());
        assertEquals("Staff Company", jsonNode.get("companyName").asText());
        assertEquals("Jane Smith", jsonNode.get("contactName").asText());
    }

    @Test
    void testCreateCustomer_WithoutAuth_ShouldReturn401() {
        String customerJson = """
            {
                "id": "TEST3",
                "companyName": "Unauthorized Company",
                "contactName": "Unauthorized User",
                "contactTitle": "Manager",
                "address": "789 Unauthorized St",
                "city": "Unauthorized City",
                "region": "Unauthorized Region",
                "postalCode": "99999",
                "country": "Unauthorized Country",
                "phone": "555-0999",
                "fax": "555-0998"
            }
            """;

        HttpEntity<String> entity = new HttpEntity<>(customerJson);
        ResponseEntity<String> response = restTemplate.exchange(
            getApiUrl("/customers"), HttpMethod.POST, entity, String.class);
        
        assertResponseStatus(response, 401);
    }

    @Test
    void testUpdateCustomer_AsAdmin_ShouldSucceed() throws Exception {
        // First create a customer
        String createJson = """
            {
                "id": "TEST4",
                "companyName": "Original Company",
                "contactName": "Original Contact",
                "contactTitle": "Original Title",
                "address": "Original Address",
                "city": "Original City",
                "region": "Original Region",
                "postalCode": "00000",
                "country": "Original Country",
                "phone": "555-0000",
                "fax": "555-0001"
            }
            """;

        HttpEntity<String> createEntity = new HttpEntity<>(createJson, createAuthHeaders(adminToken));
        ResponseEntity<String> createResponse = restTemplate.exchange(
            getApiUrl("/customers"), HttpMethod.POST, createEntity, String.class);
        
        JsonNode createdCustomer = objectMapper.readTree(createResponse.getBody());
        String customerId = createdCustomer.get("id").asText();

        // Update the customer
        String updateJson = """
            {
                "id": "TEST4",
                "companyName": "Updated Company",
                "contactName": "Updated Contact",
                "contactTitle": "Updated Title",
                "address": "Updated Address",
                "city": "Updated City",
                "region": "Updated Region",
                "postalCode": "11111",
                "country": "Updated Country",
                "phone": "555-1111",
                "fax": "555-1112"
            }
            """;

        HttpEntity<String> updateEntity = new HttpEntity<>(updateJson, createAuthHeaders(adminToken));
        ResponseEntity<String> updateResponse = restTemplate.exchange(
            getApiUrl("/customers/" + customerId), HttpMethod.PUT, updateEntity, String.class);
        
        assertResponseStatus(updateResponse, 200);
        
        JsonNode updatedCustomer = objectMapper.readTree(updateResponse.getBody());
        assertEquals("Updated Company", updatedCustomer.get("companyName").asText());
        assertEquals("Updated Contact", updatedCustomer.get("contactName").asText());
        assertEquals("Updated City", updatedCustomer.get("city").asText());
    }

    @Test
    void testDeleteCustomer_AsAdmin_ShouldSucceed() throws Exception {
        // First create a customer
        String createJson = """
            {
                "id": "TEST5",
                "companyName": "To Delete Company",
                "contactName": "Delete Contact",
                "contactTitle": "Delete Title",
                "address": "Delete Address",
                "city": "Delete City",
                "region": "Delete Region",
                "postalCode": "22222",
                "country": "Delete Country",
                "phone": "555-2222",
                "fax": "555-2223"
            }
            """;

        HttpEntity<String> createEntity = new HttpEntity<>(createJson, createAuthHeaders(adminToken));
        ResponseEntity<String> createResponse = restTemplate.exchange(
            getApiUrl("/customers"), HttpMethod.POST, createEntity, String.class);
        
        JsonNode createdCustomer = objectMapper.readTree(createResponse.getBody());
        String customerId = createdCustomer.get("id").asText();

        // Delete the customer
        HttpEntity<String> deleteEntity = new HttpEntity<>(createAuthHeaders(adminToken));
        ResponseEntity<String> deleteResponse = restTemplate.exchange(
            getApiUrl("/customers/" + customerId), HttpMethod.DELETE, deleteEntity, String.class);
        
        assertResponseStatus(deleteResponse, 204);

        // Verify customer is deleted
        HttpEntity<String> getEntity = new HttpEntity<>(createAuthHeaders(staffToken));
        ResponseEntity<String> getResponse = restTemplate.exchange(
            getApiUrl("/customers/" + customerId), HttpMethod.GET, getEntity, String.class);
        assertResponseStatus(getResponse, 404);
    }

    @Test
    void testGetCustomersWithPagination_ShouldReturnPaginatedResults() throws Exception {
        HttpEntity<String> entity = new HttpEntity<>(createAuthHeaders(staffToken));
        ResponseEntity<String> response = restTemplate.exchange(
            getApiUrl("/customers?page=0&size=5"), HttpMethod.GET, entity, String.class);
        
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
    void testGetCustomersWithSorting_ShouldReturnSortedResults() throws Exception {
        HttpEntity<String> entity = new HttpEntity<>(createAuthHeaders(staffToken));
        ResponseEntity<String> response = restTemplate.exchange(
            getApiUrl("/customers?sort=companyName,asc"), HttpMethod.GET, entity, String.class);
        
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
                "Customers should be sorted in ascending order by company name");
        }
    }

    @Test
    void testCreateCustomerWithInvalidData_ShouldReturn400() {
        String invalidCustomerJson = """
            {
                "id": "",
                "companyName": "Valid Company",
                "contactName": "Valid Contact",
                "contactTitle": "Valid Title",
                "address": "Valid Address",
                "city": "Valid City",
                "region": "Valid Region",
                "postalCode": "33333",
                "country": "Valid Country",
                "phone": "555-3333",
                "fax": "555-3334"
            }
            """;

        HttpEntity<String> entity = new HttpEntity<>(invalidCustomerJson, createAuthHeaders(adminToken));
        ResponseEntity<String> response = restTemplate.exchange(
            getApiUrl("/customers"), HttpMethod.POST, entity, String.class);
        
        assertResponseStatus(response, 400);
    }

    @Test
    void testSearchCustomers_ShouldReturnFilteredResults() throws Exception {
        HttpEntity<String> entity = new HttpEntity<>(createAuthHeaders(staffToken));
        ResponseEntity<String> response = restTemplate.exchange(
            getApiUrl("/customers?q=alfki"), HttpMethod.GET, entity, String.class);
        
        assertResponseStatus(response, 200);
        
        JsonNode jsonNode = objectMapper.readTree(response.getBody());
        assertTrue(jsonNode.has("content"));
        assertTrue(jsonNode.get("content").isArray());
        
        // Should find customers containing "alfki"
        boolean foundAlfki = false;
        for (JsonNode customer : jsonNode.get("content")) {
            String companyName = customer.get("companyName").asText().toLowerCase();
            String contactName = customer.get("contactName").asText().toLowerCase();
            if (companyName.contains("alfki") || contactName.contains("alfki")) {
                foundAlfki = true;
                break;
            }
        }
        assertTrue(foundAlfki, "Should find customers containing 'alfki'");
    }
}

